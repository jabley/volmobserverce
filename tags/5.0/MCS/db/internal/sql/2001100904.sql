rem ---------------------------------------------------------------------------
rem $Header: /src/voyager/db/internal/sql/2001100904.sql,v 1.1 2001/12/27 14:51:21 jason Exp $
rem ---------------------------------------------------------------------------
rem (c) Volantis Systems Ltd 2000. 
rem ---------------------------------------------------------------------------
rem Change History:
rem
rem Date         Who             Description
rem ---------    --------------- ----------------------------------------------
rem 18-0ct-01    Payal            VBM:2001100904 - Created.
rem ---------------------------------------------------------------------------

insert into propertyEditors (grp, property, attribute, value) values (
        'stylePolicies',
        'view',
        'defaultValue',
        '');
insert into propertyEditors (grp, property, attribute, value) values (
        'stylePolicies',
        'view',
        'category',
        'dynvis');
insert into propertyEditors (grp, property, attribute, value) values (
        'stylePolicies',
        'view',
        'type',
        'select');
insert into propertyEditors (grp, property, attribute, value) values (
        'stylePolicies',
        'view',
        'count',
        '2');
insert into propertyEditors (grp, property, attribute, value) values (
        'stylePolicies',
        'view',
        '0',
        'tv');
insert into propertyEditors (grp, property, attribute, value) values (
        'stylePolicies',
        'view',
        '1',
        'web');

commit;
