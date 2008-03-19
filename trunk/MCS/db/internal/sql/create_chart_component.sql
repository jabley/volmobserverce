rem ---------------------------------------------------------------------------
rem $Header: /src/voyager/db/internal/sql/create_chart_component.sql,v 1.1 2001/12/27 14:51:21 jason Exp $
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

create table ChartComponent
(
        name		                varchar(255) NOT NULL,
	fallbackChartComponentName	varchar(255),
	fallbackImageComponentName	varchar(255),
	fallbackTextComponentName	varchar(255),
        revision                        numeric(10)  NOT NUll,
	CONSTRAINT PK_ChartComponent PRIMARY KEY (name)
);
