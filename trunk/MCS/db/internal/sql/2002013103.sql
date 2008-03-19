rem ---------------------------------------------------------------------------
rem $Header: /src/voyager/db/internal/sql/2002013103.sql,v 1.1 2002/02/04 13:22:40 adrian Exp $
rem ---------------------------------------------------------------------------
rem (c) Volantis Systems Ltd 2000. 
rem ---------------------------------------------------------------------------
rem Change History:
rem
rem Date         Who             Description
rem ---------    --------------- ----------------------------------------------
rem 04-Feb-02    Adrian          VBM:2002013103 - SQL script to add a new
rem                              property list-caption-align for themes form
rem                              tab.  Provided user ability to align checkbox
rem                              and radio captions on the left or right of the
rem                              input button/box
rem ---------------------------------------------------------------------------


INSERT INTO vmpropertyeditors (grp, property, attribute, value) VALUES ('stylePolicies', 'list-caption-align', 'category', 'forms');
INSERT INTO vmpropertyeditors (grp, property, attribute, value) VALUES ('stylePolicies', 'list-caption-align', 'type', 'select');
INSERT INTO vmpropertyeditors (grp, property, attribute, value) VALUES ('stylePolicies', 'list-caption-align', 'editable', 'false');
INSERT INTO vmpropertyeditors (grp, property, attribute, value) VALUES ('stylePolicies', 'list-caption-align', 'count', '2');
INSERT INTO vmpropertyeditors (grp, property, attribute, value) VALUES ('stylePolicies', 'list-caption-align', '0', 'left');
INSERT INTO vmpropertyeditors (grp, property, attribute, value) VALUES ('stylePolicies', 'list-caption-align', '1', 'right');
INSERT INTO vmpropertyeditors (grp, property, attribute, value) VALUES ('stylePolicies', 'list-caption-align', 'defaultValue', 'left');
INSERT INTO vmpropertyeditors (grp, property, attribute, value) VALUES ('stylePolicies', 'list-caption-align', 'cssExclude', 'true');

INSERT INTO vmresourcebundles (bundle, value, policykey) VALUES ('stylePolicies', 'list-caption-align', 'policies.list-caption-align/text');

commit;

