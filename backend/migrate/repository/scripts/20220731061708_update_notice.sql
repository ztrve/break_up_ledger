-- // update_notice
-- Migration SQL that makes the change goes here.
alter table break_up_ledger.notice add deal_result int4 default 0;

comment on column break_up_ledger.notice.deal_result is '处理结果 0拒绝 1同意';

-- //@UNDO
-- SQL to undo the change goes here.
alter table break_up_ledger.notice drop column deal_result;
