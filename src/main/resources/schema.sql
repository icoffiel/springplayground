-- TODO - Move into liquibase or flyway
create table USERS(
	username varchar_ignorecase(50) not null primary key,
	password varchar_ignorecase(50) not null,
	enabled boolean not null
);

create table AUTHORITIES (
	username varchar_ignorecase(50) not null,
	authority varchar_ignorecase(50) not null,
	constraint fk_authorities_users foreign key(username) references USERS(username)
);
create unique index ix_auth_username on AUTHORITIES (username,authority);