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
 ! $Header: /src/voyager/com/volantis/mcs/migration/xsl/europa/themes-rev.xsl,v 1.5 2002/08/29 09:36:04 adrian Exp $
 ! ============================================================================
 ! (c) Volantis Systems Ltd 2001. 
 ! ============================================================================
 ! Change History:
 !
 ! Date               Who                        Description
 ! =========    =============== ===============================================
 ! 08-MAy-02    Ian             VBM:2002040810 - Created
 ! 17-May-02    Adrian          VBM:2002021111 - Added transformation for new
 !                              styleProperty mariner-http-method-hint
 ! 20-May-02    Adrian          VBM:2002040811 - match="deviceTheme/rule"
 !                              updated to fix bug where class attribute was
 !                              created when an idSelector was found.  Now
 !                              creates an id attribute.
 ! 23-May-02    Adrian          VBM:2002041503 - Modified transformations for
 !                              marinerMMFlashAlign & marinerMMFlashScaledAlign
 !                              as they were testing for single character
 !                              representations of the values instead of the
 !                              full names.
 ! 21-Jun-02    Adrian          VBM:2002041702 - updated textDecoration
 !                              template to match 'textDecoration' instead of
 !                              'Scale'
 ! 28-Jun-02    Paul            VBM:2002051302 - Handled conversion from
 !                              mariner-background-dynamic-visual back to
 !                              background-image-type.
 ! 10-Jul-02    Ian             VBM:2002031803 - Moved from migration/europa
 !                              directory for generic migration utility.
 ! 15-Jul-02    Ian             VBM:2002031803 - Fixed bug with backgroundColor
 !                              and marinerFormActionStyle.
 ! 29-Aug-02    Adrian          VBM:2002082803 - updated text-decoration to
 !                              correctly translate keyword(none) to 'none'
 ! ======================================================================== -->

<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xalan="http://xml.apache.org/xslt">

  <xsl:output method="xml"
              indent="yes"
              xalan:indent-amount="2"
              doctype-public="-//VOLANTIS//DTD MARINER 2.5//EN"
              doctype-system="com/volantis/mcs/repository/xml/dtd/phobos.dtd"/>

  <xsl:template match="*">
    <xsl:copy>
      <xsl:apply-templates select="@*"/>
      <xsl:apply-templates/>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="@*">
    <xsl:copy/>
  </xsl:template>


    <xsl:template name="getChartColors">
      <xsl:for-each select="chartColor">
        <xsl:call-template name="getColor"/>
        <xsl:text>;</xsl:text>
      </xsl:for-each>
    </xsl:template>

    <xsl:template name="getColor">
        <xsl:choose>
          <xsl:when test="@rgb!=''">
            <xsl:value-of select="@rgb"/>
          </xsl:when>
          <xsl:when test="@color!=''">
            <xsl:value-of select="@color"/>
          </xsl:when>
         <xsl:when test="@keyword!=''">
            <xsl:value-of select="@keyword"/>
          </xsl:when>
        </xsl:choose>
    </xsl:template>


    <xsl:template name="getFonts">
        <xsl:for-each select="*">
          <xsl:choose>
            <xsl:when test="@keyword!=''">
              <xsl:value-of select="@keyword"/>
            </xsl:when>
            <xsl:when test="@string!=''">
              <xsl:value-of select="@string"/>
            </xsl:when>
          </xsl:choose>
          <xsl:text>,</xsl:text>
        </xsl:for-each>
    </xsl:template>

    <xsl:template name="getLength">
      <xsl:choose>
        <xsl:when test="@keyword!=''">
          <xsl:value-of select="@keyword"/>
        </xsl:when>
        <xsl:when test="@length!=''">
          <xsl:value-of select="@length"/>
          <xsl:value-of select="@lengthUnits"/>
        </xsl:when>
        <xsl:when test="@percentage!=''">
          <xsl:value-of select="@percentage"/>
          <xsl:text>%</xsl:text>
        </xsl:when>
      </xsl:choose>
    </xsl:template>


    <xsl:template match="deviceTheme">
      <deviceTheme deviceName="{@deviceName}">
        <themeAttributes>
          <xsl:if test="@externalStyleSheet!=''">
            <attribute>
              <xsl:attribute name="name">externalCSS</xsl:attribute>
              <xsl:attribute name="value"><xsl:value-of select="@externalStyleSheet"/></xsl:attribute>
            </attribute>
          </xsl:if>
        </themeAttributes>
        <xsl:apply-templates/>
      </deviceTheme>
    </xsl:template>

    <xsl:template match="deviceTheme/rule">
      <style>
        <xsl:if test="simpleSelectorSequence/typeSelector/@type!=''">
          <xsl:attribute name="tag">
            <xsl:value-of select="simpleSelectorSequence/typeSelector/@type"/>
            <xsl:for-each select="simpleSelectorSequence/pseudoClassSelector">
              <xsl:text>:</xsl:text>
              <xsl:value-of select="@identifier"/>
              <xsl:if test="@parameter!=''">
                <xsl:text>(</xsl:text>
                <xsl:value-of select="@parameter"/>
                <xsl:text>)</xsl:text>
              </xsl:if>
            </xsl:for-each>
            <xsl:if test="simpleSelectorSequence/pseudoElementSelector/@identifier!=''">
              <xsl:text>:</xsl:text>
              <xsl:value-of select="simpleSelectorSequence/pseudoElementSelector/@identifier"/>
              <xsl:if test="simpleSelectorSequence/pseudoElementSelector/@parameter!=''">
                <xsl:text>(</xsl:text>
                  <xsl:value-of select="simpleSelectorSequence/pseudoElementSelector/@parameter"/>
                  <xsl:text>)</xsl:text>
              </xsl:if>
            </xsl:if>
          </xsl:attribute>
        </xsl:if>
        <xsl:if test="simpleSelectorSequence/classSelector/@class!=''">
          <xsl:attribute name="class">
            <xsl:value-of select="simpleSelectorSequence/classSelector/@class"/>
          </xsl:attribute>
        </xsl:if>
        <xsl:if test="simpleSelectorSequence/idSelector/@id!=''">
          <xsl:attribute name="id">
            <xsl:value-of select="simpleSelectorSequence/idSelector/@id"/>
          </xsl:attribute>
        </xsl:if>
        <xsl:apply-templates/>
      </style>
    </xsl:template>

    <xsl:template match="simpleSelectorSequence"/>
    <xsl:template match="styleProperties">
      <xsl:apply-templates/>
    </xsl:template>

      <!--

        Transform action-image

        -->
     <xsl:template match="styleProperties/marinerFormActionImage">
       <attribute>
          <xsl:attribute name="name">action-image</xsl:attribute>
          <xsl:attribute name="value"><xsl:value-of select="@marinerComponentURI"/></xsl:attribute>
       </attribute>
    </xsl:template>

    <!--
        Transform action-ui

        -->
     <xsl:template match="styleProperties/marinerFormActionStyle">
       <attribute>
          <xsl:attribute name="name">action-ui</xsl:attribute>
         <xsl:choose>
           <xsl:when test="@keyword='default'">
             <xsl:attribute name="value">button</xsl:attribute>
           </xsl:when>
           <xsl:when test="@keyword='image'">
             <xsl:attribute name="value">image</xsl:attribute>
           </xsl:when>
         </xsl:choose>
      </attribute>
    </xsl:template>


      <!--

        Transform adornments

        -->
     <xsl:template match="styleProperties/marinerChartAdornments">
       <attribute>
          <xsl:attribute name="name">adornments</xsl:attribute>
         <xsl:choose>
           <xsl:when test="@keyword='all'">
             <xsl:attribute name="value">true</xsl:attribute>
           </xsl:when>
           <xsl:when test="@keyword='none'">
             <xsl:attribute name="value">false</xsl:attribute>
           </xsl:when>
         </xsl:choose>
      </attribute>
    </xsl:template>


    <!--

        Transform align

      -->
     <xsl:template match="styleProperties/marinerMMFlashAlign">
       <attribute>
          <xsl:attribute name="name">align</xsl:attribute>
         <xsl:choose>
           <xsl:when test="@keyword='bottom'">
             <xsl:attribute name="value">bottom</xsl:attribute>
           </xsl:when>
           <xsl:when test="@keyword='center'">
             <xsl:attribute name="value">center</xsl:attribute>
           </xsl:when>
           <xsl:when test="@keyword='left'">
             <xsl:attribute name="value">left</xsl:attribute>
           </xsl:when>
           <xsl:when test="@keyword='right'">
             <xsl:attribute name="value">right</xsl:attribute>
           </xsl:when>
           <xsl:when test="@keyword='top'">
             <xsl:attribute name="value">top</xsl:attribute>
           </xsl:when>
         </xsl:choose>
      </attribute>
    </xsl:template>

    <!--

        Transform background-attachment

      -->
    <xsl:template match="styleProperties/backgroundAttachment">
      <attribute>
        <xsl:attribute name="name">background-attachment</xsl:attribute>
        <xsl:choose>
          <xsl:when test="@keyword!=''">
            <xsl:attribute name="value"><xsl:value-of select="@keyword"/></xsl:attribute>
          </xsl:when>
        </xsl:choose>
      </attribute>
    </xsl:template>


    <!--

        Transform background-image

      -->
    <xsl:template match="styleProperties/backgroundImage">
      <attribute>
         <xsl:attribute name="name">background-image</xsl:attribute>
         <xsl:attribute name="value"><xsl:value-of select="@marinerComponentURI"/></xsl:attribute>
      </attribute>
    </xsl:template>

    <!--

        Transform background-color

      -->
    <xsl:template match="styleProperties/backgroundColor">
      <attribute>
         <xsl:attribute name="name">background-color</xsl:attribute>
         <xsl:attribute name="value"><xsl:call-template name="getColor"/></xsl:attribute>
      </attribute>
    </xsl:template>

    <!--

        Transform background-image-type

       -->
    <xsl:template match="styleProperties/marinerBackgroundDynamicVisual">
      <attribute>
         <xsl:attribute name="name">background-image</xsl:attribute>
         <xsl:attribute name="value"><xsl:value-of select="@marinerComponentURI"/></xsl:attribute>
      </attribute>
      <attribute name="background-type" value="dynamic-visual"/>
    </xsl:template>

      <!--

        Transform background-position

        -->
    <xsl:template match="styleProperties/backgroundPosition">
      <attribute>
         <xsl:attribute name="name">background-position</xsl:attribute>
        <xsl:attribute name="value">
          <xsl:for-each select="backgroundXPosition">
            <xsl:call-template name="getLength"/>
          </xsl:for-each>
          <xsl:text> </xsl:text>
          <xsl:for-each select="backgroundYPosition">
            <xsl:call-template name="getLength"/>
          </xsl:for-each>
        </xsl:attribute>
      </attribute>
    </xsl:template>


    <!--

        Transform background-repeat

      -->
    <xsl:template match="styleProperties/backgroundRepeat">
      <attribute>
         <xsl:attribute name="name">background-repeat</xsl:attribute>
         <xsl:attribute name="value"><xsl:value-of select="@keyword"/></xsl:attribute>
      </attribute>
    </xsl:template>

    <!--

        Transform border-color

      -->
    <xsl:template match="styleProperties/borderBottomColor">
      <attribute>
        <xsl:attribute name="name">border-color</xsl:attribute>
        <xsl:attribute name="value">
          <xsl:call-template name="getColor"/>
        </xsl:attribute>
      </attribute>
    </xsl:template>

    <xsl:template match="styleProperties/borderLeftColor"/>
    <xsl:template match="styleProperties/borderRightColor"/>
    <xsl:template match="styleProperties/borderTopColor"/>

    <!--

        Transform border-style

      -->
    <xsl:template match="styleProperties/borderBottomStyle">
      <attribute>
        <xsl:attribute name="name">border-style</xsl:attribute>
        <xsl:attribute name="value"><xsl:value-of select="@keyword"/></xsl:attribute>
      </attribute>
    </xsl:template>

    <xsl:template match="styleProperties/borderLeftStyle"/>
    <xsl:template match="styleProperties/borderRightStyle"/>
    <xsl:template match="styleProperties/borderTopStyle"/>

    <!--

        Transform border-bottom-width

      -->
    <xsl:template match="styleProperties/borderBottomWidth">
      <attribute>
        <xsl:attribute name="name">border-bottom-width</xsl:attribute>
        <xsl:attribute name="value">
          <xsl:call-template name="getLength"/>
        </xsl:attribute>
      </attribute>
    </xsl:template>
    <!--

        Transform border-left-width

      -->
    <xsl:template match="styleProperties/borderLeftWidth">
      <attribute>
        <xsl:attribute name="name">border-left-width</xsl:attribute>
        <xsl:attribute name="value">
          <xsl:call-template name="getLength"/>
        </xsl:attribute>
      </attribute>
    </xsl:template>

    <!--

        Transform border-right-width

      -->
    <xsl:template match="styleProperties/borderRightWidth">
      <attribute>
        <xsl:attribute name="name">border-right-width</xsl:attribute>
        <xsl:attribute name="value">
          <xsl:call-template name="getLength"/>
        </xsl:attribute>
      </attribute>
    </xsl:template>

    <!--

        Transform border-top-width

      -->
    <xsl:template match="styleProperties/borderTopWidth">
      <attribute>
        <xsl:attribute name="name">border-top-width</xsl:attribute>
        <xsl:attribute name="value">
          <xsl:call-template name="getLength"/>
        </xsl:attribute>
      </attribute>
    </xsl:template>

    <!--

        Transform color

      -->
    <xsl:template match="styleProperties/color">
      <attribute>
        <xsl:attribute name="name">color</xsl:attribute>
        <xsl:attribute name="value">
          <xsl:call-template name="getColor"/>
        </xsl:attribute>
      </attribute>
    </xsl:template>

    <!--

        Transform columns

      -->
    <xsl:template match="styleProperties/marinerColumns">
      <attribute>
        <xsl:attribute name="name">columns</xsl:attribute>
        <xsl:attribute name="value"><xsl:value-of select="@integer"/></xsl:attribute>
      </attribute>
    </xsl:template>

    <!--

        Transform font-family

      -->
    <xsl:template match="styleProperties/fontFamily">
      <attribute>
        <xsl:attribute name="name">font-family</xsl:attribute>
        <xsl:variable name="fonts">
          <xsl:call-template name="getFonts"/>
        </xsl:variable>
        <xsl:attribute name="value">
          <xsl:value-of select="substring($fonts,1,string-length($fonts)-1)"/>
        </xsl:attribute>
      </attribute>
    </xsl:template>

    <!--

        Transform font-size

      -->
    <xsl:template match="styleProperties/fontSize">
      <attribute>
        <xsl:attribute name="name">font-size</xsl:attribute>
        <xsl:attribute name="value">
          <xsl:call-template name="getLength"/>
        </xsl:attribute>
      </attribute>
    </xsl:template>

    <!--

        Transform font-style

      -->
    <xsl:template match="styleProperties/fontStyle">
      <attribute>
        <xsl:attribute name="name">font-style</xsl:attribute>
        <xsl:attribute name="value">
          <xsl:value-of select="@keyword"/>
        </xsl:attribute>
      </attribute>
    </xsl:template>

    <!--

        Transform font-variant

      -->
    <xsl:template match="styleProperties/fontVariant">
      <attribute>
        <xsl:attribute name="name">font-variant</xsl:attribute>
        <xsl:attribute name="value">
          <xsl:value-of select="@keyword"/>
        </xsl:attribute>
      </attribute>
    </xsl:template>

    <!--

        Transform font-weight

      -->
    <xsl:template match="styleProperties/fontWeight">
      <attribute>
        <xsl:attribute name="name">font-weight</xsl:attribute>
        <xsl:choose>
          <xsl:when test="@keyword='weight-100'">
            <xsl:attribute name="value">100</xsl:attribute>
          </xsl:when>
          <xsl:when test="@keyword='weight-200'">
            <xsl:attribute name="value">200</xsl:attribute>
          </xsl:when>
          <xsl:when test="@keyword='weight-300'">
            <xsl:attribute name="value">300</xsl:attribute>
          </xsl:when>
          <xsl:when test="@keyword='weight-400'">
            <xsl:attribute name="value">400</xsl:attribute>
          </xsl:when>
          <xsl:when test="@keyword='weight-500'">
            <xsl:attribute name="value">500</xsl:attribute>
          </xsl:when>
          <xsl:when test="@keyword='weight-600'">
            <xsl:attribute name="value">600</xsl:attribute>
          </xsl:when>
          <xsl:when test="@keyword='weight-700'">
            <xsl:attribute name="value">700</xsl:attribute>
          </xsl:when>
          <xsl:when test="@keyword='weight-800'">
            <xsl:attribute name="value">800</xsl:attribute>
          </xsl:when>
          <xsl:when test="@keyword='weight-900'">
            <xsl:attribute name="value">900</xsl:attribute>
          </xsl:when>
          <xsl:otherwise>
            <xsl:attribute name="value">
              <xsl:value-of select="@keyword"/>
            </xsl:attribute>
          </xsl:otherwise>
        </xsl:choose>
      </attribute>
    </xsl:template>

    <!--

        Transform foreground-colors

      -->
    <xsl:template match="styleProperties/marinerChartForegroundColors">
      <attribute>
        <xsl:attribute name="name">foreground-colors</xsl:attribute>
        <xsl:variable name="colors">
          <xsl:call-template name="getChartColors"/>
        </xsl:variable>
        <xsl:attribute name="value">
          <xsl:value-of select="substring($colors,1,string-length($colors)-1)"/>
        </xsl:attribute>
      </attribute>
    </xsl:template>


    <!--

        Transform grid-color

      -->
    <xsl:template match="styleProperties/marinerChartGridColor">
      <attribute>
        <xsl:attribute name="name">grid-color</xsl:attribute>
        <xsl:attribute name="value">
          <xsl:call-template name="getColor"/>
        </xsl:attribute>
      </attribute>
    </xsl:template>

    <!--

        Transform height

      -->
    <xsl:template match="styleProperties/height">
      <attribute>
        <xsl:attribute name="name">height</xsl:attribute>
        <xsl:attribute name="value">
          <xsl:call-template name="getLength"/>
        </xsl:attribute>
      </attribute>
    </xsl:template>

    <!--

        Transform heighthint

      -->
    <xsl:template match="styleProperties/marinerChartHeight">
      <attribute>
        <xsl:attribute name="name">heighthint</xsl:attribute>
        <xsl:attribute name="value">
          <xsl:value-of select="@percentage"/>
        </xsl:attribute>
      </attribute>
    </xsl:template>






    <!--

        Transform label-values

      -->
    <xsl:template match="styleProperties/marinerChatLabelValues">
      <attribute>
        <xsl:attribute name="name">label-values</xsl:attribute>
        <xsl:attribute name="value">
         <xsl:choose>
           <xsl:when test="@keyword='yes'">
             <xsl:attribute name="value">true</xsl:attribute>
           </xsl:when>
           <xsl:when test="@keyword='no'">
             <xsl:attribute name="value">false</xsl:attribute>
           </xsl:when>
         </xsl:choose>
        </xsl:attribute>
      </attribute>
    </xsl:template>



    <!--

        Transform letter-spacing

      -->
    <xsl:template match="styleProperties/letterSpacing">
      <attribute>
        <xsl:attribute name="name">letter-spacing</xsl:attribute>
        <xsl:attribute name="value">
          <xsl:value-of select="@keyword"/>
        </xsl:attribute>
      </attribute>
    </xsl:template>

    <!--

        Transform linegap

      -->
    <xsl:template match="styleProperties/marinerLineGap">
      <attribute>
        <xsl:attribute name="name">linegap</xsl:attribute>
        <xsl:attribute name="value">
          <xsl:call-template name="getLength"/>
        </xsl:attribute>
      </attribute>
    </xsl:template>

    <!--

        Transform line-height

      -->
    <xsl:template match="styleProperties/lineHeight">
      <attribute>
        <xsl:attribute name="name">line-height</xsl:attribute>
        <xsl:attribute name="value">
          <xsl:call-template name="getLength"/>
        </xsl:attribute>
      </attribute>
    </xsl:template>




    <!--

        Transform link-ui

      -->
    <xsl:template match="styleProperties/marinerLinkStyle">
      <attribute>
        <xsl:attribute name="name">link-ui</xsl:attribute>
        <xsl:choose>
          <xsl:when test="@keyword='default'">
            <xsl:attribute name="value">none</xsl:attribute>
          </xsl:when>
          <xsl:when test="@keyword='button'">
            <xsl:attribute name="value">button</xsl:attribute>
          </xsl:when>
        </xsl:choose>
      </attribute>
    </xsl:template>


    <!--

        Transform list-caption-align

      -->
    <xsl:template match="styleProperties/marinerSelectionListOptionLayout">
      <attribute>
        <xsl:attribute name="name">list-caption-align</xsl:attribute>
        <xsl:choose>
          <xsl:when test="@keyword='caption-first'">
            <xsl:attribute name="value">left</xsl:attribute>
          </xsl:when>
          <xsl:when test="@keyword='control-first'">
            <xsl:attribute name="value">right</xsl:attribute>
          </xsl:when>
        </xsl:choose>
      </attribute>
    </xsl:template>


      <!--

        Transform list-style-image

        -->
     <xsl:template match="styleProperties/listStyleImage">
       <attribute>
          <xsl:attribute name="name">list-style-image</xsl:attribute>
          <xsl:attribute name="value"><xsl:value-of select="@marinerComponentURI"/></xsl:attribute>
       </attribute>
    </xsl:template>




    <!--

        Transform list-style-position

      -->
    <xsl:template match="styleProperties/listStylePosition">
      <attribute>
        <xsl:attribute name="name">list-style-position</xsl:attribute>
        <xsl:attribute name="value">
          <xsl:value-of select="@keyword"/>
        </xsl:attribute>
      </attribute>
    </xsl:template>

    <!--

        Transform list-style-type

      -->
    <xsl:template match="styleProperties/listStyleType">
      <attribute>
        <xsl:attribute name="name">list-style-type</xsl:attribute>
        <xsl:attribute name="value">
          <xsl:value-of select="@keyword"/>
        </xsl:attribute>
      </attribute>
    </xsl:template>

    <!--

        Transform list-ui

      -->
    <xsl:template match="styleProperties/marinerSelectionListStyle">
      <attribute>
        <xsl:attribute name="name">list-ui</xsl:attribute>
        <xsl:choose>
          <xsl:when test="@keyword='default'">
            <xsl:attribute name="value">menu</xsl:attribute>
          </xsl:when>
          <xsl:when test="@keyword='controls'">
            <xsl:attribute name="value">checkbox</xsl:attribute>
          </xsl:when>
        </xsl:choose>
      </attribute>
    </xsl:template>

    <!--

        Transform margin-bottom

      -->
    <xsl:template match="styleProperties/marginBottom">
      <attribute>
        <xsl:attribute name="name">margin-bottom</xsl:attribute>
        <xsl:attribute name="value">
          <xsl:call-template name="getLength"/>
        </xsl:attribute>
      </attribute>
    </xsl:template>

    <!--

        Transform margin-left

      -->
    <xsl:template match="styleProperties/marginLeft">
      <attribute>
        <xsl:attribute name="name">margin-left</xsl:attribute>
        <xsl:attribute name="value">
          <xsl:call-template name="getLength"/>
        </xsl:attribute>
      </attribute>
    </xsl:template>

    <!--

        Transform margin-right

      -->
    <xsl:template match="styleProperties/marginRight">
      <attribute>
        <xsl:attribute name="name">margin-right</xsl:attribute>
        <xsl:attribute name="value">
          <xsl:call-template name="getLength"/>
        </xsl:attribute>
      </attribute>
    </xsl:template>

    <!--

        Transform margin-top

      -->
    <xsl:template match="styleProperties/marginTop">
      <attribute>
        <xsl:attribute name="name">margin-top</xsl:attribute>
        <xsl:attribute name="value">
          <xsl:call-template name="getLength"/>
        </xsl:attribute>
      </attribute>
    </xsl:template>

    <!--

        Transform mariner-http-method-hint

      -->
    <xsl:template match="styleProperties/marinerHttpMethodHint">
      <attribute>
        <xsl:attribute name="name">mariner-http-method-hint</xsl:attribute>
        <xsl:attribute name="value"><xsl:value-of select="@keyword"/></xsl:attribute>
      </attribute>
    </xsl:template>

    <!--

        Transform padding-left

      -->
    <xsl:template match="styleProperties/paddingLeft">
      <attribute>
        <xsl:attribute name="name">padding-left</xsl:attribute>
        <xsl:attribute name="value">
          <xsl:call-template name="getLength"/>
        </xsl:attribute>
      </attribute>
    </xsl:template>

    <!--

        Transform padding-right

      -->
    <xsl:template match="styleProperties/paddingRight">
      <attribute>
        <xsl:attribute name="name">padding-right</xsl:attribute>
        <xsl:attribute name="value">
          <xsl:call-template name="getLength"/>
        </xsl:attribute>
      </attribute>
    </xsl:template>

    <!--

        Transform padding-top

      -->
    <xsl:template match="styleProperties/paddingTop">
      <attribute>
        <xsl:attribute name="name">padding-top</xsl:attribute>
        <xsl:attribute name="value">
          <xsl:call-template name="getLength"/>
        </xsl:attribute>
      </attribute>
    </xsl:template>

    <!--

        Transform padding-bottom

      -->
    <xsl:template match="styleProperties/paddingBottom">
      <attribute>
        <xsl:attribute name="name">padding-bottom</xsl:attribute>
        <xsl:attribute name="value">
          <xsl:call-template name="getLength"/>
        </xsl:attribute>
      </attribute>
    </xsl:template>

    <!--

        Transform paragap

      -->
    <xsl:template match="styleProperties/marinerParagraphGap">
      <attribute>
        <xsl:attribute name="name">paragap</xsl:attribute>
        <xsl:attribute name="value">
          <xsl:call-template name="getLength"/>
        </xsl:attribute>
      </attribute>
    </xsl:template>

    <!--

        Transform quality

      -->
    <xsl:template match="styleProperties/marinerMMFlashQuality">
      <attribute>
        <xsl:attribute name="name">quality</xsl:attribute>
        <xsl:attribute name="value">
          <xsl:value-of select="@keyword"/>
        </xsl:attribute>
      </attribute>
    </xsl:template>

    <!--

        Transform rowgap

      -->
    <xsl:template match="styleProperties/marinerRowGap">
      <attribute>
        <xsl:attribute name="name">rowgap</xsl:attribute>
        <xsl:attribute name="value">
          <xsl:call-template name="getLength"/>
        </xsl:attribute>
      </attribute>
    </xsl:template>

    <!--

        Transform rows

      -->
    <xsl:template match="styleProperties/marinerRows">
      <attribute>
        <xsl:attribute name="name">rows</xsl:attribute>
        <xsl:attribute name="value">
          <xsl:value-of select="@integer"/>
        </xsl:attribute>
      </attribute>
    </xsl:template>

    <!--

        Transform salign

      -->
    <xsl:template match="styleProperties/marinerMMFlashScaledAlign">
      <attribute>
        <xsl:attribute name="name">salign</xsl:attribute>
        <xsl:choose>
          <xsl:when test="marinerMMFlashYPosition/@keyword='bottom'">
            <xsl:choose>
              <xsl:when test="marinerMMFlashXPosition/@keyword='left'">
                <xsl:attribute name="value">bottomleft</xsl:attribute>
              </xsl:when>
              <xsl:when test="marinerMMFlashXPosition/@keyword='right'">
                <xsl:attribute name="value">bottomright</xsl:attribute>
              </xsl:when>
              <xsl:otherwise>
                <xsl:attribute name="value">bottom</xsl:attribute>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:when>
          <xsl:when test="marinerMMFlashYPosition/@keyword='top'">
            <xsl:choose>
              <xsl:when test="marinerMMFlashXPosition/@keyword='left'">
                <xsl:attribute name="value">topleft</xsl:attribute>
              </xsl:when>
              <xsl:when test="marinerMMFlashXPosition/@keyword='right'">
                <xsl:attribute name="value">topright</xsl:attribute>
              </xsl:when>
              <xsl:otherwise>
                <xsl:attribute name="value">top</xsl:attribute>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:when>
          <xsl:when test="marinerMMFlashYPosition/@keyword='center'">
            <xsl:attribute name="value">center</xsl:attribute>
          </xsl:when>
           <xsl:when test="marinerMMFlashXPosition/@keyword='left'">
             <xsl:attribute name="value">left</xsl:attribute>
           </xsl:when>
           <xsl:when test="marinerMMFlashXPosition/@keyword='right'">
             <xsl:attribute name="value">right</xsl:attribute>
           </xsl:when>
         </xsl:choose>
      </attribute>
    </xsl:template>

    <!--

        Transform scale

      -->
    <xsl:template match="styleProperties/marinerMMFlashScale">
      <attribute>
        <xsl:attribute name="name">scale</xsl:attribute>
        <xsl:choose>
          <xsl:when test="@keyword='exact-fit'">
            <xsl:attribute name="value">exactfit</xsl:attribute>
          </xsl:when>
          <xsl:when test="@keyword='no-border'">
            <xsl:attribute name="value">noborder</xsl:attribute>
          </xsl:when>
          <xsl:when test="@keyword='show-all'">
            <xsl:attribute name="value">showall</xsl:attribute>
          </xsl:when>
        </xsl:choose>
      </attribute>
    </xsl:template>

    <!--

        Transform text-align

      -->
    <xsl:template match="styleProperties/textAlign">
      <attribute>
        <xsl:attribute name="name">text-align</xsl:attribute>
        <xsl:attribute name="value">
          <xsl:value-of select="@keyword"/>
        </xsl:attribute>
      </attribute>
    </xsl:template>

    <!--

        Transform text-decoration

     -->
    <xsl:template match="styleProperties/textDecoration">
      <attribute>
        <xsl:attribute name="name">text-decoration</xsl:attribute>
        <xsl:choose>
          <xsl:when test="@underline='true'">
            <xsl:attribute name="value">underline</xsl:attribute>
          </xsl:when>
          <xsl:when test="@overline='true'">
            <xsl:attribute name="value">overline</xsl:attribute>
          </xsl:when>
          <xsl:when test="@line-through='true'">
            <xsl:attribute name="value">line-through</xsl:attribute>
          </xsl:when>
          <xsl:when test="@blink='true'">
            <xsl:attribute name="value">blink</xsl:attribute>
          </xsl:when>
          <xsl:when test="@keyword='none'">
            <xsl:attribute name="value">none</xsl:attribute>
          </xsl:when>
        </xsl:choose>
      </attribute>
    </xsl:template>

    <!--

        Transform text-indent

      -->
    <xsl:template match="styleProperties/textIndent">
      <attribute>
        <xsl:attribute name="name">text-indent</xsl:attribute>
        <xsl:attribute name="value">
          <xsl:call-template name="getLength"/>
        </xsl:attribute>
      </attribute>
    </xsl:template>


    <!--

        Transform text-transform

      -->
    <xsl:template match="styleProperties/textTransform">
      <attribute>
        <xsl:attribute name="name">text-transform</xsl:attribute>
        <xsl:attribute name="value">
          <xsl:value-of select="@keyword"/>
        </xsl:attribute>
      </attribute>
    </xsl:template>

    <!--

        Transform vertical-align

      -->
    <xsl:template match="styleProperties/verticalAlign">
      <attribute>
        <xsl:attribute name="name">vertical-align</xsl:attribute>
        <xsl:attribute name="value">
          <xsl:call-template name="getLength"/>
        </xsl:attribute>
      </attribute>
    </xsl:template>

    <!--

        Transform view

      -->
    <xsl:template match="styleProperties/marinerLinkMedia">
      <attribute>
        <xsl:attribute name="name">view</xsl:attribute>
        <xsl:attribute name="value">
          <xsl:value-of select="@keyword"/>
        </xsl:attribute>
      </attribute>
    </xsl:template>

    <!--

        Transform volantisOrientation

      -->
    <xsl:template match="styleProperties/marinerMenuOrientation">
      <attribute>
        <xsl:attribute name="name">volantisOrientation</xsl:attribute>
        <xsl:attribute name="value">
          <xsl:value-of select="@keyword"/>
        </xsl:attribute>
      </attribute>
    </xsl:template>


    <!--

        Transform width

      -->
    <xsl:template match="styleProperties/width">
      <attribute>
        <xsl:attribute name="name">width</xsl:attribute>
        <xsl:attribute name="value">
          <xsl:call-template name="getLength"/>
        </xsl:attribute>
      </attribute>
    </xsl:template>

    <!--

        Transform widthhint

      -->
    <xsl:template match="styleProperties/marinerChartWidth">
      <attribute>
        <xsl:attribute name="name">widthhint</xsl:attribute>
        <xsl:attribute name="value">
          <xsl:value-of select="@percentage"/>
        </xsl:attribute>
      </attribute>
    </xsl:template>

    <!--

        Transform white-space

      -->
    <xsl:template match="styleProperties/whiteSpace">
      <attribute>
        <xsl:attribute name="name">white-space</xsl:attribute>
        <xsl:attribute name="value">
          <xsl:value-of select="@keyword"/>
        </xsl:attribute>
      </attribute>
    </xsl:template>

    <!--

        Transform wmode

      -->
    <xsl:template match="styleProperties/marinerMMFlashWindowsMode">
      <attribute>
        <xsl:attribute name="name">wmode</xsl:attribute>
        <xsl:attribute name="value">
          <xsl:value-of select="@keyword"/>
        </xsl:attribute>
      </attribute>
    </xsl:template>

    <!--

        Transform word-spacing

      -->
    <xsl:template match="styleProperties/wordSpacing">
      <attribute>
        <xsl:attribute name="name">word-spacing</xsl:attribute>
        <xsl:attribute name="value">
          <xsl:call-template name="getLength"/>
        </xsl:attribute>
      </attribute>
    </xsl:template>

    <!--

        Transform x-angle

      -->
    <xsl:template match="styleProperties/marinerChartXAxisAngle">
      <attribute>
        <xsl:attribute name="name">x-angle</xsl:attribute>
        <xsl:attribute name="value">
          <xsl:value-of select="@angle"/>
        </xsl:attribute>
      </attribute>
    </xsl:template>

    <!--

        Transform y-angle

      -->
    <xsl:template match="styleProperties/marinerChartYAxisAngle">
      <attribute>
        <xsl:attribute name="name">y-angle</xsl:attribute>
        <xsl:attribute name="value">
          <xsl:value-of select="@angle"/>
        </xsl:attribute>
      </attribute>
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
