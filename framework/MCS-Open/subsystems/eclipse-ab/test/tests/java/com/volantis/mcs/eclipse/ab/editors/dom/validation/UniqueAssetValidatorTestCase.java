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
 
package com.volantis.mcs.eclipse.ab.editors.dom.validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import junit.framework.Assert;

import com.volantis.mcs.eclipse.common.odom.ODOMElement;
import com.volantis.mcs.eclipse.ab.editors.dom.validation.UniqueAssetValidator;
import com.volantis.mcs.objects.PropertyValueLookUp;
import com.volantis.mcs.xml.validation.DOMSupplementaryValidationProvider;
import com.volantis.mcs.xml.validation.DOMSupplementaryValidator;
import com.volantis.mcs.xml.validation.ErrorReporter;
import com.volantis.mcs.xml.validation.ErrorDetails;
import com.volantis.mcs.xml.xpath.XPath;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import org.jdom.Element;

public class UniqueAssetValidatorTestCase extends TestCaseAbstract {
    
    // COPIED FROM 1.4 Collections source
    // TODO Remove when moved to 1.4
    private static void rotate(List list, int distance) {
        int size = list.size();
        if (size == 0)
            return;
        distance = distance % size;
        if (distance < 0)
            distance += size;
        if (distance == 0)
            return;

        for (int cycleStart = 0, nMoved = 0; nMoved != size; cycleStart++) {
            Object displaced = list.get(cycleStart);
            int i = cycleStart;
            do {
                i += distance;
                if (i >= size)
                    i -= size;
                displaced = list.set(i, displaced);
                nMoved++;
            } while (i != cycleStart);
        }
    }
    
    // The fixed asset name and expected identity attributes used for
    // all the tests
    public static final String ASSET_NAME = "textAsset";
    public static final String[] ID_ATTRIBS =
        new String[]{"project", "name", "deviceName", "language"};
    public static final String[] OTHER_ATTRIBS =
        new String[]{"dhansak", "biryani", "jalfrezi"};
        
    // The fixed name of the element that has dependent elements (and their
    // names and validator classes) used throughout the tests
    public static final String HAS_DEPS_ELEMENT_NAME = "imageComponent";
    public static final String[] DEPENDENT_ELEMENTS =
        new String[]{"deviceImageAsset", "genericImageAsset",
                     "convertibleImageAsset"};
    public static final Class[] DEP_ELES_VALIDATORS_CLASSES;
    static {
        DEP_ELES_VALIDATORS_CLASSES = new Class[DEPENDENT_ELEMENTS.length];
        for (int i = 0; i < DEP_ELES_VALIDATORS_CLASSES.length; i++) {
            DEP_ELES_VALIDATORS_CLASSES[i] = UniqueAssetValidator.class;
        }
    }
    
    // Constructor
    public UniqueAssetValidatorTestCase() {
        
        // Make sure that the actual ID attributes are what the test harness
        // thinks they are: if not, change ID_ATTRIBS above
        final List actualIdAtts =
            PropertyValueLookUp.getIdentityAttributes(ASSET_NAME);
        final List expectedIdAtts = Arrays.asList(ID_ATTRIBS);
        if(!expectedIdAtts.containsAll(actualIdAtts) ||
           !actualIdAtts.containsAll(expectedIdAtts)) {
            throw new IllegalStateException(
                "Update TH to change ID_ATTRIBS to match actualIdAtts");
        }
        
        // Make sure that the actual dependent elements are what the test
        // harness thinks they are: if not, change DEPENDENT_ELEMENTS above
        final List actualDepElements =
            PropertyValueLookUp.getDependentElements(hasDependents.getName());
        final List expectedDepElements = Arrays.asList(DEPENDENT_ELEMENTS);
        if(!expectedDepElements.containsAll(actualDepElements) ||
           !actualDepElements.containsAll(expectedDepElements)) {
            throw new IllegalStateException(
                "Update TH to change DEPENDENT_ELEMENTS to match actualIdAtts");
        }
    }

    // The instance under test
    private final UniqueAssetValidator uav = new UniqueAssetValidator();
    
    // The element to pass into the validate method
    private final ODOMElement assetParent = new ODOMElement("ignored");
    
    // Element for testing the registration
    private final ODOMElement hasDependents =
        new ODOMElement(HAS_DEPS_ELEMENT_NAME);
    
    // These are kept separate (until a test is actually run) so we can
    // shuffle them about without being attached to the parent
    private final List assets = new ArrayList();
    
    // Extension for testing
    private interface DOMSupplementaryValidationProviderTest
        extends DOMSupplementaryValidationProvider {
        void checkContents(String[] elementNames, Class[] classes);
        void clearContents();
    }
    
    // DOMSupplementaryValidationProvider implementation for testing
    private static final DOMSupplementaryValidationProviderTest DSVP =
        new DOMSupplementaryValidationProviderTest() {
        private final HashMap elementNamesToValidators = new HashMap();

        public void addSupplementaryValidator(
            String namespaceURI,
            String elementName,
            DOMSupplementaryValidator supplementaryValidator) {
            if (elementNamesToValidators.containsKey(elementName)) {
                throw new IllegalStateException("Added twice: " + elementName);
            }
            elementNamesToValidators.put(elementName, supplementaryValidator);
        }

        public void removeSupplementaryValidator(
            String namespaceURI,
            String elementName,
            DOMSupplementaryValidator supplementaryValidator) {
            if (!elementNamesToValidators.containsKey(elementName)) {
                throw new IllegalStateException("Not present: " + elementName);
            }
            elementNamesToValidators.remove(elementName);
        }

        // Extensions
        public void checkContents(String[] elementNames, Class[] classes) {
            
            // Check for silly args
            if (elementNames.length != classes.length) {
                throw new IllegalArgumentException("Bad test arrays");
            }
            
            // Check map size
            if (elementNames.length != elementNamesToValidators.size()) {
                throw new IllegalStateException("Wrong validator map size");
            }
            
            // Check map contents
            for (int i = 0; i < elementNames.length; i++) {
                final Class actualClass =
                    elementNamesToValidators.get(elementNames[i]).getClass();
                if (actualClass == null) {
                    throw new IllegalStateException(
                        "Not present: " + elementNames[i]);
                }
                final Class expectedClass = classes[i];
                if (!actualClass.equals(expectedClass)) {
                    throw new IllegalStateException(
                        "Bad class:" + actualClass.getName());
                }
            }
        }
        
        public void clearContents() {
            elementNamesToValidators.clear();
        }
    };
    
    // Extension for testing
    private interface ErrorReporterTest extends ErrorReporter {
        ErrorReporter reset();
        void assertReports(String preds, boolean checkOrder);
    }
    
    // ErrorReporter implementation for testing
    private static final ErrorReporterTest ER = new ErrorReporterTest() {
        private boolean started;
        private boolean completed;
        private StringBuffer preds = new StringBuffer();

        public void reportError(ErrorDetails details) {
            XPath xpath = details.getXPath();

            System.out.println("reportError: " + xpath.getExternalForm());
            final String xpstr = xpath.getExternalForm();
            final int idxOfPredOpen = xpstr.indexOf('[');
            Assert.assertFalse("[ not found", idxOfPredOpen < 0);
            final char predCh = xpstr.charAt(idxOfPredOpen + 1);
            Assert.assertTrue(predCh + " not digit", Character.isDigit(predCh));
            preds.append(predCh);
        }

        public void validationStarted(Element root, XPath xpath) {
            if(started || completed) {
                throw new IllegalArgumentException("Bad start state");
            }
            started = true;
        }

        public void validationCompleted(XPath xpath) {
            if(!started || completed) {
                throw new IllegalArgumentException("Bad completed state");
            }
            completed = true;
        }
        
        public ErrorReporter reset() {
            started = false;
            completed = false;
            preds.setLength(0);
            return this;
        }
        
        public void assertReports(String preds, boolean checkOrder) {
            if (checkOrder) {
                Assert.assertEquals(preds, this.preds.toString());
            } else {
                Assert.assertEquals(preds.length(), this.preds.length());
            }
        }
    };
    
    private void setIdAttrib(int childPos, int attPos, String value) {
        for(int i = 0; i < childPos + 1 - assets.size(); i++) {
            assets.add(new ODOMElement(ASSET_NAME));
        }
        final ODOMElement element = (ODOMElement)assets.get(childPos);
        final String name = ID_ATTRIBS[attPos];
        Assert.assertNull("Att exists: " + name, element.getAttribute(name));
        element.setAttribute(name, value);
    }
    
    private void setOtherAttrib(int childPos, int attPos) {
        for(int i = 0; i < childPos + 1 - assets.size(); i++) {
            assets.add(new ODOMElement(ASSET_NAME));
        }
        final ODOMElement element = (ODOMElement)assets.get(childPos);
        element.setAttribute(OTHER_ATTRIBS[attPos], "someNonIdValue");
    }
    
    private void doTest(
        String info, String preds, boolean checkOrder, boolean standalone) {
        
        // First attach the assets - use a clone so that the following
        // getContent().clear() does not destroy them
        System.out.println("\nTest: " + info);
        assetParent.setContent(Arrays.asList(assets.toArray()));
        
        // Then run the test
        uav.validate(assetParent, ER.reset());
        ER.assertReports(preds, checkOrder);
        
        // Then make sure the element is clear of assets before the
        // next test is run (so the above attach works)
        assetParent.getContent().clear();
        
        // If this is a stand-alone test, reset the assets for next time
        if (standalone) {
            assets.clear();
        }
    }
    
    private void doPerms(String info, String preds) {

        // This method is for tests with > 1 asset
        Assert.assertTrue("Need > 1 assets", assets.size() > 1);
        
        // Try out various permutations of assets
        for(int i = 0; i < assets.size() - 1; i++) {
            
            // Do it as the caller intended, then reversed, then rotated
            doTest(info + " (original)", preds, i == 0, false);
            Collections.reverse(assets);
            doTest(info + " (reversed)", preds, false, false);
            rotate(assets, 1);
            doTest(info + " (rotated)", preds, false, false);

            // Now shuffle it about a few times and try each            
            for(int shuf = 0; shuf < assets.size() - 1; shuf++) {
                Collections.shuffle(assets);
                doTest(info + " (shuffled)", preds, false, false);
            }
        }
        
        // Finally clear it for the next test
        assets.clear();
    }
    
    public void testEmpty() {
        doTest("Empty", "", true, true);
    }
    
    public void testOneAsset_A() {
        setIdAttrib(0, 0, "0");
        doTest("1 asset, 1 id", "", true, true);
    }
    
    public void testOneAsset_B() {
        setOtherAttrib(0, 0);
        doTest("1 asset, 1 other", "", true, true);
    }
    
    public void testOneAsset_C() {
        setIdAttrib(0, 0, "0");
        setOtherAttrib(0, 0);
        doTest("1 asset, 1 id, 1 other", "", true, true);
    }
    
    public void testTwoAsset_A() {
        setIdAttrib(0, 0, "0");
        setIdAttrib(1, 0, "0");
        doPerms("Minimal 2-way match", "12");
    }
    
    public void testTwoAsset_B() {
        setIdAttrib(0, 0, "0");
        setIdAttrib(0, 1, "1");
        setIdAttrib(0, 2, "2");
        setIdAttrib(1, 0, "0");
        setIdAttrib(1, 1, "1");
        setIdAttrib(1, 2, "2");
        doPerms("Maximal clean 2-way match", "12");
    }
    
    public void testTwoAsset_C() {
        setIdAttrib(0, 2, "2");
        setIdAttrib(0, 0, "0");
        setIdAttrib(0, 1, "1");
        setOtherAttrib(0, 1);
        setIdAttrib(1, 0, "0");
        setOtherAttrib(1, 2);
        setIdAttrib(1, 1, "1");
        setIdAttrib(1, 2, "2");
        doPerms("As last but with reordering and other attribs", "12");
    }
    
    public void testTwoAsset_D() {
        setOtherAttrib(0, 1);
        setOtherAttrib(1, 2);
        doPerms("Empty id subset match", "12");
    }
    
    public void testTwoAsset_E() {
        setIdAttrib(0, 0, "0");
        setIdAttrib(1, 1, "1");
        doPerms("Null intersection 2-way mismatch", "");
    }
    
    public void testTwoAsset_F() {
        setIdAttrib(0, 0, "0");
        setIdAttrib(1, 0, "0");
        setIdAttrib(1, 1, "1");
        doPerms("Subset/superset 2-way mismatch", "");
    }
    
    public void testTwoAsset_G() {
        setIdAttrib(0, 0, "0");
        setIdAttrib(0, 2, "2");
        setIdAttrib(1, 0, "0");
        setIdAttrib(1, 1, "1");
        doPerms("2-way subset/superset 2-way mismatch", "");
    }
    
    public void testTwoAsset_H() {
        setIdAttrib(0, 0, "0a");
        setIdAttrib(1, 0, "0b");
        doPerms("Minimal value mismatch", "");
    }
    
    public void testMultiAsset_A() {
        setIdAttrib(0, 0, "0");
        setIdAttrib(1, 1, "1");
        setIdAttrib(2, 0, "0");
        doPerms("Multi-matches: [1],[3]", "13");
    }
    
    public void testMultiAsset_B() {
        setIdAttrib(0, 0, "0");
        setIdAttrib(1, 0, "0");
        setIdAttrib(2, 1, "1");
        setIdAttrib(3, 0, "0");
        setIdAttrib(4, 0, "0");
        doPerms("Multi-matches: [1],[2],[4],[5] (all same)", "1245");
    }
    
    public void testMultiAsset_C() {
        setIdAttrib(0, 0, "0");
        setIdAttrib(1, 0, "0");
        setIdAttrib(2, 1, "1");
        setIdAttrib(3, 2, "2");
        setIdAttrib(4, 2, "2");
        doPerms("Multi-matches: [1],[2],[4],[5] (2 pairs)", "1245");
    }
    
    public void testMultiAsset_D() {
        setIdAttrib(0, 0, "0");
        setIdAttrib(1, 2, "2");
        setIdAttrib(2, 1, "1");
        setIdAttrib(3, 0, "0");
        setIdAttrib(4, 2, "2");
        doPerms("Multi-matches: [1],[2],[4],[5] (another 2 pairs)", "1245");
    }
    
    public void testMultiAsset_E() {
        setIdAttrib(0, 0, "0");
        setIdAttrib(1, 1, "1");
        setIdAttrib(2, 2, "2");
        setIdAttrib(3, 0, "1");
        setIdAttrib(4, 1, "2");
        setIdAttrib(5, 2, "0");
        setIdAttrib(6, 0, "2");
        setIdAttrib(7, 1, "0");
        setIdAttrib(8, 2, "1");
        doPerms("All distinct because values different", "");
    }
    
    public void testAddValidatorToProvider() {
        DSVP.clearContents();
        UniqueAssetValidator.addValidatorToProvider(hasDependents, DSVP);
        DSVP.checkContents(new String[] {hasDependents.getName()},
                           new Class[] {UniqueAssetValidator.class});
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

 02-Dec-04	6354/1	adrianj	VBM:2004112605 Refactor XML validation error reporting

 10-Sep-04	5488/1	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 10-Aug-04	5130/1	doug	VBM:2004080310 Added support for null device assets to GUI

 05-Aug-04	5081/3	geoff	VBM:2004080306 Implement Null Assets: JDBC Accessor (make it simpler)

 04-Aug-04	5081/1	geoff	VBM:2004080306 Implement Null Assets: JDBC Accessor

 10-Sep-04	5432/4	allan	VBM:2004081803 Fix TAC error messages and validate blank list enrties

 08-Sep-04	5432/2	allan	VBM:2004081803 Validation for range min and max values

 09-Jul-04	4841/1	adrianj	VBM:2004010802 Removal of ODOMValidatable infrastructure

 01-Feb-04	2821/1	mat	VBM:2004012701 Change tests and generate scripts for Projects

 08-Jan-04	2447/2	philws	VBM:2004010609 Fix UniqueAssetValidator and repackage it

 07-Jan-04	2426/1	richardc	VBM:2004010607 Refactored registration of UniqueAssetValidator

 06-Jan-04	2323/1	doug	VBM:2003120701 Added better validation error messages

 04-Jan-04	2364/1	doug	VBM:2004010401 Fixed problem with ComboViewer set/getValue()

 31-Dec-03	2306/4	richardc	VBM:2003121723 Added UniqueAssetValidator and applied to AssetsSection

 ===========================================================================
*/
