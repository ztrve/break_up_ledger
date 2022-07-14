-- // init_schema
-- Migration SQL that makes the change goes here.
-- 更新时间戳的触发器
CREATE OR REPLACE FUNCTION break_up_ledger.update_timestamp()
    RETURNS TRIGGER AS
$$
BEGIN
    -- noinspection SqlResolve
    NEW.update_time = current_timestamp;
    RETURN NEW;
END;
$$ language plpgsql;

-- //@UNDO
-- SQL to undo the change goes here.
-- DROP SCHEMA ${schema};
DROP FUNCTION break_up_ledger.update_timestamp();


