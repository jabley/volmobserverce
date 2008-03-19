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
 * secure-connection element
 * 	@author adam.kowalski@experter.pl
 */


public class SecureConnectionBean {

	/**
	 * port
	 */
	private int port;
	
	/**
	 * keep-alive
	 */
	private int keepAlive;

	/**
	 * @return Returns the keepAlive.
	 */
	public int getKeepAlive() {
		return keepAlive;
	}

	/**
	 * @param keepAlive The keepAlive to set.
	 */
	public void setKeepAlive(int keepAlive) {
		this.keepAlive = keepAlive;
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
