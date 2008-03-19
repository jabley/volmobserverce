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

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;
import org.eclipse.jface.util.ListenerList;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * This class listens to ISelection change events and translates these events
 * into ODOMElementSelectionEvent's.
 * <p/>
 * In order to do this we implement Eclipse's ISelectionListener and expect to
 * receive an Eclipse IStructuredSelection in its selectionChanged() method.
 * <p/>
 * If some other form of selection is received an IllegalArgumentException will
 * be thrown. The elements in the ISelectionListener are assumed to be
 * ODOMElements. Any other form of element will cause an
 * IllegalArgumentException to be thrown. If the selection is OK, the handling
 * of the selected elements will be delegated to the encapsulated
 * provider's update() method.
 */
public class ODOMSelectionManager
        implements ISelectionChangedListener, ISelectionProvider {
    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(ODOMSelectionManager.class);

    /**
     * A map of filter to provider objects.
     */
    private Map filterToProviderMap =
            Collections.synchronizedMap(new HashMap());

    /**
     * Is the ODOMSelectionManager enabled or disabled. Disabled it
     * will not fire events.
     */
    private boolean isEnabled = true;

    /**
     * A list maintaining the current selection.
     */
    private final List selection = new ArrayList();

    /**
     * A list of listeners interested in selection changes.
     */
    private final ListenerList selectionChangedListeners = new ListenerList();

    /**
     * Default constructor ODOMSelectionFilter as a parameter.
     *
     * @param filter the ODOMSelectionFilter used to create the default
     *               provider. This may be null.
     */
    public ODOMSelectionManager(ODOMSelectionFilter filter) {
        if (filter == null) {
            filter = new ODOMSelectionFilter(null, null);
        }
        filterToProviderMap.put(null,
                new DefaultODOMElementSelectionProvider(filter));
    }

    /**
     * Create an ODOMElementSelectionProvider each time a caller invokes the
     * addSelectionChangedListener() with a filter that it has not seen before. To
     * discover whether or not it has seen the filter before, it should
     * establish if there is a ODOMElementSelectionProvider in the collection
     * with an equal filter.
     * <p/>
     * Newly created ODOMElementSelectionProviders are added to the collection.
     * (NOTE: if the filter is null a DefaultODOMElementSelectionProvider should
     * be used that will not filter or resolve anything.)
     *
     * @param listener the listener to add to the collection of listeners.
     * @param filter   the filter to use to identify the provider. May be null.
     */
    public void addSelectionListener(ODOMElementSelectionListener listener,
                                     ODOMSelectionFilter filter) {
        ODOMElementSelectionProvider provider =
                (ODOMElementSelectionProvider) filterToProviderMap.get(filter);
        if (provider == null) {
            provider = new DefaultODOMElementSelectionProvider(filter);
            filterToProviderMap.put(filter, provider);
        }
        provider.addSelectionListener(listener);
    }

    /**
     * This should find the right ODOMSelectionProvider provider in the
     * collection using the filter. If appropriate ODOMSelectionProvider is
     * found then its removeSelectionListener() method should be called passing
     * through the ElementSelectionListener to remove.
     * <p/>
     * If an appropriate ODOMSelectionProvider is not found then an
     * IllegalStateException should be throw to signify that a remove was
     * attempted on a listener that is not listening.
     *
     * @param listener the listener to remove from the collection of listeners.
     * @param filter   the filter to use to identify the provider. May be null.
     */
    public void removeSelectionListener(ODOMElementSelectionListener listener,
                                        ODOMSelectionFilter filter) {
        ODOMElementSelectionProvider provider =
                (ODOMElementSelectionProvider) filterToProviderMap.get(filter);
        if (provider != null) {
            provider.removeSelectionListener(listener);
        } else {
            throw new IllegalStateException("A remove was attempted on a " + //$NON-NLS-1$
                    "listener that is not listening: " + listener); //$NON-NLS-1$
        }
    }

    /**
     * IChangedSelectionListener callback method.
     * @throws IllegalArgumentException If event is null.
     */
    // rest of javadoc inherited
    public void selectionChanged(SelectionChangedEvent event) {
        if (event == null) {
            throw new IllegalArgumentException("Cannot be null: event"); //$NON-NLS-1$
        }
        ISelection selection = event.getSelection();
        if (selection instanceof IStructuredSelection) {
            List elements = ((IStructuredSelection) selection).toList();
            for (int i = 0; i < elements.size(); i++) {
                Object element = elements.get(i);
                if (!(element instanceof ODOMElement)) {
                    throw new IllegalArgumentException(
                            "Unsupported element found: " + element); //$NON-NLS-1$
                }
            }

            updateSelection(elements);
        } else {
            throw new IllegalArgumentException("Unexpected selection type: " + //$NON-NLS-1$
                    selection);
        }
    }


    /**
     * Sets the selection on all registered
     * <code>ODOMElementSelectionProvider</code>to be the provided collection
     * of elements.
     * @param elements a list of <code>ODOMElement</code> to be part of the
     * selection
     */
    public void setSelection(List elements) {
        updateSelection(elements);
    }


    /**
     * Updates the selection with the given elements and fires a
     * SelectionChangedEvent to all registered listeners interested in
     * selection changes. Also, all interested ODOMElementSelectionProviders
     * are informed of a selection event.
     * @param elements the list of elements for the updated selection
     */
    private void updateSelection(List elements) {
        // Create a new Collection so that add/remove still work
        // while we're notifying listeners.
        if (isEnabled) {
            // Fire a SelectionChangedEvent.
            fireSelectionChangedEvent(elements);
            Collection values = new ArrayList(filterToProviderMap.values());
            if ((values != null) && (values.size() > 0)) {
                Iterator iterator = values.iterator();
                while (iterator.hasNext()) {
                    // Inform an ODOMElementSelectionProvider that a selection
                    // has taken place.
                    ((ODOMElementSelectionProvider) iterator.next()).
                            update(elements);
                }
            }
        }
    }

    /**
     * Set the enabled state of the ODOMSelectionManager. If enabled the
     * manager works as normal. If disabled the ODOMSelectionManager will
     * not notify its listeners. Please ensure that suitable finally statements
     * are used to re-enable the SelectionManager if you disable it.
     *
     * @param enable the enabled state you wish the ODOMSelectionManager to
     * take.
     */
    public void setEnabled(boolean enable) {
        this.isEnabled = enable;
    }

    /**
     * Return the enabled state of this selection manager.
     */
    public boolean isEnabled() {
        return this.isEnabled;
    }

    // javadoc inherited
    public void addSelectionChangedListener(
            ISelectionChangedListener listener) {
        if (listener != null) {
            selectionChangedListeners.add(listener);
        }
    }

    /**
     * Gets the current selection.
     * @return an ODOMElementSelection representing the current selection.
     */
    public ISelection getSelection() {
        ODOMElementSelection odomSelection = null;
        synchronized (selection) {
            odomSelection = new ODOMElementSelection(selection);
        }
        return odomSelection;
    }

    // javadoc inherited
    public void removeSelectionChangedListener(
            ISelectionChangedListener listener) {
        if (listener != null) {
            selectionChangedListeners.remove(listener);
        }
    }

    /**
     * Sets the current selection.
     * @param selection the selection to set, which must be an
     * ODOMElementSelection.
     * @throws IllegalArgumentException if selection is not an
     * ODOMElementSelection
     */
    public void setSelection(ISelection selection) {
        if (selection instanceof ODOMElementSelection) {
            this.setSelection(((IStructuredSelection) selection).toList());
        } else {
            throw new IllegalArgumentException("Expected instance of " +
                    "ODOMElementSelection but got: " + selection);
        }
    }

    /**
     * Replaces the current selection with the contents of the given list, and
     * then fires a SelectionChangedEvent to all registered listeners
     * interested in SelectionChangedEvents.
     * @param selectionList the new selection which has caused the event
     */
    private void fireSelectionChangedEvent(List selectionList) {
        // Replace the current selection with the new selection.
        synchronized (selection) {
            selection.clear();
            selection.addAll(selectionList);
        }
        // Create the event that is fired to interested listeners.
        SelectionChangedEvent event =
                new SelectionChangedEvent(this, getSelection());
        Object[] listeners = selectionChangedListeners.getListeners();
        // Fire a SelectionChangedEvent at each listener.
        for (int i = 0; i < listeners.length; i++) {
            ((ISelectionChangedListener) listeners[i]).selectionChanged(event);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 02-Sep-04	5369/1	allan	VBM:2004051306 Don't unselect devices from Structure page selection

 03-Aug-04	5030/14	pcameron	VBM:2004070705 Fixed IllegalArgumentException

 02-Aug-04	5030/12	pcameron	VBM:2004070705 Fixed IllegalArgumentException

 02-Aug-04	5030/9	pcameron	VBM:2004070705 Fixed IllegalArgumentException

 02-Aug-04	5030/5	pcameron	VBM:2004070705 Using ODOMElementSelection

 02-Aug-04	5030/3	pcameron	VBM:2004070705 Implemented ISelectionProvider on ODOMSelectionManager and DeviceEditor

 26-May-04	4470/3	matthew	VBM:2004041406 reduce flicker in layout designer

 26-May-04	4470/1	matthew	VBM:2004041406 Reduce flicker in Layout Designer

 11-May-04	4262/1	matthew	VBM:2004051009 DefaultODOMElementSelectionProvider modified to avoid ConcurrentModificationException

 17-Feb-04	2988/2	eduardo	VBM:2004020908 undo/redo reafctoring for multi-page editor

 11-Feb-04	2862/1	allan	VBM:2004020411 The DeviceRepositoryBrowser.

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 23-Jan-04	2720/1	allan	VBM:2004011201 LayoutDesign page with selection and partial action framework.

 12-Dec-03	2123/5	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 12-Dec-03	2123/3	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 29-Nov-03	2064/1	philws	VBM:2003112901 Correct the result of resolved selections

 25-Nov-03	2005/8	byron	VBM:2003112006 Eclipse to ODOM events - replaced defaultProvider with default value in map

 25-Nov-03	2005/6	byron	VBM:2003112006 Eclipse to ODOM events - fixed javadoc and updated testcases

 24-Nov-03	2005/2	byron	VBM:2003112006 Eclipse to ODOM events

 23-Nov-03	1974/1	steve	VBM:2003112006 ODOM Selection changes

 ===========================================================================
*/
