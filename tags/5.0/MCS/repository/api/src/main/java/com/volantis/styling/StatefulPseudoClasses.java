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

package com.volantis.styling;

import java.lang.reflect.Field;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * A combination of pseudo classes that represent different states within the
 * user interface.
 *
 * <p>These can be combined together in a number of different ways. The order
 * in which they are combined is irrelevant, e.g.
 * {@link #LINK :link}{@link #HOVER :hover} is the same as
 * {@link #HOVER :hover}{@link #LINK :link}</p>
 *
 * <ul>
 * <li>{@link #LINK :link} and {@link #VISITED :visited} are mutually exclusive
 * and so cannot be combined together, but each can be combined with any of the
 * other pseudo classes.</li>
 *
 * <li>{@link #HOVER :hover}, {@link #ACTIVE :active} and {@link #FOCUS :focus}
 * can be combined together.</li>
 * </ul>
 */
public abstract class StatefulPseudoClasses {

    /**
     * The Class that implements this class.
     *
     * <p><stromg>This plus the code that initialises it must come before all
     * other fields.</strong></p>
     */
    public static final Class implementationClass;

    /**
     * Access the default instance using reflection to prevent
     * dependencies between this and the implementation class.
     */
    static {
        try {
            ClassLoader loader = StatefulPseudoClasses.class.getClassLoader();
            implementationClass = loader.loadClass("com.volantis.styling.StatefulPseudoClassImpl");

        } catch (ClassNotFoundException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    /**
     * Get the value of a static field.
     *
     * @param name The name of the field.
     *
     * @return The value of a static field.
     */
    private static StatefulPseudoClass getStaticField(String name) {
        try {
            Field field = implementationClass.getDeclaredField(name);
            return (StatefulPseudoClass) field.get(null);
        } catch (NoSuchFieldException e) {
            throw new UndeclaredThrowableException(e);
        } catch (SecurityException e) {
            throw new UndeclaredThrowableException(e);
        } catch (IllegalAccessException e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    /**
     * Represents the <code>:link</code> pseudo class.
     */
    public static final StatefulPseudoClass LINK = getStaticField("LINK");

    /**
     * Represents the <code>:hover</code> pseudo class.
     */
    public static final StatefulPseudoClass HOVER = getStaticField("HOVER");

    /**
     * Represents the <code>:focus</code> pseudo class.
     */
    public static final StatefulPseudoClass FOCUS = getStaticField("FOCUS");

    /**
     * Represents the <code>:active</code> pseudo class.
     */
    public static final StatefulPseudoClass ACTIVE = getStaticField("ACTIVE");

    /**
     * Represents the <code>:visited</code> pseudo class.
     */
    public static final StatefulPseudoClass VISITED = getStaticField("VISITED");

    /**
     * Represents the <code>:mcs-concealed</code> pseudo class.
     */
    public static final StatefulPseudoClass MCS_CONCEALED = getStaticField("MCS_CONCEALED");

    /**
     * Represents the <code>:mcs-unfolded</code> pseudo class.
     */
    public static final StatefulPseudoClass MCS_UNFOLDED = getStaticField("MCS_UNFOLDED");

    /**
     * Represents the <code>:mcs-invalid</code> pseudo class.
     */
    public static final StatefulPseudoClass MCS_INVALID = getStaticField("MCS_INVALID");

    /**
     * Represents the <code>:mcs-normal</code> pseudo class.
     */
    public static final StatefulPseudoClass MCS_NORMAL = getStaticField("MCS_NORMAL");
    
    /**
     * Represents the <code>:mcs-busy</code> pseudo class.
     */
    public static final StatefulPseudoClass MCS_BUSY = getStaticField("MCS_BUSY");
    
    /**
     * Represents the <code>:mcs-failed</code> pseudo class.
     */
    public static final StatefulPseudoClass MCS_FAILED = getStaticField("MCS_FAILED");
    
    /**
     * Represents the <code>:mcs-suspended</code> pseudo class.
     */
    public static final StatefulPseudoClass MCS_SUSPENDED = getStaticField("MCS_SUSPENDED");
    
    /**
     * Represents the <code>:mcs-disabled</code> pseudo class.
     */
    public static final StatefulPseudoClass MCS_DISABLED = getStaticField("MCS_DISABLED");
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 22-Jul-05	9110/3	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 18-Jul-05	9029/1	pduffin	VBM:2005071301 Adding ability for styling engine to support nested style sheets

 02-Jun-05	7997/1	pduffin	VBM:2005050324 Added styling API

 ===========================================================================
*/
