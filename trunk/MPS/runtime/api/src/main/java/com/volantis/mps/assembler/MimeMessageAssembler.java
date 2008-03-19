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
 * $Header: /src/mps/com/volantis/mps/assembler/MimeMessageAssembler.java,v 1.15 2003/01/31 17:59:39 sfound Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 12-Nov-02    ianw            VBM:2002092305 - Created
 * 15-Nov-02    sumit           VBM:2002110104 - Added support for Text messages
 * 19-Nov-02    Chris W         VBM:2002111824 - Wrap message body in
 *                              ByteArrayDataSource which can handle any mime
 *                              type, rather than setting the content.
 * 28-Nov-02    Chris W         VBM:2002112704 - assembleMessage sets the
 *                              Content-ID as well as the Content-Location
 * 28-Nov-02    Sumit           VBM:2002112602 - Changed assembleMessage() sig
 *                              to accept MessageAttachments parameter
 * 02-Dec-02    Byron           VBM:2002103009 - Added checkMessageConstraints,
 *                              calculateMaxImageSize, determineAssetSize and
 *                              getParameterValue. Modified assembleMessage()
 * 07-Jan-03    Chris W         VBM:2003010705 - assembleMessage() calls private
 *                              helper checkAssetExists(). If the file
 *                              associated with the asset does not exist the
 *                              asset is not included in the message.
 * 13-Jan-03    Steve           VBM:2002122009 - Read tf.maxfilesize parameter 
 *                              from URL instead of maximagesize.
 * 17-Jan-02    ianw            VBM:2003010708 - Changed asset url's to used
 *                              cid: + filename so that images display on all 
 *                              devices including Netscape.
 * 21-Jan-02    Chris W         VBM:2003012103 - assembleMessage() uses
 *                              VolantisMimeBodyPart instead of javax.mail.internet.
 *                              MimeBodyPart for the body of the message.
 *                              VolantisMimeBodyPart extends MimeBodyPart in
 *                              order to set the Content-Transfer-Encoding of
 *                              smil markup correctly. 
 * 28-Jan-03    Steve           VBM:2003012704 - Check for null attachments before
 *                              trying to add them.
 * 29-Jan-03    ianw            VBM:2003012906 - Fixed filename issues for
 *                              Windoze.   
 * 31-Jan-03    Steve           VBM:2003012911 - Fixed up attachment filename
 *                              if it doesnt agree with the mime type extension
 * -----------------------------------------------------------------------------
 */

package com.volantis.mps.assembler;

import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.mps.attachment.DeviceMessageAttachment;
import com.volantis.mps.attachment.MessageAttachment;
import com.volantis.mps.attachment.MessageAttachments;
import com.volantis.mps.localization.LocalizationFactory;
import com.volantis.mps.message.MessageAsset;
import com.volantis.mps.message.MessageException;
import com.volantis.mps.message.ProtocolIndependentMessage;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.URLDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Hashtable;

/**
 * This <CODE>MessageAssembler</CODE> assembles Multipart Mime messages such
 * as those used by MHTML or MMS.
 */
public class MimeMessageAssembler extends MessageAssembler {

    private static final String mark = "(c) Volantis Systems Ltd 2002.";

    /**
     * The logger to use
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger( MimeMessageAssembler.class );

    /**
     * The localizer instance for this class.
     */
    private static final ExceptionLocalizer localizer =
            LocalizationFactory.createExceptionLocalizer( MimeMessageAssembler.class );

    /**
     * The lookup table for common mimetypes
     */
    private static final Map mimeToExt = new Hashtable();

    static {
        // populate extToMime table
        mimeToExt.put( "image/bmp", "bmp" );
        mimeToExt.put( "image/gif", "gif" );
        mimeToExt.put( "image/jpeg", "jpg" );
        mimeToExt.put( "image/png", "png" );
        mimeToExt.put( "image/tiff", "tiff" );
        mimeToExt.put( "image/pjpeg", "pjpeg" );
        mimeToExt.put( "image/vnd.wap.wbmp", "wbmp" );
        mimeToExt.put( "audio/gsm", "gsm" );
        mimeToExt.put( "audio/midi", "midi" );
        mimeToExt.put( "audio/mpeg3", "mp3" );
        mimeToExt.put( "audio/x-ms-wma", "wma" );
        mimeToExt.put( "audio/sp-midi", "mid" );
        mimeToExt.put( "audio/wav", "wav" );
        mimeToExt.put( "application/vnd.nokia.ringing-tone", "mg" );
        mimeToExt.put( "application/vnd.smaf", "mmf" );
        mimeToExt.put( "audio/mmf", "rmf" );
        mimeToExt.put( "audio/basic", "au" );
        mimeToExt.put( "audio/imelody", "imy" );
        mimeToExt.put( "audio/amr", "amr" );
        mimeToExt.put( "video/mpeg", "mpg" );
        mimeToExt.put( "video/x-ms-wm", "wm" );
        mimeToExt.put( "video/x-ms-wmv", "wmv" );
        mimeToExt.put( "application/x-shockwave-flash", "swf" );
        mimeToExt.put( "video/quicktime", "qt" );
        mimeToExt.put( "video/vnd.rn-realvideo", "rv" );
        mimeToExt.put( "application/vnd.rn-realmedia", "rm" );
        mimeToExt.put( "application/vnd.rn-realaudio", "ram" );
        mimeToExt.put( "video/avi", "avi" );

    }

    /**
     * Creates a new instance of MimeMessageAssembler
     */
    public MimeMessageAssembler() {
    }

    /**
     * Assembles the body of the message, Attachments are attached as mime
     * multipart objects with Content-Disposition: attachment;
     * <p/>
     * Also determine whether this message falls within the constraints
     * specified, such as sizing,
     *
     * @param message     the message to be used in the assembly
     * @param attachments the attachments for the message
     * @return a new MimeMultipart message containing all
     *         message bodies.
     * @throws MessageException thrown if there are any problems retrieving the
     *                          assets.
     */
    public Object assembleMessage( ProtocolIndependentMessage message,
                                   MessageAttachments attachments )
            throws MessageException {

        MimeMultipart mimeMultipart = new MimeMultipart( "related" );
        try {
            // Assemble the body
            BodyPart body = new VolantisMimeBodyPart();
            BodyPart asset;
            body.setDataHandler(new DataHandler(
                    new ByteArrayDataSource(
                        message.getMessage(),
                        message.getMimeType())));
            body.setHeader("Content-Type", message.getMimeType());
            mimeMultipart.addBodyPart( body );

            // Assemble Assets
            Map assetMap = message.getAssetMap();
            Iterator i = assetMap.keySet().iterator();
            String mimeReference;
            MarinerURL url;
            MessageAsset messageAsset;

            // Sizes are in bytes
            Integer maxImageSize = message.getMaxFileSize();
            Integer maxMessageSize = message.getMaxMMSize();
            int actualMessageSize = 0;

            ArrayList imageAssets = new ArrayList();

            // Process each of the assets
            while( i.hasNext() ) {

                mimeReference = ( String ) i.next();
                // We can only retrieve assets that reside on the
                // server.
                messageAsset = ( MessageAsset ) assetMap.get( mimeReference );

                // Set appropriate headers
                asset = new MimeBodyPart();
                asset.setHeader( "Content-Disposition",
                        "inline; filename=\"" + mimeReference + "\"" );
                asset.setHeader( "Content-ID", "<" + mimeReference + ">" );

                if (messageAsset.isTextMessage()) {
                    // Handle simple textual content
                    String contentType = "text/plain";
                    if (message.getCharacterEncoding() != null) {
                            contentType += "; charset=" +
                                            message.getCharacterEncoding();
                    }
                    asset.setContent( messageAsset.getText(), contentType);
                    mimeMultipart.addBodyPart( asset );
                    actualMessageSize += determineAssetSize( asset );
                } else {
                    // Handle other content, but only if they reside on the
                    // server
                    if( messageAsset.getLocationType() ==
                            MessageAsset.ON_SERVER ) {
                        url = fixupURL( message.getBaseURL(),
                                new MarinerURL( messageAsset.getUrl() ) );
                        URL assetURL = new URL( url.getExternalForm() );

                        // Now sort out the content type and whether the asset
                        // exists or not...
                        try {
                            URLConnection connection =
                                    assetURL.openConnection();

                            // Check to see if a connection object has been
                            // successfully obtained
                            if( connection != null ) {
                                // Set the connection up
                                connection.setDoInput( true );
                                connection.setDoOutput( false );
                                connection.setAllowUserInteraction( false );

                                // Actually connect
                                connection.connect();

                                // Force the connection to be used
                                connection.getInputStream();

                                // Grab the content type from the stream to
                                // use in the mime body part of the asset
                                String contentType =
                                        connection.getContentType();
                                // By getting here, the asset exists and the
                                // connection succesfully opened so add the
                                // asset
                                asset.setDataHandler( new DataHandler( assetURL ) );
                                asset.setHeader( "Content-location",
                                        url.getExternalForm() );
                                asset.setHeader( "Content-Type", contentType );
                                mimeMultipart.addBodyPart( asset );

                                // Store the images for later size processing
                                imageAssets.add( asset );
                            }
                        } catch( MalformedURLException mue ) {
                            // Quietly ignore this asset
                            if( logger.isDebugEnabled() ) {
                                logger.debug( "Ignoring asset with malformed URL: " +
                                        url.toString() );
                            }
                        } catch( IOException ioe ) {
                            // Quietly ignore this asset
                            if( logger.isDebugEnabled() ) {
                                logger.debug( "Ignoring asset with URL that " +
                                        "doesn't exist: " + assetURL +
                                        " (" + url.toString() + ")" );
                            }
                        }
                    } else {
                        // For device based assets there's not much that can
                        // be set apart from where it is!
                        asset.setHeader( "Content-Location",
                                "file://" + messageAsset.getUrl() );
                    }
                }
            }

            // Add on any attachments
            if( attachments != null ) {
                mimeMultipart = addAttachments( mimeMultipart, attachments );
            }

            // Check to see if the message is within the specified boundaries.
            checkMessageConstraints( imageAssets,
                    maxMessageSize,
                    maxImageSize,
                    actualMessageSize );
        } catch( Exception e ) {
            throw new MessageException( e );
        }
        return mimeMultipart;

    }

    /**
     * Check to see if the message constraints have been violated. If they have
     * then throw an MessageException with an appropriated message.
     *
     * @param imageAssets       an arraylist of convertible images in the
     *                          Message
     * @param maxMessageSize    the maximum size that this message should be
     *                          in bytes
     * @param maxImageSize      the maximum size that this each image should be
     *                          in bytes (specified by the device).
     * @param actualMessageSize the actual message size (excluding convertible
     *                          image assets)
     * @throws MessageException if the size boundaries have been exceeded
     */
    protected void checkMessageConstraints( ArrayList imageAssets,
                                            Integer maxMessageSize,
                                            Integer maxImageSize,
                                            int actualMessageSize )
            throws MessageException {

        // Iterate through the convertible image assets and add all the
        // image sizes to the actual message size.
        Iterator assets = imageAssets.iterator();
        int assetSize;

        while( assets.hasNext() ) {
            BodyPart bodyPart = ( MimeBodyPart ) assets.next();

            // Check to see if this transcoded image is greater than
            // the allocated chunk from the remainder of the message.
            assetSize = determineAssetSize( bodyPart );
            actualMessageSize += assetSize;

        }

        // Now check to see if the actual message size (including all assets)
        // is greater than that specified.
        if (((maxMessageSize != null) && maxMessageSize.intValue() >= 0) &&
                (actualMessageSize > maxMessageSize.intValue())) {
            final String messageKey = "message-too-big";
            final Object[] messageParams = new Object[]{
                new Integer(actualMessageSize), maxMessageSize};
            logger.warn(messageKey, messageParams);
            throw new MessageException(localizer.format(messageKey,
                                                        messageParams));
        }
    }

    /**
     * Determine the asset size in bytes.
     *
     * @param asset the asset that will be used to determine the
     *              asset size
     * @return asset size in bytes
     * @throws MessageException if the size cannot be determined.
     */
    protected int determineAssetSize( BodyPart asset )
            throws MessageException {

        int size = 0;
        try {
            ByteArrayOutputStream bais = new ByteArrayOutputStream();
            asset.writeTo( bais );
            size += bais.size();
        } catch( IOException e ) {
            throw new MessageException( localizer.format(
                                 "asset-size-unavailable-io-error" )
                                  );
        } catch( MessagingException e ) {
            throw new MessageException( localizer.format(
                                 "asset-size-unavailable-message-write-error" )
                                  );
        }
        return size;
    }

    /**
     * Adds the attachments provided to the mimeMultipart specified.
     *
     * @param mimeMultipart The multipart to which the attachments should be
     *                      added
     * @param attachments   The attachments to add
     */
    private MimeMultipart addAttachments( MimeMultipart mimeMultipart,
                                          MessageAttachments attachments ) {
        Iterator itr = attachments.iterator();
        while( itr.hasNext() ) {
            DeviceMessageAttachment attachment =
                    ( DeviceMessageAttachment ) itr.next();
            DataSource ds;
            BodyPart bodyPart;

            try {
                if( attachment.getValueType() == MessageAttachment.FILE ) {
                    // The attachment is a file type so create a FileDataSource
                    ds = new FileDataSource( attachment.getValue() );
                } else {
                    // If for some reason the URL does not return a content
                    // type then this type of datasource defaults to
                    // application/octet-stream.
                    ds = new URLDataSource( new URL( attachment.getValue() ) );
                }

                bodyPart = new MimeBodyPart();
                bodyPart.setDataHandler( new DataHandler( ds ) );
                bodyPart.setDisposition( Part.ATTACHMENT );
                bodyPart.setFileName( getFileName( attachment.getValue(),
                                                   attachment.getMimeType() ) );
            } catch( Exception me ) {
                // If we catch an exception above we might as well
                // skip this attachment
                logger.error( "body-part-creation-failure-skipping-attachment",
                              me );
                continue;
            }
            try {
                String bodyMimeType = bodyPart.getContentType();
                String messageMimeType = attachment.getMimeType();

                // Check if the Content-Type determined by the DataHandler
                // is the same or a more specific than the mimeType specified
                // in the attachment. E.g. "text/html; charset=us-ascii" as
                // against "text/html;" If it is more specific then leave as is,
                // if it is different then use the mime type specified in the
                // attachment
                if( messageMimeType != null &&
                        bodyMimeType.indexOf( messageMimeType ) == -1 ) {
                    bodyPart.addHeader( "Content-Type", messageMimeType );
                }
            } catch( Exception e ) {
                // Here we have failed to correct the mime type if required.
                // Leave the bodyPart as is and continue as this is not critical
                logger.warn( "attachment-mime-type-assignment-failure", e );
            }

            try {
                mimeMultipart.addBodyPart( bodyPart );
            } catch( MessagingException me ) {
                //Oops. Could not add bodyPart. Oh well log it and continue
                logger.error( "attachment-addition-failure-continuing", me );
            }
        }
        return mimeMultipart;
    }

    /**
     * Obtains the filename from the path specified.
     *
     * @param pathName The path to a file
     * @param mimeType Mime type of file
     * @return The file name plus extension or path if no file name
     *         found at end of path
     */
    protected String getFileName( String pathName, String mimeType ) {
        String fileName = null;
        String internalPathName = null; // prevent modification pathName

        if( pathName != null ) {

            // Handle Windoze filenames
            fileName = pathName.replace( '\\', '/' );
            internalPathName = pathName.replace( '\\', '/' );

            int pos = fileName.lastIndexOf( '/' );

            if( pos != -1 ) {
                fileName = fileName.substring( pos + 1 );
            }

            int qm = fileName.indexOf( '?' );

            if( qm != -1 ) {
                fileName = fileName.substring( 0, qm );
            }
        }
        if( fileName == null || fileName.length() == 0 ) {
            return internalPathName;
        }

        if( mimeType != null ) {
            int lastDot = fileName.lastIndexOf( "." );
            String fileExtension = null;
            if( lastDot > -1 ) {
                fileExtension = fileName.substring( lastDot + 1 );
                fileName = fileName.substring( 0, lastDot );
            }

            // now check if it's a mime type that must have specific extension
            String mappedExtension = ( String ) mimeToExt.get( mimeType );
            if( mappedExtension != null ) {
                // we have a mime type that must have specified extension
                fileName = fileName + "." + mappedExtension;
            }
            if( mappedExtension == null && fileExtension != null ) {
                // no mapping but preserve file extension
                fileName = fileName + "." + fileExtension;
            }
            if( mappedExtension == null && fileExtension == null ) {
                // no mapping & no extension - generate from mime type
                int pos = mimeType.lastIndexOf( "/" );
                String mimeExtension = null;
                if( pos > -1 ) {
                    mimeExtension = mimeType.substring( pos + 1 );
                }
                fileName = fileName + "." + mimeExtension;
            }
        }

        return fileName;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 07-Jul-05	829/2	amoore	VBM:2005052302 Fixed encoding problems that caused DB characters to be represented by '?'

 01-Jul-05	776/4	amoore	VBM:2005052302 Fixed encoding problems that caused DB characters to be represented by '?'

 18-May-05	744/3	amoore	VBM:2005051206 Updated MimeMessageAssembler to use proper Content-Type header in text assest

 17-May-05	744/1	amoore	VBM:2005051206 Updated MPS to ensure correct encoding of messages when using DBCS

 29-Apr-05	647/1	philws	VBM:2005040509 Minor exception message changes and switch to MarlinSAXHelper

 15-Apr-05	536/3	amoore	VBM:2005041310 Code style changes

 15-Apr-05	538/5	amoore	VBM:2005041310 Removed text/xml entry mime type mapping

 15-Apr-05	538/3	amoore	VBM:2005041310 Removed excess mappings and changed mapping to mime > ext

 14-Apr-05	538/1	amoore	VBM:2005041310 Updated getFileName(...) to not mangle certain extensions

 18-Mar-05	430/1	emma	VBM:2005031707 Merge from MPS V3.3.0 - Ensuring mps' LocalizationFactory used; moved localization source code to localization subsystem

 18-Mar-05	428/1	emma	VBM:2005031707 Making all classes use mps' LocalizationFactory, and moving localization source code to localization subsystem

 29-Nov-04	243/2	geoff	VBM:2004112302 Provide new Logging Infrastructure: MPS

 19-Oct-04	198/1	matthew	VBM:2004101311 allow mss logging to work (stop MCS from hijacking it)

 30-Sep-04	172/1	claire	VBM:2004071903 Mime types not being set for non-text assets on the server

 22-Sep-04	140/1	claire	VBM:2004070704 Tidying up Transforce use in the MimeMessageAssembler and fixing testcases

 30-Jul-04	133/3	claire	VBM:2004070703 Ensuring maximum sizing is honoured on messages

 ===========================================================================
*/
