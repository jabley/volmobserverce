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
 ! This XSLT is responsible for handling elements in the XDIME CP List
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
     ! Transforms the XHTML2 <dl>, <ol> and <ul> elements, by creating a new
     ! element and copying the attributes and the content.
     -->
    <xsl:template match="xh2:dl | xh2:ol | xh2:ul">
        <xsl:element name="{local-name()}">
            <xsl:call-template name="create-pane-attribute"/>
            <xsl:apply-templates select="@*"/>
            <xsl:apply-templates/>
        </xsl:element>
    </xsl:template>


    <!--
     ! Transforms the items of a non-navigational list by creating new copies
     ! of the items.
     -->
    <xsl:template match="xh2:dl/xh2:dt | xh2:dl/xh2:dd |
                         xh2:ol/xh2:li | xh2:ul/xh2:li">
        <xsl:element name="{local-name()}">
            <xsl:apply-templates select="@*"/>
            <xsl:apply-templates/>
        </xsl:element>
    </xsl:template>


    <!--
     ! Transforms a navigational list element (nl) into a menu element.
     -->
    <xsl:template match="xh2:nl">
        <xsl:element name="menu">
            <xsl:call-template name="create-pane-attribute"/>
            <xsl:apply-templates select="@*"/>
            <xsl:apply-templates/>
        </xsl:element>
    </xsl:template>


    <!--
     ! Transforms the label element of a navigational list by creating a
     ! new copy of it.
     -->
    <xsl:template match="xh2:nl/xh2:label">
        <xsl:element name="label">
            <xsl:call-template name="create-pane-attribute"/>
            <xsl:apply-templates select="@*"/>
            <xsl:apply-templates/>
        </xsl:element>
    </xsl:template>


    <!--
     ! Transforms the items of a navigational list element (nl) into
     ! menuitem elements.
     -->
    <xsl:template match="xh2:nl/xh2:li">
        <xsl:element name="menuitem">
            <xsl:call-template name="create-pane-attribute"/>
            <xsl:choose>
                <xsl:when test="@href">
                    <xsl:attribute name="href">
                        <xsl:value-of select="@href"/>
                    </xsl:attribute>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:message terminate="yes">
                        <xsl:value-of select="name()"/>
                        <xsl:text> must have an href attribute.</xsl:text>
                    </xsl:message>
                </xsl:otherwise>
            </xsl:choose>

            <xsl:attribute name="text">
                <xsl:value-of select="normalize-space(.)"/>
            </xsl:attribute>
            <xsl:apply-templates select="@*[local-name() != 'href']"/>
        </xsl:element>
    </xsl:template>


    <!--
     ! Text is allowed inside <li> and <label>
     !-->
    <xsl:template match="xh2:li/text() | xh2:label/text()">
        <xsl:copy/>
    </xsl:template>

</xsl:stylesheet>

<!--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Jun-04	4630/8	pduffin	VBM:2004060306 Integrated and produced distribution

 16-Jun-04	4645/10	pcameron	VBM:2004060306 Committed for integration

 16-Jun-04	4630/6	pduffin	VBM:2004060306 Some more changes

 16-Jun-04	4645/7	pcameron	VBM:2004060306 Committed for integration

 15-Jun-04	4630/2	pduffin	VBM:2004060306 Added some more xdime cp stuff

 09-Jun-04	4645/4	pcameron	VBM:2004060306 Commit changes for integration

 09-Jun-04	4645/2	pcameron	VBM:2004060306 Commit changes for integration

 ===========================================================================
-->
