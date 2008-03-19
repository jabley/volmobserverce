CREATE TABLE VMDEVICE_TACS  (
   PROJECT              VARCHAR(255)                    NOT NULL,
   NAME                 VARCHAR(20)                     NOT NULL,
   TAC                  NUMERIC(8)                       NOT NULL
);

ALTER TABLE VMDEVICE_TACS
   ADD CONSTRAINT PK_VMDEVICE_TACS PRIMARY KEY (PROJECT, NAME, TAC);

REM
REM ===========================================================================
REM Change History
REM ===========================================================================
REM $Log$

REM 10-Aug-04	5147/1	adrianj	VBM:2004080318 Added support for TAC values to importer

REM 04-Aug-04	5072/1	byron	VBM:2004080304 JDBC foundation accessors for device identification by TAC

REM ===========================================================================
REM
