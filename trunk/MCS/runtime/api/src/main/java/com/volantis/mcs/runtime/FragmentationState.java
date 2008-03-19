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
 * $Header: /src/voyager/com/volantis/mcs/runtime/FragmentationState.java,v 1.8 2003/04/29 11:42:43 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 19-Nov-01    Paul            VBM:2001110202 - Created.
 * 22-Nov-01    Paul            VBM:2001110202 - Added support for dissecting
 *                              within regions.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 13-Feb-02    Paul            VBM:2002021203 - Moved from context package.
 * 13-Feb-02    Adrian          VBM:2002021307 - Change InclusionState.
 *                              setFragmentName to set shardIndex to initial
 *                              value of zero.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 22-Feb-02    Paul            VBM:2002021802 - Added dissecting pane name
 *                              and shard index to the parameter string.
 * 05-Jun-02    Adrian          VBM:2002021103 - return 0 from getShardIndex
 *                              methods where the dissectingPaneName is not
 *                              known.  Previously returned -1 which resulted
 *                              in null output.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * 29-Apr-03    Chris W         VBM:2003040311 - InclusionState.equals() now
 *                              compares all three of its properties to
 *                              determine equality.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.runtime;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

/**
 * This class contains all the information about how to fragment a page
 * and any inclusions within regions.
 */
public class FragmentationState {

  /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(FragmentationState.class);

  /**
   * The map from inclusion paths to InclusionState instances.
   */
  private Map inclusions;

  /**
   * The index of this object within the cache.
   */
  private int cacheIndex;

  /**
   * The canonical specifier of this object.
   */
  private String canonicalSpecifier;

  /**
   * Create a new <code>FragmentationState</code>.
   */
  public FragmentationState () {
    inclusions = new HashMap ();
    cacheIndex = -1;
  }

  /**
   * Set the cache index of this object.
   * @param cacheIndex The cache index of this object.
   */
  public void setCacheIndex (int cacheIndex) {
    this.cacheIndex = cacheIndex;
  }

  /**
   * Get the cache index of this object.
   * @return The cache index of this object.
   * @throws IllegalStateException Thrown if the cache index has not been set.
   */
  public int getCacheIndex () {
    if (cacheIndex == -1) {
      throw new IllegalStateException ("cacheIndex has not been set");
    }
    return cacheIndex;
  }

  /**
   * Set the canonical specifier of this object.
   * @param canonicalSpecifier The canonical specifier of this object.
   */
  public void setCanonicalSpecifier (String canonicalSpecifier) {
    this.canonicalSpecifier = canonicalSpecifier;
  }

  /**
   * Get the canonical specifier of this object.
   * @return The canonical specifier of this object.
   * @throws IllegalStateException Thrown if the canonical specifier has not
   * been set.
   */
  public String getCanonicalSpecifier () {
    if (canonicalSpecifier == null) {
      throw new IllegalStateException ("Canonical specifier has not been set");
    }

    return canonicalSpecifier;
  }

  /**
   * Test to see whether this object is empty.
   * @return True if this object is empty and false otherwise.
   */
  public boolean isEmpty () {
    // If the map of inclusions is empty then this is empty.
    if (inclusions.isEmpty ()) {
      return true;
    }

    // We need to check to see whether any of the inclusions are not empty,
    // if any of them are not empty then the state is not empty.
    for (Iterator i = inclusions.values ().iterator (); i.hasNext ();) {
      InclusionState inclusionState = (InclusionState) i.next ();
      if (!inclusionState.isEmpty ()) {
        return false;
      }
    }

    // No non empty inclusions were found so we must be empty.
    return true;
  }

  /**
   * Remove any information about the specified inclusion.
   * @param inclusionPath The string which identifies which inclusion to
   * remove.
   */
  public void removeInclusionState (String inclusionPath) {
    inclusions.remove (inclusionPath);
  }

  /**
   * Set the name of the fragment for the specified inclusion.
   * @param inclusionPath The identity of the inclusion, this is a hierarchical
   * string of regions and indexes of inclusions within the region.
   * @param fragmentName The name of the fragment for the specified inclusion.
   */
  public void setFragmentName (String inclusionPath, String fragmentName) {

    InclusionState inclusionState
      = (InclusionState) inclusions.get (inclusionPath);
    if (inclusionState == null) {
      inclusionState = new InclusionState ();
      inclusions.put (inclusionPath, inclusionState);
    }

    inclusionState.setFragmentName (fragmentName);
  }

  /**
   * Get the name of the current fragment for the specified Inclusion and
   * specified page identifier. If the page identifier does not match the
   * identifier which is currently stored in the Inclusion then return null.
   * @param inclusionPath The full path to the Inclusion.
   * @param pageIdentifier The identifier of the page which is included in the
   * inclusion.
   * @return The fragment name, or null.
   */
  public String getFragmentName (String inclusionPath, String pageIdentifier) {

    InclusionState inclusionState
      = (InclusionState) inclusions.get (inclusionPath);
    if (inclusionState == null) {
      return null;
    }

    // If the inclusion does not contain the same page as last time then clear
    // the fragment name and set the path to the current page.
    /*
    String current = inclusionState.getPageIdentifier ();
    if (current == null) {
      inclusionState.setPageIdentifier (pageIdentifier);
    } else if (!current.equals (pageIdentifier)) {
      inclusionState.setFragmentName (null);
      inclusionState.setPageIdentifier (pageIdentifier);

      return null;
    }
    */

    return inclusionState.getFragmentName ();
  }

  /**
   * Set the index of the shard to display in the specified pane of the current
   * fragment of the specified inclusion.
   * @param inclusionPath The identity of the inclusion, this is a hierarchical
   * string of regions and indexes of inclusions within the region.
   * @param paneName The name of the DissectingPane within the fragment.
   * @param shardIndex The index of the shard.
   */
  public void setShardIndex (String inclusionPath,
                             String paneName,
                             int shardIndex) {

    InclusionState inclusionState
      = (InclusionState) inclusions.get (inclusionPath);
    if (inclusionState == null) {
      inclusionState = new InclusionState ();
      inclusions.put (inclusionPath, inclusionState);
    }

    inclusionState.setShardIndex (paneName, shardIndex);
  }

  /**
   * Get the index of the shard to display in the specified pane in the current
   * fragment of the specified inclusion.
   * @param inclusionPath The identity of the inclusion, this is a hierarchical
   * string of regions and indexes of inclusions within the region.
   * @param paneName The name of the DissectingPane within the fragment.
   * @return The index of the shard, this defaults to 0.
   */
  public int getShardIndex (String inclusionPath,
                            String paneName) {

    InclusionState inclusionState
      = (InclusionState) inclusions.get (inclusionPath);
    if (inclusionState == null) {
      if (logger.isDebugEnabled()) {
        logger.debug("Failed to resolve inclusionPath: " + inclusionPath
                     + ". Defaulting to ShardIndex 0.");
      }
      return 0;
    }
    return inclusionState.getShardIndex (paneName);
  }

  /**
   * Copy the inclusions from the other FragmentationState specified.
   * @param other The FragmentationState object whose inclusion state needs to
   * be copied.
   */
  private void copyInclusions (FragmentationState other) {

    for (Iterator i = other.inclusions.entrySet ().iterator ();
         i.hasNext ();) {
      Map.Entry entry = (Map.Entry) i.next ();
      InclusionState state
        = ((InclusionState) entry.getValue ()).cloneState ();
      inclusions.put (entry.getKey (), state);
    }
  }

  /**
   * Discard any information about inclusions and fragments which are nested
   * inside the specified inclusion.
   * @param inclusionPath The path to the inclusion.
   */
  public void discardNestedState (String inclusionPath) {
    String prefix = inclusionPath + ".";
    for (Iterator i = inclusions.keySet ().iterator (); i.hasNext ();) {
      String key = (String) i.next ();
      if (key != null && key.startsWith (prefix)) {
        i.remove ();
      }
    }
  }

  /**
   * Create a deep clone of this object.
   * @return The new <code>FragmentationState</code> object.
   */
  public FragmentationState cloneState () {
    FragmentationState other = new FragmentationState ();
    other.copyInclusions (this);
    return other;
  }

  /**
   * This class encapsulates the fragmentation state associated with a
   * particular inclusion.
   */
  public static class InclusionState {

    /**
     * The identifier of the page which is rendered in this inclusion.
     */
    //private String pageIdentifier;

    /**
     * The fragment to render in the inclusion.
     */
    private String fragmentName;

    /**
     * The name of the dissecting pane.
     */
    private String dissectingPaneName;

    /**
     * The index of the shard.
     */
    private int shardIndex;

    /**
     * Create a new <code>InclusionState</code>.
     */
    public InclusionState () {
    }

    /**
     * Create a new <code>InclusionState</code> which is a copy of the
     * specified <code>InclusionState</code> object.
     * @param other The <code>InclusionState</code> to copy.
     */
    private InclusionState (InclusionState other) {
      fragmentName = other.fragmentName;
      //pageIdentifier = other.pageIdentifier;
      dissectingPaneName = other.dissectingPaneName;
      shardIndex = other.shardIndex;
    }

    /**
     * Test to see whether this object is empty.
     * @return True if this object is empty and false otherwise.
     */
    public boolean isEmpty () {
      return (fragmentName == null
              && dissectingPaneName == null
              && shardIndex == 0);
    }

    /**
     * Set the identifier of the page which was rendered in this inclusion.
     * @param pageIdentifier The identifier of the page which was rendered in
     * this inclusion.
     */
    /*
    public void setPageIdentifier (String pageIdentifier) {
      this.pageIdentifier = pageIdentifier;
    }
    */

    /**
     * Get the identifier of the page which was rendered in this inclusion.
     * @return The identifier of the page which was rendered in this inclusion.
     */
    /*
    public String getPageIdentifier () {
      return pageIdentifier;
    }
    */

    /**
     * Set the name of the current fragment within this inclusion.
     * @param fragmentName The name of the current fragment in this inclusion.
     */
    public void setFragmentName (String fragmentName) {
      // If the new fragment name is different to the existing one then clear
      // the dissecting pane and shard index, otherwise if they are the same
      // then do nothing.
      if (FragmentationState.equals (this.fragmentName, fragmentName)) {
        return;
      }

      this.fragmentName = fragmentName;
      this.dissectingPaneName = null;
      this.shardIndex = 0;
    }

    /**
     * Get the name of the current fragment within this inclusion.
     * @return The name of the current fragment within this inclusion.
     */
    public String getFragmentName () {
      return fragmentName;
    }

    public void setShardIndex (String dissectingPaneName,
                               int shardIndex) {
      this.dissectingPaneName = dissectingPaneName;
      this.shardIndex = shardIndex;
    }

    /**
     * Get the index of the current shard within the dissection.
     * @param dissectingPaneName The name of the dissecting pane currently
     * being processed.
     * @return The index of the current shard.
     */
    public int getShardIndex (String dissectingPaneName) {
      if (FragmentationState.equals (this.dissectingPaneName,
                                     dissectingPaneName)) {
        return shardIndex;
      } else {
        if (logger.isDebugEnabled()) {
          logger.debug("failed to match " + this.dissectingPaneName + " with "
                       + dissectingPaneName + ". Defaulting to ShardIndex 0.");
        }
      }
      return 0;
    }

    /**
     * Create a clone of this object.
     * @return The new <code>InclusionState</code>.
     */
    public InclusionState cloneState () {
      return new InclusionState (this);
    }

    // Javadoc inherited from super class. 
    public boolean equals (Object other) { 
      if (!(other instanceof InclusionState)) { 
        return false; 
      } 
 
      InclusionState o = (InclusionState) other;
      // The tests are ordered so that those fields which are easiest to test
      // and are most likely to be different are tested before other fields
      // which are harder to test, or more likely to match. This can slightly
      // improve performance at no real cost. 
      return shardIndex == o.shardIndex 
        && FragmentationState.equals (dissectingPaneName, o.dissectingPaneName) 
        && FragmentationState.equals (fragmentName, o.fragmentName); 
    } 

    // Javadoc inherited from super class.
    public String toString () {
      return "InclusionInfo"
        + "@" + Integer.toHexString (System.identityHashCode (this))
        + " [" + paramString () + "]";
    }

    // Javadoc inherited from super class.
    public int hashCode () {
      throw new UnsupportedOperationException ();
    }

    /**
     * Return a String representation of the state of the object.
     * @return The String representation of the state of the object.
     */
    protected String paramString () {
      return fragmentName + ", " + dissectingPaneName + ", " + shardIndex;
    }
  }

  // Javadoc inherited from super class.
  public int hashCode () {
    throw new UnsupportedOperationException ();
  }

  // Javadoc inherited from super class.
  public boolean equals (Object other) {
    if (!(other instanceof FragmentationState)) {
      return false;
    }

    FragmentationState o = (FragmentationState) other;
    return equals (inclusions, o.inclusions);
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
    return inclusions.toString ();
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

  /**
   * This interface defines the method needed by objects which can change
   * the FragmentationState.
   */
  public static interface Change {

    /**
     * Apply the change to the specified fragmentation state.
     * @param state The <code>FragmentationState</code> object which needs
     * changing.
     */
    public void apply (FragmentationState state);
  }

  /**
   * This class encapsulates a change to the fragment for a particular
   * inclusion.
   */
  public static class FragmentChange
    implements Change {

    /**
     * The path which identifies the inclusion to change.
     */
    private String inclusionPath;

    /**
     * The name of the fragment.
     */
    private String fragmentName;

    /**
     * Flag which controls whether the new fragment is the enclosing fragment.
     */
    private boolean toEnclosing;

    /**
     * Flag which controls whether the new fragment is the default fragment.
     */
    private boolean isDefault;

    /**
     * Create a new <code>FragmentChange</code> object.
     * @param inclusionPath The path which identifies the inclusion to change.
     * @param fragmentName The name of the fragment.
     * @param toEnclosing True if the fragment is the enclosing fragment and
     * false otherwise.
     * @param isDefault True if the fragment is the default fragment and false
     * otherwise.
     */
    public FragmentChange (String inclusionPath, String fragmentName,
                           boolean toEnclosing, boolean isDefault) {
      this.inclusionPath = inclusionPath;
      this.fragmentName = fragmentName;
      this.toEnclosing = toEnclosing;
      this.isDefault = isDefault;
    }

    /**
     * Applies the changes encapsulated in this object to the specified
     * <code>FragmentationState</code> object.
     * @param state The <code>FragmentationState</code> object which needs
     * changing.
     */
    public void apply (FragmentationState state) {

      if(logger.isDebugEnabled()){
          logger.debug ("Setting fragment in "
                    + inclusionPath + " to " + fragmentName);
      }

      state.setFragmentName (inclusionPath, fragmentName);
      if (toEnclosing) {
        if(logger.isDebugEnabled()){
            logger.debug ("Discarding state nested within "
                      + inclusionPath);
        }
        state.discardNestedState (inclusionPath);

        // If the enclosing fragment is the default fragment for the layout
        // then discard the information about it, this reduces the number of
        // possible fragmentation states for a particular layout which reduces
        // the memory usage.
        if (isDefault) {
          state.removeInclusionState (inclusionPath);
        }
      }
    }

    // Javadoc inherited from super class.
    public boolean equals (Object other) {
      if (!(other instanceof FragmentChange)) {
        return false;
      }

      FragmentChange o = (FragmentChange) other;
      return FragmentationState.equals (inclusionPath, o.inclusionPath)
        && FragmentationState.equals (fragmentName, o.fragmentName)
        && toEnclosing == o.toEnclosing
        && isDefault == o.isDefault;
    }

    // Javadoc inherited from super class.
    public int hashCode () {
      throw new UnsupportedOperationException ();
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
      return inclusionPath + "," + fragmentName + "," + toEnclosing;
    }
  }

  /**
   * This class encapsulates a change to the fragment for a particular
   * inclusion.
   */
  public static class ShardChange
    implements Change {

    /**
     * The path which identifies the inclusion to change.
     */
    private String inclusionPath;

    /**
     * The name of the dissecting pane within the current fragment of the
     * inclusion.
     */
    private String dissectingPaneName;

    /**
     * The change to apply to the shard index.
     */
    private int delta;

    /**
     * Create a new <code>ShardChange</code> object.
     * @param inclusionPath The path which identifies the inclusion to change.
     * @param dissectingPaneName The name of the dissecting pane.
     * @param delta The change to apply to the shard index, +1 to move to the
     * next shard and -1 to move to the previous shard.
     */
    public ShardChange (String inclusionPath, String dissectingPaneName,
                        int delta) {
      this.inclusionPath = inclusionPath;
      this.dissectingPaneName = dissectingPaneName;
      this.delta = delta;
    }

    /**
     * Applies the changes encapsulated in this object to the specified
     * <code>FragmentationState</code> object.
     * @param state The <code>FragmentationState</code> object which needs
     * changing.
     */
    public void apply (FragmentationState state) {

      int shardIndex = state.getShardIndex (inclusionPath, dissectingPaneName);
      shardIndex += delta;

      if(logger.isDebugEnabled()){
          logger.debug ("Setting shard in "
                    + inclusionPath + " to " + shardIndex);
      }

      state.setShardIndex (inclusionPath, dissectingPaneName, shardIndex);
      /*
        if (shardIndex == 0) {
        state.removeShardIndex (inclusionPath, fragmentName, paneName);
        throw new UnsupportedOperationException ();
        } else {
        state.setShardIndex (inclusionPath, dissectingPaneName, shardIndex);
        }
      */
    }

    // Javadoc inherited from super class.
    public boolean equals (Object other) {
      if (!(other instanceof ShardChange)) {
        return false;
      }

      ShardChange o = (ShardChange) other;
      return FragmentationState.equals (inclusionPath, o.inclusionPath)
        && FragmentationState.equals (dissectingPaneName, o.dissectingPaneName)
        && delta == o.delta;
    }

    // Javadoc inherited from super class.
    public int hashCode () {
      throw new UnsupportedOperationException ();
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
      return inclusionPath + "," + dissectingPaneName + "," + delta;
    }
  }
}

/*
 * Local variables:
 * c-basic-offset: 2
 * End:
 */

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
