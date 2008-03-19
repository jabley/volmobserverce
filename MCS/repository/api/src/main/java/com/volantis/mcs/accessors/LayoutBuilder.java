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
 * $Header: /src/voyager/com/volantis/mcs/accessors/LayoutBuilder.java,v 1.10 2003/03/20 15:15:29 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 15-Oct-01    Paul            VBM:2001101202 - Created.
 * 29-Oct-01    Paul            VBM:2001102901 - Layout has been renamed
 *                              DeviceLayout.
 * 05-Nov-01    Paul            VBM:2001092607 - Renamed layoutName to name.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 12-Feb-02    Steve           VBM:2001101803 - If FormFragment is popped then
 *                              ensure that the enclosing form has a default
 *                              fragment and add the fragment to the form.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 07-Aug-02    Paul            VBM:2002080701 - Retrieve destination layout
 *                              from set of layout properties.
 * 09-Dec-02    Allan           VBM:2002120615 - Replaced String version of
 *                              Format type with FormatType where possible.
 *                              Added typeStringToFormatType Map.
 * 10-Dec-02    Allan           VBM:2002121017 - Moved the mapping of
 *                              FormatType to a String into FormatTypeMapper.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.accessors;

import com.volantis.mcs.layouts.Layout;
import com.volantis.mcs.layouts.Form;
import com.volantis.mcs.layouts.FormFragment;
import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.FormatType;
import com.volantis.mcs.layouts.FormatTypeMapper;
import com.volantis.mcs.layouts.Fragment;
import com.volantis.mcs.layouts.LayoutException;
import com.volantis.mcs.layouts.LayoutFactory;
import com.volantis.mcs.layouts.SimpleAttributeContainer;
import com.volantis.mcs.layouts.FormatConstants;
import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.layouts.common.LayoutType;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.mcs.localization.LocalizationFactory;

import java.util.Iterator;

/**
 * This class knows how to build a layout from formats.
 *
 * @deprecated this was designed for use by the old accessors. it should be removed.
 */
public class LayoutBuilder {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(LayoutBuilder.class);

    /**
     * Used for localizing exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(LayoutBuilder.class);

  /**
   * The layout that is being built.
   */
  private Layout layout;

  /**
   * The factory for creating layouts and formats.
   */
  private LayoutFactory factory;

  /**
   * The root format.
   */
  private Format root;

  /**
   * The current format.
   */
  private Format currentFormat;

  /**
   * The current sub component.
   */
  private SimpleAttributeContainer currentSubComponent;

  /**
   * The index of the current format within its parent.
   */
  private int currentIndex;

  /**
   * A flag which indicates whether any of the Formats were old fashioned
   * fragments.
   */
  private boolean migratedFragments;

    /**
   * Create a new LayoutBuilder with the specified factory.
   */
  public LayoutBuilder (LayoutFactory factory) {
    this.factory = factory;
  }

  /**
   * Get the layout, this should only be called when the layout has been
   * completed.
   * @return The Layout object which was built.
   */
  public Layout getLayout () {
    layout.setRootFormat (root);
    return layout;
  }

    /**
     * Get the layout, this should only be called when the layout has been
     * completed.
     * @return The Layout object which was built.
     */
    public CanvasLayout getCanvasLayout () {
      layout.setRootFormat (root);
      return (CanvasLayout) layout;
    }

  /**
   * Create the Layout object.
   * @throws com.volantis.mcs.layouts.LayoutException If there is a problem building the layout.
   */
  public void createLayout (LayoutType type)
    throws LayoutException {

    layout = factory.createDeviceLayout (type);
  }

  /**
   * Push another format onto the tree of formats.
   * @param type The type of the format to create.
   * @param index The index of the format within its parent.
   * @throws LayoutException If there is a problem building the layout.
   */
  public void pushFormat (String type, int index)
    throws LayoutException {

    if(logger.isDebugEnabled()){
        logger.debug ("Pushing format " + currentFormat);
    }

      FormatType formatType = FormatTypeMapper.getFormatType(type);
      if(formatType==null) {
          throw new LayoutException(EXCEPTION_LOCALIZER.format(
                  "unknown-format-type", type));
      }
    currentFormat = factory.createFormat(formatType, currentFormat, index);
    currentIndex = index;

    if (root == null) {
        root = currentFormat;
      if(logger.isDebugEnabled()){
          logger.debug ("Set root to " + root);
      }
    } else {
      if(logger.isDebugEnabled()){
          logger.debug ("Pushed format " + currentFormat);
      }
    }
  }

  /**
   * Create a sub component of the current format.
   * @param type The type of sub component to create.
   * @param index The index of the sub component within the current format.
   * @throws LayoutException If there is a problem building the layout.
   */
  public void createSubComponent (String type, int index)
    throws LayoutException {

    currentSubComponent = currentFormat.createSubComponent (type, index);
    if (currentSubComponent == null) {
      throw new LayoutException(EXCEPTION_LOCALIZER.format(
              "unknown-type", type));
    }

    if(logger.isDebugEnabled()){
        logger.debug ("Created sub component " + currentSubComponent);
    }
  }

  /**
   * Finish processing the current format and continue processing its parent.
   * @throws LayoutException If there is a problem building the layout.
   */
  public Format popFormat ()
    throws LayoutException {

    if(logger.isDebugEnabled()){
        logger.debug ("Popping format " + currentFormat);

    // Allow the format to do any initialisation after its children have
    // been read.

    }    factory.formatChildrenHaveBeenCreated (currentFormat);

    // If this is a form fragment, determine if it is the default
    // fragment for the form.
    if( currentFormat instanceof FormFragment )
    {
        FormFragment fragment = (FormFragment)currentFormat;
        Form parent = fragment.getEnclosingForm();
        if(logger.isDebugEnabled()){
            logger.debug( "Processing form fragment " + fragment.getName() +
                      " Parent form is " + parent );
        }
        if( parent != null )
        {
            parent.addFormFragment( fragment );
        }
    }

      Format poppedFormat = currentFormat;
    currentFormat = currentFormat.getParent ();

    // If we had to add a Fragment wrapper around the format then make
    // sure that it is initialised properly.
    if (currentFormat instanceof Fragment && migratedFragments) {
      Fragment fragment = (Fragment) currentFormat;
      factory.formatChildrenHaveBeenCreated (fragment);
      currentFormat = fragment.getParent ();
    }


    if(logger.isDebugEnabled()){
        logger.debug ("Popped format " + currentFormat);
    }

      return poppedFormat;
  }

  /**
   * Set the attributes of the current sub component, format, or layout
   * depending on which is currently being handled.
   * @param name The attribute name.
   * @param value The attribute value.
   * @throws LayoutException If there is a problem building the layout.
   */
  public void setAttribute (String name, String value)
    throws LayoutException {

    // If there is a current sub component then work on the sub component,
    // else if there is a current format the work on that, else work on the
    // layout.
    if (currentSubComponent != null) {
      setSubComponentAttribute (name, value);
    } else if (currentFormat != null) {
      setFormatAttribute (name, value);
    } else {
      setLayoutAttribute (name, value);
    }
  }

  /**
   * This is called when all the attributes for the current object have been
   * read and allows the builder to do initialisation based on the values of
   * the attributes.
   * @throws LayoutException If there is a problem building the layout.
   */
  public void attributesRead ()
    throws LayoutException {

    // If there is a current sub component then work on the sub component,
    // else if there is a current format the work on that, else work on the
    // layout.
    if (currentSubComponent != null) {
      subComponentAttributesRead ();
    } else if (currentFormat != null) {
      formatAttributesRead ();
    } else {
      layoutAttributesRead ();
    }
  }

  /**
   * Set an attribute on the layout.
   * @param name The attribute name.
   * @param value The attribute value.
   * @throws LayoutException If there is a problem building the layout.
   */
  private void setLayoutAttribute (String name, String value)
    throws LayoutException {

    if(logger.isDebugEnabled()){
        logger.debug ("Setting layout attribute "
                  + name + " = " + value);
    }

    if (name.equals (Layout.DEFAULT_FRAGMENT_NAME_ATTRIBUTE)) {
      layout.setDefaultFragmentName (value);
    } else if (name.equals (Layout.DEFAULT_SEGMENT_NAME_ATTRIBUTE)) {
      layout.setDefaultSegmentName (value);
    } else if (name.equals (Layout.LAYOUT_GROUP_NAME_ATTRIBUTE)) {
      layout.setLayoutGroupName (value);
    } else if (name.equals (Layout.DESTINATION_LAYOUT_ATTRIBUTE)) {
      layout.setDestinationLayout (value);
    } else {
      throw new LayoutException(EXCEPTION_LOCALIZER.format(
              "invalid-attribute",new Object[]{name, value}));
    }
  }

    /**
     * Set an attribute on the format.
     * @param name The attribute name.
     * @param value The attribute value.
     * @throws LayoutException If there is a problem building the layout.
     */
    protected void setFormatAttribute(String name, String value)
            throws LayoutException {

        if(logger.isDebugEnabled()){
            logger.debug("Setting format attribute "
                     + name + " = " + value);
        }

        Object realValue = value;
        if(name.equals("type")) {
            realValue = FormatTypeMapper.getFormatType(value);
        }
        if(name.equals(FormatConstants.DEPRECATED_FRAGMENT_NAME_ATTRIBUTE)) {
            migrateFragment(value);
        } else {
            currentFormat.setAttribute(name, realValue);
        }
    }

  /**
   * Migrate the current format from the old style fragment which was done
   * by using an attribute on a format to a new style Fragment format if it
   * is necessary.
   * @param fragmentName The name of the fragment to migrate30.
   * @throws LayoutException If there is a problem building the layout.
   */
  private void migrateFragment (String fragmentName)
    throws LayoutException {

    // We need to wrap the format in a fragment if it is not already a
    // fragment, and either there is no enclosing fragment, or the
    // enclosing fragment has a different name.
    Fragment enclosingFragment;
    if (!(currentFormat instanceof Fragment) &&
        ((enclosingFragment = currentFormat.getEnclosingFragment ()) == null
         || !enclosingFragment.getName ().equals (fragmentName))) {

      if(logger.isDebugEnabled()){
          logger.debug ("Old style fragment named "
                    + fragmentName + " detected");

      // Create the Fragment wrapper, replace the current format.

      }      Fragment fragment = (Fragment) factory.createFormat
        (FormatType.FRAGMENT,
         currentFormat.getParent (),
         currentIndex);

      // If the current format was the root then the fragment wrapper should
      // now be root.
      if (root == currentFormat) {
        root = fragment;
        if(logger.isDebugEnabled()){
            logger.debug ("Changed root to " + root);
        }
      }

      // Set the fragment attributes.
      fragment.setName (fragmentName);

      // Do any initialisation which depends on the attributes.
      factory.formatAttributesHaveBeenSet (fragment);

      // Remember the attributes.
      SimpleAttributeContainer attributes = currentFormat;

      // Recreate the current format as a child of the fragment.
      currentFormat = factory.createFormat (currentFormat.getFormatType (),
                                            fragment, 0);
      currentIndex = 0;

      // Copy the attributes from the original format.
      for (Iterator iterator = attributes.attributeNames ();
           iterator.hasNext ();) {
        String name = (String) iterator.next ();
        String value = (String) attributes.getAttribute (name);
        currentFormat.setAttribute (name, value);
      }

      // Note: at this point there is a hole in the instance numbers
      // due to us discarding the format we just created. This shouldn't
      // be a problem because they are only used internally, they are
      // regenerated when the layout is written out to the repository.

      // Remember that we have migrated fragments.
      migratedFragments = true;
    }
  }

  /**
   * Set an attribute on the sub component.
   * @param name The attribute name.
   * @param value The attribute value.
   */
  private void setSubComponentAttribute (String name, String value) {

    if(logger.isDebugEnabled()){
        logger.debug ("Setting sub component attribute "
                  + name + " = " + value);
    }

    currentSubComponent.setAttribute (name, value);
  }

  /**
   * Initialise the layout based on its attributes.
   */
  private void layoutAttributesRead () {
    if(logger.isDebugEnabled()){
        logger.debug ("Layout attributes have been read");
    }
  }

  /**
   * Initialise the current format based on its attributes.
   * @throws LayoutException If there is a problem building the layout.
   */
  protected void formatAttributesRead ()
    throws LayoutException {

    if(logger.isDebugEnabled()){
        logger.debug ("Format attributes for " + currentFormat
                  + " have been read");
    }
    factory.formatAttributesHaveBeenSet (currentFormat);
  }

  /**
   * Initialise the current sub component based on its attributes.
   * @throws LayoutException If there is a problem building the layout.
   */
  private void subComponentAttributesRead ()
    throws LayoutException {

    if(logger.isDebugEnabled()){
        logger.debug ("Sub component attributes for "
                  + currentSubComponent + " have been read");
    }
    currentFormat.subComponentInitialised (currentSubComponent);
    currentSubComponent = null;
  }

    public void setAnonymous(boolean anonymous) {
        layout.setAnonymous(anonymous);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Sep-05	9590/1	schaloner	VBM:2005092204 Updating layouts for JiBX. Removed interface constants antipattern

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 24-May-05	7890/2	pduffin	VBM:2005042705 Committing extensive restructuring changes

 29-Apr-05	7946/1	pduffin	VBM:2005042821 Moved code out of repository, in preparation for some device work

 18-May-05	8279/1	matthew	VBM:2005042702 Refactor RepositoryException and its derived classes to use ExceptionLocalizers

 19-Apr-05	7738/1	philws	VBM:2004102604 Port RepositoryException localization from 3.3

 19-Apr-05	7720/1	philws	VBM:2004102604 Localize RepositoryException messages

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
