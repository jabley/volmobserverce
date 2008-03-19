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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.NodeIteratee;
import com.volantis.mcs.dom.RecursingDOMVisitor;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StyleValues;
import com.volantis.mcs.themes.properties.TextAlignKeywords;
import com.volantis.shared.iteration.IterationAction;
import com.volantis.styling.NestedStyles;
import com.volantis.styling.NestedStylesIteratee;
import com.volantis.styling.Styles;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.MutablePropertyValues;

public class DeferredInheritTransformer
        extends RecursingDOMVisitor
        implements DOMTransformer, NodeIteratee, NestedStylesIteratee {

    /**
     * The text-align property.
     */
    private static final StyleProperty TEXT_ALIGN =
            StylePropertyDetails.TEXT_ALIGN;

    private final StyleValues rootStyleValues;
    private Element contentRoot;

    public DeferredInheritTransformer() {
        rootStyleValues = StylePropertyDetails.getDefinitions()
                .getStandardDetailsSet().getRootStyleValues();
    }

    public Document transform(DOMProtocol protocol, Document document) {
        contentRoot = document.getContentRoot();
        document.forEachChild(this);
        return document;
    }

    // Javadoc inherited.
    public void visit(Element element) {

        // Before processing the children.
        Styles styles = element.getStyles();
        boolean visitChildren;
        if (styles == null) {
            // If the element is the root of the document then visit its
            // children even if it does not have styles as the <html> element
            // does not have styles. Otherwise if it does not have styles then
            // none of the children should have styles so there is no point
            // in visiting them.
            visitChildren = (element.getParent() == contentRoot);
        } else {
            StyleValues parentValues = getParentValues(element);

            // Before processing the children perform any deferred inheritance.
            transformStyles(styles, parentValues);

            // Visit the children.
            visitChildren = true;
        }

        if (visitChildren) {
            // Process the children.
            element.forEachChild(this);
        }
    }

    private void transformStyles(Styles styles, StyleValues parentValues) {

        MutablePropertyValues inputValues = styles.findPropertyValues();
        StyleValue value = inputValues.getStyleValue(TEXT_ALIGN);
        if (value == TextAlignKeywords._INTERNAL_DEFERRED_INHERIT) {
            // Inherit the value from the parent.
            StyleValue parent = parentValues.getStyleValue(TEXT_ALIGN);
            inputValues.setComputedValue(TEXT_ALIGN, parent);
            inputValues.markAsUnspecified(TEXT_ALIGN);
        }

        // Process nested styles.
        styles.iterate(this);
    }

    private StyleValues getParentValues(Element element) {
        Element parent = element.getParent();
        StyleValues parentValues = null;
        if (parent != null) {
            Styles parentStyles = parent.getStyles();
            if (parentStyles != null) {
                parentValues = parentStyles.findPropertyValues();
            }
        }
        if (parentValues == null) {
            parentValues = rootStyleValues;
        }
        return parentValues;
    }

    public IterationAction next(NestedStyles nestedStyles) {

        StyleValues parentStyles =
                nestedStyles.getContainer().getPropertyValues();

        transformStyles(nestedStyles, parentStyles);
        return IterationAction.CONTINUE;
    }
}
