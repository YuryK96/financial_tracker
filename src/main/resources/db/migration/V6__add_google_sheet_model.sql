CREATE TABLE google_tokens
(
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_id     UUID NOT NULL UNIQUE REFERENCES account (id) ON DELETE CASCADE,
    access_token   VARCHAR(2000),
    refresh_token  VARCHAR(500),
    token_expiry   TIMESTAMP,
    spreadsheet_id VARCHAR(255),
    created_at     TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_google_tokens_account_id ON google_tokens (account_id);