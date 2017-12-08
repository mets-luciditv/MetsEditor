<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" >
    <xsl:output method="xml" omit-xml-declaration="yes" indent="no"/>
    <xsl:template match="div">
        <xsl:apply-templates/>
    </xsl:template>
    <xsl:template match="p">
        <xsl:copy>
            <xsl:apply-templates />
        </xsl:copy>
    </xsl:template>
    <xsl:template match="p/node()">
        <xsl:call-template name="IdentityTransform" />
    </xsl:template>
    <xsl:template match="p/child::node()[1]">
        <lb xml:id='P0000L00'></lb><xsl:call-template name="IdentityTransform" />
    </xsl:template>
    <xsl:template match="p/br">
        <lb xml:id='P0000L00'></lb>
    </xsl:template>
    <xsl:template match="@*|node()">
        <xsl:call-template name="IdentityTransform" />
    </xsl:template>
    <xsl:template name="IdentityTransform">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="p/text()">
        <xsl:if test="position() = 1"><lb></lb></xsl:if>
        <xsl:value-of
                select="translate(.,'&#xA;','')"/>
    </xsl:template>
</xsl:stylesheet>