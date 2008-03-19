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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.context;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.volantis.mcs.integration.PageURLDetails;
import com.volantis.mcs.integration.PageURLRewriter;
import com.volantis.mcs.integration.PageURLType;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.synergetics.utilities.Base64;

/**
 * The context held during the lifetime of single package prerendering process +
 * methods to rewrite URIs.
 * 
 * @mock.generate
 */
public class PrerendererPackageContext {
    /**
     * The set of page URIs. Each page URI in this set is already normalized and
     * resolved against the prefixPathURI.
     */
    private final HashSet pageURISet;

    /**
     * The list of relative page URIs, as provided in the constructor.
     */
    private final List pageURIs;

    /**
     * The URI of the start page. The URI is already normalized and resolved
     * against prefixPathURI, and is included within the pageURISet.
     */
    private final URI firstPageURI;

    /**
     * The absolute base URI, consisting only of scheme and authority
     * components. All URIs on prerendered pages which are not to be rewritten
     * to local, will be resolved against this URI before rendering.
     */
    private final URI baseURI;
    
    /**
     * The prefix path URI, containing absolute path component.
     */
    private final URI prefixPathURI;
    
    /**
     * A set including all already rewritten URIs. All URIs stored in this set
     * are normalized, resolved against prefixPathURI and does not contain a
     * fragment component.
     */
    private final Map rewrittenURIMap;
    
    /**
     * The map containing URIs, which have been rewritten since last request.
     */
    private Map incrementalRewrittenURIMap;

    /**
     * Map containing rewritten page URIs.
     */
    private List rewrittenPageURIs;
    
    /**
     * The number of the next rewritten URI.
     */
    private long nextRewrittenURINumber = 1;
    
    /**
     * The PageURLRewriter used within this context.
     */
    private final PageURLRewriter pageURLRewriter = new PrerendererPageURLRewriter();

    /**
     * Initializes this context provided with list of page URIs, base and prefix
     * URI.
     * 
     * <p>
     * Following rules apply to the arguments:
     * <li>The pageURIs list should consist only of instances of java.set.URI,
     * and each URI should consist only of path component with optional
     * query component.</li>
     * <li>The baseURI should consist only of scheme, authority and path
     * component.</li>
     * <li>The prefixPathURI should consist only of absolute path component.</li>
     * </p>
     * 
     * @param pageURIs The list of page URIs (java.set.URI)
     * @param baseURI the base URI
     * @param prefixPathURI the prefix
     * @throws ClassCastException In case the list of pageURIs contains an
     *             instance of class different than java.set.URI.
     * @throws IllegalArgumentException In case the some of the URIs is illegal.
     */
    public PrerendererPackageContext(List pageURIs, URI baseURI, URI prefixPathURI) {
        // Validate each of page URIs.
        for (int index = 0; index < pageURIs.size(); index++) {
            URI pageURI = (URI) pageURIs.get(index);
        
            if (pageURI.getScheme() != null 
                    || pageURI.getAuthority() != null
                    || pageURI.getPath() == null
                    || pageURI.getFragment() != null) {
                throw new IllegalArgumentException("Each pathURI must consist only of path component, with optional query component");
            }
        }
        
        // Validate base URI.
        if (baseURI.getScheme() == null 
                || baseURI.getAuthority() == null
                || baseURI.getPath() == null
                || !baseURI.getPath().endsWith("/")
                || baseURI.getQuery() != null 
                || baseURI.getFragment() != null) {
            throw new IllegalArgumentException("The baseURI must consist only of scheme, authority and path components");
        }
        
        // Validate prefix path URI.
        if (prefixPathURI.getScheme() != null 
                || prefixPathURI.getAuthority() != null
                || prefixPathURI.getPath() == null
                || !prefixPathURI.getPath().startsWith("/")
                || !prefixPathURI.getPath().endsWith("/")
                || prefixPathURI.getQuery() != null 
                || prefixPathURI.getFragment() != null) {
            throw new IllegalArgumentException("The prefixPathURI must consist only of absolute path component");
        }
        
        // Initialize.
        this.pageURIs = pageURIs;
        
        // Prepare a set of resolved page URIs
        this.pageURISet = new HashSet();
        
        for (int index = 0; index < pageURIs.size(); index++) {
            URI pageURI = (URI) pageURIs.get(index);
            
            this.pageURISet.add(prefixPathURI.resolve(pageURI));
        }
        
        // Prepare resolved and normalized URI to the first page.
        if (!pageURIs.isEmpty()) {
            this.firstPageURI = prefixPathURI.resolve((URI) pageURIs.get(0));
        } else {
            this.firstPageURI = null;
        }
        
        this.baseURI = baseURI;

        this.prefixPathURI = prefixPathURI;
        
        this.rewrittenURIMap = new HashMap();
        
        this.incrementalRewrittenURIMap = new HashMap();
        
        // Initialize page URI map containing rewritten page URIs.
        ArrayList rewrittenPageURIs = new ArrayList();
        
        Iterator iterator = pageURIs.iterator();
        
        while (iterator.hasNext()) {
            URI pageURI = (URI) iterator.next();
            
            pageURI = prefixPathURI.resolve(pageURI);
            
            URI rewrittenPageURI = rewritePageURI(pageURI);
            
            rewrittenPageURIs.add(rewrittenPageURI);
            
            rewrittenURIMap.put(pageURI, rewrittenPageURI);
        }
        
        this.rewrittenPageURIs = Collections.unmodifiableList(rewrittenPageURIs);
    }
    
    /**
     * Returns PageURLRewriter used within this context.
     * 
     * @return PageURLRewriter used within this context.
     */
    public PageURLRewriter getPageURLRewriter() {
        return pageURLRewriter;
    }
    
    /**
     * Rewrites an URI of specified type, which was included on the page
     * requested within specified request context.
     * 
     * @param requestContext The request context of the owning page.
     * @param uri The URI to rewrite.
     * @param type The type of the URI to rewrite.
     * @return the rewritten URI.
     */
    public URI rewriteURI(
            MarinerRequestContext requestContext,
            URI uri,
            PageURLType type) {

        // Step 1a: Resolve and normalize remote URI.
        // This is the base for comparision with the URIs specified in initial
        // set of pages.
        URI remoteURI = resolveAndNormalizeURI(requestContext, uri);
        
        // Step 1b: Strip the fragment component of the URI.
        URI remoteURINoFragment = stripFragmentComponent(remoteURI);

        // Step 2: Decide, whether the URI needs to be rewritten to local one,
        // or to the production one, or whether it should remain unchanged.
        boolean shouldRewriteToLocal = false;
        boolean shouldRewriteToProduction = false;
        
        // URI should be rewritten to local one, if its a resource, or it's a
        // href to one of the pages included within the installation package.
        // TODO: What with dissected pages?
        if (type == PageURLType.ANCHOR) {
            // Anchor URIs will be rewritten only, if they are included within
            // the initial list of page URIs.
            if (pageURISet.contains(remoteURINoFragment)) {
                shouldRewriteToLocal = true;
            }
        } else {
            // All non anchor elements except WIDGET ones will be rewritten to local.
            // That includes style-sheets, scripts, object sources etc...
            shouldRewriteToLocal = (type != PageURLType.WIDGET);
        }
        
        // URI should be rewritten to production one, if it's not rewritten
        // to local, and does not contain scheme nor authority component.
        if (!shouldRewriteToLocal && (remoteURI.getScheme() == null) && (remoteURI.getAuthority() == null)) {
            shouldRewriteToProduction = true;
        }

        // Step 3: Rewrite the URI.
        URI rewrittenURI;
        
        if (shouldRewriteToLocal) {
            URI rewrittenURINoFragment = (URI) rewrittenURIMap.get(remoteURINoFragment);
            
            if (rewrittenURINoFragment == null) {
                // The URI hasn't been rewritten yet.
                // If the URI is to be rewritten locally, do that
                // and add it to the list of rewritten URIs.
                // The URI should be rewritten without the fragment component.
                rewrittenURINoFragment = rewriteURIToLocal(remoteURINoFragment);

                // Add the rewritten URI to the set of rewritten URIs.
                rewrittenURIMap.put(remoteURINoFragment, rewrittenURINoFragment);

                // If this URI is rewritten for the first time, add it to the
                // incremental set of rewritten URIs.
                incrementalRewrittenURIMap.put(prefixPathURI.relativize(remoteURINoFragment), rewrittenURINoFragment);
            }   
            
            // After rewriting, append the fragment component back.
            rewrittenURI = appendFragmentComponent(rewrittenURINoFragment, remoteURI);


        } else if (shouldRewriteToProduction) {
            // If the URI is to be rewritten to production one,
            // do it right now.
            rewrittenURI = rewriteURIToProduction(remoteURI);
            
        } else {
            // Otherwise the URI should remain unchanged.
            rewrittenURI = uri;
        }
        
        return rewrittenURI;
    }
    
    /**
     * @return Returns the rewrittenPageURIs.
     */
    public List getRewrittenPageURIs() {
        return rewrittenPageURIs;
    }

    /**
     * @return Returns the incrementalRewritten.
     */
    public Map getIncrementalRewrittenURIMap() {
        Map result = incrementalRewrittenURIMap;
        
        incrementalRewrittenURIMap = new HashMap();
        
        return result;
    }
    
    /**
     * Rewrites specified URI to local one, and returns it.
     * 
     * @param uri The URI to rewrite.
     * @return The rewritten URI.
     */
    private URI rewriteURIToLocal(URI uri) {
        // This variable will hold the rewritten URI. Initially it's the source one.
        URI rewrittenURI = uri;
        
        // This variable will be set to true, if the URI is to be rewritten.
        boolean rewrite = false;
        
        // Step 1: If URI is absolute (contains a scheme component), it'll be
        // rewritten only if its 'http' one. All non-absolute URIs
        // will be rewritten.
        if (uri.isAbsolute()) {
            String scheme = uri.getScheme();
            
            if (scheme.equals("http")) {
                rewrite = true;
            }
        } else {
            rewrite = true;
        }

        // If URI is to be rewritten, do it right now.
        if (rewrite) {
            rewrittenURI = rewriteURI(uri);
        }
        
        // Return possibly rewritten URI.
        return rewrittenURI;
    }
    
    /**
     * Rewrites the URI to the production one.
     * 
     * @param uri The URI to rewrite.
     * @return the rewritten URI.
     */
    private URI rewriteURIToProduction(URI uri) {
        return baseURI.resolve(prefixPathURI.relativize(uri));
    }
    
    /**
     * Rewrites the page URI, which should already be resolved and normalized.
     * 
     * If specified URI is the first URI on the pageURIs list, it's rewritten to
     * "index.html". Otherwise, a unique URI is generated.
     * 
     * Note, that this method invoked twice with the same URI produces different
     * results. Duplicate checking should be performed outside of this method.
     * 
     * @param uri The page URI
     * @return rewritten URI
     */
    private URI rewritePageURI(URI uri) {
        return rewriteURI(uri);
    }
    
    /**
     * Unconditionally rewrites the URI.
     * 
     * If specified URI is the first URI on the pageURIs list, it's rewritten to
     * "index.html". Otherwise, a unique URI is generated.
     * 
     * Note, that this method invoked twice with the same URI produces different
     * results. Duplicate checking should be performed outside of this method.
     * 
     * @param uri The URI to rewrite.
     * @return rewritten URI.
     */
    private URI rewriteURI(URI uri) {
        String rewrittenURIString;

        if ((firstPageURI != null) && firstPageURI.equals(uri)) {
            // If this is the URI of a start page, rewrite it to "index.html"
            // file placed in top-level directory.
            rewrittenURIString = "index.html";
            
        } else {
            // Step 2: Encode the URI using next number, encoded with Radix-36 encoder.
            rewrittenURIString = Long.toString(nextRewrittenURINumber++, 36);

            // If, by accident, encoded URI is "index" which is reserved for the
            // first page, encode the URI using next number.
            if (rewrittenURIString.equals("index")) {
                rewrittenURIString = Long.toString(nextRewrittenURINumber++, 36);
            }
            
            // Step 3: Extract URI extension, including the dot character and
            // append it to the excoded URI. Additionally, convert '.xdime'
            // extension in '.html'.
            String path = uri.getPath();

            if (path != null) {
                int lastDotIndex = path.lastIndexOf('.');

                // If the dot was found, and it is not the last character in the
                // path,
                // extract the extension from the path, including the dot
                // character.
                if ((lastDotIndex != -1) && (lastDotIndex < path.length() - 1)) {
                    String extension = path.substring(lastDotIndex);

                    // Convert xdime extension into html.
                    if (extension.equals(".xdime")) {
                        extension = ".html";
                    }

                    rewrittenURIString += extension;
                }
            }
        }

        // Now, after we got the rewritten URI string, convert it back to the
        // java.set.URI instance.
        URI rewrittenURI;

        try {
            rewrittenURI = new URI(rewrittenURIString);
        } catch (URISyntaxException e) {
            // This should never happen, since the valid URI is prepared in
            // this method. If it unlikely happens, it means that this
            // method needs to be fixed to prepare a valid URI.
            throw new RuntimeException("Invalid URI after rewriting.");
        }

        // Return possibly rewritten URI.
        return rewrittenURI;
    }
    
    /**
     * Returns the URI path of the request.
     * 
     * @param requestContext The request context.
     * @return the URI path.
     */
    private URI getRequestPathURI(MarinerRequestContext requestContext) {
        // Get the absolute filename of the currently requested page (as String)
        MarinerPageContext pageContext = ContextInternals.getMarinerPageContext(requestContext);
        
        String requestFileString = pageContext.getRequestURL(false).getFile();

        // Convert the string to a File instance.
        File requestFile = new File(requestFileString);

        // Get the path component of the filename (as String).
        String requestPathString = requestFile.getParent() + "/";

        // Return an URI instance with path component.
        try {
            return new URI(requestPathString);
        } catch (URISyntaxException e) {
            // This should never happen, since each valid URL is a valid URI.
            throw new RuntimeException("Invalid relative request URI.");
        }
    }
    
    /**
     * Resolves and normalizes the URI with the base request URI.
     * 
     * @param requestContext The request context.
     * @param uri The URI to resolve and normalize.
     * @return resolved and normalized URI.
     */
    private URI resolveAndNormalizeURI(
            MarinerRequestContext requestContext, URI uri) {
        // Rewrite the URI, having the absolute request path provided. 
        URI requestPathURI = getRequestPathURI(requestContext);        
        
        return requestPathURI.resolve(uri);        
    }

    /**
     * Strips the fragment component from the URI.
     * 
     * @param uri The URI to strip the fragment component from.
     * @return the URI without the fragment component.
     */
    private URI stripFragmentComponent(URI uri) {
        try {
            // Use the constructor, which takes scheme, scheme-specific part and
            // fragment components, providing all but fragment component.            
            return new URI(uri.getScheme(), getURISchemeSpecificPart(uri), null);
            
        } catch (URISyntaxException e) {
            // This should never happen
            throw new IllegalStateException("Can not strip fragment component from URI.");
        }
    }
    
    /**
     * Appends the fragment component from one URI to the other.
     * 
     * @param uriNoFragment The URI without the fragment component.
     * @param uri The URI to take fragment component from.
     * @return The URI with appended fragment component.
     */
    private URI appendFragmentComponent(URI uriNoFragment, URI uri) {
        try {
            // Use the constructor, which takes scheme, scheme-specific part and
            // fragment components, providing all but fragment component.
            return new URI(
                    uriNoFragment.getScheme(), 
                    getURISchemeSpecificPart(uriNoFragment), 
                    uri.getFragment());
            
        } catch (URISyntaxException e) {
            // This should never happen
            throw new IllegalStateException("Cannot append fragment component to URI " + uriNoFragment);
        }
    }
    
    /**
     * Returns scheme specific part of an URI.
     * 
     * @param uri The URI.
     * @return The scheme specific part.
     */
    private String getURISchemeSpecificPart(URI uri) {
        // There's a bug in Java 1.4.2 (4866303).
        // The getSchemeSpecificPart() method should return unquoted string,
        // while it returns the quoted one. That's why it needs to be unquoted
        // explicitely. The bug is fixed in Java 1.5.
        String schemeSpecificPart = uri.getSchemeSpecificPart();
        
        if (System.getProperty("java.version").startsWith("1.4")) {
            try {
                schemeSpecificPart = URLDecoder.decode(schemeSpecificPart, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                // Nothing we can do here
                throw new RuntimeException(e);
            }
        }
        
        return schemeSpecificPart;
    }
    
    /**
     * Encodes a string for use within a URL.
     * Encoded string contains only valid URL characters.
     * 
     * @param string The string to encode.
     * @return the encoded string.
     */
    private String encode(String string) {
        // Step 1: Encode the string using Base64 encoding.
        // The false parameter indicates, that no line breaks are to be inserted
        // every 80 characters.
        StringBuffer stringBuffer = new StringBuffer(Base64.encodeString(string, false));
        
        // Step 2: Convert encoded Base64 string from standard alphabet to the
        // "Filename Safe Alphabet" (see: RFC 3548). Following rules will apply:
        //  - convert all '+' characters to '-'
        //  - convert all '/' characters to '_'
        //  - trim all trailing '=' characters
        for (int index = 0; index < stringBuffer.length(); index++) {
            char ch = stringBuffer.charAt(index);
            
            if (ch == '+') {
                stringBuffer.setCharAt(index, '-');
                
            } else if (ch == '/') {
                stringBuffer.setCharAt(index, '_');
                
            } else if (ch == '=') {
                // The '=' character may appear only at the end of the string,
                // so setting the new length will be enough to trim those.
                // The loop will finish after setting the length, which is equal
                // to index.
                stringBuffer.setLength(index);
            }
        }
        
        return stringBuffer.toString();
    }
    
    /**
     * A PageURLRewriter for use within this context.
     */
    private final class PrerendererPageURLRewriter implements PageURLRewriter {
        // Javadoc inherited
        public MarinerURL rewriteURL(
                MarinerRequestContext requestContext, 
                MarinerURL url,
                PageURLDetails details) {
            
            // Create and instance of java.set.URI from MarinerURL.
            URI uri;

            try {
                // Need to create URI from components, since the single-argument
                // constructor expects the URI string to be already quoted, while
                // the other constructors will quote the components automatically.
                //uri = new URI(url.getProtocol(), url.getAuthority(), url.getPath(), url.getQuery(), url.getReference());
                
                uri = new URI(url.getExternalForm());
            } catch (URISyntaxException e) {
                // This should never happen, because each MarinerURL is a valid URI.
                throw new RuntimeException("Error creating URI from MarinerURL "
                        + url.getExternalForm(), e);
            }

            // Rewrite the URI
            uri = rewriteURI(requestContext, uri, details.getPageURLType());

            // Convert back rewritten URI into MarinerURL instance.
            // TODO: Optimise this code in case we have writable (non read-only)
            // instance of MarinerURL - there's no need to create a new instance in
            // that case.
            url = new MarinerURL(uri.toString());
            
            return url;
        }
    }
}
