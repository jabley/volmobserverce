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
 * ============================================================================
 * (c) Volantis Systems Ltd 2005. 
 * ============================================================================
-->

<!-- 
// source xhtml elements in this stylesheet come from 
// http://www.w3.org/TR/xhtml-modularization/abstract_modules.html#s_tablemodule:
// caption, table, td, th, tr, col, colgroup, tbody, thead, tfoot
-->


<xsl:stylesheet version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:cdm="http://www.volantis.com/xmlns/marlin-cdm"
    xmlns:pipeline="http://www.volantis.com/xmlns/marlin-pipeline"
    xmlns:template="http://www.volantis.com/xmlns/marlin-template"
    xmlns:urid="http://www.volantis.com/xmlns/marlin-uri-driver"
    xmlns:xh="http://www.w3.org/1999/xhtml"
	xmlns:sel="http://www.w3.org/2004/06/diselect">
	
	<xsl:output method="xml" indent="yes"/>


	<!--
	// convert table, tbody, tr elements into div if pane selector
	// todo: 
	//	- just apply-templates if form inside 
	-->
	<xsl:template match="xh:table | xh:tbody | xh:thead | xh:tfoot | xh:tr">
		<xsl:choose>
			<xsl:when test="@class and contains(@class, ',')">

				<!-- // convert to div if there is pane selector in the class attribute -->
				<xsl:element name="{$convertTableElementTo}">
					<xsl:attribute name="pane" namespace=""><xsl:value-of select="normalize-space(substring-after(@class, ','))"/></xsl:attribute>
					<xsl:variable name="temp" select="normalize-space(substring-before(concat(@class, ','), ','))"/>
					<xsl:if test="$temp">
						<xsl:attribute name="class" namespace=""><xsl:value-of select="$temp"/></xsl:attribute>
					</xsl:if>					
					<xsl:apply-templates/>
				</xsl:element>	
				
			</xsl:when>
			<xsl:otherwise>
				<!-- // ignore element if no pane selector -->
				<xsl:apply-templates/>
			
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>	



	<!--
	// change td to div, if td has a caption selector then do not output this element
	// when childs are form elements don't generate td (form elements must be enclosed directly within a form)
	-->
	<xsl:template match="xh:td | xh:th">
		<xsl:element name="div">

			<xsl:if test="@class and contains(@class, ',')">
				<xsl:attribute name="pane" namespace=""><xsl:value-of select="normalize-space(substring-after(@class, ','))"/></xsl:attribute>
			</xsl:if>

			<xsl:variable name="temp" select="normalize-space(substring-before(concat(@class, ','), ','))"/>

			<xsl:if test="$temp">
				<xsl:attribute name="class" namespace=""><xsl:value-of select="$temp"/></xsl:attribute>
			</xsl:if>
			
			<xsl:apply-templates/>
		</xsl:element>
	</xsl:template>

	<!--
	// removes element and its content
	-->
	<xsl:template match="xh:caption | xh:col | xh:colgroup">
	</xsl:template>
	
	
</xsl:stylesheet>

