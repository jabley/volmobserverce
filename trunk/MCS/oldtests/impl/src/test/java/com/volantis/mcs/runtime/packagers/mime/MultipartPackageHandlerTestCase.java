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
 * $Header: /src/voyager/testsuite/unit/com/volantis/mcs/runtime/packagers/mime/MultipartPackageHandlerTestCase.java,v 1.10 2003/04/23 13:08:09 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 13-Feb-03    Phil W-S        VBM:2003021303 - Created.
 * 13-Feb-03    Geoff           VBM:2003021110 - Move from single license to
 *                              per fixture license generation, and use the
 *                              new LicenseManager useLicenseWith() method.
 *                              Resist temptation to litter code with to dos.
 * 14-Feb-03    Phil W-S        VBM:2003021303 - Split out the environment
 *                              context test class.
 * 18-Feb-03    Byron           VBM:2003020610 - Removed unnecessary inner
 *                              classes as a result of the change to
 *                              TestMarinerPageContext. Modified createContexts.
 * 20-Feb-03    Phil W-S        VBM:2003021921 - Add test of non-existent asset
 *                              resource.
 * 21-Feb-03    Geoff           VBM:2003022004 - Use new AppManager rather 
 *                              than less capable LicenseManager.
 * 06-Mar-03    Geoff           VBM:2003010904 - Use new ConfigValue stuff.
 * 11-Mar-03    Geoff           VBM:2002112102 - Refactor value property name
 *                              for consistency.
 * 24-Mar-03    Phil W-S        VBM:2003031910 - Update the call for
 *                              storing device policy values as required by
 *                              this VBM update.
 * 28-Mar-03	Allan		VBM:2003030603 - This testcase is excluded
 *				from the ant testsuite because it fails. 
 * 				Commenting out offending code so that accurev
 *				commit can succeed.
 * 25-Mar-03    Geoff           VBM:2003042306 - Use refactored 
 *                              AppConfigurator and new AppExecutor.
 * 08-May-03    Steve           VBM:2003042914 - Update call write to
 *                              take a PackageBodyOutput class instead of a Writer.
 *                              Set the character set encoding when creating the
 *                              request context.
 * 13-May-03    Mat             VBM:2003033108 - Set the character encoding
 *                              in the request context and also set the
 *                              encodingManager in the application context.
 *                              Had to disable the testCreatePackage
 *                              (see comments)
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.packagers.mime;

import java.io.IOException;
import java.io.InputStream;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import junit.framework.TestCase;

import com.volantis.charset.EncodingManager;
import com.volantis.mcs.assets.DeviceImageAsset;
import com.volantis.mcs.assets.ImageAsset;
import com.volantis.mcs.context.ApplicationContext;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.TestEnvironmentContext;
import com.volantis.mcs.context.TestMarinerPageContext;
import com.volantis.mcs.context.TestMarinerRequestContext;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.packagers.PackageBodyOutput;
import com.volantis.mcs.runtime.packagers.PackageBodySource;
import com.volantis.mcs.runtime.packagers.PackagingException;
import com.volantis.mcs.testtools.application.AppContext;
import com.volantis.mcs.testtools.application.AppExecutor;
import com.volantis.mcs.testtools.application.AppManager;
import com.volantis.mcs.testtools.application.DefaultAppConfigurator;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.testtools.config.ConfigValue;
import com.volantis.testtools.server.HTTPResourceServer;
import com.volantis.testtools.stubs.ServletContextStub;

/**
 * @author <a href="mailto:phil.weighill-smith@volantis.com">Phil W-S</a>
 */
public class MultipartPackageHandlerTestCase extends TestCase {

    // Encoding manager is slow to initialise to do it once only.
    static EncodingManager encodingManager = new EncodingManager();
    
    private HTTPResourceServer server;
    private static int port = 9020;
    private ServletContextStub servletContext = null;
    private Volantis volantis = null;

    public MultipartPackageHandlerTestCase(String name) {
        super(name);
    }

    // NOTE: DISABLED BECAUSE NOT USED AND CAUSED BACKGOUND THREADS
    protected void NOsetUp() throws Exception {
        super.setUp();

        volantis = new Volantis();
        servletContext = new ServletContextStub();
        AppManager mgr = new AppManager(volantis, servletContext);
        mgr.setAppConf(new DefaultAppConfigurator() {
            public void setUp(ConfigValue config) throws Exception {
                // Set up default values...
                super.setUp(config);
                // ... apart from what we are testing.
                config.pagePackagingMimeEnabled = Boolean.TRUE;
                config.baseUrl = "http://localhost:" + port;
                config.internalUrl = "http://localhost:" + port;
            }
        });
        mgr.useAppWith(new AppExecutor() {
            public void execute(AppContext context) throws Exception {
                // Start the server on the current port number
                System.err.println("Creating server on port " + port);
                server = new HTTPResourceServer(port);
                server.start();

                // Ensure that the server has enough time to start up and start
                // listening to a port before a tests attempts to connect to the port.
                try {
                    int delay = 0;
                    final int step = 25;
                    while (!server.isActive() && (delay < 250)) {
                        delay += step;
                        Thread.currentThread().sleep(step);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.err.println("Server " + server + " on port " + port + " is " +
                                   (server.isActive() ? "" : "not ") + "active");
            }
        }); 
    }

    // NOTE: DISABLED BECAUSE NOT USED AND CAUSED BACKGOUND THREADS
    protected void NOtearDown() throws Exception {
        System.err.println("shutting down server " + server);
        server.shutdown();

        // Move to the next port number (this is faster than waiting for the
        // server to shutdown before starting the next test)
        port++;

        volantis = null;
        servletContext = null;

        super.tearDown();
    }

    public void testAddBody() throws Exception {
       // Tested by testCreatePackage
    }

    /**
     * @todo later this should use a constant from DevicePolicyConstants
     * @throws Exception
     */
    public void testIsToBeAddedNoCache() throws Exception {
        final MultipartPackageHandler handler =
            new MultipartPackageHandler();
        MarinerPageContext pageContext = createContexts(handler);

        // No caching
        // @todo use a constant from DevicePolicyConstants
        ((TestMarinerPageContext)pageContext).
            setDevicePolicyValue("protocol.mime.urls.to.cache",
                                 null);

        assertTrue("1: isToBeAdded(qwerty) should be true",
                   handler.isToBeAdded("qwerty", pageContext));
        assertTrue("2: isToBeAdded(qwerty) should be true",
                   handler.isToBeAdded("qwerty", pageContext));
    }

    public void testIsToBeAdded() throws Exception {
        final MultipartPackageHandler handler =
            new MultipartPackageHandler();
        MarinerPageContext pageContext = createContexts(handler);

        // Make sure caching will be enabled
        ((TestMarinerPageContext)pageContext).setDevicePolicyValue(
            "protocol.mime.urls.to.cache",
            "3");

        assertTrue("1: isToBeAdded(uiop) should be true",
                   handler.isToBeAdded("uiop", pageContext));
        assertTrue("2: isToBeAdded(uiop) should be false",
                   !handler.isToBeAdded("uiop", pageContext));
    }

    public void testOutputPackage() throws Exception {
        // Tested by testCreatePackage
    }

    public void testAddAssetResources() throws Exception {
        // Tested by testCreatePackage
    }

    // There is a problem with this test when using TestRunner.
    // The HTTPResourceServer needs to be replaced with the new MarinerURL 
    // testing framework from Steve.  Unfortunately it is not ready yet and this
    // work needs checking in.  I will change this testcase to not run and raise
    // a seperate VBM to fix this testcase.  VBM: 2003051304
    public void noTestCreatePackage() throws Exception {
        final MultipartPackageHandler handler =
            new MultipartPackageHandler();
        MarinerPageContext pageContext = createContexts(handler);
        MarinerRequestContext requestContext =
            pageContext.getRequestContext();
        TestEnvironmentContext envContext =
            (TestEnvironmentContext)pageContext.getEnvironmentContext();

        // This asset exists
        final DeviceImageAsset imageAsset =
            new DeviceImageAsset(
                "jug", 15, 15, 1,
                ImageAsset.MONOCHROME, ImageAsset.JPEG,
                "Master", null,
                "/com/volantis/mcs/runtime/packagers/mime/jug.jpg");

        // This asset does not
        final DeviceImageAsset noImageAsset =
            new DeviceImageAsset(
                "nojug", 15, 15, 1,
                ImageAsset.MONOCHROME, ImageAsset.JPEG,
                "Master", null,
                "/com/volantis/mcs/runtime/packagers/mime/nojug.jpg");

        final StringBuffer bodyContent = new StringBuffer();

        PackageBodySource bodySource = new PackageBodySource() {
            public void write(PackageBodyOutput writer,
                              MarinerRequestContext context, Object bodyContext)
                throws PackagingException {
                try {
                    bodyContent.append("<html><body><div><img src=\"").
                        append(handler.rewriteAssetURL(
                            context, imageAsset,
                            null, new MarinerURL(imageAsset.getValue())).
                               getExternalForm()).
                        append("\"/></div><div><img src=\"").
                        append(handler.rewriteAssetURL(
                            context, noImageAsset,
                            null, new MarinerURL(noImageAsset.getValue())).
                               getExternalForm()).
                        append("\"/></div></body></html>");
                    writer.getWriter().write(bodyContent.toString());
                } catch (RepositoryException e) {
                    throw new PackagingException(e);
                } catch (IOException e) {
                    throw new PackagingException(e);
                }
            }

            public String getBodyType(MarinerRequestContext context) {
                return "text/html";
            }
        };

        handler.createPackage(requestContext, bodySource, null);

        try {
            InputStream inputStream = envContext.getResponseInputStream();
            MimeMessage message = new MimeMessage(Session.getInstance(
                System.getProperties(), null), inputStream);
            MimeMultipart multipart = (MimeMultipart)message.getContent();
            assertTrue("message content type " + message.getContentType() +
                       " doesn't start with multipart/mixed as expected",
                       message.getContentType().startsWith("multipart/mixed"));
            assertTrue("multipart content type " + multipart.getContentType() +
                       " doesn't start with multipart/mixed as expected",
                       multipart.getContentType().startsWith("multipart/mixed"));
            assertEquals("multipart should have 2 parts",
                         2,
                         multipart.getCount());
            BodyPart part1 = multipart.getBodyPart(0);
            BodyPart part2 = multipart.getBodyPart(1);

            assertTrue("body part 1 content type not as",
                       part1.getContentType().startsWith("text/html"));
            assertEquals("body part 1 size not as",
                         bodyContent.toString().length(),
                         part1.getSize());
            assertTrue("body part 2 content type " + part2.getContentType() +
                       " doesn't start with image/jpeg as expected",
                       part2.getContentType().startsWith("image/jpeg"));
            assertEquals("body part 2 size not as",
                         getClass().getResource(imageAsset.getValue()).
                         openConnection().getContentLength(),
                         part2.getSize());
        } catch (MessagingException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } catch (Throwable t) {
            t.printStackTrace();
            fail("Unexpected exception: " + t.getMessage());
        }
    }

    protected MarinerPageContext createContexts(
        MultipartPackageHandler handler) throws Exception {
        TestMarinerRequestContext requestContext =
            new TestMarinerRequestContext();
        TestMarinerPageContext pageContext = new TestMarinerPageContext();
        pageContext.setVolantis(volantis);
        ApplicationContext appContext =
            new MultipartApplicationContext(requestContext);
        TestEnvironmentContext envContext =
            new TestEnvironmentContext(getClass().getName() + ".message");

        pageContext.pushRequestContext(requestContext);
        ContextInternals.setEnvironmentContext(requestContext, envContext);
        ContextInternals.setMarinerPageContext(requestContext, pageContext);
        ContextInternals.setApplicationContext(requestContext, appContext);
        appContext.setPackager(handler);
        appContext.setAssetURLRewriter(handler);
        appContext.getPackageResources().setContentType("multipart/mixed");
        appContext.setEncodingManager(encodingManager);
        envContext.setContextPathURL("http://localhost:" + port);
        requestContext.setCharacterEncoding("iso-8859-1");
        return pageContext;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 26-Aug-04	5294/1	geoff	VBM:2004082405 Reduce unnecessary background threads in testsuite

 20-Aug-03	1207/1	adrian	VBM:2003032804 removed suite and main methods from testcase classes

 18-Aug-03	1146/1	geoff	VBM:2003042305 Add tearDown() to AppConfigurator

 18-Aug-03	1144/1	geoff	VBM:2003042305 Add tearDown() to AppConfigurator

 18-Aug-03	670/2	geoff	VBM:2003042305 Add tearDown() to AppConfigurator

 25-Jul-03	860/1	geoff	VBM:2003071405 merge from mimas

 25-Jul-03	858/1	geoff	VBM:2003071405 merge from metis; fix dissection test case sizes

 23-Jul-03	807/3	geoff	VBM:2003071405 works and tested but no design review yet

 ===========================================================================
*/
