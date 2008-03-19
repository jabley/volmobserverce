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
 ! This XSLT is responsible for handling elements in the XDIME CP Document
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
     ! href attribute is allowed on the a element. Just copy it.
     -->
    <xsl:template match="xh2:a">
        <xsl:element name="a">
            <xsl:apply-templates select="@*"/>
            <xsl:call-template name="create-pane-attribute"/>
            <xsl:apply-templates/>
        </xsl:element>
    </xsl:template>


    <xsl:template match="xh2:a/@href">
        <xsl:copy/>
    </xsl:template>

    <!--
     !
     -->
    <xsl:template match="xh2:a/text()">
        <xsl:copy/>
    </xsl:template>

</xsl:stylesheet>

<!--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Jun-04	4630/10	pduffin	VBM:2004060306 Integrated and produced distribution

 16-Jun-04	4630/8	pduffin	VBM:2004060306 Some more changes

 16-Jun-04	4645/9	pcameron	VBM:2004060306 Committed for integration

 15-Jun-04	4630/6	pduffin	VBM:2004060306 Added some more xdime cp stuff

 11-Jun-04	4645/7	pcameron	VBM:2004060306 Commit changes for integration

 10-Jun-04	4645/1	pcameron	VBM:2004060306 Commit changes for integration

 08-Jun-04	4630/3	pduffin	VBM:2004060306 Added some constants

 07-Jun-04	4630/1	pduffin	VBM:2004060306 Added framework for XDIME-CP interim solution

 ===========================================================================
-->
