rem ---------------------------------------------------------------------------
rem $Header: /src/voyager/db/internal/sql/2001122103.sql,v 1.2 2002/02/07 18:26:42 pduffin Exp $
rem ---------------------------------------------------------------------------
rem (c) Volantis Systems Ltd 2000. 
rem ---------------------------------------------------------------------------
rem Change History:
rem
rem Date         Who             Description
rem ---------    --------------- ----------------------------------------------
rem 06-Feb-02    Paul            VBM:2001122103 - Created.
rem ---------------------------------------------------------------------------

DROP TABLE VMSCRIPTCOMPONENT;
DROP TABLE VMSCRIPTASSET;

CREATE TABLE VMSCRIPTCOMPONENT ( 
  NAME                       VARCHAR (255)  NOT NULL, 
  REVISION                   NUMERIC (9)   NOT NULL ) ; 


ALTER TABLE VMSCRIPTCOMPONENT
 ADD CONSTRAINT PK_VMSCRIPTCOMPONENT
  PRIMARY KEY (NAME) 
; 

CREATE TABLE VMSCRIPTASSET ( 
  ASSETGROUPNAME             VARCHAR (255), 
  CHARACTERSET               VARCHAR (255), 
  DEVICENAME                 VARCHAR (255)  NOT NULL, 
  MIMETYPE                   VARCHAR (255)  NOT NULL, 
  NAME                       VARCHAR (255)  NOT NULL, 
  PROGRAMMINGLANGUAGE        VARCHAR (255)  NOT NULL, 
  REVISION                   NUMERIC (9)   NOT NULL,
  VALUE                      VARCHAR (255), 
  VALUETYPE       NUMERIC (9)   NOT NULL ) ; 


ALTER TABLE VMSCRIPTASSET
 ADD CONSTRAINT PK_VMSCRIPTASSET
  PRIMARY KEY (NAME, DEVICENAME) 
; 

