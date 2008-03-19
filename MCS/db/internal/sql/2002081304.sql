rem ---------------------------------------------------------------------------
rem $Header: /src/voyager/db/internal/sql/2002081304.sql,v 1.3 2002/10/11 09:47:31 ianw Exp $
rem ---------------------------------------------------------------------------
rem (c) Volantis Systems Ltd 2000. 
rem ---------------------------------------------------------------------------
rem Change History:
rem
rem Date         Who             Description
rem ---------    --------------- ----------------------------------------------
rem 04-Sep-02    Ian             VBM:2002081304 - Created this script to update
rem                              jdbc repository with MTS GUI updates.
rem 11-Oct-02    Ian             VBM:2002081304 - Fixed not enough values.
rem ---------------------------------------------------------------------------

INSERT INTO VMRESOURCEBUNDLES ( BUNDLE, VALUE, REVISION, POLICYKEY ) 
 VALUES ( 'devicePolicies', 'Rules', 1, 'category.rules/text' ); 

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'deviceCategories', 'rules', 'prefix', NULL, 1 ); 

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'cpng24rule', 'defaultValue', 'cp24', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'cpng24rule', 'type', 'text', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'cpng24rule', 'category', 'rules', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'cgif8rule', 'type', 'text', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'cgif8rule', 'category', 'rules', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'cpng8rule', 'defaultValue', 'cp8',	1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'gjpeg8rule', 'type', 'text', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'gjpeg8rule', 'category', 'rules', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'gpng16rule', 'defaultValue', 'gp16', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'cjpeg24rule', 'defaultValue', 'cj24', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'cjpeg24rule', 'type', 'text', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'cjpeg24rule', 'category', 'rules',	1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'cgif8rule', 'defaultValue', 'cg8',	1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'cpng8rule', 'type', 'text', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'cpng8rule', 'category', 'rules', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'gjpeg8rule', 'defaultValue', 'gj8', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'gpng16rule', 'type', 'text', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'gpng16rule', 'category', 'rules', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'gpng8rule', 'defaultValue', 'gp8', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'gpng8rule', 'type', 'text', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'gpng8rule', 'category', 'rules', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'ggif8rule', 'defaultValue', 'gg8', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'ggif8rule', 'type', 'text', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'ggif8rule', 'category', 'rules', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'gpng4rule', 'defaultValue', 'gp4',	1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'gpng4rule', 'type', 'text', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'gpng4rule', 'category', 'rules', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'ggif4rule', 'defaultValue', 'gg4',	1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'ggif4rule', 'type', 'text', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'ggif4rule', 'category', 'rules', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'gpng2rule', 'defaultValue', 'gp2', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'gpng2rule', 'type', 'text', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'gpng2rule', 'category', 'rules', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'ggif2rule', 'defaultValue', 'gg2', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'ggif2rule', 'type', 'text', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'ggif2rule', 'category', 'rules', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'gpng1rule', 'defaultValue', 'gp1',	1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'gpng1rule', 'type', 'text', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'gpng1rule', 'category', 'rules', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'gwbmp1rule', 'type', 'text', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'gwbmp1rule', 'category', 'rules', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'gwbmp1rule', 'defaultValue', 'gw1', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'ggif1rule', 'defaultValue', 'gg1', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'ggif1rule', 'type', 'text', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'ggif1rule', 'category', 'rules', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'preferredimagetype', 'count', '7', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'preferredimagetype', '6', 'WBMP', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'preferredimagetype', 'defaultValue', 'JPEG', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'preferredimagetype', 'category', 'image', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'preferredimagetype', 'type', 'select', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'preferredimagetype', '5', 'TIFF', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'preferredimagetype', '4', 'PNG', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'preferredimagetype', '3', 'PJPEG', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'preferredimagetype', '2', 'JPEG', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'preferredimagetype', '1', 'GIF', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies', 'preferredimagetype', '0', 'BMP', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies' ,'tiffinpage', 'defaultValue' ,'false' ,1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies' ,'tiffinpage' ,'type' ,'boolean', 1 );

INSERT INTO VMPROPERTYEDITORS ( GRP, PROPERTY, ATTRIBUTE, VALUE,REVISION ) 
 VALUES ( 'devicePolicies' ,'tiffinpage' ,'category' ,'image', 1 );

INSERT INTO VMPOLICY_VALUES ( NAME, POLICY, VALUE, REVISION )
 VALUES ( 'Master', 'preferredimagetype', 'JPEG', 1 );

INSERT INTO VMPOLICY_VALUES ( NAME, POLICY, VALUE, REVISION )
 VALUES ( 'Master', 'cjpeg24rule', 'cj24', 1 );

INSERT INTO VMPOLICY_VALUES ( NAME, POLICY, VALUE, REVISION )
 VALUES ( 'Master', 'cpng24rule', 'cp24', 1 );

INSERT INTO VMPOLICY_VALUES ( NAME, POLICY, VALUE, REVISION )
 VALUES ( 'Master', 'cpng8rule', 'cp8', 1 );

INSERT INTO VMPOLICY_VALUES ( NAME, POLICY, VALUE, REVISION )
 VALUES ( 'Master', 'cgif8rule', 'cg8', 1 );

INSERT INTO VMPOLICY_VALUES ( NAME, POLICY, VALUE, REVISION )
 VALUES ( 'Master', 'gjpeg8rule', 'gj8', 1 );

INSERT INTO VMPOLICY_VALUES ( NAME, POLICY, VALUE, REVISION )
 VALUES ( 'Master', 'gpng16rule', 'gp16', 1 );

INSERT INTO VMPOLICY_VALUES ( NAME, POLICY, VALUE, REVISION )
 VALUES ( 'Master', 'gpng8rule', 'gp8', 1 );

INSERT INTO VMPOLICY_VALUES ( NAME, POLICY, VALUE, REVISION )
 VALUES ( 'Master', 'ggif8rule', 'gg8', 1 );

INSERT INTO VMPOLICY_VALUES ( NAME, POLICY, VALUE, REVISION )
 VALUES ( 'Master', 'gpng4rule', 'gp4', 1 );

INSERT INTO VMPOLICY_VALUES ( NAME, POLICY, VALUE, REVISION )
 VALUES ( 'Master', 'ggif4rule', 'gg4', 1 );

INSERT INTO VMPOLICY_VALUES ( NAME, POLICY, VALUE, REVISION )
 VALUES ( 'Master', 'gpng2rule', 'gp2', 1 );

INSERT INTO VMPOLICY_VALUES ( NAME, POLICY, VALUE, REVISION )
 VALUES ( 'Master', 'ggif2rule', 'gg2', 1 );

INSERT INTO VMPOLICY_VALUES ( NAME, POLICY, VALUE, REVISION )
 VALUES ( 'Master', 'gpng1rule', 'gp1', 1 );

INSERT INTO VMPOLICY_VALUES ( NAME, POLICY, VALUE, REVISION )
 VALUES ( 'Master', 'ggif1rule', 'gg1', 1 );

INSERT INTO VMPOLICY_VALUES ( NAME, POLICY, VALUE, REVISION )
 VALUES ( 'Master', 'gwbmp1rule', 'gw1', 1 );

INSERT INTO VMPOLICY_VALUES ( NAME, POLICY, VALUE, REVISION )
 VALUES ( 'Master', 'tiffinpage', 'false', 1 );

INSERT INTO VMRESOURCEBUNDLES ( BUNDLE, VALUE, POLICYKEY, REVISION )
 VALUES ( 'devicePolicies', '4 Bit Grey Scale GIF Rule Name', 'policies.ggif4rule/text', 1 );

INSERT INTO VMRESOURCEBUNDLES ( BUNDLE, VALUE, POLICYKEY, REVISION )
 VALUES ( 'devicePolicies', 'Transcoding rule for 2bit GIF greyscale images.', 'policies.ggif2rule/short', 1 );

INSERT INTO VMRESOURCEBUNDLES ( BUNDLE, VALUE, POLICYKEY, REVISION )
 VALUES ( 'devicePolicies', '4 Bit Grey Scale PNG Rule Name', 'policies.gpng4rule/text', 1 );

INSERT INTO VMRESOURCEBUNDLES ( BUNDLE, VALUE, POLICYKEY, REVISION )
 VALUES ( 'devicePolicies', '24 Bit Color PNG Rule Name', 'policies.cpng24rule/text', 1 );

INSERT INTO VMRESOURCEBUNDLES ( BUNDLE, VALUE, POLICYKEY, REVISION )
 VALUES ( 'devicePolicies', 'Transcoding rule for 8bit PNG color images.', 'policies.cpng8rule/short', 1 );

INSERT INTO VMRESOURCEBUNDLES ( BUNDLE, VALUE, POLICYKEY, REVISION )
 VALUES ( 'devicePolicies', 'Transcoding rule for 8bit PNG greyscale images.', 'policies.gpng8rule/short', 1 );

INSERT INTO VMRESOURCEBUNDLES ( BUNDLE, VALUE, POLICYKEY, REVISION )
 VALUES ( 'devicePolicies', 'Transcoding rule for 4bit PNG greyscale images.', 'policies.gpng4rule/short', 1 );

INSERT INTO VMRESOURCEBUNDLES ( BUNDLE, VALUE, POLICYKEY, REVISION )
 VALUES ( 'devicePolicies', '24 Bit Color JPEG Rule Name', 'policies.cjpeg24rule/text', 1 );

INSERT INTO VMRESOURCEBUNDLES ( BUNDLE, VALUE, POLICYKEY, REVISION )
 VALUES ( 'devicePolicies', '1 Bit Grey Scale GIF Rule Name', 'policies.ggif1rule/text', 1 );

INSERT INTO VMRESOURCEBUNDLES ( BUNDLE, VALUE, POLICYKEY, REVISION )
 VALUES ( 'devicePolicies', '8 Bit Grey Scale JPEG Rule Name', 'policies.gjpeg8rule/text', 1 );

INSERT INTO VMRESOURCEBUNDLES ( BUNDLE, VALUE, POLICYKEY, REVISION )
 VALUES ( 'devicePolicies', 'Transcoding rule for 1bit WBMP greyscale images.', 'policies.gwbmp1rule/short', 1 );

INSERT INTO VMRESOURCEBUNDLES ( BUNDLE, VALUE, POLICYKEY, REVISION )
 VALUES ( 'devicePolicies', 'Transcoding rule for 16bit PNG greyscale images.', 'policies.gpng16rule/short', 1 );

INSERT INTO VMRESOURCEBUNDLES ( BUNDLE, VALUE, POLICYKEY, REVISION )
 VALUES ( 'devicePolicies', '2 Bit Grey Scale GIF Rule Name', 'policies.ggif2rule/text', 1 );

INSERT INTO VMRESOURCEBUNDLES ( BUNDLE, VALUE, POLICYKEY, REVISION )
 VALUES ( 'devicePolicies', 'Transcoding rule for 2bit PNG greyscale images.', 'policies.gpng2rule/short', 1 );

INSERT INTO VMRESOURCEBUNDLES ( BUNDLE, VALUE, POLICYKEY, REVISION )
 VALUES ( 'devicePolicies', 'Transcoding rule for 24bit PNG color images.', 'policies.cpng24rule/short', 1 );

INSERT INTO VMRESOURCEBUNDLES ( BUNDLE, VALUE, POLICYKEY, REVISION )
 VALUES ( 'devicePolicies', 'Transcoding rule for 8bit JPEG greyscale images.', 'policies.gjpeg8rule/short', 1 );

INSERT INTO VMRESOURCEBUNDLES ( BUNDLE, VALUE, POLICYKEY, REVISION )
 VALUES ( 'devicePolicies', 'Transcoding rule for 24bit JPEG color images.', 'policies.cjpeg24rule/short', 1 );

INSERT INTO VMRESOURCEBUNDLES ( BUNDLE, VALUE, POLICYKEY, REVISION )
 VALUES ( 'devicePolicies', '8 Bit Grey Scale GIF Rule Name', 'policies.ggif8rule/text', 1 );

INSERT INTO VMRESOURCEBUNDLES ( BUNDLE, VALUE, POLICYKEY, REVISION )
 VALUES ( 'devicePolicies', '1 Bit WBMP Rule Name', 'policies.gwbmp1rule/text', 1 );

INSERT INTO VMRESOURCEBUNDLES ( BUNDLE, VALUE, POLICYKEY, REVISION )
 VALUES ( 'devicePolicies', '1 Bit Grey Scale PNG Rule Name', 'policies.gpng1rule/text', 1 );

INSERT INTO VMRESOURCEBUNDLES ( BUNDLE, VALUE, POLICYKEY, REVISION )
 VALUES ( 'devicePolicies', 'Transcoding rule for 1bit GIF greyscale images.', 'policies.ggif1rule/short', 1 );

INSERT INTO VMRESOURCEBUNDLES ( BUNDLE, VALUE, POLICYKEY, REVISION )
 VALUES ( 'devicePolicies', 'Transcoding rule for 8bit GIF greyscale images.', 'policies.ggif8rule/short', 1 );

INSERT INTO VMRESOURCEBUNDLES ( BUNDLE, VALUE, POLICYKEY, REVISION )
 VALUES ( 'devicePolicies', 'Transcoding rule for 4bit GIF greyscale images.', 'policies.ggif4rule/short', 1 );

INSERT INTO VMRESOURCEBUNDLES ( BUNDLE, VALUE, POLICYKEY, REVISION )
 VALUES ( 'devicePolicies', '8 Bit Color PNG Rule Name', 'policies.cpng8rule/text', 1 );

INSERT INTO VMRESOURCEBUNDLES ( BUNDLE, VALUE, POLICYKEY, REVISION )
 VALUES ( 'devicePolicies', 'Transcoding rule for 1bit PNG greyscale images.', 'policies.gpng1rule/short', 1 );

INSERT INTO VMRESOURCEBUNDLES ( BUNDLE, VALUE, POLICYKEY, REVISION )
 VALUES ( 'devicePolicies', '8 Bit Color GIF Rule Name', 'policies.cgif8rule/text', 1 );

INSERT INTO VMRESOURCEBUNDLES ( BUNDLE, VALUE, POLICYKEY, REVISION )
 VALUES ( 'devicePolicies', 'Transcoding rule for 24bit PNG color images.', 'policies.cgif8rule/short', 1 );

INSERT INTO VMRESOURCEBUNDLES ( BUNDLE, VALUE, POLICYKEY, REVISION )
 VALUES ( 'devicePolicies', '2 Bit Grey Scale PNG Rule Name', 'policies.gpng2rule/text', 1 );

INSERT INTO VMRESOURCEBUNDLES ( BUNDLE, VALUE, POLICYKEY, REVISION )
 VALUES ( 'devicePolicies', '8 Bit Grey Scale PNG Rule Name', 'policies.gpng8rule/text', 1 );

INSERT INTO VMRESOURCEBUNDLES ( BUNDLE, VALUE, POLICYKEY, REVISION )
 VALUES ( 'devicePolicies', '16 Bit Grey Scale PNG Rule Name', 'policies.gpng16rule/text', 1 );

INSERT INTO VMRESOURCEBUNDLES ( BUNDLE, VALUE, POLICYKEY, REVISION )
 VALUES ( 'devicePolicies', 'Preferred Image Type', 'policies.preferredimagetype/text', 1 );

INSERT INTO VMRESOURCEBUNDLES ( BUNDLE, VALUE, POLICYKEY, REVISION )
 VALUES ( 'devicePolicies', 'The preferred image encoding for this device.', 'policies.preferredimagetype/short', 1 );

INSERT INTO VMRESOURCEBUNDLES ( BUNDLE, VALUE, POLICYKEY, REVISION )
 VALUES ( 'devicePolicies', 'TIFF images within web pages', 'policies.tiffinpage/text', 1 );

INSERT INTO VMRESOURCEBUNDLES ( BUNDLE, VALUE, POLICYKEY, REVISION )
 VALUES ( 'devicePolicies', 'Can TIFF images be displayed as part of a web page?', 'policies.tiffinpage/short', 1 );



commit;
