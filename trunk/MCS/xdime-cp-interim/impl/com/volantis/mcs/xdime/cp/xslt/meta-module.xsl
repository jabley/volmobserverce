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


    <!--
     ! Transforms the XHTML2 meta element by creating a new copy of it. The
     ! content is not copied as this is dealt with by the match on the name
     ! attribute.
     -->
    <xsl:template match="xh2:meta">
        <xsl:element name="meta">
            <xsl:apply-templates select="@*"/>

            <!-- Create the content attribute -->
            <xsl:attribute name="content">
                <xsl:value-of select="normalize-space(.)"/>
            </xsl:attribute>
        </xsl:element>
    </xsl:template>


    <!--
     ! Copies the name attribute of the meta element, and creates a new
     ! content attribute whose value is the content of the parent element.
     -->
    <xsl:template match="xh2:meta/@name">
        <!-- Copy the name attribute -->
        <xsl:copy/>
    </xsl:template>

    <!--
     ! The class, id and title attributes are ignored on the meta element.
     -->
    <xsl:template match="xh2:meta/@class | xh2:meta/@id | xh2:meta/@title"/>

</xsl:stylesheet>

<!--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Jun-04	4630/7	pduffin	VBM:2004060306 Integrated and produced distribution

 16-Jun-04	4630/5	pduffin	VBM:2004060306 Some more changes

 15-Jun-04	4630/2	pduffin	VBM:2004060306 Added some more xdime cp stuff

 11-Jun-04	4645/5	pcameron	VBM:2004060306 Commit changes for integration

 09-Jun-04	4645/3	pcameron	VBM:2004060306 Commit changes for integration

 09-Jun-04	4645/1	pcameron	VBM:2004060306 Commit changes for integration

 ===========================================================================
-->
