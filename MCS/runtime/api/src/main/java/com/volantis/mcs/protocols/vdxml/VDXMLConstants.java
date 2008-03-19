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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.vdxml;

/**
 * Holds a set of useful constants for use in the VDXML protocol and associated
 * classes.
 */
public final class VDXMLConstants {
    /**
     * Represents all types of pane.
     */
    public static final String PSEUDO_PANE_ELEMENT = "pane";

    /**
     * Represents all types of grid (including format iterations).
     */
    public static final String PSEUDO_GRID_ELEMENT = "grid";

    /**
     * Represents rows in a {@link #PSEUDO_GRID_ELEMENT}.
     */
    public static final String PSEUDO_ROW_ELEMENT = "row";

    /**
     * Represents cells in a {@link #PSEUDO_ROW_ELEMENT}.
     */
    public static final String PSEUDO_CELL_ELEMENT = "cell";

    /**
     * Represents all types of block elements.
     */
    public static final String PSEUDO_BLOCK_ELEMENT = "block";

    /**
     * Represents all types of inline elements.
     */
    public static final String PSEUDO_INLINE_ELEMENT = "inline";

    /**
     * Used to handle form fragmentation links.  Also {@link #PSEUDO_PREV_NAME}
     * {@link #PSEUDO_NEXT_VALUE}, amd {@link #PSEUDO_PREV_VALUE}
     */
    public static final String PSEUDO_NEXT_NAME = "next-name";

    /**
     * Used to handle form fragmentation links.  Also {@link #PSEUDO_PREV_NAME}
     * {@link #PSEUDO_NEXT_NAME}, amd {@link #PSEUDO_PREV_VALUE}
     */
    public static final String PSEUDO_NEXT_VALUE = "next-value";

    /**
     * Used to handle form fragmentation links.  Also {@link #PSEUDO_NEXT_NAME},
     * {@link #PSEUDO_NEXT_VALUE}, amd {@link #PSEUDO_PREV_VALUE}
     */
    public static final String PSEUDO_PREV_NAME = "prev-name";

    /**
     * Used to handle form fragmentation links.  Also {@link #PSEUDO_NEXT_NAME},
     * {@link #PSEUDO_NEXT_VALUE}, amd {@link #PSEUDO_PREV_NAME}
     */
    public static final String PSEUDO_PREV_VALUE = "prev-value";

    /**
     * Appears on both {@link #PSEUDO_PANE_ELEMENT}s and
     * {@link #PSEUDO_GRID_ELEMENT}s.
     */
    public static final String PSEUDO_PADDING_ATTRIBUTE = "cell-padding";

    /**
     * Appears on {@link #PSEUDO_GRID_ELEMENT}s.
     */
    public static final String PSEUDO_SPACING_ATTRIBUTE = "cell-spacing";

    /**
     * Appears on {@link #PSEUDO_PANE_ELEMENT}s.
     */
    public static final String PSEUDO_BORDER_ATTRIBUTE = "border-width";

    /**
     * Appears on {@link #PSEUDO_PANE_ELEMENT}s.
     */
    public static final String PSEUDO_BORDER_COLOUR_ATTRIBUTE =
            "border-colour";

    /**
     * Used to indicate that a pane represents a dissecting pane, or not. If
     * not found, implies the pane is not dissecting.
     *
     * <p>Appears on {@link #PSEUDO_PANE_ELEMENT}s.</p>
     */
    public static final String PSEUDO_DISSECTION_ATTRIBUTE = "dissecting";

    /**
     * Fake element that is generated when a dissecting pane is encountered.
     * It is then used to indicate areas of the DOM to be manipulated in the
     * transfomer.  It may also contain a {@link #PSEUDO_DISSECTION_CONTENT}
     * element which itself contains information that is moved during
     * transformation.
     */
    public static final String PSEUDO_DISSECTION_ELEMENT = "dissecting";

    /**
     * A fake element that holds contain that is only pertinent for dissecting
     * panes.  This is generally the next/forward information that is ncessary
     * for moving between the dissected parts of the overall pane.  It is used
     * in conjunction with {@link #PSEUDO_DISSECTION_ELEMENT}.
     */
    public static final String PSEUDO_DISSECTION_CONTENT = "content";

    /**
     * Used to indicate that a pane represents a special output area, or not.
     * If not found, implies the pane is a normal one.
     *
     * <p>Appears on {@link #PSEUDO_PANE_ELEMENT}s.</p>
     */
    public static final String PSEUDO_DESTINATION_AREA_ATTRIBUTE = "special";

    /**
     * Used to indicate, on a pane with
     * {@link #PSEUDO_DESTINATION_AREA_ATTRIBUTE} set to
     * {@link VDXMLVersion2_0#NAVIGATION_DESTINATION}, that there is a help
     * zone in the page being output.
     */
    public static final String PSEUDO_HAS_HELP_ZONE_ATTRIBUTE = "help-zoned";

    /**
     * This is used when outputing a NBSP in VDXML.  This is necessary because
     * the existing values in the protocols encode the value in a way in
     * which the gateway cannot understand.  Sending an entity (without an
     * encoded ampersand!) ensures the gateway does any translations
     * necessary internally.
     */
    public static final String VDXML_NBSP = "\u00a0";

    /**
     * Standard VDXML element
     */
    public static final String VDXML_ELEMENT = "VDXML";

    /**
     * Standard VDXML element
     */
    public static final String FRAME_ELEMENT = "FRAME";

    /**
     * Standard VDXML element.  This will truncate any content.  To have a
     * dissecting pane then see {@link #DISSECT_TEXT_ELEMENT}.
     */
    public static final String TEXT_BLOCK_ELEMENT = "TEXTE";

    /**
     * Standard VDXML element.  Used when the gateway should dissect the
     * text area as necessary to ensure all contents are displayed instead of
     * truncated.  This is how dissecting panes are implemented in VDXML.
     * If any {@link #NAVIGATION_ELEMENT} occur in the page then this tag
     * will generate invalid VDXML and a {@link #DISSECT_TEXTNAVIG_ELEMENT}
     * should be used instead.
     */
    public static final String DISSECT_TEXT_ELEMENT = "TEXTEDEF";

    /**
     * Standard VDXML element.  For more details on using this element
     * see {@link #DISSECT_TEXT_ELEMENT}.
     */
    public static final String DISSECT_TEXTNAVIG_ELEMENT = "TEXTEDEFNAVIG";

    public static final String DISSECT_NEXT_HELP = "SISUITE";

    public static final String DISSECT_PREVIOUS_HELP = "SIRETOUR";

    public static final String DISSECT_PREVIOUS_NEXT_HELP = "SIRETOURSUITE";

    /**
     * Standard VDXML element
     */
    public static final String BORDER_ELEMENT = "CADREP";

    /**
     * Standard VDXML element
     */
    public static final String IMAGE_ELEMENT = "IMG";

    /**
     * Standard VDXML element
     */
    public static final String FORM_ELEMENT = "FORM";

    /**
     * Standard VDXML element
     */
    public static final String LINK_ELEMENT = "RACCOURCI";

    /**
     * Standard VDXML element
     */
    public static final String VALIDFORM_ELEMENT = "VALIDFORM";

    /**
     * Standard VDXML element
     */
    public static final String HORIZONTAL_RULE_ELEMENT = "HR";

    /**
     * Attribute that represents a function key (of which eight exist on
     * VDXML/Minitel devices.  See also {@link #RELOAD_FUNCTION_STRING},
     * {@link #NEXT_FUNCTION_STRING}, {@link #PREVIOUS_FUNCTION_STRING}, and
     * {@link #SEND_FUNCTION_STRING}.
     */
    public static final String FUNCTION_ATTRIBUTE = "FNCT";

    /**
     * Attribute that represents a URL
     */
    public static final String URL_ATTRIBUTE = "URL";

    /**
     * Attribute that represents shortcut text
     */
    public static final String TEXT_ATTRIBUTE = "TEXT";

    /**
     * One of the eight function keys
     */
    public static final String RELOAD_FUNCTION_STRING = "REPETITION";

    /**
     * One of the eight function keys
     */
    public static final String NEXT_FUNCTION_STRING = "SUITE";

    /**
     * One of the eight function keys
     */
    public static final String PREVIOUS_FUNCTION_STRING = "RETOUR";

    /**
     * One of the eight function keys
     */
    public static final String SEND_FUNCTION_STRING = "ENVOI";

    /**
     * Standard VDXML element
     */
    public static final String INPUT_FIELD_ELEMENT = "SAISIE";

    /**
     * Standard VDXML element
     */
    public static final String IMPLICIT_ELEMENT = "PARAMFORM";

    /**
     * Attribute that represents the action link in a {@link #FORM_ELEMENT}.
     */
    public static final String ACTION_ATTRIBUTE = "URL";

    /**
     * Attribute that represents the request method in a {@link #FORM_ELEMENT).
     */
    public static final String METHOD_ATTRIBUTE = "REQ";

    /**
     * Standard VDXML element
     */
    public static final String SELECT_ELEMENT = "SELECT";

    /**
     * Standard VDXML element
     */
    public static final String DESELECT_ELEMENT = "DESELECT";

    /**
     * Standard VDXML element
     */
    public static final String HELP_ELEMENT = "AIDE";

    /**
     * Standard VDXML element
     */
    public static final String ALIAS_ELEMENT = "ALIAS";

    /**
     * An attribute that represents the alias in a value-alias pair in
     * {@link #ALIAS_ELEMENT}.
     */
    public static final String ALIAS_ATTRIBUTE = "ALIAS";

    /**
     * Name attribute
     */
    public static final String NAME_ATTRIBUTE = "NAME";

    /**
     * Standard VDXML element
     */
    public static final String HELP_ZONE_ELEMENT = "ZONEAIDE";

    /**
     * Standard VDXML element
     */
    public static final String LINE_BREAK_ELEMENT = "BR";

    /**
     * An attribute that represents a value.  This also occurs in a
     * value-alias pair in {@link #ALIAS_ELEMENT}.
     */
    public static final String VALUE_ATTRIBUTE = "VAL";

    /**
     * Attribute that indicates whether free text entry can be used for an
     * input field.  See {@link #YES_VALUE} and {@link #NO_VALUE}.
     */
    public static final String LIBREVAL_ATTRIBUTE = "LIBREVAL";

    /**
     * Source atttribute
     */
    public static final String SOURCE_ATTRIBUTE = "SRC";

    /**
     * Initial value attribute for fields
     */
    public static final String INIT_ATTRIBUTE = "INIT";

    /**
     * Shortcut for a yes value in the markup in French - (O)ui.
     */
    public static final String YES_VALUE = "O";

    /**
     * Shortcut for a no value in the markup in French - (N)on.
     */
    public static final String NO_VALUE = "N";

    /**
     * A string representation of yes in French.
     */
    public static final String YES_STRING = "Oui";

    /**
     * A string representation of no in French.
     */
    public static final String NO_STRING = "Non";

    /**
     * Representation of true as a numeric value.
     */
    public static final String TRUE_NUMERIC_VALUE = "1";

    /**
     * Representation of false as a numeric value.
     */
    public static final String FALSE_NUMERIC_VALUE = "0";

    /**
     * The usable display width of a VDXML device in characters.
     */
    public static final String DISPLAY_WIDTH_IN_CHARS = "40";

    /**
     * The usable display height of a VDXML device in characters.
     */
    public static final String DISPLAY_HEIGHT_IN_CHARS = "24";

    /**
     * This is used to prevent any problems with empty values being an
     * intentionally empty field and with a field set to empty because it
     * is not important.
     */
    public static final String ENCODED_FIELD_VALUE = "-";

    /**
     * Used to indicate a selected choice field in a form and the return url.
     */
    public static final String CHOICE_CAPITAL = "X";

    /**
     * Used to indicate a selected choice field in a form and the return url.
     */
    public static final String CHOICE_LOWERCASE = "x";

    /**
     * Used to indicate an unselected choice field in a form and the return url.
     */
    public static final String CHOICE_EMPTY = "";

    /**
     * Standard VDXML element
     */
    public static final String NAVIGATION_ELEMENT = "NAVIG";

    /**
     * Alignment attribute
     */
    public static final String ALIGN_ATTRIBUTE = "ALIGN";

    /**
     * Width attribute - used on most visual VDXML elements.
     */
    public static final String WIDTH_ATTRIBUTE = "DX";

    /**
     * Height attribute - used on most visual VDXML elements.
     */
    public static final String HEIGHT_ATTRIBUTE = "DY";

    /**
     * Attribute representing the x position of the elements top left corner.
     */
    public static final String X_ATTRIBUTE = "X";

    /**
     * Attribute representing the y position of the elements top left corner.
     */
    public static final String Y_ATTRIBUTE = "Y";

    /**
     * Size attribute, which represents the width of those elements to which
     * it is applied.
     */
    public static final String SIZE_ATTRIBUTE = "TAILLE";

    /**
     * Background colour attribute.
     */
    public static final String BACKGROUND_COLOUR_ATTRIBUTE = "CF";

    /**
     * Foreground/Text colour attribute.
     */
    public static final String TEXT_COLOUR_ATTRIBUTE = "CC";

    /**
     * Attribute representing the colour of the left border.
     */
    public static final String BORDER_LEFT_COLOUR_ATTRIBUTE = "CBG";

    /**
     * Attribute representing the colour of the right border.
     */
    public static final String BORDER_RIGHT_COLOUR_ATTRIBUTE = "CBD";

    /**
     * Attribute representing the colour of the line within the border.
     */
    public static final String BORDER_LINE_COLOUR_ATTRIBUTE = "CBI";

    /**
     * Attribute representing the when the colour should change on the border
     */
    public static final String BORDER_COLOUR_CHANGE_ATTRIBUTE = "LBG";

    /**
     * The background colour of a {@link #NAVIGATION_ELEMENT} used within
     * a {@link #DISSECT_TEXTNAVIG_ELEMENT}.
     */
    public static final String NAVIG_BACKGROUND_ATTRIBUTE = "CFNAVIG";

    /**
     * The foreground/text colour of a {@link #NAVIGATION_ELEMENT} used within
     * a {@link #DISSECT_TEXTNAVIG_ELEMENT}.
     */
    public static final String NAVIG_TEXT_COLOUR_ATTRIBUTE = "CCNAVIG";

    /**
     * The X position of a {@link #NAVIGATION_ELEMENT} used within
     * a {@link #DISSECT_TEXTNAVIG_ELEMENT}.
     */
    public static final String NAVIG_X_ATTRIBUTE = "XNAVIG";

    /**
     * The Y position of a {@link #NAVIGATION_ELEMENT} used within
     * a {@link #DISSECT_TEXTNAVIG_ELEMENT}.
     */
    public static final String NAVIG_Y_ATTRIBUTE = "YNAVIG";

    /**
     * The character that should be displayed in an input field for blank
     * areas.
     */
    public static final String CHARACTER_ATTRIBUTE = "CD";

    /**
     * Input fields ({@link #INPUT_FIELD_ELEMENT} are always two larger
     * than specified when displayed on a Minitel/VDXML device.  To ensure
     * that the layouts work as intended then this offset is used with
     * the calculated geometry to ensure a visually consistent behaviour.
     */
    public static final int INPUT_FIELD_SIZE_OFFSET = 2;
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 23-Sep-04	5599/1	geoff	VBM:2004092214 Port VDXML to MCS: port existing protocol code

 09-Jun-04	4495/26	claire	VBM:2004051807 Fixed multiple select handling, including incorporating architecture clarification

 08-Jun-04	4495/24	claire	VBM:2004051807 Added todo on multiple select choices

 07-Jun-04	4495/22	claire	VBM:2004051807 Added JavaDoc where missing, and tidied up some existing comments

 07-Jun-04	4483/20	philws	VBM:2004051807 Input field resizing to account for Minitel behaviour

 04-Jun-04	4495/20	claire	VBM:2004051807 Implemented dissecting panes and ensured elements are created from the DOM pool

 04-Jun-04	4483/18	philws	VBM:2004051807 Add inheritance of width and height from the VDXML element itself

 02-Jun-04	4495/17	claire	VBM:2004051807 Null style fix, tidy up form fragmentation, utilise DOMPool when transforming, and some JavaDoc additions

 01-Jun-04	4495/15	claire	VBM:2004051807 Horizontal menus and line breaks

 01-Jun-04	4483/16	philws	VBM:2004051807 Added image and horizontal rule handling

 28-May-04	4483/14	philws	VBM:2004051807 Navigation handling

 28-May-04	4495/13	claire	VBM:2004051807 Form fragment links

 28-May-04	4483/11	philws	VBM:2004051807 Basic help working

 28-May-04	4495/11	claire	VBM:2004051807 Menu handling and reload links

 28-May-04	4575/2	geoff	VBM:2004051807 Minitel VDXML protocol support (incomplete inline integration)

 28-May-04	4483/7	philws	VBM:2004051807 Updates for initial property state for inline and block element processing

 28-May-04	4495/8	claire	VBM:2004051807 Colour handling for form fields and basic action support

 28-May-04	4483/5	philws	VBM:2004051807 Fix pane <-> input field mapping

 28-May-04	4495/5	claire	VBM:2004051807 Refactoring some of the form handling code

 27-May-04	4495/3	claire	VBM:2004051807 Very basic VDXML form handling

 27-May-04	4483/3	philws	VBM:2004051807 Initial end-to-end layout rendering

 27-May-04	4483/1	philws	VBM:2004051807 Functional layout transformer

 ===========================================================================
*/
