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
 * $Header: /src/voyager/com/volantis/mcs/objects/AbstractRepositoryObjectIdentity.java,v 1.9 2003/03/24 16:35:26 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 24-Oct-01    Paul            VBM:2001092608 - Created.
 * 06-Feb-02    Paul            VBM:2001122103 - Removed some unnecessary white
 *                              space.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from 
 *                              class to string.
 * 03-Apr-02    Adrian          VBM:2001102414 - Added getRenamedIdentity()
 *                              to provide a generic mechanism to copy an
 *                              identity and change the name.  This allows
 *                              branded identities to be created from
 *                              identities generated from names in jsp source.
 * 17-May-02    Paul            VBM:2002050101 - Added check to make sure that
 *                              the name of an identity cannot be null.
 * 17-Mar-03    Steve           VBM:2003022403 - Added API doclet tags
 * ----------------------------------------------------------------------------
 */


package com.volantis.mcs.objects;

import java.io.Serializable;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.project.Project;

/**
 * Implement some common methods used by all object identities.
 *
 * @deprecated See {@link com.volantis.mcs.policies}.
 *             This was deprecated in version 3.5.1.
 */
public abstract class AbstractRepositoryObjectIdentity
  implements RepositoryObjectIdentity, Serializable, Cloneable {

  /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(AbstractRepositoryObjectIdentity.class);

  /**
   * The name of the object.
   */
  private String name;

    /**
     * The project used with this object.   A null value is possible if it
     * has not been set.
     */
    private Project project;

    /**
     * Create a new <code>AbstractRepositoryObjectIdentity</code>.
     * @param project The project associated with the object.
     * @param name    The name of the object.
     */
    public AbstractRepositoryObjectIdentity(Project project, String name) {
        if (name == null) {
            throw new IllegalArgumentException ("name is null");
        }else if ("".equals(name)) {
            logger.warn("asset-name-empty");
        }
        this.name = name;
        this.project = project;
    }

    /**
     * Create a new <code>AbstractRepositoryObjectIdentity</code>.
     * @param name The name of the object.
     */
    public AbstractRepositoryObjectIdentity (String name) {
        this(null, name);
    }

  // Javadoc inherited from super class.
  public String getName () {
    return name;
  }

    // Javadoc inherited from super class.
    public boolean equals(Object object) {
        if (object == null || object.getClass () != getClass ()) {
            return false;
        }

        AbstractRepositoryObjectIdentity o =
                (AbstractRepositoryObjectIdentity) object;

        return name.equals(o.name) &&
                (project == null ? true : project.equals(o.project));
    }

    // Javadoc inherited from super class.
    public int hashCode() {
        return name.hashCode() + getClass().hashCode()
               + (project == null ? 0 : project.hashCode());
    }

  // Javadoc inherited from super class.
  public String toString () {
    return getClass ().getName ()
      + "@" + Integer.toHexString (System.identityHashCode (this))
      + " [" + paramString () + "]";
  }

    // Javadoc inherited from super class.
    public int compareTo (Object object) {
        // Null and incorrect classes will case the JVM to throw
        // NullPointerExceptions or ClassCastExceptions anyway - as per the
        // JavaDoc spec. for this method.

        AbstractRepositoryObjectIdentity identity =
                (AbstractRepositoryObjectIdentity) object;

        // Order by name, then project
        int result = name.compareTo(identity.name);
        if (result != 0 || project == null) {
            return result;
        }
        int projectHash = System.identityHashCode(project);
        int identityProjectHash = System.identityHashCode(identity.project);

        return projectHash - identityProjectHash;
    }

  /**
   * Return a String representation of the state of the object.
   * @return The String representation of the state of the object.
   * @volantis-api-include-in PublicAPI
   */
  protected String paramString () {
    return project == null ? "Null project" : project.toString() + ", " + name;
  }

  /**
   * Compare two objects, either of which could be null, for equality.
   * @param o1 The first object to compare.
   * @param o2 The second object to compare.
   * @return True if both objects are null, or they equal each other and
   * false otherwise.
   */
  protected static boolean equals (Object o1, Object o2) {
    return o1 == null ? o2 == null : o1.equals (o2);
  }

  /**
   * Compare two objects, either of which could be null, and return less than
   * 0 if the first object is 'less than' the second, 0 if they are equal and
   * greater than 0 if the first object is 'greater than' the second.
   * <p>
   * Null compares less than anything.
   * </p>
   * @param o1 The first object to compare.
   * @param o2 The second object to compare.
   * @return A number which defines an ordering for the two objects.
   */
  protected static int compare (Object o1, Object o2) {
    if (o1 == o2) {
      return 0;
    }

    if (o1 == null) {
      return -1;
    }

    if (o2 == null) {
      return +1;
    }

    return ((Comparable) o1).compareTo (o2);
  }

  /**
   * Get the hash code of the object which could be null.
   * @param o The object whose hash code is needed.
   */
  protected static int hashCode (Object o) {
    return o == null ? 0 : o.hashCode ();
  }

  /**
   * Compare two integers for equality.
   * @param i1 The first integer to compare.
   * @param i2 The second integer to compare.
   * @return True if the integers are equals.
   */
  protected static boolean equals (int i1, int i2) {
    return i1 == i2;
  }

  /**
   * Compare two integers, and return less than 0 if the first integer is
   * 'less than' the second, 0 if they are equal and greater than 0 if the
   * first integer is 'greater than' the second.
   * @param i1 The first integer to compare.
   * @param i2 The second integer to compare.
   * @return A number which defines an ordering for the two integers.
   */
  protected static int compare (int i1, int i2) {
    return i1 - i2;
  }

  /**
   * Get the hash code of the integer.
   * @param i The integer whose hash code is needed.
   */
  protected static int hashCode (int i) {
    return i;
  }

    /**
     * Make a clone of this object but update the name to use a new name
     *
     * @param newName the new name for the identity name. May not be null.
     * @return a copy of the this identity but with the name attribute updated to
     *         have a new name.
     */
    public RepositoryObjectIdentity getRenamedIdentity(String newName) {
        AbstractRepositoryObjectIdentity clone;

        if (newName == null) {
            throw new IllegalArgumentException("New name for identity cannot " +
                    "be null");
        } else if ("".equals(newName)) {
            logger.warn("asset-name-empty");
        }

        try {
            clone = (AbstractRepositoryObjectIdentity) this.clone();
            clone.name = newName;
        } catch (CloneNotSupportedException e) {
            logger.warn("remnamed-identity-error");
            clone = this;
        }

        return clone;
    }




    // JavaDoc inherited
    public Project getProject() {
        return project;
    }

    /**
     * Make a clone of this object but update the project to be a different
     * project, as specified in the parameters.
     * @param newProject The new project to use
     * @return A copy of he <code>RepositoryObjectIdentity</code> with a new
     * project
     * @volantis-api-include-in PublicAPI
     */
    public RepositoryObjectIdentity getRenamedProject(Project newProject) {
        AbstractRepositoryObjectIdentity clone;
        try {
            clone = (AbstractRepositoryObjectIdentity)this.clone();
        } catch (CloneNotSupportedException cnse) {
            logger.warn("project-identity-error");
            return this;
        }

        clone.project = newProject;
        return clone;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 24-Jan-05	6760/1	tom	VBM:2005011709 Fix branding identity handling and add WebSphere portal filter errors to include output of received content

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 19-Feb-04	2789/7	tony	VBM:2004012601 refactored localised logging to synergetics

 18-Feb-04	2789/5	tony	VBM:2004012601 update localisation services

 12-Feb-04	2789/3	tony	VBM:2004012601 Localised logging (and exceptions)

 03-Feb-04	2767/5	claire	VBM:2004012701 Adding project handling code

 01-Feb-04	2821/1	mat	VBM:2004012701 Change tests and generate scripts for Projects

 30-Jan-04	2767/2	claire	VBM:2004012701 Add project

 ===========================================================================
*/
