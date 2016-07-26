create table IF NOT EXISTS users(
  id bigint auto_increment not null primary key,
	username varchar_ignorecase(50) not null,
	password varchar_ignorecase(50) not null,
	enabled boolean not null
);

create table IF NOT EXISTS authority (
	id bigint not null,
	authority varchar_ignorecase(50) not null,
);

-- Join table for users and authorities
CREATE TABLE IF NOT EXISTS user_authority (
  user_id bigint not null,
  authority_id bigint not null,
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (authority_id) REFERENCES authority(id)
);

-- Initial Authorities
INSERT INTO authority (id, authority) values (1, 'ROLE_ADMIN');
INSERT INTO authority (id, authority) values (2, 'ROLE_USER');

-- Default Admin
INSERT INTO users (username, password, enabled) values ('admin', 'password', true);
INSERT INTO user_authority (user_id, authority_id) values(1, 1);

-- Default User
INSERT INTO users (username, password, enabled) values ('user', 'password', true);
INSERT INTO user_authority (user_id, authority_id) values (2, 2);