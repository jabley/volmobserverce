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
 ! This XSLT cleans up automatically merged XSLTs.
 ! ***************************************************************************************************
 !
 !-->
<xsl:stylesheet xmlns:common="http://exslt.org/common" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0" exclude-result-prefixes="common">

    <!-- Output as xml using xalan specific extensions. -->
    <xsl:output xmlns:xalan="http://xml.apache.org/xslt"
                xalan:indent-amount="4"
                indent="yes"
                method="xml"/>

    <xsl:key name="global-param-name" match="/xsl:stylesheet/xsl:param" use="@name"/>

    <xsl:key name="named-templates" match="/xsl:stylesheet/xsl:template[@name]" use="@name"/>

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

    <xsl:template match="xsl:stylesheet">
        <xsl:copy>
            <xsl:apply-templates select="@*"/>

            <!--
             ! Remove duplicates parameters and ensure that they are all added at the front.
             !-->
            <xsl:for-each select="xsl:param">
                <xsl:if test="generate-id(.) = generate-id(key('global-param-name', @name)[1])">
                    <xsl:copy>
                        <xsl:apply-templates select="@*"/>
                        <xsl:apply-templates/>
                    </xsl:copy>
                </xsl:if>
            </xsl:for-each>
            <xsl:apply-templates select="node()[not(self::xsl:param)]"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="xsl:stylesheet/xsl:template[@name]">
        <xsl:if test="generate-id(.) = generate-id(key('named-templates', @name)[1])">
            <xsl:copy>
                <xsl:apply-templates select="@*"/>
                <xsl:apply-templates/>
            </xsl:copy>
        </xsl:if>
    </xsl:template>

</xsl:stylesheet>

<!--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 16-Jun-04	4630/1	pduffin	VBM:2004060306 Some more changes

 ===========================================================================
-->
