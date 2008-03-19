rem ---------------------------------------------------------------------------
rem $Header: /src/voyager/db/internal/sql/2001100103.sql,v 1.1 2002/04/10 13:45:10 payal Exp $
rem ---------------------------------------------------------------------------
rem (c) Volantis Systems Ltd 2000. 
rem ---------------------------------------------------------------------------
rem Change History:
rem
rem Date         Who             Description
rem ---------    --------------- ----------------------------------------------
rem 10-Apr-02    Payal            VBM:2001100103 - Created.
rem ---------------------------------------------------------------------------


update vmresourcebundles set value='Perceived Color Saturation' where policykey='policies.pcvdcolsat/text' and value='Perceived Colour Saturation';

commit;
