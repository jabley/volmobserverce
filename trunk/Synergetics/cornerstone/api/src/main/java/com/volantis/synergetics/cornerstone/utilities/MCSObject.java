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
package com.volantis.synergetics.cornerstone.utilities;

/**
 * An useful abstract base class to enforce some kind of commonality for
 * common operations.
 * <p>
 * Currently all it does is help subclasses implement toString in a consistent
 * fashion.
 *
 * @deprecated not a good idea, not sure what I was thinking.  
 * @todo: get rid of this, moving any implementation into ObjectHelper
 * toStringData could become a interface for anonymous implementation.
 */
public abstract class MCSObject {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2004.";

    // Javadoc inherited from super class.
    public String toString() {
        return toString(true, true);
    }

    /**
     * A version of {@link #toString} which renders in a shorter form.
     * <p>
     * For now this means leaving out the package name.
     *
     * @return string description of this object.
     */
    public String toStringShort() {
        return toString(false, true);
    }

    /**
     * A version of {@link #toString} which can render in various forms
     * depending on the parameters passed in.
     *
     * @param fullyQualifiedName if true, render the fully qualified package
     *      name as part of the description, otherwise just render the base
     *      class name.
     * @param withHashCode if true, render the hash code of the object as part
     *      of the description, otherwise do not.
     * @return string description of this object.
     */
    public String toString(boolean fullyQualifiedName, boolean withHashCode) {

        StringBuffer buffer = new StringBuffer();

        // Add the class name, in either fully qualified or base format.
        String fullyQualifiedClassName = getClass().getName();
        if (fullyQualifiedName) {
            buffer.append(fullyQualifiedClassName);
        } else {
            String baseClassName;
            int lastDot = fullyQualifiedClassName.lastIndexOf(".");
            if (lastDot > 0) {
                baseClassName = fullyQualifiedClassName.substring(
                        lastDot + 1);
            } else {
                baseClassName = fullyQualifiedClassName;
            }
            buffer.append(baseClassName);
        }

        // Add the hash code, if requested.
        if (withHashCode) {
            buffer.append("@");
            buffer.append(Integer.toHexString(System.identityHashCode(this)));
        }

        buffer.append(" [" + toStringData() + "]");
        return buffer.toString();
    }

    /**
     * Return a String representation of the state of the object.
     * @return The String representation of the state of the object.
     */
    protected abstract String toStringData () ;

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 15-Jul-05	9067/3	geoff	VBM:2005071415 More refactoring for: XDIMECP: Generate optimised CSS for a DOM.

 24-May-05	7890/2	pduffin	VBM:2005042705 Committing extensive restructuring changes

 29-Apr-05	7946/1	pduffin	VBM:2005042821 Moved code out of repository, in preparation for some device work

 13-Apr-05	7572/1	geoff	VBM:2005040612 Device repository merge report: create event model and XML report listeners

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 19-Nov-04	5733/1	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 ===========================================================================
*/
