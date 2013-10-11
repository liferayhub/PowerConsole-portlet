create table powerconsole_CommandHistory (
	id_ LONG not null primary key,
	mode_ VARCHAR(75) null,
	command VARCHAR(75) null,
	userId LONG,
	executionDate DATE null,
	executionTime LONG
);