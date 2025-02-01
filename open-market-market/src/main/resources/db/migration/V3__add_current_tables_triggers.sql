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

CREATE FUNCTION update_updated_at_tb_items_categories()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.ict_updated_at = now();
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_items_categories_task_updated_on
    BEFORE UPDATE
    ON
        tb_items_categories
    FOR EACH ROW
EXECUTE PROCEDURE update_updated_at_tb_items_categories();

--

CREATE FUNCTION update_updated_at_tb_users_items()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.uit_updated_at = now();
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_users_items_task_updated_on
    BEFORE UPDATE
    ON
        tb_users_items
    FOR EACH ROW
EXECUTE PROCEDURE update_updated_at_tb_users_items();

--

CREATE FUNCTION update_updated_at_tb_items_sales()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.isl_updated_at = now();
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_items_sales_task_updated_on
    BEFORE UPDATE
    ON
        tb_items_sales
    FOR EACH ROW
EXECUTE PROCEDURE update_updated_at_tb_items_sales();

--

CREATE FUNCTION update_updated_at_tb_offers()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.ofr_updated_at = now();
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_offers_task_updated_on
    BEFORE UPDATE
    ON
        tb_offers
    FOR EACH ROW
EXECUTE PROCEDURE update_updated_at_tb_offers();

--

CREATE FUNCTION update_updated_at_tb_offers_user_item()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.oui_updated_at = now();
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_offers_user_item_task_updated_on
    BEFORE UPDATE
    ON
        tb_offers_user_item
    FOR EACH ROW
EXECUTE PROCEDURE update_updated_at_tb_offers_user_item();

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