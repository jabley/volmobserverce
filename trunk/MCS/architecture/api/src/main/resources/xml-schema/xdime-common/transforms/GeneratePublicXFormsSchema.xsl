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
 ! This XSLT generates the external XHTML 2 schema individual modules.
 ! ***************************************************************************************************
 !
 !-->
<xsl:stylesheet xmlns:common="http://exslt.org/common" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0" exclude-result-prefixes="common xsi">

    <!-- Output as xml using xalan specific extensions. -->
    <xsl:output xmlns:xalan="http://xml.apache.org/xslt"
                xalan:indent-amount="4"
                encoding="us-ascii"
                indent="yes"
                method="xml"/>

    <xsl:include href="../../xdime-common/transforms/GeneratePublicSchemaCore.xsl"/>

    <xsl:template name="GetPublicComment">
        <xsl:comment> ==========================================================================
 ! (c) Volantis Systems Ltd 2004. 
 ! ============================================================================
 ! This document defines the subset of XForms that is supported within the
 ! XDIME CP Language.
 ! ======================================================================== </xsl:comment>
    </xsl:template>

    <!-- Include all the XForms modules when included from here. -->

    <xsl:template match="xs:include[contains(@schemaLocation, 'modules/xforms-common-base.xsd')]">
        <xsl:call-template name="IncludeSchema">
            <xsl:with-param name="heading">Common Module</xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template match="xs:include[contains(@schemaLocation, 'modules/xforms-core-module.xsd')]">
        <xsl:call-template name="IncludeSchema">
            <xsl:with-param name="heading">Core Module</xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template match="xs:include[contains(@schemaLocation, 'modules/xforms-controls-module.xsd')]">
        <xsl:call-template name="IncludeSchema">
            <xsl:with-param name="heading">Controls Module</xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template match="xs:include[contains(@schemaLocation, 'modules/xforms-group-module.xsd')]">
        <xsl:call-template name="IncludeSchema">
            <xsl:with-param name="heading">Group Module</xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <!-- Purposely ignore all the XForms modules when included from other modules. -->
    <xsl:template match="xs:include[starts-with(@schemaLocation, 'xforms-')]"/>

    <!-- Import other schema when imported from top level schema. -->
    <xsl:template match="xs:import[@schemaLocation = 'xhtml2.xsd']">
        <xsl:call-template name="ImportSchema"/>
    </xsl:template>

    <!-- Purposely ignore XHTML 2 common base module. -->
    <xsl:template match="xs:import[contains(@schemaLocation, 'xhtml2-common-base.xsd')]"/>

</xsl:stylesheet>
