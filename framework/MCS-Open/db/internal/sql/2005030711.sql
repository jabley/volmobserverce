ALTER TABLE VMCONVERTIBLEIMAGEASSET ADD SEQUENCE NUMERIC(1) DEFAULT 0;
ALTER TABLE VMCONVERTIBLEIMAGEASSET ADD SEQUENCESIZE NUMERIC(9) DEFAULT 1;
ALTER TABLE VMDEVICEIMAGEASSET ADD SEQUENCE NUMERIC(1) DEFAULT 0;
ALTER TABLE VMDEVICEIMAGEASSET ADD SEQUENCESIZE NUMERIC(9) DEFAULT 1;
ALTER TABLE VMGENERICIMAGEASSET ADD SEQUENCE NUMERIC(1) DEFAULT 0;
ALTER TABLE VMGENERICIMAGEASSET ADD SEQUENCESIZE NUMERIC(9) DEFAULT 1;


REM
REM ===========================================================================
REM Change History
REM ===========================================================================
REM $Log$

REM 09-Mar-05	7315/1	allan	VBM:2005030711 Add sequences of image assets.

REM ===========================================================================
REM
