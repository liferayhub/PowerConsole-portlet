<%@include file="/html/init.jsp"%>
<portlet:actionURL var="save" name="save">
</portlet:actionURL>
<aui:form name="fm" method="post" action="<%= save %>">
	<aui:input name="welcomeMessage" type="textarea" label="Welcome Message:" value="${requestScope['welcomeMessage']}"></aui:input>
	<aui:input name="height" label="Console Height:" value="${requestScope['height']}"></aui:input>
	<aui:input name="historySize" label="History List Size:" value="${requestScope['historySize']}"></aui:input>
	<aui:input name="devMode" type="checkbox" label="Development Mode"
		checked="<%= Boolean.parseBoolean((request.getAttribute(\"devMode\")+\"\").toString()) %>"></aui:input>
	<aui:button-row>
		<aui:button name="addButton" type="submit" value="Save" />
	</aui:button-row>
</aui:form>