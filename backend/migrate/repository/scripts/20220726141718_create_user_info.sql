-- // create_user_info
-- Migration SQL that makes the change goes here.
create table break_up_ledger.user_info
(
    id          bigserial
        constraint user_info_pk
            primary key,
    code        varchar(20)             not null,
    nickname    varchar(10)             not null,
    avatar_url  text                    not null,
    phone       int4,
    wx_open_id  varchar(128)            not null,
    update_time timestamp default now() not null,
    create_time timestamp default now() not null
);

comment on table break_up_ledger.user_info is '用户信息';

create unique index user_info_id_uindex on break_up_ledger.user_info (id);

create index user_info_wx_open_id_index on break_up_ledger.user_info (wx_open_id);
create unique index user_info_wx_open_id_uindex on break_up_ledger.user_info (wx_open_id);

-- //@UNDO
-- SQL to undo the change goes here.
drop table break_up_ledger.user_info;

