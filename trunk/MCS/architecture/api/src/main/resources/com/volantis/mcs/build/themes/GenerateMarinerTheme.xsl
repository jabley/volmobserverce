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

<xsl:stylesheet xmlns:exslt-common="http://exslt.org/common" xmlns:thm="http://vine.uk.volantis.com/architecture/document/xmlns/ThemeInfo" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0" exclude-result-prefixes="exslt-common thm">

    <xsl:variable name="annotations" select="document('ThemePropertyDefinitionAnnotations.xml')/annotations"/>

    <xsl:variable name="RT-PropertyTypes">
        <type name="mcs-border-color">BorderColorType</type>
        <type name="border-style">BorderStyleType</type>
        <type name="border-width">BorderWidthType</type>
        <type name="margin-width">MarginWidthType</type>
        <type name="padding-width">PaddingWidthType</type>
        <type name="mcs-position">PositionType</type>
    </xsl:variable>
    <xsl:variable name="PropertyTypes" select="exslt-common:node-set($RT-PropertyTypes)"/>

    <xsl:key name="type-definitions" match="thm:typeDefinition" use="thm:name"/>

    <!-- Output as xml using xalan specific extensions. -->
    <xsl:output xmlns:xalan="http://xml.apache.org/xslt"
                xalan:indent-amount="4"
                indent="yes"
                method="xml"/>

    <xsl:template match="/">
        <xs:schema xmlns:syntax="http://www.volantis.com/Mariner/Syntax" xmlns:common="http://www.volantis.com/Mariner/Common" xmlns:jdbc="http://www.volantis.com/Mariner/JDBCAnnotations" elementFormDefault="qualified">
            <xsl:attribute name="attributeFormDefault">unqualified</xsl:attribute>
            <!--
            <xsl:comment> ==================================================================== </xsl:comment>
            <xsl:comment>         Global annotations                                           </xsl:comment>
            <xsl:comment> ==================================================================== </xsl:comment>
            <xs:annotation>
                <xs:appinfo>
                    <jdbc:table name="TEXT_PROPERTIES">
                        <jdbc:column name="THEME">
                            <xsl:attribute name="primaryKey">true</xsl:attribute>
                            <jdbc:string length="255"/>
                        </jdbc:column>
                        <jdbc:column name="DEVICE">
                            <xsl:attribute name="primaryKey">true</xsl:attribute>
                            <jdbc:string length="255"/>
                        </jdbc:column>
                        <jdbc:column name="RULE_REF">
                            <xsl:attribute name="primaryKey">true</xsl:attribute>
                            <jdbc:int/>
                        </jdbc:column>
                    </jdbc:table>
                    <common:enumeration name="CommonSyntaxValues">
                        <common:value name="not-set">
                            <xsl:attribute name="value">-2</xsl:attribute>
                        </common:value>
                        <common:value name="inherit">
                            <xsl:attribute name="value">-1</xsl:attribute>
                        </common:value>
                    </common:enumeration>
                </xs:appinfo>
            </xs:annotation>
            -->
            <xsl:comment> ==================================================================== </xsl:comment>
            <xsl:comment>         Property Types                                               </xsl:comment>
            <xsl:comment> ==================================================================== </xsl:comment>
<!--            <xs:complexType name="AngleType">-->
<!--                <xs:sequence>-->
<!--                    <xs:element name="value">-->
<!--                        <xsl:attribute name="type">xs:double</xsl:attribute>-->
<!--                    </xs:element>-->
<!--                    <xs:choice>-->
<!--                        <xs:element name="deg"/>-->
<!--                        <xs:element name="grad"/>-->
<!--                        <xs:element name="rad"/>-->
<!--                    </xs:choice>-->
<!--                </xs:sequence>-->
<!--            </xs:complexType>-->
            <xs:complexType name="BorderColorType">
                <xs:choice>
<!--                    <xs:element ref="ColorType"/>-->
<!--                    <xs:element ref="inherit"/>-->
                    <xsl:processing-instruction name="theme">defaultValueFromProperty="true" sourceProperty="color"</xsl:processing-instruction>
                </xs:choice>
            </xs:complexType>
            <xs:complexType name="BorderStyleType">
                <xs:choice>
                    <xs:element name="BorderStyle">
                        <xs:complexType>
                            <xs:choice>
                                <xs:element name="none">
                                    <xsl:processing-instruction name="theme">isDefaultValue="true"</xsl:processing-instruction>
                                    <xsl:processing-instruction name="enumeration">grp="BorderStyle"</xsl:processing-instruction>
                                </xs:element>
                                <xs:element name="hidden">
                                    <xsl:processing-instruction name="enumeration">grp="BorderStyle"</xsl:processing-instruction>
                                </xs:element>
                                <xs:element name="dotted">
                                    <xsl:processing-instruction name="enumeration">grp="BorderStyle"</xsl:processing-instruction>
                                </xs:element>
                                <xs:element name="dashed">
                                    <xsl:processing-instruction name="enumeration">grp="BorderStyle"</xsl:processing-instruction>
                                </xs:element>
                                <xs:element name="solid">
                                    <xsl:processing-instruction name="enumeration">grp="BorderStyle"</xsl:processing-instruction>
                                </xs:element>
                                <xs:element name="double">
                                    <xsl:processing-instruction name="enumeration">grp="BorderStyle"</xsl:processing-instruction>
                                </xs:element>
                                <xs:element name="groove">
                                    <xsl:processing-instruction name="enumeration">grp="BorderStyle"</xsl:processing-instruction>
                                </xs:element>
                                <xs:element name="ridge">
                                    <xsl:processing-instruction name="enumeration">grp="BorderStyle"</xsl:processing-instruction>
                                </xs:element>
                                <xs:element name="inset">
                                    <xsl:processing-instruction name="enumeration">grp="BorderStyle"</xsl:processing-instruction>
                                </xs:element>
                                <xs:element name="outset">
                                    <xsl:processing-instruction name="enumeration">grp="BorderStyle"</xsl:processing-instruction>
                                </xs:element>
                            </xs:choice>
                        </xs:complexType>
                    </xs:element>
                    <xs:element ref="inherit"/>
                </xs:choice>
            </xs:complexType>
            <xs:complexType name="BorderWidthType">
                <xs:choice>
                    <xs:element name="BorderWidth">
                        <xs:complexType>
                            <xs:choice>
                                <xs:element name="thin">
                                    <xsl:processing-instruction name="enumeration">grp="BorderWidth"</xsl:processing-instruction>
                                </xs:element>
                                <xs:element name="medium">
                                    <xsl:processing-instruction name="theme">isDefaultValue="true"</xsl:processing-instruction>
                                    <xsl:processing-instruction name="enumeration">grp="BorderWidth"</xsl:processing-instruction>
                                </xs:element>
                                <xs:element name="thick">
                                    <xsl:processing-instruction name="enumeration">grp="BorderWidth"</xsl:processing-instruction>
                                </xs:element>
                                <xs:element ref="length"/>
                            </xs:choice>
                        </xs:complexType>
                    </xs:element>
                    <xs:element ref="inherit"/>
                </xs:choice>
            </xs:complexType>
            <xs:complexType name="ColorType">
<!--                <xs:choice>-->
<!--                    <xs:element name="rgb-triplet">-->
<!--                        <xs:complexType>-->
<!--                            <xs:attribute name="red">-->
<!--                                <xsl:attribute name="type">xs:integer</xsl:attribute>-->
<!--                                <xsl:attribute name="use">required</xsl:attribute>-->
<!--                            </xs:attribute>-->
<!--                            <xs:attribute name="green">-->
<!--                                <xsl:attribute name="type">xs:integer</xsl:attribute>-->
<!--                                <xsl:attribute name="use">required</xsl:attribute>-->
<!--                            </xs:attribute>-->
<!--                            <xs:attribute name="blue">-->
<!--                                <xsl:attribute name="type">xs:integer</xsl:attribute>-->
<!--                                <xsl:attribute name="use">required</xsl:attribute>-->
<!--                            </xs:attribute>-->
<!--                        </xs:complexType>-->
<!--                    </xs:element>-->
<!--                    <xs:element name="rgb-singleton">-->
<!--                        <xs:complexType>-->
<!--                            <xs:attribute name="value">-->
<!--                                <xsl:attribute name="type">xs:integer</xsl:attribute>-->
<!--                                <xsl:attribute name="use">required</xsl:attribute>-->
<!--                            </xs:attribute>-->
<!--                        </xs:complexType>-->
<!--                    </xs:element>-->
<!--                    <xs:choice>-->
<!--                        <xs:element name="aqua"/>-->
<!--                        <xs:element name="black"/>-->
<!--                        <xs:element name="blue"/>-->
<!--                        <xs:element name="fuchsia"/>-->
<!--                        <xs:element name="gray"/>-->
<!--                        <xs:element name="green"/>-->
<!--                        <xs:element name="lime"/>-->
<!--                        <xs:element name="maroon"/>-->
<!--                        <xs:element name="navy"/>-->
<!--                        <xs:element name="olive"/>-->
<!--                        <xs:element name="purple"/>-->
<!--                        <xs:element name="red"/>-->
<!--                        <xs:element name="silver"/>-->
<!--                        <xs:element name="teal"/>-->
<!--                        <xs:element name="white"/>-->
<!--                        <xs:element name="yellow"/>-->
<!--                        <xs:element name="ActiveBorder"/>-->
<!--                        <xs:element name="ActiveCaption"/>-->
<!--                        <xs:element name="AppWorkspace"/>-->
<!--                        <xs:element name="Background"/>-->
<!--                        <xs:element name="ButtonFace"/>-->
<!--                        <xs:element name="ButtonHighlight"/>-->
<!--                        <xs:element name="ButtonShadow"/>-->
<!--                        <xs:element name="ButtonText"/>-->
<!--                        <xs:element name="CaptionText"/>-->
<!--                        <xs:element name="GrayText"/>-->
<!--                        <xs:element name="Highlight"/>-->
<!--                        <xs:element name="HighlightText"/>-->
<!--                        <xs:element name="InactiveBorder"/>-->
<!--                        <xs:element name="InactiveCaption"/>-->
<!--                        <xs:element name="InactiveCaptionText"/>-->
<!--                        <xs:element name="InfoBackground"/>-->
<!--                        <xs:element name="InfoText"/>-->
<!--                        <xs:element name="Menu"/>-->
<!--                        <xs:element name="MenuText"/>-->
<!--                        <xs:element name="Scrollbar"/>-->
<!--                        <xs:element name="ThreeDDarkShadow"/>-->
<!--                        <xs:element name="ThreeDFace"/>-->
<!--                        <xs:element name="ThreeDHighlight"/>-->
<!--                        <xs:element name="ThreeDLightShadow"/>-->
<!--                        <xs:element name="ThreeDShadow"/>-->
<!--                        <xs:element name="Window"/>-->
<!--                        <xs:element name="WindowFrame"/>-->
<!--                        <xs:element name="WindowText"/>-->
<!--                    </xs:choice>-->
<!--                </xs:choice>-->
            </xs:complexType>
            <xs:complexType name="LengthType">
<!--                <xs:sequence>-->
<!--                    <xs:element name="value">-->
<!--                        <xsl:attribute name="type">xs:double</xsl:attribute>-->
<!--                    </xs:element>-->
<!--                    <xs:choice>-->
<!--                        <xs:element name="mm"/>-->
<!--                        <xs:element name="cm"/>-->
<!--                        <xs:element name="pt"/>-->
<!--                        <xs:element name="pc"/>-->
<!--                        <xs:element name="em"/>-->
<!--                        <xs:element name="ex"/>-->
<!--                        <xs:element name="px"/>-->
<!--                        <xs:element name="in"/>-->
<!--                    </xs:choice>-->
<!--                </xs:sequence>-->
            </xs:complexType>
            <xs:complexType name="MarginWidthType">
                <xs:choice>
                    <xs:element name="MarginWidth">
                        <xs:complexType>
                            <xs:choice>
                                <xs:element ref="length">
                                    <xsl:processing-instruction name="theme">isDefaultValue="true" value="0" length-unit="mm"</xsl:processing-instruction>
                                </xs:element>
                                <xs:element ref="percentage"/>
                                <xs:element name="auto">
                                    <xsl:processing-instruction name="enumeration">grp="MarginWidth"</xsl:processing-instruction>
                                </xs:element>
                            </xs:choice>
                        </xs:complexType>
                    </xs:element>
                    <xs:element ref="inherit"/>
                </xs:choice>
            </xs:complexType>
            <xs:complexType name="MarinerComponentURI">
<!--                <xs:annotation>-->
<!--                    <xs:documentation>Function syntax is mariner-component-url()</xs:documentation>-->
<!--                </xs:annotation>-->
<!--                <xs:attribute name="value">-->
<!--                    <xsl:attribute name="type">xs:string</xsl:attribute>-->
<!--                    <xsl:attribute name="use">required</xsl:attribute>-->
<!--                </xs:attribute>-->
            </xs:complexType>
            <xs:complexType name="PaddingWidthType">
                <xs:choice>
                    <xs:element name="PaddingWidth">
                        <xs:complexType>
                            <xs:choice>
                                <xs:element ref="length">
                                    <xsl:processing-instruction name="theme">isDefaultValue="true" value="0" length-unit="mm"</xsl:processing-instruction>
                                </xs:element>
                                <xs:element ref="percentage"/>
                            </xs:choice>
                        </xs:complexType>
                    </xs:element>
                    <xs:element ref="inherit"/>
                </xs:choice>
            </xs:complexType>
            <xs:complexType name="PercentageType">
<!--                <xs:sequence>-->
<!--                    <xs:element name="value">-->
<!--                        <xsl:attribute name="type">xs:double</xsl:attribute>-->
<!--                    </xs:element>-->
<!--                </xs:sequence>-->
            </xs:complexType>
            <xs:complexType name="PositionType">
                <xs:choice>
                    <xs:element name="Position">
                        <xs:complexType>
                            <xs:choice>
                                <xs:element ref="length"/>
                                <xs:element ref="percentage"/>
                                <xs:element name="auto">
                                    <xsl:processing-instruction name="enumeration">grp="PositionEdge"</xsl:processing-instruction>
                                </xs:element>
                            </xs:choice>
                        </xs:complexType>
                    </xs:element>
                    <xs:element ref="inherit"/>
                </xs:choice>
            </xs:complexType>
            <xs:complexType name="TimeType">
<!--                <xs:sequence>-->
<!--                    <xs:element name="value">-->
<!--                        <xsl:attribute name="type">xs:double</xsl:attribute>-->
<!--                    </xs:element>-->
<!--                    <xs:choice>-->
<!--                        <xs:element name="ms"/>-->
<!--                        <xs:element name="s"/>-->
<!--                    </xs:choice>-->
<!--                </xs:sequence>-->
            </xs:complexType>
            <xs:complexType name="URI">
<!--                <xs:annotation>-->
<!--                    <xs:documentation>CSS spec shows that the keyword for this is actually url!</xs:documentation>-->
<!--                </xs:annotation>-->
<!--                <xs:attribute name="value">-->
<!--                    <xsl:attribute name="type">xs:string</xsl:attribute>-->
<!--                    <xsl:attribute name="use">required</xsl:attribute>-->
<!--                </xs:attribute>-->
            </xs:complexType>
            <xs:complexType name="keyword">
<!--                <xs:attribute name="value">-->
<!--                    <xsl:attribute name="type">xs:integer</xsl:attribute>-->
<!--                    <xsl:attribute name="use">required</xsl:attribute>-->
<!--                </xs:attribute>-->
            </xs:complexType>
            <xsl:comment> ==================================================================== </xsl:comment>
            <xsl:comment>         Shared elements                                              </xsl:comment>
            <xsl:comment> ==================================================================== </xsl:comment>
            <xs:element name="angle">
<!--                <xsl:attribute name="type">AngleType</xsl:attribute>-->
            </xs:element>
            <xs:element name="ColorType">
<!--                <xsl:attribute name="type">ColorType</xsl:attribute>-->
            </xs:element>
            <xs:element name="inherit"/>
            <xs:element name="integer">
<!--                <xsl:attribute name="type">xs:integer</xsl:attribute>-->
            </xs:element>
            <xs:element name="length">
<!--                <xsl:attribute name="type">LengthType</xsl:attribute>-->
            </xs:element>
            <xs:element name="mariner-component-uri">
<!--                <xsl:attribute name="type">MarinerComponentURI</xsl:attribute>-->
            </xs:element>
            <xs:element name="number">
<!--                <xsl:attribute name="type">xs:double</xsl:attribute>-->
            </xs:element>
            <xs:element name="percentage">
<!--                <xsl:attribute name="type">PercentageType</xsl:attribute>-->
            </xs:element>
            <xs:element name="string">
<!--                <xsl:attribute name="type">xs:string</xsl:attribute>-->
            </xs:element>
            <xs:element name="time">
<!--                <xsl:attribute name="type">TimeType</xsl:attribute>-->
            </xs:element>
            <xs:element name="uri">
<!--                <xsl:attribute name="type">URI</xsl:attribute>-->
            </xs:element>
            <xs:element name="fraction"/>
            <xsl:comment> ==================================================================== </xsl:comment>
            <xsl:comment>         Properties                                                   </xsl:comment>
            <xsl:comment> ==================================================================== </xsl:comment>
            <xsl:apply-templates select="thm:themeDefinition/thm:property[not(starts-with(thm:name, 'mcs-'))]"/>
            <xsl:comment> ==================================================================== </xsl:comment>
            <xsl:comment>         Mariner properties                                           </xsl:comment>
            <xsl:comment> ==================================================================== </xsl:comment>
            <xsl:apply-templates select="thm:themeDefinition/thm:property[starts-with(thm:name, 'mcs-')]"/>
        </xs:schema>

        <!-- Check that all the engineering annotations properties match architecture defined properties. -->
        <xsl:variable name="definitions" select="/thm:themeDefinition"/>
        <xsl:for-each select="$annotations/property">
            <xsl:variable name="name" select="name"/>
            <xsl:if test="not($definitions/thm:property[thm:name = $name])">
                <xsl:message>Engineering annotation found for undefined property <xsl:value-of select="$name"/>.</xsl:message>
            </xsl:if>
        </xsl:for-each>

    </xsl:template>

    <xsl:template match="thm:property">
        <xsl:variable name="name" select="thm:name"/>
        <xsl:variable name="annotation" select="$annotations/property[name = $name]"/>
        <xsl:if test="$annotation">

            <xs:element name="{$name}">
                <xsl:choose>
                    <xsl:when test=".//thm:typeRef">
                        <xsl:attribute name="type">
                            <xsl:variable name="type" select=".//thm:typeRef"/>
                            <xsl:value-of select="$PropertyTypes/type[@name = $type]"/>
                        </xsl:attribute>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:apply-templates mode="generate-content-model" select="thm:type">
                            <xsl:with-param name="enumerationGroup" select="$annotation/enumerationGroup"/>
                        </xsl:apply-templates>
                    </xsl:otherwise>
                </xsl:choose>
                <xsl:processing-instruction name="property">

                    <xsl:call-template name="generate-property-versions"/>

                    <xsl:if test="thm:specifications/thm:mcs">
                        <xsl:text> mariner="true"</xsl:text>
                    </xsl:if>
                    <xsl:if test="$annotation/dbTable">
                        <xsl:text> dbtable="</xsl:text>
                        <xsl:value-of select="$annotation/dbTable"/>
                        <xsl:text>"</xsl:text>
                    </xsl:if>
                    <xsl:if test="$annotation/dbColumnPrefix">
                        <xsl:text> dbcolumn_prefix="</xsl:text>
                        <xsl:value-of select="$annotation/dbColumnPrefix"/>
                        <xsl:text>"</xsl:text>
                    </xsl:if>
                    <xsl:if test="$annotation/enumerationGroup">
                        <xsl:text> enumeration="</xsl:text>
                        <xsl:value-of select="$annotation/enumerationGroup"/>
                        <xsl:text>"</xsl:text>
                    </xsl:if>
                    <xsl:if test="$annotation/naturalName">
                        <xsl:text> naturalName="</xsl:text>
                        <xsl:value-of select="$annotation/naturalName"/>
                        <xsl:text>"</xsl:text>
                    </xsl:if>
                    <xsl:if test=".//thm:pairType">
                        <xsl:text> pair="true"</xsl:text>
                    </xsl:if>
                </xsl:processing-instruction>
                <xsl:variable name="RT-type">
                    <xsl:choose>
                        <xsl:when test=".//thm:typeRef">
                            <xsl:variable name="typeName" select="string(.//thm:typeRef)"/>
                            <xsl:copy-of select="key('type-definitions', $typeName)"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:copy-of select="thm:type"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:variable>
                <xsl:variable name="type" select="exslt-common:node-set($RT-type)"/>

                <xsl:if test="not(.//thm:orderedSetType|.//thm:listType|.//thm:pairType|.//thm:fractionType)">

                    <!-- save the property so we can extract its version info
                      as a default later -->
                    <xsl:variable name="property" select="."/>

                    <xsl:for-each select="$type//thm:uriType">
                        <xsl:processing-instruction name="value_type">
                            <xsl:text> type="URI"</xsl:text>
                            <xsl:call-template name="generate-value-type-versions">
                                <xsl:with-param name="property" select="$property"/>
                            </xsl:call-template>
                        </xsl:processing-instruction>
                    </xsl:for-each>
                    <xsl:for-each select="$type//thm:mcsComponentURIType">
                        <xsl:processing-instruction name="value_type">
                            <xsl:text> type="componentURI"</xsl:text>
                            <xsl:call-template name="generate-value-type-versions">
                                <xsl:with-param name="property" select="$property"/>
                            </xsl:call-template>
                        </xsl:processing-instruction>
                    </xsl:for-each>
                    <xsl:for-each select="$type//thm:mcsTranscodableURIType">
                        <xsl:processing-instruction name="value_type">
                            <xsl:text> type="transcodableURI"</xsl:text>
                            <xsl:call-template name="generate-value-type-versions">
                                <xsl:with-param name="property" select="$property"/>
                            </xsl:call-template>
                        </xsl:processing-instruction>
                    </xsl:for-each>
                    <xsl:for-each select="$type//thm:colorType">
                        <xsl:processing-instruction name="value_type">
                            <xsl:text> type="color"</xsl:text>
                            <xsl:call-template name="generate-value-type-versions">
                                <xsl:with-param name="property" select="$property"/>
                            </xsl:call-template>
                        </xsl:processing-instruction>
                    </xsl:for-each>
                    <xsl:for-each select="$type//thm:keywords">
                        <!-- only select first keywords cos text decoration has two
                           because it had "normal" keywords and also "bitset" keywords -->
                        <xsl:if test="position() = 1">
                          <xsl:processing-instruction name="value_type">
                              <xsl:text> type="keyword"</xsl:text>
                              <xsl:call-template name="generate-value-type-versions">
                                  <xsl:with-param name="property" select="$property"/>
                              </xsl:call-template>
                          </xsl:processing-instruction>
                        </xsl:if>
                    </xsl:for-each>
                    <xsl:for-each select="$type//thm:angleType">
                        <xsl:processing-instruction name="value_type">
                            <xsl:text> type="angle"</xsl:text>
                            <xsl:call-template name="generate-value-type-versions">
                                <xsl:with-param name="property" select="$property"/>
                            </xsl:call-template>
                        </xsl:processing-instruction>
                    </xsl:for-each>
                    <xsl:for-each select="$type//thm:functionCallType">
                        <xsl:processing-instruction name="value_type">
                            <xsl:text> type="function-call"</xsl:text>
                            <xsl:call-template name="generate-value-type-versions">
                                <xsl:with-param name="property" select="$property"/>
                            </xsl:call-template>
                        </xsl:processing-instruction>
                    </xsl:for-each>
                    <xsl:for-each select="$type//thm:timeType">
                        <xsl:processing-instruction name="value_type">
                            <xsl:text> type="time"</xsl:text>
                            <xsl:call-template name="generate-value-type-versions">
                                <xsl:with-param name="property" select="$property"/>
                            </xsl:call-template>
                        </xsl:processing-instruction>
                    </xsl:for-each>
                    <xsl:for-each select="$type//thm:lengthType">
                        <xsl:processing-instruction name="value_type">
                            <xsl:text> type="length"</xsl:text>
                            <xsl:call-template name="generate-value-type-versions">
                                <xsl:with-param name="property" select="$property"/>
                            </xsl:call-template>
                        </xsl:processing-instruction>
                    </xsl:for-each>
                    <xsl:for-each select="$type//thm:numberType">
                        <xsl:processing-instruction name="value_type">
                            <xsl:text> type="number"</xsl:text>
                            <xsl:call-template name="generate-value-type-versions">
                                <xsl:with-param name="property" select="$property"/>
                            </xsl:call-template>
                        </xsl:processing-instruction>
                    </xsl:for-each>
                    <xsl:for-each select="$type//thm:integerType">
                        <xsl:processing-instruction name="value_type">
                            <xsl:text> type="integer"</xsl:text>
                            <xsl:call-template name="generate-value-type-versions">
                                <xsl:with-param name="property" select="$property"/>
                            </xsl:call-template>
                        </xsl:processing-instruction>
                    </xsl:for-each>
                    <xsl:for-each select="$type//thm:percentageType">
                        <xsl:processing-instruction name="value_type">
                            <xsl:text> type="percentage"</xsl:text>
                            <xsl:call-template name="generate-value-type-versions">
                                <xsl:with-param name="property" select="$property"/>
                            </xsl:call-template>
                        </xsl:processing-instruction>
                    </xsl:for-each>
                    <xsl:for-each select="$type//thm:stringType">
                        <xsl:processing-instruction name="value_type">
                            <xsl:text> type="string"</xsl:text>
                            <xsl:call-template name="generate-value-type-versions">
                                <xsl:with-param name="property" select="$property"/>
                            </xsl:call-template>
                        </xsl:processing-instruction>
                    </xsl:for-each>
                    <xsl:for-each select="$type//thm:frequencyType">
                        <xsl:processing-instruction name="value_type">
                            <xsl:text> type="frequency"</xsl:text>
                            <xsl:call-template name="generate-value-type-versions">
                                <xsl:with-param name="property" select="$property"/>
                            </xsl:call-template>
                        </xsl:processing-instruction>
                    </xsl:for-each>
                </xsl:if>
            </xs:element>
        </xsl:if>
        <xsl:if test="not($annotation)">
            <xsl:message>Engineering annotations not found for <xsl:value-of select="$name"/>.</xsl:message>
        </xsl:if>
    </xsl:template>

    <xsl:template mode="generate-content-model" match="text()"/>

    <xsl:template mode="generate-content-model" match="thm:type">
        <xsl:param name="enumerationGroup"/>
        <xs:complexType>
            <xsl:apply-templates mode="generate-content-model">
                <xsl:with-param name="enumerationGroup" select="$enumerationGroup"/>
            </xsl:apply-templates>
        </xs:complexType>
    </xsl:template>

    <xsl:template mode="generate-content-model" match="thm:angleType">
        <xs:element ref="angle"/>
    </xsl:template>

    <xsl:template mode="generate-content-model" match="thm:choiceType">
        <xsl:param name="enumerationGroup"/>
        <xs:choice>
            <xsl:apply-templates mode="generate-content-model">
                <xsl:with-param name="enumerationGroup" select="$enumerationGroup"/>
            </xsl:apply-templates>
            <xsl:call-template name="AddInherit"/>
        </xs:choice>
    </xsl:template>

    <xsl:template mode="generate-content-model" match="thm:colorType">
        <xs:element ref="ColorType"/>
    </xsl:template>

    <xsl:template mode="generate-content-model" match="thm:integerType">
        <xs:element ref="integer"/>
    </xsl:template>

    <xsl:template mode="generate-content-model" match="thm:keywords">
        <xsl:param name="enumerationGroup"/>

        <xsl:apply-templates mode="generate-content-model">
            <xsl:with-param name="enumerationGroup" select="$enumerationGroup"/>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template mode="generate-content-model" match="thm:keyword">
        <xsl:param name="enumerationGroup"/>

        <xsl:variable name="keyword-element-name">
            <xsl:apply-templates mode="get-keyword-element-name" select="."/>
        </xsl:variable>

        <xs:element name="{$keyword-element-name}">
            <xsl:processing-instruction name="enumeration">
                <xsl:if test="$enumerationGroup != ''">
                    <xsl:text> grp="</xsl:text>
                    <xsl:value-of select="$enumerationGroup"/>
                    <xsl:text>"</xsl:text>
                </xsl:if>
<!--                <xsl:apply-templates mode="get-enumeration-group" select="."/>-->

                <xsl:call-template name="generate-keyword-versions"/>

            </xsl:processing-instruction>
        </xs:element>
    </xsl:template>

    <xsl:template mode="generate-content-model" match="thm:lengthType">
        <xsl:param name="minOccurs" select="1"/>
        <xs:element ref="length">
            <xsl:if test="$minOccurs = 0">
                <xsl:attribute name="minOccurs">
                    <xsl:value-of select="$minOccurs"/>
                </xsl:attribute>
            </xsl:if>
        </xs:element>
    </xsl:template>

    <xsl:template mode="generate-content-model" match="thm:listType|thm:orderedSetType">
        <xsl:param name="enumerationGroup"/>
        <xs:choice>
            <xs:choice maxOccurs="unbounded">
                <xsl:apply-templates mode="generate-content-model" select="thm:choiceType/*">
                    <xsl:with-param name="enumerationGroup" select="$enumerationGroup"/>
                </xsl:apply-templates>
            </xs:choice>
            <xsl:call-template name="AddInherit"/>
        </xs:choice>
    </xsl:template>

    <xsl:template mode="generate-content-model" match="thm:mcsComponentURIType">
        <xs:element ref="mariner-component-uri"/>
    </xsl:template>

    <xsl:template mode="generate-content-model" match="thm:mcsTranscodableURIType">
        <xs:element ref="mariner-component-uri"/>
    </xsl:template>

    <xsl:template mode="generate-content-model" match="thm:numberType">
        <xs:element ref="number"/>
    </xsl:template>

    <xsl:template mode="generate-content-model" match="thm:pairType">
        <xsl:param name="enumerationGroup"/>
        <xs:choice>
            <xs:sequence>
                <xsl:apply-templates mode="generate-content-model" select="thm:first">
                    <xsl:with-param name="enumerationGroup" select="$enumerationGroup"/>
                </xsl:apply-templates>
                <xsl:apply-templates mode="generate-content-model" select="thm:second">
                    <xsl:with-param name="enumerationGroup" select="$enumerationGroup"/>
                </xsl:apply-templates>
            </xs:sequence>
            <xsl:call-template name="AddInherit"/>
        </xs:choice>
    </xsl:template>

    <xsl:template mode="generate-content-model" match="thm:fractionType">
        <xsl:param name="enumerationGroup"/>
        <xs:element ref="fraction">
            <xs:complexType>
                <xs:sequence>
                    <xsl:apply-templates mode="generate-content-model" select="thm:numerator">
                        <xsl:with-param name="enumerationGroup" select="$enumerationGroup"/>
                    </xsl:apply-templates>
                    <xsl:apply-templates mode="generate-content-model" select="thm:denominator">
                        <xsl:with-param name="enumerationGroup" select="$enumerationGroup"/>
                    </xsl:apply-templates>
                </xs:sequence>
            </xs:complexType>
        </xs:element>
    </xsl:template>

    <xsl:template mode="generate-content-model" match="thm:pairType/thm:first|thm:pairType/thm:second">
        <xsl:param name="enumerationGroup"/>

        <xsl:variable name="propertyName" select="ancestor::thm:property/thm:name"/>
        <xsl:variable name="groupName" select="thm:name"/>
        <xsl:variable name="actualEnumerationGroup">
            <xsl:value-of select="$annotations/property[name = $propertyName]/keywordGroup[name = $groupName]/enumerationGroup"/>
        </xsl:variable>

        <xsl:apply-templates mode="generate-content-model" select="*[not(self::thm:name)]">
            <xsl:with-param name="enumerationGroup" select="$actualEnumerationGroup"/>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template mode="generate-content-model" match="thm:fractionType/thm:numerator|thm:fractionType/thm:denominator">
        <xsl:param name="enumerationGroup"/>

        <xsl:variable name="propertyName" select="ancestor::thm:property/thm:name"/>
        <xsl:variable name="groupName" select="local-name()"/>
        <xsl:variable name="actualEnumerationGroup">
            <xsl:value-of select="$annotations/property[name = $propertyName]/keywordGroup[name = $groupName]/enumerationGroup"/>
        </xsl:variable>

        <xsl:apply-templates mode="generate-content-model">
            <xsl:with-param name="enumerationGroup" select="$actualEnumerationGroup"/>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template mode="generate-content-model" match="thm:percentageType">
        <xs:element ref="percentage"/>
    </xsl:template>

    <xsl:template mode="generate-content-model" match="thm:stringType">
        <xs:element ref="string"/>
    </xsl:template>

    <xsl:template mode="generate-content-model" match="thm:timeType">
        <xs:element ref="time"/>
    </xsl:template>

    <xsl:template mode="generate-content-model" match="thm:uriType">
        <xs:element ref="uri"/>
    </xsl:template>

    <xsl:template mode="generate-content-model" match="thm:frequencyType">
        <xs:element ref="frequency"/>
    </xsl:template>

    <xsl:template name="AddInherit">
        <xsl:if test="not(ancestor::thm:pairType|ancestor::thm:choiceType|ancestor::thm:listType|thm:orderedSetType|
        ancestor::thm:fractionType)">
            <xs:element ref="inherit"/>
        </xsl:if>
    </xsl:template>

    <!--
     ! Mode: get-enumeration-group
     !
     !  Templates active within this mode are used to get the name of the enumeration group.
     !-->
    <xsl:template mode="get-enumeration-group" match="text()"/>

    <xsl:template mode="get-enumeration-group" match="thm:keyword">
        <xsl:variable name="propertyName" select="ancestor::thm:property/thm:name"/>
        <xsl:text> grp="</xsl:text>
        <xsl:value-of select="$annotations/property[name = $propertyName]/enumerationGroup"/>
        <xsl:text>"</xsl:text>
    </xsl:template>

    <!--
     ! Mode: get-enumeration-group
     !
     !  Templates active within this mode are used to get the name of the enumeration group.
     !-->
    <xsl:template mode="get-keyword-element-name" match="text()"/>

    <xsl:template mode="get-keyword-element-name" match="thm:keyword">
        <xsl:value-of select="thm:name"/>
    </xsl:template>

    <xsl:template mode="get-keyword-element-name" match="thm:keyword[number(thm:name) > 0 and ancestor::thm:property/thm:name = 'font-weight']">
        <xsl:text>weight-</xsl:text>
        <xsl:value-of select="thm:name"/>
    </xsl:template>


    <!--
        Code to generate the version markers required for CSS Version support.
        todo: later: encourage architecture to add missing css version data below.
        todo: later: factor below templates together once above is addressed.
      -->

    <xsl:template name="generate-property-versions">
        <xsl:text> css1="</xsl:text>
        <xsl:choose>
            <xsl:when test="thm:specifications/thm:css1">true</xsl:when>
            <xsl:otherwise>false</xsl:otherwise>
        </xsl:choose>
        <xsl:text>"</xsl:text>

        <xsl:text> css2="</xsl:text>
        <xsl:choose>
            <xsl:when test="thm:specifications/thm:css2">true</xsl:when>
            <xsl:otherwise>false</xsl:otherwise>
        </xsl:choose>
        <xsl:text>"</xsl:text>

        <xsl:text> cssmobile="</xsl:text>
        <xsl:choose>
            <xsl:when test="thm:specifications/thm:cssMobileProfile">true</xsl:when>
            <xsl:otherwise>false</xsl:otherwise>
        </xsl:choose>
        <xsl:text>"</xsl:text>

        <xsl:text> csswap="</xsl:text>
        <xsl:choose>
            <xsl:when test="thm:specifications/thm:cssWap">true</xsl:when>
            <xsl:otherwise>false</xsl:otherwise>
        </xsl:choose>
        <xsl:text>"</xsl:text>
    </xsl:template>

    <xsl:template name="generate-value-type-versions">
        <xsl:param name="property"/>
        <!-- data contains no values for css1, css2 -->

        <xsl:variable name="specifications-RT">
            <xsl:choose>
                <!--
                 ! If any of the sibling keywords have a specification then
                 ! the keywords have to explictly specify the specifications
                 ! that they are valid within themselves, rather than falling
                 ! back to the property.
                 !-->
                <xsl:when test="thm:specifications">
                    <xsl:copy-of select="thm:specifications/*"/>
                </xsl:when>

                <!--
                 ! Otherwise, fall straight back to the property.
                 !-->
                <xsl:otherwise>
                    <xsl:copy-of select="$property/thm:specifications/*"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>

        <xsl:variable name="specifications" select="exslt-common:node-set($specifications-RT)"/>

        <xsl:text> cssmobile="</xsl:text>
        <xsl:choose>
            <xsl:when test="$specifications/thm:cssMobileProfile">true</xsl:when>
            <xsl:otherwise>false</xsl:otherwise>
        </xsl:choose>
        <xsl:text>"</xsl:text>

        <xsl:text> csswap="</xsl:text>
        <xsl:choose>
            <xsl:when test="$specifications/thm:cssWap">true</xsl:when>
            <xsl:otherwise>false</xsl:otherwise>
        </xsl:choose>
        <xsl:text>"</xsl:text>
    </xsl:template>

    <xsl:template name="generate-keyword-versions">
        <!-- data contains no values for css1, css2-->

        <xsl:variable name="specifications-RT">
            <xsl:choose>
                <!--
                 ! If any of the sibling keywords have a specification then
                 ! the keywords have to explictly specify the specifications
                 ! that they are valid within themselves, rather than falling
                 ! back to the property.
                 !-->
                <xsl:when test="../thm:keyword/thm:specifications">
                    <xsl:copy-of select="thm:specifications/*"/>
                </xsl:when>

                <!--
                 ! Otherwise, fall straight back to the property.
                 !-->
                <xsl:otherwise>
                    <xsl:copy-of select="ancestor::thm:property/thm:specifications/*"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>

        <xsl:variable name="specifications" select="exslt-common:node-set($specifications-RT)"/>

        <xsl:text> cssmobile="</xsl:text>
        <xsl:choose>
            <xsl:when test="$specifications/thm:cssMobileProfile">true</xsl:when>
            <xsl:otherwise>false</xsl:otherwise>
        </xsl:choose>
        <xsl:text>"</xsl:text>

        <xsl:text> csswap="</xsl:text>
        <xsl:choose>
            <xsl:when test="$specifications/thm:cssWap">true</xsl:when>
            <xsl:otherwise>false</xsl:otherwise>
        </xsl:choose>
        <xsl:text>"</xsl:text>
    </xsl:template>

</xsl:stylesheet>

<!--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Dec-05	10845/1	pduffin	VBM:2005121511 Moved architecture files from CVS to MCS Depot

 29-Nov-05	10505/7	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/3	pduffin	Quick fix for GenerateMarinerTheme.xsl

 29-Nov-05	10505/3	pduffin	Quick fix for GenerateMarinerTheme.xsl

 29-Nov-05	10347/1	pduffin	Quick fix for GenerateMarinerTheme.xsl

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 13-Sep-05	9496/1	pduffin	VBM:2005091211 Replaced text-decoration with individual properties

 13-Jan-05	6607/1	ianw	VBM:2005010703 New DB Schema generation

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 19-Nov-04	5733/5	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 25-Mar-04	3550/2	pduffin	VBM:2004032306 Improved theme generation code, reducing the number of automatically generated classes and added support for initial value

 ===========================================================================
-->
