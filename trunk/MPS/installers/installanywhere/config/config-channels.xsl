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
<!--
 ! ============================================================================
 ! (c) Volantis Systems Ltd 2005. 
 ! ============================================================================
 !-->

<xsl:stylesheet 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
	xmlns:mcs="http://www.volantis.com/xmlns/mcs/config"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	exclude-result-prefixes="mcs"
>

<xsl:namespace-alias stylesheet-prefix="mcs" result-prefix="#default"/>

    <!--
     ! Specify XML output
     !-->
    <xsl:output method="xml" indent="yes"/>



    <!--
     ! Find the channels tag and put the MPS channel config stuff in
     !-->
    <xsl:template match="//mcs:channels">

		<xsl:text disable-output-escaping="yes">
            &lt;channels>
                 &lt;channel name="smtp" class="com.volantis.mps.channels.SMTPChannelAdapter">
                     &lt;argument name="host" value="@smtp-channel-host@"/>
                     &lt;argument name="auth" value="@smtp-channel-auth@"/>
                     &lt;argument name="user" value="@smtp-channel-user@"/>
                     &lt;argument name="password" value="@smtp-channel-pass@"/>
                 &lt;/channel>
                  

                 &lt;channel name="smsc" class="com.volantis.mps.channels.LogicaSMSChannelAdapter">
                     &lt;argument name="smsc-ip" value="@sms-channel-ip@"/>
                     &lt;argument name="smsc-port" value="@sms-channel-port@"/>
                     &lt;argument name="smsc-user" value="@sms-channel-user@"/>
                     &lt;argument name="smsc-password" value="@sms-channel-pass@"/>
                     &lt;argument name="smsc-bindtype" value="@sms-channel-bindtype@"/>
                     &lt;argument name="smsc-supportsmulti" value="@sms-channel-multi@"/>
                     &lt;argument name="smsc-pooling" value="@sms-channel-pooling@"/>
                     &lt;argument name="smsc-poolsize" value="@sms-channel-poolsize@"/>
                     &lt;argument name="smsc-on-pool-exhausted" value="@sms-channel-on-pool-exhausted@"/>
                     &lt;argument name="smsc-on-pool-exhausted-wait" value="@sms-channel-on-pool-exhausted-wait@"/>
                     &lt;argument name="smsc-validation-interval" value="@sms-channel-validation-interval@"/>
                     &lt;argument name="smsc-validate-before-use" value="@sms-channel-validate-before-use@"/>
                 &lt;/channel>
                  

                 &lt;channel name="mmsc" class="com.volantis.mps.channels.NokiaMMSChannelAdapter">
                     &lt;argument name="url" value="@mms-config-url@"/>
                     &lt;argument name="default-country-code" value="@mms-config-defcode@"/>
                 &lt;/channel>
                  

                 &lt;channel name="wappush" class="com.volantis.mps.channels.NowSMSWAPPushChannelAdapter">
                     &lt;argument name="url" value="@mss-gateway-url@"/>
                     &lt;argument name="default-country-code" value="@mms-config-defcode@"/>
                     &lt;argument name="message-store-url" value="@message-store-url@/mss"/>
                 &lt;/channel>
            &lt;/channels>
		 </xsl:text>
            <xsl:apply-templates/>
    </xsl:template>

    
    
    <!--
     ! When the markup doesn't exist, copy all "standard" markup.
     !-->
    <xsl:template match="* | node()">
	<xsl:copy>
    	    <xsl:apply-templates select="@*"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    
    <!--
     ! Copy all standard attributes.
     -->
    <xsl:template match="@*">
        <xsl:copy/>
    </xsl:template>
    
</xsl:stylesheet>
