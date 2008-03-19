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
<xsl:stylesheet xmlns:common="http://exslt.org/common" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0" exclude-result-prefixes="common">

    <!-- Output as xml using xalan specific extensions. -->
    <xsl:output xmlns:xalan="http://xml.apache.org/xslt"
                xalan:indent-amount="4"
                indent="yes"
                method="xml"/>

    <!-- Remember the root target namespace. -->
    <xsl:variable name="rootTargetNamespace" select="/xs:schema/@targetNamespace"/>

    <!-- Comments outside the xs:schema element are ignored. -->
    <xsl:template match="/comment()"/>

    <!-- Comments that contain the phrase 'Schema Name:' are also ignored. -->
    <xsl:template match="comment()[contains(., 'Schema Name:')]"/>

    <!-- Comments that contain the phrase 'Imports and Inclusions' are also ignored. -->
    <xsl:template match="comment()[contains(., 'Imports and Inclusions')]"/>

    <xsl:template match="/">
        <!-- Add public comment. -->
        <xsl:call-template name="GetPublicComment"/>

        <xsl:apply-templates/>
    </xsl:template>

    <!-- By default text is ignored. -->
    <xsl:template match="text()"/>

    <!-- Text within the annotation element is copied. -->
    <xsl:template match="xs:annotation//text()">
        <xsl:copy/>
    </xsl:template>

    <!-- By default all elements are copied. -->
    <xsl:template match="*">
        <xsl:copy>
            <xsl:apply-templates select="@*"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>

    <!-- By default all attributes are copied. -->
    <xsl:template match="@*">
        <xsl:copy/>
    </xsl:template>

    <!-- By default comments are copied. -->
    <xsl:template match="comment()">
        <xsl:comment> ======================================================================
     !         <xsl:value-of select="normalize-space(translate(., '*!=', ''))"/>
     ! ==================================================================== </xsl:comment>
<!--
        <xsl:comment> ======================================================================
     !         <xsl:value-of select="normalize-space(translate(., '*!=', ''))"/>
     ! ==================================================================== </xsl:comment>
        <xsl:comment>
     !         <xsl:value-of select="normalize-space(translate(., '*!=', ''))"/>
     !</xsl:comment>
-->
    </xsl:template>

    <!-- By default all xs:include are ignored. -->
    <xsl:template match="xs:include">
        <xsl:message>Ignoring xs:include of <xsl:value-of select="@schemaLocation"/>.</xsl:message>
    </xsl:template>

    <!-- If this is the top level schema then include the common module, otherwise do not. A top level schema is distinguished by its targetNamespace. -->
    <xsl:template match="xs:include[contains(@schemaLocation, 'common')]">
        <xsl:if test="parent::node()/@targetNamespace = $rootTargetNamespace">

            <xsl:comment>
     ! **********************************************************************
     !     Common Descriptions 
     ! **********************************************************************
     !</xsl:comment>

            <xsl:call-template name="IncludeSchema"/> 
        </xsl:if>
    </xsl:template>

    <!-- If this is the top level schema then include the data source module, otherwise do not. A top level schema is distinguished by its targetNamespace. -->
    <xsl:template match="xs:include[contains(@schemaLocation, 'data-source-module.xsd')]">
        <xsl:if test="parent::node()/@targetNamespace = $rootTargetNamespace">

            <xsl:comment>
     ! **********************************************************************
     !     Data Source Descriptions 
     ! **********************************************************************
     !</xsl:comment>

            <xsl:call-template name="IncludeSchema"/> 
        </xsl:if>
    </xsl:template>

    <!-- Include the pipeline configuration module. -->
    <xsl:template match="xs:include[@schemaLocation = 'modules/pipeline-configuration.xsd']">
        <xsl:comment>
     ! **********************************************************************
     !     Pipeline Descriptions 
     ! **********************************************************************
     !</xsl:comment>
 
        <xsl:call-template name="IncludeSchema"/> 
    </xsl:template>

    <!-- Include the projects configuration module. -->
    <xsl:template match="xs:include[@schemaLocation = 'modules/projects-module.xsd']">
        <xsl:comment>
     ! **********************************************************************
     !     Project Descriptions 
     ! **********************************************************************
     !</xsl:comment>
 
        <xsl:call-template name="IncludeSchema"/> 
    </xsl:template>

    <!-- Include the protocols configuration module. -->
    <xsl:template match="xs:include[@schemaLocation = 'modules/protocols-module.xsd']">
        <xsl:comment>
     ! **********************************************************************
     !     Protocol Descriptions 
     ! **********************************************************************
     !</xsl:comment>
 
        <xsl:call-template name="IncludeSchema"/> 
    </xsl:template>

    <!-- Include the device configuration module. -->
    <xsl:template match="xs:include[@schemaLocation = 'modules/device-module.xsd']">
        <xsl:comment>
     ! **********************************************************************
     !     Device Descriptions 
     ! **********************************************************************
     !</xsl:comment>
 
        <xsl:call-template name="IncludeSchema"/> 
    </xsl:template>

    <!-- Include the management configuration module. -->
    <xsl:template match="xs:include[@schemaLocation = 'modules/management-module.xsd']">
        <xsl:comment>
     ! **********************************************************************
     !     Management Descriptions 
     ! **********************************************************************
     !</xsl:comment>
 
        <xsl:call-template name="IncludeSchema"/> 
    </xsl:template>

    <xsl:template name="IncludeSchema">
        <xsl:variable name="schema" select="document(@schemaLocation, .)"/>
        <xsl:for-each select="$schema/xs:schema">
            <xsl:apply-templates/>
        </xsl:for-each>
    </xsl:template>

    <xsl:template name="GetPublicComment">
        <xsl:comment> ==========================================================================
 ! (c) Volantis Systems Ltd 2001. 
 ! ============================================================================
 ! This document 
 ! ======================================================================== </xsl:comment>
    </xsl:template>


</xsl:stylesheet>
