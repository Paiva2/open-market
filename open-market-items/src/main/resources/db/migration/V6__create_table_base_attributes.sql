CREATE TABLE tb_base_attributes (
    bat_id UUID primary key,
    bat_attributes JSONB DEFAULT NULL,
    bat_created_at TIMESTAMP NOT NULL DEFAULT now(),
    bat_updated_at TIMESTAMP NOT NULL DEFAULT now(),
    bat_item_id UUID NOT NULL UNIQUE,

    CONSTRAINT fk_base_attribute_item_ref FOREIGN KEY (bat_item_id) REFERENCES tb_items (itm_id)
);

CREATE FUNCTION update_updated_at_tb_base_attributes()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.bat_updated_at = now();
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_tb_base_attributes_task_updated_on
    BEFORE UPDATE
    ON
        tb_base_attributes
    FOR EACH ROW
EXECUTE PROCEDURE update_updated_at_tb_base_attributes();

--