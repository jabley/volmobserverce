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

import com.volantis.mcs.devices.policy.types.PolicyType;
import com.volantis.mcs.devices.policy.types.StructurePolicyType;
import com.volantis.shared.metadata.type.immutable.ImmutableMetaDataType;
import com.volantis.shared.metadata.type.mutable.MutableFieldDefinition;
import com.volantis.shared.metadata.type.mutable.MutableStructureType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Default implementation of {@link StructurePolicyType}.
 */ 
public class DefaultStructurePolicyType
        extends DefaultPolicyType
        implements StructurePolicyType {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    /**
     * Flag to indicate if initialisation is complete.
     */ 
    private boolean complete;

    /**
     * The map of field names to types for this structure. 
     * <p>
     * This will be a normal map before {@link #complete} has been called, 
     * and an unmodifiable map afterwards.
     */ 
    private Map fieldTypes = new HashMap();

    /**
     * Add a field name to type mapping to the structure.
     * <p>
     * Note: This method may only be called before {@link #complete}.
     * 
     * @param name the name of the field
     * @param type the type of the field
     */ 
    public void addFieldType(String name, PolicyType type) {
        ensureIncomplete();

        fieldTypes.put(name, type);
    }
    
    /**
     * Mark the structure as having had it's initialisation completed.
     * <p>
     * Note: This method must be called after {@link #addFieldType} and before 
     * {@link #getFieldTypes}. 
     */ 
    public void complete() {
        ensureIncomplete();
        
        fieldTypes = Collections.unmodifiableMap(fieldTypes);

        complete = true;
    }
    
    /**
     * Note: this method must be called after {@link #complete}.
     */ 
    public Map getFieldTypes() {
        ensureComplete();
        
        return fieldTypes;
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

        MutableStructureType structureType = TYPE_FACTORY.createStructureType();
        Set fields = structureType.getMutableFields();

        for (Iterator i = fieldTypes.entrySet().iterator(); i.hasNext();) {
            Map.Entry field = (Map.Entry) i.next();
            String fieldName = (String) field.getKey();
            InternalPolicyType fieldType
                    = (InternalPolicyType) field.getValue();

            MutableFieldDefinition fieldDefinition =
                    TYPE_FACTORY.createFieldDefinition(fieldName);
            fieldDefinition.setType(fieldType.createMetaDataType());
            fields.add(fieldDefinition);
        }

        return (ImmutableMetaDataType) structureType.createImmutable();
    }

    // Javadoc inherited.
    public String toString() {
        return "[DefaultStructurePolicyType: " +
                "fieldTypes=" + fieldTypes + "]";
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 17-Jan-05	6707/1	pduffin	VBM:2005011710 Refactored device repository API to fix couple of performance and code duplication issues. Added support for retrieving device policy values as meta data

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 30-Jul-04	4993/1	geoff	VBM:2004072804 Public API for Device Repository: Final cleanup and javadoc

 28-Jul-04	4956/1	geoff	VBM:2004072305 Public API for Device Repository: metadata support for import tool (finalise)

 23-Jul-04	4945/1	geoff	VBM:2004072205 Public API for Device Repository: Common Metadata Infrastructure

 ===========================================================================
*/
