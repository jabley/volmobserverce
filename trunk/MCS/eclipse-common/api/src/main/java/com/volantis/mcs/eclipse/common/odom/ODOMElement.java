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

import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.mcs.xml.xpath.XPathException;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * This is an observable implementation of the JDOM Element.
 */
public class ODOMElement extends Element implements ODOMObservable {
    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(ODOMElement.class);


    /**
     * The name to represent elements that are null.
     */
    public static final String NULL_ELEMENT_NAME = "null"; //$NON-NLS-1$

    /**
     * The name for elements that are undefined.
     */
    public static final String UNDEFINED_ELEMENT_NAME =
            "undefined"; //$NON-NLS-1$

    /**
     * The change support object responsible for notifying all registered
     * listeners. This is lazily created by the {@link ODOMObservable}
     * interface implementation when the first observer is registered.
     *
     * @supplierCardinality 0..1
     * @supplierRole changeSupport
     * @link aggregation
     */
    protected ODOMChangeSupport changeSupport;

    /**
     * Initializes the new instance with default values.
     */
    protected ODOMElement() {
    }

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param name      the element's name
     * @param namespace the element's namespace
     */
    public ODOMElement(String name, Namespace namespace) {
        super(name, namespace);
    }

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param name the element's name
     */
    public ODOMElement(String name) {
        super(name);
    }

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param name the element's name
     * @param uri  the element's namespace URI
     */
    public ODOMElement(String name, String uri) {
        super(name, uri);
    }

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param name   the element's name
     * @param prefix the element's namespace prefix
     * @param uri    the element's namespace URI
     */
    public ODOMElement(String name, String prefix, String uri) {
        super(name, prefix, uri);
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
     * ODOMAttribute#removeChangeListener}, {@link
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
        ODOMChangeEvent event = null;

        if (ODOMChangeSupport.changeSupportEnabled()) {

            event = fireChange(ODOMChangeEvent.createNew(this,
                    oldValue,
                    newValue,
                    changeQualifier));
        }

        return event;
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

        ODOMChangeEvent result = null;
        if (ODOMChangeSupport.changeSupportEnabled()) {

            Object oldValue = event.getOldValue();
            Object newValue = event.getNewValue();

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
    protected void notifyParent(Element parent, ODOMChangeEvent event) {

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
    // Element method augmentation to trigger change events
    //-------------------------------------------------------------------------

    /**
     * Augments {@link Element#setName} to ensure that a {@link
     * com.volantis.mcs.eclipse.common.odom.ChangeQualifier#NAME} change event
     * is fired.
     */
    public Element setName(String name) {
        String oldName = getName();

        Element result = super.setName(name);

        fireChange(oldName, name, ChangeQualifier.NAME);

        return result;
    }

    /**
     * Augments {@link Element#setNamespace} to ensure that a {@link
     * com.volantis.mcs.eclipse.common.odom.ChangeQualifier#NAMESPACE} change
     * event is fired.
     */
    public Element setNamespace(Namespace namespace) {
        Namespace oldNamespace = getNamespace();

        Element result = super.setNamespace(namespace);

        fireChange(oldNamespace, namespace, ChangeQualifier.NAMESPACE);

        return result;
    }

    /**
     * Augments {@link Element#setParent} to ensure that a {@link
     * com.volantis.mcs.eclipse.common.odom.ChangeQualifier#HIERARCHY} change
     * event is fired.
     */
    protected Element setParent(Element parent) {
        return setParent(parent, -1);
    }

    protected Element setParent(Element parent, int indexAsRemovedChild) {
        // obtain a reference to the old parent as we will need to notify it
        // that it has changed.
        Element oldParent = getParent();
        // set the new parent
        Element result = super.setParent(parent);

        if (oldParent != null || parent != null) {
            // fire a change event. This will notify the new parent that the
            // hierarchy has changed
            ODOMChangeEvent event =
                    fireChange(oldParent, parent, ChangeQualifier.HIERARCHY);

            if (event != null) {
                // notify the old parent of the change.
                notifyParent(oldParent, event);
            }
        }

        return result;
    }

    /**
     * This will cause a {@link
     * com.volantis.mcs.eclipse.common.odom.ChangeQualifier#HIERARCHY} event
     * to be fired.
     */
    public Element detach() {
        // No event trigger is required here since the superclass version will
        // end up calling {@link Element#setParent} with null anyway
        Element detachedElement = super.detach();
        if (detachedElement != null) {
            // Handle recursively warning about listeners
            recurseOverListeners(this, new ListenersCommand() {
                public void doDetach(ODOMChangeSupport changeSupport) {
                    if (changeSupport != null) {
                        changeSupport.outputListenerWarnings();
                    }
                }
            });
        }
        return detachedElement;
    }

    /**
     * Replaces the superclass implementation to ensure that an {@link
     * ODOMText} is added. Note that this is likely to cause multiple {@link
     * com.volantis.mcs.eclipse.common.odom.ChangeQualifier#HIERARCHY} events
     * to be fired (one per existing piece of
     * content to remove it and one for the new Text node).
     */
    public Element setText(String text) {
        // No event trigger is required here since the superclass version will
        // end up calling {@link #setParent} with null for all current
        // content (Element and Text) and then {@link Text#setParent} with
        // this object

        // However, the type of Text node created must be ODOM...

        // Force the content to be cleared first (we don't have direct access
        // to the {@link ContentList#clear} method so call the superclass
        // to just remove the content)
        super.setText(null);

        if (text != null) {
            addContent(new ODOMText(text));
        }

        return this;
    }

    /**
     * Replaces the superclass implementation to ensure that an {@link
     * ODOMText} is added. This will cause a
     * {@link com.volantis.mcs.eclipse.common.odom.ChangeQualifier#HIERARCHY}
     * event to be fired.
     */
    public Element addContent(String str) {
        // No event trigger is required here since the superclass version will
        // end up calling {@link Element#addContent(Text)}.

        // However, the type of Text node created must be ODOM...
        return addContent(new ODOMText(str));
    }

    /**
     * This will cause a
     * {@link com.volantis.mcs.eclipse.common.odom.ChangeQualifier#HIERARCHY}
     * event to be fired.
     */
    public Element addContent(Text text) {
        // No event trigger is required here since the superclass version will
        // end up calling {@link Text#setParent} with this object
        return super.addContent(text);
    }

    /**
     * This will cause a {@link
     * com.volantis.mcs.eclipse.common.odom.ChangeQualifier#HIERARCHY} event
     * to be fired.
     */
    public Element addContent(Element element) {
        // No event trigger is required here since the superclass version will
        // end up calling {@link Element#setParent} with this object
        Element newElement = super.addContent(element);

        return newElement;
    }

    /**
     * This will cause a {@link
     * com.volantis.mcs.eclipse.common.odom.ChangeQualifier#HIERARCHY} event
     * to be fired.
     */
    public boolean removeChild(String name) {
        // No event trigger is required here since the superclass version will
        // end up calling {@link Element#removeChild(String,Namespace)}
        return super.removeChild(name);
    }

    /**
     * This will cause a {@link
     * com.volantis.mcs.eclipse.common.odom.ChangeQualifier#HIERARCHY} event
     * to be fired.
     */
    public boolean removeChild(String name, Namespace ns) {
        // No event trigger is required here since the superclass version will
        // end up calling {@link #setParent} with null for the specified
        // child (Element and Text)
        return super.removeChild(name, ns);
    }

    /**
     * This will cause multiple {@link
     * com.volantis.mcs.eclipse.common.odom.ChangeQualifier#HIERARCHY} events
     * to be fired (one per existing child).
     */
    public boolean removeChildren(String name) {
        // No event trigger is required here since the superclass version will
        // end up calling {@link Element#removeChildren(String,Namespace)}
        return super.removeChildren(name);
    }

    /**
     * This will cause multiple {@link
     * com.volantis.mcs.eclipse.common.odom.ChangeQualifier#HIERARCHY} events
     * to be fired (one per existing child).
     */
    public boolean removeChildren(String name, Namespace ns) {
        // No event trigger is required here since the superclass version will
        // end up calling {@link #setParent} with null for the specified
        // child(ren) (Element and Text)
        return super.removeChildren(name, ns);
    }

    /**
     * This will cause multiple {@link
     * com.volantis.mcs.eclipse.common.odom.ChangeQualifier#HIERARCHY} events
     * to be fired (one per existing attribute plus one per new attribute).
     */
    public Element setAttributes(List newAttributes) {
        // No event trigger is required here since the superclass version will
        // end up calling {@link Attribute#setParent} with null for the current
        // attributes followed by calling {@link Attribute#setParent} with
        // this object for all new attributes
        return super.setAttributes(newAttributes);
    }

    /**
     * Replaces the superclass implementation to ensure that an {@link
     * ODOMAttribute} is added. This overloading attempts to treat the
     * set request as a value update if possible, i.e. it checks first
     * for the existence of an attribute of the given name, and, if it
     * exists, updates its value to the given value. In this case, a {@link
     * com.volantis.mcs.eclipse.common.odom.ChangeQualifier#ATTRIBUTE_VALUE}
     * event will be fired. If the attribute does not exist, then a new
     * attribute is created from the parameter data and stored in the element.
     * In this case, a {@link
     * com.volantis.mcs.eclipse.common.odom.ChangeQualifier#HIERARCHY} event
     * will be fired for the new attribute.
     *
     * @param name  the name of the attribute to set
     * @param value the value of the attribute to set
     * @return the element updated (this)
     */
    public Element setAttribute(String name, String value) {

        // Look for the attribute with the name in the parameter
        Element result = this;
        final Attribute existingAttribute = super.getAttribute(name);

        // If the attribute exists, just update its value; otherwise,
        // construct a new attribute and set it in the element
        if (existingAttribute != null) {
            existingAttribute.setValue(value);
        } else {
            result = super.setAttribute(new ODOMAttribute(name, value));
        }
        return result;
    }

    /**
     * Identical in all respects to {@link #setAttribute(String,String)}
     * except that the given namespace is applied to the check for the
     * pre-existence of the attribute and (if applicable) the creation of
     * the new attribute.
     *
     * @param name  the name of the attribute to set
     * @param value the value of the attribute to set
     * @param ns    the attribute namespace
     * @return the element updated (this)
     */
    public Element setAttribute(String name, String value, Namespace ns) {

        // Look for the attribute with the name in the parameter
        Element result = this;
        final Attribute existingAttribute = super.getAttribute(name, ns);

        // If the attribute exists, just update its value; otherwise,
        // construct a new attribute and set it in the element
        if (existingAttribute != null) {
            existingAttribute.setValue(value);
        } else {
            result = super.setAttribute(new ODOMAttribute(name, value, ns));
        }
        return result;
    }

    /**
     * Replaces the superclass implementation to ensure that an {@link
     * ODOMAttribute} is added. This will potentially cause multiple {@link
     * com.volantis.mcs.eclipse.common.odom.ChangeQualifier#HIERARCHY} events
     * to be fired (one for any previous attribute of the same name, if any,
     * and one for the new attribute): note that unlike
     * {@link Element#setAttribute(String,String)}, this
     * method does not first check for the existence of an attribute with
     * the same name as the name of the attribute supplied; for that
     * behaviour, that overloading should be used.
     *
     * @param attribute the attribute to set on the element
     * @return the element updated (this)
     */
    public Element setAttribute(Attribute attribute) {
        // No event trigger is required here since the superclass version will
        // end up calling {@link Attribute#setParent} with null for any
        // existing (duplicate) attribute followed by calling
        // {@link Attribute#setParent} with this object for the new attribute
        return super.setAttribute(attribute);
    }

    /**
     * This will cause a {@link
     * com.volantis.mcs.eclipse.common.odom.ChangeQualifier#HIERARCHY} event
     * to be fired.
     */
    public boolean removeAttribute(String name) {
        // No event trigger is required here since the superclass version will
        // end up calling {@link Attribute#setParent} with null
        return super.removeAttribute(name);
    }

    /**
     * This will cause a {@link
     * com.volantis.mcs.eclipse.common.odom.ChangeQualifier#HIERARCHY} event
     * to be fired.
     */
    public boolean removeAttribute(String name, Namespace ns) {
        // No event trigger is required here since the superclass version will
        // end up calling {@link Attribute#setParent} with null
        return super.removeAttribute(name, ns);
    }

    /**
     * This will cause a {@link
     * com.volantis.mcs.eclipse.common.odom.ChangeQualifier#HIERARCHY} event
     * to be fired.
     */
    public boolean removeAttribute(Attribute attribute) {
        // No event trigger is required here since the superclass version will
        // end up calling {@link Attribute#setParent} with null
        return super.removeAttribute(attribute);
    }

    /**
     * This will cause a {@link
     * com.volantis.mcs.eclipse.common.odom.ChangeQualifier#HIERARCHY} event
     * to be fired.
     */
    public boolean removeContent(Element element) {
        // No event trigger is required here since the superclass version will
        // end up calling {@link Element#setParent} with null
        return super.removeContent(element);
    }

    /**
     * This will cause a {@link
     * com.volantis.mcs.eclipse.common.odom.ChangeQualifier#HIERARCHY} event
     * to be fired.
     */
    public boolean removeContent(Text text) {
        // No event trigger is required here since the superclass version will
        // end up calling {@link Text#setParent} with null
        return super.removeContent(text);
    }

    /**
     * @note the (deep) clone will not have any listeners registered even if
     * the original element did.
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

    /**
     * Moves {@link ODOMChangeSupport}-registered listeners from
     * this ODOMElement (the source) to <code>toElement</code> as follows:
     * 1. For each child element of this ODOMElement
     * a. If the child also exists in toElement then recursively move
     * listeners between the two children.
     * b. Otherwise, the destination has no corresponding child, so remove
     * all listeners from the <code>toElement</code> child.
     * 2. Remove all the listeners from this ODOMElement.
     *
     * @param toElement the destination element for the listeners
     * @todo This method does not currently move listeners on ODOMAttributes,
     * ODOMTexts or other ODOMObservables. To deal with these other nodes and
     * listeners, you probably ought to use a {@link Walker} to visit each node.
     */
    public synchronized void moveListeners(ODOMElement toElement) {
        if (!toElement.getName().equals(getName())) {
            throw new IllegalArgumentException("Element to move listeners to " +
                    "must be of same type. Expected a " + getName() + " but" +
                    " received a " + toElement.getName());
        }
        List children = getChildren();
        Iterator childIterator = children.iterator();

        Namespace namespace[] = new Namespace[]{getNamespace()};

        while (childIterator.hasNext()) {
            ODOMElement child = (ODOMElement) childIterator.next();
            // Get the relative XPath of the child.
            XPath childXPath = new XPath((Element) child, namespace);

            try {
                // Does the relative path find an element in the toElement?
                ODOMElement toChild = (ODOMElement)
                        childXPath.selectSingleNode((Element) toElement);

                if (toChild != null) {
                    // Corresponding child also exists in the source element,
                    // so recursively move listeners between the two children.
                    child.moveListeners(toChild);
                } else {
                    // Source element does not contain a corresponding child,
                    // so remove all listeners (if any) from the destination
                    // child.
                    ODOMChangeSupport childChangeSupport = child.changeSupport;
                    if (childChangeSupport != null) {
                        childChangeSupport.setChangeListeners(null);
                        childChangeSupport.setQualifiedChangeListeners(null);
                    }
                }
            } catch (XPathException e) {
                throw new UnsupportedOperationException();
            }
        }

        if (changeSupport != null) {
            List listeners = changeSupport.getChangeListeners();
            Map qListeners = changeSupport.getQualifiedChangeListeners();
            changeSupport.setChangeListeners(null);
            changeSupport.setQualifiedChangeListeners(null);

            toElement.initChangeSupport(); // ensure there is a change support

            toElement.changeSupport
                    .setChangeListeners(new ArrayList(listeners));
            toElement.changeSupport.setQualifiedChangeListeners(qListeners);
        }
    }

    // JavaDoc inherited
    public void detachObservable() {
        super.detach();
        // Handle recursively removing listeners
        recurseOverListeners(this, new ListenersCommand() {
            public void doDetach(ODOMChangeSupport changeSupport) {
                if (changeSupport != null) {
                    changeSupport.removeAllListeners();
                }
            }
        });
    }

    /**
     * A utility method that applies a command to the element provided and
     * then also recurses over any children that the provided element has and
     * applies the same command to them.
     *
     * @param currentElement The element to operate on, and whose children
     *                       should also be operated on.
     * @param command        The command to apply to the element(s).
     */
    private void recurseOverListeners(ODOMElement currentElement,
                                      ListenersCommand command) {
        command.doDetach(currentElement.changeSupport);
        Iterator childIterator = currentElement.getChildren().iterator();
        while (childIterator.hasNext()) {
            ODOMElement child = (ODOMElement) childIterator.next();
            recurseOverListeners(child, command);
        }
    }

    /**
     * This is a means of facilitating different behaviours when detaching
     * listeners within {@link ODOMObservable#detachObservable} and
     * {@link org.jdom.Element#detach} and other <code>org.jdom.*</code>
     * classes that implement a <code>detach()</code> method.
     */
    public interface ListenersCommand {

        /**
         * Do the detach operation and process the detached elements and
         * reference to them as necessary
         *
         * @param changeSupport The change support object responsible for
         *                      handling the listeners.
         */
        void doDetach(ODOMChangeSupport changeSupport);
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Oct-05	9734/1	adrianj	VBM:2005100510 Allow text in ProxyElements

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 16-Nov-04	4394/2	allan	VBM:2004051018 Undo/Redo in device editor.

 13-May-04	4301/3	byron	VBM:2004051018 CC/PP section does not handle undo/redo

 09-Jul-04	4841/1	adrianj	VBM:2004010802 Removal of ODOMValidatable infrastructure

 19-May-04	4429/5	claire	VBM:2004051401 Remove listeners for detached ODOMObservable instances

 12-May-04	4307/1	allan	VBM:2004051201 Fix restore button and moveListeners()

 11-May-04	4250/5	pcameron	VBM:2004051005 Added Restore Defaults button and changed ODOMElement and StandardElementHandler to deal with listener removal

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 29-Jan-04	2689/2	eduardo	VBM:2003112407 undo/redo manager for ODOM

 07-Jan-04	2447/1	philws	VBM:2004010609 Initial code for revised validation mechanism

 15-Dec-03	2223/1	richardc	VBM:2003121203 setAttribute(String,String) checks for existence of attribute first

 15-Dec-03	2160/10	doug	VBM:2003120702 Addressed some rework issues

 15-Dec-03	2160/8	doug	VBM:2003120702 Modified ODOMObservables so that they cab validate themesevles.

 13-Dec-03	2123/8	allan	VBM:2003102005 Supermerged to fix log conflicts.

 12-Dec-03	2123/5	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 13-Dec-03	2198/4	doug	VBM:2003121003 Ensured ODOMObservables generate notification after the change has occurred

 09-Dec-03	2170/1	pcameron	VBM:2003102103 Added AssetTypeSection

 07-Nov-03	1813/1	philws	VBM:2003110520 Add ODOMCDATA and provide correct clone feature

 04-Nov-03	1613/2	philws	VBM:2003102101 Observable DOM

 ===========================================================================
*/
