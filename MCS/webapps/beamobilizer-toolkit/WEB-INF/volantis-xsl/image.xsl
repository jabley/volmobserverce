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
// http://www.w3.org/TR/xhtml-modularization/abstract_modules.html#s_imagemodule:
// img
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
	// image components
	-->
	<xsl:template match="xh:img">
		<xsl:variable name="componentWithParam">
			<xsl:call-template name="substring-after-last">
				<xsl:with-param name="input" select="@src"/>
				<xsl:with-param name="substr">/</xsl:with-param>
				<xsl:with-param name="until">?component=</xsl:with-param>				
			</xsl:call-template>
		</xsl:variable>
		<xsl:variable name="temp" select="substring-before(substring-before(concat($componentWithParam, '?'), '?'),'.')"/>
		<xsl:variable name="imgURL">
			<xsl:choose>
				<xsl:when test="contains($componentWithParam, '?component=')">
					<!-- The URL specifies that we need to reference an image component and supplies a prefix for its path -->
					<xsl:value-of select="concat('/', substring-after($componentWithParam, '?component='),'/',$temp, '.mimg')"/>
				</xsl:when>
				<xsl:when test="contains($componentWithParam, '?component')">
					<!-- The URL specifies that we need to reference an image component but there is no additional prefix -->
					<xsl:value-of select="concat('/',$temp, '.mimg')"/>
				</xsl:when>
				<xsl:otherwise>
				<!-- Ther is no component so use the URL unchanged-->
					<xsl:value-of select="@src"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		<!-- Generate the img element -->
			<xsl:element name="img">
				<xsl:choose>
					<xsl:when test="contains($imgURL, '.mimg')">
						<!-- The URL references an image component so use the src attribute -->
						<xsl:attribute name="src" namespace=""><xsl:value-of select="$imgURL"/></xsl:attribute>
					</xsl:when>
					<xsl:otherwise>
						<!-- The URL references an image that must be transcoded to fit the device so use the urlc attribute -->
						<xsl:attribute name="urlc" namespace=""><xsl:value-of select="$imgURL"/></xsl:attribute>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:copy-of select="@id"/>
				<xsl:copy-of select="@class"/>
				<xsl:copy-of select="@alt"/>
			</xsl:element>
	</xsl:template>
	
	<!--
	// get part of the input after last substr
	-->
	<xsl:template name="substring-after-last">
		<xsl:param name="input"/>
		<xsl:param name="substr"/>
		<xsl:param name="until"/>

		<xsl:choose>
			<xsl:when test="$substr and contains($input, $substr)">
				<xsl:variable name="before" select="substring-before($input, $substr)"/>
				<xsl:variable name="temp" select="substring-after($input, $substr)"/>
		
				<xsl:choose>
					<xsl:when test="not(contains($temp, $until)) and contains($before, $until)">
						<xsl:value-of select="$input"/>
						
					</xsl:when>
					<xsl:when test="$substr and contains($temp,$substr)">
						<xsl:call-template name="substring-after-last">
							<xsl:with-param name="input" select="$temp"/>
							<xsl:with-param name="substr" select="$substr"/>
							<xsl:with-param name="until" select="$until"/>
						</xsl:call-template>
						
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="$temp"/>
					</xsl:otherwise>
				</xsl:choose>
				
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$input"/>
				
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	
</xsl:stylesheet>
