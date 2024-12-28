CREATE TABLE IF NOT EXISTS tb_users
(
    usr_id          uuid PRIMARY KEY,
    usr_external_id VARCHAR(250) UNIQUE NOT NULL,
    usr_username    VARCHAR(250)        NOT NULL,
    usr_email       VARCHAR(250) UNIQUE NOT NULL,
    usr_enabled     BOOLEAN NOT NULL DEFAULT TRUE,
    usr_created_at  TIMESTAMP           NOT NULL DEFAULT now(),
    usr_updated_at  TIMESTAMP           NOT NULL DEFAULT now()
);

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