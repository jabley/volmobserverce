drop table themes;
create table themes
(
        theme_id number(38),
	name	varchar2(20),
	device	varchar2(20),
	fallback varchar2(20)
);

create index idx_themes on themes (theme_id) tablespace INDX; 
drop sequence seq_themes;
create sequence seq_themes;
/
