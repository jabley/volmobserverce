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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/context/TestMarinerPageContext.java,v 1.17 2003/04/28 11:50:37 chrisw Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 20-Jan-03    Geoff           VBM:2003012101 - Created.
 * 07-Feb-03    Chris W         VBM:2003020609 - Added implementation of
 *                              addPaneMapping() and getPane().
 * 13-Feb-03    Phil W-S        VBM:2003021303 - Add environmentContext
 *                              property and getSessionContext method.
 * 18-Feb-03    Byron           VBM:2003020610 - Added setters for new member
 *                              variables:  pageGenerationCache, volantis,
 *                              rootPageURL, fragmentationState. Also added
 *                              supporting methods: getSessionURLRewriter,
 *                              getVolantisBean, getPageGenerationCache,
 *                              getRootPageURL, getFragmentationState.
 * 03-Mar-03    Byron           VBM:2003022813 - Modified getTextFromReference to
 *                              return non-empty value.
 * 14-Mar-03    Doug            VBM:2003030409 - Added the requestedURL member
 *                              and corresponding getter & setter methods.
 * 21-Mar-03    Sumit           VBM:2003022828 - Added setTextFromObject to
 *                              return a specific string. Mod to getTe..Object
 *                              to return this class member if not null
 * 24-Mar-03    Phil W-S        VBM:2003031910 - Update the mechanism for
 *                              device policy values to use a map of key/value
 *                              pairs.
 * 31-Mar-03    Sumit           VBM:2003032714 - Added set/getCurrentElement()
 * 22-Apr-03    Allan           VBM:2003041506 - Added set/getElementStack()
 *                              and initialize the elementStack property. Added
 *                              popElement() and pushElement().
 * 22-Apr-03    Byron           VBM:2003032608 - Updated to port some changes
 *                              made in METIS. Without these changes the test
 *                              cases will not work. Also re-ordered some
 *                              methods to be more similar to the METIS version
 *                              (ease of cvs diff in future).
 * 22-Apr-03    Geoff           VBM:2003040305 - Add support for emulated
 *                              retrieveBestScriptAsset and simple computeURL
 *                              which doesn't require an asset url rewriter.
 * 25-Apr-03    Byron           VBM:2003040302 - Modified releaseRSB method.
 * 25-Apr-03    Chris W         VBM:2003030404 - Added getTagEmphasisEmulation
 *                              and setTagEmphasisEmulation so we can test the
 *                              generation of emulated emphasis tags in wml
 *                              protocol openCard methods.
 * 28-May-03    Byron           VBM:2003051904 - Added generateWMLActionID
 *                              method.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.context;

import com.volantis.charset.Encoding;
import com.volantis.charset.NoEncoding;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.integration.URLRewriter;
import com.volantis.mcs.integration.iapi.IAPIElement;
import com.volantis.mcs.layouts.Form;
import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.FormatNamespace;
import com.volantis.mcs.layouts.FormatScope;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.papi.PAPIElement;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.TestDeviceLayoutContext;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.forms.FormDataManager;
import com.volantis.mcs.protocols.forms.FragmentableFormData;
import com.volantis.mcs.protocols.layouts.FormatInstance;
import com.volantis.mcs.protocols.menu.builder.MenuModelBuilder;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.FragmentationState;
import com.volantis.mcs.runtime.PageGenerationCache;
import com.volantis.mcs.runtime.RequestHeaders;
import com.volantis.mcs.runtime.RuntimePageURLRewriter;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayout;
import com.volantis.mcs.runtime.policies.PolicyReferenceResolver;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.mcs.utilities.ReusableStringBuffer;
import junitx.util.PrivateAccessor;
import org.apache.log4j.Category;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * A MarinerPageContext which is "designed" for use with test cases.
 * <p>
 * Note that the original "design" for this was simply to collect together all
 * the existing usages demonstrated in the various inner classes which had
 * previously extended MarinerPageContext before the existance of this class,
 * so the code is not necessarily the best at the moment. However, with
 * continued use and more refactoring, this should evolve into something which
 * is useful for all test cases.
 * <p>
 * Make sure you run ALL the test cases if you modify this class!
 */
public class TestMarinerPageContext extends MarinerPageContext {

    /**
     * Flag stating if we wish to use tag emphasis emulation. This is used to
     * test in WMLRootTestCase.testOpenLayout.
     */
    private boolean enableTagEmphasisEmulation;

    /**
     * The log4j object to log to.
     */
    private static Category logger = Category.getInstance
            ("com.volantis.mcs.context.TestMarinerPageContext");

    private VolantisProtocol protocol;

    private MarinerRequestContext requestContext;

    private RuntimeDeviceLayout deviceLayout;

    private FormatInstance formatInstance;

    private InternalDevice device;

    private String deviceName;

    private String shortInclusionPath;

    private Pane currentPane;

    private Map devicePolicyValues = new HashMap();

    private Map booleanDevicePolicyValues = new HashMap();

    private boolean formFragmentResetState;

    private FormatScope formatScope = new FormatScope();

    private PageGenerationCache pageGenerationCache;

    private Volantis volantis;

    private MarinerURL rootPageURL;

    private FragmentationState fragmentationState;

    private PolicyReferenceResolver policyReferenceResolver;

    /**
     * Stack of Element objects
     */
    private Stack elementStack = new Stack();

    /**
     * Stack IAPIElement objects.
     */
    private Stack iapiElementStack = new Stack();

    /**
     * Stack MCSIElement objects.
     */
    private Stack mcsiElementStack = new Stack();


    /**
     * Stack of output buffers.
     */
    private Stack outputBufferStack = new Stack();

    /**
     * The url that the getRequestURL returns
     */
    private MarinerURL requestedURL;

    /**
     * The current element we want returned from a getCurrentElement call
     */
    private PAPIElement currentElement;

    private String chartImageBase;
    private String url;
    private MenuModelBuilder menuModelBuilder;
    private Form form;
    private FormDataManager formDataManager;

    public TestMarinerPageContext() {
        volantis = new com.volantis.mcs.runtime.Volantis();
        try {
            PrivateAccessor.setField(volantis, "pageURLRewriter",
                    new RuntimePageURLRewriter(null, null));
            PrivateAccessor.setField(volantis, "layoutURLRewriter",
                    new RuntimePageURLRewriter(null, null));            
            this.pushDeviceLayoutContext(new TestDeviceLayoutContext());
        } catch (Exception e) {
            throw new UndeclaredThrowableException(e);
        }
    }

    public ReusableStringBuffer allocateRSB() {
        return new ReusableStringBuffer();
    }

    public void releaseRSB(ReusableStringBuffer rsb) {
        rsb.setLength(0);
    }

    public VolantisProtocol getProtocol() {
        if (protocol != null) {
            return protocol;
        } else {
            return super.getProtocol();
        }
    }

    public void setProtocol(VolantisProtocol protocol) {
        this.protocol = protocol;
    }



    public URLRewriter getSessionURLRewriter() {
        return new URLRewriter() {
            public MarinerURL mapToExternalURL(MarinerRequestContext context,
                                               MarinerURL url) {
                return url;
            }

            public MarinerURL mapToMarinerURL(MarinerRequestContext context,
                                              MarinerURL url) {
                return url;
            }
        };
    }

    public com.volantis.mcs.runtime.Volantis getVolantisBean() {
        return volantis;
    }

    public void setVolantis(com.volantis.mcs.runtime.Volantis volantis) {
        this.volantis = volantis;
    }

    public RuntimeDeviceLayout getDeviceLayout() {
        return deviceLayout;
    }

    public void setDeviceLayout(RuntimeDeviceLayout deviceLayout) {
        this.deviceLayout = deviceLayout;
    }

    public void setFormatInstance(FormatInstance formatContext) {
        this.formatInstance = formatContext;
    }

    public FormatInstance getFormatInstance(Format format, NDimensionalIndex index) {
        return formatInstance;
    }

    public InternalDevice getDevice() {
        return device;
    }

    public void setDevice(InternalDevice device) {
        this.device = device;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String name) {
        deviceName = name;
    }

    public String getShortInclusionPath() {
        return shortInclusionPath;
    }

    public Pane getCurrentPane() {
        if (currentPane != null) {
            return currentPane;
        } else {
            return super.getCurrentPane();
        }
    }

    public void setCurrentPane(Pane currentPane) {
        this.currentPane = currentPane;
    }

    public PageGenerationCache getPageGenerationCache() {
        return pageGenerationCache;
    }

    public void setPageGenerationCache(PageGenerationCache pageGenerationCache) {
        this.pageGenerationCache = pageGenerationCache;
    }

    public MarinerURL getRootPageURL(boolean clone) {
        return rootPageURL;
    }

    public void setRootPageURL(MarinerURL rootPageURL) {
        this.rootPageURL = rootPageURL;
    }

    public FragmentationState getFragmentationState() {
        return fragmentationState;
    }

    public void setFragmentationState(FragmentationState state) {
        fragmentationState = state;
    }

    public void setCurrentOutputBuffer(OutputBuffer currentOutputBuffer) {
        pushOutputBuffer(currentOutputBuffer);
    }

    public String getDevicePolicyValue(String policyName) {
        return (String) devicePolicyValues.get(policyName);
    }

    public void setDevicePolicyValue(String policyName, String value) {
        devicePolicyValues.put(policyName, value);
    }

    public boolean getBooleanDevicePolicyValue(String policyName) {
        Boolean value = (Boolean) booleanDevicePolicyValues.get(policyName);
        if (value != null) {
            return value.booleanValue();
        } else {
            return false;
        }
    }

    public void setBooleanDevicePolicyValue(String policyName, boolean value) {
        booleanDevicePolicyValues.put(policyName, new Boolean(value));
    }

    public boolean getFormFragmentResetState() {
        return formFragmentResetState;
    }

    public void setFormFragmentResetState(boolean formFragmentResetState) {
        this.formFragmentResetState = formFragmentResetState;
    }

    // javadoc inherited from superclass
    public void pushElement(PAPIElement element) {
        elementStack.push(element);
    }

    // javadoc inherited from superclass
    public void popElement(PAPIElement expectedElement) {
        PAPIElement popped = (PAPIElement) elementStack.pop();
        if (null != expectedElement &&
                !expectedElement.equals(popped)) {
            throw new IllegalStateException("PAPIELEMENT STACK:"
                    + " Expected " + expectedElement
                    + " popped " + popped);
        }
    }

    /**
     * IAPI: Push the specified IAPIElement onto the top of the stack.
     * @param element The IAPIElement to push onto the stack.
     */
    public void pushIAPIElement(IAPIElement element) {
        iapiElementStack.push(element);
    }

    /**
     * IAPI: Pop the IAPIElement from the top of the stack.
     * @return The IAPIElement from the top of the stack
     */
    public IAPIElement popIAPIElement() {
        return (IAPIElement) iapiElementStack.pop();
    }

    /**
     * IAPI: Return the IAPIElement from the top of the stack without
     * removing it from the stack.
     * @return The IAPI element from the top of the stack
     */
    public IAPIElement peekIAPIElement() {
        return (IAPIElement) iapiElementStack.peek();
    }

    /**
     * MCSI: Push the specified PAPIElement onto the top of the stack.
     * @param element The PAPIElement to push onto the stack.
     */
    public void pushMCSIElement(PAPIElement element) {
        mcsiElementStack.push(element);
    }

    /**
     * MCSI: Pop the PAPIElement from the top of the stack.
     * @return The PAPIElement from the top of the stack
     */
    public PAPIElement popMCSIElement() {
        PAPIElement element = (PAPIElement) mcsiElementStack.pop();
        return element;
    }

    /**
     * MCSI: Return the MCSIElement from the top of the stack without
     * removing it from the stack.
     * @return The MCSI element from the top of the stack
     */
    public PAPIElement peekMCSIElement() {
        PAPIElement element = (PAPIElement) mcsiElementStack.peek();
        return element;
    }

    /**
     * Local implementation since parent formatScope instance and accessor
     * are private.
     */
    public Format getFormat(String name, FormatNamespace namespace) {
        return formatScope.retrieveFormat(name, namespace);
    }

    /**
     * Helper method that allows TestCases to add a pane that getPane will
     * return
     * @param pane the Pane to store
     */
    public void addPaneMapping(Pane pane) {
        formatScope.addFormat(pane);
    }

    /**
     * PAPI: Push the specified OutputBuffer onto the top of the stack.
     * @param outputBuffer The OutputBuffer to push onto the stack.
     */
    public void pushOutputBuffer(OutputBuffer outputBuffer) {
        outputBufferStack.push(outputBuffer);
        if (logger.isDebugEnabled()) {
            logger.debug("OUTPUT BUFFER STACK: Pushed " + outputBuffer);
        }
    }

    /**
     * PAPI: Pop the current outputBuffer from the top of the stack.
     * @param expectedOutputBuffer The OutputBuffer which is expected to be
     * popped. If this is not null and not equal to the outputBuffer on the
     * top of the stack then throw an IllegalStateException.
     * @throws IllegalStateException If the OutputBuffer on the top of the stack
     * does not match the expected outputBuffer if specified.
     */
    public void popOutputBuffer(OutputBuffer expectedOutputBuffer) {

        OutputBuffer outputBuffer = (OutputBuffer) outputBufferStack.pop();
        if (expectedOutputBuffer != null && expectedOutputBuffer != outputBuffer) {
            throw new IllegalStateException("OUTPUT BUFFER STACK:"
                    + " Expected " + expectedOutputBuffer
                    + " popped " + outputBuffer);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("OUTPUT BUFFER STACK: Popped " + outputBuffer);
        }
    }

    /**
     * PAPI: Get the current outputBuffer from the top of the stack.
     * @return The OutputBuffer which is on the top of the stack.
     */
    public OutputBuffer getCurrentOutputBuffer() {

        OutputBuffer currentOutputBuffer;
        OutputBuffer topOutputBuffer;

        if (outputBufferStack == null || outputBufferStack.isEmpty()) {
            topOutputBuffer = null;
            currentOutputBuffer = null;
        } else {
            topOutputBuffer = (OutputBuffer) outputBufferStack.peek();

            // The OutputBuffer on the stack could be a simple one, or a
            // compound one. If it is a compound one then we need to return
            // its current buffer.
            currentOutputBuffer = topOutputBuffer.getCurrentBuffer();
        }

        if (logger.isDebugEnabled()) {
            if (currentOutputBuffer == topOutputBuffer) {
                logger.debug("OUTPUT BUFFER STACK: Current " + currentOutputBuffer);
            } else {
                logger.debug("OUTPUT BUFFER STACK: Top " + topOutputBuffer
                        + " Current " + currentOutputBuffer);
            }
        }
        return currentOutputBuffer;
    }

    // Javadoc inherited form superclass
    public MarinerURL getRequestURL(boolean clone) {
        return requestedURL;
    }

    /**
     * Setter to allow TestCase to set the MarinerURL that the
     * getRequestURL method will return
     * @param url
     */
    public void setRequestURL(MarinerURL url) {
        requestedURL = url;
    }

    public void setChartImageBase(String chartImageBase) {
        this.chartImageBase = chartImageBase;
    }

    public String getChartImagesBase() {
        return chartImageBase;
    }

    // javadoc inherited from superclass
    public PAPIElement getCurrentElement() {
        if (currentElement != null) {
            return currentElement;
        }
        return (elementStack.isEmpty()) ? null :
                (PAPIElement) elementStack.peek();
    }

    public void setCurrentElement(PAPIElement element) {
        currentElement = element;
    }

    public String generateWMLActionID() {
        return "123";
    }

    // set charset to UTF8 for testing.
    // This avoids having to have an application context for the EncodingMgr
    // and also makes it faster.

    private Encoding charsetEncoding = new NoEncoding("UTF-8", 106);
    private String charsetName = "UTF-8";

    public void setCharsetName(String charsetName) {
        this.charsetName = charsetName;
        // NoEncodings are fast to create, BitSetEncodings are s.l.o.w.
        charsetEncoding = new NoEncoding(charsetName, Encoding.MIBENUM_NOT_CONFIGURED);
    }

    public String getCharsetName() {
        return charsetName;
    }

    public Encoding getCharsetEncoding() {
        return charsetEncoding;
    }

    public String getAbsolutePageBaseURL() {
        return url;
    }

    public void setAbsolutePageBaseURL(String url) {
        this.url = url;
    }

    /**
     * Get the menu builder (if there is one).
     * @return the menu builder (if there is one).
     */
    public MenuModelBuilder getMenuBuilder() {
        if (menuModelBuilder == null) {
            return super.getMenuBuilder();
        } else {
            return menuModelBuilder;
        }
    }

    /**
     * Set the menu model builder.
     * @param menuModelBuilder the menu model builder.
     */
    public void setMenuBuilder(MenuModelBuilder menuModelBuilder) {
        this.menuModelBuilder = menuModelBuilder;
    }

    /**
     * Initialise this test page.
     *
     * rest of javadoc inherited
     */
    public void initialisePage(
            Volantis volantisBean,
            MarinerRequestContext requestContext,
            MarinerRequestContext enclosingRequestContext,
            MarinerURL marinerRequestURL,
            RequestHeaders requestHeaders) {
        this.volantis = volantisBean;
        this.requestContext = requestContext;

        ApplicationContext appContext =
                ContextInternals.getApplicationContext(requestContext);

        try {
            PrivateAccessor.setField(this, "applicationContext", appContext);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e.getMessage());
        }

        EnvironmentContext environmentContext =
                requestContext.getEnvironmentContext();
        // If we don't have a session context device yet then get it from the
        // applicationContext.
        try {
            if (environmentContext.getSessionContext().getDevice() == null) {
                environmentContext.getSessionContext().
                        setDevice(appContext.getDevice());
            }
            // Make the device available to this test page context.
            this.setDevice(environmentContext.getSessionContext().getDevice());
        } catch (RepositoryException re) {
            throw new RuntimeException(re.getMessage());
        }
    }

    // javadoc inherited from superclass
    public Form getForm(String formName) {
        return form;
    }

    /**
     * Set the form to retrieve.
     */
    public void setForm(Form form) {
        this.form = form;
    }

    /**
     * Override to prevent it from being used.
     */ 
    public void updateFormFragmentationState(FragmentableFormData formData) {
    }

    // Javadoc inherited.
    public FormDataManager getFormDataManager() {
        if (formDataManager == null) {
            formDataManager = new FormDataManager();
        }
        return formDataManager;
    }

    /**
     * Add information about fragmented forms known about in this context. In
     * real operation fragmented form data queries would be delegated to the
     * {@link FormDataManager} in the session context.
     *
     * @param formDataManager
     */
    public void setFormDataManager(FormDataManager formDataManager) {
        this.formDataManager = formDataManager;
    }

    public void setPolicyReferenceResolver(
            PolicyReferenceResolver referenceResolver) {
        this.policyReferenceResolver = referenceResolver;
    }

    public PolicyReferenceResolver getPolicyReferenceResolver() {
        return policyReferenceResolver;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 28-Nov-05	10467/1	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 28-Nov-05	10394/1	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 03-Oct-05	9673/2	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 12-Sep-05	9372/1	ianw	VBM:2005082221 Allow only one instance of MarinerPageContext for a page

 22-Aug-05	9363/4	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9363/1	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9298/2	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 15-Jul-05	9073/1	pduffin	VBM:2005071420 Created runtime device layout that only exposes the parts of DeviceLayout that are needed at runtime.

 05-Jul-05	8813/1	pcameron	VBM:2005061608 Added aspect ratio parameter processing to XDIME-CP

 11-Feb-05	6931/1	geoff	VBM:2005020901 R821: Branding using Projects: Prerequisites: Fix remaining manual id creation

 09-Feb-05	6914/2	geoff	VBM:2005020707 R821: Branding using Projects: Prerequisites: Fix schema and related expressions

 25-Jan-05	6712/1	pduffin	VBM:2005011713 Committing work to handle selection method plugins. There was significant refactoring of a number of areas to allow sharing of classes and to ease testing.

 06-Jan-05	6391/1	adrianj	VBM:2004120207 Refactored DeviceLayoutRenderer into separate renderer classes

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 06-Dec-04	5800/4	ianw	VBM:2004090605 New Build system

 19-Nov-04	6253/1	claire	VBM:2004111704 mergevbm: Handle portal themes correctly and remove caching of themes and emulation in protocols

 19-Nov-04	6236/1	claire	VBM:2004111704 Handle portal themes correctly and remove caching of themes and emulation in protocols

 09-Nov-04	6027/6	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 08-Nov-04	6027/4	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 29-Oct-04	6027/1	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 28-Oct-04	5897/1	geoff	VBM:2004102104 chartimages generated within portlets need some form of unique identifier.

 05-Nov-04	6112/2	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 02-Nov-04	5882/1	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 07-Oct-04	5239/1	pcameron	VBM:2004072012 Fixed layout:getPaneInstance with a parameter and nested contexts

 13-Sep-04	5371/2	byron	VBM:2004083102 Title attribute on the <menuitem> element is being ignored

 08-Sep-04	5449/1	claire	VBM:2004090809 New Build Mechanism: Remove the use of utilities.UndeclaredThrowableException

 22-Jul-04	4713/11	geoff	VBM:2004061004 Support iterated Regions (fix merge conflicts)

 02-Jul-04	4713/8	geoff	VBM:2004061004 Support iterated Regions (review comments)

 29-Jun-04	4713/5	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 21-Jun-04	4713/1	geoff	VBM:2004061004 Support iterated Regions (commit prototype for safety)

 20-Jul-04	4874/2	byron	VBM:2004070601 Upgrade Davisor chart package to v4.2

 01-Jul-04	4778/1	allan	VBM:2004062912 Use the Volantis.pageURLRewriter to rewrite page urls

 28-Jun-04	4733/1	allan	VBM:2004062105 Convert Volantis to use the new PageURLRewriter

 14-Jun-04	4704/1	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 15-Apr-04	3884/1	claire	VBM:2004040712 Added AssetReferenceException

 26-Mar-04	3500/1	claire	VBM:2004031806 Initial implementation of abstract component image references

 06-Feb-04	2828/3	ianw	VBM:2004011922 corrected logging issues

 04-Feb-04	2828/1	ianw	VBM:2004011922 Added MCSI content handler

 05-Dec-03	2075/1	mat	VBM:2003120106 Rename Device and add a public Device Interface

 28-Jul-03	755/2	adrian	VBM:2003022801 fixed merge problems with iapi implementation

 18-Jul-03	812/1	adrian	VBM:2003071609 Added canvas and session level scopes for markup plugins

 16-Jul-03	757/1	adrian	VBM:2003070706 Added IAPI, MarkupPlugin and configuration.

 25-Jul-03	860/1	geoff	VBM:2003071405 merge from mimas

 04-Jul-03	337/1	chrisw	VBM:2003020609 implemented rework, added testcases

 ===========================================================================
*/
