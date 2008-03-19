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
 * $Header: /src/voyager/com/volantis/mcs/layouts/Pane.java,v 1.75 2003/04/17 10:21:06 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 27-Jun-01    Paul            VBM:2001062704 - Added this change history,
 *                              sorted out the copyright, added visit
 *                              method and renamed GridFormat to Grid.
 * 09-Jul-01    Paul            VBM:2001062810 - Sorted out the background
 *                              image retrieval, changed the visit method
 *                              to return a boolean and made getContentBuffer
 *                              public.
 * 23-Jul-01    Paul            VBM:2001070507 - Added support for destination
 *                              area and renamed setBorder method in
 *                              PaneAttributes to setBorderWidth so that it
 *                              more closely matches its use.
 * 24-Jul-01    Paul            VBM:2001061904 - Added support for width unit
 *                              format attributes.
 * 26-Jul-01    Paul            VBM:2001072301 - Initialise the pane attribute
 *                              in PaneAttributes.
 * 30-Jul-01    Paul            VBM:2001071609 - Trimmed content buffers and
 *                              removed some unnecessary code.
 * 02-Aug-01    Allan           VBM:2001072604 - Replaced openComment() -
 *                              comment - closeComment() with
 *                              doComment(comment).
 * 03-Aug-01    Kula            VBM:2001080102 Height property added to pane
 * 14-Sep-01    Allan           VBM:2001091103 - Modified writeFormatPreamble
 *                              and writeFormatPostamble methods to set the
 *                              parent format on paneAttributes.
 * 01-Oct-01    Doug            VBM:2001092501 - now use the Format.java
 *                              method getBestBackgroundImage to calculate the
 *                              background image url. Removed the methods
 *                              getBackgroundImage and setBackroundImage. Added
 *                              Format.BACKGROUND_COMPONENT_TYPE_ATTRIBUTE
 *                              to the userAttributes and defaultAttributes
 *                              arrays
 * 29-Oct-01    Paul            VBM:2001102901 - Layout has been renamed
 *                              DeviceLayout and all methods relating to the
 *                              runtime generation of layouts have been
 *                              moved into protocols.
 * 02-Nov-01    Paul            VBM:2001102403 - Cleaned up.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from class
                                to string.
 * 22 May 02    Steve           VBM:2002040809 Allow user to specify a style
 *                              class for a pane. This style is stored here in
 *                              the format so that it can be passed on to the
 *                              protocol at render time.
 * 24 May 02    Steve           VBM:2002040809 Removed style class from the pane
 *                              as it should be stored in the pane context.
 * 09-Dec-02    Allan           VBM:2002120615 - Replaced String version of
 *                              Format type with FormatType where possible.
 * 10-Dec-02    Allan           VBM:2002110102 - Removed setAttribute() and
 *                              setDeviceLayout().
 * 03-Jan-03    Phil W-S        VBM:2002122404 - Add the OPTIMIZATION_LEVEL
 *                              attribute.
 * 16-Apr-03    Geoff           VBM:2003041603 - Add FormatVisitorException to
 *                              declaration of visit() method.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.layouts;

import com.volantis.mcs.model.path.Step;
import com.volantis.mcs.model.validation.ValidationContext;

/**
 * A format that holds content. A pane can hold the output from
 * one or more tags. For example, a pane could hold a single
 * paragraph, or several paragraphs and headings. However, there is no
 * opportunity to apply formatting to individual tags that use the same
 * pane. To apply formatting, use formats that consist of multiple panes,
 * such as Grid.
 *
 * @mock.generate base="Format"
 */
public class Pane extends Format
    implements OptimizationLevelAttribute, DestinationAreaAttribute {

  private static String [] userAttributes = new String [] {
    FormatConstants.NAME_ATTRIBUTE,
    FormatConstants.BACKGROUND_COLOUR_ATTRIBUTE,
    FormatConstants.BACKGROUND_COMPONENT_ATTRIBUTE,
    FormatConstants.BACKGROUND_COMPONENT_TYPE_ATTRIBUTE,
    FormatConstants.BORDER_WIDTH_ATTRIBUTE,
    FormatConstants.CELL_PADDING_ATTRIBUTE,
    FormatConstants.CELL_SPACING_ATTRIBUTE,
    FormatConstants.DESTINATION_AREA_ATTRIBUTE,
    FormatConstants.VERTICAL_ALIGNMENT_ATTRIBUTE,
    FormatConstants.HORIZONTAL_ALIGNMENT_ATTRIBUTE,
    FormatConstants.HEIGHT_ATTRIBUTE,
    FormatConstants.WIDTH_ATTRIBUTE,
    FormatConstants.WIDTH_UNITS_ATTRIBUTE,
    FormatConstants.OPTIMIZATION_LEVEL_ATTRIBUTE,
    FormatConstants.FILTER_KEYBOARD_USABILITY_ATTRIBUTE,
    "border-left", "border-top"
  };

  private static String [] defaultAttributes = new String [] {
    FormatConstants.BACKGROUND_COLOUR_ATTRIBUTE,
    FormatConstants.BACKGROUND_COMPONENT_ATTRIBUTE,
    FormatConstants.BACKGROUND_COMPONENT_TYPE_ATTRIBUTE,
    FormatConstants.BORDER_WIDTH_ATTRIBUTE,
    FormatConstants.CELL_PADDING_ATTRIBUTE,
    FormatConstants.CELL_SPACING_ATTRIBUTE,
    FormatConstants.DESTINATION_AREA_ATTRIBUTE,
    FormatConstants.VERTICAL_ALIGNMENT_ATTRIBUTE,
    FormatConstants.HORIZONTAL_ALIGNMENT_ATTRIBUTE,
    FormatConstants.HEIGHT_ATTRIBUTE,
    FormatConstants.WIDTH_ATTRIBUTE,
    FormatConstants.WIDTH_UNITS_ATTRIBUTE,
    FormatConstants.OPTIMIZATION_LEVEL_ATTRIBUTE,
    FormatConstants.FILTER_KEYBOARD_USABILITY_ATTRIBUTE,
  };

  private static String [] persistentAttributes = userAttributes;

  /**
   * Create a new pane
   * @param canvasLayout The Layout to which this pane belongs
   */
  public Pane (CanvasLayout canvasLayout) {
    super (0, canvasLayout);
  }


  public FormatType getFormatType () {
    return FormatType.PANE;
  }

  public String [] getUserAttributes () {
    return userAttributes;
  }

  public String [] getDefaultAttributes () {
    return defaultAttributes;
  }

  public String [] getPersistentAttributes () {
    return persistentAttributes;
  }

  public String getFilterKeyboardUsability() {
        return (String) getAttribute(
            FormatConstants.FILTER_KEYBOARD_USABILITY_ATTRIBUTE);
  }

  public void setFilterKeyboardUsability(String filterKeyboardUsability) {
      setAttribute(FormatConstants.FILTER_KEYBOARD_USABILITY_ATTRIBUTE,
                   filterKeyboardUsability);
  }

  public String getOptimizationLevel() {
        return (String) getAttribute(
            FormatConstants.OPTIMIZATION_LEVEL_ATTRIBUTE);
  }

  public void setOptimizationLevel(String optimizationLevel) {
      setAttribute(FormatConstants.OPTIMIZATION_LEVEL_ATTRIBUTE,
                   optimizationLevel);
  }

  public void setDestinationArea (String destinationArea) {
        setAttribute(FormatConstants.DESTINATION_AREA_ATTRIBUTE,
            destinationArea);
  }

  public String getDestinationArea () {
        return (String) getAttribute(
            FormatConstants.DESTINATION_AREA_ATTRIBUTE);
    }

  // Javadoc inherited from super class.
  public boolean visit (FormatVisitor visitor, Object object)
          throws FormatVisitorException {
    return visitor.visit (this, object);
  }

    // Javadoc inherited.
    public void validate(ValidationContext context) {
        validateNonDissectingPaneAttributes(context, "pane");
    }

    private void validateNonDissectingPaneAttributes(
            final ValidationContext context, String element) {

        validateAllPaneAttributes(context, element);
        validateAdditionalNonDissectablePaneAndGridAttributes(context,
            element);
        // destinationAreaAttribute requires no validation.
    }

    void validateAllPaneAttributes(ValidationContext context, String element) {
        validateRequiredName(context);
        validateAllPaneAndGridAndIteratorAttributes(context, element);
        validateFilterKeyboardUsabilityAttribute(context);
    }

    private void validateFilterKeyboardUsabilityAttribute(
            ValidationContext context) {

        Step step = context.pushPropertyStep("filterOnKeyboardUsability");
        final String filterKeyboardUsability = getFilterKeyboardUsability();
        if (filterKeyboardUsability != null) {
            if (!LayoutTypeValidator.isUnsigned(filterKeyboardUsability)) {
                addErrorDiagnostic(context, context.createMessage(
                        LayoutMessages.FILTER_KEYBOARD_USABILITY_ILLEGAL,
                        filterKeyboardUsability));
            }
        }
        context.popStep(step);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 28-Nov-05	10467/1	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 28-Nov-05	10394/1	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 02-Oct-05	9652/1	gkoch	VBM:2005092204 completely custom marshalling/unmarshalling of layoutFormat

 29-Sep-05	9590/1	schaloner	VBM:2005092204 Updating layouts for JiBX. Removed interface constants antipattern

 11-Jul-05	8992/1	pduffin	VBM:2005071109 Modified layouts and formats to allow separation between runtime and design time classes

 18-May-05	8279/1	matthew	VBM:2005042702 Refactor RepositoryException and its derived classes to use ExceptionLocalizers

 19-Apr-05	7738/1	philws	VBM:2004102604 Port RepositoryException localization from 3.3

 19-Apr-05	7720/1	philws	VBM:2004102604 Localize RepositoryException messages

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
