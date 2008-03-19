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
 * $Header: /src/voyager/com/volantis/mcs/layouts/Layout.java,v 1.18 2003/03/31 14:56:31 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 27-Jun-01    Paul            VBM:2001062704 - Added this change history,
 *                              sorted out the copyright and added support for
 *                              Forms.
 * 23-Jul-01    Paul            VBM:2001070507 - Use new openLayout and
 *                              closeLayout methods.
 * 23-Jul-01    Paul            VBM:2001070507 - Renamed setBorder method to
 *                              setBorderWidth so that it more closely matches
 *                              its use.
 * 27-Jul-01    Paul            VBM:2001072301 - Initialise the layout
 *                              attribute in LayoutAttributes.
 * 27-Jul-01    Paul            VBM:2001072603 - Renamed getDefaultFragment
 *                              method to getDefaultFragmentName.
 * 20-Aug-01    Mat             VBM: 2001060401 - Added dynamoPortalXMLFile
 *                              with appropriate get and set methods
 * 21-Sep-01    Doug            VBM:2001090302 - Added support for LinkAssets
 *                              The src of the default segment can now be
 *                              specified via a LinkAsset therefore changed
 *                              getDefaultSegmentSrc to reflect this
 * 17-Oct-01    Paul            VBM:2001101701 - Removed unnecessary
 *                              dynamoPortalXMLFilename.
 * 24-Oct-01    Paul            VBM:2001092608 - Made Layout an instance of a
 *                              RepositoryObject.
 * 29-Oct-01    Paul            VBM:2001102901 - Layout has been renamed
 *                              DeviceLayout and all methods relating to the
 *                              runtime generation of layouts have been
 *                              moved into protocols.
 * 02-Nov-01    Paul            VBM:2001102403 - Made use of new ...Namespace
 *                              classes to simplify this class.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 14-Jan-02    Paul            VBM:2002011414 - Added annotations to indicate
 *                              which of the properties of this class
 *                              constitute its identity.
 * 14-Feb-02    Steve           VBM:2001101803 - FormFragment support
 *                              Added the form fragment namsepace and
 *                              associated method implementations.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 17-May-02    Paul            VBM:2002050101 - Changed the names of some
 *                              annotations to make them more meaningful.
 * 27-May-02    Paul            VBM:2002050301 - Added annotation to generate
 *                              a remote accessor.
 * 07-Aug-02    Paul            VBM:2002080701 - Added destinationLayout
 *                              property.
 * 25-Oct-02    Allan           VBM:2002102501 - Added call to identityChanged
 *                              in setDeviceName().
 * 30-Oct-02    Allan           VBM:2002103107 - Added a FormatRegister to
 *                              replace namespaces. Deprecated namespace
 *                              methods. Added methods retrieveFormatMap(),
 *                              addFormat(), removeFormat(),
 *                              retrieveFormat() and formatRegistered().
 * 26-Nov-02    Allan           VBM:2002110102 - FormatRegister has been
 *                              renamed to FormatScope. Removed all 
 *                              FormatNamespace methods that were deprecated
 *                              by 2002103107.
 * 02-Dec-02    Steve           VBM:2002090210 - Extends 
 *                              AbstractCacheableRepositoryObject as the 
 *                              component now holds is caching information
 * 05-Mar-03    Allan           VBM:2003021801 - Implement equals() and 
 *                              hashCode(). 
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug 
 *                              statements in if(logger.isDebugEnabled()) block
 * 26-Mar-03    Allan           VBM:2003021803 - Deprecated no-arg constructor.
 * 31-Mar-03    Allan           VBM:2003030601 - Implemented clone().
 * 03-Jun-03    Allan           VBM:2003060301 - ObjectHelper moved to 
 *                              Synergetics. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.layouts;

import com.volantis.mcs.layouts.common.LayoutType;
import com.volantis.mcs.layouts.model.LayoutModel;
import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.model.validation.Validatable;
import com.volantis.mcs.model.validation.integrity.DefinitionType;
import com.volantis.mcs.model.path.Step;
import com.volantis.synergetics.ObjectHelper;

import java.util.Collection;
import java.util.Iterator;

/**
 * The Device specific Layout.
 * <p>
 * The implementations of the namespaces are grouped together to make it
 * easier to find and maintain them. Please make sure that any other changes
 * to this class do not break that grouping.
 * </p>
 *
 * @mock.generate
 */
public abstract class Layout implements Validatable {

    /**
   * The key for accessing the default fragment name.
   */
  public static final String DEFAULT_FRAGMENT_NAME_ATTRIBUTE
    = "DefaultFragmentName";

    /**
   * The key for accessing the default segment name.
   */
  public static final String DEFAULT_SEGMENT_NAME_ATTRIBUTE
    = "DefaultSegmentName";

  /**
   * The key for accessing the Layout group name.
   */
  public static final String LAYOUT_GROUP_NAME_ATTRIBUTE
    = "LayoutGroupName";

  /**
   * The key for accessing the destination layout name.
   */
  public static final String DESTINATION_LAYOUT_ATTRIBUTE
    = "DestinationLayout";

  // -------------------------------------------------------------------------
  //   GUI only data.
  // -------------------------------------------------------------------------

  // -------------------------------------------------------------------------
  //   Common data used by both the GUI and the runtime.
  // -------------------------------------------------------------------------

  /**
   * The format that is the root of the format tree for this layout
   */
  private Format rootFormat;

  /**
   * The name of the Layout group to which this Layout belongs.
   */
  private String layoutGroupName;

  /**
   * The name of the destination layout.
   */
  private String destinationLayout;

  /**
   * The number of formats in the Layout.
   */
  private int formatCount;

  /**
   * The FormatScope for this Layout.
   */
  protected FormatScope formatScope;


  /**
   * Holds value of property defaultFragmentName.
   */
  private String defaultFragmentName;

  /**
   * Holds value of property defaultFormFragmentName.
   */
  private String defaultFormFragmentName;

  /**
   * Holds value of property defaultSegmentName
   */
  private String defaultSegmentName;

    /**
     * The presentable name for a canvas type of device layout.
     */
    public static final String CANVAS_TYPE = "Canvas";

    /**
     * The element name for a canvas type of device layout (note that
     * there is no class equivalent for this element yet hence the need
     * for a different means to acquire the element name such as this one).
     */
    public static final String CANVAS_TYPE_ELEMENT_NAME =
            LayoutSchemaType.CANVAS_LAYOUT.getName();

    /**
     * The presentable name for a montage type of device layout.
     */
    public static final String MONTAGE_TYPE = "Montage";

    /**
     * The element name for a montage type of device layout (note that
     * there is no class equivalent for this element yet hence the need
     * for a different means to acquire the element name such as this one).
     */
    public static final String MONTAGE_TYPE_ELEMENT_NAME =
            LayoutSchemaType.MONTAGE_LAYOUT.getName();

    /**
     * Indicates whether the layout is anonymous.
     */
    private boolean anonymous;

    // -------------------------------------------------------------------------
  //   Runtime only data.
  // -------------------------------------------------------------------------

  // -------------------------------------------------------------------------
  //   Common constructors
  // -------------------------------------------------------------------------

    /**
     * Override equals to test the equality of DeviceLayouts.
     *
     * WARNING: Using this method in a runtime environment could cause a
     * performance degradation.
     *
     * @param other The Object to compare.
     * @return true if o is equal to this; false otherwise.
     */
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        boolean equals = false;
        if(other != null && getClass().equals(other.getClass())) {
            Layout layout = (Layout) other;
            equals = ObjectHelper.equals(getDefaultSegmentName(),
                                         layout.getDefaultSegmentName()) &&
                    ObjectHelper.equals(getDefaultFormFragmentName(),
                                        layout.getDefaultFormFragmentName()) &&
                    ObjectHelper.equals(getDefaultFragmentName(),
                                        layout.getDefaultFragmentName()) &&
                    ObjectHelper.equals(getFormatScope(),
                                        layout.getFormatScope()) &&
                    ObjectHelper.equals(getFormatCount(),
                                        layout.getFormatCount()) &&
                    ObjectHelper.equals(getDestinationLayout(),
                                        layout.getDestinationLayout()) &&
                    ObjectHelper.equals(getLayoutGroupName(),
                                        layout.getLayoutGroupName()) &&
                    ObjectHelper.equals(getType(), layout.getType()) &&
                    ObjectHelper.equals(getRootFormat(),
                                        layout.getRootFormat()) &&
                    (isAnonymous() == layout.isAnonymous());
        }

        return equals;
    }
//
    /**
     * Override Object.hashCode() to provide a specific implematation that
     * considerers the contents of the Layout.
     *
     * WARNING: Using this method in a runtime environment could cause a
     * performance degradation.
     *
     * @return the hashCode for this Layout.
     */
    public int hashCode() {
        int hashCode = super.hashCode();
        hashCode += ObjectHelper.hashCode(getDefaultSegmentName());
        hashCode += ObjectHelper.hashCode(getDefaultFormFragmentName());
        hashCode += ObjectHelper.hashCode(getDefaultFragmentName());
        hashCode += ObjectHelper.hashCode(getFormatScope());
        hashCode += getFormatCount();
        hashCode += ObjectHelper.hashCode(getDestinationLayout());
        hashCode += ObjectHelper.hashCode(getLayoutGroupName());
        hashCode += ObjectHelper.hashCode(getType());
        hashCode += ObjectHelper.hashCode(getRootFormat());
        hashCode += isAnonymous() ? 0: 1;

        return hashCode;
    }
    
  /**
   * Initialise.
   */
  public Layout () {
  }

    /**
     * Add a Format to this Layout.
     * @param format The Format to add.
     */
    public void addFormat(Format format) {
        if(formatScope == null) {
            formatScope = new FormatScope();
        }
        formatScope.addFormat(format);
    }

    /**
     * Remove a Format belonging to this Layout.
     * @param format The Format to remove.
     * @return The Format that was removed or null if nothing was removed.
     */
    public Format removeFormat(Format format) {
        if(formatScope != null) {
            return formatScope.removeFormat(format);
        } else {
            return null;
        }
    }

    /**
     * Retrieve a named format of a specified Class from this Layout.
     * @param name The name of the Format to retrieve. This is a more
     * efficient method to retrieve the specified Format than the (String,
     * String) version.
     * @param formatType The FormatScope index of the
     * Format to retrieve.
     * @return The specified Format if it has been registered or null.
     */
    public Format retrieveFormat(String name, FormatType formatType) {
      if(formatScope==null) {
        return null;
      }
      return formatScope.retrieveFormat(name, formatType);
    }

    /**
     * Provide the FormatScope of this Layout.
     * @return The FormatScope.
     */
    public FormatScope getFormatScope() {
        return formatScope;
    }

  // -------------------------------------------------------------------------
  //   Common methods used by both the GUI and the runtime.
  // -------------------------------------------------------------------------

  // Javadoc inherited from super class.
//  protected RepositoryObjectIdentity createIdentity () {
//    return new DeviceLayoutIdentity(getProject(), getName(), getDeviceName());
//  }

  /**
   * Get the value of the type property.
   * @return The value of the type property.
   */
  public LayoutType getType () {
      return LayoutType.CANVAS;
  }

  public boolean isCanvas () {
      return true;
  }

    /**
   * Set the value of the layoutGroupName property.
   * @param layoutGroupName The new value of the layoutGroupName property.
   */
  public void setLayoutGroupName (String layoutGroupName) {
    this.layoutGroupName = layoutGroupName;
  }

  /**
   * Get the value of the layoutGroupName property.
   * @return The value of the layoutGroupName property.
   */
  public String getLayoutGroupName () {
    return layoutGroupName;
  }

  /**
   * Set the value of the destinationLayout property.
   * @param destinationLayout The new value of the destinationLayout property.
   */
  public void setDestinationLayout (String destinationLayout) {
    this.destinationLayout = destinationLayout;
  }

  /**
   * Get the value of the destinationLayout property.
   * @return The value of the destinationLayout property.
   */
  public String getDestinationLayout () {
    return destinationLayout;
  }

  /**
   * Set the value of the formatCount property.
   * @param formatCount The new value of the formatCount property.
   */
  public void setFormatCount (int formatCount) {
    this.formatCount = formatCount;
  }

  /**
   * Get the value of the formatCount property.
   * @return The value of the formatCount property.
   */
  public int getFormatCount () {
    return formatCount;
  }

  /**
   * Set the value of the rootFormat property.
   * @param rootFormat The new value of the rootFormat property.
   */
  public void setRootFormat (Format rootFormat) {
    this.rootFormat = rootFormat;
  }

  /**
   * Get the value of the rootFormat property.
   * @return The value of the rootFormat property.
   */
  public Format getRootFormat () {
    return rootFormat;
  }

  /**
   * Set the value of the defaultFragmentName property.
   * @param defaultFragmentName The new value of the defaultFragmentName
   * property.
   */
  public void setDefaultFragmentName (String defaultFragmentName) {
    this.defaultFragmentName = defaultFragmentName;
  }

  /**
   * Get the value of the defaultFragmentName property.
   * @return The value of the defaultFragmentName property.
   */
  public String getDefaultFragmentName () {
    return defaultFragmentName;
  }

  /**
   * Set the value of the defaultFormFragmentName property.
   * @param defaultFragmentName The new value of the defaultFormFragmentName
   * property.
   */
  public void setDefaultFormFragmentName (String defaultFragmentName) {
    this.defaultFormFragmentName = defaultFragmentName;
  }

  /**
   * Get the value of the defaultFormFragmentName property.
   * @return The value of the defaultFormFragmentName property.
   */
  public String getDefaultFormFragmentName () {
    return defaultFormFragmentName;
  }

  /**
   * Set the value of the defaultSegmentName property.
   * @param defaultSegmentName The new value of the defaultSegmentName
   * property.
   */
  public void setDefaultSegmentName (String defaultSegmentName) {
    this.defaultSegmentName = defaultSegmentName;
  }

  /**
   * Get the value of the defaultSegmentName property.
   * @return The value of the defaultSegmentName property.
   */
  public String getDefaultSegmentName () {
    return defaultSegmentName;
  }
    
    /**
     * Implement a deep clone.
     */ 
    public Object clone() {
        throw new UnsupportedOperationException();
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    // Javadoc inherited.
    public void validate(ValidationContext context) {
        // Validate the root format.
        if (rootFormat != null) {
            Step step = context.pushPropertyStep(LayoutModel.ROOT_FORMAT);

            // NOTE: Fragment.validate creates subscopes for all these scopes.

            Collection formatNamespaceDefinitionTypes =
                    FormatNamespaceDefinitionTypes.getTypes();

            // Create scopes for all format namespace scopes.
            for (Iterator types = formatNamespaceDefinitionTypes.iterator();
                    types.hasNext();) {
                DefinitionType type = (DefinitionType) types.next();
                context.beginDefinitionScope(type);
            }
            // Create a scope for the dissecting pane scope.
            context.beginDefinitionScope(
                    LayoutModel.DISSECTING_PANE_SCOPE_TYPE);

            // todo: validate default fragment name references a valid fragment
            // note that the default form fragment stuff appears broken/unused

            rootFormat.validate(context);

            for (Iterator types = formatNamespaceDefinitionTypes.iterator();
                    types.hasNext();) {
                DefinitionType type = (DefinitionType) types.next();
                context.endDefinitionScope(type);
            }
            context.endDefinitionScope(
                    LayoutModel.DISSECTING_PANE_SCOPE_TYPE);

            context.popStep(step);
        }
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 18-Oct-05	9789/1	emma	VBM:2005101113 Refactor JDBC Accessors to use chunked accessor

 29-Sep-05	9590/1	schaloner	VBM:2005092204 Updating layouts for JiBX. Removed interface constants antipattern

 11-Jul-05	8992/1	pduffin	VBM:2005071109 Modified layouts and formats to allow separation between runtime and design time classes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/4	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/2	tony	VBM:2004012601 Localised logging (and exceptions)

 09-Feb-04	2846/1	claire	VBM:2004011915 Adding project init to identities. Fixing assetURL rewrite

 30-Jan-04	2767/2	claire	VBM:2004012701 Add project

 28-Jan-04	2752/1	byron	VBM:2004012602 Address issues from review

 06-Jan-04	2412/1	allan	VBM:2004010407 Fixed dirty status handling when switching editor page.

 29-Dec-03	2258/2	allan	VBM:2003121725 Make Layout editor and wizard conform to new layout schema

 17-Dec-03	2213/1	allan	VBM:2003121401 Basic editor support for all policies. Some bugs remain.

 03-Nov-03	1698/5	pcameron	VBM:2003102411 Added some new PolicyValue methods and refactored

 ===========================================================================
*/
