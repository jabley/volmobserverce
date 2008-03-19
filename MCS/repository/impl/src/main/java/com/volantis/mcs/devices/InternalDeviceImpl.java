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
 * $Header: /src/voyager/com/volantis/mcs/devices/Device.java,v 1.16 2003/03/20 15:15:29 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 15-Oct-01    Paul            VBM:2001101202 - Added this header and
 *                              added methods to set and remove a policy
 *                              value.
 * 24-Oct-01    Paul            VBM:2001092608 - Made the device patterns part
 *                              of the object and added methods to get and
 *                              set them.
 * 29-Oct-01    Paul            VBM:2001102901 - Device has moved from
 *                              utilities package to devices package.
 * 19-Nov-01    Paul            VBM:2001110202 - Added constructor which only
 *                              takes a fallback device, allowed the policies
 *                              map to be null and also removed the JDBC
 *                              specific imports.
 * 20-Nov-01    Paul            VBM:2001110202 - Removed the extra constructor
 *                              as it is no longer needed because
 *                              MarinerContextDevice now delegates to this
 *                              class itself.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 14-Jan-02    Paul            VBM:2002011414 - Added annotations to indicate
 *                              which of the properties of this class
 *                              constitute its identity.
 * 29-Jan-02    Allan           VBM:2001121703 - Made the patterns property
 *                              into a Set. Fixed bug in
 *                              setFallbackDevice() so that the fallback
 *                              policy name is updated when this method is
 *                              called. Updated class to implement
 *                              DevicePolicyConstants. Use
 *                              FALLBACK_POLICY_CONSTANT in get/set fallback
 *                              methods.
 * 21-Feb-02    Doug            VBM:2002011405 - Pattern strings are now stored
 *                              in a map rather than a set so that revisions
 *                              can be held. Modified setPatterns() and
 *                              getPatterns() accordingly. Modified
 *                              setPolicyValue() to set revision to modified.
 *                              Added the methods getRevisions() and
 *                              setRevisions().
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 07-May-02    Allan           VBM:2002040804 - Added getStyleSheetVersion().
 * 17-May-02    Paul            VBM:2002050101 - Changed the names of some
 *                              annotations to make them more meaningful.
 * 27-Aug-02    Ian             VBM:2002081510 - Added support for TIFF
 *                              encoding.
 * 06-Sep-02    Ian             VBM:2002081307 - Added getPreferredImageType
 *                              method.
 * 11-Oct-02    Chris W         VBM:2002102403 - Added getPixelsY() to return
 *                              the device's height.
 * 28-Nov-02    Mat             VBM:2002112213 - Added supportsAudioEncoding
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.devices;

import com.volantis.devrep.repository.api.devices.DefaultDevice;
import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.assets.AudioAsset;
import com.volantis.mcs.assets.DynamicVisualAsset;
import com.volantis.mcs.assets.ImageAsset;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.policies.variants.audio.AudioEncoding;
import com.volantis.mcs.policies.variants.image.ImageEncoding;
import com.volantis.mcs.policies.variants.image.ImageRendering;
import com.volantis.mcs.policies.variants.metadata.EncodingCollection;
import com.volantis.mcs.policies.variants.metadata.InternalEncodingCollection;
import com.volantis.mcs.policies.variants.video.VideoEncoding;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Provides all methods for accessing a devices name, policies and protocol
 * tag values.
 * <p/>
 * Also used by the management system to add, copy and delete devices.
 * <p/>
 * (Re-written for delta release. Note that further updates required since
 * currently this implementation is specific to JDBC repositories. It might
 * also be better to get rid of PolicyException and use RespositoryException
 * instead.)
 */
public class InternalDeviceImpl
        implements InternalDevice {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(InternalDeviceImpl.class);

    /**
     * Constant for the character used to separate values in a multi-valued
     * policy that is represented as a String.
     */
    private static final String POLICY_VALUE_SEPARATOR = ",";

    /**
     * The details of the audio encoding policies.
     */
    private static final EncodingDevicePolicy [] audioEncodingPolicies;
    private static final EncodingDevicePolicy[] imageEncodingPolicies;

    private static final EncodingDevicePolicy[] videoEncodingPolicies;

    static {
        audioEncodingPolicies = new EncodingDevicePolicy[] {
            new EncodingDevicePolicy("adcpm32inpage", AudioAsset.ADCPM32,
                    AudioEncoding.ADPCM32),

            new EncodingDevicePolicy("baudioinpage", AudioAsset.BASIC_AUDIO,
                    AudioEncoding.BASIC),

            new EncodingDevicePolicy("gsminpage", AudioAsset.GSM_AUDIO,
                    AudioEncoding.GSM),

            new EncodingDevicePolicy("midiinpage", AudioAsset.MIDI_AUDIO,
                    AudioEncoding.MIDI),

            new EncodingDevicePolicy("mp3inpage", AudioAsset.MP3_AUDIO,
                    AudioEncoding.MP3),

            new EncodingDevicePolicy("realaudioinpage", AudioAsset.REAL_AUDIO,
                    AudioEncoding.REAL),

            new EncodingDevicePolicy("msmainpage", AudioAsset.WINDOWS_MEDIA_AUDIO,
                    AudioEncoding.WINDOWS_MEDIA),

            new EncodingDevicePolicy("amrinpage", AudioAsset.AMR_AUDIO,
                    AudioEncoding.AMR),

            new EncodingDevicePolicy("imelodyinpage", AudioAsset.IMELODY_AUDIO,
                    AudioEncoding.IMELODY),

            new EncodingDevicePolicy("nokringinpage", AudioAsset.NOKRING_AUDIO,
                    AudioEncoding.NOKIA_RING_TONE),

            new EncodingDevicePolicy("rmfinpage", AudioAsset.RMF_AUDIO,
                    AudioEncoding.RMF),

            new EncodingDevicePolicy("smafinpage", AudioAsset.SMAF_AUDIO,
                    AudioEncoding.SMAF),

            new EncodingDevicePolicy("spmidiinpage", AudioAsset.SP_MIDI_AUDIO,
                    AudioEncoding.SP_MIDI),

            new EncodingDevicePolicy("wavinpage", AudioAsset.WAV_AUDIO,
                    AudioEncoding.WAV),
        };

        imageEncodingPolicies = new EncodingDevicePolicy [] {

            new EncodingDevicePolicy("bmpinpage", ImageAsset.BMP,
                    ImageEncoding.BMP),

            new EncodingDevicePolicy("gifinpage", ImageAsset.GIF,
                    ImageEncoding.GIF),

            new EncodingDevicePolicy("jpeginpage", ImageAsset.JPEG,
                    ImageEncoding.JPEG),

            new EncodingDevicePolicy("pjpeginpage", ImageAsset.PJPEG,
                    ImageEncoding.PJPEG),

            new EncodingDevicePolicy("pnginpage", ImageAsset.PNG,
                    ImageEncoding.PNG),

            new EncodingDevicePolicy("tiffinpage", ImageAsset.TIFF,
                    ImageEncoding.TIFF),

            new EncodingDevicePolicy("wbmpinpage", ImageAsset.WBMP,
                    ImageEncoding.WBMP),

            new EncodingDevicePolicy("videotexinpage", ImageAsset.VIDEOTEX,
                    ImageEncoding.VIDEOTEX),

        };

        videoEncodingPolicies = new EncodingDevicePolicy[] {
            new EncodingDevicePolicy("agifinpage",
                    DynamicVisualAsset.ANIMATED_GIF,
                    VideoEncoding.ANIMATED_GIF),

            new EncodingDevicePolicy("mpeg1inpage",
                    DynamicVisualAsset.MPEG1,
                    VideoEncoding.MPEG1),

            new EncodingDevicePolicy("mpeg4inpage",
                    DynamicVisualAsset.MPEG4,
                    VideoEncoding.MPEG4),

            new EncodingDevicePolicy("msvidinpage",
                    DynamicVisualAsset.WINDOWS_MEDIA_VIDEO,
                    VideoEncoding.WINDOWS_MEDIA),

            new EncodingDevicePolicy("flashinpage",
                    DynamicVisualAsset.MACROMEDIA_FLASH,
                    VideoEncoding.MACROMEDIA_FLASH),

            new EncodingDevicePolicy("swinpage",
                    DynamicVisualAsset.MACROMEDIA_SHOCKWAVE,
                    VideoEncoding.MACROMEDIA_SHOCKWAVE),

            new EncodingDevicePolicy("qtimeinpage",
                    DynamicVisualAsset.QUICKTIME_VIDEO,
                    VideoEncoding.QUICKTIME),

            new EncodingDevicePolicy("realvidinpage",
                    DynamicVisualAsset.REAL_VIDEO,
                    VideoEncoding.REAL),

            new EncodingDevicePolicy("tvinpage",
                    DynamicVisualAsset.TV,
                    VideoEncoding.TV),
        };
    }

    /**
     * Multi-valued policies that have been converted into List objects.
     */
    private Map listPolicies = Collections.synchronizedMap(new HashMap());

    /**
     * Bit-set representation of supported image encodings.
     */
    private int supportedImageEncodingBitSet;

    /**
     * Array equivalent of supportedImageEncodingBitSet for use with Repository
     * queries.
     */
    private int[] supportedImageEncodingsArray;

    /**
     * Bit-set representation of supported dynVis encodings.
     */
    private int supportedDynVisEncodings;

    /**
     * Array equivalent of supportedDynVisEncodings for use with Repository
     * queries.
     */
    private int[] supportedDynVisEncodingsArray;

    /**
     * Bit-set representation of supported audio encodings.
     */
    private int supportedAudioEncodingBitSet;

    private EncodingCollection supportedAudioEncodings;

    private EncodingCollection supportedImageEncodings;

    private EncodingCollection supportedVideoEncodings;

    /**
     * The configuration associated with this device
     * @todo the configuration is stored as an object because the device
     * is in a different subsystem to the protocol configuration. This needs
     * addressing
     */
    private Object protocolConfiguration;

    private DefaultDevice device;

    /**
     * Create a new <code>InternalDevice</code>.
     */
    public InternalDeviceImpl(DefaultDevice device) {
        this.device = device;
    }

    public String getName() {
        return device.getName();
    }

    /**
     * Set the value for the specified policy.
     *
     * @param policy The name of the policy to set.
     * @param value  The new value for the policy.
     */
    public void setPolicyValue(String policy, String value) {
        device.setPolicyValue(policy, value);
        // Remove from the listPolicies cache so that users of listPolicies
        // know that this policy must be reloaded.
        listPolicies.remove(policy);
    }

    /**
     * Remove the setting of the specified policy.
     *
     * @param policy The name of the policy to remove.
     */
    public void removePolicyValue(String policy) {
        device.removePolicyValue(policy);
        listPolicies.remove(policy);
    }

    public String selectSingleKnownPolicyValue(String policy,
                                          Collection selection) {
        if(policy==null) {
            throw new IllegalArgumentException("Cannot be null: policy");
        }
        if(selection==null) {
            throw new IllegalArgumentException("Cannot be null: selection");
        }
        String value = getPolicyValue(policy);

        if (value != null && value.indexOf(POLICY_VALUE_SEPARATOR) != -1) {
            // The value is a multi-valued value so we need to find the
            // the right policy to select. This is done by obtaining a List
            // representation of the policy value and finding the first
            // value in the List that is in the selection.
            List listPolicyValue =
                    (List) listPolicies.get(policy);
            if(listPolicyValue==null) {
                listPolicyValue = createListFromPolicyValue(value);
                listPolicies.put(policy, listPolicyValue);
            }
            // Iterate over the listPolicyValue until a value is found that
            // is in the given selection.
            value = null;
            int size = listPolicyValue.size();
            for(int i=0; i<size && value==null; i++) {
                if(selection.contains(listPolicyValue.get(i))) {
                    value = (String) listPolicyValue.get(i);
                }
            }
        } else if (value != null) {
            // The value is a single value so make sure it is in the
            // available selection.
            value = selection.contains(value) ? value : null;
        }

        return value;
    }

    /**
     * Create a List from a multi-valued policy value. Note that the
     * PolicyValue api is not used here because it expects contained values
     * to be other PolicyValues which they are not - they are Strings - and
     * converting these Strings to more PolicyValue objects creates
     * unnecessary memory usage. Additionally PolicyValues cannot be created
     * without knowledge of the type of the policy value and that is not
     * known by InternalDevice.
     *
     * @param multiValuedPolicyValue the multi-valued policy represented as
     * a series of comma separated strings.
     * @return a List representation of the given multi-valued policy value.
     */
    private List createListFromPolicyValue(String multiValuedPolicyValue) {
        List listPolicyValue = new ArrayList();
        StringTokenizer tokenizer =
                new StringTokenizer(multiValuedPolicyValue,
                        POLICY_VALUE_SEPARATOR);
        while(tokenizer.hasMoreTokens()) {
            String nextToken = tokenizer.nextToken();
            if(nextToken.startsWith(" ")) {
                nextToken = nextToken.trim();
            }
            listPolicyValue.add(nextToken);
        }

        return listPolicyValue;
    }

    public String getSpecifiedPolicyValue(String policyName) {
        return device.getPolicyValue(policyName);
    }

    public boolean getBooleanPolicyValue(String policy) {
        String value = getPolicyValue(policy);

        return "true".equalsIgnoreCase(value);
    }

    public int getIntegerPolicyValue(String policy) {
        String value = getPolicyValue(policy);

        return Integer.parseInt(value);
    }

    public int getIntegerPolicyValue(String policy, int defaultValue) {
        String value = getPolicyValue(policy);

        if (value == null) {
            return defaultValue;
        }

        return Integer.parseInt(value);
    }

    public Iterator getPolicyNames() {
        return device.getPolicyNames();
    }

    public String getTVChannelPrefix() {
        return getPolicyValue("tvChannelPrefix");
    }

    public int getPixelDepth() {
        return getIntegerPolicyValue("pixeldepth", 0);
    }

    public int getPixelsX() {
        return getIntegerPolicyValue("pixelsx", 0);
    }

    public int getPixelsY() {
        return getIntegerPolicyValue("pixelsy", 0);
    }

    public int getDeprecatedRenderMode() {
        String s = getPolicyValue("rendermode");

        // Currently there is a descrepency between a device rendermode
        // and an ImageAsset rendering. This should be addressed soon but
        // for now we will fix it here.
        if (s.equals("greyscale")) {
            return ImageAsset.MONOCHROME;
        } else {
            return ImageAsset.COLOR;
        }
    }

    public ImageRendering getRenderMode() {

        String s = getPolicyValue("rendermode");

        // Currently there is a discrepency between a device rendermode
        // and an ImageAsset rendering. This should be addressed soon but
        // for now we will fix it here.
        if (s.equals("greyscale")) {
            return ImageRendering.GRAYSCALE;
        } else {
            return ImageRendering.COLOR;
        }
    }

    public int getPreferredImageType() {
        String preferredImageType = getPolicyValue("preferredimagetype");
        return ImageAsset.getEncodingFromEncodingName(preferredImageType);
    }

    public String getStyleSheetVersion() {

        // Treat the ssversion as an ordered list and take the first
        // known value from the list, which should give us future compatibility
        // against extra style sheet versions being added to the device
        // repository that this existing code knows nothing about.
        return selectSingleKnownPolicyValue(
            DevicePolicyConstants.STYLE_SHEET_VERSION,
            DevicePolicyConstants.SUPPORTED_STYLE_SHEET_VERSIONS);
    }

    /**
     * Initialize the supported image encodings.
     */
    protected void initialiseSupportedImageEncodings() {
        supportedImageEncodingBitSet = 0;

        List encodings = new ArrayList();
        for (int i = 0; i < imageEncodingPolicies.length; i++) {
            EncodingDevicePolicy encodingPolicy = imageEncodingPolicies[i];
            if (getBooleanPolicyValue(encodingPolicy.getPolicy())) {
                supportedImageEncodingBitSet |= encodingPolicy.getOldEncoding();
                encodings.add(encodingPolicy.getNewEncoding());
            }
        }
        supportedImageEncodings = new InternalEncodingCollection(encodings);
    }

    /**
     * Initialize the supported audio encodings.
     */
    protected void initialiseSupportedAudioEncodings() {
        supportedAudioEncodingBitSet = 0;

        List encodings = new ArrayList();
        for (int i = 0; i < audioEncodingPolicies.length; i++) {
            EncodingDevicePolicy encodingPolicy = audioEncodingPolicies[i];
            if (getBooleanPolicyValue(encodingPolicy.getPolicy())) {
                supportedAudioEncodingBitSet |= (1 << encodingPolicy.getOldEncoding());
                encodings.add(encodingPolicy.getNewEncoding());
            }
        }
        supportedAudioEncodings = new InternalEncodingCollection(encodings);
    }

    public boolean supportsAudioEncoding(int encoding) {
        if (supportedAudioEncodings == null) {
            initialiseSupportedAudioEncodings();
        }

        int bitEncoding = 1 << encoding;
        if (logger.isDebugEnabled()) {
            String supported = ((bitEncoding & supportedAudioEncodingBitSet) != 0) ?
                    "supported" : "not supported";
            logger.debug("Encoding " + encoding + " " + supported);
        }
        return (bitEncoding & supportedAudioEncodingBitSet) != 0;
    }

    public EncodingCollection getSupportedAudioEncodings() {
        if (supportedAudioEncodings == null) {
            initialiseSupportedAudioEncodings();
        }

        return supportedAudioEncodings;
    }

    public EncodingCollection getSupportedImageEncodings() {
        if (supportedImageEncodings == null) {
            initialiseSupportedImageEncodings();
        }
        
        return supportedImageEncodings;
    }

    public boolean supportsImageEncoding(int encoding) {
        if (supportedImageEncodings == null) {
            initialiseSupportedImageEncodings();
        }

        return (encoding & supportedImageEncodingBitSet) != 0;
    }

    public int[] getSupportedImageEncodingArray() {
        if (supportedImageEncodingsArray == null) {

            if (supportedImageEncodings == null) {
                initialiseSupportedImageEncodings();
            }

            // First time through count the number of bits set.
            int count;
            int encodings;
            for (count = 0, encodings = supportedImageEncodingBitSet; encodings != 0;
                 count += 1) {
                // Generate a mask which will clear the lowest bit when ANDed with the
                // encodings and select it when the inverse of the mask is ANDed
                // with the encodings.
                int mask = encodings - 1;

                // Clear the lowest bit.
                encodings = encodings & mask;
            }

            supportedImageEncodingsArray = new int[count];

            encodings = supportedImageEncodingBitSet;
            for (count = 0, encodings = supportedImageEncodingBitSet; encodings != 0;
                 count += 1) {
                // Generate a mask which will clear the lowest bit when ANDed with the
                // encodings and select it when the inverse of the mask is ANDed
                // with the encodings.
                int mask = encodings - 1;

                supportedImageEncodingsArray[count] = encodings & ~mask;

                // Clear the lowest bit.
                encodings = encodings & mask;
            }
        }

        return supportedImageEncodingsArray;
    }

    protected void initialiseSupportedDynVisEncodings() {
        supportedDynVisEncodings = 0;

        List encodings = new ArrayList();
        for (int i = 0; i < videoEncodingPolicies.length; i++) {
            EncodingDevicePolicy encodingPolicy = videoEncodingPolicies[i];
            if (getBooleanPolicyValue(encodingPolicy.getPolicy())) {
                supportedDynVisEncodings |= encodingPolicy.getOldEncoding();
                encodings.add(encodingPolicy.getNewEncoding());
            }
        }
        supportedVideoEncodings = new InternalEncodingCollection(encodings);

        supportedDynVisEncodings = 0;
    }

    public boolean supportsDynVisEncoding(int encoding) {
        if (supportedVideoEncodings == null) {
            initialiseSupportedDynVisEncodings();
        }

        return (encoding & supportedDynVisEncodings) != 0;
    }

    public int[] getSupportedDynVisEncodings() {
        if (supportedDynVisEncodingsArray == null) {

            if (supportedVideoEncodings == null) {
                initialiseSupportedDynVisEncodings();
            }

            // First time through count the number of bits set.
            int count;
            int encodings;
            for (count = 0, encodings = supportedDynVisEncodings; encodings != 0;
                 count += 1) {
                // Generate a mask which will clear the lowest bit when ANDed with the
                // encodings and select it when the inverse of the mask is ANDed
                // with the encodings.
                int mask = encodings - 1;

                // Clear the lowest bit.
                encodings = encodings & mask;
            }

            supportedDynVisEncodingsArray = new int[count];

            encodings = supportedDynVisEncodings;
            for (count = 0, encodings = supportedDynVisEncodings; encodings != 0;
                 count += 1) {
                // Generate a mask which will clear the lowest bit when ANDed with the
                // encodings and select it when the inverse of the mask is ANDed
                // with the encodings.
                int mask = encodings - 1;

                supportedDynVisEncodingsArray[count] = encodings & ~mask;

                // Clear the lowest bit.
                encodings = encodings & mask;
            }
        }

        return supportedDynVisEncodingsArray;
    }

    public EncodingCollection getSupportedVideoEncodings() {
        if (supportedVideoEncodings == null) {
            initialiseSupportedDynVisEncodings();
        }

        return supportedVideoEncodings;
    }

    // Javadoc inherited
    public List getPolicyValues(String policyName) {
        // Currently not implemented, will be done as part of requirement 822
        return null;
    }

    // Javadoc inherited
    public List getCommaSeparatedPolicyValues(String policyName) {
        List policyValues;
        
        String value = getPolicyValue(policyName);
        
        if (value == null) {
            policyValues = Collections.EMPTY_LIST;
            
        } else {
            // Split values by comma.
            String[] values = value.split(",");
            
            // Trim leading and trainling white characters.
            for (int i = 0; i < values.length; i++) {
                values[i] = values[i].trim();
            }

            policyValues = Arrays.asList(values);
        }
        
        return policyValues;
    }

    public Object getProtocolConfiguration() {
        return this.protocolConfiguration;
    }

    public void setProtocolConfiguration(Object protocolConfiguration) {
        this.protocolConfiguration = protocolConfiguration;
    }

    public InternalDevice getFallbackDevice() {
        final DefaultDevice defaultDevice = device.getFallbackDevice();
        final InternalDeviceImpl fallbackDevice;
        if (defaultDevice != null) {
            fallbackDevice = new InternalDeviceImpl(defaultDevice);
        } else {
            fallbackDevice = null;
        }
        return fallbackDevice;
    }

    public String getPolicyValue(final String policy) {
        return device.getComputedPolicyValue(policy);
    }

    public Device getDevice() {
        return device;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Sep-05	9540/3	geoff	VBM:2005091906 Protocol Parameterisation: Basic Rendundant CSS Property Filtering

 15-Sep-05	9472/1	ibush	VBM:2005090808 Add default styling for sub/sup elements

 21-Jun-05	8833/1	pduffin	VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

 28-Apr-05	7908/1	pduffin	VBM:2005042712 Removing Revision object, added UNKNOWN_REVISION constant to JDBCAccessorHelper. This will be removed when revisions are removed from the database tables

 14-Dec-04	6472/3	allan	VBM:2004121003 Intern device names and policies

 14-Dec-04	6472/1	allan	VBM:2004121003 Intern device names and device property names

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Nov-04	5733/1	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 22-Sep-04	5567/3	allan	VBM:2004092010 Rework issues.

 21-Sep-04	5567/1	allan	VBM:2004092010 Handle multi-valued device policy selection.

 10-Aug-04	5147/1	adrianj	VBM:2004080318 Added support for TAC values to importer

 28-Jul-04	4940/2	geoff	VBM:2004072103 Public API for Device Repository (umbrella)

 03-Jun-04	4532/1	byron	VBM:2004052104 ASCII-Art Image Asset Encoding

 25-Mar-04	3537/1	geoff	VBM:2004031905 Websphere Session Persistence complains about InternalDevice

 24-Mar-04	3484/9	geoff	VBM:2004031905 Websphere Session Persistence complains about InternalDevice

 22-Mar-04	3484/7	geoff	VBM:2004031905 Websphere Session Persistence complains about InternalDevice (add no arg constructor and avoid nullpointer exceptions)

 22-Mar-04	3484/5	geoff	VBM:2004031905 Websphere Session Persistence complains about InternalDevice (do it properly)

 19-Mar-04	3484/1	geoff	VBM:2004031905 Websphere Session Persistence complains about InternalDevice

 19-Feb-04	2789/4	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/2	tony	VBM:2004012601 Localised logging (and exceptions)

 11-Feb-04	2862/2	allan	VBM:2004020411 The DeviceRepositoryBrowser.

 09-Feb-04	2846/1	claire	VBM:2004011915 Adding project init to identities. Fixing assetURL rewrite

 30-Jan-04	2767/1	claire	VBM:2004012701 Add project

 05-Dec-03	2075/1	mat	VBM:2003120106 Rename Device and add a public Device Interface

 02-Sep-03	1290/11	steve	VBM:2003082105 Synchronise the whole of getSecondaryDevice

 02-Sep-03	1290/9	steve	VBM:2003082105 Synchronise the whole of addSecondaryDevice

 02-Sep-03	1290/7	steve	VBM:2003082105 One return point from getSecondaryDevice

 02-Sep-03	1290/5	steve	VBM:2003082105 Rework and made device cache threadsafe

 02-Sep-03	1290/3	steve	VBM:2003082105 Secondary ID Header implementation

 02-Sep-03	1290/1	steve	VBM:2003082105 Secondary ID Header implementation

 ===========================================================================
*/
