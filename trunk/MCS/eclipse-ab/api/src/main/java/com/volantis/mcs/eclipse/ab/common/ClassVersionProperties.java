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
package com.volantis.mcs.eclipse.ab.common;

import com.volantis.mcs.eclipse.ab.ABPlugin;
import com.volantis.mcs.eclipse.common.EclipseCommonMessages;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ResourceBundle;

/**
 * Provides access to classes that are defined dynamically based on a
 * version-specific properties file.
 */
public final class ClassVersionProperties {
    private static final String BUNDLE_NAME =
            "com.volantis.mcs.eclipse.ab.common.ClassVersions";

    private static final String COMMUNITY_EDITION = "community";

    private static final String EDITION_KEY = "edition";

    private static ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    private static final Class[] EMPTY_CLASS_ARRAY = new Class[0];

    /**
     * The private constructor.
     */
    private ClassVersionProperties() {
    }

    /**
     * Get a keyed property message as a String. This method delegates
     * to EclipseCommonMessages.getString().
     * @param key The message key.
     * @return The message or null if no message is found.
     */
    public static String getString(String key) {
        return EclipseCommonMessages.getString(getResourceBundle(), key);
    }

    /**
     * Instantiate a class named in the properties file.
     *
     * @param key The key of the class to instantiate
     * @return An instance of the specified class, or null if none can be
     *         created
     */
    public static Object getInstance(String key) {
        return getInstance(key, EMPTY_CLASS_ARRAY, EMPTY_CLASS_ARRAY);
    }

    /**
     * Instantiate a class named in the properties file.
     *
     * @return An instance of the specified class, or null if none can be
     *         created
     */
    public static Object getInstance(String key, Class[] paramTypes,
                                     Object[] constructorArgs) {
        Object instance = null;
        String className = getString(key);
        try {
            Class clazz = Class.forName(className);
            Constructor constructor = clazz.getConstructor(paramTypes);
            instance = constructor.newInstance(constructorArgs);
        } catch (ClassNotFoundException cnfe) {
            // Ignore this exception - we'll return null as no instance could be
            // created.
            EclipseCommonPlugin.logError(ABPlugin.getDefault(),
                    ClassVersionProperties.class, cnfe);
        } catch (NoSuchMethodException nsme) {
            // Ignore this exception - we'll return null as no instance could be
            // created.
            EclipseCommonPlugin.logError(ABPlugin.getDefault(),
                    ClassVersionProperties.class, nsme);
        } catch (InstantiationException ie) {
            // Ignore this exception - we'll return null as no instance could be
            // created.
            EclipseCommonPlugin.logError(ABPlugin.getDefault(),
                    ClassVersionProperties.class, ie);
        } catch (IllegalAccessException iea) {
            // Ignore this exception - we'll return null as no instance could be
            // created.
            EclipseCommonPlugin.logError(ABPlugin.getDefault(),
                    ClassVersionProperties.class, iea);
        } catch (InvocationTargetException ite) {
            // Ignore this exception - we'll return null as no instance could be
            // created.
            EclipseCommonPlugin.logError(ABPlugin.getDefault(),
                    ClassVersionProperties.class, ite);
        }

        return instance;
    }

    /**
     * Return true if this is the community edition of MCS.
     *
     * @return True if this is the community edition, false otherwise
     */
    public static boolean isCommunityEdition() {
        return COMMUNITY_EDITION.equals(getString(EDITION_KEY));
    }

    /**
     * Get the ResourceBundle for class versions.
     * @return The Controls ResourceBundle.
     */
    public static ResourceBundle getResourceBundle() {
        return RESOURCE_BUNDLE;
    }
}
