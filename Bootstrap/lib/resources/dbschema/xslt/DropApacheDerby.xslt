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

<?xmlspysamplexml C:\nonthemetables.xml?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:sql="http://www.volantis.com/xmlns/internal/sql">
    <xsl:output method="html" version="1.0" encoding="UTF-8" indent="yes"/>

    <xsl:param name="comments" select="false"/>

    <!-- Match the Root element -->
    <xsl:template match="/">
        <xsl:apply-templates/>
    </xsl:template>

    <!-- Match the sql:schema element and produce header comments for
         the file. -->

    <xsl:template match="sql:schema">
        <xsl:if test="$comments = 'true'">
            <xsl:text>-- ----------------------------------------------------------------------------
</xsl:text>
            <xsl:text>-- (c) Volantis Systems Ltd 2003 
</xsl:text>
            <xsl:text>-- ----------------------------------------------------------------------------
</xsl:text>
            <xsl:text>-- WARNING:
</xsl:text>
            <xsl:text>-- This script drops all tables in the Volantis MCS JDBC repository
</xsl:text>
            <xsl:text>--
</xsl:text>
            <xsl:text>-- IMPORTANT:
</xsl:text>
            <xsl:text>-- Please create a backup copy of your database BEFORE you execute this script
</xsl:text>
            <xsl:text>--
</xsl:text>
            <xsl:text>-- You may need to perform a COMMIT transaction after executing this script.
</xsl:text>
            <xsl:text>-- ----------------------------------------------------------------------------
</xsl:text>
        </xsl:if>
        <xsl:apply-templates/>
    </xsl:template>

    <!-- Match the sql:table element to produce the "DROP TABLE"
         statement -->
    <xsl:template match="sql:table">
        <xsl:text>
DROP TABLE </xsl:text>
        <xsl:value-of select="sql:name"/>
        <xsl:text>;</xsl:text>
    </xsl:template>
</xsl:stylesheet>
<!--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 27-Jul-05	506/5	adrianj	VBM:2005071504 Moving database DDL script generation into Synergetics

 ===========================================================================
-->
