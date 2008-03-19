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
 * web-server element
 * 	@author adam.kowalski@experter.pl
 */

public class WebServerBean {

	/**
	 * base-url
	 */
	private String baseURL;

	/**
	 * @return Returns the baseURL.
	 */
	public String getBaseURL() {
		return baseURL;
	}

	/**
	 * @param baseURL The baseURL to set.
	 */
	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}
	
	
}
