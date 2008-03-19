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
 ! This XSLT is responsible for handling elements in the XDIME CP Caption
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
     ! Transform the caption element into a div element, with the same content.
     ! Any href attribute is processed to wrap the caption's content.
     -->
    <xsl:template match="xh2:caption">
        <xsl:element name="div">
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
     ! Copy the caption element's text.
    -->
    <xsl:template match="xh2:caption/text()">
        <xsl:value-of select="."/>
    </xsl:template>


</xsl:stylesheet>

<!--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Jun-04	4630/6	pduffin	VBM:2004060306 Integrated and produced distribution

 16-Jun-04	4630/4	pduffin	VBM:2004060306 Some more changes

 16-Jun-04	4645/5	pcameron	VBM:2004060306 Committed for integration

 15-Jun-04	4630/2	pduffin	VBM:2004060306 Added some more xdime cp stuff

 11-Jun-04	4645/3	pcameron	VBM:2004060306 Commit changes for integration

 11-Jun-04	4645/1	pcameron	VBM:2004060306 Commit changes for integration

 ===========================================================================
-->
