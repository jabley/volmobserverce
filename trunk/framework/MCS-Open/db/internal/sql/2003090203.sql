alter table VMMENUPROPERTIES  add MRNR_MENU_LSTL_SYNTAX numeric(3) DEFAULT -1 NOT NULL;
alter table VMMENUPROPERTIES  add MRNR_MENU_LSTL_PRIORITY numeric(3) DEFAULT 0 NOT NULL;
alter table VMMENUPROPERTIES  add MRNR_MENU_LSTL_KEYWD numeric(9) DEFAULT 0 NOT NULL;

commit;

alter table VSMENUPROPERTIES  add MRNR_MENU_LSTL_SYNTAX numeric(3) DEFAULT -1 NOT NULL;
alter table VSMENUPROPERTIES  add MRNR_MENU_LSTL_PRIORITY numeric(3) DEFAULT 0 NOT NULL;
alter table VSMENUPROPERTIES  add MRNR_MENU_LSTL_KEYWD numeric(9) DEFAULT 0 NOT NULL;

commit;
REM
REM ===========================================================================
REM Change History
REM ===========================================================================
REM $Log$

REM 08-Sep-03	1326/1	doug	VBM:2003090203 Added new menu link style property

REM ===========================================================================
REM
