CREATE TABLE IF NOT EXISTS tb_attributes_item (
      aui_id UUID PRIMARY KEY UNIQUE,
      aui_attributes JSONB DEFAULT NULL,
      aui_created_at TIMESTAMP NOT NULL DEFAULT now(),
      aui_updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS tb_users_items (
    uit_user_id UUID NOT NULL,
    uit_item_id UUID NOT NULL,
    uit_item_attribute_id UUID NOT NULL UNIQUE,
    uit_quantity BIGINT NOT NULL DEFAULT 0,
    uit_created_at TIMESTAMP NOT NULL DEFAULT now(),
    uit_updated_at TIMESTAMP NOT NULL DEFAULT now(),

    CONSTRAINT fk_items_ref FOREIGN KEY (uit_item_id) REFERENCES tb_items (itm_id),
    CONSTRAINT fk_users_ref FOREIGN KEY (uit_user_id) REFERENCES tb_users (usr_id),
    CONSTRAINT fk_attributes_ref FOREIGN KEY (uit_item_attribute_id) REFERENCES tb_attributes_item (aui_id),
    CONSTRAINT pk_users_items PRIMARY KEY (uit_user_id, uit_item_id, uit_item_attribute_id)
);

CREATE FUNCTION update_updated_at_tb_user_item()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.uit_updated_at = now();
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_tb_user_item_task_updated_on
    BEFORE UPDATE
    ON
        tb_users_items
    FOR EACH ROW
EXECUTE PROCEDURE update_updated_at_tb_user_item();

--