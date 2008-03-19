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

import com.volantis.mcs.dom.DOMVisitor;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.dom.RecursingDOMVisitor;
import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.DOMTransformer;
import com.volantis.mcs.protocols.trans.TwoDVector;
import com.volantis.mcs.protocols.vdxml.style.values.VDXMLColorValue;

/**
 * The transformer responsible for the conversion of the whole document into
 * valid VDXML. Delegates to a {@link VDXMLXDIMETransformer} to convert the
 * XDIME generated aspects.
 */
public class VDXMLTransformer implements DOMTransformer {

    /**
     * If any dissecting pane is encountered this is set as a reference to
     * it.  It is then used if a navigation pane is encountered to allow
     * for them to be merged to create valid VDXML
     */
    private Element scrollingPane = null;

    /**
     * If any navigation pane is encountered this is set as a reference to
     * it.  It is then used if a navigation pane is encountered to allow
     * for them to be merged to create valid VDXML
     */
    private Element navigationPane = null;

    /**
     * Represents pseudo markup entities that have geometry and structure and
     * that can be converted into real VDXML markup.
     */
    private abstract class Entity {
        /**
         * The width of the entity in display character cells.
         */
        protected int width = 0;

        /**
         * The height of the entity in display character cells.
         */
        protected int height = 0;

        /**
         * The absolute horizontal position of the entity in display character
         * cells from the left hand side of the screen. Note that these values
         * validly are in the range [1..40] inclusive.
         */
        protected int x = 0;

        /**
         * The absolute horizontal position of the entity in display character
         * cells from the left hand side of the screen. Note that these values
         * validly are in the range [1..40] inclusive.
         */
        protected int y = 0;

        protected final DOMFactory factory;

        protected Entity(DOMFactory factory) {
            this.factory = factory;
        }

        /**
         * Supporting method used to set both dimensions in one call.
         *
         * @param width  the width of the entity in display character cells
         * @param height the height of the entity in display character cells
         */
        protected void setDimensions(int width, int height) {
            this.width = width;
            this.height = height;
        }

        /**
         * This is used to calculate the dimensions of the entity based on its
         * content. Where entities are hierarchical they should do a
         * depth-first calculation of nested entity dimensions so that the
         * dimensions of their content can contribute to their own dimensions.
         */
        public abstract void calculateDimensions();

        /**
         * This is used to calculate the position of the entity based on the
         * specified origin.
         *
         * @param originX the origin against which the position should be
         *                determined
         * @param originY the origin against which the position should be
         *                determined
         */
        public abstract void calculatePosition(int originX, int originY);

        /**
         * This is used to convert the entity's markup into valid VDXML.
         *
         * @param frames   an element into which any required FRAMEs can be
         */
        public abstract void convertToVdxml(
                Element frames);

        /**
         * Disposes of this entity in a garbage collector friendly manner.
         */
        public abstract void release();

        /**
         * Supporting method used to obtain a numeric value from the given
         * element's specified attribute, or zero if the attribute doesn't
         * exist.
         *
         * @param element   the element to be queried
         * @param attribute the name of the attribute to be queried
         * @return the attribute's numeric value or zero
         */
        protected int getValue(Element element,
                               String attribute) {
            int value = 0;
            String stringValue = element.getAttributeValue(attribute);

            if (stringValue != null) {
                value = Integer.parseInt(stringValue);
            }

            return value;
        }

        /**
         * Supporting method used to obtain a numeric value from the given
         * element's specified attribute, or from the alternative element's
         * specified attribute, or zero if the attribute doesn't exist on
         * either element.
         *
         * @param element   the element to be queried
         * @param alternative
         *                  the alternative element to be queried
         * @param attribute the name of the attribute to be queried
         * @return the attribute's numeric value or zero
         */
        protected int getValue(Element element,
                               Element alternative,
                               String attribute) {
            int value = 0;
            String stringValue = element.getAttributeValue(attribute);

            if ((stringValue == null) &&
                    (alternative != null)) {
                stringValue = alternative.getAttributeValue(attribute);
            }

            if (stringValue != null) {
                value = Integer.parseInt(stringValue);
            }

            return value;
        }

        /**
         * For debug purposes only.
         *
         * @return a string representation of the entity and its state
         */
        public String toString() {
            return new StringBuffer().append("x='").append(x).
                    append("' y='").append(y).
                    append("' width='").append(width).
                    append("' height='").append(height).append("'").toString();
        }

        protected Element allocateElement() {
            return factory.createElement();
        }
    }

    /**
     * Represents a cell in a grid.
     */
    private class Cell extends Entity {
        /**
         * The element that is the pseudo markup cell. May only be null for a
         * fake "top-level" entity.
         */
        protected Element cell;

        /**
         * If this cell contains a pane, this references the pane pseudo
         * markup. Only one of this or {@link #grid} should ever be non-null
         */
        protected Element pane;

        /**
         * If this cell contains a grid, this references the entity that
         * represents that grid. Null if the cell doesn't contain a grid.
         * Only one of this or {@link #pane} should ever be non-null
         */
        protected Grid grid;

        /**
         * If this cell is not a fake "top-level" one, this references the
         * grid entity that contains this cell. Null otherwise
         */
        protected Grid container;

        /**
         * Initializes the new instance using the given parameters. The two
         * parameters should only be null when the instance is a fake
         * "top-level" one. In all other cases both should be non-null.
         *
         * @param cell      the pseudo markup cell for which this entity is
         *                  needed
         * @param container the grid entity that contains this cell
         */
        public Cell(DOMFactory factory,
                    Element cell,
                    Grid container) {
            super(factory);
            this.cell = cell;
            this.container = container;
        }

        // javadoc unnecessary
        protected void setGrid(Grid grid) {
            this.grid = grid;
        }

        // javadoc unnecessary
        public void setPane(Element pane) {
            this.pane = pane;
        }

        /**
         * Transforms the given input document, which is expected to be a
         * mixture of real VDXML markup and pseudo markup, into pure VDXML,
         * setting up the required geometries, colour inheritance etc.
         *
         * @param document the document to be transformed
         */
        public void transform(Document document) {
            final Element frames = allocateElement();

            // This visitor generates the grid/cell instance hierarchy from the
            // pseudo markup
            final DOMVisitor gridGenerator = new GridGenerator(frames, this);

            // Generate the hierarchy of Grid/Cell instances needed, hung off
            // this fake cell instance
            gridGenerator.visit(document);

            // Calculate all the dimensions; this aligns rows and columns in
            // size as needed within grids
            calculateDimensions();

            // Calculate the positions of the panes and grid frames
            calculatePosition(1, 1);

            // Convert the pseudo markup into real VDXML
            convertToVdxml(frames);

            // This visitor makes sure that any text blocks found inside a
            // form are moved out to immediately before the form. It also
            // ensures that the HELP_ZONE_ELEMENT appears before any
            // HELP_ELEMENTs. It also inserts next and previous links based on
            // pseudo values.
            // This works on the final real VDXML markup
            DOMVisitor generalFixer = new GeneralFixerVisitor(frames);

            // Finally ensure that forms don't contain any text blocks and the
            // help zone is before any content that could contain help elements
            generalFixer.visit(document);
        }

        /**
         * Create a URL based on the parameters provided.  This method is used
         * to create a fully functional form fragment link.
         *
         * @param formSpecifier The base url of the form, and that which is
         *                      used in the session as the action on the
         *                      complete form.
         * @param functionURL   The form fragment identifier, usually in the
         *                      form of a name=value pair.
         *
         * @return A complete URL based on the parameters that can be
         * inserted into VDXML as a functional link on a VALIDFORM link to
         * allow previous/next to work in fragments.
         */
        private String createURL(String formSpecifier, String functionURL) {
            String url = null;
            if (formSpecifier != null) {
                url = formSpecifier + functionURL;
            } else {
                url = functionURL;
            }
            return url;
        }

        /**
         * Inserts a fragment link as markup into the VDXML.
         *
         * @param url       The actual link to activate as the fragment.
         * @param element   The element to add the fragment link.  This should
 *                  be an input field if the VDXML is to be valid.
         * @param function  The function key which will activate this on a
*                  Minitel device or emulator.  This should be
*                  {@link VDXMLConstants#PREVIOUS_FUNCTION_STRING} or
*                  {@link VDXMLConstants#NEXT_FUNCTION_STRING} if the
*                  generated VDXML is to behave in a consistent way.
         * @param getMethod Whether the form operates with GET or POST.  This
         */
        private void addFragmentLink(
                String url,
                Element element,
                String function,
                String getMethod) {
            // Allocate the element from the factory.
            Element valid = allocateElement();

            // Set the various attributes on the newly acquired element
            valid.setName(VDXMLConstants.VALIDFORM_ELEMENT);
            valid.setAttribute(VDXMLConstants.FUNCTION_ATTRIBUTE, function);
            valid.setAttribute(VDXMLConstants.URL_ATTRIBUTE, url);
            valid.setAttribute(VDXMLConstants.METHOD_ATTRIBUTE, getMethod);

            // Add the valid element into the DOM
            valid.addToTail(element);
        }

        // javadoc inherited
        public void calculateDimensions() {
            // If there is a nested pane the dimensions will already be set
            // otherwise inherit dimensions from the nested grid
            if (grid != null) {
                grid.calculateDimensions();

                setDimensions(grid.width,
                              grid.height);
            } else if (pane != null) {
                int w = getValue(pane, VDXMLConstants.WIDTH_ATTRIBUTE);
                int h = getValue(pane, VDXMLConstants.HEIGHT_ATTRIBUTE);
                int p = getPadding();
                int b = getValue(pane,
                                 pane, VDXMLConstants.PSEUDO_BORDER_ATTRIBUTE);

                setDimensions(w + (2 * (p + b)), h + (2 * (p + b)));
            }
        }

        /**
         * A supporting method used to determine the padding associated with
         * a given pane. If the pane doesn't specify padding but the containing
         * grid does, the padding is inherited from the grid.
         *
         * @return the padding specified or zero
         */
        private int getPadding() {
            return getValue(pane,
                            (container != null) ?
                            container.getGrid() :
                            null,
                            VDXMLConstants.PSEUDO_PADDING_ATTRIBUTE);
        }

        // javadoc inherited
        public void calculatePosition(int originX, int originY) {
            x = originX;
            y = originY;

            if (grid != null) {
                grid.calculatePosition(originX, originY);
            } else if (pane != null) {
                int padding = getPadding();
                int border = getValue(pane,
                                      VDXMLConstants.PSEUDO_BORDER_ATTRIBUTE);

                pane.setAttribute(VDXMLConstants.X_ATTRIBUTE,
                                  Integer.toString(x + padding + border));
                pane.setAttribute(VDXMLConstants.Y_ATTRIBUTE,
                                  Integer.toString(y + padding + border));
            }
        }

        // javadoc inherited
        public void convertToVdxml(
                Element frames) {
            if (grid != null) {
                grid.convertToVdxml(frames);
            } else if (pane != null) {
                if (getValue(pane, VDXMLConstants.PSEUDO_BORDER_ATTRIBUTE) != 0) {
                    // Create the border markup
                    String borderColour =
                            pane.getAttributeValue(VDXMLConstants.
                                              PSEUDO_BORDER_COLOUR_ATTRIBUTE);
                    String backgroundColour =
                            getInheritValue(
                                    pane.getParent(),
                                    VDXMLConstants.
                                    BACKGROUND_COLOUR_ATTRIBUTE);
                    int x = getValue(pane, VDXMLConstants.X_ATTRIBUTE);
                    int y = getValue(pane, VDXMLConstants.Y_ATTRIBUTE);
                    int w = getValue(pane, VDXMLConstants.WIDTH_ATTRIBUTE);
                    int h = getValue(pane, VDXMLConstants.HEIGHT_ATTRIBUTE);
                    int p = getPadding() + 1; // Allows for the border itself
                    Element border = allocateElement();

                    if (backgroundColour == null) {
                        backgroundColour = VDXMLColorValue.BLACK.toString();
                    }

                    border.setName(VDXMLConstants.BORDER_ELEMENT);
                    border.setAttribute(VDXMLConstants.
                                        BACKGROUND_COLOUR_ATTRIBUTE,
                                        backgroundColour);
                    border.setAttribute(VDXMLConstants.
                                        BORDER_LEFT_COLOUR_ATTRIBUTE,
                                        backgroundColour);
                    border.setAttribute(VDXMLConstants.
                                        BORDER_RIGHT_COLOUR_ATTRIBUTE,
                                        backgroundColour);
                    border.setAttribute(VDXMLConstants.
                                        BORDER_COLOUR_CHANGE_ATTRIBUTE,
                                        "0");

                    if (borderColour == null) {
                        borderColour = VDXMLColorValue.WHITE.toString();
                    }

                    border.setAttribute(VDXMLConstants.
                                        BORDER_LINE_COLOUR_ATTRIBUTE,
                                        borderColour);

                    border.setAttribute(VDXMLConstants.X_ATTRIBUTE,
                                        Integer.toString(x - p));
                    border.setAttribute(VDXMLConstants.Y_ATTRIBUTE,
                                        Integer.toString(y - p));
                    border.setAttribute(VDXMLConstants.WIDTH_ATTRIBUTE,
                                        Integer.toString(w + (2 * p)));
                    border.setAttribute(VDXMLConstants.HEIGHT_ATTRIBUTE,
                                        Integer.toString(h + (2 * p)));

                    border.insertBefore(pane);
                }

                if (xdimeTransformer != null) {
                    // This must be called after applying geometry but before
                    // converting any pseudo markup into real markup
                    xdimeTransformer.transform(factory, pane);
                }

                if (pane.getObject() != VDXMLXDIMETransformer.PANE_REMOVED) {
                    // The pane is still in the DOM so clean up the pane
                    // markup before converting it into a text block
                    // @todo later process dissecting panes
                    String destinationArea = pane.getAttributeValue(
                            VDXMLConstants.PSEUDO_DESTINATION_AREA_ATTRIBUTE);

                    String dissectingPane = pane.getAttributeValue(
                            VDXMLConstants.PSEUDO_DISSECTION_ATTRIBUTE);

                    pane.removeAttribute(VDXMLConstants.
                                         PSEUDO_BORDER_ATTRIBUTE);
                    pane.removeAttribute(VDXMLConstants.
                                         PSEUDO_BORDER_COLOUR_ATTRIBUTE);
                    pane.removeAttribute(VDXMLConstants.
                                         PSEUDO_SPACING_ATTRIBUTE);
                    pane.removeAttribute(VDXMLConstants.
                                         PSEUDO_PADDING_ATTRIBUTE);

                    pane.removeAttribute(VDXMLConstants.
                                         PSEUDO_DESTINATION_AREA_ATTRIBUTE);
                    pane.removeAttribute(VDXMLConstants.
                                         PSEUDO_DISSECTION_ATTRIBUTE);

                    if (VDXMLVersion2_0.HELP_ZONE_DESTINATION.equals(
                            destinationArea)) {
                        // This is a special type of pane. This doesn't have
                        // colour attributes (the protocol doesn't write them).
                        // Also, all content should be thrown away since it
                        // must be empty
                        pane.setName(VDXMLConstants.HELP_ZONE_ELEMENT);
                    } else if (VDXMLVersion2_0.NAVIGATION_DESTINATION.equals(
                            destinationArea)) {
                        // This is another special type of "pane". This doesn't
                        // have alignment and has size instead of width with
                        // no height (is fixed as one character). Its content
                        // must be wrapped in a select -> help pair, on to the
                        // latter the alignment should be copied. A navigation
                        // cannot have a help if there is no help zone
                        String width = pane.getAttributeValue(
                                VDXMLConstants.WIDTH_ATTRIBUTE);

                        if (pane.getAttributeValue(
                                VDXMLConstants.
                                PSEUDO_HAS_HELP_ZONE_ATTRIBUTE) != null) {
                            // Allocate the element from the factory.
                            Element select = allocateElement();

                            Element help = allocateElement();
                            String align = pane.getAttributeValue(
                                    VDXMLConstants.ALIGN_ATTRIBUTE);

                            select.setName(VDXMLConstants.SELECT_ELEMENT);
                            help.setName(VDXMLConstants.HELP_ELEMENT);
                            help.addToHead(select);
                            pane.addChildrenToHead(help);
                            select.addToHead(pane);

                            if (align != null) {
                                help.setAttribute(VDXMLConstants.
                                                  ALIGN_ATTRIBUTE,
                                                  align);
                            }

                            pane.removeAttribute(
                                    VDXMLConstants.
                                    PSEUDO_HAS_HELP_ZONE_ATTRIBUTE);
                        }

                        pane.removeAttribute(VDXMLConstants.WIDTH_ATTRIBUTE);
                        pane.removeAttribute(VDXMLConstants.HEIGHT_ATTRIBUTE);
                        pane.removeAttribute(VDXMLConstants.ALIGN_ATTRIBUTE);
                        pane.setAttribute(VDXMLConstants.SIZE_ATTRIBUTE,
                                          Integer.toString(
                                                  Integer.parseInt(width) -
                                                  2));
                        pane.setName(VDXMLConstants.NAVIGATION_ELEMENT);
                        navigationPane = pane;
                        // Any dissecting (scrolling) panes found elsehwere
                        // within this page forces a merge of this element
                        // with the scrolling one to create a
                        // {@link VDXMLConstants#DISSECT_TEXTNAVIG_ELEMENT}
                        // and a removal of the navig to prevent invalid VDXML
                        if (scrollingPane != null) {
                            mergeNavigAndDissectingPanes();
                        }
                    } else if (dissectingPane != null) {
                        pane.setName(VDXMLConstants.DISSECT_TEXT_ELEMENT);

                        // Juggle fake dissection elements
                        Element parent = pane.getParent();
                        Node children = parent.getHead();
                        while (children != null) {
                            if (children instanceof Element) {
                                Element element = (Element) children;
                                if (element.getName().equals(VDXMLConstants.
                                           PSEUDO_DISSECTION_CONTENT)) {
                                    // Copy contents across
                                    element.addChildrenToHead(pane);
                                    break;
                                }
                            } else {
                                children = children.getNext();
                            }
                        }
                        pane.replace(parent);

                        // Any navigs found elsehwere within this page forces
                        // a conversion of this element to a
                        // {@link VDXMLConstants#DISSECT_TEXTNAVIG_ELEMENT}
                        // and a removal of the navig to prevent invalid VDXML
                        scrollingPane = pane;
                        if (navigationPane != null) {
                            mergeNavigAndDissectingPanes();
                        }
                    } else {
                        pane.setName(VDXMLConstants.TEXT_BLOCK_ELEMENT);
                    }
                } else {
                    // todo later Investigate that this indeed happens after all calls to this method - I think it does (CK, 20040602)
                    // Pane should be cleaned up by other code
                }
            }

            if (cell != null) {
                // The cell becomes redundant, as does its parent
                cell.setName(null);
                cell.getParent().setName(null);
            }
        }

        /**
         * This merges the two panes (scrolling and navigation) which are set
         * during the conversion to VDXML.  This merge is necessary because of
         * the need to generate valid VDXML and the restriction that only one
         * of {@link VDXMLConstants#NAVIGATION_ELEMENT} and
         * {@link VDXMLConstants#DESELECT_ELEMENT} can appear within the
         * same markup,  The solution is to use a merge of these two elements
         * known as {@link VDXMLConstants#DISSECT_TEXTNAVIG_ELEMENT}.
         *
         */
        private void mergeNavigAndDissectingPanes() {
            if (scrollingPane == null || navigationPane == null) {
                throw new IllegalStateException("Both the scrolling " +
                        "dissection pane and the navigation pane must have " +
                        "been defined to be able to merge them.");
            }

            // Copy the necessary navigation attributes across
            scrollingPane.setName(VDXMLConstants.DISSECT_TEXTNAVIG_ELEMENT);
            String value = navigationPane.getAttributeValue(
                             VDXMLConstants.BACKGROUND_COLOUR_ATTRIBUTE);
            if (value == null) {
                // Force a default value to comply with the DTD
                value = VDXMLColorValue.BLACK.toString();
            }
            scrollingPane.setAttribute(
                    VDXMLConstants.NAVIG_BACKGROUND_ATTRIBUTE, value);
            value = navigationPane.getAttributeValue(
                    VDXMLConstants.TEXT_COLOUR_ATTRIBUTE);
            if (value == null) {
                // Force a default value to comply with the DTD
                value = VDXMLColorValue.WHITE.toString();
            }
            scrollingPane.setAttribute(
                    VDXMLConstants.NAVIG_TEXT_COLOUR_ATTRIBUTE, value);
            scrollingPane.setAttribute(
                    VDXMLConstants.NAVIG_X_ATTRIBUTE,
                    navigationPane.getAttributeValue(VDXMLConstants.X_ATTRIBUTE));
            scrollingPane.setAttribute(
                    VDXMLConstants.NAVIG_Y_ATTRIBUTE,
                    navigationPane.getAttributeValue(VDXMLConstants.Y_ATTRIBUTE));
            scrollingPane.setAttribute(
                    VDXMLConstants.SIZE_ATTRIBUTE,
                    navigationPane.getAttributeValue(VDXMLConstants.SIZE_ATTRIBUTE));
            String character = navigationPane.getAttributeValue(
                    VDXMLConstants.CHARACTER_ATTRIBUTE);
            if (character != null) {
                scrollingPane.setAttribute(VDXMLConstants.CHARACTER_ATTRIBUTE,
                                           character);
            }

            //And now remove the navigation element
            navigationPane.remove();
            navigationPane = null;
        }

        /**
         * Returns the nearest ancestor's value for the named attribute. May
         * return null.
         *
         * @param element   the element to be searched for the given attribute
         * @param attribute the attribute to be searched for
         * @return the value of the "closest" version of the attribute or null
         *         if not found
         */
        protected String getInheritValue(Element element, String attribute) {
            String colour = null;

            while ((colour == null) && (element != null)) {
                colour = element.getAttributeValue(attribute);
                element = element.getParent();
            }

            return colour;
        }

        // javadoc inherited
        public void release() {
            this.pane = null;
            this.cell = null;
            this.container = null;

            if (grid != null) {
                grid.release();
                grid = null;
            }
        }

        // javadoc inherited
        public String toString() {
            StringBuffer toString = new StringBuffer().
                    append("<cell ").append(super.toString()).
                    append(" has-pane='").append(pane != null).
                    append("' has-grid='").append(grid != null).append("'");

            if (pane != null) {
                toString.append("><pane width='").
                        append(pane.getAttributeValue(VDXMLConstants.
                                                 WIDTH_ATTRIBUTE)).
                        append("' height='").
                        append(pane.getAttributeValue(VDXMLConstants.
                                                 HEIGHT_ATTRIBUTE)).
                        append("' border-width='").
                        append(getValue(pane,
                                        VDXMLConstants.PSEUDO_BORDER_ATTRIBUTE)).
                        append("' cell-padding='").
                        append(getPadding()).
                        append("'/></cell>");
            } else if (grid != null) {
                toString.append(">").append(grid.toString()).append("</cell>");
            } else {
                toString.append("/>");
            }

            return toString.toString();
        }

        private class GeneralFixerVisitor
                extends RecursingDOMVisitor {

            private final Element frames;

            private Element containingForm;
            Element latestInputField;
            String formSpecifier;

            public GeneralFixerVisitor(Element frames) {
                this.frames = frames;
                latestInputField = null;
                formSpecifier = null;
            }


            // Javadoc inherited.
            public void visit(Document document) {
                document.forEachChild(this);
            }

            public void visit(Element element) {

                if (VDXMLConstants.FORM_ELEMENT.equals(element.getName())) {
                    // Grab the specifier before visiting children
                    formSpecifier = element.getAttributeValue(
                            VDXMLConstants.ACTION_ATTRIBUTE);

                    // Using the form URL for creation of fragment links
                    if (formSpecifier != null) {
                        // Check for existing parameters so the correct
                        // character is used before appending the name-
                        // value pair for the fragment links when they
                        // are processed.
                        if (formSpecifier.indexOf('?') == -1) {
                            formSpecifier += "?";
                        } else {
                            formSpecifier += "&";
                        }
                    }

                    // Visit the children
                    Element savedContainingForm = containingForm;
                    containingForm = element;
                    element.forEachChild(this);
                    containingForm = savedContainingForm;

                    // Add any next form fragment link
                    String nextURL = null;
                    String nextName = element.getAttributeValue(
                            VDXMLConstants.PSEUDO_NEXT_NAME);
                    String nextValue = element.getAttributeValue(
                            VDXMLConstants.PSEUDO_NEXT_VALUE);
                    if ((nextName != null) && (nextValue != null)) {
                        nextURL = nextName + "=" + nextValue;
                    }
                    if ((nextURL != null) && (latestInputField != null)) {
                        String url = createURL(formSpecifier, nextURL);
                        addFragmentLink(
                                url, latestInputField,
                                VDXMLConstants.NEXT_FUNCTION_STRING,
                                element.getAttributeValue(
                                        VDXMLConstants.METHOD_ATTRIBUTE));
                    }

                    // Tidy up after ourselves and remove pseudo attributes
                    element.removeAttribute(
                            VDXMLConstants.PSEUDO_NEXT_NAME);
                    element.removeAttribute(
                            VDXMLConstants.PSEUDO_NEXT_VALUE);
                    element.removeAttribute(
                            VDXMLConstants.PSEUDO_PREV_NAME);
                    element.removeAttribute(
                            VDXMLConstants.PSEUDO_PREV_VALUE);

                } else if (VDXMLConstants.TEXT_BLOCK_ELEMENT.equals(
                        element.getName()) &&
                        (containingForm != null)) {
                    element.remove();
                    element.insertBefore(containingForm);
                } else if (VDXMLConstants.INPUT_FIELD_ELEMENT.equals(
                        element.getName())) {
                    if (containingForm != null) {
                        // Adding previous form fragment links
                        Element form = containingForm;
                        String prevURL = null;
                        String prevName = element.getAttributeValue(
                                VDXMLConstants.PSEUDO_PREV_NAME);
                        String prevValue = element.getAttributeValue(
                                VDXMLConstants.PSEUDO_PREV_VALUE);
                        if ((prevName != null) && (prevValue != null)) {
                            prevURL = prevName + "=" + prevValue;
                        }
                        if ((prevURL != null) &&
                                (latestInputField == null)) {
                            String url = createURL(formSpecifier, prevURL);
                            addFragmentLink(url, element,
                                            VDXMLConstants.PREVIOUS_FUNCTION_STRING,
                                            form.getAttributeValue(
                                                    VDXMLConstants.METHOD_ATTRIBUTE));
                        }
                    }

                    latestInputField = element;
                } else if (VDXMLConstants.HELP_ZONE_ELEMENT.equals(
                        element.getName())) {
                    // Move the help zone to before any content since
                    // it has to appear before the help elements
                    element.remove();
                    element.addToTail(frames);
                } else {
                    element.forEachChild(this);
                }
            }
        }

        private class GridGenerator
                extends RecursingDOMVisitor {

            private final Element frames;

            private Grid containingGrid;

            private Cell containingCell;

            public GridGenerator(Element frames, Cell cell) {
                this.frames = frames;
                this.containingCell = cell;
            }

            // Javadoc inherited.
            public void visit(Document document) {
                document.forEachChild(this);
            }

            // Javadoc inherited.
            public void visit(Element element) {
                // Handle the various possible options based on the element
                // names and the current generator state
                if (VDXMLConstants.PSEUDO_PANE_ELEMENT.equals(
                        element.getName())) {

                    if (containingCell != null) {
                        containingCell.setPane(element);
                    } else {
                        // This is a top-level pane. No processing needed
                    }
                } else if (VDXMLConstants.PSEUDO_GRID_ELEMENT.equals(
                        element.getName())) {
                    Grid grid = new Grid(factory, element);

                    Grid savedContainingGrid = containingGrid;
                    containingGrid = grid;
                    element.forEachChild(this);
                    containingGrid = savedContainingGrid;

                    if (containingCell != null) {
                        containingCell.setGrid(grid);
                    } else {
                        // This is a top-level grid; should never happen
                        // since we start transformations on a fake
                        // top-level cell
                    }
                } else if (VDXMLConstants.PSEUDO_ROW_ELEMENT.equals(
                        element.getName())) {

                    containingGrid.startRow();

                    element.forEachChild(this);

                } else if (VDXMLConstants.PSEUDO_CELL_ELEMENT.equals(
                        element.getName())) {

                    Cell cell = new Cell(factory, element, containingGrid);
                    containingGrid.addCell(cell);

                    Cell savedContainingCell = containingCell;
                    containingCell = cell;
                    element.forEachChild(this);
                    containingCell = savedContainingCell;

                } else {
                    if (VDXMLConstants.VDXML_ELEMENT.equals(
                            element.getName())) {
                        // Insert the frames placeholder into the document
                        // as the first child of the VDXML
                        frames.addToHead(element);
                    }

                    // Just cascade down; this element is of no interest
                    element.forEachChild(this);
                }
            }
        }
    }

    /**
     * Represents a grid of cells.
     */
    private class Grid extends Entity {
        /**
         * Used to track the current column while generating the Grid/Cell
         * hierarchy. Valid column values are in the range [0..n]. This is
         * pre-incremented before use.
         */
        protected int currentCol = -1;

        /**
         * Used to track the current row while generating the Grid/Cell
         * hierarchy. Valid row values are in the range [0..m]. This is
         * pre-incremented before use.
         */
        protected int currentRow = -1;

        /**
         * The spacing to be used around and between the cells. May be zero.
         */
        protected int spacing = 0;

        /**
         * Used to hold a two-dimensional array of the cells within this grid.
         * Indices are measured ([0..n], [0..m]) - width then height.
         */
        protected TwoDVector cells = new TwoDVector();

        /**
         * References the pseudo grid element for which the grid entity was
         * created. Should never be null.
         */
        protected Element grid;

        /**
         * Initializes the new instance using the given parameters.
         *
         * @param grid   the pseudo grid element for which this entity is
         *               required
         */
        public Grid(DOMFactory factory, Element grid) {
            super(factory);
            this.grid = grid;

            spacing = getValue(grid, VDXMLConstants.PSEUDO_SPACING_ATTRIBUTE);
        }

        /**
         * Must be called when a new row is started while generating the
         * Grid/Cell instance hierarchy.
         */
        public void startRow() {
            currentRow++;
            currentCol = -1;
        }

        /**
         * Must be called when a new cell is found while generating the
         * Grid/Cell instance hierarchy.
         *
         * @param cell the cell to be added at the next available cells index
         */
        public void addCell(Cell cell) {
            currentCol++;

            cells.add(currentCol, currentRow, cell);
        }

        // javadoc unnecessary
        protected Element getGrid() {
            return grid;
        }

        // javadoc inherited
        public void calculateDimensions() {
            final int numRows = cells.getHeight();
            final int numColumns = cells.getWidth();
            Cell cell;
            int maxHeight;
            int maxWidth;
            int totalHeight = 0;
            int totalWidth = 0;

            // Firstly determine all the basic dimensions for all cells and
            // adjust cell heights to match the maximum height found on each
            // row
            for (int row = 0; row < numRows; row++) {
                maxHeight = 0;

                for (int col = 0; col < numColumns; col++) {
                    cell = ((Cell)cells.get(col, row));

                    if (cell != null) {
                        cell.calculateDimensions();

                        if (cell.height > maxHeight) {
                            maxHeight = cell.height;
                        }
                    }
                }

                for (int col = 0; col < numColumns; col++) {
                    cell = ((Cell)cells.get(col, row));

                    if (cell != null) {
                        cell.height = maxHeight;
                    }
                }

                // Add this row height to the total height
                totalHeight += maxHeight;
            }

            // Next, work out the maximum widths for each column and adjust
            // all cells in each column to that width
            for (int col = 0; col < numColumns; col++) {
                maxWidth = 0;

                for (int row = 0; row < numRows; row++) {
                    cell = ((Cell)cells.get(col, row));

                    if (cell != null) {
                        if (cell.width > maxWidth) {
                            maxWidth = cell.width;
                        }
                    }
                }

                for (int row = 0; row < numRows; row++) {
                    cell = ((Cell)cells.get(col, row));

                    if (cell != null) {
                        cell.width = maxWidth;
                    }
                }

                // Add this column width to the total width
                totalWidth += maxWidth;
            }

            // Take spacing into account in both dimensions
            totalHeight += (numRows + 1) * spacing;
            totalWidth += (numColumns + 1) * spacing;

            // Store these dimensions
            width = totalWidth;
            height = totalHeight;
        }

        // javadoc inherited
        public void calculatePosition(int originX, int originY) {
            int offsetX;
            int offsetY = spacing;
            Cell cell;
            int height = 0;
            int width = 0;

            final int numRows = cells.getHeight();
            final int numColumns = cells.getWidth();

            // The grid is positioned at the specified origin
            x = originX;
            y = originY;

            // Determine the positions of all cells taking cell spacing into
            // account
            for (int row = 0; row < numRows; row++) {
                offsetX = spacing;

                for (int col = 0; col < numColumns; col++) {
                    cell = (Cell)cells.get(col, row);

                    if (cell != null) {
                        cell.calculatePosition(originX + offsetX,
                                               originY + offsetY);
                        width = cell.width;
                        height = cell.height;

                        offsetX += width + spacing;
                    }
                }

                offsetY += height + spacing;
            }
        }

        // javadoc inherited
        public void convertToVdxml(
                Element frames) {
            final int numRows = cells.getHeight();
            final int numColumns = cells.getWidth();
            final String backgroundColour = grid.getAttributeValue(
                    VDXMLConstants.BACKGROUND_COLOUR_ATTRIBUTE);
            Cell cell;

            // Firstly convert nested markup
            for (int row = 0; row < numRows; row++) {
                for (int col = 0; col < numColumns; col++) {
                    cell = ((Cell)cells.get(col, row));

                    if (cell != null) {
                        cell.convertToVdxml(frames);
                    }
                }
            }

            // Now do the biz with this markup
            if (backgroundColour != null) {
                // Frames are not needed for grids that don't specify a
                // background colour - colour is inherited from the "parent"
                // grid

                // @todo only generate a frame that is actually needed: handle "inherited" background colour
                Element frame = allocateElement();

                frame.setName(VDXMLConstants.FRAME_ELEMENT);

                frame.setAttribute(VDXMLConstants.X_ATTRIBUTE,
                                   Integer.toString(x));
                frame.setAttribute(VDXMLConstants.Y_ATTRIBUTE,
                                   Integer.toString(y));
                frame.setAttribute(VDXMLConstants.WIDTH_ATTRIBUTE,
                                   Integer.toString(width));
                frame.setAttribute(VDXMLConstants.HEIGHT_ATTRIBUTE,
                                   Integer.toString(height));
                frame.setAttribute(VDXMLConstants.BACKGROUND_COLOUR_ATTRIBUTE,
                                   backgroundColour);
                frame.addToHead(frames);
            }

            // This markup is now redundant
            grid.setName(null);
        }

        // javadoc inherited
        public void release() {
            final int numRows = cells.getHeight();
            final int numColumns = cells.getWidth();
            Cell cell;

            for (int row = 0; row < numRows; row++) {
                for (int col = 0; col < numColumns; col++) {
                    cell = ((Cell)cells.get(col, row));

                    if (cell != null) {
                        cell.release();
                    }
                }
            }

            cells.clear();

            this.cells = null;
            this.grid = null;
        }

        // javadoc inherited
        public String toString() {
            StringBuffer toString = new StringBuffer();

            final int height = cells.getHeight();
            final int width = cells.getWidth();

            toString.append("<grid rows='").append(height).
                    append("' columns='").append(width).
                    append("' cell-spacing='").append(spacing).
                    append("'>");

            for (int row = 0; row < height; row++) {
                toString.append("<row>");

                for (int col = 0; col < width; col++) {
                    toString.append(cells.get(col, row));
                }

                toString.append("</row>");
            }

            toString.append("</grid>");

            return toString.toString();
        }
    }

    /**
     * The transformer used to convert XDIME generated pseudo markup into
     * VDXML.
     */
    private final VDXMLXDIMETransformer xdimeTransformer;

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param xdimeTransformer
     *         the object used to convert XDIME generated pseudo markup into
     *         valid VDXML.
     */
    public VDXMLTransformer(VDXMLXDIMETransformer xdimeTransformer) {
        this.xdimeTransformer = xdimeTransformer;
    }

    // javadoc inherited
    public Document transform(DOMProtocol protocol,
                              Document document) {
        // Create a dummy cell to hold and process the entire layout
        DOMFactory factory = protocol.getDOMFactory();
        Cell startPoint = new Cell(factory, null, null);

        // Do the transformation
        startPoint.transform(document);

        // Be nice to the garbage collector (and make sure we lose references
        // to DOM nodes as these are probably pooled!)
        startPoint.release();
        // As well as being nice this also ensures these values are reset!
        scrollingPane = null;
        navigationPane = null;

        return document;
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Jun-05	8856/1	geoff	VBM:2005062005 Refactoring for XDIMECP: Generate optimised CSS for a DOM.

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Oct-04	5778/1	adrianj	VBM:2004083106 Provide styling engine API

 23-Sep-04	5599/1	geoff	VBM:2004092214 Port VDXML to MCS: port existing protocol code

 04-Jun-04	4483/29	philws	VBM:2004051807 Help zone and input field colour fixes

 04-Jun-04	4495/9	claire	VBM:2004051807 Implemented dissecting panes and ensured elements are created from the DOM pool

 02-Jun-04	4495/7	claire	VBM:2004051807 Null style fix, tidy up form fragmentation, utilise DOMPool when transforming, and some JavaDoc additions

 01-Jun-04	4495/5	claire	VBM:2004051807 Fixing form fragmentation

 01-Jun-04	4483/27	philws	VBM:2004051807 Fix potential memory leak in transformer

 28-May-04	4483/25	philws	VBM:2004051807 Navigation handling

 28-May-04	4495/1	claire	VBM:2004051807 Form fragment links

 28-May-04	4483/22	philws	VBM:2004051807 Basic help working

 28-May-04	4575/2	geoff	VBM:2004051807 Minitel VDXML protocol support (fix underline)

 28-May-04	4483/18	philws	VBM:2004051807 Fix form text block handling

 28-May-04	4483/15	philws	VBM:2004051807 Updates for initial property state for inline and block element processing

 28-May-04	4483/13	philws	VBM:2004051807 Fix pane <-> input field mapping

 27-May-04	4483/11	philws	VBM:2004051807 Better colour handling

 27-May-04	4483/9	philws	VBM:2004051807 Initial end-to-end layout rendering

 27-May-04	4483/7	philws	VBM:2004051807 Functional layout transformer

 25-May-04	4483/3	philws	VBM:2004051807 Initial VDXML transformer implementation

 ===========================================================================
*/
