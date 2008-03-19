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

<!-- ==========================================================================
 ! $Header: /src/voyager/com/volantis/mcs/migration/xsl/europa/locationType.xsl,v 1.3 2002/07/11 16:04:27 ianw Exp $
 ! ============================================================================
 ! (c) Volantis Systems Ltd 2001. 
 ! ============================================================================
 ! Change History:
 !
 ! Date               Who                        Description
 ! =========    =============== ===============================================
 ! 09-Jul-02    Ian             VBM:2002070905 - Created
 ! 10-Jul-02    Ian             VBM:2002031803 - Moved from migration/europa
 !                              directory for generic migration utility.
 ! 11-Jul-02    Ian             VBM:2002070905 - Changed devices to device.
 ! ======================================================================== -->
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xalan="http://xml.apache.org/xslt">

  <!-- Import common declarations. -->
  <xsl:import href="../common/common.xsl"/>


  <!--

       Transform AssetGroup deviceType

    -->
  <xsl:template match="assetGroup">
    <xsl:copy>
      <xsl:apply-templates select="@*"/>
      <xsl:choose>
        <xsl:when test="@locationType='0'">
          <xsl:attribute name="locationType">server</xsl:attribute>
        </xsl:when>
        <xsl:when test="@locationType='1'">
          <xsl:attribute name="locationType">device</xsl:attribute>
        </xsl:when>
        <xsl:otherwise>
          <xsl:message terminate="no">ERROR: Asset Group has invalid locationType '<xsl:value-of select="@locationType"/>'</xsl:message>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates/>
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>

<!--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 ===========================================================================
-->
