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
 ! This XSLT is responsible for handling elements in the XDIME CP Text
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

    <xsl:include href="hypertext-module.xsl"/>


    <!--
     ! Transform the emulated text block elements into span elements with
     ! an appropriate attribute based on the name of the element.
     -->
    <xsl:template match="xh2:abbr | xh2:dfn | xh2:quote | xh2:var">
        <!-- Save the name of the element for use later -->
        <xsl:variable name="element-name">
            <xsl:value-of select="local-name()"/>
        </xsl:variable>

        <xsl:variable name="emulated-class">
            <xsl:text>mcs-emulated-</xsl:text>
            <xsl:value-of select="local-name()"/>
            <xsl:if test="@class">
                <xsl:text>-</xsl:text>
                <xsl:value-of select="@class"/>
            </xsl:if>
        </xsl:variable>

        <!--
         ! Create the span element with a class attribute whose content is
         ! mcs-emulated-NAME[-CLASS] where NAME is the name of the element saved
         ! above, and the optional CLASS (with hyphen) is the value of any
         ! existing class attribute.
         -->
        <xsl:call-template name="WrapContentsInAnchor">
            <xsl:with-param name="contents">
                <xsl:element name="span">
                    <xsl:call-template name="create-pane-attribute"/>
                    <xsl:attribute name="class">
                        <xsl:value-of select="$emulated-class"/>
                    </xsl:attribute>
                    <xsl:apply-templates/>
                </xsl:element>
            </xsl:with-param>
        </xsl:call-template>
    </xsl:template>


    <!--
     ! The href attribute is only valid on the text elements matched
     ! below. An href occurring on any other text element will result in
     ! an error and termination of the stylesheet transformation in common.xsl.
     ! Valid href attributes are dealt with by the WrapContentsInAnchor
     ! template, so are ignored by this template.
    -->
    <xsl:template match="xh2:cite | xh2:code | xh2:em | xh2:kbd | xh2:samp |
                         xh2:span | xh2:strong | xh2:sub | xh2:sup">

        <xsl:call-template name="WrapContentsInAnchor">
            <xsl:with-param name="contents">
                <xsl:element name="{local-name()}">
                    <xsl:apply-templates select="@*[local-name() != 'href']"/>
                    <xsl:call-template name="create-pane-attribute"/>
                    <xsl:apply-templates/>
                </xsl:element>
            </xsl:with-param>
        </xsl:call-template>

    </xsl:template>


    <!--
     ! Copy text nodes since nothing else in this module matches on text!
     ! -->
    <xsl:template match="xh2:address/text() | xh2:em/text() |
                         xh2:samp/text() | xh2:span/text() | xh2:strong/text() |
                         xh2:sub/text() | xh2:sup/text() | xh2:abbr/text() |
                         xh2:quote/text() | xh2:dfn/text() | xh2:var/text() |
                         xh2:cite/text() | xh2:code/text() | xh2:kbd/text() |
                         xh2:blockquote/text() | xh2:pre/text() | xh2:p/text()">
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

 11-Jun-04	4645/11	pcameron	VBM:2004060306 Commit changes for integration

 11-Jun-04	4645/9	pcameron	VBM:2004060306 Commit changes for integration

 11-Jun-04	4645/7	pcameron	VBM:2004060306 Commit changes for integration

 09-Jun-04	4645/5	pcameron	VBM:2004060306 Commit changes for integration

 09-Jun-04	4645/3	pcameron	VBM:2004060306 Commit changes for integration

 09-Jun-04	4645/1	pcameron	VBM:2004060306 Commit changes for integration

 ===========================================================================
-->
