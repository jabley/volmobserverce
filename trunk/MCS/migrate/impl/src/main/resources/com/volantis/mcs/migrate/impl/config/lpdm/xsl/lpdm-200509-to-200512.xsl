<?xml version="1.0" encoding="ISO-8859-1"?>
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

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:old="http://www.volantis.com/xmlns/2005/09/marlin-lpdm"
    xmlns="http://www.volantis.com/xmlns/2005/12/marlin-lpdm"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    exclude-result-prefixes="old">

    <!--
     ! Strip whitespace from all input elements apart from the explicitly
     ! specified elements.
     !-->
    <xsl:strip-space elements="*"/>

    <!--
     ! All content of the string element on input is significant, even white
     ! space.
     !-->
    <xsl:preserve-space elements="old:string"/>

    <xsl:output method="xml" indent="yes"
        xmlns:xalan="http://xml.apache.org/xslt"
        xalan:indent-amount="4"/>

    <xsl:template match="/">
        <xsl:apply-templates/>
    </xsl:template>

    <!--
     ! =========================================================================
     !     Internal theme restructuring.
     ! =========================================================================
     !-->

    <xsl:template match="old:subjectSelectorSequence|old:contextualSelectorSequence">
        <selectorSequence>
            <xsl:apply-templates/>
        </selectorSequence>
    </xsl:template>

    <xsl:template match="old:typeSelector">
        <type>
            <xsl:value-of select="@type"/>
        </type>
    </xsl:template>

    <xsl:template match="old:classSelector">
        <class>
            <xsl:value-of select="@class"/>
        </class>
    </xsl:template>

    <xsl:template match="old:idSelector">
        <id>
            <xsl:value-of select="@id"/>
        </id>
    </xsl:template>

    <xsl:template match="old:universalSelector">
        <universal>
            <xsl:if test="@namespace">
                <namespace>
                    <xsl:value-of select="@namespace"/>
                </namespace>
            </xsl:if>
        </universal>
    </xsl:template>

    <xsl:template match="old:combinedSelector">
        <combinedSelector>
            <context>
                <xsl:apply-templates select="old:contextualSelectorSequence"/>
            </context>
            <combinator>
                <xsl:value-of select="@combinator"/>
            </combinator>
            <subject>
                <xsl:apply-templates select="old:subjectSelectorSequence|old:combinedSelector"/>
            </subject>
        </combinedSelector>
    </xsl:template>

    <xsl:template match="old:attributeSelectors|old:structuralPseudoClassSelectors|old:statefulPseudoClassSelectors|old:pseudoElementSelectors">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="old:set">
        <attribute>
            <xsl:if test="@namespace">
                <namespace><xsl:value-of select="@namespace"/></namespace>
            </xsl:if>
            <localName><xsl:value-of select="@attribute"/></localName>
            <exists/>
        </attribute>
    </xsl:template>

    <xsl:template match="old:equals|old:contains-word|old:starts-with|old:ends-with|old:contains|old:language-match">
        <attribute>
            <xsl:if test="@namespace">
                <namespace><xsl:value-of select="@namespace"/></namespace>
            </xsl:if>
            <localName><xsl:value-of select="@attribute"/></localName>
            <xsl:element name="{local-name()}">
                <xsl:value-of select="@value"/>
            </xsl:element>
        </attribute>
    </xsl:template>

    <xsl:template match="old:styleProperties">
        <properties>
            <xsl:apply-templates/>
        </properties>
    </xsl:template>

    <xsl:template match="old:styleProperties/old:*">
        <property name="{local-name()}">
            <xsl:if test="@priority = 'important'">
                <xsl:attribute name="priority">important</xsl:attribute>
            </xsl:if>
            <xsl:apply-templates/>
        </property>
    </xsl:template>

    <!--
      - Copy anything unrecognised with low priority, updating the namespace
      -->
    <xsl:template match="old:*" priority="-3">
        <xsl:element name="{local-name()}">
            <xsl:apply-templates select="@*|node()"/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="node()|@*" priority="-5">
        <xsl:copy>
            <xsl:apply-templates select="node()|@*"/>
        </xsl:copy>
    </xsl:template>

    <!--
      - Upgrade schema version where appropriate
      -->
    <xsl:template match="@xsi:schemaLocation[.='http://www.volantis.com/xmlns/2005/09/marlin-lpdm http://www.volantis.com/schema/2005/09/marlin-lpdm.xsd']">
        <xsl:attribute name="xsi:schemaLocation">http://www.volantis.com/xmlns/2005/12/marlin-lpdm http://www.volantis.com/schema/2005/12/marlin-lpdm.xsd</xsl:attribute>
    </xsl:template>

</xsl:stylesheet>
