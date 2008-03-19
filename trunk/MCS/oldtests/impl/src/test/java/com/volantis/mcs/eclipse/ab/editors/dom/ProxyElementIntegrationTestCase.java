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
import com.volantis.mcs.eclipse.common.odom.xpath.ODOMXPath;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import junit.framework.Assert;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

class Utils {

    private static final XMLOutputter outPutter = new XMLOutputter();

    private static final HashMap reasonStringMap = new HashMap();

    static {
        reasonStringMap.put(ProxyElementDetails.ATTRIB_VALUES, "ATTRIB_VALUES");
        reasonStringMap.put(ProxyElementDetails.ATTRIBUTES, "ATTRIBUTES");
        reasonStringMap.put(ProxyElementDetails.ELEMENT_NAMES, "ELEMENT_NAMES");
        reasonStringMap.put(ProxyElementDetails.ELEMENTS, "ELEMENTS");
    }

    public static void print(Object o) {
        try {
            if (o instanceof Element) {
                outPutter.output((Element)o, System.out);
            } else if (o instanceof Document) {
                outPutter.output((Document)o, System.out);
            } else if (o instanceof List) {
                Iterator iter = ((List)o).iterator();
                while (iter.hasNext()) {
                    print(iter.next());
                }
            } else {
                System.out.println(o);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot print");
        }
    }

    /**
     * This returns a sorted copy of the attributes in the given element: the
     * sorting algorithm is arbitrary but consistent, enabling the correct
     * comparison between sets of attributes in different elements
     */
    private static Attribute[] getAttributesSortedCopy(Element e) {

        // Order of the attributes is important for comparison hence the
        // use of a TreeMap which explicitly supports this.
        final TreeMap map = new TreeMap();
        final Iterator attsIter = e.getAttributes().iterator();

        // Loop through the attributes
        while (attsIter.hasNext()) {
            // Form a hashcode that is generated from the name and value and
            // use this to index the attribute in a map
            Attribute attribute = (Attribute)attsIter.next();
            map.put(
                    new Integer(
                            (attribute.getName() + attribute.getValue()).hashCode()),
                    attribute);
        }
        return (Attribute[])map.values().toArray(new Attribute[map.size()]);
    }

    /**
     * This returns a sorted copy of the elements in the List: the
     * sorting algorithm is arbitrary but consistent, enabling the correct
     * comparison between sets of elements in different elements
     */
    private static Element[] getChildrenSortedCopy(List e) {

        // Order of the attributes is important for comparison hence the
        // use of a TreeMap which explicitly supports this.
        final TreeMap map = new TreeMap();
        final Iterator elesIter = e.iterator();

        // Loop through the children
        while (elesIter.hasNext()) {
            Element element = (Element)elesIter.next();

            // If an element is null (e.g. from a null target) then
            // set it to the same element defined in the DTD for null
            if (element == null) {
                element = new Element("null");
            }

            // Form a hashcode that is generated from the elements name, and
            // its attribs names and values and use this to index the element
            // in a map
            final StringBuffer buff = new StringBuffer(element.getName());
            Attribute[] sortedAtts = getAttributesSortedCopy(element);
            for (int i = 0; i < sortedAtts.length; i++) {
                buff.append(sortedAtts[i].getName()).
                        append(sortedAtts[i].getValue());
            }
            map.put(new Integer(buff.toString().hashCode()), element);
        }
        return (Element[])map.values().toArray(new Element[map.size()]);
    }

    /**
     * This compares the set of attributes in each element and throws an
     * unchecked exception if the sets are different in number or content
     * (names and values)
     */
    private static void compareAttributes(Element a, Element b)
            throws IllegalStateException {

        // First check the attribute count in each, then if OK order them in
        // an arbitrary but consistent way
        Assert.assertEquals(
                "Atts count",
                a.getAttributes().size(),
                b.getAttributes().size());
        final Attribute[] aSorted = getAttributesSortedCopy(a);
        final Attribute[] bSorted = getAttributesSortedCopy(b);
        Assert.assertEquals("Unique att count", aSorted.length, bSorted.length);

        // Now we just have to check the names and values
        for (int i = 0; i < aSorted.length; i++) {
            Assert.assertEquals(
                    "Atts name",
                    aSorted[i].getName(),
                    bSorted[i].getName());
            Assert.assertEquals(
                    "Atts value",
                    aSorted[i].getValue(),
                    bSorted[i].getValue());
        }
    }

    /**
     * Bitmasks for how much of elements to compare
     */
    public static final int COMP_NAME = 1;

    public static final int COMP_ATTS = 2;

    public static final int COMP_RECURSE = 4;

    /**
     * This compares one set of elements against
     * another and throws an unchecked exception if the sets are different
     * in number or content (children or attribute names and values)
     */
    public static void compareElements(
            String aIs, List a, String bIs, List b, int mask, boolean print)
            throws IllegalStateException {

        // Dump id requested (lists of elements are handled)
        if (print) {
            System.out.println("\nAbout to compare: " + aIs + ".....");
            Utils.print(a);
            System.out.println("\nwith " + bIs + ".....");
            Utils.print(b);
        }

        // First check the attribute count in each, then if OK order them in
        // an arbitrary but consistent way
        Assert.assertEquals("Elements count", a.size(), b.size());
        final Element[] aSorted = getChildrenSortedCopy(a);
        final Element[] bSorted = getChildrenSortedCopy(b);
        Assert.assertEquals("Unique ele count", aSorted.length, bSorted.length);

        // Now we just have to check the names, attributes and children
        for (int i = 0; i < aSorted.length; i++) {

            if ((mask & COMP_NAME) != 0) {
                Assert.assertEquals(
                        "Element name",
                        aSorted[i].getName(),
                        bSorted[i].getName());
            }

            if ((mask & COMP_ATTS) != 0) {
                compareAttributes(aSorted[i], bSorted[i]);
            }

            if ((mask & COMP_RECURSE) != 0) {
                compareElements(
                        null, aSorted[i].getChildren(),
                        null, bSorted[i].getChildren(),
                        mask, false);
            }
        }
    }

    /**
     * This compares one set of elements against
     * another and throws an unchecked exception if the sets are different
     * in number or content (children or attribute names and values)
     */
    public static void compareElements(
            String aIs, Element[] a,
            String bIs, Element[] b,
            int mask, boolean print)
            throws IllegalStateException {
        compareElements(
                aIs, Arrays.asList(a), bIs, Arrays.asList(b), mask, print);
    }

    /**
     * This compares the two specified elements nd throws an
     * unchecked exception if there are any differences at all in their names,
     * their attributes (names or values) or children (recursively)
     */
    public static void compareElements(
            String aIs, Element a,
            String bIs, Element b,
            int mask, boolean print)
            throws IllegalStateException {
        compareElements(
                aIs, new Element[]{a}, bIs, new Element[]{b}, mask, print);
    }

    /**
     * Convenience function for extracting values of all attributes present
     * in an element
     */
    public static void getAttribNames(Element from, List to) {
        Iterator attsIter = from.getAttributes().iterator();
        while (attsIter.hasNext()) {
            Attribute a = (Attribute)attsIter.next();
            to.add(a.getName());
        }
    }

    /**
     * Convenience function for extracting names of all attributes present
     * in an element
     */
    public static void getAttribValues(Element from, List to) {
        Iterator attsIter = from.getAttributes().iterator();
        while (attsIter.hasNext()) {
            Attribute a = (Attribute)attsIter.next();
            to.add(a.getValue());
        }
    }

    /*
     * Maps a ChangeReason to a readable string
     */
    public static String getReasonString(
            ProxyElementDetails.ChangeReason reason) {
        return (String)reasonStringMap.get(reason);
    }
}

interface ITestProxyElementDetails extends ProxyElementDetails {
    List getProxiedElements();

    void setAttributeNames(List names);

    void setElementName(String name);

    int getSetProxiedsCallCount();

    ProxyElementDetails.ChangeReason getLastReason();

    void resetSetProxiedsCallCount();
}

class TestProxyElementDetails implements ITestProxyElementDetails {
    private final List attribNames = new ArrayList();

    private final List proxiedElements = new ArrayList();

    private String elementName;

    private int setProxiedsCallCount;

    private ProxyElementDetails.ChangeReason lastReason;

    public TestProxyElementDetails(String elementName) {
        this.elementName = elementName;
    }

    public boolean setProxiedElements(
            Iterator elements, ProxyElementDetails.ChangeReason reason) {

        setProxiedsCallCount++;
        lastReason = reason;

        proxiedElements.clear();
        Utils.print(
                "\n\n>>>> setProxiedElements ("
                + elementName
                + ") ("
                + Utils.getReasonString(reason)
                + ")");
        while (elements.hasNext()) {
            Element e = (Element)elements.next();
            Utils.print(e);
            Utils.print("");
            proxiedElements.add(e);
        }
        Utils.print("<<<< setProxiedElements exit\n");
        // For integration tests, ALWAYS return true, as we always want to
        // check the proxied elements (and the false case, which is trivial,
        // can be inspection- or unit-tested)
        return true;
    }

    public String getElementName() {
        return elementName;
    }

    public Namespace getElementNamespace() {
        return Namespace.NO_NAMESPACE;
    }

    public String[] getAttributeNames() {
        return (String[])attribNames.toArray(new String[attribNames.size()]);
    }

    public boolean isAttributeName(String name) {
        return attribNames.contains(name);
    }

    // EXTENSIONS //

    public List getProxiedElements() {
        return proxiedElements;
    }

    public void setAttributeNames(List names) {
        Utils.print("\nsetAttributeNames (" + elementName + ")");
        attribNames.clear();
        Iterator iter = names.iterator();
        while (iter.hasNext()) {
            String name = (String)iter.next();
            Utils.print(name);
            attribNames.add(name);
        }
        Utils.print("\nsetAttributeNames exit\n");
    }

    public void setElementName(String name) {
        elementName = name;
    }

    public int getSetProxiedsCallCount() {
        return setProxiedsCallCount;
    }

    public ProxyElementDetails.ChangeReason getLastReason() {
        return lastReason;
    }

    public void resetSetProxiedsCallCount() {
        setProxiedsCallCount = 0;
        lastReason = null;
    }
}

class TestProxyElement extends ProxyElement {
    public final ITestProxyElementDetails details;

    public TestProxyElement(
            ITestProxyElementDetails details,
            ODOMFactory odomFactory) {
        super(details, odomFactory);
        this.details = details;
    }
}

/**
 * Test cases for ProxyElement
 * @todo fix TEST_DIRY stuff to use resources
 */
public class ProxyElementIntegrationTestCase extends TestCaseAbstract {
    

    // These are common to all tests and all scenarios
    private final static File TEST_DIRY = new File(
        ProxyElementIntegrationTestCase.class.getResource(".").getFile());

    private final SAXBuilder builder = new SAXBuilder(true);

    private final ODOMFactory odomFactory = new ODOMFactory();

    private final HashMap elementsMap = new HashMap();

    private final HashMap hierarchyMap = new HashMap();

    private final HashMap reasonMap = new HashMap();

    private ODOMElement currentSelection = null;

    // These are the actual test objects (see the script DTD)
    private final ProxyElement topEle =
            new TestProxyElement(new TestProxyElementDetails("topA"), odomFactory);

    private final ProxyElement middleEle =
            new TestProxyElement(
                    new TestProxyElementDetails("middleA"),
                    odomFactory);

    private final ProxyElement bottomEle =
            new TestProxyElement(
                    new TestProxyElementDetails("bottomA"),
                    odomFactory);

    // This only needs to be done once for all tests and scenarios
    public ProxyElementIntegrationTestCase() {

        // Make sure we build ODOM stuff
        builder.setIgnoringElementContentWhitespace(true);
        builder.setFactory(odomFactory);

        // Set the parent/child relationships according to the DTD
        hierarchyMap.put(bottomEle, middleEle);
        hierarchyMap.put(middleEle, topEle);

        middleEle.addContent(bottomEle);
        topEle.addContent(middleEle);

        // ProxyElement lookup table
        elementsMap.put(topEle.getName(), topEle);
        elementsMap.put(middleEle.getName(), middleEle);
        elementsMap.put(bottomEle.getName(), bottomEle);

        // Listen for proxy element name changes to maintain elementsMap
        Iterator proxyIter = elementsMap.values().iterator();
        while (proxyIter.hasNext()) {
            ((ODOMElement)proxyIter.next()).addChangeListener(
                    new ODOMChangeListener() {
                        public void changed(
                                ODOMObservable node, ODOMChangeEvent event) {
                            final ProxyElement changed =
                                    (ProxyElement)event.getSource();
                            elementsMap.remove(event.getOldValue());
                            elementsMap.put(event.getNewValue(), changed);
                        }
                    }, ChangeQualifier.NAME);
        }

        // ChangeReason lookup table
        reasonMap.put("a", ProxyElementDetails.ATTRIBUTES);
        reasonMap.put("av", ProxyElementDetails.ATTRIB_VALUES);
        reasonMap.put("e", ProxyElementDetails.ELEMENTS);
        reasonMap.put("en", ProxyElementDetails.ELEMENT_NAMES);
        reasonMap.put("n", null);
    }

    /**
     * Run all the test scripts in sorted order.
     *
     * @throws Exception if a test fails
     */
    public void testAll() throws Exception {
        // Get all script.**.xml files
        final File[] scriptFiles = TEST_DIRY.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return (pathname.getName().startsWith("script.") &&
                        pathname.getName().endsWith(".xml"));
            }
        });

        if (scriptFiles == null) {
            throw new IllegalStateException("No scripts found!");
        }

        // Sort them into alphabetical order
        Arrays.sort(scriptFiles, new Comparator() {
            public int compare(Object o1, Object o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });

        // Now run the individual scripts!
        for (int i = 0; i < scriptFiles.length; i++) {
            Element script = builder.build(scriptFiles[i]).getRootElement();
            Utils.print("\n\nRunning script " + scriptFiles[i].getName());
            Utils.print(script.getAttribute("title").getValue());
            Utils.print("****************************************");
            runTestScript(script);
        }
    }

    // This method actually runs a script: it is called for each XML script
    // file, whose contents has been read into the specified Document, and
    // which is expected to be applied to the given ProxyElement
    private void runTestScript(Element script) throws Exception {
        // Loop through the script "directives" (1st generation kids of above)
        final Iterator directivesIter =
                script.getChildren().iterator();
        while (directivesIter.hasNext()) {
            runDirective((ODOMElement)directivesIter.next());
        }
    }

    // Runs an individual script directive
    private void runDirective(ODOMElement directive) throws Exception {
        final String directiveName = directive.getName();

        // All script elements must have an "info" attribute: print it
        Utils.print("\n\nScript element <" +
                    directiveName +
                    ">:\nExpected: " +
                    directive.getAttribute("expected").getValue());
        Utils.print("==============================");

        // Now action the top-level script element according to name
        if (directiveName.equals("applySelection")) {
            processApplySelection(directive);
        } else if (directiveName.equals("checkProxieds")) {
            processCheckProxieds(directive);
        } else if (directiveName.equals("checkProxy")) {
            processCheckProxy(directive);
        } else if (directiveName.equals("detailsGetters")) {
            processDetailsGetters(directive);
        } else if (directiveName.equals("selectionHierarchy")) {
            processSelectionHierarchy(directive);
        } else if (directiveName.equals("updateAttribValue")) {
            processUpdateAttribValue(directive);
        } else if (directiveName.equals("updateElementName")) {
            processUpdateElementName(directive);
        } else if ("updateSelection".equals(directiveName)) {
            processUpdateSelection(directive);
        } else if ("amendProxy".equals(directiveName)) {
            processAmendProxy(directive);
        } else {
            // Unsupported script operation
            throw new IllegalArgumentException(
                    "Unknown script element: " + directiveName);
        }
    }

    /**
     * Runs the applySelection directive.
     *
     * @param directive the directive to be executed
     */
    private void processApplySelection(ODOMElement directive) {
        // Make a note of what element tree constitutes the current
        // selection, and pass it to the top-level proxy element
        currentSelection = directive;
        topEle.selectionChanged(
                new ODOMElementSelectionEvent(currentSelection.getChildren()));
    }

    /**
     * Runs the checkProxieds directive.
     *
     * @param directive the directive to be executed
     */
    private void processCheckProxieds(ODOMElement directive) {
        // Get the test data from the directive
        final String name = directive.getAttribute("name").getValue();
        final String recurse = directive.getAttribute("recurse").getValue();

        // Use getAttributeValue as this one is optional
        final String callCount = directive.getAttributeValue("callCount");
        final String reason = directive.getAttributeValue("reason");

        // Get the appropriate ProxyElement
        final TestProxyElement proxyElement =
                (TestProxyElement)elementsMap.get(name);

        // Check the new proxied elements
        int mask = Utils.COMP_NAME | Utils.COMP_ATTS;
        if (recurse.equals("y")) {
            mask |= Utils.COMP_RECURSE;
        }
        Utils.compareElements(
                "expected proxieds for " + name,
                directive.getChildren(),
                "actual proxieds for " + name,
                proxyElement.details.getProxiedElements(),
                mask, true);

        // callCount and reason: must be both null or both non-null
        if (callCount == null) {

            // In null case, reset the calls count
            Assert.assertNull(reason);
            proxyElement.details.resetSetProxiedsCallCount();

        } else {

            // Check both against expected
            Assert.assertNotNull(reason);
            final int expectedSetProxiedsCallCount =
                    Integer.parseInt(callCount);
            final int actualSetProxiedsCallCount =
                    proxyElement.details.getSetProxiedsCallCount();

            Assert.assertEquals(
                    "setProxiedsCallCount for " + name,
                    expectedSetProxiedsCallCount,
                    actualSetProxiedsCallCount);

            Assert.assertEquals(
                    "Reason ",
                    Utils.getReasonString(
                            (ProxyElementDetails.ChangeReason)reasonMap.get(reason)),
                    Utils.getReasonString(
                            proxyElement.details.getLastReason()));
        }
    }

    /**
     * Runs the checkProxy directive.
     *
     * @param directive the directive to be executed
     */
    private void processCheckProxy(ODOMElement directive) {
        // Get the test data from the directive
        final String name = directive.getAttribute("name").getValue();
        final String recurse = directive.getAttribute("recurse").getValue();
        final List children = directive.getChildren();

        // Get the appropriate ProxyElement
        final ProxyElement proxyElement =
                (ProxyElement)elementsMap.get(name);

        // Check the proxy
        int mask = Utils.COMP_NAME | Utils.COMP_ATTS;
        if (recurse.equals("y")) {
            mask |= Utils.COMP_RECURSE;
        }
        Assert.assertEquals(1, children.size());
        Utils.compareElements(
                "expected proxy for " + name, (Element)children.get(0),
                "actual proxy for " + name, proxyElement,
                mask, true);
    }

    /**
     * Runs the detailsGetters directive.
     *
     * @param directive the directive to be executed
     */
    private void processDetailsGetters(ODOMElement directive) {
        // Get the test data from the directive
        final String name = directive.getAttribute("name").getValue();
        final Element attributeNames = directive.getChild("attributeNames");
        final Element elementName = directive.getChild("elementName");

        // Get the appropriate ProxyElement
        final TestProxyElement proxyElement =
                (TestProxyElement)elementsMap.get(name);

        // If there's a new set of attrib names (optional) then apply
        // it to the details object for the next setProxiedElements
        if (attributeNames != null) {
            List newAttribNames = new ArrayList();
            Utils.getAttribValues(attributeNames, newAttribNames);
            proxyElement.details.setAttributeNames(newAttribNames);
        }

        // Ditto for the element name
        if (elementName != null) {
            proxyElement.details.setElementName(
                    elementName.getAttribute("name").getValue());
        }
    }

    private void processSelectionHierarchy(ODOMElement directive) throws Exception {
        // Get the data from the test script
        final String action =
                directive.getAttribute("action").getValue();
        final Element p2s = directive.getChild("pathToSelection");
        final String contextStr =
                p2s.getAttribute("context").getValue();
        final String xpathStr = p2s.getAttribute("xpath").getValue();

        // This is never allowed to be empty...
        final ODOMXPath xpath = new ODOMXPath(xpathStr);
        Assert.assertEquals("xpath len", true, xpathStr.length() > 0);

        // ...but context may be, in which case it is taken to be
        // the entire selection
        final ODOMElement context =
                (contextStr.length() == 0
                ? currentSelection
                : (ODOMElement)getSingletonNode(
                        currentSelection, contextStr));

        // Just pass it off to the appropriate helper
        Utils.print("\nBefore selectionHierarchy:");
        Utils.print(currentSelection);
        if (action.equals("c")) {
            processCreate(context, xpath);
        } else if (action.equals("d")) {
            processDelete(context, xpath);
        } else {
            throw new IllegalStateException("Unknown action: " + action);
        }
        Utils.print("\nAfter selectionHierarchy:");
        Utils.print(currentSelection);
    }

    /**
     * Runs the updateAttribValue directive.
     *
     * @param directive the directive to be executed
     */
    private void processUpdateAttribValue(ODOMElement directive) throws Exception {
        // Get the test data from the directive
        final List children = directive.getChildren();
        Assert.assertEquals("children", 1, children.size());
        final Element child = (Element)children.get(0);
        final String name = directive.getAttribute("name").getValue();
        final String value = directive.getAttribute("value").getValue();

        // Get the thing to operate on
        ODOMAttribute attribute = null;
        if (child.getName().equals("proxy")) {

            // It's a proxy element, so get it by name and lookup
            // the attribute
            final ODOMElement element = (ODOMElement)elementsMap.
                    get(child.getAttribute("name").getValue());
            attribute = (ODOMAttribute)element.getAttribute(name);

        } else if (child.getName().equals("pathToSelection")) {

            // It's an attribute within the selection, so first get
            // the context element
            final ODOMElement context = (ODOMElement)
                    getSingletonNode(
                            currentSelection,
                            child.getAttribute("context").getValue());

            // Use the context to get the attribute
            final String xpathStr = child.getAttribute("xpath").getValue();
            Assert.assertEquals("xpath len", true, xpathStr.length() > 0);
            attribute = (ODOMAttribute)getSingletonNode(context, xpathStr);

        } else {
            throw new IllegalStateException(
                    "Unknown child: " + child);
        }

        // This directive applies to EXTANT attributes only
        if (attribute == null) {
            throw new IllegalStateException("Could not find attrib");
        }

        // Now set the attribute
        Utils.print("\nBefore updateAttribValue:");
        Utils.print(attribute.getParent());
        attribute.setValue(value);
        Utils.print("\nAfter updateAttribValue:");
        Utils.print(attribute.getParent());
    }

    /**
     * Runs the updateElementName directive.
     *
     * @param directive the directive to be executed
     */
    private void processUpdateElementName(ODOMElement directive) throws Exception {
        // Get the test data from the directive
        final List children = directive.getChildren();
        Assert.assertEquals("children", 1, children.size());
        final Element child = (Element)children.get(0);
        final String newName = directive.getAttribute("newName").getValue();

        // Get the thing to operate on
        ODOMElement element = null;
        if (child.getName().equals("proxy")) {

            // It's a proxy element, so just look it up
            element = (ODOMElement)elementsMap.
                    get(child.getAttribute("name").getValue());

        } else if (child.getName().equals("pathToSelection")) {

            // It's in the selection, so first get the context....
            final ODOMElement context = (ODOMElement)
                    getSingletonNode(
                            currentSelection,
                            child.getAttribute("context").getValue());

            // ...then look up the actual element to update - note that
            // this is allowed to be of zero length as we are allowed
            // to rename a top-level element
            final String xpathStr = child.getAttribute("xpath").getValue();
            element =
                    (xpathStr.length() == 0
                    ? context
                    : (ODOMElement)getSingletonNode(context,
                                                    child.getAttribute("xpath").getValue()));

        } else {
            throw new IllegalStateException(
                    "Unknown child: " + child);
        }

        // This directive applies to EXTANT attributes only
        if (element == null) {
            throw new IllegalStateException("Could not find element");
        }

        // Now set the name
        Utils.print("\nBefore setName:");
        Utils.print(element);
        element.setName(newName);
        Utils.print("\nAfter setName:");
        Utils.print(element);
    }

    /**
     * Processes the updateSelection directive given.
     *
     * @param directive
     *         an element defining the set of XPaths that should be processed
     *         to become the new selection
     * @throws Exception if there is a problem processing the selection update
     */
    private void processUpdateSelection(ODOMElement directive)
            throws Exception {
        // Get the test data from the directive
        final List children = directive.getChildren();
        final List selection = new ArrayList(children.size());

        Iterator i = children.iterator();

        while (i.hasNext()) {
            ODOMElement e = (ODOMElement)i.next();

            if ("xpath".equals(e.getName())) {
                String xpath = e.getAttributeValue("path");

                if (xpath == null) {
                    throw new IllegalArgumentException(
                            "missing path atttribute");
                }

                // Add the resolved element
                selection.add(getSingletonNode(currentSelection, xpath));
            } else {
                throw new IllegalArgumentException(
                        "unrecognised child " + e.getName());
            }
        }

        // Apply the selection change to the top level proxy (which simulates
        // what should happen in the real environment)
        topEle.selectionChanged(
                new ODOMElementSelectionEvent(selection));
    }

    /**
     * Processes the amendProxy directive given.
     *
     * @param directive an element defining the modification required in the
     *                  proxy hierarchy
     * @throws Exception if there is a problem processing the selection update
     */
    private void processAmendProxy(ODOMElement directive) throws Exception {
        String proxyName = directive.getAttributeValue("name");
        String action = directive.getAttributeValue("action");

        ProxyElement proxy = (ProxyElement)elementsMap.get(proxyName);

        if (proxy == null) {
            throw new IllegalArgumentException(
                    "Proxy " + proxyName + " does not exist");
        } else {
            if ("connect".equals(action)) {
                if (proxy.getParent() != null) {
                    throw new IllegalArgumentException(
                            "Proxy " + proxyName +
                            " not currently disconnected");
                } else {
                    ProxyElement parent =
                            (ProxyElement)hierarchyMap.get(proxy);

                    if (parent == null) {
                        throw new IllegalArgumentException(
                                "Proxy " + proxyName +
                                " cannot be connected as it is top level");
                    } else {
                        parent.addContent(proxy);
                    }
                }
            } else if ("disconnect".equals(action)) {
                if (proxy.getParent() == null) {
                    throw new IllegalArgumentException(
                            "Proxy " + proxyName +
                            " cannot be disconnected as it is already " +
                            "disconnected or is top level");
                } else {
                    proxy.detach();
                }
            } else {
                throw new IllegalArgumentException(
                        "Action " + action + " not recognized");
            }
        }
    }

    // Helper method to create an entity in the current selection
    private void processCreate(
            ODOMElement context, ODOMXPath xpath) throws Exception {

        // Attempt the creation from the specified context
        final ODOMObservable created = xpath.create(context, odomFactory);
        Utils.print("\nCreate OK: " + xpath.getExternalForm());
        if (created == null) {
            throw new IllegalStateException("Nothing created with " + xpath);
        }

        // Creations only apply to attributes or elements; if a value is given,
        // attempt to stick it in
        if (created instanceof ODOMAttribute) {
            Utils.print("Attribute created: " + xpath.getExternalForm());
        } else if (created instanceof ODOMElement) {
            Utils.print("Element created: " + xpath.getExternalForm());
        } else {
            throw new IllegalStateException("Bad create: " + created);
        }
    }

    // Helper method to delete an entity in the current selection
    private void processDelete(
            ODOMElement context, ODOMXPath xpath) throws Exception {

        // Delete from the specified context
        final ODOMObservable deleted = xpath.remove(context);
        Utils.print("\nDeleted OK: " + xpath.getExternalForm());
        if (deleted instanceof ODOMAttribute) {
            Utils.print("Attribute deleted: " + xpath.getExternalForm());
        } else if (deleted instanceof ODOMElement) {
            Utils.print("Element deleted: " + xpath.getExternalForm());
        } else {
            throw new IllegalStateException("Bad delete: " + deleted);
        }
    }

    // Helper method to navigate to what the xpath is pointing to
    private ODOMObservable getSingletonNode(
            ODOMElement context,
            String xpathStr)
            throws Exception {

        // Update is expected to work on exactly one entity
        final XPath xpath = new XPath(xpathStr);
        final List finds = xpath.selectNodes(context);
        if (finds == null) {
            throw new IllegalStateException(
                    "Found null: " + xpath.getExternalForm());
        }
        if (finds.size() != 1) {
            throw new IllegalStateException(
                    "Found " + finds.size() + " in " + xpath.getExternalForm());
        }
        final ODOMObservable found = (ODOMObservable)finds.get(0);
        Utils.print("\nFind OK: " + xpath.getExternalForm());
        return found;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 21-Dec-04	6524/1	allan	VBM:2004112610 Move xpath and xml validation out of eclipse

 14-Dec-04	6451/4	ianw	VBM:2004121001 moved pipelind tld's and tidied up build.xml

 14-Dec-04	6451/2	ianw	VBM:2004121001 Fixup build and move some relase stuff back to development

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Sep-04	5674/1	philws	VBM:2004080406 Fix proxy element hierarchical source event problem seen with undoing a swap action on the layout editor

 01-Sep-04	5341/1	claire	VBM:2004090101 New Build Mechanism : MCS support for JDK 1.4.2_05

 09-Jul-04	4841/1	adrianj	VBM:2004010802 Removal of ODOMValidatable infrastructure

 19-Dec-03	2254/2	richardc	VBM:2003121901 Do nothing if target is detached but target==source

 18-Dec-03	2137/13	richardc	VBM:2003120402 Extra tests for event non-generation

 17-Dec-03	2137/11	richardc	VBM:2003120402 Version for initial review (code and test harness)

 03-Dec-03	1968/5	richardc	VBM:2003111502 Untested draft for code review

 ===========================================================================
*/
