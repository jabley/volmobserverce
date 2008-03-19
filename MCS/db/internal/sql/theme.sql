drop table theme;
create table theme
(
	name	varchar2(20),
	policy	varchar2(200),
	value	varchar2(1024),
	helptext	varchar2(2048),
	ui_type	varchar2(20)
);

create index idx_theme on theme (name,policy) tablespace INDX; 
/
