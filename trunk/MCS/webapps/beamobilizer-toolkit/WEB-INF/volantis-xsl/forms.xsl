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
// http://www.w3.org/TR/xhtml-modularization/abstract_modules.html#s_extformsmodule:
// form, input, select, option, textarea, button, fieldset, label, legend, optgroup
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:cdm="http://www.volantis.com/xmlns/marlin-cdm" xmlns:pipeline="http://www.volantis.com/xmlns/marlin-pipeline" xmlns:template="http://www.volantis.com/xmlns/marlin-template" xmlns:urid="http://www.volantis.com/xmlns/marlin-uri-driver" xmlns:xh="http://www.w3.org/1999/xhtml" xmlns:sel="http://www.w3.org/2004/06/diselect">
	<xsl:output method="xml" indent="yes"/>
	<!--
	// converts the form element to xfform, features:
	// 	- nested form is removed
	//	- form name is based on selector in class attribute, original form
	//		 name is ignored (to achieve default layout)
	//	- all attributes except class and name are copied 
	-->
	<xsl:template match="xh:form">
		<xsl:choose>
			<xsl:when test="ancestor::xh:form">
				<xsl:message>nested form, removed</xsl:message>
			</xsl:when>
			<xsl:otherwise>
				<xsl:element name="xfform">
					<!-- // form name -->
					<xsl:choose>
						<xsl:when test="@class and contains(@class, ',')">
							<xsl:attribute name="name" namespace=""><xsl:value-of select="normalize-space(substring-after(@class, ','))"/></xsl:attribute>
						</xsl:when>
						<xsl:otherwise>
							<xsl:attribute name="name" namespace=""><xsl:value-of select="$defaultFormName"/></xsl:attribute>
						</xsl:otherwise>
					</xsl:choose>
					<!-- // style class -->
					<xsl:variable name="tempClass">
						<xsl:value-of select="normalize-space(substring-before(concat(@class, ','), ','))"/>
					</xsl:variable>
					<xsl:if test="$tempClass">
						<xsl:attribute name="class" namespace=""><xsl:value-of select="$tempClass"/></xsl:attribute>
					</xsl:if>
					<xsl:copy-of select="@*[name()!='class' and name()!='name']"/>
					<xsl:apply-templates/>
				</xsl:element>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- 
		Work out the name to use for an xfforms input control. 
		It could be within an id or a name attribute
	 -->
	<xsl:template name="getControlName">
		<xsl:variable name="tagName">
			<xsl:value-of select="substring-before(substring-after(current()/@name, '{actionForm.'), '}')"/>
		</xsl:variable>
		<xsl:variable name="tagId">
			<xsl:value-of select="substring-before(substring-after(current()/@id, '{actionForm.'), '}')"/>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="$tagId and $tagId != ''">
				<xsl:value-of select="$tagId"/>
			</xsl:when>
			<xsl:when test="$tagName and $tagName != ''">
				<xsl:value-of select="$tagName"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$defaultControlName"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- 
		Work out the caption to use for an xfforms input control. 
		It could be within a label element or a class attribute 
	-->
	<xsl:template name="getControlCaption">
		<xsl:param name="controlName"/>
		<xsl:variable name="labelCaption">
			<xsl:value-of select="ancestor::xh:form//xh:label[@for=$controlName]"/>
		</xsl:variable>
		<xsl:variable name="classCaption">
			<xsl:value-of select="ancestor::xh:form//*[contains(@class, concat($captionMarker, $controlName))]"/>
		</xsl:variable>
		<xsl:choose>
			<xsl:when test="$labelCaption">
				<xsl:value-of select="$labelCaption"/>
			</xsl:when>
			<xsl:when test="$classCaption">
				<xsl:value-of select="$classCaption"/>
			</xsl:when>
		</xsl:choose>
	</xsl:template>
	<!-- 
		Work out the entry pane name 
	-->
	<xsl:template name="getEntryPaneName">
		<xsl:param name="controlName"/>
		<xsl:choose>
			<xsl:when test="@class and contains(@class, ',')">
				<xsl:value-of select="normalize-space(substring-after(@class, ','))"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="concat($controlName,$defaultEntryPaneSuffix)"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!-- 
		Work out the caption pane name 
	-->
	<xsl:template name="getCaptionPaneName">
		<xsl:param name="controlName"/>
		<xsl:choose>
			<xsl:when test="@class and contains(@class, ',')">
				<xsl:value-of select="normalize-space(substring-after(@class, ','))"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="concat($controlName,$defaultCaptionPaneSuffix)"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>
	<!--
	// textarea, input text, input password
	// transform element to xftextinput, features:
	// - pane selector on class attribute
	// - looking for name in name and id attribute
	// - caption based on label element or element in form with caption marker
	// - style class based on provided class attribute or default (different for text input 
	//		and textarea)
	-->
	<xsl:template match="xh:textarea|xh:input[@type='text' or @type='password' or not(@type)]">
		<xsl:variable name="controlName">
			<xsl:call-template name="getControlName"/>
		</xsl:variable>
		<xsl:variable name="controlCaption">
			<xsl:call-template name="getControlCaption">
				<xsl:with-param name="controlName" select="$controlName"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:variable name="entryPaneName">
			<xsl:call-template name="getEntryPaneName">
				<xsl:with-param name="controlName" select="$controlName"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:variable name="captionPaneName">
			<xsl:call-template name="getCaptionPaneName">
				<xsl:with-param name="controlName" select="$controlName"/>
			</xsl:call-template>
		</xsl:variable>
		
		<xsl:element name="xftextinput">
			<xsl:attribute name="name" namespace=""><xsl:value-of select="current()/@name"/></xsl:attribute>
			<xsl:attribute name="caption" namespace=""><xsl:value-of select="$controlCaption"/></xsl:attribute>
			<xsl:attribute name="entryPane" namespace=""><xsl:value-of select="$entryPaneName"/></xsl:attribute>
			<xsl:attribute name="captionPane" namespace=""><xsl:value-of select="$captionPaneName"/></xsl:attribute>
			<xsl:attribute name="initial" namespace=""><xsl:value-of select="current()/@value"/></xsl:attribute>
			<!-- // style class -->
			<xsl:variable name="tempClass">
				<xsl:value-of select="normalize-space(substring-before(concat(@class, ','), ','))"/>
			</xsl:variable>
			<xsl:choose>
				<!-- // set provided class attribute -->
				<xsl:when test="$tempClass">
					<xsl:attribute name="class" namespace=""><xsl:value-of select="normalize-space(substring-before(concat(@class, ','), ','))"/></xsl:attribute>
				</xsl:when>
				<xsl:otherwise>
					<xsl:choose>
						<!-- // class attribute depends on field type -->
						<xsl:when test="local-name()='textarea'">
							<xsl:attribute name="class" namespace=""><xsl:value-of select="$defaultTextareaClass"/></xsl:attribute>
						</xsl:when>
						<xsl:otherwise>
							<xsl:attribute name="class" namespace=""><xsl:value-of select="$defaultInputtextClass"/></xsl:attribute>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:copy-of select="@*[name()='maxlength' or name()='type']"/>
		</xsl:element>
	</xsl:template>
	<!-- 
		convert input hidden to xfimplicit with class attribute hidden
	-->
	<xsl:template match="xh:input[@type='hidden']">
		<xsl:element name="xfimplicit">
			<xsl:attribute name="class" namespace="">hidden</xsl:attribute>
			<!-- // name -->
			<xsl:choose>
				<xsl:when test="@name">
					<xsl:attribute name="name" namespace=""><xsl:value-of select="@name"/></xsl:attribute>
				</xsl:when>
				<xsl:when test="@id">
					<xsl:attribute name="name" namespace=""><xsl:value-of select="@id"/></xsl:attribute>
				</xsl:when>
			</xsl:choose>
			<!-- // value -->
			<xsl:attribute name="value" namespace=""><xsl:value-of select="@value"/></xsl:attribute>
		</xsl:element>
	</xsl:template>
	<!--
		Submit
	-->
	<xsl:template match="xh:input[@type='submit' or @type='reset']">
		<xsl:variable name="entryPaneName">
			<xsl:call-template name="getEntryPaneName">
				<xsl:with-param name="controlName" select="current()/@type"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:element name="xfaction">
			<xsl:attribute name="type" namespace=""><xsl:value-of select="@type"/></xsl:attribute>
			<xsl:attribute name="entryPane" namespace=""><xsl:value-of select="$entryPaneName"/></xsl:attribute>
			<xsl:if test="@value">
				<xsl:attribute name="value" namespace=""><xsl:value-of select="@value"/></xsl:attribute>
				<xsl:attribute name="caption" namespace=""><xsl:value-of select="@value"/></xsl:attribute>
			</xsl:if>
			<xsl:variable name="tempClass">
				<xsl:value-of select="normalize-space(substring-before(concat(@class, ','), ','))"/>
			</xsl:variable>
			<xsl:choose>
				<xsl:when test="$tempClass">
					<xsl:attribute name="class" namespace=""><xsl:value-of select="$tempClass"/></xsl:attribute>
				</xsl:when>
				<xsl:otherwise>
					<xsl:attribute name="class" namespace=""><xsl:value-of select="$defaultSubmitClass"/></xsl:attribute>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:element>
	</xsl:template>
	<!--
		Button
	-->
	<xsl:template match="xh:button[@type='submit' or @type='reset']">
		<xsl:variable name="entryPaneName">
			<xsl:call-template name="getEntryPaneName">
				<xsl:with-param name="controlName" select="current()/@type"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:element name="xfaction">
			<xsl:attribute name="type" namespace=""><xsl:value-of select="@type"/></xsl:attribute>
			<xsl:attribute name="entryPane" namespace=""><xsl:value-of select="$entryPaneName"/></xsl:attribute>
			<xsl:if test="@value">
				<xsl:attribute name="value" namespace=""><xsl:value-of select="@value"/></xsl:attribute>
			</xsl:if>
			<xsl:attribute name="caption" namespace=""><xsl:value-of select="."/></xsl:attribute>
			<xsl:variable name="tempClass">
				<xsl:value-of select="normalize-space(substring-before(concat(@class, ','), ','))"/>
			</xsl:variable>
			<xsl:choose>
				<xsl:when test="$tempClass">
					<xsl:attribute name="class" namespace=""><xsl:value-of select="$tempClass"/></xsl:attribute>
				</xsl:when>
				<xsl:otherwise>
					<xsl:attribute name="class" namespace=""><xsl:value-of select="$defaultSubmitClass"/></xsl:attribute>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:element>
	</xsl:template>
	<!--
	-->
	<!-- 
		Single selection
	-->
	<xsl:template match="xh:select">
		<xsl:variable name="controlName">
			<xsl:call-template name="getControlName"/>
		</xsl:variable>
		<xsl:variable name="controlCaption">
			<xsl:call-template name="getControlCaption">
				<xsl:with-param name="controlName" select="$controlName"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:variable name="entryPaneName">
			<xsl:call-template name="getEntryPaneName">
				<xsl:with-param name="controlName" select="$controlName"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:variable name="captionPaneName">
			<xsl:call-template name="getCaptionPaneName">
				<xsl:with-param name="controlName" select="$controlName"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:element name="xfsiselect">
			<xsl:attribute name="name" namespace=""><xsl:value-of select="current()/@name"/></xsl:attribute>
			<xsl:attribute name="caption" namespace=""><xsl:value-of select="$controlCaption"/></xsl:attribute>
			<xsl:attribute name="entryPane" namespace=""><xsl:value-of select="$entryPaneName"/></xsl:attribute>
			<xsl:attribute name="captionPane" namespace=""><xsl:value-of select="$captionPaneName"/></xsl:attribute>
			<xsl:if test="count(child::xh:option[@selected]) = 1">
				<xsl:choose>
					<xsl:when test="child::xh:option[@selected and @value]">
						<xsl:attribute name="initial" namespace=""><xsl:value-of select="child::xh:option[@selected]/@value"/></xsl:attribute>
					</xsl:when>
					<xsl:otherwise>
						<xsl:attribute name="initial" namespace=""><xsl:value-of select="child::xh:option[@selected]"/></xsl:attribute>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:if>

			<xsl:variable name="tempClass">
				<xsl:value-of select="normalize-space(substring-before(concat(@class, ','), ','))"/>
			</xsl:variable>
			<xsl:choose>
				<xsl:when test="$tempClass">
					<xsl:attribute name="class" namespace=""><xsl:value-of select="$tempClass"/></xsl:attribute>
				</xsl:when>
				<xsl:otherwise>
					<xsl:attribute name="class" namespace=""><xsl:value-of select="$defaultSubmitClass"/></xsl:attribute>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:copy-of select="@*[name() != 'class']"/>
			<xsl:apply-templates/>
		</xsl:element>
	</xsl:template>
	
	
	
	<!-- 
		Multiple selection
	-->
	<xsl:template match="xh:select[@multiple]">
		<xsl:variable name="controlName">
			<xsl:call-template name="getControlName"/>
		</xsl:variable>
		<xsl:variable name="controlCaption">
			<xsl:call-template name="getControlCaption">
				<xsl:with-param name="controlName" select="$controlName"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:variable name="entryPaneName">
			<xsl:call-template name="getEntryPaneName">
				<xsl:with-param name="controlName" select="$controlName"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:variable name="captionPaneName">
			<xsl:call-template name="getCaptionPaneName">
				<xsl:with-param name="controlName" select="$controlName"/>
			</xsl:call-template>
		</xsl:variable>
		<xsl:element name="xfmuselect">
			<xsl:attribute name="name" namespace=""><xsl:value-of select="current()/@name"/></xsl:attribute>
			<xsl:attribute name="caption" namespace=""><xsl:value-of select="$controlCaption"/></xsl:attribute>
			<xsl:attribute name="entryPane" namespace=""><xsl:value-of select="$entryPaneName"/></xsl:attribute>
			<xsl:attribute name="captionPane" namespace=""><xsl:value-of select="$captionPaneName"/></xsl:attribute>
			<xsl:variable name="tempClass">
				<xsl:value-of select="normalize-space(substring-before(concat(@class, ','), ','))"/>
			</xsl:variable>
			<xsl:choose>
				<xsl:when test="$tempClass">
					<xsl:attribute name="class" namespace=""><xsl:value-of select="$tempClass"/></xsl:attribute>
				</xsl:when>
				<xsl:otherwise>
					<xsl:attribute name="class" namespace=""><xsl:value-of select="$defaultSubmitClass"/></xsl:attribute>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:copy-of select="@*[name() != 'class']"/>
			<xsl:apply-templates/>
		</xsl:element>
	</xsl:template>
	<!--
	// converts option element to xfoption, features
	//	- pane selector in class attribute
	//	- caption and label attribute are based on the original attributes or element value
	-->
	<xsl:template match="xh:option[normalize-space()]">
		<xsl:element name="xfoption">
			<xsl:variable name="temp" select="normalize-space(substring-before(concat(@class, ','), ','))"/>
			<xsl:if test="$temp">
				<xsl:attribute name="class" namespace=""><xsl:value-of select="$temp"/></xsl:attribute>
			</xsl:if>
			<!--
			// In HTML the option value can also be specified in the body
			// of the option
			-->
			<xsl:choose>
				<xsl:when test="@value">
					<xsl:attribute name="value" namespace=""><xsl:value-of select="@value"/></xsl:attribute>
				</xsl:when>
				<xsl:otherwise>
					<xsl:attribute name="value" namespace=""><xsl:value-of select="."/></xsl:attribute>
				</xsl:otherwise>
			</xsl:choose>
			<!--
			// If we have a label then we use it, otherwise we assume that the option
            // label should be derived from the body content of the select 
			// possible optimalization: it seems that BEA never generates label attribute
			-->
			<xsl:choose>
				<xsl:when test="@label">
					<xsl:attribute name="caption" namespace=""><xsl:value-of select="@label"/></xsl:attribute>
				</xsl:when>
				<xsl:otherwise>
					<xsl:attribute name="caption" namespace=""><xsl:value-of select="."/></xsl:attribute>
				</xsl:otherwise>
			</xsl:choose>
			<!-- 
			// The use of the selected attribute is only actually relevant to multi-select
			// selections but doesn't harm or hinder single selection definitions
			-->
			<xsl:if test="@selected">
				<xsl:attribute name="selected" namespace="">true</xsl:attribute>
			</xsl:if>
		</xsl:element>
	</xsl:template>
	<!--
	// converts optgroup element to xfoptgroup, the label attribute is renamed to caption;
	// all attributes except label are copied; content is processed
	-->
	<xsl:template match="xh:optgroup">
		<xsl:element name="xfoptgroup">
			<xsl:if test="@label">
				<xsl:attribute name="caption" namespace=""><xsl:value-of select="@label"/></xsl:attribute>
			</xsl:if>
			<xsl:copy-of select="@*[name() != 'label']"/>
			<xsl:apply-templates/>
		</xsl:element>
	</xsl:template>
	<!-- 
	// radio groups
	// this is the main radio group template.
	// this template ONLY generates any markup for the 1st item in the radio group,
	// bea workshop generates its radio button option as <input type="radio"><span>label</span>
	// so there is no relation between radio and its label in attributes, the code assumes that 
	// first span following the radio is radio label
	-->
	<xsl:template match="xh:input[@type='radio']">
		<xsl:variable name="radio_group_name">
			<xsl:choose>
				<xsl:when test="@name">
					<xsl:value-of select="@name"/>
				</xsl:when>
				<xsl:when test="@id">
					<xsl:value-of select="@id"/>
				</xsl:when>
			</xsl:choose>
		</xsl:variable>
		<!-- 
		// We only wish to generate the outer xfiselect for the first item in the
        // radio group. We can use the preceding axis to determine this 
		-->
		<xsl:if test="not(preceding-sibling::xh:input[@type='radio' and (@name = $radio_group_name)])">
			<xsl:element name="xfsiselect">
				<xsl:if test="count(following::xh:input[@type='radio' and @name=$radio_group_name and @checked]) = 1">
					<xsl:attribute name="initial" namespace=""><xsl:value-of select="following::xh:input[@type='radio' and @name=$radio_group_name and @checked]/@value"/></xsl:attribute>
				</xsl:if>
				<!-- // pane selector -->
				<xsl:choose>
					<xsl:when test="@class and contains(@class, ',')">
						<xsl:attribute name="entryPane" namespace=""><xsl:value-of select="normalize-space(substring-after(@class, ','))"/></xsl:attribute>
					</xsl:when>
					<xsl:otherwise>
						<xsl:attribute name="entryPane" namespace=""><xsl:value-of select="$defaultPortletContentPane"/></xsl:attribute>
					</xsl:otherwise>
				</xsl:choose>
				<!-- // class -->
				<xsl:variable name="temp" select="normalize-space(substring-before(concat(@class, ','), ','))"/>
				<xsl:choose>
					<xsl:when test="$temp">
						<xsl:attribute name="class" namespace=""><xsl:value-of select="$temp"/></xsl:attribute>
					</xsl:when>
					<xsl:otherwise>
						<xsl:attribute name="class" namespace=""><xsl:value-of select="$defaultRadioClass"/></xsl:attribute>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:attribute name="name" namespace=""><xsl:value-of select="$radio_group_name"/></xsl:attribute>
				<!-- 
				// we create the first option in the group 
				// BEA workshop creates labels for radio buttons as 'span' elements next to radio elements
				-->
				<xsl:element name="xfoption">
					<xsl:attribute name="value" namespace=""><xsl:value-of select="@value"/></xsl:attribute>
					<xsl:attribute name="caption" namespace=""><xsl:value-of select="following::node()[1][self::xh:span]"/></xsl:attribute>
				</xsl:element>
				<!-- // We now process any other sibling radio group items -->
				<xsl:apply-templates select="following::xh:input[@type='radio' and (@name = $radio_group_name)]" mode="within_a_radio_group"/>
			</xsl:element>
		</xsl:if>
	</xsl:template>
	<!--
	// radio  
	// this template is used for all but the 1st radio group item  
	-->
	<xsl:template match="xh:input[@type='radio']" mode="within_a_radio_group">
		<xsl:element name="xfoption">
			<xsl:attribute name="value" namespace=""><xsl:value-of select="@value"/></xsl:attribute>
			<xsl:attribute name="caption" namespace=""><xsl:value-of select="following::node()[1][self::xh:span]"/></xsl:attribute>
		</xsl:element>
	</xsl:template>
	<!--
	// checkbox
	-->
	<xsl:template match="xh:input[@type='checkbox']">
		<xsl:variable name="input_name">
			<xsl:choose>
				<xsl:when test="@name">
					<xsl:value-of select="@name"/>
				</xsl:when>
				<xsl:when test="@id">
					<xsl:value-of select="@id"/>
				</xsl:when>
			</xsl:choose>
		</xsl:variable>
		<xsl:element name="xfmuselect">
			<!-- // pane selector -->
			<xsl:choose>
				<xsl:when test="@class and contains(@class, ',')">
					<xsl:attribute name="entryPane" namespace=""><xsl:value-of select="normalize-space(substring-after(@class, ','))"/></xsl:attribute>
				</xsl:when>
				<xsl:otherwise>
					<xsl:attribute name="entryPane" namespace=""><xsl:value-of select="$defaultPortletContentPane"/></xsl:attribute>
				</xsl:otherwise>
			</xsl:choose>
			<!-- // class -->
			<xsl:variable name="temp" select="normalize-space(substring-before(concat(@class, ','), ','))"/>
			<xsl:choose>
				<xsl:when test="$temp">
					<xsl:attribute name="class" namespace=""><xsl:value-of select="$temp"/></xsl:attribute>
				</xsl:when>
				<xsl:otherwise>
					<xsl:attribute name="class" namespace=""><xsl:value-of select="$defaultCheckboxClass"/></xsl:attribute>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:attribute name="name" namespace=""><xsl:value-of select="$input_name"/></xsl:attribute>
			<xsl:element name="xfoption">
				<!--
				// label from span element next to checkbox
				-->
				<xsl:attribute name="caption" namespace=""><xsl:value-of select="following::node()[1][self::xh:span]"/></xsl:attribute>
				<xsl:if test="@checked">
					<xsl:attribute name="selected" namespace="">true</xsl:attribute>
				</xsl:if>
				<xsl:attribute name="value" namespace=""><xsl:value-of select="@value"/></xsl:attribute>
			</xsl:element>
		</xsl:element>
	</xsl:template>
	<!--
	// ignore tag, process content
	-->
	<xsl:template match="xh:fieldset|xh:legend">
		<xsl:apply-templates/>
	</xsl:template>
	<!-- 
	// remove any label elements and content as we process them elsewhere
	-->
	<xsl:template match="xh:label">
	</xsl:template>
		<!-- 
		I dont think we support submission from an image in xfforms.
	-->
	<!--
	<xsl:template match="xh:input[@type='image']">
		<xsl:variable name="entryPaneName">
			<xsl:call-template name="getEntryPaneName">
				<xsl:with-param name="controlName" select="current()/@type"/>
			</xsl:call-template>
		</xsl:variable>
		

		<xsl:element name="xfaction">
			<xsl:attribute name="entryPane" namespace=""><xsl:value-of select="$entryPaneName"/></xsl:attribute>
			<xsl:variable name="tempClass">
				<xsl:value-of select="normalize-space(substring-before(concat(@class, ','), ','))"/>
			</xsl:variable>
			<xsl:choose>
				<xsl:when test="$tempClass">
					<xsl:attribute name="class" namespace=""><xsl:value-of select="$tempClass"/></xsl:attribute>
				</xsl:when>
				<xsl:otherwise>
					<xsl:attribute name="class" namespace=""><xsl:value-of select="$defaultSubmitClass"/></xsl:attribute>
				</xsl:otherwise>
			</xsl:choose>
			<xsl:attribute name="type" namespace="">submit</xsl:attribute>
			<xsl:choose>
				<xsl:when test="@alt">
					<xsl:attribute name="caption" namespace=""><xsl:value-of select="@alt"/></xsl:attribute>
					<xsl:if test="@value">
						<xsl:attribute name="value" namespace=""><xsl:value-of select="@value"/></xsl:attribute>
					</xsl:if>
				</xsl:when>
				<xsl:otherwise>
					<xsl:if test="@value">
						<xsl:attribute name="caption" namespace=""><xsl:value-of select="@value"/></xsl:attribute>
						<xsl:attribute name="value" namespace=""><xsl:value-of select="@value"/></xsl:attribute>
					</xsl:if>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:element>
	</xsl:template>
	-->

</xsl:stylesheet>
