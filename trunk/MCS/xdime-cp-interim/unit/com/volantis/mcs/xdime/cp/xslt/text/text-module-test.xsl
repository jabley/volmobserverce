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
 ! This stylesheet adds additional templates in order to allow the Text Module
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

    <xsl:include href="../layout-module.xsl"/>
    <xsl:include href="../text-module.xsl"/>

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


    <xsl:template mode="get-style" match="xh2:abbr[@class='paned']">
        <css:style>
            <css:pane-name>abbr-pane</css:pane-name>
        </css:style>
    </xsl:template>

    <xsl:template mode="get-style" match="xh2:dfn[@class='paned']">
        <css:style>
            <css:pane-name>dfn-pane</css:pane-name>
        </css:style>
    </xsl:template>

    <xsl:template mode="get-style" match="xh2:quote[@class='paned']">
        <css:style>
            <css:pane-name>quote-pane</css:pane-name>
        </css:style>
    </xsl:template>

    <xsl:template mode="get-style" match="xh2:var[@class='paned']">
        <css:style>
            <css:pane-name>var-pane</css:pane-name>
        </css:style>
    </xsl:template>

    <xsl:template mode="get-style" match="xh2:cite[@class='cite-class']">
        <css:style>
            <css:pane-name>cite-pane</css:pane-name>
        </css:style>
    </xsl:template>

    <xsl:template mode="get-style" match="xh2:span[@class='span-class']">
        <css:style>
            <css:pane-name>span-pane</css:pane-name>
        </css:style>
    </xsl:template>

    <xsl:template mode="get-style" match="xh2:code[@class='code-class']">
        <css:style>
            <css:pane-name>code-pane</css:pane-name>
        </css:style>
    </xsl:template>

    <xsl:template mode="get-style" match="xh2:strong[@class='strong-class']">
        <css:style>
            <css:pane-name>strong-pane</css:pane-name>
        </css:style>
    </xsl:template>

    <xsl:template mode="get-style" match="xh2:em[@class='em-class']">
        <css:style>
            <css:pane-name>em-pane</css:pane-name>
        </css:style>
    </xsl:template>

    <xsl:template mode="get-style" match="xh2:sub[@class='sub-class']">
        <css:style>
            <css:pane-name>sub-pane</css:pane-name>
        </css:style>
    </xsl:template>

    <xsl:template mode="get-style" match="xh2:kbd[@class='kbd-class']">
        <css:style>
            <css:pane-name>kbd-pane</css:pane-name>
        </css:style>
    </xsl:template>

    <xsl:template mode="get-style" match="xh2:sup[@class='sup-class']">
        <css:style>
            <css:pane-name>sup-pane</css:pane-name>
        </css:style>
    </xsl:template>

    <xsl:template mode="get-style" match="xh2:samp[@class='samp-class']">
        <css:style>
            <css:pane-name>samp-pane</css:pane-name>
        </css:style>
    </xsl:template>

</xsl:stylesheet>

<!--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Jun-04	4645/10	pcameron	VBM:2004060306 Committed for integration

 16-Jun-04	4645/8	pcameron	VBM:2004060306 Committed for integration

 15-Jun-04	4645/5	pcameron	VBM:2004060306 Fixed test cases after integration

 09-Jun-04	4645/3	pcameron	VBM:2004060306 Commit changes for integration

 09-Jun-04	4645/1	pcameron	VBM:2004060306 Commit changes for integration

 ===========================================================================
-->
