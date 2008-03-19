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
 ! This XSLT provides templates that will copy a schema restructuring it
 ! if necessary to produce a valid schema.
 ! ***************************************************************************************************
 !
 !-->
<xsl:stylesheet xmlns:common="http://exslt.org/common" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0" exclude-result-prefixes="common">

    <!-- Output as xml using xalan specific extensions. -->
    <xsl:output xmlns:xalan="http://xml.apache.org/xslt"
                xalan:indent-amount="4"
                encoding="us-ascii"
                indent="yes"
                method="xml"/>

    <!--
     ! Spaces are not needed within the schema.
     !-->
    <xsl:strip-space elements="*"/>

    <!-- By default text is ignored. -->
    <xsl:template match="text()"/>

    <!-- Text within the annotation element is copied. -->
    <xsl:template match="xs:annotation/*//text()">
        <xsl:copy/>
    </xsl:template>

    <!-- By default all elements are copied. -->
    <xsl:template match="*">
        <xsl:copy>
            <xsl:apply-templates select="@*"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>

    <!-- By default all attributes are copied. -->
    <xsl:template match="@*">
        <xsl:copy/>
    </xsl:template>

    <!-- By default all comments are copied. -->
    <xsl:template match="comment()">
        <xsl:copy/>
    </xsl:template>

    <!--
     ! Copy the schema.
     !
     ! This will ensure that the import and include statements come
     ! before all other 
     !-->
    <xsl:template match="xs:schema">
        <xsl:copy>
            <xsl:apply-templates select="@*"/>
            <xsl:apply-templates select="xs:import|xs:include|xs:redefine"/>
            <xsl:apply-templates select="node()[not(self::xs:import|self::xs:include|self::xs:redefine)]"/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>
