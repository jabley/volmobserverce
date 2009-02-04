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
 ! This stylesheet adds additional templates in order to allow the FormsCore
 ! Module to be unit tested.
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
    <xsl:include href="../xforms-controls-module.xsl"/>


    <xsl:template mode="get-style" match="xf:input[@class='xyz']">
        <css:style>
            <css:pane-name>XYZEntryPane</css:pane-name>
        </css:style>
    </xsl:template>

    <xsl:template mode="get-style" match="xf:input[@class='xyz']/xf:label">
        <css:style>
            <css:pane-name>XYZCaptionPane</css:pane-name>
        </css:style>
    </xsl:template>

    <xsl:template mode="get-style" match="xf:select[@class='qrs']">
        <css:style>
            <css:pane-name>QRSEntryPane</css:pane-name>
        </css:style>
    </xsl:template>

    <xsl:template mode="get-style" match="xf:select[@class='qrs']/xf:label">
        <css:style>
            <css:pane-name>QRSCaptionPane</css:pane-name>
        </css:style>
    </xsl:template>

    <xsl:template mode="get-style" match="xf:select[@class='qrs']/xf:item">
        <css:style>
            <css:pane-name>QRSItemEntryPane<xsl:value-of select="position()"/>
            </css:pane-name>
        </css:style>
    </xsl:template>

    <xsl:template mode="get-style" match="xf:select[@class='qrs']/xf:item/xf:label">
        <css:style>
            <css:pane-name>QRSItemCaptionPane<xsl:value-of select="position()"/>
            </css:pane-name>
        </css:style>
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


    <xsl:template match="/mcs:unit">
        <xsl:element name="unit">
            <xsl:apply-templates mode="form" select="@*"/>
            <xsl:apply-templates mode="form"/>
        </xsl:element>
    </xsl:template>

    <xsl:template mode="form" match="/mcs:unit/xh2:html">
        <xsl:apply-templates mode="form"/>
    </xsl:template>

    <xsl:template mode="form" match="/mcs:unit/xh2:html/xh2:head">
        <xsl:apply-templates mode="form"/>
    </xsl:template>

    <xsl:template mode="form" match="/mcs:unit/xh2:html/xh2:head/xf:model">
        <xsl:apply-templates mode="form"/>
    </xsl:template>

    <xsl:template mode="form" match="/mcs:unit/xh2:html/xh2:head/xf:model/xf:submission">
        <xsl:apply-templates mode="form"/>
    </xsl:template>

    <xsl:template mode="form" match="/mcs:unit/xh2:html/xh2:body">
        <xsl:apply-templates mode="form"/>
    </xsl:template>

</xsl:stylesheet>

<!--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Jun-04	4645/9	pcameron	VBM:2004060306 Committed for integration

 16-Jun-04	4645/7	pcameron	VBM:2004060306 Committed for integration

 15-Jun-04	4645/5	pcameron	VBM:2004060306 Committed for integration

 11-Jun-04	4645/1	pcameron	VBM:2004060306 Commit changes for integration

 ===========================================================================
-->
