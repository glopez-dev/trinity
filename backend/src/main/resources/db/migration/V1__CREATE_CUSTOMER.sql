CREATE TABLE customer
(
    id                   UUID PRIMARY KEY,
    email                VARCHAR(255) NOT NULL,
    first_name           VARCHAR(255),
    last_name            VARCHAR(255),
    hashed_password      VARCHAR(255),
    last_login_at        TIMESTAMP    NOT NULL,
    role                 VARCHAR(50) DEFAULT 'CUSTOMER',
    status               VARCHAR(50) DEFAULT 'ACTIVE',
    paypal_user_id       VARCHAR(255),
    paypal_access_token  TEXT,
    paypal_refresh_token TEXT,
    token_expires_at     TIMESTAMP,
    created_at           TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    updated_at           TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    version              INTEGER     DEFAULT 0
);

-- Create indexes for improved performance
CREATE INDEX idx_customer_email ON customer (email);
CREATE INDEX idx_customer_paypal_user_id ON customer (paypal_user_id);