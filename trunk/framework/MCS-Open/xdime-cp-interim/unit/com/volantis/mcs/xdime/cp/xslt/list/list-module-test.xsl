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


<!--
 ! This stylesheet adds additional templates in order to allow the List Module
 ! to be unit tested.
 !-->
<xsl:stylesheet version="1.0"
    xmlns:cdm="http://www.volantis.com/xmlns/marlin-cdm"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xh2="http://www.w3.org/2002/06/xhtml2"
    xmlns:mcs="http://www.volantis.com/xmlns/2004/06/xdimecp/mcs"
    xmlns:xf="http://www.w3.org/2002/xforms"
    xmlns:sel="http://www.w3.org/2004/06/diselect"
    xmlns:css="http://www.volantis.com/xmlns/2004/06/xdimecp/interim/css"
    exclude-result-prefixes="xh2 mcs xf sel css">

    <xsl:namespace-alias stylesheet-prefix="xh2" result-prefix="#default"/>

    <xsl:include href="../common-functions.xsl"/>
    <xsl:include href="../layout-module.xsl"/>
    <xsl:include href="../list-module.xsl"/>

    <xsl:template match="/mcs:unit">
        <xsl:element name="unit">
            <xsl:apply-templates select="@*"/>
            <xsl:apply-templates/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="*">
        <xsl:element name="{local-name()}">
            <xsl:apply-templates select="@*"/>
            <xsl:apply-templates/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="@*">
        <xsl:copy/>
    </xsl:template>

    <xsl:template mode="get-style" match="xh2:dl[@class='my-dl']">
        <css:style>
            <css:pane-name>dl-pane</css:pane-name>
        </css:style>
    </xsl:template>

    <xsl:template mode="get-style" match="xh2:ol[@class='my-ol']">
        <css:style>
            <css:pane-name>ol-pane</css:pane-name>
        </css:style>
    </xsl:template>

    <xsl:template mode="get-style" match="xh2:ul[@class='my-ul']">
        <css:style>
            <css:pane-name>ul-pane</css:pane-name>
        </css:style>
    </xsl:template>

    <xsl:template mode="get-style" match="xh2:nl[@class='my-menu']">
        <css:style>
            <css:pane-name>menu-pane</css:pane-name>
        </css:style>
    </xsl:template>

    <xsl:template mode="get-style" match="xh2:nl/xh2:label[@class='menu-label']">
        <css:style>
            <css:pane-name>label-pane</css:pane-name>
        </css:style>
    </xsl:template>

    <xsl:template mode="get-style" match="xh2:nl/xh2:li[@class='menu-item']">
        <css:style>
            <css:pane-name>item-pane</css:pane-name>
        </css:style>
    </xsl:template>

</xsl:stylesheet>

<!--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Jun-04	4645/11	pcameron	VBM:2004060306 Committed for integration

 16-Jun-04	4645/9	pcameron	VBM:2004060306 Committed for integration

 15-Jun-04	4645/5	pcameron	VBM:2004060306 Fixed test cases after integration

 09-Jun-04	4645/2	pcameron	VBM:2004060306 Commit changes for integration

 ===========================================================================
-->
