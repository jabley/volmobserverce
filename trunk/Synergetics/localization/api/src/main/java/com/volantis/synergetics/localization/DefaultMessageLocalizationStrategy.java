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

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


/**
 * A Default {@link MessageLocalizationStrategy} that looks up the localized
 * version of a message in a ResourceBundle
 */
public class DefaultMessageLocalizationStrategy
    implements MessageLocalizationStrategy {

    /**
     * Path to the Message.properties file that this class uses.
     */
    private final String resourceBundlePath;

    /**
     * Used to retrieve the localized error messages.
     */
    private ResourceBundle resourceBundle;

    /**
     * Initializes a <code>DefaultMessageLocalizationStrategy</code> instance.
     *
     * @param classLoader       the classLoader that should be used to retrieve
     *                          the Messages resource bundle.
     * @param productIdentifier an identifier used to identify the Volantis
     *                          product that is requiring logging facilities.
     */
    public DefaultMessageLocalizationStrategy(ClassLoader classLoader,
                                              String productIdentifier) {        resourceBundlePath = "localization/" + productIdentifier + "/Message";
        try {
            resourceBundle = ResourceBundle.getBundle(resourceBundlePath,
                                                      Locale.getDefault(),
                                                      classLoader);
        } catch (MissingResourceException e) {
            // this should never happen. If it does the resource bundle will be
            // null and the format methods will log messages that
            // indicate that the resource bundle could not be found
            resourceBundle = null;
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
        String message;
        if (key == null) {
            // if key is null we construct a message with that information
            message = buildErrorMessage("CANNOT RETRIEVE LOCALIZED MESSAGE AS MESSAGE KEY " +
                                        "IS NULL",
                                        key,
                                        substitutionArgs);

        } else if (resourceBundle == null) {
            // if resource bundle is null then we want to reflect this in the
            // message
            message = buildErrorMessage("CANNOT RETRIEVE LOCALIZED MESSAGE AS RESOURCE " +
                                        "BUNDLE '" + resourceBundlePath +
                                        "' COULD NOT BE FOUND",
                                        key,
                                        substitutionArgs);
        } else {
            try {
                // retrieve the message from the bundle and format it correctly
                message = MessageFormat.format(resourceBundle.getString(key),
                                               substitutionArgs);
            } catch (MissingResourceException e) {
                // no resource in bundle for given key so construct an
                // alternative message.
                message = buildErrorMessage("CANNOT RETRIEVE LOCALIZED MESSAGE AS RESOURCE DOES " +
                                            "NOT EXIST IN BUNDLE FOR THE GIVEN KEY",
                                            key,
                                            substitutionArgs);
            }

        }
        return message;
    }

    /**
     * Appends the key and subtitution arguments to the error message.
     *
     * @param message          An error message.
     * @param key              the key. Can be null.
     * @param substitutionArgs the substitution arguments. Can be null.
     * @return the message with the key and substitution arguments appended.
     */
    private String buildErrorMessage(String message,
                                     String key,
                                     Object[] substitutionArgs) {
        StringBuffer buffer = new StringBuffer(message);
        if (key != null) {
            buffer.append(": messageKey = ").append(key);
        }
        if (substitutionArgs != null) {
            for (int i = 0; i < substitutionArgs.length; i++) {
                buffer.append(": argument = ").append(substitutionArgs[i]);
            }
        }
        return buffer.toString();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Dec-04	363/1	doug	VBM:2004122204 Reduced memory overhead of new logging mechanism

 29-Nov-04	343/5	doug	VBM:2004111702 Refactored Logging framework

 25-Nov-04	343/3	doug	VBM:2004111702 Refactored Logging framework

 18-Nov-04	343/1	doug	VBM:2004111702 Refactoring of logging framework

 ===========================================================================
*/
