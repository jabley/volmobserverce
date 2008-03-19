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

package com.volantis.devrep.repository.impl.devices.policy.values;

import com.volantis.devrep.repository.api.devices.policy.values.InternalPolicyValue;
import com.volantis.mcs.devices.policy.values.SimplePolicyValue;
import com.volantis.mcs.devices.policy.values.StructurePolicyValue;
import com.volantis.shared.metadata.value.immutable.ImmutableMetaDataValue;
import com.volantis.shared.metadata.value.mutable.MutableStructureValue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * This class provides the default implementation of a Structure Policy Value.
 */
public class DefaultStructurePolicyValue
        extends DefaultCompositePolicyValue
        implements StructurePolicyValue {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    /**
     * The map of field names to types for this structure.
     * <p>
     * This will be a normal map before {@link #complete} has been called,
     * and an unmodifiable map afterwards.
     */
    protected Map fieldMap = new HashMap();

    /**
     * The value as a string with values output as [key=value] [key=value] ...
     */
    protected String valueString = null;

    /**
     * Initialize a new instance of this immutable value.  Any necessary
     * conversions on the parameter will be done as this object is
     * constructed.
     */
    public DefaultStructurePolicyValue() {
    }

    /**
     * Add a field name and value mapping to the structure.
     * <strong>Note:</strong> This method may only be called before
     * {@link #complete}.
     *
     * @param name  The name of the field
     * @param value The value of the field
     */
    public void addField(String name, SimplePolicyValue value) {
        ensureIncomplete();

        fieldMap.put(name, value);
    }

    // JavaDoc inherited
    protected void complete() {
        ensureIncomplete();
        fieldMap = Collections.unmodifiableMap(fieldMap);
        complete = true;
    }

    // JavaDoc inherited
    public String getAsString() {
        ensureComplete();

        if (valueString == null) {
            StringBuffer buffer = new StringBuffer();
            Set keys = fieldMap.keySet();
            for (Iterator i = keys.iterator(); i.hasNext(); ) {
                Object key = i.next();
                SimplePolicyValue value = (SimplePolicyValue)fieldMap.get(key);
                buffer.append(key);
                buffer.append(":");
                buffer.append(value == null ? "null" : value.getAsString());
                if (i.hasNext()) {
                    buffer.append(" ");
                }
            }
            valueString = buffer.toString();
        }
        return valueString;
    }

    // JavaDoc inherited
    public Map getFieldValuesAsMap() {
        ensureComplete();
        return fieldMap;
    }

    // Javadoc inherited
    public ImmutableMetaDataValue createMetaDataValue() {
        MutableStructureValue structureValue =
                VALUE_FACTORY.createStructureValue();
        Map fields =    structureValue.getFieldValuesAsMutableMap();

        for (Iterator i = fieldMap.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry) i.next();
            String name = (String) entry.getKey();
            InternalPolicyValue value = (InternalPolicyValue) entry.getValue();
            fields.put(name, value == null ? null : value.createMetaDataValue());
        }

        return (ImmutableMetaDataValue) structureValue.createImmutable();
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

 08-Oct-04	5755/3	geoff	VBM:2004092209 NullPointerException thrown when Accessing DeviceRepository API

 08-Oct-04	5755/1	geoff	VBM:2004092209 NullPointerException thrown when Accessing DeviceRepository API

 28-Jul-04	4952/1	claire	VBM:2004072301 Public API for Device Repository: Provide PolicyValue implementations

 ===========================================================================
*/
