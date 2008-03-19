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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.runtime.debug;

import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.WalkingDOMVisitorStub;
import com.volantis.mcs.dom.DOMWalker;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.dom.debug.DocumentStyler;
import com.volantis.mcs.dom.debug.StyledDocumentWriter;
import com.volantis.mcs.dom.debug.DebugCharacterEncoder;
import com.volantis.mcs.dom.output.DOMDocumentOutputter;
import com.volantis.mcs.dom2theme.AssetResolver;
import com.volantis.mcs.dom2theme.ExtractorContext;
import com.volantis.mcs.dom2theme.StyledDOMThemeExtractorFactory;
import com.volantis.mcs.dom2theme.StyledDocumentOptimizer;
import com.volantis.mcs.dom2theme.extractor.ExtractorConfiguration;
import com.volantis.mcs.dom2theme.extractor.ExtractorConfigurationBuilder;
import com.volantis.mcs.dom2theme.extractor.PropertyDetailsSetHelper;
import com.volantis.mcs.expression.PolicyExpression;
import com.volantis.mcs.policies.PolicyReference;
import com.volantis.mcs.policies.variants.metadata.EncodingCollection;
import com.volantis.mcs.protocols.DOMTransformer;
import com.volantis.mcs.protocols.DeferredInheritTransformer;
import com.volantis.mcs.protocols.trans.NullRemovingDOMTransformer;
import com.volantis.mcs.runtime.styling.CSSCompilerBuilder;
import com.volantis.mcs.runtime.styling.StylingFunctions;
import com.volantis.mcs.runtime.themes.ThemeStyleSheetCompilerFactory;
import com.volantis.mcs.themes.MutableShorthandSet;
import com.volantis.mcs.themes.ShorthandSet;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.xdime.XDIMESchemata;
import com.volantis.shared.iteration.IterationAction;
import com.volantis.shared.throwable.ExtendedRuntimeException;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.compiler.CSSCompiler;
import com.volantis.styling.compiler.InlineStyleSheetCompilerFactory;
import com.volantis.styling.compiler.StyleSheetCompilerFactory;
import com.volantis.styling.device.DeviceOutlook;
import com.volantis.styling.engine.StylingEngine;
import com.volantis.styling.properties.PropertyDetailsSet;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.properties.StylePropertyDefinitions;
import com.volantis.styling.properties.StylePropertyIteratee;
import com.volantis.styling.sheet.CompiledStyleSheet;
import org.xml.sax.EntityResolver;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A class to allow easy parsing and rendering of styled DOMs.
 * <p>
 * Useful for integration testing code which uses styled DOMs.
 */
public class StrictStyledDOMHelper {

    private final EntityResolver resolver;

    private StyledDocumentOptimizer optimizer;

    /**
     * Construct a new StrictStyledDOMHelper with a null EntityResolver
     */
    public StrictStyledDOMHelper() {
        this(null);
    }

    public StrictStyledDOMHelper(EntityResolver resolver) {
        this.resolver = resolver;

        StyledDOMThemeExtractorFactory extractorFactory =
                StyledDOMThemeExtractorFactory.getDefaultInstance();

        final AssetResolver assetResolver = new AssetResolver() {

            // javadoc inherited
            public PolicyReference evaluateExpression(PolicyExpression expression) {
                return null;
            }

            // javadoc inherited
            public String resolveImage(PolicyReference reference) {
                return null;
            }

            // javadoc inherited
            public String resolveTranscodableImage(String transcodableUrl) {
                return null;
            }

            // javadoc inherited
            public String resolveVideo(PolicyReference reference) {
                return null;
            }

            // Javadoc inherited.
            public String resolveText(
                    PolicyReference reference,
                    EncodingCollection requiredEncodings) {
                return null;
            }
        };

        ExtractorContext context = new ExtractorContext() {

            // javadoc inherited
            public AssetResolver getAssetResolver() {
                return assetResolver;
            }
            public boolean generateTypeRules() {
                return true;
            }
        };

        StylePropertyDefinitions definitions =
                StylePropertyDetails.getDefinitions();
        final List standardProperties = new ArrayList();
        definitions.iterateStyleProperties(new StylePropertyIteratee() {
            public IterationAction next(StyleProperty property) {
                String name = property.getName();

                // Ignore all the mcs specific properties as they will not be
                // written out in the final output. The exception to this is
                // the mcs-input-format property as that may be written out as
                // -wap-input-... properties.
                if (property == StylePropertyDetails.MCS_INPUT_FORMAT ||
                        !name.startsWith("mcs-")) {
                    standardProperties.add(property);
                }
                return IterationAction.CONTINUE;
            }
        });

        PropertyDetailsSet detailsSet =
                PropertyDetailsSetHelper.getDetailsSet(standardProperties);

        ShorthandSet supportedShorthands = new MutableShorthandSet();

        ExtractorConfigurationBuilder extractorBuilder =
                extractorFactory.createConfigurationBuilder();
        extractorBuilder.setDetailsSet(detailsSet);
        extractorBuilder.setSupportedShorthands(supportedShorthands);

        CSSCompiler compiler = StylingFactory.getDefaultInstance()
                .createDeviceCSSCompiler(DeviceOutlook.OPTIMISTIC);
        CompiledStyleSheet deviceStyleSheet = compiler.compile(
                new StringReader(DevicePolicyConstants.DEFAULT_DISPLAY_CSS), null);
        extractorBuilder.setDeviceStyleSheet(deviceStyleSheet);

        ExtractorConfiguration configuration =
                extractorBuilder.buildConfiguration();

        optimizer = extractorFactory.createOptimizer(configuration, context);
    }

    /**
     * Return a styled <code>Document</code> based on the the specified
     * <code>InputStream</code>.
     *
     * @param inputStream
     * @return a Document
     * @throws RuntimeException if there was a problem.
     */
    public Document parse(InputStream inputStream) {
        Document document;
        try {
            XMLReader reader = DOMUtilities.getReader();
            if (resolver != null) {
                reader.setEntityResolver(resolver);
            }
            document = DOMUtilities.read(reader, inputStream);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return parse(document);
    }

    /**
     * Return a <code>Document</code> based on the specified XML fragment.
     *
     * @param fragment
     * @return a Document
     * @throws RuntimeException if there was a problem.
     */
    public Document parse(String fragment) {
        Document document;
        try {
            document = DOMUtilities.read(fragment);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return parse(document);
    }

    /**
     * Return a styled version of the Document, using the default styles
     * described in the resource
     * <code>com/volantis/mcs/runtime/default.css</code>.
     *
     * @param document
     * @return a styled Document
     */
    public Document parse(Document document) {

        // Create a CSS compiler.
        CSSCompilerBuilder compilerBuilder = new CSSCompilerBuilder();
        StyleSheetCompilerFactory compilerFactory =
                getStyleSheetCompilerFactory();
        compilerBuilder.setStyleSheetCompilerFactory(compilerFactory);
        CSSCompiler cssCompiler = compilerBuilder.getCSSCompiler();

        CompiledStyleSheet defaultCompiledStyleSheet;
        try {
            URL url = getClass().getResource("/com/volantis/mcs/runtime/default.css");
            InputStream stream = url.openStream();
            defaultCompiledStyleSheet = cssCompiler.compile(
                    new InputStreamReader(stream), null);
        } catch (IOException e) {
            throw new ExtendedRuntimeException(e);
        }

        StylingFactory stylingFactory = StylingFactory.getDefaultInstance();
        StylingEngine stylingEngine =
                stylingFactory.createStylingEngine(
                        new InlineStyleSheetCompilerFactory(
                                StylingFunctions.getResolver()));
        stylingEngine.pushStyleSheet(defaultCompiledStyleSheet);

        DocumentStyler styler = new DocumentStyler(stylingEngine,
                XDIMESchemata.CDM_NAMESPACE);
        styler.style(document);

        // Make sure that every element has styles set on it.
        DOMWalker walker = new DOMWalker(new WalkingDOMVisitorStub() {
            // Javadoc inherited.
            public void visit(Element element) {
                if (element.getStyles() == null) {
                    throw new IllegalArgumentException(
                            "element " + element.getName() + " has no styles");
                }
            }
        });
        walker.walk(document);

        DOMTransformer transformer = new DeferredInheritTransformer();
        document = transformer.transform(null, document);

        return document;
    }

    /**
     * Get the StyleSheetCompilerFactory.
     */
    protected StyleSheetCompilerFactory getStyleSheetCompilerFactory() {
        return ThemeStyleSheetCompilerFactory.getDefaultInstance();
    }

    /**
     * Return a <code>String</code> based on the specified
     * <code>Element</code>.
     *
     * @param element
     * @return an XML fragment string
     */
    public String render(Element element) {
        final Document document = DOMFactory.getDefaultInstance()
                .createDocument();
        document.addNode(element);
        return render(document);
    }

    /**
     * Return a <code>String</code> based on the specified
     * <code>Document</code>.
     *
     * @param document
     * @return an XML string
     */
    public String render(Document document) {

        DOMTransformer transformer = new NullRemovingDOMTransformer();
        document = transformer.transform(null, document);

        transformer = new DeferredInheritTransformer();
        document = transformer.transform(null, document);

        // Clear all inferrable properties from the document.
        optimizer.optimizeDocument(document);

        StringWriter writer = new StringWriter();
        StyledDocumentWriter documentWriter = new StyledDocumentWriter(writer);
        DOMDocumentOutputter outputter = new DOMDocumentOutputter(
                documentWriter, new DebugCharacterEncoder());
        try {
            outputter.output(document);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return writer.toString();
    }

    /**
     * Normalise a string XML fragment by parsing it into a styled DOM and then
     * rendering it back to a string.
     *
     * @param fragment
     * @return
     * @throws java.io.IOException
     * @throws org.xml.sax.SAXException
     */
    public String normalize(String fragment)
            throws IOException, SAXException {

        final Document expectedDOM = parse(fragment);
        return render(expectedDOM);
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Dec-05      10641/1 geoff   VBM:2005113024 Pagination page rendering issues

 06-Dec-05      10621/1 geoff   VBM:2005113024 Pagination page rendering issues

 05-Dec-05      10512/1 pduffin VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05      10347/3 pduffin VBM:2005111405 Massive changes for performance

 25-Nov-05      10453/1 rgreenall       VBM:2005092107 Restrict the length of lines written by MCS for devices that have a maximum line limit.

 25-Nov-05      9708/1  rgreenall       VBM:2005092107 Restrict the length of lines written by MCS for devices that have a maximum line limit.

 12-Oct-05      9673/2  pduffin VBM:2005092906 Improved validation and fixed layout formatting

 10-Oct-05      9637/1  emma    VBM:2005092807 Adding tests for XForms emulation

 29-Sep-05      9600/1  geoff   VBM:2005092306 Implement fine grained control over vertical whitespace in WML

 22-Aug-05      9298/4  geoff   VBM:2005080402 Style portlets and inclusions correctly.

 22-Aug-05      9331/9  gkoch   VBM:2005081603 InputStream -> Reader

 22-Aug-05      9223/4  emma    VBM:2005080403 Remove style class from within protocols and transformers

 18-Aug-05      9007/5  pduffin VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 16-Aug-05      9287/6  gkoch   VBM:2005080509 resolved conflict

 16-Aug-05      9287/3  gkoch   VBM:2005080509 vbm2005080509: supermerge + comment

 16-Aug-05      9287/1  gkoch   VBM:2005080509 vbm2005080509 applied review comments

 16-Aug-05      9286/1  geoff   VBM:2005072208 Normalizing of inferrable properties does not work properly.

 09-Aug-05      9195/3  emma    VBM:2005080510 Refactoring to create StyledDOMTester

 19-Jul-05      8668/13 geoff   VBM:2005060302 XDIMECP: Generate optimised CSS for a DOM.

 ===========================================================================
*/
