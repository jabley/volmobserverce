-- (c) Volantis Systems Ltd 2003 
-- ---------------------------------------------------------------------------
-- Oracle script for creating new user 
--
-- You may need to perform a COMMIT transaction after executing this script.
-- ---------------------------------------------------------------------------

create user database-user identified by database-password default tablespace users temporary tablespace temp;

grant unlimited tablespace to database-user;

create role voladmin;

grant create session to voladmin;

grant create table to voladmin;

grant create sequence to voladmin;

grant exp_full_database to voladmin;

grant imp_full_database to voladmin;

grant connect to voladmin;

grant voladmin to database-user;

