/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package org.liferayhub.pc.service.impl;

import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.liferayhub.pc.formatter.SQLRunner;
import org.liferayhub.pc.model.CommandHistory;
import org.liferayhub.pc.service.CommandHistoryLocalServiceUtil;
import org.liferayhub.pc.service.base.PowerConsoleServiceBaseImpl;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.portal.kernel.dao.db.DBFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.ReleaseInfo;
import com.liferay.portal.kernel.util.ServerDetector;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portal.service.CompanyLocalServiceUtil;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.service.permission.PortalPermissionUtil;
import com.liferay.portal.service.permission.UserPermissionUtil;
import com.liferay.portal.util.PortalUtil;

/**
 * The implementation of the power console remote service.
 * 
 * <p>
 * All custom service methods should be put in this class. Whenever methods are
 * added, rerun ServiceBuilder to copy their definitions into the
 * {@link org.liferayhub.pc.service.PowerConsoleService} interface.
 * 
 * <p>
 * This is a remote service. Methods of this service are expected to have
 * security checks based on the propagated JAAS credentials because this service
 * can be accessed remotely.
 * </p>
 * 
 * @author Sheikh Sajid
 * @see org.liferayhub.pc.service.base.PowerConsoleServiceBaseImpl
 * @see org.liferayhub.pc.service.PowerConsoleServiceUtil
 */
public class PowerConsoleServiceImpl extends PowerConsoleServiceBaseImpl {
	private static final String UNRECOGNIZED_COMMAND = "Unrecognized command";

	/*
	 * NOTE FOR DEVELOPERS:
	 * 
	 * Never reference this interface directly. Always use {@link
	 * org.liferayhub.pc.service.PowerConsoleServiceUtil} to access the power
	 * console remote service.
	 */
	public String runCommand(long userId, long companyId, String mode,
			String command) {
		String response = "";
		Date startDate = new Date();
		if ("server".equalsIgnoreCase(mode)) {
			if ("help".equalsIgnoreCase(command)) {
				response = "Commands:\n"
						+ "help\t\t\tShow this help message\n"
						+ "version\t\t Display the version of the Liferay Portal\n"
						+ "date\t\t\tDisplay the date and time of the database server\n"
						+ "uptime\t\t  Display the uptime of the portal\n"
						+ "adduser\t\t Add a new user";
			} else if ("version".equalsIgnoreCase(command)) {
				response = ReleaseInfo.getReleaseInfo()
						+ " running on "
						+ StringUtil.upperCaseFirstLetter(ServerDetector
								.getServerId());
			} else if ("date".equalsIgnoreCase(command)) {
				response = new Date().toString();
			} else if ("uptime".equalsIgnoreCase(command)) {
				response = getUptime(PortalUtil.getUptime().getTime());
			} else if (command.toLowerCase().startsWith("adduser")) {
				response = handleAddUser(userId, companyId, command);
			}
		} else if ("db".equalsIgnoreCase(mode)) {
			if ("help".equalsIgnoreCase(command)) {
				response = "Commands:\n"
						+ "help\t\t\tShow this help message\n"
						+ "version\t\t Display the version of the database\n"
						+ "date\t\t\tDisplay the date and time of the database server\n"
						+ "<sql query>\t Display result of any SQL query";
			} else if ("date".equalsIgnoreCase(command)) {
				response = runSQLQuery("select now() \"\"");
			} else if ("version".equalsIgnoreCase(command)) {
				response = runSQLQuery("select '"
						+ DBFactoryUtil.getDBFactory().getDB().getType()
						+ "' as '', version() ''");
			} else
				response = runSQLQuery(command);
		} else if ("jvm".equalsIgnoreCase(mode)) {
			if ("help".equalsIgnoreCase(command)) {
				response = "Commands:\n"
						+ "help\t\tShow this help message\n"
						+ "mem\t\t Display memory usage of the JVM\n"
						+ "osinfo\t  Display operating system info of the running JVM\n"
						+ "vminfo\t  Display VM info";
			} else if ("mem".equalsIgnoreCase(command)) {
				MemoryMXBean mem = ManagementFactory.getMemoryMXBean();
				response = "Heap        : "
						+ mem.getHeapMemoryUsage().toString();
				response += "\nNon Heap    : "
						+ mem.getNonHeapMemoryUsage().toString();
				response += "\nFinalization: "
						+ mem.getObjectPendingFinalizationCount()
						+ " objects pending";
			} else if ("osinfo".equalsIgnoreCase(command)) {
				OperatingSystemMXBean os = ManagementFactory
						.getOperatingSystemMXBean();
				response = os.getName() + "[" + os.getArch() + "] "
						+ os.getVersion() + " (" + os.getAvailableProcessors()
						+ " processors)";
				response += "\nLoad Average: " + os.getSystemLoadAverage();
			} else if ("vminfo".equalsIgnoreCase(command)) {
				RuntimeMXBean vm = ManagementFactory.getRuntimeMXBean();
				response = vm.getVmName() + " " + vm.getVmVersion() + " by "
						+ vm.getVmVendor();
				response += "\n" + vm.getSpecName() + " " + vm.getSpecVersion()
						+ " by " + vm.getSpecVendor();
				response += "\nStarted at: "
						+ DateFormat.getInstance().format(
								new Date(vm.getStartTime()));
				response += "\nUptime    : " + getUptime(vm.getStartTime());
			} else {
				response = UNRECOGNIZED_COMMAND;
			}
		}
		// save command to database if it is not 'help'
		if (!command.startsWith("help")) {
			try {
				// add to history
				Date endDate = new Date();
				long id = CounterLocalServiceUtil
						.increment(CommandHistory.class.getName());
				CommandHistory history = CommandHistoryLocalServiceUtil
						.createCommandHistory(id);
				history.setCommand(command);
				history.setExecutionDate(startDate);
				history.setExecutionTime(endDate.getTime()
						- startDate.getTime());
				history.setMode(mode);
				history.setUserId(userId);
				CommandHistoryLocalServiceUtil.updateCommandHistory(history);
				// TODO: delete the oldest entry > MAX_HISTORY_SIZE
				// get the history size
				long historySize = 100;
				List<CommandHistory> historyList = CommandHistoryLocalServiceUtil
						.findCommandHistoryByUserId(userId);
				if (historyList.size() >= historySize) {
					CommandHistoryLocalServiceUtil
							.deleteCommandHistory(historyList.get(0));
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
		}
		return response;
	}

	private String handleAddUser(long userId, long companyId, String command) {
		String response = "";
		CommandLineParser parser = new PosixParser();
		String cmd = command.substring(7);
		Options options = new Options();
		Option option = new Option("sn", true, "screen name of the user");
		options.addOption(option);
		option = new Option("e", true, "email of the user");
		option.setRequired(true);
		options.addOption(option);
		option = new Option("fn", true, "first name of the user");
		option.setRequired(true);
		options.addOption(option);
		option = new Option("ln", true, "last name of the user");
		option.setRequired(true);
		options.addOption(option);
		try {
			CommandLine cmdLine = parser.parse(options, cmd.split(" "));

			User user = addUser(userId, companyId, cmdLine);

			response = "Added user with screen name " + user.getScreenName();
			// HelpFormatter formatter = new HelpFormatter();
			// StringWriter sw = new StringWriter();
			// PrintWriter pw = new PrintWriter(sw);
			// formatter.printUsage(pw, 80, "adduser", options);
			// response = sw.toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return response;
	}

	private User addUser(long userId, long companyId, CommandLine cmdLine)
			throws SystemException, PortalException {
		boolean autoScreenName = false;
		String screenName = cmdLine.getOptionValue("sn");
		if (screenName == null || "".equals(screenName)) {
			autoScreenName = true;
		} else {
			screenName = StringUtil.lowerCase(screenName);
		}

		String emailAddrress = cmdLine.getOptionValue("e");
		long facebookId = 0;
		String openId = StringPool.BLANK;
		String languageId = StringPool.BLANK;
		String timeZoneId = StringPool.BLANK;
		String greeting = StringPool.BLANK;
		String firstName = cmdLine.getOptionValue("fn");
		String middleName = StringPool.BLANK;
		String lastName = cmdLine.getOptionValue("ln");
		int prefixId = 0;
		int suffixId = 0;
		boolean male = true;
		int birthdayMonth = 0;
		int birthdayDay = 0;
		int birthdayYear = 0;
		String comments = StringPool.BLANK;
		String smsSn = StringPool.BLANK;
		String aimSn = StringPool.BLANK;
		String facebookSn = StringPool.BLANK;
		String icqSn = StringPool.BLANK;
		String jabberSn = StringPool.BLANK;
		String msnSn = StringPool.BLANK;
		String mySpaceSn = StringPool.BLANK;
		String skypeSn = StringPool.BLANK;
		String twitterSn = StringPool.BLANK;
		String ymSn = StringPool.BLANK;
		String jobTitle = StringPool.BLANK;

		long[] organizationIds = null;

		Company company = CompanyLocalServiceUtil.getCompany(companyId);
		long creatorUserId = userId;

		if ((creatorUserId != 0) || !company.isStrangers()) {
			if (!PortalPermissionUtil.contains(getPermissionChecker(),
					ActionKeys.ADD_USER)
					&& !UserPermissionUtil.contains(getPermissionChecker(), 0,
							organizationIds, ActionKeys.ADD_USER)) {

				throw new PrincipalException();
			}
		}

		long usrId = CounterLocalServiceUtil.increment(User.class.getName());
		User user = UserLocalServiceUtil.createUser(usrId);
		user.setScreenName(screenName);
		user.setEmailAddress(emailAddrress);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setStatus(WorkflowConstants.STATUS_APPROVED);
		user.setAgreedToTermsOfUse(false);
		user.setCompanyId(companyId);
		Date date = new Date();
		user.setCreateDate(date);
		user.setModifiedDate(date);

		UserLocalServiceUtil.addUser(user);

		return user;
	}

	private String getUptime(long time) {
		long uptimeDiff = System.currentTimeMillis() - time;
		long days = uptimeDiff / Time.DAY;
		long hours = (uptimeDiff / Time.HOUR) % 24;
		long minutes = (uptimeDiff / Time.MINUTE) % 60;
		long seconds = (uptimeDiff / Time.SECOND) % 60;

		NumberFormat numberFormat = NumberFormat.getInstance();

		numberFormat.setMaximumIntegerDigits(2);
		numberFormat.setMinimumIntegerDigits(2);
		String response = "";
		if (days > 0) {
			response = days + (days > 1 ? " days " : " day ");
		}
		response += numberFormat.format(hours) + ":"
				+ numberFormat.format(minutes) + ":"
				+ numberFormat.format(seconds);
		return response;
	}

	public List<CommandHistory> getHistories(long userId) {
		try {
			return CommandHistoryLocalServiceUtil
					.findCommandHistoryByUserId(userId);
		} catch (SystemException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	private String runSQLQuery(String command) {
		String response = "";
		try {
			StringWriter writer = new StringWriter();
			SQLRunner runner = new SQLRunner(writer, "t");
			runner.runStatement(command);
			response = writer.toString();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return response;
	}
}