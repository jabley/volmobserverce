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
  xmlns:gphoto="http://schemas.google.com/photos/2007">

  <xsl:template match="atom:feed">
    <h1><xsl:value-of select="atom:title"/></h1>
    <xsl:for-each select="atom:entry">
      <div>
	<object>
	  <xsl:attribute name="src"><xsl:value-of select="atom:content/@src"/></xsl:attribute>
 	  <xsl:attribute name="srctype"><xsl:value-of select="atom:content/@type"/></xsl:attribute>
	  <param name="mcs-external-label">
	    <xsl:attribute name="value">
	      <xsl:value-of select="atom:title"/>
	    </xsl:attribute>
	  </param>
	</object>
      </div>
    </xsl:for-each>
  </xsl:template>

</xsl:stylesheet>
