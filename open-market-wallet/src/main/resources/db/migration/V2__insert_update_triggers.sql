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

CREATE FUNCTION update_updated_at_tb_wallets()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.wlt_updated_at = now();
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_wallets_task_updated_on
    BEFORE UPDATE
    ON
        tb_wallets
    FOR EACH ROW
EXECUTE PROCEDURE update_updated_at_tb_wallets();

--

CREATE FUNCTION update_updated_at_tb_wallets_ledgers()
    RETURNS TRIGGER AS
$$
BEGIN
    NEW.wlg_updated_at = now();
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_wallets_ledgers_task_updated_on
    BEFORE UPDATE
    ON
        tb_wallets_ledgers
    FOR EACH ROW
EXECUTE PROCEDURE update_updated_at_tb_wallets_ledgers();

--