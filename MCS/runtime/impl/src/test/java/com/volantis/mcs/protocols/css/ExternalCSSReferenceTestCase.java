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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.css;

import com.volantis.mcs.cache.CacheTimeMock;
import com.volantis.mcs.cache.CacheManagerMock;
import com.volantis.mcs.cache.CacheStoreMock;
import com.volantis.mcs.cache.css.CSSCache;
import com.volantis.mcs.cache.impl.DefaultCacheStore;
import com.volantis.mcs.context.MarinerPageContextMock;
import com.volantis.mcs.context.MarinerRequestContextMock;
import com.volantis.mcs.context.EnvironmentContextMock;
import com.volantis.mcs.context.ResponseCachingDirectives;
import com.volantis.mcs.protocols.css.reference.ExternalCSSReference;
import com.volantis.mcs.protocols.html.XHTMLBasicCSSModule;
import com.volantis.mcs.protocols.OutputBufferMock;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.assets.implementation.AssetResolverMock;
import com.volantis.mcs.runtime.StyleSheetConfiguration;
import com.volantis.mcs.runtime.URLRewriterManager;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.mcs.integration.URLRewriter;
import com.volantis.mcs.integration.PageURLType;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.shared.system.SystemClock;

public class ExternalCSSReferenceTestCase extends TestCaseAbstract {
    private CSSModule cssModule;
    private CSSCache cssCache;
    private StyleSheetConfiguration cfg;
    private MarinerURL contextPathURL;
    private MarinerPageContextMock marinerpageContext;
    private CacheManagerMock cacheManager;
    private ResponseCachingDirectives cachingDirectives;
    private URLRewriter sessionURLRewriter;

    /**
     * Test the external css generation when the configuration has 'session' type
     * set.
     */
    public void testMarkupWithSessionTypeSet() throws Exception {

        cfg = new StyleSheetConfiguration();
        cfg.setCssSessionType("include-id-in-url");
        contextPathURL = new MarinerURL("/mariner/url");

        final EnvironmentContextMock environmentContextMock =
            new EnvironmentContextMock("environmentContextMock", expectations);

        cachingDirectives = new ResponseCachingDirectives(SystemClock.getDefaultInstance());
        cachingDirectives.enable();
        assertTrue(cachingDirectives.isEnabled());

        environmentContextMock.expects.getCachingDirectives().returns(
            cachingDirectives).any();

        sessionURLRewriter = new URLRewriterManager.DefaultURLRewriter();
        environmentContextMock.expects.getSessionURLRewriter().returns(
                sessionURLRewriter).any();

        MarinerRequestContextMock requestContext =
                new MarinerRequestContextMock("requestContext", expectations);

        marinerpageContext = new MarinerPageContextMock("mpcID", expectations);

        marinerpageContext.expects.getSessionURLRewriter().returns(
                sessionURLRewriter).any();

        marinerpageContext.expects.getEnvironmentContext().returns(
            environmentContextMock).any();

        marinerpageContext.expects.getRequestContext()
                .returns(requestContext).any();

        AssetResolverMock assetResolver = new AssetResolverMock(
                "assetResolverMock", expectations);

        marinerpageContext.expects.getAssetResolver()
                .returns(assetResolver).any();

        marinerpageContext.expects.getEnvironmentContext()
                .returns(null).any();

        final String result = "/mariner/CSSServlet?key=MTE5Mzc1Mjk0Njk5Ny0xMTkzNzUyOTQ2OTk3LTA%3D";

        cssModule = new XHTMLBasicCSSModule(marinerpageContext);
        cacheManager = new CacheManagerMock("cacheManagerMock", expectations);
        cssCache = new CSSCache(0);
        
        ExternalCSSReference ref = new ExternalCSSReference(
                cssModule, cssCache, cfg, contextPathURL, marinerpageContext);
        String css = "p { background-color:red;}";

        marinerpageContext.expects.getSessionURLRewriter()
                .returns(sessionURLRewriter).any();

        ref.writePlaceHolderMarkup(new DOMOutputBuffer());

        assetResolver.fuzzy.rewriteURLWithPageURLRewriter(mockFactory.expectsAny(),
                                                          PageURLType.STYLE_SHEET)
                .returns(result).any();
        
        ref.updateMarkup(css);
    }
}
