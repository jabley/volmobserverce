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

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Manage url prefix rewriting for sets of urls.
 */
public class URLPrefixRewriteManager {

    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(
                    URLPrefixRewriteManager.class);

    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(
                    URLPrefixRewriteManager.class);

    /**
     * The SortedURLTreeMap that stores the URLPrefixRewriteRules keyed on
     * urls. The SortedURLTreeMap allows us always to obtain the most specific
     * matched url from the map.
     */
    private final SortedURLTreeMap urlTreeMap = new SortedURLTreeMap();

    /**
     * In the case where the currentURL is nothing (i.e. null or "") this
     * implies that the matching rule should be for all relative urls. As such
     * there can only be one such rule since it matches all relative urls.
     * Additionally the only kind of operation we can perform is ADD_PREFIX.
     * This field is the rule for all relative urls.
     */
    private URLRewriteRule relativeRule;

    /**
     * An array of URLRewriteRules that are not bound to any url.
     */
    private List unboundRules;

    /**
     * Add a rewritable url prefix (i.e. the details that enable such a rewrite
     * to be done) to this URLPrefixRewriteManager.
     *
     * @param currentURLPrefix a current prefix that may be removed or replaced
     *                         depending on the operation. This can only be
     *                         null if the operation is ADD_PREFIX. Note there
     *                         can only be a single rewriteable url prefix that
     *                         has a null currentURLPrefix. This is because a
     *                         null currentURLPrefix indicates that the
     *                         operation is adding a prefix to a relative url.
     *                         There is no mechanism to differentiate between
     *                         different relative urls.
     * @param newURLPrefix     a url prefix that may be added to or become the
     *                         replacement prefix depending on the operation.
     *                         This can only be null if the operation is
     *                         REMOVE_PREFIX.
     * @param operation        the URLPrefixRewriteOperation to apply when a
     *                         url that is selected based on currentURLPrefix
     *                         is found.
     */
    public void addRewritableURLPrefix(String currentURLPrefix,
                                       String newURLPrefix,
                                       URLPrefixRewriteOperation operation) {

        URLRewriteRule rule = new URLPrefixRewriteRule(currentURLPrefix,
                                                       newURLPrefix,
                                                       operation);

        if (currentURLPrefix == null || currentURLPrefix.length() == 0) {
            relativeRule = rule;
        } else {
            urlTreeMap.put(currentURLPrefix, rule);
        }
    }

    /**
     * Registers the array URLRewriteRules with this manager. These rules will
     * only be executed for a particular URL if a {@link URLRewriteRule} has
     * not been registered for the URL via the {@link #addRewritableURLPrefix}
     * method.
     *
     * Unlike rules created by the URLPrefixRewriteManager these rules are not
     * bound to any particular url. If a matching rule is not found among all
     * of the rules created by calls to addRewriteableURLPrefix then each of
     * the rules given here will be executed in turn. The first rule to return
     * a url different from the target url (i.e. the original url in the page)
     * is considered to be the matching rule and the url returned by it will be
     * used in the page in place of the target url.
     *
     * @param rules the array of {@link URLRewriteRule} that are to be added.
     *              Cannot be null.
     */
    public void addRewritableRules(URLRewriteRule[] rules) {
        if (rules == null) {
            throw new IllegalArgumentException("rules cannot be null");
        }
        if (unboundRules == null) {
            unboundRules = new ArrayList(rules.length);
        }
        unboundRules.addAll(Arrays.asList(rules));
    }

    /**
     * Remove a rewritable url prefix from this URLPrefixRewriteManager. If no
     * corresponding rewritable url is found then there are no changes.
     *
     * Currently this method is unsupported because there is no existing way to
     * remove items from a SortedURLTreeMap.
     *
     * @param currentURLPrefix the key for the url prefix to remove which is
     *                         the same as the currentURLPrefix that was used
     *                         to add the url to this URLPrefixRewriteManager.
     * @throws UnsupportedOperationException every time this method is called.
     */
    public void removeRewritableURLPrefix(String currentURLPrefix) {
        throw new UnsupportedOperationException("Method not supported.");
    }

    /**
     * Search for a matching rewrite rule for the given url. Then execute the
     * rewrite rule and return the result. If no rewrite rule corresponding
     * rule is found then the  given url is returned as is. Rules that have
     * been registered via the {@link #addRewritableURLPrefix} method are
     * executed before those that were registered via the {@link
     * #addRewritableRules} method.
     *
     * @param url        the url upon which to find a rewrite rule for and
     *                   execute the rewrite against.
     * @param contextURL the contextURL for the rule.
     * @return the original url with its prefix rewritten or just the original
     *         url if no rewrite rule could be found
     */
    public String findAndExecuteRule(String url, String contextURL) {
        URLRewriteRule rule;
        if (relativeRule != null && new URLIntrospector(url).isRelative()) {
            rule = relativeRule;
        } else {
            rule = (URLRewriteRule) urlTreeMap.get(url);
        }
        if (rule != null) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Found rule: url=" + url
                        + ", contextURL=" + contextURL
                        + ", rule=" + rule.toString());
            }
            url = rule.execute(url, contextURL);
        } else if (unboundRules != null) {
            if (null == url) {
                throw new IllegalArgumentException(
                        EXCEPTION_LOCALIZER.format("illegal-arg-value-null"));
            }

            String rewrittenURL = null;
            // see if there is an "unbound" rule that can handle this url
            for (int i = 0; i < unboundRules.size() && rewrittenURL == null;
                 i++) {
                String result = ((URLRewriteRule) unboundRules.get(i)).
                    execute(url, contextURL);
                if (!url.equals(result)) {

                    // we have a rule that has rewritten the url

                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Unbound URLRewriteRule found. "
                                + url + " -> " + result);
                    }
                    rewrittenURL = result;
                    url = rewrittenURL;
                }
            }
        }
        return url;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 23-Dec-04	361/4	allan	VBM:2004122108 Support multiple calls to addRewritableRules()

 23-Dec-04	361/2	allan	VBM:2004122108 Support multiple calls to addRewritableRules()

 04-Oct-04	320/3	doug	VBM:2004092306 Allowed URLRewriteRules to be added that are not bound to a prefix

 04-Oct-04	320/1	doug	VBM:2004092306 Allowed URLRewriteRules to be added that are not bound to a prefix

 01-Oct-04	318/2	doug	VBM:2004092306 Allowed URLRewriteRules to be added that are not bound to a prefix

 01-Sep-04	254/1	doug	VBM:2004052801 Fixed problem with REMOVE_PREFIX URLPrefixRewriteOperation

 26-May-04	246/1	allan	VBM:2004052102 Provide a relative url rule.

 25-May-04	227/1	allan	VBM:2004052103 Provide a general url prefix rewriting facility

 ===========================================================================
*/
