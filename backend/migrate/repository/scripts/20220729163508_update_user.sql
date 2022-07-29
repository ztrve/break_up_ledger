-- // update_user
-- Migration SQL that makes the change goes here.
alter table break_up_ledger.user_info alter column phone type varchar(20) using phone::varchar(20);



-- //@UNDO
-- SQL to undo the change goes here.
alter table break_up_ledger.user_info alter column phone type varchar(20) using phone::int4;



