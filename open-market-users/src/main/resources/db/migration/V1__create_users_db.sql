CREATE TABLE tb_users
(
    usr_id         SERIAL PRIMARY KEY,
    usr_username   VARCHAR(100) NOT NULL,
    usr_email      VARCHAR(250) NOT NULL UNIQUE,
    usr_password   VARCHAR      NOT NULL,
    usr_created_at TIMESTAMP    NOT NULL DEFAULT now(),
    usr_updated_at TIMESTAMP    NOT NULL DEFAULT now()
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