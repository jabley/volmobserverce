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
package com.volantis.devrep.repository.impl.devices.policy.types;

import com.volantis.mcs.devices.policy.types.SelectionPolicyType;
import com.volantis.shared.metadata.type.constraint.mutable.MutableEnumeratedConstraint;
import com.volantis.shared.metadata.type.immutable.ImmutableMetaDataType;
import com.volantis.shared.metadata.type.mutable.MutableStringType;
import com.volantis.shared.metadata.value.mutable.MutableStringValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Default implementation of {@link SelectionPolicyType}.
 */ 
public class DefaultSelectionPolicyType
        extends DefaultPolicyType
        implements SelectionPolicyType {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    /**
     * Flag to indicate if initialisation is complete.
     */ 
    private boolean complete;

    /**
     * The list of keywords for this selection. 
     * <p>
     * This will be a normal list before {@link #complete} has been called, 
     * and an unmodifiable list afterwards.
     */ 
    private List keywords = new ArrayList();

    /**
     * Add a keyword to the selection.
     * <p>
     * Note: This method may only be called before {@link #complete}.
     * 
     * @param keyword the keyword to add.
     */ 
    public void addKeyword(String keyword) {
        
        ensureIncomplete();
        keywords.add(keyword);
    }

    /**
     * Mark the selection as having had it's initialisation completed.
     * <p>
     * Note: This method must be called after {@link #addKeyword} and before 
     * {@link #getKeywords}. 
     */ 
    public void complete() {
        
        ensureIncomplete();
        keywords = Collections.unmodifiableList(keywords);
        complete = true;
    }
    
    /**
     * Note: this method must be called after {@link #complete}.
     */ 
    public List getKeywords() {
        
        ensureComplete();
        return keywords;
    }

    /**
     * Throws a runtime exception if this type's initialisation has not been
     * completed.
     */
    private void ensureComplete() {
        
        if (!complete) {
            throw new IllegalStateException();
        }
    }
    
    /**
     * Throws a runtime exception if this type's initialisation has been
     * completed.
     */
    private void ensureIncomplete() {
        
        if (complete) {
            throw new IllegalStateException();
        }
    }

    // Javadoc inherited.
    public ImmutableMetaDataType createMetaDataType() {
        ensureComplete();

        // Create a string type whose enumerated constraint contains the
        // keyword values.
        MutableStringType stringType = TYPE_FACTORY.createStringType();

        MutableEnumeratedConstraint enumeratedConstraint
                = CONSTRAINT_FACTORY.createEnumeratedConstraint();
        List allowableValues = enumeratedConstraint.getMutableEnumeratedValues();

        for (Iterator i = keywords.iterator(); i.hasNext();) {
            String keyword = (String) i.next();
            MutableStringValue value = VALUE_FACTORY.createStringValue();
            value.setValue(keyword);
            allowableValues.add(value);
        }

        // Add the constraint.
        stringType.setEnumeratedConstraint(enumeratedConstraint);

        return (ImmutableMetaDataType) stringType.createImmutable();
    }

    // Javadoc inherited.
    public String toString() {
        return "[DefaultSelectionPolicyType: " +
                "keywords=" + keywords + "]";
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 19-Jan-05	6686/1	pduffin	VBM:2005010506 Completed code to read and write XML versions of the resource components, asset and templates. Also, fixed a few problems with the implementation of the MetaData API

 17-Jan-05	6707/1	pduffin	VBM:2005011710 Refactored device repository API to fix couple of performance and code duplication issues. Added support for retrieving device policy values as meta data

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 30-Jul-04	4993/1	geoff	VBM:2004072804 Public API for Device Repository: Final cleanup and javadoc

 28-Jul-04	4956/1	geoff	VBM:2004072305 Public API for Device Repository: metadata support for import tool (finalise)

 23-Jul-04	4945/1	geoff	VBM:2004072205 Public API for Device Repository: Common Metadata Infrastructure

 ===========================================================================
*/
