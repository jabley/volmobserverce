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

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	<!--
        Copy Standard PAPI attributes
    -->
	<xsl:template name="copyStandardAttrs">
		<xsl:copy-of select="@id"/>
		<xsl:copy-of select="@class"/>
		<xsl:copy-of select="@title"/>
	</xsl:template>
	<xsl:template match="*">
		<xsl:apply-templates/>
	</xsl:template>
	<!--Big tag-->
	<xsl:template match="big">
		<xsl:copy>
			<xsl:call-template name="copyStandardAttrs"/>
			<xsl:apply-templates/>
		</xsl:copy>
	</xsl:template>
	<!--Br tag-->
	<xsl:template match="br">
		<xsl:copy>
			<xsl:call-template name="copyStandardAttrs"/>
			<xsl:apply-templates/>
		</xsl:copy>
	</xsl:template>
	<!--Card tag -->
	<xsl:template match="card">
		<canvas layoutName="/BasicTranscoderForm.mlyt">
			<xsl:call-template name="copyStandardAttrs"/>
			<xsl:choose>
				<xsl:when test="//postfield">
					<xfform name="Form">
						<xsl:attribute name="action"><xsl:value-of select="//postfield/../@href"/></xsl:attribute>
						<xsl:if test="//postfield/../@method">
							<xsl:attribute name="method"><xsl:value-of select="//postfield/../@method"/></xsl:attribute>
						</xsl:if>
						<xsl:apply-templates mode="form"/>
						<xsl:for-each select="//postfield/..">
						<xfaction type="submit" entryPane="Pane">
							<xsl:attribute name="caption">
								<xsl:value-of select="../@label"/>
							</xsl:attribute>
						</xfaction>
						</xsl:for-each>
					</xfform>
				</xsl:when>
				<xsl:otherwise>
					<pane name="Pane">
						<xsl:apply-templates/>
					</pane>
				</xsl:otherwise>
			</xsl:choose>
		</canvas>
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
			<xsl:attribute name="url"><xsl:value-of select="@src"/></xsl:attribute>
			<xsl:copy-of select="@alt"/>
		</xsl:copy>
	</xsl:template>
	<!--P tag-->
	<xsl:template match="p">
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
	<!--
	
		Form processing Templates
		
	-->
	<xsl:template match="br|go[@method!='post']" mode="form">
		<xfcontent pane="Pane">
			<xsl:apply-templates select="."/>
		</xfcontent>
	</xsl:template>
	<xsl:template match="input" mode="form">
		<xftextinput entryPane="Pane">
			<xsl:call-template name="copyStandardAttrs"/>
			<xsl:if test="@title">
				<xsl:attribute name="caption"><xsl:value-of select="@title"/></xsl:attribute>
			</xsl:if>
			<xsl:copy-of select="@type"/>
			<xsl:copy-of select="@name"/>
			<xsl:if test="@maxlength">
				<xsl:attribute name="maxLength"><xsl:value-of select="@maxlength"/></xsl:attribute>
			</xsl:if>
		</xftextinput>
	</xsl:template>
	<xsl:template match="strong" mode="form"/>
	<xsl:template match="go[@method!='post']">
		<a>
			<xsl:copy-of select="@href"/>
			<xsl:value-of select="parent::do/@label"/>
		</a>
		<br/>
	</xsl:template>
</xsl:stylesheet>
<!--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Feb-04	3150/1	ianw	VBM:2004021907 Changed file extension in transcoders

 05-Feb-04	2849/3	ianw	VBM:2003112606 Migrated xslts to new layout standards

 04-Feb-04	2849/1	ianw	VBM:2003112606 Updated Transcoders with correct basic form handling versions

 15-Jan-04	2616/1	mat	VBM:2004011509 MCS

 16-Dec-03	2229/1	mat	VBM:2003121521 Added XSLTs and overrode methods in MCSPortletFilter

 ===========================================================================
-->
