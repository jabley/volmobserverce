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
// http://www.w3.org/TR/xhtml-modularization/abstract_modules.html#s_metamodule,
// http://www.w3.org/TR/xhtml-modularization/abstract_modules.html#s_scriptmodule,
// http://www.w3.org/TR/xhtml-modularization/abstract_modules.html#s_stylemodule,
// http://www.w3.org/TR/xhtml-modularization/abstract_modules.html#s_linkmodule,
// http://www.w3.org/TR/xhtml-modularization/abstract_modules.html#s_basemodule,
// http://www.w3.org/TR/xhtml-modularization/abstract_modules.html#s_imapmodule:
// base, link, style, noscript, script, meta, map, area
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
	// removes element and its content
	-->
	<xsl:template match="xh:base | xh:link | xh:style | xh:script | xh:noscript | xh:meta | xh:map | xh:area">
	</xsl:template>
	
	
	<!--
	// removes all caption markers
	// note: first check if attribute contains caption marker at all 
	// and then check if caption marker format is valid 
	// this method should be a bit faster
	-->
	<xsl:template 
		match="*[@class and contains(@class, 'caption-') and contains(@class, ',') and starts-with(normalize-space(substring-after(@class, ',')), 'caption-')]"
		priority="2"/>
		
	
	<!--
	// ignore elements within form, which include formelements (input, textarea etc)
	-->
	<xsl:template 
		match="*[ancestor::xh:form and (descendant::xh:input or descendant::xh:select or descendant::xh:textarea or descendant::xh:button)]"
		priority="2">
			<xsl:apply-templates/>
	</xsl:template>
		
		
		
	
</xsl:stylesheet>

