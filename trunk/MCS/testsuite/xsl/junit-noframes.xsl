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

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:html="http://www.w3.org/Profiles/XHTML-transitional">

<!-- ==========================================================================
 ! $Header: /src/voyager/testsuite/xsl/junit-noframes.xsl,v 1.3 2002/11/29 09:33:59 aboyd Exp $
 ! ============================================================================
 ! (c) Volantis Systems Ltd 2002. 
 ! ============================================================================
 ! Change History:
 !
 ! Date         Who             Description
 ! =========    =============== ===============================================
 ! 27-Sep-02    Phil W-S        VBM:2002092510 - Created. Only variation from
 !                              the standard JUnit Report XSL is inlining the
 !                              stylesheets to allow e-mailing of results.
 ! 29-Nov-02    Allan           VBM:2002112902 - Fixed bug where be <a> tags
 !                              for internal targets were using a # in the
 !                              target name.
 ! ======================================================================= -->

<xsl:include href="toolkit.xsl"/>

<!--
	====================================================
		Create the page structure
    ====================================================
-->
<xsl:template match="testsuites">
	<HTML>
		<HEAD>
			<!--LINK REL ="stylesheet" TYPE="text/css" HREF="stylesheet.css" TITLE="Style"/-->
		<!-- put the style in the html so that we can mail it w/o problem -->
		<style type="text/css">
			BODY {
			font:normal 68% verdana,arial,helvetica;
			color:#000000;
			}
			TD {
			FONT-SIZE: 68%
			}
			P {
			line-height:1.5em;
			margin-top:0.5em; margin-bottom:1.0em;
			}
			H1 {
			MARGIN: 0px 0px 5px; FONT: 165% verdana,arial,helvetica
			}
			H2 {
			MARGIN-TOP: 1em; MARGIN-BOTTOM: 0.5em; FONT: bold 125% verdana,arial,helvetica
			}
			H3 {
			MARGIN-BOTTOM: 0.5em; FONT: bold 115% verdana,arial,helvetica
			}
			H4 {
			MARGIN-BOTTOM: 0.5em; FONT: bold 100% verdana,arial,helvetica
			}
			H5 {
			MARGIN-BOTTOM: 0.5em; FONT: bold 100% verdana,arial,helvetica
			}
			H6 {
			MARGIN-BOTTOM: 0.5em; FONT: bold 100% verdana,arial,helvetica
			}	
            .Error {
            	font-weight:bold; background:#EEEEE0; color:purple;
            }
            .Failure {
            	font-weight:bold; background:#EEEEE0; color:red;
            }
            .Pass {
            	background:#EEEEE0;
            }
			</style>
      <script language="JavaScript"><![CDATA[   
        function toggle (field)
        {
          field.style.display = (field.style.display == "block") ? "none" : "block";
        }  ]]> 
      </script>
		</HEAD>
		<body text="#000000" bgColor="#ffffff">
			<a name="top"></a>
			<xsl:call-template name="header"/>
			
			<!-- Summary part -->
			<xsl:call-template name="summary"/>
			<hr size="1" width="95%" align="left"/>
			
			<!-- Package List part -->
			<xsl:call-template name="packagelist"/>
			<hr size="1" width="95%" align="left"/>
			
			<!-- For each package create its part -->
			<xsl:call-template name="packages"/>
			<hr size="1" width="95%" align="left"/>
			
			<!-- For each class create the  part -->
			<xsl:call-template name="classes"/>
			
		</body>
	</HTML>
</xsl:template>
	
	
	
	<!-- ================================================================== -->
	<!-- Write a list of all packages with an hyperlink to the anchor of    -->
	<!-- of the package name.                                               -->
	<!-- ================================================================== -->
	<xsl:template name="packagelist">	
		<h2>Packages</h2>
		Note: package statistics are not computed recursively, they only sum up all of its testsuites numbers.
		<table border="0" cellpadding="5" cellspacing="2" width="95%">
			<xsl:call-template name="packageSummaryHeader"/>
			<!-- list all packages recursively -->
			<xsl:for-each select="./testsuite[not(./@package = preceding-sibling::testsuite/@package)]">
				<xsl:sort select="@package"/>
				<xsl:variable name="testCount" select="sum(../testsuite[./@package = current()/@package]/@tests)"/>
				<xsl:variable name="errorCount" select="sum(../testsuite[./@package = current()/@package]/@errors)"/>
				<xsl:variable name="failureCount" select="sum(../testsuite[./@package = current()/@package]/@failures)"/>
				<xsl:variable name="timeCount" select="sum(../testsuite[./@package = current()/@package]/@time)"/>
				
				<!-- write a summary for the package -->
				<tr valign="top">
					<!-- set a nice color depending if there is an error/failure -->
					<xsl:attribute name="class">
						<xsl:choose>
						    <xsl:when test="$failureCount &gt; 0">Failure</xsl:when>
							<xsl:when test="$errorCount &gt; 0">Error</xsl:when>
							<xsl:otherwise>Pass</xsl:otherwise>
						</xsl:choose>
					</xsl:attribute>				
					<td><a href="#{@package}"><xsl:value-of select="@package"/></a></td>
					<td><xsl:value-of select="$testCount"/></td>
					<td><xsl:value-of select="$errorCount"/></td>
					<td><xsl:value-of select="$failureCount"/></td>
					<td>
                        <xsl:call-template name="display-time">
                        	<xsl:with-param name="value" select="$timeCount"/>
                        </xsl:call-template>					
					</td>					
				</tr>
			</xsl:for-each>
		</table>		
	</xsl:template>
	
	
	<!-- ================================================================== -->
	<!-- Write a package level report                                       -->
	<!-- It creates a table with values from the document:                  -->
	<!-- Name | Tests | Errors | Failures | Time                            -->
	<!-- ================================================================== -->
	<xsl:template name="packages">
		<!-- create an anchor to this package name -->
		<xsl:for-each select="./testsuite[not(./@package = preceding-sibling::testsuite/@package)]">
			<xsl:sort select="@package"/>
				<a name="{@package}"></a>
				<h3>Package <xsl:value-of select="@package"/></h3>
				
				<table border="0" cellpadding="5" cellspacing="2" width="95%">
					<xsl:call-template name="packageSummaryHeader"/>
			
					<!-- match the testsuites of this package -->
					<xsl:apply-templates select="../testsuite[./@package = current()/@package]"/>					
				</table>
				<a href="#top">Back to top</a>
				<p/>
				<p/>
		</xsl:for-each>
	</xsl:template>

	<!-- ================================================================== -->
	<!-- Process a testsuite node                                           -->
	<!-- It creates a table with values from the document:                  -->
	<!-- Name | Tests | Errors | Failures | Time                            -->
	<!-- It must match the table definition at the package level            -->
	<!-- ================================================================== -->	
	<xsl:template match="testsuite">
		<tr valign="top">
			<!-- set a nice color depending if there is an error/failure -->
			<xsl:attribute name="class">
				<xsl:choose>
				    <xsl:when test="@failures[.&gt; 0]">Failure</xsl:when>
					<xsl:when test="@errors[.&gt; 0]">Error</xsl:when>
					<xsl:otherwise>Pass</xsl:otherwise>
				</xsl:choose>
			</xsl:attribute>
		
			<!-- print testsuite information -->
			<td><a href="#{@name}"><xsl:value-of select="@name"/></a></td>
			<td><xsl:value-of select="@tests"/></td>
			<td><xsl:value-of select="@errors"/></td>
			<td><xsl:value-of select="@failures"/></td>
			<td>
                <xsl:call-template name="display-time">
                	<xsl:with-param name="value" select="@time"/>
                </xsl:call-template>
			</td>
		</tr>
    <tr>
			<td colspan="5">
        <a>
          <xsl:attribute name="onclick">javascript:toggle(<xsl:call-template name="dot-replace"><xsl:with-param name="package"><xsl:value-of select="@package"/>.<xsl:value-of select="@name"/></xsl:with-param></xsl:call-template>);</xsl:attribute>
          Show/Hide Properties
        </a>
        <div>
          <xsl:attribute name="id">
			      <xsl:call-template name="dot-replace">
				      <xsl:with-param name="package">
                <xsl:value-of select="@package"/>.<xsl:value-of select="@name"/>
              </xsl:with-param>
			      </xsl:call-template>
          </xsl:attribute>
          <xsl:attribute name="style">
            display=none
          </xsl:attribute>
          <table>
            <xsl:for-each select="./Properties/Property">
              <xsl:sort select="@name"/>
              <tr>
                <td><xsl:value-of select="@name"/></td>
                <td><xsl:value-of select="@value"/></td>
              </tr>
            </xsl:for-each>
          </table>
        </div>
      </td>
    </tr>
	</xsl:template>
	
	<xsl:template name="classes">
		<xsl:for-each select="./testsuite">
			<xsl:sort select="@name"/>
			<!-- create an anchor to this class name -->
			<a name="{@name}"></a>
			<h3>TestCase <xsl:value-of select="@name"/></h3>
			
			<table border="0" cellpadding="5" cellspacing="2" width="95%">
				<!-- Header -->
				<xsl:call-template name="classesSummaryHeader"/>

				<!-- match the testcases of this package -->
				<xsl:apply-templates select="testcase"/>
			</table>
			<a href="#top">Back to top</a>
		</xsl:for-each>
	</xsl:template>

  <xsl:template name="dot-replace">
	  <xsl:param name="package"/>
	  <xsl:choose>
		  <xsl:when test="contains($package,'.')"><xsl:value-of select="substring-before($package,'.')"/>_<xsl:call-template name="dot-replace"><xsl:with-param name="package" select="substring-after($package,'.')"/></xsl:call-template></xsl:when>
		  <xsl:otherwise><xsl:value-of select="$package"/></xsl:otherwise>
	  </xsl:choose>
  </xsl:template>

</xsl:stylesheet>

<!--
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 10-Feb-04	2846/2	claire	VBM:2004011915 Update to comments and JavaDoc

 ===========================================================================
-->
