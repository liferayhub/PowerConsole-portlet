package org.liferayhub.pc;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.PortletPreferencesFactoryUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * Portlet implementation class ConsoleAdminPortlet
 */
public class ConsoleAdminPortlet extends MVCPortlet {
	@Override
	public void doView(RenderRequest renderRequest, RenderResponse renderResponse) throws IOException, PortletException {
		try {
			PortletPreferences prefs = PortletPreferencesFactoryUtil.getPortletSetup(renderRequest);
			renderRequest.setAttribute("welcomeMessage", prefs.getValue("welcomeMessage",
					"Welcome to Power Console.\nWith Great Powers comes Greater Responsibilities"));
			renderRequest.setAttribute("devMode", prefs.getValue("devMode", "false"));
			renderRequest.setAttribute("height", prefs.getValue("height", "100"));
			renderRequest.setAttribute("historySize", prefs.getValue("historySize", "100"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.doView(renderRequest, renderResponse);
	}

	public void save(ActionRequest actionRequest, ActionResponse actionResponse) throws IOException, PortletException {
		try {
			PortletPreferences prefs = PortletPreferencesFactoryUtil.getPortletSetup(actionRequest);
			prefs.setValue("welcomeMessage", actionRequest.getParameter("welcomeMessage"));
			prefs.setValue("devMode", actionRequest.getParameter("devMode"));
			prefs.setValue("height", actionRequest.getParameter("height"));
			prefs.setValue("historySize", actionRequest.getParameter("historySize"));
			prefs.store();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
