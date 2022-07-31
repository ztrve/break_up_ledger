-- // ledger
-- Migration SQL that makes the change goes here.
create table break_up_ledger.ledger
(
    id                bigserial
        constraint ledger_pk
            primary key,
    name              varchar(10) not null,
    type              int4    default 0,
    owner_id          bigint      not null,
    leader_id         bigint      not null,
    can_member_commit boolean default true,
    update_time timestamp default now() not null,
    create_time timestamp default now() not null
);

comment on table break_up_ledger.ledger is '账本';

comment on column break_up_ledger.ledger.name is '账本名';

comment on column break_up_ledger.ledger.type is '账本类型 0普通账本';

comment on column break_up_ledger.ledger.owner_id is '账本拥有者';

comment on column break_up_ledger.ledger.leader_id is '账本实际管理人';

comment on column break_up_ledger.ledger.can_member_commit is '成员可以提交 TRUE可以 FALSE不可以';

create unique index ledger_id_uindex
    on break_up_ledger.ledger (id);

CREATE TRIGGER update_time
    BEFORE UPDATE
    ON break_up_ledger.ledger
    FOR EACH ROW
EXECUTE PROCEDURE break_up_ledger.update_timestamp();

-- //@UNDO
-- SQL to undo the change goes here.
drop table break_up_ledger.ledger;

