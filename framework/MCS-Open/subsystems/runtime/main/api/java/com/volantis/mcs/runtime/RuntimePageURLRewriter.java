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
package com.volantis.mcs.runtime;

import com.volantis.mcs.application.MarinerApplication;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.integration.PageURLDetails;
import com.volantis.mcs.integration.PageURLRewriter;
import com.volantis.mcs.utilities.MarinerURL;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * A PageURLRewriter implementation suitable for the runtime environment.
 * What this means is that instances of this class will always provide a
 * PageURLRewriter even if that rewriter does no url rewriting.
 */
public class RuntimePageURLRewriter implements PageURLRewriter {

    /**
     * The underlying PageURLRewriter that this
     * RuntimePageURLRewriter delegates to.
     */
    private final PageURLRewriter pageURLRewriter;

    /**
     * Initialize a new RuntimePageURLRewriter that will delegate to an
     * instance of a given PageURLRewriter class. If no such instance
     * is available then a default PageURLRewriter will be provided
     * that does no url rewriting.
     *
     * @param rewriterClassName The fully qualified class name of the
     * PageURLRewriter that this RuntimePageURLRewriter should
     * delegate to. Can be null and if so a defatul PageURLRewriter that
     * does not do any url rewriting will be provided.
     * @param application The current MarinerApplication. If the specified
     * rewriterClass had a constructor that whose signature has a single
     * argument that is a MarinerApplication then that constructor will be
     * used to construct the PageURLRewriter using this argument.
     * @throws ClassNotFoundException If the named class cannot be found.
     * @throws InstantiationException If the named class cannot be
     * instantiated.
     * @throws IllegalAccessException If the constructor for the named class
     * is not accessible to this method.
     * @throws InvocationTargetException If the Constructor associated with
     * the rewriterClassName could not be invoked.
     * @throws IllegalArgumentException If rewriterClassName is not null and
     * not and does not implement PageURLRewriter.
     */
    public RuntimePageURLRewriter(String rewriterClassName,
                                  MarinerApplication application)
            throws ClassNotFoundException, InstantiationException,
            IllegalAccessException, InvocationTargetException {
        if (rewriterClassName == null) {
            pageURLRewriter = new DefaultNavigationURLRewriter();
        } else {
            Class rewriterClass = Class.forName(rewriterClassName);
            if (!PageURLRewriter.class.isAssignableFrom(rewriterClass)) {
                throw new IllegalArgumentException("Expected a kind of " +
                        "PageURLRewriter for class: " +
                        rewriterClassName);
            }
            pageURLRewriter = createRewriter(rewriterClass, application);
        }
    }

    /**
     * In the default case when the user has not provided their own
     * PageURLRewriter the RuntimePageURLRewriter will delegate to a
     * DefaultNavigationURLRewriter which does not rewrite the url. In
     * some instances it is useful for the caller to know if the
     * RuntimePageURLRewriter is delegating to a user provided PageURLRewriter.
     * This method provides this feature.
     *
     * @return true if this RuntimePageURLRewriter is delegating to a user
     * provided PageURLRewriter; false otherwise.
     */
    public boolean isUserDefinedRewriter() {
        return !(pageURLRewriter instanceof DefaultNavigationURLRewriter);
    }

    // javadoc inherited
    public MarinerURL rewriteURL(MarinerRequestContext context,
                                 MarinerURL url,
                                 PageURLDetails details) {
        return pageURLRewriter.rewriteURL(context, url, details);
    }

    /**
     * Create an instance of the specified rewriterClass.
     *
     * @param rewriterClass the rewriter Class
     * @return an instance of the specified PageRewriter class. If there is a
     * Constructor in the rewriter class that has a single parameter that
     * is a MarinerApplicationContext then this Constructor will be returned;
     * otherwise the default Constructor will be returned.
     * @throws InstantiationException If the named class cannot be
     * instantiated.
     * @throws IllegalAccessException If the constructor for the named class
     * is not accessible to this method.
     * @throws InvocationTargetException If the Constructor associated with
     * the rewriterClassName could not be invoked.
     */
    private static PageURLRewriter createRewriter(Class rewriterClass,
                                                 MarinerApplication marinerApplication)
            throws InvocationTargetException, InstantiationException,
            IllegalAccessException {
        Constructor constructors [] = rewriterClass.getConstructors();
        PageURLRewriter rewriter = null;
        Constructor defaultConstructor = null;
        for (int i = 0; i < constructors.length && rewriter == null; i++) {
            Class paramTypes [] = constructors[i].getParameterTypes();
            switch (paramTypes.length) {
                case 0:
                    defaultConstructor = constructors[i];
                    break;
                case 1:
                    if (paramTypes[0] == MarinerApplication.class) {
                        rewriter = (PageURLRewriter) constructors[i].
                                newInstance(new Object[]{marinerApplication});
                    }
                    break;
            }
        }

        return rewriter != null ? rewriter :
                (PageURLRewriter) defaultConstructor.newInstance(null);
    }

    /**
     * A PageURLRewriter that does not rewrite the url.
     */
    private static class DefaultNavigationURLRewriter
            implements PageURLRewriter {
        // javadoc inherited
        public MarinerURL rewriteURL(MarinerRequestContext context,
                                     MarinerURL url,
                                     PageURLDetails details) {
            return url;
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 01-Jul-04	4778/1	allan	VBM:2004062912 Use the Volantis.pageURLRewriter to rewrite page urls

 28-Jun-04	4733/1	allan	VBM:2004062105 Convert Volantis to use the new PageURLRewriter

 ===========================================================================
*/
