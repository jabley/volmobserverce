rem ---------------------------------------------------------------------------
rem $Header: /src/voyager/db/internal/sql/create_text_asset.sql,v 1.1 2001/12/27 14:51:21 jason Exp $
rem ---------------------------------------------------------------------------
rem (c) Volantis Systems Ltd 2000. 
rem ---------------------------------------------------------------------------
rem Change History:
rem
rem Date         Who             Description
rem ---------    --------------- ----------------------------------------------
rem 25-Jul-01    Paul            VBM:2001071103 - Created.
rem ---------------------------------------------------------------------------

CREATE TABLE TextAsset (
  name VARCHAR2 (255),
  revision NUMBER(10),
  assetGroupName VARCHAR2 (255),
  value VARCHAR2 (255),
  valueType NUMBER(10) NOT NULL,
  language VARCHAR2 (255),
  deviceName VARCHAR2 (255),
  encoding NUMBER(10),
  CONSTRAINT PK_TextAsset PRIMARY KEY (name, language, deviceName)
);
