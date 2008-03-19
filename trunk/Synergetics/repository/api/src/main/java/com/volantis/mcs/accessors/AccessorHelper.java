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
 * $Header: /src/voyager/com/volantis/mcs/accessors/AccessorHelper.java,v 1.25 2003/03/20 15:15:29 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 10-Apr-01    Allan           Created.
 * 04-Jun-01    Paul            VBM:2001051103 - Changed cache key separator
 *                              to / as it is an illegal character in names.
 * 26-Jun-01    Paul            VBM:2001051103 - Replaced Vector with
 *                              Collection.
 * 06-Sep-01    Payal           VBM:2001032704 - Modified
 *                              selectBestGenericImageAsset method to select
 *                              PNG over current best image of same width. So
 *                              following rule five to select images.
 * 17-Oct-01    Paul            VBM:2001101701 - Added extra methods to help
 *                              with creating, initialising and reading the
 *                              properties of beans.
 * 24-Oct-01    Paul            VBM:2001092608 - Modified the
 *                              getPropertyDescriptor methods to filter out
 *                              the 'identity' property and added a
 *                              createNewInstance method which takes an array
 *                              of arguments.
 * 29-Oct-01    Paul            VBM:2001102901 - Device has moved from
 *                              utilities package to devices package.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 04-Jan-02    Paul            VBM:2002010403 - Removed dependency on
 *                              MarinerPageContext.
 * 11-Jan-02    Paul            VBM:2002010403 - Moved REVISION_FIELD into
 *                              JDBCAccessorHelper.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 29-May-02    Paul            VBM:2002050301 - Moved the
 *                              selectBestGenericImageAsset method to the
 *                              GenericImageAssetSelectionPolicy.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * 03-Jun-03    Allan           VBM:2003060301 - UndeclaredThrowableException 
 *                              moved to Synergetics. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.accessors;

import com.volantis.mcs.repository.RepositoryEnumeration;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.UndeclaredThrowableException;
import com.volantis.synergetics.localization.ExceptionLocalizer;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

/**
 * Contains general methods for use by Accessor classes.
 */
public class AccessorHelper {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(AccessorHelper.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(AccessorHelper.class);

  /**
   * Separator for fields that are combined to make a key for caches.
   */
  public static final String CACHE_KEY_SEPARATOR = "/";

    /**
     * An empty enumeration.
     */
    private static RepositoryEnumeration emptyEnumeration =
            new RepositoryEnumeration() {
                public boolean hasNext() {
                    return false;
                }

                public Object next()
                        throws RepositoryException {
                    throw new RepositoryException(exceptionLocalizer.format(
                            "enumeration-exhausted"));
                }

                public void close() {
                }
            };

  /**
   * The Comparator which defines an ordering on PropertyDescriptors.
   */
  private static Comparator propertyDescriptorComparator;

  static {
    propertyDescriptorComparator = new Comparator () {
        public int compare (Object o1, Object o2) {
          PropertyDescriptor p1 = (PropertyDescriptor) o1;
          PropertyDescriptor p2 = (PropertyDescriptor) o2;
          return p1.getName ().compareTo (p2.getName ());
        }
      };
  }

  /**
   * Return the name of the class of an object without including
   * the name of the package or if the class is an array or the
   * ';' at the end.
   */
  public static String getSimpleClassName(Object o) {
    Class cls;

    if(o instanceof Class) {
      cls = (Class)o;
    } else {
      cls = o.getClass();
    }

    String name = cls.getName();
    String simpleName = name.substring(name.lastIndexOf('.')+1);
    simpleName.replace(';',' ').trim();

    return simpleName;
  }

  /**
   * Return the properties (those fields that have public reader
   * operations i.e get methods) as a set of name/values pairs within an
   * Hashtable. This method is of use for accessors who need to
   * represent an object in terms of its properties where these properties
   * have the same name in the repository as in the object.
   *
   * @param bean Object to query for properties
   * @return Hashtable of property names and their corresponding
   * values
   * @throws RepositoryException if there is a problem with introspection
   * of the object
   */
  public static Hashtable getProperties(Object bean)
    throws RepositoryException {

    try {
      PropertyDescriptor[] pds = getPropertyDescriptors (bean.getClass ());
      Hashtable properties = new Hashtable(pds.length);

      for(int i=0; i<pds.length; i++) {
        String name = pds[i].getName();
        Method get = pds[i].getReadMethod();
        Object value = get.invoke(bean, null);
        if(value!=null) {
          properties.put(name, value);
        }
      }

      return properties;
    }
    catch(IllegalAccessException e) {
      logger.error("unexpected-sql-exception", e);
      throw new RepositoryException(e);
    }
    catch(InvocationTargetException e) {
      logger.error ("unexpected-sql-exception", e);
      throw new RepositoryException(e);
    }
  }

  /**
   * Return an empty enumeration, it can be shared as it has no state which
   * gets updated.
   *
   * @return An RepositoryEnumeration.
   */
  public static RepositoryEnumeration getEmptyEnumeration () {
    return emptyEnumeration;
  }

  /**
   * Create a new array of PropertyDescriptors by removing some from the
   * specified array.
   *
   * @param descriptors The array of PropertyDescriptors to filter.
   * @param set The set which contains the names of those properties whose
   * descriptors should be either included or excluded from the returned array.
   * @param exclude If true then those properties named in the set are excluded
   * from the returned array, otherwise only those properties named in the set
   * are included in the returned array.
   * @return A new array of PropertyDescriptors.
   */
  protected static
    PropertyDescriptor [] filterPropertyDescriptors (PropertyDescriptor [] descriptors,
                                                     Set set,
                                                     boolean exclude) {

    // Calculate the size of the filtered array of property descriptors.
    int count = 0;
    for (int i = 0; i < descriptors.length; i += 1) {
      PropertyDescriptor descriptor = descriptors [i];
      String name = descriptor.getName ();

      // Add the property to the array if
      //   we are excluding properties which are in the map but the map
      //   does not contain the property name,
      // or
      //   we are including properties which are in the map and the map does
      //   container the property name.
      //
      if (exclude == set.contains (name)) {
        if(logger.isDebugEnabled()){
            logger.debug ("Ignoring descriptor for property "
                      + name);
        }
      } else {
        count += 1;
      }
    }

    // Populate the filtered array of property descriptors.
    PropertyDescriptor [] filtered = new PropertyDescriptor [count];
    count = 0;
    for (int i = 0; i < descriptors.length; i += 1) {
      PropertyDescriptor descriptor = descriptors [i];

      // Add the property to the array if
      //   we are excluding properties which are in the map but the map
      //   does not contain the property name,
      // or
      //   we are including properties which are in the map and the map does
      //   container the property name.
      //
      if (exclude != set.contains (descriptor.getName ())) {
        filtered [count] = descriptors [i];
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
   * @param set The set which contains the names of those properties to be
   * excluded from the returned array.
   * @return A new array of PropertyDescriptors.
   */
  public static
    PropertyDescriptor [] excludePropertyDescriptors (PropertyDescriptor [] descriptors,
                                                      Set set) {

    return filterPropertyDescriptors (descriptors, set, true);
  }

  /**
   * Create a new array of PropertyDescriptors from the specified array by
   * only including all those properties named in the Set.
   *
   * @param descriptors The array of PropertyDescriptors to filter.
   * @param set The set which contains the names of those properties to be
   * included in the returned array.
   */
  public static
    PropertyDescriptor [] includePropertyDescriptors (PropertyDescriptor [] descriptors,
                                                      Set set) {

    return filterPropertyDescriptors (descriptors, set, false);
  }

  /**
   * Get an array of property descriptors for the specified class.
   *
   * @param beanClass The class whose property descriptors are needed.
   * @return A new array of PropertyDescriptors.
   */
  public static
    PropertyDescriptor [] getPropertyDescriptors (Class beanClass) {
    return getPropertyDescriptors (beanClass, null);
  }

  /**
   * Get an array of property descriptors for the specified class and ignore
   * those properties specified in the Set.
   *
   * @param beanClass The class whose property descriptors are needed.
   * @param ignore The Set of properties to ignore. If it is null then no
   * properties are ignored.
   * @return A new array of PropertyDescriptors.
   */
  public static PropertyDescriptor [] getPropertyDescriptors (Class beanClass,
                                                              Set ignore) {

    try {
      BeanInfo beanInfo = Introspector.getBeanInfo (beanClass,
                                                    Object.class);
      PropertyDescriptor [] descriptors = beanInfo.getPropertyDescriptors ();
      Arrays.sort (descriptors, propertyDescriptorComparator);

      // Always have to filter out the identity property.
      if (ignore == null) {
        ignore = new HashSet ();
      } else {
        ignore = new HashSet (ignore);
      }
      ignore.add ("identity");
      /*
        if (ignore == null) {
        return descriptors;
        }
      */

      return excludePropertyDescriptors (descriptors, ignore);
    }
    catch (IntrospectionException ie) {
      logger.error ("unexpected-introspection-exception", ie);
      throw new RuntimeException ("Internal error");
    }
  }

  /**
   * Invoke the read method in the descriptor on the bean and return the
   * result.
   *
   * This method catches any of the introspection exceptions and if the
   * exception is a subclass of RuntimeException, or Error then they are
   * rethrown, otherwise an UndeclaredThrowableException which wraps the
   * exception is thrown. It does this because the introspection should
   * not fail because it is being done on our own classes and if it does
   * fail then there is a bug in the code which needs fixing.
   *
   * @param descriptor The PropertyDescriptor whose read method is to be
   * invoked.
   * @param bean The object on which the method will be invoked.
   * @return The value read from the bean.
   * @throws com.volantis.synergetics.UndeclaredThrowableException If there was a problem invoking
   * the read method.
   */
  public static Object invokeReadMethod (PropertyDescriptor descriptor,
                                         Object bean)
    throws UndeclaredThrowableException {

    try {
      Method reader = descriptor.getReadMethod ();
      return reader.invoke (bean, null);
    }
    catch (IllegalAccessException iae) {
      logger.error ("unexpected-illegal-access-exception", iae);
      throw new UndeclaredThrowableException (iae);
    }
    catch (IllegalArgumentException iae) {
      logger.error ("unexpected-illegal-argument-exception", iae);
      throw new UndeclaredThrowableException (iae);
    }
    catch (InvocationTargetException ite) {
      Throwable t = ite.getTargetException ();
      logger.error ("unexpected-invocation-target-exception", t);
      if (t instanceof Error) {
        throw (Error) t;
      } else if (t instanceof RuntimeException) {
        throw (RuntimeException) t;
      } else {
        throw new UndeclaredThrowableException (t);
      }
    }
  }

  /**
   * Convert a Boolean object to some other class.
   *
   * This supports the following conversions:
   * <dl>
   * <dt>Boolean.TYPE</dt>
   * <dd>
   * This actually involves no conversion at all as it is handled by the
   * reflection code but it needs to be explicitly tested for here.
   * </dd>
   * </dl>
   *
   * @param object The Boolean object to convert.
   * @param target The class to convert the object to.
   * @return The converted object.
   * @throws UnsupportedOperationException If the conversion is not supported.
   */
  private static Object convertBooleanToObject (Boolean object, Class target)
    throws UnsupportedOperationException {

    if (target == Boolean.TYPE) {
      return object;
    } else {
      throw new UnsupportedOperationException
        ("Cannot convert from Boolean to " + target);
    }
  }

  /**
   * Convert an Integer object to some other class.
   *
   * This supports the following conversions:
   * <dl>
   * <dt>Integer.TYPE</dt>
   * <dd>
   * This actually involves no conversion at all as it is handled by the
   * reflection code but it needs to be explicitly tested for here.
   * </dd>
   * </dl>
   *
   * @param object The Integer object to convert.
   * @param target The class to convert the object to.
   * @return The converted object.
   * @throws UnsupportedOperationException If the conversion is not supported.
   */
  private static Object convertIntegerToObject (Integer object, Class target)
    throws UnsupportedOperationException {

    if (target == Integer.TYPE) {
      return object;
    } else {
      throw new UnsupportedOperationException
        ("Cannot convert from Integer to " + target);
    }
  }

  /**
   * Convert a String object to some other class.
   *
   * This supports the following conversions:
   * <dl>
   * <dt>Integer.TYPE</dt>
   * <dd>
   * This is done by calling Integer.valueOf (object).
   * </dd>
   * </dl>
   *
   * @param object The Integer object to convert.
   * @param target The class to convert the object to.
   * @return The converted object.
   * @throws UnsupportedOperationException If the conversion is not supported.
   */
  private static Object convertStringToObject (String object, Class target)
    throws UnsupportedOperationException {

    if (target == Integer.class || target == Integer.TYPE) {
      return Integer.valueOf (object);
    } else {
      throw new UnsupportedOperationException ("Cannot convert from String to "
                                               + target);
    }
  }

  /**
   * Convert an object to some other class.
   *
   * This handles some conversions not supported by the reflection code, but
   * which are needed when initialising objects from a repository which may
   * store its information using different types, such as Strings.
   *
   * Currently null is converted to null for all types.
   *
   * @param object The object to convert.
   * @param target The class to convert the object to.
   * @return The converted object.
   * @throws UnsupportedOperationException If the conversion is not supported.
   */
  private static Object convertObject (Object object, Class target)
    throws UnsupportedOperationException {

    if (object == null) {
      return null;
    } else {
      Class source = object.getClass ();
      if (source == target) {
        return object;
      } else if (source == Boolean.class) {
        return convertBooleanToObject ((Boolean) object, target);
      } else if (source == Integer.class) {
        return convertIntegerToObject ((Integer) object, target);
      } else if (source == String.class) {
        return convertStringToObject ((String) object, target);
      } else {
        throw new UnsupportedOperationException ("Cannot convert from type "
                                                 + source + " to " + target);
      }
    }
  }

  /**
   * Invoke the write method in the descriptor on the bean.
   *
   * This method catches any of the introspection exceptions and if the
   * exception is a subclass of RuntimeException, or Error then they are
   * rethrown, otherwise an UndeclaredThrowableException which wraps the
   * exception is thrown. It does this because the introspection should
   * not fail because it is being done on our own classes and if it does
   * fail then there is a bug in the code which needs fixing.
   *
   * @param descriptor The PropertyDescriptor whose write method is to be
   * invoked.
   * @param bean The object on which the method will be invoked.
   * @param args The arguments to the write method, the number of arguments
   * must match the number of parameters in the write method.
   * @throws IllegalArgumentException If the number of arguments does not
   * match the number of parameters.
   * @throws com.volantis.synergetics.UndeclaredThrowableException If there was a problem invoking
   * the read method.
   */
  public static void invokeWriteMethod (PropertyDescriptor descriptor,
                                        Object bean,
                                        Object [] args)
    throws IllegalArgumentException,
          UndeclaredThrowableException {

    try {
      Method writer = descriptor.getWriteMethod ();
      Class [] parameterTypes = writer.getParameterTypes ();

      if(logger.isDebugEnabled()){
          logger.debug ("Invoking write method for property "
                    + descriptor.getName ());
      }

      if (args.length != parameterTypes.length) {
        throw new IllegalArgumentException ("Expected " + parameterTypes.length
                                            + " arguments was given "
                                            + args.length + " arguments");
      }

      for (int a = 0; a < args.length; a += 1) {
        Object source = args [0];
        Object target = convertObject (source, parameterTypes [a]);

        if(logger.isDebugEnabled()){
            logger.debug ("Argument " + a + " is " + target
                      + (target == null
                         ? ""
                         : (" of " + target.getClass ()))
                      );
        }

        args [a] = target;
      }

      writer.invoke (bean, args);
    }
    catch (IllegalAccessException iae) {
      logger.error ("unexpected-illegal-access-exception", iae);
      throw new RuntimeException ("Internal exception");
    }
    catch (IllegalArgumentException iae) {
      logger.error ("unexpected-illegal-argument-exception", iae);
      throw new RuntimeException ("Internal exception");
    }
    catch (InvocationTargetException ite) {
      Throwable t = ite.getTargetException ();
      logger.error ("unexpected-invocation-target-exception", t);
      if (t instanceof Error) {
        throw (Error) t;
      } else if (t instanceof RuntimeException) {
        throw (RuntimeException) t;
      } else {
        throw new UndeclaredThrowableException (t);
      }
    }
  }

  /**
   * Create a new instance of a class using the specified constructor.
   *
   * This method catches any of the introspection exceptions and if the
   * exception is a subclass of RuntimeException, or Error then they are
   * rethrown, otherwise an UndeclaredThrowableException which wraps the
   * exception is thrown. It does this because the introspection should
   * not fail because it is being done on our own classes and if it does
   * fail then there is a bug in the code which needs fixing.
   *
   * @param constructor The constructor to use.
   * @param args The arguments for the constructor.
   * @return The new instance.
   * @throws com.volantis.synergetics.UndeclaredThrowableException If there was a problem instantiating
   * the class.
   */
  public static Object createNewInstance (Constructor constructor,
                                          Object [] args)
    throws UndeclaredThrowableException {

    try {
      return constructor.newInstance (args);
    }
    catch (InstantiationException ie) {
      logger.error ("unexpected-instantiation-exception", ie);
      throw new UndeclaredThrowableException (ie);
    }
    catch (IllegalAccessException iae) {
      logger.error ("unexpected-illegal-access-exception", iae);
      throw new UndeclaredThrowableException (iae);
    }
    catch (InvocationTargetException ite) {
      Throwable t = ite.getTargetException ();
      logger.error ("unexpected-invocation-target-exception", t);
      if (t instanceof Error) {
        throw (Error) t;
      } else if (t instanceof RuntimeException) {
        throw (RuntimeException) t;
      } else {
        throw new UndeclaredThrowableException (t);
      }
    }
    catch (ExceptionInInitializerError eiie) {
      Throwable t = eiie.getException ();
      if (t == null) {
        logger.error ("unexpected-exception", eiie);
        throw new RuntimeException ("Internal error");
      } else if (t instanceof Error) {
        throw (Error) t;
      } else if (t instanceof RuntimeException) {
        throw (RuntimeException) t;
      } else {
        throw new UndeclaredThrowableException (t);
      }
    }
  }

  /**
   * Create a new instance of the specified class.
   *
   * The specified class MUST have a default constructor.
   *
   * This method catches any of the introspection exceptions and if the
   * exception is a subclass of RuntimeException, or Error then they are
   * rethrown, otherwise an UndeclaredThrowableException which wraps the
   * exception is thrown. It does this because the introspection should
   * not fail because it is being done on our own classes and if it does
   * fail then there is a bug in the code which needs fixing.
   *
   * @param beanClass The class which is to be instantiated.
   * @return The new instance.
   * @throws com.volantis.synergetics.UndeclaredThrowableException If there was a problem instantiating
   * the class.
   */
  public static Object createNewInstance (Class beanClass)
    throws UndeclaredThrowableException {

    try {
      return beanClass.newInstance ();
    }
    catch (InstantiationException ie) {
      logger.error ("unexpected-instantiation-exception", ie);
      throw new UndeclaredThrowableException (ie);
    }
    catch (IllegalAccessException iae) {
      logger.error ("unexpected-illegal-access-exception", iae);
      throw new UndeclaredThrowableException (iae);
    }
    catch (ExceptionInInitializerError eiie) {
      Throwable t = eiie.getException ();
      if (t == null) {
        logger.error ("unexpected-exception", eiie);
        throw new RuntimeException ("Internal error");
      } else if (t instanceof Error) {
        throw (Error) t;
      } else if (t instanceof RuntimeException) {
        throw (RuntimeException) t;
      } else {
        throw new UndeclaredThrowableException (t);
      }
    }
  }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-Apr-05	7738/1	philws	VBM:2004102604 Port RepositoryException localization from 3.3

 19-Apr-05	7720/1	philws	VBM:2004102604 Localize RepositoryException messages

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6232/8	doug	VBM:2004111702 refactored logging framework

 29-Nov-04	6332/1	doug	VBM:2004112913 Refactored logging framework

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/5	tony	VBM:2004012601 refactored localised logging to synergetics

 18-Feb-04	2789/3	tony	VBM:2004012601 update localisation services

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
