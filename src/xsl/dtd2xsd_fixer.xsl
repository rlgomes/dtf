<!--
	Stylesheet used to fix the testcases that were previously written using 
    DTD and now want to use the newly supported XSD grammar.
-->
<xsl:stylesheet version="2.0"
	            xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns="http://dtf.org/v1">

    <xsl:output method="xml" indent="yes"/>
    
	<xsl:template match="script">
        <xsl:element name="script">
			<xsl:attribute name="name"><xsl:value-of select="@name"/></xsl:attribute>
            <xsl:apply-templates/>
        </xsl:element>	
    </xsl:template>

	<xsl:template match="loop">
        <xsl:if test="@type = 'sequence'">
    		<xsl:element name="for">
    			<xsl:attribute name="property"><xsl:value-of select="@property"/></xsl:attribute>
    			<xsl:attribute name="range"><xsl:value-of select="@range"/></xsl:attribute>
                <xsl:apply-templates/>
            </xsl:element>
        </xsl:if>
        <xsl:if test="@type = 'parallel'">
    		<xsl:element name="parallelloop">
    			<xsl:attribute name="property"><xsl:value-of select="@property"/></xsl:attribute>
    			<xsl:attribute name="range"><xsl:value-of select="@range"/></xsl:attribute>
                <xsl:apply-templates/>
            </xsl:element>
        </xsl:if>
        <xsl:if test="@type = 'timer'">
    		<xsl:element name="timer">
    			<xsl:attribute name="property"><xsl:value-of select="@property"/></xsl:attribute>
    			<xsl:attribute name="interval"><xsl:value-of select="@range"/></xsl:attribute>
                <xsl:apply-templates/>
            </xsl:element>
        </xsl:if>
	</xsl:template>

	<xsl:template match="@*|node()|comment()|text()|processing-instruction()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()|comment()|text()|processing-instruction()"/>
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>