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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.themes.impl;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.model.path.Step;
import com.volantis.mcs.model.property.PropertyIdentifier;
import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.mcs.model.validation.ValidationContext;
import com.volantis.mcs.themes.AttributeSelector;
import com.volantis.mcs.themes.AttributeSelectorActionEnum;
import com.volantis.mcs.themes.SelectorVisitor;
import com.volantis.mcs.themes.constraints.Constraint;
import com.volantis.mcs.themes.impl.parsing.AttributeSelectorParser;
import com.volantis.synergetics.localization.MessageLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Basic implementation of the {@link AttributeSelector} interface.
 */
public class DefaultAttributeSelector
        extends AbstractSelector
        implements AttributeSelector {

    /**
     * Used to identify the name property of this class when logging
     * validation errors.
     */
    public static final PropertyIdentifier NAME =
            new PropertyIdentifier(AttributeSelector.class, "name");

   /**
     * Used to identify the name property of this class when logging
     * validation errors.
     */
    public static final PropertyIdentifier ACTION =
           new PropertyIdentifier(AttributeSelector.class, "action");

   /**
     * Used to identify the name property of this class when logging
     * validation errors.
     */
    public static final PropertyIdentifier VALUE =
           new PropertyIdentifier(AttributeSelector.class, "value");

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(DefaultAttributeSelector.class);

    /**
     * Used to retrieve localized messages.
     */
    private static final MessageLocalizer MESSAGE_LOCALIZER =
            LocalizationFactory.createMessageLocalizer(
                    DefaultAttributeSelector.class);

    /**
     * The name of the attribute to match.
     */
    private String name;

    private Constraint constraint;

    /**
     * The namespace of the attribute.
     */
    private String namespacePrefix;

    // Javadoc inherited
    public String getName() {
        return name;
    }

    // Javadoc inherited
    public void setName(String newName) {
        name = newName;
    }

    // Javadoc inherited
    public String getNamespacePrefix() {
        return namespacePrefix;
    }

    // Javadoc inherited
    public void setNamespacePrefix(String newNamespacePrefix) {
        namespacePrefix = newNamespacePrefix;
    }

    public Constraint getConstraint() {
        return constraint;
    }

    public void setConstraint(Constraint constraint) {
        this.constraint = constraint;
    }

    public void setConstraint(AttributeSelectorActionEnum action,
            String value) {
        try {
            constraint = AttributeSelectorActionEnum.createConstraint(action);
            if (constraint.requiresValue()) {
                constraint.setValue(value);
            }
        } catch (IllegalAccessException e) {
           LOGGER.error(MESSAGE_LOCALIZER.format("invalid-constraint", action));
        } catch (InstantiationException e) {
            LOGGER.error(MESSAGE_LOCALIZER.format("invalid-constraint", action));
        }
    }

    // Javadoc inherited.
    public void validate(ValidationContext context) {

        Step step;

        // Requirement 1: The selector has a name
        step = context.pushPropertyStep(NAME);
        if (name == null || name.trim().length() == 0) {
            context.addDiagnostic(this, DiagnosticLevel.ERROR,
                     context.createMessage("theme-attribute-selector-no-name"));
        } else {
            // Requirement 2: The attribute name is valid
            // @todo later Determine what 'valid' means for an attribute name - legal name, or valid attribute in XDIME (possibly taking into account the ClassSelector)
        }
        context.popStep(step);

        // Requirement 3: The selector has a constraint
        step = context.pushPropertyStep(ACTION);
        if (constraint == null) {
            context.addDiagnostic(this, DiagnosticLevel.ERROR,
                    context.createMessage(
                            "theme-attribute-selector-no-action"));
        }
        // Requirement 4: The constraint is valid.
        constraint.validate(context);

        context.popStep(step);
    }

    // Javadoc inherited
    public String toString() {
        return new AttributeSelectorParser().objectToText(this);
    }

    // Javadoc inherited
    public boolean equals(Object o) {
        boolean equal = false;
        if (o != null && o.getClass() == DefaultAttributeSelector.class) {
            DefaultAttributeSelector other = (DefaultAttributeSelector) o;
            equal = (name == null ? other.name == null : name.equals(other.name)) &&
                    (namespacePrefix == null ? other.namespacePrefix == null : namespacePrefix.equals(other.namespacePrefix)) &&
                    (constraint == null ? other.constraint == null : constraint.equals(other.constraint));
        }
        return equal;
    }

    // Javadoc inherited
    public int hashCode() {
        int hash = 171305;
        hash = hash * 613 + (constraint == null ? 0 : constraint.hashCode());
        hash = hash * 613 + (name == null ? 0 : name.hashCode());
        hash = hash * 613 + (namespacePrefix == null ? 0 : namespacePrefix.hashCode());
        return hash;
    }

    // Javadoc inherited.
    public void accept(SelectorVisitor visitor) {
        visitor.visit(this);
    }

    public Object copy() {
        AttributeSelector copy = new DefaultAttributeSelector();
        copy.setConstraint(constraint);
        copy.setName(name);
        copy.setNamespacePrefix(namespacePrefix);
        return copy;
    }
}
/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10374/1	emma	VBM:2005111705 Interim commit

 16-Nov-05	10341/1	pduffin	VBM:2005111410 Ported changes forward from MCS 3.5

 16-Nov-05	10315/2	pduffin	VBM:2005111410 Added support for copying model objects

 14-Nov-05	10287/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.

 11-Nov-05	10199/1	pduffin	VBM:2005110413 Fixed issues mapping diagnostic messages from model to proxies. Also added document, line and column numbers into the messages created when validating models when loading them at runtime.
 01-Nov-05	9888/1	pduffin	VBM:2005101811 Committing new user interface changes that have been ported forward from 3.5

 31-Oct-05	9992/3	emma	VBM:2005101811 Adding new style property validation

 30-Oct-05	9992/1	emma	VBM:2005101811 Adding new style property validation

 05-Sep-05	9407/5	pduffin	VBM:2005083007 Removed old themes model

 31-Aug-05	9407/3	pduffin	VBM:2005083007 Changed layout style sheet builder over to using the new model, added support for nth child

 30-Aug-05	9407/1	pduffin	VBM:2005083007 Added SelectorVisitor

 21-Jul-05	8713/1	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 21-Jul-05	8713/3	adrianj	VBM:2005060902 Enhanced GUI for creating new selectors

 ===========================================================================
*/
