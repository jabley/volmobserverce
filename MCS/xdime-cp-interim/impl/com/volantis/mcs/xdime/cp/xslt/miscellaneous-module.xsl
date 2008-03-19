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
    xmlns:sel="http://www.w3.org/2004/06/diselect"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">

    <!--
     ! A top level mcs:unit element can only contain an html element.
     !-->
    <xsl:template match="/mcs:unit">
        <xsl:element name="unit">
            <xsl:apply-templates select="@*"/>
            <xsl:apply-templates select="xh2:html"/>
        </xsl:element>

        <!-- Fail if contains any other nodes. -->
        <xsl:apply-templates select="*[not(self::xh2:html)]|node()[not(*)]"
            mode="unexpected"/>
    </xsl:template>

    <!--
     ! Another other mcs:unit element can simply be ignored.
     !-->
    <xsl:template match="mcs:unit">
        <xsl:apply-templates/>
    </xsl:template>

    <!--
     ! Ignore schema instance attributes.
     !-->
    <xsl:template match="@xsi:*"/>

    <!--
     ! Copy the base attribute.
     !-->
    <xsl:template match="@xml:base">
        <xsl:copy/>
    </xsl:template>

</xsl:stylesheet>

<!--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Jun-04	4630/4	pduffin	VBM:2004060306 Integrated and produced distribution

 15-Jun-04	4630/2	pduffin	VBM:2004060306 Added some more xdime cp stuff

 11-Jun-04	4645/1	pcameron	VBM:2004060306 Commit changes for integration

 ===========================================================================
-->
