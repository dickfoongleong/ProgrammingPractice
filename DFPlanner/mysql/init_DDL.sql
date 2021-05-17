#########################
# Initializing Database #
#########################
DROP DATABASE IF EXISTS dfplanner;
CREATE DATABASE dfplanner;
USE dfplanner;
###################
# Generate tables #
###################
CREATE TABLE user (
    ID BIGINT(20) AUTO_INCREMENT,
    FIRST_NAME VARCHAR(255) NOT NULL,
    LAST_NAME VARCHAR(255) NOT NULL,
    MIDDLE_NAME VARCHAR(1),
    EMAIL VARCHAR(255) NOT NULL UNIQUE,
    USERNAME VARCHAR(255) NOT NULL UNIQUE,
    PASSWORD VARCHAR(255) NOT NULL UNIQUE,
    IS_ENABLED BOOLEAN NOT NULL DEFAULT false,
    PRIMARY KEY (ID)
);
CREATE TABLE task (
    ID BIGINT(20) AUTO_INCREMENT,
    USER_ID BIGINT(20),
    CODE VARCHAR(15) UNIQUE NOT NULL,
    TITLE VARCHAR(255) NOT NULL,
    DUE_DATE DATETIME NOT NULL,
    COLOR VARCHAR(7) DEFAULT '#FFFFFF',
    IS_DONE BOOLEAN NOT NULL DEFAULT false,
    PRIMARY KEY (ID),
    FOREIGN KEY (USER_ID) REFERENCES user (ID)
);
CREATE TABLE subtask (
    ID BIGINT(20) AUTO_INCREMENT,
    TASK_ID BIGINT(20),
    TITLE VARCHAR(255) NOT NULL,
    IS_DONE BOOLEAN NOT NULL DEFAULT false,
    PRIMARY KEY (ID),
    FOREIGN KEY (TASK_ID) REFERENCES task (ID)
);