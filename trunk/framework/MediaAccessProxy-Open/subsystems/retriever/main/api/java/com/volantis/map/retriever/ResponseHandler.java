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
/* ----------------------------------------------------------------------------
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.map.retriever;

import java.io.IOException;

/**
 * Should be implemented by other components which will use 
 * resource InputStream or other retrived information
 * 
 * @mock.generate
 */
public interface ResponseHandler {	
	/**
	 * Method which use information contained in Representation
	 * 
	 * @param representation object which contains all needed inforation 
	 * about retrieved resource
	 */
	public void handle(Representation representation) throws IOException;	
}
