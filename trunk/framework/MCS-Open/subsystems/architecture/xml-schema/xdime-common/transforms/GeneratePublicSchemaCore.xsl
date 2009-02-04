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
     ! Include the schema post process XSL that will by default copy the
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
        <xsl:call-template name="GetPublicComment"/>

        <xsl:apply-templates/>
    </xsl:template>

    <!-- Comments are ignored by default -->
    <xsl:template match="comment()"/>

    <!-- By default top level comments are copied. -->
    <xsl:template match="xs:schema/comment()">
        <xsl:comment> ======================================================================
     !         <xsl:value-of select="normalize-space(translate(., '*!=', ''))"/>
     ! ==================================================================== </xsl:comment>
    </xsl:template>

    <!-- Comments outside the xs:schema element are ignored. -->
    <xsl:template match="/comment()"/>

    <!--
     ! Include the contents of the schema.
     !-->
    <xsl:template name="IncludeSchema">
        <xsl:param name="schemaLocation" select="@schemaLocation"/>
        <xsl:param name="heading">
            <xsl:message>No heading for <xsl:value-of select="$schemaLocation"/></xsl:message>
        </xsl:param>

        <xsl:if test="$heading and $heading != ''">
            <xsl:comment>
     ! **********************************************************************
     !     <xsl:value-of select="$heading"/>
     ! **********************************************************************
     !</xsl:comment>
        </xsl:if>

        <!--<xsl:message>Including <xsl:value-of select="$schemaLocation"/>.</xsl:message>-->

        <xsl:variable name="schema" select="document($schemaLocation, .)"/>
        <xsl:for-each select="$schema/xs:schema">
            <xsl:apply-templates/>
        </xsl:for-each>
    </xsl:template>

    <!--
     ! Import the contents of the schema.
     !-->
    <xsl:template name="ImportSchema">
        <xsl:copy>
            <xsl:apply-templates select="@*"/>
        </xsl:copy>
    </xsl:template>

    <!-- By default all xs:include are ignored and raise a warning. -->
    <xsl:template match="xs:include">
        <xsl:message>Ignoring xs:include of <xsl:value-of select="@schemaLocation"/>.</xsl:message>
    </xsl:template>

    <!-- By default all xs:import are ignored and raise a warning. -->
    <xsl:template match="xs:import">
        <xsl:message>Ignoring xs:import of <xsl:value-of select="@schemaLocation"/>.</xsl:message>
    </xsl:template>

</xsl:stylesheet>
