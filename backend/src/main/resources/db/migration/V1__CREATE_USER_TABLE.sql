-- Création du type enum pour UserStatus
CREATE TYPE user_status AS ENUM ('ACTIVE', 'INACTIVE', 'DELETED');

-- Création du type enum pour EmployeeRole
CREATE TYPE employee_role AS ENUM ('STORE_MANAGER', 'CASHIER');

-- Table employees
CREATE TABLE employees (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    -- PersonalInfo embedded
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    phone_number VARCHAR(50),
    -- Status et timestamps
    status user_status NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    last_login_at TIMESTAMP,
    -- Champs spécifiques à Employee
    role employee_role NOT NULL,
    hire_date TIMESTAMP,
    termination_date TIMESTAMP
);

-- Table customers
CREATE TABLE customers (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    -- PersonalInfo embedded
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    phone_number VARCHAR(50),
    -- Status et timestamps
    status user_status NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    last_login_at TIMESTAMP
);