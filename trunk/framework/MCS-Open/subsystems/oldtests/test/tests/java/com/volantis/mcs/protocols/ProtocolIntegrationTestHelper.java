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
 * $Header: /src/voyager/testsuite/integration/com/volantis/mcs/protocols/ProtocolIntegrationTestHelper.java,v 1.6 2003/04/30 08:35:40 byron Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2002. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 03-Apr-03    Allan           VBM:2003040303 - Created. Test helper for
 *                              protocol integration tests.
 * 17-Apr-03    Geoff           VBM:2003041505 - Commented out System.out
 *                              calls which clutter the JUnit console output.
 * 17-Apr-03    Byron           VBM:2003032608 - Modified setupProtocol and
 *                              doTest to setup the DOM test environment.
 * 17-Apr-03    Byron           VBM:2003040302 - Added private doTheTest and
 *                              public doTestWithoutUsingDOMComparisonoverloaded
 *                              methods. Modified setupProtocol, doTest and
 *                              added convertDOMToString as a result of
 *                              refactoring.
 * 23-Apr-03    Allan           VBM:2003042302 - Modified convertDOMToString() 
 *                              to use provideDOMNormalizedString. 
 * 29-Apr-03    Byron           VBM:2003042812 - Modified setupProtocol and
 *                              doTheTest.
 * 09-May-03    Steve           VBM:2003042914 - Modified doTheTest to use
 *                              a PackageBodyOutput class to write to the
 *                              protocol. The protocol now writes to an
 *                              ByteArrayOutputStream instead of a StringWriter
 *                              which will let us use this for binary output
 *                              too. 
 * 03-Jun-03    Allan           VBM:2003060301 - ObjectHelper moved to 
 *                              Synergetics. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols;

import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.devices.InternalDevice;
import com.volantis.mcs.devices.InternalDeviceTestHelper;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.protocols.layouts.FormInstance;
import com.volantis.mcs.protocols.layouts.RegionInstance;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.runtime.configuration.ProtocolsConfiguration;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayout;
import com.volantis.mcs.runtime.layouts.RuntimeLayoutAdapter;
import com.volantis.mcs.runtime.packagers.AbstractPackageBodyOutput;
import com.volantis.mcs.runtime.styling.StylingFunctions;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.styling.Styles;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.compiler.CompilerConfiguration;
import com.volantis.styling.compiler.StyleSheetCompiler;
import com.volantis.styling.compiler.StyleSheetSource;
import com.volantis.styling.sheet.CompiledStyleSheet;
import com.volantis.synergetics.ObjectHelper;
import com.volantis.testtools.MethodInvoker;
import com.volantis.testtools.mock.ExpectationBuilder;
import junit.framework.Assert;

import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Map;

public class ProtocolIntegrationTestHelper {

    /**
     * Sets up a protocol with all the objects needed to enabled the public
     * write methods called upon it to succeed. This method will require
     * further work and tidying for more complex protocol methods and possibly
     * for DOMProtocols.
     *
     * @param expectations
     * @param protocol The protocol to set up.
     */
    private static void setupProtocol(
            ExpectationBuilder expectations, VolantisProtocol protocol)
            throws Exception {
        Volantis volantis = new Volantis();
        TestMarinerPageContext pageContext = new TestMarinerPageContext();
        pageContext.setVolantis(volantis);
        // Initialise the device.
        pageContext.setDeviceName("Unknown Device");
        InternalDevice device = InternalDeviceTestHelper.createTestDevice();
        pageContext.setDevice(device);

        pageContext.setProtocol(protocol);
        // Set the protocols configuration
        ProtocolsConfiguration config = new ProtocolsConfiguration();
        config.setWmlPreferredOutputFormat("wml");
        volantis.setProtocolsConfiguration(config);


        MarinerRequestContext requestContext =
                ProtocolTestAbstract.initialiseMarinerRequestContext(
                        expectations);
        pageContext.pushRequestContext(requestContext);

        protocol.setMarinerPageContext(pageContext);
        protocol.initialise();
        protocol.initialiseCanvas();
        protocol.initialisePageHead();
        protocol.setWriteHead(false);

        StylingFactory stylingFactory = StylingFactory.getDefaultInstance();
        CompilerConfiguration configuration =
                stylingFactory.createCompilerConfiguration();
        configuration.setSource(StyleSheetSource.THEME);
        configuration.addFunctionResolver(StylingFunctions.getResolver());
        StyleSheetCompiler compiler = stylingFactory.createStyleSheetCompiler(
                configuration);
        CompiledStyleSheet compiledStyleSheet = compiler.compileStyleSheet(null);

        DeviceLayoutContext deviceLayoutContext =
                new TestDeviceLayoutContext(pageContext);
        deviceLayoutContext.setMarinerPageContext(pageContext);
        RuntimeDeviceLayout runtimeDeviceLayout = new RuntimeLayoutAdapter(
                "layoutName", new CanvasLayout(), compiledStyleSheet, null);
        deviceLayoutContext.setDeviceLayout(runtimeDeviceLayout);
        deviceLayoutContext.initialise ();

        pageContext.pushDeviceLayoutContext(deviceLayoutContext);
        MarinerURL requestURL = new MarinerURL();
        pageContext.setRequestURL(requestURL);

        // Need to setup a region instance for testing inclusions
        RegionInstance regionInstance =
                new RegionInstance(new NDimensionalIndex(new int[1]));
        regionInstance.setDeviceLayoutContext(deviceLayoutContext);

        pageContext.pushContainerInstance(regionInstance);
    }

    /**
     * Run the test.
     */
    public static void doTestWithoutUsingDOMComparison(
            ExpectationBuilder expectations,
            VolantisProtocol protocol,
            MethodInvoker invoker,
            String expectedResult)
            throws Exception {
        doTheTest(expectations, protocol, invoker, expectedResult, false);
    }

    /**
     * Run the test.
     */
    public static void doTest(
            ExpectationBuilder expectations, VolantisProtocol protocol,
            MethodInvoker invoker,
            String expectedResult)
            throws Exception {
        doTheTest(expectations, protocol, invoker, expectedResult, true);
    }

    /**
     * Run the test. The test is performed against the given procotol. The
     * invoke invoke the actual procotol method under test. The output that is
     * generated from the protocol method should be - when normalised via
     * DOMUtilities - the same as the expectedResult when it too is normalised
     * in the same way.
     *
     * @param expectations
     * @param  protocol            the current protocol
     * @param  invoker             the invoker that will invoke the test method
     * @param  expectedResult      the expected result
     * @param  forceDOMComparison if true the actual andvoic expected dom strings
     */
    private static void doTheTest(
            ExpectationBuilder expectations, VolantisProtocol protocol,
            MethodInvoker invoker,
            String expectedResult,
            boolean forceDOMComparison)
            throws Exception {
        setupProtocol(expectations, protocol);

        // Open the page before the method is invoked ensures that an
        // output buffer is placed on the stack of buffers.
        CanvasAttributes canvasAttributes = new CanvasAttributes();
        protocol.openCanvasPage(canvasAttributes);

        final TestMarinerPageContext pageContext =
            (TestMarinerPageContext)protocol.getMarinerPageContext();
        FormInstance formInstance = new FormInstance(NDimensionalIndex.ZERO_DIMENSIONS) {
            public OutputBuffer getContentBuffer(boolean create) {
                return pageContext.getCurrentOutputBuffer();
            }
        };
        formInstance.setDeviceLayoutContext(pageContext.getDeviceLayoutContext());
        pageContext.setFormatInstance(formInstance);

        invoker.invoke();

        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        final OutputStreamWriter writer = new OutputStreamWriter( stream );
        
        protocol.write(
            new AbstractPackageBodyOutput() {
                public Writer getRealWriter() {
                    return writer;
                }
                public OutputStream getRealOutputStream() {
                    return stream;
                }
                
            }, null, canvasAttributes);
        
        writer.flush();
        
        String actualResult = new String( stream.toByteArray() );

        // Wrap the actual and expected in a dummyroot tag
        actualResult = "<dummyroot>" + actualResult + "</dummyroot>";
        expectedResult = "<dummyroot>" + expectedResult + "</dummyroot>";

        if (forceDOMComparison) {
            String msg =  "Could not parse DOM ";
            expectedResult = convertDOMToString(protocol,
                                                msg + "(expected)",
                                                expectedResult);
            actualResult = convertDOMToString(protocol,
                                              msg + "(actual)",
                                              actualResult);
        }
        Assert.assertEquals(expectedResult, actualResult);
    }

    /**
     * Added a result of refactoring
     *
     * @param  protocol   the protocol
     * @param  msg        the msg to display if the conversion fails (cannot be
     *                    parsed, or other)
     * @param  domToParse the dom string to parse.
     * @return            the resulting string after the conversion.
     */
    static private String convertDOMToString(VolantisProtocol protocol,
                                             String msg,
                                             String domToParse)
            throws Exception{
        String result = null;
        try {
            result = DOMUtilities.provideDOMNormalizedString(
                    domToParse, protocol.getCharacterEncoder());
        } catch (Exception e) {
            Assert.fail(msg + domToParse);
        }
        return result;
    }

    /**
     * Set the properties on a VolantisAttribute object.
     *
     * @param attributes The VolantisAttribute object.
     * @param defaultValues the map containing the default values. The key is
     * the name of the property and the value is the string value to use.
     */
    private static void setProperties(MCSAttributes attributes, Map defaultValues)
            throws IllegalAccessException,
            InvocationTargetException {
        PropertyDescriptor props [] =
                ObjectHelper.getPropertyDescriptors(attributes.getClass());

        for(int i=0; i<props.length; i++) {
            PropertyDescriptor prop = props[i];
            if(prop.getPropertyType().equals(String.class)) {
                String value = (String) defaultValues.get(prop.getName());
                if (value == null) {
                    value = prop.getName();
                }
                ObjectHelper.invokeWriteMethod(prop, attributes, value);
            }
        }
    }

    /**
     * This method returns an instance of the given class of VolantisAttribute
     * with each attribute property set. String properties whose values are
     * null are set to the name of the property. Primitives and Objects other
     * than String are ignored. (NOTE: if this factory method needs to provide
     * values for non-String Objects then mechanisms for creating defaults of
     * these objects should be provided i.e. don't override this method or fill
     * in the details later - unless you want specific behaviour)
     *
     * This method delegates the call to the {@link #provideAttributes(Class,
     * com.volantis.styling.Styles, java.util.Map)} stating that no style sheet
     * to be used and specifies no default values.
     *
     * @param  attributesClass the attribute class used for setting the
     *                         properties via reflection
     * @return                 an instance of the given class of
     *                         VolantisAttribute with each attribute property
     *                         set.
     */
    public static MCSAttributes
            provideAttributes(Class attributesClass)
            throws InstantiationException,
            IllegalAccessException,
            InvocationTargetException {

        return provideAttributes(attributesClass, null, Collections.EMPTY_MAP);
    }

    /**
     * This method returns an instance of the given class of VolantisAttribute
     * with each attribute property set. String properties whose values are
     * null are set to the name of the property or to the value stored in the
     * default values map. The keys in the default values map are the name of
     * the properties and the values are the String values to be used.
     * Primitives and Objects other than String are ignored.
     *
     * (NOTE: if this factory method needs to provide values for non-String
     * Objects then mechanisms for creating defaults of these objects should be
     * provided i.e. don't override this method or fill in the details later -
     * unless you want specific behaviour)
     *
     * If specified, styles are set to the created attribute before setting the
     * property values.
     *
     * With this version of provideAttributes it is now possible to create
     * attributes that store the property values in styles.
     *
     * @param  attributesClass the attribute class used for setting the
     *                         properties via reflection
     * @param styles           the styles to set, may be <code>null</code>
     * @param defaultValues    the default values map to be used for String
     *                         properties. May be empty, but may not be
     *                         <code>null</code>.
     * @return                 an instance of the given class of
     *                         VolantisAttribute with each attribute property
     *                         set.
     */
    public static MCSAttributes provideAttributes(
            final Class attributesClass,
            final Styles styles,
            final Map defaultValues)
        throws InstantiationException,
            IllegalAccessException,
            InvocationTargetException {

        MCSAttributes attributes = (MCSAttributes) attributesClass.newInstance();

        if (styles != null) {
            attributes.setStyles(styles);
        }
        setProperties(attributes, defaultValues);
        return attributes;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05	10641/1	geoff	VBM:2005113024 Pagination page rendering issues

 06-Dec-05	10621/1	geoff	VBM:2005113024 Pagination page rendering issues

 29-Nov-05	10504/1	ianw	VBM:2005112312 Fixed pseudoElements in GUI and JIBX

 29-Nov-05	10484/1	ianw	VBM:2005112312 Fixed pseudoElements in GUI and JIBX

 28-Nov-05	10467/1	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 28-Nov-05	10394/1	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 12-Sep-05	9372/1	ianw	VBM:2005082221 Allow only one instance of MarinerPageContext for a page

 19-Aug-05	9245/3	gkoch	VBM:2005081006 vbm2005081006 storing property values in styles

 18-Aug-05	9007/3	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 15-Jul-05	9073/1	pduffin	VBM:2005071420 Created runtime device layout that only exposes the parts of DeviceLayout that are needed at runtime.

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 02-Jun-05	8005/4	pduffin	VBM:2005050404 Moved dom to its own subsystem

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 11-May-05	8123/2	ianw	VBM:2005050906 Refactored DeviceTheme to seperat out CSSEmulator

 05-Apr-05	7513/1	geoff	VBM:2003100606 DOMOutputBuffer allows creation of text which renders incorrectly in WML

 28-Feb-05	7149/1	geoff	VBM:2005011306 Theme overlay is not working for Remote Repositories.

 28-Feb-05	7127/1	geoff	VBM:2005011306 Theme overlay is not working for Remote Repositories.

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 05-Nov-04	6112/2	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 02-Nov-04	5882/2	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 29-Jun-04	4713/2	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 29-Apr-04	4098/1	mat	VBM:2004042809 Made pooling of objects in the DOMProtocol configurable

 05-Dec-03	2075/1	mat	VBM:2003120106 Rename Device and add a public Device Interface

 ===========================================================================
*/
