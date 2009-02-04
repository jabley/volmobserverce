<?xml version='1.0'?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns="http://docbook.org/ns/docbook"
                xmlns:d="http://docbook.org/ns/docbook"
                xmlns:exsl="http://exslt.org/common"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                exclude-result-prefixes="d exsl"
                version='1.0'>

    <xsl:param name="product-file"/>

    <xsl:variable name="productDOM" select="document($product-file)"/>
    
    <!--
     ! =========================================================================
     !  Templates for transforming custom link types.
     ! =========================================================================
     !-->

    <!-- book://Storefront/main#id Target element with id in main book of Storefront -->
    <!-- book:/main#id -->
    <!-- book:/main -->
    <!-- javadoc://Storefront/com.volantis.blah/Class#method -->
    <!-- javadoc:/com.volantis.blah/ -->
    <xsl:template match="d:link[starts-with(@xlink:href, 'book:/')]" priority="100">

<xsl:message>Product file: <xsl:value-of select="$product-file"/> and <xsl:copy-of select="$productDOM"/></xsl:message>

        <xsl:variable name="href" select="@xlink:href"/>

        <xsl:variable name="relative">
            <xsl:call-template name="extract-product">
                <xsl:with-param name="href" select="$href"/>
                <xsl:with-param name="uri" select="substring-after($href, 'book:/')"/>
            </xsl:call-template>
        </xsl:variable>

        <xsl:copy>
            <!-- Copy all the attributes, apart from ones specifically handled here. -->
            <xsl:apply-templates select="@*" mode="arch-link"/>
            
            <xsl:attribute name="xlink:href">
                <xsl:value-of select="$relative"/>
            </xsl:attribute>

            <!-- Copy the link label. -->
            <xsl:apply-templates select="*|text()"/>
        </xsl:copy>

    </xsl:template>

    <xsl:template name="extract-product">
        <xsl:param name="href"/>
        <xsl:param name="uri"/>

        <xsl:choose>
            <xsl:when test="starts-with($uri,'/')">
                <xsl:variable name="tmp1" select="substring-after($uri, '/')"/>
                <xsl:variable name="product" select="substring-before($tmp1, '/')"/>
                <xsl:variable name="identifier" select="translate($product, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')"/>
                <xsl:variable name="tmp2" select="substring-after($tmp1, '/')"/>
                
                <xsl:variable name="other" select="$productDOM/product/other[identifier = $identifier]"/>
                <xsl:choose>
                    <xsl:when test="$productDOM/product[translate(name, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz') = $identifier or identifier = $identifier]">
                        <!-- Link to current product -->
                        <xsl:call-template name="extract-book">
                            <xsl:with-param name="href" select="$href"/>
                            <xsl:with-param name="uri" select="$tmp2"/>
                            <xsl:with-param name="relative" select="'../../'"/>
                        </xsl:call-template>
                    </xsl:when>
                    <xsl:when test="not($other)">
                        <xsl:message terminate="yes">Unknown product <xsl:value-of select="$product"/></xsl:message>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:variable name="version">
                            <xsl:choose>
                                <xsl:when test="$other/branch"><xsl:value-of select="$other/branch/text()"/></xsl:when>
                                <xsl:otherwise><xsl:value-of select="$other/version/text()"/></xsl:otherwise>
                            </xsl:choose>
                        </xsl:variable>
                        <xsl:call-template name="extract-book">
                            <xsl:with-param name="href" select="$href"/>
                            <xsl:with-param name="uri" select="$tmp2"/>
                            <xsl:with-param name="relative" select="concat('http://architecture.uk.volantis.com/product/', $identifier, '/', $version, '/docbook/books/')"/>
                        </xsl:call-template>
                    </xsl:otherwise>
                </xsl:choose>
                
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="extract-book">
                    <xsl:with-param name="href" select="$href"/>
                    <xsl:with-param name="uri" select="$uri"/>
                    <xsl:with-param name="relative" select="'../../'"/>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="extract-book">
        <xsl:param name="href"/>
        <xsl:param name="uri"/>
        <xsl:param name="relative"/>
        
        <xsl:choose>
            <xsl:when test="substring-before($uri,'#')">
                <xsl:variable name="book" select="substring-before($uri, '#')"/>
                <xsl:variable name="id" select="substring-after($uri, '#')"/>
                
        <!--        <xsl:attribute name="xlink:href">-->
                    <xsl:value-of select="concat($relative, $book, '/html/', 'book.html#', $id)"/>
                <!--</xsl:attribute>-->
            </xsl:when>
            <xsl:otherwise>
                <xsl:variable name="book" select="$uri"/>
                
                <!--<xsl:attribute name="xlink:href">-->
                    <xsl:value-of select="concat($relative, $book, '/html/', 'book.html')"/>
<!--                </xsl:attribute>-->
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="d:link/@xlink:href" mode="arch-link"/>
    <xsl:template match="@*" mode="arch-link">
        <xsl:copy/>
    </xsl:template>

<!--<xsl:template match="text()"/>-->

</xsl:stylesheet>
