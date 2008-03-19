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
 * $Header: /src/voyager/com/volantis/mcs/objects/AbstractRepositoryObject.java,v 1.5 2003/03/24 16:35:26 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 24-Oct-01    Paul            VBM:2001092608 - Created.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 08-May-02    Paul            VBM:2002050305 - Added constructor which takes
 *                              an identity.
 * 17-May-02    Paul            VBM:2002050101 - Added annotations to
 *                              constrain field lengths.
 * 24-Mar-03    Steve           VBM:2003022403 - Added API doclet tags
 * 03-Jun-03    Allan           VBM:2003060301 - UndeclaredThrowableException
 *                              moved to Synergetics.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.objects;

import com.volantis.synergetics.UndeclaredThrowableException;
import com.volantis.mcs.project.Project;

import java.io.Serializable;

/**
 * This class provides functionality which is common across RepositoryObject
 * implementations.
 * <p>
 * Instances of this class cache the identity returned by createIdentity in
 * order to reduce the amount of object creation. It is the subclasses
 * responsibility to call identityChanged () when any changes are made to the
 * object which have changed its identity. This causes the cached reference
 * to the identity to be cleared which means that the next time the getIdentity
 * method is called a new RepositoryObjectIdentity is created.
 * </p>
 * <p>
 * This class also implements identityMatches method which was previously
 * implemented by each family of sub classes. It has also been deprecated in
 * favour of calling equals on the identities of each object as returned by
 * getIdentity.
 *
 * @deprecated See {@link com.volantis.mcs.policies}.
 *             This was deprecated in version 3.5.1.
 */
public abstract class AbstractRepositoryObject
  implements Cloneable,
	     RepositoryObject,
             Serializable {

  /**
   * The name of the object.
   *
   * @mariner-object-field-length 254
   */
  private String name;

  /**
   * The current identity of the object, may be null if it has not been
   * requested, or if the identity has changed since it was last created.
   */
  private RepositoryObjectIdentity identity;

    /**
     * The project used with this object.  A null value is possible if it
     * has not been set.
     */
    private Project project;


  /**
   * Create a new <code>AbstractRepositoryObject</code>.
   * @param name The name of the object.
   */
  public AbstractRepositoryObject (String name) {
    this.name = name;
  }

  /**
   * Create a new <code>AbstractRepositoryObject</code>.
   */
  public AbstractRepositoryObject () {
    this ((String) null);
  }

    /**
     * Create a new <code>AbstractRepositoryObject</code>.
     * @param identity The identity to use.
     */
    public AbstractRepositoryObject (RepositoryObjectIdentity identity) {
        this (identity.getName ());

        // Make sure that the correct identity has been passed.
        Class identityObjectClass = identity.getObjectClass ();
        Class objectClass = getClass ();
        if (identityObjectClass != objectClass) {
            throw new IllegalArgumentException("Identity object class " +
                    identityObjectClass + " does not match object class "
                    + objectClass);
        }

        this.identity = identity;
        this.project = identity.getProject();
    }

   /**
    * Set the project to which this repository object belongs.
    * @param project The project to which this repository object belongs. If
    * this is null then the object belongs to the default project.
    * @volantis-api-include-in PublicAPI
    */

    public void setProject(Project project) {
        this.project = project;
        identityChanged();
    }

   /**
    * Get the project to which this repository object belongs.
    * @return The project to which this repository object belongs. If this
    * is null then the object belongs to the default project.
    * @volantis-api-include-in PublicAPI
    */
    public Project getProject() {
        return project;
    }

  /**
   * Set the name of the object.
   * @param name The name of the object.
   * @volantis-api-include-in PublicAPI
   */
  public void setName (String name) {
    this.name = name;
    identityChanged ();
  }

  /**
   * Get the name of the object.
   * @return The name of the object.
   * @volantis-api-include-in PublicAPI
   */

  public String getName () {
    return name;
  }

  /**
   * Get the identity of the object.
   * <p>
   * The identity object is created when it is first asked for and a reference
   * to it is kept until the identity of the object has been changed at which
   * time the reference is cleared. This means that the next time that this
   * method is called a new identity object is created.
   * </p>
   * @return The identity of the object.
   * @volantis-api-include-in PublicAPI
   */
  public RepositoryObjectIdentity getIdentity () {
    if (identity == null) {
      identity = createIdentity ();
    }

    return identity;
  }

  /**
   * Clears the reference to the identity, this must be called when any of the
   * properties which form part of this object's identity have changed.
   */
  protected void identityChanged () {
    identity = null;
  }

  /**
   * Create a new <code>RepositoryObjectIdentity</code> object which
   * identifies this object.
   */
  protected abstract RepositoryObjectIdentity createIdentity ();

  // Javadoc inherited from super class.
  public Object clone () {
    try {
      return super.clone ();
    }
    catch (CloneNotSupportedException cnse) {
      throw new UndeclaredThrowableException (cnse);
    }
  }

  /**
   * Compare this object with another object to see if they both
   * represent the same object.
   *
   * @param object the object to compare with this object
   * @return True if the objects represent the same object and false otherwise.
   *
   * @deprecated Use getIdentity method to retrieve an identity object for each
   * of the objects and then compare them.
   */
  public boolean identityMatches (Object object) {

    if (object instanceof RepositoryObject) {
      RepositoryObjectIdentity i1 = getIdentity ();
      RepositoryObjectIdentity i2 = ((RepositoryObject) object).getIdentity ();
      return i1.equals (i2);
    }

    return false;
  }

    // Javadoc inherited from super class.
    public boolean equals (Object object) {
        if (object == null || object.getClass () != getClass ()) {
            return false;
        }

        AbstractRepositoryObject o = (AbstractRepositoryObject)object;

        return equals(name, o.name) &&
                (project == null ? true : project.equals(o.project));
    }

    // Javadoc inherited from super class.
    public int hashCode () {
        return (name == null? 0: name.hashCode()) + getClass().hashCode()
               + (project == null ? 0 : project.hashCode());
    }

    // JavaDoc inherited
    public int compareTo(Object object) {
        // Null and incorrect classes will case the JVM to throw
        // NullPointerExceptions or ClassCastExceptions anyway - as per the
        // JavaDoc spec. for this method.

        AbstractRepositoryObject identity = (AbstractRepositoryObject) object;

        // Order by name, then project
        int result = name.compareTo(identity.name);
        if (result != 0 || project == null) {
            return result;
        }

        int projectHash = System.identityHashCode(project);
        int identityProjectHash = System.identityHashCode(identity.project);

        return projectHash - identityProjectHash;

    }

  // Javadoc inherited from super class.
  public String toString () {
    return getClass ().getName ()
      + "@" + Integer.toHexString (System.identityHashCode (this))
      + " [" + paramString () + "]";
  }

  /**
   * Return a String representation of the state of the object.
   * @return The String representation of the state of the object.
   */
  protected String paramString () {
    return name;
  }

  /**
   * Compare two objects, either of which could be null.
   * @param o1 The first object to compare.
   * @param o2 The second object to compare.
   * @return True if both objects are null, or they equal each other and
   * false otherwise.
   */
  protected static boolean equals (Object o1, Object o2) {
    return o1 == null ? o2 == null : o1.equals (o2);
  }

  /**
   * Get the hash code of the object which could be null.
   * @param o The object whose hash code is needed.
   */
  protected static int hashCode (Object o) {
    return o == null ? 0 : o.hashCode ();
  }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 23-Oct-05	9789/1	emma	VBM:2005101113 Migrate JDBC Accessors to chunked accessors

 28-Sep-05	9445/1	gkoch	VBM:2005090603 Introduced ComponentContainers to bring components and their assets together

 11-Mar-05	7308/3	tom	VBM:2005030702 Added XHTMLSmartClient and support for image sequences

 11-Mar-05	7308/1	tom	VBM:2005030702 Added XHTMLSmartClient and support for image sequences

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 12-Feb-04	3009/1	mat	VBM:2004021210 fix up identities in Export

 30-Jan-04	2767/2	claire	VBM:2004012701 Add project

 ===========================================================================
*/
