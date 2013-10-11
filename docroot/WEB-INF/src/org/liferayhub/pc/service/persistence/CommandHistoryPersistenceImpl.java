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

package org.liferayhub.pc.service.persistence;

import com.liferay.portal.NoSuchModelException;
import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.cache.CacheRegistryUtil;
import com.liferay.portal.kernel.dao.orm.EntityCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderCacheUtil;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.InstanceFactory;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.model.ModelListener;
import com.liferay.portal.service.persistence.BatchSessionUtil;
import com.liferay.portal.service.persistence.ResourcePersistence;
import com.liferay.portal.service.persistence.UserPersistence;
import com.liferay.portal.service.persistence.impl.BasePersistenceImpl;

import org.liferayhub.pc.NoSuchCommandHistoryException;
import org.liferayhub.pc.model.CommandHistory;
import org.liferayhub.pc.model.impl.CommandHistoryImpl;
import org.liferayhub.pc.model.impl.CommandHistoryModelImpl;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The persistence implementation for the command history service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Sheikh Sajid
 * @see CommandHistoryPersistence
 * @see CommandHistoryUtil
 * @generated
 */
public class CommandHistoryPersistenceImpl extends BasePersistenceImpl<CommandHistory>
	implements CommandHistoryPersistence {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use {@link CommandHistoryUtil} to access the command history persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY = CommandHistoryImpl.class.getName();
	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List1";
	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION = FINDER_CLASS_NAME_ENTITY +
		".List2";
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_BY_USERID = new FinderPath(CommandHistoryModelImpl.ENTITY_CACHE_ENABLED,
			CommandHistoryModelImpl.FINDER_CACHE_ENABLED,
			CommandHistoryImpl.class, FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findByUserId",
			new String[] {
				Long.class.getName(),
				
			"java.lang.Integer", "java.lang.Integer",
				"com.liferay.portal.kernel.util.OrderByComparator"
			});
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USERID =
		new FinderPath(CommandHistoryModelImpl.ENTITY_CACHE_ENABLED,
			CommandHistoryModelImpl.FINDER_CACHE_ENABLED,
			CommandHistoryImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByUserId",
			new String[] { Long.class.getName() },
			CommandHistoryModelImpl.USERID_COLUMN_BITMASK);
	public static final FinderPath FINDER_PATH_COUNT_BY_USERID = new FinderPath(CommandHistoryModelImpl.ENTITY_CACHE_ENABLED,
			CommandHistoryModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByUserId",
			new String[] { Long.class.getName() });
	public static final FinderPath FINDER_PATH_WITH_PAGINATION_FIND_ALL = new FinderPath(CommandHistoryModelImpl.ENTITY_CACHE_ENABLED,
			CommandHistoryModelImpl.FINDER_CACHE_ENABLED,
			CommandHistoryImpl.class, FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
			"findAll", new String[0]);
	public static final FinderPath FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL = new FinderPath(CommandHistoryModelImpl.ENTITY_CACHE_ENABLED,
			CommandHistoryModelImpl.FINDER_CACHE_ENABLED,
			CommandHistoryImpl.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0]);
	public static final FinderPath FINDER_PATH_COUNT_ALL = new FinderPath(CommandHistoryModelImpl.ENTITY_CACHE_ENABLED,
			CommandHistoryModelImpl.FINDER_CACHE_ENABLED, Long.class,
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll", new String[0]);

	/**
	 * Caches the command history in the entity cache if it is enabled.
	 *
	 * @param commandHistory the command history
	 */
	public void cacheResult(CommandHistory commandHistory) {
		EntityCacheUtil.putResult(CommandHistoryModelImpl.ENTITY_CACHE_ENABLED,
			CommandHistoryImpl.class, commandHistory.getPrimaryKey(),
			commandHistory);

		commandHistory.resetOriginalValues();
	}

	/**
	 * Caches the command histories in the entity cache if it is enabled.
	 *
	 * @param commandHistories the command histories
	 */
	public void cacheResult(List<CommandHistory> commandHistories) {
		for (CommandHistory commandHistory : commandHistories) {
			if (EntityCacheUtil.getResult(
						CommandHistoryModelImpl.ENTITY_CACHE_ENABLED,
						CommandHistoryImpl.class, commandHistory.getPrimaryKey()) == null) {
				cacheResult(commandHistory);
			}
			else {
				commandHistory.resetOriginalValues();
			}
		}
	}

	/**
	 * Clears the cache for all command histories.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		if (_HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE) {
			CacheRegistryUtil.clear(CommandHistoryImpl.class.getName());
		}

		EntityCacheUtil.clearCache(CommandHistoryImpl.class.getName());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	/**
	 * Clears the cache for the command history.
	 *
	 * <p>
	 * The {@link com.liferay.portal.kernel.dao.orm.EntityCache} and {@link com.liferay.portal.kernel.dao.orm.FinderCache} are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(CommandHistory commandHistory) {
		EntityCacheUtil.removeResult(CommandHistoryModelImpl.ENTITY_CACHE_ENABLED,
			CommandHistoryImpl.class, commandHistory.getPrimaryKey());

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@Override
	public void clearCache(List<CommandHistory> commandHistories) {
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);
		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);

		for (CommandHistory commandHistory : commandHistories) {
			EntityCacheUtil.removeResult(CommandHistoryModelImpl.ENTITY_CACHE_ENABLED,
				CommandHistoryImpl.class, commandHistory.getPrimaryKey());
		}
	}

	/**
	 * Creates a new command history with the primary key. Does not add the command history to the database.
	 *
	 * @param id the primary key for the new command history
	 * @return the new command history
	 */
	public CommandHistory create(long id) {
		CommandHistory commandHistory = new CommandHistoryImpl();

		commandHistory.setNew(true);
		commandHistory.setPrimaryKey(id);

		return commandHistory;
	}

	/**
	 * Removes the command history with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param id the primary key of the command history
	 * @return the command history that was removed
	 * @throws org.liferayhub.pc.NoSuchCommandHistoryException if a command history with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public CommandHistory remove(long id)
		throws NoSuchCommandHistoryException, SystemException {
		return remove(Long.valueOf(id));
	}

	/**
	 * Removes the command history with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the command history
	 * @return the command history that was removed
	 * @throws org.liferayhub.pc.NoSuchCommandHistoryException if a command history with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public CommandHistory remove(Serializable primaryKey)
		throws NoSuchCommandHistoryException, SystemException {
		Session session = null;

		try {
			session = openSession();

			CommandHistory commandHistory = (CommandHistory)session.get(CommandHistoryImpl.class,
					primaryKey);

			if (commandHistory == null) {
				if (_log.isWarnEnabled()) {
					_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchCommandHistoryException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
					primaryKey);
			}

			return remove(commandHistory);
		}
		catch (NoSuchCommandHistoryException nsee) {
			throw nsee;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	protected CommandHistory removeImpl(CommandHistory commandHistory)
		throws SystemException {
		commandHistory = toUnwrappedModel(commandHistory);

		Session session = null;

		try {
			session = openSession();

			BatchSessionUtil.delete(session, commandHistory);
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		clearCache(commandHistory);

		return commandHistory;
	}

	@Override
	public CommandHistory updateImpl(
		org.liferayhub.pc.model.CommandHistory commandHistory, boolean merge)
		throws SystemException {
		commandHistory = toUnwrappedModel(commandHistory);

		boolean isNew = commandHistory.isNew();

		CommandHistoryModelImpl commandHistoryModelImpl = (CommandHistoryModelImpl)commandHistory;

		Session session = null;

		try {
			session = openSession();

			BatchSessionUtil.update(session, commandHistory, merge);

			commandHistory.setNew(false);
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}

		FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITH_PAGINATION);

		if (isNew || !CommandHistoryModelImpl.COLUMN_BITMASK_ENABLED) {
			FinderCacheUtil.clearCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
		}

		else {
			if ((commandHistoryModelImpl.getColumnBitmask() &
					FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USERID.getColumnBitmask()) != 0) {
				Object[] args = new Object[] {
						Long.valueOf(commandHistoryModelImpl.getOriginalUserId())
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_USERID, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USERID,
					args);

				args = new Object[] {
						Long.valueOf(commandHistoryModelImpl.getUserId())
					};

				FinderCacheUtil.removeResult(FINDER_PATH_COUNT_BY_USERID, args);
				FinderCacheUtil.removeResult(FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USERID,
					args);
			}
		}

		EntityCacheUtil.putResult(CommandHistoryModelImpl.ENTITY_CACHE_ENABLED,
			CommandHistoryImpl.class, commandHistory.getPrimaryKey(),
			commandHistory);

		return commandHistory;
	}

	protected CommandHistory toUnwrappedModel(CommandHistory commandHistory) {
		if (commandHistory instanceof CommandHistoryImpl) {
			return commandHistory;
		}

		CommandHistoryImpl commandHistoryImpl = new CommandHistoryImpl();

		commandHistoryImpl.setNew(commandHistory.isNew());
		commandHistoryImpl.setPrimaryKey(commandHistory.getPrimaryKey());

		commandHistoryImpl.setId(commandHistory.getId());
		commandHistoryImpl.setMode(commandHistory.getMode());
		commandHistoryImpl.setCommand(commandHistory.getCommand());
		commandHistoryImpl.setUserId(commandHistory.getUserId());
		commandHistoryImpl.setExecutionDate(commandHistory.getExecutionDate());
		commandHistoryImpl.setExecutionTime(commandHistory.getExecutionTime());

		return commandHistoryImpl;
	}

	/**
	 * Returns the command history with the primary key or throws a {@link com.liferay.portal.NoSuchModelException} if it could not be found.
	 *
	 * @param primaryKey the primary key of the command history
	 * @return the command history
	 * @throws com.liferay.portal.NoSuchModelException if a command history with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public CommandHistory findByPrimaryKey(Serializable primaryKey)
		throws NoSuchModelException, SystemException {
		return findByPrimaryKey(((Long)primaryKey).longValue());
	}

	/**
	 * Returns the command history with the primary key or throws a {@link org.liferayhub.pc.NoSuchCommandHistoryException} if it could not be found.
	 *
	 * @param id the primary key of the command history
	 * @return the command history
	 * @throws org.liferayhub.pc.NoSuchCommandHistoryException if a command history with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public CommandHistory findByPrimaryKey(long id)
		throws NoSuchCommandHistoryException, SystemException {
		CommandHistory commandHistory = fetchByPrimaryKey(id);

		if (commandHistory == null) {
			if (_log.isWarnEnabled()) {
				_log.warn(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + id);
			}

			throw new NoSuchCommandHistoryException(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY +
				id);
		}

		return commandHistory;
	}

	/**
	 * Returns the command history with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the command history
	 * @return the command history, or <code>null</code> if a command history with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	@Override
	public CommandHistory fetchByPrimaryKey(Serializable primaryKey)
		throws SystemException {
		return fetchByPrimaryKey(((Long)primaryKey).longValue());
	}

	/**
	 * Returns the command history with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param id the primary key of the command history
	 * @return the command history, or <code>null</code> if a command history with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public CommandHistory fetchByPrimaryKey(long id) throws SystemException {
		CommandHistory commandHistory = (CommandHistory)EntityCacheUtil.getResult(CommandHistoryModelImpl.ENTITY_CACHE_ENABLED,
				CommandHistoryImpl.class, id);

		if (commandHistory == _nullCommandHistory) {
			return null;
		}

		if (commandHistory == null) {
			Session session = null;

			boolean hasException = false;

			try {
				session = openSession();

				commandHistory = (CommandHistory)session.get(CommandHistoryImpl.class,
						Long.valueOf(id));
			}
			catch (Exception e) {
				hasException = true;

				throw processException(e);
			}
			finally {
				if (commandHistory != null) {
					cacheResult(commandHistory);
				}
				else if (!hasException) {
					EntityCacheUtil.putResult(CommandHistoryModelImpl.ENTITY_CACHE_ENABLED,
						CommandHistoryImpl.class, id, _nullCommandHistory);
				}

				closeSession(session);
			}
		}

		return commandHistory;
	}

	/**
	 * Returns all the command histories where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @return the matching command histories
	 * @throws SystemException if a system exception occurred
	 */
	public List<CommandHistory> findByUserId(long userId)
		throws SystemException {
		return findByUserId(userId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the command histories where userId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param userId the user ID
	 * @param start the lower bound of the range of command histories
	 * @param end the upper bound of the range of command histories (not inclusive)
	 * @return the range of matching command histories
	 * @throws SystemException if a system exception occurred
	 */
	public List<CommandHistory> findByUserId(long userId, int start, int end)
		throws SystemException {
		return findByUserId(userId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the command histories where userId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param userId the user ID
	 * @param start the lower bound of the range of command histories
	 * @param end the upper bound of the range of command histories (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching command histories
	 * @throws SystemException if a system exception occurred
	 */
	public List<CommandHistory> findByUserId(long userId, int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_BY_USERID;
			finderArgs = new Object[] { userId };
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_BY_USERID;
			finderArgs = new Object[] { userId, start, end, orderByComparator };
		}

		List<CommandHistory> list = (List<CommandHistory>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if ((list != null) && !list.isEmpty()) {
			for (CommandHistory commandHistory : list) {
				if ((userId != commandHistory.getUserId())) {
					list = null;

					break;
				}
			}
		}

		if (list == null) {
			StringBundler query = null;

			if (orderByComparator != null) {
				query = new StringBundler(3 +
						(orderByComparator.getOrderByFields().length * 3));
			}
			else {
				query = new StringBundler(3);
			}

			query.append(_SQL_SELECT_COMMANDHISTORY_WHERE);

			query.append(_FINDER_COLUMN_USERID_USERID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);
			}

			else {
				query.append(CommandHistoryModelImpl.ORDER_BY_JPQL);
			}

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(userId);

				list = (List<CommandHistory>)QueryUtil.list(q, getDialect(),
						start, end);
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (list == null) {
					FinderCacheUtil.removeResult(finderPath, finderArgs);
				}
				else {
					cacheResult(list);

					FinderCacheUtil.putResult(finderPath, finderArgs, list);
				}

				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first command history in the ordered set where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching command history
	 * @throws org.liferayhub.pc.NoSuchCommandHistoryException if a matching command history could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public CommandHistory findByUserId_First(long userId,
		OrderByComparator orderByComparator)
		throws NoSuchCommandHistoryException, SystemException {
		CommandHistory commandHistory = fetchByUserId_First(userId,
				orderByComparator);

		if (commandHistory != null) {
			return commandHistory;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("userId=");
		msg.append(userId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchCommandHistoryException(msg.toString());
	}

	/**
	 * Returns the first command history in the ordered set where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching command history, or <code>null</code> if a matching command history could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public CommandHistory fetchByUserId_First(long userId,
		OrderByComparator orderByComparator) throws SystemException {
		List<CommandHistory> list = findByUserId(userId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last command history in the ordered set where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching command history
	 * @throws org.liferayhub.pc.NoSuchCommandHistoryException if a matching command history could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public CommandHistory findByUserId_Last(long userId,
		OrderByComparator orderByComparator)
		throws NoSuchCommandHistoryException, SystemException {
		CommandHistory commandHistory = fetchByUserId_Last(userId,
				orderByComparator);

		if (commandHistory != null) {
			return commandHistory;
		}

		StringBundler msg = new StringBundler(4);

		msg.append(_NO_SUCH_ENTITY_WITH_KEY);

		msg.append("userId=");
		msg.append(userId);

		msg.append(StringPool.CLOSE_CURLY_BRACE);

		throw new NoSuchCommandHistoryException(msg.toString());
	}

	/**
	 * Returns the last command history in the ordered set where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching command history, or <code>null</code> if a matching command history could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public CommandHistory fetchByUserId_Last(long userId,
		OrderByComparator orderByComparator) throws SystemException {
		int count = countByUserId(userId);

		List<CommandHistory> list = findByUserId(userId, count - 1, count,
				orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the command histories before and after the current command history in the ordered set where userId = &#63;.
	 *
	 * @param id the primary key of the current command history
	 * @param userId the user ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next command history
	 * @throws org.liferayhub.pc.NoSuchCommandHistoryException if a command history with the primary key could not be found
	 * @throws SystemException if a system exception occurred
	 */
	public CommandHistory[] findByUserId_PrevAndNext(long id, long userId,
		OrderByComparator orderByComparator)
		throws NoSuchCommandHistoryException, SystemException {
		CommandHistory commandHistory = findByPrimaryKey(id);

		Session session = null;

		try {
			session = openSession();

			CommandHistory[] array = new CommandHistoryImpl[3];

			array[0] = getByUserId_PrevAndNext(session, commandHistory, userId,
					orderByComparator, true);

			array[1] = commandHistory;

			array[2] = getByUserId_PrevAndNext(session, commandHistory, userId,
					orderByComparator, false);

			return array;
		}
		catch (Exception e) {
			throw processException(e);
		}
		finally {
			closeSession(session);
		}
	}

	protected CommandHistory getByUserId_PrevAndNext(Session session,
		CommandHistory commandHistory, long userId,
		OrderByComparator orderByComparator, boolean previous) {
		StringBundler query = null;

		if (orderByComparator != null) {
			query = new StringBundler(6 +
					(orderByComparator.getOrderByFields().length * 6));
		}
		else {
			query = new StringBundler(3);
		}

		query.append(_SQL_SELECT_COMMANDHISTORY_WHERE);

		query.append(_FINDER_COLUMN_USERID_USERID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields = orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				query.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						query.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(WHERE_GREATER_THAN);
					}
					else {
						query.append(WHERE_LESSER_THAN);
					}
				}
			}

			query.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				query.append(_ORDER_BY_ENTITY_ALIAS);
				query.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						query.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						query.append(ORDER_BY_ASC);
					}
					else {
						query.append(ORDER_BY_DESC);
					}
				}
			}
		}

		else {
			query.append(CommandHistoryModelImpl.ORDER_BY_JPQL);
		}

		String sql = query.toString();

		Query q = session.createQuery(sql);

		q.setFirstResult(0);
		q.setMaxResults(2);

		QueryPos qPos = QueryPos.getInstance(q);

		qPos.add(userId);

		if (orderByComparator != null) {
			Object[] values = orderByComparator.getOrderByConditionValues(commandHistory);

			for (Object value : values) {
				qPos.add(value);
			}
		}

		List<CommandHistory> list = q.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Returns all the command histories.
	 *
	 * @return the command histories
	 * @throws SystemException if a system exception occurred
	 */
	public List<CommandHistory> findAll() throws SystemException {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the command histories.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param start the lower bound of the range of command histories
	 * @param end the upper bound of the range of command histories (not inclusive)
	 * @return the range of command histories
	 * @throws SystemException if a system exception occurred
	 */
	public List<CommandHistory> findAll(int start, int end)
		throws SystemException {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the command histories.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to {@link com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS} will return the full result set.
	 * </p>
	 *
	 * @param start the lower bound of the range of command histories
	 * @param end the upper bound of the range of command histories (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of command histories
	 * @throws SystemException if a system exception occurred
	 */
	public List<CommandHistory> findAll(int start, int end,
		OrderByComparator orderByComparator) throws SystemException {
		FinderPath finderPath = null;
		Object[] finderArgs = new Object[] { start, end, orderByComparator };

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
				(orderByComparator == null)) {
			finderPath = FINDER_PATH_WITHOUT_PAGINATION_FIND_ALL;
			finderArgs = FINDER_ARGS_EMPTY;
		}
		else {
			finderPath = FINDER_PATH_WITH_PAGINATION_FIND_ALL;
			finderArgs = new Object[] { start, end, orderByComparator };
		}

		List<CommandHistory> list = (List<CommandHistory>)FinderCacheUtil.getResult(finderPath,
				finderArgs, this);

		if (list == null) {
			StringBundler query = null;
			String sql = null;

			if (orderByComparator != null) {
				query = new StringBundler(2 +
						(orderByComparator.getOrderByFields().length * 3));

				query.append(_SQL_SELECT_COMMANDHISTORY);

				appendOrderByComparator(query, _ORDER_BY_ENTITY_ALIAS,
					orderByComparator);

				sql = query.toString();
			}
			else {
				sql = _SQL_SELECT_COMMANDHISTORY.concat(CommandHistoryModelImpl.ORDER_BY_JPQL);
			}

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				if (orderByComparator == null) {
					list = (List<CommandHistory>)QueryUtil.list(q,
							getDialect(), start, end, false);

					Collections.sort(list);
				}
				else {
					list = (List<CommandHistory>)QueryUtil.list(q,
							getDialect(), start, end);
				}
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (list == null) {
					FinderCacheUtil.removeResult(finderPath, finderArgs);
				}
				else {
					cacheResult(list);

					FinderCacheUtil.putResult(finderPath, finderArgs, list);
				}

				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the command histories where userId = &#63; from the database.
	 *
	 * @param userId the user ID
	 * @throws SystemException if a system exception occurred
	 */
	public void removeByUserId(long userId) throws SystemException {
		for (CommandHistory commandHistory : findByUserId(userId)) {
			remove(commandHistory);
		}
	}

	/**
	 * Removes all the command histories from the database.
	 *
	 * @throws SystemException if a system exception occurred
	 */
	public void removeAll() throws SystemException {
		for (CommandHistory commandHistory : findAll()) {
			remove(commandHistory);
		}
	}

	/**
	 * Returns the number of command histories where userId = &#63;.
	 *
	 * @param userId the user ID
	 * @return the number of matching command histories
	 * @throws SystemException if a system exception occurred
	 */
	public int countByUserId(long userId) throws SystemException {
		Object[] finderArgs = new Object[] { userId };

		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_BY_USERID,
				finderArgs, this);

		if (count == null) {
			StringBundler query = new StringBundler(2);

			query.append(_SQL_COUNT_COMMANDHISTORY_WHERE);

			query.append(_FINDER_COLUMN_USERID_USERID_2);

			String sql = query.toString();

			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(sql);

				QueryPos qPos = QueryPos.getInstance(q);

				qPos.add(userId);

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (count == null) {
					count = Long.valueOf(0);
				}

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_BY_USERID,
					finderArgs, count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Returns the number of command histories.
	 *
	 * @return the number of command histories
	 * @throws SystemException if a system exception occurred
	 */
	public int countAll() throws SystemException {
		Long count = (Long)FinderCacheUtil.getResult(FINDER_PATH_COUNT_ALL,
				FINDER_ARGS_EMPTY, this);

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query q = session.createQuery(_SQL_COUNT_COMMANDHISTORY);

				count = (Long)q.uniqueResult();
			}
			catch (Exception e) {
				throw processException(e);
			}
			finally {
				if (count == null) {
					count = Long.valueOf(0);
				}

				FinderCacheUtil.putResult(FINDER_PATH_COUNT_ALL,
					FINDER_ARGS_EMPTY, count);

				closeSession(session);
			}
		}

		return count.intValue();
	}

	/**
	 * Initializes the command history persistence.
	 */
	public void afterPropertiesSet() {
		String[] listenerClassNames = StringUtil.split(GetterUtil.getString(
					com.liferay.util.service.ServiceProps.get(
						"value.object.listener.org.liferayhub.pc.model.CommandHistory")));

		if (listenerClassNames.length > 0) {
			try {
				List<ModelListener<CommandHistory>> listenersList = new ArrayList<ModelListener<CommandHistory>>();

				for (String listenerClassName : listenerClassNames) {
					Class<?> clazz = getClass();

					listenersList.add((ModelListener<CommandHistory>)InstanceFactory.newInstance(
							clazz.getClassLoader(), listenerClassName));
				}

				listeners = listenersList.toArray(new ModelListener[listenersList.size()]);
			}
			catch (Exception e) {
				_log.error(e);
			}
		}
	}

	public void destroy() {
		EntityCacheUtil.removeCache(CommandHistoryImpl.class.getName());
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_ENTITY);
		FinderCacheUtil.removeCache(FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION);
	}

	@BeanReference(type = CommandHistoryPersistence.class)
	protected CommandHistoryPersistence commandHistoryPersistence;
	@BeanReference(type = ResourcePersistence.class)
	protected ResourcePersistence resourcePersistence;
	@BeanReference(type = UserPersistence.class)
	protected UserPersistence userPersistence;
	private static final String _SQL_SELECT_COMMANDHISTORY = "SELECT commandHistory FROM CommandHistory commandHistory";
	private static final String _SQL_SELECT_COMMANDHISTORY_WHERE = "SELECT commandHistory FROM CommandHistory commandHistory WHERE ";
	private static final String _SQL_COUNT_COMMANDHISTORY = "SELECT COUNT(commandHistory) FROM CommandHistory commandHistory";
	private static final String _SQL_COUNT_COMMANDHISTORY_WHERE = "SELECT COUNT(commandHistory) FROM CommandHistory commandHistory WHERE ";
	private static final String _FINDER_COLUMN_USERID_USERID_2 = "commandHistory.userId = ?";
	private static final String _ORDER_BY_ENTITY_ALIAS = "commandHistory.";
	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY = "No CommandHistory exists with the primary key ";
	private static final String _NO_SUCH_ENTITY_WITH_KEY = "No CommandHistory exists with the key {";
	private static final boolean _HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE = GetterUtil.getBoolean(PropsUtil.get(
				PropsKeys.HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE));
	private static Log _log = LogFactoryUtil.getLog(CommandHistoryPersistenceImpl.class);
	private static CommandHistory _nullCommandHistory = new CommandHistoryImpl() {
			@Override
			public Object clone() {
				return this;
			}

			@Override
			public CacheModel<CommandHistory> toCacheModel() {
				return _nullCommandHistoryCacheModel;
			}
		};

	private static CacheModel<CommandHistory> _nullCommandHistoryCacheModel = new CacheModel<CommandHistory>() {
			public CommandHistory toEntityModel() {
				return _nullCommandHistory;
			}
		};
}