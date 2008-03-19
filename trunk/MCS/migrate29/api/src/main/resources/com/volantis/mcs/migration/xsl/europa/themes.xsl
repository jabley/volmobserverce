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
 ! $Header: /src/voyager/com/volantis/mcs/migration/xsl/europa/themes.xsl,v 1.8 2002/11/01 16:33:55 geoff Exp $
 ! ============================================================================
 ! (c) Volantis Systems Ltd 2001. 
 ! ============================================================================
 ! Change History:
 !
 ! Date               Who                        Description
 ! =========    =============== ===============================================
 ! 28-Apr-02    Ian             VBM:2002040806 - Created
 ! 29-Apr-02    Ian             VBM:2002040806 - Renamed marinerTextInputRows
 !                              to marinerRows and marinerTextInputColumns
 !                              to marinerColumns.
 ! 29-Apr-02    Ian             VBM:2002040806 - Added missing attributes.
 ! 30-Apr-02    Ian             VBM:2002040806 - Fixed numerous bugs.
 ! 01-May-02    Ian             VBM:2002040806 - Support pseudoClass/Elements.
 ! 01-May-02    Ian             VBM:2002040806 - Add support for external CSS.
 ! 01-May-02    Ian             VBM:2002040806 - Convert form elements in style
 !                              tags to xf* and junk all styles with invalid
 !                              tag values.
 ! 01-May-02    Ian             VBM:2002040806 - Added fixup for table-align.
 ! 02-May-02    Ian             VBM:2002040806 - Fixed vetricalAlign.
 ! 03-May-02    Ian             VBM:2002040806 - Fixup for sans_serif.
 ! 03-May-02    Ian             VBM:2002040806 - Improved error checking.
 ! 03-May-02    Ian             VBM:2002040806 - Changed universalSelector.
 ! 17-May-02    Adrian          VBM:2002021111 - Added transformation for new
 !                              styleProperty mariner-http-method-hint
 ! 23-May-02    Adrian          VBM:2002041503 - Modified transformations for
 !                              marinerMMFlashAlign & marinerMMFlashScaledAlign
 !                              as they were creating single character
 !                              representations of the values instead of the
 !                              full names.
 ! 25-Jun-02    Ian             VBM:2002062406 - Changed font-family processing
 !                              to drop null font-family.
 ! 28-Jun-02    Paul            VBM:2002051302 - Use common declarations.
 ! 08-Jul-02    Adrian          VBM:2002070101 - Updated... list-style-type to
 !                              cope with the typo 'cirle' from deimos,
 !                              font-size to cope with typo 'smalled', and
 !                              salign to cope with truncated values eg 'bl'
 !                              instead of 'bottomleft'.
 ! 10-Jul-02    Ian             VBM:2002031803 - Moved from migration/europa
 !                              directory for generic migration utility.
 ! 06-Aug-02    Byron           VBM:2002073107 - Modified getChartColor to 
 !                              not import 'transparent' colours to Europa
 ! 28-Aug-02    Byron           VBM:2002082802 - Modified 'bottom right'
 !                              in getPosition to correctly update to bottom
 !                              and right (not left)
 ! 29-Aug-02    Adrian          VBM:2002082803 - updated text-decoration to
 !                              correctly translate 'none' to keyword 'none'
 ! 19-Aug-02    Adrian          VBM:2002091004 - Updated template doRule to
 !                              filter out rules with class or id selectors
 !                              with space chars or prefixed by digit or hyphen
 ! 30-Oct-02    Geoff           VBM:2002082910 - updated getWidth and getLength
 !                              so that border percentages are fixed to px.
 ! 31-Oct-02    Geoff           VBM:2002083009 - added getPositiveLengthTag and
 !                              changed getWidth to getWidthTag to ignore tags
 !                              with negative values when invalid.
 ! ======================================================================== -->
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xalan="http://xml.apache.org/xslt">

  <!-- Import common declarations. -->
  <xsl:import href="../common/common.xsl"/>

  <!--

        Utility templates

    -->

  <xsl:template name="getChartColor">
    <xsl:param name="value"  select="'transparent'"/>
    <xsl:variable name="color" select="normalize-space($value)"/>

    
    <xsl:if test="contains($color,';')">
       <xsl:if test="substring-before($color,';')!='transparent'">
          <chartColor>
            <xsl:call-template name="getColor">
              <xsl:with-param name="value" select="substring-before($color,';')"/>
            </xsl:call-template>
         </chartColor>
      </xsl:if>
      <xsl:call-template name="getChartColor">
        <xsl:with-param name="value" select="substring-after($color,';')"/>
      </xsl:call-template>
    </xsl:if>
    <xsl:if test="not(contains($color,';'))">
       <xsl:if test="$color!='transparent'">
         <chartColor>
           <xsl:call-template name="getColor">
             <xsl:with-param name="value" select="$color"/>
           </xsl:call-template>
         </chartColor>
      </xsl:if>
    </xsl:if>
  </xsl:template>

  <xsl:template name="getColor">
    <xsl:param name="value"  select="'#'"/>
    <xsl:choose>
      <xsl:when test="$value='transparent'">
        <xsl:attribute name="keyword">transparent</xsl:attribute>
      </xsl:when>
      <xsl:when test="substring($value,1,1)='#'">
        <xsl:attribute name="rgb"><xsl:value-of select="$value"/></xsl:attribute>
      </xsl:when>
      <xsl:otherwise>
        <xsl:attribute name="color"><xsl:value-of select="$value"/></xsl:attribute>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="getFontType">
    <xsl:param name="value"  select="'serif'"/>
    <xsl:variable name="font" select="normalize-space($value)"/>
    <xsl:variable name="fontLower" select="translate($font,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')"/>
    <xsl:choose>
      <xsl:when test="$fontLower='serif' or
                                  $fontLower='sans-serif' or
                                  $fontLower='cursive' or
                                  $fontLower='fantasy' or
                                  $fontLower='monospace'">
        <genericFamily>
          <xsl:attribute name="keyword"><xsl:value-of select="$fontLower"/></xsl:attribute>
        </genericFamily>
      </xsl:when>
      <xsl:when test="$fontLower='sans_serif'">
        <genericFamily>
          <xsl:attribute name="keyword">sans-serif</xsl:attribute>
        </genericFamily>
      </xsl:when>
      <xsl:otherwise>
        <familyName>
          <xsl:attribute name="string"><xsl:value-of select="$font"/></xsl:attribute>
        </familyName>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="getFont">
    <xsl:param name="value"  select="'serif'"/>
    <xsl:variable name="font" select="normalize-space($value)"/>

    <xsl:if test="contains($font,',')">
      <xsl:call-template name="getFontType">
        <xsl:with-param name="value" select="substring-before($font,',')"/>
      </xsl:call-template>
      <xsl:call-template name="getFont">
        <xsl:with-param name="value" select="substring-after($font,',')"/>
      </xsl:call-template>
    </xsl:if>
    <xsl:if test="not(contains($font,','))">
      <xsl:call-template name="getFontType">
        <xsl:with-param name="value" select="$font"/>
      </xsl:call-template>
    </xsl:if>
  </xsl:template>

  <xsl:template name="getPositiveLengthTag">
    <xsl:param name="tag"/>
    <xsl:param name="value"  select="'50%'"/>
    <xsl:param name="allowNumber"  select="'false'"/>
    <xsl:param name="allowPercentage"  select="'true'"/>
    
    <xsl:if test="substring($value,1,1)='-'">
      <xsl:message terminate="no">WARNING: negative value '<xsl:value-of select="$value"/>' not allowed in '<xsl:value-of select='@name'/>' attribute in theme <xsl:value-of select="../../../@name"/> ignoring
      </xsl:message>
    </xsl:if>
    <xsl:if test="substring($value,1,1)!='-'">
      <xsl:element name="{$tag}"> 
        <xsl:call-template name="getLength">
          <xsl:with-param name="value" select="$value"/>
          <xsl:with-param name="allowNumber" select="$allowNumber"/>
          <xsl:with-param name="allowPercentage" select="$allowPercentage"/>
        </xsl:call-template>
      </xsl:element>
    </xsl:if>
  </xsl:template>
  
  <xsl:template name="getLength">
    <xsl:param name="value"  select="'50%'"/>
    <xsl:param name="allowNumber"  select="'false'"/>
    <xsl:param name="allowPercentage"  select="'true'"/>
      <xsl:choose>
        <xsl:when test="contains($value,'%')">
          <xsl:if test="$allowPercentage='true'">
            <xsl:attribute name="percentage"><xsl:value-of select="substring-before($value,'%')"/></xsl:attribute>
          </xsl:if>
          <xsl:if test="$allowPercentage='false'">
            <xsl:attribute name="length"><xsl:value-of select="substring-before($value,'%')"/></xsl:attribute>
            <xsl:attribute name="lengthUnits">px</xsl:attribute>
            <xsl:message terminate="no">WARNING: percentage not allowed in '<xsl:value-of select='@name'/>' attribute in theme <xsl:value-of select="../../../@name"/> setting to length type px
            </xsl:message>
          </xsl:if>
        </xsl:when>
        <xsl:when test="contains($value,'mm')">
          <xsl:attribute name="length"><xsl:value-of select="substring-before($value,'mm')"/></xsl:attribute>
          <xsl:attribute name="lengthUnits">mm</xsl:attribute>
        </xsl:when>
        <xsl:when test="contains($value,'cm')">
          <xsl:attribute name="length"><xsl:value-of select="substring-before($value,'cm')"/></xsl:attribute>
          <xsl:attribute name="lengthUnits">cm</xsl:attribute>
        </xsl:when>
        <xsl:when test="contains($value,'in')">
          <xsl:attribute name="length"><xsl:value-of select="substring-before($value,'in')"/></xsl:attribute>
          <xsl:attribute name="lengthUnits">in</xsl:attribute>
        </xsl:when>
        <xsl:when test="contains($value,'px')">
          <xsl:attribute name="length"><xsl:value-of select="substring-before($value,'px')"/></xsl:attribute>
          <xsl:attribute name="lengthUnits">px</xsl:attribute>
        </xsl:when>
        <xsl:when test="contains($value,'pc')">
          <xsl:attribute name="length"><xsl:value-of select="substring-before($value,'pc')"/></xsl:attribute>
          <xsl:attribute name="lengthUnits">pc</xsl:attribute>
        </xsl:when>
        <xsl:when test="contains($value,'em')">
          <xsl:attribute name="length"><xsl:value-of select="substring-before($value,'em')"/></xsl:attribute>
          <xsl:attribute name="lengthUnits">em</xsl:attribute>
        </xsl:when>
        <xsl:when test="contains($value,'ex')">
          <xsl:attribute name="length"><xsl:value-of select="substring-before($value,'ex')"/></xsl:attribute>
          <xsl:attribute name="lengthUnits">ex</xsl:attribute>
        </xsl:when>
        <xsl:when test="contains($value,'pt')">
          <xsl:attribute name="length"><xsl:value-of select="substring-before($value,'pt')"/></xsl:attribute>
          <xsl:attribute name="lengthUnits">pt</xsl:attribute>
        </xsl:when>
        <xsl:otherwise>
          <xsl:choose>
            <xsl:when test="$allowNumber='true'">
              <xsl:attribute name="number"><xsl:value-of select="$value"/></xsl:attribute>
            </xsl:when>
            <xsl:when test="$allowNumber='false'">
              <xsl:attribute name="length"><xsl:value-of select="$value"/></xsl:attribute>
              <xsl:attribute name="lengthUnits">px</xsl:attribute>
              <xsl:message terminate="no">WARNING: number not allowed in '<xsl:value-of select='@name'/>' attribute in theme <xsl:value-of select="../../../@name"/> setting to length type px
              </xsl:message>
            </xsl:when>
          </xsl:choose>
        </xsl:otherwise>
      </xsl:choose>
  </xsl:template>

  <xsl:template name="getPosition">
    <xsl:param name="prefix" select="''"/>
    <xsl:param name="position"  select="'center'"/>
    <xsl:variable name="value" select="normalize-space($position)"/>
    <xsl:choose>
      <xsl:when test="$value='top left' or $value='left top'">
        <xsl:element name="{$prefix}XPosition">
          <xsl:attribute name="keyword">left</xsl:attribute>
        </xsl:element>
        <xsl:element name="{$prefix}YPosition">
          <xsl:attribute name="keyword">top</xsl:attribute>
        </xsl:element>
      </xsl:when>
      <xsl:when test="$value='top' or $value='top center' or $value='center top'">
        <xsl:element name="{$prefix}XPosition">
          <xsl:attribute name="keyword">center</xsl:attribute>
        </xsl:element>
        <xsl:element name="{$prefix}YPosition">
          <xsl:attribute name="keyword">top</xsl:attribute>
        </xsl:element>
      </xsl:when>
      <xsl:when test="$value='top right' or $value='right top'">
        <xsl:element name="{$prefix}XPosition">
          <xsl:attribute name="keyword">right</xsl:attribute>
        </xsl:element>
        <xsl:element name="{$prefix}YPosition">
          <xsl:attribute name="keyword">top</xsl:attribute>
        </xsl:element>
      </xsl:when>
      <xsl:when test="$value='left' or $value='left center' or $value='center left'">
        <xsl:element name="{$prefix}XPosition">
          <xsl:attribute name="keyword">left</xsl:attribute>
        </xsl:element>
        <xsl:element name="{$prefix}YPosition">
          <xsl:attribute name="keyword">center</xsl:attribute>
        </xsl:element>
      </xsl:when>
      <xsl:when test="$value='center' or $value='center center'">
        <xsl:element name="{$prefix}XPosition">
          <xsl:attribute name="keyword">center</xsl:attribute>
        </xsl:element>
        <xsl:element name="{$prefix}YPosition">
          <xsl:attribute name="keyword">center</xsl:attribute>
        </xsl:element>
      </xsl:when>
      <xsl:when test="$value='right' or $value='right center' or $value='center right'">
        <xsl:element name="{$prefix}XPosition">
          <xsl:attribute name="keyword">right</xsl:attribute>
        </xsl:element>
        <xsl:element name="{$prefix}YPosition">
          <xsl:attribute name="keyword">center</xsl:attribute>
        </xsl:element>
      </xsl:when>
      <xsl:when test="$value='bottom left' or $value='left bottom'">
        <xsl:element name="{$prefix}XPosition">
          <xsl:attribute name="keyword">left</xsl:attribute>
        </xsl:element>
        <xsl:element name="{$prefix}YPosition">
          <xsl:attribute name="keyword">bottom</xsl:attribute>
        </xsl:element>
      </xsl:when>
      <xsl:when test="$value='bottom' or $value='bottom center' or $value='center bottom'">
        <xsl:element name="{$prefix}XPosition">
          <xsl:attribute name="keyword">center</xsl:attribute>
        </xsl:element>
        <xsl:element name="{$prefix}YPosition">
          <xsl:attribute name="keyword">bottom</xsl:attribute>
        </xsl:element>
      </xsl:when>
      <xsl:when test="$value='bottom right' or $value='right bottom'">
        <xsl:element name="{$prefix}XPosition">
          <xsl:attribute name="keyword">right</xsl:attribute>
        </xsl:element>
        <xsl:element name="{$prefix}YPosition">
          <xsl:attribute name="keyword">bottom</xsl:attribute>
        </xsl:element>
      </xsl:when>
      <xsl:otherwise>
        <xsl:if test="contains($value,' ')">
          <xsl:element name="{$prefix}XPosition">
            <xsl:call-template name="getLength">
            <xsl:with-param name="value" select="substring-before($value,' ')"/>
            <xsl:with-param name="allowNumber" select="'false'"/>
            </xsl:call-template>
          </xsl:element>
          <xsl:element name="{$prefix}YPosition">
            <xsl:call-template name="getLength">
            <xsl:with-param name="value" select="substring-after($value,' ')"/>
            <xsl:with-param name="allowNumber" select="'false'"/>
            </xsl:call-template>
          </xsl:element>
        </xsl:if>
        <xsl:if test="not(contains($value,' '))">
          <xsl:element name="{$prefix}XPosition">
            <xsl:call-template name="getLength">
              <xsl:with-param name="value" select="$value"/>
              <xsl:with-param name="allowNumber" select="'false'"/>
            </xsl:call-template>
          </xsl:element>
        </xsl:if>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="getFontSize">
    <xsl:param name="value"  select="'medium'"/>
    <xsl:variable name="size" select="normalize-space($value)"/>

    <xsl:choose>
      <xsl:when test="$size='xx-small' or
                                 $size='x-small' or
                                 $size='small' or
                                 $size='medium' or
                                 $size='large' or
                                 $size='x-large' or
                                 $size='xx-large' or
                                 $size='larger' or
                                 $size='smaller'">
        <xsl:attribute name="keyword"><xsl:value-of select="$size"/></xsl:attribute>
      </xsl:when>
      <xsl:when test="$size='smalled'">
        <xsl:attribute name="keyword">smaller</xsl:attribute>
      </xsl:when>
      <xsl:otherwise>
          <xsl:call-template name="getLength">
            <xsl:with-param name="value" select="$size"/>
            <xsl:with-param name="allowNumber" select="'false'"/>
          </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="getPseudoIdentifier">
    <xsl:param name="value"  select="''"/>
    <xsl:variable name="pseudo" select="normalize-space($value)"/>

    <xsl:if test="contains($pseudo,':')">
      <xsl:call-template name="getPseudoIdentifier">
        <xsl:with-param name="value" select="substring-before($pseudo,',')"/>
      </xsl:call-template>
      <xsl:call-template name="getPseudoIdentifier">
        <xsl:with-param name="value" select="substring-before($pseudo,',')"/>
      </xsl:call-template>
    </xsl:if>
    <xsl:if test="not(contains($pseudo,':'))">
      <xsl:choose>
        <xsl:when test="contains($pseudo,'(')">
          <xsl:call-template name='getPseudoType'>
            <xsl:with-param name="identifier" select="substring-before($pseudo,'(')"/>
            <xsl:with-param name="parameter" select="substring-before(substring-after($pseudo,'('),')')"/>
          </xsl:call-template>
        </xsl:when>
        <xsl:otherwise>
          <xsl:call-template name='getPseudoType'>
            <xsl:with-param name="identifier" select="$pseudo"/>
          </xsl:call-template>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:if>
  </xsl:template>

  <xsl:template name="getPseudoType">
    <xsl:param name="identifier"  select="''"/>
    <xsl:param name="parameter"  select="''"/>

    <xsl:choose>
      <xsl:when test="$identifier='hover' or $identifier='link'  or
                      $identifier='first-child' or $identifier='visited' or
                      $identifier='active' or $identifier='focus' or
                      $identifier='lang'">
        <pseudoClassSelector>
          <xsl:attribute name="identifier"><xsl:value-of select="$identifier"/></xsl:attribute>
          <xsl:if test="$parameter!=''">
            <xsl:attribute name="parameter"><xsl:value-of select="$parameter"/></xsl:attribute>
          </xsl:if>
        </pseudoClassSelector>
      </xsl:when>
      <xsl:when test="$identifier='first-line' or $identifier='first-letter'  or
                      $identifier='before' or $identifier='after'">
        <pseudoElementSelector>
          <xsl:attribute name="identifier"><xsl:value-of select="$identifier"/></xsl:attribute>
          <xsl:if test="$parameter!=''">
            <xsl:attribute name="parameter"><xsl:value-of select="$parameter"/></xsl:attribute>
          </xsl:if>
        </pseudoElementSelector>
      </xsl:when>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="getMargin">
    <xsl:param name="value"  select="'auto'"/>
    <xsl:variable name="margin" select="normalize-space($value)"/>

    <xsl:choose>
      <xsl:when test="$margin='auto'">
        <xsl:attribute name="keyword">auto</xsl:attribute>
      </xsl:when>
      <xsl:otherwise>
          <xsl:call-template name="getLength">
            <xsl:with-param name="value" select="$margin"/>
            <xsl:with-param name="allowNumber" select="'false'"/>
          </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="getSpacing">
    <xsl:param name="value"  select="'normal'"/>
    <xsl:variable name="spacing" select="normalize-space($value)"/>

    <xsl:choose>
      <xsl:when test="$spacing='normal'">
        <xsl:attribute name="keyword"><xsl:value-of select="$spacing"/></xsl:attribute>
      </xsl:when>
      <xsl:otherwise>
          <xsl:call-template name="getLength">
            <xsl:with-param name="value" select="$spacing"/>
            <xsl:with-param name="allowNumber" select="'false'"/>
          </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="getWidthTag">
    <xsl:param name="tag"/>
    <xsl:param name="value"  select="'center'"/>
    <xsl:variable name="width" select="normalize-space($value)"/>

    <xsl:choose>
      <xsl:when test="$width='thin' or
                                  $width='medium' or
                                  $width='thick'">
        <xsl:element name="{$tag}">
          <xsl:attribute name="keyword"><xsl:value-of select="$width"/></xsl:attribute>
        </xsl:element>
      </xsl:when>
      <xsl:otherwise>
          <xsl:call-template name="getPositiveLengthTag">
            <xsl:with-param name="tag" select="$tag"/>
            <xsl:with-param name="value" select="$width"/>
            <xsl:with-param name="allowNumber" select="'false'"/>
            <xsl:with-param name="allowPercentage" select="'false'"/>
          </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>


  <xsl:template name="doRule">
      <xsl:param name="tag"  select="''"/>
      <xsl:param name="class"  select="''"/>
      <xsl:param name="id"  select="''"/>
    
    <xsl:choose>
    <xsl:when test="number(substring($class,1,1)) or substring($class,1,1)='-' or contains($class, ' ')">
      <xsl:message terminate="no">ERROR: Class names must not be prefixed by digit or hyphen characters.  Removing rule with tag <xsl:value-of select="$tag"/> class <xsl:value-of select="$class"/> and id <xsl:value-of select="$id"/> in theme <xsl:value-of select="../../@name"/></xsl:message>
    </xsl:when>
    <xsl:when test="number(substring($id,1,1)) or substring($id,1,1)='-' or contains($id, ' ')">
      <xsl:message terminate="no">ERROR: IDs must not be prefixed by digit or hyphen characters.  Removing rule with tag <xsl:value-of select="$tag"/> class <xsl:value-of select="$class"/> and id <xsl:value-of select="$id"/> in theme <xsl:value-of select="../../@name"/></xsl:message>
    </xsl:when>
    <xsl:otherwise>
    <rule>
      <simpleSelectorSequence>
        <xsl:choose>
          <xsl:when test="contains($tag,':')">
            <typeSelector>
            <xsl:attribute name="type"><xsl:value-of select="substring-before($tag,':')"/></xsl:attribute>
            </typeSelector>
          </xsl:when>
          <xsl:when test="$tag!=''">
            <typeSelector>
            <xsl:attribute name="type"><xsl:value-of select="$tag"/></xsl:attribute>
            </typeSelector>
          </xsl:when>
          <xsl:otherwise>
            <universalSelector/>
          </xsl:otherwise>
        </xsl:choose>
        <xsl:if test="$class!=''">
          <classSelector class="{$class}"/>
        </xsl:if>
        <xsl:if test="$id!=''">
          <idSelector id="{$id}"/>
        </xsl:if>
        <xsl:if test="contains($tag,':')">
          <xsl:call-template name="getPseudoIdentifier">
            <xsl:with-param name="value" select="substring-after($tag,':')"/>
          </xsl:call-template>
        </xsl:if>
      </simpleSelectorSequence>
      <styleProperties>
        <xsl:apply-templates/>
      </styleProperties>
    </rule>
    </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!--

       Transform Style attributes

    -->
  <xsl:template match="deviceTheme/style">
    <xsl:variable name="lowerTag">
      <xsl:value-of select="translate(@tag,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')"/>
    </xsl:variable>

    <xsl:choose>
      <xsl:when test="$lowerTag='a' or $lowerTag='address' or
                      $lowerTag='b' or $lowerTag='big' or
                      $lowerTag='blockquote' or $lowerTag='br' or
                      $lowerTag='canvas' or $lowerTag='chart' or
                      $lowerTag='cite' or $lowerTag='code' or
                      $lowerTag='dd' or $lowerTag='div' or
                      $lowerTag='dl' or $lowerTag='dt' or
                      $lowerTag='dynvis' or $lowerTag='em' or
                      $lowerTag='h1' or $lowerTag='h2' or
                      $lowerTag='h3' or $lowerTag='h4' or
                      $lowerTag='h5' or $lowerTag='h6' or
                      $lowerTag='hr' or $lowerTag='i' or
                      $lowerTag='img' or $lowerTag='kbd' or
                      $lowerTag='li' or $lowerTag='logo' or
                      $lowerTag='menu' or $lowerTag='menuitem' or
                      $lowerTag='meta' or $lowerTag='mmflash' or
                      $lowerTag='montage' or $lowerTag='noscript' or
                      $lowerTag='ol' or $lowerTag='p' or
                      $lowerTag='pane' or $lowerTag='pre' or
                      $lowerTag='quicktime' or $lowerTag='realaudio' or
                      $lowerTag='realvideo' or $lowerTag='region' or
                      $lowerTag='samp' or $lowerTag='script' or
                      $lowerTag='segment' or $lowerTag='small' or
                      $lowerTag='span' or $lowerTag='strong' or
                      $lowerTag='sub' or $lowerTag='sup' or
                      $lowerTag='table' or $lowerTag='tbody' or
                      $lowerTag='td' or $lowerTag='th' or
                      $lowerTag='tr' or $lowerTag='tfoot' or
                      $lowerTag='thead' or $lowerTag='tt' or
                      $lowerTag='u' or $lowerTag='ul' or
                      $lowerTag='va' or $lowerTag='winaudio' or $lowerTag='winvideo' or
                      $lowerTag='xfaction' or $lowerTag='xfboolean' or
                      $lowerTag='xfform' or $lowerTag='xfmuselect' or
                      $lowerTag='xfoption' or $lowerTag='xfsiselect' or
                      $lowerTag='xftextinput'">
        <xsl:call-template name="doRule">
          <xsl:with-param name="tag" select="$lowerTag"/>
          <xsl:with-param name="class" select="@class"/>
          <xsl:with-param name="id" select="@id"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$lowerTag='body'">
        <xsl:message terminate="no">WARNING: Converting style tag 'body' to 'canvas' in theme <xsl:value-of select="../../@name"/>
        </xsl:message>
        <xsl:call-template name="doRule">
          <xsl:with-param name="tag" select="'canvas'"/>
          <xsl:with-param name="class" select="@class"/>
          <xsl:with-param name="id" select="@id"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$lowerTag='form'">
        <xsl:message terminate="no">WARNING: Converting style tag 'form' to 'xfform' in theme <xsl:value-of select="../../@name"/>
        </xsl:message>
        <xsl:call-template name="doRule">
          <xsl:with-param name="tag" select="'xfform'"/>
          <xsl:with-param name="class" select="@class"/>
          <xsl:with-param name="id" select="@id"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$lowerTag='option'">
        <xsl:message terminate="no">WARNING: Converting style tag 'option' to 'xfoption' in theme <xsl:value-of select="../../@name"/>
        </xsl:message>
        <xsl:call-template name="doRule">
          <xsl:with-param name="tag" select="'xfoption'"/>
          <xsl:with-param name="class" select="@class"/>
          <xsl:with-param name="id" select="@id"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$lowerTag='textarea'">
        <xsl:message terminate="no">WARNING: Converting style tag 'textarea' to 'xftextinput' in theme <xsl:value-of select="../../@name"/>
        </xsl:message>
        <xsl:call-template name="doRule">
          <xsl:with-param name="tag" select="'xftextinput'"/>
          <xsl:with-param name="class" select="@class"/>
          <xsl:with-param name="id" select="@id"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$lowerTag='select'">
        <xsl:message terminate="no">WARNING: Converting style tag 'select' to 'xfsiselect' &amp; 'xfmuselect' in theme <xsl:value-of select="../../@name"/>
        </xsl:message>
        <xsl:call-template name="doRule">
          <xsl:with-param name="tag" select="'xfsiselect'"/>
          <xsl:with-param name="class" select="@class"/>
          <xsl:with-param name="id" select="@id"/>
        </xsl:call-template>
        <xsl:call-template name="doRule">
          <xsl:with-param name="tag" select="'xfmuselect'"/>
          <xsl:with-param name="class" select="@class"/>
          <xsl:with-param name="id" select="@id"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="contains($lowerTag,':')">
        <xsl:call-template name="doRule">
          <xsl:with-param name="tag" select="$lowerTag"/>
          <xsl:with-param name="class" select="@class"/>
          <xsl:with-param name="id" select="@id"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:when test="$lowerTag!=''">
        <xsl:message terminate="no">ERROR: Style tag '<xsl:value-of select="$lowerTag"/>' is not valid in theme <xsl:value-of select="../../@name"/>
        </xsl:message>
      </xsl:when>
      <xsl:otherwise>
        <xsl:call-template name="doRule">
          <xsl:with-param name="tag" select="''"/>
          <xsl:with-param name="class" select="@class"/>
          <xsl:with-param name="id" select="@id"/>
        </xsl:call-template>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="deviceTheme">
    <xsl:copy>
      <xsl:apply-templates select="@*"/>
      <xsl:for-each select="themeAttributes/attribute">
        <xsl:if test="@name='externalCSS'">
          <xsl:attribute name="externalStyleSheet"><xsl:value-of select="@value"/></xsl:attribute>
        </xsl:if>
      </xsl:for-each>
      <xsl:apply-templates/>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="deviceTheme/themeAttributes"/>


  <!--

        Transform Attributes

    -->
  <xsl:template match="deviceTheme/style/attribute">
    <xsl:choose>

      <!--

        Transform action-image

        -->
      <xsl:when test="@name='action-image'">
        <marinerFormActionImage>
          <xsl:attribute name="marinerComponentURI"><xsl:value-of select="@value"/></xsl:attribute>
        </marinerFormActionImage>
      </xsl:when>

      <!--

        Transform action-ui

        -->
      <xsl:when test="@name='action-ui'">
        <xsl:choose>
          <xsl:when test="@value='button'">
            <marinerFormActionStyle>
              <xsl:attribute name="keyword">defalt</xsl:attribute>
            </marinerFormActionStyle>
          </xsl:when>
          <xsl:when test="@value='image'">
            <marinerFormActionStyle>
              <xsl:attribute name="keyword">image</xsl:attribute>
            </marinerFormActionStyle>
          </xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="no">ERROR: Could not decode 'action-ui' attribute in theme <xsl:value-of select="../../../@name"/>
            </xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>

      <!--

        Transform adornments

        -->
      <xsl:when test="@name='adornments'">
        <xsl:choose>
          <xsl:when test="@value='true'">
            <marinerChartAdornments>
              <xsl:attribute name="keyword">all</xsl:attribute>
            </marinerChartAdornments>
          </xsl:when>
          <xsl:when test="@value='false'">
            <marinerChartAdornments>
              <xsl:attribute name="keyword">none</xsl:attribute>
            </marinerChartAdornments>
          </xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="no">ERROR: Could not decode 'adornments' attribute in theme <xsl:value-of select="../../../@name"/>
            </xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>

      <!--

        Transform align

        -->
      <xsl:when test="@name='align'">
        <xsl:choose>
          <xsl:when test="@value='b'">
            <marinerMMFlashAlign>
              <xsl:attribute name="keyword">bottom</xsl:attribute>
            </marinerMMFlashAlign>
          </xsl:when>
          <xsl:when test="@value='bottom'">
            <marinerMMFlashAlign>
              <xsl:attribute name="keyword">bottom</xsl:attribute>
            </marinerMMFlashAlign>
          </xsl:when>
          <xsl:when test="@value='c'">
            <marinerMMFlashAlign>
              <xsl:attribute name="keyword">center</xsl:attribute>
            </marinerMMFlashAlign>
          </xsl:when>
          <xsl:when test="@value='center'">
            <marinerMMFlashAlign>
              <xsl:attribute name="keyword">center</xsl:attribute>
            </marinerMMFlashAlign>
          </xsl:when>
          <xsl:when test="@value='l'">
            <marinerMMFlashAlign>
              <xsl:attribute name="keyword">left</xsl:attribute>
            </marinerMMFlashAlign>
          </xsl:when>
          <xsl:when test="@value='left'">
            <marinerMMFlashAlign>
              <xsl:attribute name="keyword">left</xsl:attribute>
            </marinerMMFlashAlign>
          </xsl:when>
          <xsl:when test="@value='r'">
            <marinerMMFlashAlign>
              <xsl:attribute name="keyword">right</xsl:attribute>
            </marinerMMFlashAlign>
          </xsl:when>
          <xsl:when test="@value='right'">
            <marinerMMFlashAlign>
              <xsl:attribute name="keyword">right</xsl:attribute>
            </marinerMMFlashAlign>
          </xsl:when>
          <xsl:when test="@value='t'">
            <marinerMMFlashAlign>
              <xsl:attribute name="keyword">top</xsl:attribute>
            </marinerMMFlashAlign>
          </xsl:when>
          <xsl:when test="@value='top'">
            <marinerMMFlashAlign>
              <xsl:attribute name="keyword">top</xsl:attribute>
            </marinerMMFlashAlign>
          </xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="no">ERROR: Could not decode 'align' attribute in theme <xsl:value-of select="../../../@name"/>
            </xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>

      <!--

        Transform background-attachment

        -->
      <xsl:when test="@name='background-attachment'">
        <backgroundAttachment>
          <xsl:attribute name="keyword"><xsl:value-of select="@value"/></xsl:attribute>
        </backgroundAttachment>
      </xsl:when>
      <!--

        Transform background-color

        -->
      <xsl:when test="@name='background-color'">
        <backgroundColor>
          <xsl:call-template name="getColor">
            <xsl:with-param name="value" select="@value"/>
          </xsl:call-template>
        </backgroundColor>
      </xsl:when>

      <!--

        Transform background-image

        -->
      <xsl:when test="@name='background-image'">
        <backgroundImage>
          <xsl:attribute name="marinerComponentURI"><xsl:value-of select="@value"/></xsl:attribute>
        </backgroundImage>
      </xsl:when>

      <!--

        Transform background-image-type

        -->
      <xsl:when test="@name='background-image-type'">
        <marinerBackgroundType>
        <xsl:choose>
          <xsl:when test="@value='Dynamic Visual'">
            <xsl:attribute name="keyword">dynamic-visual</xsl:attribute>
          </xsl:when>
          <xsl:otherwise>
            <xsl:attribute name="keyword">image</xsl:attribute>
          </xsl:otherwise>
        </xsl:choose>
        </marinerBackgroundType>
      </xsl:when>

      <!--

        Transform background-position

        -->
      <xsl:when test="@name='background-position'">
        <backgroundPosition>
          <xsl:call-template name="getPosition">
            <xsl:with-param name="prefix" select="'background'"/>
            <xsl:with-param name="position" select="@value"/>
          </xsl:call-template>
        </backgroundPosition>
      </xsl:when>

      <!--

        Transform background-repeat

        -->
      <xsl:when test="@name='background-repeat'">
        <backgroundRepeat>
          <xsl:attribute name="keyword"><xsl:value-of select="@value"/></xsl:attribute>
        </backgroundRepeat>
      </xsl:when>

      <!--

        Transform banner

        -->
      <xsl:when test="@name='banner'">
        <xsl:message terminate="no">WARNING: Dropping attribute '<xsl:value-of select="@name"/>' in theme <xsl:value-of select="../../../@name"/>
        </xsl:message>
      </xsl:when>

      <!--

        Transform border-color

        -->

      <xsl:when test="@name='border-color'">
        <borderBottomColor>
          <xsl:call-template name="getColor">
            <xsl:with-param name="value" select="@value"/>
          </xsl:call-template>
        </borderBottomColor>
        <borderLeftColor>
          <xsl:call-template name="getColor">
            <xsl:with-param name="value" select="@value"/>
          </xsl:call-template>
        </borderLeftColor>
        <borderRightColor>
          <xsl:call-template name="getColor">
            <xsl:with-param name="value" select="@value"/>
          </xsl:call-template>
        </borderRightColor>
        <borderTopColor>
          <xsl:call-template name="getColor">
            <xsl:with-param name="value" select="@value"/>
          </xsl:call-template>
        </borderTopColor>
      </xsl:when>

      <!--

        Transform border-style

        -->
      <xsl:when test="@name='border-style'">
        <borderBottomStyle>
          <xsl:attribute name="keyword"><xsl:value-of select="@value"/></xsl:attribute>
        </borderBottomStyle>
        <borderLeftStyle>
          <xsl:attribute name="keyword"><xsl:value-of select="@value"/></xsl:attribute>
        </borderLeftStyle>
        <borderRightStyle>
          <xsl:attribute name="keyword"><xsl:value-of select="@value"/></xsl:attribute>
        </borderRightStyle>
        <borderTopStyle>
          <xsl:attribute name="keyword"><xsl:value-of select="@value"/></xsl:attribute>
        </borderTopStyle>
      </xsl:when>

      <!--

        Transform border-bottom-width

        -->
      <xsl:when test="@name='border-bottom-width'">
        <xsl:call-template name="getWidthTag">
          <xsl:with-param name="tag" select="'borderBottomWidth'"/>
          <xsl:with-param name="value" select="@value"/>
        </xsl:call-template>
      </xsl:when>
      <!--

        Transform border-left-width

        -->
      <xsl:when test="@name='border-left-width'">
        <xsl:call-template name="getWidthTag">
          <xsl:with-param name="tag" select="'borderLeftWidth'"/>
          <xsl:with-param name="value" select="@value"/>
        </xsl:call-template>
      </xsl:when>
      <!--

        Transform border-right-width

        -->
      <xsl:when test="@name='border-right-width'">
        <xsl:call-template name="getWidthTag">
          <xsl:with-param name="tag" select="'borderRightWidth'"/>
          <xsl:with-param name="value" select="@value"/>
        </xsl:call-template>
      </xsl:when>

      <!--

        Transform border-top-width

        -->
      <xsl:when test="@name='border-top-width'">
        <xsl:call-template name="getWidthTag">
          <xsl:with-param name="tag" select="'borderTopWidth'"/>
          <xsl:with-param name="value" select="@value"/>
        </xsl:call-template>
      </xsl:when>

      <!--

        Transform color

        -->
      <xsl:when test="@name='color'">
        <color>
          <xsl:call-template name="getColor">
            <xsl:with-param name="value" select="@value"/>
          </xsl:call-template>
        </color>
      </xsl:when>

      <!--

        Transform columns

        -->
      <xsl:when test="@name='columns'">
        <marinerColumns>
          <xsl:attribute name="integer"><xsl:value-of select="@value"/></xsl:attribute>
        </marinerColumns>
      </xsl:when>

      <!--

        Transform font-size

        -->
      <xsl:when test="@name='font-size'">
        <fontSize>
          <xsl:call-template name="getFontSize">
            <xsl:with-param name="value" select="@value"/>
          </xsl:call-template>
        </fontSize>
      </xsl:when>

      <!--

        Transform font-family

        -->
      <xsl:when test="@name='font-family'">
        <xsl:if test="@value!=''">
          <fontFamily>
            <xsl:call-template name="getFont">
              <xsl:with-param name="value" select="@value"/>
            </xsl:call-template>
          </fontFamily>
        </xsl:if>
      </xsl:when>

      <!--

        Transform font-style

        -->
      <xsl:when test="@name='font-style'">
        <xsl:choose>
          <xsl:when test="@value='normal' or @value='italic' or @value='oblique'">
            <fontStyle>
              <xsl:attribute name="keyword"><xsl:value-of select="@value"/></xsl:attribute>
            </fontStyle>
          </xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="no">ERROR: Could not decode 'font-style' attribute in theme <xsl:value-of select="../../../@name"/>
            </xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>

      <!--

        Transform font-variant

        -->
      <xsl:when test="@name='font-variant'">
        <fontVariant>
          <xsl:attribute name="keyword"><xsl:value-of select="@value"/></xsl:attribute>
        </fontVariant>
      </xsl:when>

      <!--

        Transform font-weight

        -->
      <xsl:when test="@name='font-weight'">
        <fontWeight>
<!--
          <xsl:choose>
            <xsl:when test="@value='100'">
              <xsl:attribute name="keyword">weight-100</xsl:attribute>
            </xsl:when>
            <xsl:when test="@value='200'">
              <xsl:attribute name="keyword">weight-200</xsl:attribute>
            </xsl:when>
            <xsl:when test="@value='300'">
              <xsl:attribute name="keyword">weight-300</xsl:attribute>
            </xsl:when>
            <xsl:when test="@value='400'">
              <xsl:attribute name="keyword">weight-400</xsl:attribute>
            </xsl:when>
            <xsl:when test="@value='500'">
              <xsl:attribute name="keyword">weight-500</xsl:attribute>
            </xsl:when>
            <xsl:when test="@value='600'">
              <xsl:attribute name="keyword">weight-600</xsl:attribute>
            </xsl:when>
            <xsl:when test="@value='700'">
              <xsl:attribute name="keyword">weight-700</xsl:attribute>
            </xsl:when>
            <xsl:when test="@value='800'">
              <xsl:attribute name="keyword">weight-800</xsl:attribute>
            </xsl:when>
            <xsl:when test="@value='900'">
              <xsl:attribute name="keyword">weight-900</xsl:attribute>
            </xsl:when>
            <xsl:otherwise>
              <xsl:attribute name="keyword">
                <xsl:value-of select="@value"/>
              </xsl:attribute>
            </xsl:otherwise>
          </xsl:choose>
-->
          <xsl:attribute name="keyword">
            <xsl:value-of select="@value"/>
          </xsl:attribute>
        </fontWeight>
      </xsl:when>

      <!--

        Transform foreground-colors

        -->
      <xsl:when test="@name='foreground-colors'">
        <marinerChartForegroundColors>
          <xsl:call-template name="getChartColor">
            <xsl:with-param name="value" select="@value"/>
          </xsl:call-template>
        </marinerChartForegroundColors>
      </xsl:when>

      <!--

        Transform hspace

        -->
      <xsl:when test="@name='hspace'">
        <xsl:message terminate="no">WARNING: Dropping attribute '<xsl:value-of select="@name"/>' in theme <xsl:value-of select="../../../@name"/>
        </xsl:message>
      </xsl:when>

      <!--

        Transform grid-color

        -->
      <xsl:when test="@name='grid-color'">
        <marinerChartGridColor>
          <xsl:call-template name="getColor">
            <xsl:with-param name="value" select="@value"/>
          </xsl:call-template>
        </marinerChartGridColor>
      </xsl:when>

      <!--

        Transform height

        -->
      <xsl:when test="@name='height'">
        <xsl:choose>
          <xsl:when test="@value='auto'">
            <height>
              <xsl:attribute name="keyword">auto</xsl:attribute>
            </height>
          </xsl:when>
          <xsl:otherwise>
            <xsl:call-template name="getPositiveLengthTag">
              <xsl:with-param name="tag" select="'height'"/>
              <xsl:with-param name="value" select="@value"/>
              <xsl:with-param name="allowNumber" select="'false'"/>
            </xsl:call-template>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>

      <!--

        Transform heighthint

        -->
      <xsl:when test="@name='heighthint'">
        <marinerChartHeight>
          <xsl:attribute name="percentage"><xsl:value-of select="@value"/></xsl:attribute>
        </marinerChartHeight>
      </xsl:when>

      <!--

        Transform image-align

        -->
      <xsl:when test="@name='image-align'">
        <xsl:message terminate="no">WARNING: Dropping attribute '<xsl:value-of select="@name"/>' in theme <xsl:value-of select="../../../@name"/>
        </xsl:message>
      </xsl:when>

      <!--

        Transform image-border

        -->
      <xsl:when test="@name='image-border'">
        <xsl:message terminate="no">WARNING: Dropping attribute '<xsl:value-of select="@name"/>' in theme <xsl:value-of select="../../../@name"/>
        </xsl:message>
      </xsl:when>

      <!--

        Transform label-values

        -->
      <xsl:when test="@name='label-values'">
        <xsl:choose>
          <xsl:when test="@value='true'">
            <marinerChartLabelValues>
              <xsl:attribute name="keyword">yes</xsl:attribute>
            </marinerChartLabelValues>
          </xsl:when>
          <xsl:when test="@value='false'">
            <marinerChartLabelValues>
              <xsl:attribute name="keyword">no</xsl:attribute>
            </marinerChartLabelValues>
          </xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="no">ERROR: Could not decode 'label-values' attribute in theme <xsl:value-of select="../../../@name"/>
            </xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>
      <!--

        Transform letter-spacing

        -->
      <xsl:when test="@name='letter-spacing'">
        <letterSpacing>
          <xsl:call-template name="getSpacing">
            <xsl:with-param name="value" select="@value"/>
          </xsl:call-template>
        </letterSpacing>
      </xsl:when>

      <!--

        Transform linegap

        -->
      <xsl:when test="@name='linegap'">
        <marinerLineGap>
          <xsl:call-template name="getLength">
            <xsl:with-param name="value" select="@value"/>
            <xsl:with-param name="allowNumber" select="'false'"/>
          </xsl:call-template>
        </marinerLineGap>
      </xsl:when>

      <!--

        Transform line-height

        -->
      <xsl:when test="@name='line-height'">
        <lineHeight>
          <xsl:choose>
            <xsl:when test="@value='normal'">
              <xsl:attribute name="keyword">normal</xsl:attribute>
            </xsl:when>
            <xsl:otherwise>
              <xsl:call-template name="getLength">
                <xsl:with-param name="value" select="@value"/>
                <xsl:with-param name="allowNumber" select="'true'"/>
              </xsl:call-template>
            </xsl:otherwise>
          </xsl:choose>
        </lineHeight>
      </xsl:when>


      <!--

        Transform link-ui

        -->
      <xsl:when test="@name='link-ui'">
        <xsl:choose>
          <xsl:when test="@value='none'">
            <marinerLinkStyle>
              <xsl:attribute name="keyword">default</xsl:attribute>
            </marinerLinkStyle>
          </xsl:when>
          <xsl:when test="@value='button'">
            <marinerLinkStyle>
              <xsl:attribute name="keyword">button</xsl:attribute>
            </marinerLinkStyle>
          </xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="no">ERROR: Could not decode 'link-ui' attribute in theme <xsl:value-of select="../../../@name"/>
            </xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>

      <!--

        Transform list-caption-align

        -->
      <xsl:when test="@name='list-caption-align'">
        <xsl:choose>
          <xsl:when test="@value='left'">
            <marinerSelectionListOptionLayout>
              <xsl:attribute name="keyword">caption-first</xsl:attribute>
            </marinerSelectionListOptionLayout>
          </xsl:when>
          <xsl:when test="@value='right'">
            <marinerSelectionListOptionLayout>
              <xsl:attribute name="keyword">control-first</xsl:attribute>
            </marinerSelectionListOptionLayout>
          </xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="no">ERROR: Could not decode 'list-caption-align' attribute in theme <xsl:value-of select="../../../@name"/>
            </xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>

      <!--

        Transform list-style-image

        -->
      <xsl:when test="@name='list-style-image'">
        <listStyleImage>
          <xsl:attribute name="marinerComponentURI"><xsl:value-of select="@value"/></xsl:attribute>
        </listStyleImage>
      </xsl:when>

      <!--

        Transform list-style-position

        -->
      <xsl:when test="@name='list-style-position'">
        <listStylePosition>
          <xsl:attribute name="keyword"><xsl:value-of select="@value"/></xsl:attribute>
        </listStylePosition>
      </xsl:when>

      <!--

        Transform list-style-type

        -->
      <xsl:when test="@name='list-style-type'">
        <xsl:choose>
          <xsl:when test="@value='disc'">
            <listStyleType>
              <xsl:attribute name="keyword"><xsl:value-of select="@value"/></xsl:attribute>
            </listStyleType>
          </xsl:when>
          <xsl:when test="@value='circle'">
            <listStyleType>
              <xsl:attribute name="keyword"><xsl:value-of select="@value"/></xsl:attribute>
            </listStyleType>
          </xsl:when>
          <xsl:when test="@value='cirle'">
            <listStyleType>
              <xsl:attribute name="keyword">circle</xsl:attribute>
            </listStyleType>
          </xsl:when>
          <xsl:when test="@value='square'">
            <listStyleType>
              <xsl:attribute name="keyword"><xsl:value-of select="@value"/></xsl:attribute>
            </listStyleType>
          </xsl:when>
          <xsl:when test="@value='decimal'">
            <listStyleType>
              <xsl:attribute name="keyword"><xsl:value-of select="@value"/></xsl:attribute>
            </listStyleType>
          </xsl:when>
          <xsl:when test="@value='decimal-leading-zero'">
            <listStyleType>
              <xsl:attribute name="keyword"><xsl:value-of select="@value"/></xsl:attribute>
            </listStyleType>
          </xsl:when>
          <xsl:when test="@value='lower-roman'">
            <listStyleType>
              <xsl:attribute name="keyword"><xsl:value-of select="@value"/></xsl:attribute>
            </listStyleType>
          </xsl:when>
          <xsl:when test="@value='upper-roman'">
            <listStyleType>
              <xsl:attribute name="keyword"><xsl:value-of select="@value"/></xsl:attribute>
            </listStyleType>
          </xsl:when>
          <xsl:when test="@value='lower-greek'">
            <listStyleType>
              <xsl:attribute name="keyword"><xsl:value-of select="@value"/></xsl:attribute>
            </listStyleType>
          </xsl:when>
          <xsl:when test="@value='lower-alpha'">
            <listStyleType>
              <xsl:attribute name="keyword"><xsl:value-of select="@value"/></xsl:attribute>
            </listStyleType>
          </xsl:when>
          <xsl:when test="@value='lower-latin'">
            <listStyleType>
              <xsl:attribute name="keyword"><xsl:value-of select="@value"/></xsl:attribute>
            </listStyleType>
          </xsl:when>
          <xsl:when test="@value='upper-alpha'">
            <listStyleType>
              <xsl:attribute name="keyword"><xsl:value-of select="@value"/></xsl:attribute>
            </listStyleType>
          </xsl:when>
          <xsl:when test="@value='upper-latin'">
            <listStyleType>
              <xsl:attribute name="keyword"><xsl:value-of select="@value"/></xsl:attribute>
            </listStyleType>
          </xsl:when>
          <xsl:when test="@value='hebrew'">
            <listStyleType>
              <xsl:attribute name="keyword"><xsl:value-of select="@value"/></xsl:attribute>
            </listStyleType>
          </xsl:when>
          <xsl:when test="@value='armenian'">
            <listStyleType>
              <xsl:attribute name="keyword"><xsl:value-of select="@value"/></xsl:attribute>
            </listStyleType>
          </xsl:when>
          <xsl:when test="@value='georgian'">
            <listStyleType>
              <xsl:attribute name="keyword"><xsl:value-of select="@value"/></xsl:attribute>
            </listStyleType>
          </xsl:when>
          <xsl:when test="@value='cjk-ideographic'">
            <listStyleType>
              <xsl:attribute name="keyword"><xsl:value-of select="@value"/></xsl:attribute>
            </listStyleType>
          </xsl:when>
          <xsl:when test="@value='hiragana'">
            <listStyleType>
              <xsl:attribute name="keyword"><xsl:value-of select="@value"/></xsl:attribute>
            </listStyleType>
          </xsl:when>
          <xsl:when test="@value='katakana'">
            <listStyleType>
              <xsl:attribute name="keyword"><xsl:value-of select="@value"/></xsl:attribute>
            </listStyleType>
          </xsl:when>
          <xsl:when test="@value='hiragana-iroha'">
            <listStyleType>
              <xsl:attribute name="keyword"><xsl:value-of select="@value"/></xsl:attribute>
            </listStyleType>
          </xsl:when>
          <xsl:when test="@value='katakana-iroha'">
            <listStyleType>
              <xsl:attribute name="keyword"><xsl:value-of select="@value"/></xsl:attribute>
            </listStyleType>
          </xsl:when>
          <xsl:when test="@value='none'">
            <listStyleType>
              <xsl:attribute name="keyword"><xsl:value-of select="@value"/></xsl:attribute>
            </listStyleType>
          </xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="no">ERROR: Could not decode 'list-style-type' attribute in theme <xsl:value-of select="../../../@name"/>
            </xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>


      <!--

        Transform list-ui

        -->
      <xsl:when test="@name='list-ui'">
        <xsl:choose>
          <xsl:when test="@value='menu'">
            <marinerSelectionListStyle>
              <xsl:attribute name="keyword">default</xsl:attribute>
            </marinerSelectionListStyle>
          </xsl:when>
          <xsl:when test="@value='radio' or @value='checkbox'">
            <marinerSelectionListStyle>
              <xsl:attribute name="keyword">controls</xsl:attribute>
            </marinerSelectionListStyle>
          </xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="no">ERROR: Could not decode 'list-ui' attribute in theme <xsl:value-of select="../../../@name"/>
            </xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>

      <!--

        Transform mainmenu

        -->
      <xsl:when test="@name='mainmenu'">
        <xsl:message terminate="no">WARNING: Dropping attribute '<xsl:value-of select="@name"/>' in theme <xsl:value-of select="../../../@name"/>
        </xsl:message>
      </xsl:when>

      <!--

        Transform mariner-http-method-hint

        -->
      <xsl:when test="@name='mariner-http-method-hint'">
        <xsl:choose>
          <xsl:when test="@value='get'">
            <marinerHttpMethodHint>
              <xsl:attribute name="keyword">get</xsl:attribute>
            </marinerHttpMethodHint>
          </xsl:when>
          <xsl:when test="@value='post'">
            <marinerHttpMethodHint>
              <xsl:attribute name="keyword">post</xsl:attribute>
            </marinerHttpMethodHint>
          </xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="no">ERROR: Could not decode 'mariner-http-method-hint' attribute in theme <xsl:value-of select="../../../@name"/>
            </xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>

      <!--

        Transform margin-bottom

        -->
      <xsl:when test="@name='margin-bottom'">
        <marginBottom>
          <xsl:call-template name="getMargin">
            <xsl:with-param name="value" select="@value"/>
          </xsl:call-template>
        </marginBottom>
      </xsl:when>
      <!--

        Transform margin-left

        -->
      <xsl:when test="@name='margin-left'">
        <marginLeft>
          <xsl:call-template name="getMargin">
            <xsl:with-param name="value" select="@value"/>
          </xsl:call-template>
        </marginLeft>
      </xsl:when>
      <!--

        Transform margin-right

        -->
      <xsl:when test="@name='margin-right'">
        <marginRight>
          <xsl:call-template name="getMargin">
            <xsl:with-param name="value" select="@value"/>
          </xsl:call-template>
        </marginRight>
      </xsl:when>
      <!--

        Transform margin-top

        -->
      <xsl:when test="@name='margin-top'">
        <marginTop>
          <xsl:call-template name="getMargin">
            <xsl:with-param name="value" select="@value"/>
          </xsl:call-template>
        </marginTop>
      </xsl:when>

      <!--

        Transform menu

        -->
      <xsl:when test="@name='menu'">
        <xsl:message terminate="no">WARNING: Dropping attribute '<xsl:value-of select="@name"/>' in theme <xsl:value-of select="../../../@name"/>
        </xsl:message>
      </xsl:when>

      <!--

        Transform mode

        -->
      <xsl:when test="@name='mode'">
        <xsl:message terminate="no">WARNING: Dropping attribute '<xsl:value-of select="@name"/>' in theme <xsl:value-of select="../../../@name"/>
        </xsl:message>
      </xsl:when>

      <!--

        Transform options-caption-align

        -->
      <xsl:when test="@name='options-caption-align'">
        <xsl:message terminate="no">WARNING: Dropping attribute '<xsl:value-of select="@name"/>' in theme <xsl:value-of select="../../../@name"/>
        </xsl:message>
      </xsl:when>

      <!--

        Transform padding-bottom

        -->
      <xsl:when test="@name='padding-bottom'">
        <xsl:call-template name="getPositiveLengthTag">
          <xsl:with-param name="tag" select="'paddingBottom'"/>
          <xsl:with-param name="value" select="@value"/>
          <xsl:with-param name="allowNumber" select="'false'"/>
        </xsl:call-template>
      </xsl:when>

      <!--

        Transform padding-left

        -->
      <xsl:when test="@name='padding-left'">
        <xsl:call-template name="getPositiveLengthTag">
          <xsl:with-param name="tag" select="'paddingLeft'"/>
          <xsl:with-param name="value" select="@value"/>
          <xsl:with-param name="allowNumber" select="'false'"/>
        </xsl:call-template>
      </xsl:when>

      <!--

        Transform padding-right

        -->
      <xsl:when test="@name='padding-right'">
          <xsl:call-template name="getPositiveLengthTag">
            <xsl:with-param name="tag" select="'paddingRight'"/>
            <xsl:with-param name="value" select="@value"/>
            <xsl:with-param name="allowNumber" select="'false'"/>
          </xsl:call-template>
      </xsl:when>

     <!--

        Transform padding-top

        -->
      <xsl:when test="@name='padding-top'">
        <xsl:call-template name="getPositiveLengthTag">
          <xsl:with-param name="tag" select="'paddingTop'"/>
          <xsl:with-param name="value" select="@value"/>
          <xsl:with-param name="allowNumber" select="'false'"/>
        </xsl:call-template>
      </xsl:when>

      <!--

        Transform paragap

        -->
      <xsl:when test="@name='paragap'">
        <marinerParagraphGap>
          <xsl:call-template name="getLength">
            <xsl:with-param name="value" select="@value"/>
            <xsl:with-param name="allowNumber" select="'false'"/>
          </xsl:call-template>
        </marinerParagraphGap>
      </xsl:when>

      <!--

        Transform quality

        -->
      <xsl:when test="@name='quality'">
        <marinerMMFlashQuality>
          <xsl:choose>
            <xsl:when test="@value='autolow'">
              <xsl:attribute name="keyword">auto-low</xsl:attribute>
            </xsl:when>
            <xsl:when test="@value='autohigh'">
              <xsl:attribute name="keyword">auto-high</xsl:attribute>
            </xsl:when>
            <xsl:otherwise>
                <xsl:attribute name="keyword"><xsl:value-of select="@value"/></xsl:attribute>
            </xsl:otherwise>
          </xsl:choose>
        </marinerMMFlashQuality>
      </xsl:when>

      <!--

        Transform rowgap

      -->
      <xsl:when test="@name='rowgap'">
        <marinerRowGap>
          <xsl:call-template name="getLength">
            <xsl:with-param name="value" select="@value"/>
            <xsl:with-param name="allowNumber" select="'false'"/>
          </xsl:call-template>
        </marinerRowGap>
      </xsl:when>
      

      <!--

        Transform rows

        -->
      <xsl:when test="@name='rows'">
        <marinerRows>
          <xsl:attribute name="integer"><xsl:value-of select="@value"/></xsl:attribute>
        </marinerRows>
      </xsl:when>

      <!--

        Transform salign

        -->
      <xsl:when test="@name='salign'">
        <xsl:choose>
          <xsl:when test="@value='bottom' or @value='b'">
            <marinerMMFlashScaledAlign>
              <marinerMMFlashXPosition>
                <xsl:attribute name="keyword">center</xsl:attribute>
              </marinerMMFlashXPosition>
              <marinerMMFlashYPosition>
                <xsl:attribute name="keyword">bottom</xsl:attribute>
              </marinerMMFlashYPosition>
            </marinerMMFlashScaledAlign>
          </xsl:when>
          <xsl:when test="@value='bottomleft' or @value='bl'">
            <marinerMMFlashScaledAlign>
              <marinerMMFlashXPosition>
                <xsl:attribute name="keyword">left</xsl:attribute>
              </marinerMMFlashXPosition>
              <marinerMMFlashYPosition>
                <xsl:attribute name="keyword">bottom</xsl:attribute>
              </marinerMMFlashYPosition>
            </marinerMMFlashScaledAlign>
          </xsl:when>
          <xsl:when test="@value='bottomright' or @value='br'">
            <marinerMMFlashScaledAlign>
              <marinerMMFlashXPosition>
                <xsl:attribute name="keyword">right</xsl:attribute>
              </marinerMMFlashXPosition>
              <marinerMMFlashYPosition>
                <xsl:attribute name="keyword">bottom</xsl:attribute>
              </marinerMMFlashYPosition>
            </marinerMMFlashScaledAlign>
          </xsl:when>
          <xsl:when test="@value='center' or @value='c'">
            <marinerMMFlashScaledAlign>
              <marinerMMFlashXPosition>
                <xsl:attribute name="keyword">center</xsl:attribute>
              </marinerMMFlashXPosition>
              <marinerMMFlashYPosition>
                <xsl:attribute name="keyword">center</xsl:attribute>
              </marinerMMFlashYPosition>
            </marinerMMFlashScaledAlign>
          </xsl:when>
          <xsl:when test="@value='left' or @value='l'">
            <marinerMMFlashScaledAlign>
              <marinerMMFlashXPosition>
                <xsl:attribute name="keyword">left</xsl:attribute>
              </marinerMMFlashXPosition>
            </marinerMMFlashScaledAlign>
          </xsl:when>
          <xsl:when test="@value='right' or @value='r'">
            <marinerMMFlashScaledAlign>
              <marinerMMFlashXPosition>
                <xsl:attribute name="keyword">right</xsl:attribute>
              </marinerMMFlashXPosition>
            </marinerMMFlashScaledAlign>
          </xsl:when>
          <xsl:when test="@value='top' or @value='t'">
            <marinerMMFlashScaledAlign>
              <marinerMMFlashXPosition>
                <xsl:attribute name="keyword">center</xsl:attribute>
              </marinerMMFlashXPosition>
              <marinerMMFlashYPosition>
                <xsl:attribute name="keyword">top</xsl:attribute>
              </marinerMMFlashYPosition>
            </marinerMMFlashScaledAlign>
          </xsl:when>
          <xsl:when test="@value='topleft' or @value='tl'">
            <marinerMMFlashScaledAlign>
              <marinerMMFlashXPosition>
                <xsl:attribute name="keyword">left</xsl:attribute>
              </marinerMMFlashXPosition>
              <marinerMMFlashYPosition>
                <xsl:attribute name="keyword">top</xsl:attribute>
              </marinerMMFlashYPosition>
            </marinerMMFlashScaledAlign>
          </xsl:when>
          <xsl:when test="@value='topright' or @value='tr'">
            <marinerMMFlashScaledAlign>
              <marinerMMFlashXPosition>
                <xsl:attribute name="keyword">right</xsl:attribute>
              </marinerMMFlashXPosition>
              <marinerMMFlashYPosition>
                <xsl:attribute name="keyword">top</xsl:attribute>
              </marinerMMFlashYPosition>
            </marinerMMFlashScaledAlign>
          </xsl:when>

          <xsl:otherwise>
            <xsl:message terminate="no">ERROR: Could not decode 'salign' attribute in theme <xsl:value-of select="../../../@name"/>
            </xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>

      <!--

        Transform scale

        -->
      <xsl:when test="@name='scale'">
        <xsl:choose>
          <xsl:when test="@value='exactfit'">
            <marinerMMFlashScale>
              <xsl:attribute name="keyword">exact-fit</xsl:attribute>
            </marinerMMFlashScale>
          </xsl:when>
          <xsl:when test="@value='noborder'">
            <marinerMMFlashScale>
              <xsl:attribute name="keyword">no-border</xsl:attribute>
            </marinerMMFlashScale>
          </xsl:when>
          <xsl:when test="@value='showall'">
            <marinerMMFlashScale>
              <xsl:attribute name="keyword">show-all</xsl:attribute>
            </marinerMMFlashScale>
          </xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="no">ERROR: Could not decode 'scale' attribute in theme <xsl:value-of select="../../../@name"/>
            </xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>

      <!--

        Transform table-align

        -->
      <xsl:when test="@name='table-align'">
        <textAlign>
          <xsl:attribute name="keyword"><xsl:value-of select="@value"/></xsl:attribute>
        </textAlign>
      </xsl:when>

      <!--

        Transform text-align

        -->
      <xsl:when test="@name='text-align'">
        <textAlign>
          <xsl:attribute name="keyword"><xsl:value-of select="@value"/></xsl:attribute>
        </textAlign>
      </xsl:when>

       <!--

        Transform text-decoration

        -->
      <xsl:when test="@name='text-decoration'">
        <xsl:choose>
          <xsl:when test="@value='underline'">
            <textDecoration>
              <xsl:attribute name="underline">true</xsl:attribute>
            </textDecoration>
          </xsl:when>
          <xsl:when test="@value='overline'">
            <textDecoration>
              <xsl:attribute name="overline">true</xsl:attribute>
            </textDecoration>
          </xsl:when>
          <xsl:when test="@value='line-through'">
            <textDecoration>
              <xsl:attribute name="line-through">true</xsl:attribute>
            </textDecoration>
          </xsl:when>
          <xsl:when test="@value='blink'">
            <textDecoration>
              <xsl:attribute name="blink">true</xsl:attribute>
            </textDecoration>
          </xsl:when>
          <xsl:when test="@value='none'">
            <textDecoration>
              <xsl:attribute name="keyword">none</xsl:attribute>
            </textDecoration>
          </xsl:when>
          <xsl:otherwise>
            <xsl:message terminate="no">ERROR: Could not decode 'text-decoration' attribute in theme <xsl:value-of select="../../../@name"/>
            </xsl:message>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>

      <!--

        Transform text-indent

        -->
      <xsl:when test="@name='text-indent'">
        <textIndent>
          <xsl:call-template name="getLength">
            <xsl:with-param name="value"><xsl:value-of select="@value"/></xsl:with-param>
            <xsl:with-param name="allowNumber" select="'false'"/>
          </xsl:call-template>
        </textIndent>
      </xsl:when>

      <!--

        Transform text-transform

        -->
      <xsl:when test="@name='text-transform'">
        <textTransform>
          <xsl:attribute name="keyword"><xsl:value-of select="@value"/></xsl:attribute>
        </textTransform>
      </xsl:when>

      <!--

        Transform vertical-align

        -->
      <xsl:when test="@name='vertical-align'">
        <verticalAlign>
          <xsl:choose>
            <xsl:when test="@value='baseline'">
              <xsl:attribute name="keyword">baseline</xsl:attribute>
            </xsl:when>
            <xsl:when test="@value='bottom'">
              <xsl:attribute name="keyword">bottom</xsl:attribute>
            </xsl:when>
            <xsl:when test="@value='text-bottom'">
              <xsl:attribute name="keyword">text-bottom</xsl:attribute>
            </xsl:when>
            <xsl:when test="@value='sub'">
              <xsl:attribute name="keyword">sub</xsl:attribute>
            </xsl:when>
            <xsl:when test="@value='middle'">
              <xsl:attribute name="keyword">middle</xsl:attribute>
            </xsl:when>
            <xsl:when test="@value='super'">
              <xsl:attribute name="keyword">super</xsl:attribute>
            </xsl:when>
            <xsl:when test="@value='top'">
              <xsl:attribute name="keyword">top</xsl:attribute>
            </xsl:when>
            <xsl:when test="@value='text-top'">
              <xsl:attribute name="keyword">text-top</xsl:attribute>
            </xsl:when>
            <xsl:otherwise>
              <xsl:call-template name="getLength">
                <xsl:with-param name="value"><xsl:value-of select="@value"/></xsl:with-param>
                <xsl:with-param name="allowNumber" select="'false'"/>
              </xsl:call-template>
            </xsl:otherwise>
          </xsl:choose>
        </verticalAlign>
      </xsl:when>

      <!--

        Transform view

        -->
      <xsl:when test="@name='view'">
        <marinerLinkMedia>
          <xsl:attribute name="keyword"><xsl:value-of select="@value"/></xsl:attribute>
        </marinerLinkMedia>
      </xsl:when>

      <!--

        Transform volantisOrientation

        -->
      <xsl:when test="@name='volantisOrientation'">
        <marinerMenuOrientation>
          <xsl:attribute name="keyword"><xsl:value-of select="@value"/></xsl:attribute>
        </marinerMenuOrientation>
      </xsl:when>

      <!--

        Transform vspace

        -->
      <xsl:when test="@name='vspace'">
        <xsl:message terminate="no">WARNING: Dropping attribute '<xsl:value-of select="@name"/>' in theme <xsl:value-of select="../../../@name"/>
        </xsl:message>
      </xsl:when>

      <!--

        Transform width

        -->
      <xsl:when test="@name='width'">
        <xsl:choose>
          <xsl:when test="@value='auto'">
            <width>
              <xsl:attribute name="keyword">auto</xsl:attribute>
            </width>
          </xsl:when>
          <xsl:otherwise>
            <xsl:call-template name="getPositiveLengthTag">
              <xsl:with-param name="tag" select="'width'"/>
              <xsl:with-param name="value" select="@value"/>
              <xsl:with-param name="allowNumber" select="'false'"/>
            </xsl:call-template>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>

      <!--

        Transform widthhint

        -->
      <xsl:when test="@name='widthhint'">
        <marinerChartWidth>
          <xsl:attribute name="percentage"><xsl:value-of select="@value"/></xsl:attribute>
        </marinerChartWidth>
      </xsl:when>

      <!--

        Transform white-space

        -->
      <xsl:when test="@name='white-space'">
        <whiteSpace>
          <xsl:attribute name="keyword"><xsl:value-of select="@value"/></xsl:attribute>
        </whiteSpace>
      </xsl:when>

      <!--

        Transform wmode

        -->
      <xsl:when test="@name='wmode'">
        <marinerMMFlashWindowsMode>
          <xsl:attribute name="keyword"><xsl:value-of select="@value"/></xsl:attribute>
        </marinerMMFlashWindowsMode>
      </xsl:when>

      <!--

        Transform word-spacing

        -->
      <xsl:when test="@name='word-spacing'">
        <wordSpacing>
          <xsl:call-template name="getSpacing">
            <xsl:with-param name="value" select="@value"/>
          </xsl:call-template>
        </wordSpacing>
      </xsl:when>

      <!--

        Transform x-angle

        -->
      <xsl:when test="@name='x-angle'">
        <marinerChartXAxisAngle>
          <xsl:attribute name="angle"><xsl:value-of select="@value"/></xsl:attribute>
          <xsl:attribute name="angleUnits">deg</xsl:attribute>
        </marinerChartXAxisAngle>
      </xsl:when>

      <!--

        Transform y-angle

        -->
      <xsl:when test="@name='y-angle'">
        <marinerChartYAxisAngle>
          <xsl:attribute name="angle"><xsl:value-of select="@value"/></xsl:attribute>
          <xsl:attribute name="angleUnits">deg</xsl:attribute>
        </marinerChartYAxisAngle>
      </xsl:when>

      <xsl:otherwise>
        <xsl:message terminate="no">ERROR: Could not decode '<xsl:value-of select="@name"/>' attribute in theme <xsl:value-of select="../../../@name"/>
        </xsl:message>
      </xsl:otherwise>
    </xsl:choose>
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
