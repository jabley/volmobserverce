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
package com.volantis.vdp.configuration.sps.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import our.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

import com.volantis.vdp.configuration.BasicConfiguration;
import com.volantis.vdp.configuration.sps.ISPSConfiguration;
import com.volantis.vdp.configuration.sps.bean.SPSBean;
import com.volantis.vdp.configuration.sps.xml.SPSRuleSet;
import com.volantis.vdp.configuration.exception.ConfigurationException;
import com.volantis.vdp.configuration.BasicConfiguration;

/**
 * 	@author adam.kowalski@experter.pl
 */

public class ISPSConfigurationImpl extends BasicConfiguration implements ISPSConfiguration{

	/**
	 * default filename for sps configuration file
	 */
	public static final String DEFAULT_FILENAME = "sps-config.xml";
	/**
	 * configuration JB for sps configuration file
	 */
	SPSBean configuration;
	
//	 javadoc unnecessary
	public ISPSConfigurationImpl() 
	{
		this.filename = ISPSConfigurationImpl.DEFAULT_FILENAME;
	}
	
	/* (non-Javadoc)
	 * @see com.volantis.vdp.configuration.sps.ISPSConfiguration#getSCHost()
	 */
	public String getSCHost() {
		return configuration.getSecureConnectionBean().getHost();
	}

	/* (non-Javadoc)
	 * @see com.volantis.vdp.configuration.sps.ISPSConfiguration#getSCPort()
	 */
	public int getSCPort() {
		return configuration.getSecureConnectionBean().getPort();
	}

	/* (non-Javadoc)
	 * @see com.volantis.vdp.configuration.sps.ISPSConfiguration#getSCUser()
	 */
	public String getSCUser() {
		
		return configuration.getSecureConnectionBean().getUser();
	}

	/* (non-Javadoc)
	 * @see com.volantis.vdp.configuration.sps.ISPSConfiguration#getSCPassword()
	 */
	public String getSCPassword() {
		
		return configuration.getSecureConnectionBean().getPassword();
	}

	/* (non-Javadoc)
	 * @see com.volantis.vdp.configuration.sps.ISPSConfiguration#getWebServerURL()
	 */
	public String getWebServerURL() {
		
		return configuration.getWebServerBean().getBaseURL();
	}

	/* (non-Javadoc)
	 * @see com.volantis.vdp.configuration.sps.ISPSConfiguration#getLog4jFile()
	 */
	public String getLog4jFile() {
		
		return configuration.getLog4jBean().getFilename();
	}

	/* (non-Javadoc)
	 * @see com.volantis.vdp.configuration.IConfiguration#read()
	 */
	public void read() throws ConfigurationException{
		SPSRuleSet rule = new SPSRuleSet();
		Digester digester = new Digester();
        digester.setValidating( false );
		rule.addRuleInstaces(digester);
		File input = new File(path + File.separatorChar + filename);
		try {
			configuration = (SPSBean) digester.parse(input);
			this.timestamp = (new Date()).getTime();
			
		} catch (IOException e) {
			String msg = "IOException in configuration reading: " + e.getMessage();
			throw new ConfigurationException(msg, e);
		} catch (SAXException e) {
			String msg = "SAXException in configuration reading: " + e.getMessage();
			throw new ConfigurationException(msg, e);
		}
		
	}

	/* (non-Javadoc)
	 * @see com.volantis.vdp.configuration.IConfiguration#refresh()
	 */
	public void refresh() throws ConfigurationException {
		if(this.configuration == null || (this.timestamp + BasicConfiguration.DEFUAL_REFRESH_TIME) < (new Date()).getTime())
		{
			this.read();
		}
		
	}
	
}
