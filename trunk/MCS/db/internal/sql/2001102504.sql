-- ---------------------------------------------------------------------------
-- $Header: /src/voyager/db/internal/sql/2001102504.sql,v 1.1 2001/12/27 14:51:21 jason Exp $
-- ---------------------------------------------------------------------------
-- (c) Volantis Systems Ltd 2000. 
-- ---------------------------------------------------------------------------
-- Change History:
--
-- Date         Who             Description
-- ---------    --------------- ----------------------------------------------
-- 25-Oct-01    Allan            VBM:2001102504 - Created.
-- 23-Nov-01    Allan            VBM:2001102504 - Added categories and content
--                               from category tables.
-- 26-Nov-01    Allan            VBM:2001102504 - Added prefix of ! on 
--                               category names.
-- 05-Dec-01    Allan            VBM:2001120502 - Commented out script and
--                               escape categories since these do not yet
--                               exist.
-- ---------------------------------------------------------------------------
drop table VMPolicyDescriptor;
create table VMPolicyDescriptor
(
        name    varchar(254) not null,
        parent  varchar(254) not null,
        type    varchar(64) not null,
        CONSTRAINT PK_VMPolicyDescriptor PRIMARY KEY (name, parent, type)
);

-- Layouts
insert into VMPolicyDescriptor values('!Layouts','/','Category');
insert into VMPolicyDescriptor select unique layout, '/!Layouts', '!Layouts' from VMDevice_Layouts;

-- Themes
insert into VMPolicyDescriptor values('!Themes','/','Category');
insert into VMPolicyDescriptor select unique name, '/!Themes', '!Themes' from VMThemes;

-- Components
insert into VMPolicyDescriptor values('!Components','/','Category');

-- Audio Components
insert into VMPolicyDescriptor values('AudioComponents','/!Components','Category');
insert into VMPolicyDescriptor select unique name, '/!Components/AudioComponents','!Components/AudioComponents' from VMAudioComponent;

-- Asset Groups
insert into VMPolicyDescriptor values('AssetGroups','/!Components','Category');
insert into VMPolicyDescriptor select unique name, '/!Components/AssetGroups','!Components/AssetGroups' from VMAssetGroup;

-- Button Image Components
insert into VMPolicyDescriptor values('ButtonImageComponents','/!Components','Category');
insert into VMPolicyDescriptor select unique name, '/!Components/ButtonImageComponents','!Components/ButtonImageComponents' from VMButtonImageComponent;

-- Chart Components
insert into VMPolicyDescriptor values('ChartComponents','/!Components','Category');
insert into VMPolicyDescriptor select unique name, '/!Components/ChartComponents','!Components/ChartComponents' from VMChartComponent;

-- Dynamic Visual Components
insert into VMPolicyDescriptor values('DynamicVisualComponents','/!Components','Category');
insert into VMPolicyDescriptor select unique name, '/!Components/DynamicVisualComponents','!Components/DynamicVisualComponents' from VMDynamicVisualComponent;

-- Escape Components
--insert into VMPolicyDescriptor values('EscapeComponents','/!Components','Category');
--insert into VMPolicyDescriptor select unique name, '/!Components/EscapeComponents','!Components/EscapeComponents' from VMEscapeComponent;

-- Image Components
insert into VMPolicyDescriptor values('ImageComponents','/!Components','Category');
insert into VMPolicyDescriptor select unique name, '/!Components/ImageComponents','!Components/ImageComponents' from VMImageComponent;

-- Link Components
insert into VMPolicyDescriptor values('LinkComponents','/!Components','Category');
insert into VMPolicyDescriptor select unique name, '/!Components/LinkComponents','!Components/LinkComponents' from VMLinkComponent;

-- Rollover Image Components
insert into VMPolicyDescriptor values('RolloverImageComponents','/!Components','Category');
insert into VMPolicyDescriptor select unique name, '/!Components/RolloverImageComponents','!Components/RolloverImageComponents' from VMRolloverImageComponent;

-- Script Components
--insert into VMPolicyDescriptor values('ScriptComponents','/!Components','Category');
--insert into VMPolicyDescriptor select unique name, '/!Components/ScriptComponents','!Components/ScriptComponents' from VMScriptComponent;

-- Text Components
insert into VMPolicyDescriptor values('TextComponents','/!Components','Category');
insert into VMPolicyDescriptor select unique name, '/!Components/TextComponents','!Components/TextComponents' from VMTextComponent;




commit;
