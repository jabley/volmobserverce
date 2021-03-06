rem ---------------------------------------------------------------------------
rem $Header: /src/voyager/db/internal/sql/2002040811.sql,v 1.2 2002/05/16 14:51:58 adrian Exp $
rem ---------------------------------------------------------------------------
rem (c) Volantis Systems Ltd 2000. 
rem ---------------------------------------------------------------------------
rem Change History:
rem
rem Date         Who             Description
rem ---------    --------------- ----------------------------------------------
rem 10-May-02    Adrian          VBM:2002040811 - Created this sql script to
rem                              generate database tables for the new themes
rem                              implementation.
rem 16-May-02    Adrian          VBM:2002040811 - corrected revision field to
rem                              use numeric instead of number.
rem ---------------------------------------------------------------------------

CREATE TABLE VMTHEME (
  NAME            VARCHAR (255) NOT NULL,
  REVISION        NUMERIC (10));

ALTER TABLE VMTHEME
  ADD CONSTRAINT PK_VMTHEME
    PRIMARY KEY (NAME)
;


CREATE TABLE VMDEVICE_THEME (
  THEME                 VARCHAR (255) NOT NULL,
  DEVICE                VARCHAR (255) NOT NULL,
  EXTERNAL_STYLE_SHEET  VARCHAR (1024),
  REVISION        NUMERIC (10));

ALTER TABLE VMDEVICE_THEME
  ADD CONSTRAINT PK_VMDEVICE_THEME
    PRIMARY KEY (THEME, DEVICE)
;


CREATE TABLE VMRULE (
  THEME           VARCHAR (255) NOT NULL,
  DEVICE          VARCHAR (255) NOT NULL,
  RULE_INDEX      NUMERIC (9) NOT NULL);

ALTER TABLE VMRULE
  ADD CONSTRAINT PK_VMRULE
    PRIMARY KEY (THEME, DEVICE, RULE_INDEX)
;


CREATE TABLE VMSELECTOR_GROUP (
  THEME           VARCHAR (255) NOT NULL,
  DEVICE          VARCHAR (255) NOT NULL,
  RULE_INDEX      NUMERIC (9) NOT NULL,
  SELECTOR_REF    NUMERIC (9) NOT NULL);

ALTER TABLE VMSELECTOR_GROUP
  ADD CONSTRAINT PK_VMSELECTOR_GROUP
    PRIMARY KEY (THEME, DEVICE, RULE_INDEX, SELECTOR_REF)
;


CREATE TABLE VMELEMENT_SELECTOR (
  THEME           VARCHAR (255) NOT NULL,
  DEVICE          VARCHAR (255) NOT NULL,
  SELECTOR_INDEX  NUMERIC (9) NOT NULL,
  ELEMENT_NAME    VARCHAR (31) NOT NULL);

ALTER TABLE VMELEMENT_SELECTOR
  ADD CONSTRAINT PK_VMELEMENT_SELECTOR
    PRIMARY KEY (THEME, DEVICE, SELECTOR_INDEX)
;


CREATE TABLE VMCLASS_SELECTOR (
  THEME           VARCHAR (255) NOT NULL,
  DEVICE          VARCHAR (255) NOT NULL,
  SELECTOR_INDEX  NUMERIC (9) NOT NULL,
  SEQUENCE_INDEX  NUMERIC (9) NOT NULL,
  STYLE_CLASS     VARCHAR (127) NOT NULL);

ALTER TABLE VMCLASS_SELECTOR
  ADD CONSTRAINT PK_VMCLASS_SELECTOR
    PRIMARY KEY (THEME, DEVICE, SELECTOR_INDEX, SEQUENCE_INDEX)
;


CREATE TABLE VMID_SELECTOR (
  THEME           VARCHAR (255) NOT NULL,
  DEVICE          VARCHAR (255) NOT NULL,
  SELECTOR_INDEX  NUMERIC (9) NOT NULL,
  SEQUENCE_INDEX  NUMERIC (9) NOT NULL,
  STYLE_ID        VARCHAR (127) NOT NULL);

ALTER TABLE VMID_SELECTOR
  ADD CONSTRAINT PK_VMID_SELECTOR
    PRIMARY KEY (THEME, DEVICE, SELECTOR_INDEX, SEQUENCE_INDEX)
;


CREATE TABLE VMATTRIBUTE_SELECTOR (
  THEME           VARCHAR (255) NOT NULL,
  DEVICE          VARCHAR (255) NOT NULL,
  SELECTOR_INDEX  NUMERIC (9) NOT NULL,
  SEQUENCE_INDEX  NUMERIC (9) NOT NULL,
  ATTRIBUTE_NAME  VARCHAR (127) NOT NULL,
  OPERATOR        NUMERIC (1) NOT NULL,
  ATTRIBUTE_VALUE VARCHAR (127) NOT NULL);

ALTER TABLE VMATTRIBUTE_SELECTOR
  ADD CONSTRAINT PK_VMATTRIBUTE_SELECTOR
    PRIMARY KEY (THEME, DEVICE, SELECTOR_INDEX, SEQUENCE_INDEX)
;


CREATE TABLE VMPSEUDO_CLASS_SELECTOR (
  THEME           VARCHAR (255) NOT NULL,
  DEVICE          VARCHAR (255) NOT NULL,
  SELECTOR_REF    NUMERIC (9) NOT NULL,
  SEQUENCE_INDEX  NUMERIC (9) NOT NULL,
  IDENTIFIER      VARCHAR (127) NOT NULL,
  PARAMETER       VARCHAR (127));

ALTER TABLE VMPSEUDO_CLASS_SELECTOR
  ADD CONSTRAINT PK_VMPSEUDO_CLASS_SELECTOR
    PRIMARY KEY (THEME, DEVICE, SELECTOR_REF, SEQUENCE_INDEX)
;


CREATE TABLE VMPSEUDO_ELEMENT_SELECTOR (
  THEME           VARCHAR (255) NOT NULL,
  DEVICE          VARCHAR (255) NOT NULL,
  SELECTOR_REF    NUMERIC (9) NOT NULL,
  IDENTIFIER      VARCHAR (127) NOT NULL,
  PARAMETER       VARCHAR (127));

ALTER TABLE VMPSEUDO_ELEMENT_SELECTOR
  ADD CONSTRAINT PK_VMPSEUDO_ELEMENT_SELECTOR
    PRIMARY KEY (THEME, DEVICE, SELECTOR_REF)
;


CREATE TABLE VMCOMBINED_SELECTOR (
  THEME               VARCHAR (255) NOT NULL,
  DEVICE              VARCHAR (255) NOT NULL,
  SELECTOR_INDEX      NUMERIC (9) NOT NULL,
  LEFT_SELECTOR_REF   NUMERIC (9) NOT NULL,
  COMBINATOR          NUMERIC (1) NOT NULL,
  RIGHT_SELECTOR_REF  NUMERIC (9) NOT NULL);

ALTER TABLE VMCOMBINED_SELECTOR
  ADD CONSTRAINT PK_VMCOMBINED_SELECTOR
    PRIMARY KEY (THEME, DEVICE, SELECTOR_INDEX)
;