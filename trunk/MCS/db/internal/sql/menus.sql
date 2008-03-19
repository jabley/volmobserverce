drop table menus;
create table menus
(
	name	varchar2(40),
	next	varchar2(40),
	stmt	varchar2(255),
	endstmt varchar2(255)
);
