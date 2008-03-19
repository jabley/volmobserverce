<?xml version="1.0" encoding="UTF-8"?>
<!--
This file is part of Volantis Mobility Server. 

Volantis Mobility Server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Volantis Mobility Server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Volantis Mobility Server.  If not, see <http://www.gnu.org/licenses/>. 
-->

<!-- ==========================================================================
 ! $Header: 
 ! ============================================================================
 ! (c) Volantis Systems Ltd 2003. 
 ! ============================================================================
 ! Change History:
 !
 ! Date         Who             Description
 ! =========    =============== ===============================================
 ! ======================================================================== -->
<!--
 !
 ! ***************************************************************************************************
 ! This file is owned by Architecture and can only be changed by a member of
 ! that group.
 !
 ! This XSLT generates the external description of remote policies from the individual modules.
 ! ***************************************************************************************************
 !
 !-->
<xsl:stylesheet xmlns:common="http://exslt.org/common" xmlns:thm="http://vine.uk.volantis.com/architecture/document/xmlns/ThemeInfo" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" version="1.0" exclude-result-prefixes="common thm">

    <!-- Output as xml using xalan specific extensions. -->
    <xsl:output xmlns:xalan="http://xml.apache.org/xslt"
                xalan:indent-amount="4"
                encoding="us-ascii"
                indent="yes"
                method="xml"/>

    <xsl:include href="GeneratePublicSchemaCore.xsl"/>

    <!-- Remember the root target namespace. -->
    <xsl:variable name="rootTargetNamespace" select="/xs:schema/@targetNamespace"/>

    <xsl:variable name="themeProperties" select="document('../themes/themePropertyDefinitions.xml')/thm:themeDefinition"/>

    <!--
     ! Create a schema element here so that it picks up the namespace declarations.
     !
     ! The problem that this is trying to solve is that a schema module includes
     ! schemata in different namespaces. If this is not done then every element
     ! from within that module has namespace declarations replicated on them.
     ! 
     ! Have to add a default namespace, otherwise the resulting schema is not valid.
     !-->
    <xsl:template match="xs:schema">
        <xs:schema xmlns="http://www.volantis.com/xmlns/2006/02/marlin-lpdm"
                   xmlns:lpdm="http://www.volantis.com/xmlns/2006/02/marlin-lpdm"
                   xmlns:mdv="http://www.volantis.com/xmlns/2004/12/meta-data-values"
                   xmlns:mdt="http://www.volantis.com/xmlns/2004/12/meta-data-types">

            <xsl:apply-templates select="@*"/>
            <xsl:apply-templates/>
        </xs:schema>
    </xsl:template>

    <xsl:template name="GetPublicComment">
        <xsl:comment> ==========================================================================
 ! (c) Volantis Systems Ltd 2001. 
 ! ============================================================================
 ! This document 
 ! ======================================================================== </xsl:comment>
    </xsl:template>

    <!-- Include the extended layout module. -->
    <xsl:template match="xs:include[@schemaLocation = 'modules/layout-description-module-internal.xsd']">
        <xsl:comment>
     ! **********************************************************************
     !     Layout Descriptions 
     ! **********************************************************************
     !</xsl:comment>
 
        <xsl:call-template name="IncludeSchema"/> 
    </xsl:template>

    <!-- Include the xml-device-theme-internal.xsd module. -->
    <xsl:template match="xs:include[@schemaLocation = 'xml-device-theme-internal.xsd']">

        <xsl:comment>
     ! **********************************************************************
     !     XML Device Theme Descriptions 
     ! **********************************************************************
     !</xsl:comment>
 
        <xsl:call-template name="IncludeSchema"/> 
    </xsl:template>

    <!-- Purposely ignore the common-layout-theme-types-internal.xsd module when it is included from within another module. -->
    <xsl:template match="xs:include[@schemaLocation = 'common-layout-theme-types-internal.xsd']"/>

    <!-- Include the common-layout-theme-types-internal.xsd module when it is included from marlin-lpdm-internal.xsd. -->
    <xsl:template match="xs:include[@schemaLocation = 'modules/common-layout-theme-types-internal.xsd']">

        <xsl:comment>
     ! **********************************************************************
     !     Common Layout / Theme Descriptions 
     ! **********************************************************************
     !</xsl:comment>
 
        <xsl:call-template name="IncludeSchema"/> 
    </xsl:template>

    <xsl:template match="xs:import[substring-before(@schemaLocation, 'meta-data-values.xsd') or substring-before(@schemaLocation, 'meta-data-types.xsd')]">
        <xs:import>
            <xsl:attribute name="schemaLocation">
                <xsl:text>../../2004/12/</xsl:text>
                <xsl:value-of select="substring-after(@schemaLocation, '../../../meta/')"/>
            </xsl:attribute>
            <xsl:apply-templates select="@namespace"/>
        </xs:import>
    </xsl:template>

    <!--
     ! If this is the top level schema then include the core module,
     ! otherwise do not. A top level schema is distinguished by its
     ! targetNamespace.
     !-->
    <xsl:template match="xs:include[contains(@schemaLocation, 'core-module-internal.xsd')]">
        <xsl:if test="parent::node()/@targetNamespace = $rootTargetNamespace">

            <xsl:comment>
     ! **********************************************************************
     !     Core Descriptions
     ! **********************************************************************
     !</xsl:comment>

            <xsl:call-template name="IncludeSchema"/>
        </xsl:if>
    </xsl:template>

    <!--
     ! If this is the top level schema then include the core module,
     ! otherwise do not. A top level schema is distinguished by its
     ! targetNamespace.
     !-->
    <xsl:template match="xs:include[contains(@schemaLocation, 'policy-type-module.xsd')]">
        <xsl:if test="parent::node()/@targetNamespace = $rootTargetNamespace">
            <xsl:call-template name="IncludeSchema"/>
        </xsl:if>
    </xsl:template>

    <!-- Include the component module. -->
    <xsl:template match="xs:include[@schemaLocation = 'modules/component-description-module-internal.xsd']">
        <xsl:comment>
     ! **********************************************************************
     !     Component And Asset Descriptions
     ! **********************************************************************
     !</xsl:comment>

        <xsl:call-template name="IncludeSchema"/>
    </xsl:template>

    <!-- Include the component module. -->
    <xsl:template match="xs:include[@schemaLocation = 'modules/policy-description-module-internal.xsd']">
        <xsl:call-template name="IncludeSchema"/>
    </xsl:template>

    <!-- Include the theme module. -->
    <xsl:template match="xs:include[@schemaLocation = 'modules/theme-description-module-internal.xsd']">
        <xsl:comment>
     ! **********************************************************************
     !     Theme Descriptions
     ! **********************************************************************
     !</xsl:comment>

        <xsl:call-template name="IncludeSchema"/>
    </xsl:template>

</xsl:stylesheet>
