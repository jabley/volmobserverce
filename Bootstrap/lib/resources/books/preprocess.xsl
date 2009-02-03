<?xml version='1.0'?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns="http://docbook.org/ns/docbook"
                xmlns:d="http://docbook.org/ns/docbook"
                xmlns:exsl="http://exslt.org/common"
                xmlns:xi="http://www.w3.org/2001/XInclude"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                exclude-result-prefixes="d exsl xi"
                version='1.0'>

    <xsl:param name="base-dir"/>
    <xsl:param name="change-history-href"/>
    <xsl:param name="includes-dir"/>
    <xsl:variable name="change-history"
                  select="document($change-history-href)"/>

    
    <xsl:template match="*">
        <xsl:copy>
            <xsl:apply-templates select="@*"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="@*">
        <xsl:copy/>
    </xsl:template>

    <xsl:include href="custom-links.xsl"/>
        
        
    <xsl:template match="@href[parent::xi:include]" priority="0.5">
        <xsl:attribute name="href">
            <xsl:value-of select="concat($base-dir, '/', .)"/>
        </xsl:attribute>
    </xsl:template>

    <xsl:template match="@href[starts-with(., '../../built/output/docbook/includes/')]" priority="1">
        <xsl:attribute name="href">
            <xsl:value-of select="concat($includes-dir, substring-after(., '../../built/output/docbook/includes'))"/>
        </xsl:attribute>
    </xsl:template>

    <xsl:template match="d:book[not(/d:book/d:part[@xml:id='appendix'])]">
        <xsl:copy>
            <xsl:apply-templates select="@*"/>
            <xsl:apply-templates/>

            <part xml:id="appendix">
                <title>Appendix</title>
                <xsl:apply-templates select="$change-history"
                                     mode="appendix"/>
            </part>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="d:book/d:part[@xml:id='appendix']">
        <xsl:copy>
            <xsl:apply-templates select="@*"/>
            <xsl:apply-templates/>

            <xsl:apply-templates select="$change-history"
                                 mode="appendix"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="/d:book/d:info">
        <xsl:copy>
            <xsl:apply-templates select="@*"/>
            <xsl:apply-templates/>

            <xsl:apply-templates select="$change-history" mode="header"/>
        </xsl:copy>
    </xsl:template>
    
    <!-- Ignore the revision that is in the document. -->
    <xsl:template match="d:revhistory"/>

    <xsl:template match="*" mode="header">
        <xsl:copy>
            <xsl:apply-templates select="@*" mode="header"/>
            <xsl:apply-templates mode="header"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="@*" mode="header">
        <xsl:copy/>
    </xsl:template>

    <xsl:template match="d:revhistory" mode="header">
        <xsl:copy>
            <xsl:apply-templates select="@*" mode="header"/>

            <title><link linkend="full-change-history">Change History</link></title>

            <xsl:apply-templates select="d:revision[position() &lt;= 10]"
                                 mode="header"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="d:title" mode="header"/>

    <xsl:template match="*" mode="appendix">
        <xsl:copy>
            <xsl:apply-templates select="@*" mode="appendix"/>
            <xsl:apply-templates mode="appendix"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="@*" mode="appendix">
        <xsl:copy/>
    </xsl:template>

    <xsl:template match="d:revhistory" mode="appendix">
        <appendix xml:id="full-change-history">
            <title>Full Change History</title>
            <xsl:copy>
                <xsl:apply-templates select="@*" mode="appendix"/>
                <xsl:apply-templates select="d:revision" mode="appendix"/>
            </xsl:copy>
        </appendix>
    </xsl:template>

    <xsl:template match="d:title" mode="appendix"/>

</xsl:stylesheet>
