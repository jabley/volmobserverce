alter table vmdevice_theme add importparent numeric(1);
update vmdevice_theme set importparent=0;
alter table vmdevice_theme modify (importparent numeric(1) NOT NULL);
REM
REM ===========================================================================
REM Change History
REM ===========================================================================
REM $Log$

REM 12-Aug-04	5111/1	doug	VBM:2004080405 Added Theme overlay support

REM ===========================================================================
REM
