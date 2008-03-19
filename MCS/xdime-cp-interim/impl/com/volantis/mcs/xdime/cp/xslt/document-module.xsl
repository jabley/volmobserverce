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
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xh2="http://www.w3.org/2002/06/xhtml2"
    xmlns:mcs="http://www.volantis.com/xmlns/2004/06/xdimecp/mcs"
    xmlns:xf="http://www.w3.org/2002/xforms"
    xmlns:sel="http://www.w3.org/2004/06/diselect">

    <!-- Common functions (named templates), variables etc. -->
    <xsl:include href="common-functions.xsl"/>

    <!--
     ! Process the XHTML2 html element.
     !
     ! This element does not map to any XDIME element.
     !-->
    <xsl:template match="xh2:html">
        <xsl:apply-templates/>
    </xsl:template>

    <!--
     ! Process the XHTML2 head element.
     !
     ! This element does not map to any XDIME element.
     !-->
    <xsl:template match="xh2:html/xh2:head">
        <xsl:apply-templates/>
    </xsl:template>

    <!--
     ! Process the XHTML2 title element.
     !
     ! This element does not map to any XDIME element.
     !-->
    <xsl:template match="xh2:head/xh2:title">
        <xsl:apply-templates/>
    </xsl:template>

    <!--
     ! The XHTML2 title element can have text in it.
     !-->
    <xsl:template match="xh2:title/text()">
        <!-- do nothing -->
    </xsl:template>

    <!--
     ! Process the XHTML2 body element.
     !
     ! This element does not map to any XDIME element.
     !-->
    <xsl:template match="xh2:html/xh2:body">
        <xsl:apply-templates mode="top-level-element"/>
    </xsl:template>

</xsl:stylesheet>

<!--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Jun-04	4630/6	pduffin	VBM:2004060306 Added some more xdime cp stuff

 08-Jun-04	4630/3	pduffin	VBM:2004060306 Added some constants

 07-Jun-04	4630/1	pduffin	VBM:2004060306 Added framework for XDIME-CP interim solution

 ===========================================================================
-->
