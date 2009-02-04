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
package com.volantis.vdp.configuration.sps.bean;


/**
 * configuration JB for sps configuration
 * 	@author adam.kowalski@experter.pl
 */
public class SPSBean{

	/**
	 * secure-connection element
	 */
	private SecureConnectionBean secureConnectionBean;
	/**
	 * web-server element
	 */
	private WebServerBean webServerBean;
	/**
	 * log4j element
	 */
	private Log4jBean log4jBean;
	/**
	 * @return Returns the log4jBean.
	 */
	public Log4jBean getLog4jBean() {
		return log4jBean;
	}
	/**
	 * @param log4jBean The log4jBean to set.
	 */
	public void setLog4jBean(Log4jBean log4jBean) {
		this.log4jBean = log4jBean;
	}
	/**
	 * @return Returns the secureConnectionBean.
	 */
	public SecureConnectionBean getSecureConnectionBean() {
		return secureConnectionBean;
	}
	/**
	 * @param secureConnectionBean The secureConnectionBean to set.
	 */
	public void setSecureConnectionBean(SecureConnectionBean secureConnectionBean) {
		this.secureConnectionBean = secureConnectionBean;
	}
	/**
	 * @return Returns the webServerBean.
	 */
	public WebServerBean getWebServerBean() {
		return webServerBean;
	}
	/**
	 * @param webServerBean The webServerBean to set.
	 */
	public void setWebServerBean(WebServerBean webServerBean) {
		this.webServerBean = webServerBean;
	}
	
	
	
	
	
}
