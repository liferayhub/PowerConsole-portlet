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

import java.util.List;

import org.liferayhub.pc.model.CommandHistory;
import org.liferayhub.pc.service.base.CommandHistoryLocalServiceBaseImpl;

import com.liferay.portal.kernel.exception.SystemException;

/**
 * The implementation of the command history local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link org.liferayhub.pc.service.CommandHistoryLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Sheikh Sajid
 * @see org.liferayhub.pc.service.base.CommandHistoryLocalServiceBaseImpl
 * @see org.liferayhub.pc.service.CommandHistoryLocalServiceUtil
 */
public class CommandHistoryLocalServiceImpl
	extends CommandHistoryLocalServiceBaseImpl {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this interface directly. Always use {@link org.liferayhub.pc.service.CommandHistoryLocalServiceUtil} to access the command history local service.
	 */
	public List<CommandHistory> findCommandHistoryByUserId(long userId) throws SystemException {
		return commandHistoryPersistence.findByUserId(userId);
	}
}