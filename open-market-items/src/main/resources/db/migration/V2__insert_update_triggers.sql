CREATE FUNCTION update_updated_at_tb_users()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.usr_updated_at = now();
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_users_task_updated_on
    BEFORE UPDATE
    ON
        tb_users
    FOR EACH ROW
EXECUTE PROCEDURE update_updated_at_tb_users();

--

CREATE FUNCTION update_updated_at_tb_items()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.itm_updated_at = now();
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_items_task_updated_on
    BEFORE UPDATE
    ON
        tb_items
    FOR EACH ROW
EXECUTE PROCEDURE update_updated_at_tb_items();

--

CREATE FUNCTION update_updated_at_tb_categories()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.cat_updated_at = now();
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_categories_task_updated_on
    BEFORE UPDATE
    ON
        tb_categories
    FOR EACH ROW
EXECUTE PROCEDURE update_updated_at_tb_categories();

--

CREATE FUNCTION update_updated_at_tb_items_alterations()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.ial_updated_at = now();
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_items_alterations_task_updated_on
    BEFORE UPDATE
    ON
        tb_items_alterations
    FOR EACH ROW
EXECUTE PROCEDURE update_updated_at_tb_items_alterations();

--

CREATE FUNCTION update_updated_at_tb_items_categories()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.ict_updated_at = now();
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_tb_items_categories_task_updated_on
    BEFORE UPDATE
    ON
        tb_items_categories
    FOR EACH ROW
EXECUTE PROCEDURE update_updated_at_tb_items_categories();

--


CREATE FUNCTION update_updated_at_tb_attribute_item()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.aui_updated_at = now();
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_tb_attribute_item_task_updated_on
    BEFORE UPDATE
    ON
        tb_attributes_item
    FOR EACH ROW
EXECUTE PROCEDURE update_updated_at_tb_attribute_item();

--