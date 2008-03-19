delete from propertyEditors where grp='stylePolicies' and property='link-ui';
insert into propertyEditors (grp, property, attribute, value) values (
        'stylePolicies',
        'link-ui',
        'type',
        'select');
insert into propertyEditors (grp, property, attribute, value) values (
        'stylePolicies',
        'link-ui',
        'defaultValue',
        'none');
insert into propertyEditors (grp, property, attribute, value) values (
        'stylePolicies',
        'link-ui',
        'category',
        'volantis');
insert into propertyEditors (grp, property, attribute, value) values (
        'stylePolicies',
        'link-ui',
        'count',
        '2');
insert into propertyEditors (grp, property, attribute, value) values (
        'stylePolicies',
        'link-ui',
        '0',
        'none');
insert into propertyEditors (grp, property, attribute, value) values (
        'stylePolicies',
        'link-ui',
        '1',
        'button');
commit;

