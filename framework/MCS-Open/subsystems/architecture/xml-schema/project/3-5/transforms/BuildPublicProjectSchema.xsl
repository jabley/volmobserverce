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
<xsl:stylesheet xmlns:common="http://exslt.org/common"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" version="1.0" exclude-result-prefixes="common">

    <!-- Output as xml using xalan specific extensions. -->
    <xsl:output xmlns:xalan="http://xml.apache.org/xslt"
                xalan:indent-amount="4"
                encoding="us-ascii"
                indent="yes"
                method="xml"/>

    <xsl:include href="../../../utilities/transforms/AbstractBuildPublicSchema.xsl"/>

    <xsl:template name="GetPublicComment">
        <xsl:comment> ==========================================================================
 ! (c) Volantis Systems Ltd 2006. 
 ! ======================================================================== </xsl:comment>
    </xsl:template>

    <!-- Include the policy type module. -->
    <xsl:template match="xs:include[contains(@schemaLocation, 'policy-type-module.xsd')]">
        <xsl:call-template name="IncludeSchemaAtTopLevel"/>
    </xsl:template>

    <!-- Include the common module. -->
    <xsl:template match="xs:include[@schemaLocation = 'modules/common-module.xsd']">
        <xsl:call-template name="IncludeSchemaAtTopLevel"/>
    </xsl:template>

    <!-- Include the partition module. -->
    <xsl:template match="xs:include[@schemaLocation = 'modules/partition-module.xsd']">
        <xsl:call-template name="IncludeSchemaAtTopLevel"/>
    </xsl:template>

</xsl:stylesheet>
