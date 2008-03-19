<?xml version="1.0" encoding="ISO-8859-1"?>
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

<!--===========================================================================
 ! $Header: /src/voyager/com/volantis/mcs/protocols/XHTMLBasic.java,v 1.7 2001/10/30 15:16:05 pduffin Exp $
 ! ============================================================================
 ! (c) Volantis Systems Ltd 2002. 
 ! ============================================================================
 ! Change History:
 !
 ! Date         Who             Description
 ! =========    =============== ===============================================
 ! 12-May-03    Doug            VBM:2002121803 - XSL stylesheet for use in 
 !                              transfromation integration tests.
 ! ============================================================================
 ! =========================================================================-->
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:one1="http://www.volantis.com/root1"
    xmlns:two2="http://www.volantis.com/root2"
    xmlns:three3="http://www.volantis.com/root3">

    <xsl:param name="complex-params"/>

<xsl:template match="/">
  <html>
  <xsl:if test="catalog/@xml:base">
      <xsl:attribute name="xml:base">
          <xsl:value-of select="catalog/@xml:base"/>
      </xsl:attribute>
  </xsl:if>
  <body>
  <h2>My CD Collection</h2>
    <table border="1">
      <tr bgcolor="#9acd32">

        <th align="left"><xsl:value-of select="$complex-params/one1:root1/title/@value"/></th>
        <th align="left"><xsl:value-of select="$complex-params/two2:root2/child2/name/@value"/></th>
      </tr>
      <xsl:for-each select="catalog/cd">
      <tr>
        <td><xsl:value-of select="title"/></td>
        <td><xsl:value-of select="artist"/></td>
      </tr>
      </xsl:for-each>
    </table>
  </body>
  </html>
</xsl:template>
</xsl:stylesheet>
<!--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 30-Apr-04	686/1	adrian	VBM:2004042802 Add parameter support for transforms

 02-Feb-04	564/1	adrian	VBM:2004011902 Updated base uri support

 30-Jan-04	531/1	adrian	VBM:2004011905 added context updating and context annotation support to pipeline processes

 ===========================================================================
-->
