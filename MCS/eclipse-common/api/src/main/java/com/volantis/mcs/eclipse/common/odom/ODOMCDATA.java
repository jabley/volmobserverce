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

import org.jdom.CDATA;
import org.jdom.Element;
import org.jdom.Text;

/**
 * This is an observable implementation of the JDOM CDATA.
 */
public class ODOMCDATA extends CDATA implements ODOMObservable {
    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(ODOMCDATA.class);

    /**
     * The change support object responsible for notifying all registered
     * listeners. This is lazily created by the {@link ODOMObservable}
     * interface implementation when the first observer is registered.
     *
     * @supplierCardinality 0..1
     * @supplierRole changeSupport
     * @link aggregation
     */
    private ODOMChangeSupport changeSupport;

    /**
     * Initializes the new instance with default values.
     */
    protected ODOMCDATA() {
    }

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param text the initial text in the text node.
     */
    public ODOMCDATA(String text) {
        super(text);
    }

    //-------------------------------------------------------------------------
    // ODOMObservable interface implementation
    //-------------------------------------------------------------------------

    /**
     * @note The {@link ODOMElement#childChanged}, {@link
     * ODOMAttribute#childChanged}, {@link ODOMText#childChanged} and {@link
     * ODOMCDATA#childChanged} implementations are all identical. This is
     * implemented this way to avoid garbage.
     */
    public void childChanged(ODOMChangeEvent event) {
        if (ODOMChangeSupport.changeSupportEnabled()) {
            if (changeSupport != null) {
                changeSupport.fireChange(event);
            }

            notifyParent(getParent(), event);
        }
    }

    /**
     * Names are not supported
     *
     * @return nothing - this method will throw an exception.
     * @throws UnsupportedOperationException - always.
     */
    public String getName() {
        throw new UnsupportedOperationException(
                "ODOMCDATA has no name."); //$NON-NLS-1$
    }

    /**
     * @note The {@link ODOMElement#addChangeListener}, {@link
     * ODOMAttribute#addChangeListener}, {@link ODOMText#addChangeListener} and
     * {@link ODOMCDATA#addChangeListener} implementations are all identical.
     * This is implemented this way to avoid garbage.
     */
    public void addChangeListener(ODOMChangeListener listener) {
        initChangeSupport();

        changeSupport.addChangeListener(listener);
    }

    /**
     * @note The {@link ODOMElement#addChangeListener}, {@link
     * ODOMAttribute#addChangeListener}, {@link ODOMText#addChangeListener} and
     * {@link ODOMCDATA#addChangeListener} implementations are all identical.
     * This is implemented this way to avoid garbage.
     */
    public void addChangeListener(
            ODOMChangeListener listener,
            ChangeQualifier changeQualifier) {
        initChangeSupport();

        changeSupport.addChangeListener(listener, changeQualifier);
    }

    /**
     * @note The {@link ODOMElement#removeChangeListener}, {@link
     * ODOMAttribute#reODOMmoveChangeListener}, {@link
     * ODOMText#removeChangeListener} and {@link
     * ODOMCDATA#removeChangeListener} implementations are all identical. This
     * is implemented this way to avoid garbage.
     */
    public void removeChangeListener(ODOMChangeListener listener) {
        if (changeSupport != null) {
            changeSupport.removeChangeListener(listener);
        }
    }

    /**
     * @note The {@link ODOMElement#removeChangeListener}, {@link
     * ODOMAttribute#removeChangeListener}, {@link
     * ODOMText#removeChangeListener} and {@link
     * ODOMCDATA#removeChangeListener} implementations are all identical. This
     * is implemented this way to avoid garbage.
     */
    public void removeChangeListener(
            ODOMChangeListener listener,
            ChangeQualifier changeQualifier) {
        if (changeSupport != null) {
            changeSupport.removeChangeListener(listener, changeQualifier);
        }
    }

    //-------------------------------------------------------------------------
    // ODOMObservable interface support methods
    //-------------------------------------------------------------------------

    /**
     * Ensures that a change support instance is available when needed by
     * lazy instantiation.
     *
     * @note The {@link ODOMElement#initChangeSupport}, {@link
     * ODOMAttribute#initChangeSupport}, {@link ODOMText#initChangeSupport}
     * and {@link ODOMCDATA#initChangeSupport} implementations are all
     * identical. This is implemented this way to avoid garbage.
     */
    private synchronized void initChangeSupport() {
        if (changeSupport == null) {
            changeSupport = new ODOMChangeSupport(this);
        }
    }

    /**
     * Allows a qualified change event to be fired.
     *
     * @param oldValue        the old value to go in the event
     * @param newValue        the new value to go in the event
     * @param changeQualifier the qualifier to go in the event
     * @return the event fired, or null if the old and new values were the same
     * @note The {@link ODOMElement#fireChange(Object,Object,ChangeQualifier)},
     * {@link ODOMAttribute#fireChange(Object,Object,ChangeQualifier)},
     * {@link ODOMText#fireChange(Object,Object,ChangeQualifier)} and
     * {@link ODOMCDATA#fireChange(Object,Object,ChangeQualifier)}
     * implementations are all identical. This is implemented this way to avoid
     * garbage.
     */
    private ODOMChangeEvent fireChange(Object oldValue,
                                       Object newValue,
                                       ChangeQualifier changeQualifier) {
        return fireChange(ODOMChangeEvent.createNew(this,
                oldValue,
                newValue,
                changeQualifier));
    }

    /**
     * Allows a change event to be fired.
     *
     * @param event the event to be fired
     * @return the event fired, or null if the old and new values were the same
     * @note The {@link ODOMElement#fireChange(ODOMChangeEvent)}, {@link
     * ODOMAttribute#fireChange(ODOMChangeEvent)}, {@link
     * ODOMText#fireChange(ODOMChangeEvent)} and {@link
     * ODOMCDATA#fireChange(ODOMChangeEvent)} implementations are all
     * identical. This is implemented this way to avoid garbage.
     */
    private ODOMChangeEvent fireChange(ODOMChangeEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
        ODOMChangeEvent result = null;

        if ((oldValue != newValue) &&
                !((oldValue != null) &&
                        (newValue != null) &&
                        oldValue.equals(newValue))) {
            if (changeSupport != null) {
                changeSupport.fireChange(event);
            }

            result = event;

            notifyParent(getParent(), event);
        } else if (logger.isDebugEnabled()) {
            logger.debug("Firing of \"" + //$NON-NLS-1$
                    event.getChangeQualifier().toString() +
                    "\" event for " + event.getSource() + " in " +
                    //$NON-NLS-1$ //$NON-NLS-2$
                    this +
                    " suppressed because the value has not changed"); //$NON-NLS-1$
        }

        return result;
    }

    /**
     * Allows a change event to be passed up to the parent, if the parent is an
     * {@link ODOMObservable}.
     *
     * @param parent the parent that is to be notified
     * @param event  the event to be passed to the parent
     * @note The {@link ODOMElement#notifyParent(Element,ODOMChangeEvent)},
     * {@link ODOMAttribute#notifyParent(Element,ODOMChangeEvent)},
     * {@link ODOMText#notifyParent(Element,ODOMChangeEvent)} and
     * {@link ODOMCDATA#notifyParent(Element,ODOMChangeEvent)} implementations
     * are all identical. This is implemented this way to avoid garbage.
     */
    private void notifyParent(Element parent, ODOMChangeEvent event) {

        if (parent instanceof ODOMObservable) {
            ((ODOMObservable) parent).childChanged(event);
        } else if (logger.isDebugEnabled()) {
            if (parent == null) {
                logger.debug("Parent is null"); //$NON-NLS-1$
            } else {
                logger.debug("Parent is not observable"); //$NON-NLS-1$
            }
        }
    }

    //-------------------------------------------------------------------------
    // CDATA method augmentation to trigger change events
    //-------------------------------------------------------------------------

    /**
     * Augments {@link CDATA#setText} to ensure that a
     * {@link com.volantis.mcs.eclipse.common.odom.ChangeQualifier#TEXT}
     * change event is fired.
     *
     * @note The {@link ODOMText#setText} and {@link ODOMCDATA#setText}
     * implementations are identical. This is implemented this way to avoid
     * garbage.
     */
    public Text setText(String text) {
        String oldText = getText();

        Text result = super.setText(text);

        fireChange(oldText, text, ChangeQualifier.TEXT);

        return result;
    }

    /**
     * Augments {@link CDATA#setParent} to ensure that a
     * {@link com.volantis.mcs.eclipse.common.odom.ChangeQualifier#HIERARCHY}
     * change event is fired.
     *
     * @note The {@link ODOMText#setParent} and {@link ODOMCDATA#setParent}
     * implementations are identical. This is implemented this way to avoid
     * garbage.
     */
    protected Text setParent(Element parent) {
        // obtain a reference to the old parent as we will need to notify it
        // that it has changed.
        Element oldParent = getParent();
        // set the new parent
        Text result = super.setParent(parent);

        // fire a change event. This will notify the new parent that the
        // hierarchy has changed
        ODOMChangeEvent event =
                fireChange(oldParent, parent, ChangeQualifier.HIERARCHY);

        if (event != null) {
            // notify the old parent of the change.
            notifyParent(oldParent, event);
        }
        return result;
    }

    /**
     * This will cause a
     *
     * @link com.volantis.mcs.eclipse.common.odom.ChangeQualifier#HIERARCHY}
     * event to be fired.
     * @note The {@link ODOMText#detach} and {@link ODOMCDATA#detach}
     * implementations are identical. This is implemented this way to avoid
     * garbage.
     */
    public Text detach() {
        // No event trigger is required here since the superclass version will
        // end up calling {@link Text#setParent} with null anyway
        Text detachedText = super.detach();
        if (detachedText != null && changeSupport != null) {
            changeSupport.outputListenerWarnings();
        }
        return detachedText;
    }

    /**
     * Augments {@link CDATA#append(String)} to ensure that a
     * {@link com.volantis.mcs.eclipse.common.odom.ChangeQualifier#TEXT}
     * change event is fired.
     *
     * @note The {@link ODOMText#append(String)} and {@link
     * ODOMCDATA#append(String)} implementations are identical. This is
     * implemented this way to avoid garbage.
     */
    public void append(String text) {
        String oldText = getText();

        super.append(text);

        String newText = getText();

        fireChange(oldText, newText, ChangeQualifier.TEXT);
    }

    /**
     * Augments {@link CDATA#append(Text)} to ensure that a {@link
     * com.volantis.mcs.eclipse.common.odom.ChangeQualifier#TEXT} change event
     * is fired.
     *
     * @note The {@link ODOMText#append(Text)} and {@link
     * ODOMCDATA#append(Text)} implementations are identical. This is
     * implemented this way to avoid garbage.
     */
    public void append(Text text) {
        String oldText = getText();

        super.append(text);

        String newText = getText();

        fireChange(oldText, newText, ChangeQualifier.TEXT);
    }

    /**
     * @note the clone will not have any listeners registered even if
     * the original CDATA did.
     */
    public Object clone() {
        // In order to avoid erroneous triggering of events during the cloning
        // process, and to ensure that the clone has a null change support
        // instance, temporarily detach the original's change support
        ODOMChangeSupport support = changeSupport;

        changeSupport = null;

        Object clone = super.clone();

        changeSupport = support;

        return clone;
    }

    // JavaDoc inherited
    public void detachObservable() {
        super.detach();
        if (changeSupport != null) {
            changeSupport.removeAllListeners();
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

 09-Jul-04	4841/1	adrianj	VBM:2004010802 Removal of ODOMValidatable infrastructure

 19-May-04	4429/5	claire	VBM:2004051401 Remove listeners for detached ODOMObservable instances

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 29-Jan-04	2689/1	eduardo	VBM:2003112407 undo/redo manager for ODOM

 07-Jan-04	2447/1	philws	VBM:2004010609 Initial code for revised validation mechanism

 15-Dec-03	2160/8	doug	VBM:2003120702 Addressed some rework issues

 15-Dec-03	2160/6	doug	VBM:2003120702 Modified ODOMObservables so that they cab validate themesevles.

 13-Dec-03	2123/6	allan	VBM:2003102005 Supermerged to fix log conflicts.

 12-Dec-03	2123/3	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 13-Dec-03	2198/3	doug	VBM:2003121003 Ensured ODOMObservables generate notification after the change has occurred

 08-Dec-03	2157/2	pcameron	VBM:2003111302 Added ElementAttributesSection

 07-Nov-03	1813/1	philws	VBM:2003110520 Add ODOMCDATA and provide correct clone feature

 ===========================================================================
*/
