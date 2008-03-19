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
package com.volantis.vdp.configuration.scs.bean;
/**
 * configuration JB for the SecureConnectionServer configuration
 * 	@author adam.kowalski@experter.pl
 */

public class SCSBean {
	/**
	 * proxy element
	 */
	private ProxyBean proxyBean;
	/**
	 * secure-connection element
	 */
	private SecureConnectionBean secureConnectionBean;
	/**
	 * authentication-provider element
	 */
	private AuthenticationProviderBean authenticationProviderBean;
	/**
	 * log4j element
	 */
	private Log4jBean log4jBean;
	/**
	 * @return Returns the authenticationProviderBean.
	 */
	public AuthenticationProviderBean getAuthenticationProviderBean() {
		return authenticationProviderBean;
	}
	/**
	 * @param authenticationProviderBean The authenticationProviderBean to set.
	 */
	public void setAuthenticationProviderBean(
			AuthenticationProviderBean authenticationProviderBean) {
		this.authenticationProviderBean = authenticationProviderBean;
	}
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
	 * @return Returns the proxyBean.
	 */
	public ProxyBean getProxyBean() {
		return proxyBean;
	}
	/**
	 * @param proxyBean The proxyBean to set.
	 */
	public void setProxyBean(ProxyBean proxyBean) {
		this.proxyBean = proxyBean;
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


	
	
	
}
