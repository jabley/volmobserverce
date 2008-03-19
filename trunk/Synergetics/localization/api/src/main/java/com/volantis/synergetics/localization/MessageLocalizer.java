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

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides localised formatted messages. The format methods that take a {@link
 * Category} parameter should not be used directly as they may use a {@link
 * MessageLocalizationStrategy} in order to decorate the message. These methods
 * should only really be used by the {@link com.volantis.synergetics.log.LogDispatcher}
 * implementations and the {@link ExceptionLocalizer} class. Those format
 * methods that do not require a <code>Category</code> parameter may be used by
 * any client to retrieve a localized message from the associated
 * Message.properties ResourceBundle.
 */
public class MessageLocalizer {

    /**
     * We cache the ldefault localization strategies for efficeincy.
     */
    private static Map defaultStrategies = new HashMap();

    /**
     * We cache the localization strategies for efficeincy.
     */
    private static Map strategies = new HashMap();

    /**
     * The strategy that will be used to perform localization.
     */
    private MessageLocalizationStrategy messageLocalizationStrategy;

    /**
     * A Default Message Localization Strategy that simple retrieves the
     * localized message from the associated Resource bundle.
     */
    private MessageLocalizationStrategy defaultMessageLocalizationStrategy;

    /**
     * Factory method for creating a <code>MessageLocalizer</code> instance
     *
     * @param clientClass       the client class.
     * @param productIdentifier an identifier used to identify the Volantis
     * @return a MessageLocalizer instance
     */
    public static MessageLocalizer getLocalizer(Class clientClass,
                                                String productIdentifier) {
        return new MessageLocalizer(clientClass, productIdentifier);
    }

    /**
     * Constructs a new <code>MessageLocalizer</code> instance.
     *
     * @param clientClass       the client class.
     * @param productIdentifier an identifier used to identify the Volantis
     *                          product that is requiring logging facilities.
     */
    private MessageLocalizer(Class clientClass, String productIdentifier) {
        if (clientClass == null) {
            throw new IllegalArgumentException("clientClass cannot be null");
        }
        if (productIdentifier == null) {
            throw new IllegalArgumentException(
                "productIdentifier cannot be null");
        }
        // get hold of the class loader
        ClassLoader classLoader = clientClass.getClassLoader();

        synchronized (defaultStrategies) {
            // see if the default strategy for this associated product has
            // been cached
            defaultMessageLocalizationStrategy =
                (MessageLocalizationStrategy) defaultStrategies.get(
                    productIdentifier);
            if (defaultMessageLocalizationStrategy == null) {
                // The strategy was not in the cache so we will create it and
                // store it away.
                defaultMessageLocalizationStrategy =
                    new DefaultMessageLocalizationStrategy(classLoader,
                                                           productIdentifier);
                defaultStrategies.put(productIdentifier,
                                      defaultMessageLocalizationStrategy);
            }
        }

        // try to load the IDPrefixingMessageLocalizationStrategy. This class
        // may not be shipped if the this localization strategy is not needed.
        try {
            // load the class
            Class prefixingClass = Class.forName("com.volantis.synergetics.localization." +
                                                 "IDPrefixingMessageLocalizationStrategy");
            Constructor prefixingConstructor = prefixingClass.getConstructor(new Class[]{
                ClassLoader.class,
                String.class});

            synchronized (strategies) {
                // see if the strategy for the associated product has been
                // cached
                messageLocalizationStrategy =
                    (MessageLocalizationStrategy)
                    strategies.get(productIdentifier);
                if (messageLocalizationStrategy == null) {
                    // The strategy was not in the cache so we will attempt to
                    // create it and store it away.
                    messageLocalizationStrategy =
                        (MessageLocalizationStrategy)
                        prefixingConstructor.newInstance(new Object[]{
                            classLoader,
                            productIdentifier});
                    strategies.put(productIdentifier,
                                   messageLocalizationStrategy);
                }
            }
        } catch (Exception e) {
            // if any sort of error occurs fallback to the default localization
            // strategy. This will occur if the 
            // IDPrefixingMessageLocalizationStrategy is not shipped with the
            // product (ie if plain message localization is required). As this
            // is expected behaviour we do not log or print the exception.
            messageLocalizationStrategy = defaultMessageLocalizationStrategy;
        }
    }

    /**
     * Provides a localized formatted message using the given key to retrieve
     * the message from  the associated message bundle. If a {@link
     * MessageLocalizationStrategy} has been set up then this will be used when
     * to localize the message.
     *
     * @param key      the mesage key
     * @param category the message category
     * @return the localized message
     */
    public String format(Category category, String key) {
        return messageLocalizationStrategy.formatMessage(key, category);
    }

    /**
     * Provides a localized formatted message using the given key to retrieve
     * the message from  the associated message bundle. The substitutionArg
     * parameter will be substituted into the message at it's single
     * substitution point. If a {@link MessageLocalizationStrategy} has been
     * set up then this will be used when to localize the message.
     *
     * @param key             the mesage key
     * @param category        the message category
     * @param substitutionArg the single substitution argument
     * @return the localized formatted message
     */
    public String format(Category category,
                         String key,
                         Object substitutionArg) {
        return messageLocalizationStrategy.formatMessage(key,
                                                         category,
                                                         substitutionArg);
    }

    /**
     * Provides a localized formatted message using the given key to retrieve
     * the message from  the associated message bundle. The substitutionArgs
     * will be substituted into the message at it's substitution points. If a
     * {@link MessageLocalizationStrategy} has been set up then this will be
     * used when to localize the message.
     *
     * @param key              the mesage key
     * @param category         the message category
     * @param substitutionArgs the substitution arguments
     * @return the localized formatted message
     */
    public String format(Category category,
                         String key,
                         Object[] substitutionArgs) {
        return messageLocalizationStrategy.formatMessage(key,
                                                         category,
                                                         substitutionArgs);
    }

    /**
     * Provides a localized formatted message using the given key to retrieve
     * the message from  the associated message bundle. The {@link
     * DefaultMessageLocalizationStrategy} will be used even if an alternative
     * has been provided.
     *
     * @param key the mesage key
     * @return the localized message
     */
    public String format(String key) {
        return defaultMessageLocalizationStrategy.formatMessage(key, null);
    }

    /**
     * Provides a localized formatted message using the given key to retrieve
     * the message from  the associated message bundle. The substitutionArg
     * parameter will be substituted into the message at it's single
     * substitution point. The {@link DefaultMessageLocalizationStrategy} will
     * be used even if an alternative has been provided.
     *
     * @param key             the mesage key
     * @param substitutionArg the single substitution argument
     * @return the localized formatted message
     */
    public String format(String key,
                         Object substitutionArg) {
        return defaultMessageLocalizationStrategy.formatMessage(key,
                                                                null,
                                                                substitutionArg);
    }

    /**
     * Provides a localized formatted message using the given key to retrieve
     * the message from  the associated message bundle. The substitutionArgs
     * will be substituted into the message at it's substitution points. The
     * {@link DefaultMessageLocalizationStrategy} will be used even if an
     * alternative has been provided.
     *
     * @param key              the mesage key
     * @param substitutionArgs the substitution arguments
     * @return the localized formatted message
     */
    public String format(String key,
                         Object[] substitutionArgs) {
        return defaultMessageLocalizationStrategy.formatMessage(key,
                                                                null,
                                                                substitutionArgs);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Dec-04	363/1	doug	VBM:2004122204 Reduced memory overhead of new logging mechanism

 20-Dec-04	358/1	doug	VBM:2004122005 Enhancements to the MessageLocalizer interface

 29-Nov-04	343/13	doug	VBM:2004111702 Refactored logging framework

 29-Nov-04	343/11	doug	VBM:2004111702 Refactored Logging framework

 29-Nov-04	343/9	doug	VBM:2004111702 Refactored Logging framework

 29-Nov-04	343/7	doug	VBM:2004111702 Refactored Logging framework

 29-Nov-04	343/5	doug	VBM:2004111702 Refactored Logging framework

 25-Nov-04	343/3	doug	VBM:2004111702 Refactored Logging framework

 18-Nov-04	343/1	doug	VBM:2004111702 Refactoring of logging framework

 ===========================================================================
*/
