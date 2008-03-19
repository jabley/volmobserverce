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
package com.volantis.mcs.themes;

import com.volantis.mcs.model.validation.Validatable;
import com.volantis.mcs.model.property.PropertyIdentifier;

import java.util.List;

/**
 * This interface associates a list of selectors with a set of style policies.
 *
 * @mock.generate
 */
public interface Rule extends ThemeVisitorAcceptor, Validatable {
    public PropertyIdentifier SELECTORS =
        new PropertyIdentifier(Rule.class, "selectors");
    public PropertyIdentifier STYLE_PROPERTIES =
                new PropertyIdentifier(Rule.class, "styleProperties");

    /**
     * Set the value of the selectors property.
     *
     * @param selectors The new value of the selectors property.
     */
    void setSelectors(List selectors);

    /**
     * Get the value of the selectors property.
     *
     * @return The value of the selectors property.
     */
    List getSelectors();

    /**
     * Set the value of the properties property.
     *
     * @param properties The new value of the properties property.
     */
    void setProperties(StyleProperties properties);

    /**
     * Get the value of the properties property.
     *
     * @return The value of the properties property.
     */
    StyleProperties getProperties();

    /**
     * Visit all the selectors in the rule.
     *
     * @param visitor The visitor to invoke.
     */
    void visitSelectors(SelectorVisitor visitor);
}
