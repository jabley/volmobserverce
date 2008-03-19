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

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
xmlns:fo="http://www.w3.org/1999/XSL/Format"
xmlns:mcs="http://www.volantis.com/mcs"
xmlns:xforms="http://www.w3.org/2002/xforms"
xmlns:sel="http://www.volantis.com/sel">
<!--
Version 6: This version moves XForms support to Version 1.1 level by removing the need for namespace
                prefixes on the forms controls elements.
Version 5: This version adds support for the content selection elements and the sel:expr attribute
Version 4: This version completes support for emulated XHTML 2 elements. It adds
                support for overriding default classes on emulated elements.
                It replaces pane elements with pane attributes to make the resulting markup
                more compact and faster to parse.
                It also restructures the stylesheet to simplify processing.
Version 3: This version puts each 'top level' element into its own region
                It also implements most standard XHTML 2 elements and implements the 
                emulated XHTML 2 elements abbr, dfn and var
Version 2: This version has the logic for assigning form fields to pane names 
                All form input field types are supported
-->

    <!-- Global Variables -->
    <!-- The prefix for the default class names used in the generated span elements that emulate 
           abbr, dfn, nl, li within nl, quote and var -->
    <xsl:variable name="emuClassPrefix">
        <xsl:text>mcs-</xsl:text>
    </xsl:variable>

    <!-- The XHTML 2 top level elements (need regions and panes) -->            
    <xsl:template match="address | blockquote | div | dl | h1 | h2 | h3 | h4 | h5 | h6 | ol | p | pre | table | ul">
        <xsl:call-template name="processXHTML2TopLevelElement"/>
    </xsl:template>
    
    <!-- The XHTML 2 sub elements -->            
    <xsl:template match="a | em | cite | code | dd | dt | kbd | label | li | samp | span | strong | sub | sup | td | th | tr ">
        <xsl:call-template name="processXHTML2SubElement"/>
    </xsl:template>
    
    <!-- The Emulated XHTML 2 elements -->
    <xsl:template match="abbr | dfn | quote| var">
        <xsl:call-template name="processEmulatedXHTML2Element"/>
    </xsl:template>
    
    <!-- The Emulated XHTML 2 nl element-->            
    <xsl:template match="nl">
        <xsl:variable name="regionnum">
            <xsl:call-template name="countPrecedingElements"/>
        </xsl:variable>
        <xsl:element name="region">
            <xsl:attribute name="name">
                <xsl:value-of select="concat('Region',$regionnum)"/>
            </xsl:attribute>
            <xsl:element name="canvas">
                <xsl:attribute name="type">
                    <xsl:text>inclusion</xsl:text>
                </xsl:attribute>
                <xsl:attribute name="layoutName">
                    <xsl:call-template name="generateLayoutName">
                        <xsl:with-param name="prefix" select="'nl_'"/>
                    </xsl:call-template>
                </xsl:attribute>             
              	<xsl:apply-templates mode="innl"/>
            </xsl:element>
        </xsl:element>
    </xsl:template>
    
    <!-- The Emulated XHTML 2 li element within an nl -->            
    <xsl:template match="li" mode="innl">
        <xsl:variable name="panenum">
            <xsl:call-template name="countPrecedingListItems"/>
        </xsl:variable>
        <xsl:element name="div">
            <xsl:attribute name="pane">
                <xsl:value-of select="concat('pane',$panenum)"/>
            </xsl:attribute>
            <xsl:call-template name="generateEmulatedXHTML2CoreAttributes">
                <xsl:with-param name="classSuffix" select="'nlli'"/>
            </xsl:call-template>
            <xsl:choose>
                <xsl:when test="@href">
                    <xsl:element name="a">
                        <xsl:attribute name="href">
                            <xsl:value-of select="@href"/>
                        </xsl:attribute>
                        <xsl:apply-templates/>
                    </xsl:element>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates/>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:element>
    </xsl:template>

    <!-- The XHTML 2 object Element-->    
    <xsl:template match="object">
    	<xsl:element name="img">
          <xsl:call-template name="copyAttrs"/>
        	<xsl:attribute name="altText">
        		<xsl:value-of select="."/>
        	</xsl:attribute>
	</xsl:element>
    </xsl:template>

    <!-- The mcs unit element -->
    <xsl:template match="mcs:unit">
        <xsl:element name="unit">
            <xsl:apply-templates/>
        </xsl:element>
    </xsl:template>
       
    <!-- The XFORMS model element -->
    <xsl:template match="xforms:model">
        <xsl:variable name="regionnum">
            <xsl:call-template name="countPrecedingElements"/>
        </xsl:variable>
        <xsl:element name="region">
            <xsl:attribute name="name">
                <xsl:value-of select="concat('Region',$regionnum)"/>
            </xsl:attribute>
            <xsl:element name="canvas">
                <xsl:attribute name="type">
                    <xsl:text>inclusion</xsl:text>
                </xsl:attribute>
                <xsl:attribute name="layoutName">
                    <xsl:call-template name="generateLayoutName">
                        <xsl:with-param name="prefix" select="'xform_'"/>
                    </xsl:call-template>
                </xsl:attribute>
                <xsl:element name="xfform">
                    <xsl:attribute name="name">
                        <xsl:value-of select="@id"/>
                    </xsl:attribute>
                    <xsl:attribute name="action">
                        <xsl:value-of select="xforms:submission/@action"/>
                    </xsl:attribute>
                    <xsl:attribute name="method">
                        <xsl:value-of select="xforms:submission/@method"/>
                    </xsl:attribute>
                    <xsl:apply-templates mode="inform" select="//xforms:* | //input | //textarea | //select | //select1 | 
                    //item | //label | //value | //submit"/>
                </xsl:element>
            </xsl:element>
        </xsl:element>
    </xsl:template>
    
    <!-- The XFORMS input and textarea elements -->
    <xsl:template mode="inform" match="input | textarea">
        <xsl:element name="xftextinput">
            <xsl:attribute name="caption">
                <xsl:value-of select="label"/>
            </xsl:attribute>
            <xsl:attribute name="name">
                <xsl:value-of select="@ref"/>
            </xsl:attribute>
            <xsl:call-template name="copyFormAttrs"/>
            <xsl:call-template name="outputFormFieldPaneNames"/>
        </xsl:element>
    </xsl:template>
       
    <!--XFORMS:select1 element-->
    <xsl:template mode="inform" match="select1">
        <xsl:element name="xfsiselect">
            <xsl:attribute name="caption">
                <xsl:value-of select="label"/>
            </xsl:attribute>
            <xsl:attribute name="name">
                <xsl:value-of select="@ref"/>
            </xsl:attribute>
            <xsl:call-template name="copyFormAttrs"/>          
            <xsl:call-template name="outputFormFieldPaneNames"/>
            <xsl:call-template name="processItems"/>
        </xsl:element>
    </xsl:template>
                    
    <!--XFORMS:select element-->
    <xsl:template mode="inform" match="select">
        <xsl:element name="xfmuselect">
            <xsl:attribute name="caption">
                <xsl:value-of select="label"/>
            </xsl:attribute>
            <xsl:attribute name="name">
                <xsl:value-of select="@ref"/>
            </xsl:attribute>
            <xsl:call-template name="copyFormAttrs"/>
            <xsl:call-template name="outputFormFieldPaneNames"/>
            <xsl:call-template name="processItems"/>
        </xsl:element>
    </xsl:template>
    
    <!--XFORMS:item element-->
    <xsl:template name="processItems">
        <xsl:for-each select="item">
            <xsl:element name="xfoption">
                <xsl:attribute name="caption">
                    <xsl:value-of select="label"/>
                </xsl:attribute>
                <xsl:attribute name="value">
                    <xsl:value-of select="value"/>
                </xsl:attribute>
            </xsl:element>
        </xsl:for-each>        
    </xsl:template>

    <!-- The XFORMS submit element -->   
    <xsl:template mode="inform" match="submit">
        <xsl:element name="xfaction">
            <xsl:attribute name="type">
                <xsl:text disable-output-escaping="no">submit</xsl:text>
            </xsl:attribute>
            <xsl:attribute name="caption">
                <xsl:value-of select="label"/>
            </xsl:attribute>
            <xsl:attribute name="entryPane">
                <xsl:text>submitr</xsl:text>
            </xsl:attribute>
        </xsl:element>
    </xsl:template>
    
   <!-- The XFORMS label element. It must be ignored because its content is used by other elements -->
    <xsl:template mode="inform" match="label"/>
    <xsl:template match="label"/>
    
   <!-- The XFORMS value element. It must be ignored because its content is used by other elements -->
    <xsl:template mode="inform" match="value"/>
    <xsl:template match="value"/>
    
    <!-- Selection Elements -->
    <xsl:template match="sel:select | sel:when | sel:otherwise">
        <xsl:element name="{name()}">
            <xsl:call-template name="copyAttrs"/>
            <xsl:apply-templates/>
        </xsl:element>
    </xsl:template>
    
    <!-- Count the preceding form fields that need a pane -->
    <xsl:template name="countFormFields">
        <xsl:value-of select="count(preceding::input | preceding::submit | preceding::select | 
            preceding::select1 | preceding::textarea)"/>
    </xsl:template>
    
    <!-- Count the preceding li elements in the current list -->
    <xsl:template name="countPrecedingListItems">
        <xsl:value-of select="count(preceding-sibling::li)"/>
    </xsl:template>
    
    <!-- Output form field caption and entry pane names -->
    <xsl:template name="outputFormFieldPaneNames">
        <xsl:variable name="panenum">
            <xsl:call-template name="countFormFields"/>
        </xsl:variable>
        <xsl:attribute name="entryPane">
            <xsl:value-of select="concat('entry',$panenum)"/>
        </xsl:attribute>
        <xsl:attribute name="captionPane">
            <xsl:value-of select="concat('caption',$panenum)"/>
        </xsl:attribute>
    </xsl:template>
    
    <!-- Count the preceding top level elements that need regions -->
    <xsl:template name="countPrecedingElements">
        <xsl:variable name="allpreceding">
             <xsl:value-of select="count(preceding-sibling::*)"/>
        </xsl:variable>
        <xsl:variable name="formpreceding">
            <xsl:call-template name="countFormFields"/>
        </xsl:variable>
        <xsl:value-of select="$allpreceding - $formpreceding"/>
    </xsl:template>
        
    <!-- Process an XHTML 2 top level element -->            
    <xsl:template name="processXHTML2TopLevelElement">
        <xsl:variable name="regionnum">
            <xsl:call-template name="countPrecedingElements"/>
        </xsl:variable>
   
        <xsl:element name="region">
            <xsl:attribute name="name">
                <xsl:value-of select="concat('Region',$regionnum)"/>
            </xsl:attribute>
            
            <xsl:element name="canvas">
                <xsl:attribute name="type">
                    <xsl:text>inclusion</xsl:text>
                </xsl:attribute>
                <xsl:attribute name="layoutName">
                    <xsl:call-template name="generateLayoutName">
                        <xsl:with-param name="prefix" select="'single_'"/>
                    </xsl:call-template>
                </xsl:attribute>
                <xsl:element name="{name()}">
                    <xsl:attribute name="pane">
                        <xsl:text>single</xsl:text>
                    </xsl:attribute>
                    <xsl:call-template name="copyAttrs"/>
            	     <xsl:apply-templates/>
                </xsl:element>
            </xsl:element>
        </xsl:element>
    </xsl:template>
    
    <!-- Process an XHTML 2 subelement -->            
    <xsl:template name="processXHTML2SubElement">
        <xsl:element name="{name()}">
            <xsl:call-template name="copyAttrs"/>
	       <xsl:apply-templates/>
        </xsl:element> 
    </xsl:template>
    
    <!-- Process an emulated XHTML 2 element-->
    <xsl:template name="processEmulatedXHTML2Element">
        <xsl:element name="span">
            <xsl:call-template name="generateEmulatedClass">
                <xsl:with-param name="classSuffix" select="name()"/>
            </xsl:call-template>
            <xsl:copy-of select="@id"/>            	
            <xsl:copy-of select="@title"/>            	
	       <xsl:apply-templates/>
        </xsl:element> 
    </xsl:template>
    
    <!-- Process the class for an emulated XHTML 2 element-->
    <xsl:template name="generateEmulatedClass">
        <xsl:param name="classSuffix"/>
        <xsl:attribute name="class">
            <xsl:choose>
               <xsl:when test="@class">
                   <xsl:value-of select="@class"/>
               </xsl:when>
               <xsl:otherwise>
    	              <xsl:value-of select="concat($emuClassPrefix, $classSuffix)"/>
               </xsl:otherwise>
            </xsl:choose>
        </xsl:attribute>
    </xsl:template>
        
     <!-- Generate a layout name -->
    <xsl:template name="generateLayoutName">
        <xsl:param name="prefix"/>
        <xsl:choose>
            <xsl:when test="@class">
                <xsl:value-of select="concat($prefix, @class)"/>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="concat($prefix, 'default')"/>
            </xsl:otherwise>
         </xsl:choose>
    </xsl:template>
    
    <!-- Copy the XHTML 2 Core attribute collection for a  -->            
    <xsl:template name="generateEmulatedXHTML2CoreAttributes">
        <xsl:param name="classSuffix"/>
        <xsl:call-template name="generateEmulatedClass">
            <xsl:with-param name="classSuffix" select="$classSuffix"/>
        </xsl:call-template>
            <xsl:for-each select="@*">
                <xsl:choose>
                    <xsl:when test="name()='sel:expr'">
                        <xsl:call-template name="copyExpr"/>
                    </xsl:when>
                    <xsl:when test="name()='class'"/>
                    <xsl:otherwise>
                        <xsl:attribute name="{name()}">
                            <xsl:value-of select="."/>            
                        </xsl:attribute>            	
                    </xsl:otherwise>
                </xsl:choose>                
        </xsl:for-each>
    </xsl:template>
    
    <xsl:template name="copyAttrs">
        <xsl:for-each select="@*">
            <xsl:choose>
                <xsl:when test="name()='sel:expr'">
                    <xsl:call-template name="copyExpr"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:attribute name="{name()}">
                        <xsl:value-of select="."/>            
                    </xsl:attribute>            	
                </xsl:otherwise>
            </xsl:choose>                
        </xsl:for-each>
    </xsl:template>
    
    <xsl:template name="copyFormAttrs">
        <xsl:for-each select="@*">
            <xsl:choose>
                <xsl:when test="name()='sel:expr'">
                    <xsl:call-template name="copyExpr"/>
                </xsl:when>
                <xsl:when test="name()='ref'"/>
                <xsl:otherwise>
                    <xsl:attribute name="{name()}">
                        <xsl:value-of select="."/>            
                    </xsl:attribute>            	
                </xsl:otherwise>
            </xsl:choose>                
        </xsl:for-each>
    </xsl:template>

    <xsl:template name="copyExpr">
        <xsl:attribute name="expr">
            <xsl:value-of select="."/>
        </xsl:attribute>
    </xsl:template>
</xsl:stylesheet>

<!--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Jun-04	4630/1	pduffin	VBM:2004060306 Added framework for XDIME-CP interim solution

 ===========================================================================
-->
