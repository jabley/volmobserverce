rem ---------------------------------------------------------------------------
rem $Header: /src/voyager/db/internal/sql/2001092501.sql,v 1.1 2001/12/27 14:51:21 jason Exp $
rem ---------------------------------------------------------------------------
rem (c) Volantis Systems Ltd 2000. 
rem ---------------------------------------------------------------------------
rem Change History:
rem
rem Date         Who             Description
rem ---------    --------------- ----------------------------------------------
rem 1-0ct-01    Doug            VBM:2001092501 - Created.
rem ---------------------------------------------------------------------------

update format_attributes set name = 'BackgroundComponent' 
	where name = 'BackgroundImage';

update propertyeditors set value = 'text' where grp = 'stylePolicies' 
	and property = 'background-image' and attribute = 'type';

delete from  propertyeditors where grp = 'stylePolicies'
	and property = 'background-image' and attribute = 'defaultValue';

commit;

delete from propertyeditors where property = 'background-image-type';

commit;

insert into propertyeditors (grp,property,attribute,value)
  values ('stylePolicies','background-image-type','category','background');

insert into propertyeditors (grp,property,attribute,value)
  values ('stylePolicies','background-image-type','type','select');

insert into propertyeditors (grp,property,attribute,value)
  values ('stylePolicies','background-image-type','count','2');

insert into propertyeditors (grp,property,attribute,value)
  values ('stylePolicies','background-image-type','editable','false');

insert into propertyeditors (grp,property,attribute,value)
  values ('stylePolicies','background-image-type','0','Image');

insert into propertyeditors (grp,property,attribute,value)
  values ('stylePolicies','background-image-type','1','Dynamic Visual');

insert into propertyeditors (grp,property,attribute,value)
  values ('stylePolicies','background-image-type','defaultValue','Image');

commit;

update resourcebundles 
  set value = 'background component (Name of a Volantis component, not an image file)' 
  where policykey='policies.background-image/short' and bundle='stylePolicies';

commit;

delete from resourcebundles where bundle='stylePolicies' 
 and policykey='policies.background-image-type/short';

commit;

insert into resourcebundles (bundle, value, policykey) 
 values('stylePolicies', 'background component type (component type of specified background component)', 'policies.background-image-type/short');


delete from resourcebundles where bundle = 'stylePolicies' and 
	policykey = 'policies.background-attachment/text';
commit;
 
insert into resourcebundles (bundle, value, policykey) 
 values('stylePolicies', 'Background Attachment', 'policies.background-attachment/text');


delete from resourcebundles where bundle = 'stylePolicies' and 
	policykey = 'policies.background-color/text';
commit;

insert into resourcebundles (bundle, value, policykey) 
 values('stylePolicies', 'Background Color', 'policies.background-color/text');

delete from resourcebundles where bundle = 'stylePolicies' and 
	policykey = 'policies.background-image/text';
commit;

insert into resourcebundles (bundle, value, policykey) 
 values('stylePolicies', 'Background Component', 'policies.background-image/text');

delete from resourcebundles where bundle = 'stylePolicies' and policykey = 'policies.background-image-type/text';
commit;

insert into resourcebundles (bundle, value, policykey) 
 values('stylePolicies', 'Background Component Type', 'policies.background-image-type/text');

delete from resourcebundles where bundle = 'stylePolicies' and policykey = 'policies.background-position/text';
commit;

insert into resourcebundles (bundle, value, policykey) 
 values('stylePolicies', 'Background Position', 'policies.background-position/text');

delete from resourcebundles where bundle = 'stylePolicies' and policykey = 'policies.background-repeat/text';
commit;

insert into resourcebundles (bundle, value, policykey) 
 values('stylePolicies', 'Background Repeat', 'policies.background-repeat/text');
commit;
