CREATE TABLE IF NOT EXISTS tb_items (
    itm_id UUID PRIMARY KEY,
    itm_external_id VARCHAR(250) UNIQUE DEFAULT NULL,
    itm_name VARCHAR(250) UNIQUE NOT NULL,
    itm_description VARCHAR(300) NOT NULL,
    itm_photo_url VARCHAR(500) NOT NULL,
    itm_unique BOOLEAN NOT NULL DEFAULT FALSE,
    itm_base_selling_price NUMERIC(12, 2) NOT NULL DEFAULT 0,
    itm_active BOOLEAN NOT NULL DEFAULT TRUE,
    itm_created_at TIMESTAMP NOT NULL DEFAULT now(),
    itm_updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS tb_categories (
    cat_id SERIAL PRIMARY KEY,
    cat_external_id VARCHAR(250) UNIQUE DEFAULT NULL,
    cat_name VARCHAR(50) UNIQUE NOT NULL,
    cat_created_at TIMESTAMP NOT NULL DEFAULT now(),
    cat_updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS tb_items_categories (
    ict_item_id uuid,
    ict_category_id SERIAL,
    ict_created_at TIMESTAMP NOT NULL DEFAULT now(),
    ict_updated_at TIMESTAMP NOT NULL DEFAULT now(),

    CONSTRAINT fk_items_ref FOREIGN KEY (ict_item_id) REFERENCES tb_items (itm_id),
    CONSTRAINT fk_users_ref FOREIGN KEY (ict_category_id) REFERENCES tb_categories (cat_id)
);

CREATE TABLE IF NOT EXISTS tb_users_items (
    uit_user_id UUID NOT NULL,
    uit_item_id UUID NOT NULL,
    uit_quantity BIGINT NOT NULL DEFAULT 0,
    uit_created_at TIMESTAMP NOT NULL DEFAULT now(),
    uit_updated_at TIMESTAMP NOT NULL DEFAULT now(),

    CONSTRAINT fk_items_ref FOREIGN KEY (uit_item_id) REFERENCES tb_items (itm_id),
    CONSTRAINT fk_users_ref FOREIGN KEY (uit_user_id) REFERENCES tb_users (usr_id)
);

CREATE TABLE IF NOT EXISTS tb_items_sales (
    isl_id UUID PRIMARY KEY,
    isl_item_id UUID NOT NULL,
    isl_user_id UUID NOT NULL,
    isl_quantity BIGINT NOT NULL DEFAULT 1,
    isl_value NUMERIC(12, 2) NOT NULL,
    isl_expiration_date TIMESTAMP NOT NULL,
    isl_accept_offers BOOLEAN NOT NULL DEFAULT FALSE,
    isl_only_offers BOOLEAN NOT NULL DEFAULT FALSE,
    isl_created_at TIMESTAMP NOT NULL DEFAULT now(),
    isl_updated_at TIMESTAMP NOT NULL DEFAULT now(),

    CONSTRAINT fk_items_ref FOREIGN KEY (isl_item_id) REFERENCES tb_items (itm_id),
    CONSTRAINT fk_users_ref FOREIGN KEY (isl_user_id) REFERENCES tb_users (usr_id)
);

CREATE TABLE IF NOT EXISTS tb_offers (
    ofr_id UUID PRIMARY KEY,
    ofr_user_id UUID NOT NULL,
    ofr_value NUMERIC(12, 2) NOT NULL,
    ofr_item_sale_id UUID NOT NULL,
    ofr_created_at TIMESTAMP NOT NULL DEFAULT now(),
    ofr_updated_at TIMESTAMP NULL DEFAULT now(),

    CONSTRAINT fk_users_ref FOREIGN KEY (ofr_user_id) REFERENCES tb_users (usr_id),
    CONSTRAINT fk_items_sales_ref FOREIGN KEY (ofr_item_sale_id) REFERENCES tb_items_sales (isl_id)
);

CREATE TABLE IF NOT EXISTS tb_offers_user_item (
    oui_user_id UUID NOT NULL,
    oui_item_id UUID NOT NULL,
    oui_offer_id UUID NOT NULL,
    uit_quantity BIGINT NOT NULL DEFAULT 1,
    oui_created_at TIMESTAMP NOT NULL DEFAULT now(),
    oui_updated_at TIMESTAMP NOT NULL DEFAULT now(),

    CONSTRAINT fk_users_ref FOREIGN KEY (oui_user_id) REFERENCES tb_users (usr_id),
    CONSTRAINT fk_items_ref FOREIGN KEY (oui_item_id) REFERENCES tb_items (itm_id),
    CONSTRAINT fk_offers_ref FOREIGN KEY (oui_offer_id) REFERENCES tb_offers (ofr_id)
);