-- // create_user_ledger_tag
-- Migration SQL that makes the change goes here.
create table break_up_ledger.user_ledger_tag
(
    id          bigserial,
    user_id     bigint                  not null,
    tag         varchar(10)             not null,
    is_default_tag boolean   default false not null,
    update_time timestamp default now() not null,
    create_time timestamp default now() not null
);

comment on table break_up_ledger.user_ledger_tag is '用户账本标签库';

comment on column break_up_ledger.user_ledger_tag.user_id is '用户id';

comment on column break_up_ledger.user_ledger_tag.tag is '标签';

comment on column break_up_ledger.user_ledger_tag.is_default_tag is '是默认标签';

create unique index user_ledger_tag_id_uindex
    on break_up_ledger.user_ledger_tag (id);

alter table break_up_ledger.user_ledger_tag
    add constraint user_ledger_tag_pk
        primary key (id);

CREATE TRIGGER update_time
    BEFORE UPDATE
    ON break_up_ledger.user_ledger_tag
    FOR EACH ROW
EXECUTE PROCEDURE break_up_ledger.update_timestamp();


-- //@UNDO
-- SQL to undo the change goes here.
drop table break_up_ledger.user_ledger_tag;




