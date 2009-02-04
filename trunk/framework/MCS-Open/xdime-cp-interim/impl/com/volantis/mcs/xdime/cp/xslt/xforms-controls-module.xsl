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
 ! This XSLT is responsible for handling elements in the XDIME CP Text
 ! Module.
 !-->
<xsl:stylesheet version="1.0"
    xmlns:cdm="http://www.volantis.com/xmlns/marlin-cdm"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xh2="http://www.w3.org/2002/06/xhtml2"
    xmlns:exslt-common="http://exslt.org/common"
    xmlns:str="http://exslt.org/strings"
    xmlns:mcs="http://www.volantis.com/xmlns/2004/06/xdimecp/mcs"
    xmlns:xf="http://www.w3.org/2002/xforms"
    xmlns:sel="http://www.w3.org/2004/06/diselect">

    <!--<xsl:namespace-alias stylesheet-prefix="xh2" result-prefix="#default"/>-->

    <xsl:template mode="form" match="@class | @id">
        <xsl:copy/>
    </xsl:template>


    <xsl:template match="xh2:xfimplicit/@title"/>


    <xsl:template match="@title">
        <xsl:attribute name="prompt">
            <xsl:value-of select="."/>
        </xsl:attribute>
    </xsl:template>

    <!--
     ! By default ignore form control elements at the top level.
     !-->
    <xsl:template mode="top-level-element" match="xf:*">
        <!--
        <xsl:message>Ignoring
            <xsl:value-of select="name()"/>
        </xsl:message>
        -->
    </xsl:template>

    <!--
     ! The first control is what triggers the form to be written out.
     !-->
    <xsl:template mode="top-level-element"
        match="xf:*[not(preceding-sibling::xf:*)]">

        <!--
        <xsl:message>First control
            <xsl:value-of select="name()"/>
        </xsl:message>
        -->

        <!--
         ! Find the submission element within the model.
         !-->
        <xsl:variable name="submission" select="//xf:model/xf:submission"/>

        <xsl:call-template name="WrapInCanvas">
            <xsl:with-param name="contents">
                <xsl:element name="xfform">
                    <!--
                     ! Fixed name of the form.
                     !-->
                    <xsl:attribute name="name">form</xsl:attribute>

                    <!--
                     ! Get the action and method from the submission element.
                     !-->
                    <xsl:attribute name="action">
                        <xsl:value-of select="$submission/@action"/>
                    </xsl:attribute>
                    <xsl:attribute name="method">
                        <xsl:value-of select="$submission/@method"/>
                    </xsl:attribute>

                    <!--
                     ! Process the controls.
                     !-->
                    <xsl:apply-templates mode="form" select="../xf:*"/>

                    <!--
                     ! Create any hidden values.
                     -->
                    <xsl:call-template name="create-hidden-values"/>
                </xsl:element>
            </xsl:with-param>
        </xsl:call-template>

    </xsl:template>

    <!--
     ! Mode: form
     !     Templates that match in this mode are responsible for converting
     ! forms.
     !-->
    <xsl:template mode="form" match="xf:input">
        <xsl:element name="xftextinput">
            <xsl:call-template name="create-initial-value-attribute">
                <xsl:with-param name="ref" select="@ref"/>
            </xsl:call-template>
            <xsl:call-template name="create-entry-pane-attribute"/>
            <xsl:apply-templates mode="form" select="@*"/>
            <xsl:apply-templates mode="form"/>
        </xsl:element>
    </xsl:template>

    <xsl:template mode="form" match="xf:textarea">
        <xsl:element name="xftextinput">
            <xsl:call-template name="create-initial-value-attribute">
                <xsl:with-param name="ref" select="@ref"/>
            </xsl:call-template>
            <xsl:call-template name="create-entry-pane-attribute"/>
            <xsl:apply-templates mode="form" select="@*"/>
            <xsl:apply-templates mode="form"/>
        </xsl:element>
    </xsl:template>

    <xsl:template mode="form" match="xf:select1">
        <xsl:element name="xfsiselect">
            <xsl:call-template name="create-initial-value-attribute">
                <xsl:with-param name="ref" select="@ref"/>
                <xsl:with-param name="valid-values" select="xf:item/xf:value"/>
                <!-- The default value for single select is the first option -->
                <xsl:with-param name="default-value" select="//xf:item[1]/xf:value"/>
            </xsl:call-template>
            <xsl:call-template name="create-entry-pane-attribute"/>
            <xsl:apply-templates mode="form" select="@*"/>
            <xsl:apply-templates mode="form"/>
        </xsl:element>
    </xsl:template>

    <xsl:template mode="form" match="xf:select">
        <xsl:element name="xfmuselect">
            <xsl:call-template name="create-entry-pane-attribute"/>
            <xsl:apply-templates mode="form" select="@*"/>
            <xsl:apply-templates mode="form"/>
        </xsl:element>
    </xsl:template>

    <xsl:template mode="form" match="xf:item">
        <xsl:element name="xfoption">
            <xsl:if test="local-name(..) = 'select' and ../@ref">
                <xsl:call-template name="create-selection-attribute">
                    <xsl:with-param name="ref" select="../@ref"/>
                    <xsl:with-param name="value" select="xf:value/."/>
                </xsl:call-template>
            </xsl:if>
            <xsl:call-template name="create-entry-pane-attribute"/>
            <xsl:apply-templates mode="form" select="@*"/>
            <xsl:apply-templates mode="form"/>
        </xsl:element>
    </xsl:template>

    <xsl:template mode="form" match="xf:value">
        <xsl:attribute name="value">
            <xsl:value-of select="normalize-space(.)"/>
        </xsl:attribute>
        <xsl:apply-templates mode="form" select="@*"/>
        <xsl:apply-templates mode="form"/>
    </xsl:template>

    <xsl:template mode="form" match="xf:submit">
        <xsl:variable name="submission-id" select="//xf:model/xf:submission[1]/@id"/>
        <xsl:if test="@submission != $submission-id">
            <xsl:message terminate="yes">
                <xsl:text>Fatal mismatch between submission ID in header (</xsl:text>
                <xsl:value-of select="$submission-id"/>
                <xsl:text>) and </xsl:text>
                <xsl:value-of select="@submission"/>
            </xsl:message>
        </xsl:if>
        <xsl:element name="xfaction">
            <xsl:attribute name="type">
                <xsl:value-of select="'submit'"/>
            </xsl:attribute>
            <xsl:call-template name="create-entry-pane-attribute"/>
            <xsl:apply-templates mode="form" select="@*"/>
            <xsl:apply-templates mode="form"/>
        </xsl:element>
    </xsl:template>

    <xsl:template mode="form" match="xf:secret">
        <xsl:element name="xftextinput">
            <xsl:call-template name="create-initial-value-attribute">
                <xsl:with-param name="ref" select="@ref"/>
            </xsl:call-template>
            <xsl:attribute name="type">
                <xsl:value-of select="'password'"/>
            </xsl:attribute>
            <xsl:call-template name="create-entry-pane-attribute"/>
            <xsl:apply-templates mode="form" select="@*"/>
            <xsl:apply-templates mode="form"/>
        </xsl:element>
    </xsl:template>

    <xsl:template mode="form" match="xf:label">
        <xsl:variable name="pane-name">
            <xsl:call-template name="get-pane-name"/>
        </xsl:variable>
        <xsl:if test="$pane-name != ''">
            <xsl:attribute name="captionPane">
                <xsl:value-of select="$pane-name"/>
            </xsl:attribute>
        </xsl:if>

        <xsl:attribute name="caption">
            <xsl:value-of select="normalize-space(.)"/>
        </xsl:attribute>
        <xsl:apply-templates mode="form" select="@*"/>
    </xsl:template>

    <xsl:template mode="form" match="xf:label/@class">
        <xsl:attribute name="captionClass">
            <xsl:value-of select="."/>
        </xsl:attribute>
    </xsl:template>

    <xsl:template mode="form" match="xf:submit/@submission"/>

    <xsl:template mode="form" match="xf:label/text() | xf:value/text()"/>

    <!--
     ! Standard templates to detect errors.
     !-->
    <xsl:template mode="form" match="*">
        <xsl:call-template name="unexpected-element"/>
    </xsl:template>

    <xsl:template mode="form" match="@*">
        <xsl:call-template name="unexpected-attribute"/>
    </xsl:template>

    <xsl:template mode="form" match="text()">
        <xsl:call-template name="unexpected-text"/>
    </xsl:template>


    <xsl:template mode="form" name="create-entry-pane-attribute">
        <xsl:variable name="pane-name">
            <xsl:call-template name="get-pane-name"/>
        </xsl:variable>
        <xsl:if test="$pane-name and $pane-name != ''">
            <xsl:attribute name="entryPane">
                <xsl:value-of select="$pane-name"/>
            </xsl:attribute>
        </xsl:if>
    </xsl:template>


    <!--
     ! Creates the initial value attribute for a form control. The initial value
     ! is retrieved from the model by matching the name used in the model with
     ! the control's ref attribute (the ref parameter).
     !
     ! If the control is a selection control then the retrieved value must match
     ! one of the control's values (the valid-values parameter). If no matching
     ! value is found, then the default value (if any) is used to initialise the
     ! control (the default-value parameter).
     !
     ! In the case of a control which can have multiple values selected, you can
     ! specify the separator character that is used (the value-separator
     ! parameter). The default separator is a comma.
     !
     ! A control is only given an initial value attribute if it references a
     ! value in the model.
     -->
    <xsl:template mode="form" name="create-initial-value-attribute">
        <xsl:param name="ref"/>
        <xsl:param name="valid-values"/>
        <xsl:param name="default-value"/>
        <xsl:param name="value-separator" select="','"/>

        <!-- Retrieve the referenced model value, if any -->
        <xsl:if test="$ref and $ref != ''">
            <xsl:variable name="model-value">
                <xsl:call-template name="get-model-value">
                    <xsl:with-param name="ref" select="$ref"/>
                </xsl:call-template>
            </xsl:variable>

            <!-- Only create the attribute if the control references a value -->
            <xsl:if test="$model-value and $model-value != ''">

                <!-- Create the initial value for the control, if any. -->
                <xsl:variable name="initial-value">
                    <xsl:choose>
                        <!-- No valid values to check against so just use the model value. -->
                        <xsl:when test="not($valid-values) or valid-values = ''">
                            <xsl:value-of select="$model-value"/>
                        </xsl:when>

                        <!-- There are valid values to check against. If the model value matches, use it. -->
                        <xsl:when test="exslt-common:node-set($valid-values)[. = $model-value]">
                            <xsl:value-of select="$model-value"/>
                        </xsl:when>

                        <!-- The model value didn't match; it may be multi-valued. -->
                        <xsl:when test="exslt-common:node-set($valid-values)">

                            <!-- Create a variable containing all the valid values. -->
                            <xsl:variable name="matching-values">
                                <!-- Split the possibly multi-valued value into separate values. -->
                                <xsl:for-each
                                    select="str:split($model-value, $value-separator)">
                                    <!-- Get each single value -->
                                    <xsl:variable name="single-value" select="."/>

                                    <!-- Check if the single value matches one of the valid values -->
                                    <xsl:if test="exslt-common:node-set($valid-values)[. = $single-value]">
                                        <!-- Found a match, so append the value to the variable under construction. -->
                                        <xsl:value-of select="$single-value"/>
                                        <xsl:value-of select="$value-separator"/>
                                    </xsl:if>
                                </xsl:for-each>
                            </xsl:variable>

                            <!-- Finally, construct the values for the initial value variable under construction. -->
                            <xsl:choose>
                                <xsl:when test="$matching-values and $matching-values != ''">
                                    <!--
                                     ! There are matching values so use them. Strip off the last
                                     ! value separator.
                                     -->
                                    <xsl:value-of
                                        select="substring($matching-values, 1,
                                        string-length($matching-values) -
                                        string-length($value-separator))"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <!-- There were no matching values so use the default value, if supplied. -->
                                    <xsl:value-of select="$default-value"/>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:when>
                    </xsl:choose>
                </xsl:variable>

                <!-- If there is an initial value, create the attribute. -->
                <xsl:if test="$initial-value and $initial-value != ''">
                    <xsl:attribute name="initial">
                        <xsl:value-of select="$initial-value"/>
                    </xsl:attribute>
                </xsl:if>
            </xsl:if>
        </xsl:if>
    </xsl:template>

    <!--
     ! Creates a selected=true attribute for initialising the xfoption
     ! elements of an xfmuselect multi-select list.
     -->
    <xsl:template mode="form" name="create-selection-attribute">
        <xsl:param name="ref"/>
        <xsl:param name="value"/>
        <xsl:param name="value-separator" select="','"/>

        <!-- Retrieve the referenced model value, if any -->
        <xsl:if test="$ref and $ref != ''">
            <xsl:variable name="model-value">
                <xsl:call-template name="get-model-value">
                    <xsl:with-param name="ref" select="$ref"/>
                </xsl:call-template>
            </xsl:variable>

            <!-- Only create the attribute if the control references a value -->
            <xsl:if test="$model-value and $model-value != ''">

                <!-- Split the possibly multi-valued value into separate values. -->
                <xsl:for-each
                    select="str:split($model-value, $value-separator)">

                    <!-- Check if the single value matches one of the valid values -->
                    <xsl:if test=". = $value">
                        <!-- Found a match, so mark as selected. -->
                        <xsl:attribute name="selected">
                            <xsl:text>true</xsl:text>
                        </xsl:attribute>
                    </xsl:if>
                </xsl:for-each>
            </xsl:if>
        </xsl:if>
    </xsl:template>

    <xsl:template mode="form" match="@ref">
        <xsl:attribute name="name">
            <xsl:value-of select="."/>
        </xsl:attribute>
    </xsl:template>


</xsl:stylesheet>
<!--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Sep-04	5380/30	pcameron	VBM:2004090204 Fixed xfmuselect initialisation and changed si namespace

 06-Sep-04	5378/6	pcameron	VBM:2004090204 Fixed xfmuselect initialisation and changed si namespace

 27-Aug-04	5310/22	pcameron	VBM:2004082409 Added XForms initialisation to XDIME-CP Interim

 22-Jun-04	4645/15	pcameron	VBM:2004060306 Committed for integration

 17-Jun-04	4630/9	pduffin	VBM:2004060306 Integrated and produced distribution

 16-Jun-04	4630/7	pduffin	VBM:2004060306 Some more changes

 16-Jun-04	4645/13	pcameron	VBM:2004060306 Committed for integration

 15-Jun-04	4645/10	pcameron	VBM:2004060306 Committed for integration

 15-Jun-04	4630/3	pduffin	VBM:2004060306 Added some more xdime cp stuff

 11-Jun-04	4645/1	pcameron	VBM:2004060306 Commit changes for integration

 ===========================================================================
-->
