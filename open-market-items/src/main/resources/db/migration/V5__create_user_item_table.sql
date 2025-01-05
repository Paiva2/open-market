CREATE TABLE IF NOT EXISTS tb_users_items (
    uit_user_id UUID NOT NULL,
    uit_item_id UUID NOT NULL,
    uit_quantity BIGINT NOT NULL DEFAULT 0,
    uit_created_at TIMESTAMP NOT NULL DEFAULT now(),
    uit_updated_at TIMESTAMP NOT NULL DEFAULT now(),

    CONSTRAINT fk_items_ref FOREIGN KEY (uit_item_id) REFERENCES tb_items (itm_id),
    CONSTRAINT fk_users_ref FOREIGN KEY (uit_user_id) REFERENCES tb_users (usr_id)
);