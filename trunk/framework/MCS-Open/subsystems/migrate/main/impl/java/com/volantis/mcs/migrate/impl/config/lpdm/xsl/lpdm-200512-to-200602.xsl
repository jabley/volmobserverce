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
    xmlns:old="http://www.volantis.com/xmlns/2005/12/marlin-lpdm"
    xmlns="http://www.volantis.com/xmlns/2006/02/marlin-lpdm"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    exclude-result-prefixes="old">

    <!--
     ! Strip whitespace from all input elements apart from the explicitly
     ! specified elements.
     !-->
    <xsl:strip-space elements="*"/>

    <!--
     ! All content of the string element on input is significant, even white
     ! space.
     !-->
    <xsl:preserve-space elements="old:string"/>

    <xsl:output method="xml" indent="yes"
        xmlns:xalan="http://xml.apache.org/xslt"
        xalan:indent-amount="4"/>

    <xsl:template match="/">
        <xsl:apply-templates/>
    </xsl:template>

    <!--
     ! Only add a local schema location if the root element on input is an
     ! old lpdm element.
     !-->
    <xsl:template name="AddLocalSchemaLocation">
        <xsl:if test="/old:*">
            <xsl:attribute name="xsi:schemaLocation">http://www.volantis.com/xmlns/2006/02/marlin-lpdm http://www.volantis.com/schema/2006/02/marlin-lpdm.xsd</xsl:attribute>
        </xsl:if>
    </xsl:template>

    <!--
     ! =========================================================================
     !     Cache Control
     ! =========================================================================
     !-->
    <xsl:template name="CacheControl">
        <xsl:if test="@cacheThisPolicy or @timeToLive or @retryInterval or @retryMaxCount or @retryFailedRetrieval or @retainDuringRetry">
            <cache-control>
                <xsl:if test="@cacheThisPolicy">
                    <cache-this-policy>
                        <xsl:value-of select="@cacheThisPolicy"/>
                    </cache-this-policy>
                </xsl:if>
                <xsl:if test="@retainDuringRetry">
                    <retain-during-retry>
                        <xsl:value-of select="@retainDuringRetry"/>
                    </retain-during-retry>
                </xsl:if>
                <xsl:if test="@retryFailedRetrieval">
                    <retry-failed-retrieval>
                        <xsl:value-of select="@retryFailedRetrieval"/>
                    </retry-failed-retrieval>
                </xsl:if>
                <xsl:if test="@retryInterval">
                    <retry-interval>
                        <xsl:value-of select="@retryInterval"/>
                    </retry-interval>
                </xsl:if>
                <xsl:if test="@retryMaxCount">
                    <retry-max-count>
                        <xsl:value-of select="@retryMaxCount"/>
                    </retry-max-count>
                </xsl:if>
                <xsl:if test="@timeToLive">
                    <time-to-live>
                        <xsl:value-of select="@timeToLive"/>
                    </time-to-live>
                </xsl:if>
            </cache-control>
        </xsl:if>
    </xsl:template>

    <!--
     ! Ignore all the caching attributes when matched as a separate template.
     !-->
    <xsl:template match="@cacheThisPolicy|@retainDuringRetry|@retryFailedRetrieval|@retryInterval|@retryMaxCount|@timeToLive"/>

    <!--
     ! =========================================================================
     !     Device Targeted Selection
     ! =========================================================================
     !-->
    <xsl:template name="DeviceTargetedSelection">
        <selection>
            <targeted>
                <devices>
                    <device>
                        <xsl:value-of select="@deviceName"/>
                    </device>
                </devices>
            </targeted>
        </selection>
    </xsl:template>

    <!--
     ! =========================================================================
     !     URL Content Selection
     ! =========================================================================
     !-->
    <xsl:template name="BaseURL">
        <xsl:if test="@assetGroupName or @localSrc = 'local'">
            <base>
                <xsl:if test="@assetGroupName">
                    <policy-reference>
                        <name>
                            <xsl:value-of select="@assetGroupName"/>
                        </name>
                        <type>base-url</type>
                    </policy-reference>
                </xsl:if>
                <xsl:if test="@localSrc = 'local'">
                    <location>device</location>
                </xsl:if>
            </base>
        </xsl:if>
    </xsl:template>

    <xsl:template name="Content">
        <content>
            <xsl:choose>
                <xsl:when test="@sequence = 'true'">
                    <auto-url-sequence>
                        <xsl:call-template name="BaseURL"/>
                        <template>
                            <xsl:value-of select="@value"/>
                        </template>
                        <size>
                            <xsl:value-of select="@sequenceSize"/>
                        </size>
                    </auto-url-sequence>
                </xsl:when>
                <xsl:when test="@valueType='literal'">
                    <embedded>
                        <data>
                            <xsl:value-of select="@value"/>
                        </data>
                    </embedded>
                </xsl:when>
                <xsl:otherwise>
                    <url>
                        <xsl:call-template name="BaseURL"/>
                        <relative>
                            <xsl:value-of select="@value"/>
                        </relative>
                    </url>
                </xsl:otherwise>
            </xsl:choose>
        </content>
    </xsl:template>

    <!--
     ! =========================================================================
     !     Alternates Selection
     ! =========================================================================
     !-->
    <xsl:template name="Alternates">
        <xsl:if test="@*[substring-after(local-name(.), 'fallback')]">
            <alternates>
                <xsl:if test="@fallbackDynVisComponentName">
                    <alternate>
                        <policy-reference>
                            <name><xsl:value-of select="@fallbackDynVisComponentName"/></name>
                            <type>video</type>
                        </policy-reference>
                    </alternate>
                </xsl:if>
                <xsl:if test="@fallbackAudioComponentName">
                    <alternate>
                        <policy-reference>
                            <name><xsl:value-of select="@fallbackAudioComponentName"/></name>
                            <type>audio</type>
                        </policy-reference>
                    </alternate>
                </xsl:if>
                <xsl:if test="@fallbackChartComponentName">
                    <alternate>
                        <policy-reference>
                            <name><xsl:value-of select="@fallbackChartComponentName"/></name>
                            <type>chart</type>
                        </policy-reference>
                    </alternate>
                </xsl:if>
                <xsl:if test="@fallbackImageComponentName">
                    <alternate>
                        <policy-reference>
                            <name><xsl:value-of select="@fallbackImageComponentName"/></name>
                            <type>image</type>
                        </policy-reference>
                    </alternate>
                </xsl:if>
                <xsl:if test="@fallbackTextComponentName">
                    <alternate>
                        <policy-reference>
                            <name><xsl:value-of select="@fallbackTextComponentName"/></name>
                            <type>text</type>
                        </policy-reference>
                    </alternate>
                </xsl:if>
            </alternates>
        </xsl:if>
    </xsl:template>
    
    <!--
     ! =========================================================================
     !     Null Device Asset 2 Variant
     ! =========================================================================
     !-->
    <xsl:template match="old:nullDeviceAsset">
        <variant>
            <type>null</type>
            <xsl:call-template name="DeviceTargetedSelection"/>
        </variant>
    </xsl:template>

    <!--
     ! =========================================================================
     !     Asset Group 2 Base URL Policy
     ! =========================================================================
     !-->
    <xsl:template match="old:assetGroup">
        <base-url-policy>
            <xsl:call-template name="AddLocalSchemaLocation"/>
            <xsl:call-template name="CacheControl"/>
            <url>
                <xsl:value-of select="@prefixURL"/>
            </url>
            <xsl:choose>
                <xsl:when test="not(@locationType) or @locationType = 'server'">
                    <location>context</location>
                </xsl:when>
                <xsl:otherwise>
                    <location>
                        <xsl:value-of select="@locationType"/>
                    </location>
                </xsl:otherwise>
            </xsl:choose>
        </base-url-policy>
    </xsl:template>

    <xsl:template mode="get-policy-type" match="old:*">
        <xsl:value-of select="substring-before(local-name(), 'Component')"/>
    </xsl:template>

    <xsl:template mode="get-policy-type" match="old:dynamicVisualComponent">
        <xsl:text>video</xsl:text>
    </xsl:template>

    <!--
     ! =========================================================================
     !     Component 2 Variable Policy
     ! =========================================================================
     !-->
    <xsl:template match="old:*[substring-before(local-name(), 'Component') and not(substring-after(local-name(), 'Component'))]">
        <variable-policy>
            <xsl:call-template name="AddLocalSchemaLocation"/>
            <type>
                <xsl:apply-templates mode="get-policy-type" select="."/>
            </type>
            <xsl:call-template name="CacheControl"/>
            <xsl:call-template name="Alternates"/>
            <variants>
                <xsl:apply-templates/>
            </variants>
        </variable-policy>
    </xsl:template>

    <!--
     ! =========================================================================
     !     Audio Assets
     ! =========================================================================
     !-->
    <xsl:template name="AudioMetaData">
        <meta-data>
            <audio>
                <encoding>
                    <xsl:choose>
                        <xsl:when test="@encoding = 'realAudio'">real</xsl:when>
                        <xsl:when test="@encoding = 'adpcm32'">adpcm32</xsl:when>
                        <xsl:when test="@encoding = 'amr'">amr</xsl:when>
                        <xsl:when test="@encoding = 'basic'">basic</xsl:when>
                        <xsl:when test="@encoding = 'gsm'">gsm</xsl:when>
                        <xsl:when test="@encoding = 'iMelody'">imelody</xsl:when>
                        <xsl:when test="@encoding = 'midi'">midi</xsl:when>
                        <xsl:when test="@encoding = 'mp3'">mp3</xsl:when>
                        <xsl:when test="@encoding = 'nokring'">nokia-ring-tone</xsl:when>
                        <xsl:when test="@encoding = 'realAudio'">real</xsl:when>
                        <xsl:when test="@encoding = 'rmf'">rmf</xsl:when>
                        <xsl:when test="@encoding = 'smaf'">smaf</xsl:when>
                        <xsl:when test="@encoding = 'spMidi'">sp-midi</xsl:when>
                        <xsl:when test="@encoding = 'wav'">wav</xsl:when>
                        <xsl:when test="@encoding = 'windowsAudio'">windows-media</xsl:when>
                        <xsl:otherwise>
                            <xsl:message>
                                <xsl:text>Unknown encoding </xsl:text>
                                <xsl:value-of select="@encoding"/>
                            </xsl:message>
                        </xsl:otherwise>
                    </xsl:choose>
                </encoding>
            </audio>
        </meta-data>
    </xsl:template>

    <xsl:template match="old:audioAsset">
        <variant>
            <type>audio</type>
            <selection>
                <encoding/>
            </selection>
            <xsl:call-template name="AudioMetaData"/>
            <xsl:call-template name="Content"/>
        </variant>
    </xsl:template>

    <xsl:template match="old:deviceAudioAsset">
        <variant>
            <type>audio</type>
            <xsl:call-template name="DeviceTargetedSelection"/>
            <xsl:call-template name="AudioMetaData"/>
            <xsl:call-template name="Content"/>
        </variant>
    </xsl:template>

    <!--
     ! =========================================================================
     !     Chart Assets
     ! =========================================================================
     !-->
    <xsl:template match="old:chartAsset">
        <variant>
            <type>chart</type>
            <selection>
                <default/>
            </selection>
            <meta-data>
                <chart>
                    <type>
                        <xsl:value-of select="@type"/>
                    </type>
                    <height-hint>
                        <xsl:value-of select="@heightHint"/>
                    </height-hint>
                    <width-hint>
                        <xsl:value-of select="@widthHint"/>
                    </width-hint>
                    <x-axis>
                        <title>
                            <xsl:value-of select="@XTitle"/>
                        </title>
                        <interval>
                            <xsl:value-of select="@XInterval"/>
                        </interval>
                    </x-axis>
                    <y-axis>
                        <title>
                            <xsl:value-of select="@YTitle"/>
                        </title>
                        <interval>
                            <xsl:value-of select="@YInterval"/>
                        </interval>
                    </y-axis>
                </chart>
            </meta-data>
        </variant>
    </xsl:template>

    <!--
     ! =========================================================================
     !     Dynamic Visual Assets
     ! =========================================================================
     !-->
    <xsl:template match="old:dynamicVisualAsset">
        <variant>
            <type>video</type>
            <selection>
                <encoding/>
            </selection>
            <meta-data>
                <video>
                    <encoding>
                        <xsl:choose>
                            <xsl:when test="@encoding = '3gpp'">3gpp</xsl:when>
                            <xsl:when test="@encoding = 'flash'">macromedia-flash</xsl:when>
                            <xsl:when test="@encoding = 'gif'">animated-gif</xsl:when>
                            <xsl:when test="@encoding = 'mpeg1'">mpeg1</xsl:when>
                            <xsl:when test="@encoding = 'mpeg4'">mpeg4</xsl:when>
                            <xsl:when test="@encoding = 'quickTime'">quicktime</xsl:when>
                            <xsl:when test="@encoding = 'realVideo'">real</xsl:when>
                            <xsl:when test="@encoding = 'shock'">macromedia-shockwave</xsl:when>
                            <xsl:when test="@encoding = 'tv'">tv</xsl:when>
                            <xsl:when test="@encoding = 'windowsVideo'">windows-media</xsl:when>

                            <xsl:otherwise>
                                <xsl:message>
                                    <xsl:text>Unknown encoding </xsl:text>
                                    <xsl:value-of select="@encoding"/>
                                </xsl:message>
                            </xsl:otherwise>
                        </xsl:choose>
                    </encoding>
                    <width>
                        <xsl:value-of select="@pixelsX"/>
                    </width>
                    <height>
                        <xsl:value-of select="@pixelsY"/>
                    </height>
                </video>
            </meta-data>
            <xsl:call-template name="Content"/>
        </variant>
    </xsl:template>

    <!--
     ! =========================================================================
     !     Image Assets
     ! =========================================================================
     !-->
    <xsl:template name="ImageMetaData">
        <meta-data>
            <image>
                <encoding>
                    <xsl:choose>
                        <xsl:when test="@encoding = 'bmp'">bmp</xsl:when>
                        <xsl:when test="@encoding = 'gif'">gif</xsl:when>
                        <xsl:when test="@encoding = 'jpeg'">jpeg</xsl:when>
                        <xsl:when test="@encoding = 'pjpeg'">pjpeg</xsl:when>
                        <xsl:when test="@encoding = 'png'">png</xsl:when>
                        <xsl:when test="@encoding = 'tiff'">tiff</xsl:when>
                        <xsl:when test="@encoding = 'wbmp'">wbmp</xsl:when>
                        <xsl:when test="@encoding = 'videotex'">videotex</xsl:when>

                        <xsl:otherwise>
                            <xsl:message>
                                <xsl:text>Unknown encoding </xsl:text>
                                <xsl:value-of select="@encoding"/>
                            </xsl:message>
                        </xsl:otherwise>
                    </xsl:choose>
                </encoding>
                <pixel-depth>
                    <xsl:choose>
                        <!-- 0 used to be equivalent to 1, now it is invalid -->
                        <xsl:when test="@pixelDepth = '0'">
                            <xsl:text>1</xsl:text>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="@pixelDepth"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </pixel-depth>
                <width>
                    <xsl:value-of select="@pixelsX"/>
                </width>
                <height>
                    <xsl:value-of select="@pixelsY"/>
                </height>
                <rendering>
                    <xsl:choose>
                        <xsl:when test="@rendering = 'monochrome'">
                            <xsl:text>grayscale</xsl:text>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="@rendering"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </rendering>
                <xsl:if test="self::old:convertibleImageAsset">
                    <conversion-mode>always</conversion-mode>
                    <xsl:if test="@preserveLeft">
                        <preserve-left>
                            <xsl:value-of select="@preserveLeft"/>
                        </preserve-left>
                    </xsl:if>
                    <xsl:if test="@preserveRight">
                        <preserve-right>
                            <xsl:value-of select="@preserveRight"/>
                        </preserve-right>
                    </xsl:if>
                </xsl:if>
            </image>
        </meta-data>
    </xsl:template>

    <xsl:template match="old:convertibleImageAsset">
        <variant>
            <type>image</type>
            <selection>
                <default/>
            </selection>
            <xsl:call-template name="ImageMetaData"/>
            <xsl:call-template name="Content"/>
        </variant>
    </xsl:template>

    <xsl:template match="old:deviceImageAsset">
        <variant>
            <type>image</type>
            <xsl:call-template name="DeviceTargetedSelection"/>
            <xsl:call-template name="ImageMetaData"/>
            <xsl:call-template name="Content"/>
        </variant>
    </xsl:template>

    <xsl:template match="old:genericImageAsset">
        <variant>
            <type>image</type>
            <selection>
                <generic-image>
                    <width-hint>
                        <xsl:value-of select="@widthHint"/> 
                    </width-hint>
                </generic-image>
            </selection>
            <xsl:call-template name="ImageMetaData"/>
            <xsl:call-template name="Content"/>
        </variant>
    </xsl:template>

    <!--
     ! =========================================================================
     !     Link Assets
     ! =========================================================================
     !-->
    <xsl:template match="old:linkAsset">
        <variant>
            <type>link</type>
            <xsl:call-template name="DeviceTargetedSelection"/>
            <xsl:call-template name="Content"/>
        </variant>
    </xsl:template>

    <!--
     ! =========================================================================
     !     Script Assets
     ! =========================================================================
     !-->
    <xsl:template match="old:scriptAsset">
        <variant>
            <type>script</type>
            <xsl:call-template name="DeviceTargetedSelection"/>
            <meta-data>
                <script>
                    <encoding>
                        <xsl:variable name="language" select="translate(@programmingLanguage, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')"/>
                        <xsl:choose>
                            <xsl:when test="$language = 'javascript'">javascript</xsl:when>
                            <xsl:when test="$language = 'javascript1.0'">javascript-1.0</xsl:when>
                            <xsl:when test="$language = 'javascript1.1'">javascript-1.1</xsl:when>
                            <xsl:when test="$language = 'javascript1.2'">javascript-1.2</xsl:when>
                            <xsl:when test="$language = 'javascript1.3'">javascript-1.3</xsl:when>
                            <xsl:when test="$language = 'javascript1.4'">javascript-1.4</xsl:when>
                            <xsl:when test="$language = 'wmltask'">WML-task</xsl:when>
                            <xsl:otherwise>
                                <xsl:message>
                                    <xsl:text>Unknown programming language </xsl:text>
                                    <xsl:value-of select="$language"/>
                                </xsl:message>
                            </xsl:otherwise>
                        </xsl:choose>
                    </encoding>
                    <xsl:if test="@characterSet != ''">
                        <character-set>
                            <xsl:value-of select="@characterSet"/>
                        </character-set>
                    </xsl:if>
                </script>
            </meta-data>
            <xsl:call-template name="Content"/>
        </variant>
    </xsl:template>


    <!--
     ! =========================================================================
     !     Text Assets
     ! =========================================================================
     !-->
    <xsl:template match="old:textAsset">
        <variant>
            <type>text</type>
            <xsl:call-template name="DeviceTargetedSelection"/>
            <meta-data>
                <text>
                    <encoding>
                        <xsl:choose>
<!--                            <xsl:when test="@encoding = 'formValidator'">formValidator</xsl:when>-->
                            <xsl:when test="@encoding = 'plain'">plain</xsl:when>
                            <xsl:when test="not(@encoding)">plain</xsl:when>
                            <xsl:when test="@encoding = 'formValidator'">form-validator</xsl:when>
                            <xsl:when test="@encoding = 'voiceXMLError'">voice-xml-error</xsl:when>
                            <xsl:when test="@encoding = 'voiceXMLHelp'">voice-xml-help</xsl:when>
                            <xsl:when test="@encoding = 'voiceXMLNuanceGrammar'">voice-xml-nuance-grammar</xsl:when>
                            <xsl:when test="@encoding = 'voiceXMLPrompt'">voice-xml-prompt</xsl:when>
<!--                            <xsl:when test="@encoding = 'volantisMarkup'">volantisMarkup</xsl:when>-->
<!--                            <xsl:when test="@encoding = 'xmlMarkup'">xmlMarkup</xsl:when>-->
                            <xsl:otherwise>
                                <xsl:message>
                                    <xsl:text>Unknown encoding </xsl:text>
                                    <xsl:value-of select="@encoding"/>
                                </xsl:message>
                            </xsl:otherwise>
                        </xsl:choose>
                    </encoding>

                    <language>
                        <xsl:value-of select="@language"/>
                    </language>
                </text>
            </meta-data>
<!--                    <encoding>-->
<!--                        <xsl:variable name="language" select="translate(@programmingLanguage, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')"/>-->
<!--                        <xsl:choose>-->
<!--                            <xsl:when test="$language = 'javascript'">javascript</xsl:when>-->
<!--                            <xsl:when test="$language = 'javascript1.0'">javascript-1.0</xsl:when>-->
<!--                            <xsl:when test="$language = 'javascript1.1'">javascript-1.1</xsl:when>-->
<!--                            <xsl:when test="$language = 'javascript1.2'">javascript-1.2</xsl:when>-->
<!--                            <xsl:when test="$language = 'javascript1.3'">javascript-1.3</xsl:when>-->
<!--                            <xsl:when test="$language = 'javascript1.4'">javascript-1.4</xsl:when>-->
<!--                            <xsl:when test="$language = 'wmltask'">WML-task</xsl:when>-->
<!--                            <xsl:otherwise>-->
<!--                                <xsl:message>-->
<!--                                    <xsl:text>Unknown programming language </xsl:text>-->
<!--                                    <xsl:value-of select="$language"/>-->
<!--                                </xsl:message>-->
<!--                            </xsl:otherwise>-->
<!--                        </xsl:choose>-->
<!--                    </encoding>-->
<!--                    <character-set>-->
<!--                        <xsl:value-of select="@characterSet"/>-->
<!--                    </character-set>-->
<!--                </text>-->
<!--            </meta-data>-->
            <xsl:call-template name="Content"/>
        </variant>
    </xsl:template>

    <xsl:template name="ImageReference">
        <xsl:param name="name"/>
        <policy-reference>
            <name>
                <xsl:value-of select="$name"/>
            </name>
            <type>image</type>
        </policy-reference>
    </xsl:template>

    <!--
     ! =========================================================================
     !     Button 2 Policy
     ! =========================================================================
     !-->
    <xsl:template match="old:buttonImageComponent" priority="2">
        <button-image-policy>
            <xsl:call-template name="AddLocalSchemaLocation"/>
            <xsl:call-template name="CacheControl"/>
            <xsl:call-template name="Alternates"/>

            <up>
                <xsl:call-template name="ImageReference">
                    <xsl:with-param name="name" select="@upImageComponentName"/>
                </xsl:call-template>
            </up>

            <down>
                <xsl:call-template name="ImageReference">
                    <xsl:with-param name="name" select="@downImageComponentName"/>
                </xsl:call-template>
            </down>

            <over>
                <xsl:call-template name="ImageReference">
                    <xsl:with-param name="name" select="@overImageComponentName"/>
                </xsl:call-template>
            </over>

        </button-image-policy>
    </xsl:template>

    <!--
     ! =========================================================================
     !     Rollover 2 Policy
     ! =========================================================================
     !-->
    <xsl:template match="old:rolloverImageComponent" priority="2">
        <rollover-image-policy>
            <xsl:call-template name="AddLocalSchemaLocation"/>
            <xsl:call-template name="CacheControl"/>
            <xsl:call-template name="Alternates"/>

            <normal>
                <xsl:call-template name="ImageReference">
                    <xsl:with-param name="name" select="@normalImageComponentName"/>
                </xsl:call-template>
            </normal>

            <over>
                <xsl:call-template name="ImageReference">
                    <xsl:with-param name="name" select="@overImageComponentName"/>
                </xsl:call-template>
            </over>

        </rollover-image-policy>
    </xsl:template>

    <!--
     ! =========================================================================
     !     Layout 2 Variable Policy
     ! =========================================================================
     !-->
    <xsl:template match="old:layoutFormat">
        <variable-policy>
            <xsl:call-template name="AddLocalSchemaLocation"/>
            <type>layout</type>
            <xsl:call-template name="CacheControl"/>
            <variants>
                <xsl:apply-templates/>
            </variants>
        </variable-policy>
    </xsl:template>

    <xsl:template match="old:deviceLayoutCanvasFormat|old:deviceLayoutMontageFormat">
        <variant>
            <type>layout</type>
            <xsl:call-template name="DeviceTargetedSelection"/>
            <content>
                <layout>
                    <xsl:choose>
                        <xsl:when test="self::old:deviceLayoutCanvasFormat">
                            <canvasLayout>
                                <xsl:apply-templates select="@*"/>
                                <xsl:apply-templates/>
                            </canvasLayout>
                        </xsl:when>
                        <xsl:otherwise>
                            <montageLayout>
                                <xsl:apply-templates select="@*"/>
                                <xsl:apply-templates/>
                            </montageLayout>
                        </xsl:otherwise>
                    </xsl:choose>
                </layout>
            </content>
        </variant>
    </xsl:template>

    <!--
     ! Ignore deviceName attributes on deviceLayoutCanvasFormat and
     ! deviceLayoutMontageFormat as they are handled elsewhere.
     !-->
    <xsl:template match="old:deviceLayoutCanvasFormat/@deviceName|old:deviceLayoutMontageFormat/@deviceName"/>

    <!--
     ! =========================================================================
     !     Theme 2 Variable Policy
     ! =========================================================================
     !-->
    <xsl:template match="old:theme">
        <variable-policy>
            <xsl:call-template name="AddLocalSchemaLocation"/>
            <type>theme</type>
            <xsl:call-template name="CacheControl"/>
            <variants>
                <xsl:apply-templates/>
            </variants>
        </variable-policy>
    </xsl:template>

    <xsl:template match="old:deviceTheme|old:cssDeviceTheme">
        <variant>
            <type>theme</type>
            <xsl:call-template name="DeviceTargetedSelection"/>
            <content>
                <theme>
                    <xsl:if test="@importParentTheme">
                        <import-parent>
                            <xsl:value-of select="@importParentTheme"/>
                        </import-parent>
                    </xsl:if>
                    <xsl:if test="@externalStyleSheet">
                        <xsl:message>Warning: External style sheets no longer supported.</xsl:message>
                    </xsl:if>
                    <style-sheet>
                    <xsl:choose>
                        <xsl:when test="self::old:cssDeviceTheme">
                            <!--
                             ! Old CSS is parsed using the lax parser to
                             ! maintain backwards compatibility.
                             !-->
                            <css parser="lax">
                                <xsl:value-of select="."/>
                            </css>
                        </xsl:when>
                        <xsl:otherwise>
                            <rules>
                                <xsl:apply-templates select="old:rule"/>
                            </rules>
                        </xsl:otherwise>
                    </xsl:choose>
                    </style-sheet>
                </theme>
            </content>
        </variant>
    </xsl:template>

    <!--
     ! =========================================================================
     !     Internal theme restructuring.
     ! =========================================================================
     !-->

    <xsl:template match="old:selectorSequence|old:selectorSequence">
        <selector-sequence>
            <xsl:apply-templates/>
        </selector-sequence>
    </xsl:template>

    <xsl:template match="old:combinedSelector">
        <combined-selector>
            <xsl:apply-templates/>
        </combined-selector>
    </xsl:template>

    <xsl:template match="old:localName">
        <local-name>
            <xsl:apply-templates/>
        </local-name>
    </xsl:template>

<!--    <xsl:template match="old:colorName">-->
<!--        <named-color>-->
<!--            <xsl:apply-templates/>-->
<!--        </named-color>-->
<!--    </xsl:template>-->
<!---->
<!--    <xsl:template match="old:colorRGB">-->
<!--        <rgb>-->
<!--            <xsl:apply-templates/>-->
<!--        </rgb>-->
<!--    </xsl:template>-->

    <xsl:template match="old:mcsComponentURI">
        <mcs-policy-reference>
            <expression>
                <xsl:apply-templates/>
            </expression>
        </mcs-policy-reference>
    </xsl:template>

    <!--
      - Copy anything unrecognised with low priority, updating the namespace
      -->
    <xsl:template match="old:*" priority="-3">
        <xsl:element name="{local-name()}">
            <xsl:apply-templates select="@*|node()"/>
        </xsl:element>
    </xsl:template>

    <xsl:template match="node()|@*" priority="-5">
        <xsl:copy>
            <xsl:apply-templates select="node()|@*"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="@xsi:schemaLocation">
        <xsl:call-template name="AddLocalSchemaLocation"/>
    </xsl:template>

</xsl:stylesheet>
