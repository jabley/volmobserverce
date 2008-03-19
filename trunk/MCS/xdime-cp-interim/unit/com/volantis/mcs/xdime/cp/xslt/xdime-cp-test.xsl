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

<xsl:stylesheet version="1.0"
    xmlns="http://www.volantis.com/xmlns/marlin-cdm"
    xmlns:cdm="http://www.volantis.com/xmlns/marlin-cdm"
    xmlns:xalan="http://xml.apache.org/xslt"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xh2="http://www.w3.org/2002/06/xhtml2"
    xmlns:xf="http://www.w3.org/2002/xforms"
    xmlns:css="http://www.volantis.com/xmlns/2004/06/xdimecp/interim/css">

    <!-- Generic mapping. -->
    <xsl:import href="xdime-cp.xsl"/>

    <!--<xsl:namespace-alias stylesheet-prefix="cdm" result-prefix="#default"/>-->

    <!-- Output as xml using xalan specific extensions. -->
    <xsl:output method="xml"
                indent="yes"
                xalan:indent-amount="2"/>

    <xsl:template match="/">
        <xsl:message>Start</xsl:message>
        <xsl:apply-templates/>
        <xsl:message>End</xsl:message>
    </xsl:template>

    <!--
     ! Styling template.
     !-->
    <xsl:template mode="get-style"
        match="xh2:h1|xh2:h2|xh2:p|xh2:pre|xh2:ol|xh2:ul|xh2:table|xh2:nl">
        <css:style>
            <css:layout-name>single_default</css:layout-name>
            <css:pane-name>single</css:pane-name>
        </css:style>
    </xsl:template>

    <!--
     ! Controls (except the submit) go in numbered entry pane.
     !-->
    <xsl:template mode="get-style" match="xh2:body/xf:*[not(self::xf:submit)]">
        <css:style>
            <css:layout-name>xform_default</css:layout-name>
            <css:pane-name>
                <xsl:text>entry</xsl:text>
                <xsl:call-template name="get-form-index"/>
            </css:pane-name>
        </css:style>
    </xsl:template>

    <!--
     ! Control labels go in numbered caption pane with the same number as the
     ! entry pane for the control. This does not include xf:item labels or
     ! submit labels.
     !-->
    <xsl:template mode="get-style" match="xf:*[not(self::xf:item|self::xf:submit)]/xf:label">
        <css:style>
            <css:pane-name>
                <xsl:text>caption</xsl:text>
                <xsl:call-template name="get-form-index">
                    <xsl:with-param name="node" select=".."/>
                </xsl:call-template>
            </css:pane-name>
        </css:style>
    </xsl:template>

    <!--
     ! Submit control goes into submitr pane.
     !-->
    <xsl:template mode="get-style" match="xf:submit">
        <css:style>
            <css:layout-name>xform_default</css:layout-name>
            <css:pane-name>
                <xsl:text>submitr</xsl:text>
            </css:pane-name>
        </css:style>
    </xsl:template>

    <!--
     ! <nl> with class of navblock goes in nl_navblock layout.
     !-->
    <xsl:template mode="get-style" match="xh2:nl[@class = 'navblock']">
        <css:style>
            <css:layout-name>nl_navblock</css:layout-name>
            <css:pane-name>single</css:pane-name>
        </css:style>
    </xsl:template>

</xsl:stylesheet>

<!--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Jun-04	4630/1	pduffin	VBM:2004060306 Added some more xdime cp stuff

 08-Jun-04	4630/3	pduffin	VBM:2004060306 Added some constants

 07-Jun-04	4630/1	pduffin	VBM:2004060306 Added framework for XDIME-CP interim solution

 ===========================================================================
-->
