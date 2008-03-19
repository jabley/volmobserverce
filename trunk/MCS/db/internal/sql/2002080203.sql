rem ---------------------------------------------------------------------------
rem $Header: /src/voyager/db/internal/sql/2002080203.sql,v 1.2 2002/08/05 09:23:03 adrian Exp $
rem ---------------------------------------------------------------------------
rem (c) Volantis Systems Ltd 2000. 
rem ---------------------------------------------------------------------------
rem Change History:
rem
rem Date         Who             Description
rem ---------    --------------- ----------------------------------------------
rem 05-Aug-02    Adrian          VBM:2002080203 - Created this script to update
rem                              jdbc repository with new protocol.
rem ---------------------------------------------------------------------------

insert into vmpropertyeditors (GRP, PROPERTY, ATTRIBUTE, VALUE, REVISION) values ('devicePolicies', 'protocol', 22, 'XHTMLBasic_Netfront3', 0);

update vmpropertyeditors set value='23' where grp='devicePolicies' and property='protocol' and attribute='count';

commit;