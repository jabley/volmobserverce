<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
  xmlns:yweather="http://xml.weather.yahoo.com/ns/rss/1.0"
  xmlns:geo="http://www.w3.org/2003/01/geo/wgs84_pos#">
  <xsl:output method="xml"/>
  <xsl:template match="/">
    <div class="company_item" xmlns="http://www.w3.org/2002/06/xhtml2">
      <h3 class="company_header">
        <xsl:value-of select="/rss/channel/item/title"/>
      </h3>
      <div>
        <h4>Current Conditions:</h4>
        <p><xsl:value-of select="/rss/channel/item/yweather:condition/@text"/>,
          <xsl:value-of
            select="/rss/channel/item/yweather:condition/@temp"/>
          <xsl:value-of select="/rss/channel/yweather:units/@temperature"/></p>
        <h4>Forecast:</h4>
        <xsl:for-each select="/rss/channel/item/yweather:forecast">
          <p>
            <xsl:value-of select="current()/@day"/> - <xsl:value-of
              select="current()/@text"/>.
            High: <xsl:value-of select="current()/@high"/>
            <xsl:value-of select="/rss/channel/yweather:units/@temperature"/> Low:
            <xsl:value-of select="current()/@low"/>
            <xsl:value-of select="/rss/channel/yweather:units/@temperature"/>
          </p>
        </xsl:for-each>
      </div>
    </div>
  </xsl:template>
</xsl:stylesheet>