-- // create_ledger_member_wallet
-- Migration SQL that makes the change goes here.
alter table break_up_ledger.ledger_member
    add wallet_amount int default 0 not null;

comment on column break_up_ledger.ledger_member.wallet_amount is '钱包余额 单位分';


-- //@UNDO
-- SQL to undo the change goes here.
alter table break_up_ledger.ledger_member drop column wallet_amount;

