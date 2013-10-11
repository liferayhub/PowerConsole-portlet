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

package org.liferayhub.pc.model.impl;

import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.model.CacheModel;

import org.liferayhub.pc.model.CommandHistory;

import java.io.Serializable;

import java.util.Date;

/**
 * The cache model class for representing CommandHistory in entity cache.
 *
 * @author Sheikh Sajid
 * @see CommandHistory
 * @generated
 */
public class CommandHistoryCacheModel implements CacheModel<CommandHistory>,
	Serializable {
	@Override
	public String toString() {
		StringBundler sb = new StringBundler(13);

		sb.append("{id=");
		sb.append(id);
		sb.append(", mode=");
		sb.append(mode);
		sb.append(", command=");
		sb.append(command);
		sb.append(", userId=");
		sb.append(userId);
		sb.append(", executionDate=");
		sb.append(executionDate);
		sb.append(", executionTime=");
		sb.append(executionTime);
		sb.append("}");

		return sb.toString();
	}

	public CommandHistory toEntityModel() {
		CommandHistoryImpl commandHistoryImpl = new CommandHistoryImpl();

		commandHistoryImpl.setId(id);

		if (mode == null) {
			commandHistoryImpl.setMode(StringPool.BLANK);
		}
		else {
			commandHistoryImpl.setMode(mode);
		}

		if (command == null) {
			commandHistoryImpl.setCommand(StringPool.BLANK);
		}
		else {
			commandHistoryImpl.setCommand(command);
		}

		commandHistoryImpl.setUserId(userId);

		if (executionDate == Long.MIN_VALUE) {
			commandHistoryImpl.setExecutionDate(null);
		}
		else {
			commandHistoryImpl.setExecutionDate(new Date(executionDate));
		}

		commandHistoryImpl.setExecutionTime(executionTime);

		commandHistoryImpl.resetOriginalValues();

		return commandHistoryImpl;
	}

	public long id;
	public String mode;
	public String command;
	public long userId;
	public long executionDate;
	public long executionTime;
}