<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
   <xsl:output method="xml"/>
   <xsl:template match="/">
      <div xmlns="http://www.w3.org/2002/06/xhtml2">
         <ul>
            <xsl:for-each select="/html/body/ul/li">
               <li>
                  <xsl:value-of select="translate(current(),'&#194;','')"/>
               </li>
            </xsl:for-each>
         </ul>
      </div>
   </xsl:template>
</xsl:stylesheet>
