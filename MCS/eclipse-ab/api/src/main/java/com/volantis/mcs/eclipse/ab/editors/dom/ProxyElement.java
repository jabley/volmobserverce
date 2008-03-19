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

import com.volantis.mcs.eclipse.common.odom.ODOMAttribute;
import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.common.odom.ODOMElementSelectionEvent;
import com.volantis.mcs.eclipse.common.odom.ODOMElementSelectionListener;
import com.volantis.mcs.eclipse.common.odom.ODOMFactory;
import com.volantis.mcs.eclipse.common.odom.xpath.ODOMXPath;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.synergetics.log.LogDispatcher;
import org.jdom.Attribute;
import org.jdom.Comment;
import org.jdom.Element;
import org.jdom.EntityRef;
import org.jdom.Namespace;
import org.jdom.ProcessingInstruction;
import org.jdom.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * We are only interested in element selections where all possible element
 * selections are represented by the root proxy element.
 * <p/>
 * <p>Only the top-level ProxyElement should be set up as a selection
 * listener; this top-level instance will propagate received selections
 * down to its child proxy elements.</p>
 * <p/>
 * <p>This specialization needs to provide augmented versions of attribute
 * value change methods that:</p>
 * <p/>
 * <ol>
 * <p/>
 * <li>Delete the underlying target attributes if the attribute value is
 * set to empty</li>
 * <p/>
 * <li>Delete the underlying target element when the last attribute is
 * deleted and there are no child elements or when the last child
 * element is deleted and there are no attributes</li>
 * <p/>
 * </ol>
 * <p/>
 * <p>An attempt to change an attribute value (to a value other than "")
 * when there are null proxied elements (targets) must result in the creation
 * of the entire hierarchy down to that missing target's attribute.</p>
 * <p/>
 * <p>Conversely, an attempt to set an attribute value to "" should delete
 * the underlying attribute and any now empty hierarchy, potentially
 * meaning that the target must be set null.</p>
 */
public class ProxyElement
        extends ODOMElement
        implements ODOMElementSelectionListener {
    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(ProxyElement.class);


    /**
     * Calculated in {@link #setParent} and {@link #selectionChanged}.
     * This will be the XPath relative to the Proxy's root node. I.e. the root
     * node of the <strong>proxy</strong> hierarchy. This will be null in
     * the root proxy node.
     */
    private ODOMXPath xpath;

    /**
     * The factory for creating attributes and elements
     */
    private final ODOMFactory odomFactory;

    /**
     * The customizable attributes of the enclosing ProxyElement: refer to
     * the javadoc comments for its class. It is final and therefore must be
     * provided by the constructor(s). Note that although it is final, this
     * class does not assume that the instance's methods always return the
     * same values.
     */
    private final ProxyElementDetails details;

    /**
     * This object that manages the proxied elements (aka delegates). See
     * the javadoc comments for this class.
     */
    private final ProxyElementDelegates delegates;

    /**
     * This is the embedded listener to "aggregation" events from the
     * delegates manager object. Note that an embedded listener is used
     * (as opposed to this instance) to keep the callback members private.
     */
    private final AggregationListener listener =
            new AggregationListener() {

                // javadoc inherited
                public void updatedElementName(String elementName) {

                    // Update our own name in the SUPERCLASS (otherwise if it were
                    // in THIS, it would be propagated back down); then force a
                    // recalculation of the xpath, as that depends on the value of
                    // this new name (which in turn is retrievable from the details
                    // object), and recurse this to the child ProxyElements
                    ProxyElement.super.setName(elementName);
                    ProxyElement.this.calculateXPath(true);
                }

                // javadoc inherited
                public void updatedElementNamespace(Namespace elementNamespace) {

                    // Update our own namespace in the SUPERCLASS because it is
                    // an unsupported operation at THIS level
                    ProxyElement.super.setNamespace(elementNamespace);
                }

                // javadoc inherited
                public void newAttribute(String attribName, String attribValue)
                        throws IllegalStateException {

                    // The ProxyElement must manage its attributes' lifecycles
                    // exactly according to these callbacks - so it is a fatal bad
                    // state error if this attribute already exists; otherwise, just
                    // create it
                    if (ProxyElement.super.getAttribute(attribName) != null) {
                        throw new IllegalStateException
                                ("Already had " + attribName);
                    }
                    // NB directly use "new", not a factory method, because factories
                    // are associated with validators, and we don't want validation
                    // on this (aggregated) attribute
                    ProxyElement.super.setAttribute
                            (new ODOMAttribute(attribName, attribValue));
                }

                // javadoc inherited
                public void updatedAttributeValue(String attribName,
                                                  String attribValue)
                        throws IllegalStateException {

                    // The ProxyElement must manage its attributes' lifecycles
                    // exactly according to these callbacks - so it is a fatal bad
                    // state error if this attribute is not found; otherwise, just
                    // update its value
                    final Attribute attribute =
                            ProxyElement.super.getAttribute(attribName);
                    if (attribute == null) {
                        throw new IllegalStateException
                                ("Missing " + attribName);
                    }
                    attribute.setValue(attribValue);
                }

                // javadoc inherited
                public void deletedAttribute(String attribName)
                        throws IllegalStateException {

                    // The ProxyElement must manage its attributes' lifecycles
                    // exactly according to these callbacks - so it is a fatal bad
                    // state error if this attribute has already been deleted;
                    // otherwise, just delete it
                    if (ProxyElement.super.getAttribute(attribName) == null) {
                        throw new IllegalStateException
                                ("Missing " + attribName);
                    }
                    ProxyElement.super.removeAttribute(attribName);
                }

                // Javadoc inherited
                public void updatedTextValue(String textValue) {
                    // Only update the text if we have any
                    final Text text = getChildTextNode();
                    if (text != null) {
                        text.setText(textValue);
                    }
                }
            };

    /**
     * This method re-calculates this instance's source-to-target xpath using
     * data from its parent and the proxy element details object, and causes
     * all its ProxyElement children (if any) to do the same
     *
     * @param recurse Indicates whether this method should invoke the
     *                same method on all its ProxyElement children (if any)
     */
    private void calculateXPath(boolean recurse) {

        // Get the parent of this instance, which is expected to be either null
        // or another instance of this class
        final ODOMElement parent = (ODOMElement) getParent();
        if (parent == null) {

            // No parent, so we're top-level, so we don't need an xpath
            xpath = null;

        } else if (parent instanceof ProxyElement) {
            Namespace ns = details.getElementNamespace();

            if (ns != null) {
                String uri = ns.getURI();
                if (uri == null || "".equals(uri)) {
                    ns = null;
                } else {
                    String prefix = ns.getPrefix();
                    if ((prefix == null) ||
                            ("".equals(prefix))) {
                        // @todo dream up a unique internal prefix
                        prefix = "lpdm";
                        ns = Namespace.getNamespace(prefix, ns.getURI());
                    }
                }
            }

            // Parent is one of us, so we know our xpath will not be null, and
            // therefore we'll need the namespace in a form suitable for the
            // XPath constructors
            final Namespace[] namespaces =
                    (ns == null
                    ? null
                    : new Namespace[]{ns});

            // Now get the parent's xpath: if it's null, our xpath is just an
            // (absolute) path to the proxied element; otherwise, we just
            // append the name of the proxied element to the parent's xpath
            final XPath parentXPath = ((ProxyElement) parent).xpath;
            if (parentXPath == null) {
                if (ns != null) {
                    xpath = new ODOMXPath(new StringBuffer(ns.getPrefix()).
                            append(':').
                            append(details.getElementName()).
                            toString(),
                            namespaces);
                } else {
                    xpath = new ODOMXPath(details.getElementName(), namespaces);
                }
            } else {
                if (ns != null) {
                    xpath = new ODOMXPath(parentXPath,
                            new StringBuffer(ns.getPrefix()).
                            append(':').
                            append(details.getElementName()).
                            toString(),
                            namespaces);
                } else {
                    xpath = new ODOMXPath
                            (parentXPath, details.getElementName(), namespaces);
                }
            }
        } else {
            // Parent is a non-null non-ProxyElement
            throw new IllegalStateException("PE's parent is not PE");
        }

        // Last thing to do is get all this instance's children to re-calculate
        // their xpaths, because their xpaths depend on their parent's just
        // like ours did above
        if (recurse) {
            final Iterator childIter = getChildren().iterator();
            while (childIter.hasNext()) {
                ODOMElement child = (ODOMElement) childIter.next();
                if (child instanceof ProxyElement) {
                    ((ProxyElement) child).calculateXPath(true);
                } else {
                    throw new IllegalStateException("Child is not a PE");
                }
            }
        }
    }

    /**
     * This method passes the given event to this instance's supporting
     * delegates object, and then gets any of its ProxyElement children
     * to do the same.
     *
     * @param event The selection event
     */
    protected void applySelection(ODOMElementSelectionEvent event) {

        // First of all calculate the xpath for this instance only
        // (see why below)
        calculateXPath(false);

        // Now apply the selection to this instance's delegates object, using
        // the freshly-calculated xpath
        delegates.applySelection(event, xpath);

        // To make sure the xpath is re-calculated given the new selection
        calculateXPath(false);

        // Now recursively do the same thing for any ProxyElement children,
        // thereby causing them to recalculate their xpath (which is why we
        // did not set recurse to true in calculateXPath above)
        final Iterator childIter = getChildren().iterator();
        while (childIter.hasNext()) {
            ODOMElement child = (ODOMElement) childIter.next();
            if (child instanceof ProxyElement) {
                ((ProxyElement) child).applySelection(event);
            }
        }
    }

    /**
     * Constructor
     *
     * @param details     The client-customizable dynamic traits of the proxy
     *                    and proxied elements
     * @param odomFactory Used for constructing missing target attributes and
     *                    elements (or deleting them) where necessary
     * @throws NullPointerException     if details parameter is null. We cannot
     *                                  throw an IllegalArgumentException due to the fact that details
     *                                  is used in the call to the super constructor which has to be the
     *                                  first statement in this constructor.
     * @throws IllegalArgumentException if odomFactory parameter is null.
     */
    public ProxyElement
            (ProxyElementDetails details, ODOMFactory odomFactory) {

        // Must construct ourselves properly: this does things like setting
        // up the element name and namespace. We pass in null for the
        // validator as the Proxy does not perform validation itself. The
        // ODOMElements that are being managed will validate themeselves
        // whenever their state changes.
        //
        // Note: We can't just pass the namespace to the super constructor
        // because this causes this.setNamespace to be invoked which, in the
        // case of the proxy, is an illegal operation.
        super(details.getElementName());
        super.setNamespace(details.getElementNamespace());

        // Check args and set up our instance fields
        this.details = details;
        if (odomFactory == null) {
            throw new IllegalArgumentException("Null ODOMFactory");
        }
        this.odomFactory = odomFactory;
        this.delegates = new ProxyElementDelegates
                (this.odomFactory, this.details, this.listener);
    }

    /**
     * Remove the empty targets
     *
     * @see ProxyElementDelegates#removeEmptyTargets
     */
    public void removeEmptyTargets() {
        delegates.removeEmptyTargets();
    }

    /**
     * Get the targets
     *
     * @return the list of targets.
     * @see ProxyElementDelegates#getTargets
     */
    public List getTargets() {
        return delegates.getTargets();
    }

    /**
     * Create empty targets.
     *
     * @see ProxyElementDelegates#createEmptyTargets
     */
    public void createEmptyTargets() {
        delegates.createEmptyTargets();
    }

    /**
     * This instance is registered by an external agency as a listener for
     * selection events (i.e. this instance is a listener but does not itself
     * add itself to the appropriate list of listeners). When these events
     * occur, this method passes it to this instance's supporting delegates
     * manager object, and also recursively gets its ProxyElement children
     * (if any) to do the same. Because of this recursion, this method makes
     * sure that if it receives such an event, it is the top-level
     * ProxyElement (i.e. does not have a parent).X
     *
     * @param event The selection event
     * @throws IllegalStateException if this instance is not a top-level
     *                               ProxyElement, i.e. if its parent is non-null (whatever its class, which
     *                               at any rate should be ProxyElement)
     */
    public void selectionChanged(ODOMElementSelectionEvent event) {
        if (logger.isDebugEnabled()) {
            logger.debug("selectionChanged");
        }

        // First make the error check described in the method javadoc
        if (getParent() != null) {
            throw new IllegalStateException
                    ("selectionChanged only allowed at top level");
        }

        // Recursively apply the selection to this instance's delegates object
        // and make child ProxyElements of this instance (if any) to do the
        // same to their delegates instance: note that this method includes
        // calculating the xpath for use by the delegates
        applySelection(event);
    }

    /**
     * The {@link #xpath} attribute should be calculated and stored. This is
     * generated from the given name (or the elementName from the
     * ProxyElementDetails) and the path of the parent ProxyElement, if
     * there is one.
     * <p/>
     * <p>Note that this method is only allowed to be called when there
     * is currently a non-empty selection being managed by the supporting
     * delegates object: in this case, an IllegalStateException is thrown
     *
     * @param element The new parent
     * @throws IllegalStateException as described above
     */
    protected Element setParent(Element element)
            throws IllegalStateException {

        // First check the error condition described in the method comments
        if (delegates.size() != 0) {
            throw new IllegalStateException
                    ("Cannot setParent when there is a selection");
        }

        // Set the parent in the super first, then calculate xpath because
        // that method depends on the parent's xpath, and queries what its
        // parent is (so do things in this order)
        final Element result = super.setParent(element);
        calculateXPath(true);
        return result;
    }

    /**
     * Sets the text of the element.
     */
    public Element setText(String str) {
        if (logger.isDebugEnabled()) {
            logger.debug("setText: value=\"" + str + "\"");
        }

        // First make sure that the text exists. This is because clients
        // of this class are only allowed to get and set values on EXTANT
        // nodes
        final Text text = getChildTextNode();
        if (text == null) {
            throw new IllegalArgumentException(
                    "ProxyElement does not contain text");
        }

        // Propagate it to the targets and set it in this instance
        delegates.propagateSetText(str);
        text.setText(str);

        return this;
    }

    /**
     * Retrieves the first text node within the element.
     *
     * @return The first text node, or null if none exist
     */
    private Text getChildTextNode() {
        List children = getContent();
        Text text = null;
        if (children != null) {
            Iterator it = children.iterator();
            while (it.hasNext() && text == null) {
                Object nextChild = it.next();
                if (nextChild instanceof Text) {
                    text = (Text) nextChild;
                }
            }
        }
        return text;
    }

    /**
     * Not allowed because this is too general and anyway text/string content
     * is not allowed. Only ProxyElement children are allowed, for which
     * setContent(Element) is used
     */
    public Element setContent(List list) {
        throw new UnsupportedOperationException("setContent(List) not permitted in the ProxyElement");
    }

    /**
     * UNSUPPORTED
     */
    public Element addContent(String str) {
        throw new UnsupportedOperationException("addContent(String) not permitted in the ProxyElement");
    }

    /**
     * Support adding of proxied text elements.
     */
    public Element addContent(Text txt) {
        if (!(txt instanceof ProxyText)) {
            throw new IllegalStateException("ProxyElement can only have ProxyText text children");
        } else if (getChildTextNode() != null) {
            throw new IllegalStateException("ProxyElement can only have a single text node");
        }
        return super.addContent(txt);
    }

    /**
     * Proxy sub-elements are allowed.
     */
    public Element addContent(Element element) {
        if (!(element instanceof ProxyElement)) {
            throw new IllegalStateException("ProxyElement can only have children class ProxyElement");
        }
        return super.addContent(element);
    }

    /**
     * UNSUPPORTED
     */
    public Element addContent(ProcessingInstruction instruction) {
        throw new UnsupportedOperationException("addContent(PI) not permitted in the ProxyElement");
    }

    /**
     * UNSUPPORTED
     */
    public Element addContent(EntityRef ref) {
        throw new UnsupportedOperationException("addContent(EntityRef) not permitted in the ProxyElement");
    }

    /**
     * UNSUPPORTED
     */
    public Element addContent(Comment comment) {
        throw new UnsupportedOperationException("addContent(Comment) not permitted in the ProxyElement");
    }

    /**
     * UNSUPPORTED
     */
    public Element setAttributes(List list) {
        throw new UnsupportedOperationException("Attributes are controlled by the ProxyElement's details");
    }

    // javadoc inherited
    public List getAttributes() {
        Iterator attribsIter = super.getAttributes().iterator();
        List result = new ArrayList();
        while (attribsIter.hasNext()) {
            ODOMAttribute attribute = (ODOMAttribute) attribsIter.next();
            // TODO This should not return a new one every time (ideally)
            result.add(new ProxyAttribute(attribute, this));
        }
        return result;
    }

    // javadoc inherited
    public Attribute getAttribute(String str) {
        ODOMAttribute attribute = (ODOMAttribute) super.getAttribute(str);
        if (attribute != null) {
            // TODO This should not return a new one every time (ideally)
            attribute = new ProxyAttribute(attribute, this);
        }
        return attribute;
    }

    // javadoc inherited
    public Attribute getAttribute(String str, Namespace namespace) {
        ODOMAttribute attribute =
                (ODOMAttribute) super.getAttribute(str, namespace);
        return new ProxyAttribute(attribute, this);
    }

    /**
     * Identical to this.setAttribute(attribute.getName(),attribute.getValue())
     */
    public Element setAttribute(Attribute attribute) {
        return this.setAttribute(attribute.getName(), attribute.getValue());
    }

    // javadoc inherited
    public Element setAttribute(String attribName, String attribValue) {
        if (logger.isDebugEnabled()) {
            logger.debug("setAttribute: name=\"" + attribName +
                    "\" value=\"" + attribValue + "\"");
        }
        // First make sure that the attribute exists. This is because clients
        // of this class are only allowed to get and set values on EXTANT
        // attributes; the control of the lifecycle of attributes is managed
        // ONLY by the AggregationListener instance within this class
        final Attribute attribute = super.getAttribute(attribName);
        if (attribute == null) {
            throw new IllegalArgumentException("Bad attribute name");
        }

        // Propagate it to the targets and set it in this instance
        delegates.propagateSetAttribute(attribName, attribValue);
        attribute.setValue(attribValue);

        return this;
    }

    /**
     * UNSUPPORTED
     */
    public Element setAttribute
            (String str, String str1, Namespace namespace) {
        // We don't have attributes with separate namespaces
        throw new UnsupportedOperationException("Per-attribute namespaces are not supported");
    }

    /**
     * UNSUPPORTED
     */
    public boolean removeAttribute(String str) {
        throw new UnsupportedOperationException("Attributes are controlled by the ProxyElement's details");
    }

    /**
     * UNSUPPORTED
     */
    public boolean removeAttribute(Attribute attribute) {
        throw new UnsupportedOperationException("Attributes are controlled by the ProxyElement's details");
    }

    /**
     * UNSUPPORTED
     */
    public boolean removeAttribute(String str, Namespace namespace) {
        throw new UnsupportedOperationException("Per-namespace attributes not supported");
    }

    /**
     * UNSUPPORTED
     */
    public boolean removeContent(ProcessingInstruction instruction) {
        throw new UnsupportedOperationException("Content removal not permitted in the ProxyElement");
    }

    /**
     * UNSUPPORTED
     */
    public boolean removeContent(Comment comment) {
        throw new UnsupportedOperationException("Content removal not permitted in the ProxyElement");
    }

    /**
     * UNSUPPORTED
     */
    public boolean removeContent(Text text) {
        throw new UnsupportedOperationException("Content removal not permitted in the ProxyElement");
    }

    /**
     * UNSUPPORTED
     */
    public boolean removeContent(EntityRef ref) {
        throw new UnsupportedOperationException("Content removal not permitted in the ProxyElement");
    }

    /**
     * UNSUPPORTED
     */
    public Element setChildren(List list) {
        throw new UnsupportedOperationException("setChildren not permitted in the ProxyElement");
    }

    // javadoc inherited
    public Element setNamespace(Namespace namespace) {

        // Only complain if the namespace is not null, as if it is null then
        // it's not really an attempt to set the namespace: such a call with
        // a null namespace may be fired from within the JDOM code during
        // this instance's constructor's call to the superclass constructor
        if (namespace != null) {
            throw new UnsupportedOperationException("The namespace is controlled by the ProxyElementDetails");
        } else {

            // Necessary to explicitly do this as there's a bug in JDOM whereby
            // if you don't do this, getQualifiedName generates a
            // NullPointerException because the namespace object is still null,
            // whereas this sets it to a non-null (empty) value
            super.setNamespace(null);
        }
        return this;
    }

    // javadoc inherited
    // TODO Should this recalculate the xpath?
    public Element setName(String str) {

        // Set the name on the super (which will notify any listeners) and then
        // propagate the new name to the delegates (targets) manager object
        super.setName(str);

        // It is necessary to do this test as this method might be called from
        // within JDOM code as part of the call to the super's constructor
        // from this instance's constructor, which will occur when the
        // delegates object is still null
        if (delegates != null) {
            delegates.propagateSetName(str);
        }
        return this;
    }

    /**
     * Remove children of the current selection.
     */
    public void removeTargetChildren() {
        if (delegates != null) {
            List targetList = delegates.getTargets();
            if (targetList != null) {
                Iterator it = targetList.iterator();
                while (it.hasNext()) {
                    Element target = (Element) it.next();
                    if (target != null) {
                        target.removeChildren();
                    }
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

 16-Jun-05	8807/1	adrianj	VBM:2005050502 Fix for style pair editing in builder

 16-Jun-05	8801/2	adrianj	VBM:2005050502 Fix for style pair editing in builder

 15-Jun-05	8758/1	adrianj	VBM:2005050502 Fix for style pair editing in builder
 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 09-Jul-04	4841/1	adrianj	VBM:2004010802 Removal of ODOMValidatable infrastructure

 10-Mar-04	3256/6	byron	VBM:2004021106 The theme StyleList property types do not work in the theme editor - review issues

 09-Mar-04	3256/4	byron	VBM:2004021106 The theme StyleList property types do not work in the theme editor - rework issues

 03-Mar-04	3256/2	byron	VBM:2004021106 The theme StyleList property types do not work in the theme editor

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 27-Jan-04	2765/1	doug	VBM:2004012608 Fixed namespace related bug in the ProxyElement

 23-Jan-04	2682/5	doug	VBM:2003112506 Added StylePropertiesSection to eclipse gui

 23-Jan-04	2682/3	doug	VBM:2003112506 Added StylePropertiesSection to eclipse gui

 17-Dec-03	2137/12	richardc	VBM:2003120402 Version for initial review (code and test harness)

 16-Dec-03	2203/4	richardc	VBM:2003121102 Process element hierarchy event fixed for all types of source/target relationship

 15-Dec-03	2160/3	doug	VBM:2003120702 Modified ODOMObservables so that they cab validate themesevles.

 12-Dec-03	2123/1	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 10-Dec-03	1968/17	richardc	VBM:2003111502 Second semi-tested draft for code release

 03-Dec-03	1968/9	richardc	VBM:2003111502 Untested draft for code review

 28-Nov-03	2041/1	byron	VBM:2003111502 Provide sophisticated, hierarchical ODOM ProxyElement - testcases

 28-Nov-03	1968/6	richardc	VBM:2003111502 Code complete subject to javadoc, nulls check, exception details and review

 27-Nov-03	1968/4	richardc	VBM:2003111502 Remove self-propagation

 27-Nov-03	1968/2	richardc	VBM:2003111502 First draft for unit test

 ===========================================================================
*/
