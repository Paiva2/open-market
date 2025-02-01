CREATE TABLE IF NOT EXISTS tb_users
(
    usr_id          uuid PRIMARY KEY,
    usr_external_id VARCHAR(250) UNIQUE NOT NULL,
    usr_username    VARCHAR(250)        NOT NULL,
    usr_email       VARCHAR(250) UNIQUE NOT NULL,
    usr_created_at  TIMESTAMP           NOT NULL DEFAULT now(),
    usr_updated_at  TIMESTAMP           NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS tb_items
(
    itm_id                 uuid PRIMARY KEY,
    itm_name               VARCHAR(250) UNIQUE NOT NULL,
    itm_description        VARCHAR(300)        NOT NULL,
    itm_photo_url          VARCHAR(500)        NOT NULL,
    itm_unique             BOOLEAN             NOT NULL DEFAULT FALSE,
    itm_base_selling_price NUMERIC(12, 2)      NOT NULL DEFAULT 0,
    itm_active             BOOLEAN             NOT NULL DEFAULT TRUE,
    itm_stackable          BOOLEAN             NOT NULL DEFAULT TRUE,
    itm_created_at         TIMESTAMP           NOT NULL DEFAULT now(),
    itm_updated_at         TIMESTAMP           NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS tb_categories
(
    cat_id         SERIAL PRIMARY KEY,
    cat_name       VARCHAR(50) UNIQUE NOT NULL,
    cat_created_at TIMESTAMP          NOT NULL DEFAULT now(),
    cat_updated_at TIMESTAMP          NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS tb_items_alterations
(
    ial_id         SERIAL PRIMARY KEY,
    ial_action     TEXT CHECK (ial_action IN ('CREATION', 'UPDATE', 'DELETE')) NOT NULL,
    ial_created_at TIMESTAMP                                                   NOT NULL DEFAULT now(),
    ial_updated_at TIMESTAMP                                                   NOT NULL DEFAULT now(),
    ial_user_id    UUID,
    ial_item_id    UUID,

    CONSTRAINT fk_users_ref FOREIGN KEY (ial_user_id) REFERENCES tb_users (usr_id),
    CONSTRAINT fk_items_ref FOREIGN KEY (ial_item_id) REFERENCES tb_items (itm_id)
);

CREATE TABLE IF NOT EXISTS tb_items_categories
(
    ict_item_id     uuid,
    ict_category_id SERIAL,
    ict_created_at  TIMESTAMP NOT NULL DEFAULT now(),
    ict_updated_at  TIMESTAMP NOT NULL DEFAULT now(),

    CONSTRAINT fk_items_ref FOREIGN KEY (ict_item_id) REFERENCES tb_items (itm_id),
    CONSTRAINT fk_users_ref FOREIGN KEY (ict_category_id) REFERENCES tb_categories (cat_id)
);