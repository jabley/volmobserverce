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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.map.ics.imageprocessor.parameters.impl;

import com.volantis.map.ics.configuration.OutputImageRules;
import com.volantis.map.common.param.ParameterBuilderException;
import com.volantis.map.localization.LocalizationFactory;
import com.volantis.map.common.param.Parameters;
import com.volantis.map.common.param.ParameterNames;
import com.volantis.map.common.param.MutableParameters;
import com.volantis.map.operation.ResourceDescriptor;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * This param builder retrieves parameters for image format preservation from
 * service config and servlet request and stores them into Params block If the
 * builder is used, it changes MaxImageSize and DestinationFormatRule.
 */
public class PreservationParamsBuilder {



    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
        LocalizationFactory.createLogger(PreservationParamsBuilder.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
        LocalizationFactory.createExceptionLocalizer(
            PreservationParamsBuilder.class);

    /**
     * Map used for matching image MIME type with image conversion rule and
     * device policy.
     */
    private static final Map FORMAT_RULES_TABLE;

    /**
     * Repository for accessing devices database. Opening of device repository
     * is quite long operation, so repository is cached during calls to improve
     * performance of ICS when image format preservation feature is turned on.
     */
//    private static DeviceRepository repository = null;

    /**
     * Used for synchronizing access to repository.
     */
    private static Object synchronizer = new Object();

    /**
     * Internal class for storing DestinationFormatRule and corresponding
     * policies. Used by Map FORMAT_RULES_TABLE
     */
    static class RulesWithPolicies {

        private String outputRule;

        private String policy;

        public RulesWithPolicies(String rule, String newPolicy) {
            outputRule = rule;
            policy = newPolicy;
        }

        public String getOutputRule() {
            return outputRule;
        }

        public String getPolicy() {
            return policy;
        }
    }

    static {
        Map rules = new HashMap();

        rules.put("image/png",
                  new RulesWithPolicies(OutputImageRules.COLOURPNG24,
                                        "pnginpage"));
        rules.put("image/gif",
                  new RulesWithPolicies(OutputImageRules.COLOURGIF8,
                                        "gifinpage"));
        rules.put("image/jpeg",
                  new RulesWithPolicies(OutputImageRules.COLOURJPEG24,
                                        "jpeginpage"));
        rules.put("image/vnd.wap.wbmp",
                  new RulesWithPolicies(OutputImageRules.WBMP,
                                        "wbmpinpage"));
        rules.put("image/bmp",
                  new RulesWithPolicies(OutputImageRules.COLOURBMP24,
                                        "bmpinpage"));
        rules.put("image/tiff",
                  new RulesWithPolicies(OutputImageRules.COLOURTIFF24,
                                        "tiffinpage"));
        rules.put("image/svg+xml", new RulesWithPolicies("", ""));

        FORMAT_RULES_TABLE = Collections.synchronizedMap(rules);
    }

    /**
     * Processes HttpServletRequest and gets the device by User-Agent from the
     * device repository, which URL is specified in the config. Checks whether
     * particular image is supported by the device, and if it's not, sets
     * DestinationFormatRule to the preferred image format. If there is no
     * preferredimage type policy for the given device, tries to set
     * DestinationFormatRule to any supported format Also checks if the image
     * size is appropriate for the divice, otherwise sets MaxImageSize to the
     * maximum supported size.
     *
     * @param request - Servlet request to retrieve image processing parameters
     *                from.
     * @param descritptor the resource descriptor for this request
     * @throws com.volantis.map.common.param.ParameterBuilderException is thrown if it is impossible to determine
     *                               whether image format preservation is
     *                               neededor not.
     */
    public void process(HttpServletRequest request,
                        ResourceDescriptor descritptor) throws ParameterBuilderException {

        try {
            Parameters params = descritptor.getInputParameters();

            if (params.containsName(ParameterNames.SUPPORTED_IMAGES)) {

                RulesWithPolicies preservedRule = (RulesWithPolicies) FORMAT_RULES_TABLE
                        .get(params.getParameterValue(ParameterNames.SOURCE_IMAGE_MIME_TYPE));

                if (preservedRule == null) {
                    LOGGER.warn("content-type-not-supported", params.getParameterValue(ParameterNames.SOURCE_IMAGE_MIME_TYPE));
                    return;
                }

                String [] supportedTypes = params.getParameterValue(ParameterNames.SUPPORTED_IMAGES).split(",");
                for (int i = 0; i < supportedTypes.length; i++) {
                    if(preservedRule.getPolicy().startsWith(supportedTypes[i].toLowerCase())) {
                        ((MutableParameters)params).removeParameterValue(ParameterNames.DESTINATION_FORMAT_RULE);
                    }
                }
            }
            
        } catch (Exception e) {
            throw new ParameterBuilderException(e);
        }
    }
}
