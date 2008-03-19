-- (c) Volantis Systems Ltd 2006 
-- ---------------------------------------------------------------------------
-- Oracle script for creating locks table for collaborative working.
--
-- You may need to perform a COMMIT transaction after executing this script.
-- ---------------------------------------------------------------------------

CREATE TABLE VMPOLICYLOCKS (
    PROJECT      VARCHAR2(255) NOT NULL,
    RESOURCEID     VARCHAR2(255) NOT NULL,
    PRINCIPAL    VARCHAR2(63) NOT NULL,
    ACQUIRED     DATE NOT NULL
);

ALTER TABLE VMPOLICYLOCKS
  ADD CONSTRAINT PK_VMPOLICYLOCKS
  PRIMARY KEY ( PROJECT, RESOURCEID );
