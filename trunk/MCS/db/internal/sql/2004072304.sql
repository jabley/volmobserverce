create table VMPOLICY_CATEGORY  (
   PROJECT              VARCHAR(255)                    not null,
   CATEGORY_ID          NUMERIC(9)                      not null,
   CATEGORY_NAME        VARCHAR(255)                    not null
);

alter table VMPOLICY_CATEGORY
   add constraint PK_VMPOLICY_CATEGORY primary key (PROJECT, CATEGORY_ID);

create table VMPOLICY_TYPE  (
   PROJECT              VARCHAR(255)                    not null,
   POLICY               VARCHAR(200)                    not null,
   TYPE_INSTANCE_ID     NUMERIC(9)                      not null,
   CATEGORY_ID          NUMERIC(9)                      not null
);

alter table VMPOLICY_TYPE
   add constraint PK_VMPOLICY_TYPE primary key (PROJECT, POLICY);

create table VMTYPES  (
   PROJECT              VARCHAR(255)                    not null,
   TYPE_INSTANCE_ID     NUMERIC(9)                      not null,
   TYPE_ID              NUMERIC(3)                      not null
);

alter table VMTYPES
   add constraint PK_VMTYPES primary key (PROJECT, TYPE_INSTANCE_ID);

create table VMTYPES_RANGE  (
   PROJECT              VARCHAR(255)                    not null,
   TYPE_INSTANCE_ID     NUMERIC(9)                      not null,
   MIN_VALUE            NUMERIC(9)                      not null,
   MAX_VALUE            NUMERIC(9)                      not null
);

alter table VMTYPES_RANGE
   add constraint PK_VMTYPES_RANGE primary key (PROJECT, TYPE_INSTANCE_ID);

create table VMTYPES_SELECTION  (
   PROJECT              VARCHAR(255)                    not null,
   TYPE_INSTANCE_ID     NUMERIC(9)                      not null,
   KEYWORD              VARCHAR(255)                    not null
);

alter table VMTYPES_SELECTION
   add constraint PK_VMTYPES_SELECTION primary key (PROJECT, TYPE_INSTANCE_ID, KEYWORD);

create table VMTYPES_SET  (
   PROJECT              VARCHAR(255)                    not null,
   TYPE_INSTANCE_ID     NUMERIC(9)                      not null,
   MEMBER_INSTANCE_ID   NUMERIC(9)                      not null,
   ORDERED              NUMERIC(1)                      default 0
);

alter table VMTYPES_SET
   add constraint PK_VMTYPES_SET primary key (PROJECT, TYPE_INSTANCE_ID);

create table VMTYPES_STRUCTURE  (
   PROJECT              VARCHAR(255)                    not null,
   TYPE_INSTANCE_ID     NUMERIC(9)                      not null,
   FIELDNAME            VARCHAR(255)                    not null,
   FIELD_INSTANCE_ID    NUMERIC(9)                      not null
);

alter table VMTYPES_STRUCTURE
   add constraint PK_VMTYPES_STRUCTURE primary key (PROJECT, TYPE_INSTANCE_ID, FIELDNAME);

REM
REM ===========================================================================
REM Change History
REM ===========================================================================
REM $Log$

REM 28-Jul-04	4964/1	geoff	VBM:2004072602 Public API for Device Repository: JDBC metadata write support

REM 27-Jul-04	4954/1	byron	VBM:2004072304 Public API for Device Repository: implement JDBC metadata read support - updated sql scripts

REM ===========================================================================
REM
