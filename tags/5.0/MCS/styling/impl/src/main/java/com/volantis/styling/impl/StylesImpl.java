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

package com.volantis.styling.impl;

import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.styling.NestedStyles;
import com.volantis.styling.NestedStylesIteratee;
import com.volantis.styling.PseudoStyleEntity;
import com.volantis.styling.Styles;
import com.volantis.styling.StatefulPseudoClass;
import com.volantis.styling.impl.values.MutablePropertyValuesImpl;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.styling.values.PropertyValues;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Implementation of {@link Styles}.
 */
public class StylesImpl
        implements NestedStyles {

    /**
     * The style entity with which this is associated its container.
     *
     * <p>This is only set if this is contained within another styles
     * object.</p>
     */
    private final PseudoStyleEntity pseudoStyleEntity;

    /**
     * The container of this, may be null.
     */
    private final StylesImpl container;

    /**
     * The set of property values associated with this styles.
     */
    private MutablePropertyValues values;

    private List nestedStylesList;

    public StylesImpl() {
        this(null, null);
    }

    public StylesImpl(PseudoStyleEntity styleEntity,
                      StylesImpl container) {
        this(styleEntity, container, null);
    }

    public StylesImpl(
            PseudoStyleEntity styleEntity, StylesImpl container,
            PropertyValues propertyValues) {

        this.pseudoStyleEntity = styleEntity;
        this.container = container;

        if (propertyValues != null) {
            values = new MutablePropertyValuesImpl(propertyValues);
        }
    }

    // Javadoc inherited.
    public MutablePropertyValues getPropertyValues() {
        if (values == null) {
            this.values = new MutablePropertyValuesImpl(
                    StylePropertyDetails.getDefinitions());
        }
        return values;
    }

    public MutablePropertyValues findPropertyValues() {
        return values;
    }

    // Javadoc inherited.
    public NestedStyles findNestedStyles(PseudoStyleEntity entity) {
        if (nestedStylesList == null) {
            return null;
        } else {
            return findStyles(entity);
        }
    }

    public NestedStyles findNestedStyles(StatefulPseudoClass pseudoClass) {
        return findNestedStyles(pseudoClass.getSet());
    }

    // Javadoc inherited.
    public NestedStyles getNestedStyles(PseudoStyleEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("entity cannot be null");
        }

        NestedStyles styles;
        if (nestedStylesList == null) {
            nestedStylesList = new ArrayList();
            styles = null;
        } else {
            styles = findStyles(entity);
        }

        if (styles == null) {
            styles = new StylesImpl(entity, this);
            nestedStylesList.add(styles);
        }

        return styles;
    }

    public NestedStyles getNestedStyles(StatefulPseudoClass pseudoClass) {
        return getNestedStyles(pseudoClass.getSet());
    }

    // Javadoc inherited.
    public NestedStyles removeNestedStyles(PseudoStyleEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("entity cannot be null");
        }

        NestedStyles styles = null;
        if (nestedStylesList != null) {
            styles = findStyles(entity);
        }

        if (styles != null) {
            nestedStylesList.remove(styles);
        }

        return styles;
    }

    public NestedStyles removeNestedStyles(StatefulPseudoClass pseudoClass) {
        return removeNestedStyles(pseudoClass.getSet());
    }

    public PseudoStyleEntity getPseudoStyleEntity() {
        return pseudoStyleEntity;
    }

    /**
     * Find the nested styles associated with the specified entity.
     *
     * @param entity The entity whose associated style is to be found.
     *
     * @return The nested styles, or null if they could not be found.
     */
    private NestedStyles findStyles(PseudoStyleEntity entity) {
        if (nestedStylesList != null) {
            for (int i = 0; i < nestedStylesList.size(); i++) {
                StylesImpl styles = (StylesImpl) nestedStylesList.get(i);
                if (styles.pseudoStyleEntity.equals(entity)) {
                    return styles;
                }
            }
        }

        return null;
    }

    // Javadoc inherited.
    public Iterator iterator() {
        if (nestedStylesList != null) {
            return nestedStylesList.iterator();
        }

        return Collections.EMPTY_LIST.iterator();
    }

    public void iterate(NestedStylesIteratee iteratee) {
        if (nestedStylesList != null) {
            for (int i = 0; i < nestedStylesList.size(); i++) {
                NestedStyles styles = (NestedStyles) nestedStylesList.get(i);
                iteratee.next(styles);
            }
        }
    }

    // Javadoc inherited.
    public Styles getContainer() {
        return container;
    }

    public String toString() {
        return "Styles(values="+values+
            ", nestedStylesList="+nestedStylesList+
            ", pseudoStyleEntity="+ pseudoStyleEntity+")";
    }

    // Javadoc inherited.
    public Styles copy() {
        StylesImpl newStyles = new StylesImpl(pseudoStyleEntity, container);
        if (values != null) {
            newStyles.values = new MutablePropertyValuesImpl(values);
        }
        if (nestedStylesList != null) {
            newStyles.nestedStylesList = new ArrayList();
            for (int i = 0; i < nestedStylesList.size(); i++) {
                Styles styles = (Styles) nestedStylesList.get(i);
                newStyles.nestedStylesList.add(styles.copy());
            }
        }

        return newStyles;
    }

    public boolean hasNestedStyles() {
        return nestedStylesList != null && !nestedStylesList.isEmpty();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10527/5	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 05-Dec-05	10527/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 05-Dec-05	10512/2	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10505/8	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/4	pduffin	VBM:2005111405 Massive changes for performance

 29-Nov-05	10505/4	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (3)

 18-Nov-05	10347/1	pduffin	VBM:2005111405 Removed some unnecessary usages of setSpecifiedValue

 29-Nov-05	10480/1	pduffin	VBM:2005070711 Merged changes from main trunk

 29-Nov-05	10478/1	pduffin	VBM:2005070711 Fixed merge conflicts

 11-Nov-05	10253/1	emma	VBM:2005110902 Fixing two layout rendering bugs

 05-Oct-05	9440/2	schaloner	VBM:2005070711 Added marker pseudo-element support

 29-Nov-05	10478/1	pduffin	VBM:2005070711 Fixed merge conflicts

 11-Nov-05	10282/1	emma	VBM:2005110902 Forward port: fixing two layout rendering bugs

 11-Nov-05	10253/1	emma	VBM:2005110902 Fixing two layout rendering bugs
 05-Oct-05	9440/2	schaloner	VBM:2005070711 Added marker pseudo-element support

 06-Sep-05	9413/1	schaloner	VBM:2005070406 Implemented before and after pseudo-element support

 22-Aug-05	9184/1	pabbott	VBM:2005080508 Move CSS eumlator to post process step

 15-Jul-05	9067/3	geoff	VBM:2005071415 More refactoring for: XDIMECP: Generate optimised CSS for a DOM.

 20-Jun-05	8483/1	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 08-Jun-05	7997/1	pduffin	VBM:2005050324 Added basic styling implementation, enhancements to mock and ported tests that depended on dynamic mock to use the new generator

 ===========================================================================
*/
