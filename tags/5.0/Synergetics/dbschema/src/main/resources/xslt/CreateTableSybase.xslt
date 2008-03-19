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
            <xsl:text>-- You may need to perform a COMMIT transaction after executing this script.
</xsl:text>
            <xsl:text>-- ----------------------------------------------------------------------------
</xsl:text>
        </xsl:if>
        <xsl:apply-templates mode="fields"/>
        <!-- <xsl:apply-templates mode="constraints"/> -->
        <!-- <xsl:apply-templates mode="index"/> -->
    </xsl:template>


    <!-- Match the sql:table element to produce the "CREATE TABLE"
         statement -->
    <xsl:template match="sql:table" mode="fields">
        <xsl:text>
CREATE TABLE </xsl:text>
        <xsl:value-of select="sql:name"/>
        <xsl:text> (
</xsl:text>
        <xsl:apply-templates select="sql:index|sql:field" mode="fields"/>
        <xsl:text> )</xsl:text>
    </xsl:template>

    <!-- Match the sql:field element to produce the field definitions
         for the table -->
    <xsl:template match="sql:field" mode="fields">
        <xsl:if test="preceding-sibling::*[self::sql:field | self::sql:index] or parent::sql:index[preceding-sibling::*[self::sql:field | self::sql:index]]">
            <xsl:text>,
</xsl:text>            
        </xsl:if>
        <xsl:text>  </xsl:text>
        <xsl:value-of select="sql:name"/>
        <xsl:text> </xsl:text>
        <xsl:value-of select="sql:datatype"/>
        <xsl:if test="sql:size">
            <xsl:text> (</xsl:text>
            <xsl:value-of select="sql:size"/>
            <xsl:text>)</xsl:text>
        </xsl:if>
        <xsl:if test="sql:default">
            <xsl:text> DEFAULT </xsl:text>
            <!-- Need special handling for CHAR and VARCHAR (not NULL) values -->
            <xsl:choose>
                <xsl:when test="(sql:datatype='VARCHAR' or sql:datatype='CHAR') and sql:default!='NULL'">
                    <xsl:text>'</xsl:text>
                    <xsl:value-of select="sql:default"/>
                    <xsl:text>'</xsl:text>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="sql:default"/>
                </xsl:otherwise>
            </xsl:choose>                    
        </xsl:if>
        <xsl:if test="sql:nullable='false'">
            <xsl:text> NOT NULL</xsl:text>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="sql:index" mode="fields">
        <xsl:apply-templates select="sql:field" mode="fields"/>
    </xsl:template>
    
</xsl:stylesheet>
<!--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 26-Jul-05	506/1	adrianj	VBM:2005071504 DB DDL file generation code moved to Synergetics

 24-Feb-05	7081/1	rgreenall	VBM:2005022301 SQL scripts are now generated in a form suitable for Sybase

 ===========================================================================
-->
