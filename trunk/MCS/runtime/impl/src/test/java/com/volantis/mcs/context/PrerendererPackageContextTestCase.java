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

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.volantis.mcs.integration.PageURLType;
import com.volantis.mcs.runtime.PageURLDetailsFactory;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Test case for PrerendererPackageContext.
 */
public class PrerendererPackageContextTestCase extends TestCaseAbstract {
    private PrerendererPackageContext prerendererPackageContext;
    
    private MarinerRequestContextMock marinerRequestContextMock;
    
    private MarinerPageContextMock marinerPageContextMock;

    private URI baseURI;
    
    private List pageURIs;
    
    private boolean requestURIExpectationSet;

    private URI prefixPathURI;

    /**
     * Tests, that rewriting an URI of the first page from the list gives
     * 'index.html'.
     */
    public void testRewriteIncludedStartPageURI() throws Exception {
        initializeTest();
        
        assertEquals("index.html", rewrite(getPageURI(0), PageURLType.ANCHOR));
    }
    
    /**
     * Tests, that rewriting each page URI from the list really rewrites it to
     * local URI.
     */
    public void testRewriteIncludedNonStartPageURI() throws Exception {
        initializeTest();
        
        for(int index = 0; index < pageURIs.size(); index++) {
            assertRewriteToLocal(getPageURI(index), PageURLType.ANCHOR);
        }
    }
    
    /**
     * Tests, that rewriting page URI not from the list does rewrite it to the
     * production URI.
     */
    public void testRewriteNotIncludedPageURI() throws Exception {
        initializeTest();
        
        assertRewriteToProduction("non-listed-page.xdime", PageURLType.ANCHOR);
    }
    
    /**
     * Tests, that locally rewritten URI preserves extension.
     */
    public void testRewritePreservesExtension() throws Exception {
        initializeTest();
        
        assertTrue(rewrite("logo.jpg", PageURLType.IMAGE).endsWith(".jpg"));
    }
    
    /**
     * Tests, that locally rewritten page URI replaces extension from .xdime to
     * .html.
     */
    public void testRewritePageUsesHTMLExtension() throws Exception {
        initializeTest();
        
        assertTrue(rewrite(getPageURI(1), PageURLType.ANCHOR).endsWith(".html"));
    }
    
    /**
     * Tests, that the locally rewritten URI preserve the fragment component.
     */
    public void testRewritePreservesFragment() throws Exception {
        initializeTest();
        
        assertTrue(rewrite(getPageURI(1) + "#fragment-1", PageURLType.ANCHOR).endsWith("#fragment-1"));
    }

    /**
     * Tests, that the two URIs which differs only by fragment component are
     * rewritten to the same URIs, differing only by fragment component.
     */
    public void testRewriteSameURIDifferentFragment() throws Exception {
        initializeTest();
        
        String rewritten1 = rewrite(getPageURI(1) + "#fragment-1", PageURLType.ANCHOR);
        String rewritten2 = rewrite(getPageURI(1) + "#fragment-2", PageURLType.ANCHOR);

        String rewritten1NoFragment = rewritten1.substring(0, rewritten1.lastIndexOf("#"));
        String rewritten2NoFragment = rewritten2.substring(0, rewritten2.lastIndexOf("#"));
        
        assertTrue(rewritten1NoFragment.equals(rewritten2NoFragment));
    }

    /**
     * Tests, that URI are normalized before rewriting. Having two
     * relative URIs on two different pages refering the same resource,
     * rewritten URI should be the same.
     */
    public void testNormalizationToLocal() throws Exception {
        initializeTest();
        
        String rewritten1 = rewrite("help/index.xdime", PageURLType.ANCHOR);

        String rewritten2 = rewrite("help/A/../index.xdime", PageURLType.ANCHOR);
        
        assertEquals(rewritten1, rewritten2);
    }
    
    /**
     * Tests, that getIncrementalRewrittenURIMap() does not return duplicates.
     */
    public void testIncrementalDuplicates() throws Exception {
        initializeTest();
        
        rewrite("logo.jpg", PageURLType.IMAGE);
        
        assertEquals(1, prerendererPackageContext.getIncrementalRewrittenURIMap().size());

        rewrite("logo.jpg", PageURLType.IMAGE);
        
        assertEquals(0, prerendererPackageContext.getIncrementalRewrittenURIMap().size());
    }

    /**
     * Tests, that getIncrementalRewrittenURIMap() does not include page URIs from the list.
     */
    public void testIncrementalNotIncludesListedPage() throws Exception {
        initializeTest();
        
        rewrite(getPageURI(0), PageURLType.ANCHOR);

        rewrite("logo.jpg", PageURLType.IMAGE);
        
        assertEquals(1, prerendererPackageContext.getIncrementalRewrittenURIMap().size());
    }

    /**
     * Tests, that getIncrementalRewrittenURIMap() does not include page URIs not from the list.
     */
    public void testIncrementalIncludesNonListedPage() throws Exception {
        initializeTest();
        
        rewrite("non-listed-page.xdime", PageURLType.ANCHOR);
        
        assertEquals(0, prerendererPackageContext.getIncrementalRewrittenURIMap().size());
    }

    /**
     * Tests, that two URIs, which differs only by fragment component are treat
     * as one URI.
     */
    public void testIncrementalFragments() throws Exception {
        initializeTest();
        
        rewrite("icon.jpg#fragment-1", PageURLType.IMAGE);

        rewrite("icon.jpg#fragment-2", PageURLType.IMAGE);
        
        assertTrue(prerendererPackageContext.getIncrementalRewrittenURIMap().size() == 1);
    }
    
    /**
     * Tests, that rewritten URL returned by getRewrittenPageURIs() method are
     * the same, as if the URL were explicitely rewritten using rewriteURL()
     * method.
     */
    public void testRewrittenPageURIs() throws Exception {
        initializeTest();
        
        List rewrittenPageURIs = prerendererPackageContext.getRewrittenPageURIs();
        
        // The size should equal
        assertEquals(pageURIs.size(), rewrittenPageURIs.size());
        
        // And each element should equal to the rewritten one.
        for (int index = 0; index < pageURIs.size(); index++) {
            assertEquals(rewrite(getPageURI(index), PageURLType.ANCHOR), 
                    prerendererPackageContext.getRewrittenPageURIs().get(index).toString());
        }
    }
    
    /**
     * @inheritDoc
     */
    protected void setUp() throws Exception {
        super.setUp();
        
        pageURIs = new ArrayList();
        
        marinerRequestContextMock = new MarinerRequestContextMock(
                "marinerRequestContextMock", expectations);

        marinerPageContextMock = new MarinerPageContextMock(
                "marinerPageContextMock", expectations);
                
        // Setup expectations
        marinerRequestContextMock.expects.getMarinerPageContext()
                .returns(marinerPageContextMock).any();
        
        requestURIExpectationSet = false;
    }

    private void addPageURI(String pageURI) throws Exception {
        this.pageURIs.add(new URI(pageURI));
    }
    
    private String getPageURI(int index) {
        return pageURIs.get(index).toString();
    }
    
    private void setBaseURI(String baseURI) throws Exception {
        this.baseURI = new URI(baseURI);
    }
    
    private void setPrefixPathURI(String prefixPathURI) throws Exception {
        this.prefixPathURI = new URI(prefixPathURI);
    }
    
    private void createPrerendererPackageContext() throws Exception {
        if (baseURI == null) {
            setBaseURI("http://www.google.com/gmail/");
        }

        if (prefixPathURI == null) {
            setPrefixPathURI("/apps/gmail-dev/");
        }
        
        if (pageURIs.isEmpty()) {
            addPageURI("welcome.xdime");
            addPageURI("help.xdime");
            addPageURI("help/index.xdime");
            addPageURI("register.xdime?id=12");
        }
        
        prerendererPackageContext = new PrerendererPackageContext(pageURIs, baseURI, prefixPathURI);
    }
    
    private void setRequestURIExpectation(String requestURI) {
        marinerPageContextMock.expects.getRequestURL(false)
            .returns(new MarinerURL(requestURI)).any();
        
        requestURIExpectationSet = true;
    }
    
    private void initializeTest() throws Exception {
        createPrerendererPackageContext();
        
        if (!requestURIExpectationSet) {
            URI requestURI = new URI("http://localhost:8080");
            
            requestURI = requestURI.resolve(prefixPathURI);
            
            requestURI = requestURI.resolve((URI)pageURIs.get(0));
            
            setRequestURIExpectation(requestURI.toString());
        }
    }

    private String rewrite(String uri, PageURLType type) throws Exception {
        return rewrite(new MarinerURL(uri), type).getExternalForm();
    }
    
    private MarinerURL rewrite(MarinerURL url, PageURLType type) {
        return prerendererPackageContext.getPageURLRewriter()
            .rewriteURL(marinerRequestContextMock, url, 
                    PageURLDetailsFactory.createPageURLDetails(type));
    }

    private void assertRewriteToLocal(String uri, PageURLType uriType) throws Exception {
        URI source = new URI(uri);
        
        URI rewritten = new URI(rewrite(uri, uriType));

        // It needs to be relative (not absolute).
        assertTrue("Rewritten URI should be relative", 
                !rewritten.isAbsolute());

        // It can not contain an authority component.
        assertTrue("Rewritten URI should not contain authority component",
                rewritten.getAuthority() == null);

        // It can not contain an authority component.
        String sourceFragment = source.getFragment();
        String rewrittenFragment = rewritten.getFragment();
        assertTrue("Rewritten URI should maintain the fragment component: both must be null or both must be not null.",
                (sourceFragment == null) == (rewrittenFragment == null));
        assertTrue("Rewritten URI should maintain the fragment component: if not null, they must equal.",
                sourceFragment == null || rewrittenFragment == null 
                        || sourceFragment.equals(rewrittenFragment));

        // The file extension should be maintained, with one exception:
        //  - ".xdime" should be converted to ".html".
        String sourcePath = source.getPath();
        
        if (sourcePath != null) {
            int lastDotIndex = sourcePath.lastIndexOf('.');
            
            if (lastDotIndex != -1) {
                String sourceExtension = sourcePath.substring(lastDotIndex);
                String rewrittenPath = rewritten.getPath();
                
                if (sourceExtension.equals(".xdime")) {
                    assertTrue("xdime extension should be converted to html",
                            (rewrittenPath != null) && rewrittenPath.endsWith(".html"));
                } else {
                    assertTrue("Extension should be maintained",
                            (rewrittenPath != null) && rewrittenPath.endsWith(sourceExtension));
                }
            }
        }
    }

    private void assertRewriteToProduction(String uri, PageURLType uriType) throws Exception {
        URI source = new URI(uri);
        
        URI rewritten = new URI(rewrite(uri, uriType));

        // The URI should be resolved against base URI.
        assertEquals(baseURI.resolve(source), rewritten);
    }
}
