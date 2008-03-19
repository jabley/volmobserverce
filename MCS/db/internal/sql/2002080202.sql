rem ---------------------------------------------------------------------------
rem $Header: /src/voyager/db/internal/sql/2002080202.sql,v 1.2 2002/08/08 12:26:18 philws Exp $
rem ---------------------------------------------------------------------------
rem (c) Volantis Systems Ltd 2002. 
rem ---------------------------------------------------------------------------
rem Change History:
rem
rem Date         Who             Description
rem ---------    --------------- ----------------------------------------------
rem 08-Aug-02    Phil W-S        VBM:2002080202 - Created. Updates the JDBC
rem                              repository with the new protocol.
rem ---------------------------------------------------------------------------

insert into vmpropertyeditors (GRP, PROPERTY, ATTRIBUTE, VALUE, REVISION)
    values ('devicePolicies', 'protocol', 23, 'XHTMLBasic_MIB2_1', 0);

update vmpropertyeditors
    set value='24'
    where grp='devicePolicies' and property='protocol' and attribute='count';

commit;
