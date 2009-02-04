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

<!-- ==========================================================================
 ! $Header: 
 ! ============================================================================
 ! (c) Volantis Systems Ltd 2003. 
 ! ============================================================================
 ! Change History:
 !
 ! Date         Who             Description
 ! =========    =============== ===============================================
 ! ======================================================================== -->
<!--
 !
 ! ***************************************************************************************************
 ! This file is owned by Architecture and can only be changed by a member of
 ! that group.
 !
 ! This XSLT generates the external description of remote policies from the individual modules.
 ! ***************************************************************************************************
 !
 !-->
<xsl:stylesheet xmlns:common="http://exslt.org/common" xmlns:thm="http://vine.uk.volantis.com/architecture/document/xmlns/ThemeInfo" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" version="1.0" exclude-result-prefixes="common thm">

    <!-- Output as xml using xalan specific extensions. -->
    <xsl:output xmlns:xalan="http://xml.apache.org/xslt"
                xalan:indent-amount="4"
                encoding="us-ascii"
                indent="yes"
                method="xml"/>

    <xsl:include href="GeneratePublicSchemaCore.xsl"/>

    <xsl:include href="PropertyTypes.xsl"/>

    <xsl:variable name="themeProperties" select="document('../themes/themePropertyDefinitions.xml')/thm:themeDefinition"/>

    <!--
     ! Create a schema element here so that it picks up the namespace declarations.
     !
     ! The problem that this is trying to solve is that a schema module includes
     ! schemata in different namespaces. If this is not done then every element
     ! from within that module has namespace declarations replicated on them.
     ! 
     ! Have to add a default namespace, otherwise the resulting schema is not valid.
     !-->
    <xsl:template match="xs:schema">
        <xs:schema xmlns="http://www.volantis.com/xmlns/2005/09/marlin-lpdm"
                   xmlns:lpdm="http://www.volantis.com/xmlns/2005/09/marlin-lpdm"
                   xmlns:mdv="http://www.volantis.com/xmlns/2004/12/meta-data-values"
                   xmlns:mdt="http://www.volantis.com/xmlns/2004/12/meta-data-types">

            <xsl:apply-templates select="@*"/>
            <xsl:apply-templates/>
        </xs:schema>
    </xsl:template>

    <xsl:template name="GetPublicComment">
        <xsl:comment> ==========================================================================
 ! (c) Volantis Systems Ltd 2001. 
 ! ============================================================================
 ! This document 
 ! ======================================================================== </xsl:comment>
    </xsl:template>

    <!-- Include the extended layout module. -->
    <xsl:template match="xs:include[@schemaLocation = 'modules/layout-description-module-internal.xsd']">
        <xsl:comment>
     ! **********************************************************************
     !     Layout Descriptions 
     ! **********************************************************************
     !</xsl:comment>
 
        <xsl:call-template name="IncludeSchema"/> 
    </xsl:template>

    <!-- Purposely ignore the css-device-theme-internal.xsd file. -->
    <xsl:template match="xs:include[@schemaLocation = 'css-device-theme-internal.xsd']">

        <xsl:comment>
     ! **********************************************************************
     !     CSS Device Theme Descriptions 
     ! **********************************************************************
     !</xsl:comment>
 
        <xsl:call-template name="IncludeSchema"/> 
    </xsl:template>

    <!-- Include the xml-device-theme-internal.xsd module. -->
    <xsl:template match="xs:include[@schemaLocation = 'xml-device-theme-internal.xsd']">

        <xsl:comment>
     ! **********************************************************************
     !     XML Device Theme Descriptions 
     ! **********************************************************************
     !</xsl:comment>
 
        <xsl:call-template name="IncludeSchema"/> 
    </xsl:template>

    <!-- Purposely ignore the common-layout-theme-types-internal.xsd module when it is included from within another module. -->
    <xsl:template match="xs:include[@schemaLocation = 'common-layout-theme-types-internal.xsd']"/>

    <!-- Include the common-layout-theme-types-internal.xsd module when it is included from marlin-lpdm-internal.xsd. -->
    <xsl:template match="xs:include[@schemaLocation = 'modules/common-layout-theme-types-internal.xsd']">

        <xsl:comment>
     ! **********************************************************************
     !     Common Layout / Theme Descriptions 
     ! **********************************************************************
     !</xsl:comment>
 
        <xsl:call-template name="IncludeSchema"/> 
    </xsl:template>

    <xsl:template match="xs:import[substring-before(@schemaLocation, 'meta-data-values.xsd') or substring-before(@schemaLocation, 'meta-data-types.xsd')]">
        <xs:import>
            <xsl:attribute name="schemaLocation">
                <xsl:text>../../2004/12/</xsl:text>
                <xsl:value-of select="substring-after(@schemaLocation, '../../../meta/')"/>
            </xsl:attribute>
            <xsl:apply-templates select="@namespace"/>
        </xs:import>
    </xsl:template>

    <!-- Generate the list of property element references. -->
    <xsl:template match="xs:element[@ref = 'standard-css-properties']">
        <xsl:for-each select="$themeProperties/thm:property">
            <xsl:sort select="thm:name"/>
            <xs:element>
                <xsl:attribute name="ref"><xsl:value-of select="thm:name"/></xsl:attribute>
                <xsl:attribute name="minOccurs">0</xsl:attribute>
            </xs:element>
        </xsl:for-each>
    </xsl:template>

    <!--
     ! Ignore the reference to the mcs extension properties. It is just there
     ! for when the schema is modified using other tools.
     !-->
    <xsl:template match="xs:element[@ref = 'mcs-extension-properties']"/>

    <!-- Generate the definitions of the standard css properties. -->
    <xsl:template match="xs:element[@name = 'standard-css-properties']">
        <xsl:apply-templates select="$themeProperties/thm:property[not(starts-with(thm:name, 'mcs-'))]">
            <xsl:sort select="thm:name"/>
        </xsl:apply-templates>
    </xsl:template>

    <!-- Generate the definitions of the mcs extenstion properties. -->
    <xsl:template match="xs:element[@name = 'mcs-extension-properties']">
        <xsl:apply-templates select="$themeProperties/thm:property[starts-with(thm:name, 'mcs-')]">
            <xsl:sort select="thm:name"/>
        </xsl:apply-templates>
    </xsl:template>

    <!-- Generate the definition of the element relating to a specific theme property. -->
    <xsl:template match="thm:property">
        <xs:element name="{thm:name}">
            <xsl:choose>
                <xsl:when test=".//thm:typeRef">
                    <xsl:attribute name="type">
                        <xsl:variable name="type" select=".//thm:typeRef"/>
                        <xsl:value-of select="$PropertyTypes/type[@name = $type]"/>
                    </xsl:attribute>
                </xsl:when>
                <xsl:otherwise>
                    <xs:complexType>
                        <xsl:apply-templates mode="thm:content-model" select="thm:type"/>
                        <xs:attributeGroup ref="StyleValueAttributes"/>
                    </xs:complexType>
                </xsl:otherwise>
            </xsl:choose>
        </xs:element>
    </xsl:template>

    <!--
     ! Mode: thm:content-model
     !
     !  Templates active within this mode generate schema content model
     !  definitions from theme property type information.
     !-->

    <!-- By default ignore text and elements in this mode. -->
    <xsl:template mode="thm:content-model" match="*|text()"/>

    <!--
     ! Match: thm:type
     !
     !  This does not generate any output itself, it simply applies the
     !  templates to its children.
     !-->

    <xsl:template mode="thm:content-model" match="thm:type">
        <xs:choice>
            <xs:element ref="inherit"/>
            <xs:element ref="invalidStyle"/>
            <xsl:apply-templates mode="thm:content-model"/>
        </xs:choice>
    </xsl:template>

    <!--
     ! Match: thm:choiceType
     !
     !  This does not generate any output itself, it simply applies the
     !  templates to its children.
     !-->

    <xsl:template mode="thm:content-model" match="thm:choiceType">
        <xsl:apply-templates mode="thm:content-model"/>
    </xsl:template>

    <!--
     ! Match: thm:angleType
     !
     !  Angle values are represented using a nested angle element.
     !-->

    <xsl:template mode="thm:content-model" match="thm:angleType">
        <xs:element ref="angle"/>
    </xsl:template>

    <!--
     ! Match: thm:colorType
     !
     !  Color values are represented using a nested colorXXX element.
     !-->

    <xsl:template mode="thm:content-model" match="thm:colorType">
        <xs:element ref="colorPercentages"/>
        <xs:element ref="colorName"/>
        <xs:element ref="colorRGB"/>
    </xsl:template>

    <!--
     ! Match: thm:functionCallType
     !
     !  Function values are represented using a nested function element.
     !-->

    <xsl:template mode="thm:content-model" match="thm:functionCallType">
        <xs:element ref="functionCall"/>
    </xsl:template>

    <!--
     ! Match: thm:identifierType
     !
     !  Identifier values are represented using a nested identifier element.
     !-->

    <xsl:template mode="thm:content-model" match="thm:identifierType">
        <xs:element ref="identifier"/>
    </xsl:template>

    <!--
     ! Match: thm:integerType
     !
     !  Integer values are represented using a nested integer element.
     !-->

    <xsl:template mode="thm:content-model" match="thm:integerType">
        <xs:element ref="integer"/>
    </xsl:template>

    <!--
     ! Match: thm:keywords
     !
     !  Keywords are represented as enumerations within the keyword
     !  attribute. They do not affect the content model.
     !-->

    <xsl:template mode="thm:content-model" match="thm:keywords">
        <xs:element name="keyword">
            <xs:simpleType>
                <xs:restriction base="xs:string">
                    <xsl:for-each select="thm:keyword">
                        <xs:enumeration value="{thm:name}"/>
                    </xsl:for-each>
                </xs:restriction>
            </xs:simpleType>
        </xs:element>
    </xsl:template>

    <!--
     ! Match: thm:lengthType
     !
     !  Length values are represented using a nested length element.
     !-->

    <xsl:template mode="thm:content-model" match="thm:lengthType">
        <xs:element ref="length"/>
    </xsl:template>

    <!--
     ! Match: thm:listType
     !
     !  Items in a list are represented as a nested item element. It has
     !  no effect on the attributes.
     !-->

    <xsl:template mode="thm:content-model" match="thm:listType">
        <xs:element name="list">
            <xs:complexType>
                <xs:choice>
                    <xsl:attribute name="minOccurs">0</xsl:attribute>
                    <xsl:attribute name="maxOccurs">unbounded</xsl:attribute>
        
                    <xsl:apply-templates mode="thm:content-model" select="*[not(self::thm:name)]"/>
               </xs:choice>
                <xs:attribute name="unique" type="xs:boolean" fixed="false"/>
           </xs:complexType>
       </xs:element>
    </xsl:template>

    <!--
     ! Match: thm:mcsComponentURIType
     !
     !  MCS component URI values are represented using a nested 
     !  mcsComponentURI element.
     !-->

    <xsl:template mode="thm:content-model" match="thm:mcsComponentURIType">
        <xs:element ref="mcsComponentURI"/>
    </xsl:template>

    <!--
     ! Match: thm:numberType
     !
     !  Number values are represented using a nested number element.
     !-->

    <xsl:template mode="thm:content-model" match="thm:numberType">
        <xs:element ref="number"/>
    </xsl:template>

    <!--
     ! Match: thm:orderedSetType
     !
     !  Items in an ordered set are represented as a nested item element. It has
     !  no effect on the attributes.
     !-->

    <xsl:template mode="thm:content-model" match="thm:orderedSetType">
        <xs:element name="list">
            <xs:complexType>
                <xs:choice>
                    <xsl:attribute name="minOccurs">0</xsl:attribute>
                    <xsl:attribute name="maxOccurs">unbounded</xsl:attribute>
                
                    <xsl:apply-templates mode="thm:content-model" select="*[not(self::thm:name)]"/>
                </xs:choice>
                <xs:attribute name="unique" type="xs:boolean" fixed="true"/>
           </xs:complexType>
       </xs:element>
    </xsl:template>

    <!--
     ! Match: thm:pairType
     !
     !  Pair types are represented using two nested elements, first and second.
     !  It has no effect on the attributes.
     !-->

    <xsl:template mode="thm:content-model" match="thm:pairType">
        <xs:element name="pair">
            <xs:complexType>
                <xs:sequence>
                    <xsl:apply-templates mode="thm:content-model" select="thm:first"/>
                    <xsl:apply-templates mode="thm:content-model" select="thm:second"/>
                </xs:sequence>
            </xs:complexType>
        </xs:element>
    </xsl:template>

    <xsl:template mode="thm:content-model" match="thm:pairType/thm:first|thm:pairType/thm:second">
        <xs:element name="{local-name()}">
            <xsl:if test="self::thm:second">
                <xsl:attribute name="minOccurs">0</xsl:attribute>
            </xsl:if>
            <xs:complexType>
                <xs:choice>
                    <xsl:apply-templates mode="thm:content-model" select="*[not(self::thm:name)]"/>
                </xs:choice>
            </xs:complexType>
        </xs:element>
    </xsl:template>

    <!--
     ! Match: thm:percentageType
     !
     !  Percentage values are represented using a nested percentage element.
     !-->

    <xsl:template mode="thm:content-model" match="thm:percentageType">
        <xs:element ref="percentage"/>
    </xsl:template>

    <!--
     ! Match: thm:stringType
     !
     !  String values are represented using a nested string element.
     !-->

    <xsl:template mode="thm:content-model" match="thm:stringType">
        <xs:element ref="string"/>
    </xsl:template>

    <!--
     ! Match: thm:timeType
     !
     !  Time values are represented using a nested time element.
     !-->

    <xsl:template mode="thm:content-model" match="thm:timeType">
        <xs:element ref="time"/>
    </xsl:template>

    <!--
     ! Match: thm:uriType
     !
     !  URI values are represented using a nested uri element.
     !-->

    <xsl:template mode="thm:content-model" match="thm:uriType">
        <xs:element ref="uri"/>
    </xsl:template>

</xsl:stylesheet>
