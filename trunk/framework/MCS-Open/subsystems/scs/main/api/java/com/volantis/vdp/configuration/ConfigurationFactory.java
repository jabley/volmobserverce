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
package com.volantis.vdp.configuration;

import java.lang.reflect.Method;

import com.volantis.vdp.configuration.exception.ConfigurationException;
import com.volantis.vdp.configuration.exception.ConfigurationException;

/**
 * Factory class to create configuration
 * @author adam.kowalski@experter.pl
 */

public class ConfigurationFactory {
	
	//	 javadoc unnecessary
	private static ConfigurationFactory singleton = null;

	/**
	 * @return ConfigurationFactory singleton
	 */
	public static ConfigurationFactory getInstance() {
		if (singleton == null) {
			singleton = new ConfigurationFactory();
		}
		return singleton;
	}

/**
 * @param interface class to be initialized
 * @return interface configuration for the class specified by param
 * @throws ConfigurationException
 */
public IConfiguration getConfiguration(Class clazz) throws ConfigurationException {
		IConfiguration rtConfiguration = null;

		try {
			String packageDefinitionName = "";
			String classDefinitionName = clazz.getName();
			int inx = clazz.getName().lastIndexOf(".");
			if(inx != -1)
			{
				packageDefinitionName = clazz.getName().substring(0, inx+1) + "impl.";
				classDefinitionName = clazz.getName().substring(inx+1);
			}
			
			Class classDefinition = Class.forName(packageDefinitionName + classDefinitionName + "Impl");
			Object object = classDefinition.newInstance();
			Method createMethod = classDefinition.getMethod("create", (Class [])null);
			rtConfiguration = (IConfiguration) createMethod.invoke(object, (Object[])null);
		} catch (Exception e) {
			String msg = "Exception in creating Configuration";
			throw new ConfigurationException(msg, e);
		}
	     return rtConfiguration;
	        
	}}
