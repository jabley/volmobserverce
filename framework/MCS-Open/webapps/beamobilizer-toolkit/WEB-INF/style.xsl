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
 * 
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
	
	<xsl:param name="defaultPortalLayout" select="'/default-portal-layout.mlyt'"/>
	<xsl:param name="defaultPortletLayout" select="'/default-portlet-layout.mlyt'"/>

	<xsl:param name="defaultSubmitClass" select="'linkSubmit'"/>
	
	<xsl:param name="defaultPortletTitlebarPane" select="'portletTitlebar'"/>
	<xsl:param name="defaultPortletContentPane" select="'portletContent'"/>

	<xsl:param name="convertTableElementTo" select="'div'"/>	
	<xsl:param name="convertTdElementTo" select="'div'"/>
	<xsl:param name="convertPElementTo" select="'div'"/>
	
	<xsl:param name="layoutDataSeparator" select="'-'"/>
	<xsl:param name="layoutDataMarker" select="'canvas-'"/>
	
	<xsl:param name="captionMarker" select="'caption-'"/>

	<xsl:param name="defaultFormName" select="'Form'"/>
	<xsl:param name="defaultTextareaClass" select="'textarea'"/>
	<xsl:param name="defaultInputtextClass" select="'inputtext'"/>
	<xsl:param name="defaultRadioClass" select="'radio'"/>
	<xsl:param name="defaultCheckboxClass" select="'checkbox'"/>	
	<xsl:param name="defaultEntryPaneSuffix" select="'Entry'"/>	
	<xsl:param name="defaultCaptionPaneSuffix" select="'Caption'"/>
	<xsl:param name="defaultControlName" select="'UnnamedControl'"/>

	

    <xsl:include href="volantis-xsl/applet.xsl"/>
	<xsl:include href="volantis-xsl/edit.xsl"/>
	<xsl:include href="volantis-xsl/forms.xsl"/>
	<xsl:include href="volantis-xsl/frames.xsl"/>
    <xsl:include href="volantis-xsl/hypertext.xsl"/>
	<xsl:include href="volantis-xsl/image.xsl"/>
	<xsl:include href="volantis-xsl/legacy.xsl"/>
	<xsl:include href="volantis-xsl/list.xsl"/>
    <xsl:include href="volantis-xsl/misc.xsl"/>
    <xsl:include href="volantis-xsl/object.xsl"/>
    <xsl:include href="volantis-xsl/presentation.xsl"/>
    <xsl:include href="volantis-xsl/tables.xsl"/>	
	<xsl:include href="volantis-xsl/text.xsl"/>
	<xsl:include href="volantis-xsl/diselect.xsl"/>

    <!-- Just pass on the content of the html element -->
    <xsl:template match="xh:html">
        <xsl:apply-templates/>
    </xsl:template>
	
	
    <!-- Ignore the head element -->
    <xsl:template match="xh:head">
    </xsl:template>
	
	
	<!--
	// Body causes generation of a canvas
	-->
    <xsl:template match="xh:body">
		<!-- Work out the layout name to be used -->
		<xsl:variable name="templateData" select="xh:div/@class"/>
		<xsl:variable name="skinLayout" select="//xh:head/xh:link[@type = 'mcsLayout']/@rel"/>
		<xsl:variable name="skinTheme" select="//xh:head/xh:link[@type = 'mcsTheme']/@rel"/>
		
		<xsl:variable name="layout">
			<xsl:choose>
				<!-- The portal layout is specified in the custom template -->
				<xsl:when test="$templateData and contains($templateData, $layoutDataMarker)">
					<xsl:value-of select="concat('/',substring-before(substring-after($templateData, $layoutDataMarker), $layoutDataSeparator),'.mlyt')"/>
				</xsl:when>
				<!-- The portal layout is specified in the skins.properties file -->
				<xsl:when test="$skinLayout and $skinLayout != ''">
					<xsl:value-of select="$skinLayout"/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$defaultPortalLayout"/>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>
		
		<xsl:variable name="theme">
			<xsl:choose>
				<!-- The portal layout is specified in the custom template -->
				<xsl:when test="$templateData and contains($templateData, $layoutDataMarker)">
					<xsl:value-of select="concat('/',substring-after(substring-after($templateData, $layoutDataMarker), $layoutDataSeparator),'.mthm')"/>
				</xsl:when>
				<!-- The portal theme is specified in the skins.properties file -->
				<xsl:when test="$skinTheme and $skinTheme != ''">
					<xsl:value-of select="$skinTheme"/>
				</xsl:when>
				<xsl:otherwise>
					<!-- Not theme has been specified -->
				</xsl:otherwise>
			</xsl:choose>
		</xsl:variable>

		<xsl:variable name="title">
		  <xsl:value-of select="xh:html/xh:head/xh:title"/>
		</xsl:variable>

		<xsl:message>portal: layout[<xsl:value-of select="$layout"/>], theme[<xsl:value-of select="$theme"/>]</xsl:message>
		
		
		<!-- Generate the portal canvas --> 
		 <xsl:element name="canvas">
			<xsl:attribute namespace="" name="layoutName">
			  <xsl:value-of select="$layout"/>
			</xsl:attribute> 
			<xsl:if test="$theme and $theme != ''">
				<xsl:attribute namespace="" name="theme">
					<xsl:value-of select="$theme"/>
				</xsl:attribute> 
			</xsl:if>
			<xsl:attribute namespace="" name="type">
			  <xsl:text>portal</xsl:text>
			</xsl:attribute> 
			<xsl:if test="$title and $title != ''">
				<xsl:attribute namespace="" name="pageTitle">
					<xsl:value-of select="$title"/>
				</xsl:attribute> 
			</xsl:if>
			<xsl:attribute namespace="" name="class" >
			  <xsl:text>bea-portal-body</xsl:text>
			</xsl:attribute> 
			<xsl:copy-of select="/xh:html/@xml:base"/>
            <xsl:apply-templates select="//xh:div[@class = 'bea-portal-body-content']"/>
        </xsl:element> 
    </xsl:template>

	<!--
	// portlets go to different regions whose names are based on a position in portal grid 
	-->
	<xsl:template match="//xh:table[@class = 'bea-portal-layout-grid']/xh:tr">
		<xsl:for-each select="xh:td[@class = 'bea-portal-layout-placeholder-container']">
					
			<xsl:variable name="column">
				<xsl:value-of select="position()"/>
			</xsl:variable>
			
			<xsl:for-each select="current()//xh:div[@class = 'bea-portal-window']">

				<xsl:variable name="templateData" select="../../xh:div/@class"/>
				
				<xsl:variable name="layout">			
					<xsl:choose>
						<xsl:when test="contains($templateData, $layoutDataMarker)">
							<xsl:value-of select="concat('/',substring-before(substring-after($templateData, $layoutDataMarker), $layoutDataSeparator),'.mlyt')"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="$defaultPortletLayout"/>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>

				<xsl:variable name="theme">			
					<xsl:choose>
						<xsl:when test="contains($templateData, $layoutDataMarker)">
							<xsl:value-of select="concat('/',substring-after(substring-after($templateData, $layoutDataMarker), $layoutDataSeparator),'.mthm')"/>
						</xsl:when>
						<xsl:otherwise>
							<!-- Not theme has been specified -->
						</xsl:otherwise>
					</xsl:choose>
				</xsl:variable>
					
		
				<xsl:message>portlet[<xsl:value-of select="$column"/>, <xsl:value-of select="position()"/>]: layout[<xsl:value-of select="$layout"/>], theme[<xsl:value-of select="$theme"/>]</xsl:message>

				<!-- Place the portlet in the appropriate region -->
				<xsl:element name="region">
					<xsl:attribute name="name" namespace="">RegionC<xsl:value-of select="$column"/>R<xsl:value-of select="position()"/></xsl:attribute>
					<xsl:text>&#13;</xsl:text>
					<xsl:element name="canvas">
						<xsl:attribute name="type" namespace="">portlet</xsl:attribute>
						<xsl:attribute name="class" namespace="">bea-portal-window</xsl:attribute>
						<xsl:attribute name="layoutName" namespace="">
							<xsl:value-of select="$layout"/>
						</xsl:attribute>
						
						<xsl:if test="$theme and $theme != ''">
							<xsl:attribute namespace="" name="theme">
								<xsl:value-of select="$theme"/>
							</xsl:attribute> 
						</xsl:if>
						<xsl:text>&#13;</xsl:text>
						<xsl:apply-templates/>
					</xsl:element>
				</xsl:element>
				
			</xsl:for-each>
		
		</xsl:for-each>
	</xsl:template>

	<!-- 
	// portal header
	-->
	<xsl:template match="xh:div[@class = 'bea-portal-body-header']">
		<xsl:element name="pane">
			<xsl:attribute name="name" namespace="">header</xsl:attribute>
			<xsl:apply-templates/>
		</xsl:element>
	</xsl:template>
	
	
	
	<!--
	// portal content
	-->
	<xsl:template match="xh:div[@class = 'bea-portal-body-content']">
		<xsl:apply-templates select="xh:div[@class = 'bea-portal-body-header']"/>
		<xsl:apply-templates select="//xh:table[@class = 'bea-portal-layout-grid']/xh:tr"/>
	</xsl:template>


	<!--
	// portlet titlebar area
	-->
	<xsl:template match="xh:div[@class = 'bea-portal-window-titlebar']">
		<xsl:element name="pane">
			<xsl:attribute name="name" namespace="">
				<xsl:value-of select="$defaultPortletTitlebarPane"/>
			</xsl:attribute>
			<xsl:attribute name="class" namespace="">bea-portal-window-titlebar</xsl:attribute>
			<xsl:element name="div">
				<xsl:apply-templates select="current()//xh:td[@class='bea-portal-window-titlebar-title']"/>
			</xsl:element>
		</xsl:element>
	</xsl:template>
	
	<!--
	// portlet content area
	-->
	<xsl:template match="xh:div[@class = 'bea-portal-window-content']">
		<xsl:element name="pane">
			<xsl:attribute name="name" namespace="">
				<xsl:value-of select="$defaultPortletContentPane"/>
			</xsl:attribute>
			<xsl:attribute name="class" namespace="">bea-portal-window-content</xsl:attribute>
			<xsl:apply-templates/> 
		</xsl:element>
	</xsl:template>
	
	
</xsl:stylesheet>
