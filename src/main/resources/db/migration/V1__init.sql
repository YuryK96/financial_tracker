CREATE TABLE account
(
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name       VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE category
(
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name       VARCHAR(50) NOT NULL,
    account_id UUID        NOT NULL,
    created_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (name, account_id),
    CONSTRAINT fk_category_account FOREIGN KEY (account_id)
        REFERENCES account (id) ON DELETE CASCADE
);


CREATE TABLE subcategory
(
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(50) NOT NULL,
    category_id UUID        NOT NULL,
    created_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_subcategory_category FOREIGN KEY (category_id)
        REFERENCES category (id) ON DELETE CASCADE
);


CREATE TABLE transaction
(
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    amount         DECIMAL(19, 2) NOT NULL,
    currency       VARCHAR(3)     NOT NULL,
    type           VARCHAR(10)    NOT NULL,
    account_id     UUID           NOT NULL,
    subcategory_id UUID           NOT NULL,
    created_at     TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_transaction_account FOREIGN KEY (account_id)
        REFERENCES account (id) ON DELETE CASCADE,
    CONSTRAINT fk_transaction_subcategory FOREIGN KEY (subcategory_id)
        REFERENCES subcategory (id) ON DELETE CASCADE
);


