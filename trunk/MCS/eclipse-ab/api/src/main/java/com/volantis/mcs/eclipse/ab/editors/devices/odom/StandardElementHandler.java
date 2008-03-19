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
package com.volantis.mcs.eclipse.ab.editors.devices.odom;

import java.util.List;

import com.volantis.devrep.repository.api.devices.DeviceRepositorySchemaConstants;
import com.volantis.mcs.eclipse.common.odom.ChangeQualifier;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeEvent;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeListener;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMFactory;
import com.volantis.mcs.eclipse.common.odom.ODOMObservable;
import org.jdom.Element;
import org.jdom.filter.Filter;
import org.jdom.input.JDOMFactory;

/**
 * This class is responsible for managing the standard element associated with
 * a given ODOMElement. The standard element is used to revert a modified
 * element back it's original state.
 * <STRONG>
 * Note that when you have finished using a StandardElementHandler instance,
 * you must call its {@link #dispose} method.
 * </STRONG>
 */
public class StandardElementHandler {

    /**
     * Used to factor ODOMObservable nodes
     */
    private static JDOMFactory JDOM_FACTORY = new ODOMFactory();

    /**
     * The name of the element that is being managed. This is not the tag
     * name but rather the value of a "name" attribute that the element
     * will have. Never null.
     */
    private final String name;

    /**
     * The element that is the parent of the element that this class is
     * managing. Never null.
     */
    private final ODOMElement parent;

    /**
     * This will reference the element that this class is managing. Could be
     * null if the element does not exist.
     */
    private DeviceODOMElement element;

    /**
     * This will be a clone of the managed element. Will be null if the element
     * did not exist when this handler was initialised.
     */
    private DeviceODOMElement originalElement;

    /**
     * A Filter that can be used to locate the managed element from the parent
     * node.
     */
    private Filter nameFilter;

    /**
     * Listner that will listen for the managed element being added to the
     * parent.
     */
    private ODOMChangeListener parentListener;

    /**
     * Listner that will listen for updates to the managed element.
     */
    private ODOMChangeListener elementListener;

    /**
     * Initializes a <code>StandardElementHandler</code> instance with the
     * given arguments
     * @param parent the ODOMelement for the parent of the element that this
     * class is managing. Cannot be null.
     * @param name the name of the element that this class is managing. Cannot
     * be null.
     * @throws java.lang.IllegalArgumentException if either of the arguments are null.
     */
    public StandardElementHandler(final ODOMElement parent,
                                  final String name) {
        // parent and name arguments cannot be null
        if (parent == null) {
            throw new IllegalArgumentException("Cannot be null: parent");
        }
        if (name == null) {
            throw new IllegalArgumentException("Cannot be null: name");
        }
        this.parent = parent;
        this.name = name;

        // this Filter will only match those elements that hava a "name"
        // attribute whose value is equal the name of the element being
        // managed
        nameFilter = new Filter() {
            // javadoc inherited
            public boolean matches(Object candidate) {
                boolean matched = false;
                if (candidate instanceof ODOMElement) {
                    // match if the element has a name attribute with the
                    // same value as the name argument
                    // @todo device names and policy names both required so perhaps should pass in the attribute name
                    matched = name.equals(
                            ((ODOMElement) candidate).getAttributeValue(
                                    DeviceRepositorySchemaConstants.
                            POLICY_NAME_ATTRIBUTE));
                }
                return matched;
            }
        };

        // this listener will listen for updates to the managed element
        elementListener = new ODOMChangeListener() {
            // javadoc inherited
            public void changed(ODOMObservable node,
                                ODOMChangeEvent event) {
                // three types of changes can occur.
                // 1. The managed element could be deleted. We need to handle
                //    this by removing the listener from the element.
                // 2. The managed element could be modified. If this occurs
                //    then we might need to add a standard element.
                // 3. The managed element could be created. This will never
                //    happen as this listener will not be registered when the
                //    managed element does not exist.
                if (event.getOldValue() == parent &&
                        event.getNewValue() == null &&
                        event.getSource() == element) {
                    // the managed element has been delelted
                    // unregister this listener with the element
                    // Note: we don't remove the parentListener from the
                    // parent element as this
                    element.removeChangeListener(elementListener);

                } else if(event.getSource().getName().
                        equals(DeviceRepositorySchemaConstants.
                        STANDARD_ELEMENT_NAME) &&
                        event.getNewValue()==null) {
                    // Here we have the content of the standard element
                    // being removed. This can only happen here if an
                    // undo/redo is in progresss. In this case the
                    // standard element is no longer relevant and is
                    // about to be deleted itself.
                    element.removeChangeListener(elementListener);
                } else {
                    // the managed element has been modified. See if it has
                    // a standard element
                    if (findStandardElement(element) == null) {
                        // this is the first time the element has changed so
                        // we need to add the standard element to the managed
                        // element
                        ((ODOMElement) element.getParent()).
                                removeChangeListener(parentListener,
                                        ChangeQualifier.HIERARCHY);
                        element.removeChangeListener(elementListener);
                        element.getChildren().add(
                                createStandardElement(originalElement));
                        element.addChangeListener(elementListener);
                        ((ODOMElement) element.getParent()).
                                addChangeListener(parentListener,
                                        ChangeQualifier.HIERARCHY);
                    }
                }
            }
        };


        // This will listen for the creation of the managed element.
        parentListener = new ODOMChangeListener() {
            // javadoc inherited
            public void changed(ODOMObservable node,
                                ODOMChangeEvent event) {
                // only interested if the element does not exist and the event
                // indicates a node has been added
                if (element == null &&
                        event.getOldValue() == null &&
                        event.getNewValue() == parent) {
                    // see if the node added is the element that is being
                    // managed
                    if (nameFilter.matches(event.getSource())) {
                        // The element that is being managed has just
                        // beed added to the parent.
                        element = (DeviceODOMElement) event.getSource();
                        // create a standard element (pass in null so that
                        // an "empty" standard element is created indicating
                        // that the element did not originally exist
                        ((ODOMElement) element.getParent()).
                                removeChangeListener(parentListener,
                                        ChangeQualifier.HIERARCHY);
                        Element standardElement = createStandardElement(null);
                        // add the standard element to the managed element
                        element.getChildren().add(standardElement);

                        element.setStandardElementHandler(
                                StandardElementHandler.this);

                        // finally register the element listener with the
                        // element
                        element.addChangeListener(elementListener);
                        ((ODOMElement) element.getParent()).
                                addChangeListener(parentListener,
                                        ChangeQualifier.HIERARCHY);
                    }
                }
            }
        };

        // see if the element being managed already exists
        element = findManagedElement(parent);
        if (element != null) {
            // take a copy of the original element so that we are able
            // to create a standard element if needed
            originalElement = (DeviceODOMElement) element.clone();

            element.setStandardElementHandler(
                    StandardElementHandler.this);

            // register the listener with element
            element.addChangeListener(elementListener);
        }
        // register the parent listener with the parent element
        parent.addChangeListener(parentListener, ChangeQualifier.HIERARCHY);
    }

    /**
     * Restores the managed element to its original value. This also
     * moves listeners from the managed element back onto the original restored
     * element.
     *
     * NOTE: this method will synchronize on the parent ODOMElement prior
     * to the restore operation. This prevents XPathExceptions that can occur
     * if validation is underway or starts before the restore operation is
     * complete.
     */
    public void restore() {
        // see if the element has a standard element. If no standard
        // element exists then we don't have anything to restore.
        Element standardElement = findStandardElement(element);
        if (standardElement != null) {
            synchronized (parent) {
                ODOMElement restoreElement = null;
                // remove the listeners
                parent.removeChangeListener(parentListener,
                        ChangeQualifier.HIERARCHY);
                element.removeChangeListener(elementListener);
                // find the index of the managed element in the parents content
                // list
                int elementIndex = parent.getContent().indexOf(element);

                // get the content of the standard element. If it has not content
                // then the element originally did not exist.
                List standardChildren = standardElement.getChildren();
                if (!standardChildren.isEmpty()) {
                    // need to check that there is only one child
                    if (standardChildren.size() > 1) {
                        throw new IllegalStateException(
                                "The standard element has more than one child");
                    }
                    // get the element that will be restored
                    restoreElement =
                            (ODOMElement) standardChildren.get(0);

                    // Move all listeners from the current element to the
                    // restored element.
                    element.moveListeners(restoreElement);

                    // we need to detach this from the standard element
                    restoreElement.detach();
                    // updated the parent so that the restored element is
                    // restored to the correct index
                    parent.getContent().add(elementIndex, restoreElement);
                }

                // Detach the managed element from its parent.
                element.detach();
                // detach the standard element from the managed element
                standardElement.detach();

                // If restoreElement is null, this means that element was a brand
                // new element that did not previously exist. Therefore, since
                // element has just been detached above, there is no further work
                // to do. On the other hand, if restoreElement is non-null, then
                // make it the current element of the handler.
                element = (DeviceODOMElement) restoreElement;
                if (element != null) {
                    // update the element reference so that it references the
                    // restored value
                    originalElement = (DeviceODOMElement) element.clone();
                    element.setStandardElementHandler(this);
                    element.addChangeListener(elementListener);
                }
                parent.addChangeListener(parentListener,
                        ChangeQualifier.HIERARCHY);
            }
        }
    }


    /**
     * Creates a "standard" element whose contents are the ODOMElement argument
     * @param content the content for the standard element. If null the standard
     * element will not have any content
     * @return a "standard" ODOMElement
     */
    private ODOMElement createStandardElement(DeviceODOMElement content) {
        // create the standard element
        ODOMElement standard = (ODOMElement) JDOM_FACTORY.element(
                DeviceRepositorySchemaConstants.STANDARD_ELEMENT_NAME,
                parent.getNamespace());
        if (content != null) {
            // Ensure that the element within the standard element does
            // not itself have a StandardElementHandler in order to avoid
            // recursive change handling. The standard handler will be set
            // on the contained element if it is restored (see restore()).
            content.setStandardElementHandler(null);
            standard.addContent(content);
        }
        return standard;
    }

    /**
     * Returns a standard element child of the root argument or null if there
     * is no standard element
     * @param root the root element. Can be null.
     * @return the standard element or null if one does not exist.
     */
    private ODOMElement findStandardElement(ODOMElement root) {
        ODOMElement standardElement = null;
        if (root != null) {
            List standardElements = root.getChildren(
                    DeviceRepositorySchemaConstants.STANDARD_ELEMENT_NAME,
                    root.getNamespace());
            int count = standardElements.size();
            if (count > 1) {
                throw new IllegalStateException(
                        "Found more than one standard element");
            }
            standardElement = (count == 0) ? null :
                    (ODOMElement) standardElements.get(0);
        }

        return standardElement;
    }

    /**
     * Will find an ODOMElement that has a name attribute whose value equals
     * the name parameter and is a child of the root argument
     * @return the element that was found or null if it could not be located.
     * @throws java.lang.IllegalStateException if more than one element matched
     */
    private DeviceODOMElement findManagedElement(Element root) {
        List elements = root.getContent(nameFilter);
        int count = elements.size();
        if (count > 1) {
            throw new IllegalStateException(
                    "Found more than one child element with a name " +
                    "attribute value of " + name);
        }
        return (count == 0) ? null : (DeviceODOMElement) elements.get(0);
    }

    /**
     * Removes internal ODOMChangeListeners used by StandardElementHandler.
     * This method should be called when a user is finished with this instance
     * of StandardElementHandler.
     */
    public void dispose() {
        if (element != null) {
            element.removeChangeListener(elementListener);
        }
        parent.removeChangeListener(parentListener,
                        ChangeQualifier.HIERARCHY);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 16-Nov-04	4394/7	allan	VBM:2004051018 Undo/Redo in device editor.

 16-Nov-04	4394/4	allan	VBM:2004051018 Undo/Redo in device editor.

 14-May-04	4394/1	allan	VBM:2004051018 StandardElement handler re-write. Undo/redo nearly working.

 14-May-04	4301/6	byron	VBM:2004051018 CC/PP section does not handle undo/redo

 13-May-04	4301/2	byron	VBM:2004051018 CC/PP section does not handle undo/redo

 13-May-04	4351/1	allan	VBM:2004051011 Fix NullPointerException in StandardElementHandler

 12-May-04	4307/1	allan	VBM:2004051201 Fix restore button and moveListeners()

 11-May-04	4250/8	pcameron	VBM:2004051005 Added Restore Defaults button and changed ODOMElement and StandardElementHandler to deal with listener removal

 10-May-04	4210/1	matthew	VBM:2004050502 change DevicesDeviceMigrationjob.java to not write out attributes that are empty

 14-Apr-04	3683/9	pcameron	VBM:2004030401 Some tweaks to PolicyController and refactoring of PolicyOriginSelection

 13-Apr-04	3683/3	pcameron	VBM:2004030401 Added PolicyController

 14-Apr-04	3693/5	doug	VBM:2004031001 fixed problem with event identification

 13-Apr-04	3693/3	doug	VBM:2004031001 Fixed bug where a standard element was not being created when the managed element was created for the first time

 05-Apr-04	3693/1	doug	VBM:2004031001 MCS

 ===========================================================================
*/
