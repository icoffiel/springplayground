create table IF NOT EXISTS USERS(
	username varchar_ignorecase(50) not null primary key,
	password varchar_ignorecase(50) not null,
	enabled boolean not null
);

create table IF NOT EXISTS AUTHORITIES (
	username varchar_ignorecase(50) not null,
	authority varchar_ignorecase(50) not null,
	constraint fk_authorities_users foreign key(username) references USERS(username)
);
create unique index ix_auth_username on AUTHORITIES (username,authority);

-- Default Admin
insert into users (username, password, enabled) values ('admin', 'password', true);
insert into authorities (username, authority) values ('admin', 'ROLE_USER');

-- Default User
insert into users (username, password, enabled) values ('user', 'password', true);
insert into authorities (username, authority) values ('user', 'ROLE_USER');