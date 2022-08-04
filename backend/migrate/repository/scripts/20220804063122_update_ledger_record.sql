-- // update_ledger_record
-- Migration SQL that makes the change goes here.
alter table break_up_ledger.ledger_record
    add prev_wallet_amount int default 0 not null;

comment on column break_up_ledger.ledger_record.prev_wallet_amount is '结算前共同账户余额 单位分';

alter table break_up_ledger.ledger_record
    add after_wallet_amount int default 0 not null;

comment on column break_up_ledger.ledger_record.after_wallet_amount is '结算后共同账户余额 单位分';

alter table break_up_ledger.ledger_record alter column prev_wallet_amount drop default;

alter table break_up_ledger.ledger_record alter column after_wallet_amount drop default;


-- //@UNDO
-- SQL to undo the change goes here.
alter table break_up_ledger.ledger_record drop column prev_wallet_amount;

alter table break_up_ledger.ledger_record drop column after_wallet_amount;



