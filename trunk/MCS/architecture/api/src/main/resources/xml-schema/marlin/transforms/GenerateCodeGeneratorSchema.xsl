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
 ! This XSLT generates external schemata from internal versions.
 ! ***************************************************************************************************
 !
 !-->
<xsl:stylesheet xmlns:common="http://exslt.org/common" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" version="1.0" exclude-result-prefixes="common">

    <!-- Output as xml using xalan specific extensions. -->
    <xsl:output xmlns:xalan="http://xml.apache.org/xslt"
                xalan:indent-amount="4"
                indent="yes"
                method="xml"/>

    <!-- Copy everything through by default. -->
    <xsl:template match="*">
        <xsl:copy>
            <xsl:apply-templates select="@*"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="@*|text()|processing-instruction()|comment()">
        <xsl:copy/>
    </xsl:template>

    <!-- Remove top level complexType elements. -->
    <xsl:template match="/xs:schema/xs:complexType"/>

    <!-- Handle elements that refer to complexTypes using the type attribute. -->
    <xsl:template match="xs:element[@type]">
        <xsl:copy>
            <!-- Copy the name attribute. -->
            <xsl:apply-templates select="@name"/>

            <!-- Insert the named complexType body. -->
            <xsl:variable name="type" select="@type"/>
            <xsl:variable name="complexType" select="/xs:schema/xs:complexType[@name = $type]"/>
            <xs:complexType>
                <!-- Copy the mixed attribute if it is set. -->
                <xsl:if test="$complexType/@mixed">
                    <xsl:attribute name="mixed"><xsl:value-of select="$complexType/@mixed"/></xsl:attribute>
                </xsl:if>

                <!-- Copy the body of the complexType. -->
                <xsl:apply-templates select="$complexType/node()"/>
            </xs:complexType>

            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>

    <!-- Handle elements that extend complex types. -->
    <xsl:template match="xs:element[xs:complexType/xs:complexContent/xs:extension]">
        <xsl:copy>
            <!-- Copy the name attribute. -->
            <xsl:apply-templates select="@name"/>

            <!-- Insert the named complexType body. -->
            <xsl:variable name="type" select="xs:complexType/xs:complexContent/xs:extension/@base"/>
            <xsl:variable name="complexType" select="/xs:schema/xs:complexType[@name = $type]"/>
            <xs:complexType>
                <!-- Copy the mixed attribute if it is set. -->
                <xsl:if test="$complexType/@mixed">
                    <xsl:attribute name="mixed"><xsl:value-of select="$complexType/@mixed"/></xsl:attribute>
                </xsl:if>

                <!-- Copy the body of the complexType. -->
                <xsl:apply-templates select="$complexType/node()"/>

                <!-- Add the extension body. -->
                <xsl:apply-templates select="xs:complexType/xs:complexContent/xs:extension/node()"/>

            </xs:complexType>

            <!-- Copy the body of the element making sure to ignore the complexType. -->
            <xsl:apply-templates select="node()[not(self::xs:complexType)]"/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>
