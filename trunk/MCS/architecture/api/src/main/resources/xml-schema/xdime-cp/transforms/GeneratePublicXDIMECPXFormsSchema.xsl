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
 ! This XSLT generates the external XForms schema from individual modules.
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

    <xsl:include href="../../xdime-common/transforms/GeneratePublicXFormsSchema.xsl"/>

    <!--
     ! Copy the import of the XDIME CP SI and XML Events Schemas.
     !-->
    <xsl:template match="xs:import[@schemaLocation = '../xdime-cp-si.xsd']">
        <xsl:call-template name="ImportSchema"/>
    </xsl:template>

    <xsl:template match="xs:import[@schemaLocation = '../../xdime-common/src/xml-events.xsd']">
        <xsl:call-template name="ImportSchema"/>
    </xsl:template>

    <!--
     ! Change the schema location for the XDIME CP SI and XML Events Schemas to
     ! match the relative locations of the schemata in the build directory.
     !-->
    <xsl:template match="xs:import/@schemaLocation[. = '../xdime-cp-si.xsd']">
        <xsl:attribute name="schemaLocation">xdime-cp-si.xsd</xsl:attribute>
    </xsl:template>

    <xsl:template match="xs:import/@schemaLocation[. = '../../xdime-common/src/xml-events.xsd']">
        <xsl:attribute name="schemaLocation">xml-events.xsd</xsl:attribute>
    </xsl:template>

</xsl:stylesheet>
