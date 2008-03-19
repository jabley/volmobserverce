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
package com.volantis.mcs.eclipse.common;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


/**
 * A class containing common resource bundle accessing methods. This
 * class contains static methods only. It cannot be instantiated.
 */
public final class EclipseCommonMessages {

    /**
     * The name of the bundle for EclipseCommonMessages
     */
    private static final String BUNDLE_NAME =
            "com.volantis.mcs.eclipse.common.EclipseCommonMessages"; //$NON-NLS-1$


    /**
     * The device repository resource prefix.
     */
    public static final String DEVICE_REPOSITORY_RESOURCE_PREFIX =
            "DeviceRepository."; //$NON-NLS-1$

    /**
     * The key for the error decorator image.
     */
    public static final String ERROR_DECORATOR_KEY =
            "EclipseCommonPlugin.error.decorator"; //$NON-NLS-1$

    /**
     * The bundle for EclipseCommonMessages.
     */
    static final ResourceBundle RESOURCE_BUNDLE =
            ResourceBundle.getBundle(BUNDLE_NAME);

    /**
     * Make the constructor private since this class is not meant to be
     * instantiated.
     */
    private EclipseCommonMessages() {
    }


    /**
     * Given the element or attribute name corresponding to a policy
     * name (e.g. imageComponent) return the icon representing this
     * policy.
     *
     * NOTE: The caller is responsible for disposing of the returned
     * Image.
     *
     * @param name The name of the element or attribute that represents the
     * policy.
     * @return The icon for the policy.
     */
    public static Image getPolicyIcon(String name) {
        return getImage("Policy." + name + ".image"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * Given the element or attribute name corresponding to a policy
     * name (e.g. imageComponent) return the localized version of the
     * name as specified by the EclipseCommonMessages properties file
     * (e.g. Image Component)
     * @param name The name of the element or attribute that represents the
     * policy.
     * @return The localized name of the policy.
     */
    public static String getLocalizedPolicyName(String name) {
        return getString("PolicyName." + name); //$NON-NLS-1$
    }

    /**
     * A more specific version of getLocalizedPolicyName() that uses
     * both an element and an attribute to create the key.
     *
     * @param element The name of the element. Cannot be null.
     * @param attribute The name of the attribute. Can be null.
     * @return The localized name of the policy identified by both the
     * element and attribute. If attribute is null this
     * method delegates to the single argument version.
     * @throws IllegalArgumentException If both element is null.
     */
    public static String getLocalizedPolicyName(String element,
                                                String attribute) {
        if (element == null) {
            throw new IllegalArgumentException("Cannot be null: element."); //$NON-NLS-1$
        }

        String localized;

        if (attribute == null) {
            localized = getLocalizedPolicyName(element);
        } else {
            localized = getString("PolicyName." + element + "." + attribute); //$NON-NLS-1$ //$NON-NLS-2$
        }

        return localized;
    }

    /**
     * Get a specified property as a String. The bundle used is the
     * EclipseCommonMessages bundle.
     * @param key The key of the property to find.
     * @return The property or null if not found.
     */
    public static String getString(String key) {
        return getString(RESOURCE_BUNDLE, key);
    }

    /**
     * Get a specified property as a String from the given bundle
     * @param key The key of the property to find.
     * @param fallback A fallback String to return if the property does not
     * exist
     * @return The property or the fallback argument if the property is
     * not found in the bundle.
     */
    public static String getString(ResourceBundle bundle,
                                   String key,
                                   String fallback) {
        String value;
        try {
            value = bundle.getString(key);
        } catch (MissingResourceException e) {
            value = fallback;
        }
        return value;
    }

    /**
     * Get a specified property as a String. The bundle used is the
     * EclipseCommonMessages bundle.
     * @param key The key of the property to find.
     * @param fallback A fallback String to return if the property does not
     * exist
     * @return The property or the fallback argument if the property is
     * not found in the bundle.
     */
    public static String getString(String key, String fallback) {
        return getString(RESOURCE_BUNDLE, key, fallback);
    }

    /**
     * Get a specified property as an Integer. The bundle used is the
     * EclipseCommonMessages bundle.
     * @param key The key of the property to find.
     * @return The property or null if not found.
     */
    public static Integer getInteger(String key) {
        return getInteger(RESOURCE_BUNDLE, key);
    }


    /**
     * Get an Image from the EclipseCommonMessages resource bundle.
     *
     * NOTE: The caller is responsible for disposing of the returned
     * Image.
     *
     * @param key The key with which to find the file name of the image.
     * @return The Image derived from the resource.
     */
    public static Image getImage(String key) {
        String fileName = getString(key);
        return getImage(EclipseCommonMessages.class, fileName);
    }

    /**
     * Get a keyed property message as an Image.
     *
     * NOTE: The caller is responsible for disposing of the returned
     * Image.
     *
     * @param cls The Class whose getResourceAsStream() method will be
     * used to load the image.
     * @param imageFile The file name of the image relative to the
     * package/directory of the given Class.
     * @return The Image derived from the imageFile.
     */
    public static Image getImage(Class cls, String imageFile) {
        Image image = null;
        InputStream is = null;
        try {
            is = cls.getResourceAsStream(imageFile);
            if (is == null) {
                throw new IllegalStateException("Could not get InputStream " +
                        "for resource \"" + imageFile + "\" using class " + 
                        cls.getName());
            }
            Display display = Display.getCurrent();
            image = new Image(display, is);
        } catch (Throwable t) {
            EclipseCommonPlugin.handleError(EclipseCommonPlugin.getDefault(), t);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    EclipseCommonPlugin.handleError(
                        EclipseCommonPlugin.getDefault(), e);
                }
            }
        }
        return image;
    }


    /**
     * Get all the keys from a resource bundle that start with the specified
     * prefix.
     * @param prefix the prefix for the keys to be returned
     * @return a List of all keys that start with the prefix. If no keys are
     * found an empty List is returned.
     * This method <b>never</b> returns <code>null</code>.
     */
    public static List getKeys(ResourceBundle bundle, String prefix) {
        List list = new ArrayList();
        Enumeration en = bundle.getKeys();
        while (en.hasMoreElements()) {
            String str = en.nextElement().toString();
            if (str.startsWith(prefix)) {
                list.add(str);
            }
        }

        return list;
    }

    /**
     * Get all the keys from a resource bundle that start with the specified
     * prefix and sort them using a comparator
     * @param prefix the prefix for the keys to be returned
     * @param comp the Comparator to use for sorting
     * @return a List of all keys that start with the prefix. If no keys are
     * found an empty List is returned.
     * This method <b>never</b> returns <code>null</code>.
     */
    public static List getSortedKeys(ResourceBundle bundle, String prefix,
                                     Comparator comp) {
        List list = getKeys(bundle, prefix);
        Collections.sort(list, comp);
        return list;
    }

    /**
     * Get a specified property as a String using a bundle and key. Exceptions
     * and Errors are logged using EclipseCommonPlugin.logError.
     * @param bundle The ResourceBundle in which to find the property.
     * @param key The key of the property to find.
     * @return The property or null if not found.
     */
    public static String getString(ResourceBundle bundle, String key) {
        String s = null;
        try {
            s = bundle.getString(key);
        } catch (Throwable t) {
            EclipseCommonPlugin.handleError(EclipseCommonPlugin.getDefault(), t);
        }

        return s;
    }

    /**
     * Get a specified property as an Integer using a bundle and key.
     * Exceptions and Errors are logged using EclipseCommonPlugin.logError.
     * @param bundle The ResourceBundle in which to find the property.
     * @param key The key of the property to find.
     * @return The property or null if not found.
     */
    public static Integer getInteger(ResourceBundle bundle, String key) {
        Integer i = null;
        try {
            String s = bundle.getString(key);
            // This is necessary to avoid exceptions when some files have been
            // translated and have spaces after a integer value. 
            i = Integer.valueOf(s.trim());
        } catch (Throwable t) {
            EclipseCommonPlugin.handleError(EclipseCommonPlugin.getDefault(), t);
        }

        return i;
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Aug-04	5264/1	allan	VBM:2004081008 Remove invalid plugin dependencies

 23-Mar-04	3389/1	byron	VBM:2004030905 NLV properties files need adding to build

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 19-Jan-04	2562/4	allan	VBM:2003112010 Fix imports and javadoc.

 18-Jan-04	2562/2	allan	VBM:2003112010 ODOMOutlinePage displaying, decorating and tooltipping.

 06-Jan-04	2323/2	doug	VBM:2003120701 Added better validation error messages

 17-Dec-03	2219/1	doug	VBM:2003121502 Added dom validation to the eclipse editors

 12-Dec-03	2123/3	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 31-Oct-03	1661/3	steve	VBM:2003102410 Moved messages to ControlsMessages and Factory should not be a singleton

 20-Oct-03    1502/7    allan    VBM:2003092202 Completed validation for PolicySelector.
 30-Oct-03	1639/1	byron	VBM:2003101602 Create a MCS Project properties page

 20-Oct-03	1502/7	allan	VBM:2003092202 Completed validation for PolicySelector.

 17-Oct-03    1502/5    allan    VBM:2003092202 Set the selected policy in the Text properly. Improved error handling.

 17-Oct-03    1502/3    allan    VBM:2003092202 Component selection dialog with filtering and error handling

 13-Oct-03    1549/1    allan    VBM:2003101302 Eclipse Common plugin

 ===========================================================================
*/
