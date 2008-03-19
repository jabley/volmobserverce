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

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:slpdm="http://www.volantis.com/xmlns/marlin-lpdm"
    xmlns:lpdm="http://www.volantis.com/xmlns/2005/09/marlin-lpdm"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    exclude-result-prefixes="slpdm">

  <xsl:strip-space elements="*"/>
  <xsl:output method="xml" indent="yes" xalan:indent-amount="4" xmlns:xalan="http://xml.apache.org/xslt"/>

  <xsl:template match="/">
    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="slpdm:theme">
    <lpdm:theme>
      <xsl:apply-templates select="@*|node()"/>
    </lpdm:theme>
  </xsl:template>

  <xsl:template match="slpdm:deviceTheme">
    <lpdm:deviceTheme>
      <xsl:attribute name="deviceName"><xsl:value-of select="@deviceName"/></xsl:attribute>
      <xsl:apply-templates/>
    </lpdm:deviceTheme>
  </xsl:template>

  <xsl:template match="slpdm:rule">
      <lpdm:rule>
          <xsl:apply-templates select="slpdm:simpleSelectorSequence"/>
          <xsl:apply-templates select="slpdm:simpleSelectorSequence[.//slpdm:typeSelector[@type='xfsiselect']]">
              <xsl:with-param name="new-type" select="'xfoption'"/>
          </xsl:apply-templates>
          <xsl:apply-templates select="slpdm:styleProperties"/>
      </lpdm:rule>
  </xsl:template>

  <!--
    - The simple selector sequence now has an imposed ordering - apply templates
    - in such a way as to enforce this ordering.
    -->
  <xsl:template match="slpdm:simpleSelectorSequence">
      <xsl:param name="new-type"/>
    <lpdm:subjectSelectorSequence>
      <!--
        - First is either a type selector or universal selector. In theory it is
        - possible to have more than one under the old schema, though this would
        - be largely meaningless. If this happens, take the first and leave a
        - comment in the output.
        -->
      <xsl:apply-templates select="(slpdm:typeSelector | slpdm:universalSelector)[1]">
          <xsl:with-param name="new-type" select="$new-type"/>
      </xsl:apply-templates>
      <xsl:if test="(slpdm:typeSelector | slpdm:universalSelector)[2]">
        <xsl:comment>The source from which this document was generated contained multiple type/universal selectors, which is not valid. Only the first has been retained.</xsl:comment>
      </xsl:if>

      <!--
        - Any number of class selectors are allowed
        -->
      <xsl:apply-templates select="slpdm:classSelector"/>

      <!--
        - Only one ID selector is valid - again, the old schema allows more than
        - one in theory, which needs to be prevented.
        -->
      <xsl:apply-templates select="slpdm:idSelector[1]"/>
      <xsl:if test="slpdm:idSelector[2]">
        <xsl:comment>The source from which this document was generated contained multiple ID selectors, which is not valid. Only the first has been retained.</xsl:comment>
      </xsl:if>

      <!--
        - Attribute selectors are merged into a single parent, and are now
        - distinguished as elements rather than attributes. Any number of any
        - types are allowed, as they may apply to different attributes.
        -->
      <xsl:if test="slpdm:attributeSelector">
        <lpdm:attributeSelectors>
          <xsl:apply-templates select="slpdm:attributeSelector"/>
        </lpdm:attributeSelectors>
      </xsl:if>

      <!--
        - Structural pseudo-class selectors (first-child and nth-child) are
        - next. Only first-child is supported in the older schema, and
        - duplicates can be safely stripped since there is no distinction
        - between them.
        -->
      <xsl:if test="slpdm:pseudoClassSelector[@identifier='first-child']">
        <lpdm:structuralPseudoClassSelectors>
          <xsl:apply-templates select="slpdm:pseudoClassSelector[@identifier='first-child'][1]"/>
        </lpdm:structuralPseudoClassSelectors>
      </xsl:if>

      <!--
        - Only one of the old-style pseudo-elements is permitted (though this
        - may correspond to two pseudo-elements in the new schema).
        -->
      <xsl:if test="slpdm:pseudoElementSelector">
        <lpdm:pseudoElementSelectors>
          <xsl:apply-templates select="slpdm:pseudoElementSelector[1]"/>
          <xsl:if test="slpdm:pseudoElementSelector[2]">
            <xsl:comment>The source from which this document was generated contained multiple pseudo-element selectors, which is not valid. Only the first has been retained.</xsl:comment>
          </xsl:if>
        </lpdm:pseudoElementSelectors>
      </xsl:if>

      <!--
        - Stateful pseudo-class selectors are the final selector type. These
        - must be in strict sequence, and only one of visited/link is permitted.
        - In all other cases, duplicates will be silently ignored since as with
        - the structural pseudo-class selectors
        -->
      <xsl:if test="slpdm:pseudoClassSelector[@identifier != 'first-child']">
        <lpdm:statefulPseudoClassSelectors>
          <xsl:apply-templates select="slpdm:pseudoClassSelector[@identifier = 'link' or @identifier = 'visited'][1]"/>
          <xsl:if test="slpdm:pseudoClassSelector[@identifier = 'link' or @identifier = 'visited'][2]">
            <xsl:comment>The source from which this document was generated contained multiple pseudo-class selectors for link and/or visited, which is not valid. Only the first has been retained.</xsl:comment>
          </xsl:if>
          <xsl:apply-templates select="slpdm:pseudoClassSelector[@identifier = 'active'][1]"/>
          <xsl:apply-templates select="slpdm:pseudoClassSelector[@identifier = 'focus'][1]"/>
          <xsl:apply-templates select="slpdm:pseudoClassSelector[@identifier = 'hover'][1]"/>
        </lpdm:statefulPseudoClassSelectors>
      </xsl:if>
    </lpdm:subjectSelectorSequence>
  </xsl:template>

  <!--
    - Copy across selectors that have not changed in the new version
    -->
  <xsl:template match="slpdm:typeSelector">
      <xsl:param name="new-type"/>
      <xsl:variable name="type">
          <xsl:choose>
              <xsl:when test="$new-type != ''">
                  <xsl:value-of select="$new-type"/>
              </xsl:when>

              <xsl:otherwise>
                  <xsl:value-of select="@type"/>
              </xsl:otherwise>
          </xsl:choose>
      </xsl:variable>

    <lpdm:typeSelector>
      <xsl:attribute name="type"><xsl:value-of select="$type"/></xsl:attribute>
    </lpdm:typeSelector>
  </xsl:template>
  <xsl:template match="slpdm:universalSelector">
    <lpdm:universalSelector>
      <xsl:if test="@namespace">
        <xsl:attribute name="namespace"><xsl:value-of select="@namespace"/></xsl:attribute>
      </xsl:if>
    </lpdm:universalSelector>
  </xsl:template>
  <xsl:template match="slpdm:classSelector">
    <lpdm:classSelector>
      <xsl:attribute name="class"><xsl:value-of select="@class"/></xsl:attribute>
    </lpdm:classSelector>
  </xsl:template>
  <xsl:template match="slpdm:idSelector">
    <lpdm:idSelector>
      <xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
    </lpdm:idSelector>
  </xsl:template>

  <!--
    - Handle all defined attribute selectors
    -->
  <xsl:template match="slpdm:attributeSelector[@operator='set']">
    <lpdm:set>
      <xsl:attribute name="attribute"><xsl:value-of select="@name"/></xsl:attribute>
    </lpdm:set>
  </xsl:template>
  <xsl:template match="slpdm:attributeSelector[@operator='equals']">
    <lpdm:equals>
      <xsl:attribute name="attribute"><xsl:value-of select="@name"/></xsl:attribute>
      <xsl:attribute name="value"><xsl:value-of select="@value"/></xsl:attribute>
    </lpdm:equals>
  </xsl:template>
  <xsl:template match="slpdm:attributeSelector[@operator='include' or @operator='includes']">
    <lpdm:contains-word>
      <xsl:attribute name="attribute"><xsl:value-of select="@name"/></xsl:attribute>
      <xsl:attribute name="value"><xsl:value-of select="@value"/></xsl:attribute>
    </lpdm:contains-word>
  </xsl:template>
  <xsl:template match="slpdm:attributeSelector[@operator='dashmatch' or @operator='dashMatch']">
    <lpdm:language-match>
      <xsl:attribute name="attribute"><xsl:value-of select="@name"/></xsl:attribute>
      <xsl:attribute name="value"><xsl:value-of select="@value"/></xsl:attribute>
    </lpdm:language-match>
  </xsl:template>

  <!--
    - Handle all defined pseudo-class selectors.
    -->
  <xsl:template match="slpdm:pseudoClassSelector[@identifier='first-child']">
    <lpdm:first-child/>
  </xsl:template>
  <xsl:template match="slpdm:pseudoClassSelector[@identifier='active']">
    <lpdm:active/>
  </xsl:template>
  <xsl:template match="slpdm:pseudoClassSelector[@identifier='focus']">
    <lpdm:focus/>
  </xsl:template>
  <xsl:template match="slpdm:pseudoClassSelector[@identifier='hover']">
    <lpdm:hover/>
  </xsl:template>
  <xsl:template match="slpdm:pseudoClassSelector[@identifier='link']">
    <lpdm:link/>
  </xsl:template>
  <xsl:template match="slpdm:pseudoClassSelector[@identifier='visited']">
    <lpdm:visited/>
  </xsl:template>

  <!--
    - Handle all defined pseudo-element selectors.
    -->
  <xsl:template match="slpdm:pseudoElementSelector[@identifier='first-line']">
    <lpdm:first-line/>
  </xsl:template>
  <xsl:template match="slpdm:pseudoElementSelector[@identifier='first-letter']">
    <lpdm:first-letter/>
  </xsl:template>
  <xsl:template match="slpdm:pseudoElementSelector[@identifier='mcs-shortcut']">
    <lpdm:mcs-shortcut/>
  </xsl:template>
  <xsl:template match="slpdm:pseudoElementSelector[@identifier='mcs-shortcut-after']">
    <lpdm:mcs-shortcut/>
    <lpdm:after/>
  </xsl:template>

  <xsl:template match="slpdm:styleProperties">
    <lpdm:styleProperties>
      <xsl:apply-templates/>
    </lpdm:styleProperties>
  </xsl:template>

  <!--
    - Copy anything unrecognised with low priority, updating the namespace
    -->
  <xsl:template match="slpdm:*" priority="-3">
      <xsl:element name="lpdm:{local-name()}">
          <xsl:apply-templates select="@*|node()"/>
      </xsl:element>
  </xsl:template>

    <xsl:template match="comment()" priority="-3">
        <xsl:comment>
            <xsl:value-of select="."/>
        </xsl:comment>
    </xsl:template>

  <xsl:template match="*|@*" priority="-5">
      <xsl:copy>
          <xsl:apply-templates select="@*|node()"/>
      </xsl:copy>
  </xsl:template>

  <!--
    - Upgrade schema version where appropriate
    -->
   <xsl:template match="@xsi:schemaLocation[.='http://www.volantis.com/xmlns/marlin-lpdm http://www.volantis.com/schema/v3.0/marlin-lpdm.xsd']">
     <xsl:attribute name="xsi:schemaLocation">http://www.volantis.com/xmlns/2005/09/marlin-lpdm http://www.volantis.com/schema/2005/09/marlin-lpdm.xsd</xsl:attribute>
   </xsl:template>

    <!--
     ! ========================================================================
     ! Migrate values.
     ! ========================================================================
     !-->
    <xsl:template match="slpdm:styleProperties/slpdm:*">
        <xsl:element name="lpdm:{local-name()}">
            <xsl:if test="@priority">
                <xsl:attribute name="priority">
                    <xsl:value-of select="@priority"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:apply-templates mode="migrate-values" select="."/>
        </xsl:element>
        <!--
         ! If the current element is background-color and it has no sibling
         ! color element and it is part of a rule that consists only of
         ! simple selector sequences that have type selectors for hr then
         ! create a color element with the same value as this element.
         !-->
        <xsl:if test="self::slpdm:background-color and parent::*[not(slpdm:color)] and count(ancestor::*[2]/slpdm:simpleSelectorSequence) = count(ancestor::*[2]/slpdm:simpleSelectorSequence[slpdm:typeSelector[@type='hr']])">
            <lpdm:color>
                <xsl:if test="@priority">
                    <xsl:attribute name="priority">
                        <xsl:value-of select="@priority"/>
                    </xsl:attribute>
                </xsl:if>
                <xsl:apply-templates mode="migrate-values" select="."/>
            </lpdm:color>
        </xsl:if>
    </xsl:template>

    <xsl:template mode="migrate-values" match="text()"/>

    <xsl:template mode="migrate-values" match="slpdm:*">
        <xsl:choose>

            <!-- Angle -->
            <xsl:when test="@angle">
                <lpdm:angle units="deg">
                    <xsl:if test="@angleUnits">
                        <xsl:attribute name="units">
                            <xsl:value-of select="@angleUnits"/>
                        </xsl:attribute>
                    </xsl:if>
                    <xsl:value-of select="@angle"/>
                </lpdm:angle>
            </xsl:when>

            <!-- Color -->
            <xsl:when test="@rgb">
                <lpdm:colorRGB>
                    <xsl:value-of select="@rgb"/>
                </lpdm:colorRGB>
            </xsl:when>

            <xsl:when test="@color">
                <lpdm:colorName>
                    <xsl:value-of select="@color"/>
                </lpdm:colorName>
            </xsl:when>

            <xsl:when test="@red or @blue or @green">
                <lpdm:colorPercentages red="0" green="0" blue="0">
                    <xsl:if test="@red">
                        <xsl:attribute name="red">
                            <xsl:value-of select="@red"/>
                        </xsl:attribute>
                    </xsl:if>
                    <xsl:if test="@green">
                        <xsl:attribute name="green">
                            <xsl:value-of select="@green"/>
                        </xsl:attribute>
                    </xsl:if>
                    <xsl:if test="@blue">
                        <xsl:attribute name="blue">
                            <xsl:value-of select="@blue"/>
                        </xsl:attribute>
                    </xsl:if>
                </lpdm:colorPercentages>
            </xsl:when>

            <!-- Inherit -->
            <xsl:when test="@inherit">
                <lpdm:inherit/>
            </xsl:when>

            <!-- Invalid -->
            <xsl:when test="@invalid">
                <lpdm:invalidStyle>
                    <xsl:value-of select="@invalid"/>
                </lpdm:invalidStyle>
            </xsl:when>

            <!-- Integer -->
            <xsl:when test="@integer">
                <lpdm:integer>
                    <xsl:value-of select="@integer"/>
                </lpdm:integer>
            </xsl:when>

            <!-- Keyword -->
            <xsl:when test="@keyword">
                <lpdm:keyword>
                    <xsl:value-of select="@keyword"/>
                </lpdm:keyword>
            </xsl:when>

            <!-- Length -->
            <xsl:when test="@length">
                <lpdm:length units="pt">
                    <xsl:if test="@lengthUnits">
                        <xsl:attribute name="units">
                            <xsl:value-of select="@lengthUnits"/>
                        </xsl:attribute>
                    </xsl:if>
                    <xsl:value-of select="@length"/>
                </lpdm:length>
            </xsl:when>

            <!-- MCSComponentURI -->
            <xsl:when test="@mcsComponentURI">
                <lpdm:mcsComponentURI>
                    <xsl:value-of select="@mcsComponentURI"/>
                </lpdm:mcsComponentURI>
            </xsl:when>

            <!-- Number -->
            <xsl:when test="@number">
                <lpdm:number>
                    <xsl:value-of select="@number"/>
                </lpdm:number>
            </xsl:when>

            <!-- Percentage -->
            <xsl:when test="@percentage">
                <lpdm:percentage>
                    <xsl:value-of select="@percentage"/>
                </lpdm:percentage>
            </xsl:when>

            <!-- String -->
            <xsl:when test="@string">
                <lpdm:string>
                    <xsl:value-of select="@string"/>
                </lpdm:string>
            </xsl:when>

            <!-- Time -->
            <xsl:when test="@time">
                <lpdm:time units="s">
                    <xsl:if test="@timeUnits">
                        <xsl:attribute name="units">
                            <xsl:value-of select="@timeUnits"/>
                        </xsl:attribute>
                    </xsl:if>
                    <xsl:value-of select="@time"/>
                </lpdm:time>
            </xsl:when>

            <!-- URI -->
            <xsl:when test="@uri">
                <lpdm:uri>
                    <xsl:value-of select="@uri"/>
                </lpdm:uri>
            </xsl:when>

            <!-- List / Ordered Set -->
            <xsl:when test="slpdm:item">
                <xsl:variable name="unique">
                    <xsl:choose>
                        <xsl:when test="self::slpdm:font-family">
                            <xsl:text>true</xsl:text>
                        </xsl:when>

                        <xsl:when test="self::slpdm:mcs-chart-foreground-colors or self::slpdm:counter-reset or self::slpdm:counter-increment">
                            <xsl:text>false</xsl:text>
                        </xsl:when>

                        <xsl:otherwise>
                            <xsl:message terminate="yes">
                                <xsl:text>Unknown list/orderedSet property '</xsl:text>
                                <xsl:value-of select="local-name()"/>
                                <xsl:text>'</xsl:text>
                            </xsl:message>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:variable>
                <lpdm:list unique="{$unique}">
                    <xsl:for-each select="slpdm:item">
                        <xsl:apply-templates mode="migrate-values" select="."/>
                    </xsl:for-each>
                </lpdm:list>
            </xsl:when>

            <!-- Pair -->
            <xsl:when test="slpdm:first">
                <lpdm:pair>
                    <lpdm:first>
                        <xsl:apply-templates mode="migrate-values" select="slpdm:first"/>
                    </lpdm:first>
                    <xsl:if test="slpdm:second">
                        <lpdm:second>
                            <xsl:apply-templates mode="migrate-values" select="slpdm:second"/>
                        </lpdm:second>
                     </xsl:if>
                </lpdm:pair>
            </xsl:when>
            <xsl:when test="slpdm:second">

            </xsl:when>

        </xsl:choose>
    </xsl:template>

    <!--
     ! ========================================================================
     ! Migrate any properties that need it.
     ! ========================================================================
     !-->

    <!--
     ! text-decoration is split into a number of properties.
     !-->
    <xsl:template match="slpdm:styleProperties/slpdm:text-decoration">
        <xsl:choose>
            <xsl:when test="@blink='true'">
                <lpdm:mcs-text-blink>
                    <lpdm:keyword>blink</lpdm:keyword>
                </lpdm:mcs-text-blink>
            </xsl:when>
            <xsl:otherwise>
                <lpdm:mcs-text-blink>
                    <lpdm:keyword>none</lpdm:keyword>
                </lpdm:mcs-text-blink>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:choose>
            <xsl:when test="@line-through='true'">
                <lpdm:mcs-text-line-through-style>
                    <lpdm:keyword>solid</lpdm:keyword>
                </lpdm:mcs-text-line-through-style>
            </xsl:when>
            <xsl:otherwise>
                <lpdm:mcs-text-line-through-style>
                    <lpdm:keyword>none</lpdm:keyword>
                </lpdm:mcs-text-line-through-style>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:choose>
            <xsl:when test="@overline='true'">
                <lpdm:mcs-text-overline-style>
                    <lpdm:keyword>solid</lpdm:keyword>
                </lpdm:mcs-text-overline-style>
            </xsl:when>
            <xsl:otherwise>
                <lpdm:mcs-text-overline-style>
                    <lpdm:keyword>none</lpdm:keyword>
                </lpdm:mcs-text-overline-style>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:choose>
            <xsl:when test="@underline='true'">
                <lpdm:mcs-text-underline-style>
                    <lpdm:keyword>solid</lpdm:keyword>
                </lpdm:mcs-text-underline-style>
            </xsl:when>
            <xsl:otherwise>
                <lpdm:mcs-text-underline-style>
                    <lpdm:keyword>none</lpdm:keyword>
                </lpdm:mcs-text-underline-style>
            </xsl:otherwise>
        </xsl:choose>

    </xsl:template>


    <!--
     ! content containing a string is turned into a list.
     !-->
    <xsl:template mode="migrate-values"
                  match="slpdm:styleProperties/slpdm:content[@string]">

        <lpdm:list unique="false">
            <lpdm:string>
                <xsl:value-of select="@string"/>
            </lpdm:string>
        </lpdm:list>
        
    </xsl:template>

</xsl:stylesheet>
<!--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 14-Dec-05	10835/1	geoff	VBM:2005121405 P900; superscript does not work with CssMobleProfile

 14-Dec-05	10830/1	pduffin	VBM:2005121405 Migrate rules containing only selector sequences for hr elements so that if they do not have a color property that it is set from the background-color property

 16-Dec-05	10845/1	pduffin	VBM:2005121511 Moved architecture files from CVS to MCS Depot

 06-Dec-05	10619/1	ibush	VBM:2005113017 Fix xfoption typeSelectors

 06-Dec-05	10606/1	ibush	VBM:2005113017 Fix xfoption typeSelectors

 21-Oct-05	9938/1	ianw	VBM:2005101915 Fix up theme accessor issues

 21-Oct-05	9936/3	ianw	VBM:2005101915 Fix up theme accessor issues

 21-Oct-05	9936/1	ianw	VBM:2005101915 Fix up theme accessor issues

 11-Oct-05	9729/2	geoff	VBM:2005100507 Mariner Export fails with NPE

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 16-Sep-05	9512/3	pduffin	VBM:2005091408 Added support for invalid style values

 13-Sep-05	9496/2	pduffin	VBM:2005091211 Replaced text-decoration with individual properties

 18-May-05	8181/5	adrianj	VBM:2005050505 XDIME/CP Migration CLI

 13-May-05	8181/1	adrianj	VBM:2005050505 XSL for theme migration

 ===========================================================================
-->
