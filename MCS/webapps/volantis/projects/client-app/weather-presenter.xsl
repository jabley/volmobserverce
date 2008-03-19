<?xml version="1.0"?>
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

<xsl:stylesheet
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:template match="/">
  
        <response:response xmlns="http://www.w3.org/2002/06/xhtml2"
            xmlns:mcs="http://www.volantis.com/xmlns/2006/01/xdime/mcs"
            xmlns:response="http://www.volantis.com/xmlns/2006/05/widget/response"           
            xmlns:widget="http://www.volantis.com/xmlns/2006/05/widget">
        <response:head>        
          <response:link rel="mcs:theme" href="/themes/main.mthm"/>

        </response:head>
        
        <response:body>
                        
        <widget:block-content id="myWeatherPresenter">
          <table class="widget">
            <tr>
              <td class="nowrap">City: </td>
              <td class="nowrap"><xsl:value-of select="//yweather:location@city"/></td>
            </tr>
            <tr>
              <td class="nowrap">Date:</td>
              <td class="nowrap"><xsl:value-of select="rss/channel/item/pubDate"/></td>
            </tr>
            <tr>
              <td class="nowrap">Weather:</td>            
              <td class="nowrap">
                <xsl:value-of select="//yweather:condition@text"/>, 
                Temp<xsl:text>: </xsl:text><xsl:value-of select="//yweather:condition@temp"/><xsl:text>   </xsl:text><xsl:value-of select="//yweather:units@temperature"/>                         
              </td>
            </tr>          
          </table>
		<!-- Remove the next line if MCS stops rendering tables with align="left" attribute
		     which causes them to be FLOATED to the left margin -->
	  <div style="clear: left;"></div>        </widget:block-content>
        </response:body>
        </response:response>

  </xsl:template>
</xsl:stylesheet>
