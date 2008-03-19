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

package com.volantis.mcs.runtime.dissection;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.integration.URLRewriter;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.mcs.wbsax.AttributeStartCode;
import com.volantis.mcs.wbsax.Codec;
import com.volantis.mcs.wbsax.CopyReferenceResolver;
import com.volantis.mcs.wbsax.PublicIdCode;
import com.volantis.mcs.wbsax.StringReferenceFactory;
import com.volantis.mcs.wbsax.StringReference;
import com.volantis.mcs.wbsax.StringTable;
import com.volantis.mcs.wbsax.VersionCode;
import com.volantis.mcs.wbsax.WBSAXContentHandler;
import com.volantis.mcs.wbsax.WBSAXException;
import com.volantis.mcs.wbsax.WBSAXFilterHandler;
import com.volantis.mcs.wbsax.WBSAXString;
import com.volantis.mcs.wbsax.StringFactory;

/**
 * This class optimises urls by storing the jsessionid in the string table. This
 * means that the jsessionid is stored only once for each page, rather than in
 * each url thus allowing more content to fit into a page.
 */
public class URLOptimiser extends WBSAXFilterHandler {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(URLOptimiser.class);

    /**
     * The MarinerPageContext used to give us access to a URLRewriter
     */
    private MarinerPageContext pageContext;

    /**
     * If we expect to receive a url in the next WBSAX event that then this
     * flag will be set to true.
     */
    private boolean expectUrl;

    private StringFactory strings;
    
    /**
     * The output StringTable used by this filter. This is different from 
     * the input StringTable passed into startDocument!
     */
    private StringTable outputStringTable;

    /**
     * The StringReferenceFactory used to add string references to the
     * StringTable.
     */
    private StringReferenceFactory references;

    /**
     * Create a URLOptimiser filter.
     * 
     * @param next The next WBSAXContentHandler in the chain
     */
    public URLOptimiser(WBSAXContentHandler next) {
        super(next);
        expectUrl = false;
    }

    public void startDocument(VersionCode version, PublicIdCode publicId,
            Codec codec, StringTable stringTable, StringFactory strings)
            throws WBSAXException {

        initialiseDocument(strings);
        handler.startDocument(version, publicId, codec, outputStringTable, 
                strings);
    }

    /**
     * Private helper to initialise the ReferenceResolver, StringTable and
     * StringReferenceFactory
     */
    private void initialiseDocument(StringFactory strings) {        
        this.strings = strings;
        outputStringTable = new StringTable();
        references = new StringReferenceFactory(outputStringTable, strings);
        resolver = new CopyReferenceResolver(references); 
        insertJSessionId();
    }

    /**
     * Insert the jsessionid into the string table. This has to be done now as
     * a complete StringTable is needed by the dissector.
     */
    private void insertJSessionId() {
        // We are only interested in the jsessionid so we can use a fake url.
        
        // Get the URLRewriter to use to encode session information in the
        // URL and use it, do this before stripping the URL as otherwise it is
        // not done properly.
        MarinerRequestContext requestContext = pageContext.getRequestContext();
        URLRewriter sessionURLRewriter = pageContext.getSessionURLRewriter();
        MarinerURL sessionURL
            = sessionURLRewriter.mapToExternalURL(requestContext,
                           pageContext.getRequestURL(true));

        if (logger.isDebugEnabled()) {
            logger.debug("Encoded url is "
                         + sessionURL.getExternalForm());
        }

        // As the link we generate is always back to this page we can reduce the
        // overhead by removing the protocol, authority and all but the last part
        // of the path. We do this before URL rewriting as we have no idea what
        // the URL will look like afterwards.
        sessionURL.setProtocol(null);
        sessionURL.setAuthority(null);
        String path = sessionURL.getPath();
        int index = path.lastIndexOf('/');
        if (index != -1) {
            path = path.substring(index + 1);
            sessionURL.setPath(path);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Relative url is "
                         + sessionURL.getExternalForm());
        }

        // perform any URL rewriting that might be needed
        URLRewriter urlRewriter
            = pageContext.getVolantisBean().getURLRewriter();
        MarinerURL externalURL =
            urlRewriter.mapToExternalURL(requestContext, sessionURL);
            
        // Split the URL up
        SessionIdentifierSearcher searcher
             = SessionIdentifierSearcherFactory.create();            
        SessionIdentifierURL splitURL
             = searcher.getJSessionId(externalURL.getExternalForm());
            
        // If present, add jsessionid to the string table   
        if (splitURL.hasJsessionid()) {
            references.createReference(splitURL.getJsessionid());
            if (logger.isDebugEnabled()) {
                logger.debug(  "Added jsessionid " + splitURL.getJsessionid()
                             + " to string table");
            }            
        }       
    }

    public void startDocument(VersionCode version, StringReference publicId,
            Codec codec, StringTable stringTable, StringFactory strings)
            throws WBSAXException {

        initialiseDocument(strings);
        handler.startDocument(version, publicId, codec, stringTable,
                strings);
    }
      
    public void addAttribute(AttributeStartCode start) throws WBSAXException {
        if ("href".equals(start.getName())) {
            expectUrl = true;
        }
        
        handler.addAttribute(start);
    }

    public void addAttributeValue(WBSAXString part) throws WBSAXException {
        if (expectUrl) {
            SessionIdentifierSearcher searcher
                 = SessionIdentifierSearcherFactory.create();            
            SessionIdentifierURL splitURL
                 = searcher.getJSessionId(part.toString());
            
            // Add the part of the url before the jsessionid as an inline string
            WBSAXString prefix = strings.create(splitURL.getPrefix());
            handler.addAttributeValue(prefix);
            
            // If present, add jsessionid to the string table   
            if (splitURL.hasJsessionid()) {
                StringReference jsessionid
                     = references.createReference(splitURL.getJsessionid());
                handler.addAttributeValue(jsessionid);
            }
            
            // If present, add the part of the url after the jsessionid as an
            // inline string
            if (splitURL.hasSuffix())  {
                WBSAXString suffix = strings.create(splitURL.getSuffix());
                handler.addAttributeValue(suffix);                   
            }
            expectUrl = false;
        } else {
            handler.addAttributeValue(part);
        }                     
    }

    /**
     * Sets the MarinerPageContext for this object. MarinerPageContext is used
     * to get the url rewriter so we can add a jsessionid to the string table
     * @param context
     */
    public void setPageContext(MarinerPageContext context) {
        pageContext = context;        
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/4	doug	VBM:2004111702 Refactored Logging framework

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 14-Jul-03	790/1	geoff	VBM:2003070404 manual merge and just copy wbsax from metis to ensure we got everything

 14-Jul-03	781/2	geoff	VBM:2003070404 clean up WBSAX

 16-Jun-03	372/2	geoff	VBM:2003060609 avoid merge hell by committing on geoffs machine

 13-Jun-03	372/1	chrisw	VBM:2003060609 Implement wmlc url optimiser

 ===========================================================================
*/
