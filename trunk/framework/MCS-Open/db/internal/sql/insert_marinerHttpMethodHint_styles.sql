rem ---------------------------------------------------------------------------
rem $Header: /src/voyager/db/internal/sql/insert_marinerHttpMethodHint_styles.sql,v 1.1 2002/05/21 13:06:12 adrian Exp $
rem ---------------------------------------------------------------------------
rem (c) Volantis Systems Ltd 2000. 
rem ---------------------------------------------------------------------------
rem Change History:
rem
rem Date         Who             Description
rem ---------    --------------- ----------------------------------------------
rem 17-May-02    Adrian          VBM:2002021111 - Created this sql script to
rem                              add the mariner-http-method-hint style
rem                              property to the propertyeditors table.
rem ---------------------------------------------------------------------------

insert into vmpropertyeditors (grp,property,attribute,value)
  values ('stylePolicies','mariner-http-method-hint','category','volantis');
insert into vmpropertyeditors (grp,property,attribute,value)
  values ('stylePolicies','mariner-http-method-hint','type','select');
insert into vmpropertyeditors (grp,property,attribute,value)
  values ('stylePolicies','mariner-http-method-hint','editable','false');
insert into vmpropertyeditors (grp,property,attribute,value)
  values ('stylePolicies','mariner-http-method-hint','count','2');
insert into vmpropertyeditors (grp,property,attribute,value)
  values ('stylePolicies','mariner-http-method-hint','0','get');
insert into vmpropertyeditors (grp,property,attribute,value)
  values ('stylePolicies','mariner-http-method-hint','1','post');
insert into vmpropertyeditors (grp,property,attribute,value)
  values ('stylePolicies','mariner-http-method-hint','defaultValue','get');

INSERT INTO vmresourcebundles (bundle, value, policykey) VALUES ('stylePolicies', 'mariner-http-method-hint', 'policies.mariner-http-method-hint/text');

alter table vmgeneralproperties add MRNR_HTTP_MTHD_HINT_SYNTAX numeric(3) DEFAULT -1 NOT NULL;

alter table vmgeneralproperties add MRNR_HTTP_MTHD_HINT_PRIORITY numeric(3) DEFAULT 0 NOT NULL;

alter table vmgeneralproperties add MRNR_HTTP_MTHD_HINT_KEYWD numeric(3) DEFAULT 0 NOT NULL;

commit;

