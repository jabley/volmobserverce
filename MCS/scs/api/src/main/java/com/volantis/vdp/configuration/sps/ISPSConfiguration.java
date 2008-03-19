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
package com.volantis.vdp.configuration.sps;

/**
 * 	@author adam.kowalski@experter.pl
 */

public interface ISPSConfiguration {
	/**
	 * @return secure-connection-configuration/host
	 */
	String getSCHost();
	/**
	 * @return secure-connection-configuration/port
	 */
	int getSCPort();
	/**
	 * @return secure-connection-configuration/user
	 */
	String getSCUser();
	/**
	 * @return secure-connection-configuration/password
	 */
	String getSCPassword();
	/**
	 * @return web-server/base-url
	 */
	String getWebServerURL();
	/**
	 * @return log4j/xml-configuration-file
	 */
	String getLog4jFile();

}
