-- Table pour les adresses des clients
CREATE TABLE customer_addresses
(
    customer_id UUID         NOT NULL,
    -- Champs de l'Address value object
    street      VARCHAR(255) NOT NULL,
    city        VARCHAR(100) NOT NULL,
    zip_code    VARCHAR(20)  NOT NULL,
    country     VARCHAR(100) NOT NULL,
    is_default  BOOLEAN DEFAULT false,
    -- Contrainte de clé étrangère
    CONSTRAINT fk_customer_addresses
        FOREIGN KEY (customer_id)
            REFERENCES customers (id)
            ON DELETE CASCADE
);

