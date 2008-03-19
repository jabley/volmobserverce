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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.devices;

import com.volantis.mcs.policies.variants.image.ImageRendering;
import com.volantis.mcs.policies.variants.metadata.EncodingCollection;

import java.util.Collection;
import java.util.List;
import java.util.Iterator;

/**
 * @mock.generate
 * @volantis-api-exclude-from PublicAPI
 * @volantis-api-exclude-from ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 */
public interface InternalDevice {

    /**
     * Get the name of the device.
     * @return The name of the device.
     */
    public String getName ();

    /**
     * Get the fallback device.
     */
    InternalDevice getFallbackDevice();

    /**
     * Select a single known policy value for a specified policy that may or
     * may not be a multi-valued policy, where the single policy value must be
     * one of the values in a provided Collection.
     * <p>
     * If a policy value is obtained for the policy that is not in
     * the provided Collection then this method will return null. Otherwise
     * this method will return the first value obtained for the given device
     * policy that is in the provided Collection.
     * <p>
     * NOTE: This method expects multi-valued policy values to be separated
     * by the POLICY_VALUE_SEPARATOR character.
     * <p>
     * <strong>NOTE:</strong> This is useful for dealing with policy values
     * which may be ordered lists in order to guarantee future compatibility.
     * For example, protocols may be specified as a list for this reason. Since
     * we know the names of all the protocols shipped with a particular version
     * of the product, we can supply them here and if a new version of the
     * device repository contains a new protocol that we do not understand, we
     * can skip it and use the next best entry in the list. Currently existing
     * device repositories use this facility sparingly at best, but may make
     * more use of it in future.
     *
     * @param policy    the name of the policy whose value to select
     * @param selection a Collection of selectable policy values
     * @return the selected policy value from the provided Collection of
     *         selectable policy values or null of no policy value obtained from the
     *         device is included in the selection.
     * @throws IllegalArgumentException if either policy or selection are null
     */
    String selectSingleKnownPolicyValue(String policy,
                                        Collection selection);

    /**
     * Get the value for the specified policy iff the value was specified on
     * this physical device, i.e. ignore any inherited/fallback device values.
     *
     * @param policyName String name of policy whose value to get
     * @return requested policy value
     */
    String getSpecifiedPolicyValue(String policyName);

    /**
     * Get the value for the specified policy.
     *
     * @param policy String name of policy whose value to get
     * @return requested policy value
     */
    String getPolicyValue(String policy);

    boolean getBooleanPolicyValue(String policy);

    int getIntegerPolicyValue(String policy);

    int getIntegerPolicyValue(String policy, int defaultValue);

    /**
     * Convenience method to get the tv channel prefix for this
     * device.
     *
     * @return tvChannelPrefix policy for this device or null if no
     *         so policy exists for this device.
     */
    String getTVChannelPrefix();

    /**
     * Convenience method to get the pixel depth policy for this
     * device as an int.
     *
     * @return pixel depth policy for this device as an int or 0 if
     *         the pixeldepth policy is not found.
     */
    int getPixelDepth();

    /**
     * Convenience method to get the pixelx policy for this
     * device as an int.
     *
     * @return pixelsx policy for this device as an int or 0 if
     *         the pixelsx policy is not found.
     */
    int getPixelsX();

    /**
     * Convenience method to get the pixely policy for this
     * device as an int.
     *
     * @return pixelsy policy for this device as an int or 0 if
     *         the pixelsy policy is not found.
     */
    int getPixelsY();

    /**
     * Convenience method to get the rendermode policy for this
     * device as an int.
     *
     * @return rendermode policy for this device as an int or 0 if
     *         the rendermode policy is not found.
     */
    int getDeprecatedRenderMode();

    /**
     * Convenience method to get the rendermode policy for this
     * device as an object.
     *
     * @return rendermode policy for this device as an object.
     */
    ImageRendering getRenderMode();

    /**
     * Convenience method to get the preferredImageType policy for this
     * device as an int.
     *
     * @return preferredImageType policy for this device as an int or 0 if
     *         the rendermode policy is not found.
     */
    int getPreferredImageType();

    /**
     * Convenience method to get the ssversion policy for this device.
     *
     * @return the ssversion (StyleSheetVersion) for this device.
     */
    String getStyleSheetVersion();

    /**
     * State whether this device supports a particular encoding.
     *
     * @param encoding the encoding in question
     * @return true if this device supports the encoding; otherwise false.
     * @see com.volantis.mcs.assets.AudioAsset
     */
    boolean supportsAudioEncoding(int encoding);

    EncodingCollection getSupportedAudioEncodings();

    EncodingCollection getSupportedImageEncodings();

    /**
     * State whether this device supports a particular encoding.
     *
     * @param encoding the encoding in question
     * @return true if this device supports the encoding; otherwise false.
     * @see com.volantis.mcs.assets.ImageAsset
     */
    boolean supportsImageEncoding(int encoding);

    /**
     * Provide all the image encodings supported by this device in an array.
     *
     * @return int array containing all the values of all the image encodings
     *         supported by this device.
     */
    int[] getSupportedImageEncodingArray();

    /**
     * State whether this device supports a particular encoding.
     *
     * @param encoding the encoding in question
     * @return true if this device supports the encoding; otherwise false.
     * @see com.volantis.mcs.assets.DynamicVisualAsset
     */
    boolean supportsDynVisEncoding(int encoding);

    /**
     * Provide all the image encodings supported by this device in an array.
     *
     * @return int array containing all the values of all the image encodings
     *         supported by this device.
     */
    int[] getSupportedDynVisEncodings();

    EncodingCollection getSupportedVideoEncodings();

    Iterator getPolicyNames();

    // Javadoc inherited
    List getPolicyValues(String policyName);

    /**
     * Returns the list of policy values, assuming that the policy value is
     * comma separated string of the values.
     * 
     * @param policyName The policy name
     * @return The list of policy values.
     */
    List getCommaSeparatedPolicyValues(String policyName);

    /**
     * get the protocol configuration associated with this device
     *
     * @return
     */
    Object getProtocolConfiguration();

    /**
     * set the protocol configuration associated with the device
     *
     * @param protocolConfiguration
     */
    void setProtocolConfiguration(Object protocolConfiguration);

    Device getDevice();
}
