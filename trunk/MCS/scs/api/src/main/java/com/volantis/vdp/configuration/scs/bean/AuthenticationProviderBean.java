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
 * authentication-provider element
 * 
 * 	@author adam.kowalski@experter.pl
 */


public class AuthenticationProviderBean {

	/**
	 * class 
	 */
	private String classname;

	/**
	 * @return Returns the className.
	 */
	public String getClassname() {
		return classname;
	}

	/**
	 * @param className The className to set.
	 */
	public void setClassname(String className) {
		this.classname = className;
	}
	
	
}
