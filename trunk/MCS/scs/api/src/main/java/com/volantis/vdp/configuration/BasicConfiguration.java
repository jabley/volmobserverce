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

import com.volantis.vdp.configuration.exception.ConfigurationException;

/**
 * The base class for other configurations
 * @author adam.kowalski@experter.pl
 */


public abstract class BasicConfiguration implements IConfiguration{
	/**
	 * directory location
	 */
	protected String path;
	/**
	 * filename
	 */
	protected String filename;
	/**
	 * the time of latest refresh in msec
	 */
	protected long timestamp;
	/**
	 * refresh every 15minutes
	 */
	public static final long DEFUAL_REFRESH_TIME = 100*60*15;
//	public static final String DEFAULT_CONFIGURATION_DIR = "/home/experter/workspace/MSCConfiguration/doc";
//	private static final String DEFAULT_CONFIGURATION_DIR = "";
	
//	 javadoc unnecessary
	public BasicConfiguration()
	{
//		path = System.getProperty("config.dir", DEFAULT_CONFIGURATION_DIR);
		path = System.getProperty("config.dir");
		if(path == null)
			throw new RuntimeException("Exception in initializing ConfigurationModule: there is no 'config.dir' property set in VM properties");
		
	}
	
	/**
	 * @return Returns the filename.
	 */
	public String getFilename() {
		return filename;
	}
	/**
	 * @param filename The filename to set.
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}
	/**
	 * @return Returns the timestamp.
	 */
	public long getTimestamp() {
		return timestamp;
	}
	/**
	 * @param timestamp The timestamp to set.
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	/**
	 * @return Returns the path.
	 */
	public String getPath() {
		return path;
	}
	/**
	 * @param path The path to set.
	 */
	public void setPath(String path) {
		this.path = path;
	}
	
	
	/* (non-Javadoc)
	 * @see com.volantis.vdp.configuration.IConfiguration#create()
	 */
	public IConfiguration create() throws ConfigurationException{
		IConfiguration rtConfiguration = null;
		try {
			rtConfiguration = (IConfiguration) this.getClass().newInstance();
			rtConfiguration.refresh();
		} catch (Exception e) {
			throw new ConfigurationException(e.getMessage(), e);
		}
		return rtConfiguration;
	
	}
	
}
