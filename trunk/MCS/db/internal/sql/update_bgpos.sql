update style_attributes set ui_type = 'editselect' where name = 'background-position';
insert into ui_values (name, type, value) values (
        'background-position',
        'style',
        'top left');
insert into ui_values (name, type, value) values (
        'background-position',
        'style',
        'top center');
insert into ui_values (name, type, value) values (
        'background-position',
        'style',
        'top right');
insert into ui_values (name, type, value) values (
        'background-position',
        'style',
        'center left');
insert into ui_values (name, type, value) values (
        'background-position',
        'style',
        'center center');
insert into ui_values (name, type, value) values (
        'background-position',
        'style',
        'center right');
insert into ui_values (name, type, value) values (
        'background-position',
        'style',
        'bottom left');
insert into ui_values (name, type, value) values (
        'background-position',
        'style',
        'bottom center');
insert into ui_values (name, type, value) values (
        'background-position',
        'style',
        'bottom right');

commit;
