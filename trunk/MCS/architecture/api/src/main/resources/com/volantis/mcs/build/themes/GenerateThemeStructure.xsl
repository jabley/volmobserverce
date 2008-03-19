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

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
    xmlns:thm="http://vine.uk.volantis.com/architecture/document/xmlns/ThemeInfo" 
    xmlns:struct="http://www.volantis.com/xmlns/2005/10/ThemeStruct" exclude-result-prefixes="thm">
   
    <xsl:output indent="yes" method="xml" />

    <!--
        
        This XSL translates the themePropertyDefinitions.xml into a structure
        suitable for processing with JIBX to enable the structure of theme 
        properties to be represented in an object model.
 
    -->
 
    <xsl:template match="/">
        <xsl:apply-templates/>
    </xsl:template>

 
    <xsl:template match="thm:themeDefinition" >
        <struct:themeStructureDefinition>
            <xsl:apply-templates/>
         </struct:themeStructureDefinition>
    </xsl:template>
    
    <xsl:template match="thm:property">
        <struct:property name="{thm:name}">            
            <struct:choiceType>
                <struct:inheritType/>
                <xsl:apply-templates>
                    <xsl:with-param name="inChoice" select="true()"/>
                </xsl:apply-templates>
            </struct:choiceType>
        </struct:property>
    </xsl:template>

    <xsl:template match="thm:type">
        <xsl:param name="inChoice"/>
        <xsl:apply-templates>
            <xsl:with-param name="inChoice" select="$inChoice"/>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="thm:typeRef">
        <xsl:param name="inChoice"/>
        <xsl:variable name="ref"><xsl:value-of select="text()"/></xsl:variable>
        <xsl:apply-templates select="/thm:themeDefinition/thm:typeDefinition[thm:name=$ref]/thm:type">
            <xsl:with-param name="inChoice" select="$inChoice"/>
        </xsl:apply-templates>
    </xsl:template>
    
    <xsl:template match="thm:choiceType">
        <xsl:param name="inChoice"/>
        <xsl:choose>
            <xsl:when test="$inChoice">
                <xsl:apply-templates/>
            </xsl:when>
            <xsl:otherwise>
                <struct:choiceType>
                    <xsl:apply-templates/>
                </struct:choiceType>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="thm:keywords">
        <struct:keywordsType>
            <xsl:apply-templates/>
        </struct:keywordsType>
    </xsl:template>
    
    <xsl:template match="thm:keyword">
        <struct:keyword><xsl:value-of select="thm:name"/></struct:keyword>
    </xsl:template>
        
    <xsl:template match="thm:angleType | thm:colorType |
                                      thm:first | thm:second |
                                      thm:functionCallType | thm:frequencyType |
                                      thm:identifierType | thm:integerType |
                                      thm:lengthType |
                                      thm:listType |thm:numberType | thm:orderedSetType |
                                      thm:pairType | thm:percentageType |
                                      thm:stringType | thm:timeType | thm:uriType |
                                      thm:fractionType| thm:numerator | thm:denominator">
        <xsl:element name="struct:{local-name()}">
            <xsl:apply-templates/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="thm:mcsComponentURIType">
        <struct:componentURIType/>
    </xsl:template>

    <xsl:template match="thm:mcsTranscodableURIType">
        <struct:transcodableURIType/>
    </xsl:template>

    <xsl:template match="*|text()"/>
</xsl:stylesheet>

<!--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 01-Nov-05	9992/2	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 31-Oct-05	9992/1	emma	VBM:2005101811 Adding new style property validation

 28-Oct-05	9965/5	ianw	VBM:2005101811 fix bindings

 27-Oct-05	9965/3	ianw	VBM:2005101811 interim commit

 26-Oct-05	9965/1	ianw	VBM:2005101811 Interim commit

 ===========================================================================
-->
