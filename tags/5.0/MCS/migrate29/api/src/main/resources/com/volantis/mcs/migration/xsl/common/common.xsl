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
 ! $Header: /src/voyager/com/volantis/mcs/migration/xsl/common/common.xsl,v 1.3 2002/08/16 15:26:47 pduffin Exp $
 ! ============================================================================
 ! (c) Volantis Systems Ltd 2002. 
 ! ============================================================================
 ! Change History:
 !
 ! Date               Who       Description
 ! =========    =============== ===============================================
 ! 28-Jun-02    Paul            VBM:2002051302 - Created to contain common
 !                              declarations.
 ! 10-Jul-02    Ian             VBM:2002031803 - Moved from migration/europa
 !                              directory for generic migration utility.
 ! 16-Aug-02    Paul            VBM:2002081514 - Removed doctype-public and
 !                              doctype-system attributes as they are set by
 !                              the migration tool.
 ! ======================================================================== -->

<!--
 ! This style sheet contains common declarations which are used in all the
 ! style sheets.
 !-->
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xalan="http://xml.apache.org/xslt">

  <!-- Strip space from around all the elements. -->
  <xsl:strip-space elements="*"/>

  <!-- Discard the mariner version attribute. -->
  <xsl:template match="/mariner/@version">
  </xsl:template>

  <!-- Copy unmatched elements and their attributes by default. -->
  <xsl:template match="*">
    <xsl:copy>
      <xsl:apply-templates select="@*"/>
      <xsl:apply-templates/>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="@*">
    <xsl:copy/>
  </xsl:template>

  <!-- Output as xml using xalan specific extensions. -->
  <xsl:output method="xml"
              indent="yes"
              xalan:indent-amount="2"/>

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
