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
 * $Header: /src/voyager/com/volantis/mcs/layouts/Fragment.java,v 1.27 2003/04/17 10:21:06 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 27-Feb-01    Paul            Created.
 * 27-Jun-01    Paul            VBM:2001062704 - Sorted out the copyright
 *                              and added visit method.
 * 09-Jul-01    Paul            VBM:2001062810 - Changed the visit method to
 *                              return a boolean.
 * 27-Jul-01    Paul            VBM:2001072603 - Renamed getDefaultFragment
 *                              method to getDefaultFragmentName.
 * 23-Aug-01    Allan           VBM:2001081615 - Set the tagName on the
 *                              FragmentAttributes to "a" so that anchor
 *                              styles are picked up for fragments in
 *                              protocols that emulate style sheets - in
 *                              createFragmentAnchor(). Also removed some
 *                              obselete comments.
 * 15-Oct-01    Mat             VBM:2001100804 - Changed
 *                              createFragmentAnchor() to use the base-url
 *                              from the config file.
 * 29-Oct-01    Paul            VBM:2001102901 - Layout has been renamed
 *                              DeviceLayout and all methods relating to the
 *                              runtime generation of layouts have been
 *                              moved into protocols.
 * 21-Nov-01    Pether          VBM:2001112101 - Added the attribute
 *                              BACK_LINK_TEXT_ATTRIBUTE to support back
 *                              link text.
 * 02-Jan-02    Paul            VBM:2002010201 - Removed unnecessary import.
 * 30-Jan-02    Steve		VBM:2002011411 - Read and write
 *                              PEER_LINK_ATTRIBUTE
 * 14-Mar-02    Adrian          VBM:2002020101 - modified method getAttribute
 *                              to check if the deviceLayout is null before
 *                              calling methods on it.  Added methods...
 *                              checkUnsetDefaultFragment
 *                              checkResetDefaultFragment to manage the state
 *                              of the DeviceLayout defaultFragment.  If the
 *                              fragment is removed it remembers if it was the
 *                              default in case it is readded.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from 
 *                              class
 *                              to string.
 * 09-Dec-02    Allan           VBM:2002120615 - Replaced String version of
 *                              Format type with FormatType where possible.
 * 12-Dec-02    Allan           VBM:2002110102 -
 *                              Removed setDeviceLayout(). Modified
 *                              setAttribute() to make better use of
 *                              super.setAttribute(). Removed all references
 *                              to FormatNamespaces replacing these with
 *                              a FormatScope and addFormat(), removeFormat(),
 *                              retrieveFormat() and getFormatScope() methods.
 *                              to string.
 * 17-Jan-03    Allan           VBM:2002121021 - Modified getAttribute() to
 *                              check for a null name before trying to use it
 * 14-Mar-03    Doug            VBM:2003030409 - Added 
 *                              Format.FRAG_LINK_ORDER_ATTRIBUTE entry to the 
 *                              userAttributes array. Added the 
 *                              isParentLinkFirst() method.
 * 26-Mar-03    Allan           VBM:2003021803 - Added to do on addFormat(). 
 * 26-Mar-03    Allan           VBM:2003021803 - Added to do on addFormat().
 * 28-Mar-03    Allan           VBM:2003030603 - Made dissectingPanes and 
 *                              orphanedDefaultFragmen protected. 
 * 16-Apr-03    Geoff           VBM:2003041603 - Add FormatVisitorException to
 *                              declaration of visit() method.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.layouts;

import com.volantis.mcs.layouts.model.LayoutModel;
import com.volantis.mcs.model.path.Step;
import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.model.validation.integrity.DefinitionType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * A format that contains a Fragment of a Layout.
 *
 * @mock.generate base="Format"
 */
public class Fragment extends Format {

  private static String [] userAttributes = new String [] {
    FormatConstants.NAME_ATTRIBUTE,
    FormatConstants.PARENT_FRAGMENT_NAME_PSEUDO_ATTRIBUTE,
    FormatConstants.LINK_TEXT_ATTRIBUTE,
    FormatConstants.BACK_LINK_TEXT_ATTRIBUTE,
    FormatConstants.FRAGMENT_LINK_STYLE_CLASS_ATTRIBUTE,
    FormatConstants.PEER_LINK_ATTRIBUTE,
    FormatConstants.FRAG_LINK_ORDER_ATTRIBUTE,
    FormatConstants.DEFAULT_FRAGMENT_NAME_PSEUDO_ATTRIBUTE
  };

  private static String [] defaultAttributes = new String [] {
    FormatConstants.LINK_TEXT_ATTRIBUTE,
    FormatConstants.BACK_LINK_TEXT_ATTRIBUTE,
    FormatConstants.PEER_LINK_ATTRIBUTE
  };

  private static String [] persistentAttributes = new String [] {
    FormatConstants.NAME_ATTRIBUTE,
    FormatConstants.LINK_TEXT_ATTRIBUTE,
    FormatConstants.BACK_LINK_TEXT_ATTRIBUTE,
    FormatConstants.FRAGMENT_LINK_STYLE_CLASS_ATTRIBUTE,
    FormatConstants.PEER_LINK_ATTRIBUTE
  };

  protected Map dissectingPanes;

    /**
     * The FormatScope of this Fragment.
     */
    protected FormatScope formatScope;

  /**
   * If this is the Layout DefaultFragment we need to remember if
   * orphaned from the parent incase we get a new parent and are
   * therefore still part of the layout and thereby still the default.
   */
  protected boolean orphanedDefaultFragment = false;

  /**
   * The one and only constructor
   */
  public Fragment (CanvasLayout canvasLayout) {
    super (1, canvasLayout);
  }

    /**
   * Return the type of this Format  (Fragment)
   */
  public FormatType getFormatType () {
    return FormatType.FRAGMENT;
  }

    /**
     * Provide the FormatScope of this Fragment.
     * @return The FormatScope.
     */
    public FormatScope getFormatScope() {
        return formatScope;
    }

  /**
   * Return the attributes that can be set by the user
   */
  public String [] getUserAttributes () {
    return userAttributes;
  }

  /**
   * Return the attributes that have default values
   */
  public String [] getDefaultAttributes () {
    return defaultAttributes;
  }

  /**
   * Return the attributes that are saved.
   */
  public String [] getPersistentAttributes () {
    return persistentAttributes;
  }

    // Javadoc inherited from super class.
    public boolean visit (FormatVisitor visitor, Object object)
            throws FormatVisitorException {
    return visitor.visit (this, object);
  }

    /**
     * Check if this is the <code>Layout</code> default fragment.
     * If so, set this to be false and remember that it used to be the default
     * in case this fragment is readded later.
     * @return true if this was the default fragment
     */
    public boolean checkUnsetDefaultFragment() {
      String isDefault = (String)
	getAttribute(FormatConstants.DEFAULT_FRAGMENT_NAME_PSEUDO_ATTRIBUTE);
      if (isDefault.equals("true")) {
	setAttribute(FormatConstants.DEFAULT_FRAGMENT_NAME_PSEUDO_ATTRIBUTE,
                 "false");
	orphanedDefaultFragment = true;
	return true;
      }
      return false;
    }

    /**
     * Check if this was the <code>Layout</code> default fragment.
     * If so, reset this to be true as this fragment has been readded to the
     * Layout.
     * @return true if this was reset to be the default fragment
     */
    public boolean checkResetDefaultFragment() {
      if (orphanedDefaultFragment) {
	setAttribute(FormatConstants.DEFAULT_FRAGMENT_NAME_PSEUDO_ATTRIBUTE,
                 "true");
	orphanedDefaultFragment = false;
	return true;
      }
      return false;
    }

  /** Set the name of the Fragment as an attribute
   * @param name The name of the Fragment
   */
  public void setName (String name) {
    setAttribute(FormatConstants.NAME_ATTRIBUTE,
                 name);
  }

  /** Retrieve the name of the Fragment
   * @return The name of the Fragment
   */
  public String getName() {
    return (String) getAttribute (FormatConstants.NAME_ATTRIBUTE);
  }

  /** Getter for property PeerLinks.
   * @return boolean denoting if peer links are required
   */
  public boolean getPeerLinks() 
  {
    String value = getPeerLink();
    if( value == null ) {
      return false;
    } else {
      return value.equalsIgnoreCase( "true" );
    }
  }

    public String getPeerLink() {
        return (String)getAttribute(FormatConstants.PEER_LINK_ATTRIBUTE );
    }

    /** Setter for property peerLinks.
     * @param peerLinks the peer links.
     */
    public void setPeerLinks(String peerLinks) {
      setAttribute(FormatConstants.PEER_LINK_ATTRIBUTE,
                    peerLinks);
    }

    /**
     * Return the value of the link class attribute
     * @return the value of the link class attribute or null if one
     * was not set.
     */
    public String getLinkClass() {
        return (String)
              getAttribute(FormatConstants.FRAGMENT_LINK_STYLE_CLASS_ATTRIBUTE);
    }

    /**
     * Setter for property fragmentLinkStyleClass.
     * @param fragmentLinkStyleClass the fragment link style class.
     */
    public void setLinkClass(String fragmentLinkStyleClass) {
      setAttribute (FormatConstants.FRAGMENT_LINK_STYLE_CLASS_ATTRIBUTE,
                    fragmentLinkStyleClass);
    }

    /**
     * Does the user want the parent link generated before the peer links
     * @return true if and only if parent links should come before any 
     * peer links
     */
    public boolean isParentLinkFirst() {
        String firstLink = getFragmentLinkOrder();
        // if link is null then previous link should be generated first
        return (FormatConstants.PARENT_FIRST.equals(firstLink));
    }

    public String getFragmentLinkOrder() {
        return (String)getAttribute(FormatConstants.FRAG_LINK_ORDER_ATTRIBUTE);
    }

    /**
     * Setter for property fragLinkOrder.
     * @param parentLinkFirst true if parent links should come before any
     * peer links.
     */
    public void setParentLinkFirst(final boolean parentLinkFirst) {
        final String value = parentLinkFirst? FormatConstants.PARENT_FIRST:
                                   FormatConstants.PEERS_FIRST;
        setFragmentLinkOrder(value);
    }

    public void setFragmentLinkOrder(final String value) {
        setAttribute(FormatConstants.FRAG_LINK_ORDER_ATTRIBUTE, value);
    }

    /** Setter for property linkText.
   * @param linkText New value of property linkText.
   */
  public void setLinkText(String linkText) {
    setAttribute (FormatConstants.LINK_TEXT_ATTRIBUTE, linkText);
  }

  /** Getter for property linkText.
   * @return Value of property linkText.
   */
  public String getLinkText() {
    return (String) getAttribute (FormatConstants.LINK_TEXT_ATTRIBUTE);
  }

  /** Setter for property backLinkText.
   * @param linkText New value of property linkText.
   */
  public void setBackLinkText(String linkText) {
    setAttribute (FormatConstants.BACK_LINK_TEXT_ATTRIBUTE, linkText);
  }

  /** Getter for property backLinkText.
   * @return Value of property linkText.
   */
  public String getBackLinkText() {
    return (String) getAttribute (FormatConstants.BACK_LINK_TEXT_ATTRIBUTE);
  }

  /**
   * Setter for property defaultFragmentNamePseudoAttribute.
   * @param defaultFragmentName the default fragment name pseudo attribute.
   */
  public void setDefaultFragmentName(String defaultFragmentName) {
    setAttribute (FormatConstants.DEFAULT_FRAGMENT_NAME_PSEUDO_ATTRIBUTE,
                  defaultFragmentName);
  }

  /**
   * Getter for property defaultFragmentNamePseudoAttribute.
   * @return the default fragment name pseudo attribute.
   */
  public String getDefaultFragmentName() {
    return (String)
            getAttribute(FormatConstants.DEFAULT_FRAGMENT_NAME_PSEUDO_ATTRIBUTE);
  }

  /**
   * Setter for property parentFragmentNamePseudoAttribute.
   * @param parentFragmentName the parent fragment name pseudo attribute.
   */
  public void setParentFragmentName(String parentFragmentName) {
    setAttribute (FormatConstants.PARENT_FRAGMENT_NAME_PSEUDO_ATTRIBUTE,
                  parentFragmentName);
  }

  /**
   * Getter for property parentFragmentNamePseudoAttribute.
   * @return the parent fragment name pseudo attribute.
   */
  public String getParentFragmentName() {
    return (String)
            getAttribute(FormatConstants.PARENT_FRAGMENT_NAME_PSEUDO_ATTRIBUTE);
  }

  /**
   * todo later note that this method does not add the dissecting pane to 
   * the FormatScope it never has but this looks like a bug to me.
   */ 
  public void addDissectingPane (DissectingPane dissectingPane) {
    if (dissectingPanes == null) {
      dissectingPanes = new HashMap ();
    }
    dissectingPanes.put (dissectingPane.getName (), dissectingPane);
  }

  public DissectingPane getDissectingPane (String name) {
    if (dissectingPanes == null) {
      return null;
    }
    return (DissectingPane) dissectingPanes.get (name);
  }

  /**
   * TODO: Multiple DissectingPanes
   *
   * This should be removed when multiple dissecting panes are allowed in
   * each fragment.
   */
  public DissectingPane getDissectingPane () {
    if (dissectingPanes == null) {
      return null;
    }

    Iterator iterator = dissectingPanes.values ().iterator ();
    if (iterator.hasNext ()) {
      return (DissectingPane) iterator.next ();
    }

    return null;
  }

  public void removeDissectingPane (DissectingPane dissectingPane) {
    removeDissectingPane (dissectingPane.getName ());
  }

  public void removeDissectingPane (String name) {
    if (dissectingPanes == null) {
      return;
    }
    dissectingPanes.remove (name);
  }

  public boolean hasDissectingPane () {
    return (dissectingPanes != null && dissectingPanes.size () != 0);
  }


    /**
     * Override the SimpleAttributeContainer.setAttribute method to enable
     * special processing to be done.
     */
    public void setAttribute(String name, Object value) {
        if(name.equals(FormatConstants.DEFAULT_FRAGMENT_NAME_PSEUDO_ATTRIBUTE)) {
            String defaultFragmentName = layout.getDefaultFragmentName();
            String fragmentName = getName();

            if("true".equals(value)) {
                layout.setDefaultFragmentName(fragmentName);
            } else if(fragmentName.equals(defaultFragmentName)) {
                layout.setDefaultFragmentName(null);
            }
        } else if(name.equals(FormatConstants.PARENT_FRAGMENT_NAME_PSEUDO_ATTRIBUTE)) {
            throw new RuntimeException("Cannot set parent fragment name");
        } else {
            super.setAttribute(name, value);
        }
    }

  /**
   * Override the SimpleAttributeContainer.setAttribute method to enable
   * special processing to be done.
   */
  public Object getAttribute (String name) {
    if (name.equals (FormatConstants.DEFAULT_FRAGMENT_NAME_PSEUDO_ATTRIBUTE)) {
      Layout layout = getLayout();
      if (layout == null) {
	return "false";
      }

      String defaultFragmentName = layout.getDefaultFragmentName ();

      String fragmentName = getName ();
      if (fragmentName!=null && fragmentName.equals (defaultFragmentName)) {
        return "true";
      }
      return "false";
    }
    else if (name.equals (FormatConstants.PARENT_FRAGMENT_NAME_PSEUDO_ATTRIBUTE)) {
      Fragment enclosingFragment = getEnclosingFragment ();
      if (enclosingFragment != null) {
        return enclosingFragment.getName ();
      }
      return null;
    }
    else {
      return super.getAttribute (name);
    }
  }

    /**
     * Add a Format to this Layout.
     * @param format The Format to add.
     * @todo later It is possible to add a Format that is not a child - this
     * should not be allowed. Note that there is no specific implementation
     * of hashCode() or equals() in Fragment because we assume that no-one
     * will ever try to add a Format that is not a child - currently this is 
     * the case.
     */
    public void addFormat(Format format) {
        if(formatScope == null) {
            FormatType types [] = { FormatType.FRAGMENT };
            formatScope = new FormatScope(layout.getFormatScope(),
                                          types);
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
     * Class) version.
     * @param formatType The FormatType of the Format to retrieve.
     * @return The specified Format if it has been registered or null.
     */
    public Format retrieveFormat(String name, FormatType formatType) {
        if(formatScope == null) {
            return null;
        }
        return formatScope.retrieveFormat(name, formatType);
    }

    // Javadoc inherited.
    public void validate(ValidationContext context) {

        validateFragmentAttributes(context);

        // Fragments may not appear within spatial and temporal format
        // iterators.
        if (isContainedBy(SpatialFormatIterator.class)) {
            context.addDiagnostic(sourceLocation, DiagnosticLevel.ERROR,
                    context.createMessage("fragment-must-not-be-in-spatial",
                            this.getName()));
        }
        if (isContainedBy(TemporalFormatIterator.class)) {
            context.addDiagnostic(sourceLocation, DiagnosticLevel.ERROR,
                    context.createMessage("fragment-must-not-be-in-temporal",
                            this.getName()));
        }

        // Fragments generally define a logical "scope" for their contained
        // formats. For example, pane names must be unique within a fragment,
        // and there may only be one dissecting pane within a fragment. So here
        // we must ensure that all the definition scopes that are defined
        // for layouts and which are part of this fragments logical "scope" are
        // reset by creating a sub scope before we validate the children and
        // ending the sub scope afterwards.

        // TODO: later: maybe it would be better for each type to declare when
        // its subscope started rather than have to do explicitly like this.

        Collection formatNamespaceDefinitionTypes =
                FormatNamespaceDefinitionTypes.getTypes();

        // Create sub-scopes for all format namespace scopes.
        for (Iterator types = formatNamespaceDefinitionTypes.iterator();
                types.hasNext();) {
            DefinitionType type = (DefinitionType) types.next();
            context.beginDefinitionScope(type);
        }
        // Create a sub-scope for the dissecting pane scope.
        context.beginDefinitionScope(LayoutModel.DISSECTING_PANE_SCOPE_TYPE);

        validateChildren(context);

        for (Iterator types = formatNamespaceDefinitionTypes.iterator();
                types.hasNext();) {
            DefinitionType type = (DefinitionType) types.next();
            context.endDefinitionScope(type);
        }
        context.endDefinitionScope(LayoutModel.DISSECTING_PANE_SCOPE_TYPE);
    }

    /**
     * NOTE: there is no schema attribute group for this, we created it
     * purely to make the code easier to read.
     *
     * @param context
     */
    private void validateFragmentAttributes(ValidationContext context) {

        // Fragments must have unique names *within the containing scope*.
        validateRequiredName(context);

        // linkText does not require validation.
        // backLinkText does not require validation.
        // QuotedComponentReferenceOrLiteralTextType == any string

        Step step = context.pushPropertyStep("linkStyleClass");
        final String linkStyleClass = getLinkClass();
        if (linkStyleClass != null) {
            if (!LayoutTypeValidator.isThemeClassNameType(linkStyleClass)) {
                addErrorDiagnostic(context, context.createMessage(
                        LayoutMessages.LINK_STYLE_CLASS_ILLEGAL,
                        linkStyleClass));
            }
        }
        context.popStep(step);

        step = context.pushPropertyStep("showPeerLinks");
        final String showPeerLinks = getPeerLink();
        if (showPeerLinks != null) {
            if (!LayoutTypeValidator.isBoolean(showPeerLinks)) {
                addErrorDiagnostic(context, context.createMessage(
                        LayoutMessages.SHOW_PEER_LINKS_ILLEGAL,
                        showPeerLinks));
            }
        }
        context.popStep(step);

        step = context.pushPropertyStep("fragmentLinkOrder");
        final String fragmentLinkOrder = getFragmentLinkOrder();
        if (fragmentLinkOrder != null) {
            Set keywords = LayoutTypeValidator.getOldKeywords("fragment", "fragmentLinkOrder",
                    new String[] {"parent-first", "peers-first"});
            if (!keywords.contains(fragmentLinkOrder)) {
                addErrorDiagnostic(context, context.createMessage(
                        LayoutMessages.FRAGMENT_LINK_ORDER_ILLEGAL,
                        fragmentLinkOrder));
            }
        }
        context.popStep(step);
    }

    // javadoc inherited
    public boolean equals(final Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        if (!super.equals(other)) return false;

        final Fragment fragment = (Fragment) other;

        return orphanedDefaultFragment == fragment.orphanedDefaultFragment &&
            !(dissectingPanes != null ?
                !dissectingPanes.equals(fragment.dissectingPanes) :
                fragment.dissectingPanes != null) &&
            !(formatScope != null ?
                !formatScope.equals(fragment.formatScope) :
                fragment.formatScope != null);
    }

    // javadoc inherited
    public int hashCode() {
        int result = 31 * super.hashCode() +
            (dissectingPanes != null ? dissectingPanes.hashCode() : 0);
        result = 31 * result +
            (formatScope != null ? formatScope.hashCode() : 0);
        result = 31 * result + (orphanedDefaultFragment ? 1 : 0);
        return result;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 30-Sep-05	9652/1	gkoch	VBM:2005092204 Initial marshaller/unmarshaller for layoutFormat

 29-Sep-05	9590/1	schaloner	VBM:2005092204 Updating layouts for JiBX. Removed interface constants antipattern

 11-Jul-05	8992/1	pduffin	VBM:2005071109 Modified layouts and formats to allow separation between runtime and design time classes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 26-Oct-04	5977/1	tom	VBM:2004022303 Added linkStyleClass to fragments

 26-Oct-04	5954/1	tom	VBM:2004022303 Added linkStyleClass to fragments

 09-Sep-03	1364/1	doug	VBM:2003090507 Added new Fragment Style Class attribute to Fragments

 ===========================================================================
*/
