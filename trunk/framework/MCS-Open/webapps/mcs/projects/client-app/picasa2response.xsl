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
<xsl:stylesheet
  xmlns="http://www.w3.org/2002/06/xhtml2"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:atom="http://www.w3.org/2005/Atom"
  xmlns:openSearch="http://a9.com/-/spec/opensearchrss/1.0/"
  xmlns:exif="http://schemas.google.com/photos/exif/2007" 
  xmlns:geo="http://www.w3.org/2003/01/geo/wgs84_pos#" 
  xmlns:gml="http://www.opengis.net/gml" 
  xmlns:georss="http://www.georss.org/georss" 
  xmlns:photo="http://www.pheed.com/pheed/" 
  xmlns:media="http://search.yahoo.com/mrss/" 
  xmlns:batch="http://schemas.google.com/gdata/batch" 
  xmlns:gphoto="http://schemas.google.com/photos/2007"
  xmlns:gallery="http://www.volantis.com/xmlns/2006/10/gallery-widget"
  xmlns:widget="http://www.volantis.com/xmlns/2006/05/widget">
  <xsl:template match="atom:feed">
    <gallery:items count="6">
      <xsl:for-each select="atom:entry">
        <xsl:apply-templates select="."/>
      </xsl:for-each>
    </gallery:items>
  </xsl:template>

  <xsl:template match="atom:entry">
    <gallery:item>
      <widget:summary style="mcs-effect-style:fade; mcs-effect-duration:1s">
        <div>
          <object style="width:50px;height:50px">
            <xsl:attribute name="src"><xsl:value-of select="atom:content/@src"/></xsl:attribute>
            <xsl:attribute name="srctype"><xsl:value-of select="atom:content/@type"/></xsl:attribute>
            <param name="mcs-external-label">
              <xsl:attribute name="value">
                <xsl:value-of select="media:title"/>
              </xsl:attribute>
            </param>
          </object> 
        </div>
      </widget:summary>
      <widget:detail style="mcs-effect-style:slide-left; mcs-effect-duration:1s">
        <div style="text-align: center;width:100%;height:100%">
          <div style="width:100%;height:10%"><xsl:value-of select="atom:title"/></div>
          <div style="width:100%;height:80%">
            <object style="width:170px;height:170px">
              <xsl:attribute name="src"><xsl:value-of select="atom:content/@src"/></xsl:attribute>
              <xsl:attribute name="srctype"><xsl:value-of select="atom:content/@type"/></xsl:attribute>
              <param name="mcs-external-label">
                <xsl:attribute name="value">
                  <xsl:value-of select="media:title"/>
                </xsl:attribute>
              </param>
            </object>
          </div>
          <div style="width:100%;height:10%"><xsl:value-of select="atom:summary"/></div>
        </div>
      </widget:detail>
    </gallery:item>
  </xsl:template>
</xsl:stylesheet>
