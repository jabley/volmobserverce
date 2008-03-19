delete from propertyEditors where grp='stylePolicies' and property='paragap';
insert into propertyEditors (grp, property, attribute, value) values (
        'stylePolicies',
        'paragap',
        'type',
        'int');
insert into propertyEditors (grp, property, attribute, value) values (
        'stylePolicies',
        'paragap',
        'defaultValue',
        '0);
insert into propertyEditors (grp, property, attribute, value) values (
        'stylePolicies',
        'paragap',
        'category',
        'dynvis');

delete from propertyEditors where grp='stylePolicies' and property='linegap';
insert into propertyEditors (grp, property, attribute, value) values (
        'stylePolicies',
        'linegap',
        'type',
        'int');
insert into propertyEditors (grp, property, attribute, value) values (
        'stylePolicies',
        'linegap',
        'defaultValue',
        '0);
insert into propertyEditors (grp, property, attribute, value) values (
        'stylePolicies',
        'linegap',
        'category',
        'dynvis');

delete from propertyEditors where grp='stylePolicies' and property='rowgap';
insert into propertyEditors (grp, property, attribute, value) values (
        'stylePolicies',
        'rowgap',
        'type',
        'int');
insert into propertyEditors (grp, property, attribute, value) values (
        'stylePolicies',
        'rowgap',
        'defaultValue',
        '0);
insert into propertyEditors (grp, property, attribute, value) values (
        'stylePolicies',
        'rowgap',
        'category',
        'dynvis');

delete from propertyEditors where grp='stylePolicies' and property='banner';
insert into propertyEditors (grp, property, attribute, value) values (
        'stylePolicies',
        'banner',
        'type',
        'text');
insert into propertyEditors (grp, property, attribute, value) values (
        'stylePolicies',
        'banner',
        'category',
        'dynvis');

delete from propertyEditors where grp='stylePolicies' and property='mode';
insert into propertyEditors (grp, property, attribute, value) values (
        'stylePolicies',
        'mode',
        'defaultValue',
        'window');
insert into propertyEditors (grp, property, attribute, value) values (
        'stylePolicies',
        'mode',
        'category',
        'dynvis');
insert into propertyEditors (grp, property, attribute, value) values (
        'stylePolicies',
        'mode',
        'type',
        'select');
insert into propertyEditors (grp, property, attribute, value) values (
        'stylePolicies',
        'mode',
        'defaultValue',
        'window');
insert into propertyEditors (grp, property, attribute, value) values (
        'stylePolicies',
        'mode',
        'count',
        '2');
insert into propertyEditors (grp, property, attribute, value) values (
        'stylePolicies',
        'mode',
        '0',
        'window');
insert into propertyEditors (grp, property, attribute, value) values (
        'stylePolicies',
        'mode',
        '1',
        'full');

delete from propertyEditors where grp='stylePolicies' and property='mainmenu';
insert into propertyEditors (grp, property, attribute, value) values (
        'stylePolicies',
        'mainmenu',
        'type',
        'boolean');
insert into propertyEditors (grp, property, attribute, value) values (
        'stylePolicies',
        'mainmenu',
        'defaultValue',
        'false');
insert into propertyEditors (grp, property, attribute, value) values (
        'stylePolicies',
        'mainmenu',
        'category',
        'dynvis');

commit;

