<?xml version="1.0" encoding="ISO-8859-1"?>
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

<!-- ==========================================================================
 ! (c) Volantis Systems Ltd 2003. 
 ! ======================================================================== -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="xml" indent="yes"/>
    <!--
        Copy Standard PAPI attributes
    -->
        <xsl:template name="copyStandardAttrs">
        <xsl:copy-of select="@id"/>
        <xsl:copy-of select="@class"/>
        <xsl:copy-of select="@title"/>
    </xsl:template>
    <!--
        Work out how many columns in a HTML table
    -->
    <xsl:template name="getCols">
        <xsl:param name="nodeset"/>
        <xsl:param name="cols" select="0"/>
        <xsl:param name="pos" select="1"/>
        <xsl:choose>
            <xsl:when test="count($nodeset) &lt; $pos">
                <xsl:value-of select="$cols"/>
            </xsl:when>
            <xsl:when test="number($nodeset[$pos]/@colspan) &gt; 1">
                <xsl:call-template name="getCols">
                    <xsl:with-param name="nodeset" select="$nodeset"/>
                    <xsl:with-param name="cols" select="$cols+number($nodeset[$pos]/@colspan)"/>
                    <xsl:with-param name="pos" select="$pos+1"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="getCols">
                    <xsl:with-param name="nodeset" select="$nodeset"/>
                    <xsl:with-param name="cols" select="$cols+1"/>
                    <xsl:with-param name="pos" select="$pos+1"/>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xsl:template match="html">
            <pane name="error">
		Start of portlet--------
                <br/>
                <xsl:apply-templates/>
                <br/>
		End of portlet--------
            </pane>
    </xsl:template>
    <!--Anchor Tag-->
    <xsl:template match="a">
        <xsl:copy>
            <xsl:call-template name="copyStandardAttrs"/>
            <xsl:attribute name="href"><xsl:value-of select="@href"/></xsl:attribute>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    <!--Address Tag-->
    <xsl:template match="address">
        <xsl:copy>
            <xsl:call-template name="copyStandardAttrs"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    <!--B tag-->
    <xsl:template match="b">
        <xsl:copy>
            <xsl:call-template name="copyStandardAttrs"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    <!--Base tag-->
    <xsl:template match="base">
        <xsl:message terminate="no">Base element has been used</xsl:message>
    </xsl:template>
    <!--Big tag-->
    <xsl:template match="big">
        <!--<xsl:copy>
            <xsl:call-template name="copyStandardAttrs"/>
            <xsl:apply-templates/>
        </xsl:copy>-->
    </xsl:template>
    <!--Blockquote tag-->
    <xsl:template match="blockquote">
        <!--<xsl:copy>
            <xsl:call-template name="copyStandardAttrs"/>
            <xsl:apply-templates/>
        </xsl:copy>-->
    </xsl:template>
    <!--Br tag-->
    <xsl:template match="br">
        <xsl:copy>
            <xsl:call-template name="copyStandardAttrs"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    <!--Cite tag-->
    <xsl:template match="cite">
        <xsl:copy>
            <xsl:call-template name="copyStandardAttrs"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    <!--Code tag-->
    <xsl:template match="code">
        <xsl:copy>
            <xsl:call-template name="copyStandardAttrs"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    <!--Dfn tag-->
    <xsl:template match="dfn">
        <xsl:element name="i">
            <xsl:call-template name="copyStandardAttrs"/>
            <xsl:apply-templates/>
        </xsl:element>
    </xsl:template>
    <!--Div tag-->
    <xsl:template match="div">
        <xsl:copy>
            <xsl:call-template name="copyStandardAttrs"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    <!--Dd tag-->
    <xsl:template match="dd">
        <xsl:copy>
            <xsl:call-template name="copyStandardAttrs"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    <!--Dl tag-->
    <xsl:template match="dl">
        <xsl:copy>
            <xsl:call-template name="copyStandardAttrs"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    <!--Dt tag-->
    <xsl:template match="dt">
        <xsl:copy>
            <xsl:call-template name="copyStandardAttrs"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    <!--Em tag-->
    <xsl:template match="em">
        <xsl:copy>
            <xsl:call-template name="copyStandardAttrs"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    <!--H1 tag-->
    <xsl:template match="h1">
        <xsl:copy>
            <xsl:call-template name="copyStandardAttrs"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    <!--H2 tag-->
    <xsl:template match="h2">
        <xsl:copy>
            <xsl:call-template name="copyStandardAttrs"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    <!--H3 tag-->
    <xsl:template match="h3">
        <xsl:copy>
            <xsl:call-template name="copyStandardAttrs"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    <!--H4 tag-->
    <xsl:template match="h4">
        <xsl:copy>
            <xsl:call-template name="copyStandardAttrs"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    <!--H5 tag-->
    <xsl:template match="h5">
        <xsl:copy>
            <xsl:call-template name="copyStandardAttrs"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    <!--H6 tag-->
    <xsl:template match="h6">
        <xsl:copy>
            <xsl:call-template name="copyStandardAttrs"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    <!--Hr tag-->
    <xsl:template match="hr">
        <xsl:copy>
            <xsl:call-template name="copyStandardAttrs"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    <!--I tag-->
    <xsl:template match="i">
        <xsl:copy>
            <xsl:call-template name="copyStandardAttrs"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    <!--Img tag-->
    <xsl:template match="img">
        <xsl:copy>
            <xsl:call-template name="copyStandardAttrs"/>
            <xsl:attribute name="url">
            <xsl:value-of select="@src"/></xsl:attribute>
            <xsl:copy-of select="@alt"/>
        </xsl:copy>
    </xsl:template>
    <!--kbd tag-->
    <xsl:template match="kbd">
        <xsl:element name="code">
            <xsl:call-template name="copyStandardAttrs"/>
            <xsl:apply-templates/>
        </xsl:element>
    </xsl:template>
    <!--Li tag-->
    <xsl:template match="li">
        <xsl:copy>
            <xsl:call-template name="copyStandardAttrs"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    <!--Meta tag-->
    <xsl:template match="meta">
        <!-- <xsl:copy>
            <xsl:copy-of select="@name"/>
            <xsl:copy-of select="@http-equiv"/>
            <xsl:copy-of select="@content"/>
            <xsl:apply-templates/>
        </xsl:copy>-->
    </xsl:template>
    <!--Ol tag-->
    <xsl:template match="ol">
        <xsl:copy>
            <xsl:call-template name="copyStandardAttrs"/>
            <xsl:copy-of select="@start"/>
            <xsl:copy-of select="@type"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    <!--P tag-->
    <xsl:template match="p">
        <xsl:copy>
            <xsl:call-template name="copyStandardAttrs"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    <!--Pre tag-->
    <xsl:template match="pre">
        <xsl:copy>
            <xsl:call-template name="copyStandardAttrs"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    <!--Samp tag-->
    <xsl:template match="samp">
        <xsl:copy>
            <xsl:call-template name="copyStandardAttrs"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    <!--Scipt tag-->
    <xsl:template match="script">
    </xsl:template>
    <!--Small tag-->
    <xsl:template match="small">
        <xsl:copy>
            <xsl:call-template name="copyStandardAttrs"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    <!--Span tag-->
    <xsl:template match="span">
        <xsl:copy>
            <xsl:call-template name="copyStandardAttrs"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    <!--Strong tag-->
    <xsl:template match="strong">
        <xsl:copy>
            <xsl:call-template name="copyStandardAttrs"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    <!--Table tag-->
    <xsl:template match="table">
       <xsl:copy>
            <xsl:call-template name="copyStandardAttrs"/>
            <xsl:attribute name="cols"><xsl:call-template name="getCols"><xsl:with-param name="nodeset" select="tr[1]/td"/></xsl:call-template></xsl:attribute>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    <!--Td tag-->
    <xsl:template match="td">
        <xsl:copy>
            <xsl:call-template name="copyStandardAttrs"/>
            <xsl:copy-of select="@align"/>
            <xsl:copy-of select="@bgcolor"/>
            <xsl:copy-of select="@colspan"/>
            <xsl:copy-of select="@height"/>
            <xsl:copy-of select="@nowrap"/>
            <xsl:copy-of select="@rowspan"/>
            <xsl:copy-of select="@valign"/>
            <xsl:copy-of select="@width"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    <!--Tr tag-->
    <xsl:template match="tr">
        <xsl:copy>
            <xsl:call-template name="copyStandardAttrs"/>
            <xsl:copy-of select="@align"/>
            <xsl:copy-of select="@bgcolor"/>
            <xsl:copy-of select="@valign"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    <!--Ul tag-->
    <xsl:template match="ul">
        <xsl:copy>
            <xsl:call-template name="copyStandardAttrs"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    <!-- Comments-->
    <xsl:template match="comment()">
    </xsl:template>
</xsl:stylesheet>
<!--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Oct-03	440/1	doug	VBM:2003102911 Added Flow control process to tail of all pipelines

 ===========================================================================
-->
