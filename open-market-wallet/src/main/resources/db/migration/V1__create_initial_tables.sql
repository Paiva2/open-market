CREATE TABLE IF NOT EXISTS tb_users
(
    usr_id          uuid PRIMARY KEY,
    usr_external_id VARCHAR(250) UNIQUE NOT NULL,
    usr_username    VARCHAR(250)        NOT NULL,
    usr_email       VARCHAR(250) UNIQUE NOT NULL,
    usr_created_at  TIMESTAMP           NOT NULL DEFAULT now(),
    usr_updated_at  TIMESTAMP           NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS tb_wallets
(
    wlt_id     uuid PRIMARY KEY,
    wlt_created_at  TIMESTAMP NOT NULL DEFAULT now(),
    wlt_updated_at  TIMESTAMP NOT NULL DEFAULT now(),

    wlt_user_id uuid NOT NULL,
    CONSTRAINT fk_users_ref FOREIGN KEY (wlt_user_id) REFERENCES tb_users (usr_id)
);

CREATE TABLE IF NOT EXISTS tb_wallets_ledgers
(
    wlg_id     uuid PRIMARY KEY,
    wlg_transaction_type TEXT CHECK (wlg_transaction_type IN ('WITHDRAWAL', 'DEPOSIT', 'TRANSFER', 'PAYMENT')) NOT NULL,
    wlg_value BIGINT NOT NULL,
    wlg_created_at  TIMESTAMP NOT NULL DEFAULT now(),
    wlg_updated_at  TIMESTAMP NOT NULL DEFAULT now(),

    wlg_wallet_id uuid NOT NULL,
    CONSTRAINT fk_wallets_ref FOREIGN KEY (wlg_wallet_id) REFERENCES tb_wallets (wlt_id)
);