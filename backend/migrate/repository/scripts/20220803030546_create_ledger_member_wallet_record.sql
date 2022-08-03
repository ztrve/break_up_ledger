-- // create_ledger_member_wallet_record
-- Migration SQL that makes the change goes here.
create table break_up_ledger.ledger_member_wallet_record
(
    id                  bigserial,
    ledger_id           bigint                  not null,
    ledger_member_id    bigint                  not null,
    amount              int                     not null,
    tag                 varchar(10)             not null,
    extra               varchar(64),
    prev_wallet_amount  int                     not null,
    after_wallet_amount int                     not null,
    creator_id          bigint                  not null,
    update_time         timestamp default now() not null,
    create_time         timestamp default now() not null
);

comment on table break_up_ledger.ledger_member_wallet_record is '账本成员钱包记录';

comment on column break_up_ledger.ledger_member_wallet_record.ledger_id is '账本id';

comment on column break_up_ledger.ledger_member_wallet_record.ledger_member_id is '账本成员id';

comment on column break_up_ledger.ledger_member_wallet_record.amount is '记账金额 单位分 可正可副';

comment on column break_up_ledger.ledger_member_wallet_record.tag is '标签';

comment on column break_up_ledger.ledger_member_wallet_record.extra is '描述';

comment on column break_up_ledger.ledger_member_wallet_record.prev_wallet_amount is '记账前钱包余额 单位分';

comment on column break_up_ledger.ledger_member_wallet_record.after_wallet_amount is '记账后钱包余额 单位分';

comment on column break_up_ledger.ledger_member_wallet_record.creator_id is '记录人id';

create unique index ledger_member_wallet_record_id_uindex
    on break_up_ledger.ledger_member_wallet_record (id);

alter table break_up_ledger.ledger_member_wallet_record
    add constraint ledger_member_wallet_record_pk
        primary key (id);

CREATE TRIGGER update_time
    BEFORE UPDATE
    ON break_up_ledger.ledger_member_wallet_record
    FOR EACH ROW
EXECUTE PROCEDURE break_up_ledger.update_timestamp();

-- //@UNDO
-- SQL to undo the change goes here.
drop table break_up_ledger.ledger_member_wallet_record;

