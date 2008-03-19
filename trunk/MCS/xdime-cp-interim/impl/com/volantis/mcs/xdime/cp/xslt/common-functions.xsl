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
 ! Contains common templates that are used by all the modules.
 !
 ! This needs to be included in each module when run stand alone.
 !-->
<xsl:stylesheet version="1.0"
    xmlns:cdm="http://www.volantis.com/xmlns/marlin-cdm"
    xmlns:exslt-common="http://exslt.org/common"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xh2="http://www.w3.org/2002/06/xhtml2"
    xmlns:mcs="http://www.volantis.com/xmlns/2004/06/xdimecp/mcs"
    xmlns:xf="http://www.w3.org/2002/xforms"
    xmlns:sel="http://www.w3.org/2004/06/diselect">

    <xsl:template name="unexpected-element">
        <xsl:message terminate="yes">
            <xsl:text>Unexpected element </xsl:text>
            <xsl:value-of select="name()"/>
            <xsl:text> child of '</xsl:text>
            <xsl:value-of select="name(..)"/>
            <xsl:text>'</xsl:text>
        </xsl:message>
    </xsl:template>

    <xsl:template name="unexpected-attribute">
        <xsl:message terminate="yes">
            <xsl:text>Unexpected attribute </xsl:text>
            <xsl:value-of select="name()"/>
            <xsl:text> on '</xsl:text>
            <xsl:value-of select="name(..)"/>
            <xsl:text>'</xsl:text>
            <xsl:text> on '</xsl:text>
            <xsl:value-of select="name(../..)"/>
            <xsl:text>'</xsl:text>
            <xsl:text> on '</xsl:text>
            <xsl:value-of select="name(../../..)"/>
            <xsl:text>'</xsl:text>
        </xsl:message>
    </xsl:template>

    <!--
     ! Text nodes containing only white space do not cause an error.
     !-->
    <xsl:template name="unexpected-text">
        <xsl:if test="normalize-space(.) != ''">
            <xsl:message terminate="yes">
                <xsl:text>Unexpected text </xsl:text>
                <xsl:text>'</xsl:text>
                <xsl:value-of select="."/>
                <xsl:text>'</xsl:text>
                <xsl:text> in '</xsl:text>
                <xsl:value-of select="name(..)"/>
                <xsl:text>'</xsl:text>
            </xsl:message>
        </xsl:if>
    </xsl:template>

    <!--
     ! Wrap the current element's contents with an anchor if it has an href
     ! attribute. This will be used by all elements that have Common Attributes
     ! apart from <a>.
     !
     ! contents - The contents to wrap.
     !-->
    <xsl:template name="WrapContentsInAnchor">
        <xsl:param name="contents"></xsl:param>

        <xsl:choose>
            <!--
             ! Only wrap in an anchor if the containing element has an href
             ! attribute but is not an a.
             !-->
            <xsl:when test="@href and not(self::xh2:a)">
                <xsl:element name="a">
                    <xsl:attribute name="href">
                        <xsl:value-of select="@href"/>
                    </xsl:attribute>
                    <xsl:if test="@access">
                        <xsl:attribute name="shortcut">
                            <xsl:value-of select="@access"/>
                        </xsl:attribute>
                    </xsl:if>
                    <xsl:copy-of select="exslt-common:node-set($contents)"/>
                </xsl:element>
            </xsl:when>

            <!--
             ! Just output the contents.
             !-->
            <xsl:otherwise>
                <xsl:copy-of select="exslt-common:node-set($contents)"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>


    <!--
     ! Gets the file extension of the given contents. The file extension is
     ! the string before the first ? (if any), and after the last dot that is
     ! before the first ?.
     -->
    <xsl:template name="get-extension">
        <xsl:param name="contents"/>

        <!-- Ensure that any URL parameters are removed. -->
        <xsl:variable name="without-url-params">
            <xsl:choose>
                <xsl:when test="contains($contents, '?')">
                    <xsl:value-of select="substring-before($contents, '?')"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="$contents"/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>

        <!-- Now retrieve the extension -->
        <xsl:variable name="ext">
            <xsl:call-template name="substring-after-last">
                <xsl:with-param name="contents" select="$without-url-params"/>
                <xsl:with-param name="delimiter" select="'.'"/>
            </xsl:call-template>
        </xsl:variable>

        <xsl:value-of select="$ext"/>
    </xsl:template>


    <!--
     ! Gets the string which occurs after the last occurrence of the supplied
     ! delimiter in the given contents.
     -->
    <xsl:template name="substring-after-last">
        <xsl:param name="contents"/>
        <xsl:param name="delimiter"/>
        <xsl:choose>
            <!--
             ! If the contents contains the delimiter then recursively process.
            -->
            <xsl:when test="$delimiter and contains($contents, $delimiter)">
                <xsl:call-template name="substring-after-last">
                    <!-- Contents to process is what's after the delimiter. -->
                    <xsl:with-param name="contents"
                        select="substring-after($contents, $delimiter)"/>
                    <xsl:with-param name="delimiter" select="$delimiter"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <!-- The delimiter is not present so return the contents. -->
                <xsl:value-of select="$contents"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>


    <!--
     ! Gets the string which occurs before the first occurrence of the supplied
     ! delimiter in the given contents.
     -->
    <xsl:template name="substring-before-last">
        <xsl:param name="contents"/>
        <xsl:param name="delimiter"/>

        <xsl:if test="$delimiter and contains($contents, $delimiter)">
            <xsl:variable name="temp" select="substring-after($contents, $delimiter)"/>
            <xsl:value-of select="substring-before($contents, $delimiter)"/>
            <xsl:if test="contains($temp, $delimiter)">
                <xsl:value-of select="$delimiter"/>
                <xsl:call-template name="substring-before-last">
                    <xsl:with-param name="contents" select="$temp"/>
                    <xsl:with-param name="delimiter" select="$delimiter"/>
                </xsl:call-template>
            </xsl:if>
        </xsl:if>
    </xsl:template>

    <!--
     ! Ignore any access attribute as that is transformed only if there is
     ! also an href attribute.
     -->
    <xsl:template match="@access"/>


    <!--
     ! All elements, except when overridden, can have these attributes, so
     ! copy them.
     -->
    <xsl:template match="@class | @id | @title">
        <xsl:copy/>
    </xsl:template>

    <!--
      ! Get the index of the specified node in its containing element.
      ! The index of a node is equal to the number of preceding siblings.
      !
      ! node - The node whose index is required, if not specified then it
      !        defaults to the current node.
      !-->
    <xsl:template name="get-index">
        <xsl:param name="node" select="."/>

        <!--
         ! Use a for-each to set the context node correctly for the
         ! preceding-sibling axis to start from the correct point.
         !-->
        <xsl:for-each select="$node[1]">
            <xsl:value-of select="count(preceding-sibling::*)"/>
        </xsl:for-each>
    </xsl:template>

    <!--
      ! Get the index of the specified form node in its containing element.
      ! The index of a node is equal to the number of preceding form siblings.
      !
      ! node - The node whose index is required, if not specified then it
      !        defaults to the current node.
      !-->
    <xsl:template name="get-form-index">
        <xsl:param name="node" select="."/>

        <!--
         ! Use a for-each to set the context node correctly for the
         ! preceding-sibling axis to start from the correct point.
         !-->
        <xsl:for-each select="$node[1]">
            <xsl:value-of select="count(preceding-sibling::xf:*)"/>
        </xsl:for-each>
    </xsl:template>

</xsl:stylesheet>

<!--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Sep-04	5383/1	pcameron	VBM:2004082607 URL parameters are ignored when finding image extensions

 02-Sep-04	5381/5	pcameron	VBM:2004082607 URL parameters are ignored when finding image extensions

 21-Jun-04	4645/6	pcameron	VBM:2004060306 Committed for integration

 17-Jun-04	4630/7	pduffin	VBM:2004060306 Integrated and produced distribution

 16-Jun-04	4645/4	pcameron	VBM:2004060306 Committed for integration

 16-Jun-04	4630/5	pduffin	VBM:2004060306 Some more changes

 15-Jun-04	4645/1	pcameron	VBM:2004060306 Fixed test cases after integration

 15-Jun-04	4630/3	pduffin	VBM:2004060306 Added some more xdime cp stuff

 11-Jun-04	4645/6	pcameron	VBM:2004060306 Commit changes for integration

 11-Jun-04	4645/4	pcameron	VBM:2004060306 Commit changes for integration

 10-Jun-04	4645/1	pcameron	VBM:2004060306 Commit changes for integration

 08-Jun-04	4630/3	pduffin	VBM:2004060306 Added some constants

 07-Jun-04	4630/1	pduffin	VBM:2004060306 Added framework for XDIME-CP interim solution

 ===========================================================================
-->
