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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This supporting class is for use by ODOMObservable implementations to handle
 * listener management and event propagation. Change qualified listeners are
 * handled by encapsulated instances of this class.
 */
public class ODOMChangeSupport implements Serializable {

    /**
     * Allow change support to be globally enabled/disabled
     * for performance i.e. when it is not needed don't use it.
     */
    private static volatile boolean changeSupportEnabled = true;

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(ODOMChangeSupport.class);

    /**
     * The ODOMObservable that is being observed (listened to).
     */
    private transient ODOMObservable source;

    /**
     * The set of listeners registered against the {@link #source} to be
     * managed by this change support.
     *
     * @associates <{ODOMChangeListener}>
     * @supplierCardinality 0..n
     * @supplierRole listeners
     */
    private transient ArrayList listeners;

    /**
     * The set of change support instances used to handle qualified event
     * listeners. This map is keyed on the ChangeQualifier literal.
     *
     * @associates <{ODOMChangeSupport}>
     * @supplierCardinality 0..n
     * @supplierRole qualifiedListeners
     */
    private transient Map qualifiedListeners;

    /**
     * Determine if ODOM change support is enabled.
     *
     * @return true if ODOM change support is enabled; false otherwise
     */
    public static boolean changeSupportEnabled() {
        return changeSupportEnabled;
    }

    /**
     * Used for command pattern that allows code to be executed
     * that for the duration change support is disabled.
     */
    public interface ChangeSupportDisabledCommand {
        Object execute();
    }

    /**
     * Allow some code section to be execute while change support is disabled.
     * This is the only way to do something while change support is disabled
     * and this method guarantees sequential execution of code while
     * change support is disabled. Sequential execution is needed because
     * change support enablement is a global static property - it needs
     * to be because the code that checks this flag is completely independent
     * of the code that sets it so a major restructuring would be needed
     * otherwise.
     * <p/>
     * When the executed command completes change support is guaranteed to
     * be re-enabled.
     *
     * @param command the ChangeSupportDisabledCommand that is executed while
     * @return the Object returned by the ChangeSupportDisabledCommand.execute
     *         method - which may be null.
     */
    public static synchronized Object executeWithoutChangeSupport(
            ChangeSupportDisabledCommand command) {
        try {
            changeSupportEnabled = false;
            return command.execute();
        } finally {
            changeSupportEnabled = true;
        }
    }

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param source the observable for which event listener management is to
     *               be provided.
     */
    public ODOMChangeSupport(ODOMObservable source) {
        this.source = source;
    }

    /**
     * The given listener is registered, unqualified, against the source
     * observable (unless it is already listening, unqualified).
     *
     * @param listener the listener to be added
     */
    public synchronized void addChangeListener(ODOMChangeListener listener) {
        if (listener != null) {
            if (listeners == null) {
                listeners = new ArrayList();
            }

            if (!listeners.contains(listener)) {
                listeners.add(listener);

                if (logger.isDebugEnabled()) {
                    logger.debug("Added listener " + listener + " to " +
                            //$NON-NLS-1$ //$NON-NLS-2$
                            source);
                }
            } else {
                logger.warn("listener-already-listening",
                        new Object[]{listener, source});
            }
        }
    }

    /**
     * The given listener is registered, qualified (unless null or {@link
     * com.volantis.mcs.eclipse.common.odom.ChangeQualifier#ANY} is given),
     * against the source observable (unless it is already listening, on the
     * given qualifier).
     *
     * @param listener        the listener to be added
     * @param changeQualifier the qualifier identifying the aspect of the
     *                        source object to be listened to
     */
    public synchronized void addChangeListener(
            ODOMChangeListener listener,
            ChangeQualifier changeQualifier) {
        if ((changeQualifier == null) ||
                (changeQualifier == ChangeQualifier.ANY)) {
            // Treat as unqualified
            addChangeListener(listener);
        } else if (listener != null) {
            ODOMChangeSupport qualifiedSupport;

            if (qualifiedListeners == null) {
                qualifiedListeners = new HashMap();
            }

            if ((qualifiedSupport =
                    (ODOMChangeSupport) qualifiedListeners
                            .get(changeQualifier)) ==
                    null) {
                qualifiedSupport = new ODOMChangeSupport(source);

                qualifiedListeners.put(changeQualifier, qualifiedSupport);
            }

            if (logger.isDebugEnabled()) {
                logger.debug(
                        "Adding \"" + changeQualifier.toString() + //$NON-NLS-1$
                                "\" listener " + listener + " to " +
                                //$NON-NLS-1$ //$NON-NLS-2$
                                source);
            }

            qualifiedSupport.addChangeListener(listener);
        }
    }

    /**
     * The unqualified listener is removed (unless its not actually listening
     * unqualified).
     *
     * @param listener the listener to be removed
     */
    public synchronized void removeChangeListener(ODOMChangeListener listener) {
        if (listener != null) {
            if (listeners != null) {
                listeners.remove(listener);
            }

            if (logger.isDebugEnabled()) {
                logger.debug("Removed listener " + listener + " from " +
                        //$NON-NLS-1$ //$NON-NLS-2$
                        source);
            }
        }
    }

    /**
     * The qualified (unless null or {@link
     * com.volantis.mcs.eclipse.common.odom.ChangeQualifier#ANY} is given)
     * listener is removed (unless its not actually listening on the given
     * qualifier).
     *
     * @param listener        the listener to be removed
     * @param changeQualifier the qualifier identifying the aspect of the
     *                        source object that the listener was listening
     *                        for
     */
    public synchronized void removeChangeListener(
            ODOMChangeListener listener,
            ChangeQualifier changeQualifier) {
        if ((changeQualifier == null) ||
                (changeQualifier == ChangeQualifier.ANY)) {
            // Treat as unqualified
            removeChangeListener(listener);
        } else if ((listener != null) &&
                (qualifiedListeners != null)) {
            ODOMChangeSupport qualifiedSupport =
                    (ODOMChangeSupport) qualifiedListeners.get(changeQualifier);

            if (qualifiedSupport != null) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Removing \"" + changeQualifier.toString() +
                            //$NON-NLS-1$
                            "\" listener " + listener + " from " +
                            //$NON-NLS-1$ //$NON-NLS-2$
                            source);
                }

                qualifiedSupport.removeChangeListener(listener);
            }
        }
    }

    /**
     * All qualified and unqualified listeners that are registered are removed.
     */
    public synchronized void removeAllListeners() {
        listeners = null;
        qualifiedListeners = null;
    }

    /**
     * Generates warning messages for all listeners (written to the log file)
     * that are registered with this object.
     */
    public synchronized void outputListenerWarnings() {

        // Warn about the general listeners - add a message about each
        listenerWarnings();

        // Warn about the qualified listeners - add a message about each
        changeSupportWarnings();
    }

    /**
     * A utility method that will iterate the qualifiedListeners Map and
     * output a warning to the log file about each item in it.
     * This method assumes that the collection contains instances of
     * {@link ODOMChangeSupport}.  If this is not the case, a
     * {@link ClassCastException} will be thrown.
     */
    private synchronized void changeSupportWarnings() {
        if (qualifiedListeners != null) {
            Collection entries = qualifiedListeners.values();
            if (entries != null) {
                for (Iterator i = entries.iterator(); i.hasNext(); /* empty */)
                {
                    ODOMChangeSupport support = (ODOMChangeSupport) i.next();
                    logger.warn("odom-listening-warning",
                            new Object[]{support, source});
                    // recursivley print warnings
                    support.outputListenerWarnings();
                }
            }
        }
    }

    /**
     * A utility method that will iterate over the listeners List and
     * output a warning to the log file about each item in the collection.
     * This method assumes that the collection contains instances of
     * {@link ODOMChangeListener}.  If this is not the case, a
     * {@link ClassCastException} will be thrown.
     */
    private synchronized void listenerWarnings() {
        if (this.listeners != null) {
            for (Iterator i = listeners.iterator(); i.hasNext(); /* empty */) {
                ODOMChangeListener listener = (ODOMChangeListener) i.next();
                logger.warn("odom-listener-details",
                        new Object[]{listener, source});
            }
        }
    }

    /**
     * The given event is fired against the source's unqualified listeners
     * (invoking the {@link ODOMChangeListener#changed} method) and any
     * qualified listeners registered for the event's {@link
     * ODOMChangeEvent#changeQualifier}.
     *
     * @param event the event to be fired
     */
    public void fireChange(ODOMChangeEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if ((oldValue != newValue) &&
                !((oldValue != null) &&
                        (newValue != null) &&
                        oldValue.equals(newValue))) {
            ArrayList targets = null;
            ODOMChangeSupport qualifiedSupport = null;

            if (logger.isDebugEnabled()) {
                logger.debug("Firing \"" + //$NON-NLS-1$
                        event.getChangeQualifier().toString() +
                        "\" event for " + event.getSource() + " in " +
                        //$NON-NLS-1$ //$NON-NLS-2$
                        source);
            }

            synchronized (this.getClass()) {
                if (listeners != null) {
                    targets = (java.util.ArrayList) listeners.clone();
                }

                ChangeQualifier changeQualifier = event.getChangeQualifier();

                if (qualifiedListeners != null) {
                    qualifiedSupport =
                            (ODOMChangeSupport) qualifiedListeners.
                                    get(changeQualifier);
                }
            }

            if (targets != null) {
                for (int i = 0; i < targets.size(); i++) {
                    ODOMChangeListener target =
                            (ODOMChangeListener) targets.get(i);
                    target.changed(source, event);
                }
            }

            if (qualifiedSupport != null) {
                if (logger.isDebugEnabled()) {
                    logger.debug(
                            "Passing event to qualifiedSupport"); //$NON-NLS-1$
                }

                qualifiedSupport.fireChange(event);
            }
        } else if (logger.isDebugEnabled()) {
            logger.debug("Firing of \"" + //$NON-NLS-1$
                    event.getChangeQualifier().toString() +
                    "\" event for " + event.getSource() + " in " +
                    //$NON-NLS-1$ //$NON-NLS-2$
                    source +
                    " suppressed because the value has not changed"); //$NON-NLS-1$
        }
    }

    /**
     * Get a copy of the list of listeners.
     *
     * @return a listener list
     */
    synchronized List getChangeListeners() {
        List list = Collections.EMPTY_LIST;
        if (listeners != null) {
            list = (List) listeners.clone();
        }
        return list;
    }

    /**
     * Get a copy of the Map of qualified listeners.
     *
     * @return a Map of qualified listeners
     */
    synchronized Map getQualifiedChangeListeners() {
        Map map = Collections.EMPTY_MAP;
        if (qualifiedListeners != null) {
            map = new HashMap(qualifiedListeners);
        }
        return map;
    }

    /**
     * Set the ArrayList of change listeners
     *
     * @param changeListeners the ArrayList of changeListeners
     */
    synchronized void setChangeListeners(ArrayList changeListeners) {
        this.listeners = changeListeners;
    }

    /**
     * Set the Map of qualified changeSupport objects.
     *
     * @param qualifiedListeners the qualified change listeners
     */
    synchronized void setQualifiedChangeListeners(Map qualifiedListeners) {
        this.qualifiedListeners = qualifiedListeners;
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

 28-Jun-04	4735/1	matthew	VBM:2004062104 Ensure that listener warning information works correctly for qualifiedListeners

 19-May-04	4429/3	claire	VBM:2004051401 Remove listeners for detached ODOMObservable instances

 12-May-04	4307/3	allan	VBM:2004051201 Fix restore button and moveListeners()

 12-May-04	4307/1	allan	VBM:2004051201 Fix restore button and moveListeners()

 11-May-04	4250/5	pcameron	VBM:2004051005 Added Restore Defaults button and changed ODOMElement and StandardElementHandler to deal with listener removal

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 27-Nov-03	2046/1	philws	VBM:2003112603 Clarify contract on ODOMObservable methods and update ODOMChangeSupport to follow this contract

 04-Nov-03	1613/2	philws	VBM:2003102101 Observable DOM

 ===========================================================================
*/
