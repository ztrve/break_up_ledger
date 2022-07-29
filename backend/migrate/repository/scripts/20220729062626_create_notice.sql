-- // notice
-- Migration SQL that makes the change goes here.
create table break_up_ledger.notice
(
    id bigserial,
    notice_type varchar(64) not null,
    notice_name varchar(64) not null,
    notice_msg varchar(128) not null,
    notice_data text not null,
    initiator_id bigint not null,
    handler_id bigint not null,
    deal_status int4 default 0 not null,
    update_time timestamp default now() not null,
    create_time timestamp default now() not null
);

comment on table break_up_ledger.notice is '通知';

comment on column break_up_ledger.notice.notice_type is '通知类型';

comment on column break_up_ledger.notice.notice_name is '通知名称';

comment on column break_up_ledger.notice.notice_msg is '通知信息';

comment on column break_up_ledger.notice.notice_data is '通知数据，用于通知成功后，反射接口获取数据。不对外展示';

comment on column break_up_ledger.notice.initiator_id is '发起人';

comment on column break_up_ledger.notice.handler_id is '处理人';

comment on column break_up_ledger.notice.deal_status is '处理状态 0未处理 1已处理';

create unique index notice_id_uindex
    on break_up_ledger.notice (id);

alter table break_up_ledger.notice
    add constraint notice_pk
        primary key (id);

CREATE TRIGGER update_time
    BEFORE UPDATE
    ON break_up_ledger.notice
    FOR EACH ROW
EXECUTE PROCEDURE break_up_ledger.update_timestamp();


-- //@UNDO
-- SQL to undo the change goes here.
drop table break_up_ledger.notice;



