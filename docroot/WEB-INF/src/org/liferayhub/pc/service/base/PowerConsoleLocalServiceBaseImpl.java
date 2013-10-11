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

package org.liferayhub.pc.service.base;

import com.liferay.counter.service.CounterLocalService;

import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.bean.IdentifiableBean;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.InfrastructureUtil;
import com.liferay.portal.service.BaseLocalServiceImpl;
import com.liferay.portal.service.ResourceLocalService;
import com.liferay.portal.service.ResourceService;
import com.liferay.portal.service.UserLocalService;
import com.liferay.portal.service.UserService;
import com.liferay.portal.service.persistence.ResourcePersistence;
import com.liferay.portal.service.persistence.UserPersistence;

import org.liferayhub.pc.service.CommandHistoryLocalService;
import org.liferayhub.pc.service.PowerConsoleLocalService;
import org.liferayhub.pc.service.PowerConsoleService;
import org.liferayhub.pc.service.persistence.CommandHistoryPersistence;

import javax.sql.DataSource;

/**
 * The base implementation of the power console local service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link org.liferayhub.pc.service.impl.PowerConsoleLocalServiceImpl}.
 * </p>
 *
 * @author Sheikh Sajid
 * @see org.liferayhub.pc.service.impl.PowerConsoleLocalServiceImpl
 * @see org.liferayhub.pc.service.PowerConsoleLocalServiceUtil
 * @generated
 */
public abstract class PowerConsoleLocalServiceBaseImpl
	extends BaseLocalServiceImpl implements PowerConsoleLocalService,
		IdentifiableBean {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link org.liferayhub.pc.service.PowerConsoleLocalServiceUtil} to access the power console local service.
	 */

	/**
	 * Returns the command history local service.
	 *
	 * @return the command history local service
	 */
	public CommandHistoryLocalService getCommandHistoryLocalService() {
		return commandHistoryLocalService;
	}

	/**
	 * Sets the command history local service.
	 *
	 * @param commandHistoryLocalService the command history local service
	 */
	public void setCommandHistoryLocalService(
		CommandHistoryLocalService commandHistoryLocalService) {
		this.commandHistoryLocalService = commandHistoryLocalService;
	}

	/**
	 * Returns the command history persistence.
	 *
	 * @return the command history persistence
	 */
	public CommandHistoryPersistence getCommandHistoryPersistence() {
		return commandHistoryPersistence;
	}

	/**
	 * Sets the command history persistence.
	 *
	 * @param commandHistoryPersistence the command history persistence
	 */
	public void setCommandHistoryPersistence(
		CommandHistoryPersistence commandHistoryPersistence) {
		this.commandHistoryPersistence = commandHistoryPersistence;
	}

	/**
	 * Returns the power console local service.
	 *
	 * @return the power console local service
	 */
	public PowerConsoleLocalService getPowerConsoleLocalService() {
		return powerConsoleLocalService;
	}

	/**
	 * Sets the power console local service.
	 *
	 * @param powerConsoleLocalService the power console local service
	 */
	public void setPowerConsoleLocalService(
		PowerConsoleLocalService powerConsoleLocalService) {
		this.powerConsoleLocalService = powerConsoleLocalService;
	}

	/**
	 * Returns the power console remote service.
	 *
	 * @return the power console remote service
	 */
	public PowerConsoleService getPowerConsoleService() {
		return powerConsoleService;
	}

	/**
	 * Sets the power console remote service.
	 *
	 * @param powerConsoleService the power console remote service
	 */
	public void setPowerConsoleService(PowerConsoleService powerConsoleService) {
		this.powerConsoleService = powerConsoleService;
	}

	/**
	 * Returns the counter local service.
	 *
	 * @return the counter local service
	 */
	public CounterLocalService getCounterLocalService() {
		return counterLocalService;
	}

	/**
	 * Sets the counter local service.
	 *
	 * @param counterLocalService the counter local service
	 */
	public void setCounterLocalService(CounterLocalService counterLocalService) {
		this.counterLocalService = counterLocalService;
	}

	/**
	 * Returns the resource local service.
	 *
	 * @return the resource local service
	 */
	public ResourceLocalService getResourceLocalService() {
		return resourceLocalService;
	}

	/**
	 * Sets the resource local service.
	 *
	 * @param resourceLocalService the resource local service
	 */
	public void setResourceLocalService(
		ResourceLocalService resourceLocalService) {
		this.resourceLocalService = resourceLocalService;
	}

	/**
	 * Returns the resource remote service.
	 *
	 * @return the resource remote service
	 */
	public ResourceService getResourceService() {
		return resourceService;
	}

	/**
	 * Sets the resource remote service.
	 *
	 * @param resourceService the resource remote service
	 */
	public void setResourceService(ResourceService resourceService) {
		this.resourceService = resourceService;
	}

	/**
	 * Returns the resource persistence.
	 *
	 * @return the resource persistence
	 */
	public ResourcePersistence getResourcePersistence() {
		return resourcePersistence;
	}

	/**
	 * Sets the resource persistence.
	 *
	 * @param resourcePersistence the resource persistence
	 */
	public void setResourcePersistence(ResourcePersistence resourcePersistence) {
		this.resourcePersistence = resourcePersistence;
	}

	/**
	 * Returns the user local service.
	 *
	 * @return the user local service
	 */
	public UserLocalService getUserLocalService() {
		return userLocalService;
	}

	/**
	 * Sets the user local service.
	 *
	 * @param userLocalService the user local service
	 */
	public void setUserLocalService(UserLocalService userLocalService) {
		this.userLocalService = userLocalService;
	}

	/**
	 * Returns the user remote service.
	 *
	 * @return the user remote service
	 */
	public UserService getUserService() {
		return userService;
	}

	/**
	 * Sets the user remote service.
	 *
	 * @param userService the user remote service
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Returns the user persistence.
	 *
	 * @return the user persistence
	 */
	public UserPersistence getUserPersistence() {
		return userPersistence;
	}

	/**
	 * Sets the user persistence.
	 *
	 * @param userPersistence the user persistence
	 */
	public void setUserPersistence(UserPersistence userPersistence) {
		this.userPersistence = userPersistence;
	}

	public void afterPropertiesSet() {
		Class<?> clazz = getClass();

		_classLoader = clazz.getClassLoader();
	}

	public void destroy() {
	}

	/**
	 * Returns the Spring bean ID for this bean.
	 *
	 * @return the Spring bean ID for this bean
	 */
	public String getBeanIdentifier() {
		return _beanIdentifier;
	}

	/**
	 * Sets the Spring bean ID for this bean.
	 *
	 * @param beanIdentifier the Spring bean ID for this bean
	 */
	public void setBeanIdentifier(String beanIdentifier) {
		_beanIdentifier = beanIdentifier;
	}

	public Object invokeMethod(String name, String[] parameterTypes,
		Object[] arguments) throws Throwable {
		Thread currentThread = Thread.currentThread();

		ClassLoader contextClassLoader = currentThread.getContextClassLoader();

		if (contextClassLoader != _classLoader) {
			currentThread.setContextClassLoader(_classLoader);
		}

		try {
			return _clpInvoker.invokeMethod(name, parameterTypes, arguments);
		}
		finally {
			if (contextClassLoader != _classLoader) {
				currentThread.setContextClassLoader(contextClassLoader);
			}
		}
	}

	/**
	 * Performs an SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) throws SystemException {
		try {
			DataSource dataSource = InfrastructureUtil.getDataSource();

			SqlUpdate sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(dataSource,
					sql, new int[0]);

			sqlUpdate.update();
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
	}

	@BeanReference(type = CommandHistoryLocalService.class)
	protected CommandHistoryLocalService commandHistoryLocalService;
	@BeanReference(type = CommandHistoryPersistence.class)
	protected CommandHistoryPersistence commandHistoryPersistence;
	@BeanReference(type = PowerConsoleLocalService.class)
	protected PowerConsoleLocalService powerConsoleLocalService;
	@BeanReference(type = PowerConsoleService.class)
	protected PowerConsoleService powerConsoleService;
	@BeanReference(type = CounterLocalService.class)
	protected CounterLocalService counterLocalService;
	@BeanReference(type = ResourceLocalService.class)
	protected ResourceLocalService resourceLocalService;
	@BeanReference(type = ResourceService.class)
	protected ResourceService resourceService;
	@BeanReference(type = ResourcePersistence.class)
	protected ResourcePersistence resourcePersistence;
	@BeanReference(type = UserLocalService.class)
	protected UserLocalService userLocalService;
	@BeanReference(type = UserService.class)
	protected UserService userService;
	@BeanReference(type = UserPersistence.class)
	protected UserPersistence userPersistence;
	private String _beanIdentifier;
	private ClassLoader _classLoader;
	private PowerConsoleLocalServiceClpInvoker _clpInvoker = new PowerConsoleLocalServiceClpInvoker();
}