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
  xmlns:flickr="urn:flickr:">
  <xsl:template match="/">
    <h1>Flickr gallery</h1>
    <xsl:for-each select="//photo">
      <div>
        <p><xsl:value-of select="@title"/></p>
        <object srctype="image/jpeg">
          <xsl:attribute name="src">http://farm<xsl:value-of select="@farm"/>.static.flickr.com/<xsl:value-of select="@server"/>/<xsl:value-of select="@id"/>_<xsl:value-of select="@secret"/>.jpg</xsl:attribute>
          <param name="mcs-external-label">
            <xsl:attribute name="value">
                <xsl:value-of select="@title"/>
            </xsl:attribute>
          </param>
        </object>
      </div>
    </xsl:for-each>
  </xsl:template>
</xsl:stylesheet>

