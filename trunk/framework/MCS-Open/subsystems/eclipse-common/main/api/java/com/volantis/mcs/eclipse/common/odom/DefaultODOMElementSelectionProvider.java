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

import com.volantis.mcs.xml.xpath.XPathException;
import com.volantis.mcs.eclipse.common.EclipseCommonPlugin;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;


/**
 * This class manages ODOMElement selections and listeners.
 *
 * <p>ODOMElement objects are passed to the selector through the update() method
 * which takes a <code>List</code> of ODOM Elements. Each element is resolved
 * through the XPath resolver and then filtered by name. The order of this is
 * important as the resolver may resolve an element to another element which is
 * then filtered. It is perceived that in normal usage, either a filter list OR
 * an XPath resolver will be used and occasionally both.</p>
 *
 * <p>The elements remaining after resolution and filtering are taken to
 * constitute an event. If the elements are different than the last set of
 * elements, that event is sent to all registered listeners. If the elements
 * have not changed, IE the elements are identical to last time update was
 * called the event will not be sent.</p>
 *
 * <p>ODOMElementSelectionListeners may be added and removed to an instance
 * of this object using the addXX and removeXXX methods respectively.
 *
 * If update() has already been called and we have a list of elements
 * to make up an event, that event will be sent to any listener that is
 * registered.</p>
 */
public class DefaultODOMElementSelectionProvider
        implements ODOMElementSelectionProvider {

    /**
     * List of listener objects to be notified of changes.
     */
    private List listeners = null;

    /**
     * List of ODOMElement objects constituting an event
     */
    private List eventElements;

    /**
     * Build area List for creating new element set to avoid garbage.
     */
    private List eventBuilder;

    /**
     * The ODOM selection filter.
     */
    private ODOMSelectionFilter filter;

    /**
     * Create an element selection with a filter
     */
    public DefaultODOMElementSelectionProvider(ODOMSelectionFilter filter) {
        this.filter = filter;
        listeners = new ArrayList();
        eventElements = new ArrayList();
        eventBuilder = new ArrayList();
    }

    /**
     * Notifies listeners of a selection update. Note that the selection is
     * transformed to a form appropriate to the listeners using the associated
     * {@link ODOMSelectionFilter} configuration.
     *
     * <p>This works passing each element in the passed list resolving it
     * through XPath and filtering it by name. If the resolved element makes it
     * through, it is added to our list of elements. If the resulting element
     * list is <strong>not</strong> the same as last time (i.e. they have
     * changed) then the resolved elements are sent to every listener as an
     * {@link ODOMElementSelectionEvent}</p>
     *
     * @param elements
     */
    public void update(List elements) {
        // Assume that the elements are the same as last time
        boolean modified = false;

        // Clear our build area
        eventBuilder.clear();

        // Filter each element
        Iterator iter = elements.iterator();
        while (iter.hasNext()) {
            ODOMElement element = (ODOMElement) iter.next();
            ODOMElement resolvedElement = null;

            try {
                // Convert the actual element selection into one appropriate
                // to the listeners
                resolvedElement = filter.resolve(element);

                // If the conversion worked and the resolved element is not to
                // be filtered out and it hasn't already been processed this
                // time through
                if ((resolvedElement != null) &&
                    filter.include(resolvedElement) &&
                    !eventBuilder.contains(resolvedElement)) {
                    // If the element is in the current list, we haven't
                    // changed the selection
                    if (eventElements.contains(resolvedElement)) {
                        eventElements.remove(resolvedElement);
                    } else {
                        // The element was not in the current list so we are
                        // adding a new element
                        modified = true;
                    }

                    eventBuilder.add(resolvedElement);
                }
            } catch (XPathException e) {
                logException(e);
            }
        }

        // If there are elements left from last time, then delete them and note
        // that we have changed. Unless something has been selected but
        // the filter configuration is saying that it is not interested in
        // selections where it has filtered everything out. Note that
        // this is not the same as empty selections i.e. where something was
        // selected and then nothing is selected.
        if (eventElements.size() != 0 &&
                (elements.isEmpty() ||
                !filter.getConfiguration().ignoreEmptyFilteredSelections())) {
            modified = true;
            eventElements.clear();
        }

        // Swap the new and old lists to avoid garbage. Note that the
        // eventBuilder list will now be empty and the eventElements list is
        // the new element list. If the elements in the list have changed,
        // then modified will be true.
        List tmp = eventElements;
        eventElements = eventBuilder;
        eventBuilder = tmp;

        // If the list has changed, send the new list to every listener
        if (modified) {

            // copy the list so that ConcurrentModificationExceptions are avoided
            // if the listener.selectionChanged method decides to add or remove a listener
            // from this when an iteration is in progress.
            List listenersCopy = new ArrayList(listeners);
            ODOMElementSelectionEvent event =
                    new ODOMElementSelectionEvent(eventElements);

            iter = listenersCopy.iterator();
            while (iter.hasNext()) {

                ODOMElementSelectionListener listener =
                        (ODOMElementSelectionListener) iter.next();
                listener.selectionChanged(event);
            }

        }

    }

    /**
     * Log the XPathException using the EclipseCommonPlugin logError method.
     *
     * @param e the XPathException to log.
     */
    protected void logException(XPathException e) {
        EclipseCommonPlugin.logError(EclipseCommonPlugin.getDefault(),
                DefaultODOMElementSelectionProvider.class,
                e);
    }

    /**
     * Add a new selection listener. If update() has already been called,
     * there will be a list of selected elements. These will be sent to the
     * new listener as an event after registration.
     * @param listener the ODOMElementSelectionListener to be sent events
     */
    public void addSelectionListener(ODOMElementSelectionListener listener) {
        if (listener != null) {
            listeners.add(listener);
            // Send the new listener a welcoming event if there is one.
            if ((eventElements != null) && (eventElements.size() > 0)) {
                listener.selectionChanged(new ODOMElementSelectionEvent(
                        eventElements));
            }
        }

    }

    /**
     * Remove a selection listener.
     * @param listener the ODOMElementSelectionListener to remove
     */
    public void removeSelectionListener(ODOMElementSelectionListener listener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener);
        }

    }

    /**
     * Get the <code>ODOMSelectionFilter</code> filter.
     *
     * @return the <code>ODOMSelectionFilter</code> filter.
     */
    public ODOMSelectionFilter getSelectionFilter() {
        return filter;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 02-Sep-04	5369/1	allan	VBM:2004051306 Don't unselect devices from Structure page selection

 11-May-04	4262/1	matthew	VBM:2004051009 DefaultODOMElementSelectionProvider modified to avoid ConcurrentModificationException

 29-Nov-03	2064/1	philws	VBM:2003112901 Correct the result of resolved selections

 27-Nov-03	2036/2	byron	VBM:2003111902 Element Selection implementation - added testcase and fixed bugs

 24-Nov-03	2005/1	byron	VBM:2003112006 Eclipse to ODOM events

 23-Nov-03	1974/1	steve	VBM:2003112006 ODOM Selection changes

 20-Nov-03	1960/4	steve	VBM:2003111902 Classes Renamed

 20-Nov-03	1960/1	steve	VBM:2003111902 Pre-XPath save

 ===========================================================================
*/
