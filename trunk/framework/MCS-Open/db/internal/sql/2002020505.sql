rem ---------------------------------------------------------------------------
rem $Header: /src/voyager/db/internal/sql/2002020505.sql,v 1.3 2002/04/05 10:58:51 payal Exp $
rem ---------------------------------------------------------------------------
rem (c) Volantis Systems Ltd 2000. 
rem ---------------------------------------------------------------------------
rem Change History:
rem
rem Date         Who             Description
rem ---------    --------------- ----------------------------------------------
rem 19-Feb-02    Payal            VBM:2002020505 - Created.
rem 05-Apr-02    Payal            VBM:2002020505 - Modified font names 
rem                               to be seperated by a comma followed 
rem                               by a space and first letter of following font
rem                               names(serif, sans serif, monospace, cursive,
rem                               fantasy) sholud be in Lower case.
rem ---------------------------------------------------------------------------


insert into vmpropertyEditors (grp, property, attribute, value) values (
        'stylePolicies',
        'font-family',
        'defaultValue',
        '');
insert into vmpropertyEditors (grp, property, attribute, value) values (
        'stylePolicies',
        'font-family',
        'editable',
        'true');
insert into vmpropertyEditors (grp, property, attribute, value) values (
        'stylePolicies',
        'font-family',
        'count',
        '9');
insert into vmpropertyEditors (grp, property, attribute, value) values (
        'stylePolicies',
        'font-family',
        '0',
        'Times New Roman, Times, serif');
insert into vmpropertyEditors (grp, property, attribute, value) values (
        'stylePolicies',
        'font-family',
        '1',
        'Georgia, Times New Roman, Times, serif');
insert into vmpropertyEditors (grp, property, attribute, value) values (
        'stylePolicies',
        'font-family',
        '2',
        'Arial, Helvetica, sans-serif');
insert into vmpropertyEditors (grp, property, attribute, value) values (
        'stylePolicies',
        'font-family',
        '3',
        'Helvetica, Arial, Verdana, sans-serif');

insert into vmpropertyEditors (grp, property, attribute, value) values (
        'stylePolicies',
        'font-family',
        '4',
        'Tahoma, Arial, sans-serif');
insert into vmpropertyEditors (grp, property, attribute, value) values (
        'stylePolicies',
        'font-family',
        '5',
        'Verdana, Arial, Helvetica, sans-serif');
insert into vmpropertyEditors (grp, property, attribute, value) values (
        'stylePolicies',
        'font-family',
        '6',
        'Courier New, Courier, Mono, monospace');
insert into vmpropertyEditors (grp, property, attribute, value) values (
        'stylePolicies',
        'font-family',
        '7',
        'Comic Sans, cursive');
insert into vmpropertyEditors (grp, property, attribute, value) values (
        'stylePolicies',
        'font-family',
        '8',
        'Western, fantasy');

commit;
