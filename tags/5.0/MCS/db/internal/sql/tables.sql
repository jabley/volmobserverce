rem ---------------------------------------------------------------------------
rem $Header: /src/voyager/db/internal/sql/tables.sql,v 1.3 2003/04/08 09:38:32 sfound Exp $
rem ---------------------------------------------------------------------------
rem (c) Volantis Systems Ltd 2000. 
rem ---------------------------------------------------------------------------
rem Change History:
rem
rem Date         Who             Description
rem ---------    --------------- ----------------------------------------------
rem 21-Nov-01    Payal            VBM:2001111202 - Created.
rem 03-Dec-01    Doug             VBM:2001112901 - Changed column 'PREFIXURL' 
rem 				  in table VMASSETGROUP to allow null's
rem 30-Sep-02    Steve            VBM:2002071604 - Added VMAPPLICATIONPROPERTIES 
rem                               table.
rem ---------------------------------------------------------------------------


DROP TABLE VMAPPLICATIONPROPERTIES CASCADE CONSTRAINTS;

CREATE TABLE VMAPPLICATIONPROPERTIES (
  NAME            VARCHAR2 (255) NOT NULL,
  PROXYNAMESPACEBASEURL  VARCHAR (255),
  CACHEBASEDIRECTORY     VARCHAR (255),
  POLICYBASEURL          VARCHAR (255),
  CANVASBASEURL          VARCHAR (255),
  THEMEBASEURL           VARCHAR (255),
  LAYOUTBASEURL          VARCHAR (255),
  COMPONENTBASEURL       VARCHAR (255),
  MAPCOOKIESENABLED NUMBER(1);
  CONSTRAINT PK_VMAPPLICATIONPROPERTIES
  PRIMARY KEY ( NAME ) );

DROP TABLE VMASSETGROUP CASCADE CONSTRAINTS ; 

CREATE TABLE VMASSETGROUP ( 
  NAME            VARCHAR2 (255)  NOT NULL, 
  PREFIXURL       VARCHAR2 (255), 
  REPOSITORYNAME  VARCHAR2 (255), 
  REVISION        NUMBER (10)   DEFAULT 0 NOT NULL, 
  CONSTRAINT PK_VMASSETGROUP
  PRIMARY KEY ( NAME ) ) ; 

DROP TABLE VMAUDIOASSET CASCADE CONSTRAINTS ; 

CREATE TABLE VMAUDIOASSET ( 
  NAME            VARCHAR2 (255)  NOT NULL, 
  REVISION        NUMBER (10)   NOT NULL, 
  ASSETGROUPNAME  VARCHAR2 (255), 
  VALUE           VARCHAR2 (255), 
  ENCODING        NUMBER (10)   NOT NULL, 
  CONSTRAINT PK_VMAUDIOASSET 
  PRIMARY KEY ( NAME, ENCODING ) ) ; 

DROP TABLE VMAUDIOCOMPONENT CASCADE CONSTRAINTS ; 

CREATE TABLE VMAUDIOCOMPONENT ( 
  NAME                        VARCHAR2 (255)  NOT NULL, 
  REVISION                    NUMBER (10)   NOT NULL, 
  FALLBACKTEXTCOMPONENTNAME   VARCHAR2 (255), 
  FALLBACKAUDIOCOMPONENTNAME  VARCHAR2 (255), 
  CONSTRAINT PK_VMAUDIOCOMPONENT
  PRIMARY KEY ( NAME ) ) ; 

DROP TABLE VMBRPLUGIN_ATTRS CASCADE CONSTRAINTS ; 

CREATE TABLE VMBRPLUGIN_ATTRS ( 
  NAME       VARCHAR2 (20), 
  ATTRNAME   VARCHAR2 (128), 
  ATTRVALUE  VARCHAR2 (128), 
  REVISION   NUMBER (10)   DEFAULT 0 NOT NULL ) ; 

DROP TABLE VMBUTTONIMAGECOMPONENT CASCADE CONSTRAINTS ; 

CREATE TABLE VMBUTTONIMAGECOMPONENT ( 
  NAME                       VARCHAR2 (255)  NOT NULL, 
  REVISION                   NUMBER (10)   NOT NULL, 
  FALLBACKTEXTCOMPONENTNAME  VARCHAR2 (255), 
  UPIMAGECOMPONENTNAME       VARCHAR2 (255), 
  DOWNIMAGECOMPONENTNAME     VARCHAR2 (255), 
  OVERIMAGECOMPONENTNAME     VARCHAR2 (255), 
  CONSTRAINT PK_VMBUTTONIMAGECOMPONENT
  PRIMARY KEY ( NAME ) ) ; 

DROP TABLE VMCHARTASSET CASCADE CONSTRAINTS ; 

CREATE TABLE VMCHARTASSET ( 
  NAME        VARCHAR2 (32)  NOT NULL, 
  HEIGHTHINT  NUMBER (6), 
  WIDTHHINT   NUMBER (6), 
  TYPE        VARCHAR2 (11), 
  XTITLE      VARCHAR2 (64), 
  YTITLE      VARCHAR2 (64), 
  XINTERVAL   NUMBER (6), 
  YINTERVAL   NUMBER (6), 
  REVISION    NUMBER (10)   NOT NULL, 
  CONSTRAINT PK_VMCHARTASSET
  PRIMARY KEY ( NAME ) ) ; 

DROP TABLE VMCHARTCOMPONENT CASCADE CONSTRAINTS ; 

CREATE TABLE VMCHARTCOMPONENT ( 
  NAME                        VARCHAR2 (255)  NOT NULL, 
  FALLBACKCHARTCOMPONENTNAME  VARCHAR2 (255), 
  FALLBACKIMAGECOMPONENTNAME  VARCHAR2 (255), 
  FALLBACKTEXTCOMPONENTNAME   VARCHAR2 (255), 
  REVISION                    NUMBER (10)   NOT NULL, 
  CONSTRAINT PK_VMCHARTCOMPONENT
  PRIMARY KEY ( NAME ) ) ; 

DROP TABLE VMDEVICEIMAGEASSET CASCADE CONSTRAINTS ; 

CREATE TABLE VMDEVICEIMAGEASSET ( 
  NAME            VARCHAR2 (255)  NOT NULL, 
  REVISION        NUMBER (10)   NOT NULL, 
  ASSETGROUPNAME  VARCHAR2 (255), 
  VALUE           VARCHAR2 (255)  NOT NULL, 
  PIXELSX         NUMBER (10)   NOT NULL, 
  PIXELSY         NUMBER (10)   NOT NULL, 
  PIXELDEPTH      NUMBER (10)   NOT NULL, 
  RENDERING       NUMBER (10)   NOT NULL, 
  ENCODING        NUMBER (10)   NOT NULL, 
  DEVICENAME      VARCHAR2 (255)  NOT NULL, 
  LOCALSRC        NUMBER (1), 
  CONSTRAINT PK_VMDEVICEIMAGEASSET
  PRIMARY KEY ( NAME, DEVICENAME ) ) ; 

DROP TABLE VMDEVICE_LAYOUTS CASCADE CONSTRAINTS ; 

CREATE TABLE VMDEVICE_LAYOUTS ( 
  LAYOUT           VARCHAR2 (32)  NOT NULL, 
  DEVICE           VARCHAR2 (32)  NOT NULL, 
  DEFAULTFRAGMENT  VARCHAR2 (32), 
  DEFAULTSEGMENT   VARCHAR2 (32), 
  VERSION          NUMBER (6), 
  REVISION         NUMBER (10)   DEFAULT 0, 
  CONSTRAINT PK_VMDEVICELAYOUTS
  PRIMARY KEY ( LAYOUT, DEVICE ) ) ; 

DROP TABLE VMDEVICE_PATTERNS CASCADE CONSTRAINTS ; 

CREATE TABLE VMDEVICE_PATTERNS ( 
  NAME      VARCHAR2 (20)  NOT NULL, 
  PATTERN   VARCHAR2 (255)  NOT NULL, 
  REVISION  NUMBER (10)   DEFAULT 0, 
  CONSTRAINT PK_VMDEVICEPATTERNS
  PRIMARY KEY ( NAME, PATTERN ) ) ; 

DROP TABLE VMDYNAMICVISUALASSET CASCADE CONSTRAINTS ; 

CREATE TABLE VMDYNAMICVISUALASSET ( 
  NAME            VARCHAR2 (255)  NOT NULL, 
  REVISION        NUMBER (10)   NOT NULL, 
  ASSETGROUPNAME  VARCHAR2 (255), 
  VALUE           VARCHAR2 (255), 
  ENCODING        NUMBER (10)   NOT NULL, 
  PIXELSX         NUMBER (10)   NOT NULL, 
  PIXELSY         NUMBER (10)   NOT NULL, 
  CONSTRAINT PK_VMDYNAMICVISUALASSET
  PRIMARY KEY ( NAME, ENCODING, PIXELSX, PIXELSY ) ) ; 

DROP TABLE VMDYNAMICVISUALCOMPONENT CASCADE CONSTRAINTS ; 

CREATE TABLE VMDYNAMICVISUALCOMPONENT ( 
  NAME                         VARCHAR2 (255)  NOT NULL, 
  REVISION                     NUMBER (10)   NOT NULL, 
  FALLBACKTEXTCOMPONENTNAME    VARCHAR2 (255), 
  FALLBACKDYNVISCOMPONENTNAME  VARCHAR2 (255), 
  FALLBACKIMAGECOMPONENTNAME   VARCHAR2 (255), 
  FALLBACKAUDIOCOMPONENTNAME   VARCHAR2 (255), 
  CONSTRAINT PK_VMDYNAMICVISUALCOMPONENT
  PRIMARY KEY ( NAME ) ) ; 

DROP TABLE VMESCAPECOMPONENT CASCADE CONSTRAINTS ; 

CREATE TABLE VMESCAPECOMPONENT ( 
  NAME                       VARCHAR2 (255)  NOT NULL, 
  REVISION                   NUMBER (10)   NOT NULL, 
  FALLBACKTEXTCOMPONENTNAME  VARCHAR2 (255), 
  CONSTRAINT PK_VMESCAPECOMPONENT
  PRIMARY KEY ( NAME ) ) ; 

DROP TABLE VMEXTERNALREPOSITORYDEFINITION CASCADE CONSTRAINTS ; 

CREATE TABLE VMEXTERNALREPOSITORYDEFINITION ( 
  REPOSITORYNAME  VARCHAR2 (255)  NOT NULL, 
  ATTRIBUTENAME   VARCHAR2 (255)  NOT NULL, 
  ATTRIBUTEVALUE  VARCHAR2 (255), 
  REPOSITORYTYPE  NUMBER (10)   NOT NULL, 
  REVISION        NUMBER (10)   DEFAULT 0 NOT NULL, 
  CONSTRAINT PK_VMEXTERNALREPOSITORYACCESS
  PRIMARY KEY ( ATTRIBUTENAME, REPOSITORYTYPE, REPOSITORYNAME ) ) ; 

DROP TABLE VMFORMAT_ATTRIBUTES CASCADE CONSTRAINTS ; 

CREATE TABLE VMFORMAT_ATTRIBUTES ( 
  LAYOUT    VARCHAR2 (32)  NOT NULL, 
  DEVICE    VARCHAR2 (32)  NOT NULL, 
  INSTANCE  NUMBER (6)    NOT NULL, 
  NAME      VARCHAR2 (32)  NOT NULL, 
  VALUE     VARCHAR2 (128), 
  REVISION  NUMBER (10)   DEFAULT 0, 
  CONSTRAINT PK_VMFORMATATTRIBUTES
  PRIMARY KEY ( LAYOUT, DEVICE, INSTANCE, NAME ) ) ; 

DROP TABLE VMGENERICIMAGEASSET CASCADE CONSTRAINTS ; 

CREATE TABLE VMGENERICIMAGEASSET ( 
  NAME            VARCHAR2 (255)  NOT NULL, 
  REVISION        NUMBER (10)   NOT NULL, 
  ASSETGROUPNAME  VARCHAR2 (255), 
  VALUE           VARCHAR2 (255)  NOT NULL, 
  PIXELSX         NUMBER (10)   NOT NULL, 
  PIXELSY         NUMBER (10)   NOT NULL, 
  PIXELDEPTH      NUMBER (10)   NOT NULL, 
  RENDERING       NUMBER (10)   NOT NULL, 
  ENCODING        NUMBER (10)   NOT NULL, 
  WIDTHHINT       NUMBER (10)   NOT NULL, 
  LOCALSRC        NUMBER (1), 
  CONSTRAINT PK_VMGENERICIMAGEASSET
  PRIMARY KEY ( NAME, PIXELSX, PIXELSY, PIXELDEPTH, RENDERING, ENCODING, WIDTHHINT ) ) ; 

DROP TABLE VMIMAGECOMPONENT CASCADE CONSTRAINTS ; 

CREATE TABLE VMIMAGECOMPONENT ( 
  NAME                        VARCHAR2 (255)  NOT NULL, 
  REVISION                    NUMBER (10)   NOT NULL, 
  FALLBACKTEXTCOMPONENTNAME   VARCHAR2 (255), 
  FALLBACKIMAGECOMPONENTNAME  VARCHAR2 (255), 
  CONSTRAINT PK_VMIMAGECOMPONENT
  PRIMARY KEY ( NAME ) ) ; 

DROP TABLE VMLAYOUTS CASCADE CONSTRAINTS ; 

CREATE TABLE VMLAYOUTS ( 
  LAYOUT    VARCHAR2 (32)  NOT NULL, 
  TYPE      VARCHAR2 (32), 
  REVISION  NUMBER (10)   DEFAULT 0, 
  CONSTRAINT PK_VMLAYOUTS
  PRIMARY KEY ( LAYOUT ) ) ; 

DROP TABLE VMLAYOUT_FORMATS CASCADE CONSTRAINTS ; 

CREATE TABLE VMLAYOUT_FORMATS ( 
  LAYOUT      VARCHAR2 (32)  NOT NULL, 
  DEVICE      VARCHAR2 (32)  NOT NULL, 
  INSTANCE    NUMBER (6)    NOT NULL, 
  PARENT      NUMBER (6), 
  CHILDINDEX  NUMBER (2), 
  TYPE        VARCHAR2 (32), 
  REVISION    NUMBER (10)   DEFAULT 0, 
  CONSTRAINT PK_VMLAYOUTFORMATS
  PRIMARY KEY ( LAYOUT, DEVICE, INSTANCE ) ) ; 

DROP TABLE VMLINKASSET CASCADE CONSTRAINTS ; 

CREATE TABLE VMLINKASSET ( 
  NAME            VARCHAR2 (255)  NOT NULL, 
  REVISION        NUMBER (10), 
  ASSETGROUPNAME  VARCHAR2 (255), 
  VALUE           VARCHAR2 (1024), 
  DEVICENAME      VARCHAR2 (255)  NOT NULL, 
  CONSTRAINT PK_VMLINKASSET
  PRIMARY KEY ( NAME, DEVICENAME ) ) ;  

DROP TABLE VMLINKCOMPONENT CASCADE CONSTRAINTS ; 

CREATE TABLE VMLINKCOMPONENT ( 
  NAME                       VARCHAR2 (255)  NOT NULL, 
  FALLBACKTEXTCOMPONENTNAME  VARCHAR2 (255), 
  REVISION                   NUMBER (10)   NOT NULL, 
  CONSTRAINT PK_VMLINKCOMPONENT
  PRIMARY KEY ( NAME ) ) ; 

DROP TABLE VMLOCKS CASCADE CONSTRAINTS ; 

CREATE TABLE VMLOCKS ( 
  CLASS    VARCHAR2 (64)  NOT NULL, 
  NAME     VARCHAR2 (254)  NOT NULL, 
  LOCKKEY  VARCHAR2 (254)  NOT NULL, 
  CONSTRAINT PK_VMLOCKS
  PRIMARY KEY ( CLASS, NAME, LOCKKEY ) ) ; 

DROP TABLE VMLOCK_HOLDERS CASCADE CONSTRAINTS ; 

CREATE TABLE VMLOCK_HOLDERS ( 
  CLASS     VARCHAR2 (64)  NOT NULL, 
  NAME      VARCHAR2 (254)  NOT NULL, 
  LOCKKEY   VARCHAR2 (254)  NOT NULL, 
  USERNAME  VARCHAR2 (32), 
  HOSTNAME  VARCHAR2 (128), 
  CONSTRAINT PK_VMLOCK_HOLDERS
  PRIMARY KEY ( CLASS, NAME, LOCKKEY ) ) ; 

DROP TABLE VMPOLICYPREFERENCE CASCADE CONSTRAINTS ; 

CREATE TABLE VMPOLICYPREFERENCE ( 
  USERIDENTIFICATION  VARCHAR2 (255)  NOT NULL, 
  POLICYNAME          VARCHAR2 (255)  NOT NULL, 
  POLICYVALUE         VARCHAR2 (255)  NOT NULL, 
  DEVICENAME          VARCHAR2 (20), 
  CONSTRAINT PK_VMPOLICYPREFERENCE
  PRIMARY KEY ( USERIDENTIFICATION ) ) ; 

DROP TABLE VMPOLICY_VALUES CASCADE CONSTRAINTS ; 

CREATE TABLE VMPOLICY_VALUES ( 
  NAME      VARCHAR2 (20)  NOT NULL, 
  POLICY    VARCHAR2 (200)  NOT NULL, 
  VALUE     VARCHAR2 (1024), 
  REVISION  NUMBER (10)   DEFAULT 0, 
  CONSTRAINT PK_VMPOLICYVALUES
  PRIMARY KEY ( NAME, POLICY ) ) ; 

DROP TABLE VMPROPERTYEDITORS CASCADE CONSTRAINTS ; 

CREATE TABLE VMPROPERTYEDITORS ( 
  GRP        VARCHAR2 (32)  NOT NULL, 
  PROPERTY   VARCHAR2 (64)  NOT NULL, 
  ATTRIBUTE  VARCHAR2 (64)  NOT NULL, 
  VALUE      VARCHAR2 (128), 
  REVISION   NUMBER (10)   DEFAULT 0, 
  CONSTRAINT PK_VMPROPERTYEDITORS
  PRIMARY KEY ( GRP, PROPERTY, ATTRIBUTE ) ) ; 

DROP TABLE VMRESOURCEBUNDLES CASCADE CONSTRAINTS ; 

CREATE TABLE VMRESOURCEBUNDLES ( 
  BUNDLE     VARCHAR2 (64), 
  VALUE      VARCHAR2 (2048), 
  REVISION   NUMBER (10)   DEFAULT 0, 
  POLICYKEY  VARCHAR2 (254) ) ; 


CREATE UNIQUE INDEX PK_VMRESOURCEBUNDLES ON 
  VMRESOURCEBUNDLES(BUNDLE, POLICYKEY) ; 


DROP TABLE VMREVISION CASCADE CONSTRAINTS ; 

CREATE TABLE VMREVISION ( 
  REVISION  NUMBER (10)   DEFAULT 0 NOT NULL ) ; 

DROP TABLE VMROLLOVERIMAGECOMPONENT CASCADE CONSTRAINTS ; 

CREATE TABLE VMROLLOVERIMAGECOMPONENT ( 
  NAME                       VARCHAR2 (255)  NOT NULL, 
  REVISION                   NUMBER (10)   NOT NULL, 
  FALLBACKTEXTCOMPONENTNAME  VARCHAR2 (255), 
  NORMALIMAGECOMPONENTNAME   VARCHAR2 (255), 
  OVERIMAGECOMPONENTNAME     VARCHAR2 (255), 
  CONSTRAINT PK_VMROLLOVERIMAGECOMPONENT
  PRIMARY KEY ( NAME ) ) ; 

DROP TABLE VMSCRIPTCOMPONENT CASCADE CONSTRAINTS ; 

CREATE TABLE VMSCRIPTCOMPONENT ( 
  NAME                       VARCHAR2 (255)  NOT NULL, 
  REVISION                   NUMBER (10)   NOT NULL, 
  FALLBACKTEXTCOMPONENTNAME  VARCHAR2 (255), 
  CONSTRAINT PK_VMSCRIPTCOMPONENT
  PRIMARY KEY ( NAME, REVISION ) ) ; 

DROP TABLE VMSMARTLINK CASCADE CONSTRAINTS ; 

CREATE TABLE VMSMARTLINK ( 
  URL             VARCHAR2 (256), 
  STATE           VARCHAR2 (10), 
  LASTALIVE       NUMBER (30), 
  LASTDEAD        NUMBER (30), 
  TRANSCOUNT      NUMBER (30), 
  AVETRANS        NUMBER (30), 
  SRCCONTENTTYPE  VARCHAR2 (30), 
  REVISION        NUMBER (10)   DEFAULT 0 ) ; 

DROP TABLE VMSTYLES CASCADE CONSTRAINTS ; 

CREATE TABLE VMSTYLES ( 
  THEME_NAME  VARCHAR2 (20), 
  DEVICE      VARCHAR2 (20), 
  TAG         VARCHAR2 (20), 
  CLASS       VARCHAR2 (20), 
  ID          VARCHAR2 (20), 
  NAME        VARCHAR2 (25), 
  VALUE       VARCHAR2 (150), 
  REVISION    NUMBER (10)   DEFAULT 0 ) ; 

DROP TABLE VMTEXTASSET CASCADE CONSTRAINTS ; 

CREATE TABLE VMTEXTASSET ( 
  NAME            VARCHAR2 (255)  NOT NULL, 
  REVISION        NUMBER (10)   NOT NULL, 
  ASSETGROUPNAME  VARCHAR2 (255), 
  VALUE           VARCHAR2 (255), 
  VALUETYPE       NUMBER (10)   NOT NULL, 
  LANGUAGE        VARCHAR2 (255)  NOT NULL, 
  DEVICENAME      VARCHAR2 (255)  NOT NULL, 
  ENCODING        NUMBER (10)   NOT NULL, 
  CONSTRAINT PK_VMTEXTASSET
  PRIMARY KEY ( NAME, LANGUAGE, DEVICENAME ) ) ; 

DROP TABLE VMTEXTCOMPONENT CASCADE CONSTRAINTS ; 

CREATE TABLE VMTEXTCOMPONENT ( 
  NAME                       VARCHAR2 (255)  NOT NULL, 
  REVISION                   NUMBER (10)   NOT NULL, 
  FALLBACKTEXTCOMPONENTNAME  VARCHAR2 (255), 
  CONSTRAINT PK_VMTEXTCOMPONENT
  PRIMARY KEY ( NAME, REVISION ) ) ; 

DROP TABLE VMTEXTIMG CASCADE CONSTRAINTS ; 

CREATE TABLE VMTEXTIMG ( 
  NAME         VARCHAR2 (30), 
  BORDERWIDTH  NUMBER (4), 
  BORDERTYPE   VARCHAR2 (30), 
  FONTNAME     VARCHAR2 (30), 
  BACKGROUND   VARCHAR2 (14), 
  TEXTALIGN    VARCHAR2 (20), 
  BORDERCOLOR  VARCHAR2 (14), 
  IMAGEHEIGHT  NUMBER (4), 
  IMAGEWIDTH   NUMBER (4), 
  TEXTCOLOR    VARCHAR2 (14), 
  TEXT         VARCHAR2 (124), 
  FONTSIZE     NUMBER (4), 
  REVISION     NUMBER (10)   DEFAULT 0 ) ; 

DROP TABLE VMTHEMES CASCADE CONSTRAINTS ; 

CREATE TABLE VMTHEMES ( 
  NAME      VARCHAR2 (20)  NOT NULL, 
  DEVICE    VARCHAR2 (20)  NOT NULL, 
  REVISION  NUMBER (10)   DEFAULT 0, 
  CONSTRAINT PK_VMTHEMES
  PRIMARY KEY ( NAME, DEVICE ) ) ; 

DROP TABLE VMUSERINFORMATION CASCADE CONSTRAINTS ; 

CREATE TABLE VMUSERINFORMATION ( 
  USERIDENTIFICATION      VARCHAR2 (255)  NOT NULL, 
  EXTERNALREPOSITORYNAME  VARCHAR2 (255), 
  CONSTRAINT PK_VMUSER
  PRIMARY KEY ( USERIDENTIFICATION ) ) ; 

DROP TABLE VMVOLANTIS_TABS CASCADE CONSTRAINTS ; 

CREATE TABLE VMVOLANTIS_TABS ( 
  TABLE_NAME  VARCHAR2 (30)  NOT NULL ) ; 

commit;

REM
REM ===========================================================================
REM Change History
REM ===========================================================================
REM $Log$

REM 06-Oct-04	5710/1	geoff	VBM:2004052005 Short column name support

REM ===========================================================================
REM
