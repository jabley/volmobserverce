drop index idx_styles;
drop table styles;
create table styles
(
        theme_id number(38),
	tag	varchar2(20),
	class	varchar2(20),
	id	varchar2(20),
	name	varchar2(20),
	value	varchar2(20)
);


create index idx_styles on styles (theme_id) tablespace INDX; 
/
