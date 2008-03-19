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
 * $Header: /src/voyager/com/volantis/mcs/layouts/FormatConstants.java,v 1.13 2003/03/14 16:37:28 doug Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 02-Nov-01    Paul            VBM:2001102403 - Created.
 * 22-Nov-01    Pether          VBM:2001112101 - Added the
 *                              BACK_LINK_TEXT_ATTRIBUTE attribut.
 * 25-Jan-02    Adrian          VBM:2001110201 - new MEASURE_ATTRIBUTE for key
 *                              in MeasureAttributeView
 * 30-Jan-02	  Steve		        VBM: 2002011411 - Added PEER_LINK_ATTRIBUTE
 * 13-Feb-02    Steve           VBM: 2001101803 - Added
 *                              PARENT_FORM_NAME_PSEUDO_ATTRIBUTE to key
 *                              the name of the parent form for form fragments
 * 04-Mar-02    Allan           VBM:2002030102 - Added default value constants
 *                              for borderWidth, horzizAlign and verticalAlign.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 28-Mar-02    Steve           VBM:2002021404 - Added attribute names for
 *                              previous and next fragment links in form
 *                              fragments.
 * 03-May-02    Paul            VBM:2002042203 - Added constant for the
 *                              MaximumContentSize attribute.
 * 08-Nov-02    Byron           VBM:2002110515 - Added default CELL_SPACING
 *                              value
 * 03-Dec-02    Phil W-S        VBM:2002111208 - Added the new shard link
 *                              previous and next style classes.
 * 03-Jan-03    Phil W-S        VBM:2002122404 - Add the OPTIMIZATION_LEVEL
 *                              attribute and values.
 * 14-Mar-03    Doug            VBM:2003030409 - Added constants used for 
 *                              specifying shard and fragment link generation
 *                              ordering
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.layouts;

/**
 * The set of constants used by formats.
 */
public interface FormatConstants {

  /**
   * The key for accessing the border width attribute which controls the
   * width of the border generated around this format.
   */
  public static final String BORDER_WIDTH_ATTRIBUTE = "BorderWidth";

  /**
   * The default value for the border width attribute.
   */
  public static final String DEFAULT_BORDER_WIDTH = "0";

  /**
   * The key for accessing the cell padding attribute which controls the
   * amount of padding added around the children of this format.
   */
  public static final String CELL_PADDING_ATTRIBUTE = "CellPadding";

  /**
   * The default value for the cell padding attribute.
   */
  public static final String DEFAULT_CELL_PADDING = "0";

  /**
   * The key for accessing the cell padding attribute which controls the
   * amount of padding added around the children of this format.
   */
  public static final String CELL_SPACING_ATTRIBUTE = "CellSpacing";

  /**
   * The default value for the cell padding attribute.
   */
  public static final String DEFAULT_CELL_SPACING = "0";

  /**
   * The key for accessing the destination area attribute which controls the
   * destination area for the contents of the format.
   */
  public static final String DESTINATION_AREA_ATTRIBUTE = "DestinationArea";

  /**
   * The key for accessing the horizontal alignment attribute which controls
   * the horizontal position of the children within this format, or the
   * contents within the pane.
   */
  public static final String HORIZONTAL_ALIGNMENT_ATTRIBUTE
    = "HorizontalAlignment";
  public static final String HORIZONTAL_ALIGNMENT_VALUE_LEFT = "Left";
  public static final String HORIZONTAL_ALIGNMENT_VALUE_CENTER = "Center";
  public static final String HORIZONTAL_ALIGNMENT_VALUE_RIGHT = "Right";
  public static final String HORIZONTAL_ALIGNMENT_VALUE_START = "Start";
  public static final String HORIZONTAL_ALIGNMENT_VALUE_END = "End";
  public static final String DEFAULT_HORIZONTAL_ALIGNMENT =
    HORIZONTAL_ALIGNMENT_VALUE_LEFT;

  /**
   * The key for accessing the vertical alignment attribute which controls
   * the vertical position of the children within this format, or the
   * contents within the pane.
   */
  public static final String VERTICAL_ALIGNMENT_ATTRIBUTE
    = "VerticalAlignment";
  public static final String VERTICAL_ALIGNMENT_VALUE_TOP = "Top";
  public static final String VERTICAL_ALIGNMENT_VALUE_CENTER = "Center";
  public static final String VERTICAL_ALIGNMENT_VALUE_BOTTOM = "Bottom";
  public static final String DEFAULT_VERTICAL_ALIGNMENT =
    VERTICAL_ALIGNMENT_VALUE_CENTER;

  public static final String LINK_TEXT_ATTRIBUTE = "LinkText";

  public static final String BACK_LINK_TEXT_ATTRIBUTE = "BackLinkText";

  public static final String PREVIOUS_LINK_TEXT_ATTRIBUTE =
                                                    "PreviousLinkText";
  public static final String NEXT_LINK_TEXT_ATTRIBUTE =
                                                    "NextLinkText";

  /**
   * Identifier for the fragment link class attribute
   */
  public static final String FRAGMENT_LINK_STYLE_CLASS_ATTRIBUTE = "FragLinkClass";

  public static final String PEER_LINK_ATTRIBUTE = "PeerLinks";
 
  /**
   * Constant for the fragment link ordering attribute
   */
  public static final String FRAG_LINK_ORDER_ATTRIBUTE = "fragLinkOrder";
     
  /**
   * Constant that indicates a parent link is to be generated before peer links
   * for fragments
   */ 
  public static final String PARENT_FIRST = "parentFirst";
  
  /**
   * Constant that indicates peer links are to be generated before a parent 
   * links for fragments
   */ 
  public static final String PEERS_FIRST = "peersFirst";

  public static final String NEXT_LINK_STYLE_ATTRIBUTE =
                                                    "NextLinkStyle";

  public static final String PREVIOUS_LINK_STYLE_ATTRIBUTE =
                                                    "PreviousLinkStyle";

  public static final String NEXT_LINK_POSITION_ATTRIBUTE =
                                                    "NextLinkPosition";

  public static final String PREVIOUS_LINK_POSITION_ATTRIBUTE =
                                                    "PreviousLinkPosition";

  /**
   * Constant for the shard link ordering attribute.
   */
  public static final String SHARD_LINK_ORDER_ATTRIBUTE = "shardLinkOrder";  

  /**
   * Constant indicating that the previous link should come before the next 
   * link when rendering a shard
   */
  public static final String PREV_FIRST = "prevFirst";

  /**
   * Constant indicating that the next link should come before the previous 
   * link when rendering a shard
   */

  public static final String NEXT_FIRST = "nextFirst";
    
  public static final String NAME_ATTRIBUTE = "Name";

  public static final String RESET_ATTRIBUTE = "Reset";

  public static final String BACKGROUND_COLOUR_ATTRIBUTE
    = "BackgroundColour";
  public static final String FILTER_KEYBOARD_USABILITY_ATTRIBUTE
    = "FilterText";

  /**
   * Key for accessing the optimization level attribute which controls
   * table-based markup optimization for those protocols that use the
   * {@link com.volantis.mcs.protocols.trans.UnabridgedTransformer}
   * variants that perform selective table merging.
   */
  public static final String OPTIMIZATION_LEVEL_ATTRIBUTE =
      "OptimizationLevel";

  /**
   * Optimization should never be performed for a table with this
   * level associated with it.
   */
  public static final String OPTIMIZATION_LEVEL_VALUE_NEVER = "Never";

  /**
   * Optimization should only be performed for a table with this
   * level associated with it if the merging of the table will have little
   * or no visual impact.
   */
  public static final String OPTIMIZATION_LEVEL_VALUE_LITTLE_IMPACT =
      "LittleImpact";

  /**
   * Optimization should always be performed for a table with this
   * level associated with it.
   */
  public static final String OPTIMIZATION_LEVEL_VALUE_ALWAYS = "Always";
  public static final String DEFAULT_OPTIMIZATION_LEVEL =
      OPTIMIZATION_LEVEL_VALUE_NEVER;

  // Attributes added for primarilly for Montages and Segments
  public static final String FRAME_BORDER_ATTRIBUTE = "FrameBorder";
  public static final String FRAME_SPACING_ATTRIBUTE = "FrameSpacing";

  public static final String BORDER_COLOUR_ATTRIBUTE = "BorderColour";

  public static final String SCROLLING_ATTRIBUTE = "Scrolling";
  public static final String SCROLLING_VALUE_AUTOMATIC = "Auto";
  public static final String SCROLLING_VALUE_NO = "No";
  public static final String SCROLLING_VALUE_YES = "Yes";

  public static final String MARGIN_HEIGHT_ATTRIBUTE = "MarginHeight";
  public static final String MARGIN_WIDTH_ATTRIBUTE = "MarginWidth";
  public static final String RESIZE_ATTRIBUTE = "Resize";

  /**
   * The key for accessing the background image attribute which specifies
   * the name of a component that represents the background image.
   */
  public static final String BACKGROUND_COMPONENT_ATTRIBUTE
    = "BackgroundComponent";

  /**
   * The key for accessing the background image type attribute which specifies
   * the type of a component that represents the background image.
   */
  public static final String BACKGROUND_COMPONENT_TYPE_ATTRIBUTE =
    "BackgroundComponentType";

  /**
   * The key for accessing the image component type attribute which specifies
   * that the image component is of type image
   */
  public static final String BACKGROUND_COMPONENT_TYPE_IMAGE = "Image";

  /**
   * The key for accessing the dynamic visual component type attribute which
   * specifie that the image component is of type dynamic visual
   */
  public static final String BACKGROUND_COMPONENT_TYPE_DYNAMIC_VISUAL
    = "DynamicVisual";

  public static final String NEXT_SHARD_SHORTCUT_ATTRIBUTE
    = "NextShardShortcut";

  public static final String NEXT_SHARD_LINK_TEXT_ATTRIBUTE
    = "NextShardLinkText";

  public static final String NEXT_SHARD_LINK_CLASS_ATTRIBUTE
    = "NextShardLinkClass";

  public static final String PREVIOUS_SHARD_SHORTCUT_ATTRIBUTE
    = "PreviousShardShortcut";

  public static final String PREVIOUS_SHARD_LINK_TEXT_ATTRIBUTE
    = "PreviousShardLinkText";

  public static final String PREVIOUS_SHARD_LINK_CLASS_ATTRIBUTE
      = "PreviousShardLinkClass";

  public static final String PARENT_FRAGMENT_NAME_PSEUDO_ATTRIBUTE
    = "ParentFragmentName";

  public static final String PARENT_FORM_NAME_PSEUDO_ATTRIBUTE
    = "ParentFormName";

  public static final String DEFAULT_FRAGMENT_NAME_PSEUDO_ATTRIBUTE
    = "DefaultFragmentName";

  public static final String DEFAULT_SEGMENT_NAME_PSEUDO_ATTRIBUTE
    = "DefaultSegmentName";

  /**
   * This attribute has been deprecated and only exists in order to allow
   * old style layouts to be loaded.
   */
  public static final String DEPRECATED_FRAGMENT_NAME_ATTRIBUTE
    = "FragmentName";

  /**
   * The key for accessing the height attribute which controls the
   * height of the format.
   */
  public static final String HEIGHT_ATTRIBUTE = "Height";

  /**
   * The key for accessing the width units attribute which controls the
   * units of the width of the format.
   */
  public static final String HEIGHT_UNITS_ATTRIBUTE = "HeightUnits";
  public static final String HEIGHT_UNITS_VALUE_PERCENT = "percent";
  public static final String HEIGHT_UNITS_VALUE_PIXELS = "pixels";

  /**
   * The key for accessing the width attribute which controls the
   * width of the format.
   */
  public static final String WIDTH_ATTRIBUTE = "Width";

  /**
   * The key for accessing the width units attribute which controls the
   * units of the width of the format.
   */
  public static final String WIDTH_UNITS_ATTRIBUTE = "WidthUnits";
  public static final String WIDTH_UNITS_VALUE_PERCENT = "percent";
  public static final String WIDTH_UNITS_VALUE_PIXELS = "pixels";

  /**
   * The key for accessing the measure units attribute which controls the
   * units of the measurement of the format.
   */
  public static final String MEASURE_ATTRIBUTE = "Measure";

  /**
   * The key for accessing the maximum content size of the dissecting pane.
   */
  public static final String MAXIMUM_CONTENT_SIZE_ATTRIBUTE
    = "MaximumContentSize";


  /**
   * Spatial Iterator constants
   */
  public static final String ITERATOR_SEPARATOR_ATTRIBUTE =
        "IteratorSeparator";
  public static final String ITERATOR_2D_INDEXING_DIR_ATTRIBUTE =
        "SpatialIterator2DIndexingDir";
  public static final String ITERATOR_ROWS_ATTRIBUTE =
        "SpatialIteratorRows";
  public static final String ITERATOR_ROW_COUNT_ATTRIBUTE =
        "SpatialIteratorRowCount";
  public static final String ITERATOR_COLUMNS_ATTRIBUTE =
        "SpatialIteratorColumns";
  public static final String ITERATOR_COLUMN_COUNT_ATTRIBUTE =
        "SpatialIteratorColumnCount";


    /**
     * The style class constant for Grid and Spatial format iterators.
     */
    public static final String STYLE_CLASS = "StyleClass";

    /**
     * Bi-Directionality attribute for Pane, Grid and Spatial iterators.
     */
    public static final String DIRECTION_ATTRIBUTE = "Directionality";
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 26-Oct-04	5977/1	tom	VBM:2004022303 Added linkStyleClass to fragments

 26-Oct-04	5954/1	tom	VBM:2004022303 Added linkStyleClass to fragments

 08-Oct-04	5758/1	byron	VBM:2004100804 Support style classes on grids and spatial format iterators: Common

 26-Nov-03	2027/1	mat	VBM:2003112109 Ignore spatial iterator attributes when comparing and fix tomcat build

 09-Sep-03	1364/1	doug	VBM:2003090507 Added new Fragment Style Class attribute to Fragments

 ===========================================================================
*/
