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
 * $Header: /src/voyager/com/volantis/mcs/bundles/BundleUtilities.java,v 1.5 2003/03/20 15:15:29 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 12-Oct-00    Paul            Created.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 26-Feb-03    Geoff           VBM:2003022604 - Remove unnecessary classloader
 *                              parameter from getBundle() methods.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.bundles;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.MissingResourceException;

public class BundleUtilities {

    private static String mark = 
            "(c) Volantis Systems Ltd 2000. ";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(BundleUtilities.class);

    /**
     * Locate a bundle using the basename specified, using a default locale, 
     * looking up bundle classes via the Thread's context class loader.
     * 
     * @param baseName
     * @return the bundle
     */ 
    public static EnhancedBundle getBundle(String baseName) {
        return getBundle(baseName, Locale.getDefault());
    }

    /**
     * Locate a bundle using the basename specified, using specified locale, 
     * looking up bundle classes via the Thread's context class loader.
     * 
     * @param baseName
     * @return the bundle
     */ 
    public static EnhancedBundle getBundle(String baseName, Locale locale) {

        Locale defaultLocale = Locale.getDefault();
        EnhancedBundle bundle = null;
        EnhancedBundle root;

        // Search down through the hierarchy until we do not find a more
        // specific bundle.
        root = lookFor(baseName, null);
        if (root == null) {
            throw new MissingResourceException("Can't find bundle for base name "
                    + baseName + ", locale "
                    + locale, baseName, null);
        }

        // Search in the specified locale.
        bundle = lookFor(baseName, root, locale);

        // If we could not find the bundle and the specified locale and the
        // default locale are not the same then look in the default locale
        // as well..
        if (bundle == root && !locale.equals(defaultLocale)) {

            //logger.debug ("trying the alternate locale");

            // Search in the default locale.
            bundle = lookFor(baseName, root, defaultLocale);

            // Search in the default locale.
            //bundle = lookForLanguage (baseName, defaultLocale, loader);
        }

        //logger.debug ("Returning bundle " + bundle);

        return bundle;
    }

    private static EnhancedBundle lookFor(String baseName,
            EnhancedBundle parent, Locale locale) {

        String language = locale.getLanguage();
        boolean languageSet = (language.length() != 0);
        String country = locale.getCountry();
        boolean countrySet = (country.length() != 0);
        String variant = locale.getVariant();
        boolean variantSet = (variant.length() != 0);
        String name = baseName;

        EnhancedBundle bundle;

        //logger.debug ("Language '" + language
        //+ "' country '" + country
        //+ "' variant '" + variant
        //+ "'");

        // Check to see whether there is any point in looking for the language
        // specific bundle.
        if (!languageSet && !countrySet && !variantSet) {
            //logger.debug ("1 bundle " + parent);
            return parent;
        }

        // There is so look for it.
        name = name + "_" + language;
        bundle = lookFor(name, parent);
        if (bundle == null) {
            //logger.debug ("2 bundle " + parent);
            return parent;
        }

        // The bundle now becomes the parent.
        parent = bundle;

        // Check to see whether there is any point in looking for the
        // language/country specific bundle.
        if (!countrySet && !variantSet) {
            //logger.debug ("3 bundle " + parent);
            return parent;
        }

        // There is so look for it.
        name = name + "_" + country;
        bundle = lookFor(name, parent);
        if (bundle == null) {
            //logger.debug ("4 bundle " + parent);
            return parent;
        }

        // The bundle now becomes the parent.
        parent = bundle;

        // Check to see whether there is any point in looking for the
        // language/country/variant specific bundle.
        if (!variantSet) {
            //logger.debug ("5 bundle " + parent);
            return parent;
        }

        // There is so look for it.
        name = name + "_" + variant;
        bundle = lookFor(name, parent);
        if (bundle == null) {
            //logger.debug ("6 bundle " + parent);
            return parent;
        }

        //logger.debug ("7 bundle " + bundle);
        return bundle;
    }

    private static EnhancedBundle lookFor(String bundleName,
            EnhancedBundle parent) {

        String resourceName = bundleName.replace('.', '/') + ".properties";
        InputStream inputStream;
        AbstractEnhancedBundle bundle = null;

        //logger.debug ("Looking for " + resourceName);

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        inputStream = cl.getResourceAsStream(resourceName);
        if (inputStream != null) {
            try {
                bundle = new EnhancedPropertyBundle(inputStream, resourceName);
                bundle.setParent(parent);

                inputStream.close();
                if(logger.isDebugEnabled()){
                    logger.debug("Found " + resourceName);
                }
            } catch (IOException e) {
            }
        }

        return bundle;
    }

    public static Image getImage(EnhancedBundle bundle,
            String key) {
        return (Image) bundle.getObject(key, Image.class, null);
    }

    public static ImageIcon getImageIcon(EnhancedBundle bundle,
            String key) {
        return (ImageIcon) bundle.getObject(key, ImageIcon.class, null);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 10-Oct-03	1512/1	allan	VBM:2003100702 Generic policy wizard with first wizard page

 ===========================================================================
*/
