Liferay.Service.register("Liferay.Service.powerconsole", "org.liferayhub.pc.service", "PowerConsole-portlet");

Liferay.Service.registerClass(
	Liferay.Service.powerconsole, "PowerConsole",
	{
		runCommand: true,
		getHistories: true
	}
);