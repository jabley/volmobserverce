drop table categories;
create table categories
(
category_id	number(10),
parent_category_id 	number(10),
name	varchar2(256)
);

insert into categories (select unique 0, 0, category from feeds);

update categories set category_id = seq_category.nextval;

insert into categories (select seq_category.nextval, c.category_id, f.longname from feeds f, categories c where f.category = c.name);
