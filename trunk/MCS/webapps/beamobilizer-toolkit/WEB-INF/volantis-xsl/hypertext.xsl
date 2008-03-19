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
// http://www.w3.org/TR/xhtml-modularization/abstract_modules.html#s_hypertextmodule:
// a
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
	// creates the same element and copies all attributes; pane attribute based
	// on pane selector in class attribute is added; the content of element is processed
	// filters elements:
	// 	- don't have href attribute 
	//  - href attribute starts with #
	//  - href attribute starts with javascript:
	// 	- href attribyte starts with https: (?)
	-->
	<xsl:template match="xh:a">
	
		<xsl:if test="@href and not(starts-with(@href,'#') or starts-with(@href,'javascript:') or starts-with(@href,'https:'))">
			<xsl:element name="a">
				<xsl:if test="@class and contains(@class, ',')">
					<xsl:attribute name="pane" namespace=""><xsl:value-of select="normalize-space(substring-after(@class, ','))"/></xsl:attribute>
				</xsl:if>

				<xsl:variable name="temp" select="normalize-space(substring-before(concat(@class, ','), ','))"/>
				<xsl:if test="$temp">
					<xsl:attribute name="class" namespace=""><xsl:value-of select="$temp"/></xsl:attribute>
				</xsl:if>

				<xsl:copy-of select="@*[ name() != 'class']"/>
				<xsl:apply-templates/>
			
			</xsl:element>
		</xsl:if>
	</xsl:template>
	
</xsl:stylesheet>

