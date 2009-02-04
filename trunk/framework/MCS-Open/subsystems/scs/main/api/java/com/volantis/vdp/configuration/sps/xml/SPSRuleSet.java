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
package com.volantis.vdp.configuration.sps.xml;

import our.apache.commons.digester.Digester;
import org.apache.commons.lang.StringUtils;

import com.volantis.vdp.configuration.sps.bean.*;
import com.volantis.vdp.configuration.PrefixRuleSet;
import com.volantis.vdp.configuration.PrefixRuleSet;

/**
 * Digester Rule for sps xml-configuration file
 * 	@author adam.kowalski@experter.pl
 */

public class SPSRuleSet extends PrefixRuleSet{
	
	/**
	 * element name
	 */
	private final static String element = "sps-config";
	
//	 javadoc unnecessary
	public SPSRuleSet()
	{
		this.prefix = null;
	}
//	 javadoc unnecessary
	public SPSRuleSet(String prefix)
	{
		this.prefix = prefix;
		
	}
	/**
	 * @param digester: set the rules for the sps configuration
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

		
		digester.addObjectCreate(basePath, SPSBean.class);
		
		String pattern = basePath + "/secure-connection-configuration";
		digester.addObjectCreate( pattern, SecureConnectionBean.class );
		digester.addSetNext( pattern, "setSecureConnectionBean");
		digester.addSetProperties( pattern, "port", "port");
        digester.addSetProperties( pattern, "host", "host");
        digester.addSetProperties( pattern, "userid", "user");
        digester.addSetProperties( pattern, "password", "password");

		pattern = basePath + "/web-server";
		digester.addObjectCreate( pattern, WebServerBean.class );
		digester.addSetNext( pattern, "setWebServerBean");
        digester.addSetProperties( pattern, "base-url", "baseURL");

		pattern = basePath + "/log4j";
		digester.addObjectCreate( pattern, Log4jBean.class );
		digester.addSetNext( pattern, "setLog4jBean");
        digester.addSetProperties( pattern, "xml-configuration-file", "filename");

		
	};
}
