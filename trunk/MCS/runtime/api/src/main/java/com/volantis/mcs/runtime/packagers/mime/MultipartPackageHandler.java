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
 * $Header: /src/voyager/com/volantis/mcs/runtime/packagers/mime/MultipartPackageHandler.java,v 1.6 2003/04/28 15:27:11 mat Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 13-Feb-03    Phil W-S        VBM:2003021303 - Created. Implements the
 *                              Packager and AssetURLRewriter required to
 *                              handle MIME multipart response packaging.
 * 19-Feb-03    Byron           VBM:2003021812 - Modified isToBeAdded and
 *                              addAssetResources to use full policy name.
 * 20-Feb-03    Phil W-S        VBM:2003021921 - Fix test in addAssetResources
 *                              to determine the existence of the asset
 *                              resource.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * 25-Apr-03    Mat             VBM:2003033108 - Changed to use the
 *                              CharsetEncodingWriter()
 * 02-May-03    Mat             VBM:2003033108 - Changed to pass the encoding
 *                              to the CharsetEncodingWriter()
 * 08-May-03    Steve           VBM:2003042914 - Update addBody call to write
 *                              to create a package body output instead of a
 *                              Writer.
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.packagers.mime;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;

import com.volantis.charset.CharsetEncodingWriter;
import com.volantis.charset.Encoding;
import com.volantis.mcs.assets.Asset;
import com.volantis.mcs.assets.AssetGroup;
import com.volantis.mcs.assets.LinkAsset;
import com.volantis.mcs.assets.ImageAsset;
import com.volantis.mcs.context.ApplicationContext;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerContextException;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.MarinerSessionContext;
import com.volantis.mcs.integration.AssetURLRewriter;
import com.volantis.mcs.repository.RepositoryException;
import com.volantis.mcs.runtime.packagers.AbstractPackageBodyOutput;
import com.volantis.mcs.runtime.packagers.PackageBodySource;
import com.volantis.mcs.runtime.packagers.PackageResources;
import com.volantis.mcs.runtime.packagers.Packager;
import com.volantis.mcs.runtime.packagers.PackagingException;
import com.volantis.mcs.runtime.packagers.PackagedURLEncoder;
import com.volantis.mcs.utilities.MarinerURL;

/**
 * This implementation of {@link Packager} provides packaging into a MIME
 * multi-part response, taking anticipated device caching of asset resources
 * into account.
 * <p>Note that this class also implements the {@link AssetURLRewriter} and is
 * expected to be registered as both the Packager and the AssetURLRewriter in
 * the ApplicationContent. This allows the handling of URL mapping and the
 * packaging using these URLs to be performed in one place.</p>
 *
 * @author <a href="mailto:phil.weighill-smith@volantis.com">Phil W-S</a>
 */
public class MultipartPackageHandler implements AssetURLRewriter, Packager,
        PackagedURLEncoder {
    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(MultipartPackageHandler.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
                LocalizationFactory.createExceptionLocalizer(MultipartPackageHandler.class);

    /**
     * The optional AssetURLRewriter that should be invoked against the
     * given asset URL before this class performs any URL rewriting.
     *
     * @supplierRole preRewriter
     * @supplierCardinality 0..1
     */
    protected AssetURLRewriter preRewriter = null;

    /**
     * Initializes the new instance using the given parameters. The preRewriter
     * is set null.
     */
    public MultipartPackageHandler() {
        this(null);
    }

    /**
     * Initializes the new instance using the given parameters.
     */
    public MultipartPackageHandler(AssetURLRewriter preRewriter) {
        this.preRewriter = preRewriter;
    }

    /**
     * This method is invoked whenever an Asset is to be processed during
     * page generation when the request indicates that MIME multi-part
     * packaging is supported and has been requested. The processing must
     * include:
     * <ol>
     *   <li>invoking the (optional) pre-rewriter to perform any initial URL
     * rewriting needed</li>
     *   <li>mapping of the re-written URL to a "package" URL, dependent on
     * the relevant device policy settings and the type of asset</li>
     *   <li>recording the mapping from original "plain" URL to "encoded" URL
     * for later use</li>
     * <ol>
     */
    public MarinerURL rewriteAssetURL(MarinerRequestContext requestContext,
                                      Asset asset,
                                      AssetGroup assetGroup,
                                      MarinerURL marinerURL)
        throws RepositoryException {
        MarinerURL rewritten = marinerURL;

        if (preRewriter != null) {
            rewritten = preRewriter.rewriteAssetURL(requestContext,
                                                    asset,
                                                    assetGroup,
                                                    marinerURL);
        }

        if (!(asset instanceof LinkAsset)) {
            String plain = rewritten.getExternalForm();
            String encoded = getEncodedURI(plain);
            ApplicationContext ac =
                ContextInternals.getApplicationContext(requestContext);
            PackageResources pr = ac.getPackageResources();

            if (pr != null) {
                boolean onClientSide = false;
                if (assetGroup != null) {
                    onClientSide = (assetGroup.getLocationType() ==
                            AssetGroup.ON_DEVICE);
                }
                if (!onClientSide && asset instanceof ImageAsset) {
                    onClientSide = ((ImageAsset) asset).isLocalSrc();
                }
                
                PackageResources.Asset prAsset =
                    new PackageResources.Asset(plain, onClientSide);

                pr.addAssetURLMapping(encoded, prAsset);

                rewritten = new MarinerURL();
                rewritten.setPath(encoded);
            } else {
                throw new NullPointerException(
                    "PackageResources must be set in the application " +
                    "context when using " + getClass().getName());
            }
        }

        return rewritten;
    }

    // javadoc inherited
    public String getEncodedURI(String plain) {
        // We currently ignore 'protocol.mime.preserve.urls' device policy.
        return plain;
    }

    public void createPackage(MarinerRequestContext context,
                              PackageBodySource bodySource,
                              Object bodyContext)
        throws PackagingException {
        MarinerPageContext pageContext =
            ContextInternals.getMarinerPageContext(context);

        // Construct the MIME multipart package
        MimeMultipart pkg = new MimeMultipart();

        // Add the body content to the package
        addBody(pkg, bodySource, context, bodyContext);

        // Add all required assets to the package
        addAssetResources(pkg, pageContext);

        // Output the package to the response
        outputPackage(pkg, pageContext);
    }

    /**
     * The body source's content is added as a body part to the given multipart
     * package.
     *
     * @param pkg            the multipart package to which the body content
     *                       should be added
     * @param bodySource     the body source who's content is to be added
     * @param requestContext the request context
     * @param bodyContext    a contextual object relevant to the body source
     *                       content generation
     * @throws PackagingException if a problem is encountered
     */
    protected void addBody(MimeMultipart pkg,
                           PackageBodySource bodySource,
                           final MarinerRequestContext requestContext,
                           Object bodyContext)
        throws PackagingException {

        final ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            // Generate the body part of the package using the right
            // character encoding
            BodyPart bodyPart = new MimeBodyPart();
            String charEncoding = requestContext.getCharacterEncoding();

            // Write out the main content for the package and put it in the
            // package body
            final Writer writer = new OutputStreamWriter(stream, charEncoding);
            bodySource.write(
                new AbstractPackageBodyOutput() {
                    public OutputStream getRealOutputStream() {
                        return stream;
                    }
                    public Writer getRealWriter() {
                        MarinerPageContext pageContext =
                                ContextInternals.getMarinerPageContext(
                                        requestContext);
                        Encoding charsetEncoding =
                                pageContext.getCharsetEncoding();
                        if (charsetEncoding == null) {
                            throw new RuntimeException(
                                "No charset found, unable to generate page");
                        }
                        return new CharsetEncodingWriter(
                            writer, charsetEncoding);
                    }
                },
                requestContext, bodyContext);
            // This must be done here otherwise the content may not get
            // into the data stream before transferring it into the byte
            // array used by the data source (due to under-the-hood buffering)
            writer.close();

            // Append the charset onto the data's content type
            String bodyContentType = bodySource.getBodyType(requestContext);
            String charsetParam = ";charset=";
            StringBuffer contentTypeBuffer =
                    new StringBuffer(bodyContentType.length() +
                    charsetParam.length() +
                    charEncoding.length());
            contentTypeBuffer.append(bodyContentType).
                    append(charsetParam).append(charEncoding);
            String contentType = contentTypeBuffer.toString();

            // Add the content to the package
            bodyPart.setDataHandler(
                new DataHandler(
                    new ByteArrayDataSource(
                        stream.toByteArray(),
                        contentType)));

            pkg.addBodyPart(bodyPart);
        } catch (UnsupportedEncodingException e) {
            // This should never happen since we always verify the encoding
            // is understood by the JVM at the time it is set.
            // I would throw IllegalStateException if it was Wrapping...
            throw new RuntimeException(
                        exceptionLocalizer.format("unexpected-encoding"), e);
        } catch (MessagingException e) {
            // Problem in the body part data handler
            throw new PackagingException(
                        exceptionLocalizer.format("problem-adding-body"), e);
        } catch (IOException e) {
            // Problem closing the writer
            // MCSRU0033X="Problem adding body to package"
            throw new PackagingException(
                        exceptionLocalizer.format("problem-adding-body"), e);
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                if(logger.isDebugEnabled()){
                    logger.debug(
                    "Unexpected problem releasing body stream resources: " +
                    e);
                }
            }
        }
    }

    /**
     * All relevant assets are added to the package (unless they are deemed
     * to already be cached by the device).
     *
     * @param pkg the multipart package to which the assets are added
     * @param context the page context
     * @throws PackagingException if an error occurs while packaging
     */
    protected void addAssetResources(MimeMultipart pkg,
                                     MarinerPageContext context)
        throws PackagingException {
        boolean includeFullyQualifiedURLs =
            context.getBooleanDevicePolicyValue(
                "protocol.mime.fully.qualified.urls");

        // Now write out the assets for the package, taking into account
        // that only some of the identified asset URLs may have actually been
        // referenced by the body content - the PackageResources' encodedURLs
        // will be non-null if dissection has been applied to the output -
        // that certain types of URL will not be packaged, depending on the
        // device policy and that assets that are thought to already be
        // cached on the device should not be resent
        MarinerRequestContext requestContext = context.getRequestContext();
        ApplicationContext ac =
            ContextInternals.getApplicationContext(requestContext);
        PackageResources pr = ac.getPackageResources();
        List encodedURLs = pr.getEncodedURLs();
        Map assetURLMap = pr.getAssetURLMap();
        Iterator iterator;
        String encodedURL;
        PackageResources.Asset asset;
        String assetURL = null;
        BodyPart assetPart;

        // Select the required set of asset URLs (either those identified
        // as appearing in the part of the page actually generated as the
        // package body, or all those identified during page generation)
        if (encodedURLs != null) {
            iterator = encodedURLs.iterator();
        } else {
            iterator = assetURLMap.keySet().iterator();
        }

        while (iterator.hasNext()) {
            encodedURL = (String)iterator.next();
            asset = (PackageResources.Asset)assetURLMap.get(encodedURL);
            assetURL = asset.getValue();

            // The only assets that should be included in the package are those
            // that meet the device's policy constraints and that are deemed
            // not to have been already cached on the device (if the device
            // policy indicates that caching is available on the device)
            if (includeFullyQualifiedURLs ||
                !isFullyQualifiedURL(assetURL)) {
                // Only add the asset to the package if it is likely that
                // it is not cached on the device
                if (isToBeAdded(assetURL, context)) {
                    assetPart = new MimeBodyPart();

                    try {
                        // If the asset is a server side asset
                        if (!asset.getOnClientSide()) {
                            URL url = null;
                            URLConnection connection;

                            try {
                                url = context.
                                    getAbsoluteURL(new MarinerURL(assetURL));

                                // Test to ensure that this asset exists
                                // before adding it to the package
                                connection = url.openConnection();

                                if (connection != null) {
                                    connection.setDoInput(true);
                                    connection.setDoOutput(false);
                                    connection.setAllowUserInteraction(false);
                                    connection.connect();

                                    // Force the connection to be used
                                    connection.getInputStream();

                                    // If this statement is reached, the
                                    // connection could be successfully
                                    // opened so the asset should exist
                                    assetPart.setDataHandler(
                                        new DataHandler(url));
                                    assetPart.setHeader("Content-Location",
                                                        assetURL);

                                    pkg.addBodyPart(assetPart);
                                }
                            } catch (MalformedURLException e) {
                                // Quietly ignore this asset
                                if(logger.isDebugEnabled()){
                                                                    logger.debug(
                                    "Ignoring asset with malformed URL: " +
                                    url.toString());
                                                                }
                            } catch (IOException e) {
                                // Quietly ignore this asset
                                if(logger.isDebugEnabled()){
                                                                    logger.debug(
                                    "Ignoring asset with URL that doesn't " +
                                    "exist: " +
                                    assetURL + " (" + url.toString() + ")");
                                                                }
                            }
                        } else {
                            // The asset resides on the client, so don't
                            // do anything except reference it from the
                            // package
                            assetPart.setHeader("Content-Location",
                                                "file://" + assetURL);
                        }
                    } catch (MessagingException e) {
                        throw new PackagingException(
                                    exceptionLocalizer.format(
                                                "could-not-add-asset",
                                                encodedURL),
                                    e);
                    }
                }
            }
        }
    }

    /**
     * The package content is completed, a message generated containing it,
     * MIME headers corrected and the package output to the response.
     *
     * @param pkg the multipart package
     * @param context the page context
     * @throws PackagingException if a problem is encountered
     * @todo later if this packager is to be applied to other devices, the
     *       set of "erroneous" headers should probably be handled via some
     *       policy values in the device
     */
    protected void outputPackage(MimeMultipart pkg,
                                 MarinerPageContext context)
        throws PackagingException {
        MarinerRequestContext requestContext = context.getRequestContext();
        ApplicationContext ac =
            ContextInternals.getApplicationContext(requestContext);
        PackageResources pr = ac.getPackageResources();

        // Final stage is setting up the message's content, content type,
        // ensuring that the message has only those headers required and
        // writing the message to the response output stream
        OutputStream outputStream = null;

        try {
            // Construct the outer-most message
            Message message = new MimeMessage(Session.getInstance(
                System.getProperties(), null));
            String messageContentType;

            message.setContent(pkg);
            message.saveChanges();

            // Remove all erroneous headers from the parts
            for (int i = 0;
                 i < pkg.getCount();
                 i++) {
                BodyPart part = pkg.getBodyPart(i);

                // @todo ideally identified from the device repository
                part.removeHeader("Content-Transfer-Encoding");
            }

            // Store the content type for later use
            messageContentType = message.getContentType();

            // Remove the erroneous headers from the message
            // @todo ideally identified from the device repository
            message.removeHeader("Message-ID");
            message.removeHeader("Mime-Version");
            message.removeHeader("Content-Type");

            // Before anything is written to the output stream make sure that
            // the response content type is set.
            //
            // The message content type will always be of the form:
            // 'multipart/mixed;
            //  <whitespace>boundary="<text>"'
            //
            // The first, fixed, part must be replaced by the required
            // contentType but the boundary part must be preserved.
            messageContentType = pr.getContentType() +
                messageContentType.substring(messageContentType.indexOf(';'));
            context.getEnvironmentContext().setContentType(messageContentType);

            // Now write the message to the output stream
            try {
                outputStream =
                    context.getEnvironmentContext().getResponseOutputStream();

                message.writeTo(outputStream);

                // Force the output through to the stream
                // @todo copied from Shaun's example code; probably not needed
                outputStream.flush();
            } catch (MarinerContextException e) {
                throw new PackagingException(
                            exceptionLocalizer.format("no-response-stream"), e);
            }
        } catch (MessagingException e) {
            throw new PackagingException(
                        exceptionLocalizer.format(
                                    "message-writing-finalizing-error"),
                        e);
        } catch (IOException e) {
            // MCSRU0037X="Problem writing to the response writer"
            throw new PackagingException(
                        exceptionLocalizer.format("response-writer-problem"),
                        e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
    }

    /**
     * Determines whether the given asset should be added to the package,
     * based on whether the device is likely to have already cached it from
     * a previous response. This method has the side-effect of updating
     * Mariner's idea of what the device cache contains.
     *
     * @param assetURL the URL of the asset to be added
     * @param context the page context
     * @return true if the asset should be added to the package
     */
    protected boolean isToBeAdded(String assetURL,
                                  MarinerPageContext context) {
        MarinerSessionContext sc = context.getSessionContext();

        // A size of zero is the guaranteed initial value for the cache
        // model size in the session context. If this value is seen, the
        // cache model needs to be initialized
        if (sc.getDeviceAssetCacheMaxSize() == 0) {
            int cacheSize = 0;
            String cacheSizeAsString =
                context.getDevicePolicyValue(
                    "protocol.mime.urls.to.cache");

            if (cacheSizeAsString != null) {
                try {
                    cacheSize = Integer.valueOf(
                        cacheSizeAsString).intValue();
                } catch (NumberFormatException e) {
                    cacheSize = 0;
                }
            }

            if (cacheSize == 0) {
                // Specifically set the value in the session context to a
                // non-zero but "disabled" value so we know that the device
                // policy value has previously been checked and set in the
                // session context
                sc.setDeviceAssetCacheMaxSize(-1);
            } else {
                sc.setDeviceAssetCacheMaxSize(cacheSize);
            }
        }

        // NB: the session context will return false if the cache model is
        // disabled
        return !sc.isAssetCached(assetURL);
    }

    /**
     * Returns true if the given URL is fully qualified. To be fully qualified
     * a URL must have a scheme (or "protocol") prefix.
     *
     * @param url the URL to be checked
     * @return true if the url is fully qualified
     */
    protected boolean isFullyQualifiedURL(String url) {
        MarinerURL theURL = new MarinerURL(url);

        return (theURL.getProtocol() != null);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 15-Sep-04	5521/1	byron	VBM:2004091406 Multi-Part Mime: Does not handle url and urlc attributes on img tag

 15-Sep-04	5519/3	byron	VBM:2004091406 Multi-Part Mime: Does not handle url and urlc attributes on img tag

 14-Sep-04	5519/1	byron	VBM:2004091406 Multi-Part Mime: Does not handle url and urlc attributes on img tag

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 25-Jul-03	860/3	geoff	VBM:2003071405 merge from metis again

 25-Jul-03	860/1	geoff	VBM:2003071405 merge from mimas

 25-Jul-03	858/1	geoff	VBM:2003071405 merge from metis; fix dissection test case sizes

 23-Jul-03	807/1	geoff	VBM:2003071405 works and tested but no design review yet

 ===========================================================================
*/
