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
 * $Header: /src/voyager/com/volantis/mcs/themes/Rule.java,v 1.4 2002/08/22 15:32:41 aboyd Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 26-Apr-02    Doug            VBM:2002040803 - Created as part of
 *                              implementation of new internal themes structure
 * 27-Jun-02    Doug            VBM:2002052102 - Added the methods clone(),
 *                              equals() and hashcode().
 * 22-Aug-02    Allan           VBM:2002082104 - Removed printlns from
 *                              equals().
 * 03-Jun-03    Allan           VBM:2003060301 - ObjectHelper and
 *                              UndeclaredThrowableException moved to
 *                              Synergetics.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.themes.impl;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.model.path.Step;
import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.themes.Rule;
import com.volantis.mcs.themes.StyleProperties;
import com.volantis.mcs.themes.SelectorVisitor;
import com.volantis.mcs.themes.Selector;
import com.volantis.mcs.themes.ThemeVisitor;
import com.volantis.mcs.themes.ThemeVisitorAcceptor;
import com.volantis.mcs.themes.PropertyValueIteratee;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.shared.iteration.IterationAction;
import com.volantis.synergetics.ObjectHelper;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.List;

/**
 * Default Rule implementation
 */
public class RuleImpl implements Rule {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(RuleImpl.class);

    /**
     * The list of selectors.
     */
    private List selectors;

    /**
     * The set of StyleProperties.
     */
    private StyleProperties properties;

    /**
     * Create a new <code>Rule</code>.
     */
    public RuleImpl() {
    }

    public void setSelectors(List selectors) {
        this.selectors = selectors;
    }

    public List getSelectors() {
        return selectors;
    }

    public void setProperties(StyleProperties properties) {
        this.properties = properties;
    }

    public StyleProperties getProperties() {
        return properties;
    }

    public void visitSelectors(SelectorVisitor visitor) {
        List selectorList = getSelectors();
        if (selectorList != null) {
            int size = selectorList.size();
            for (int i = 0; i < size; i++) {
                Selector selector = (Selector) selectorList.get(i);
                selector.accept(visitor);
            }
        }
    }

    // Javadoc inherited from super class.
    public boolean equals(Object o) {
        if (null != o && o.getClass().equals(getClass())) {
            RuleImpl r = (RuleImpl) o;
            return (ObjectHelper.equals(properties, r.properties) &&
                    ObjectHelper.equals(selectors, r.selectors));
        }
        return false;

    }

    // Javadoc inherited from super class.
    public int hashCode() {
        return
                ObjectHelper.hashCode(selectors) +
                ObjectHelper.hashCode(properties);
    }

    // Javadoc inherited from interface
    public void accept(ThemeVisitor visitor) {
        boolean traverseChildren = visitor.visit(this);

        if (traverseChildren) {

            List children = getSelectors();

            if (children != null) {
                for (int i = 0; i < children.size(); i++) {
                    Object o = children.get(i);
                    if (o instanceof ThemeVisitorAcceptor) {
                        ThemeVisitorAcceptor child = (ThemeVisitorAcceptor) o;
                        child.accept(visitor);
                    } else {
                        logger.error("not-instanceof-themevisitoracceptor", o);
                        throw new IllegalStateException();
                    }
                }
            }

            if (properties != null) {
                if (properties instanceof ThemeVisitorAcceptor) {
                    ThemeVisitorAcceptor child = (ThemeVisitorAcceptor) properties;
                    child.accept(visitor);
                } else {
                    logger.error("not-instanceof-themevisitoracceptor",
                            properties);
                    throw new IllegalStateException();
                }
            }

        }

    }

    // Javadoc inherited.
    public void validate(final ValidationContext context) {

        Step step;
        // iterate over the selectors and validate them

        step = context.pushPropertyStep(SELECTORS);
        if (selectors != null && !selectors.isEmpty()) {
            for (int i = 0; i < selectors.size(); i++) {
                Selector selector = (Selector) selectors.get(i);

                Step indexedStep = context.pushIndexedStep(i);
                selector.validate(context);
                context.popStep(indexedStep);
            }
        }
        context.popStep(step);

        // validate the style properties
        if (properties != null) {
            step = context.pushPropertyStep(STYLE_PROPERTIES);
            properties.iteratePropertyValues(new PropertyValueIteratee() {
                public IterationAction next(PropertyValue propertyValue) {
                    propertyValue.validate(context);
                    return IterationAction.CONTINUE;
                }
            });
            context.popStep(step);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-05	10505/9	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/3	pduffin	VBM:2005111405 Massive changes for performance

 29-Nov-05	10505/5	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0

 18-Nov-05	10347/1	pduffin	VBM:2005111405 Performance optimizations on the styling engine

 14-Nov-05	10287/3	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 11-Nov-05	10199/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.
 01-Nov-05	9888/1	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 30-Oct-05	9992/4	emma	VBM:2005101811 Adding new style property validation

 05-Sep-05	9407/3	pduffin	VBM:2005083007 Removed old themes model

 30-Aug-05	9407/1	pduffin	VBM:2005083007 Added SelectorVisitor

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 15-Jul-05	9067/3	geoff	VBM:2005071415 More refactoring for: XDIMECP: Generate optimised CSS for a DOM.

 09-Jun-05	8552/1	pabbott	VBM:2005051902 Version 1 of JIBX implementation

 24-May-05	8329/2	pabbott	VBM:2005051901 New vistitor pattern for Themes

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Oct-04	5862/1	tom	VBM:2004101909 refactored SelectorVisitor and Selector method names to be inline with Visitor Design Pattern

 12-Oct-04	5498/5	tom	VBM:2004082410 Added CSS2.1 Specificity Generation

 08-Sep-04	5398/1	tom	VBM:2004082012 added separateImportantAndNormalValues()as part of normalize()

 23-Mar-04	3555/1	allan	VBM:2004032205 Patch performance fixes from MCS 3.0GA

 22-Mar-04	3512/1	allan	VBM:2004032205 MCS performance enhancements.

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
