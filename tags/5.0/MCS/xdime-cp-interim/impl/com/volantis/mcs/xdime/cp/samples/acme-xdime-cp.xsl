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
 ! This stylesheet contains customisations and 'styles' for the ACME company.
 !-->
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xh2="http://www.w3.org/2002/06/xhtml2"
    xmlns:mcs="http://www.volantis.com/xmlns/2004/06/xdimecp/mcs"
    xmlns:xf="http://www.w3.org/2002/xforms"
    xmlns:sel="http://www.w3.org/2004/06/diselect"
    xmlns:css="http://www.volantis.com/xmlns/2004/06/xdimecp/interim/css"
    exclude-result-prefixes="xh2 mcs xf sel css">

    <xsl:include href="../xslt/xdime-cp.xsl"/>


    <!--
     ! h1.introduction {
     !     layout-name: "/xdimecp/pane.mlyt";
     !     pane-name: 'single'
     ! }
     !-->
    <xsl:template mode="get-style" match="xh2:h1[@class='introduction']">
        <css:style>
            <css:layout-name>/xdimecp/pane.mlyt</css:layout-name>
            <css:pane-name>single</css:pane-name>
        </css:style>
    </xsl:template>

    <!--
     ! h2.search-area {
     !     layout-name: "/xdimecp/pane.mlyt";
     !     pane-name: 'single'
     ! }
     !-->
    <xsl:template mode="get-style" match="xh2:h2[@class='search-area']">
        <css:style>
            <css:layout-name>/xdimecp/pane.mlyt</css:layout-name>
            <css:pane-name>single</css:pane-name>
        </css:style>
    </xsl:template>

    <!--
     ! p {
     !     layout-name: "/xdimecp/pane.mlyt";
     !     pane-name: 'single'
     ! }
     !-->
    <xsl:template mode="get-style" match="xh2:p">
        <css:style>
            <css:layout-name>/xdimecp/pane.mlyt</css:layout-name>
            <css:pane-name>single</css:pane-name>
        </css:style>
    </xsl:template>

    <!--
     ! h3.ordinarylist-section {
     !     layout-name: "/xdimecp/pane.mlyt";
     !     pane-name: 'single'
     ! }
     !-->
    <xsl:template mode="get-style" match="xh2:h3[@class='ordinarylist-section']">
        <css:style>
            <css:layout-name>/xdimecp/pane.mlyt</css:layout-name>
            <css:pane-name>single</css:pane-name>
        </css:style>
    </xsl:template>

    <!--
     ! h4.navlist-section {
     !     layout-name: "/xdimecp/pane.mlyt";
     !     pane-name: 'single'
     ! }
     !-->
    <xsl:template mode="get-style" match="xh2:h4[@class='navlist-section']">
        <css:style>
            <css:layout-name>/xdimecp/pane.mlyt</css:layout-name>
            <css:pane-name>single</css:pane-name>
        </css:style>
    </xsl:template>

    <!--
     ! ol.ordinary-list {
     !     layout-name: "/xdimecp/pane.mlyt";
     !     pane-name: 'single'
     ! }
     !-->
    <xsl:template mode="get-style" match="xh2:ol[@class='ordinary-list']">
        <css:style>
            <css:layout-name>/xdimecp/pane.mlyt</css:layout-name>
            <css:pane-name>single</css:pane-name>
        </css:style>
    </xsl:template>

    <!--
     ! nl {
     !     layout-name: "/xdimecp/pane.mlyt";
     !     pane-name: 'single'
     ! }
     !-->
    <xsl:template mode="get-style" match="xh2:nl">
        <css:style>
            <css:layout-name>/xdimecp/pane.mlyt</css:layout-name>
            <css:pane-name>single</css:pane-name>
        </css:style>
    </xsl:template>

    <!--
     ! xf:* {
     !     layout-name: "/xdimecp/form.mlyt";
     !     pane-name: 'control<index>'
     ! }
     !-->
    <xsl:template mode="get-style" match="xh2:body/xf:*">
        <css:style>
            <css:layout-name>/xdimecp/form.mlyt</css:layout-name>
            <css:pane-name>
                <xsl:text>control</xsl:text>
                <xsl:call-template name="get-form-index"/>
            </css:pane-name>
        </css:style>
    </xsl:template>

    <!--
     ! xf:* {
     !     layout-name: "/xdimecp/form.mlyt";
     !     pane-name: 'label<index>'
     ! }
     !-->
    <xsl:template mode="get-style" match="xf:*/xf:label">
        <css:style>
            <css:layout-name>/xdimecp/form.mlyt</css:layout-name>
            <css:pane-name>
                <xsl:text>label</xsl:text>
                <xsl:call-template name="get-form-index">
                    <xsl:with-param name="node" select=".."/>
                </xsl:call-template>
            </css:pane-name>
        </css:style>
    </xsl:template>

</xsl:stylesheet>

<!--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Jun-04	4630/3	pduffin	VBM:2004060306 Integrated and produced distribution

 16-Jun-04	4645/1	pcameron	VBM:2004060306 Committed for integration

 ===========================================================================
-->
