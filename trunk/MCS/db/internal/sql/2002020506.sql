rem ---------------------------------------------------------------------------
rem $Header: /src/voyager/db/internal/sql/2002020506.sql,v 1.2 2002/03/01 15:18:35 payal Exp $
rem ---------------------------------------------------------------------------
rem (c) Volantis Systems Ltd 2000. 
rem ---------------------------------------------------------------------------
rem Change History:
rem
rem Date         Who             Description
rem ---------    --------------- ----------------------------------------------
rem 26-Feb-02    Payal            VBM:200202506 - Created.
rem 01-Mar-02    Payal            VBM:200202506 - updated the values for 
rem                               align and salign properties. 
rem ---------------------------------------------------------------------------


update vmpropertyeditors set value='smaller' where attribute='7' and  property='font-size';

commit;


update vmpropertyeditors set value='left' where attribute='1'and  property='align';  

update vmpropertyeditors set value='right' where attribute='2' and  property='align'; 

update vmpropertyeditors set value='top' where attribute='3'  and  property='align'; 

update vmpropertyeditors set value='bottom' where attribute='4' and  property='align';  

update vmpropertyeditors set value='left' where attribute='1'  and  property='salign';

update vmpropertyeditors set value='right' where attribute='2'  and  property='salign'; 

update vmpropertyeditors set value='top' where attribute='3'  and  property='salign';  

update vmpropertyeditors set value='bottom' where attribute='4'  and  property='salign';

update vmpropertyeditors set value='topleft' where attribute='5'  and  property='salign';

update vmpropertyeditors set value='topright' where attribute='6'  and  property='salign';

update vmpropertyeditors set value='bottomleft' where attribute='7'  and  property='salign'; 

update vmpropertyeditors set value='bottomright' where attribute='8'  and  property='salign';

commit;
