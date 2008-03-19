rem ---------------------------------------------------------------------------
rem $Header: /src/voyager/db/internal/sql/2002011406.sql,v 1.1 2002/01/25 00:18:58 doug Exp $
rem ---------------------------------------------------------------------------
rem (c) Volantis Systems Ltd 2000. 
rem ---------------------------------------------------------------------------
rem Change History:
rem
rem Date         Who             Description
rem ---------    --------------- ----------------------------------------------
rem 24-Jan-02    Doug            VBM:2002011406 - SQL script to add a new 
rem                              column named locationType to the asset group
rem                              table
rem ---------------------------------------------------------------------------


alter table vmassetgroup add locationType numeric(1);
update vmassetgroup set locationType=0;
alter table vmassetgroup modify (locationType numeric(1) NOT NULL);
commit;
