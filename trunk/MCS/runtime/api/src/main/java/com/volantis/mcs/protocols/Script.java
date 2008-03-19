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
 * $Header: /src/voyager/com/volantis/mcs/protocols/Script.java,v 1.2 2003/04/23 09:44:19 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 08-Apr-03    Geoff           VBM:2003040305 - Created; represents a Script
 *                              Asset at runtime.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols;

import com.volantis.mcs.components.ScriptComponentIdentity;
import com.volantis.mcs.protocols.assets.ScriptAssetReference;

/**
 * Represents a Script Asset at runtime.
 * <p>
 * NOTE: the design for this is a mess. It needs to be refactored to use the
 * normal Factory pattern.
 *
 * @todo refactor to use normal Factory design pattern.
 */
public class Script {

    /**
     * An interface to create scripts; lets us provide different
     * implementations for each Script type and reuse the creation code for
     * each.
     */
    interface ScriptCreator {
        Script create(String value)
                throws ProtocolException;
    }

    /**
     * The value of the script, as string. May not be null.
     */
    private String value;

    /**
     * Creates a Script, from the scriptObject. If the scriptObject is a
     * script component id, it will look up the text content from the related
     * script asset. If not, it will just use {@link Object#toString} to turn
     * the object into text content.
     *
     * @param scriptObject The object from which the text is to be retrieved.
     * @return the newly created Script.
     * @throws ProtocolException if an error which needs to stop page creation
     *                           happens during script creation.
     */
    public static Script createScript(ScriptAssetReference scriptObject)
            throws ProtocolException {
        return createWith(scriptObject, new ScriptCreator() {
            public Script create(String value) {
                return new Script(value);
            }
        });
    }

    /**
     * Helper method to create script objects. Each script type needs to call
     * this with a ScriptCreator to create the actual type of script it is.
     *
     * @param scriptObject the object to create from.
     * @param creator      the object which knows which script type to create.
     * @return the newly created Script, or null.
     * @throws ProtocolException if an error which needs to stop page creation
     *                           happens during script creation.
     */
    static Script createWith(
            ScriptAssetReference scriptObject,
            ScriptCreator creator) throws ProtocolException {

        String text = scriptObject.getScript();

        Script script = null;

        if (text != null) {
            script = creator.create(text);
        }
        return script;
    }

    /**
     * Creates a Script.
     *
     * @param value the string value of the asset.
     */
    protected Script(String value) {
        if (value == null) {
            throw new IllegalStateException("Script value cannot be null");
        }
        this.value = value;
    }

    /**
     * Returns the value of the Script as a String. If the scriptObject
     * passed in the constructor is not a {@link ScriptComponentIdentity} then
     * the result is the value returned from the object's toString method,
     * otherwise the script asset for the current device is retrieved. If the
     * asset could not be found, then it returns null.
     *
     * @return The script associated with the matching script asset.
     */
    public String stringValue() {
        return value;
    }

    /**
     * Appends the contents of this script to the output buffer provided.
     *
     * @param dom the output buffer to append to.
     */
    public void appendTo(DOMOutputBuffer dom) {
        dom.appendLiteral(value);
    }

    // Inherit Javadoc.
    public boolean equals(Object obj) {
        if (obj instanceof Script) {
            Script script = (Script) obj;
            // Compare value as strings (they cannot be null).
            return value.equals(script.value);
        } else {
            return false;
        }
    }

    // Inherit Javadoc.
    public int hashCode() {
        return value.hashCode();
    }

    // Inherit Javadoc.
    public String toString() {
        // For debugging/logging purposes only!
        return "Script[value=" + value + "]";
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 24-Feb-05	7129/1	philws	VBM:2005011701 Ensure logger info, warn and error calls are localizable

 24-Feb-05	7099/1	philws	VBM:2005011701 Ensure logger info, warn and error calls are localizable

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 11-Aug-04	5139/1	geoff	VBM:2004080311 Implement Null Assets: ObjectSelectionPolicys

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 ===========================================================================
*/
