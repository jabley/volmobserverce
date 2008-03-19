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
 ! This XSLT is responsible for handling elements in the XDIME CP Table
 ! Module.
 !-->
<xsl:stylesheet version="1.0"
    xmlns:cdm="http://www.volantis.com/xmlns/marlin-cdm"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xh2="http://www.w3.org/2002/06/xhtml2"
    xmlns:mcs="http://www.volantis.com/xmlns/2004/06/xdimecp/mcs"
    xmlns:xf="http://www.w3.org/2002/xforms"
    xmlns:sel="http://www.w3.org/2004/06/diselect">

    <!--<xsl:namespace-alias stylesheet-prefix="xh2" result-prefix="#default"/>-->

    <!-- Common functions (named templates), variables etc. -->
    <xsl:include href="common-functions.xsl"/>

    <xsl:include href="caption-module.xsl"/>


    <!--
     ! Transform the XHTML2 table element and its content. If the table
     ! element has a caption element, this is changed to a div element with
     ! the same content, and placed immediately after the table.
     -->
    <xsl:template match="xh2:table">
        <!-- Transform the table element, its attributes and content -->
        <xsl:element name="table">
            <xsl:apply-templates select="@*"/>
            <xsl:call-template name="create-pane-attribute"/>
            <xsl:apply-templates select="*[not(self::xh2:caption)]"/>
        </xsl:element>

        <!--
         ! Now that the table has been transformed, transform the caption
         ! element.
         -->
        <xsl:apply-templates select="xh2:caption"/>

    </xsl:template>


    <!--
     ! Transform the table's tr element, its attributes and content.
     -->
    <xsl:template match="xh2:table/xh2:tr">
        <xsl:element name="tr">
            <xsl:apply-templates select="@*"/>
            <xsl:apply-templates/>
        </xsl:element>
    </xsl:template>


    <!--
     ! Transform the tr element's th and td elements, by creating copies of them
     ! and their contents. td and th elements can have an href attribute.
     -->
    <xsl:template match="xh2:tr/xh2:th | xh2:tr/xh2:td">
        <xsl:element name="{local-name()}">
            <xsl:apply-templates select="@*[local-name() != 'href']"/>
            <xsl:call-template name="WrapContentsInAnchor">
                <xsl:with-param name="contents">
                    <xsl:apply-templates/>
                </xsl:with-param>
            </xsl:call-template>
        </xsl:element>
    </xsl:template>


    <!--
     ! Copy text nodes since nothing else in this module matches on text!
     ! -->
    <xsl:template match="xh2:th/text() | xh2:td/text()">
        <xsl:copy/>
    </xsl:template>

</xsl:stylesheet>

<!--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Jun-04	4630/6	pduffin	VBM:2004060306 Integrated and produced distribution

 16-Jun-04	4630/4	pduffin	VBM:2004060306 Some more changes

 16-Jun-04	4645/14	pcameron	VBM:2004060306 Committed for integration

 15-Jun-04	4630/2	pduffin	VBM:2004060306 Added some more xdime cp stuff

 11-Jun-04	4645/12	pcameron	VBM:2004060306 Commit changes for integration

 09-Jun-04	4645/9	pcameron	VBM:2004060306 Commit changes for integration

 09-Jun-04	4645/6	pcameron	VBM:2004060306 Commit changes for integration

 09-Jun-04	4645/4	pcameron	VBM:2004060306 Commit changes for integration

 ===========================================================================
-->
