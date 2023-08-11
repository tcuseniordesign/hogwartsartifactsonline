DROP TABLE artifact IF EXISTS;
DROP TABLE wizard IF EXISTS;
DROP TABLE hogwarts_user IF EXISTS;
DROP SEQUENCE hogwarts_user_seq IF EXISTS;
DROP SEQUENCE wizard_seq IF EXISTS;

create sequence hogwarts_user_seq start with 10 increment by 50;
create sequence wizard_seq start with 10 increment by 50;

CREATE TABLE wizard (
    id INTEGER NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    primary key (id)
);
CREATE INDEX wizard_name ON wizard (name);

CREATE TABLE artifact (
    id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    description  VARCHAR(255) NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    owner_id INTEGER,
    PRIMARY KEY (id),
    FOREIGN KEY (owner_id) REFERENCES wizard (id)
);
CREATE INDEX artifact_name ON artifact (name);

CREATE TABLE hogwarts_user (
    id INTEGER NOT NULL AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL,
    password  VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL,
    roles VARCHAR(255) NOT NULL,
    primary key (id)
);
CREATE INDEX hogwarts_user_username ON hogwarts_user (username);
