CREATE TABLE credentials
(
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_id UUID UNIQUE NOT NULL,
    login      VARCHAR(50) NOT NULL,
    password   VARCHAR(50) NOT NULL,
    created_at TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

ALTER TABLE credentials
    ADD CONSTRAINT credentials_account_id_fkey FOREIGN KEY
        (account_id) REFERENCES account (id) ON DELETE CASCADE;

