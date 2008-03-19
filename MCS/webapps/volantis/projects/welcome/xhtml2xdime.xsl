<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
   xmlns="http://www.w3.org/2002/06/xhtml2"
   xmlns:mcs="http://www.volantis.com/xmlns/2006/01/xdime/mcs"
   xmlns:xf="http://www.w3.org/2002/xforms" exclude-result-prefixes="#default">

 
   <xsl:param name="layout">welcome.mlyt</xsl:param>
   <xsl:param name="theme">welcome.mthm</xsl:param>
   <xsl:param name="bind">part</xsl:param>


   <xsl:output method="xml" encoding="UTF-8"/>
   <xsl:template match="/">

      <html xmlns="http://www.w3.org/2002/06/xhtml2"
         xmlns:mcs="http://www.volantis.com/xmlns/2006/01/xdime/mcs"
         xmlns:xf="http://www.w3.org/2002/xforms">

         <!--more namespace decs as required-->

         <head>
            <title>
               <xsl:value-of select="//head/title"/>
            </title>
            <link rel="mcs:layout" href="{$layout}"/>
            <link rel="mcs:theme" href="{$theme}"/>
            <xsl:if test="//form">
               <xf:model>
                  <xf:submission id="{$bind}">
                     <xsl:attribute name="action">
                        <xsl:value-of select="//form/@action"/>
                     </xsl:attribute>

                  </xf:submission>


               </xf:model>
            </xsl:if>
         </head>
         <body>
            <xsl:apply-templates select="//body//div"/>

         </body>
      </html>
   </xsl:template>
   <xsl:template match="h2">
      <h2>
         <xsl:value-of select="."/>
      </h2>
   </xsl:template>
   <xsl:template match="h3">
      <h3>
         <xsl:value-of select="."/>
      </h3>
   </xsl:template>
   <xsl:template match="div">
      <div class="{@class}">
         <xsl:apply-templates/>
      </div>
   </xsl:template>
   <xsl:template match="p">
      <p>
         <xsl:copy-of select="@*"/>
         <xsl:apply-templates/>
      </p>
   </xsl:template>
   <xsl:template match="a">
      <a>
         <xsl:copy-of select="@*"/>
         <xsl:value-of select="."/>
      </a>

   </xsl:template>
   <xsl:template match="img">

      <object src="{@src}">
         <span>
            <xsl:value-of select="@alt"/>
         </span>
      </object>


   </xsl:template>
   <xsl:template match="//select">

      <xsl:choose>
         <xsl:when test="@multiple">
            <!-- handle multiple select cases with xf:select -->
         </xsl:when>

         <xsl:otherwise>
            <xf:select1 ref="{@name}">
               <xf:label/>

               <xsl:for-each select="option">
                  <xf:item>
                     <xf:label>
                        <xsl:value-of select="."/>
                     </xf:label>
                     <xf:value>
                        <xsl:value-of select="@value"/>
                     </xf:value>
                  </xf:item>
               </xsl:for-each>
            </xf:select1>
         </xsl:otherwise>
      </xsl:choose>

   </xsl:template>
   <xsl:template match="//input">

      <xsl:choose>
         <xsl:when test="@type='submit'">
            <xf:submit submission="{$bind}">
               <xf:label>
                  <xsl:value-of select="@value"/>
               </xf:label>
            </xf:submit>
         </xsl:when>
         <!-- catch additional input types here -->
      </xsl:choose>

   </xsl:template>

</xsl:stylesheet>
