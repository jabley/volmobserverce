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
package com.volantis.synergetics.localization;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * A {@link MessageLocalizationStrategy} that prefixes a unique ID to the
 * localized message.
 */
public class IDPrefixingMessageLocalizationStrategy
    implements MessageLocalizationStrategy {

    /**
     * Path to the properties file that contains the prefix mappings
     */
    private final String prefixMappingBundlePath;

    /**
     * Used to retrieve the non prefixed message.
     */
    private final DefaultMessageLocalizationStrategy defaultStrategy;

    /**
     * Properties file that contains the prefix mappings
     */
    private Properties mappings;

    /**
     * Initializes a <code>IDPrefixingMessageLocalizationStrategy</code>
     * instance with the given arguments.
     *
     * @param classLoader       a {@link ClassLoader} that can be used to
     *                          retrieve associated resources.
     * @param productIdentifier an identifier used to identify the Volantis
     *                          product that is requiring logging facilities.
     */
    public IDPrefixingMessageLocalizationStrategy(ClassLoader classLoader,
                                                  String productIdentifier) {
        defaultStrategy = new DefaultMessageLocalizationStrategy(classLoader,
                                                                 productIdentifier);
        prefixMappingBundlePath = "localization/" + productIdentifier +
            "/PrefixMappings.properties";
        try {
            InputStream inputStream = classLoader.getResourceAsStream(
                prefixMappingBundlePath);
            if (inputStream != null) {
                mappings = new Properties();
                mappings.load(inputStream);
            }
        } catch (IOException e) {
            // Could not load the properties file. The mappings instance being
            // null will cause the format methods to behave exactly like the
            // default strategy.
            mappings = null;
        }
    }

    // javadoc inherited
    public String formatMessage(String key, Category category) {
        return formatMessage(key, category, null);
    }

    // javadoc inherited
    public String formatMessage(String key,
                                Category category,
                                Object substitutionArg) {
        return formatMessage(key, category, new Object[]{substitutionArg});
    }

    // javadoc inherited
    public String formatMessage(String key,
                                Category category,
                                Object[] substitutionArgs) {
        // get hold of the unprefixed message
        String message = defaultStrategy.formatMessage(key,
                                                       category,
                                                       substitutionArgs);

        StringBuffer messageBuffer = new StringBuffer();
        if (mappings == null) {
            // No mapping file so we simply generate the message returned by
            // the DefaultMessageLocalizationStrategy.
            messageBuffer.append(message);
        } else if (key == null) {
            // if the key is null the default localization strategy will
            // contain a message that indicates this.
            messageBuffer.append(message);
        } else {
            // should be ok to go ahead and construct the prefix.
            String productCode = mappings.getProperty("vlm-prefix");
            String uniqueID = mappings.getProperty(key);
            if (productCode == null || uniqueID == null) {
                messageBuffer.append(
                    "COULD NOT CONSTRUCT PREFIX ID FOR KEY '")
                    .append(key)
                    .append("': ");
            } else {
                // append the special prefix
                messageBuffer.append(productCode)
                    .append(uniqueID)
                    .append(category.getIdentifier())
                    .append(": ");
            }

            // append the actual message
            messageBuffer.append(message);
        }
        return messageBuffer.toString();
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Jul-05	502/1	philws	VBM:2005072217 Remove irritating 'can't find the prefix file' message from each logged message when no PrefixMappings file is available

 20-Dec-04	354/2	doug	VBM:2004120202 Fixed a number of minor logging issues

 29-Nov-04	343/7	doug	VBM:2004111702 Refactored logging framework

 29-Nov-04	343/5	doug	VBM:2004111702 Refactored Logging framework

 25-Nov-04	343/3	doug	VBM:2004111702 Refactored Logging framework

 18-Nov-04	343/1	doug	VBM:2004111702 Refactoring of logging framework

 ===========================================================================
*/
