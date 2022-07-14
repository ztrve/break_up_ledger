-- // init_user_info
-- Migration SQL that makes the change goes here.
create table break_up_ledger.user_info
(
    id          bigserial
        constraint user_info_pk
            primary key,
    unit_code   bigint not null,
    photo       varchar(255),
    nick_name   varchar(50),
    name        varchar(50),
    id_card     varchar(18),
    phone       varchar(50),
    account     varchar(50),
    source      varchar(50),
    create_time timestamp default now(),
    update_time timestamp default now()
);

comment on table break_up_ledger.user_info is '用户表';

comment on column break_up_ledger.user_info.unit_code is '唯一标识符，存储用户权限模块唯一标识';

comment on column break_up_ledger.user_info.photo is '用户头像，图片访问路径';

comment on column break_up_ledger.user_info.nick_name is '用户昵称，前端展示用户昵称，不展示用户名';

comment on column break_up_ledger.user_info.name is '真实姓名';

comment on column break_up_ledger.user_info.id_card is '身份证号';

comment on column break_up_ledger.user_info.phone is '手机号';

comment on column break_up_ledger.user_info.account is '账户(登录账户名)';

comment on column break_up_ledger.user_info.source is '来源';

comment on column break_up_ledger.user_info.create_time is '创建时间';

comment on column break_up_ledger.user_info.update_time is '更新时间';

create unique index user_info_id_uindex
    on break_up_ledger.user_info (id);


-- noinspection SqlResolve

CREATE TRIGGER update_time
    BEFORE UPDATE
    ON break_up_ledger.user_info
    FOR EACH ROW
EXECUTE PROCEDURE break_up_ledger.update_timestamp();

-- //@UNDO
-- SQL to undo the change goes here.

drop table break_up_ledger.user_info;
