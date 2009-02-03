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
package com.volantis.synergetics.url;

/**
 * This class implements the {@link URLRewriteRule} in order to rewrite the
 * prefix of a url. It has three fixed variables: a currentURL, a newURL and an
 * operation to apply to the url whose prefix to change that not only
 * designates the kind of rewrite operation but also performs the operation.
 *
 * If the currentURL is something other than null then it must match the prefix
 * of the url to change since it is describing what the prefix is in the url to
 * change.
 */
public class URLPrefixRewriteRule implements URLRewriteRule {

    /**
     * The current url e.g. for replacing and removing url prefixes
     */
    private final String currentURL;

    /**
     * The new url e.g. for replacing and adding url prefixes
     */
    private final String newURL;

    /**
     * The operation to apply to rewrite a url.
     */
    private final URLPrefixRewriteOperation rewriteOperation;

    /**
     * Construct a new URLPrefixRewriteRule.
     *
     * @param currentURL       the current url e.g. for replacing and removing
     *                         url prefixes. Can be null if the rewrite rule is
     *                         ADD_PREFIX.
     * @param newURL           the new url e.g. for replacing and adding url
     *                         prefixes. Can be null if the rewrite rule is
     *                         REMOVE_PREFIX.
     * @param rewriteOperation the URLPrefixRewriteOperation to apply. Cannot
     *                         be null.
     * @throws IllegalArgumentException if a required parameter value is null.
     */
    public URLPrefixRewriteRule(String currentURL, String newURL,
                                URLPrefixRewriteOperation rewriteOperation) {
        if (rewriteOperation == null) {
            throw new IllegalArgumentException("Cannot be null: " +
                                               "rewriteOperation");
        }

        if (currentURL == null && !rewriteOperation.
            equals(URLPrefixRewriteOperation.ADD_PREFIX)) {
            throw new IllegalArgumentException("The currentURL argument " +
                                               "cannot be null unless the rewriteOperation is " +
                                               "ADD_PREFIX");
        }

        if (newURL == null && !rewriteOperation.
            equals(URLPrefixRewriteOperation.REMOVE_PREFIX)) {
            throw new IllegalArgumentException("The newURL argument " +
                                               "cannot be null unless the rewriteOperation is " +
                                               "REMOVE_PREFIX");
        }

        this.currentURL = currentURL;
        this.newURL = newURL;
        this.rewriteOperation = rewriteOperation;
    }

    // javadoc inherited
    public String execute(String url, String contextURL) {
        return rewriteOperation.performOperation(currentURL,
                                                 newURL,
                                                 contextURL,
                                                 url);
    }

    public String toString() {
        return "[" + getClass().getName() + ": currentURL=" + currentURL
                + ", newURL=" + newURL
                + ", rewriteOperation=" + rewriteOperation
                + "]";
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 04-Oct-04	320/1	doug	VBM:2004092306 Allowed URLRewriteRules to be added that are not bound to a prefix

 01-Oct-04	318/1	doug	VBM:2004092306 Allowed URLRewriteRules to be added that are not bound to a prefix

 01-Sep-04	254/1	doug	VBM:2004052801 Fixed problem with REMOVE_PREFIX URLPrefixRewriteOperation

 25-May-04	227/1	allan	VBM:2004052103 Provide a general url prefix rewriting facility

 ===========================================================================
*/
