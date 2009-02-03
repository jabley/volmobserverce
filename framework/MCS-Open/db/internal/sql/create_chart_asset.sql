rem ---------------------------------------------------------------------------
rem $Header: /src/voyager/db/internal/sql/create_chart_asset.sql,v 1.1 2001/12/27 14:51:21 jason Exp $
rem ---------------------------------------------------------------------------
rem (c) Volantis Systems Ltd 2000. 
rem ---------------------------------------------------------------------------
rem Change History:
rem
rem Date         Who             Description
rem ---------    --------------- ----------------------------------------------
rem 16-Jul-01    Paul            VBM:2001070508 - Added this header and added
rem                              a primary key.
rem ---------------------------------------------------------------------------

create table ChartAsset (
	name		varchar(32) NOT NULL,
	heighthint	number(6),
	widthhint	number(6),
	type		varchar(11),
	xTitle		varchar(64),
	yTitle		varchar(64),
	xInterval	number(6),
	yInterval	number(6),
	revision	number(10) NOT NULL,
	CONSTRAINT PK_ChartAsset PRIMARY KEY (name)
);

