rem ---------------------------------------------------------------------------
rem $Header: /src/voyager/db/internal/sql/2002051305.sql,v 1.1 2002/05/14 14:18:07 doug Exp $
rem ---------------------------------------------------------------------------
rem (c) Volantis Systems Ltd 2000. 
rem ---------------------------------------------------------------------------
rem Change History:
rem
rem Date         Who             Description
rem ---------    --------------- ----------------------------------------------
rem 14-May-02    Doug            VBM:2002051305 - SQL script to add columns to 
rem                              the vmfontproperties table in order to support
rem                              the mariner-system-font style.
rem ---------------------------------------------------------------------------

alter table vmfontproperties add MRNR_SYS_FNT_SYNTAX numeric(3) DEFAULT -1 NOT NULL;

alter table vmfontproperties add MRNR_SYS_FNT_PRIORITY numeric(3) DEFAULT 0 NOT NULL;

alter table vmfontproperties add MRNR_SYS_FNT_KEYWD numeric(3) DEFAULT 0 NOT NULL;


commit;
