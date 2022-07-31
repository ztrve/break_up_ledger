-- // ledger_member
-- Migration SQL that makes the change goes here.
create table break_up_ledger.ledger_member
(
    id          bigserial
        constraint ledger_member_pk
            primary key,
    ledger_id   bigint                  not null,
    member_id   bigint                  not null,
    update_time timestamp default now() not null,
    create_time timestamp default now() not null
);

comment on table break_up_ledger.ledger_member is '账本成员';

comment on column break_up_ledger.ledger_member.ledger_id is '账本id';

comment on column break_up_ledger.ledger_member.member_id is '成员用户id';

create unique index ledger_member_id_uindex
    on break_up_ledger.ledger_member (id);

CREATE TRIGGER update_time
    BEFORE UPDATE
    ON break_up_ledger.ledger_member
    FOR EACH ROW
EXECUTE PROCEDURE break_up_ledger.update_timestamp();

-- make index
alter table break_up_ledger.ledger_member
    add constraint ledger_member_pk_2 unique (ledger_id);



-- //@UNDO
-- SQL to undo the change goes here.
drop table break_up_ledger.ledger_member;

