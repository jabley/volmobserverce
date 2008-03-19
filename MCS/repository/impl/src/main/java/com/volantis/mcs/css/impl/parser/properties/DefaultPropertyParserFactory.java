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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.css.impl.parser.properties;

import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.properties.StylePropertyDefinitions;
import com.volantis.synergetics.UndeclaredThrowableException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * Propery parser factory that is driven by a properties file.
 *
 * <p>Every entry in the properties file defines a parser. If the key starts
 * with "shorthand." then it is a shorthand that depends on other properties
 * and so must be initialise after those properties.</p>
 */
public class DefaultPropertyParserFactory
        implements PropertyParserFactory {

    /**
     * A map from property name (including shorthands) to property parser.
     */
    private Map propertyNameToParser;
    private Map propertyNameToClass;

    /**
     * Initialise.
     *
     * @param resource The path to the properties file resource.
     */
    public DefaultPropertyParserFactory(final String resource) {

        propertyNameToParser = new HashMap();
        propertyNameToClass = new HashMap();
        InputStream in = DefaultPropertyParserFactory.class.getResourceAsStream(
                resource);
        try {
            Properties properties = new Properties();
            properties.load(in);

            // Iterate over all the properties separating them into
            // shorthands and other properties. The former can be identified
            // because the property name starts with shorthand.
            Map shorthands = new HashMap();
            Map special = new HashMap();
            for (Iterator iterator = properties.entrySet().iterator();
                 iterator.hasNext();) {

                Map.Entry entry = (Map.Entry) iterator.next();
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();

                if (key.startsWith("shorthand.")) {
                    key = key.substring(10);
                    shorthands.put(key, value);
                } else {
                    special.put(key, value);
                }
                propertyNameToClass.put(key,value);
            }

            // Iterate over all the properties adding in parsers for all apart
            // from the shorthands.
            PropertyParser parser;
            StylePropertyDefinitions definitions =
                    StylePropertyDetails.getDefinitions();
            for (Iterator i = definitions.stylePropertyIterator(); i.hasNext();) {
                StyleProperty property = (StyleProperty) i.next();
                String name = property.getName();
                if (special.containsKey(name)) {
                    parser = instantiateParser((String) special.get(name));
                    propertyNameToParser.put(name, parser);
                    propertyNameToClass.remove(name);
                } else if (!shorthands.containsKey(name)) {
                    parser = new ParameterizedPropertyParser(property);
                    propertyNameToParser.put(name, parser);
                    propertyNameToClass.remove(name);
                }
            }

            // Load all the shorthand parsers.
            for (Iterator i = shorthands.entrySet().iterator();
                 i.hasNext();) {

                Map.Entry entry = (Map.Entry) i.next();
                String name = (String) entry.getKey();
                String className = (String) entry.getValue();

                parser = instantiateParser(className);
                propertyNameToParser.put(name, parser);
                propertyNameToClass.remove(name);
            }

            if( !propertyNameToClass.isEmpty()) {
                Iterator i = propertyNameToClass.values().iterator();
                final String className = (String)i.next();
                throw new IllegalStateException("Parser class not instantiated: " + className);
            }


        } catch (IOException e) {
            throw new UndeclaredThrowableException(e,
                                                   "Could not load parser properties");
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                throw new UndeclaredThrowableException(e,
                                                       "Could not close parser properties stream");
            }
        }
    }

    // Javadoc inherited.
    public PropertyParser getPropertyParser(String propertyName) {
	String lowerName = propertyName.toLowerCase();
        PropertyParser parser = (PropertyParser)propertyNameToParser.get(lowerName);
        if( parser == null ) {
            String className = (String)propertyNameToClass.get(lowerName);
            if( className != null ) {
                parser = instantiateParser(className);
                propertyNameToParser.put(lowerName, parser);
                propertyNameToClass.remove(lowerName);
            }
        }
        return parser;
    }

    // Javadoc inherited.
    public ShorthandValueHandler getShorthandValueHandler(String propertyName) {
        return (ShorthandValueHandler) getPropertyParser(propertyName);
    }

    // Javadoc inherited.
    public ShorthandValueHandler getShorthandValueHandler(
            StyleProperty property) {
        return getShorthandValueHandler(property.getName());
    }

    // Javadoc inherited.
    public ValueConverter getValueConverter(StyleProperty property) {
        return (ValueConverter)propertyNameToParser.get(property.getName());
    }

    /**
     * The parameter types for the property parser constructor that takes a
     * parser factory implementation.
     */
    private static Class[] ctorParameterTypes = new Class[]{
        PropertyParserFactory.class
    };

    /**
     * Instantiate the parser.
     *
     * <p>Property parsers must either have a public constructor with no
     * arguments, or one which takes a {@link PropertyParserFactory}
     * reference.</p>
     *
     * @param className The name of the class within the
     * <code>com.volantis.mcs.css.impl.parser.properties</code> package.</p>
     *
     * @return The instance of the parser.
     */
    private PropertyParser instantiateParser(String className) {
        String fullName = "com.volantis.mcs.css.impl.parser.properties." + className;
        ClassLoader classLoader = DefaultPropertyParserFactory.class.getClassLoader();
        try {
            Class clazz = classLoader.loadClass(fullName);
            Constructor ctor = findConstructor(clazz, ctorParameterTypes);
            Object[] args;
            if (ctor == null) {
                ctor = findConstructor(clazz, null);
                if (ctor == null) {
                    throw new IllegalStateException(
                            "Could not find appropriate constructor");
                } else {
                    args = null;
                }
            } else {
                args = new Object[]{this};
            }

            return (PropertyParser) ctor.newInstance(args);
        } catch (ClassNotFoundException e) {
            throw new UndeclaredThrowableException(e);
        } catch (IllegalAccessException e) {
            throw new UndeclaredThrowableException(e);
        } catch (InstantiationException e) {
            throw new UndeclaredThrowableException(e);
        } catch (InvocationTargetException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    /**
     * Method to find the constructor for a class.
     *
     * @param clazz The class to search.
     * @param parameterTypes The types of the parameters to the constructor.
     *
     * @return The constructor, or null if it could not be found.
     */
    private static Constructor findConstructor(
            Class clazz, final Class[] parameterTypes) {

        try {
            return clazz.getConstructor(parameterTypes);
        } catch (NoSuchMethodException e) {
        }

        return null;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10347/1	pduffin	VBM:2005111405 Massive changes for performance

 28-Sep-05	9487/3	pduffin	VBM:2005091203 Updated JavaDoc for CSS parser and refactored

 27-Sep-05	9487/1	pduffin	VBM:2005091203 Committing new CSS Parser

 ===========================================================================
*/
