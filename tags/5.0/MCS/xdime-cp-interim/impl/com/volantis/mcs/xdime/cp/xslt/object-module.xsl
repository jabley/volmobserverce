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


<!--
 ! This XSLT is responsible for handling elements in the XDIME CP Object
 ! Module.
 !-->
<xsl:stylesheet version="1.0"
    xmlns:cdm="http://www.volantis.com/xmlns/marlin-cdm"
    xmlns:exslt-common="http://exslt.org/common"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xh2="http://www.w3.org/2002/06/xhtml2"
    xmlns:mcs="http://www.volantis.com/xmlns/2004/06/xdimecp/mcs"
    xmlns:xf="http://www.w3.org/2002/xforms"
    xmlns:sel="http://www.w3.org/2004/06/diselect"
    xmlns:cfg="http://www.volantis.com/xmlns/2004/06/xdimecp/interim/cfg"
    xmlns:str="http://exslt.org/strings"
    xmlns:fk="fake-namespace">

    <!--<xsl:namespace-alias stylesheet-prefix="xh2" result-prefix="#default"/>-->

    <!-- Common functions (named templates), variables etc. -->
    <xsl:include href="common-functions.xsl"/>

    <xsl:include href="caption-module.xsl"/>

    <!--
     ! Transform the XHTML2 object element and its content. If the object
     ! element has a caption element, this is changed to a div element with
     ! the same content, and placed immediately after the transformed object.
     -->
    <xsl:template match="xh2:object">

        <xsl:call-template name="WrapContentsInAnchor">
            <xsl:with-param name="contents">
                <!--
                 ! Transform the object element into an img element with src and
                 ! content attributes.
                 -->
                <xsl:element name="img">
                    <!-- Process the attributes -->
                    <xsl:call-template name="create-pane-attribute"/>
                    <xsl:apply-templates select="@*[local-name() != 'href']"/>
                    <!--
                     ! Process the text content. This is transformed into a
                     ! content attribute.
                      -->
                    <xsl:apply-templates select="text()"/>
                    <!-- Process all element children except for caption -->
                    <xsl:apply-templates select="*[not(self::xh2:caption)]"/>
                </xsl:element>

            </xsl:with-param>
        </xsl:call-template>

        <!--
         ! Now that the object has been transformed, transform the caption
         ! element at the end.
         -->
        <xsl:apply-templates select="xh2:caption"/>


    </xsl:template>

    <!--
     ! Ignore all param elements as they are explicitly dealt with elsewhere,
     -->
    <xsl:template match="xh2:object/xh2:param"/>

    <!--
     ! Transform the src attribute of the object element according to a set
     ! of rules based on the file extension of the attribute's value.
     -->
    <xsl:template match="xh2:object/@src">
        <!-- Save the attribute's value for later processing -->
        <xsl:variable name="src-contents">
            <xsl:variable name="rewritten-url">
                <xsl:call-template name="rewrite-url">
                    <xsl:with-param name="url">
                        <xsl:value-of select="."/>
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:variable>
            <xsl:variable name="stripped">
                <xsl:call-template name="map-to-policy">
                    <xsl:with-param name="contents" select="$rewritten-url"/>
                </xsl:call-template>
            </xsl:variable>
            <xsl:value-of select="$stripped"/>
        </xsl:variable>

        <!--
         ! Duplicate parameters are not allowed, so check if there are any.
         -->
        <xsl:call-template name="check-for-duplicate-parameters">
            <xsl:with-param name="parent" select=".."/>
            <xsl:with-param name="param-names"
                select="'mcs-transcode mcs-aspect-ratio-width mcs-aspect-ratio-height'"/>
        </xsl:call-template>

        <!--
         ! Create three result tree fragments that are used to facilitate
         ! application of a transformation rule to an extension.
        -->
        <xsl:variable name="extSet1-RT">
            <!-- These extensions result in the creation of a src attribute -->
            <fk:ext>mimg</fk:ext>
        </xsl:variable>

        <xsl:variable name="extSet2-RT">
            <!-- These extensions result in the creation of a src attribute -->
            <fk:ext>mrsc</fk:ext>
        </xsl:variable>

        <!-- Get and save the file extension of the src attribute's value -->
        <xsl:variable name="src-ext">
            <xsl:call-template name="get-extension">
                <xsl:with-param name="contents" select="."/>
            </xsl:call-template>
        </xsl:variable>

        <!--
         ! Process the src attribute according to the file extension found.
         -->
        <xsl:choose>
            <!--
             ! If the extension is mimg then create src attribute with same
             ! contents.
            -->
            <xsl:when
                test="exslt-common:node-set($extSet1-RT)/fk:ext[. = $src-ext]">
                <xsl:attribute name="src">
                    <xsl:value-of select="$src-contents"/>
                </xsl:attribute>
            </xsl:when>

            <!--
             ! If the extension is mrsc then create src attribute with contents
             ! as follows: replace the extension with -mrsc.mimg.
             -->
            <xsl:when
                test="exslt-common:node-set($extSet2-RT)/fk:ext[. = $src-ext]">
                <!-- Create the src attribute -->
                <xsl:attribute name="src">
                    <!--
                     ! Find and save the prefix i.e. the text before the
                     ! . of the extension.
                     -->
                    <xsl:variable name="prefix">
                        <!-- Create the "dot extension" value -->
                        <xsl:variable name="dot-ext">
                            <xsl:text>.</xsl:text>
                            <xsl:value-of select="$src-ext"/>
                        </xsl:variable>
                        <!-- Get the string before the "dot ext" -->
                        <xsl:value-of
                            select="substring-before($src-contents, $dot-ext)"/>
                    </xsl:variable>

                    <!--
                     ! Create the value of the new src attribute using the
                     ! prefix and the new extension.
                     -->
                    <xsl:value-of select="$prefix"/>
                    <xsl:text>-mrsc.mimg</xsl:text>
                </xsl:attribute>
            </xsl:when>


            <!--
             ! If no transcoding is required, generate a url attribute rather
             ! than a urlc attribute.
             -->
            <xsl:when test="../xh2:param[@name = 'mcs-transcode'][@value = 'false']">
                <xsl:attribute name="url">
                    <xsl:value-of select="$src-contents"/>
                </xsl:attribute>
            </xsl:when>

            <!--
             ! If an attribute value extension match has not been found, or the
             ! extension is one of the "default" extension such as gif or bmp,
             ! and transcoding hasn't been switched off,  then pass through the
             ! entire contents of the src attribute as a urlc attribute, adding
             ! any extra parameters.
             -->
            <xsl:otherwise>
                <xsl:call-template name="create-urlc-attribute">
                    <xsl:with-param name="arwidth"
                        select="../xh2:param[@name = 'mcs-aspect-ratio-width']/@value"/>
                    <xsl:with-param name="arheight"
                        select="../xh2:param[@name = 'mcs-aspect-ratio-height']/@value"/>
                    <xsl:with-param name="url" select="$src-contents"/>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <!--
     ! If the object has duplicate parameters, processing terminates.
     -->
    <xsl:template name="check-for-duplicate-parameters">
        <xsl:param name="parent"/>
        <xsl:param name="param-names"/>
        <xsl:param name="separator" select="' '"/>
        <xsl:for-each select="str:split($param-names, $separator)">
            <xsl:variable name="param" select="."/>
            <xsl:variable name="param-count">
                <xsl:value-of
                    select="count($parent/xh2:param[@name = $param])"/>
            </xsl:variable>
            <xsl:if test="$param-count > 1">
                <xsl:message terminate="yes">
                    <xsl:text>Duplicate parameter names in object are not allowed: </xsl:text>
                    <xsl:value-of select="$param"/>
                </xsl:message>
            </xsl:if>
        </xsl:for-each>
    </xsl:template>


    <!--
     ! Creates the urlc attribute, appending additional parameters if necessary.
     -->
    <xsl:template name="create-urlc-attribute">
        <xsl:param name="arwidth"/>
        <xsl:param name="arheight"/>
        <xsl:param name="url"/>

        <!-- Create the urlc value from the given url and parameter string -->
        <xsl:attribute name="urlc">
            <xsl:variable name="aspect-ratio-param">
                <xsl:call-template name="aspect-ratio-append-string">
                    <xsl:with-param name="arwidth" select="$arwidth"/>
                    <xsl:with-param name="arheight" select="$arheight"/>
                    <xsl:with-param name="url" select="$url"/>
                </xsl:call-template>
            </xsl:variable>
            <xsl:value-of select="$url"/>
            <xsl:value-of select="$aspect-ratio-param"/>
        </xsl:attribute>
    </xsl:template>


    <!-- Create the aspect ratio value, if any. -->
    <xsl:template name="aspect-ratio-append-string">
        <xsl:param name="arwidth"/>
        <xsl:param name="arheight"/>
        <xsl:param name="url"/>

        <!-- Construct a value for the aspect ratio parameters, if any. -->
        <xsl:variable name="mcs-ar-value">
            <xsl:choose>
                <xsl:when test="$arwidth and $arwidth != '' and
                $arheight and $arheight != ''">
                    <xsl:value-of select="$arwidth"/>
                    <xsl:value-of select="':'"/>
                    <xsl:value-of select="$arheight"/>
                </xsl:when>
                <xsl:when test="$arwidth and $arwidth != ''">
                    <xsl:value-of select="$arwidth"/>
                    <xsl:value-of select="':'"/>
                    <xsl:value-of select="$arwidth"/>
                </xsl:when>
                <xsl:when test="$arheight and $arheight != ''">
                    <xsl:value-of select="$arheight"/>
                    <xsl:value-of select="':'"/>
                    <xsl:value-of select="$arheight"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="''"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>

        <!-- Construct the parameter string, if any, to add to the url. -->
        <xsl:variable name="mcs-ar-param-string">
            <xsl:choose>
                <xsl:when test="$mcs-ar-value and $mcs-ar-value != ''">

                    <!--
                     ! Determine if this is the first parameter to be added.
                     -->
                    <xsl:choose>
                        <xsl:when test="contains($url, '?')">
                            <!-- additional parameter needs a & -->
                            <xsl:value-of select="'&amp;'"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <!-- first parameter needs a ? -->
                            <xsl:value-of select="'?'"/>
                        </xsl:otherwise>
                    </xsl:choose>

                    <!-- output the parameter -->
                    <xsl:text>mcs.ar=</xsl:text>
                    <xsl:value-of select="$mcs-ar-value"/>
                </xsl:when>

                <!-- neither aspect ratio height or width specified -->
                <xsl:otherwise>
                    <xsl:value-of select="''"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>

        <xsl:value-of select="$mcs-ar-param-string"/>
    </xsl:template>


    <xsl:template name="map-to-policy">
        <xsl:param name="contents"/>
        <xsl:variable name="policyExt-RT">
            <fk:ext>mimg</fk:ext>
            <fk:ext>mrsc</fk:ext>
        </xsl:variable>

        <!-- Get and save the file extension of the src attribute's value -->
        <xsl:variable name="src-ext">
            <xsl:call-template name="get-extension">
                <xsl:with-param name="contents" select="$contents"/>
            </xsl:call-template>
        </xsl:variable>

        <xsl:variable name="config">
            <xsl:call-template name="get-customer-configuration"/>
        </xsl:variable>

        <xsl:choose>
            <xsl:when test="exslt-common:node-set($policyExt-RT)/fk:ext[. = $src-ext]">

                <xsl:variable name="configuration-prefix">
                    <xsl:value-of select="exslt-common:node-set($config)//cfg:prefix/."/>
                </xsl:variable>

                <xsl:choose>
                    <xsl:when test="starts-with($contents, $configuration-prefix)">
                        <xsl:value-of select="concat('/', substring-after($contents, $configuration-prefix))"/>
                    </xsl:when>

                    <xsl:otherwise>
                        <xsl:value-of select="$contents"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$contents"/>
            </xsl:otherwise>
        </xsl:choose>

    </xsl:template>


    <!--
     ! Transform any text content of the object element by creating a new
     ! attribute, altText, whose value is the text content.
     -->
    <xsl:template match="xh2:object/text()">
        <xsl:attribute name="alt">
            <xsl:value-of select="normalize-space(.)"/>
        </xsl:attribute>
    </xsl:template>


    <xsl:template name="rewrite-url">
        <xsl:param name="url"/>

        <!-- Store the base URL if we need it -->
        <xsl:variable name="base">
            <xsl:call-template name="get-base-url"/>
        </xsl:variable>

        <xsl:variable name="scheme">
            <xsl:call-template name="get-base-url-scheme"/>
        </xsl:variable>

        <xsl:variable name="authority">
            <xsl:call-template name="get-base-url-authority"/>
        </xsl:variable>

        <!--
         ! If the URL is absolute, pass it through. It is absolute if it
         ! starts with either http:// or https://
        -->
        <xsl:choose>
            <xsl:when test='starts-with($url, "http://") or
                            starts-with($url, "https://")'>
                <xsl:value-of select="$url"/>
            </xsl:when>

            <!--
             ! Test if URL is host-relative, that is, starts with a /
             -->
            <xsl:when test='starts-with($url, "/")'>

                <xsl:if test="not($base) or $base=''">
                    <xsl:message terminate="yes">
                        <xsl:text>Cannot find xml:base attribute.</xsl:text>
                    </xsl:message>
                </xsl:if>

                <xsl:value-of select='concat($scheme, "://", $authority, $url)'/>

            </xsl:when>

            <!--
             ! Document-relative path so normalise it.
             -->
            <xsl:otherwise>
                <xsl:variable name="path">
                    <xsl:call-template name="get-base-url-path"/>
                </xsl:variable>

                <xsl:variable name="normalized-url">
                    <xsl:call-template name="normalize-path">
                        <xsl:with-param name="path" select="concat($path, '/', $url)"/>
                    </xsl:call-template>
                </xsl:variable>

                <xsl:value-of select='concat($scheme, "://", $authority, $normalized-url)'/>

            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>


    <xsl:template name="get-base-url-scheme">
        <xsl:variable name="base-url">
            <xsl:call-template name="get-base-url"/>
        </xsl:variable>
        <xsl:if test="contains($base-url, ':')">
            <xsl:value-of select="substring-before($base-url, ':')"/>
        </xsl:if>
    </xsl:template>


    <xsl:template name="get-base-url-authority">
        <xsl:variable name="base-url">
            <xsl:call-template name="get-base-url"/>
        </xsl:variable>
        <xsl:variable name="prefix">
            <xsl:choose>
                <xsl:when test="contains($base-url, ':')">
                    <xsl:if test="substring(substring-after($base-url, ':'), 1, 2) = '//'">
                        <xsl:value-of select="substring(substring-after($base-url, ':'), 3)"/>
                    </xsl:if>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:if test="substring($base-url, 1, 2) = '//'">
                        <xsl:value-of select="substring($base-url, 3)"/>
                    </xsl:if>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>

        <xsl:value-of select='substring-before($prefix, "/")'/>

    </xsl:template>


    <xsl:template name="get-base-url-path">
        <xsl:variable name="base-url">
            <xsl:call-template name="get-base-url"/>
        </xsl:variable>
        <xsl:if test="substring(substring-after($base-url, ':'), 1, 2) = '//'">
            <xsl:value-of select="substring-before(substring-after(substring-after($base-url, '//'), '/'), '/')"/>
        </xsl:if>
    </xsl:template>

    <!--
     !
     -->
    <xsl:template name="get-base-url">
        <!--
         ! The ancestor-or-self axis is a reverse axis so that the closest
         ! element is at position 1 rather than at position last()
         -->
        <xsl:value-of select="ancestor-or-self::*[@xml:base][1]/@xml:base"/>
    </xsl:template>


    <!--
     ! Normalizes the given path...... This is not a generalised template as
     ! it was written for use within the context of the rewrite-url template,
     ! whose matching conditions take care of some of the cases.
     -->
    <xsl:template name="normalize-path">
        <xsl:param name="path"/>
        <xsl:param name="result" select="''"/>

        <xsl:choose>
            <!--
             ! There is still path to process so do it, otherwise return the
             ! result.
             -->
            <xsl:when test="string-length($path)">
                <xsl:choose>

                    <!--
                     ! If the current path contains a slash then it must
                     ! be processed before adding to the result. Otherwise,
                     ! there is nothing more to process so simply append a /
                     ! and the current "slashless" path to the current result.
                     -->
                    <xsl:when test="contains($path, '/')">
                        <!--
                         ! Retrieve and store the current first segment i.e. the
                         ! bit between slashes.
                         -->
                        <xsl:variable name="segment" select="substring-before($path, '/')"/>
                        <!--
                         ! Get the remaining path, if any, or introduce a
                         ! terminating slash.
                         -->
                        <xsl:variable name="rest">
                            <xsl:choose>
                                <!--
                                 ! If there is nothing more to process, use a
                                 ! terminating slash.
                                 -->
                                <xsl:when test="substring-after($path, '/') = ''">
                                    <xsl:value-of select="'/'"/>
                                </xsl:when>
                                <!-- There is more path to process -->
                                <xsl:otherwise>
                                    <xsl:value-of select="substring-after($path, '/')"/>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:variable>

                        <!--
                         !
                         -->
                        <xsl:choose>
                            <xsl:when test="$segment = ''">
                                <!--
                                 ! A double slash has been found, i.e. there is
                                 ! no segment to process, so process remainder
                                 ! of path instead.
                                 -->
                                <xsl:call-template name="normalize-path">
                                    <xsl:with-param name="path" select="$rest"/>
                                    <xsl:with-param name="result" select="$result"/>
                                </xsl:call-template>
                            </xsl:when>
                            <xsl:when test="$segment = '.'">
                                <!--
                                 ! A . has been found, so there is effectively
                                 ! no segment to process, so process remainder
                                 ! of path instead.
                                 -->
                                <xsl:call-template name="normalize-path">
                                    <xsl:with-param name="path" select="$rest"/>
                                    <xsl:with-param name="result" select="$result"/>
                                </xsl:call-template>
                            </xsl:when>
                            <xsl:when test="$segment = '..'">
                                <!--
                                 ! A .. has been found, so process it.
                                 -->
                                <xsl:choose>

                                    <!--
                                     ! There is no result so nothing for .. to back up on.
                                     ! This is an error, so terminate processing.
                                     -->
                                    <xsl:when test="not(string-length($result))">
                                        <xsl:message terminate="yes">
                                            <xsl:text>Unable to resolve ..</xsl:text>
                                        </xsl:message>
                                    </xsl:when>

                                    <xsl:otherwise>
                                        <!--
                                         ! Remove the last segment from the result so far
                                         ! and process the remainder of the path.
                                         -->
                                        <xsl:call-template name="normalize-path">
                                            <xsl:with-param name="path" select="$rest"/>
                                            <xsl:with-param name="result">
                                                <!--
                                                 ! The result is constructed so that it does not contain a
                                                 ! terminating slash whilst processing is ongoing. Therefore,
                                                 ! the substring-before-last ignores the last segment.
                                                 -->
                                                <xsl:call-template name="substring-before-last">
                                                    <xsl:with-param name="contents" select="$result"/>
                                                    <xsl:with-param name="delimiter" select="'/'"/>
                                                </xsl:call-template>
                                            </xsl:with-param>
                                        </xsl:call-template>
                                    </xsl:otherwise>

                                </xsl:choose>
                            </xsl:when>

                            <!--
                             ! The segment isn't empty, dot or dot dot, so add it to the result
                             ! and process the rest of the path.
                             -->
                            <xsl:otherwise>
                                <xsl:call-template name="normalize-path">
                                    <xsl:with-param name="path" select="$rest"/>
                                    <xsl:with-param name="result" select="concat($result, '/', $segment)"/>
                                </xsl:call-template>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:when>
                    <!--
                     ! The path contains no slashes so append it to the result
                     ! with a slash.
                     -->
                    <xsl:otherwise>
                        <xsl:value-of select="concat($result, '/', $path)"/>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <!--
             ! There is no more path to process, so return the result.
             -->
            <xsl:otherwise>
                <!--
                <xsl:variable name="base-url">
                    <xsl:call-template name="get-base-url"/>
                </xsl:variable>
                -->
                <xsl:value-of select="$result"/>
            </xsl:otherwise>
        </xsl:choose>

    </xsl:template>


</xsl:stylesheet>

<!--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Jul-05	8813/8	pcameron	VBM:2005061608 Added aspect ratio parameter processing to XDIME-CP

 16-Jun-05	8796/5	pcameron	VBM:2005061504 Added mcs-transcode parameter to XDIME CP

 08-Sep-04	5447/3	pcameron	VBM:2004090711 object src attribute supports servlets

 30-Jun-04	4645/29	pcameron	VBM:2004060306 object's text goes to alt attribute and not altText

 30-Jun-04	4645/27	pcameron	VBM:2004060306 img url attribute now named urlc

 21-Jun-04	4645/25	pcameron	VBM:2004060306 Committed for integration

 17-Jun-04	4630/9	pduffin	VBM:2004060306 Integrated and produced distribution

 16-Jun-04	4630/7	pduffin	VBM:2004060306 Some more changes

 16-Jun-04	4645/9	pcameron	VBM:2004060306 Committed for integration

 15-Jun-04	4645/7	pcameron	VBM:2004060306 Fixed test cases after integration

 15-Jun-04	4630/2	pduffin	VBM:2004060306 Added some more xdime cp stuff

 11-Jun-04	4645/5	pcameron	VBM:2004060306 Commit changes for integration

 10-Jun-04	4645/1	pcameron	VBM:2004060306 Commit changes for integration

 ===========================================================================
-->
