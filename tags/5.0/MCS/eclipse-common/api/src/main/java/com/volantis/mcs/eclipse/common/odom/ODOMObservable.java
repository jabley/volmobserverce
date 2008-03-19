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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.eclipse.common.odom;

import org.jdom.Element;

import java.io.Serializable;

/**
 * Allows extensions of standard JDOM node classes to be observable (i.e. to
 * have {@link ODOMChangeListener}s registered with them and to fire events
 * to them when relevant events occur).
 */
public interface ODOMObservable extends Serializable {
    /**
     * Permits a change listener to be registered against all types of
     * change qualifiers (unless the listener is already registered against all
     * types of change qualifiers, in which case this method does nothing).
     *
     * @param listener the listener to be registered
     */
    void addChangeListener(ODOMChangeListener listener);

    /**
     * Permits a change listener to be registered against a specific change
     * qualifier (unless the listener is already registered against this
     * specific change qualifier, in which case this method does nothing).
     *
     * @param listener        the listener to be registered
     * @param changeQualifier the type of change of interest
     */
    void addChangeListener(ODOMChangeListener listener,
                           ChangeQualifier changeQualifier);

    /**
     * Permits a change listener previously registered against all types of
     * change qualifier to be removed (unless the given listener is not
     * registered against all types of change qualifier, in which case this
     * method does nothing).
     *
     * @param listener the listener to be unregistered
     */
    void removeChangeListener(ODOMChangeListener listener);

    /**
     * Permits a change listener previously registered against a specific
     * change qualifier to be removed (unless the given listener is not
     * registered against this specific change qualifier, in which case this
     * method does nothing).
     *
     * @param listener        the listener to be unregistered
     * @param changeQualifier the type of change against which the listener
     *                        should be removed
     */
    void removeChangeListener(ODOMChangeListener listener,
                              ChangeQualifier changeQualifier);

    /**
     * Called to notify an observable that a change event has been triggered
     * in a direct or indirect child of this observable.
     *
     * <p><strong>This is for internal use only.</strong></p>
     *
     * @param event the original change event
     */
    void childChanged(ODOMChangeEvent event);

    /**
     * Provide the name associated with this ODOMObservable if
     * appropriate.
     * @return The name of this ODOMObservable.
     * @throws UnsupportedOperationException If a name is not appropriate.
     */
    String getName();

    /**
     * Provide the parent element associated with this ODOMObservable.
     * @return The parent element.
     */
    Element getParent();

    /**
     * This detaches the ODOMObservable from its parent and removes all of its
     * related listeners, or does nothing if the element has no parent.
     */
    void detachObservable();
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 18-May-04	4429/1	claire	VBM:2004051401 Remove listeners for detached ODOMObservable instances

 12-Dec-03	2123/3	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 08-Dec-03	2157/2	pcameron	VBM:2003111302 Added ElementAttributesSection

 27-Nov-03	2046/1	philws	VBM:2003112603 Clarify contract on ODOMObservable methods and update ODOMChangeSupport to follow this contract

 04-Nov-03	1613/3	philws	VBM:2003102101 ODOMObservable must be a public interface

 04-Nov-03	1613/1	philws	VBM:2003102101 Observable DOM

 ===========================================================================
*/
