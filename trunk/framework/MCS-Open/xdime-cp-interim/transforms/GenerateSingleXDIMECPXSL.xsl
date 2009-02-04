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
<xsl:stylesheet xmlns:common="http://exslt.org/common" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:XSL="http://www.w3.org/1999/XSL/Transform/Alias" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0" exclude-result-prefixes="common">

    <!-- Output as xml using xalan specific extensions. -->
    <xsl:output xmlns:xalan="http://xml.apache.org/xslt"
                xalan:indent-amount="4"
                indent="yes"
                method="xml"/>

    <xsl:namespace-alias stylesheet-prefix="XSL" result-prefix="xsl"/>

    <xsl:variable name="root-stylesheet-id" select="generate-id(/xsl:stylesheet)"/>

    <!-- Copy everything by default. -->
    <xsl:template match="*">
        <xsl:copy>
           <xsl:apply-templates select="@*"/>
           <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="text()|comment()|processing-instruction()|@*">
        <xsl:copy/>
    </xsl:template>

    <!-- Comments outside the XSL are ignored. -->
    <xsl:template match="/comment()"/>

    <!-- Add a header at the top. -->
    <xsl:template match="/">
        <xsl:comment> ==========================================================================
 ! (c) Volantis Systems Ltd 2004. 
 ! ============================================================================
 ! This XSLT converts from XDIME CP into XDIME and is the main component of
 ! XDIME CP v1.
 !
 ! This document MUST NOT be modified under any circumstances as it could
 ! prevent XDIME CP documents from working with the future native
 ! implementation of XDIME CP.
 !
 ! Please refer to the release notes for further details about how to
 ! customise the behaviour safely.
 ! ======================================================================== </xsl:comment>
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="xsl:include|xsl:import">
        <xsl:apply-templates select="document(@href)/xsl:stylesheet/*"/>
    </xsl:template>

    <xsl:template match="xsl:stylesheet[generate-id(.) != $root-stylesheet-id]">
        <xsl:apply-templates/>
    </xsl:template>

</xsl:stylesheet>

<!--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Jun-04	4630/3	pduffin	VBM:2004060306 Integrated and produced distribution

 16-Jun-04	4630/1	pduffin	VBM:2004060306 Some more changes

 ===========================================================================
-->
