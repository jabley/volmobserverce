rem ---------------------------------------------------------------------------
rem $Header: /src/voyager/db/internal/sql/2001090302.sql,v 1.1 2001/12/27 14:51:21 jason Exp $
rem ---------------------------------------------------------------------------
rem (c) Volantis Systems Ltd 2000. 
rem ---------------------------------------------------------------------------
rem Change History:
rem
rem Date         Who             Description
rem ---------    --------------- ----------------------------------------------
rem 05-Aug-01    Doug            VBM:2001090302 - Created.
rem ---------------------------------------------------------------------------

DROP TABLE LinkComponent;

CREATE TABLE LinkComponent
(
        name		                VARCHAR2(255) NOT NULL,
	fallbackTextComponentName	VARCHAR2(255),
        revision                        NUMERIC(10)  NOT NUll,
	CONSTRAINT PK_LinkComponent PRIMARY KEY (name)
);

DROP TABLE LinkAsset;

CREATE TABLE LinkAsset 
(
  name VARCHAR2 (255),
  revision NUMBER(10),
  assetGroupName VARCHAR2 (255),
  value VARCHAR2 (1024),
  deviceName VARCHAR2 (255),
  CONSTRAINT PK_LinkAsset PRIMARY KEY (name, deviceName)
);
