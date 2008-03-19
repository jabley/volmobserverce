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
 ! This XSLT is responsible for handling elements in the XDIME CP Structure
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

    <!--
     ! Process the XHTML2 block elements that never have href attributes.
     !-->
    <xsl:template match="xh2:div | xh2:blockquote | xh2:pre | xh2:p">
        <xsl:element name="{local-name()}">

            <xsl:apply-templates select="@*"/>
            <xsl:call-template name="create-pane-attribute"/>
            <xsl:apply-templates/>

        </xsl:element>
    </xsl:template>


    <!--
     ! Process the XHTML2 block elements that can have href attributes.
     ! The href attribute is only valid on the structural elements matched
     ! below. An href occurring on any other structural element will result in
     ! an error and termination of the stylesheet transformation in common.xsl.
     ! Valid href attributes are dealt with by the WrapContentsInAnchor
     ! template, so are ignored by this template.
    -->
    <xsl:template match="xh2:h1 | xh2:h2 | xh2:h3 | xh2:h4 | xh2:h5 | xh2:h6 |
                         xh2:address">
        <xsl:element name="{local-name()}">
            <xsl:apply-templates select="@*[local-name() != 'href']"/>
            <xsl:call-template name="create-pane-attribute"/>
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
    <xsl:template match="xh2:div/text() | xh2:h1/text() | xh2:h2/text() |
                         xh2:h3/text() | xh2:h4/text() | xh2:h5/text() |
                         xh2:h6/text() | xh2:address/text() |
                         xh2:blockquote/text() | xh2:pre/text() | xh2:p/text()">
        <xsl:copy/>
    </xsl:template>

</xsl:stylesheet>

<!--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Jun-04	4630/5	pduffin	VBM:2004060306 Integrated and produced distribution

 16-Jun-04	4630/3	pduffin	VBM:2004060306 Some more changes

 16-Jun-04	4645/8	pcameron	VBM:2004060306 Committed for integration

 10-Jun-04	4645/1	pcameron	VBM:2004060306 Commit changes for integration

 09-Jun-04	4645/10	pcameron	VBM:2004060306 Commit changes for integration

 09-Jun-04	4645/8	pcameron	VBM:2004060306 Commit changes for integration

 09-Jun-04	4645/6	pcameron	VBM:2004060306 Commit changes for integration

 08-Jun-04	4645/4	pcameron	VBM:2004060306 Commit changes for integration

 08-Jun-04	4630/3	pduffin	VBM:2004060306 Added some constants

 07-Jun-04	4630/1	pduffin	VBM:2004060306 Added framework for XDIME-CP interim solution

 ===========================================================================
-->
