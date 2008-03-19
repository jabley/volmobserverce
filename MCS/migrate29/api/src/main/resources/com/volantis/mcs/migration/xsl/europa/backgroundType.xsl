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
 ! $Header: /src/voyager/com/volantis/mcs/migration/xsl/europa/backgroundType.xsl,v 1.2 2002/07/11 10:46:12 ianw Exp $
 ! ============================================================================
 ! (c) Volantis Systems Ltd 2002. 
 ! ============================================================================
 ! Change History:
 !
 ! Date               Who       Description
 ! =========    =============== ===============================================
 ! 28-Jun-02    Paul            VBM:2002051302 - Created to handle the
 !                              conversion of mariner-background-type properly.
 ! 10-Jul-02    Ian             VBM:2002031803 - Moved from migration/europa
 !                              directory for generic migration utility.
 ! ======================================================================== -->

<!--
 ! This style sheet uses the value of marinerBackgroundType to control whether
 ! backgroundImage is converted into marinerBackgroundDynamicVisual or not.
 ! It must be run after the themes.xsl style sheet.
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
       ! Call a template to handle the marinerBackgroundType and
       ! backgroundImage elements.
       !-->
      <xsl:call-template name="convertMarinerBackgroundType">
        <xsl:with-param name="type"
                        select="./marinerBackgroundType/@keyword"/>
        <xsl:with-param name="uri"
                        select="./backgroundImage/@marinerComponentURI"/>
      </xsl:call-template>
      <xsl:apply-templates/>
    </xsl:copy>
  </xsl:template>

  <!--
   ! Ignore all marinerBackgroundType elements.
   !-->
  <xsl:template match="styleProperties/marinerBackgroundType"/>

  <!--
   ! Ignore all backgroundImage elements.
   !-->
  <xsl:template match="styleProperties/backgroundImage"/>

  <!--
   ! If the styleProperties element contains a marinerBackgroundType
   ! or a backgroundImage then we need to convert them into either
   ! a backgroundImage or a marinerBackgroundDynamicVisual element
   ! depending on the value of the marinerBackgroundType.
   !
   ! This template is called for every instance of styleProperties which
   ! may or may not contain a marinerBackgroundType, or a backgroundImage
   ! so we need to check before we generate an element.
   !-->
  <xsl:template name="convertMarinerBackgroundType">
    <xsl:param name="type" select="''"/>
    <xsl:param name="uri" select="''"/>

    <!--
     ! Only generate a backgroundImage or marinerBackgroundDynamicVisual
     ! element if a uri has been specified, otherwise do nothing.
     !-->
    <xsl:if test="$uri!=''">
      <xsl:choose>
        <xsl:when test="$type='dynamic-visual'">
          <marinerBackgroundDynamicVisual>
            <xsl:attribute name="marinerComponentURI">
              <xsl:value-of select="$uri"/>
            </xsl:attribute>
          </marinerBackgroundDynamicVisual>
        </xsl:when>
        <xsl:otherwise>
          <backgroundImage>
            <xsl:attribute name="marinerComponentURI">
              <xsl:value-of select="$uri"/>
            </xsl:attribute>
          </backgroundImage>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:if>
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
