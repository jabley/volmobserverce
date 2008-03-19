drop table image;
create table image
(
	name			varchar2(32),
	altimage		varchar2(32),
	encoding		varchar2(16),
	pixelsx			number,
	pixelsy			number,
	pixeldepth		number,
	rendering		char(1),
	widthhint		number(4),
	filename		varchar2(256),

	type			varchar2(16),
	colourcapability	varchar2(4),
	minformfactor		varchar2(4)
);
