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

<xsl:stylesheet  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"    xmlns:lpdm="http://www.volantis.com/xmlns/2005/09/marlin-lpdm"
    xmlns:rpdm="http://www.volantis.com/xmlns/2005/09/marlin-rpdm"  version="1.0"  >

    <xsl:output method="xml" indent="yes"
        xmlns:xalan="http://xml.apache.org/xslt"
        xalan:indent-amount="4"/>

    <xsl:template match="/">
        <xsl:apply-templates/>
    </xsl:template>
    <!--
            Copy  unprocessed elements straight through
    -->
    <xsl:template match="*">
        <xsl:copy>
            <xsl:apply-templates select="@*"/>
            <xsl:apply-templates select="*"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="@*">
        <xsl:copy-of select="."/>
    </xsl:template>

    <!-- Ignore text in all modes. -->
    <xsl:template match="text()"/>
    <xsl:template match="text()" mode="multiple"/>

    <!--
           **************************************************
           * Functions                                                          *
           **************************************************
    -->
    <xsl:template name="copyCacheAttributes">
        <xsl:copy-of select="@cacheThisPolicy"/>
        <xsl:copy-of select="@timeToLive"/>
        <xsl:copy-of select="@retryFailedRetrieval"/>
        <xsl:copy-of select="@retryInterval"/>
        <xsl:copy-of select="@retryMaxCount"/>
        <xsl:copy-of select="@retainDuringRetry"/>
    </xsl:template>
    <xsl:template name="copyAssetAttributes">
        <xsl:copy-of select="@assetGroupName"/>
        <xsl:copy-of select="@value"/>
    </xsl:template>
    <xsl:template name="copyAudioAssetAttributes">
        <xsl:call-template name="copyAssetAttributes"/>
        <xsl:copy-of select="@encoding"/>
    </xsl:template>
    <xsl:template name="copyChartAssetAttributes">
        <xsl:copy-of select="@type"/>
        <xsl:copy-of select="@XTitle"/>
        <xsl:copy-of select="@YTitle"/>
        <xsl:copy-of select="@XInterval"/>
        <xsl:copy-of select="@YInterval"/>
        <xsl:copy-of select="@widthHint"/>
        <xsl:copy-of select="@heightHint"/>
    </xsl:template>
    <xsl:template name="copyDynamicVisualAssetAttributes">
        <xsl:call-template name="copyAssetAttributes"/>
        <xsl:copy-of select="@encoding"/>
        <xsl:copy-of select="@pixelsX"/>
        <xsl:copy-of select="@pixelsY"/>
    </xsl:template>
    <xsl:template name="copyImageAssetAttributes">
        <xsl:call-template name="copyAssetAttributes"/>
        <xsl:copy-of select="@pixelsX"/>
        <xsl:copy-of select="@pixelsY"/>
        <xsl:choose>
            <xsl:when test="@pixelDepth != ''">
                <xsl:copy-of select="@pixelDepth"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:attribute name="pixelDepth">8</xsl:attribute>
            </xsl:otherwise>
        </xsl:choose>


        <xsl:copy-of select="@rendering"/>
        <xsl:if test="@encoding != ''">
            <xsl:attribute name="encoding">
                <xsl:value-of select="translate(@encoding,
                        'ABCDEFGHIJKLMNOPQRSTUVWXYZ',
                        'abcdefghijklmnopqrstuvwxyz')"/>
            </xsl:attribute>
        </xsl:if>
    </xsl:template>
    <xsl:template name="copyLinkAssetAttributes">
        <xsl:call-template name="copyAssetAttributes"/>
        <xsl:copy-of select="@deviceName"/>
    </xsl:template>
    <xsl:template name="copyScriptAssetAttributes">
        <xsl:call-template name="copyAssetAttributes"/>
        <xsl:copy-of select="@deviceName"/>
        <xsl:copy-of select="@programmingLanguage"/>
        <xsl:copy-of select="@mimeType"/>
        <xsl:copy-of select="@valueType"/>
        <xsl:copy-of select="@characterSet"/>
    </xsl:template>
    <xsl:template name="copyTextAssetAttributes">
        <xsl:call-template name="copyAssetAttributes"/>
        <xsl:copy-of select="@deviceName"/>
        <xsl:choose>
            <xsl:when test="@language != ''">
                <xsl:copy-of select="@language"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:attribute name="language">-</xsl:attribute>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:choose>
            <xsl:when test="@encoding != ''">
                <xsl:copy-of select="@encoding"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:attribute name="encoding">plain</xsl:attribute>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:choose>
            <xsl:when test="@valueType != ''">
                <xsl:attribute name="valueType">
                    <xsl:value-of select="translate(@valueType,
        	            'ABCDEFGHIJKLMNOPQRSTUVWXYZ',
        	            'abcdefghijklmnopqrstuvwxyz')"/>
                </xsl:attribute>
            </xsl:when>
            <xsl:otherwise>
                <xsl:attribute name="valueType">url</xsl:attribute>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    <xsl:template match="remotePolicyResponse[@errorCode='1']">
        <xsl:message terminate="yes">
            Remote repository returned error code 1 (Invalid Request)
        </xsl:message>
    </xsl:template>
    <xsl:template match="remotePolicyResponse[@errorCode='2']">
        <xsl:message terminate="yes">
            Remote repository returned error code 2 (Missing component)
        </xsl:message>
    </xsl:template>
    <xsl:template match="remotePolicyResponse">
        <xsl:apply-templates/>
    </xsl:template>
    <xsl:template match="remotePolicySetResponse[@errorCode='1']">
        <xsl:message terminate="yes">
            Remote repository returned error code 1 (Invalid Request)
        </xsl:message>
    </xsl:template>
    <xsl:template match="remotePolicySetResponse[@errorCode='2']">
        <xsl:message terminate="yes">
            Remote repository returned error code 2 (Missing component)
        </xsl:message>
    </xsl:template>
    <xsl:template match="remotePolicySetResponse">
        <rpdm:policies>
            <xsl:apply-templates>
                <xsl:with-param name="path">
                    <xsl:value-of select="@rootURL"/>
                </xsl:with-param>
            </xsl:apply-templates>
        </rpdm:policies>
    </xsl:template>
    <!--
            Process the component containers
    -->
    <xsl:template match="assetGroups | audioComponents  | buttonImageComponents | chartComponents
        |dynamicVisualComponents | imageComponents | layoutFormats | linkComponents |
        rolloverImageComponents | scriptComponents | textComponents">
        <xsl:param name="path"/>
        <xsl:apply-templates mode="multiple">
            <xsl:with-param name="path">
                <xsl:value-of select="$path"/>
            </xsl:with-param>
        </xsl:apply-templates>
    </xsl:template>
    <!--
            Process the folder elements in RPDM
    -->
    <xsl:template match="assetGroupFolder | audioComponentFolder | buttonImageComponentFolder |
        chartComponentFolder | dynamicVisualComponentFolder | imageComponentFolder | layoutFolder |
        linkComponentFolder | rolloverImageComponentFolder | scriptComponentFolder | textComponentFolder"
            mode="multiple">
        <xsl:param name="path"/>
        <xsl:apply-templates mode="multiple">
            <xsl:with-param name="path">
                <xsl:value-of select="substring($path,1,string-length($path)-1)"/>
                <xsl:if test="not(substring($path,string-length($path))='/')">
                    <xsl:value-of select="substring($path,string-length($path))"/>
                </xsl:if>
                <xsl:text>/</xsl:text>
                <xsl:if test="not(starts-with(@name,'/'))">
                    <xsl:value-of select="substring(@name,1,1)"/>
                </xsl:if>
                <xsl:value-of select="substring(@name,2)"/>
            </xsl:with-param>
        </xsl:apply-templates>
    </xsl:template>
    <xsl:template match="assetGroup | audioComponent  | buttonImageComponent | chartComponent
        |dynamicVisualComponent | imageComponent | layoutFormat | linkComponent |
        rolloverImageComponent | scriptComponent | textComponent" mode="multiple">
        <xsl:param name="path"/>
        <xsl:variable name="url">
            <xsl:value-of select="substring($path,1,string-length($path)-1)"/>
            <xsl:if test="not(substring($path,string-length($path))='/')">
                <xsl:value-of select="substring($path,string-length($path))"/>
            </xsl:if>
            <xsl:text>/</xsl:text>
            <xsl:if test="not(starts-with(@name,'/'))">
                <xsl:value-of select="substring(@name,1,1)"/>
            </xsl:if>
            <xsl:value-of select="substring(@name,2)"/>
        </xsl:variable>
        <rpdm:policyContainer>
            <xsl:attribute name="url">
                <xsl:value-of select="$url"/>
            </xsl:attribute>
            <xsl:apply-templates select="."/>
        </rpdm:policyContainer>
    </xsl:template>
    <!--
        Components
    -->
    <xsl:template match="assetGroup">
        <lpdm:assetGroup>
            <xsl:call-template name="copyCacheAttributes"/>
            <xsl:copy-of select="@prefixURL"/>
            <xsl:copy-of select="@locationType"/>
        </lpdm:assetGroup>
    </xsl:template>
    <xsl:template match="audioComponent">
        <lpdm:audioComponent>
            <xsl:call-template name="copyCacheAttributes"/>
            <xsl:copy-of select="@fallbackTextComponentName"/>
            <xsl:copy-of select="@fallbackAudioComponentName"/>
            <xsl:apply-templates/>
        </lpdm:audioComponent>
    </xsl:template>
    <xsl:template match="audioAsset">
        <lpdm:audioAsset>
            <xsl:call-template name="copyAudioAssetAttributes"/>
        </lpdm:audioAsset>
    </xsl:template>
    <xsl:template match="deviceAudioAsset">
        <lpdm:deviceAudioAsset>
            <xsl:call-template name="copyAudioAssetAttributes"/>
            <xsl:copy-of select="@deviceName"/>
        </lpdm:deviceAudioAsset>
    </xsl:template>
    <xsl:template match="buttonImageComponent">
        <lpdm:buttonimageComponent>
            <xsl:call-template name="copyCacheAttributes"/>
            <xsl:copy-of select="@fallbackTextComponentName"/>
            <xsl:copy-of select="@upImageComponentName"/>
            <xsl:copy-of select="@downImageComponentName"/>
            <xsl:copy-of select="@overImageComponentName"/>
            <xsl:apply-templates/>
        </lpdm:buttonimageComponent>
    </xsl:template>
    <xsl:template match="chartComponent">
        <lpdm:audioComponent>
            <xsl:call-template name="copyCacheAttributes"/>
            <xsl:copy-of select="@fallbackTextComponentName"/>
            <xsl:copy-of select="@fallbackImageComponentName"/>
            <xsl:copy-of select="@fallbackChartComponentName"/>
            <xsl:apply-templates/>
        </lpdm:audioComponent>
    </xsl:template>
    <xsl:template match="chartAsset">
        <lpdm:chartAsset>
            <xsl:call-template name="copyChartAssetAttributes"/>
        </lpdm:chartAsset>
    </xsl:template>
    <xsl:template match="dynamicVisualComponent">
        <lpdm:audioComponent>
            <xsl:call-template name="copyCacheAttributes"/>
            <xsl:copy-of select="@fallbackTextComponentName"/>
            <xsl:copy-of select="@fallbackImageComponentName"/>
            <xsl:copy-of select="@fallbackAudioComponentName"/>
            <xsl:copy-of select="@fallbackDynVisComponentName"/>
            <xsl:apply-templates/>
        </lpdm:audioComponent>
    </xsl:template>
    <xsl:template match="dynamicVisualAsset">
        <lpdm:dynamicVisualAsset>
            <xsl:call-template name="copyDynamicVisualAssetAttributes"/>
        </lpdm:dynamicVisualAsset>
    </xsl:template>
    <xsl:template match="imageComponent">
        <lpdm:imageComponent>
            <xsl:call-template name="copyCacheAttributes"/>
            <xsl:copy-of select="@fallbackTextComponentName"/>
            <xsl:copy-of select="@fallbackImageComponentName"/>
            <xsl:apply-templates/>
        </lpdm:imageComponent>
    </xsl:template>
    <xsl:template match="deviceImageAsset">
        <lpdm:deviceImageAsset>
            <xsl:call-template name="copyImageAssetAttributes"/>
            <xsl:copy-of select="@deviceName"/>
            <xsl:copy-of select="@localSrc"/>
        </lpdm:deviceImageAsset>
    </xsl:template>
    <xsl:template match="genericImageAsset">
        <lpdm:genericImageAsset>
            <xsl:call-template name="copyImageAssetAttributes"/>
            <xsl:copy-of select="@widthHint"/>
        </lpdm:genericImageAsset>
    </xsl:template>
    <xsl:template match="convertibleImageAsset">
        <lpdm:convertibleImageAsset>
            <xsl:call-template name="copyImageAssetAttributes"/>
        </lpdm:convertibleImageAsset>
    </xsl:template>
    <xsl:template match="linkComponent">
        <lpdm:linkComponent>
            <xsl:call-template name="copyCacheAttributes"/>
            <xsl:copy-of select="@fallbackTextComponentName"/>
            <xsl:apply-templates/>
        </lpdm:linkComponent>
    </xsl:template>
    <xsl:template match="linkAsset">
        <lpdm:linkAsset>
            <xsl:call-template name="copyLinkAssetAttributes"/>
        </lpdm:linkAsset>
    </xsl:template>
    <xsl:template match="rolloverImageComponent">
        <lpdm:rolloverImageComponent>
            <xsl:call-template name="copyCacheAttributes"/>
            <xsl:copy-of select="@fallbackTextComponentName"/>
            <xsl:copy-of select="@normalImageComponentName"/>
            <xsl:copy-of select="@overImageComponentName"/>
            <xsl:apply-templates/>
        </lpdm:rolloverImageComponent>
    </xsl:template>
    <xsl:template match="scriptComponent">
        <lpdm:scriptComponent>
            <xsl:call-template name="copyCacheAttributes"/>
            <xsl:apply-templates/>
        </lpdm:scriptComponent>
    </xsl:template>
    <xsl:template match="scriptAsset">
        <lpdm:scriptAsset>
            <xsl:call-template name="copyScriptAssetAttributes"/>
        </lpdm:scriptAsset>
    </xsl:template>
    <xsl:template match="textComponent">
        <lpdm:textComponent>
            <xsl:call-template name="copyCacheAttributes"/>
            <xsl:copy-of select="@fallbackTextComponentName"/>
            <xsl:apply-templates/>
        </lpdm:textComponent>
    </xsl:template>
    <xsl:template match="textAsset">
        <lpdm:textAsset>
            <xsl:call-template name="copyTextAssetAttributes"/>
        </lpdm:textAsset>
    </xsl:template>
    <!--
        Layouts
    -->
    <xsl:template match="layout | layoutFormat">
        <lpdm:layoutFormat>
            <xsl:call-template name="copyCacheAttributes"/>
            <xsl:apply-templates/>
        </lpdm:layoutFormat>
    </xsl:template>
    <xsl:template match="basicDeviceLayout | basicDeviceLayoutFormat">
        <lpdm:deviceLayoutCanvasFormat>
            <xsl:copy-of select="@deviceName"/>
            <xsl:if test="descendant::fragmentFormat[@isDefaultFragment='true']">
                <xsl:attribute name="defaultFragment">
                    <xsl:value-of
                        select="descendant::fragmentFormat[@isDefaultFragment='true'][1]/@name"
                    />
                </xsl:attribute>
            </xsl:if>
            <xsl:apply-templates/>
        </lpdm:deviceLayoutCanvasFormat>
    </xsl:template>
    <xsl:template name="copyFormatAttributes">
        <xsl:copy-of select="@name"/>
        <xsl:copy-of select="@backgroundColor"/>
        <xsl:if test="@backgroundComponentName != ''">
            <xsl:attribute name="backgroundComponent">
                <xsl:value-of select="@backgroundComponentName"/>
            </xsl:attribute>
            <xsl:attribute name="backgroundComponentType">image</xsl:attribute>
        </xsl:if>
        <xsl:copy-of select="@borderWidth"/>
        <xsl:copy-of select="@cellPadding"/>
        <xsl:copy-of select="@cellSpacing"/>
        <xsl:copy-of select="@horizontalAlignment"/>
        <xsl:copy-of select="@verticalAlignment"/>
        <xsl:copy-of select="@width"/>
        <xsl:copy-of select="@widthUnits"/>
    </xsl:template>
    <xsl:template match="columnIteratorPaneFormat">
        <lpdm:columnIteratorPaneFormat>
            <xsl:call-template name="copyFormatAttributes"/>
            <xsl:copy-of select="@height"/>
            <xsl:apply-templates/>
        </lpdm:columnIteratorPaneFormat>
    </xsl:template>
    <xsl:template match="dissectingPaneFormat">
        <lpdm:dissectingPaneFormat>
            <xsl:call-template name="copyFormatAttributes"/>
            <xsl:if test="@nextShardLinkText != ''">
                <xsl:attribute name="nextLinkText">
                    <xsl:value-of select="@nextShardLinkText"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:if test="@previousShardLinkText != ''">
                <xsl:attribute name="previousLinkText">
                    <xsl:value-of select="@previousShardLinkText"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:apply-templates/>
        </lpdm:dissectingPaneFormat>
    </xsl:template>
    <xsl:template match="emptyFormat">
        <lpdm:emptyFormat/>
    </xsl:template>
    <xsl:template match="formt | formFormat">
        <lpdm:formFormat>
            <xsl:copy-of select="@name"/>
            <xsl:apply-templates/>
        </lpdm:formFormat>
    </xsl:template>
    <xsl:template match="formFragment | formFragmentFormat">
        <lpdm:formFragmentFormat>
            <xsl:copy-of select="@name"/>
            <xsl:if test="@linkText != ''">
                <xsl:attribute name="nextLinkText">
                    <xsl:value-of select="@linkText"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:if test="@linkStyleClass != ''">
                <xsl:attribute name="nextLinkStyleClass">
                    <xsl:value-of select="@linkStyleClass"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:if test="@linkPosition != ''">
                <xsl:attribute name="nextLinkPosition">
                    <xsl:value-of select="@linkPosition"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:if test="@backLinkText != ''">
                <xsl:attribute name="previousLinkText">
                    <xsl:value-of select="@backLinkText"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:if test="@backLinkStyleClass != ''">
                <xsl:attribute name="previousLinkStyleClass">
                    <xsl:value-of select="@backLinkStyleClass"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:if test="@backLinkPosition != ''">
                <xsl:attribute name="previousLinkPosition">
                    <xsl:value-of select="@backLinkPosition"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:if test="@generateReset != ''">
                <xsl:attribute name="allowReset">
                    <xsl:value-of select="@generateReset"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:apply-templates/>
        </lpdm:formFragmentFormat>
    </xsl:template>
    <xsl:template match="fragment | fragmentFormat">
        <lpdm:fragmentFormat>
            <xsl:copy-of select="@name"/>
            <xsl:copy-of select="@linkText"/>
            <xsl:copy-of select="@backLinkText"/>
            <xsl:if test="@generatePeerFragmentLinks != ''">
                <xsl:attribute name="showPeerLinks">
                    <xsl:value-of select="@generatePeerFragmentLinks"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:apply-templates/>
        </lpdm:fragmentFormat>
    </xsl:template>
    <xsl:template match="grid | gridFormat">
        <lpdm:gridFormat>
            <xsl:call-template name="copyFormatAttributes"/>
            <xsl:copy-of select="@height"/>
            <xsl:copy-of select="@rows"/>
            <xsl:copy-of select="@columns"/>
            <xsl:apply-templates/>
        </lpdm:gridFormat>
    </xsl:template>
    <xsl:template match="gridColumns | gridFormatColumns">
        <lpdm:gridFormatColumns>
            <xsl:call-template name="copyFormatAttributes"/>
            <xsl:copy-of select="@height"/>
            <xsl:copy-of select="@rows"/>
            <xsl:copy-of select="@columns"/>
            <xsl:apply-templates select="gridFormatColumn"/>
        </lpdm:gridFormatColumns>
    </xsl:template>
    <xsl:template match="gridColumn | gridFormatColumn">
        <lpdm:gridFormatColumn>
            <xsl:copy-of select="@width"/>
            <xsl:copy-of select="@widthUnits"/>
            <xsl:apply-templates/>
        </lpdm:gridFormatColumn>
    </xsl:template>
    <xsl:template match="gridRow | gridFormatRow">
        <lpdm:gridFormatRow>
            <xsl:copy-of select="@height"/>
            <xsl:apply-templates/>
        </lpdm:gridFormatRow>
    </xsl:template>
    <xsl:template match="pane | paneFormat">
        <lpdm:paneFormat>
            <xsl:call-template name="copyFormatAttributes"/>
            <xsl:copy-of select="@height"/>
            <xsl:apply-templates/>
        </lpdm:paneFormat>
    </xsl:template>
    <xsl:template match="region | regionFormat">
        <lpdm:regionFormat>
            <xsl:copy-of select="@name"/>
            <xsl:apply-templates/>
        </lpdm:regionFormat>
    </xsl:template>
    <xsl:template match="replica | replicaFormat">
        <lpdm:replicaFormat>
            <xsl:copy-of select="@name"/>
            <xsl:copy-of select="@sourceFormatName"/>
            <xsl:copy-of select="@sourceFormatType"/>
            <xsl:apply-templates/>
        </lpdm:replicaFormat>
    </xsl:template>
    <xsl:template match="rowIteratorPane | rowIteratorPaneFormat">
        <lpdm:rowIteratorPaneFormat>
            <xsl:call-template name="copyFormatAttributes"/>
            <xsl:copy-of select="@height"/>
            <xsl:apply-templates/>
        </lpdm:rowIteratorPaneFormat>
    </xsl:template>

    <!--
        Process Themes
    -->
    <xsl:template match="theme">
        <lpdm:theme>
            <xsl:call-template name="copyCacheAttributes"/>
            <xsl:apply-templates/>
        </lpdm:theme>
    </xsl:template>

    <xsl:template match="cssDeviceTheme">
        <lpdm:cssDeviceTheme>
            <xsl:copy-of select="@deviceName"/>
            <xsl:copy-of select="@importParentTheme"/>
            <xsl:apply-templates/>
        </lpdm:cssDeviceTheme>
    </xsl:template>

</xsl:stylesheet>

<!--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Dec-05	10098/4	phussain	VBM:2005110209 merge and recommit...

 15-Nov-05	10098/1	phussain	VBM:2005110209 Migration Framework for Repository Parser - ready for review

 05-Dec-05	10600/1	ianw	VBM:2005120212 Fix up support for 2.7 rpdm

 05-Dec-05	10597/1	ianw	VBM:2005120212 Fix up support for 2.7 rpdm

 11-Oct-05	9729/1	geoff	VBM:2005100507 Mariner Export fails with NPE

 04-Oct-05	9500/7	ianw	VBM:2005091308 Removed specific identity from accessor

 03-Oct-05	9500/5	ianw	VBM:2005091308 Rationalise RPDM and LPDM

 ===========================================================================
-->
