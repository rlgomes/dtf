<!-- 
Example XSL that is used in the static Advanced Language Features tutorial that
shows "How to update your tests between different releases" and in this 
specific case this is all about migrating between v1 of the Google static maps
API to v2.

This stylesheet is intended to be used with Saxon 9.x+ on most linuxes you 
should be able to install the package "libsaxonb-java"

Command line usage:

$ saxonb-xslt test.xml v1tov2.xsl

-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fn="http://www.w3.org/2005/xpath-functions"
                version="2.0"
                xmlns:dtf="http://dtf.org/v1"
                > 

    <xsl:output method="xml" indent="yes"/>
      
    <!-- 
    1. calls to the *get_map* function need to have the *key* parameter removed
  
    2. we need to fix any references to the *markers* parameter so that it can
       be compliant with the new format. This includes taking the following 
       marker:
       
       *markers=37.77445,-122.41885,bluea|37.76,-122.41,greenb|37.76,-122.43,redc*
      
       and converting into the new parameters
       
       *markers=color:blue|label:a|37.77445,-122.41885&markers=color:green|label:b|37.76,-122.41&markers=color:red|label:c|37.76,-122.43*
    -->
    
    <xsl:template match="@*|node()|comment()|text()|processing-instruction()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()|comment()|text()|processing-instruction()"/>
        </xsl:copy>
    </xsl:template>

    <!-- fix the markers value 
       37.77445,-122.41885,bluea
       ([-+]{0,1}[0-9]+\.[0-9]+),([-+]{0,1}[0-9]+\.[0-9]+),(black|brown|green|purple|yellow|blue|gray|orange|red|white)([a-z]{0,1})
    -->
    <xsl:function name="dtf:process-markers">
        <xsl:param name="string" />

        <xsl:choose>
            <xsl:when
                test="matches($string,'(([-+]{0,1}[0-9]+\.[0-9]+),([-+]{0,1}[0-9]+\.[0-9]+),(black|brown|green|purple|yellow|blue|gray|orange|red|white)([a-z]{0,1})\|?)+')">
                
                <!-- if the value contains the marker data then we have to 
                     process it here. -->
                     
                <xsl:variable name="nvalue">
                    <xsl:for-each select="fn:tokenize($string,'\|')">
                        <xsl:analyze-string select="."
                                            regex="([^,]+),([^,]+),(black|brown|green|purple|yellow|blue|gray|orange|red|white)(.)">

                            <xsl:matching-substring>
                                <xsl:text>color:</xsl:text><xsl:value-of select="regex-group(3)"/>
                                <xsl:text>|</xsl:text>
                                <xsl:text>label:</xsl:text><xsl:value-of select="regex-group(4)"/>
                                <xsl:text>|</xsl:text>
                                <xsl:value-of select="regex-group(1)"/>
                                <xsl:value-of select="regex-group(2)"/>
                            </xsl:matching-substring>
                        </xsl:analyze-string>
                        <xsl:text>&amp;markers=</xsl:text>
                    </xsl:for-each>
                </xsl:variable>

                <!-- remove the last unnecessary &amp;makers= -->
                <xsl:value-of select="fn:replace($nvalue,'&amp;markers=$','')" />
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$string" />
            </xsl:otherwise>
        </xsl:choose>
    </xsl:function>

    <!--  if there is marker data in the value of a defined property tag then we
          can easily use the existing dtf:process-markers to fix that data -->
    <xsl:template match="dtf:property">
        <xsl:element name="property"
                     namespace="http://dtf.org/v1">
            <xsl:attribute name="name">
                <xsl:value-of select="@name"/>
            </xsl:attribute>
            <xsl:attribute name="value">
                <xsl:value-of select="dtf:process-markers(@value)"/>
            </xsl:attribute>
         </xsl:element>
    </xsl:template>

    <xsl:template match="dtf:call">
        <xsl:choose>
            <xsl:when test="@function='get_map'">
		        <xsl:element name="call"
                             namespace="http://dtf.org/v1">
	                <!-- put the function attribute and value back in place --> 
                    <xsl:attribute name="function">
	                    <xsl:value-of select="@function"/>
	                </xsl:attribute>
                    
		            <xsl:for-each select="dtf:property">
		                <xsl:choose>
                            <!-- lose this property -->
		                    <xsl:when test="@name = 'key'"></xsl:when>
                           
		                    <xsl:when test="@name = 'markers'">
		                        <xsl:element name="property"
                                             namespace="http://dtf.org/v1">
    	                            <xsl:attribute name="name">markers</xsl:attribute>
    	                            <xsl:attribute name="value">
                                        <xsl:value-of select="dtf:process-markers(@value)"/>
                                    </xsl:attribute>
			                     </xsl:element>
		                    </xsl:when>
                           
                            <!-- pass all other properties as they were --> 
		                    <xsl:otherwise>
		                        <xsl:element name="property"
                                             namespace="http://dtf.org/v1">
		                            <xsl:attribute name="name">
                                        <xsl:value-of select="@name"/>
                                    </xsl:attribute>
		                            <xsl:attribute name="value">
                                        <xsl:value-of select="@value"/>
                                    </xsl:attribute>
		                        </xsl:element>
		                    </xsl:otherwise>
		                </xsl:choose>
		            </xsl:for-each>
		        </xsl:element>
            </xsl:when>
            <xsl:otherwise>
		        <xsl:copy>
		            <xsl:apply-templates select="@*|node()|comment()|text()|processing-instruction()"/>
		        </xsl:copy>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

</xsl:stylesheet>
