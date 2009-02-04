<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns="http://www.w3.org/2002/06/xhtml2"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:atom="http://www.w3.org/2005/Atom" version="2.0">
    <xsl:template match="atom:feed">
        <h1><xsl:value-of select="atom:title"/></h1>
        <xsl:for-each select="atom:entry">
            <div>
                <a>
                    <xsl:attribute name="href"><xsl:value-of select="atom:content/@src"/></xsl:attribute>
                    <xsl:value-of select="atom:title"/>                    
                </a>               
            </div>
        </xsl:for-each>
    </xsl:template>
    
</xsl:stylesheet>
