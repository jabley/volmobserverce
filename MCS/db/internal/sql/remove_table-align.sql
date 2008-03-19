rem ---------------------------------------------------------------------------
rem $Header: /src/voyager/db/internal/sql/remove_table-align.sql,v 1.2 2002/02/28 16:01:32 aboyd Exp $
rem ---------------------------------------------------------------------------
rem (c) Volantis Systems Ltd 2000. 
rem ---------------------------------------------------------------------------
rem Change History:
rem
rem Date         Who             Description
rem ---------    --------------- ----------------------------------------------
rem 27-Feb-02    Allan           VBM:2002022701 - Remove the table-align 
rem                              property from the vmpropertyeditors table.
rem ---------------------------------------------------------------------------
delete from vmpropertyeditors where property = 'table-align';
commit;

