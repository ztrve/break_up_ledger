-- // update_user_ledger_tag
-- Migration SQL that makes the change goes here.
truncate table break_up_ledger.user_ledger_tag;

alter table break_up_ledger.user_ledger_tag drop column tag;
alter table break_up_ledger.user_ledger_tag add ledger_tag_id bigint not null;

comment on column break_up_ledger.user_ledger_tag.ledger_tag_id is '标签id';



-- //@UNDO
-- SQL to undo the change goes here.
alter table break_up_ledger.user_ledger_tag add tag varchar(10) default '' not null;
alter table break_up_ledger.user_ledger_tag alter column tag drop default;

alter table break_up_ledger.user_ledger_tag drop column ledger_tag_id;

