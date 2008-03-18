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
 * $Header: /src/voyager/com/volantis/mcs/utilities/ObjectHelper.java,v 1.9 2003/04/03 18:22:05 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 28-Nov-01    Paul            VBM:2001112202 - Created.
 * 06-Feb-02    Paul            VBM:2001122103 - Added compareClass method.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from 
 *                              class.
 * 22-Mar-02    Allan           VBM:2002030615 - Added getSimpleClassName()
 *                              and getProperties()- moved from AccessorHelper.
 * 12-Jun-02    Allan           VBM:2002030615 - Added getPropertyNames().
 * 18-Feb-03    Allan           VBM:2003021803 - Implement an equals() for 
 *                              Object []. 
 * 04-Mar-03    Allan           VBM:2003021802 - Added a hashCode() convenience
 *                              method that returns the hashcode for an array 
 *                              of Objects. 
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * 03-Apr-03    Allan           VBM:2003040303 - Added invokeWriteMethod(). 
 * 03-Jun-03    Allan           VBM:2003060301 - This class moved to 
 *                              Synergetics along with 
 *                              UndeclaredThrowableException.
 * ----------------------------------------------------------------------------
 */

package com.volantis.synergetics;

import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

/**
 * Implement some methods which are generally useful in the context of
 * Objects.
 */
public class ObjectHelper {

    /**
     * Volantis copyright object.
     */
    private static String mark = "(c) Volantis Systems Ltd 2001.";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(ObjectHelper.class);

    /**
     * The Comparator which defines an ordering on PropertyDescriptors.
     */
    private static Comparator propertyDescriptorComparator;

    static {
        propertyDescriptorComparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                PropertyDescriptor p1 = (PropertyDescriptor) o1;
                PropertyDescriptor p2 = (PropertyDescriptor) o2;
                return p1.getName().compareTo(p2.getName());
            }
        };
    }

    /**
     * Return the properties (those fields that have public reader operations
     * i.e get methods) as a set of name/values pairs within an Hashtable. This
     * method is of use for accessors who need to represent an object in terms
     * of its properties where these properties have the same name in the
     * repository as in the object.
     *
     * @param bean Object to query for properties
     * @return Hashtable of property names and their corresponding values
     *
     * @throws java.lang.IllegalAccessException
     *          if there is a problem with introspection of the object
     * @throws java.lang.reflect.InvocationTargetException
     *          if there is a problem with introspection of the object
     */
    public static Hashtable getProperties(Object bean)
        throws IllegalAccessException, InvocationTargetException {

        return getProperties(bean, getPropertyDescriptors(bean.getClass()));
    }

    /**
     * Return the names of properties (those fields that have public reader
     * operations i.e get methods) as a Collection.
     *
     * @param bean Object to query for properties.
     * @return Collection of property names.
     *
     * @throws java.lang.IllegalAccessException
     *          if there is a problem with introspection of the object
     * @throws java.lang.reflect.InvocationTargetException
     *          if there is a problem with introspection of the object
     */
    public static Collection getPropertyNames(Object bean)
        throws IllegalAccessException, InvocationTargetException {

        return getPropertyNames(bean,
                                getPropertyDescriptors(bean.getClass()));
    }

    /**
     * Return the properties (those fields that have public reader operations
     * i.e get methods) as a set of name/values pairs within an Hashtable. This
     * method is of use for accessors who need to represent an object in terms
     * of its properties where these properties have the same name in the
     * repository as in the object.
     *
     * @param bean Object to query for properties
     * @return Hashtable of property names and their corresponding values
     *
     * @throws java.lang.IllegalAccessException
     *          if there is a problem with introspection of the object
     * @throws java.lang.reflect.InvocationTargetException
     *          if there is a problem with introspection of the object
     */
    public static Hashtable getProperties(Object bean,
                                          PropertyDescriptor[] descriptors)
        throws IllegalAccessException, InvocationTargetException {

        Hashtable properties = new Hashtable(descriptors.length);

        for (int i = 0; i < descriptors.length; i++) {
            String name = descriptors[i].getName();
            Method get = descriptors[i].getReadMethod();
            Object value = get.invoke(bean, null);
            if (value != null) {
                properties.put(name, value);
            }
        }

        return properties;
    }

    /**
     * Return the names of properties (those fields that have public reader
     * operations i.e get methods) as a Collection.
     *
     * @param bean        Object to query for properties.
     * @param descriptors The PropertyDescriptors for the bean.
     * @return Collection of property names.
     *
     * @throws java.lang.IllegalAccessException
     *          if there is a problem with introspection of the object
     * @throws java.lang.reflect.InvocationTargetException
     *          if there is a problem with introspection of the object
     */
    public static Collection getPropertyNames(Object bean,
                                              PropertyDescriptor[] descriptors)
        throws IllegalAccessException, InvocationTargetException {

        ArrayList properties = new ArrayList(descriptors.length);

        for (int i = 0; i < descriptors.length; i++) {
            String name = descriptors[i].getName();
            properties.add(name);
        }

        return properties;
    }

    /**
     * Create a new array of PropertyDescriptors by removing some from the
     * specified array.
     *
     * @param descriptors The array of PropertyDescriptors to filter.
     * @param set         The set which contains the names of those properties
     *                    whose descriptors should be either included or
     *                    excluded from the returned array.
     * @param exclude     If true then those properties named in the set are
     *                    excluded from the returned array, otherwise only
     *                    those properties named in the set are included in the
     *                    returned array.
     * @return A new array of PropertyDescriptors.
     */
    protected static
    PropertyDescriptor[] filterPropertyDescriptors(
        PropertyDescriptor[] descriptors,
        Set set,
        boolean exclude) {

        // Calculate the size of the filtered array of property descriptors.
        int count = 0;
        for (int i = 0; i < descriptors.length; i += 1) {
            PropertyDescriptor descriptor = descriptors[i];
            String name = descriptor.getName();

            // Add the property to the array if
            //   we are excluding properties which are in the map but the map
            //   does not contain the property name,
            // or
            //   we are including properties which are in the map and the map does
            //   container the property name.
            //
            if (exclude == set.contains(name)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Ignoring descriptor for property "
                                 + name);
                }
            } else {
                count += 1;
            }
        }

        // Populate the filtered array of property descriptors.
        PropertyDescriptor[] filtered = new PropertyDescriptor[count];
        count = 0;
        for (int i = 0; i < descriptors.length; i += 1) {
            PropertyDescriptor descriptor = descriptors[i];

            // Add the property to the array if
            //   we are excluding properties which are in the map but the map
            //   does not contain the property name,
            // or
            //   we are including properties which are in the map and the map does
            //   container the property name.
            //
            if (exclude != set.contains(descriptor.getName())) {
                filtered[count] = descriptors[i];
                count += 1;
            }
        }

        return filtered;
    }

    /**
     * Create a new array of PropertyDescriptors from the specified array by
     * excluding all those properties named in the Set.
     *
     * @param descriptors The array of PropertyDescriptors to filter.
     * @param set         The set which contains the names of those properties
     *                    to be excluded from the returned array.
     * @return A new array of PropertyDescriptors.
     */
    public static
    PropertyDescriptor[] excludePropertyDescriptors(
        PropertyDescriptor[] descriptors,
        Set set) {

        return filterPropertyDescriptors(descriptors, set, true);
    }

    /**
     * Create a new array of PropertyDescriptors from the specified array by
     * only including all those properties named in the Set.
     *
     * @param descriptors The array of PropertyDescriptors to filter.
     * @param set         The set which contains the names of those properties
     *                    to be included in the returned array.
     */
    public static
    PropertyDescriptor[] includePropertyDescriptors(
        PropertyDescriptor[] descriptors,
        Set set) {

        return filterPropertyDescriptors(descriptors, set, false);
    }

    /**
     * Get an array of property descriptors for the specified class.
     *
     * @param beanClass The class whose property descriptors are needed.
     * @return A new array of PropertyDescriptors.
     */
    public static
    PropertyDescriptor[] getPropertyDescriptors(Class beanClass) {
        return getPropertyDescriptors(beanClass, null);
    }

    /**
     * Get an array of property descriptors for the specified class and ignore
     * those properties specified in the Set.
     *
     * @param beanClass The class whose property descriptors are needed.
     * @param ignore    The Set of properties to ignore. If it is null then no
     *                  properties are ignored.
     * @return A new array of PropertyDescriptors.
     */
    public static PropertyDescriptor[] getPropertyDescriptors(Class beanClass,
                                                              Set ignore) {

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(beanClass,
                                                         Object.class);
            PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
            Arrays.sort(descriptors, propertyDescriptorComparator);

            // Always have to filter out the identity property.
            if (ignore == null) {
                ignore = new HashSet();
            } else {
                ignore = new HashSet(ignore);
            }
            ignore.add("identity");
            /*
              if (ignore == null) {
              return descriptors;
              }
            */

            return excludePropertyDescriptors(descriptors, ignore);
        } catch (IntrospectionException ie) {
            logger.error("unexpected-introspection-exception", ie);
            throw new RuntimeException("Internal error");
        }
    }

    /**
     * Invoke the read method in the descriptor on the bean and return the
     * result.
     *
     * This method catches any of the introspection exceptions and if the
     * exception is a subclass of RuntimeException, or Error then they are
     * rethrown, otherwise an UndeclaredThrowableException which wraps the
     * exception is thrown. It does this because the introspection should not
     * fail because it is being done on our own classes and if it does fail
     * then there is a bug in the code which needs fixing.
     *
     * @param descriptor The PropertyDescriptor whose read method is to be
     *                   invoked.
     * @param bean       The object on which the method will be invoked.
     * @return The value read from the bean.
     *
     * @throws com.volantis.synergetics.UndeclaredThrowableException
     *          If there was a problem invoking the read method.
     */
    public static Object invokeReadMethod(PropertyDescriptor descriptor,
                                          Object bean)
        throws UndeclaredThrowableException {

        try {
            Method reader = descriptor.getReadMethod();
            return reader.invoke(bean, null);
        } catch (IllegalAccessException iae) {
            logger.error("unexpected-illegal-access-exception", iae);
            throw new UndeclaredThrowableException(iae);
        } catch (IllegalArgumentException iae) {
            logger.error("unexpected-illegal-argument-exception", iae);
            throw new UndeclaredThrowableException(iae);
        } catch (InvocationTargetException ite) {
            Throwable t = ite.getTargetException();
            logger.error("unexpected-invocation-target-exception", t);
            if (t instanceof Error) {
                throw (Error) t;
            } else if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new UndeclaredThrowableException(t);
            }
        }
    }

    /**
     * Invoke the write method in the descriptor on the bean and return the
     * result.
     *
     * @param descriptor The PropertyDescriptor whose read method is to be
     *                   invoked.
     * @param bean       The object on which the method will be invoked.
     * @param value      The value to assign.
     * @throws com.volantis.synergetics.UndeclaredThrowableException
     *          If there was a problem invoking the read method.
     */
    public static void invokeWriteMethod(PropertyDescriptor descriptor,
                                         Object bean,
                                         Object value)
        throws IllegalAccessException,
        InvocationTargetException {

        Method writer = descriptor.getWriteMethod();
        Object values [] = {value};
        writer.invoke(bean, values);
    }

    /**
     * Return the name of the class of an object without including the name of
     * the package or if the class is an array or the ';' at the end.
     *
     * @param o the Object whose simple class name to get.
     * @return the simple class name of the given object.
     */
    public static String getSimpleClassName(Object o) {
        Class cls;

        if (o instanceof Class) {
            cls = (Class) o;
        } else {
            cls = o.getClass();
        }

        String name = cls.getName();
        String simpleName = name.substring(name.lastIndexOf('.') + 1);
        simpleName.replace(';', ' ').trim();

        return simpleName;
    }

    /**
     * Return a string which uniquely identifies the specified object and
     * contains the specified parameters.
     *
     * @param object      The object to identify.
     * @param paramString The parameters to encode in the string.
     * @return A string which uniquely identifies the specified object and
     *         encodes the specified parameters.
     */
    public static String identityString(Object object,
                                        String paramString) {
        if (object == null) {
            return "null";
        }

        return object.getClass().getName()
            + "@" + Integer.toHexString(System.identityHashCode(object))
            + " [" + paramString + "]";
    }

    /**
     * Compare two objects, either of which could be null, for equality.
     *
     * @param o1 The first object to compare.
     * @param o2 The second object to compare.
     * @return True if both objects are null, or they equal each other and
     *         false otherwise.
     */
    public static boolean equals(Object o1, Object o2) {
        return o1 == null ? o2 == null : o1.equals(o2);
    }

    /**
     * Compare two Object arrays, either of which could be null, for equality.
     *
     * @param o1 The first Object array to compare.
     * @param o2 The second Object array to compare.
     * @return True if both objects are null, or they equal each other and
     *         false otherwise.
     */
    public static boolean equals(Object[] o1, Object[] o2) {
        return Arrays.equals(o1, o2);
    }

    /**
     * Compare two integers for equality.
     *
     * @param i1 The first integer to compare.
     * @param i2 The second integer to compare.
     * @return True if the integers are equals.
     */
    public static boolean equals(int i1, int i2) {
        return i1 == i2;
    }

    /**
     * Get the hash code of the object which could be null.
     *
     * @param o The object whose hash code is needed.
     */
    public static int hashCode(Object o) {
        return o == null ? 0 : o.hashCode();
    }

    /**
     * Get the hash code of an Object array.
     *
     * @param o The Object [].
     */
    public static int hashCode(Object[] o) {
        // Follow the convention if o is null but provide an initial
        // value for a non-null array in case it has a length of 0 and 
        // therefore should be distinguished from a null array. We use the
        // the class of the array so that Object arrays of different classes
        // will have different hashcodes.
        int hashCode = o == null ? 0 : o.getClass().hashCode();

        if (o != null) {
            for (int i = 0; i < o.length; i++) {
                hashCode += ObjectHelper.hashCode(o[i]) + i;
            }
        }

        return hashCode;
    }

    /**
     * Get the hash code of the integer.
     *
     * @param i The integer whose hash code is needed.
     */
    public static int hashCode(int i) {
        return i;
    }

    /**
     * Compare two objects, either of which could be null, and return less than
     * 0 if the first object is 'less than' the second, 0 if they are equal and
     * greater than 0 if the first object is 'greater than' the second. <p>
     * Null compares less than anything. </p>
     *
     * @param o1 The first object to compare.
     * @param o2 The second object to compare.
     * @return A number which defines an ordering for the two objects.
     */
    public static int compare(Object o1, Object o2) {
        if (o1 == o2) {
            return 0;
        }

        if (o1 == null) {
            return -1;
        }

        if (o2 == null) {
            return +1;
        }

        return ((Comparable) o1).compareTo(o2);
    }

    /**
     * Compare two integers, and return less than 0 if the first integer is
     * 'less than' the second, 0 if they are equal and greater than 0 if the
     * first integer is 'greater than' the second.
     *
     * @param i1 The first integer to compare.
     * @param i2 The second integer to compare.
     * @return A number which defines an ordering for the two integers.
     */
    public static int compare(int i1, int i2) {
        return i1 - i2;
    }

    /**
     * Compare the two objects' classes to make sure that they are the same.
     *
     * @param o1 An object.
     * @param o2 An object.
     * @return If the classes of the two objects are the same then return 0,
     *         otherwise return < 0 or > 0 depending on how the class names
     *         should be ordered.
     */
    public static int compareClass(Object o1, Object o2) {

        int result;

        // If the objects are not the same then order by their name and hash code.
        Class c1 = o1.getClass();
        Class c2 = o2.getClass();
        if (c1 != c2) {
            if ((result = c1.getName().compareTo(c2.getName())) != 0) {
                return result;
            }

            // Compare by their hash codes which cannot be the same.
            return System.identityHashCode(c1) -
                System.identityHashCode(c2);
        }

        return 0;
    }
}

/*
 * Local variables:
 * c-basic-offset: 2
 * end:
 */

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-04	343/1	doug	VBM:2004111702 Refactored Logging framework

 ===========================================================================
*/
