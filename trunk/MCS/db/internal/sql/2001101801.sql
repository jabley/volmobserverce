-- ---------------------------------------------------------------------------
-- $Header: /src/voyager/db/internal/sql/2001101801.sql,v 1.1 2001/12/27 14:51:21 jason Exp $
-- ---------------------------------------------------------------------------
-- (c) Volantis Systems Ltd 2000. 
-- ---------------------------------------------------------------------------
-- Change History:
--
-- Date         Who             Description
-- ---------    --------------- ----------------------------------------------
-- 18-Oct-01    Allan           VBM:2001101801 - Created. Make null the 
--                              default value for the text-decoration style
-- ---------------------------------------------------------------------------

delete from propertyeditors
       where property = 'text-decoration'
         and attribute in ('0','1','2','3','4','count','defaultValue');

insert into propertyeditors (grp,property,attribute,value)
       values ('stylePolicies','text-decoration','0',null);
insert into propertyeditors (grp,property,attribute,value)
       values ('stylePolicies','text-decoration','1','none');
insert into propertyeditors (grp,property,attribute,value)
       values ('stylePolicies','text-decoration','2','underline');
insert into propertyeditors (grp,property,attribute,value)
       values ('stylePolicies','text-decoration','3','overline');
insert into propertyeditors (grp,property,attribute,value)
       values ('stylePolicies','text-decoration','4','line-through');
insert into propertyeditors (grp,property,attribute,value)
       values ('stylePolicies','text-decoration','5','blink');
insert into propertyeditors (grp,property,attribute,value)
       values ('stylePolicies','text-decoration','count','6');

