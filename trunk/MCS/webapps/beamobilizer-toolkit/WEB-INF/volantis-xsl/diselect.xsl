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
<!-- // stylesheet based on stylesheet used for converting the DISelect tags to XDIME tags in XDIME-CP. -->


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
	 ! Strips the sel namespace prefix from the selid attribute and renames the
	 ! attribute to id.
	 -->
	<xsl:template match="@sel:selid">
		<xsl:attribute name="id" namespace="">
			<xsl:value-of select="."/>
		</xsl:attribute>
	</xsl:template>


	<!--
	 ! Checks that an element does not have both a sel:selid and id attributes.
	 ! If these are both present then the stylesheet aborts. Note that this
	 ! template takes precedence over the template which matches on "@sel:selid"
	 ! as it is more specific.
	 -->
	<xsl:template match="*[@sel:selid and @id]">
		<xsl:message terminate="yes">
			<xsl:text>Cannot have both a sel:selid and an id attribute on </xsl:text>
			<xsl:value-of select="name()"/>
			<xsl:text>.</xsl:text>
		</xsl:message>
	</xsl:template>


	<!--
	 ! Strips the sel namespace prefix from the expr attribute.
	 -->
	<xsl:template match="@sel:expr">
		<xsl:attribute name="expr" namespace="">
			<xsl:value-of select="."/>
		</xsl:attribute>
	</xsl:template>


	<!--
	 ! Transforms a sel:if element into a select/when. The when element
	 ! gets the attributes and content of the sel:if element.
	 -->
	<xsl:template match="sel:if">
		<xsl:element name="select">
			<xsl:element name="when">
				<xsl:apply-templates select="@*"/>
				<xsl:apply-templates/>
			</xsl:element>
		</xsl:element>
	</xsl:template>


	<!--
	 ! Srips the sel namespace prefix from the expr attribute of sel:if and
	 ! sel:when elements.
	 -->
	<xsl:template match="sel:if/@expr | sel:when/@expr">
		<xsl:attribute name="expr" namespace="">
			<xsl:value-of select="."/>
		</xsl:attribute>
	</xsl:template>


	<!--
	 ! Srips the sel namespace prefix from the sel:select element and deals
	 ! with any expr attribute by transforming it to a select/when.
	 -->
	<xsl:template match="sel:select">
		<xsl:element name="select">
			<xsl:choose>
				<xsl:when test="@expr">
					<xsl:element name="when">
						<xsl:attribute name="expr" namespace="">
							<xsl:value-of select="@expr"/>
						</xsl:attribute>
						<xsl:apply-templates/>
					</xsl:element>
				</xsl:when>
				<xsl:otherwise>
					<xsl:apply-templates/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:element>
	</xsl:template>


	<!--
	 ! Srips the sel namespace prefix from the sel:when element.
	 -->
	<xsl:template match="sel:when">
		<xsl:element name="when">
			<xsl:apply-templates select="@*"/>
			<xsl:apply-templates/>
		</xsl:element>
	</xsl:template>


	<!--
	 ! Srips the sel namespace prefix from the sel:otherwise element.
	 -->
	<xsl:template match="sel:otherwise">
		<xsl:element name="otherwise">
			<xsl:apply-templates/>
		</xsl:element>
	</xsl:template>	
	
</xsl:stylesheet>

