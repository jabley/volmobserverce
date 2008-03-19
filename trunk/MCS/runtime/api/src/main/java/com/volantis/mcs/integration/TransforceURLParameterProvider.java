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
 * The TranscoderURLParameterProvider for transforce mode transcoding.
 */
public final class TransforceURLParameterProvider
        implements TranscoderURLParameterProvider {

    /**
     * The name of the Transforce host parameter.
     */
    private static final String HOST_PARAMETER = "tf.source.host";

    /**
     * The name of the Transforce port parameter.
     */
    private static final String PORT_PARAMETER = "tf.source.port";

    /**
     * The name of the TransForce width parameter.
     */
    private static final String WIDTH_PARAMETER = "tf.width";

    /**
     * The "fixed" TransForce height parameter and value.
     * <p>
     * This is required because Transforce will not do width transcoding
     * without a height setting, even though it is ignored. Weird.
     */
    private static final String HEIGHT_PARAMETER = "tf.height";

    /**
     * The name of the TransForce maxSize parameter.
     */
    private static final String MAXSIZE_PARAMETER = "tf.maxfilesize";

    /**
     * The name of the TransForce preserve area parameter.
     */
    private static final String PRESERVE_AREA_PARAMETER = "tf.p";
    
    
    /**
     * The name of the ICS device-specific policy value.
     */
    private static final String EXTRAS_POLICY_NAME = "transforce.params";

    /**
     * The singleton instance of this class.
     */
    public static final TransforceURLParameterProvider SINGLETON =
            new TransforceURLParameterProvider();

    /**
     * The private constructor.
     */
    private TransforceURLParameterProvider() {
    }

    // javadoc inherited
    public String getHostParameterName() {
        return HOST_PARAMETER;
    }

    // javadoc inherited
    public String getPortParameterName() {
        return PORT_PARAMETER;
    }

    // javadoc inherited
    public String getWidthParameterName() {
        return WIDTH_PARAMETER;
    }

    // javadoc inherited
    public String getHeightParameterName() {
        return HEIGHT_PARAMETER;
    }

    // javadoc inherited
    public String getMaxImageSizeParameterName() {
        return MAXSIZE_PARAMETER;
    }

    // javadoc inherited
    public String getExtrasPolicyParameterName() {
        return EXTRAS_POLICY_NAME;
    }
    // javadoc inherited
    public String getPreserveAreaParameterName() {
        return PRESERVE_AREA_PARAMETER;
    }
    
    
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
