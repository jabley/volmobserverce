<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
    xmlns="http://www.w3.org/2002/06/xhtml2"
    xmlns:mcs="http://www.volantis.com/xmlns/2006/01/xdime/mcs">


    <!-- By default all elements are copied into xhtml2 namespace -->
    <xsl:template match="*">
      <xsl:element
        name="{local-name()}" 
        namespace="http://www.w3.org/2002/06/xhtml2">
        <xsl:attribute name="style"><xsl:value-of select="@style"/></xsl:attribute>
        <xsl:apply-templates match="@*|node()"/>
      </xsl:element>
    </xsl:template>

    <xsl:template match="@*|text()">
      <xsl:copy/>
    </xsl:template>
  
    <!-- Strip comments and head -->
    <xsl:template match="comment()|head"/>
    
    <!-- Ignore html and body but preserve their contents-->
    <xsl:template match="html|body|font">
      <xsl:apply-templates/>
    </xsl:template>
    
    <!-- I tag converted to style -->
    <xsl:template match="i">        
        <span>            
            <xsl:attribute name="style">font-style: italic</xsl:attribute>            
            <xsl:apply-templates/>
        </span>
    </xsl:template>
    
    <!-- B tag converted to style -->
    <xsl:template match="b">        
        <span>            
            <xsl:attribute name="style">font-weight: bold</xsl:attribute>            
            <xsl:apply-templates/>
        </span>
    </xsl:template>
    
    <!-- U tag converted to style -->
    <xsl:template match="u">        
        <span>
            <xsl:attribute name="mcs-text-underline-style">solid</xsl:attribute>            
            <xsl:apply-templates/>
        </span>
    </xsl:template>
    
    <!-- FONT tag converted to styled span -->
    <xsl:template match="font">        
        <span>
            <xsl:attribute name="style"><xsl:value-of select="@style"/></xsl:attribute>
            <xsl:apply-templates/>
        </span>
    </xsl:template>

    <!--BR tag converted to mcs:br -->
    <xsl:template match="br">
        <mcs:br/> 
    </xsl:template>

    <!--IMG tag converted to object -->
    <xsl:template match="img">    
        <object>
            <xsl:attribute name="srctype">image/*</xsl:attribute>
            <xsl:attribute name="src">http://docs.google.com/<xsl:value-of select="@src"/></xsl:attribute>             
            <xsl:apply-templates/>
        </object>
    </xsl:template>

</xsl:stylesheet>
