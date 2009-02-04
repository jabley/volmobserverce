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
 ! This XSLT generates external schemata from internal versions.
 ! ***************************************************************************************************
 !
 !-->
<xsl:stylesheet xmlns:common="http://exslt.org/common" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0" exclude-result-prefixes="common">

    <!--
     ! Import the schema post process XSL that will by default copy the
     ! whole schema and restructure it to ensure that it is valid.
     !-->
    <xsl:import href="../../utilities/transforms/PostProcessSchema.xsl"/>

    <!-- Output as xml using xalan specific extensions. -->
    <xsl:output xmlns:xalan="http://xml.apache.org/xslt"
                xalan:indent-amount="4"
                encoding="us-ascii"
                indent="yes"
                method="xml"/>

    <!-- Remember the root target namespace. -->
    <xsl:variable name="rootTargetNamespace" select="/xs:schema/@targetNamespace"/>

    <xsl:template match="/">
        <!-- Add public comment. -->
<!--        <xsl:call-template name="GetPublicComment"/>-->

        <xsl:apply-templates/>
    </xsl:template>

    <!-- Comments are ignored by default -->
    <xsl:template match="comment()"/>

    <!-- Comments that are immediate children of xs:schema are copied. -->
    <xsl:template match="xs:schema/comment()">
        <xsl:comment> ======================================================================
     !         <xsl:value-of select="normalize-space(translate(., '*!=', ''))"/>
     ! ==================================================================== </xsl:comment>
    </xsl:template>

    <!-- Remove -internal from schemaLocation attributes. -->
    <xsl:template match="@schemaLocation[contains(., '-internal')]">
        <xsl:attribute name="{name()}">
            <xsl:value-of select="substring-before(., '-internal')"/>
            <xsl:value-of select="substring-after(., '-internal')"/>
        </xsl:attribute>
    </xsl:template>

</xsl:stylesheet>
