# VBM:2001102303 Need to addded to enable horizonal ruler emulation in WML.

insert into vmpropertyeditors (PROPERTY,GRP,ATTRIBUTE,VALUE)
values ('protocol.wml.emulate.horizontal','devicePolicies','category','protocol');

insert into vmpropertyeditors (PROPERTY,GRP,ATTRIBUTE,VALUE)
values ('protocol.wml.emulate.horizontal','devicePolicies','type','boolean');

insert into VMRESOURCEBUNDLES (BUNDLE,VALUE,POLICYKEY) 
values ('devicePolicies','Emulate WML Horizontal ruler','policies.protocol.wml.emulate.horizontal/text');

insert into VMRESOURCEBUNDLES (BUNDLE,VALUE,POLICYKEY)  
values ('devicePolicies','Emulate WML Horizontal ruler','policies.protocol.wml.emulate.horizontal/short');

insert into VMPOLICY_VALUES (NAME,POLICY,VALUE,REVISION) 
values ('Master','protocol.wml.emulate.horizontal','true','0')
