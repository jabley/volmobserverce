<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
   <xsl:output method="xml" encoding="UTF-8"/>
   <xsl:param name="header_param"/>
   <xsl:template match="/">
      <div class="company_item" xmlns="http://www.w3.org/2002/06/xhtml2"
         xmlns:sqlr="http://www.volantis.com/xmlns/marlin-sql-result">
         <h3 class="company_header">
            <xsl:value-of select="$header_param"/>
         </h3>
         <dl class="company_item">
            <xsl:for-each select="/sqlr:result/sqlr:row">
               <dt>
                  <a href="#">
                     <xsl:value-of select="current()/sqlr:string[@column='PRODUCT']"/>
                  </a>
               </dt>
               <dd>
                  <xsl:value-of select="current()/sqlr:string[@column='DESCRIPTION']"/>
                  <strong>
                     <xsl:value-of select="current()/sqlr:string[@column='PRICE']"/>
                  </strong>
               </dd>
            </xsl:for-each>
         </dl>
      </div>
   </xsl:template>
</xsl:stylesheet>
