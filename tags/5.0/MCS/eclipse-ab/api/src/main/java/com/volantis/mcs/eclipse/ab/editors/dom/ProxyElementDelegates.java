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
package com.volantis.mcs.eclipse.ab.editors.dom;

import com.volantis.mcs.eclipse.common.odom.ChangeQualifier;
import com.volantis.mcs.eclipse.common.odom.ODOMAttribute;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeEvent;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeListener;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMElementSelectionEvent;
import com.volantis.mcs.eclipse.common.odom.ODOMFactory;
import com.volantis.mcs.eclipse.common.odom.ODOMObservable;
import com.volantis.mcs.eclipse.common.odom.ODOMText;
import com.volantis.mcs.eclipse.common.odom.xpath.ODOMXPath;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.mcs.xml.xpath.XPathException;

import org.jdom.Element;
import org.jdom.Namespace;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * This class performs two basic functions:
 * 
 * <ol><li>To encapsulate the proxy element source/target pairs (populated
 * from a selection event) and to perform all listening on them. The source
 * elements are listened to for hierarchy changes (to detect target element
 * or target attribute additions or removals), the target elements are listened
 * to for element name changes, and the target attrubutes are listened to
 * for attribute value changes. These events are suitably checked and
 * filtered, and the processed results are passed to the client-owned
 * instance of the {@link com.volantis.mcs.eclipse.ab.editors.dom.AggregationListener} interface.
 * </li>
 * 
 * <li>To allow the propagation of element names or attribute values to
 * all the encapsulated target elements.</li></ol></p>
 */
public class ProxyElementDelegates {

    /**
     * This simply stores the source/target pairs in a typesafe way: it does
     * not provide listening facilities
     */
    private static class SourceTargetPairs {
        
        /**
         * The "sources" part of the stored set of source/target pairs.
         * Note that the sources and targets are maintained in separate lists
         * (rather than one list of pairs) to leverage existing List
         * functionality to facilitate, amongst other things, the searching
         * for an individual target passed in by the client, or the returning
         * of an iterator over all targets. The target associated with a
         * given source is accessed using the same index value originally
         * used to access the source. See {@link #targets}.
         */
        private final List sources = new ArrayList();
        
        /**
         * The "targets" part of the stored set of source/target pairs.
         * See {@link #sources}.
         */
        private final List targets = new ArrayList();

        /**
         * Clears the list of source/target pairs which are stored for the set
         * of proxied elements. Since this class is no more than a simple pair
         * list storage bucket, the caller is expected to manage listener
         * removal before calling this method
         */
        public void clear() {
            sources.clear();
            targets.clear();
        }

        /**
         * Adds a source/target pair to the pairs list encapsulated by this
         * class: the source must be non-null and different from all other
         * sources passed to this method (until clear() is called); the target
         * may be null but if not must be different from all other targets
         * passed to this  method (until clear() is called)
         * 
         * @param source The source part of the source/target pair
         * @param target The target part of the source/target pair
         * @throws IllegalArgumentException if the constraint(s) described
         * above are violated
         */
        public void add(ODOMElement source, ODOMElement target)
            throws IllegalArgumentException {
            if (source == null) {
                throw new IllegalArgumentException("Source is null");
            } else if (isSourceElement(source)) {
                throw new IllegalArgumentException("Source already exists");
            } else if (target != null && isTargetElement(target)) {
                throw new IllegalArgumentException("Target already exists");
            } else {
                sources.add(source);
                targets.add(target);
            }
        }

        // javadoc unnecessary
        public int size() {
            return sources.size();
        }

        // javadoc unnecessary
        public ODOMElement getSource(int index) {
            return (ODOMElement) sources.get(index);
        }

        // javadoc unnecessary
        public ODOMElement getTarget(int index) {
            return (ODOMElement) targets.get(index);
        }

        // javadoc unnecessary
        public List getTargets() {
            return targets;
        }

        // javadoc unnecessary
        public void setTarget(int index, ODOMElement target) {
            targets.set(index, target);
        }

        // javadoc unnecessary
        public boolean isSourceElement(Element element) {
            return sources.contains(element);
        }

        // javadoc unnecessary
        public boolean isTargetElement(Element element) {
            // Targets can validly be null, so prevent a spuriously
            // successful test of a target where a null is passed in
            if(element == null) {
                throw new IllegalArgumentException(
                    "isTargetElement: Error to test for null");
            }
            return targets.contains(element);
        }

        // javadoc unnecessary
        public int getSourceIndex(ODOMElement source) {
            return sources.indexOf(source);
        }
    }

    /**
     * Who gets notified of changes to supported (aggregated) attributes
     */
    private final AggregationListener aggregationListener;

    /**
     * The supporting ProxyElementDetails: used for determining which
     * attributes are to be supported, etc.
     */
    private final ProxyElementDetails proxyElementDetails;

    /**
     * The factory for creating attributes and elements
     */
    private final ODOMFactory odomFactory;

    /**
     * Manages the source/target pairs: see the class comments
     */
    private final SourceTargetPairs
            sourceTargetPairs = new SourceTargetPairs();

    /**
     * Used internally to control whether events generated from the
     * source/target pairs are ignored: this is simpler than de-registering
     * as a listener from all of them and then registering again
     */
    private boolean ignoreChangeEvents = false;

    /**
     * Enables navigation from a source element to a target element: this
     * may remain set to null if no such navigation is required, i.e. when
     * the source and target in a source/target pair are the same element
     */
    private ODOMXPath sourceToTargetPath = null;
    
    /**
     * This is the current set of supported (aka aggregated) attributes
     * returned from the ProxyElementDetails object: it is initially set
     * to null to indicate that we have not yet got this from the details
     * object for the first time (it is not sufficient to set it to an
     * empty list, as that is potentially returnable from the details object)
     */
    private String[] detailsAttributeNames = null;

    /**
     * This is the element name returned from the ProxyElementDetails object,
     * set to null to indicate that we have not yet got this from that object
     */
    String detailsElementName = null;

    /**
     * This is the element namespace returned from the ProxyElementDetails
     * object, set to null to indicate that we have not yet got this from
     * that object
     */
    Namespace detailsElementNamespace = null;

    /**
     * This is an ODOMChangeListener instance that is used to listen to
     * hierarchy changes on the source elements and attribute value changes
     * on the target elements. An anonymous class is used to keep the
     * changed method private.
     *
     * The events listened to are hierarchical events on the source elements
     * (for target element and attribute addition and removal) and attribute
     * value change events (for attributes on the target).
     */
    private final ODOMChangeListener changeListener =
        new ODOMChangeListener() {

        // javadoc inherited
        public void changed
            (ODOMObservable node, ODOMChangeEvent event) {
            if (!ignoreChangeEvents) {
                processChangeEvent(node, event);
            }
        }
    };

    /**
     * Constructs an instance of this class. See the parameter comments.
     *
     * @param proxyElementDetails This instance is used by this class to
     * determine which attributes in the targets should be supported (aka
     * aggregated). This instance is also written to by this class to update
     * it with the set of actual proxied elements (from which it re-calculates
     * the set of supported attributes).
     * @param aggregationListener Passed in by the ProxyElement to enable
     * the instance of that class to manage the lifecycle and values
     * of its set of attributes that maintain the aggregated values of the
     * supported attributes in the target.
     * @throws java.lang.IllegalArgumentException if any argument is null
     */
    public ProxyElementDelegates(
        ODOMFactory odomFactory,
        ProxyElementDetails proxyElementDetails,
        AggregationListener aggregationListener) {

        // Set up the instance fields
        if (odomFactory == null) {
            throw new IllegalArgumentException("Null ODOMFactory");
        }
        this.odomFactory = odomFactory;
        if (proxyElementDetails == null) {
            throw new IllegalArgumentException("Null ProxyElementDetails");
        }
        this.proxyElementDetails = proxyElementDetails;
        if (aggregationListener == null) {
            throw new IllegalArgumentException("Null AggregationListener");
        }
        this.aggregationListener = aggregationListener;
    }
    
    /**
     * This method returns the current count of source/target pairs, which is
     * equivalent to the size of the "source" elements in the last selection
     * passed to this instance
     * 
     * @return See method description
     */
    public int size() {
        return sourceTargetPairs.size();
    }

    /**
     * </p>The ProxyElement simply delegates is selection changed event
     * to this method. Following a call to this method, the ProxyElement's
     * instance of AggregationListener will receive a sequence of zero or
     * more newAttribute and
     * deletedAttribute method calls. For example, if prior to this call the
     * ProxyElement had attributes with names A, B, and C, then calls this
     * method and receives newAttribute(D, dVal) followed by
     * deletedAttribute(A), it should then have attributes B, C and D.
     * These attributes should remain in existence until the ProxyElement
     * re-calls this method and gets (possibly) another deletedAttribute
     * callback.</p>
     *
     * <p>This method stores the proxy target element pairs and listens to
     * the following events on them: hierarchy changes on the sources (for
     * target element and attribute additions and deletions); attribute value
     * changes on the targets; and element name changes on the targets.
     * </p>
     *
     * @param event The event received by the ProxyElement
     * @param sourceToTargetPath The xpath, maintained by the
     * ProxyElement, that enables this class to navigate from source
     * elements (i.e. selected elements) to target elements (i.e. proxied
     * elements) whose attributes (some of) are aggregated: may be null
     * to indicate top-level
     * @throws IllegalArgumentException if the event is null
     */
    public void applySelection(
        ODOMElementSelectionEvent event,
        ODOMXPath sourceToTargetPath) {

        // Check arguments
        if (event == null) {
            throw new IllegalArgumentException("Cannot apply null event");
        }

        // Remember the xpath, as it is later used when we start listening
        // to the source/target pairs we are about to populate:
        // REMEMBER that this can be null for when source==target!
        this.sourceToTargetPath = sourceToTargetPath;

        // Unregister us as listener from the sources and clear them
        for (int i = 0; i < sourceTargetPairs.size(); i++) {
            sourceTargetPairs.getSource(i).
                    removeChangeListener(changeListener);
        }
        sourceTargetPairs.clear();

        // Loop through the event elements (if any)
        final Iterator sourceElementsIter = event.getSelection().iterator();
        while (sourceElementsIter.hasNext()) {
            final ODOMElement sourceElement =
                (ODOMElement) sourceElementsIter.next();

            // Add the source/target pair, where the target is navigated to
            // from the source using sourceToTargetPath (which may be null,
            // but that is handled OK by navigateToTarget)
            sourceTargetPairs.add(
                    sourceElement, navigateToTarget(sourceElement));
        }

        // Now listen to the source/target pairs we've just set up
        for (int i = 0; i < sourceTargetPairs.size(); i++) {
            sourceTargetPairs.getSource(i).
                addChangeListener(changeListener);
        }

        // Since we've got a new set of targets, we have to tell both the
        // details object and the aggregation listener
        proxiedElementsUpdated(ProxyElementDetails.ELEMENTS);
    }

    /**
     * The relationship between this method and the setName method in
     * the listening ProxyElement is the same as the relationship between
     * propagateSetAttribute in this class and the setAttribute method in
     * the listening ProxyElement. When the ProxyElement has its setName
     * method called, it uses this method to propagate that to all the
     * target elements. This name must be an XML-valid element name (and
     * therefore, inter alia, non-null and non-empty).
     *
     * @param name The new name for all the target elements which must
     * be a valid XML name
     * @throws IllegalArgumentException if the parameter is null or empty
     */
    public void propagateSetName(String name) {

        // Check arguments
        if (name == null || name.length() == 0) {
            throw new IllegalArgumentException
                    ("Cannot propagate null name");
        }
        try {
            // Ignore events as we know we're going to cause them
            ignoreChangeEvents = true;

            // Just loop through all the non-null targets, changing their name
            boolean targetsUpdated = false;
            for (int i = 0; i < sourceTargetPairs.size(); i++) {
                final ODOMElement target = sourceTargetPairs.getTarget(i);
                
                // TODO Should we create a target if we find a null one
                // (which could only be the case if target was a (grand)
                // child of source) - if so set targetsUpdated = true;
                if (target != null) {
                    target.setName(name);
                    targetsUpdated = true;
                }
            }

            // If the targets have been updated, we need to tell both the
            // details object and the aggregation listener
            if (targetsUpdated) {
                proxiedElementsUpdated(ProxyElementDetails.ELEMENT_NAMES);
            }
            
        } finally {
            // Start listening to change events again
            ignoreChangeEvents = false;
        }
    }

    /**
     * When the ProxyElement has its setAttribute method called (without this
     * instance knowing about it), the ProxyElement has to explicitly request
     * the propagation to the targets, which it does through this method. The
     * value parameter may be empty to indicate an empty value, in which case
     * the target attributes will be deleted. (NB This is done through this
     * method, rather than some "propagateRemoveAttribute" method, to be
     * consistent with the fact that removeAttribute is an unsupported
     * operation in the ProxyElement class.)
     * 
     * <p>If the attribute value is non-empty, and the target does not exist
     * (which can only occur if the target is a (grand)child of the source)
     * then the target is created to receive the attribute.</p>
     *
     * <p>If the attribute value is empty, and the target is a (grand)child
     * of the source, and contains only the attribute specified by the given
     * attribute name, then the target is deleted along with the attribute.</p>
     *
     * @param attribName The name of the attribute to propagate, which
     * may not be null or empty
     * @param attribValue The value of the attribute to propagate, which
     * may not be null but may be empty, in which case the target attributes
     * will be deleted (this is not done through "propagateRemoveAttribute"
     * for reasons described above)
     * @throws IllegalArgumentException if either parameter is null
     */
    public void propagateSetAttribute
            (String attribName, String attribValue) {

        // Make argument checks
        if (attribName == null || attribName.length() == 0) {
            throw new IllegalArgumentException
                    ("Cannot propagate null/empty name");
        }
        if (attribValue == null) {
            throw new IllegalArgumentException
                    ("Cannot propagate null value");
        }
        try {
            // Ignore events as we know we're going to cause them: this is in
            // the try to be symmetric with its inverse in the finally
            ignoreChangeEvents = true;

            // Make a note of what we're changing in the targets
            boolean attribValuesChanged = false;
            boolean attributeHierarchyChanged = false;
            boolean elementHierarchyChanged = false;

            // Loop through all the source/target pairs
            for (int i = 0; i < sourceTargetPairs.size(); i++) {

                // Apply to the nth pair and note the resulting change
                final ProxyElementDetails.ChangeReason nthChange =
                    propagateSetAttribute(attribName, attribValue, i);

                // Note what the change was
                if (nthChange == ProxyElementDetails.ELEMENTS) {
                    elementHierarchyChanged = true;
                } else if (nthChange == ProxyElementDetails.ATTRIBUTES) {
                    attributeHierarchyChanged = true;
                } else if (nthChange == ProxyElementDetails.ATTRIB_VALUES) {
                    attribValuesChanged = true;
                } else if (nthChange != null) {
                    throw new IllegalStateException("Bad propagate reason");
                }
            }

            // If the targets have been updated, we need to tell both the
            // details object and the aggregation listener
            // Use the change reason specified for reasons detailed in the
            // javadoc for that change reason
            if (elementHierarchyChanged) {
                proxiedElementsUpdated(ProxyElementDetails.ELEMENTS);
            } else if (attributeHierarchyChanged) {
                proxiedElementsUpdated(ProxyElementDetails.ATTRIBUTES);
            } else if (attribValuesChanged) {
                proxiedElementsUpdated(ProxyElementDetails.ATTRIB_VALUES);
            } else {
                // Nothing to do; no changes at all
            }

        } catch (XPathException e) {
            throw new IllegalStateException("Bad DOM model");
        } finally {
            // Start listening to change events again
            ignoreChangeEvents = false;
        }
    }

    /**
     * Propagates an attribute to an individual source/target pair and returns
     * an indication of whether any change has been made to the target and, if
     * so, what that change was.
     * 
     * @param attribName The name of the attribute to propagate, which
     * may not be null or empty
     * @param attribValue The value of the attribute to propagate, which
     * may not be null but may be empty, in which case the target attributes
     * will be deleted (if it exists)
     * @param pairIndex The index within the source/target pairs
     * @return null if the target within the source/target pair was unchanged,
     * or a non-null reason indicating why the target was changed
     * @throws com.volantis.mcs.xml.xpath.XPathException Propagated from the XPath API
     */
    private ProxyElementDetails.ChangeReason propagateSetAttribute(
        String attribName,
        String attribValue,
        int pairIndex)
        throws XPathException {
            
        // Default to no change
        ProxyElementDetails.ChangeReason change = null;

        // Get the target element
        final ODOMElement targetElement =
                sourceTargetPairs.getTarget(pairIndex);

        // Does the attribute NOT exist in the target?
        if (targetElement == null
            || targetElement.getAttribute(attribName) == null) {

            // If there is an attribute value passed in, need to create the
            // attribute to receive it (otherwise there is nothing to do and
            // the default return of null applies)
            if (attribValue != null && attribValue.length() != 0) {
                
                // Create the attrib and note whether an element was created
                final boolean elementCreated =
                    propagateCreateAttribute(
                        attribName, attribValue, pairIndex);
                        
                // Convert the result accordingly
                change = (elementCreated
                    ? ProxyElementDetails.ELEMENTS
                    : ProxyElementDetails.ATTRIBUTES);
            }
            
        } else {
            // Target attribute DOES exist in the target: get it
            final ODOMAttribute existingAttribute =
                (ODOMAttribute) targetElement.getAttribute(attribName);
                
            // If there is an attribute value passed in...
            if (attribValue != null && attribValue.length() != 0) {    
                
                // Just update the existing attribute with the new value
                // and set the change return accordingly
                existingAttribute.setValue(attribValue);
                change = ProxyElementDetails.ATTRIB_VALUES;
                
            } else {    
                     
                // Delete the attrib and note whether its element was deleted
                final boolean elementDeleted =
                    propagateDeleteAttribute(existingAttribute, pairIndex);
                        
                // Convert the result accordingly
                change = (elementDeleted
                    ? ProxyElementDetails.ELEMENTS
                    : ProxyElementDetails.ATTRIBUTES);
            }
        }
        return change;
    }

    /**
     * When the ProxyElement has its setText method called (without this
     * instance knowing about it), the ProxyElement has to explicitly request
     * the propagation to the targets, which it does through this method.
     *
     * @param text The new text value
     */

    public void propagateSetText(String text) {
        try {
            // Ignore events as we know we're going to cause them: this is in
            // the try to be symmetric with its inverse in the finally
            ignoreChangeEvents = true;

            // Make a note of what we're changing in the targets
            boolean elementHierarchyChanged = false;
            boolean textChanged = false;

            // Loop through all the source/target pairs
            for (int i = 0; i < sourceTargetPairs.size(); i++) {
                // Apply to the nth pair and note the resulting change
                final ProxyElementDetails.ChangeReason nthChange =
                    propagateSetText(text, i);

                // Note what the change was
                if (nthChange == ProxyElementDetails.ELEMENTS) {
                    elementHierarchyChanged = true;
                } else if (nthChange == ProxyElementDetails.TEXT_VALUE) {
                    textChanged = true;
                } else if (nthChange != null) {
                    throw new IllegalStateException("Bad propagate reason");
                }
            }

            // If the targets have been updated, we need to tell both the
            // details object and the aggregation listener
            // Use the change reason specified for reasons detailed in the
            // javadoc for that change reason
            if (elementHierarchyChanged) {
                proxiedElementsUpdated(ProxyElementDetails.ELEMENTS);
            } else if (textChanged) {
                proxiedElementsUpdated(ProxyElementDetails.TEXT_VALUE);
            } else {
                // Nothing to do; no changes at all
            }
        } catch (XPathException e) {
            throw new IllegalStateException("Bad DOM model");
        } finally {
            // Start listening to change events again
            ignoreChangeEvents = false;
        }
    }

    /**
     * Propagates text to an individual source/target pair and returns
     * an indication of whether any change has been made to the target and, if
     * so, what that change was.
     *
     * @param text The new text value to propagate
     * @param pairIndex The index within the source/target pairs
     * @return null if the target within the source/target pair was unchanged,
     * or a non-null reason indicating why the target was changed
     */
    private ProxyElementDetails.ChangeReason propagateSetText(
            String text, int pairIndex) throws XPathException {

        // Default to no change
        ProxyElementDetails.ChangeReason change = null;

        boolean wasTargetCreated = false;

        // Get the target element
        ODOMElement targetElement =
                sourceTargetPairs.getTarget(pairIndex);

        if (targetElement == null) {
            ODOMElement sourceElement = sourceTargetPairs.getSource(pairIndex);
            // Create the element if it isn't there...
            sourceToTargetPath.create(sourceElement, odomFactory);

            // Navigate to the target that the create should have made
            targetElement =
                    navigateToTarget(sourceTargetPairs.getSource(pairIndex));
            if (targetElement == null) {
                throw new IllegalStateException
                            ("xpath create did not create target");
            }

            // OK, add in the new target
            sourceTargetPairs.setTarget(pairIndex, targetElement);

            // We did create an element
            wasTargetCreated = true;
        }

        targetElement.setText(text);

        change = (wasTargetCreated
            ? ProxyElementDetails.ELEMENTS
            : ProxyElementDetails.TEXT_VALUE);

        return change;
    }

    /**
     * Propagates an attribute creation to an individual source/target pair
     * and returns an indication of whether the target element had to be
     * created to receive the newly-created attribute
     * 
     * @param attribName The name of the attribute to propagate, which
     * may not be null or empty
     * @param attribValue The value of the attribute to propagate, which
     * may not be null but may be empty, in which case the target attributes
     * will be deleted (if it exists)
     * @param pairIndex The index within the source/target pairs
     * @return true if and only if a target element was created to receive
     * the new attribute
     * @throws com.volantis.mcs.xml.xpath.XPathException Propagated from the XPath API
     */
    private boolean propagateCreateAttribute(
                String attribName,
                String attribValue,
                int pairIndex)
                throws XPathException {

        // Get the souce and target elements: the source is immutable so
        // final, but we may have to re-assign the target
        final ODOMElement sourceElement =
                    sourceTargetPairs.getSource(pairIndex);

        ODOMElement targetElement =
                    sourceTargetPairs.getTarget(pairIndex);

        // flag that will be set to true if the target element had to be
        // created
        boolean wasTargetCreated = false;

        // if the target element is a ProxyElement then we do not delete an
        // attribute, instead we just set it's value to an empty string. The
        // target proxy will then propagate this "effective" delete to its
        // targets.
        if (targetElement instanceof ProxyElement) {
            targetElement.setAttribute(attribName, attribValue);
        } else {
            // Create the attribute to insert
            final ODOMAttribute newAttribute =
                        (ODOMAttribute) odomFactory.
                        attribute(attribName, attribValue);

            // Use a create xpath instead of just setAttribute on the
            // target as only the former method creates (or ensures is
            // created) the "whole path" from source to target (e.g. if
            // latter is grandchild)
            // NOTE that this is ok even where sourceToTargetPath==null
            final ODOMXPath createPath =
                        new ODOMXPath(sourceToTargetPath, newAttribute);

            // This just creates an EMPTY attribute.......
            final ODOMAttribute createdAttribute =
                        (ODOMAttribute) createPath.
                        create(sourceElement, odomFactory);

            // ....but in doing so it might have created us a new target:
            // first check whether it was originally null
            if (targetElement == null) {

                // Navigate to the target that the create should have made
                targetElement = navigateToTarget(sourceElement);
                if (targetElement == null) {
                    throw new IllegalStateException
                                ("xpath create did not create target");
                }

                // OK, add in the new target
                sourceTargetPairs.setTarget(pairIndex, targetElement);

                // We did create an element
                wasTargetCreated = true;
            }

            // Finally we can insert the actual target value!
            createdAttribute.setValue(attribValue);
        }
        return wasTargetCreated;
    }

    /**
     * Propagates an attribute deletion to an individual source/target pair
     * and returns an indication as to whether the attribute deletion also
     * resulted in its owning element (target) deletion
     * 
     * @param existingAttribute The attribute to delete
     * @param pairIndex The index within the source/target pairs
     * @return true if and only if the attribute's owning (target) element
     * was also deleted
     * @throws com.volantis.mcs.xml.xpath.XPathException Propagated from the XPath API
     */   
    private boolean propagateDeleteAttribute(
                ODOMAttribute existingAttribute,
                int pairIndex)
                throws XPathException {

        // Get the souce and target elements: the source is immutable so
        // final, but we may have to re-assign the target
        final ODOMElement sourceElement =
                    sourceTargetPairs.getSource(pairIndex);

        ODOMElement targetElement =
                    sourceTargetPairs.getTarget(pairIndex);

        // flag that will be set to true if the target element was deleted
        boolean wasTargetDeleted = false;

        if (targetElement instanceof ProxyElement) {
            targetElement.setAttribute(existingAttribute.getName(), "");
        } else {

            // Use a remove xpath instead of just removeAttribute on the
            // target as only the former method removes the "whole path"
            // from source to target (e.g. if latter is grandchild)
            // NOTE: This should still be ok if sourceToTargetPath==null
            final ODOMXPath deletePath =
                        new ODOMXPath(sourceToTargetPath, existingAttribute);

            // Now delete the attribute
            deletePath.remove(sourceElement);

            // We know that the original target was non-null, because it
            // contained the attribute: now find out whether the above
            // xpath delete also deleted the target (which it would if
            // it had been left empty)
            if (navigateToTarget(sourceElement) == null) {

                // Clear the target
                sourceTargetPairs.setTarget(pairIndex, null);

                // We did remove a (target) element
                wasTargetDeleted = true;
            }
        }
        return wasTargetDeleted;
    }

    /**
     * Processes a received change event (for which it has already been
     * determined that the event needs to be processed)
     * 
     * TODO (1) Register for target attribute name change events and throw an
     * unchecked exception if a proxied element attribute changes its name.
     * TODO (2) Retain the existing target element name change event
     * registration for all proxy elements in the hierarchy, but throw an
     * unchecked exception if one arrives indicating that a target element
     * name change has occurred on a non-top-level target.
     * TODO (3) Similarly disallow ProxyElement.setName for non-top level.
     *
     * @param node  the node reporting the event (this will be one
     * against which the listener has been registered)
     * @param event the change event
     */
    private void processChangeEvent
            (ODOMObservable node, ODOMChangeEvent event) {

        // Get the event source and node in typesafe form (we know the node
        // is an element because we only listen on sources)
        final ODOMObservable eventSrc = event.getSource();
        final ODOMElement nodeEle = (ODOMElement)node;

        // Switch on change type
        if (event.getChangeQualifier() == ChangeQualifier.HIERARCHY) {

            // Just delegate to attribute or element hierarchy event handler
            if (eventSrc instanceof ODOMElement) {
                processElementHierarchyEvent(nodeEle, event);
            } else if (eventSrc instanceof ODOMAttribute) {
                processAttribHierarchyEvent(event);
            } else if (eventSrc instanceof ODOMText) {
                processTextHierarchyEvent(event);
            }

        } else if (event.getChangeQualifier() == ChangeQualifier.NAME) {

            // If the source is an element, check whether it's a target;
            // otherwise ensure that target attributes can't change
            if (eventSrc instanceof ODOMElement) {
                if (sourceTargetPairs.isTargetElement((Element)eventSrc)) {
                    proxiedElementsUpdated(ProxyElementDetails.ELEMENT_NAMES);
                }
            } else if (eventSrc instanceof ODOMAttribute) {
                // TODO: throw an IllegalStateException if a target attribute
                // has changed name
            }

        } else if (event.getChangeQualifier() ==
                   ChangeQualifier.ATTRIBUTE_VALUE) {

            // Does the attribute belong to a target, and is its name in the
            // list of supported attributes - if so the proxy elements have
            // been updated in a notifiable way
            if (sourceTargetPairs.isTargetElement(eventSrc.getParent()) &&
                    proxyElementDetails.isAttributeName(eventSrc.getName())) {
                proxiedElementsUpdated(ProxyElementDetails.ATTRIB_VALUES);
            }
        } else if (event.getChangeQualifier() ==
                ChangeQualifier.TEXT) {
            if (sourceTargetPairs.isTargetElement(eventSrc.getParent())) {
                proxiedElementsUpdated(ProxyElementDetails.TEXT_VALUE);
            }
        }
    }
    
    /**
     * Processes a hierarchy event on a node which is a proxy target source
     * and whose event source is an attribute
     *
     * @param event The original event from the changed callback
     */
    private void processAttribHierarchyEvent(ODOMChangeEvent event) {

        // Get the event source, which we know to be an attribute
        final ODOMAttribute eventSrcAttrib = (ODOMAttribute) event.getSource();

        // Is it an addition?
        if (event.getNewValue() != null && event.getOldValue() == null) {

            // TODO Should we do this test:
            // To be consistent with element hierarchy events, make sure
            // we do NOT already have this attribute

            // If the attribute has just been added to an element that is
            // a target in the source/target pairs, and the attribute is
            // currently supported, then this counts as a notifiable
            // change to the proxied elements
            if (sourceTargetPairs.isTargetElement(
                    (Element)event.getNewValue()) &&
                proxyElementDetails.isAttributeName(
                    eventSrcAttrib.getName())) {
                proxiedElementsUpdated(ProxyElementDetails.ATTRIBUTES);
            }

        // Is it a deletion?
        } else if (event.getNewValue() == null &&
                   event.getOldValue() != null) {

            // TODO Should we do this test:
            // To be consistent with element hierarchy events, make sure
            // we DO already have this attribute

            // If the attribute has just been removed from an element that
            // is a target in the source/target pairs, and the attribute is
            // currently supported, then this counts as a notifiable
            // change to the proxied elements
            if (sourceTargetPairs.isTargetElement(
                    (Element)event.getOldValue()) &&
                proxyElementDetails.isAttributeName(
                    eventSrcAttrib.getName())) {
                proxiedElementsUpdated(ProxyElementDetails.ATTRIBUTES);
            }

        } else {
            // Event content is nonsense
            throw new IllegalStateException("Bad attribute event");
        }
    }

    /**
     * Processes a hierarchy event on a node which is a proxy target source
     * and whose event source is a text node
     *
     * @param event The original event from the changed callback
     */
    private void processTextHierarchyEvent(ODOMChangeEvent event) {
        proxiedElementsUpdated(ProxyElementDetails.TEXT_VALUE);
    }

    /**
     * Processes a hierarchy event on a node which is a proxy target source and
     * whose event source is an element.
     *
     * @param sourceElement
     *              The node on which the event happened, for which it already
     *              has been determined is a "source" element
     * @param event The original event from the changed callback
     */
    private void processElementHierarchyEvent(ODOMElement sourceElement,
                                              ODOMChangeEvent event) {
        // Get the event source, which we know to be an element
        final ODOMElement eventSrcElement = (ODOMElement) event.getSource();

        if ((sourceElement != eventSrcElement) &&
                sourceTargetPairs.isSourceElement(eventSrcElement)) {
            // ignore this event explicitly. This is here to avoid erroneous
            // multiple processings of the event when the proxy is used in
            // a situation where both an element and one of its ancestors can
            // both be sources: in this case, because of how events propagate
            // up the hierarchy in the ODOM, the proxy can get confused (e.g.
            // if the child is removed, it might appear that the
            // "sourceElement" (its ancestor) is being removed but then an
            // illegal state exception ("removing nonexistant target") would
            // be thrown).
        } else {
            // Find the corresponding proxy target (which may/may not be null)
            final int proxyIndex =
                sourceTargetPairs.getSourceIndex(sourceElement);
            final ODOMElement existingProxyTarget =
                sourceTargetPairs.getTarget(proxyIndex);

            // This combination indicates an addition
            if ((event.getNewValue() != null) &&
                    (event.getOldValue() == null)) {

                // Form an XPath that starts with a context of the source
                // element (in the sense of source/target pair) and navigates
                // down to the EVENT source element via the element that has
                // had an element added to it
                final XPath testPathToAddition =
                    (sourceElement == event.getNewValue() ?
                        null :
                        new XPath(sourceElement,
                                  (Element) event.getNewValue()));
                final XPath testPathToEventSrc =
                    new XPath(testPathToAddition, eventSrcElement.getName());

                // If the above path matches our own path for mapping from
                // source (in the sense of source/target pair) to TARGET, then
                // it's established that the EVENT source is indeed a target
                if (testPathToEventSrc.externalFormsEqual(sourceToTargetPath)) {

                    // Target addition: make sure we don't already have one
                    if (existingProxyTarget != null) {
                        throw new IllegalStateException("Double target");
                    }

                    // OK, add in the new target
                    sourceTargetPairs.setTarget(proxyIndex, eventSrcElement);

                    // Since we've got a new target, we have to tell both the
                    // details object and the aggregation listener
                    proxiedElementsUpdated(ProxyElementDetails.ELEMENTS);
                }
            } else if ((event.getNewValue() == null) &&
                    (event.getOldValue() != null)) {
                // This combination indicates a removal (detachment)

                // Removal case is simpler as we just need to test what has
                // been removed against what we have
                if (sourceTargetPairs.isTargetElement(eventSrcElement)) {

                    // Target element removal: make it is the same one as we
                    // already have, as a proxy source can only have one target
                    if (existingProxyTarget != eventSrcElement) {
                        throw new IllegalStateException(
                            "Removing nonexistent target");
                    }

                    // It's possible from the way that hierarchy events are
                    // generated that we can get this event even when the
                    // target IS the source we listened on. If that is the
                    // case do nothing; otherwise just nullify the target
                    if (sourceElement != eventSrcElement) {
                        sourceTargetPairs.setTarget(proxyIndex, null);

                        // Since we've removed a target, we have to tell both
                        // the details object and the aggregation listener
                        proxiedElementsUpdated(ProxyElementDetails.ELEMENTS);
                    }
                }
            } else {
                // Event content is nonsense
                throw new IllegalStateException("Bad attribute event");
            }
        }
    }
    
    /**
     * This method is called when one or more of the proxied elements has
     * been updated (either directly by this class, or in the case where
     * this class has got to hear about it through change events).
     * It tells both the (proxy element) details object, and the
     * aggregation listener, of a change in the set of names and values
     * of supported (aka aggregated) attributes. It notes the existing set
     * of supporting attribute names, then asks the details object to
     * re-calculate this set by giving it a new set of targets. This method
     * then uses the differences between these lists to notify the
     * aggregation listener of new attributes, deleted (removed) attributes
     * and new attribute values.
     */
    private void proxiedElementsUpdated(
        ProxyElementDetails.ChangeReason reason) {
            
        // Set the proxied elements to their new set, and make a note of
        // whether the details object indicates a "full update" is needed
        boolean performFullUpdate =
            proxyElementDetails.setProxiedElements(
                sourceTargetPairs.getTargets().iterator(), reason);
        
        // Check whether this is the first time we are reading the details
        // object; if so, initialise all our details-based instance variables
        // to "null" values that match the return constraints of the getters
        // in the details object and force the code down the "full update" path
        if (detailsAttributeNames == null) {
            detailsAttributeNames = new String[0];
            detailsElementName = ODOMElement.NULL_ELEMENT_NAME;
            detailsElementNamespace = Namespace.NO_NAMESPACE;
            performFullUpdate = true;
        }

        // If we're performing a full update, then we assume all the details
        // object's get methods may have changed
        if (performFullUpdate) {

            // Get the old attribute names as a List (this is safe to do as we
            // kept it in a shallow-copied instance array so has not been
            // affected by the above call to setProxiedElements)
            final List oldAttribNamesAsList =
                Arrays.asList(detailsAttributeNames);
        
            // Now get all the other new values
            final String newElementName = proxyElementDetails.getElementName();
            final Namespace newElementNamespace =
                proxyElementDetails.getElementNamespace();
            final String[] newAttributeNames =
                proxyElementDetails.getAttributeNames();
            final List newAttributeNamesAsList =
                Arrays.asList(newAttributeNames);
            
            // Issue updates as necessary
            if (!detailsElementName.equals(newElementName)) {
                aggregationListener.updatedElementName(newElementName);
            }
            if (!detailsElementNamespace.equals(newElementNamespace)) {
                aggregationListener.
                    updatedElementNamespace(newElementNamespace);
            }

            // Go through the attributes which were previously supported (aka
            // delegated) but which are not now
            final Iterator oldAttribNamesIter =
                oldAttribNamesAsList.iterator();
            while (oldAttribNamesIter.hasNext()) {
                final String oldAttribName =
                    (String) oldAttribNamesIter.next();
                if (!newAttributeNamesAsList.contains(oldAttribName)) {

                    // All we need to do is tell the attributes change
                    // listener that the attribute has been deleted
                    aggregationListener.deletedAttribute(oldAttribName);
                }
            }

            // Go through the attributes which are supported now
            final Iterator newAttribNamesIter =
                newAttributeNamesAsList.iterator();
            while (newAttribNamesIter.hasNext()) {
                final String newAttribName =
                    (String) newAttribNamesIter.next();

                // First, calculate what the new aggregated value is
                final String aggregatedValue =
                    getAggregateValue(newAttribName);

                // Depending on whether the attribute was supported before this
                // method was called, issue a new attribute or value updated
                // message to the aggregated value listener (the ProxyElement)
                if (oldAttribNamesAsList.contains(newAttribName)) {
                    aggregationListener.updatedAttributeValue(
                        newAttribName, aggregatedValue);
                } else {
                    aggregationListener.newAttribute(
                        newAttribName, aggregatedValue);
                }
            }
        
            // Simpler and less error-prone just to make these assignments
            // anyway regardless which subset of them has actually changed
            detailsElementName = newElementName;
            detailsElementNamespace = newElementNamespace;
            detailsAttributeNames = newAttributeNames;
            
            // Could also be the text, of course...
            aggregationListener.updatedTextValue(getAggregateText());
        } else {
            
            // All that's changed (potentially) is the attribute values,
            // so for the attributes that we have
            for (int i = 0; i < detailsAttributeNames.length; i++) {
                aggregationListener.updatedAttributeValue(
                    detailsAttributeNames[i],
                    getAggregateValue(detailsAttributeNames[i]));
            }

            // Could also be the text, of course...
            aggregationListener.updatedTextValue(getAggregateText());
        }
    }

    /**
     * Returns the "aggregate" value for the specified attribute
     *
     * @param attributeName The name of the supported attribute for which
     * we want the aggregated value
     * @return The aggregated value, which is never null: the empty String
     * ("") is used to return an "empty" aggregated value
     */
    private String getAggregateValue(String attributeName) {

        // Default to empty string, not null, as this is convention for XML
        String aggValue = "";

        // Get the first value, if it exists
        String firstValue = null;
        if (sourceTargetPairs.size() > 0) {
            final Element firstTarget = sourceTargetPairs.getTarget(0);
            firstValue =
                (firstTarget == null
                    ? null
                    : firstTarget.getAttributeValue(attributeName));
        }

        // If the first value exists and is non-empty, take the aggregate value
        // to be that value unless some subsequent value differs from it, in
        // which case revert the aggregate value to the empty string
        if (firstValue != null && firstValue.length() > 0) {
            aggValue = firstValue;
            for (int i = 1; // going from second value
                   i < sourceTargetPairs.size() && aggValue.length() > 0;
                   i++) {
                final Element nthTarget = sourceTargetPairs.getTarget(i);
                final String nthValue =
                    (nthTarget == null
                        ? null
                        : nthTarget.getAttributeValue(attributeName));
                if (nthValue == null || !firstValue.equals(nthValue)) {
                    aggValue = "";
                }
            }
        }
        return aggValue;
    }

    /**
     * Returns the "aggregate" value for the text
     *
     * @return The aggregated value, which is never null: the empty String
     * ("") is used to return an "empty" aggregated value
     */
    private String getAggregateText() {
        // Default to empty string, not null, as this is convention for XML
        String aggValue = "";

        // Get the first value, if it exists
        String firstValue = null;
        if (sourceTargetPairs.size() > 0) {
            final Element firstTarget = sourceTargetPairs.getTarget(0);
            firstValue = (firstTarget == null) ? null : firstTarget.getText();
        }

        // If the first value exists and is non-empty, take the aggregate value
        // to be that value unless some subsequent value differs from it, in
        // which case revert the aggregate value to the empty string
        if (firstValue != null && firstValue.length() > 0) {
            aggValue = firstValue;
            for (int i = 1; // going from second value
                   i < sourceTargetPairs.size() && aggValue.length() > 0;
                   i++) {
                final Element nthTarget = sourceTargetPairs.getTarget(i);
                final String nthValue =
                    (nthTarget == null) ? null : nthTarget.getText();
                if (nthValue == null || !firstValue.equals(nthValue)) {
                    aggValue = "";
                }
            }
        }
        return aggValue;
    }

    /**
     * Given a source element (from the source/target pairs), this method
     * uses the source-to-target xpath to navigate to the target (if it exists).
     * If it does not exist, it returns null. If it exists, it is returned. If
     * more than one exist, it throws an IllegalStateException (as ProxyElement
     * is predicated on sources not being able to have more than one target).
     * This method may be used (for example) to populate the target part
     * of the source/target pairs: note this method does NOT itself update
     * that store.
     * 
     * @param sourceElement The element from which to navigate to the
     * target element
     * @return The target element if found, or null if not found
     * @throws IllegalStateException if a navigation error occurred
     */
    private ODOMElement navigateToTarget(ODOMElement sourceElement) {

        // The default is either null (for when the xpath is NOT null)
        // or the source element (for when the xpath IS null)
        ODOMElement targetElement =
            (sourceToTargetPath != null ? null : sourceElement);
        
        // If the xpath is not null, try to find a non-null target
        if (sourceToTargetPath != null) {
            
            // Get the targets from the source using the non-null xpath
            List eventTargets = null;
            try {
                eventTargets = sourceToTargetPath.selectNodes(sourceElement);
            } catch (XPathException e) {
                // In this context, this is a fatal error
                throw new IllegalStateException
                        ("selectNodes: " + e.getMessage());
            }

            // Only a count of 0 or 1 is OK (null List treated as empty)
            if (eventTargets != null) {
                if (eventTargets.size() > 1) {
                    throw new IllegalStateException
                       ("source with multiple targets");
                } else if (eventTargets.size() == 1) {
                    targetElement = (ODOMElement) eventTargets.get(0);
                } else {
                    // size() == 0: default of null applies
                    // nothing to do
                }
            }
        }
        return targetElement;
    }

    /**
     * Remove any targets that have no attributes and content (make them empty).
     */
    public void removeEmptyTargets() {
        List targets = getTargets();

        for (int i = 0; i < targets.size(); i++) {
            ODOMElement odomElement = (ODOMElement) targets.get(i);
            if (odomElement != null) {
                List attributes = odomElement.getAttributes();
                if (attributes == null || attributes.size() == 0) {
                    // has no attributes.
                    List content = odomElement.getContent();
                    if (content != null && content.size() == 0) {
                        // no content therefore eligible for removal.
                        try {
                            sourceToTargetPath.remove(sourceTargetPairs.getSource(i));
                            sourceTargetPairs.setTarget(i, null);
                        } catch (XPathException e) {
                            throw new IllegalStateException(
                                    "Unabled to remove XPath: "  +
                                    sourceToTargetPath);
                        }
                    }
                }
            }
        }
    }

    /**
     * Get a list of targets.
     * @return the list of target {@link ODOMElement} objects.
     */
    public List getTargets() {
        return sourceTargetPairs.getTargets();
    }

    /**
     * Create targets if the target is empty (null).
     */
    public void createEmptyTargets() {
        List targets = getTargets();
        for (int i = 0; i < targets.size(); i++) {
            ODOMElement odomElement = (ODOMElement) targets.get(i);
            if (odomElement == null) {
                // empty target
                try {
                    ODOMElement target = (ODOMElement) sourceToTargetPath.create(
                            sourceTargetPairs.getSource(i), odomFactory);
                    sourceTargetPairs.setTarget(i, target);
                } catch (XPathException e) {
                    throw new IllegalStateException("Unabled to create XPath: "  +
                            sourceToTargetPath);
                }
            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 13-Oct-05	9732/1	adrianj	VBM:2005100509 Fixes to Eclipse GUI

 06-Oct-05	9734/2	adrianj	VBM:2005100510 Allow text in ProxyElements

 21-Dec-04	6524/2	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Sep-04	5674/1	philws	VBM:2004080406 Fix proxy element hierarchical source event problem seen with undoing a swap action on the layout editor

 05-Mar-04	3256/3	byron	VBM:2004021106 The theme StyleList property types do not work in the theme editor - null pointer fix

 03-Mar-04	3256/1	byron	VBM:2004021106 The theme StyleList property types do not work in the theme editor

 12-Feb-04	2789/3	tony	VBM:2004012601 Localised logging (and exceptions)

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 29-Jan-04	2772/1	doug	VBM:2004012703 Added the synchronizeable 'all' property controls

 15-Jan-04	2618/1	allan	VBM:2004011510 Provide an IStructuredSelection for selected ODOMElements.

 19-Dec-03	2254/2	richardc	VBM:2003121901 Do nothing if target is detached but target==source

 18-Dec-03	2137/14	richardc	VBM:2003120402 Extra tests for event non-generation

 17-Dec-03	2203/11	richardc	VBM:2003121102 Two non-bug tidyings

 17-Dec-03	2203/9	richardc	VBM:2003121102 Element name change handling correction and couple of tidyings

 16-Dec-03	2203/7	richardc	VBM:2003121102 Process element hierarchy event fixed for all types of source/target relationship

 11-Dec-03	2203/3	richardc	VBM:2003121102 Corected default return in navigateToTarget

 11-Dec-03	2203/1	richardc	VBM:2003121102 Allows for setProxiedElements to always return false

 10-Dec-03	1968/28	richardc	VBM:2003111502 Second semi-tested draft for code release

 03-Dec-03	1968/14	richardc	VBM:2003111502 Untested draft for code review

 03-Dec-03	1968/12	richardc	VBM:2003111502 Untested draft for code review

 28-Nov-03	2041/1	byron	VBM:2003111502 Provide sophisticated, hierarchical ODOM ProxyElement - testcases

 28-Nov-03	1968/5	richardc	VBM:2003111502 Code complete subject to javadoc, nulls check, exception details and review

 27-Nov-03	1968/3	richardc	VBM:2003111502 Remove self-propagation

 27-Nov-03	1968/1	richardc	VBM:2003111502 First draft for unit test

 ===========================================================================
*/
