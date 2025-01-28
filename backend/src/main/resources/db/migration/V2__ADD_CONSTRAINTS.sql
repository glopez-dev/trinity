ALTER TABLE customer
    ADD CONSTRAINT uk_customer_email UNIQUE (email);

ALTER TABLE customer
    ADD CONSTRAINT uk_customer_paypal_user_id UNIQUE (paypal_user_id);