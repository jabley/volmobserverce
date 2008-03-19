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
 * $Header: /src/mps/com/volantis/mps/assembler/MessageRequestor.java,v 1.8 2003/03/26 17:43:13 sumit Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 12-Nov-02    ianw            VBM:2002091806 - Created
 * 15-Nov-02    sumit           VBM:2002110104 - Added support to get text
 *                              from the request headers and the intermal
 *                              SMIL asset map.
 * 23-Jan-03    Sumit           VBM:2003011004 - Changed doExternalRequest to
 *                              use BufferedReader.read(..) not readline(..)
 * 26-Mar-03    Sumit           VBM:2003032602 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * ----------------------------------------------------------------------------
 */

package com.volantis.mps.assembler;

import com.volantis.mcs.application.ApplicationRegistry;
import com.volantis.mcs.application.ApplicationRegistryContainer;
import com.volantis.mcs.application.DefaultApplicationContextFactory;
import com.volantis.mcs.assets.AssetGroup;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.EnvironmentContext;
import com.volantis.mcs.context.MarinerContextException;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.internal.InternalConfig;
import com.volantis.mcs.internal.InternalRequest;
import com.volantis.mcs.internal.InternalResponse;
import com.volantis.mcs.internal.MarinerInternalRequestContext;
import com.volantis.mcs.marlin.sax.MarlinSAXHelper;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.runtime.configuration.MpsPluginConfiguration;
import com.volantis.synergetics.utilities.Base64;
import com.volantis.mcs.utilities.HttpClient;
import com.volantis.mcs.utilities.HttpResponseHeader;
import com.volantis.mps.context.MPSApplicationContext;
import com.volantis.mps.internal.MPSInternalApplicationContextFactory;
import com.volantis.mps.localization.LocalizationFactory;
import com.volantis.mps.message.MessageAsset;
import com.volantis.mps.message.MessageException;
import com.volantis.mps.message.MultiChannelMessage;
import com.volantis.mps.message.ProtocolIndependentMessage;
import com.volantis.mps.servlet.MPSServletApplicationContextFactory;
import com.volantis.synergetics.NameValuePair;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;




/**
 * This class builds a channel independent message by making either an internal
 * request to Mariner vi the <CODE>MamlSAXParser</CODE> for XML messages or via
 * an external servlet request for a JSP page.
 */
public class MessageRequestor {

    private static final String mark = "(c) Volantis Systems Ltd 2002.";

    /**
     * The logger to use
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger( MessageRequestor.class );

    /**
     * The exception localizer instance for this class.
     */
    private static final ExceptionLocalizer localizer =
            LocalizationFactory.createExceptionLocalizer( MessageRequestor.class );

    /**
     * The header key for a server side asset.
     */
    private static final String TEXT_ASSET_SERVER = "textAsset.";

    /**
     * The header key for a server side asset.
     */
    private static final String ASSET_SERVER = "asset."
                                               + AssetGroup.ON_SERVER
                                               + ".";

    /**
     * The header key for a client side asset.
     */
    private static final String ASSET_DEVICE = "asset."
                                               + AssetGroup.ON_DEVICE
                                               + ".";

    protected ApplicationRegistry applicationRegistry =
                                         ApplicationRegistry.getSingleton();

    protected static MessageRequestor instance = new MessageRequestor();

    /**
     * The Base URL fdor internal requests
     */
    private String baseUrl;

    /**
     * Creates a new instance of MessageRequestor
     */
    protected MessageRequestor() {
        // Register the MPS application with Mariner so that
        // we can make requests.

        ApplicationRegistryContainer arc;
        arc = applicationRegistry.getApplicationRegistryContainer(
                            ApplicationRegistry.DEFAULT_APPLICATION_NAME );

        DefaultApplicationContextFactory dfac =
                (DefaultApplicationContextFactory)
                arc.getInternalApplicationContextFactory();

        arc = new ApplicationRegistryContainer(
                                new MPSInternalApplicationContextFactory(),
                                new MPSServletApplicationContextFactory() );

        applicationRegistry.registerApplication( "mps", arc );

        try {
            Volantis volantisBean = Volantis.getInstance();
            if( volantisBean == null ) {
                throw new IllegalStateException
                        ( "Volantis bean has not been initialised" );
            }
            MpsPluginConfiguration config =
                    (MpsPluginConfiguration)volantisBean.getApplicationPluginConfiguration( "MPS" );

            baseUrl = config.getInternalBaseUrl();
            if( logger.isDebugEnabled() ) {
                logger.debug( "MPS internal-base-url: " + baseUrl );
            }
        } catch( Exception e ) {
            if( logger.isDebugEnabled() ) {
                logger.debug( e );
            }
        }

    }

    /**
     * Return the instance of this class
     *
     * @return MessageContextBuilder
     */
    public static MessageRequestor getInstance() {

        return instance;
    }

    /**
     * Get a channel independent representation of a page rendered by Mariner.
     *
     * @param deviceName The device name to render for.
     * @param message    The <CODE>MultiChannelMessage</CODE> to render.
     *
     * @return The channel independent message.
     *
     * @throws MessageException If an error has occurs during rendering.
     */
    public ProtocolIndependentMessage getChannelIndependentMessage(
            String deviceName,
            MultiChannelMessage message )
            throws MessageException {

        ProtocolIndependentMessage rawMessage = null;

        if( message.getMessage() != null ) {
            rawMessage = doInternalRequest( deviceName,
                    message.getMessage(),
                    message.getCharacterEncoding() );
        } else if( message.getMessageURL() != null ) {
            rawMessage = doExternalRequest( deviceName,
                    message.getMessageURL(),
                    message.getCharacterEncoding() );
            if( rawMessage != null ) {
                rawMessage.setBaseURL( message.getMessageURL() );
            }
        }
        return rawMessage;
    }

    /**
     * Invokes the <CODE>MAMLSaxParser</CODE> to make an internal request to
     * Mariner to render the message.
     *
     * @param deviceName        The device name to render for.
     * @param messageXML        The XML Message.
     * @param characterEncoding The character encoding used for the message or
     *                          <code>null</code> for default
     *
     * @return The channel independent message.
     *
     * @throws MessageException If an error has occurs during rendering.
     */
    protected ProtocolIndependentMessage doInternalRequest( String deviceName,
                                                            String messageXML,
                                                            String characterEncoding )
            throws MessageException {

        ProtocolIndependentMessage rawMessage = null;

        try {
            InternalConfig internalConfig = new InternalConfig();
            InternalRequest internalRequest = new InternalRequest( deviceName,
                                                                   "mps" );
            InternalResponse internalResponse = new InternalResponse();

            MarinerRequestContext requestContext =
                    new MarinerInternalRequestContext( internalConfig,
                            internalRequest,
                            internalResponse,
                            null );


            // set character encoding if not null
            if (characterEncoding != null) {
                requestContext.setCharacterEncoding(characterEncoding);
            }


            // Parse the contents of the response as Marlin markup.
            // The processed page will be written to the response, so
            // we don't need to do anything further.
            XMLReader reader =
                    MarlinSAXHelper.getXMLReader( requestContext, null );

            EnvironmentContext environmentContext =
                    ContextInternals.getEnvironmentContext( requestContext );

            // The getXMLReader() call above will set up the pipelineContext in
            // the environmentContext.  See MarlinSAXHelper.setPipelineContext()
            XMLPipelineContext pipelineContext =
                    environmentContext.getPipelineContext();
            // set the Base URI in the pipeline's context
            try {
                URL baseURI = new URL( baseUrl );
                if( logger.isDebugEnabled() ) {
                    logger.debug( "Setting Base URI "
                                        + baseURI.toExternalForm() );
                }
                pipelineContext.pushBaseURI( baseURI.toExternalForm() );
            } catch( MalformedURLException e ) {
                throw new MessageException( e );
            }
            reader.parse( new InputSource( new StringReader( messageXML ) ) );

            MPSApplicationContext applicationContext =
                 (MPSApplicationContext)ContextInternals.getApplicationContext(
                                                               requestContext );

            applicationContext.getAssetMap();

            // set charset type for content
            String contentType = getContentTypeHeader(
                                        internalResponse.getContentType(),
                                        characterEncoding);


            rawMessage = new ProtocolIndependentMessage(
                    internalResponse.getResponseAsString(),
                    contentType,
                    applicationContext.getAssetMap(),
                    characterEncoding);
            rawMessage.setBaseURL( new URL( baseUrl ) );
            rawMessage.setMaxFileSize( applicationContext.getMaxFileSize() );
            rawMessage.setMaxMMSize( applicationContext.getMaxMMSize() );

            requestContext.release();
        } catch (SAXException se) {
            Exception cause = se.getException();
            // Check the root cause of the SAXException, as it may not be
            // logged correctly.
            if (cause != null) {
                logger.error("sax-exception-caught", cause);
            }
            throw new MessageException(
                    localizer.format("error-parsing-xdime"),
                    se);
        } catch (Throwable e) {
            throw new MessageException(e);
        }

        return rawMessage;
    }

    /**
     * Invokes Mariner over http to make an external request to Mariner to
     * render the message.
     *
     * @param deviceName        The device name to render for.
     * @param messageURL        The URL of the Mariner JSP page containing the
     *                          message.
     * @param characterEncoding The character encoding used to generate the
     *                          message, or <code>null</code> for default
     *
     * @return The channel independent message.
     *
     * @throws MessageException If an error has occurs during rendering.
     */
    protected ProtocolIndependentMessage doExternalRequest( String deviceName,
                                                            URL messageURL,
                                                            String characterEncoding )
            throws MessageException {

        ProtocolIndependentMessage rawMessage;

        try {
            Collection requestHeaders = new ArrayList();

            requestHeaders.add( "Mariner-Application:mps" );
            requestHeaders.add( "Mariner-DeviceName:" + deviceName );
            int port = messageURL.getPort();
            if (port == -1) {
                port = messageURL.getDefaultPort();
            }
            String hostHeader = "Host: " + messageURL.getHost();
            if (port != -1) {
                hostHeader = hostHeader + ":" + Integer.toString(port);
            }
            requestHeaders.add(hostHeader);
            if( characterEncoding != null ) {
                requestHeaders.add( "Accept-Charset: " + characterEncoding );
            }

            HttpClient client = new HttpClient( messageURL, requestHeaders );

            HttpResponseHeader responseHeaders = client.getResponseHeaders();

            if( responseHeaders.getErrorCode() == 200 ) {

                // read in message from URL
                InputStreamReader isr =
                        (InputStreamReader)client.getInputStreamReader();
                String encoding = isr.getEncoding();

                StringBuffer bodyContent = new StringBuffer();

                char[] line = new char[1024];
                int read = isr.read(line, 0, line.length);
                while( read > -1 ) {
                    bodyContent.append( line, 0, read );
                    read = isr.read(line, 0, line.length);
                }
                String msg = bodyContent.toString();

                // if a charset has been specified we need to convert from
                // the InputStreamReader charset into the specified
                // charset
                if (characterEncoding != null) {
                    msg = ContentUtilities.convertEncoding(msg,
                                                           encoding,
                                                           characterEncoding);
                }
                String contentType = getContentTypeHeader(
                        responseHeaders.getHeader("Content-Type"),
                        characterEncoding);
                rawMessage = new ProtocolIndependentMessage(
                                        msg,
                                        contentType,
                                        getAssetMapFromHeaders(responseHeaders),
                                        characterEncoding);
                rawMessage.setBaseURL(messageURL);
                rawMessage.setMaxFileSize(
                                getMaxFileSizeFromHeaders( responseHeaders ) );
                rawMessage.setMaxMMSize(
                                getMaxMMSizeFromHeaders( responseHeaders ) );
            } else {
                throw new MessageException(
                        localizer.format( "mcs-server-unknown-error",
                        new Integer( responseHeaders.getErrorCode() ) ) );
            }
        } catch( Exception e ) {
            throw new MessageException( e );
        }
        return rawMessage;
    }

    /**
     * Retrieve the maximum file size from the headers.
     *
     * @param headers The response headers to query for the max file size.
     *
     * @return An Integer value containing the maximum file size, or null if
     *         the value could not be found in the headers.
     */
    protected Integer getMaxFileSizeFromHeaders( HttpResponseHeader headers ) {
        Integer maxFileSize = null;
        String maxFileSizeString = headers.getHeader( "maxfilesize" );
        if( maxFileSizeString != null ) {
            maxFileSize = new Integer( maxFileSizeString );
        }
        return maxFileSize;
    }

    /**
     * Retrieve the maximum MM message size from the headers.
     *
     * @param headers The response headers to query for the max message size.
     *
     * @return An Integer value containing the maximum message size, or null if
     *         the value could not be found in the headers.
     */
    protected Integer getMaxMMSizeFromHeaders( HttpResponseHeader headers ) {
        Integer maxMMSize = null;
        String maxMMSizeString = headers.getHeader( "maxmmsize" );
        if( maxMMSizeString != null ) {
            maxMMSize = new Integer( maxMMSizeString );
        }
        return maxMMSize;
    }

    /**
     * Get an assetMap from http request headers.
     *
     * @param responseHeaders The response headers from the request.
     *
     * @return The assetMap
     */
    protected Map getAssetMapFromHeaders( HttpResponseHeader responseHeaders ) {


        Map headerMap = responseHeaders.getHeaders();
        Map assetMap = new HashMap();
        Iterator i = headerMap.keySet().iterator();
        String mimeReference;
        String url, text;
        int locationType;
        while( i.hasNext() ) {
            String key = (String)i.next();
            // We can only retrieve assets that reside on the
            // server.
            if( key.startsWith( ASSET_SERVER ) ) {
                mimeReference = Base64.decodeToString(
                                    key.substring( ASSET_SERVER.length() ) );
                url = Base64.decodeToString(
                        ( (NameValuePair)headerMap.get( key ) ).getValue() );
                locationType = MessageAsset.ON_SERVER;

                assetMap.put( mimeReference, new MessageAsset( locationType, url ) );
            } else if( key.startsWith( ASSET_DEVICE ) ) {
                mimeReference = Base64.decodeToString(
                                    key.substring( ASSET_DEVICE.length() ) );
                url = Base64.decodeToString(
                        ( (NameValuePair)headerMap.get( key ) ).getValue() );
                locationType = MessageAsset.ON_DEVICE;

                assetMap.put( mimeReference, new MessageAsset( locationType, url ) );
            } else if( key.startsWith( TEXT_ASSET_SERVER ) ) {
                mimeReference = key.substring( TEXT_ASSET_SERVER.length() );
                text = Base64.decodeToString(
                        ( (com.volantis.synergetics.NameValuePair)headerMap.get( key ) ).getValue() );
                assetMap.put( mimeReference, new MessageAsset( text ) );
            }
        }
        return assetMap;
    }

    /**
     * Utility method to ensure that the content-type header has the correct
     * character encoding specified
     *
     * @param contentType       As obtained from the <code>InternalResponse</code>
     * @param characterEncoding The character encoding sepcifed
     *
     * @return  Content-Type header string
     */
    private String getContentTypeHeader(String contentType,
                                        String characterEncoding) {
        String mimeType = null;
        String charsetType = null;
        String header = null;

        if (contentType.indexOf(';', 0) != -1) {
            mimeType = contentType.substring(0, contentType.indexOf(';', 0));
            charsetType = contentType.substring(contentType.indexOf(';', 0) + 1);
        } else {
            mimeType = contentType;
        }

        header = mimeType;

        if (characterEncoding != null) {
            // return mime + specified charset
            return header + "; charset=" + characterEncoding;
        }
        else {
            if (charsetType != null) {
                // return original string
                return header + "; " + charsetType;
            }
            else {
                // just return mime
                return header;
            }
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Oct-05	927/1	matthew	VBM:2005100401 Base64 has moved

 07-Jul-05	829/3	amoore	VBM:2005052302 Fixed encoding problems that caused DB characters to be represented by '?'

 01-Jul-05	776/9	amoore	VBM:2005052302 Fixed encoding problems that caused DB characters to be represented by '?'

 30-Jun-05	814/1	pcameron	VBM:2005062305 Fixed some message localisations for exception throwing

 17-May-05	744/1	amoore	VBM:2005051206 Updated MPS to ensure correct encoding of messages when using DBCS

 04-May-05	651/1	amoore	VBM:2005042903 Made API changes to support message encoded in double byte languages

 20-Dec-04	270/1	pcameron	VBM:2004122004 New packagers for wemp

 29-Nov-04	243/2	geoff	VBM:2004112302 Provide new Logging Infrastructure: MPS

 19-Oct-04	198/1	matthew	VBM:2004101311 allow mss logging to work (stop MCS from hijacking it)

 30-Jul-04	133/3	claire	VBM:2004070703 Ensuring maximum sizing is honoured on messages

 10-Jun-04	121/1	ianw	VBM:2004060111 Made to work with main 3.2 MCS stream

 19-Dec-03	75/1	geoff	VBM:2003121715 Import/Export: Schemify configuration file: Clean up existing elements

 13-Oct-03	40/1	pcameron	VBM:2003100707 Removed all traces of licensing from MPS

 10-Sep-03	30/3	byron	VBM:2003090102 Librarian should handle any snapshot name via decentralized lookup table

 10-Sep-03	30/1	byron	VBM:2003090102 Librarian should handle any snapshot name via decentralized lookup table

 ===========================================================================
*/
