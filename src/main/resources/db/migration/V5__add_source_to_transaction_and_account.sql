CREATE TABLE source
(

    id         UUID PRIMARY KEY      DEFAULT gen_random_uuid(),
    name       VARCHAR(225) NOT NULL,
    account_id UUID         NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (account_id, name),
    CONSTRAINT fk_source_account FOREIGN KEY (account_id)
        REFERENCES account (id) ON DELETE CASCADE
);

ALTER TABLE transaction
    ADD COLUMN IF NOT EXISTS source_id UUID;

ALTER TABLE transaction
    ALTER COLUMN source_id SET NOT NULL;

ALTER TABLE transaction
    ADD CONSTRAINT fk_transaction_source FOREIGN KEY (source_id)
        REFERENCES source (id) ON DELETE RESTRICT;