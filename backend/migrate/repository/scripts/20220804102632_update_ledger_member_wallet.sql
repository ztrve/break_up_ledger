-- // update_ledger_member_wallet
-- Migration SQL that makes the change goes here.
alter table break_up_ledger.ledger_member_wallet_record
    add ledger_record_id bigint default 0 not null;

comment on column break_up_ledger.ledger_member_wallet_record.ledger_record_id is '账单记录id';

alter table break_up_ledger.ledger_member_wallet_record alter column ledger_record_id drop default;



-- //@UNDO
-- SQL to undo the change goes here.
alter table break_up_ledger.ledger_member_wallet_record drop column ledger_record_id;



