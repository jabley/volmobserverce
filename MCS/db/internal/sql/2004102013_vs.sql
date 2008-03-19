CREATE TABLE VSREPLACEDPROPERTIES(
  THEME varchar(255) NOT NULL,
  DEVICE varchar(255) NOT NULL,
  RULE_REF numeric(9) NOT NULL,
  CONTENT_SYNTAX numeric(3) DEFAULT -1 NOT NULL,
  CONTENT_PRIORITY numeric(3) DEFAULT 0 NOT NULL,
  CONTENT_KEYWD numeric(9) DEFAULT 0 NOT NULL,
  CONTENT_STR varchar(255) DEFAULT NULL,
  PROJECT VARCHAR(255) NOT NULL);

ALTER TABLE VSREPLACEDPROPERTIES
  ADD CONSTRAINT PK_VSREPLACEDPROPERTIES
    PRIMARY KEY (THEME, DEVICE, RULE_REF, PROJECT);

commit;

ALTER TABLE VMMENUPROPERTIES
  ADD MRNR_MITEM_SCUT_ACT_SYNTAX numeric(3) DEFAULT -1 NOT NULL;
ALTER TABLE VMMENUPROPERTIES
  ADD MRNR_MITEM_SCUT_ACT_PRIORITY numeric(3) DEFAULT 0 NOT NULL;
ALTER TABLE VMMENUPROPERTIES
  ADD MRNR_MITEM_SCUT_ACT_KEYWD numeric(9) DEFAULT 0 NOT NULL;

commit;

REM
REM ===========================================================================
REM Change History
REM ===========================================================================
REM $Log$

REM 28-Oct-04	6008/1	matthew	VBM:2004102013 Add sql update scripts to allow for Content category and its properties

REM 16-Sep-04	5531/3	claire	VBM:2004091505 Provide styling of access key prefixes: Schema Changes

REM ===========================================================================
REM
