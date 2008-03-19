rem ---------------------------------------------------------------------------
rem $Header: /src/voyager/db/internal/sql/insert_xfform_styles.sql,v 1.1 2001/12/27 14:51:22 jason Exp $
rem ---------------------------------------------------------------------------
rem (c) Volantis Systems Ltd 2000. 
rem ---------------------------------------------------------------------------
rem Change History:
rem
rem Date         Who             Description
rem ---------    --------------- ----------------------------------------------
rem 25-Jul-01    Paul            VBM:2001071103 - Created.
rem ---------------------------------------------------------------------------

insert into propertyeditors (grp,property,attribute,value)
  values ('stylePolicies','rows','category','size')
insert into propertyeditors (grp,property,attribute,value)
  values ('stylePolicies','rows','type','int')
insert into propertyeditors (grp,property,attribute,value)
  values ('stylePolicies','rows','defaultValue','1')
insert into propertyeditors (grp,property,attribute,value)
  values ('stylePolicies','columns','category','size')
insert into propertyeditors (grp,property,attribute,value)
  values ('stylePolicies','columns','type','int')
insert into propertyeditors (grp,property,attribute,value)
  values ('stylePolicies','columns','defaultValue','20')
insert into propertyeditors (grp,property,attribute,value)
  values ('stylePolicies','list-ui','category','forms')
insert into propertyeditors (grp,property,attribute,value)
  values ('stylePolicies','list-ui','type','select')
insert into propertyeditors (grp,property,attribute,value)
  values ('stylePolicies','list-ui','editable','false')
insert into propertyeditors (grp,property,attribute,value)
  values ('stylePolicies','list-ui','count','3')
insert into propertyeditors (grp,property,attribute,value)
  values ('stylePolicies','list-ui','0','checkbox')
insert into propertyeditors (grp,property,attribute,value)
  values ('stylePolicies','list-ui','1','radio')
insert into propertyeditors (grp,property,attribute,value)
  values ('stylePolicies','list-ui','2','menu')
insert into propertyeditors (grp,property,attribute,value)
  values ('stylePolicies','list-ui','defaultValue','menu')
insert into propertyeditors (grp,property,attribute,value)
  values ('stylePolicies','action-ui','category','forms')
insert into propertyeditors (grp,property,attribute,value)
  values ('stylePolicies','action-ui','type','select')
insert into propertyeditors (grp,property,attribute,value)
  values ('stylePolicies','action-ui','editable','false')
insert into propertyeditors (grp,property,attribute,value)
  values ('stylePolicies','action-ui','count','2')
insert into propertyeditors (grp,property,attribute,value)
  values ('stylePolicies','action-ui','0','button')
insert into propertyeditors (grp,property,attribute,value)
  values ('stylePolicies','action-ui','1','image')
insert into propertyeditors (grp,property,attribute,value)
  values ('stylePolicies','action-ui','defaultValue','button')
insert into propertyeditors (grp,property,attribute,value)
  values ('stylePolicies','action-image','category','forms')
insert into propertyeditors (grp,property,attribute,value)
  values ('stylePolicies','action-image','type','text')
