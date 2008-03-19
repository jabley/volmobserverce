rem ---------------------------------------------------------------------------
rem $Header: /src/voyager/db/internal/sql/copy_newtables.sql,v 1.1 2001/12/27 14:51:21 jason Exp $
rem ---------------------------------------------------------------------------
rem (c) Volantis Systems Ltd 2000. 
rem ---------------------------------------------------------------------------
rem Change History:
rem
rem Date         Who             Description
rem ---------    --------------- ----------------------------------------------
rem 21-Nov-01    Payal            VBM:2001111202 - Created.
rem ---------------------------------------------------------------------------


insert into vmassetgroup select * from assetgroup;

insert into vmaudioasset select * from audioasset;

insert into vmaudiocomponent select * from audiocomponent;

insert into vmbrplugin_attrs select * from brplugin_attrs;

insert into vmbuttonimagecomponent select * from buttonimagecomponent;

insert into vmchartasset select * from chartasset;

insert into vmchartcomponent select * from chartcomponent;

insert into vmdeviceimageasset select * from deviceimageasset;

insert into vmdevice_layouts select * from device_layouts;

insert into vmdevice_patterns select * from device_patterns;

insert into vmdynamicvisualasset select * from dynamicvisualasset;

insert into vmdynamicvisualcomponent select * from dynamicvisualcomponent;

insert into vmescapecomponent select * from escapecomponent;

insert into vmexternalrepositorydefinition select * from externalrepositorydefinition;

insert into vmformat_attributes select * from format_attributes;

insert into vmgenericimageasset select * from genericimageasset;

insert into vmimagecomponent select * from imagecomponent;

insert into vmlayouts select * from layouts;

insert into vmlayout_formats select * from layout_formats;

insert into vmlinkasset select * from linkasset;

insert into vmlinkcomponent select * from linkcomponent;

insert into vmlocks select * from locks;

insert into vmlock_holders select * from lock_holders;

insert into vmpolicypreference select * from policypreference;

insert into vmpolicy_values select * from policy_values;

insert into vmpropertyeditors select * from propertyeditors;

insert into vmresourcebundles select * from resourcebundles;

insert into vmrevision select * from revision;

insert into vmrolloverimagecomponent  select * from rolloverimagecomponent;

insert into vmscriptcomponent select * from scriptcomponent;

insert into vmsmartlink select * from smartlink;

insert into vmstyles select * from styles;

insert into vmtextasset select * from textasset;

insert into vmtextcomponent select * from textcomponent;

insert into vmtextimg select * from textimg;

insert into vmthemes select * from themes;

insert into vmuserinformation select * from userinformation;

insert into vmvolantis_tabs select * from volantis_tabs;

insert into vmpolicydescriptor select * from policydescriptor;

