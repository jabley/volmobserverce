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
 ! $Header: 
 ! ============================================================================
 ! (c) Volantis Systems Ltd 2003. 
 ! ============================================================================
 ! Change History:
 !
 ! Date         Who             Description
 ! =========    =============== ===============================================
 ! ======================================================================== -->
<!--
 !
 ! ***************************************************************************************************
 ! This file is owned by Architecture and can only be changed by a member of
 ! that group.
 !
 ! This XSLT generates the external XHTML 2 schema individual modules.
 ! ***************************************************************************************************
 !
 !-->
<xsl:stylesheet xmlns:common="http://exslt.org/common" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" version="1.0" exclude-result-prefixes="common xsi">

    <!-- Output as xml using xalan specific extensions. -->
    <xsl:output xmlns:xalan="http://xml.apache.org/xslt"
                xalan:indent-amount="4"
                encoding="us-ascii"
                indent="yes"
                method="xml"/>

    <xsl:include href="GeneratePublicSchemaCore.xsl"/>

    <xsl:template name="GetPublicComment">
        <xsl:comment> ==========================================================================
 ! (c) Volantis Systems Ltd 2004. 
 ! ============================================================================
 ! This document defines the subset of XHTML 2 that is supported within the
 ! XDIME CP Language.
 ! ======================================================================== </xsl:comment>
    </xsl:template>

    <!-- Include all the XHTML2 modules when included from here. -->

    <xsl:template match="xs:include[contains(@schemaLocation, 'modules/xhtml2-anywhere-module-base.xsd')]">
        <xsl:call-template name="IncludeSchema">
            <xsl:with-param name="heading">Anywhere Module</xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template match="xs:include[contains(@schemaLocation, 'modules/xhtml2-caption-module.xsd')]">
        <xsl:call-template name="IncludeSchema">
            <xsl:with-param name="heading">Caption Module</xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template match="xs:include[contains(@schemaLocation, 'modules/xhtml2-common-base.xsd')]">
        <xsl:call-template name="IncludeSchema">
            <xsl:with-param name="heading">Common Module</xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template match="xs:include[contains(@schemaLocation, 'modules/xhtml2-document-module-base.xsd')]">
        <xsl:call-template name="IncludeSchema">
            <xsl:with-param name="heading">Document Module</xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template match="xs:include[contains(@schemaLocation, 'modules/xhtml2-document-module.xsd')]">
        <xsl:call-template name="IncludeSchema">
            <!-- No heading needed as it is provided by base module. -->
            <xsl:with-param name="heading"/>
        </xsl:call-template>
    </xsl:template>

    <!-- This is only used within the XDIME 2 subset of the XHTML 2 schema. -->
    <xsl:template match="xs:include[contains(@schemaLocation, 'modules/xhtml2-link-module.xsd')]">
        <xsl:call-template name="IncludeSchema">
            <xsl:with-param name="heading">Link Module</xsl:with-param>
        </xsl:call-template>
    </xsl:template>

  <!-- This is only used within the XDIME 2 subset of the XHTML 2 schema. -->
    <xsl:template match="xs:include[contains(@schemaLocation, 'modules/xhtml2-access-module.xsd')]">
        <xsl:call-template name="IncludeSchema">
            <xsl:with-param name="heading">Access Module</xsl:with-param>
        </xsl:call-template>
    </xsl:template>



    <xsl:template match="xs:include[contains(@schemaLocation, 'modules/xhtml2-hypertext-module.xsd')]">
        <xsl:call-template name="IncludeSchema">
            <xsl:with-param name="heading">Hypertext Module</xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template match="xs:include[contains(@schemaLocation, 'modules/xhtml2-list-module-base.xsd')]">
        <xsl:call-template name="IncludeSchema">
            <xsl:with-param name="heading">List Module</xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template match="xs:include[contains(@schemaLocation, 'modules/xhtml2-list-module.xsd')]">
        <xsl:call-template name="IncludeSchema">
            <!-- No heading needed as it is provided by base module. -->
            <xsl:with-param name="heading"/>
        </xsl:call-template>
    </xsl:template>

    <xsl:template match="xs:include[contains(@schemaLocation, 'modules/xhtml2-meta-module.xsd')]">
        <xsl:call-template name="IncludeSchema">
            <xsl:with-param name="heading">Meta Module</xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template match="xs:include[contains(@schemaLocation, 'modules/xhtml2-object-module.xsd')]">
        <xsl:call-template name="IncludeSchema">
            <xsl:with-param name="heading">Object Module</xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template match="xs:include[contains(@schemaLocation, 'modules/xhtml2-structural-module-base.xsd')]">
        <xsl:call-template name="IncludeSchema">
            <xsl:with-param name="heading">Structural Module</xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template match="xs:include[contains(@schemaLocation, 'modules/xhtml2-structural-module.xsd')]">
        <xsl:call-template name="IncludeSchema">
            <!-- No heading needed as it is provided by base module. -->
            <xsl:with-param name="heading"/>
        </xsl:call-template>
    </xsl:template>

    <xsl:template match="xs:include[contains(@schemaLocation, 'modules/xhtml2-style-module.xsd')]">
        <xsl:call-template name="IncludeSchema">
            <xsl:with-param name="heading">Style Module</xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="xs:include[contains(@schemaLocation, 'modules/xhtml2-tables-module.xsd')]">
        <xsl:call-template name="IncludeSchema">
            <xsl:with-param name="heading">Tables Module</xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template match="xs:include[contains(@schemaLocation, 'modules/xhtml2-text-module-base.xsd')]">
        <xsl:call-template name="IncludeSchema">
            <xsl:with-param name="heading">Text Module</xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <xsl:template match="xs:include[contains(@schemaLocation, '../../xdime-common/src/modules/xhtml2-text-module.xsd')]">
        <xsl:call-template name="IncludeSchema">
            <!-- No heading needed as it is provided by base module. -->
            <xsl:with-param name="heading"/>
        </xsl:call-template>
    </xsl:template>

    <!-- Purposely ignore all the XHTML2 modules when included from other modules. -->
    <xsl:template match="xs:include[starts-with(@schemaLocation, 'xhtml2-')]"/>        

    <!-- Import other schema when imported from top level schema. -->
    <xsl:template match="xs:import[@schemaLocation = 'xforms.xsd']">
        <xsl:call-template name="ImportSchema"/>
    </xsl:template>

    <!-- Purposely ignore all schema when imported into other modules. -->
    <xsl:template match="xs:import[starts-with(@schemaLocation, '../')]"/>        

</xsl:stylesheet>
