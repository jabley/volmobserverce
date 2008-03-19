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
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:sql="http://www.volantis.com/xmlns/internal/sql"
    xmlns:exslt="http://exslt.org/common">
    <xsl:output method="html" version="1.0" encoding="UTF-8" indent="yes"/>
    
    <xsl:param name="comments" select="false"/>
    
    <!--
        Intended to be a static lookup table of reserved words. If a table name 
        or column names matches one of these, then then report an error. But 
        how?
    -->
    <xsl:variable name="keywords">
        <keywords>
            <keyword>KEYS</keyword>
        </keywords>
    </xsl:variable>
    
    <xsl:variable name="keywordsRTF" select="exslt:node-set($keywords)/keywords" />
    
    <!-- 
        Static table of data types that don't require a size being set when 
        they are specified in CREATE statements.
    -->
    <xsl:variable name="dataTypes">
        <types>
            <type>DATE</type>
            <type>BOOLEAN</type>
        </types>
    </xsl:variable>
    
    <xsl:variable name="dataTypesRTF" select="exslt:node-set($dataTypes)/types" />
    
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
        <xsl:apply-templates mode="constraints"/>
        <xsl:apply-templates mode="index"/>
    </xsl:template>
    
    
    <!-- Match the sql:table element to produce the "CREATE TABLE" 
         statement -->
    <xsl:template match="sql:table" mode="fields">
        <xsl:text>
CREATE TABLE </xsl:text>
        <xsl:call-template name="get-name">
            <xsl:with-param name="name" select="sql:name" />
        </xsl:call-template>
        <xsl:text> (
</xsl:text>
        <xsl:apply-templates select="sql:index|sql:field" mode="fields"/>
        <xsl:text> );</xsl:text>
    </xsl:template>
    
    <!-- 
        Encapsulate field name output with a check for reserved words 
    -->
    <xsl:template name="get-name">
        <xsl:param name="name" />
        
        <!-- 
            XPath 2.0 provides nice upper-case function, but we'll have to 
            manage without for now
        -->
        <xsl:if test="$keywordsRTF/keyword[text() = $name]">
            <xsl:message terminate="yes">
=====================================================
Terminating - suspected use of database reserved word '<xsl:value-of select="$name"/>'
=====================================================
            </xsl:message>
        </xsl:if>
        
        <xsl:value-of select="$name"/>
    </xsl:template>
    
    <!-- Match the sql:field element to produce the field definitions
         for the table -->
    <xsl:template match="sql:field" mode="fields">
        <xsl:if test="preceding-sibling::*[self::sql:field | self::sql:index] or parent::sql:index[preceding-sibling::*[self::sql:field | self::sql:index]]">
            <xsl:text>,
</xsl:text>            
        </xsl:if>
        <xsl:text>  </xsl:text>
        <xsl:call-template name="get-name">
            <xsl:with-param name="name" select="sql:name" />
        </xsl:call-template>
        <xsl:text> </xsl:text>
        <xsl:call-template name="get-type" />
        <xsl:call-template name="get-size" />
        <xsl:if test="sql:default">
            <xsl:text> DEFAULT </xsl:text>
            <!-- Need special handling for NULL values in TIMESTAMPS -->
            <!-- Need special handling for CHAR and VARCHAR (not NULL) values -->
            <xsl:choose>
                <xsl:when test="sql:datatype='DATE' and sql:default='NULL'">
                    <xsl:value-of select="0"/>
                </xsl:when>
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
    
    <xsl:template name="get-type">
        <xsl:choose>
            <xsl:when test="sql:datatype='VARCHAR' and sql:size > 255">
                <xsl:text>TEXT</xsl:text>
            </xsl:when>
            <xsl:when test="sql:datatype='DATE'">
                <xsl:text>TIMESTAMP</xsl:text>
            </xsl:when>
            <xsl:when test="sql:datatype='BOOLEAN'">
                <xsl:text>INT(1)</xsl:text>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="sql:datatype"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template name="get-size">
        <xsl:variable name="datatype" select="sql:datatype" />
        
        <xsl:choose>
            <xsl:when test="$dataTypesRTF/type[text() = $datatype]">
                <!-- Don't output the size -->
            </xsl:when>
            <xsl:otherwise>
                <xsl:if test="sql:size">
                    <xsl:text> (</xsl:text>
                    <xsl:value-of select="sql:size"/>
                    <xsl:text>)</xsl:text>
                </xsl:if>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="sql:index" mode="fields">
        <xsl:apply-templates select="sql:field" mode="fields"/>
    </xsl:template>
    
    <xsl:template match="sql:index" mode="index">
        <xsl:text>
CREATE INDEX </xsl:text>
        <xsl:value-of select="sql:name"/>
        <xsl:text>
  ON </xsl:text> 
        <xsl:value-of select="../sql:name"/>
        <xsl:text> (</xsl:text>
        <xsl:apply-templates select="sql:field" mode="index"/>
        <xsl:text>);</xsl:text>
        <xsl:text>
        </xsl:text>
    </xsl:template>
    
    <xsl:template match="sql:table" mode="index">
        <xsl:apply-templates select="sql:index" mode="index"/>
    </xsl:template>
    
    <xsl:template match="sql:field" mode="index">
        <xsl:if test="parent::*[self::sql:index]">
            <xsl:value-of select="sql:name"/>
            <xsl:if test="following-sibling::sql:field">
                <xsl:text>, </xsl:text>
            </xsl:if>
        </xsl:if>
    </xsl:template>    
    
    <!-- Match the sql:table element to produce the constraints via
         an ALTER TABLE statement. -->
    <xsl:template match="sql:table" mode="constraints">
        <xsl:choose>
            <!-- We don't want to create a primary key for this table -->
            <xsl:when test="sql:name='VMDEVICE_PATTERNS'"/>
            <xsl:otherwise>
                
                <xsl:if test="descendant::sql:field/sql:constraint='primary-key' ">
                    <xsl:text>
ALTER TABLE </xsl:text>
                    <xsl:value-of select="sql:name"/>
                    <xsl:text>
</xsl:text> 
                    <xsl:text>  ADD CONSTRAINT PK_</xsl:text>
                        <xsl:value-of select="sql:name"/>
                        <xsl:text>  PRIMARY KEY ( </xsl:text>
                        <xsl:apply-templates select="sql:field" mode="pk_constraint"/>
                        <xsl:text> ) </xsl:text>
                        <xsl:apply-templates select="sql:field" mode="un_constraint"/>
                        <xsl:text> ; </xsl:text>
                    </xsl:if>
                  </xsl:otherwise>
            </xsl:choose>
        </xsl:template>
    
       <xsl:template match="sql:field" mode="pk_constraint">
            <xsl:if test="sql:constraint='primary-key'">
                <xsl:if test="preceding-sibling::*[self::sql:field/sql:constraint='primary-key'] or parent::sql:index[preceding-sibling::*[self::sql:field/sql:constraint='primary-key']]">
                    <xsl:text>, </xsl:text>            
                </xsl:if>                
                <xsl:value-of select="sql:name"/>
            </xsl:if>
        </xsl:template>
  
    <xsl:template match="sql:field" mode="un_constraint">
        <xsl:if test="sql:constraint='unique'">
            <xsl:if test="preceding-sibling::*[self::sql:field/sql:constraint] or child::sql:constraint='primary-key'">
                <xsl:text>, </xsl:text>
            </xsl:if>
            <xsl:text>
  ADD CONSTRAINT UN_</xsl:text>
            <xsl:value-of select="sql:name"/>
            <xsl:text> UNIQUE ( </xsl:text>
            <xsl:value-of select="sql:name"/>
            <xsl:text> ) </xsl:text>
        </xsl:if>
    </xsl:template>
        
    </xsl:stylesheet>
    
    <!--
     ===========================================================================
     Change History
     ===========================================================================
     $Log$
    
     26-Jul-05	506/1	adrianj	VBM:2005071504 DB DDL file generation code moved to Synergetics
    
     28-Feb-05	7144/1	ianw	VBM:2005011711 Fixed generation of keys on mysql
    
     14-Jan-05	6607/4	ianw	VBM:2005010703 Minor fixup
    
     13-Jan-05	6607/1	ianw	VBM:2005010703 New DB Schema generation

 ===========================================================================
-->
