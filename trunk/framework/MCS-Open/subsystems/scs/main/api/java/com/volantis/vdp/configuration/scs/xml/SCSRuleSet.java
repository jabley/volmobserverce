/*
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
*/
package com.volantis.vdp.configuration.scs.xml;

import our.apache.commons.digester.Digester;
import org.apache.commons.lang.StringUtils;

import com.volantis.vdp.configuration.scs.bean.AuthenticationProviderBean;
import com.volantis.vdp.configuration.scs.bean.Log4jBean;
import com.volantis.vdp.configuration.scs.bean.ProxyBean;
import com.volantis.vdp.configuration.scs.bean.SCSBean;
import com.volantis.vdp.configuration.scs.bean.SecureConnectionBean;
import com.volantis.vdp.configuration.PrefixRuleSet;
import com.volantis.vdp.configuration.PrefixRuleSet;

/**
 * Digester RuleSet for SecureConnectionServer configuration
 * 	@author adam.kowalski@experter.pl
 */

public class SCSRuleSet extends PrefixRuleSet{
	
	/**
	 * the name of xml configuration file
	 */
	private final static String element = "scs-config";
	
//	 javadoc unnecessary
	public SCSRuleSet()
	{
		this.prefix = null;
		
	}
//	 javadoc unnecessary
	public SCSRuleSet(String prefix)
	{
		this.prefix = prefix;
		
	}
	/**
	 * @param digester: set the rules for the scs configuration
	 */
	public void addRuleInstaces(Digester digester)
	{
		final String basePath;
		if(StringUtils.isNotBlank(prefix)) {
			basePath = prefix + "/" + element;
		}
		else {
			basePath = element;
		}
		
		
		digester.addObjectCreate(basePath, SCSBean.class);

		String pattern = basePath + "/proxy-configuration";
		digester.addObjectCreate( pattern, ProxyBean.class );
        digester.addSetNext( pattern, "setProxyBean");
        digester.addSetProperties( pattern, "port", "port");
        digester.addSetProperties( pattern, "gateway-timeout", "gatewayTimeout");

        
		pattern = basePath + "/secure-connection-configuration";
		digester.addObjectCreate( pattern, SecureConnectionBean.class );
        digester.addSetNext( pattern, "setSecureConnectionBean");
        digester.addSetProperties( pattern, "port", "port");
        digester.addSetProperties( pattern, "keep-alive", "keepAlive");

		pattern = basePath + "/authentication-provider";
		digester.addObjectCreate( pattern, AuthenticationProviderBean.class );
        digester.addSetNext( pattern, "setAuthenticationProviderBean");
        digester.addSetProperties( pattern, "class", "classname");

		pattern = basePath + "/log4j";
		digester.addObjectCreate( pattern, Log4jBean.class );
        digester.addSetNext( pattern, "setLog4jBean");
        digester.addSetProperties( pattern, "xml-configuration-file", "filename");
        
	};
}
