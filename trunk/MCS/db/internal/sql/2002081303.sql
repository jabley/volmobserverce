rem ---------------------------------------------------------------------------
rem $Header: /src/voyager/db/internal/sql/2002081303.sql,v 1.3 2003/01/30 14:52:42 chrisw Exp $
rem ---------------------------------------------------------------------------
rem (c) Volantis Systems Ltd 2000. 
rem ---------------------------------------------------------------------------
rem Change History:
rem
rem Date         Who             Description
rem ---------    --------------- ----------------------------------------------
rem 09-Sep-02    Ian             VBM:2002081303 - Created this script to add
rem                              convertible image asset.
rem 30-Jan-03    Chris W         VBM:2003013009 - Changed varchar2 to sql92
rem                              compliant varchar
rem ---------------------------------------------------------------------------

 DROP TABLE VMCONVERTIBLEIMAGEASSET;

 CREATE TABLE VMCONVERTIBLEIMAGEASSET(
  NAME VARCHAR(255) DEFAULT NULL NOT NULL,
  REVISION numeric(9) DEFAULT 0 NOT NULL,
  ASSETGROUPNAME VARCHAR(255) DEFAULT NULL,
  VALUE VARCHAR(255) DEFAULT NULL NOT NULL,
  PIXELSX numeric(9) DEFAULT 0 NOT NULL,
  PIXELSY numeric(9) DEFAULT 0 NOT NULL,
  PIXELDEPTH numeric(9) DEFAULT 0 NOT NULL,
  RENDERING numeric(9) DEFAULT 0 NOT NULL,
  ENCODING numeric(9) DEFAULT 0 NOT NULL);

 ALTER TABLE VMConvertibleImageAsset
   ADD CONSTRAINT PK_VMConvertibleImageAsset
     PRIMARY KEY ( NAME);

