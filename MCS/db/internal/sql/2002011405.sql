rem ---------------------------------------------------------------------------
rem $Header: /src/voyager/db/internal/sql/2002011405.sql,v 1.1 2002/02/27 18:21:39 doug Exp $
rem ---------------------------------------------------------------------------
rem (c) Volantis Systems Ltd 2000. 
rem ---------------------------------------------------------------------------
rem Change History:
rem
rem Date         Who             Description
rem ---------    --------------- ----------------------------------------------
rem 27-Feb-02    Doug            VBM:2002011405 - Created. Revision table now
rem                              has a name column to describe the revision.
rem                              Safe to drop the table and recreate as this
rem                              table was not used in the past.
rem ---------------------------------------------------------------------------

drop table VMREVISION;

CREATE TABLE VMREVISION ( 
  NAME  VARCHAR (32)  NOT NULL, 
  REVISION  NUMERIC (9)   DEFAULT 0 NOT NULL ) ;

ALTER TABLE VMREVISION
 ADD CONSTRAINT PK_VMREVISION
  PRIMARY KEY (NAME) 
; 

commit;
