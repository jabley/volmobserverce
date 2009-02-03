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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.osgi.cm;

import com.volantis.osgi.cm.util.CaseInsensitiveDictionary;
import org.osgi.service.cm.ConfigurationAdmin;

import java.text.MessageFormat;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Provides helper methods for validating the names used within the {@link
 * ConfigurationAdmin} service.
 */
public class ValidationHelper {

    /**
     * The regular expression for an OSGi <code>symbolic-name</code>.
     */
    private static final String SYMBOLIC_NAME_RE =
            "[0-9A-Za-z_-]+(\\.[0-9A-Za-z_-]+)*";

    /**
     * The pattern for an OSGi <code>symbolic-name</code>.
     */
    private static final Pattern SYMBOLIC_NAME_PATTERN =
            Pattern.compile(SYMBOLIC_NAME_RE);

    /**
     * Message to use when pid does not match the pattern.
     */
    private static final MessageFormat PID_MESSAGE =
            new MessageFormat("PID ''{0}'' does not match pattern ''{1}''");

    /**
     * Message to use when factory pid does not match the pattern.
     */
    private static final MessageFormat FACTORY_PID_MESSAGE =
            new MessageFormat(
                    "Factory PID ''{0}'' does not match pattern ''{1}''");

    /**
     * Message to use when property does not match the pattern.
     */
    private static final MessageFormat PROPERTY_NAME_MESSAGE =
            new MessageFormat(
                    "Configuration property ''{0}'' does not match pattern ''{1}''");

    /**
     * Check that the specified name is a <code>symbolic-name</code>.
     *
     * @param errorFormat  The message to use if an error occurs.
     * @param symbolicName The name to check.
     */
    private static void checkSymbolicName(
            MessageFormat errorFormat, String symbolicName) {

        if (symbolicName == null) {
            String errorMessage = errorFormat.format(new Object[]{
                    symbolicName, SYMBOLIC_NAME_RE},
                    new StringBuffer(),
                    null).toString();
            throw new IllegalArgumentException(errorMessage);
        }

        Matcher matcher = SYMBOLIC_NAME_PATTERN.matcher(symbolicName);
        if (!matcher.matches()) {
            String errorMessage = errorFormat.format(new Object[]{
                    symbolicName, SYMBOLIC_NAME_RE},
                    new StringBuffer(),
                    null).toString();
            throw new IllegalArgumentException(errorMessage);
        }
    }

    /**
     * Check that the pid is of the correct format.
     *
     * @param pid The pid to check.
     */
    public static void checkPID(String pid) {
        checkSymbolicName(PID_MESSAGE, pid);
    }

    /**
     * Check that the factory pid is of the correct format.
     *
     * @param factoryPid The pid to check.
     */
    public static void checkFactoryPID(String factoryPid) {
        checkSymbolicName(FACTORY_PID_MESSAGE, factoryPid);
    }

    /**
     * Check that the property name is of the correct format.
     *
     * @param propertyName The name to check.
     */
    private static void checkPropertyName(String propertyName) {
        checkSymbolicName(PROPERTY_NAME_MESSAGE, propertyName);
    }

    /**
     * Copy and check the properties from a user supplied {@link Dictionary} to
     * an internal one.
     *
     * @param from The user supplied {@link Dictionary}.
     * @param to   The internal one.
     */
    static void copyAndCheckProperties(
            Dictionary from, CaseInsensitiveDictionary to) {

        if (from instanceof CaseInsensitiveDictionary) {
            // No need to check for duplicate names that differ only by
            // case. Still need to check types of value.
            Map fromMap = (Map) from;
            for (Iterator i = fromMap.entrySet().iterator(); i.hasNext();) {
                Map.Entry entry = (Map.Entry) i.next();
                String key = (String) entry.getKey();
                checkPropertyName(key);
                Object value = entry.getValue();
                checkPropertyValue(key, value);
                if (from != to) {
                    to.put(key, value);
                }
            }

        } else {
            Enumeration enumeration = from.keys();
            while (enumeration.hasMoreElements()) {
                String key = (String) enumeration.nextElement();
                checkPropertyName(key);
                if (to.containsKey(key)) {
                    // The only reason that this can occur is if a previous
                    // key and this one only differed by case. This is an
                    // error according to the OSGi specification.
                    throw new IllegalArgumentException(
                            "Properties contains another key that differs " +
                                    "only by case from '" + key + "'");
                }

                Object value = (Object) from.get(key);
                checkPropertyValue(key, value);
                to.put(key, value);
            }
        }
    }

    /**
     * Check to make sure that the property values are supported.
     *
     * @param property The property to check.
     * @param value    The value to check.
     */
    private static void checkPropertyValue(String property, Object value) {
        if (value == null) {
            throw new IllegalArgumentException(
                    "Properties contains null value for key '" +
                            property + "'");
        }

        Class clazz = value.getClass();
        if (clazz.isArray()) {
            Class componentType = clazz.getComponentType();
            throw new IllegalArgumentException(
                    "Arrays of " + componentType + " not supported");
        } else {
            if (clazz != String.class) {
                throw new IllegalArgumentException(
                        "Properties of " + clazz + " not supported");
            }
        }
    }
}
