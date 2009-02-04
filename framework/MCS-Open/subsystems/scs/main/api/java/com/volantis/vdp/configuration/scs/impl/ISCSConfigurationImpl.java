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
package com.volantis.vdp.configuration.scs.impl;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import our.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

import com.volantis.vdp.configuration.BasicConfiguration;
import com.volantis.vdp.configuration.scs.ISCSConfiguration;
import com.volantis.vdp.configuration.scs.bean.SCSBean;
import com.volantis.vdp.configuration.scs.xml.SCSRuleSet;
import com.volantis.vdp.configuration.exception.ConfigurationException;
import com.volantis.vdp.configuration.exception.ConfigurationException;
import com.volantis.vdp.configuration.BasicConfiguration;

/**
 * @author adam.kowalski@experter.pl
 */

public class ISCSConfigurationImpl extends BasicConfiguration implements
		ISCSConfiguration {

	/**
	 * default filename for the scs configuration
	 */
	public static final String DEFAULT_FILENAME = "scs-config.xml";

	/**
	 * configuration JB
	 */
	private SCSBean configuration;

//	 javadoc unnecessary
	public ISCSConfigurationImpl() {
		this.filename = ISCSConfigurationImpl.DEFAULT_FILENAME;
	}

	/* (non-Javadoc)
	 * @see com.volantis.vdp.configuration.SecureConnectionServer.ISCSConfiguration#getProxyPort()
	 */
	public int getProxyPort() {
		return configuration.getProxyBean().getPort();
	}

	/* (non-Javadoc)
	 * @see com.volantis.vdp.configuration.SecureConnectionServer.ISCSConfiguration#getProxyGatewayTimeoutt()
	 */
	public int getProxyGatewayTimeoutt() {

		return configuration.getProxyBean().getGatewayTimeout();
	}

	/* (non-Javadoc)
	 * @see com.volantis.vdp.configuration.SecureConnectionServer.ISCSConfiguration#getSCPort()
	 */
	public int getSCPort() {
		return configuration.getSecureConnectionBean().getPort();
	}

	/* (non-Javadoc)
	 * @see com.volantis.vdp.configuration.SecureConnectionServer.ISCSConfiguration#getSCKeepAlive()
	 */
	public int getSCKeepAlive() {

		return configuration.getSecureConnectionBean().getKeepAlive();
	}

	/* (non-Javadoc)
	 * @see com.volantis.vdp.configuration.SecureConnectionServer.ISCSConfiguration#getAuthenticationProviderClass()
	 */
	public String getAuthenticationProviderClass() {

		return configuration.getAuthenticationProviderBean().getClassname();
	}

	/* (non-Javadoc)
	 * @see com.volantis.vdp.configuration.SecureConnectionServer.ISCSConfiguration#getLog4jFile()
	 */
	public String getLog4jFile() {

		return configuration.getLog4jBean().getFilename();
	}

	/* (non-Javadoc)
	 * @see com.volantis.vdp.configuration.IConfiguration#read()
	 */
	public void read() throws ConfigurationException {
		SCSRuleSet rule = new SCSRuleSet();
		Digester digester = new Digester();
		digester.setValidating(false);
		rule.addRuleInstaces(digester);
		File input = new File(path + File.separatorChar + filename);
		try {
			configuration = (SCSBean) digester.parse(input);
			this.timestamp = (new Date()).getTime();

		} catch (IOException e) {
			String msg = "IOException in configuration reading: "
					+ e.getMessage();
			throw new ConfigurationException(msg, e);
		} catch (SAXException e) {
			String msg = "SAXException in configuration reading: "
					+ e.getMessage();
			throw new ConfigurationException(msg, e);
		}

	}

	/* (non-Javadoc)
	 * @see com.volantis.vdp.configuration.IConfiguration#refresh()
	 */
	public void refresh() throws ConfigurationException {
		if (this.configuration == null
				|| (this.timestamp + BasicConfiguration.DEFUAL_REFRESH_TIME) < (new Date())
						.getTime()) {
			this.read();
		}

	}

}
