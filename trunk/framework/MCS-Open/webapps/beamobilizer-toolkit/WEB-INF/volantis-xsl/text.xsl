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
// http://www.w3.org/TR/xhtml-modularization/abstract_modules.html#s_textmodule:
// abbr, acronym, address, blockquote, br, cite, code, dfn, div, em, h1-h6, kbd, 
// p, pre, q, samp, span, strong, var
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
	// puts the content within " &quot; signs
	-->
	<xsl:template match="xh:q">
		<xsl:text>&quot;</xsl:text><xsl:apply-templates/><xsl:text>&quot;</xsl:text>
	</xsl:template>

	<!-- 
	// ignores tag, show content
	-->
	<xsl:template match="xh:abbr | xh:acronym">
		<xsl:value-of select="."/>
	</xsl:template>

		
	<!--
	// creates the same element and copies all attributes; pane attribute based
	// on pane selector in class attribute is added; the content of element is processed
	-->
	<xsl:template match="xh:h1 | xh:h2 | xh:h3 | xh:h4 | xh:h5 | xh:h6 | xh:address | xh:div | xh:pre | xh:blockquote | xh:p">
		<xsl:element name="{local-name()}">
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
	</xsl:template>
	
	
	<!--
	// creates the same element and copies all attributes; the content of element is processed
	-->
	<xsl:template match="xh:cite | xh:code | xh:em | xh:kbd | xh:samp | xh:strong | xh:span">
		<xsl:element name="{local-name()}">
			<xsl:copy-of select="@*"/>
			<xsl:apply-templates/>
		</xsl:element>
	</xsl:template>
	
	<!-- 
	// span elements next to radio buttons are labels so remove them
	-->
	<xsl:template match="xh:span[preceding::node()[1][self::xh:input[@type='radio' or @type='checkbox']]]">
	</xsl:template>
	
	<!--
	// empty br element
	-->
	<xsl:template match="xh:br">
		<xsl:element name="br"/>
	</xsl:template>
	
	
	<!--
	// converts element to span element; pane selectors are ignored; if class attribute exists
	// then it's copied to span element, if not the default class for given element  is used
	-->
	<xsl:template match="xh:dfn | xh:var">
		<xsl:element name="span">

			<xsl:variable name="temp" select="normalize-space(substring-before(concat(@class, ','), ','))"/>
			<xsl:choose>
				<xsl:when test="$temp">
					<xsl:attribute name="class" namespace=""><xsl:value-of select="$temp"/></xsl:attribute>
				</xsl:when>
				<xsl:otherwise>
					<xsl:attribute name="class" namespace="">span.<xsl:value-of select="local-name()"/></xsl:attribute>
				</xsl:otherwise>
			</xsl:choose>					

			<xsl:copy-of select="@*[ name() != 'class']"/>
			<xsl:apply-templates/>
			
		</xsl:element>	
	</xsl:template>	
	
	
</xsl:stylesheet>

