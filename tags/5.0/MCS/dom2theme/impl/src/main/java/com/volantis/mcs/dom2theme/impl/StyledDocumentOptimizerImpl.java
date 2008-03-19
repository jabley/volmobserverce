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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.dom2theme.impl;

import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.RecursingDOMVisitor;
import com.volantis.mcs.dom2theme.StyledDocumentOptimizer;
import com.volantis.mcs.dom2theme.impl.model.OutputStyledElement;
import com.volantis.mcs.dom2theme.impl.model.OutputStyledElementIteratee;
import com.volantis.mcs.dom2theme.impl.model.OutputStyledElementList;
import com.volantis.mcs.dom2theme.impl.model.OutputStyles;
import com.volantis.mcs.dom2theme.impl.model.PseudoStylePath;
import com.volantis.mcs.dom2theme.impl.model.PseudoStylePathIteratee;
import com.volantis.mcs.dom2theme.impl.optimizer.StyledDOMOptimizer;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.PropertyValueIteratee;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.shared.iteration.IterationAction;
import com.volantis.styling.PseudoStyleEntity;
import com.volantis.styling.PseudoStyleEntityIteratee;
import com.volantis.styling.Styles;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.values.MutablePropertyValues;

/**
 * todo sort out the names as this optimizes, not normalizes.
 */
public class StyledDocumentOptimizerImpl
        extends RecursingDOMVisitor
        implements StyledDocumentOptimizer, OutputStyledElementIteratee,
        PseudoStylePathIteratee, PseudoStyleEntityIteratee,
        PropertyValueIteratee {

    private final StyledDOMOptimizer styledDOMOptimizer;
    private final StylingFactory stylingFactory;
    private Styles styles;
    private Styles nestedStyles;
    private OutputStyles outputStyles;
    private MutablePropertyValues propertyValues;

    public StyledDocumentOptimizerImpl(
            StyledDOMOptimizer styledDOMOptimizer) {
        this.styledDOMOptimizer = styledDOMOptimizer;
        stylingFactory = StylingFactory.getDefaultInstance();
    }

    public void optimizeDocument(Document document) {
        OutputStyledElementList list = styledDOMOptimizer.optimize(document);

        // Visit all the elements in the DOM and discard the styles from the
        // element.
        document.accept(this);

        list.iterate(this);
    }

    public IterationAction next(OutputStyledElement outputElement) {

        outputStyles = outputElement.getStyles();

        if (outputStyles == null) {
            styles = null;
        } else {
            // Create a normal Styles object and the populate it from the
            // output styles.
            styles = stylingFactory.createStyles(null);
            outputStyles.iterate(this);
        }

        // Now update the element.
        Element element = outputElement.getElement();
        if (styles != null) {
            element.setStyles(styles);
        }

        return IterationAction.CONTINUE;
    }

    public void next(PseudoStylePath pseudoPath) {
        nestedStyles = styles;

        // Iterate over the entities in the path drilling down to the
        // appropriate nested styles reference.
        pseudoPath.iterate(this);

        // Get the style properties associated with the path.
        MutableStyleProperties styleProperties =
                outputStyles.getPathProperties(pseudoPath);

        // Get the property values for the styles that need updating.
        propertyValues = nestedStyles.getPropertyValues();

        styleProperties.iteratePropertyValues(this);
    }

    public IterationAction next(PseudoStyleEntity entity) {
        nestedStyles = nestedStyles.getNestedStyles(entity);
        return IterationAction.CONTINUE;
    }

    public IterationAction next(PropertyValue propertyValue) {
        propertyValues.setComputedAndSpecifiedValue(
                propertyValue.getProperty(), propertyValue.getValue());
        return IterationAction.CONTINUE;
    }


    // Javadoc inherited.
    public void visit(Document document) {
        document.forEachChild(this);
    }

    // Javadoc inherited.
    public void visit(Element element) {
        element.setStyles(null);
        element.forEachChild(this);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 ===========================================================================
*/
