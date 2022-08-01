-- // update_ledger_member
-- Migration SQL that makes the change goes here.
drop index break_up_ledger.ledger_member_ledger_id_uindex;

create index ledger_member_ledger_id_index on break_up_ledger.ledger_member (ledger_id);


-- //@UNDO
-- SQL to undo the change goes here.
drop index break_up_ledger.ledger_member_ledger_id_index;

create unique index ledger_member_ledger_id_uindex on break_up_ledger.ledger_member (ledger_id);



