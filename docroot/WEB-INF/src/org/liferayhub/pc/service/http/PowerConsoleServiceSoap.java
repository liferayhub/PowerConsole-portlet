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

package org.liferayhub.pc.service.http;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import org.liferayhub.pc.service.PowerConsoleServiceUtil;

import java.rmi.RemoteException;

/**
 * <p>
 * This class provides a SOAP utility for the
 * {@link org.liferayhub.pc.service.PowerConsoleServiceUtil} service utility. The
 * static methods of this class calls the same methods of the service utility.
 * However, the signatures are different because it is difficult for SOAP to
 * support certain types.
 * </p>
 *
 * <p>
 * The benefits of using the SOAP utility is that it is cross platform
 * compatible. SOAP allows different languages like Java, .NET, C++, PHP, and
 * even Perl, to call the generated services. One drawback of SOAP is that it is
 * slow because it needs to serialize all calls into a text format (XML).
 * </p>
 *
 * <p>
 * You can see a list of services at
 * http://localhost:8080/api/secure/axis. Set the property
 * <b>axis.servlet.hosts.allowed</b> in portal.properties to configure
 * security.
 * </p>
 *
 * <p>
 * The SOAP utility is only generated for remote services.
 * </p>
 *
 * @author    Sheikh Sajid
 * @see       PowerConsoleServiceHttp
 * @see       org.liferayhub.pc.service.PowerConsoleServiceUtil
 * @generated
 */
public class PowerConsoleServiceSoap {
	public static java.lang.String runCommand(long userId, long companyId,
		java.lang.String mode, java.lang.String command)
		throws RemoteException {
		try {
			java.lang.String returnValue = PowerConsoleServiceUtil.runCommand(userId,
					companyId, mode, command);

			return returnValue;
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	public static org.liferayhub.pc.model.CommandHistory[] getHistories(
		long userId) throws RemoteException {
		try {
			java.util.List<org.liferayhub.pc.model.CommandHistory> returnValue = PowerConsoleServiceUtil.getHistories(userId);

			return returnValue.toArray(new org.liferayhub.pc.model.CommandHistory[returnValue.size()]);
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new RemoteException(e.getMessage());
		}
	}

	private static Log _log = LogFactoryUtil.getLog(PowerConsoleServiceSoap.class);
}