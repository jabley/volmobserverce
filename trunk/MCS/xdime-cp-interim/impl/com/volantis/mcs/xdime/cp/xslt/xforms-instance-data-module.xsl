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
 ! This XSLT module is responsible for handling instance data for XForms.
 !-->
<xsl:stylesheet version="1.0"
    xmlns:cdm="http://www.volantis.com/xmlns/marlin-cdm"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xh2="http://www.w3.org/2002/06/xhtml2"
    xmlns:mcs="http://www.volantis.com/xmlns/2004/06/xdimecp/mcs"
    xmlns:xf="http://www.w3.org/2002/xforms"
    xmlns:sel="http://www.w3.org/2004/06/diselect"
    xmlns:str="http://exslt.org/strings"
    xmlns:si="http://www.volantis.com/xmlns/2004/06/xdimecp/interim/si">

    <!--
     ! Ignore the si:instance since it is processed elsewhere.
     !-->
    <xsl:template mode="form" match="si:instance"/>

    <!--
     ! Ignore the si:item since it is processed elsewhere.
     !-->
    <xsl:template mode="form" match="si:item"/>

    <!--
     ! Ignore the si:item text since it is processed elsewhere,
     !-->
    <xsl:template mode="form" match="si:item/text()"/>

    <!--
     ! Gets the model value for the form control from the form's model. The
     ! value is referenced by the value of the control's ref attribute.
     -->
    <xsl:template mode="form" name="get-model-value">
        <xsl:param name="ref"/>
        <xsl:variable name="num-model-values">
            <xsl:value-of
                select='count(//xf:model/xf:instance/si:instance/si:item[@name = $ref])'/>
        </xsl:variable>

        <!-- Terminate if duplicate names are found in the model -->
        <xsl:if test="$num-model-values > 1">
            <xsl:message terminate="yes">
                <xsl:text>Duplicate names in form model are not allowed: </xsl:text>
                <xsl:value-of select="$ref"/>
            </xsl:message>
        </xsl:if>
        <xsl:value-of select="//xf:model/xf:instance/si:instance/si:item[@name = $ref]"/>
    </xsl:template>

    <!--
     ! Creates the hidden values for the form. These are created from values
     ! specified in the model that are not referenced by any form control.
     -->
    <xsl:template mode="form" name="create-hidden-values">
        <!-- Retrieve all the model items and loop round. -->
        <xsl:for-each select="//xf:model/xf:instance/si:instance/si:item">

            <!-- Save the item's name for matching. -->
            <xsl:variable name="name">
                <xsl:value-of select="@name"/>
            </xsl:variable>

            <!-- Check if a form control references the name. -->
            <xsl:if test="not(//xf:*[@ref = $name])">
                <!--
                 ! Create the xfimplicit element with the name and value
                 ! since no form control references it.
                -->
                <xsl:element name="xfimplicit">
                    <xsl:attribute name="name">
                        <xsl:value-of select="$name"/>
                    </xsl:attribute>
                    <xsl:attribute name="value">
                        <xsl:value-of select="."/>
                    </xsl:attribute>
                </xsl:element>
            </xsl:if>

        </xsl:for-each>
    </xsl:template>

</xsl:stylesheet>
<!--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Sep-04	5380/13	pcameron	VBM:2004090204 Fixed xfmuselect initialisation and reverted to interim si namespace

 27-Aug-04	5310/15	pcameron	VBM:2004082409 Added XForms initialisation to XDIME-CP Interim

 ===========================================================================
-->
