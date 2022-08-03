-- // create_ledger_record
-- Migration SQL that makes the change goes here.
create table break_up_ledger.ledger_record
(
    id          bigserial,
    ledger_id   bigint                  not null,
    amount      integer                 not null,
    tag         varchar(10)             not null,
    extra       varchar(128),
    creator_id  bigint                  not null,
    update_time timestamp default now() not null,
    create_time timestamp default now() not null
);

comment on column break_up_ledger.ledger_record.ledger_id is '账本';

comment on column break_up_ledger.ledger_record.amount is '金额 单位分 可为正数或者负数';

comment on column break_up_ledger.ledger_record.tag is '标签';

comment on column break_up_ledger.ledger_record.extra is '描述';

create unique index ledger_record_id_uindex on break_up_ledger.ledger_record (id);

alter table break_up_ledger.ledger_record
    add constraint ledger_record_pk primary key (id);

CREATE TRIGGER update_time
    BEFORE UPDATE
    ON break_up_ledger.ledger_record
    FOR EACH ROW
EXECUTE PROCEDURE break_up_ledger.update_timestamp();

-- //@UNDO
-- SQL to undo the change goes here.
drop table break_up_ledger.ledger_record;


