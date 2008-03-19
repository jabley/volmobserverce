rem ---------------------------------------------------------------------------
rem $Header: /src/voyager/db/internal/sql/2002020505-1.sql,v 1.1 2002/02/19 11:26:55 payal Exp $
rem ---------------------------------------------------------------------------
rem (c) Volantis Systems Ltd 2000. 
rem ---------------------------------------------------------------------------
rem Change History:
rem
rem Date         Who             Description
rem ---------    --------------- ----------------------------------------------
rem 19-Feb-02    Payal            VBM:2002020505 - Created.
rem ---------------------------------------------------------------------------


update vmpropertyeditors set value='select' where  grp='stylePolicies' and property='font-family' and attribute='type';
 
commit;
