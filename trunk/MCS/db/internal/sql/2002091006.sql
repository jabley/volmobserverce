rem ---------------------------------------------------------------------------
rem $Header: /src/voyager/db/internal/sql/2002091006.sql,v 1.2 2002/09/18 14:51:55 ianw Exp $
rem ---------------------------------------------------------------------------
rem (c) Volantis Systems Ltd 2000. 
rem ---------------------------------------------------------------------------
rem Change History:
rem
rem Date         Who             Description
rem ---------    --------------- ----------------------------------------------
rem 17-Sep-02    Ian             VBM:2002091006 - Created this script to update
rem                              jdbc repository with new mariner-focus
rem                              stylistic property.
rem ---------------------------------------------------------------------------

alter table vmlinkproperties  add MRNR_FOCUS_SYNTAX numeric(3) DEFAULT -1 NOT NULL;
alter table vmlinkproperties  add MRNR_FOCUS_PRIORITY numeric(3) DEFAULT 0 NOT NULL;
alter table vmlinkproperties  add MRNR_FOCUS_KEYWD numeric(9) DEFAULT 0 NOT NULL;
commit;

