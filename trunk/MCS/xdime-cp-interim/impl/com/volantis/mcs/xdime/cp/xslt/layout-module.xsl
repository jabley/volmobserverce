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
 ! Supports layout mapping done within XDIME.
 !-->
<xsl:stylesheet version="1.0"
    xmlns:cdm="http://www.volantis.com/xmlns/marlin-cdm"
    xmlns:exslt-common="http://exslt.org/common"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xh2="http://www.w3.org/2002/06/xhtml2"
    xmlns:mcs="http://www.volantis.com/xmlns/2004/06/xdimecp/mcs"
    xmlns:xf="http://www.w3.org/2002/xforms"
    xmlns:sel="http://www.w3.org/2004/06/diselect"
    xmlns:css="http://www.volantis.com/xmlns/2004/06/xdimecp/interim/css">

    <xsl:include href="style-module.xsl"/>

    <!--
     ! Named Template: get-pane-name
     !
     !     Get the pane name for the current element.
     !
     ! This gets the style (see get-style) for the current element, and
     ! extracts the layout-name. If no pane-name element is present then
     ! it returns an empty string.
     !-->
    <xsl:template name="get-pane-name">
        <!--
         ! Get the style and convert it back from a result tree into a nodeset
         ! so that it can be queried using XPath. Also remove the top level
         ! css:style to simplify querying.
         !-->
        <xsl:variable name="style-RT">
            <xsl:call-template name="get-style"/>
        </xsl:variable>
        <xsl:variable name="style" select="exslt-common:node-set($style-RT)/css:style"/>

        <!-- Get the pane name from the style. -->
        <xsl:value-of select="$style/css:pane-name"/>

    </xsl:template>

    <!--
     ! Named Template: get-layout-name
     !
     !     Get the layout name for the current element.
     !
     ! This gets the style (see get-style) for the current element, and
     ! extracts the layout-name. If no layout-name element is present then
     ! it constructs a default name from the element name and its class
     ! attribute if set.
     !-->
    <xsl:template name="get-layout-name">
        <!--
         ! Get the style and convert it back from a result tree into a nodeset
         ! so that it can be queried using XPath. Also remove the top level
         ! css:style to simplify querying.
         !-->
        <xsl:variable name="style-RT">
            <xsl:call-template name="get-style"/>
        </xsl:variable>
        <xsl:variable name="style" select="exslt-common:node-set($style-RT)/css:style"/>

        <!-- Get the layout name from the style. -->
        <xsl:variable name="layout-name" select="$style/css:layout-name/."/>

        <!--
         ! If no layout name was specified then fail.
         !-->
        <xsl:if test="not($layout-name) or $layout-name = ''">
            <xsl:message terminate="yes">
                <xsl:text>No layout-name specified for '</xsl:text>
                <xsl:value-of select="local-name()"/>
                <xsl:text>'</xsl:text>
            </xsl:message>
        </xsl:if>

        <!-- Return the layout name. -->
        <xsl:value-of select="$layout-name"/>

    </xsl:template>

    <xsl:template name="get-region-name">

        <xsl:variable name="preceding-siblings">
            <xsl:value-of select="count(preceding-sibling::xh2:*)"/>
        </xsl:variable>

        <xsl:variable name="preceding-controls">
            <xsl:value-of select="count(preceding-sibling::xf:*)"/>
        </xsl:variable>

        <!--
        <xsl:message>
            <xsl:text>PS </xsl:text>
            <xsl:value-of select="$preceding-siblings"/>
            <xsl:text> PC </xsl:text>
            <xsl:value-of select="$preceding-controls"/>
        </xsl:message>
        -->

        <xsl:text>Region</xsl:text>
        <xsl:choose>
            <xsl:when test="$preceding-controls > 0">
                <xsl:value-of select="$preceding-siblings + 1"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$preceding-siblings"/>
            </xsl:otherwise>
        </xsl:choose>

    </xsl:template>

    <xsl:template name="WrapInCanvas">
        <xsl:param name="contents"/>

        <!--
         ! Get the layout name for this element, this will always return
         ! a non empty / non null string.
         !-->
        <xsl:variable name="layout-name">
            <xsl:call-template name="get-layout-name"/>
        </xsl:variable>

        <!--
         ! Get the region name.
         !-->
        <xsl:variable name="region">
            <xsl:call-template name="get-region-name"/>
        </xsl:variable>

        <xsl:element name="region">
            <xsl:attribute name="name">
                <xsl:value-of select="$region"/>
            </xsl:attribute>
            <xsl:element name="canvas">
                <xsl:attribute name="type">inclusion</xsl:attribute>
                <xsl:attribute name="layoutName">
                    <xsl:value-of select="$layout-name"/>
                </xsl:attribute>

                <xsl:copy-of select="exslt-common:node-set($contents)"/>
            </xsl:element>
        </xsl:element>

    </xsl:template>

    <!--
     ! Process top level XHTML 2 elements.
     !-->
    <xsl:template mode="top-level-element" match="xh2:*">

        <!--
         ! Wrap the processed contents in a canvas.
         !-->
        <xsl:call-template name="WrapInCanvas">
            <xsl:with-param name="contents">
                <!--
                <xsl:element name="pane">
                    <xsl:attribute name="name">single</xsl:attribute>
                    -->
                <!--
                 ! Generate the normal markup for this element.
                 !-->
                <xsl:apply-templates select="."/>
                <!--
                </xsl:element>
                -->
            </xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <!--
     ! Non white space text is not allowed at the top level.
     !-->
    <xsl:template mode="top-level-element" match="text()">
        <xsl:call-template name="unexpected-text"/>
    </xsl:template>


    <!--
     ! Create a pane attribute if there is a pane-name style property
     ! associated with the element.
     -->
    <xsl:template name="create-pane-attribute">
        <xsl:variable name="pane-name">
            <xsl:call-template name="get-pane-name"/>
        </xsl:variable>
        <!--
        <xsl:message>Checking <xsl:value-of select="name()"/></xsl:message>
        -->
        <!--
        <xsl:variable name="pane-name" select="exslt-common:node-set($pane-name-RT)"/>
        <xsl:message>Checking <xsl:value-of select="name()"/>, pane name <xsl:value-of select="$pane-name"/></xsl:message>
        -->
        <xsl:if test="$pane-name and $pane-name != ''">
            <xsl:attribute name="pane">
                <xsl:value-of select="$pane-name"/>
            </xsl:attribute>
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>

<!--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Jun-04	4630/7	pduffin	VBM:2004060306 Integrated and produced distribution

 16-Jun-04	4630/5	pduffin	VBM:2004060306 Some more changes

 16-Jun-04	4645/3	pcameron	VBM:2004060306 Committed for integration

 15-Jun-04	4630/3	pduffin	VBM:2004060306 Added some more xdime cp stuff

 ===========================================================================
-->
