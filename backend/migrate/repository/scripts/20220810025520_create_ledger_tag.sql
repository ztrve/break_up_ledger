-- // create_ledger_tag
-- Migration SQL that makes the change goes here.
create table break_up_ledger.ledger_tag
(
    id          bigserial,
    tag         varchar(10)             not null,
    update_time timestamp default now() not null,
    create_time timestamp default now() not null
);

comment on table break_up_ledger.ledger_tag is '账本标签库';

comment on column break_up_ledger.ledger_tag.tag is '标签';

create unique index ledger_tag_id_uindex
    on break_up_ledger.ledger_tag (id);

alter table break_up_ledger.ledger_tag
    add constraint ledger_tag_pk
        primary key (id);

CREATE TRIGGER update_time
    BEFORE UPDATE
    ON break_up_ledger.ledger_tag
    FOR EACH ROW
EXECUTE PROCEDURE break_up_ledger.update_timestamp();

INSERT INTO break_up_ledger.ledger_tag (id, tag, update_time, create_time) VALUES (1, '买菜', '2022-08-10 03:10:40.719786', '2022-08-10 03:10:40.719786');
INSERT INTO break_up_ledger.ledger_tag (id, tag, update_time, create_time) VALUES (2, '水果', '2022-08-10 03:10:40.719786', '2022-08-10 03:10:40.719786');
INSERT INTO break_up_ledger.ledger_tag (id, tag, update_time, create_time) VALUES (3, '超市', '2022-08-10 03:10:40.719786', '2022-08-10 03:10:40.719786');
INSERT INTO break_up_ledger.ledger_tag (id, tag, update_time, create_time) VALUES (4, '吃饭', '2022-08-10 03:10:40.719786', '2022-08-10 03:10:40.719786');
INSERT INTO break_up_ledger.ledger_tag (id, tag, update_time, create_time) VALUES (5, '奶茶', '2022-08-10 03:10:40.719786', '2022-08-10 03:10:40.719786');
INSERT INTO break_up_ledger.ledger_tag (id, tag, update_time, create_time) VALUES (6, '日用品', '2022-08-10 03:10:40.719786', '2022-08-10 03:10:40.719786');
INSERT INTO break_up_ledger.ledger_tag (id, tag, update_time, create_time) VALUES (7, '维修费', '2022-08-10 03:10:40.719786', '2022-08-10 03:10:40.719786');
INSERT INTO break_up_ledger.ledger_tag (id, tag, update_time, create_time) VALUES (8, '水电煤气', '2022-08-10 03:10:40.719786', '2022-08-10 03:10:40.719786');
INSERT INTO break_up_ledger.ledger_tag (id, tag, update_time, create_time) VALUES (9, '宠物用品', '2022-08-10 03:10:40.719786', '2022-08-10 03:10:40.719786');
INSERT INTO break_up_ledger.ledger_tag (id, tag, update_time, create_time) VALUES (10, '服装', '2022-08-10 03:10:40.719786', '2022-08-10 03:10:40.719786');

-- //@UNDO
-- SQL to undo the change goes here.
drop table break_up_ledger.ledger_tag;

