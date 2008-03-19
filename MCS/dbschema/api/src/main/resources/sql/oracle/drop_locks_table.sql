-- (c) Volantis Systems Ltd 2006 
-- ---------------------------------------------------------------------------
-- Oracle script for removing locks table for collaborative working.
--
-- You may need to perform a COMMIT transaction after executing this script.
-- ---------------------------------------------------------------------------

DROP TABLE VMPOLICYLOCKS CASCADE CONSTRAINTS;
