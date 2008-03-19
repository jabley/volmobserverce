drop table ui_values;
create table ui_values
(
	name	varchar2(200),
	type	varchar2(10),
	value	varchar2(255)
);

drop index idx_ui_values;
create index idx_ui_values on ui_values(name,type) tablespace INDX;
