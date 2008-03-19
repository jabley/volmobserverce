rem ---------------------------------------------------------------------------
rem $Header: /src/voyager/db/internal/sql/2002011410.sql,v 1.2 2002/02/18 12:58:15 jason Exp $
rem ---------------------------------------------------------------------------
rem (c) Volantis Systems Ltd 2002. 
rem ---------------------------------------------------------------------------
rem Change History:
rem
rem Date         Who             Description
rem ---------    --------------- ----------------------------------------------
rem 31-Jan-02    Doug            VBM:2002011410 - SQL script to add a new
rem                              table - VMTHEME_ATTRIBUTES
rem ---------------------------------------------------------------------------

CREATE TABLE VMTHEME_ATTRIBUTES (
	THEME_NAME	 VARCHAR(20)	NOT NULL,
	DEVICE		 VARCHAR(20)	NOT NULL,
	NAME		 VARCHAR(32),
	VALUE		 VARCHAR(128),
	REVISION	 NUMERIC(10) DEFAULT 0 NOT NULL,
CONSTRAINT PK_VMTHEME_ATTRIBUTES
PRIMARY KEY ( THEME_NAME, DEVICE) ) ;
