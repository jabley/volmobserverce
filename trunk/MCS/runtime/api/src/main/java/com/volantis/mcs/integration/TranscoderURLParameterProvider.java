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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.integration;

/**
 * Define an interface for providing the names of the transcoder url
 * parameters.
 */
public interface TranscoderURLParameterProvider {
    
    /**
     * Get the name of the host parameter.
     * @return the host parameter name
     */
    String getHostParameterName();
    
    /**
     * Get the name of the port parameter.
     * @return the port parameter name
     */
    String getPortParameterName();
    
    /**
     * Get the name of the width parameter.
     * @return the width parameter name
     */
    String getWidthParameterName();
    
    /**
     * Get the name of the height parameter.
     * @return the name of the height parameter or null if there is no
     * height parameter
     */
    String getHeightParameterName();
    
    /**
     * Get the name of the max image size parameter.
     * @return the max image size parameter name
     */
    String getMaxImageSizeParameterName();
    
    /**
     * Get the name of the extras policy name parameter.
     * @return the extras policy name parameter
     */
    String getExtrasPolicyParameterName();
    
    /**
     * Get the name of the preserve area parameter
     * @return the preserve area parameter name
     */
    String getPreserveAreaParameterName();
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Nov-05	10168/1	ianw	VBM:2005102504 port forward web clipping

 07-Nov-05	10170/1	ianw	VBM:2005102504 port forward web clipping

 04-Nov-05	9999/2	pszul	VBM:2005102504 preserver area implemented in ConvertibleImageAsset
 
 18-Jan-05	6705/1	allan	VBM:2005011708 Remove the height from the width parameter
 
 ===========================================================================
 */
