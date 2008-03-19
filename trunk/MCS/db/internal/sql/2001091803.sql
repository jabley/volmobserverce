REM Make null the default value for text-align.

delete from propertyeditors
       where property = 'text-align'
         and attribute in ('0','1','2','3','count','defaultValue');

insert into propertyeditors (grp,property,attribute,value)
       values ('stylePolicies','text-align','0',null);
insert into propertyeditors (grp,property,attribute,value)
       values ('stylePolicies','text-align','1','left');
insert into propertyeditors (grp,property,attribute,value)
       values ('stylePolicies','text-align','2','center');
insert into propertyeditors (grp,property,attribute,value)
       values ('stylePolicies','text-align','3','right');
insert into propertyeditors (grp,property,attribute,value)
       values ('stylePolicies','text-align','4','justify');
insert into propertyeditors (grp,property,attribute,value)
       values ('stylePolicies','text-align','count','5');
