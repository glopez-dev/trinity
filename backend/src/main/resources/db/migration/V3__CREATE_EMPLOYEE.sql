CREATE TABLE employee
(
    id               UUID PRIMARY KEY,
    email            VARCHAR(255) NOT NULL,
    first_name       VARCHAR(255),
    last_name        VARCHAR(255),
    hashed_password  VARCHAR(255),
    hire_date        TIMESTAMP,
    last_login_at    TIMESTAMP    NOT NULL,
    termination_date TIMESTAMP,
    role             VARCHAR(50) DEFAULT 'EMPLOYEE',
    status           VARCHAR(50) DEFAULT 'ACTIVE',
    created_at       TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    version          INTEGER     DEFAULT 0
);