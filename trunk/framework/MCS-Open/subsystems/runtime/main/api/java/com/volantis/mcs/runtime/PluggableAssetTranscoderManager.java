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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime;

import com.volantis.mcs.integration.AssetTranscoderContext;
import com.volantis.mcs.integration.PluggableAssetTranscoder;
import com.volantis.mcs.integration.TranscodingException;
import com.volantis.mcs.integration.transcoder.ICSWithGIF;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

/**
 * Allows a plugin to be available at all times, even if it is a no-op.
 */
public class PluggableAssetTranscoderManager
    implements PluggableAssetTranscoder {
    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(PluggableAssetTranscoderManager.class);

    /**
     * The actual transcoder plugin to use.
     *
     * @supplierRole transcoder
     * @supplierCardinality 0..1
     * @link aggregation
     */
    protected PluggableAssetTranscoder transcoder;

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param transcoderConfig the configuration from the mariner config
     * @throws java.lang.ClassNotFoundException if the configuration is invalid
     * @throws java.lang.InstantiationException if the configuration is invalid
     * @throws java.lang.IllegalAccessException if the configuration is invalid
     */
    public PluggableAssetTranscoderManager(String transcoderConfig)
        throws ClassNotFoundException, InstantiationException,
        IllegalAccessException {
        initialize(transcoderConfig);
    }

    /**
     * Initialize the {@link #transcoder} from the given configuration
     * data.
     *
     * @param transcoderConfig configuration data to define which plugin to use
     * @throws java.lang.ClassNotFoundException if the configuration is invalid
     * @throws java.lang.InstantiationException if the configuration is invalid
     * @throws java.lang.IllegalAccessException if the configuration is invalid
     */
    public void initialize(String transcoderConfig)
        throws ClassNotFoundException, InstantiationException,
        IllegalAccessException {
        if ((transcoderConfig == null) ||
            ("".equals(transcoderConfig))) {
            transcoder = new ICSWithGIF();
        } else {
            // The configuration value is currently a single, fully qualified
            // java class name that should:
            //
            // 1. exist on the class path used to execute Mariner
            // 2. implement the PluggableAssetTranscoder interface
            // 3. be public
            // 4. have a public zero argument constructor
            // 5. be a thread safe implementation (this cannot be checked)
            try {
                Class customClass =
                    Class.forName(transcoderConfig);
                transcoder =
                    (PluggableAssetTranscoder)customClass.newInstance();
            } catch (ClassNotFoundException e) {
                logger.error("asset-transcoder-impl-missing", e);
                throw e;
            } catch (InstantiationException e) {
                logger.error("transcode-plugin-instantiation-error", e);
                throw e;
            } catch (IllegalAccessException e) {
                logger.error("transcoder-plugin-not-accessible", e);
                throw e;
            } catch (ClassCastException e) {
                logger.error("transcoder-plugin-impl-error", new Object[]{PluggableAssetTranscoder.class.getName()},
                             e);
                throw e;
            }
        }
    }

    // javadoc inherited
    public String constructImageURL(AssetTranscoderContext ctx)
        throws TranscodingException {
        String result = ctx.getUrl();

        if (transcoder != null) {
            result = transcoder.constructImageURL(ctx);
        }

        return result;
    }

    // javadoc inherited
    public String getHostParameter() {
        String result = null;

        if (transcoder != null) {
            result = transcoder.getHostParameter();
        }

        return result;
    }

    // javadoc inherited
    public String getPortParameter() {
        String result = null;

        if (transcoder != null) {
            result = transcoder.getPortParameter();
        }

        return result;
    }

    // javadoc inherited
    public String getWidthParameter() {
        String result = null;

        if (transcoder != null) {
            result = transcoder.getWidthParameter();
        }
        
        return result;
    }

    // javadoc inherited
    public String getMaxImageSizeParameter() {
        String result = null;

        if (transcoder != null) {
            result = transcoder.getMaxImageSizeParameter();
        }

        return result;
    }
    
//  javadoc inherited
    public String getPreserveAreaParameter() {
        String result = null;
        
        if (transcoder != null) {
            result = transcoder.getPreserveAreaParameter();
        }
        
        return result;
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

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 09-Nov-04	6156/1	geoff	VBM:2004110904 ICS GIF support shopuld be on by default in MCS v3.2.3

 04-Nov-04	6109/2	philws	VBM:2004072013 Update the convertImageURLTo... pipeline processes to utilize the current pluggable asset transcoder's parameter names

 21-Sep-04	5559/1	geoff	VBM:2004091506 Support GIF as transcoded image type in MCS and ICS

 29-Jul-04	4991/1	byron	VBM:2004070510 VTS classes need renaming in MCS to ICS

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 13-Oct-03	1547/3	philws	VBM:2003101002 Make use of PrivateAccessor instead of adding a test-only method

 13-Oct-03	1547/1	philws	VBM:2003101002 Fix asset-transcoder plugin attribute reading

 26-Sep-03	1454/1	philws	VBM:2003092401 Provide asset transcoder plugin API and configuration-selectable standard implementations

 ===========================================================================
*/
