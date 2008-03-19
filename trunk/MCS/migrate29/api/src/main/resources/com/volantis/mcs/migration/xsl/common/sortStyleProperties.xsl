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
 ! $Header: /src/voyager/com/volantis/mcs/migration/xsl/common/sortStyleProperties.xsl,v 1.2 2002/08/16 15:26:47 pduffin Exp $
 ! ============================================================================
 ! (c) Volantis Systems Ltd 2002. 
 ! ============================================================================
 ! Change History:
 !
 ! Date               Who       Description
 ! =========    =============== ===============================================
 ! 28-Jun-02    Paul            VBM:2002051302 - Created to sort the child
 !                              elements of styleProperties.
 ! 10-Jul-02    Ian             VBM:2002031803 - Moved from migration/europa
 !                              directory for generic migration utility.
 ! 16-Aug-02    Paul            VBM:2002081514 - Moved from europa directory.
 ! ======================================================================== -->

<!--
 ! Sort the children elements of styleProperties by element type so that it
 ! matches the DTD.
 !-->
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xalan="http://xml.apache.org/xslt">

  <!-- Import common declarations. -->
  <xsl:import href="../common/common.xsl"/>

  <xsl:template match="styleProperties">

    <!-- Copy the styleProperties element. -->
    <xsl:copy>
      <!-- Copy any attribute of the styleProperties element. -->
      <xsl:apply-templates select="@*"/>

      <!--
       ! Reorder the children of the styleProperties element by element type.
       !-->
      <xsl:apply-templates>
        <xsl:sort select="@element-type"/>
      </xsl:apply-templates>
    </xsl:copy>

  </xsl:template>

  <!-- Ignore the element-type attribute. -->
  <xsl:template match="styleProperties/*/@element-type"/>

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
