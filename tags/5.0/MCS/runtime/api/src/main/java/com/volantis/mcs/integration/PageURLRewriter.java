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
 * (c) Volantis Systems Ltd 2004. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.integration;

import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.mcs.context.MarinerRequestContext;

/**
 * Allows customers to rewrite URLs before they are written to the page.
 *
 * <p>Note: The rewriting of URLs from assets is mainly handled by the
 * {@link AssetURLRewriter}, the exception is
 * {@link com.volantis.mcs.assets.LinkAsset}s as they can be rewritten by
 * both rewriters. i.e. If an &lt;a&gt; element's href attribute references a
 * link asset then the URL for that will be passed to the
 * {@link AssetURLRewriter} and the result of that will then be passed to the
 * <code>PageURLRewriter</code> with a type of {@link PageURLType#ANCHOR}.</p>
 *
 * <h2>Construction</h2>
 *
 * <p>Implementations of this interface must provide either a default
 * constructor, or a constructor that takes a
 * {@link com.volantis.mcs.application.MarinerApplication}. The latter
 * constructor will be used if present, otherwise the default constructor will
 * be used. The order of creation of customer classes is undefined and so
 * implementation must not rely on being created before, or after other
 * classes.</p>
 *
 * <p>Note: It is not possible to use a non static inner class as the rewriter
 * as its constructors require an additional parameter that is an instance of
 * the containing class.</p>
 *
 * <h2>Rewriting Constraints</h2>
 *
 * <p>Implementations must not rely on the supplied URLs having a particular
 * structure (e.g. a specific parameter) as that will change in future.</p>
 *
 * <p>There is a very important relationship between URLs within a single
 * page / request that must be maintained after rewriting. This relationship is
 * defined as follows. Given two URLs <code>A</code> and <code>B</code> that
 * are rewritten to <code>A'</code> and <code>B'</code> respectively. Then if
 * <code>length(A) &lt;= length(B)</code> then
 * <code>length(A') &lt;= length(B')</code>. Failure to maintain this
 * relationship could cause pages to fail to render properly on certain
 * devices.</p>
 *
 * <p>e.g. a rewriter that added (or removed) the same number of characters to
 * the all URLs within the page would satisfy this relationship but one that
 * added (or removed) a different number of characters would not. Also, a
 * rewriter that made all the URLs the same length would satisfy this
 * relationship.</p>
 *
 * <h2>Supporting Infrastructure</h2>
 *
 * <p>It is the customers responsibility to provide any infrastructure
 * (servlets, etc) necessary to serve any requests to the rewritten URLs.</p>
 *
 * <p>e.g. If this rewriter is being used to compress the URLs then it could
 * store the original and compressed URLs into a database. The customer
 * would have to provide a servlet (or equivalent mechanism) that was the
 * destination of the compressed URLs. This servlet would then retrieve the
 * original URL from the database using the compressed URL as the key and then
 * redirect the request to that URL.</p>
 *
 * @mock.generate 
 */
public interface PageURLRewriter {

    /**
     * Rewrite the supplied URL.
     *
     * <p>The supplied URL may be marked as read only in which case this method
     * must copy it before making any changes. If it is not so marked then this
     * method may make it read only. If necessary the returned URL may also be
     * marked as read only.</p>
     *
     * <p>The normal reason for making a URL read only is because it is to be
     * stored in a cache for reuse later in which case allowing it to be
     * modified could invalidate the cache contents.</p>
     *
     * <p>The supplied URL may be document relative, host relative, or absolute
     * and it is the responsibility of this method to deal with each type
     * appropriately. The returned URL may also be in any of those forms and
     * again it is the responsibility of this method to ensure that relative
     * URLs resolve to the appropriate absolute URL.</p>
     *
     * @param context The context of the current request.
     * @param url The url that needs rewriting, may not be null.
     * @param details Information about the use that will be made of the url
     * that may affect how it is rewritten.
     * @return The url to add to the page, may not be null.
     */
    public MarinerURL rewriteURL(MarinerRequestContext context,
                                 MarinerURL url,
                                 PageURLDetails details);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 12-Jul-04	4824/3	pduffin	VBM:2004062102 Updated documentation for PageURLRewriter and related classes

 07-Jul-04	4824/1	pduffin	VBM:2004062102 Documented PageURLRewriter and related classes

 28-Jun-04	4733/2	allan	VBM:2004062105 Convert Volantis to use the new PageURLRewriter

 21-Jun-04	4728/1	allan	VBM:2004062101 Classes and interfaces for general url rewriting.

 ===========================================================================
*/
