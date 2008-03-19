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
 * proxy-configuration element
 * 
 * 	@author adam.kowalski@experter.pl
 */

public class ProxyBean {
	
	/**
	 * port
	 */
	private int port;
	
	/**
	 * gateway-timeout
	 */
	private int gatewayTimeout;

	/**
	 * @return Returns the gatewayTimeout.
	 */
	public int getGatewayTimeout() {
		return gatewayTimeout;
	}

	/**
	 * @param gatewayTimeout The gatewayTimeout to set.
	 */
	public void setGatewayTimeout(int gatewayTimeout) {
		this.gatewayTimeout = gatewayTimeout;
	}

	/**
	 * @return Returns the port.
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port The port to set.
	 */
	public void setPort(int port) {
		this.port = port;
	}
	
	
	
	
}
