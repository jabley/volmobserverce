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
 * (c) Volantis Systems Ltd 2000. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 22-Feb-02    Paul            VBM:2002021802 - Created. See class comment
 *                              for details.
 * 28-Mar-02    Steve           VBM:2002021404 - Fixed for method name changes
 *                              in FormFragmentInstance
 * 28-Mar-02    Allan           VBM:2002022007 - Added char[] versions of
 *                              writeDirect() and writeEncoded().
 * 02-Apr-02    Paul            VBM:2002021802 - Released contents of the
 *                              document and the current request's dom pool.
 * 03-Apr-02    Paul            VBM:2002021802 - Fixed problem with orientation
 *                              of menu items.
 * 03-Apr-02    Steve           VBM:2001101803 - Fixed bug where the previous
 *                              link text was never selected, it was always the
 *                              next link text selected in form fragments.
 * 04-Apr-02    Paul            VBM:2002021802 - Improved logging of menu
 *                              orientation.
 * 09-Apr-02    Allan           VBM:2002040903 - Check that document is not
 *                              null before calling release in release().
 * 25-Apr-02    Paul            VBM:2002042202 - Fixed montages, made sure
 *                              that the page head was written out correctly,
 *                              fixed a lot of the javadoc which referred to
 *                              StringBuffers and made infrastructure changes
 *                              to allow support for HTML protocols.
 * 26-Apr-02    Paul            VBM:2002042205 - Fixed release so it does not
 *                              try and release resources which were not
 *                              initialised due to an exception. Also made
 *                              the initial header optional.
 * 30-Apr-02    Allan           VBM:2002040804 - Added writeStyleSheet(),
 *                              renamed addLinkElement() to doLink(),
 *                              call writeStyleSheet() from closeCanvas() if
 *                              there is a style sheet to write.
 * 31-Apr-02    Paul            VBM:2002042205 - Added support for writing the
 *                              style sheet.
 * 01-May-02    Allan           VBM:2002040804 - Updated closeCanvas() to check
 *                              style sheet rules before trying to write a
 *                              style sheet.
 * 02-May-02    Adrian          VBM:2002040808 - Added support for new
 *                              implementation of CCS emulation.
 * 03-May-02    Allan           VBM:2002040804 - Added some debug log for
 *                              themes in closeCanvas().
 * 03-May-02    Allan           VBM:2002040804 - Removed check for existence
 *                              of theme rules in closeCanvas() - this is now
 *                              done in VolantisProtocol.writeStyleSheet().
 * 03-May-02    Paul            VBM:2002042203 - Protected some methods which
 *                              are only called from within the protocols.
 * 09-May-02    Ian             VBM:2002031203 - Changed log4j to use string.
 * 15-May-02    Byron           VBM:2002042503 - Volantis page header banner
 *                              now optional. Special case for pageheaderMsg
 *                              handled correctly.
 *                              Modified writeInitialHeader() method.
 * 23-May-02    Paul            VBM:2002042202 - Handled anchor properly so
 *                              it should now work properly on WapTV. Also
 *                              added a couple of helper methods. Removed the
 *                              writeDirect, writeEncoded, getDirectWriter
 *                              and getEncodingWriter methods.
 * 05-Jun-02    Adrian          VBM:2002021103 - Open KEEPTOGETHER_ELEMENT in
 *                              methods openTableBody and openDiv, and close in
 *                              methods closeTableBody and closeDiv
 * 10-Jun-02    Adrian          VBM:2002021103 - Changed keeptogether to
 *                              keepTogether.
 * 12-Jun-02    Steve           VBM:2002053005 - Added the META element to
 *                              the protocol mappings for CSS Transformations
 *                              this element is constant accross all derived
 *                              protocols.
 * 19-Jun-02    Adrian          VBM:2002053104 - Set inclusion style class, id
 *                              and element names on method openInclusionPage
 * 25-Jun-02    Adrian          VBM:2001082010 - Set href to " " if it was null
 *                              in method doAnchor.
 * 09-Jul-02    Steve           VBM:2002062401 - writeContent() override to write
 *                              the contents of the xfcontent tag.
 * 15-Jul-02    Byron           VBM:2002051303 - Modified writeOpenLayout() and
 *                              writeCloseInclusion() to save and restore the
 *                              insertion point correctly.
 * 26-Jul-02    Steve           VBM:2002062401 - writeContent() has shiny new
 *                              javadoc.
 * 27-Jul-02    Adrian          VBM:2002072407 - moved calls to elementMapping.
 *                              add() from initialize method to constructor.
 * 31-Jul-02    Paul            VBM:2002073008 - Added support for package.
 *                              This involved separating some code into
 *                              separate methods in order to allow it to be
 *                              reused.
 * 06-Aug-02    Paul            VBM:2002073008 - Removed support for package
 *                              element, added support for overlay attribute.
 * 06-Aug-02    Sumit           VBM:2002080509 - Added support for the
 *                              <timer> element
 * 07-Aug-02    Paul            VBM:2002080509 - Fixed problem with <timer> by
 *                              pushing the device layout content buffer on
 *                              the stack for the body of the inclusion.
 * 23-Aug-02    Adrian          VBM:2002082301 - updated openCanvasPage
 *                              to add styleClass and Id from canvas attributes
 *                              to bodyAttributes.
 * 09-Sep-02    Ian             VBM:2002081307 - Added calculation for widths
 *                              of transcodable images.
 * 10-Oct-02    Adrian          VBM:2002100405 - Updated OpenInclusion to set
 *                              inclusionStyleClass to context.getStyleClass
 *                              and inclusionStyleId to attributes.getStyleId.
 * 10-Oct-02    Adrian          VBM:2002100404 - update writePageHead to call
 *                              pageHead.writeCssCandidates and throw
 *                              IOException.  Changed writeLink and
 *                              writeOpen/CloseStyle to public from protected.
 * 14-Oct-02    Geoff           VBM:2002100905 - Updated initialise and release
 *                              so that all pages participating in a compound
 *                              page now share the same DOMPool, and also
 *                              fixed typos/unneeded code that IDEA found.
 * 17-Oct-02    Geoff           VBM:2002100905 - Clean up the code which
 *                              creates DOMPools so that dependent objects and
 *                              logging only happens when a DOMPool is really
 *                              created rather than shared, and so that we
 *                              check IncludedPageContext rather than
 *                              EnclosingPageContext to see if we have a parent
 *                              page with a Canvas.
 * 01-Nov-02    Ian             VBM:2002091806 - Moved calculation of MTS width
 *                              into VolantisProtocol and MarinerPageContext.
 * 11-Nov-02    Chris W         VBM:2002102403 - Added writeOpenSlide(),
 *                              writeCloseSlide(), openSlide() and closeSlide()
 *                              for MMS SMIL protocol
 * 11-Nov-02    Sumit           VBM:2002111105 - Changed doMenu to use a
 *                              FormatReferenceContext when calling
 *                              context.endCurrentBuffer(..)
 * 14-Nov-02    Geoff           VBM:2002103005 - moved impl. of writeLayout up
 *                              to VolantisProtocol since all child impls were
 *                              identical.
 * 14-Nov-02    Chris W         VBM:2002111402 - Changed access visibility of
 *                              getCurrentBuffer() from private to protected so
 *                              that subclasses e.g. MMS_SMIL_2_0 can call it.
 * 20-Nov-02    Geoff           VBM:2002111504 - remove unused method
 *                              (writeShardLink), unused import and local var.
 * 01-Dec-02    Phil W-S        VBM:2002112901 - Update writePageHead to
 *                              pass this protocol to the page head for
 *                              forwarding to the various candidates.
 * 09-Dec-02    Phil W-S        VBM:2002112805 - Re-write of openInclusionPage
 *                              to optimize the generation of inclusion style
 *                              classes.
 * 16-Dec-02    Adrian          VBM:2002100203 - Added more comprehensive
 *                              javadoc to constructor where elementMappings
 *                              field is populated.
 * 20-Dec-02    chris W         VBM:2002121904 - Instead of inspecting the src
 *                              property of ImageAttribute for ?tf.width=,
 *                              writeImage calls isConvertibleImageAsset method
 *                              instead. The src property will not have the
 *                              query string if we are requesting the image
 *                              from MPS.
 * 16-Jan-03    Mat             VBM:2002112603 - Added writeAudio and doAudio
 * 20-Jan-03    Adrian          VBM:2003011605 - updated postProcessPageBuffer
 *                              to remove the DOMProtocol parameter from the
 *                              call to dissector.writeShard()
 * 30-Jan-03    Sumit           VBM:2003012902 - doMeta encodes the value of
 *                              the content and http-equiv attribute
 * 16-Jan-03    Mat             VBM:2002112603 - Added writeAudio and doAudio
 * 10-Feb-02    Sumit           VBM:2003020706 - openCanvasPage sets the title
 *                              of the CanvasAttributes passed to openBody from
 *                              the pageTitle of the attributes
 * 11-Feb-03    Ian             VBM:2003020607 - Ported from MTS changes from Metis.
 * 12-Feb-03    Phil W-S        VBM:2003021206 - Refactor postProcessPageBuffer
 *                              to writeCanvasContent. Change the call to
 *                              postProcessPageBuffer in closeCanvasPage to
 *                              call super.closeCanvasPage. Add
 *                              writeMontageContent, using the document
 *                              processing and writing from closeMontagePage.
 *                              Remove document processing from
 *                              closeMontagePage and replace with a call to
 *                              super.closeMontagePage.
 * 21-Feb-03    Phil W-S        VBM:2003022006 - Updated writeCanvasContent to
 *                              explicitly invoke
 *                              PackageResources.initializeEncodedURLs when
 *                              dissection is needed.
 * 06-Mar-03    Sumit           VBM:2003022605 - Implemented spatial methods to
 *                              generate grids by default. Created new open...
 *                              and close... methods from the write... methods
 * 11-Mar-03    Mat             VBM:2003031203 - Undo changes made in
 *                              2003012902 as DOM Protocols already handle
 *                              the quoting of text.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * 25-Mar-03    Steve           VBM:2003031907 - Dont try and write the anchor
 *                              contents if the content is null.
 * 29-Mar-03    Phil W-S        VBM:2002111502 - Implement writeOpenPhoneNumber
 *                              and writeClosePhoneNumber and add
 *                              openPhoneNumber, closePhoneNumber,
 *                              doPhoneNumber, resolveQualifiedFullNumber and
 *                              addPhoneNumberContents.
 * 08-Apr-03    Sumit           VBM:2003032713 - Added render support for menu
 *                              item groups
 * 16-Apr-03    Geoff           VBM:2003041603 - Add declaration for
 *                              ProtocolException where necessary.
 * 17-Apr-03    Byron           VBM:2003032608 - Moved closeCanvasPage() method
 *                              contents into writeCanvasContent(). Removed
 *                              closeCanvasPage method.
 * 21-May-03    Sumit           VBM:2003032713 - Altered renderMenuChild to
 *                              test for null repeat before parsing float.
 *                              doMenu() tests if the pane is an iterator and
 *                              only then closes it, and pops and pushes the
 *                              new buffer allocated
 * 23-May-03    Mat             VBM:2003042907 - Changed getXMLOutputter() to
 *                              getDocumentOutputter(), added outputterWriter
 *                              instance variable, split code in
 *                              writeCanvasContent() into createPage(),
 *                              createDocument() and transformDOM() and changed
 *                              the instantiation of XMLOutputter to reflect the
 *                              changes made in that class.
 * 21-May-03    Chris W         VBM:2003040403 - Calls to DOMOutputBuffer.app..
 *                              endLiteral() changed to appendEncoded()
 * 26-May-03    Chris W         VBM:2003052205 - writeDivideHint only adds a
 *                              dividehint when inside a dissecting pane
 * 21-May-03    Allan           VBM:2003020703 - Modified doMenu() to keep the
 *                              context output buffer stack in-sync when using
 *                              a iterator pane for menu items.
 *
 * 22-May-03    Allan           VBM:2003052004 - Added 4 param version of
 *                              doMenu().
 * 23-May-03    Allan           VBM:2003052207 - Added menuIsVertical().
 * 27-May-03    Allan           VBM:2003052207 - Check
 *                              requiresVerticalSeparator in 4 param doMenu().
 * 28-May-03    Allan           VBM:2003051904 - Replaced menuIsVertical() with
 *                              menuOrientation(). Use menuOrientation() in 1
 *                              param version of doMenu(). Use a
 *                              MenuOrientation in 4 param version of doMenu().
 * 28-May-03    Mat             VBM:2003042911 - Renamed outputterWriter to
 *                              outputter and changed writeCanvasContent() &
 *                              writeMontageContent() to accept a
 *                              PackageBodyOutput instead of a Writer.
 * 30-May-03    Geoff           VBM:2003042905 - Fix merge problems between
 *                              Mat's and Allan's code.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import com.volantis.mcs.cache.Cache;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.EnvironmentContext;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.ResponseCachingDirectives;
import com.volantis.mcs.css.renderer.BorderRadiusHelper;
import com.volantis.mcs.css.renderer.StyleSheetRenderer;
import com.volantis.devrep.repository.api.devices.DevicePolicyConstants;
import com.volantis.mcs.devices.DevicePolicyValueUtil;
import com.volantis.mcs.dom.DOMFactory;
import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.Node;
import com.volantis.mcs.dom.XMLDeclaration;
import com.volantis.mcs.dom.dtd.DTD;
import com.volantis.mcs.dom.debug.StyledDocumentLogger;
import com.volantis.mcs.dom.output.CharacterEncoder;
import com.volantis.mcs.dom.output.DOMDocumentOutputter;
import com.volantis.mcs.dom.output.DocumentOutputter;
import com.volantis.mcs.dom.output.XMLDocumentWriter;
import com.volantis.mcs.dom.output.DocumentWriter;
import com.volantis.mcs.dom2theme.StyledDOMStyleAttributeRenderer;
import com.volantis.mcs.dom2theme.StyledDOMThemeExtractor;
import com.volantis.mcs.dom2theme.StyledDOMThemeExtractorFactory;
import com.volantis.mcs.dom2theme.AssetResolver;
import com.volantis.mcs.dom2theme.extractor.ExtractorConfiguration;
import com.volantis.mcs.layouts.ColumnIteratorPane;
import com.volantis.mcs.layouts.Form;
import com.volantis.mcs.layouts.FormFragment;
import com.volantis.mcs.layouts.FormatType;
import com.volantis.mcs.layouts.IteratorPane;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.layouts.RowIteratorPane;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.policies.PolicyReference;
import com.volantis.mcs.policies.variants.metadata.EncodingCollection;
import com.volantis.mcs.policies.variants.metadata.EncodingCollectionFactory;
import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.mcs.protocols.assets.LinkAssetReference;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.assets.implementation.LiteralTextAssetReference;
import com.volantis.mcs.protocols.assets.implementation.DefaultComponentTextAssetReference;
import com.volantis.mcs.protocols.corners.CornersTransformer;
import com.volantis.mcs.protocols.css.CSSModule;
import com.volantis.mcs.protocols.css.CSSRemappingTransformer;
import com.volantis.mcs.protocols.css.DefaultReplacementPseudoElementFactory;
import com.volantis.mcs.protocols.css.MarkerDOMTransformer;
import com.volantis.mcs.protocols.css.PseudoElementDOMTransformer;
import com.volantis.mcs.protocols.css.RuntimeExtractorContext;
import com.volantis.mcs.protocols.css.emulator.CSSConstants;
import com.volantis.mcs.protocols.css.emulator.DefaultStyleEmulationRenderer;
import com.volantis.mcs.protocols.css.emulator.StyleEmulationRenderer;
import com.volantis.mcs.protocols.css.reference.CssReference;
import com.volantis.mcs.protocols.css.reference.ExternalCSSReference;
import com.volantis.mcs.protocols.css.reference.ImportCSSReference;
import com.volantis.mcs.protocols.css.reference.InternalCSSReference;
import com.volantis.mcs.protocols.css.renderer.RuntimeRendererContext;
import com.volantis.mcs.protocols.dissection.DissectionConstants;
import com.volantis.mcs.protocols.forms.AbstractForm;
import com.volantis.mcs.protocols.forms.AbstractFormFragment;
import com.volantis.mcs.protocols.forms.EmulatedXFormDescriptor;
import com.volantis.mcs.protocols.forms.FragmentableFormData;
import com.volantis.mcs.protocols.forms.Link;
import com.volantis.mcs.protocols.forms.validation.TextInputFormatParser;
import com.volantis.mcs.protocols.forms.validation.TextInputFormat;
import com.volantis.mcs.protocols.highlight.HighlightTransformer;
import com.volantis.mcs.protocols.href.HrefTransformer;
import com.volantis.mcs.protocols.html.menu.DeprecatedEventAttributeUpdater;
import com.volantis.mcs.protocols.html.menu.DeprecatedExternalShortcutRenderer;
import com.volantis.mcs.protocols.layouts.ContainerInstance;
import com.volantis.mcs.protocols.layouts.FormFragmentInstance;
import com.volantis.mcs.protocols.layouts.FormInstance;
import com.volantis.mcs.protocols.menu.MenuModuleCustomisation;
import com.volantis.mcs.protocols.menu.shared.MenuModuleCustomisationImpl;
import com.volantis.mcs.protocols.menu.shared.renderer.DeprecatedAnchorOutput;
import com.volantis.mcs.protocols.menu.shared.renderer.DeprecatedDivOutput;
import com.volantis.mcs.protocols.menu.shared.renderer.DeprecatedImageOutput;
import com.volantis.mcs.protocols.menu.shared.renderer.DeprecatedLineBreakOutput;
import com.volantis.mcs.protocols.menu.shared.renderer.DeprecatedOutputLocator;
import com.volantis.mcs.protocols.menu.shared.renderer.DeprecatedSpanOutput;
import com.volantis.mcs.protocols.renderer.RendererContext;
import com.volantis.mcs.protocols.renderer.shared.DOMRendererContextImpl;
import com.volantis.mcs.protocols.response.attributes.ResponseBodyAttributes;
import com.volantis.mcs.protocols.styles.AbstractPropertyHandler;
import com.volantis.mcs.protocols.styles.AverageBorderSpacingRenderer;
import com.volantis.mcs.protocols.styles.BackgroundComponentHandler;
import com.volantis.mcs.protocols.styles.BackgroundImageHandler;
import com.volantis.mcs.protocols.styles.ChoicePropertyHandler;
import com.volantis.mcs.protocols.styles.ColorHandler;
import com.volantis.mcs.protocols.styles.DefaultingPropertyHandler;
import com.volantis.mcs.protocols.styles.PositivePixelLengthHandler;
import com.volantis.mcs.protocols.styles.PropertyHandler;
import com.volantis.mcs.protocols.styles.ValueHandler;
import com.volantis.mcs.protocols.styles.ValueHandlerToPropertyAdapter;
import com.volantis.mcs.protocols.styles.ValueRendererChecker;
import com.volantis.mcs.protocols.trans.NullRemovingDOMTransformer;
import com.volantis.mcs.protocols.trans.StyleEmulationElementConfiguration;
import com.volantis.mcs.protocols.trans.StyleEmulationRenderingTransformer;
import com.volantis.mcs.protocols.trans.StyleEmulationTransformer;
import com.volantis.mcs.protocols.viewport.ViewportControlRenderer;
import com.volantis.mcs.protocols.viewport.ViewportControlModule;
import com.volantis.mcs.runtime.StyleSheetConfiguration;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.runtime.scriptlibrarymanager.RequiredScriptModules;
import com.volantis.mcs.runtime.dissection.RuntimeDissectionContext;
import com.volantis.mcs.runtime.dissection.RuntimeDissectionURLManager;
import com.volantis.mcs.runtime.dissection.DissectionUtilities;
import com.volantis.mcs.runtime.policies.RuntimePolicyReference;
import com.volantis.mcs.runtime.debug.StrictStyledDOMHelper;
import com.volantis.mcs.runtime.packagers.PackageBodyOutput;
import com.volantis.mcs.runtime.packagers.PackageResources;
import com.volantis.mcs.themes.PropertyGroups;
import com.volantis.mcs.themes.StyleComponentURI;
import com.volantis.mcs.themes.StyleInteger;
import com.volantis.mcs.themes.StyleKeyword;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleSheet;
import com.volantis.mcs.themes.StyleString;
import com.volantis.mcs.themes.StyleTranscodableURI;
import com.volantis.mcs.themes.StyleURI;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.values.StyleKeywords;
import com.volantis.mcs.themes.properties.BorderStyleKeywords;
import com.volantis.mcs.themes.properties.MCSMenuLinkStyleKeywords;
import com.volantis.mcs.themes.properties.MCSMenuOrientationKeywords;
import com.volantis.mcs.themes.properties.MCSMenuSeparatorPositionKeywords;
import com.volantis.mcs.themes.properties.MCSMenuSeparatorTypeKeywords;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.mcs.utilities.VolantisEnvironment;
import com.volantis.mcs.dissection.impl.DOMDissectableDocumentImpl;
import com.volantis.mcs.dissection.impl.DOMDissectedContentHandlerImpl;
import com.volantis.mcs.dissection.*;
import com.volantis.mcs.dissection.dom.DissectableDocument;
import com.volantis.mcs.dissection.dom.DissectedContentHandler;
import com.volantis.shared.dependency.Cacheability;
import com.volantis.shared.dependency.Dependency;
import com.volantis.shared.dependency.DependencyContext;
import com.volantis.shared.time.Period;
import com.volantis.styling.StyleContainer;
import com.volantis.styling.Styles;
import com.volantis.styling.properties.StyleProperty;
import com.volantis.styling.values.MutablePropertyValues;
import com.volantis.styling.values.PropertyValues;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.map.agent.MediaAgent;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;


/**
 * This class is the base class of all the protocols which generate DOMs. It
 * implements the write... methods of VolantisProtocol in terms of some DOM
 * specific methods.
 *
 * @mock.generate base="VolantisProtocol"
 */
public abstract class DOMProtocol extends VolantisProtocol
    implements DissectionConstants, MenuChildRendererVisitor,
    DeprecatedSpanOutput, DeprecatedAnchorOutput, DeprecatedImageOutput,
    DeprecatedDivOutput, DeprecatedLineBreakOutput,
    DeprecatedOutputLocator {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
        LocalizationFactory.createLogger(DOMProtocol.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
        LocalizationFactory.createExceptionLocalizer(DOMProtocol.class);

    /**
     * A blank line used in the header.
     */
    private static final String blankLine
        = "                                                    ";

    /**
     * The fixed set of information added to the header.
     */
    private static final String[] fixedHeader = new String[]{
        "                                                    ",
        " Volantis Systems Ltd, 2003                         ",
        "                                                    ",
        " This page was generated by the Volantis Server     ",
        "                                                    ",
    };

    /**
     * A text input validation parser that does not force all formats to be
     * treated as if they allowed empty fields.
     */
    private static final TextInputFormatParser TEXT_INPUT_FORMAT_PARSER =
            new TextInputFormatParser(false);

    private static final EncodingCollection FORM_VALIDATOR_ENCODING;

    static {
        EncodingCollectionFactory factory =
                EncodingCollectionFactory.getDefaultInstance();
        FORM_VALIDATOR_ENCODING =
                factory.createEncodingCollection(TextEncoding.FORM_VALIDATOR);
    }

    /**
     * Ordered list of transformers.
     */
    private final TreeMap transformerSet = new TreeMap();

    /**
     * The output buffer factory for this protocol.
     */
    protected DOMOutputBufferFactory outputBufferFactory;

    /**
     * The page buffer.
     */
    protected DOMOutputBuffer pageBuffer;

    /**
     * The body buffer.
     */
    private DOMOutputBuffer bodyBuffer;

    /**
     * The contents of the current layout buffer.
     */
    //protected DOMOutputBuffer layoutsContentBuffer;

    protected Document document;

    /**
     * The attributes to use when writing the body.
     */
    private BodyAttributes bodyAttributes;

    /**
     * A {@link MenuRenderer} for rendering numeric shortcut style menus.
     */
    private MenuRenderer numericShortcutMenuRenderer;

    /**
     * The object responsible for handling form validation patterns.
     */
    protected final ProtocolConfiguration protocolConfiguration;

    /**
     * Flag to indicate whether this protocol supports CSS or not.
     */
    protected boolean supportsCSS = true;

    /**
     * Factory to use to create DOM objects.
     */
    protected final DOMFactory domFactory;

    /**
     * The CSS Module that is used by this protocol.
     */
    private CSSModule cssModule;

    protected PropertyHandler backgroundComponentHandler;
    protected PropertyHandler mcsImageHandler;

    protected PropertyHandler backgroundColorHandler;
    protected PropertyHandler colorHandler;
    protected PropertyHandler borderHandler;
    protected PropertyHandler borderSpacingHandler;
    private ValueHandler marginEdgeHandler;
    private PropertyHandler marginTopHandler;
    private ValueHandler colorValueHandler;
    protected PropertyHandler borderColorHandler;

    private Inserter inserter;

    protected ViewportControlModule viewportControlModule;

    /**
     * Renders appropriate style property emulation markup for this protocol.
     * May be null if no style property emulation is required (i.e. the device
     * can render everything using CSS).
     */
    protected StyleEmulationRenderer styleEmulationRenderer;

    /**
     * The style emulation transformer intialized in the constructor.
     */
    private StyleEmulationTransformer styleEmulationTransformer;

    /**
     * A set of locked elements.
     */
    private Set lockedElements;

    /**
     * Stack of output buffers for storing the contents of pre elements.
     */
    private Stack preBufferStack;

    /**
     * Attribute name used to mark elements to be transformed to text node.
     * Must be an invalid XML name to avoid conflict with valid XML attribute names.
     */
    private static final String TRANSFORM_TO_TEXT_ATTRIBUTE_NAME = "_TRANSFORM_TO_TEXT_";

    /**
     * The extractor context.
     */
    private RuntimeExtractorContext extractorContext;

    /**
     * Period in seconds to add to the time to live value of the media agent
     * requests. This is needed to make sure that the descriptor items in the
     * database are still there when the device wants to use them.
     */
    private static final int SAFETY_THRESHOLD_FOR_MAP_ITEMS = 600;

    /**
     * Initialise this object.
     *
     * @param supportFactory The factory used by the protocol to obtain support
     *                       objects.
     * @param configuration  The protocol specific configuration.
     */
    public DOMProtocol(ProtocolSupportFactory supportFactory,
                       ProtocolConfiguration configuration) {

        super(configuration);

        if (supportFactory == null) {
            throw new IllegalArgumentException(
                "supportFactory cannot be null");
        }

        domFactory = supportFactory.getDOMFactory();
        this.protocolConfiguration = configuration;
    }

    // Javadoc inherited from super class.
    public void initialise() {
        try {
            viewportControlModule = (ViewportControlModule) Class.forName("com.volantis.mcs.protocols.viewport.DefaultViewportControlModule").newInstance();
        } catch (Exception e) {
            logger.error("cannot-instantiate-viewport-module", e);
        }
        super.initialise();

        supportsCSS = context.getBooleanDevicePolicyValue(
            DevicePolicyConstants.SUPPORTS_CSS);

        // Configure and set the DOM pool before calling the parent as it may
        // depend on this.
        if (logger.isDebugEnabled()) {
            logger.debug("Initialising " + this);

            // If we are the "parent" page (or the parent has no canvas)...

        }

        // Create this after initialising the pool.
        pageBuffer = allocateOutputBuffer();

        // Do anything in here which depends on the context as it may not
        // have been initialised at the time the constructor has been
        // called.

        cssModule = createCSSModule();

        initialiseStyleHandlers();

        // Add in the anchor transformer as the first transformer
        addDOMTransformer(new HrefTransformer());

        // add in the before/after pseudo element transformer as the second
        addDOMTransformer(new PseudoElementDOMTransformer(
                new DefaultReplacementPseudoElementFactory(this)));
        addDOMTransformer(new MarkerDOMTransformer());

        // Add in the highlight transformer.
        if(getProtocolConfiguration().isFrameworkClientSupported()) {
        // Must be run after any transformer that creates focusable elements
        addDOMTransformer(new HighlightTransformer());

            // Add in the corners transformer.
            if(BorderRadiusHelper.isBorderRadiusEmulated(getProtocolConfiguration().getCssVersion())) {
                addDOMTransformer(new CornersTransformer());
            }
        }

        // Create the style transformer and initialize it with appropriate
        // values.
        styleEmulationTransformer = createStyleEmulationTransformer();

        // Create the style emulation renderer so that subclasses can
        // configure it.
        createStyleEmulationRenderer();

        // Wrap the style emulation renderer in a transformer.
        addDOMTransformer(new StyleEmulationRenderingTransformer(styleEmulationRenderer));
        addDOMTransformer(styleEmulationTransformer);

        // Add a CSSRemappingTransformer after the style emulation transformer
        addDOMTransformer(new CSSRemappingTransformer());

        // There is an optimisation to reduce page weight; if a given device
        // does not understand (parts of) the generated CSS, there is no point
        // in sending it in the first place.
        // Not yet implemented - a VBM has been raised for the MCS bucket task
        // of nice things to have.
//        addDOMTransformer(new UnsupportedCSSRemovingTransformer());

        // Add the deferred inherit transformer, it must come after the
        // style emulation renderer otherwise extra align attributes are
        // written out.
        addDOMTransformer(new DeferredInheritTransformer());

        // Create extractor context.
        extractorContext = new RuntimeExtractorContext(
                getMarinerPageContext().getPolicyReferenceResolver(),
                getMarinerPageContext().getAssetResolver(),
                getMarinerPageContext().getTranscodableUrlResolver(),
                protocolConfiguration.getCssVersion());
    }

    public void initialiseCanvas() {
        super.initialiseCanvas();

        inserter = new DefaultContentInserter(this,
                context.getPolicyReferenceResolver(),
                context.getAssetResolver());
    }

    /**
     * Initialise style handlers.
     */
    protected void initialiseStyleHandlers() {
        // Initialise style value handlers.
        colorValueHandler = new ColorHandler();
        colorHandler = new ValueHandlerToPropertyAdapter(
            StylePropertyDetails.COLOR, colorValueHandler);
        backgroundColorHandler = new ValueHandlerToPropertyAdapter(
            StylePropertyDetails.BACKGROUND_COLOR, colorValueHandler);
        backgroundComponentHandler = new BackgroundComponentHandler(
            context.getPolicyReferenceResolver(), context.getAssetResolver(),
            context.getTranscodableUrlResolver());

        // todo The instance created here is exactly the same as the one created in the BackgroundComponentHandler so reuse it.
        // This may not be sensible if there are issues with sharing code with state in it.
        mcsImageHandler = new ValueHandlerToPropertyAdapter(
            StylePropertyDetails.MCS_IMAGE,
            new BackgroundImageHandler(context.getPolicyReferenceResolver(),
                context.getAssetResolver(),
                context.getTranscodableUrlResolver()));

        // A border should only be rendered if the border style is not none
        // and the border width is valid.
        final ChoicePropertyHandler borderWidthHandler =
            new ChoicePropertyHandler();
        borderWidthHandler.addHandlers(PropertyGroups.BORDER_WIDTH_PROPERTIES,
                                       new PositivePixelLengthHandler());

        final ChoicePropertyHandler borderStyleHandler =
            new ChoicePropertyHandler();
        borderStyleHandler.addHandlers(PropertyGroups.BORDER_STYLE_PROPERTIES,
                new ValueRendererChecker() {
                    public void visit(StyleKeyword value,
                                      Object object) {
                        if (value != BorderStyleKeywords.NONE) {
                            string = "true";
                        }
                    }
                });

        borderHandler = new AbstractPropertyHandler() {
            public boolean isSignificant(PropertyValues propertyValues) {
                return borderStyleHandler.isSignificant(propertyValues) &&
                    borderWidthHandler.isSignificant(propertyValues);
            }

            public String getAsString(MutablePropertyValues propertyValues) {
                return borderStyleHandler.isSignificant(propertyValues) ?
                    borderWidthHandler.getAsString(propertyValues) : null;
            }
        };

        if (context.getDeviceName().endsWith("Netscape4")) {
            borderHandler = new DefaultingPropertyHandler(borderHandler, "0");
        }

        borderSpacingHandler = new AverageBorderSpacingRenderer();
        marginEdgeHandler = new PositivePixelLengthHandler();
        marginTopHandler =
            new ValueHandlerToPropertyAdapter(StylePropertyDetails.MARGIN_TOP,
                                              marginEdgeHandler);

        ChoicePropertyHandler choicePropertyHandler =
            new ChoicePropertyHandler();
        choicePropertyHandler.addHandlers(
            PropertyGroups.BORDER_COLOR_PROPERTIES, colorValueHandler);
        borderColorHandler = choicePropertyHandler;
    }

    protected CSSModule createCSSModule() {
        return null;
    }

    // Javadoc inherited.
    public PropertyHandler getBackgroundColorHandler() {
        return backgroundColorHandler;
    }

    protected String getMargin(Styles styles, StyleProperty property) {
        PropertyValues propertyValues = styles.getPropertyValues();
        StyleValue value = propertyValues.getComputedValue(property);
        return marginEdgeHandler.getAsString(value);
    }

    // Javadoc inherited.
    public OutputBufferFactory getOutputBufferFactory() {
        // Lazily initialise the factory to allow the pool time to be
        // constructed (or not) depending on configuration.
        if (outputBufferFactory == null) {
            outputBufferFactory = new DOMOutputBufferFactory(getDOMFactory());
        }
        return outputBufferFactory;
    }

    /**
     * Helper method to allocate a new DOMOutputBuffer.
     */
    protected DOMOutputBuffer allocateOutputBuffer() {
        return (DOMOutputBuffer)getOutputBufferFactory().createOutputBuffer();
    }

    public DOMFactory getDOMFactory() {
        return domFactory;
    }

    /**
     * Retrieve the protocol-specific content inserter.
     * @return The protocol-specific content inserter.
     */
    public Inserter getInserter() {
        return inserter;
    }

    // ==========================================================================
    //   General helper methods.
    // ==========================================================================

    // Javadoc inherited.
    protected FragmentLinkRendererContext createFragmentLinkRendererContext() {

        return new DOMFragmentLinkRendererContext(this);

    }

    protected DOMOutputBuffer getPageBuffer() {
        return pageBuffer;
    }

    protected DOMOutputBuffer getCurrentBuffer() {
        OutputBuffer outputBuffer = context.getCurrentOutputBuffer();
        return (DOMOutputBuffer)outputBuffer;
    }

    private DOMOutputBuffer getInitialBuffer() {
        return (DOMOutputBuffer) getPageHead().getInitial();
    }

    protected DOMOutputBuffer getHeadBuffer() {
        return (DOMOutputBuffer) getPageHead().getHead();
    }

    protected DOMOutputBuffer getScriptBuffer() {
        return (DOMOutputBuffer) getPageHead().getScript();
    }

    protected DOMOutputBuffer getExtraBuffer(String name,
                                             boolean create) {
        return (DOMOutputBuffer) getPageHead().getBuffer(name, create);
    }

    /**
     * Create a document outputter.
     *
     * @return The document outputter.
     */
    public DocumentOutputter createDocumentOutputter(Writer writer) {

        DTD dtd = protocolConfiguration.getDTD();
        DocumentWriter documentWriter = dtd.createDocumentWriter(writer);

        return new DOMDocumentOutputter(documentWriter, getCharacterEncoder());
    }

    /**
     * Create a debug document outputter.
     *
     * @return The debug document outputter.
     */
    protected DocumentOutputter createDebugDocumentOutputter(Writer writer) {
        // for now just return the normal outputter by default.
        // in future I'm sure we can think of some debug info we'd like to
        // see in here.
        return createDocumentOutputter(writer);
    }

    protected void debug(String label, DOMOutputBuffer dom) {
        System.out.println(label);
        Element root = dom.getRoot();
        Writer writer = new OutputStreamWriter(System.out);
        CharacterEncoder encoder = getCharacterEncoder();
        DocumentOutputter outputter = new DOMDocumentOutputter(
            new XMLDocumentWriter(writer), encoder);
        try {
            outputter.output(root);
            writer.flush();
            System.out.println();
        } catch (IOException e) {
            logger.error("dom-outputting-error", new Object[]{dom}, e);
        }
    }

    // --------------------------------------------------------------------------
    //   Page Generation
    // --------------------------------------------------------------------------

    /**
     * Create the actual page.
     *
     * @param attributes The canvas attributes.
     */
    private void createPage(CanvasAttributes attributes)
        throws IOException, ProtocolException {

        DeviceLayoutContext deviceLayoutContext
            = context.getDeviceLayoutContext();

        // Write out the content from the layout tree
        writeLayout(deviceLayoutContext);

        // Only write out the close body tag, if we have written
        // out the open body tag.
        if (bodyAttributes != null) {
            writeCloseBody(bodyAttributes);
            bodyAttributes = null;
        }

        // Pop the body buffer off the stack of output buffers.
        context.popOutputBuffer(bodyBuffer);

        // Do any protocol specific processing of the body buffer before adding it
        // to the page.
        processBodyBuffer(bodyBuffer);

        // Write out the page head, do this before adding the body to the page
        // buffer as the head must come before the body.
        writePageHead();

        // Add the body to the page.
        pageBuffer.addOutputBuffer(bodyBuffer);

        // Close whatever was opened.
        if (writeHead) {
            if (logger.isDebugEnabled()) {
                logger.debug("Writing pagePostamble");
            }
            writeCloseCanvas(attributes);
        } else if (inclusion) {
            if (logger.isDebugEnabled()) {
                logger.debug("Closing inclusion");
            }
            writeCloseInclusion(attributes);
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("HEAD: Not writing close canvas");
            }
        }

        // Pop the page buffer off the stack of output buffers.
        context.popOutputBuffer(pageBuffer);

    }


    /**
     * Create the widget response page - without head.
     *
     * @param attributes The canvas attributes.
     */
    private void createAJAXResponse(ResponseBodyAttributes attributes)
        throws IOException, ProtocolException {

        DeviceLayoutContext deviceLayoutContext
            = context.getDeviceLayoutContext();

        // Write out the content from the layout tree
        writeLayout(deviceLayoutContext);

        pageBuffer.clear();

        // Pop the body buffer off the stack of output buffers.
        context.popOutputBuffer(bodyBuffer);

        // Do any protocol specific processing of the body buffer before adding it
        // to the page.
        processBodyBuffer(bodyBuffer);

        // Add the body to the page.
        pageBuffer.addOutputBuffer(bodyBuffer);

        // Pop the page buffer off the stack of output buffers.
        context.popOutputBuffer(pageBuffer);

    }

    protected void addXMLDeclaration(Document document) {
        XMLDeclaration declaration = domFactory.createXMLDeclaration();
        declaration.setEncoding(context.getCharsetName());
        document.setDeclaration(declaration);
    }

    /**
     * Populate a document with the contents of the buffers.
     * @param requiresProtocolString
     */
    protected void createDocument(boolean requiresProtocolString) {
        //  Create the document with the contents of the output buffers.
        document = domFactory.createDocument();
        Node next;

        if (requiresProtocolString) {
            doProtocolString(document);
        }

        // Add the initial buffer to the document.
        Element initial = getInitialBuffer().getRoot();
        for (Node child = initial.getHead(); child != null; child = next) {
            next = child.getNext();
            child.remove();
            document.addNode(child);
        }

        // Add the contents of the page to the document.
        Element page = getPageBuffer().getRoot();
        for (Node child = page.getHead(); child != null; child = next) {
            next = child.getNext();
            child.remove();
            document.addNode(child);
        }
    }

    /**
     * Transform the DOM if necessary by applying all registered DOM
     * transformers in their specified order.
     *
     * @throws IOException
     */
    private void transformDOM() throws IOException {

        // Hook into old mechanism, adding old transformer(s) to end
        // todo - replace the getDOMTransformer() with transformerSet
        DOMTransformer domTransformer = getDOMTransformer();
        if (domTransformer != null) {
            addDOMTransformer(domTransformer);
        }
        addDOMTransformer(new NullRemovingDOMTransformer());

        addXFormEmulationTransformer();

        //  If the protocol output requires preprocessing before we do anything
        // else with it then do so.

        // This will be very slow, but beats debugging in the IDE.
//        if (logger.isDebugEnabled()) {
//            logger.debug("before transforming:");
//            logger.debug(getSerializedDocument());
//        }

        for (Iterator transformerIterator = transformerSet.values().iterator();
             transformerIterator.hasNext(); ) {
            DOMTransformer transformer = (DOMTransformer)transformerIterator.next();

            document = transformer.transform(this, document);

//            if (logger.isDebugEnabled()) {
//                logger.debug("After transformer: " + transformer.getClass());
//                logger.debug(getSerializedDocument());
//            }
        }

        // If we need to output the contents after processing.
        if (transformerSet.size()>0) {
            logPageOutput("PROCESSED PAGE OUTPUT");
        }
    }

    /**
     * Return a StringWriter containing the <code>document</code> field. If the
     * document cannot be serialized to the StringWriter, the StringWriter will
     * contain the stack trace of the IOException.
     *
     * @return a StringWriter - not null
     */
    private String getSerializedDocument() {
        StrictStyledDOMHelper strictStyledDOMHelper =
                new StrictStyledDOMHelper(null);
        return strictStyledDOMHelper.render(document);
    }

    /**
     * Getting the XFormEmulationTransformer by reflection is messy, but
     * without refactoring Volantis and/or the runtime subsystem, this is the
     * clearest solution.
     *
     * @todo refactor to avoid this
     */
    private void addXFormEmulationTransformer() {
        try {
            ClassLoader classLoader = this.getClass().getClassLoader();
            Class implClass = classLoader.loadClass(
                    "com.volantis.mcs.xdime.xforms.XFormEmulationTransformer");
            DOMTransformer xformEmulationTransformer =
                    (DOMTransformer) implClass.newInstance();
            addDOMTransformer(xformEmulationTransformer);

            // if the transformer cannot be loaded, log the problem but
            // continue. This is because throwing an exception will cause the
            // tests to fail. Runtime needs refactoring!
        } catch (ClassNotFoundException e) {
            logger.error(exceptionLocalizer.format(
                    "xform-emulation-transformer-missing"));
        } catch (IllegalAccessException e) {
            logger.error(exceptionLocalizer.format(
                    "xform-emulation-transformer-missing"));
        } catch (InstantiationException e) {
            logger.error(exceptionLocalizer.format(
                    "xform-emulation-transformer-missing"));
        }
    }

    /**
     * Writing the page content includes the following: <ul> <li>Creating a
     * document.</li> <li>Adding initial markup.</li> <li>Transforming the
     * document.</li> <li>Dissecting the document.</li> <li>Writing the
     * document.</li> </ul>
     */
    protected void writeCanvasContent(PackageBodyOutput output,
                                      CanvasAttributes attributes)
        throws IOException, ProtocolException {

        createPage(attributes);

        // Create the document, only add the doc type if writing the head.
        createDocument(writeHead);

        // If we need to log the page output now before we do anything else
        // with it.
        logPageOutput("PAGE OUTPUT");

        transformDOM();

        final RequiredScriptModules requiredScriptModules =
            context.getRequiredScriptModules();
        requiredScriptModules.writeScriptElements();

        generateCSS();

        // Convert marked element nodes to text nodes just before document is written out.
        transformMarkedElementsToText();

        setResponseCacheHeaders();

        writeDocument(output);

        final MediaAgent mediaAgent = context.getMediaAgent(false);
        if (mediaAgent != null) {

            final EnvironmentContext environmentContext =
                context.getEnvironmentContext();
            final ResponseCachingDirectives cachingDirectives =
                environmentContext.getCachingDirectives();
            if (cachingDirectives != null && cachingDirectives.isEnabled()) {
                final Period timeToLive =
                    cachingDirectives.getTimeToLive();
                
                // According to HTTp specification we treat "never expires" as "expires in approx. 1 year.
                // See http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.21
                final long ttl = ((Period.INDEFINITELY.equals(timeToLive))) ?
                        365 * 24 * 60 * 60 :
                        timeToLive.inSeconds() + SAFETY_THRESHOLD_FOR_MAP_ITEMS;

                mediaAgent.ensureMinTimeToLive(ttl);
            }
        }
    }

    /**
     * Sets the appropriate response caching headers.
     *
     * <p>Before doing that, it first disables caching if dissection is used,
     * then copies the caching parameters from the pipeline context if caching
     * is enabled.</p>
     */
    private void setResponseCacheHeaders() {

        final EnvironmentContext environmentContext =
            context.getEnvironmentContext();
        final ResponseCachingDirectives cachingDirectives =
            environmentContext.getCachingDirectives();
        if (cachingDirectives != null) {
            // disable caching if dissection is used
            if (isDissectionNeeded()) {
                cachingDirectives.disable();
            }
            if (cachingDirectives.isEnabled() &&
                    !ResponseCachingDirectives.PRIORITY_NORMAL.isLower(
                        cachingDirectives.getExpiresPriority())) {
                final XMLPipelineContext pipelineContext =
                    environmentContext.getPipelineContext();
                final DependencyContext dependencyContext =
                    pipelineContext.getDependencyContext();
                if (dependencyContext.isTrackingDependencies()) {
                    final Dependency dependency =
                        dependencyContext.extractDependency();
                    if (dependency.getCacheability() == Cacheability.CACHEABLE) {
                        cachingDirectives.setMaxAge(dependency.getTimeToLive(),
                            ResponseCachingDirectives.PRIORITY_NORMAL);
                        cachingDirectives.enable();
                    } else {
                        cachingDirectives.disable();
                    }
                }
            }
        }
        environmentContext.applyCachingDirectives();
    }

    protected void writeDocument(PackageBodyOutput output)
            throws IOException, ProtocolException {

        boolean written = false;
        Writer writer = output.getWriter();

        // If dissecting is possible then do so.
        if (isDissectionNeeded()) {
           writeViaDissection(
                   new DOMDissectableDocumentImpl(this.document, getProtocolConfiguration()), 
                   new DOMDissectedContentHandlerImpl(writer));
        } else  {
            writeDocument(document, output);
        }
    }

    protected void writeViaDissection(DissectableDocument dissectableDocument, DissectedContentHandler dissHandler)
            throws IOException, ProtocolException {
        if (logger.isDebugEnabled()) {
            logger.debug("Using dissection");
        }

        DissectionContext dissectionContext = new RuntimeDissectionContext(
                context);

        Dissector dissector = getDissector();
        try {
            DissectedDocument dissectedDocument =
                    dissector.createDissectedDocument(dissectionContext,
                            getDissectionCharacteristics(),
                            dissectableDocument,
                            getDissectionURLManager(),
                            getDissectionDocInfo());

            RequestedShards shards = DissectionUtilities.getRequestedShards(
                    context.getFragmentationState(),
                    dissectedDocument);

            // Make sure that all the shards are populated, this is only needed
            // because we do not cache the dissected document.

            for (int d = 0; d < shards.getCount(); d += 1) {
                ShardIterator iterator
                        = dissectedDocument.getShardIterator(
                        dissectionContext, d);
                int shardIndex = shards.getShard(d);
                for (int i = 0;
                     iterator.hasMoreShards() && i < shardIndex;
                     i += 1) {
                    iterator.populateNextShard();
                }
            }

            dissector.serialize(dissectionContext, dissectedDocument, shards, dissHandler);

        } catch (DissectionException de) {
            throw new ProtocolException(
                    exceptionLocalizer.format("dissection-error"),
                    de);
        }
    }

    protected DissectionCharacteristics getDissectionCharacteristics() {
        DissectionCharacteristicsImpl characteristics = new DissectionCharacteristicsImpl();
        characteristics.setMaxPageSize(getMaxPageSize());
        return characteristics;
    }

    protected DocumentInformation getDissectionDocInfo() {
        DocumentInformationImpl information = new DocumentInformationImpl();
        information.setDocumentURL(context.getRequestURL(false));
        return information;
    }

    protected DissectionURLManager getDissectionURLManager() {
        return new RuntimeDissectionURLManager(context.getPageGenerationCache());
    }

    protected void generateCSS() throws IOException {
        if (cssReference != null) {
            // After transformation create a theme for the DOM.
            StyledDOMThemeExtractorFactory factory =
                StyledDOMThemeExtractorFactory.getDefaultInstance();

            final ExtractorConfiguration configuration =
                    protocolConfiguration.getExtractorConfiguration();

            StyledDOMThemeExtractor extractor =
                    factory.createExtractor(configuration, extractorContext);
            StyleSheet styleSheet = extractor.extract(document);

            StringWriter cssStringWriter = new StringWriter();
            StyleSheetRenderer renderer = getStyleSheetRenderer();
            MarinerPageContext marinerPageContext = getMarinerPageContext();

            // todo better detect empty style sheet here and avoid rendering,
            // and also remove dubious check for empty in renderStyleSheet?
            MarinerRequestContext requestContext =
                marinerPageContext.getRequestContext();
            com.volantis.mcs.css.renderer.RendererContext runtimeCSSRendererContext =
                    new RuntimeRendererContext(cssStringWriter,
                            renderer, this,
                            getProtocolConfiguration().getCssVersion());
            renderer.renderStyleSheet(styleSheet,
                                      runtimeCSSRendererContext);
            final String css = cssStringWriter.getBuffer().toString();
            if (logger.isDebugEnabled()) {
                logger.debug("Generated CSS is");
                logger.debug("====");
                logger.debug(css);
                logger.debug("====");
            }

            cssReference.updateMarkup(css);
        }

//        WritableCSSEntity entity = new WritableCSSEntity() {
//            public void write(Writer writer) throws IOException {
//                writer.write(css);
//            }
//        };
//
//        Cache cache = getMarinerPageContext().getVolantisBean().getCSSCache();
//        CacheIdentity identity = cache.store(new CSSCacheEntry(entity));
//        String url = "/CSSServlet?key=" + identity.getBase64KeyAsString();
//        System.out.println("Generated CSS is accessible from: " + url);
//        pageHead.addURLCssCandidate(new URLLinkCssCandidate(url));
    }

    /**
     * Write the montage page.
     */
    protected void writeMontageContent(PackageBodyOutput output,
                                       MontageAttributes attributes)
        throws IOException, ProtocolException {

        // Create the document, adding the doc type.
        createDocument(true);

        logPageOutput("PAGE OUTPUT");

        setResponseCacheHeaders();

        // Write out the page.
        writeDocument(document, output);
    }


    /**
     * Writing the AJAX Widget Response content.
     * This response is not full page but only few markups without
     * header and body. What is more styles are passed as 'style'
     * attribute of each element. This is only way to create acceptable
     * AJAX Response
     * @param output
     * @param attributes
     */
    protected void writeAJAXResponseContent(PackageBodyOutput output,
                                            ResponseBodyAttributes attributes)
        throws IOException, ProtocolException {

        createAJAXResponse(attributes);

        // Create the document, without adding a doc type.
        createDocument(false);

        transformDOM();

        // After transformation write styles as attribute 'style' value
        StyledDOMThemeExtractorFactory factory =
            StyledDOMThemeExtractorFactory.getDefaultInstance();

        final ExtractorConfiguration configuration =
            protocolConfiguration.getExtractorConfiguration();

        StyledDOMStyleAttributeRenderer rewriter =
            factory.createRenderer(configuration,extractorContext);

        rewriter.renderStyleAttributes(document);

        // Convert marked element nodes to text nodes just before document is written out.
        transformMarkedElementsToText();

        setResponseCacheHeaders();

        logPageOutput("WIDGET RESPONSE OUTPUT");
        writeDocument(document, output);
    }


    /**
     * Writes the document to the supplied <code>output</code>.
     *
     * @param document the document to be written.
     * @param output to be written to.
     *
     * @throws IOException if an IOError occurs.
     */
    private void writeDocument(Document document, PackageBodyOutput output)
        throws IOException {
        writeDocument(document, output.getWriter());
    }

    /**
     * Writes the document to the supplied <code>Writer</code>.
     *
     * @param document
     * @param writer
     * @throws IOException
     */
    private void writeDocument(final Document document, final Writer writer)
            throws IOException {
        DocumentOutputter documentOutputter = createDocumentOutputter(writer);
        documentOutputter.output(document);
        documentOutputter.flush();
    }

    /**
     * Logs the contents of {@link #document} as the page output, if page
     * output logging is enabled.
     *
     * @param description a description of the page output (in caps, please).
     * @throws IOException
     */
    protected void logPageOutput(String description) throws IOException {
        if (VolantisEnvironment.logPageOutput() &&
            logger.isDebugEnabled()) {
            StringWriter writer = new StringWriter();
            createDebugDocumentOutputter(writer).output(document);

            logger.debug(
                "\n" + description + " OUTPUT STARTS ================\n");
            logger.debug(writer.getBuffer().toString());
            logger.debug(
                "\n" + description + " OUTPUT ENDS ==================\n");
            logger.debug(
                    "\n" +
                    description + " DOCUMENT STARTS ================\n");
            StyledDocumentLogger.logDocument(document);
            logger.debug(
                    "\n" +
                    description + " DOCUMENT ENDS ==================\n");
        }
    }

    // --------------------------------------------------------------------------
    //   Canvas Page
    // --------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public void openCanvasPage(CanvasAttributes attributes)
        throws IOException, ProtocolException {

        // Push the page buffer onto the stack of output buffers.
        context.pushOutputBuffer(pageBuffer);

        // If the device has a limited page size then we need to split it
        // up. Some of the protocol methods need to do different things
        // if dissecting may be required so set a flag that they can test.
        int maxPageSize = getMaxPageSize();
        if (logger.isDebugEnabled()) {
            logger.debug("maxPageSize = " + maxPageSize);
        }
        if (maxPageSize != -1) {
            Dissector dissector = getDissector();
            if (dissector != null) {
                setDissecting(true);
            }
        }

        // We only need to try and dissect if it is possible and in many cases
        // it will not be possible because there is no dissecting pane in the
        // layout. Assume that dissection is never possible and if we process
        // a dissecting pane then we will mark it as being possible.
        setDissectionNeeded(false);

        setPageType(CANVAS_PAGE);
        // Only generate the header and open canvas if we intend to write
        // it out later.
        if (writeHead) {

            // Write the standard volantis header into the page.
            writeInitialHeader();

            writeOpenCanvas(attributes);

        } else if (inclusion) {
            if (logger.isDebugEnabled()) {
                logger.debug("Opening inclusion");
            }
            writeOpenInclusion(attributes);
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("HEAD: Not writing head or open canvas");
            }
        }

        // Create a buffer into which the body will be written.
        bodyBuffer = allocateOutputBuffer();

        // Push the body buffer onto the stack of output buffers.
        context.pushOutputBuffer(bodyBuffer);

        // Only write out the open body tag, if we have written
        // out the head.
        if (writeHead) {
            bodyAttributes = new BodyAttributes();
            if (attributes.getTitle() == null) {
                attributes.setTitle(attributes.getPageTitle());
            }
            bodyAttributes.setTitle(attributes.getPageTitle());
            bodyAttributes.setId(attributes.getId());
            bodyAttributes.setCanvasAttributes(attributes);
            bodyAttributes.setStyles(attributes.getStyles());

            writeOpenBody(bodyAttributes);
        }
    }


    // --------------------------------------------------------------------------
    //   Canvas Page
    // --------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public void openAJAXResponsePage(ResponseBodyAttributes attributes)
        throws IOException, ProtocolException {

        // Push the page buffer onto the stack of output buffers.
        context.pushOutputBuffer(pageBuffer);

        // If the device has a limited page size then we need to split it
        // up. Some of the protocol methods need to do different things
        // if dissecting may be required so set a flag that they can test.

        // Never dissect AJAX Respone
        setDissectionNeeded(false);

        setPageType(CANVAS_PAGE);

        // Create a buffer into which the body will be written.
        bodyBuffer = allocateOutputBuffer();

        // Push the body buffer onto the stack of output buffers.
        context.pushOutputBuffer(bodyBuffer);

    }


    /**
     * Subclasses can override this to do any protocol specific processing of
     * the body buffer before adding it to the page.
     */
    protected void processBodyBuffer(DOMOutputBuffer buffer) {
    }

    // --------------------------------------------------------------------------
    //   Inclusion Page
    // --------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public void openInclusionPage(CanvasAttributes attributes)
        throws IOException, ProtocolException {

        writeOpenInclusion(attributes);
    }

    // Javadoc inherited from super class.
    public void closeInclusionPage(CanvasAttributes attributes)
        throws IOException, ProtocolException {

        writeCloseInclusion(attributes);
    }

    // --------------------------------------------------------------------------
    //   Montage Page
    // --------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public void openMontagePage(MontageAttributes attributes)
        throws IOException {

        // Push the page buffer onto the stack of output buffers.
        context.pushOutputBuffer(pageBuffer);

        // set the pageType flag to the montage type
        setPageType(MONTAGE_PAGE);
        writeInitialHeader();
        writeOpenMontage(attributes);
    }

    // Javadoc inherited from super class.
    public void closeMontagePage(MontageAttributes attributes)
        throws IOException, ProtocolException {

        DeviceLayoutContext deviceLayoutContext
            = context.getDeviceLayoutContext();

        // Write out the page head.
        writePageHead();

        // Write out the content from the layout tree
        writeLayout(deviceLayoutContext);

        // Write out the postamble for the montage
        writeCloseMontage(attributes);

        // Pop the page buffer off the stack of output buffers.
        context.popOutputBuffer(pageBuffer);

        final RequiredScriptModules requiredScriptModules =
            context.getRequiredScriptModules();
        requiredScriptModules.writeScriptElements();

        super.closeMontagePage(attributes);
    }

    protected void openHead(DOMOutputBuffer dom, boolean empty) {
    }

    protected void closeHead(DOMOutputBuffer dom, boolean empty) {
    }

    /**
     * Write out the page head tag and its contents. The contents of the page
     * head may include meta tags and script elements.
     */
    protected void writePageHead() throws IOException {

        // Write out the head buffer
        DOMOutputBuffer head = getHeadBuffer();

        // Write out device specific META elements
        writeDeviceSpecificMetaElements();

        getPageHead().writeCssCandidates(this);

        final MarinerPageContext pageContext = getMarinerPageContext();
        final RequiredScriptModules requiredScriptModules =
            pageContext.getRequiredScriptModules();
        requiredScriptModules.createMarkerElement();

        boolean headEmpty = head.isEmpty();
        if (writeHead) {
            openHead(pageBuffer, headEmpty);
            // if there is anything in the head buffer, then output it
            if (!headEmpty) {
                pageBuffer.addOutputBuffer(head);
            }
        }

        // Write out the script buffer.
        DOMOutputBuffer script = getScriptBuffer();
        if (!script.isEmpty()) {
            // Check to see if the device supports JavaScript, if not
            // don't write the script tag.
            if (context.getBooleanDevicePolicyValue(SUPPORTS_JAVASCRIPT)) {
                ScriptAttributes sa = new ScriptAttributes();
                sa.setLanguage("JavaScript");
                sa.setType("text/javascript");

                openScript(pageBuffer, sa);
                pageBuffer.addOutputBuffer(script);
                closeScript(pageBuffer, sa);
            }
        }

        if (writeHead) {
            closeHead(pageBuffer, headEmpty);
        }
    }

    /**
     * Writes out META elements that are specific to the current
     * device.
     */
    private void writeDeviceSpecificMetaElements() {

        // Get the device specific META attributes
        String s = context.getDevicePolicyValue(FIXED_META_VALUES);

        DevicePolicyValueUtil policyValueUtil = new DevicePolicyValueUtil();
        Map policyValueMap = policyValueUtil.createMapFromPolicyValues(s);

        Set keyset = policyValueMap.keySet();
        Iterator keysetIter = keyset.iterator();
        while (keysetIter.hasNext()) {
            String currentName = (String)keysetIter.next();
            String currentContent = (String)policyValueMap.get(currentName);

            MetaAttributes metaAttributes = new MetaAttributes();
            metaAttributes.setName(currentName);
            metaAttributes.setContent(currentContent);

            // Write meta element to the head.
            doMeta(getHeadBuffer(), metaAttributes);
        }

        ViewportControlRenderer renderer = viewportControlModule.getRenderer(this);
        if (null != renderer) {
            renderer.renderMeta(this, getHeadBuffer());
        }
    }



    // ------------------------------------------------------------------------
    //   Body Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeOpenBody(BodyAttributes attributes)
        throws ProtocolException {

        DOMOutputBuffer dom = getCurrentBuffer();
        openBody(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeCloseBody(BodyAttributes attributes) {
        DOMOutputBuffer dom = getCurrentBuffer();
        closeBody(dom, attributes);
    }

    /**
     * Add the open body markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openBody(DOMOutputBuffer dom,
                            BodyAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Add the close body markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closeBody(DOMOutputBuffer dom,
                             BodyAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Canvas Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeOpenCanvas(CanvasAttributes attributes)
        throws IOException, ProtocolException {

        if (supportsCSS) {
            writeStyleSheet();
        }

        DOMOutputBuffer dom = getCurrentBuffer();
        openCanvas(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeCloseCanvas(CanvasAttributes attributes) {

        DOMOutputBuffer dom = getCurrentBuffer();
        closeCanvas(dom, attributes);
    }

    /**
     * Add the open canvas markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openCanvas(DOMOutputBuffer dom,
                              CanvasAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Add the close canvas markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closeCanvas(DOMOutputBuffer dom,
                               CanvasAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Comments
    // ------------------------------------------------------------------------

    /**
     * Add the comment markup to the specified DOMOutputBuffer.
     *
     * @param dom     The DOMOutputBuffer to use.
     * @param comment
     * @param value
     */
    private void addComment(DOMOutputBuffer dom,
                              String comment, boolean value) {

        if (value || VolantisEnvironment.commentsEnabled()) {
            addComment(dom, comment);
        }
    }

    /**
     * Add a comment node to the specified dom.
     *
     * @param dom     The DOMOutputBuffer to add to.
     * @param comment
     */
    protected void addComment(DOMOutputBuffer dom,
                              String comment) {

        dom.addComment(comment);
    }

    // ------------------------------------------------------------------------
    //   Inclusion Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeOpenInclusion(CanvasAttributes attributes)
        throws IOException, ProtocolException {

        if (supportsCSS && context.isOutOfContextInclusion()) {
            writeStyleSheet();
        }

        DeviceLayoutContext deviceLayoutContext
            = context.getDeviceLayoutContext();
        DOMOutputBuffer dom
            = (DOMOutputBuffer)deviceLayoutContext.getContentBuffer(true);

        // Push it onto the stack so that child elements appear in the correct
        // place.
        context.pushOutputBuffer(dom);

        openInclusion(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeCloseInclusion(CanvasAttributes attributes)
        throws IOException, ProtocolException {

        DeviceLayoutContext deviceLayoutContext
            = context.getDeviceLayoutContext();

        DOMOutputBuffer dom
            = (DOMOutputBuffer)deviceLayoutContext.getContentBuffer(true);

        // Save the insertion point so that the included page may restore it
        // later.
        dom.saveInsertionPoint();

        closeInclusion(dom, attributes);

        // Pop the buffer off the stack.
        context.popOutputBuffer(dom);

        // If we are not in a region then we need to write the layout.
        if (context.getEnclosingRegionInstance() == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Generating overlay inclusion which is not in a region"
                             + " in its own card");
            }

            writeLayout(deviceLayoutContext);
        }
    }

    /**
     * Add the open inclusion markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openInclusion(DOMOutputBuffer dom,
                                 CanvasAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Add the close inclusion markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closeInclusion(DOMOutputBuffer dom,
                                  CanvasAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Initial header.
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public void writeInitialHeader() {

        DOMOutputBuffer initial = getInitialBuffer();

        for (int i = 0; i < fixedHeader.length; i += 1) {
            addComment(initial, fixedHeader[i], false);
        }

        Volantis volantisBean = context.getVolantisBean();
        String pageHeadingMsg = volantisBean.getPageHeadingMsg();

        // Only write out the page heading if the heading actually exists.
        if ((pageHeadingMsg != null) && pageHeadingMsg.trim().length() > 0) {
            addComment(initial, pageHeadingMsg, true);
        }
        addComment(initial, blankLine, false);
    }

    // ------------------------------------------------------------------------
    //   Link Element
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    //   Montage Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeOpenMontage(MontageAttributes attributes) {

        DOMOutputBuffer dom = getCurrentBuffer();
        openMontage(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeCloseMontage(MontageAttributes attributes) {

        DOMOutputBuffer dom = getCurrentBuffer();
        closeMontage(dom, attributes);
    }

    /**
     * Add the open montage markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openMontage(DOMOutputBuffer dom,
                               MontageAttributes attributes) {
    }

    /**
     * Add the close montage markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closeMontage(DOMOutputBuffer dom,
                                MontageAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Protocol string
    // ------------------------------------------------------------------------

    public void writeProtocolString() {
        doProtocolString(document);
    }

    public void writeProtocolString(Document document) {
        doProtocolString(document);
    }

    protected void doProtocolString(Document document) {
    }

    // ------------------------------------------------------------------------
    //   Style Element
    //   NOTE: there is no XDIME/PAPI style element. These methods are used only
    //   by the inline stylesheet support.
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public void writeOpenStyle(OutputBuffer out,
                               StyleAttributes attributes) {

        openStyle((DOMOutputBuffer)out, attributes);
    }

    // Javadoc inherited from super class.
    public void writeCloseStyle(OutputBuffer out,
                                StyleAttributes attributes) {

        closeStyle((DOMOutputBuffer)out, attributes);
    }

    /**
     * Add the open style markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openStyle(DOMOutputBuffer dom,
                             StyleAttributes attributes) {
    }

    /**
     * Add the close style markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closeStyle(DOMOutputBuffer dom,
                              StyleAttributes attributes) {
    }

    /**
     * Opens a style element with type and optionally media attributes
     * additionally to the attributes specified in the argument.
     * The value of the type attribute is "text/css", the value of the media
     * attribute is read from the protocol.
     *
     * If openComment is true, adds a "&lt;!--\n" literal to the element.
     *
     * @param dom the output buffer where the style element will be added to
     * @param attributes the style attributes for the element
     * @param openComment if true a literal is added to the style element
     * starting a comment.
     */
    protected void performDefaultOpenStyle(final DOMOutputBuffer dom,
                                           final StyleAttributes attributes,
                                           final boolean openComment) {

        final Element element = dom.openStyledElement("style", attributes);
        element.setAttribute("type", "text/css");
        final ProtocolConfiguration configuration = getProtocolConfiguration();
        if (configuration != null) {
            final String media = configuration.getCSSMedia();
            if (media != null) {
                element.setAttribute("media", media);
            }
        }
        if (openComment) {
            // Output a comment marker around the CSS content to prevent those
            // browsers that can't cope with CSS from outputting the content as
            // readable content. Browsers that can cope with CSS will ignore the
            // comment.
//            dom.appendLiteral("<!--\n");
            dom.openComment();
        }
    }

    protected void performDefaultCloseStyle(
            DOMOutputBuffer dom, boolean closeComment) {
        
        if (closeComment) {
            dom.closeComment();
        }
        dom.closeElement("style");
    }

    // ------------------------------------------------------------------------
    //   Slide Element of MMS SMIL
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeOpenSlide(SlideAttributes attributes) {
        DOMOutputBuffer dom = getCurrentBuffer();
        openSlide(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeCloseSlide(SlideAttributes attributes) {
        DOMOutputBuffer dom = getCurrentBuffer();
        closeSlide(dom, attributes);
    }

    /**
     * Add the open slide markup to the specified DOMOutputBuffer. A slide
     * consists of two panes one for some text and the other for an image. A
     * temporal format iterator would display several slides one after the
     * other.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */

    protected void openSlide(DOMOutputBuffer dom,
                             SlideAttributes attributes) {
    }

    /**
     * Add the close slide markup to the specified DOMOutputBuffer. A slide
     * consists of two panes one for some text and the other for an image. A
     * temporal format iterator would display several slides one after the
     * other.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closeSlide(DOMOutputBuffer dom,
                              SlideAttributes attributes) {
    }

    // ==========================================================================
    //   Dissection methods
    // ==========================================================================

    /**
     * Get the dissector.
     *
     * @return The dissector, if null then this protocol does not support
     *         dissection.
     */
    private Dissector getDissector() {
        return protocolConfiguration.getDissector();
    }

    protected DOMTransformer getDOMTransformer() {
        return null;
    }

    /**
     * Add a new transformer with the specified priority.
     * Priority deternmines the order the transformers are applied.
     *
     * @param priority priority of new transformer as an integer
     * @param newTransformer transformer to add
     */
    private void addDOMTransformer(int priority,
                                     DOMTransformer newTransformer) {

        if (newTransformer == null) {
            throw new NullPointerException();
        }

        Integer priorityObject = new Integer(priority);

        while (transformerSet.containsKey(priorityObject)) {
            priority++;
            priorityObject = new Integer(priority);
        }
        transformerSet.put(priorityObject, newTransformer);
    }

    /**
     * Add a new transformer which should be run after all the other
     * transformers that have already been added.
     *
     * @param newTransformer transformer to add
     */
    protected void addDOMTransformer(DOMTransformer newTransformer) {
        addDOMTransformer(1, newTransformer);
    }

    public Document getDocument() {
        return document;
    }

    // ==========================================================================
    //   Text methods
    // ==========================================================================

    // ==========================================================================
    //   Layout / format methods
    // ==========================================================================

    // ------------------------------------------------------------------------
    //   Column Iterator Pane Format
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final
    void writeOpenColumnIteratorPane(ColumnIteratorPaneAttributes attributes) {

        DOMOutputBuffer dom = getCurrentBuffer();
        openColumnIteratorPane(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final
    void writeCloseColumnIteratorPane(ColumnIteratorPaneAttributes attributes) {

        DOMOutputBuffer dom = getCurrentBuffer();
        closeColumnIteratorPane(dom, attributes);
    }

    /**
     * Add the open column iterator pane markup to the specified
     * DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected
    void openColumnIteratorPane(DOMOutputBuffer dom,
                                ColumnIteratorPaneAttributes attributes) {
    }

    /**
     * Add the close column iterator pane markup to the specified
     * DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected
    void closeColumnIteratorPane(DOMOutputBuffer dom,
                                 ColumnIteratorPaneAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Column Iterator Pane Format Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeOpenColumnIteratorPaneElement(
        ColumnIteratorPaneAttributes attributes) {

        DOMOutputBuffer dom = getCurrentBuffer();
        openColumnIteratorPaneElement(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeCloseColumnIteratorPaneElement(
        ColumnIteratorPaneAttributes attributes) {

        DOMOutputBuffer dom = getCurrentBuffer();
        closeColumnIteratorPaneElement(dom, attributes);
    }

    /**
     * Add the open column iterator pane element markup to the specified
     * DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected
    void openColumnIteratorPaneElement(DOMOutputBuffer dom,
                                       ColumnIteratorPaneAttributes attributes) {
    }

    /**
     * Add the close column iterator pane element markup to the specified
     * DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected
    void closeColumnIteratorPaneElement(DOMOutputBuffer dom,
                                        ColumnIteratorPaneAttributes attributes) {
    }

    /**
     * Write the contents of the column iterator pane element directly to the
     * page.
     *
     * @param buffer The contents of the column iterator pane element.
     */
    public void writeColumnIteratorPaneElementContents(OutputBuffer buffer) {

        DOMOutputBuffer dom = getCurrentBuffer();
        dom.addOutputBuffer((DOMOutputBuffer)buffer);
    }

    // ------------------------------------------------------------------------
    //   Dissecting Pane Format
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final
    void writeOpenDissectingPane(DissectingPaneAttributes attributes) {

        DOMOutputBuffer dom = getCurrentBuffer();
        openDissectingPane(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final
    void writeCloseDissectingPane(DissectingPaneAttributes attributes)
        throws ProtocolException {

        DOMOutputBuffer dom = getCurrentBuffer();
        closeDissectingPane(dom, attributes);
    }

    /**
     * Add the open dissecting pane markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected
    void openDissectingPane(DOMOutputBuffer dom,
                            DissectingPaneAttributes attributes) {
    }

    /**
     * Add the close dissecting pane markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected
    void closeDissectingPane(DOMOutputBuffer dom,
                             DissectingPaneAttributes attributes)
        throws ProtocolException {
    }

    // ------------------------------------------------------------------------
    //   Form Format
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeOpenForm(FormAttributes attributes) {

        Form form = attributes.getForm();
        FormInstance formInstance = (FormInstance)context.getFormatInstance(
            form,
            NDimensionalIndex.ZERO_DIMENSIONS);

        // Push the form's content buffer on the stack of output buffers.
        DOMOutputBuffer dom
            = (DOMOutputBuffer)formInstance.getContentBuffer(true);
        context.pushOutputBuffer(dom);

        openForm(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeCloseForm(FormAttributes attributes) {

        Form form = attributes.getForm();
        FormInstance formInstance = (FormInstance)context.getFormatInstance(
            form,
            NDimensionalIndex.ZERO_DIMENSIONS);

        DOMOutputBuffer dom = getCurrentBuffer();
        closeForm(dom, attributes);

        // Pop the form context's content buffer off the stack of output buffers.
        context.popOutputBuffer(formInstance.getContentBuffer(true));

        // Get the buffer into which the form context's content
        // buffer should be added.
        DOMOutputBuffer containingBuffer = getCurrentBuffer();

        // Add the form context's content buffer.
        containingBuffer.addOutputBuffer(dom);
    }

    /**
     * Add the open form markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openForm(DOMOutputBuffer dom,
                            FormAttributes attributes) {
    }

    /**
     * Add the close form markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closeForm(DOMOutputBuffer dom,
                             FormAttributes attributes) {
    }

    /**
     * Write the form preamble buffer directly to the page.
     *
     * @param buffer The buffer to write.
     */
    public void writeFormPreamble(OutputBuffer buffer)
        throws IOException {

        DOMOutputBuffer dom = getCurrentBuffer();
        dom.addOutputBuffer((DOMOutputBuffer)buffer);
    }

    /**
     * Write the form postamble buffer directly to the page.
     *
     * @param buffer The buffer to write.
     */
    public void writeFormPostamble(OutputBuffer buffer)
        throws IOException {

        DOMOutputBuffer dom = getCurrentBuffer();
        dom.addOutputBuffer((DOMOutputBuffer)buffer);
    }


    /**
     * write the content of an xfcontent buffer. The attributes contain the
     * pane to write the content to and an OutputBuffer holding the content
     * that is to be written.
     */
    protected void writeContent(XFContentAttributes attributes)
            throws ProtocolException {
        Pane pane = attributes.getPane();
        OutputBuffer buffer = attributes.getOutputBuffer();

        if (pane == null) {
            logger.error("null-xfcontent-pane");
            return;
        }

        if (buffer == null) {
            logger.error("null-buffer");
            return;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("XFContent:" + buffer + " pane:" + pane.getName());
        }

        DOMOutputBuffer dom = getCurrentBuffer(pane);
        SpanAttributes spanAttributes = new SpanAttributes();
        spanAttributes.setStyles(attributes.getStyles());
        openSpan(dom, spanAttributes);
        dom.addOutputBuffer((DOMOutputBuffer)buffer);
        closeSpan(dom, spanAttributes);
    }

    /**
     * Set any attribute values that should be applied to all form fields.
     *
     * @param element       on which to set the attribute values
     * @param attributes    from which to determine the values to be set
     */
    public void addFormFieldAttributes(Element element,
                                       XFFormFieldAttributes attributes) {
        // this will only be non null if we are representing an xform as xfform
        final String containingXFForm = attributes.getContainingXFFormName();
        if (containingXFForm != null) {
            element.setAttribute(ProtocolConstants.CONTAINING_XFFORM_ATTRIBUTE,
                    containingXFForm);
        }
    }

    /**
     * Create the XForm emulation element with the supplied parameters.
     *
     * @param formName      String id of the form
     * @param fd            object which describes this emulated form
     * @return Element representing the xform emulation element. May be null if
     * the protocol does not emulate the form element.
     */
    public Element createXFormEmulationElement(String formName,
                                               EmulatedXFormDescriptor fd) {
        return null;
    }

    /**
     * If this element is the type of element used to emulate XForms in this
     * protocol.
     *
     * @return true if this element is the type of element used to emulate
     * XForms in this control and false otherwise.
     */
    public boolean isXFormEmulationElement(Element element) {
        return false;
    }

    /**
     * If this element is the type of element used to emulate implicit data in
     * this protocol.
     *
     * @return true if this element is the type of element used to emulate
     * implicit data in this control and false otherwise.
     */
    public boolean isImplicitEmulationElement(Element element) {
        return false;
    }

    /**
     * Create the vform element which should appear immediately after the xform
     * emulation element.
     *
     * @param formSpecifier identifier of the form
     * @return Element representing the vform element
     */
    public Element createVFormElement(String formSpecifier) {
        return null;
    }

    /**
     * For some protocols the action element may need to contain information
     * about the form that is not known at the time the element is created.
     * E.g. {@link com.volantis.mcs.protocols.wml.WMLRoot} will need the action
     * url and method to be populated, and the post fields generated.
     *
     * @param element   Action element whose fields may need to be populated.
     * @param fd        emulated xform descriptor containing info about the form
     */
    public void populateEmulatedActionElement(Element element,
                                              EmulatedXFormDescriptor fd) {
        // do nothing by default.
    }

    /**
     * <p>Form links (i.e. links inserted to navigate between form fragments)
     * are inserted as a child of the form element (or whatever the equivalent
     * is for a particular protocol).</p>
     * <p/>
     *
     * <p>The complication is that XDIME2 forms are based on XForms and so
     * specify form controls but no surrounding form element. Therefore the
     * appropriate form element (and any hidden controls) for the protocol is
     * inserted at the common ancestor of the first and last control in a
     * form (see XFormEmulator).</p>
     * <p/>
     *
     * <p>The form and hidden controls have no visual impact, and so it doesn't
     * matter what the form's containing element is. However, form links do
     * have visual impact, and so their containing element should be valid
     * (see vbm 2007032108).</p>
     *
     * <p>For example, the following fragment is valid (despite the fact that
     * the form element appears directly inside the table element):<br/>
     * &lt;table&gt;<br/>
     *     &lt;form&gt;<br/>
     *        &lt;input name="vform" type="hidden" value="s2"/&gt;<br/>
     *        &lt;tr&gt;<br/>
     *            &lt;td&gt; some mixed content &lt;/td&gt;<br/>
     *        &lt;/tr&gt;<br/>
     *     &lt;/form&gt;<br/>
     * &lt;/table&gt;<br/>
     * However if form links appear as the last children of the form element,
     * they will not necessarily appear after their sibling elements. For
     * example, firefox displays them in the first available cell.</p>
     *
     * <p>This ensures that any known invalid containing elements are replaced
     * by a valid containing element (i.e. in the case above, a table row and
     * cell would be added before the form links). In most cases, the supplied
     * parent element will be deemed valid.
     * <p/>
     *
     * @param parentName        name of the parent element whose fitness to
     *                          contain a link should be assessed (this is the
     *                          name of the element into which the new form
     *                          element has been inserted at first, and then is
     *                          just parent.getName() on subsequent calls)
     * @param parent            which will be used as the parent of any new
     *                          required containing elements.
     * @param top               true if the form link should be inserted at the
     *                          top of the form, false if at the bottom
     * @return Element which can validly contain a form link.
     */
    public Element validateFormLinkParent(String parentName,
                                          Element parent,
                                          boolean top) {
        return parent;
    }

    // ------------------------------------------------------------------------
    //   Grid Format
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeOpenGrid(GridAttributes attributes) {

        DOMOutputBuffer dom = getCurrentBuffer();
        openGrid(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeCloseGrid(GridAttributes attributes) {

        DOMOutputBuffer dom = getCurrentBuffer();
        closeGrid(dom, attributes);
    }

    /**
     * Add the open grid markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openGrid(DOMOutputBuffer dom,
                            GridAttributes attributes) {
    }

    /**
     * Add the close grid markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closeGrid(DOMOutputBuffer dom,
                             GridAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Grid Child Format
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeOpenGridChild(GridChildAttributes attributes) {

        DOMOutputBuffer dom = getCurrentBuffer();
        openGridChild(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeCloseGridChild(GridChildAttributes attributes) {

        DOMOutputBuffer dom = getCurrentBuffer();
        closeGridChild(dom, attributes);
    }

    /**
     * Add the open grid child markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openGridChild(DOMOutputBuffer dom,
                                 GridChildAttributes attributes) {
    }

    /**
     * Add the close grid child markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closeGridChild(DOMOutputBuffer dom,
                                  GridChildAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Grid Row Format
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeOpenGridRow(GridRowAttributes attributes) {

        DOMOutputBuffer dom = getCurrentBuffer();
        openGridRow(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeCloseGridRow(GridRowAttributes attributes) {

        DOMOutputBuffer dom = getCurrentBuffer();
        closeGridRow(dom, attributes);
    }

    /**
     * Add the open grid row markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openGridRow(DOMOutputBuffer dom,
                               GridRowAttributes attributes) {
    }

    /**
     * Add the close grid row markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closeGridRow(DOMOutputBuffer dom,
                                GridRowAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Layout Format
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeOpenLayout(LayoutAttributes attributes) {

        DeviceLayoutContext deviceLayoutContext
            = attributes.getDeviceLayoutContext();

        // Add the preamble buffer to the current buffer.
        DOMOutputBuffer buffer
            = (DOMOutputBuffer)deviceLayoutContext.getPreambleBuffer(false);
        if (buffer != null) {
            getCurrentBuffer().addOutputBuffer(buffer);
        }

        // Push the device layout context's content buffer on the stack of
        // output buffers.
        DOMOutputBuffer dom
            = (DOMOutputBuffer)deviceLayoutContext.getContentBuffer(true);
        context.pushOutputBuffer(dom);


        if (deviceLayoutContext.getIncludingDeviceLayoutContext() != null
            && dom.savedInsertionPoint()) {

            dom.restoreInsertionPoint();
        }
        openLayout(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeCloseLayout(LayoutAttributes attributes) {

        DeviceLayoutContext deviceLayoutContext
            = attributes.getDeviceLayoutContext();

        DOMOutputBuffer dom = getCurrentBuffer();
        closeLayout(dom, attributes);

        // Pop the device layout context's content buffer off the stack of
        // output buffers, this will make sure that the current buffer and the
        // device layout context's content buffer are one and the same.
        context.popOutputBuffer(deviceLayoutContext.getContentBuffer(true));

        // Get the buffer into which the device layout context's content
        // buffer should be added.
        DOMOutputBuffer containingBuffer = getCurrentBuffer();

        // Add the device layout context's content buffer.
        containingBuffer.addOutputBuffer(dom);

        // Add the postamble buffer to the current buffer.
        DOMOutputBuffer buffer
            = (DOMOutputBuffer)deviceLayoutContext.getPostambleBuffer(false);
        if (buffer != null) {
            containingBuffer.addOutputBuffer(buffer);
        }
    }

    /**
     * Add the open layout markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openLayout(DOMOutputBuffer dom,
                              LayoutAttributes attributes) {
    }

    /**
     * Add the close layout markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closeLayout(DOMOutputBuffer dom,
                               LayoutAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Pane Format
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeOpenPane(PaneAttributes attributes) {

        DOMOutputBuffer dom = getCurrentBuffer();
        openPane(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeClosePane(PaneAttributes attributes) {

        DOMOutputBuffer dom = getCurrentBuffer();
        closePane(dom, attributes);
    }

    /**
     * Add the open pane markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openPane(DOMOutputBuffer dom,
                            PaneAttributes attributes) {
    }

    /**
     * Add the close pane markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closePane(DOMOutputBuffer dom,
                             PaneAttributes attributes) {
    }

    /**
     * Write the pane contents buffer directly to the page.
     *
     * @param buffer The buffer to write.
     */
    public void writePaneContents(OutputBuffer buffer)
        throws IOException {

        DOMOutputBuffer dom = getCurrentBuffer();
        dom.addOutputBuffer((DOMOutputBuffer)buffer/* trim */);
    }

    // ------------------------------------------------------------------------
    //   Row Iterator Pane Format
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final
    void writeOpenRowIteratorPane(RowIteratorPaneAttributes attributes) {

        DOMOutputBuffer dom = getCurrentBuffer();
        openRowIteratorPane(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final
    void writeCloseRowIteratorPane(RowIteratorPaneAttributes attributes) {

        DOMOutputBuffer dom = getCurrentBuffer();
        closeRowIteratorPane(dom, attributes);
    }

    /**
     * Add the open row iterator pane markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected
    void openRowIteratorPane(DOMOutputBuffer dom,
                             RowIteratorPaneAttributes attributes) {
    }

    /**
     * Add the close row iterator pane markup to the specified
     * DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected
    void closeRowIteratorPane(DOMOutputBuffer dom,
                              RowIteratorPaneAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Row Iterator Pane Format Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final
    void writeOpenRowIteratorPaneElement(RowIteratorPaneAttributes attributes) {

        DOMOutputBuffer dom = getCurrentBuffer();
        openRowIteratorPaneElement(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeCloseRowIteratorPaneElement(
        RowIteratorPaneAttributes attributes) {

        DOMOutputBuffer dom = getCurrentBuffer();
        closeRowIteratorPaneElement(dom, attributes);
    }

    /**
     * Add the open row iterator pane element markup to the specified
     * DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openRowIteratorPaneElement(DOMOutputBuffer dom,
                                              RowIteratorPaneAttributes attributes) {
    }

    /**
     * Add the close row iterator pane element markup to the specified
     * DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected
    void closeRowIteratorPaneElement(DOMOutputBuffer dom,
                                     RowIteratorPaneAttributes attributes) {
    }

    /**
     * Write the contents of the row iterator pane element directly to the
     * page.
     *
     * @param buffer The contents of the row iterator pane element.
     */
    public void writeRowIteratorPaneElementContents(OutputBuffer buffer)
        throws IOException {

        DOMOutputBuffer dom = getCurrentBuffer();
        dom.addOutputBuffer((DOMOutputBuffer)buffer/* trim */);
    }

    // ------------------------------------------------------------------------
    //   Segment Format
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeOpenSegment(SegmentAttributes attributes) {

        DOMOutputBuffer dom = getCurrentBuffer();
        openSegment(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeCloseSegment(SegmentAttributes attributes) {

        DOMOutputBuffer dom = getCurrentBuffer();
        closeSegment(dom, attributes);
    }

    /**
     * Add the open segment markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openSegment(DOMOutputBuffer dom,
                               SegmentAttributes attributes) {
    }

    /**
     * Add the close segment markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closeSegment(DOMOutputBuffer dom,
                                SegmentAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Segment Grid Format
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeOpenSegmentGrid(SegmentGridAttributes attributes) {

        DOMOutputBuffer dom = getCurrentBuffer();
        openSegmentGrid(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeCloseSegmentGrid(SegmentGridAttributes attributes) {

        DOMOutputBuffer dom = getCurrentBuffer();
        closeSegmentGrid(dom, attributes);
    }

    /**
     * Add the open segment grid markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openSegmentGrid(DOMOutputBuffer dom,
                                   SegmentGridAttributes attributes) {
    }

    /**
     * Add the close segment grid markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closeSegmentGrid(DOMOutputBuffer dom,
                                    SegmentGridAttributes attributes) {
    }

    // ==========================================================================
    //   Navigation methods.
    // ==========================================================================

    // ------------------------------------------------------------------------
    //   Anchor Element
    // ------------------------------------------------------------------------

    protected void doAnchor(DOMOutputBuffer dom,
                            AnchorAttributes attributes)
        throws ProtocolException {

        LinkAssetReference hrefObject;
        String href = null;

        if ((hrefObject = attributes.getHref()) != null) {
            href = getRewrittenLinkFromObject(
                    hrefObject, attributes.getSegment() != null);
            if (href == null) {
                // See if we have a text component fallback
                String fallbackText = getTextFallbackFromLink(hrefObject);

                if (fallbackText == null) {
                    addAnchorContents(dom, attributes);
                } else {
                    dom.appendEncoded(fallbackText);
                }

                return;
            }

            attributes.setHref(href);
        } else {
            // @todo this gross hack breaks the packager! stop it!
            attributes.setHref(" ");
        }

        openAnchor(dom, attributes);

        // Add the contents of the anchor.
        addAnchorContents(dom, attributes);

        closeAnchor(dom, attributes);

        // allow subclasses to to perform additional processing
        anchorAddedOK(attributes, href);
    }

    /**
     * Add the contents of the anchor.
     */
    private void addAnchorContents(DOMOutputBuffer dom,
                                     AnchorAttributes attributes) {

        Object contents = attributes.getContent();
        if (contents instanceof DOMOutputBuffer) {
            DOMOutputBuffer contentBuffer = (DOMOutputBuffer)contents;
            dom.addOutputBuffer(contentBuffer);
        } else {
            if (contents != null) {
                dom.appendEncoded(contents.toString());
            }
        }
    }

    /**
     * This method is called when the content of the anchor is about to be
     * processed.
     */
    protected void enteredAnchorBody() {
    }

    /**
     * This method is called when the content of the anchor has been
     * processed.
     */
    protected void exitedAnchorBody() {
    }

    /**
     * Method that subclasses should override if they wish to perform
     * additional processing for an anchor tag
     *
     * @param attributes   the AnchorAttributes object
     * @param resolvedHref the resolved url
     */
    protected void anchorAddedOK(AnchorAttributes attributes,
                                 String resolvedHref) {
    }

    // Javadoc inherited from super class.
    public void writeOpenAnchor(AnchorAttributes attributes) {

        context.pushOutputBuffer(allocateOutputBuffer());

        enteredAnchorBody();
    }

    // Javadoc inherited from super class.
    public void writeCloseAnchor(AnchorAttributes attributes)
        throws ProtocolException {

        exitedAnchorBody();

        // Get the output buffer which captured all the output generated
        // by nested elements.
        DOMOutputBuffer outputBuffer = getCurrentBuffer();
        attributes.setContent(outputBuffer);

        // Remove the output buffer from the stack.
        context.popOutputBuffer(outputBuffer);

        DOMOutputBuffer dom = getCurrentBuffer();
        
        // Check, whether this element needs finalization. It may happen in case
        // href attribute may not be provided at this very moment.
        if (!attributes.needsFinalizer()) {
            // In case finalization is not required, do the anchor right now.
        doAnchor(dom, attributes);
            
        } else {
            // In case finalization is required, defer doing the anchor until
            // finalization. Right now, insert only a placeholder element, so we
            // can go back there in finalizer.
            final Element placeHolderElement = dom.addElement("placeholder");

            attributes.setFinalizer(new ElementFinalizer() {
                public void finalizeElement(MAPAttributes attributes) 
                        throws ProtocolException {
                    finalizeAnchor(placeHolderElement, 
                            (AnchorAttributes) attributes);
                }
            });
        }
    }
    
    /**
     * Finalizes anchor element.
     *
     * @param placeHolderElement
     * @param attributes
     * @throws ProtocolException
     */
    private void finalizeAnchor(Element placeHolderElement,
            AnchorAttributes attributes) throws ProtocolException {
        DOMOutputBuffer dom = (DOMOutputBuffer)
                getOutputBufferFactory().createOutputBuffer();
    
        doAnchor(dom, attributes);
    
        placeHolderElement.replaceWith(dom.getRoot().removeChildren());
    }

    // JavaDoc inherited
    public void openAnchor(DOMOutputBuffer dom, AnchorAttributes attributes)
        throws ProtocolException {
    }

    // JavaDoc inherited
    public void closeAnchor(DOMOutputBuffer dom, AnchorAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Default Segment Link Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeDefaultSegmentLink(AnchorAttributes attributes)
        throws ProtocolException {

        DOMOutputBuffer dom = getCurrentBuffer();
        doDefaultSegmentLink(dom, attributes);
    }

    /**
     * Add the default segment link markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void doDefaultSegmentLink(DOMOutputBuffer dom,
                                        AnchorAttributes attributes)
        throws ProtocolException {
    }

    // ------------------------------------------------------------------------
    //   Fragment Link Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeFragmentLink(FraglinkAttributes attributes)
        throws ProtocolException {

        DOMOutputBuffer dom = getCurrentBuffer();
        FragmentLinkRenderer renderer = getFragmentLinkRenderer(attributes);
        if (renderer != null) {
            renderer.doFragmentLink(dom, attributes);
        }
    }

    // ------------------------------------------------------------------------
    //   Shard Link Element
    // ------------------------------------------------------------------------

    /**
     * Add the shard link markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    public final void writeShardLink(DOMOutputBuffer dom,
                                     ShardLinkAttributes attributes)
        throws ProtocolException {
        doShardLink(dom, attributes);
    }

    /**
     * Add the shard link markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void doShardLink(DOMOutputBuffer dom,
                               ShardLinkAttributes attributes)
        throws ProtocolException {
    }

    // ==========================================================================
    //   Dialling methods.
    // ==========================================================================
    public void writeOpenPhoneNumber(PhoneNumberAttributes attributes) {
        context.pushOutputBuffer(allocateOutputBuffer());
    }

    public void writeClosePhoneNumber(PhoneNumberAttributes attributes)
        throws ProtocolException {
        // Get the output buffer which captured all the output generated
        // by nested elements.
        DOMOutputBuffer outputBuffer = getCurrentBuffer();

        // Store it in the attributes for later use (in addPhoneNumberContents)
        attributes.setContent(outputBuffer);

        // Remove the output buffer from the stack.
        context.popOutputBuffer(outputBuffer);

        // Perform all required markup rendering
        doPhoneNumber(getCurrentBuffer(), attributes);
    }

    /**
     * Generate the required markup for the given phone number input. The
     * attributes are assumed to include the required content as well as the
     * full number and any other attributes appropriate to the element. If the
     * device doesn't support dialling links only the number contents are
     * rendered.
     *
     * @param dom        the output buffer to write to
     * @param attributes the phone number attributes defining what is required
     */
    private void doPhoneNumber(DOMOutputBuffer dom,
                                 PhoneNumberAttributes attributes)
        throws ProtocolException {
        // Ensure that the attributes are updated to contain the resolved
        // full number and qualified full number values
        resolvePhoneNumberAttributes(attributes);

        // Generate the required markup as required
        if (supportsDiallingLinks) {
            openPhoneNumber(dom, attributes);

        // Always output the number contents
        addPhoneNumberContents(dom, attributes);

            closePhoneNumber(dom, attributes);
        } else {
            // surround in span element so that the text is styled as requested.
            final SpanAttributes spanAttributes = new SpanAttributes();
            spanAttributes.setStyles(attributes.getStyles());
            writeOpenSpan(spanAttributes);
            // Always output the number contents
            addPhoneNumberContents(dom, attributes);
            // close the span element.
            writeCloseSpan(spanAttributes);
        }
    }

    /**
     * Add the contents of the phone number. If the content is null or empty
     * then the phone number itself is used (if available).
     *
     * @param dom        the output buffer to which the content is to be
     *                   written
     * @param attributes the attributes from which the content and/or full
     *                   number can be obtained
     */
    protected void addPhoneNumberContents(DOMOutputBuffer dom,
                                          PhoneNumberAttributes attributes) throws ProtocolException {
        Object contents = attributes.getContent();

        if (contents instanceof DOMOutputBuffer) {
            DOMOutputBuffer contentBuffer = (DOMOutputBuffer)contents;

            if (!contentBuffer.isEmpty() && !contentBuffer.isWhitespace()) {
                dom.addOutputBuffer(contentBuffer);
            } else {
                String defaultContents = attributes.getDefaultContents();

                if (defaultContents != null) {
                    dom.appendEncoded(defaultContents);
                }
            }

        } else if (contents != null) {
            dom.appendEncoded(contents.toString());
        } else {
            String defaultContents = attributes.getDefaultContents();

            if (defaultContents != null) {
                dom.appendEncoded(defaultContents);
            }
        }
        attributes.setContent(null);
    }

    /**
     * Permits the opening markup for the phone number link to be rendered as
     * required by the protocol. This should be balanced by the {@link
     * #closePhoneNumber} method.
     *
     * @param dom        the output buffer into which the markup is to be
     *                   rendered
     * @param attributes the attributes for the phone number
     */
    protected void openPhoneNumber(DOMOutputBuffer dom,
                                   PhoneNumberAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Permits the closing markup for the phone number link to be rendered as
     * required by the protocol. This should balance the {@link
     * #openPhoneNumber} method.
     *
     * @param dom        the output buffer into which the markup is to be
     *                   rendered
     * @param attributes the attributes for the phone number
     */
    protected void closePhoneNumber(DOMOutputBuffer dom,
                                    PhoneNumberAttributes attributes) {
    }

    // ==========================================================================
    //   Block element methods.
    // ==========================================================================

    // ------------------------------------------------------------------------
    //   Audio Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeAudio(AudioAttributes attributes) {
        DOMOutputBuffer dom = getCurrentBuffer();
        doAudio(dom, attributes);
    }

    protected void doAudio(DOMOutputBuffer dom, AudioAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Address Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeOpenAddress(AddressAttributes attributes)
        throws ProtocolException {
        DOMOutputBuffer dom = getCurrentBuffer();
        openAddress(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeCloseAddress(AddressAttributes attributes) {
        DOMOutputBuffer dom = getCurrentBuffer();
        closeAddress(dom, attributes);
    }

    /**
     * Add the open address markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openAddress(DOMOutputBuffer dom,
                               AddressAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Add the close address markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closeAddress(DOMOutputBuffer dom,
                                AddressAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Block Quote Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeOpenBlockQuote(BlockQuoteAttributes attributes)
        throws ProtocolException {
        DOMOutputBuffer dom = getCurrentBuffer();
        openBlockQuote(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeCloseBlockQuote(BlockQuoteAttributes attributes) {
        DOMOutputBuffer dom = getCurrentBuffer();
        closeBlockQuote(dom, attributes);
    }

    /**
     * Add the open block quote markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openBlockQuote(DOMOutputBuffer dom,
                                  BlockQuoteAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Add the close block quote markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closeBlockQuote(DOMOutputBuffer dom,
                                   BlockQuoteAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Div Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeOpenDiv(DivAttributes attributes)
        throws ProtocolException {
        DOMOutputBuffer dom = getCurrentBuffer();
        openDiv(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeCloseDiv(DivAttributes attributes) {
        DOMOutputBuffer dom = getCurrentBuffer();
        closeDiv(dom, attributes);
    }

    /**
     * Add the open div markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    public void openDiv(DOMOutputBuffer dom,
                        DivAttributes attributes) throws ProtocolException {
        // todo: later: presumably this should be masked if not dissecting?
        if ("true".equals(attributes.getKeepTogether())) {
            dom.openElement(KEEPTOGETHER_ELEMENT);
        }
    }

    /**
     * Add the close div markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    public void closeDiv(DOMOutputBuffer dom,
                         DivAttributes attributes) {
        if ("true".equals(attributes.getKeepTogether())) {
            dom.closeElement(KEEPTOGETHER_ELEMENT);
        }
    }

    // ------------------------------------------------------------------------
    //   Heading1 Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeOpenHeading1(HeadingAttributes attributes)
        throws ProtocolException {
        DOMOutputBuffer dom = getCurrentBuffer();
        openHeading1(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeCloseHeading1(HeadingAttributes attributes) {
        DOMOutputBuffer dom = getCurrentBuffer();
        closeHeading1(dom, attributes);
    }

    /**
     * Add the open heading1 markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openHeading1(DOMOutputBuffer dom,
                                HeadingAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Add the close heading1 markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closeHeading1(DOMOutputBuffer dom,
                                 HeadingAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Heading2 Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeOpenHeading2(HeadingAttributes attributes)
        throws ProtocolException {
        DOMOutputBuffer dom = getCurrentBuffer();
        openHeading2(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeCloseHeading2(HeadingAttributes attributes) {
        DOMOutputBuffer dom = getCurrentBuffer();
        closeHeading2(dom, attributes);
    }

    /**
     * Add the open heading2 markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openHeading2(DOMOutputBuffer dom,
                                HeadingAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Add the close heading2 markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closeHeading2(DOMOutputBuffer dom,
                                 HeadingAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Heading3 Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeOpenHeading3(HeadingAttributes attributes)
        throws ProtocolException {
        DOMOutputBuffer dom = getCurrentBuffer();
        openHeading3(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeCloseHeading3(HeadingAttributes attributes) {
        DOMOutputBuffer dom = getCurrentBuffer();
        closeHeading3(dom, attributes);
    }

    /**
     * Add the open heading3 markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openHeading3(DOMOutputBuffer dom,
                                HeadingAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Add the close heading3 markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closeHeading3(DOMOutputBuffer dom,
                                 HeadingAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Heading4 Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeOpenHeading4(HeadingAttributes attributes)
        throws ProtocolException {
        DOMOutputBuffer dom = getCurrentBuffer();
        openHeading4(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeCloseHeading4(HeadingAttributes attributes) {
        DOMOutputBuffer dom = getCurrentBuffer();
        closeHeading4(dom, attributes);
    }

    /**
     * Add the open heading4 markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openHeading4(DOMOutputBuffer dom,
                                HeadingAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Add the close heading4 markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closeHeading4(DOMOutputBuffer dom,
                                 HeadingAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Heading5 Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeOpenHeading5(HeadingAttributes attributes)
        throws ProtocolException {
        DOMOutputBuffer dom = getCurrentBuffer();
        openHeading5(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeCloseHeading5(HeadingAttributes attributes) {
        DOMOutputBuffer dom = getCurrentBuffer();
        closeHeading5(dom, attributes);
    }

    /**
     * Add the open heading5 markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openHeading5(DOMOutputBuffer dom,
                                HeadingAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Add the close heading5 markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closeHeading5(DOMOutputBuffer dom,
                                 HeadingAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Heading6 Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeOpenHeading6(HeadingAttributes attributes)
        throws ProtocolException {
        DOMOutputBuffer dom = getCurrentBuffer();
        openHeading6(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeCloseHeading6(HeadingAttributes attributes) {
        DOMOutputBuffer dom = getCurrentBuffer();
        closeHeading6(dom, attributes);
    }

    /**
     * Add the open heading6 markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openHeading6(DOMOutputBuffer dom,
                                HeadingAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Add the close heading6 markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closeHeading6(DOMOutputBuffer dom,
                                 HeadingAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Horizontal Rule Element
    // ------------------------------------------------------------------------

    /**
     * Write the horizontal rule markup directly to the current output buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public final void writeHorizontalRule(HorizontalRuleAttributes attributes)
        throws ProtocolException {
        DOMOutputBuffer dom = getCurrentBuffer();

        doHorizontalRule(dom, attributes);
    }

    /**
     * Add the horizontal rule markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void doHorizontalRule(DOMOutputBuffer dom,
                                    HorizontalRuleAttributes attributes)
        throws ProtocolException {
    }

    // ------------------------------------------------------------------------
    //   Paragraph Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeOpenParagraph(ParagraphAttributes attributes)
        throws ProtocolException {
        DOMOutputBuffer dom = getCurrentBuffer();
        openParagraph(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeCloseParagraph(ParagraphAttributes attributes) {
        DOMOutputBuffer dom = getCurrentBuffer();
        closeParagraph(dom, attributes);
    }

    /**
     * Add the open paragraph markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openParagraph(DOMOutputBuffer dom,
                                 ParagraphAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Add the close paragraph markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closeParagraph(DOMOutputBuffer dom,
                                  ParagraphAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Pre Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeOpenPre(PreAttributes attributes)
        throws ProtocolException {
        // allocate a new output buffer for the content
        final DOMOutputBuffer buffer = allocateOutputBuffer();
        // preserve formatting for the nested content
        buffer.setElementIsPreFormatted(true);
        // push the buffer into the stack for the content
        getMarinerPageContext().pushOutputBuffer(buffer);
        // open the pre element
        openPre(buffer, attributes);
        // add the buffer to the stack
        // openPre might have pushed a new buffer into the stack so we have to
        // save the buffer in another stack so we can use it for closePre.
        if (preBufferStack == null) {
            preBufferStack = new Stack();
        }
        preBufferStack.push(buffer);
    }

    // Javadoc inherited from super class.
    public final void writeClosePre(PreAttributes attributes) {
        final MarinerPageContext pageContext = getMarinerPageContext();
        // Get the output buffer which captured all the output generated
        // by nested elements and the pre element itself.
        // This might not be the current buffer on the stack in the page context
        // as the openPre might have pushed a new buffer into it. So we get it
        // from the preBuffer stack.
        final DOMOutputBuffer buffer = (DOMOutputBuffer) preBufferStack.pop();
        // close the pre tag
        closePre(buffer, attributes);
        // closePre removed any additional output buffers from the stack, so
        // <code>buffer</code> must be the next
        pageContext.popOutputBuffer(buffer);
        // add the content buffer to the current output buffer
        final DOMOutputBuffer parentBuffer =
            (DOMOutputBuffer) pageContext.getCurrentOutputBuffer();
        parentBuffer.addOutputBuffer(buffer);
    }

    /**
     * Add the open pre markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openPre(DOMOutputBuffer dom,
                           PreAttributes attributes) throws ProtocolException {
    }

    /**
     * Add the close pre markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closePre(DOMOutputBuffer dom,
                            PreAttributes attributes) {
    }

    // ==========================================================================
    //   List element methods.
    // ==========================================================================

    // ------------------------------------------------------------------------
    //   Definition Data Element
    // ------------------------------------------------------------------------

    /**
     * Write the open definition data markup directly to the current output
     * buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public final
    void writeOpenDefinitionData(DefinitionDataAttributes attributes)
        throws ProtocolException {

        DOMOutputBuffer dom = getCurrentBuffer();
        openDefinitionData(dom, attributes);
    }

    /**
     * Write the close definition data markup directly to the current output
     * buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public final
    void writeCloseDefinitionData(DefinitionDataAttributes attributes) {

        DOMOutputBuffer dom = getCurrentBuffer();
        closeDefinitionData(dom, attributes);
    }

    /**
     * Add the open definition data markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected
    void openDefinitionData(DOMOutputBuffer dom,
                            DefinitionDataAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Add the close definition data markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected
    void closeDefinitionData(DOMOutputBuffer dom,
                             DefinitionDataAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Definition List Element
    // ------------------------------------------------------------------------

    /**
     * Write the open definition list markup directly to the current output
     * buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public final
    void writeOpenDefinitionList(DefinitionListAttributes attributes)
        throws ProtocolException {

        DOMOutputBuffer dom = getCurrentBuffer();
        openDefinitionList(dom, attributes);
    }

    /**
     * Write the close definition list markup directly to the current output
     * buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public final
    void writeCloseDefinitionList(DefinitionListAttributes attributes) {

        DOMOutputBuffer dom = getCurrentBuffer();
        closeDefinitionList(dom, attributes);
    }

    /**
     * Add the open definition list markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected
    void openDefinitionList(DOMOutputBuffer dom,
                            DefinitionListAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Add the close definition list markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected
    void closeDefinitionList(DOMOutputBuffer dom,
                             DefinitionListAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Definition Term Element
    // ------------------------------------------------------------------------

    /**
     * Write the open definition term markup directly to the current output
     * buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public final
    void writeOpenDefinitionTerm(DefinitionTermAttributes attributes)
        throws ProtocolException {

        DOMOutputBuffer dom = getCurrentBuffer();
        openDefinitionTerm(dom, attributes);
    }

    /**
     * Write the close definition term markup directly to the current output
     * buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public final
    void writeCloseDefinitionTerm(DefinitionTermAttributes attributes) {

        DOMOutputBuffer dom = getCurrentBuffer();
        closeDefinitionTerm(dom, attributes);
    }

    /**
     * Add the open definition term markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected
    void openDefinitionTerm(DOMOutputBuffer dom,
                            DefinitionTermAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Add the close definition term markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected
    void closeDefinitionTerm(DOMOutputBuffer dom,
                             DefinitionTermAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   List Item Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeOpenListItem(ListItemAttributes attributes)
        throws ProtocolException {
        DOMOutputBuffer dom = getCurrentBuffer();
        openListItem(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeCloseListItem(ListItemAttributes attributes) {
        DOMOutputBuffer dom = getCurrentBuffer();
        closeListItem(dom, attributes);
    }

    /**
     * Add the open list item markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openListItem(DOMOutputBuffer dom,
                                ListItemAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Add the close list item markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closeListItem(DOMOutputBuffer dom,
                                 ListItemAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Ordered List Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeOpenOrderedList(OrderedListAttributes attributes)
        throws ProtocolException {
        DOMOutputBuffer dom = getCurrentBuffer();
        openOrderedList(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeCloseOrderedList(OrderedListAttributes attributes) {
        DOMOutputBuffer dom = getCurrentBuffer();
        closeOrderedList(dom, attributes);
    }

    /**
     * Add the open ordered list markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openOrderedList(DOMOutputBuffer dom,
                                   OrderedListAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Add the close ordered list markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closeOrderedList(DOMOutputBuffer dom,
                                    OrderedListAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Unordered List Element
    // ------------------------------------------------------------------------

    /**
     * Write the open unordered list markup directly to the current output
     * buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public final
    void writeOpenUnorderedList(UnorderedListAttributes attributes)
        throws ProtocolException {

        DOMOutputBuffer dom = getCurrentBuffer();
        openUnorderedList(dom, attributes);
    }

    /**
     * Write the close unordered list markup directly to the current output
     * buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public final
    void writeCloseUnorderedList(UnorderedListAttributes attributes) {

        DOMOutputBuffer dom = getCurrentBuffer();
        closeUnorderedList(dom, attributes);
    }

    /**
     * Add the open unordered list markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected
    void openUnorderedList(DOMOutputBuffer dom,
                           UnorderedListAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Add the close unordered list markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected
    void closeUnorderedList(DOMOutputBuffer dom,
                            UnorderedListAttributes attributes) {
    }

    // ==========================================================================
    //   Table element methods.
    // ==========================================================================

    // ------------------------------------------------------------------------
    //   Table Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeOpenTable(TableAttributes attributes)
        throws ProtocolException {
        DOMOutputBuffer dom = getCurrentBuffer();
        openTable(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeCloseTable(TableAttributes attributes) {
        DOMOutputBuffer dom = getCurrentBuffer();
        closeTable(dom, attributes);
    }

    /**
     * Add the open table markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openTable(DOMOutputBuffer dom,
                             TableAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Add the close table markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closeTable(DOMOutputBuffer dom,
                              TableAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Table Body Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeOpenTableBody(TableBodyAttributes attributes)
        throws ProtocolException {
        DOMOutputBuffer dom = getCurrentBuffer();
        openTableBody(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeCloseTableBody(TableBodyAttributes attributes) {
        DOMOutputBuffer dom = getCurrentBuffer();
        closeTableBody(dom, attributes);
    }

    /**
     * Add the open table body markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openTableBody(DOMOutputBuffer dom,
                                 TableBodyAttributes attributes)
        throws ProtocolException {
        if ("true".equals(attributes.getKeepTogether())) {
            dom.openElement(KEEPTOGETHER_ELEMENT);
        }
    }

    /**
     * Add the close table body markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closeTableBody(DOMOutputBuffer dom,
                                  TableBodyAttributes attributes) {
        if ("true".equals(attributes.getKeepTogether())) {
            dom.closeElement(KEEPTOGETHER_ELEMENT);
        }
    }

    // ------------------------------------------------------------------------
    //   Table Data Cell Element
    // ------------------------------------------------------------------------

    /**
     * Write the open table data cell markup directly to the current output
     * buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public final
    void writeOpenTableDataCell(TableCellAttributes attributes)
        throws ProtocolException {

        DOMOutputBuffer dom = getCurrentBuffer();
        openTableDataCell(dom, attributes);
    }

    /**
     * Write the close table data cell markup directly to the current output
     * buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public final
    void writeCloseTableDataCell(TableCellAttributes attributes) {

        DOMOutputBuffer dom = getCurrentBuffer();
        closeTableDataCell(dom, attributes);
    }

    /**
     * Add the open table data cell markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected
    void openTableDataCell(DOMOutputBuffer dom,
                           TableCellAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Add the close table data cell markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected
    void closeTableDataCell(DOMOutputBuffer dom,
                            TableCellAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Table Footer Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeOpenTableFooter(TableFooterAttributes attributes)
        throws ProtocolException {
        DOMOutputBuffer dom = getCurrentBuffer();
        openTableFooter(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeCloseTableFooter(TableFooterAttributes attributes) {
        DOMOutputBuffer dom = getCurrentBuffer();
        closeTableFooter(dom, attributes);
    }

    /**
     * Add the open table footer markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openTableFooter(DOMOutputBuffer dom,
                                   TableFooterAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Add the close table footer markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closeTableFooter(DOMOutputBuffer dom,
                                    TableFooterAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Table Header Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeOpenTableHeader(TableHeaderAttributes attributes)
        throws ProtocolException {
        DOMOutputBuffer dom = getCurrentBuffer();
        openTableHeader(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeCloseTableHeader(TableHeaderAttributes attributes) {
        DOMOutputBuffer dom = getCurrentBuffer();
        closeTableHeader(dom, attributes);
    }

    /**
     * Add the open table header markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openTableHeader(DOMOutputBuffer dom,
                                   TableHeaderAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Add the close table header markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closeTableHeader(DOMOutputBuffer dom,
                                    TableHeaderAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Table Caption Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeOpenTableCaption(CaptionAttributes attributes)
        throws ProtocolException {
        DOMOutputBuffer dom = getCurrentBuffer();
        openTableCaption(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeCloseTableCaption(CaptionAttributes attributes) {
        DOMOutputBuffer dom = getCurrentBuffer();
        closeTableCaption(dom, attributes);
    }

    /**
     * Add the open table caption markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openTableCaption(DOMOutputBuffer dom,
                                    CaptionAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Add the close table caption markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closeTableCaption(DOMOutputBuffer dom,
                                     CaptionAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Table Header Cell Element
    // ------------------------------------------------------------------------

    /**
     * Write the open table header cell markup directly to the current output
     * buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public final
    void writeOpenTableHeaderCell(TableCellAttributes attributes)
        throws ProtocolException {

        DOMOutputBuffer dom = getCurrentBuffer();
        openTableHeaderCell(dom, attributes);
    }

    /**
     * Write the close table header cell markup directly to the current output
     * buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public final
    void writeCloseTableHeaderCell(TableCellAttributes attributes) {

        DOMOutputBuffer dom = getCurrentBuffer();
        closeTableHeaderCell(dom, attributes);
    }

    /**
     * Add the open table header cell markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected
    void openTableHeaderCell(DOMOutputBuffer dom,
                             TableCellAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Add the close table header cell markup to the specified
     * DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected
    void closeTableHeaderCell(DOMOutputBuffer dom,
                              TableCellAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Table Row Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeOpenTableRow(TableRowAttributes attributes)
        throws ProtocolException {
        DOMOutputBuffer dom = getCurrentBuffer();
        openTableRow(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeCloseTableRow(TableRowAttributes attributes) {
        DOMOutputBuffer dom = getCurrentBuffer();
        closeTableRow(dom, attributes);
    }

    /**
     * Add the open table row markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openTableRow(DOMOutputBuffer dom,
                                TableRowAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Add the close table row markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closeTableRow(DOMOutputBuffer dom,
                                 TableRowAttributes attributes) {
    }

    // ==========================================================================
    //   Inline element methods.
    // ==========================================================================

    // ------------------------------------------------------------------------
    //   Big Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeOpenBig(BigAttributes attributes)
        throws ProtocolException {
        DOMOutputBuffer dom = getCurrentBuffer();
        openBig(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeCloseBig(BigAttributes attributes) {
        DOMOutputBuffer dom = getCurrentBuffer();
        closeBig(dom, attributes);
    }

    /**
     * Add the open big markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openBig(DOMOutputBuffer dom,
                           BigAttributes attributes) throws ProtocolException {
    }

    /**
     * Add the close big markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closeBig(DOMOutputBuffer dom,
                            BigAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Bold Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeOpenBold(BoldAttributes attributes)
        throws ProtocolException {
        DOMOutputBuffer dom = getCurrentBuffer();
        openBold(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeCloseBold(BoldAttributes attributes) {
        DOMOutputBuffer dom = getCurrentBuffer();
        closeBold(dom, attributes);
    }

    /**
     * Add the open bold markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openBold(DOMOutputBuffer dom,
                            BoldAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Add the close bold markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closeBold(DOMOutputBuffer dom,
                             BoldAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Cite Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeOpenCite(CiteAttributes attributes)
        throws ProtocolException {
        DOMOutputBuffer dom = getCurrentBuffer();
        openCite(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeCloseCite(CiteAttributes attributes) {
        DOMOutputBuffer dom = getCurrentBuffer();
        closeCite(dom, attributes);
    }

    /**
     * Add the open cite markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openCite(DOMOutputBuffer dom,
                            CiteAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Add the close cite markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closeCite(DOMOutputBuffer dom,
                             CiteAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Code Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeOpenCode(CodeAttributes attributes)
        throws ProtocolException {
        DOMOutputBuffer dom = getCurrentBuffer();
        openCode(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeCloseCode(CodeAttributes attributes) {
        DOMOutputBuffer dom = getCurrentBuffer();
        closeCode(dom, attributes);
    }

    /**
     * Add the open code markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openCode(DOMOutputBuffer dom,
                            CodeAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Add the close code markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closeCode(DOMOutputBuffer dom,
                             CodeAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Emphasis Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeOpenEmphasis(EmphasisAttributes attributes)
        throws ProtocolException {
        DOMOutputBuffer dom = getCurrentBuffer();
        openEmphasis(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeCloseEmphasis(EmphasisAttributes attributes) {
        DOMOutputBuffer dom = getCurrentBuffer();
        closeEmphasis(dom, attributes);
    }

    /**
     * Add the open emphasis markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openEmphasis(DOMOutputBuffer dom,
                                EmphasisAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Add the close emphasis markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closeEmphasis(DOMOutputBuffer dom,
                                 EmphasisAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Italic Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeOpenItalic(ItalicAttributes attributes)
        throws ProtocolException {
        DOMOutputBuffer dom = getCurrentBuffer();
        openItalic(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeCloseItalic(ItalicAttributes attributes) {
        DOMOutputBuffer dom = getCurrentBuffer();
        closeItalic(dom, attributes);
    }

    /**
     * Add the open italic markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openItalic(DOMOutputBuffer dom,
                              ItalicAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Add the close italic markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closeItalic(DOMOutputBuffer dom,
                               ItalicAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Keyboard Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeOpenKeyboard(KeyboardAttributes attributes)
        throws ProtocolException {
        DOMOutputBuffer dom = getCurrentBuffer();
        openKeyboard(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeCloseKeyboard(KeyboardAttributes attributes) {
        DOMOutputBuffer dom = getCurrentBuffer();
        closeKeyboard(dom, attributes);
    }

    /**
     * Add the open keyboard markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openKeyboard(DOMOutputBuffer dom,
                                KeyboardAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Add the close keyboard markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closeKeyboard(DOMOutputBuffer dom,
                                 KeyboardAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Line Break Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeLineBreak(LineBreakAttributes attributes) {
        DOMOutputBuffer dom = getCurrentBuffer();
        doLineBreak(dom, attributes);
    }

    /**
     * Add the line break markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void doLineBreak(DOMOutputBuffer dom,
                               LineBreakAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Monospace Font Element
    // ------------------------------------------------------------------------

    /**
     * Write the open monospace font markup directly to the current output
     * buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public final
    void writeOpenMonospaceFont(MonospaceFontAttributes attributes)
        throws ProtocolException {

        DOMOutputBuffer dom = getCurrentBuffer();
        openMonospaceFont(dom, attributes);
    }

    /**
     * Write the close monospace font markup directly to the current output
     * buffer.
     *
     * @param attributes The attributes to use when generating the markup.
     */
    public final
    void writeCloseMonospaceFont(MonospaceFontAttributes attributes) {

        DOMOutputBuffer dom = getCurrentBuffer();
        closeMonospaceFont(dom, attributes);
    }

    /**
     * Open a menu.
     *
     * @param dom        The DOMOutputBuffer to write to.
     * @param attributes The MenuAttributes for the menu.
     */
    protected void openMenu(DOMOutputBuffer dom, MenuAttributes attributes) {
    }

    /**
     * Close a menu.
     *
     * @param dom        The DOMOutputBuffer to write to.
     * @param attributes The MenuAttributes for the menu.
     */
    protected void closeMenu(DOMOutputBuffer dom, MenuAttributes attributes) {
    }

    /**
     * Add the open monospace font markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected
    void openMonospaceFont(DOMOutputBuffer dom,
                           MonospaceFontAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Add the close monospace font markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected
    void closeMonospaceFont(DOMOutputBuffer dom,
                            MonospaceFontAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Sample Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeOpenSample(SampleAttributes attributes)
        throws ProtocolException {
        DOMOutputBuffer dom = getCurrentBuffer();
        openSample(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeCloseSample(SampleAttributes attributes) {
        DOMOutputBuffer dom = getCurrentBuffer();
        closeSample(dom, attributes);
    }

    /**
     * Add the open sample markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openSample(DOMOutputBuffer dom,
                              SampleAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Add the close sample markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closeSample(DOMOutputBuffer dom,
                               SampleAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Small Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeOpenSmall(SmallAttributes attributes)
        throws ProtocolException {
        DOMOutputBuffer dom = getCurrentBuffer();
        openSmall(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeCloseSmall(SmallAttributes attributes) {
        DOMOutputBuffer dom = getCurrentBuffer();
        closeSmall(dom, attributes);
    }

    /**
     * Add the open small markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openSmall(DOMOutputBuffer dom,
                             SmallAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Add the close small markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closeSmall(DOMOutputBuffer dom,
                              SmallAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Span Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeOpenSpan(SpanAttributes attributes)
        throws ProtocolException {
        DOMOutputBuffer dom = getCurrentBuffer();
        openSpan(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeCloseSpan(SpanAttributes attributes) {
        DOMOutputBuffer dom = getCurrentBuffer();
        closeSpan(dom, attributes);
    }

    // JavaDoc inherited
    public void openSpan(DOMOutputBuffer dom, SpanAttributes attributes)
        throws ProtocolException {
    }

    // JavaDoc inherited
    public void closeSpan(DOMOutputBuffer dom, SpanAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Strong Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeOpenStrong(StrongAttributes attributes)
        throws ProtocolException {
        DOMOutputBuffer dom = getCurrentBuffer();
        openStrong(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeCloseStrong(StrongAttributes attributes) {
        DOMOutputBuffer dom = getCurrentBuffer();
        closeStrong(dom, attributes);
    }

    /**
     * Add the open strong markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openStrong(DOMOutputBuffer dom,
                              StrongAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Add the close strong markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closeStrong(DOMOutputBuffer dom,
                               StrongAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Subscript Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeOpenSubscript(SubscriptAttributes attributes)
        throws ProtocolException {
        DOMOutputBuffer dom = getCurrentBuffer();
        openSubscript(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeCloseSubscript(SubscriptAttributes attributes) {
        DOMOutputBuffer dom = getCurrentBuffer();
        closeSubscript(dom, attributes);
    }

    /**
     * Add the open subscript markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openSubscript(DOMOutputBuffer dom,
                                 SubscriptAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Add the close subscript markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closeSubscript(DOMOutputBuffer dom,
                                  SubscriptAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Superscript Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeOpenSuperscript(SuperscriptAttributes attributes)
        throws ProtocolException {
        DOMOutputBuffer dom = getCurrentBuffer();
        openSuperscript(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeCloseSuperscript(SuperscriptAttributes attributes) {
        DOMOutputBuffer dom = getCurrentBuffer();
        closeSuperscript(dom, attributes);
    }

    /**
     * Add the open superscript markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openSuperscript(DOMOutputBuffer dom,
                                   SuperscriptAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Add the close superscript markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closeSuperscript(DOMOutputBuffer dom,
                                    SuperscriptAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Underline Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeOpenUnderline(UnderlineAttributes attributes)
        throws ProtocolException {
        DOMOutputBuffer dom = getCurrentBuffer();
        openUnderline(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeCloseUnderline(UnderlineAttributes attributes) {
        DOMOutputBuffer dom = getCurrentBuffer();
        closeUnderline(dom, attributes);
    }

    /**
     * Add the open underline markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openUnderline(DOMOutputBuffer dom,
                                 UnderlineAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Add the close underline markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closeUnderline(DOMOutputBuffer dom,
                                  UnderlineAttributes attributes) {
    }

    // ==========================================================================
    //   Special element methods.
    // ==========================================================================

    // ------------------------------------------------------------------------
    //   Divide Hint Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeDivideHint(DivideHintAttributes attributes) {
        // Only add the dividehint marker if we are inside a dissecting pane.
        Pane pane = context.getCurrentPane();
        if (pane != null &&
            FormatType.DISSECTING_PANE.equals(pane.getFormatType())) {
            DOMOutputBuffer dom = getCurrentBuffer();
            doDivideHint(dom, attributes);
        }
    }

    /**
     * Generate the mark up for a divide hint. This will not be output to the
     * HTML device.
     *
     * @param dom        The DOMOutputBuffer to add to.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void doDivideHint(DOMOutputBuffer dom,
                                DivideHintAttributes attributes) {
        dom.addElement(DIVIDE_HINT_ELEMENT);
    }

    // ------------------------------------------------------------------------
    //   Image Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeImage(ImageAttributes attributes)
        throws ProtocolException {
        DOMOutputBuffer dom = getCurrentBuffer();

        // Check, whether this element needs finalization. It may happen in case
        // some attributes (src, width and height) may not be provided at this 
        // very moment.
        if (!attributes.needsFinalizer()) {
            // In case finalization is not required, do the image right now.
            if (attributes.getSrc() != null) {
                if (attributes.isConvertibleImageAsset()) {
                    attributes.setWidth(null);
                    attributes.setHeight(null);
                }
            }
                
            doImage(dom, attributes);
            
        } else {
            // In case finalization is required, defer doing the image until
            // finalization. Right now, insert only a placeholder element, so we
            // can go back there in finalizer.
            final Element placeHolderElement = dom.addElement("placeholder");
            
            attributes.setFinalizer(new ElementFinalizer() {
                public void finalizeElement(MAPAttributes attributes) 
                        throws ProtocolException {
                    finalizeImage(placeHolderElement, 
                            (ImageAttributes) attributes);
                }
            });
        }
    }

    /**
     * Add the image markup at the specified location.
     *
     * @param dom The DOMOutputBuffer.
     * @param attributes The attributes for the image.
     *
     * @throws ProtocolException
     */
    protected void doImage(DOMOutputBuffer dom, ImageAttributes attributes)
            throws ProtocolException {

        if (attributes.getSrc() != null) {
            if (attributes.isConvertibleImageAsset()) {
                attributes.setWidth(null);
                attributes.setHeight(null);
            }
        }
    }

    /**
     * Finalizes the image by replacing specified placeHolder element with the
     * img element.
     * 
     * @param placeHolderElement The element to replace with finalizer img.
     * @param attributes The image attributes
     * @throws ProtocolException
     */
    private void finalizeImage(Element placeHolderElement,
            ImageAttributes attributes)
            throws ProtocolException {
        DOMOutputBuffer dom = (DOMOutputBuffer)
            getOutputBufferFactory().createOutputBuffer();
        
        if (attributes.getSrc() != null) {
            if (attributes.isConvertibleImageAsset()) {
                attributes.setWidth(null);
                attributes.setHeight(null);
            }
        }

        doImage(dom, attributes);

        placeHolderElement.replaceWith(dom.getRoot().removeChildren());
    }

    // ------------------------------------------------------------------------
    //   Object Element
    // ------------------------------------------------------------------------

    // Javadoc inherited
    protected final void directWriteOpenObject(ObjectAttribute attributes)
            throws ProtocolException {
        
        // Push new output buffer on the stack, which would contain object
        // element content.
        context.pushOutputBuffer(allocateOutputBuffer());
    }

    // Javadoc inherited
    protected final void directWriteCloseObject(ObjectAttribute attributes)
            throws ProtocolException {
        final DOMOutputBuffer contentBuffer = getCurrentBuffer();

        // Remove the output buffer from the stack, containing object element
        // content.
        context.popOutputBuffer(contentBuffer);

        // Get current output buffer, to write object element to.
        DOMOutputBuffer dom = getCurrentBuffer();
        
        // Check, whether this element needs finalization. It may happen in case
        // some attributes (src, srcType, params) may not be provided at this 
        // very moment.
        if (!attributes.needsFinalizer()) {
            // In case finalization is not required, do the object right now.
            doObject(dom, attributes, contentBuffer);
            
        } else {
            // In case finalization is required, defer doing the image until
            // finalization. Right now, insert only a placeholder element, so we
            // can go back there in finalizer.
            final Element placeHolderElement = dom.addElement("placeholder");

            attributes.setFinalizer(new ElementFinalizer() {
                public void finalizeElement(MAPAttributes attributes) 
                        throws ProtocolException {
                    finalizeObject(placeHolderElement, 
                            (ObjectAttribute) attributes,
                            contentBuffer);
                }
            });
        }
    }

    /**
     * Finalizes object element by replacing specified placeHolder element with
     * the object element.
     * 
     * @param placeHolderElement The element to replace with finalized object.
     * @param attributes The object attributes
     * @param content The body content.
     * @throws ProtocolException
     */
    private void finalizeObject(Element placeHolderElement,
            ObjectAttribute attributes,
            DOMOutputBuffer content) throws ProtocolException {
        DOMOutputBuffer dom = (DOMOutputBuffer)
            getOutputBufferFactory().createOutputBuffer();

        doObject(dom, attributes, content);

        placeHolderElement.replaceWith(dom.getRoot().removeChildren());
    }
    
    /**
     * Writes object opening directly to the specified DOM output buffer.
     * 
     * @param dom The DOM output buffer to write to.
     * @param attributes The object attributes.
     * @param content the buffer with object content.
     */
    protected void doObject(DOMOutputBuffer dom, 
            ObjectAttribute attributes, 
            DOMOutputBuffer content) {

    }
    
    // ------------------------------------------------------------------------
    //   Meta Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public final void writeMeta(MetaAttributes attributes) {
        DOMOutputBuffer dom = getHeadBuffer();
        doMeta(dom, attributes);
    }

    /**
     * Add protocol specific meta tag attributes.
     *
     * @param element    The meta element to which the attributes should be
     *                   added.
     * @param attributes <code>MetaAttributes</code> for this meta tag.
     */
    protected void addMetaAttributes(Element element,
                                     MetaAttributes attributes) {
    }

    /**
     * Add the meta markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    public void doMeta(DOMOutputBuffer dom,
                          MetaAttributes attributes) {

        if (attributes != null) {
            String value;

            Element element = dom.addStyledElement("meta", attributes);

            if ((value = attributes.getName()) != null) {
                element.setAttribute("name", value);
            }
            if ((value = attributes.getHttpEquiv()) != null) {
                element.setAttribute("http-equiv", value);
            }
            if ((value = attributes.getContent()) != null) {
                element.setAttribute("content", value);
            }

            addMetaAttributes(element, attributes);
        }
    }

    // ==========================================================================
    //   Menu element methods.
    // ==========================================================================

    /**
     * Returns a <code>>MenuRenderer</code> for rendering Numeric Shortcut
     * style menus.
     *
     * @return a MenuRenderer for rendering a numeric shortcut style menu or
     *         null if the protocol does not support numeric shortcut style
     *         menus
     */
    protected MenuRenderer createNumericShortcutMenuRenderer() {
        return null;
    }

    /**
     * Return the appropriate <code>MenuRenderer</code> instance in order to
     * render the menu specified via the attributes.
     *
     * @param attributes the menus attributes
     * @return a MenuRender instance of null if no suitable renderer exists.
     */
    protected MenuRenderer getMenuRenderer(MenuAttributes attributes) {
        MenuRenderer menuRenderer = null;

        Styles styles = attributes.getStyles();
        PropertyValues propertyValues = styles.getPropertyValues();
        StyleValue styleValue = propertyValues.getComputedValue(
                StylePropertyDetails.MCS_MENU_LINK_STYLE);
        if (styleValue == MCSMenuLinkStyleKeywords.NUMERIC_SHORTCUT) {
            // need to render a numeric shortcut menu. Lazily create the
            // appropriate renderer. Not all protocols support numeric
            // shortcut menus, those that don't will return null from
            // the createNumericShortcutMenuRenderer method.
            if (numericShortcutMenuRenderer == null) {
                // create the renderer
                numericShortcutMenuRenderer =
                    createNumericShortcutMenuRenderer();
            }
            menuRenderer = numericShortcutMenuRenderer;
        }

        // TODO - all other types of menus will be rendererd via a MenuRenderer.
        return menuRenderer;
    }

    /**
     * Adds the markup used to separate vertical menu items.
     *
     * @param dom The DOMOutputBuffer to use.
     */
    protected void addVerticalMenuItemSeparator(DOMOutputBuffer dom) {
    }

    /**
     * Adds the markup used to separate horizontal menu items.
     *
     * @param dom The DOMOutputBuffer to use.
     */
    protected void addHorizontalMenuItemSeparator(DOMOutputBuffer dom) {
        addHorizontalMenuItemSeparator(dom, "non-breaking-space");
    }

    /**
     * Adds the markup used to separate horizontal menu items.
     *
     * @param dom The DOMOutputBuffer to use.
     * @param sep The separator to use
     */
    private void addHorizontalMenuItemSeparator(DOMOutputBuffer dom,
                                                  String sep) {
        if (sep.equals("non-breaking-space")) {
            dom.writeText(NBSP);
        } else if (sep.equals("space")) {
            dom.setElementIsPreFormatted(true);
            dom.writeText(" ");
        } else {
            // Do nothing !
        }
    }

    protected boolean doMenuItem(DOMOutputBuffer dom,
                                 MenuAttributes attributes,
                                 MenuItem item)
        throws ProtocolException {
        return false;
    }

    /**
     * This method processes each menu item in turn adding it to the pane. If
     * the orientation is horizontal and then pane is a column iterator then
     * each menu item is added to a separate cell, otherwise the menu item may
     * be followed by a separator.
     *
     * @deprecated This should no longer be used since Enhanced Menus have been
     *             implemented.
     */
    public void doMenu(MenuAttributes attributes) throws ProtocolException {

        MenuRenderer renderer = getMenuRenderer(attributes);
        if (renderer != null) {
            renderer.renderMenu(attributes);
        } else {

            Pane pane = attributes.getPane();
            IteratorPane iterator = null;

            // Initialise the iterator and separator. If the pane is an
            // iterator pane which iterates in the correct direction then
            // use the iteration to layout the menu items, else a separator
            // is needed.
            MenuOrientation orientation = menuOrientation(attributes);
            if (orientation.equals(MenuOrientation.UNKNOWN)) {
                // Default to vertical.
                orientation = MenuOrientation.VERTICAL;
            }

            if (orientation.equals(MenuOrientation.VERTICAL)) {
                if (pane instanceof RowIteratorPane) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Vertical menu in a row iterator pane");
                    }
                    iterator = (IteratorPane)pane;
                } else {
                    if (logger.isDebugEnabled()) {
                        logger.debug(
                            "Vertical menu not in a row iterator pane");
                    }
                }
            } else {
                if (pane instanceof ColumnIteratorPane) {
                    if (logger.isDebugEnabled()) {
                        logger.debug(
                            "Horizontal menu in a column iterator pane");
                    }
                    iterator = (IteratorPane)pane;
                } else {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Horizontal menu not in a column " +
                                     "iterator pane");
                    }
                }
            }
            // NOTE: old menu code is unused.
            //    Initialise the content buffer.
            ContainerInstance containerInstance = (ContainerInstance)
                context.getFormatInstance(pane,
                    NDimensionalIndex.ZERO_DIMENSIONS);
            DOMOutputBuffer dom =
                (DOMOutputBuffer) containerInstance.getCurrentBuffer();
            doMenu(dom, attributes, orientation, iterator != null);
        }
    }

    public void renderMenuChild(DOMOutputBuffer dom,
                                MenuAttributes attributes,
                                MenuItem child,
                                boolean notLast,
                                boolean iteratorPane,
                                MenuOrientation orientation)
        throws ProtocolException {
        // Sometimes a horizontal separator is not required it depends on how
        // the menu item looks.
        boolean horizontalSeparatorRequired =
            renderMenuItem(dom, attributes, child);
        if (!iteratorPane) {
            if (notLast) {
                if (orientation.equals(MenuOrientation.VERTICAL)) {
                    addVerticalMenuItemSeparator(dom);
                } else if (
                    horizontalSeparatorRequired
                    && orientation.equals(MenuOrientation.HORIZONTAL)) {
                    addHorizontalMenuItemSeparator(dom);
                }
            }
        }
    }

    public void renderMenuChild(DOMOutputBuffer dom,
                                MenuAttributes attributes,
                                MenuItemGroupAttributes child,
                                boolean notLast,
                                boolean iteratorPane,
                                MenuOrientation orientation)
        throws ProtocolException {

        Styles styles = child.getStyles();
        PropertyValues propertyValues = styles.getPropertyValues();
        StyleValue styleValue;
        ImageAttributes imageAttributes = null;
        int separatorRepeat = 0;
        String separatorCharacters = null;

        styleValue = propertyValues.getComputedValue(
                StylePropertyDetails.MCS_MENU_SEPARATOR_TYPE);
        if (styleValue == MCSMenuSeparatorTypeKeywords.IMAGE) {
            styleValue = propertyValues.getComputedValue(
                    StylePropertyDetails.MCS_MENU_SEPARATOR_IMAGE);
            imageAttributes =
                    createImageAttributesMenuSeparator(child, styleValue);
        } else {
            // If the repeat is null then set it to a default of 0
            StyleValue repeat = propertyValues.getComputedValue(
                    StylePropertyDetails.MCS_MENU_SEPARATOR_REPEAT);
            if (repeat instanceof StyleInteger) {
                separatorRepeat = ((StyleInteger) repeat).getInteger();
            }

            // increase separatorRepeat as the protocols must render the
            // separator atleast once and one more time than the repeat count
            separatorRepeat++;

            separatorCharacters = ((StyleString)
                    propertyValues.getComputedValue(
                            StylePropertyDetails.MCS_MENU_SEPARATOR_CHARACTERS))
                    .getString();
        }

        // Only bother to render the separator before the menu item
        // if the theme says we should.
        if (shouldRenderSeparator(MCSMenuSeparatorPositionKeywords.BEFORE, styles)) {
            // check if we are rendering an image or a string of characters
            // If rendering characters we need to get the characters and the
            // repeat count for these characters
            if (imageAttributes != null) {
                renderMenuItemSeparator(dom, imageAttributes);
            } else {
                renderMenuItemSeparator(dom,
                        separatorCharacters,
                        separatorRepeat);
            }
            //                 We must render a protocol specific menu separator here so that the
            // following menu choices don't appear on the same row or same column
            // as the menu item group separator
            if (!iteratorPane) {
                if (orientation.equals(MenuOrientation.VERTICAL)) {
                    addVerticalMenuItemSeparator(dom);
                } else if (orientation.equals(MenuOrientation.HORIZONTAL)) {
                    addHorizontalMenuItemSeparator(dom);
                }
            }
        }

        Iterator itr = child.getItems().iterator();
        while (itr.hasNext()) {
            MenuChildVisitable menuChild = (MenuChildVisitable)itr.next();
            menuChild.visit(this, dom, attributes, true, iteratorPane,
                            orientation);
        }

        // Only bother to render the separator after the menu item group
        // if the theme says so.
        if (shouldRenderSeparator(MCSMenuSeparatorPositionKeywords.AFTER, styles)) {
            if (imageAttributes != null) {
                renderMenuItemSeparator(dom, imageAttributes);
            } else {
                renderMenuItemSeparator(dom,
                        separatorCharacters,
                        separatorRepeat);
            }
        }

        // Although horizontal menu items are optional the vertical separators
        // must be rendered between menu items and menu item separators
        if (!iteratorPane) {
            if (notLast) {
                if (orientation.equals(MenuOrientation.VERTICAL)) {
                    addVerticalMenuItemSeparator(dom);
                } else if (orientation.equals(MenuOrientation.HORIZONTAL)) {
                    addHorizontalMenuItemSeparator(dom);
                }
            }
        }
    }

    private boolean renderMenuItem(DOMOutputBuffer dom,
                                   MenuAttributes attributes,
                                   MenuItem item)
        throws ProtocolException {
        return doMenuItem(dom, attributes, item);
    }

    /**
     * @param group
     * @return image attributes
     * @todo later javadoc this method
     */
    private ImageAttributes createImageAttributesMenuSeparator(
        MenuItemGroupAttributes group,
        StyleValue styleValue) {
        String uri;
        if (styleValue instanceof StyleURI) {
            uri = ((StyleURI) styleValue).getURI();
        } else if (styleValue instanceof StyleComponentURI) {
            throw new NullPointerException("fix this");
        } else if (styleValue instanceof StyleTranscodableURI) {
            throw new NullPointerException("fix this");
        } else {
            throw new NullPointerException("fix this");
        }
        ImageAttributes returnValue = new ImageAttributes();
        returnValue.setSrc(uri);
        return returnValue;
    }

    /**
     * Subclasses must overide this method to render the menu separator as a
     * valid string
     */
    protected void renderMenuItemSeparator(DOMOutputBuffer dom,
                                           String separatorCharacters,
                                           int separatorRepeat) {
    }

    /**
     * Subclasses must override this method to render a menu separator image
     * correctly
     */
    protected void renderMenuItemSeparator(DOMOutputBuffer dom,
                                           ImageAttributes imageAttributes)
        throws ProtocolException {
    }

    /**
     * Tests the give style if the requried value passed in is set or if the
     * menu separator 'both' position is set
     */
    private boolean shouldRenderSeparator(StyleKeyword requiredValue, Styles styles) {
        PropertyValues propertyValues = styles.getPropertyValues();
        StyleValue actual = propertyValues.getComputedValue(
                StylePropertyDetails.MCS_MENU_SEPARATOR_POSITION);

        //test the given value against the valued specified in the style.
        return actual == requiredValue || actual == MCSMenuSeparatorPositionKeywords.BOTH;
    }

    /**
     * Determine the orientation of a menu given some MenuAttributes.
     *
     * @param attributes The MenuAttributes.
     * @return true if the menu should be vertical; false otherwise.
     */
    public MenuOrientation menuOrientation(MenuAttributes attributes) {

        MenuOrientation orientation;

        Styles styles = attributes.getStyles();
        if (styles == null) {
            // todo remove this when old dissector is removed.
            return MenuOrientation.UNKNOWN;
        }

        PropertyValues propertyValues = styles.getPropertyValues();
        StyleValue styleValue = propertyValues.getComputedValue(
                StylePropertyDetails.MCS_MENU_ORIENTATION);
        if (styleValue == MCSMenuOrientationKeywords.HORIZONTAL) {
            orientation = MenuOrientation.HORIZONTAL;
        } else if (styleValue == MCSMenuOrientationKeywords.VERTICAL) {
            orientation = MenuOrientation.VERTICAL;
        } else {
            throw new IllegalArgumentException("Invalid orientation " +
                    styleValue.getStandardCSS());
        }

        return orientation;
    }

    /**
     * Write out a menu to one or more DOMOutputBuffers.
     *
     * @param buffer             The buffer to write out all or the first menu
     *                           item.
     * @param attributes         The menu attributes for the menu.
     * @param orientation        Flag indicating the orientation of the menu.
     * @param needsBufferPerItem Flag indicating if each item should go into a
     *                           separate DOMOutputBuffer. If this is the case,
     *                           each new buffer will be retrieved from the
     *                           current pane context.
     * @throws ProtocolException If there is a problem with doMenuItem().
     */
    public void doMenu(DOMOutputBuffer buffer,
                       MenuAttributes attributes,
                       MenuOrientation orientation,
                       boolean needsBufferPerItem)
        throws ProtocolException {

        Collection items = attributes.getItems();
        boolean emulateMenu = true;
        // todo Fix this now that there are no style classes.
//                attributes.getStyleClass() != null ||
//                attributes.getId() != null ||
//                (attributes.getTagName() != null &&
//                themeContainsElementRule(attributes.getTagName()));

        if (emulateMenu && !needsBufferPerItem) {
            openMenu(buffer, attributes);
        }

        for (Iterator i = items.iterator(); i.hasNext();) {
            if (emulateMenu && needsBufferPerItem) {
                openMenu(buffer, attributes);
            }
            MenuChildVisitable unknown = (MenuChildVisitable)i.next();
            unknown.visit(this,
                          buffer,
                          attributes,
                          i.hasNext(),
                          needsBufferPerItem,
                          orientation);

            if (emulateMenu && needsBufferPerItem) {
                closeMenu(buffer, attributes);
            }

            // The pane is an iterator pane which matches the orientation
            // required for the menu so each menu item goes in its own buffer.
            // End the current buffer and then get the next buffer.
            if (needsBufferPerItem) {

                // We don't do separators in this case
                Pane pane = attributes.getPane();
                // NOTE: old menu code is unused
                ContainerInstance containerInstance =
                        (ContainerInstance) context.getFormatInstance(
                                pane, NDimensionalIndex.ZERO_DIMENSIONS);
                containerInstance.endCurrentBuffer();
                context.popOutputBuffer(buffer);
                buffer = (DOMOutputBuffer) containerInstance.getCurrentBuffer();
                context.pushOutputBuffer(buffer);
            }
        }
        if (emulateMenu && !needsBufferPerItem) {
            closeMenu(buffer, attributes);
        }
    }



    // ==========================================================================
    //   Script element methods.
    // ==========================================================================

    // ------------------------------------------------------------------------
    //   No Script Element
    // ------------------------------------------------------------------------




// Javadoc inherited from super class.
    public final void writeOpenNoScript(NoScriptAttributes attributes)
        throws ProtocolException {
        DOMOutputBuffer dom = getCurrentBuffer();
        openNoScript(dom, attributes);
    }

    // Javadoc inherited from super class.
    public final void writeCloseNoScript(NoScriptAttributes attributes) {
        DOMOutputBuffer dom = getCurrentBuffer();
        closeNoScript(dom, attributes);
    }

    /**
     * Add the open no script markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openNoScript(DOMOutputBuffer dom,
                                NoScriptAttributes attributes)
        throws ProtocolException {
    }

    /**
     * Add the close no script markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closeNoScript(DOMOutputBuffer dom,
                                 NoScriptAttributes attributes) {
    }

    // ------------------------------------------------------------------------
    //   Script Element
    // ------------------------------------------------------------------------

    // Javadoc inherited from super class.
    public void writeOpenScript(ScriptAttributes attributes) {
        DOMOutputBuffer dom = getCurrentBuffer();
        openScript(dom, attributes);
    }

    // Javadoc inherited from super class.
    public void writeCloseScript(ScriptAttributes attributes) {
        DOMOutputBuffer dom = getCurrentBuffer();
        closeScript(dom, attributes);
    }

    /**
     * Add the open script markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void openScript(DOMOutputBuffer dom,
                              ScriptAttributes attributes) {
    }

    /**
     * Add the close script markup to the specified DOMOutputBuffer.
     *
     * @param dom        The DOMOutputBuffer to use.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void closeScript(DOMOutputBuffer dom,
                               ScriptAttributes attributes) {
    }

    // ==========================================================================
    //   Extended function form element methods.
    // ==========================================================================

    protected DOMOutputBuffer getPreambleBuffer(AbstractForm form) {

        final DOMOutputBuffer dom;
        // If this is an XDIME1 Form format then retrieve the specified buffer.
        // XDIME2 will have already set the current buffer to the specified buffer
        if (form instanceof FormInstance) {
            // preambleBuffer is used to be sure that hidden attributes won't
            // be rendered after submit. Instead hidden attributes will be
            // rendered at the beginning of the form
            dom = (DOMOutputBuffer)((FormInstance)form).getPreambleBuffer(true);
        } else {
            dom = getCurrentBuffer();
        }

        return dom;
    }

    protected DOMOutputBuffer getContentBuffer(AbstractForm form) {
        final DOMOutputBuffer dom;

        if (form instanceof FormInstance) {
            // XDIME 1
            dom = (DOMOutputBuffer)((FormInstance)form).getContentBuffer(true);
        } else {
            // XDIME 2 will already have pushed correct buffer onto the stack.
            dom = getCurrentBuffer();
        }
        return dom;
    }

    protected DOMOutputBuffer getPostambleBuffer(AbstractForm form) {

        final DOMOutputBuffer dom;

        if (form instanceof FormInstance) {
            // XDIME 1
            dom = (DOMOutputBuffer)((FormInstance)form).getPostambleBuffer(true);
        } else {
            // XDIME 2 will already have pushed correct buffer onto the stack.
            dom = getCurrentBuffer();
        }
        return dom;
    }

    /**
     * Add the value to the buffer obtained from the container instance.
     *
     * @param containerInstance     the container instance used to obtain a
     *                              buffer.
     * @param value                 to add to the buffer.
     */
    protected void addToBuffer(ContainerInstance containerInstance,
                               String value) {
        getCurrentBuffer(containerInstance).appendEncoded(value);
    }

    /**
     * Get the current DOMOutputBuffer from the pane.
     *
     * @param pane the pane used to obtain the DOMOutputBuffer.
     * @return the current DOMOutputBuffer from the pane.
     * @deprecated use {@link #getCurrentBuffer(ContainerInstance)} instead.
     */
    protected DOMOutputBuffer getCurrentBuffer(Pane pane) {
      ContainerInstance containerInstance = (ContainerInstance)
            context.getDeviceLayoutContext().getCurrentFormatInstance(pane);
        return (DOMOutputBuffer) containerInstance.getCurrentBuffer();
    }

    /**
     * Get the current DOMOutputBuffer from the pane instance.
     *
     * @param containerInstance     used to obtain the DOMOutputBuffer.
     * @return the current DOMOutputBuffer from the pane instance.
     */
    protected DOMOutputBuffer getCurrentBuffer(
            ContainerInstance containerInstance) {
        return (DOMOutputBuffer) containerInstance.getCurrentBuffer();
    }

    public void doTopFragmentLinks(DOMOutputBuffer dom,
                                   XFFormAttributes attributes) throws ProtocolException {

        if (logger.isDebugEnabled()) {
            logger.debug("Rendering fragment links before form");
        }

        AbstractForm form = attributes.getFormData();

        // Check if any fragmentation links need to be written
        if (form.isFragmented()) {
            AbstractFormFragment active = getCurrentFormFragment(form);
            AbstractFormFragment previous = form
                    .getPreviousFormFragment(active);
            AbstractFormFragment next = form.getNextFormFragment(active);

            List links = active.getBeforeFragmentLinks(previous, next);

            for (int i = 0; i < links.size(); i++) {
                Link link = (Link) links.get(i);

                if (logger.isDebugEnabled()) {
                    logger.debug("Rendering " + link.getLinkName()
                            + " form fragment action before form");
                }
                doFormLink(dom, attributes, link);
            }
        }
    }

    protected void doFormFragmentButton(
            OutputBuffer buffer, XFActionAttributes action)
            throws ProtocolException {

        doActionInput((DOMOutputBuffer) buffer, action);
    }

    public void doBottomFragmentLinks(DOMOutputBuffer dom,
                                      XFFormAttributes attributes) throws ProtocolException {

        if (logger.isDebugEnabled()) {
            logger.debug("Rendering fragment links after form");
        }

        AbstractForm form = attributes.getFormData();

        // Check if any fragmentation links need to be written
        if (form.isFragmented()) {
            AbstractFormFragment active = getCurrentFormFragment(form);
            AbstractFormFragment previous = form
                    .getPreviousFormFragment(active);
            AbstractFormFragment next = form.getNextFormFragment(active);

            List links = active.getAfterFragmentLinks(previous, next);

            for (int i = 0; i < links.size(); i++) {
                Link link = (Link) links.get(i);

                if (logger.isDebugEnabled()) {
                    logger.debug("Rendering " + link.getLinkName()
                            + " form fragment action after form");
                }
                doFormLink(dom, attributes, link);
            }
        }
    }

    /**
     * Determine the current form fragment for the specified form (the mechanism
     * is different between XDIME1 and XDIME2 form implementations).
     * 
     * @param form
     *            whose currently active fragment to return
     * @return AbstractFormFragment representing the currently active form
     *         fragment in the given form
     */
    private AbstractFormFragment getCurrentFormFragment(AbstractForm form) {
        AbstractFormFragment fragment = null;
        if (form instanceof FormInstance) {
            FormFragment formFragment = context.getCurrentFormFragment();
            FormFragmentInstance formFragmentInstance = 
                (FormFragmentInstance) context.getDeviceLayoutContext().
                                  getCurrentFormatInstance(formFragment);
            fragment = formFragmentInstance;
        } else if (form instanceof FragmentableFormData) {
            fragment = ((FragmentableFormData)form).getActiveFormFragment();
        }
        return fragment;
    }

    /**
     * Add an action to the form.
     *
     * @param dom        The DOMOutputBuffer to which the action should be
     *                   written.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void doActionInput(DOMOutputBuffer dom,
                                 XFActionAttributes attributes)
        throws ProtocolException {
    }

    public final void doActionInput(XFActionAttributes attributes)
            throws ProtocolException {

        DOMOutputBuffer dom;
        ContainerInstance containerInstance;

        // If the entry pane is not set then return as there is nothing
        // else we can do.
        if ((containerInstance = attributes.getEntryContainerInstance()) == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Entry pane instance not set");
            }
            return;
        }

        // Direct the markup to the entry panes content buffer.
        dom = (DOMOutputBuffer) containerInstance.getCurrentBuffer();

        // Generate the markup for the action.
        doActionInput(dom, attributes);
    }

      /**
     * Add implicit items to the form.
     *
     * @param dom        The DOMOutputBuffer to which the action should be
     *                   written.
     * @param attributes The attributes to use when generating the markup.
     */
    protected void doImplicitValue(DOMOutputBuffer dom,
                                   XFImplicitAttributes attributes) {
        // do nothing - override if specific behaviour required
    }

    // Javadoc inherited.
    public void doImplicitValue(XFImplicitAttributes attributes) {

        AbstractForm form = attributes.getFormData();
        DOMOutputBuffer dom = getPreambleBuffer(form);
        doImplicitValue(dom, attributes);
    }

    // ==========================================================================
    //   Custom markup methods
    // ==========================================================================

    public void writeOpenElement(CustomMarkupAttributes attributes) {

        DOMOutputBuffer dom = getCurrentBuffer();

        Element element = dom.openStyledElement(attributes.getElementName(),
                                                attributes);

        Map map = attributes.getAttributes();
        for (Iterator i = map.entrySet().iterator(); i.hasNext();) {
            Map.Entry entry = (Map.Entry)i.next();
            String attribute = (String)entry.getKey();
            String value = (String)entry.getValue();
            element.setAttribute(attribute, value);
        }
    }

    public void writeCloseElement(CustomMarkupAttributes attributes) {

        DOMOutputBuffer dom = getCurrentBuffer();

        dom.closeElement(attributes.getElementName());
    }

    public void doTimer(TimerAttributes attributes) {
        doTimer(getCurrentBuffer(), attributes);
    }

    protected void doTimer(DOMOutputBuffer dom, TimerAttributes attributes) {
    }

    // javadoc inherited
    public void writeOpenNativeMarkup(NativeMarkupAttributes attributes)
        throws ProtocolException {
        OutputBuffer buffer = getNativeMarkupOutputBuffer(attributes);

        if (logger.isDebugEnabled()) {
            logger.debug("target=" + attributes.getTargetLocation() +
                         " buffer=" + buffer);
        }

        if (buffer != null) {
            context.pushOutputBuffer(buffer);
        }
    }

    /**
     * Returns the OutputBuffer in which native markup will be written.
     *
     * @param attributes Attributes that determine the output buffer to use
     * @return OutputBuffer the output buffer to be used
     * @throws ProtocolException if there is a problem with the attribute
     *                           settings or in processing the request
     */
    protected OutputBuffer getNativeMarkupOutputBuffer(
        NativeMarkupAttributes attributes) throws ProtocolException {
        String target = attributes.getTargetLocation();

        final OutputBuffer buffer;

        if (NativeMarkupAttributes.HERE.equals(target)) {
            if (logger.isDebugEnabled()) {
                logger.debug("native markup goes here");
            }

            // Ensure we are in a pane and if so write native markup elements
            // to current output buffer
            Pane currentPane = context.getCurrentPane();

            if (currentPane == null) {
                // MCSPR0007X="nativemarkup targetLocation=\"here\" must be within a pane."
                throw new ProtocolException(exceptionLocalizer.format(
                    "nativemarkup-targetlocation-here"));
            }

            buffer = getCurrentBuffer();
        } else if (NativeMarkupAttributes.PANE.equals(target)) {
            if (logger.isDebugEnabled()) {
                logger.debug("native markup goes in pane " +
                             attributes.getPane());
            }

            // The NativeMarkupElement PAPI class will have already pushed the
            // correct pane onto the pane stack. When a pane is pushed onto
            // the stack, its corresponding output buffer is pushed on too.
            buffer = getCurrentBuffer();
        } else {
            throw new ProtocolException(exceptionLocalizer.format(
                "nativemarkup-targetlocation-invalid",
                target));
        }

        return buffer;
    }

    // javadoc inherited
    public void writeCloseNativeMarkup(NativeMarkupAttributes attributes)
        throws ProtocolException {
        OutputBuffer buffer = getNativeMarkupOutputBuffer(attributes);

        if (buffer != null) {
            context.popOutputBuffer(buffer);
        }
    }

    //==========================================================================
    // Spatial Format Iterator methods
    //==========================================================================

    /* (non-Javadoc)
     * @see com.volantis.mcs.protocols.VolantisProtocol#writeCloseSpatialFormatIterator(com.volantis.mcs.protocols.SpatialFormatIteratorAttributes)
     */
    public void writeCloseSpatialFormatIterator
        (SpatialFormatIteratorAttributes attributes) {
        DOMOutputBuffer dom = getCurrentBuffer();
        closeSpatialFormatIterator(dom, attributes);
    }

    /**
     * Closes a spatial iterator. By default we use the corresponding closeGrid
     * method
     *
     * @param dom
     * @param attributes
     */
    protected void closeSpatialFormatIterator
        (DOMOutputBuffer dom, SpatialFormatIteratorAttributes attributes) {
        GridAttributes gridAttributes = makeGridAttributes(attributes);
        closeGrid(dom, gridAttributes);
    }


    /* (non-Javadoc)
     * @see com.volantis.mcs.protocols.VolantisProtocol#writeCloseSpatialFormatIteratorChild(com.volantis.mcs.protocols.SpatialFormatIteratorAttributes)
     */
    public void writeCloseSpatialFormatIteratorChild
        (SpatialFormatIteratorAttributes attributes) {
        DOMOutputBuffer dom = getCurrentBuffer();
        closeSpatialFormatIteratorChild(dom, attributes);
    }

    /**
     * Closes the child (column) of a spatial iterator. By default we use the
     * corresponding closeGridChild method
     *
     * @param dom
     * @param attributes
     */
    protected void closeSpatialFormatIteratorChild
        (DOMOutputBuffer dom, SpatialFormatIteratorAttributes attributes) {
        closeGridChild(dom, makeGridChildAttributes(attributes));
    }

    /* (non-Javadoc)
     * @see com.volantis.mcs.protocols.VolantisProtocol#writeCloseSpatialFormatIteratorRow(com.volantis.mcs.protocols.SpatialFormatIteratorAttributes)
     */
    public void writeCloseSpatialFormatIteratorRow
        (SpatialFormatIteratorAttributes attributes) {
        DOMOutputBuffer dom = getCurrentBuffer();
        closeSpatialFormatIteratorRow(dom, attributes);
    }

    /**
     * Closes the row of a spatial iterator. By default we use the
     * corresponding closeGridRow method
     *
     * @param dom
     * @param attributes
     */
    protected void closeSpatialFormatIteratorRow
        (DOMOutputBuffer dom, SpatialFormatIteratorAttributes attributes) {
        closeGridRow(dom, makeGridRowAttributes(attributes));

    }

    /* (non-Javadoc)
     * @see com.volantis.mcs.protocols.VolantisProtocol#writeOpenSpatialFormatIterator(com.volantis.mcs.protocols.SpatialFormatIteratorAttributes)
     */
    public void writeOpenSpatialFormatIterator
        (SpatialFormatIteratorAttributes attributes) {
        DOMOutputBuffer dom = getCurrentBuffer();
        openSpatialFormatIterator(dom, attributes);
    }

    /**
     * Opens a spatial iterator. By default we use the corresponding openGrid
     * method
     *
     * @param dom
     * @param attributes
     */
    protected void openSpatialFormatIterator
        (DOMOutputBuffer dom, SpatialFormatIteratorAttributes attributes) {
        GridAttributes gridAttributes = makeGridAttributes(attributes);
        openGrid(dom, gridAttributes);
    }

    /* (non-Javadoc)
     * @see com.volantis.mcs.protocols.VolantisProtocol#writeOpenSpatialFormatIteratorChild(com.volantis.mcs.protocols.SpatialFormatIteratorAttributes)
     */
    public void writeOpenSpatialFormatIteratorChild
        (SpatialFormatIteratorAttributes attributes) {
        DOMOutputBuffer dom = getCurrentBuffer();
        openSpatialFormatIteratorChild(dom, attributes);
    }

    /**
     * Opens a spatial iterator child (column). By default we use the
     * corresponding openGridChild method
     *
     * @param dom
     * @param attributes
     */
    protected void openSpatialFormatIteratorChild
        (DOMOutputBuffer dom, SpatialFormatIteratorAttributes attributes) {
        openGridChild(dom, makeGridChildAttributes(attributes));
    }


    /* (non-Javadoc)
     * @see com.volantis.mcs.protocols.VolantisProtocol#writeOpenSpatialFormatIteratorRow(com.volantis.mcs.protocols.SpatialFormatIteratorAttributes)
     */
    public void writeOpenSpatialFormatIteratorRow
        (SpatialFormatIteratorAttributes attributes) {
        DOMOutputBuffer dom = getCurrentBuffer();
        openSpatialFormatIteratorRow(dom, attributes);
    }

    /**
     * Opens a spatial iterator row. By default we use the corresponding
     * openGridRow method
     *
     * @param dom
     * @param attributes
     */
    protected void openSpatialFormatIteratorRow
        (DOMOutputBuffer dom, SpatialFormatIteratorAttributes attributes) {
        openGridRow(dom, makeGridRowAttributes(attributes));
    }

    /**
     * Convert a spatial attribtes object into a grid attributes object
     *
     * @param attributes
     * @return GridAttributes
     */
    private GridAttributes makeGridAttributes(
        SpatialFormatIteratorAttributes attributes) {

        GridAttributes gridAttributes = new GridAttributes();
        gridAttributes.setFormat(attributes.getFormat());

        gridAttributes.setStyles(attributes.getStyles());
        gridAttributes.setColumns(attributes.getColumns());

        return gridAttributes;
    }

    /**
     * Create a GridChildAttrbutes object for use in the open/closeGridChild
     * child call
     *
     * @param attributes
     * @return GridChildAttributes
     */
    private GridChildAttributes makeGridChildAttributes(
        SpatialFormatIteratorAttributes attributes) {

        GridChildAttributes childAttributes = new GridChildAttributes();
        childAttributes.setFormat(attributes.getFormat());

        // Populate the required style class attributes based on the current
        // iteration's row/column position's style class.
        childAttributes.setStyles(attributes.getStyles());
        childAttributes.setColumns(attributes.getColumns());

        return childAttributes;
    }

    /**
     * Create a GridChildAttrbutes object for use in the open/closeGridChild
     *
     * @param attributes
     * @return GridRowAttributes
     */

    private GridRowAttributes makeGridRowAttributes(
        SpatialFormatIteratorAttributes attributes) {

        GridRowAttributes rowAttributes = new GridRowAttributes();
        rowAttributes.setFormat(attributes.getFormat());

        // Populate the required style class attributes based on the current
        // iteration's row/column position's style class.
        rowAttributes.setStyles(attributes.getStyles());
        rowAttributes.setColumns(attributes.getColumns());

        return rowAttributes;
    }

    //========================================================================
    // DeprecatedImageOutput interface implementation.
    //========================================================================

    // Javadoc inherited.
    public void outputImage(DOMOutputBuffer dom, ImageAttributes attributes)
        throws ProtocolException {
        // delegate to doImage method.
        doImage(dom, attributes);
    }

    //========================================================================
    // DeprecatedLineBreakOutput interface implementation.
    //========================================================================

    // Javadoc inherited.
    public void outputLineBreak(DOMOutputBuffer dom,
                                LineBreakAttributes attributes)
        throws ProtocolException {

        // delegate to doLineBreak method.
        doLineBreak(dom, attributes);
    }

    //========================================================================
    // DeprecatedOutputLocator interface implementation.
    //========================================================================

    // Javadoc inherited
    public DeprecatedDivOutput getDivOutput() {
        return this;
    }

    // Javadoc inherited
    public DeprecatedSpanOutput getSpanOutput() {
        return this;
    }

    // Javadoc inherited
    public DeprecatedImageOutput getImageOutput() {
        return this;
    }

    // Javadoc inherited
    public DeprecatedAnchorOutput getAnchorOutput() {
        return this;
    }

    // Javadoc inherited
    public DeprecatedLineBreakOutput getLineBreakOutput() {
        return this;
    }

    // Javadoc inherited
    public DeprecatedEventAttributeUpdater getEventAttributeUpdater() {
        return null;
    }

    // Javadoc inherited
    public DeprecatedExternalShortcutRenderer getExternalShortcutRenderer() {
        return null;
    }

    // Javadoc inherited
    protected DeprecatedOutputLocator getDeprecatedOutputLocator() {
        return this;
    }

    /**
     * At this level we knwo if CSS is supported so use that info. Currently
     * this produces the same results as the method in the super class but only
     * because the supportsCSS variable is set using the same logic as used in
     * the super. This may not always be the case, hence this specialisation
     */
    // rest of javadoc inherited.
    protected MenuModuleCustomisation provideMenuModelCustomisation() {
        return new MenuModuleCustomisationImpl(supportsAccessKeyAttribute,
                                            supportsAutomaticShortcutPrefixDisplay,
                                            supportsCSS);
    }

    protected TextInputFormatParser getTextInputFormatParser() {
        return TEXT_INPUT_FORMAT_PARSER;
    }

    /**
     * Get the text input format from the attributes.
     *
     * @param attributes The XFTextInputAttributes containing the validation
     * @return The {@link TextInputFormat}, or null if there was no format
     *         specified, or it was invalid.
     */
    protected TextInputFormat getTextInputFormat(
            XFTextInputAttributes attributes) {

        Styles styles = attributes.getStyles();

        String format = getTextFromStyleValue(
                styles, StylePropertyDetails.MCS_INPUT_FORMAT,
                FORM_VALIDATOR_ENCODING);

        TextInputFormatParser parser = getTextInputFormatParser();

        return parser.parseFormat(attributes.getName(), format);
    }

    /**
     * Get text from the styles.
     *
     * <p>The style value for the specified property must be either:</p>
     *
     * <ul>
     *
     * <li>{@link StyleKeywords#NONE} - null is returned.</li>
     *
     * <li>{@link StyleString} - string value is returned.</li>
     *
     * <li>{@link StyleComponentURI} - references a text policy whose textual
     * value is returned.</li>
     *
     * </ul>
     *
     * @param styles            The styles.
     * @param property          The property whose text value is required.
     * @param requiredEncodings The required encodings for the text policy.
     * @return The text, or null if the policy could not be resolved.
     */
    protected String getTextFromStyleValue(
            Styles styles, final StyleProperty property,
            final EncodingCollection requiredEncodings) {

        AssetResolver assetResolver = getExtractorContext().getAssetResolver();
        StyleValue value = styles.getPropertyValues()
                .getStyleValue(property);

        String format;
        if (value == StyleKeywords.NONE) {
            // No validation.
            format = null;
        } else if (value instanceof StyleString) {
            StyleString string = (StyleString) value;
            format = string.getString();
        } else if (value instanceof StyleComponentURI) {
            StyleComponentURI uri = (StyleComponentURI) value;
            PolicyReference reference = assetResolver.evaluateExpression(
                    uri.getExpression());
            format = assetResolver.resolveText(reference, requiredEncodings);
        } else {
            throw new IllegalStateException(
                    "Unknown " +
                            property.getName() +
                            " value " + value);
        }
        return format;
    }

    /**
     * Get a {@link TextAssetReference} from a style.
     *
     * <p>Just as for {@link #getTextFromStyleValue(Styles,StyleProperty,EncodingCollection)}
     * the style value for the specified property must be either:</p> <ul>
     *
     * <li>{@link StyleKeywords#NONE} - null is returned.</li>
     *
     * <li>{@link StyleString} - literal reference returned.</li>
     *
     * <li>{@link StyleComponentURI} - policy reference returned.</li>
     *
     * </ul>
     *
     * @param styles   The styles.
     * @param property The property.
     * @return A {@link TextAssetReference} that represents the style value, or
     *         null.
     */
    protected TextAssetReference getTextReferenceFromStyleValue(
            Styles styles, final StyleProperty property) {

        AssetResolver assetResolver = getExtractorContext().getAssetResolver();
        StyleValue value = styles.getPropertyValues()
                .getStyleValue(property);

        TextAssetReference reference;
        if (value == StyleKeywords.NONE) {
            // No validation.
            reference = null;
        } else if (value instanceof StyleString) {
            StyleString string = (StyleString) value;
            reference = new LiteralTextAssetReference(string.getString());
        } else if (value instanceof StyleComponentURI) {
            StyleComponentURI uri = (StyleComponentURI) value;
            reference = new DefaultComponentTextAssetReference(
                    (RuntimePolicyReference) assetResolver.evaluateExpression(
                            uri.getExpression()),
                    getMarinerPageContext().getAssetResolver());
        } else {
            throw new IllegalStateException(
                    "Unknown " +
                            property.getName() +
                            " value " + value);
        }
        return reference;
    }

    // Javadoc inherited.
    public RendererContext getRendererContext() {
        if (rendererContext == null) {
            MarinerPageContext marinerPageContext = getMarinerPageContext();
            // NOTE: any methods of MPC that are visible via this context
            // (e.g. in AssetResolver) must (currently) NOT be dependent on
            // having the correct nested instance of MPC.
            // This is because we cache the entire menu module which uses this
            // class in the protocol.
            rendererContext =
                    new DOMRendererContextImpl(marinerPageContext.getAssetResolver(),
                            marinerPageContext,
                            getOutputBufferFactory(),
                            marinerPageContext.getStylePropertyResolver(),
                            inserter);
        }
        return rendererContext;
    }

    // Javadoc inherited.
    protected CssReference createExternalCSSReference() {
        Volantis volantis = context.getVolantisBean();
        Cache cssCache = volantis.getCSSCache();
        StyleSheetConfiguration configuration =
            volantis.getStyleSheetConfiguration();
        MarinerURL contextPathUrl = context.getEnvironmentContext()
            .getContextPathURL();

        return new ExternalCSSReference(getCSSModule(), cssCache,
                                        configuration, contextPathUrl,
                                        context);
    }

    // Javadoc inherited.
    protected CssReference createInternalCSSReference() {
        return new InternalCSSReference(getCSSModule());
    }

    // Javadoc inherited.
    protected CssReference createImportCSSReference() {
        return new ImportCSSReference();
    }

    private CSSModule getCSSModule() {
        return cssModule;
    }

    // Javadoc Inherited
    public ValidationHelper getValidationHelper() {
        return this.protocolConfiguration.getValidationHelper();
    }

    /**
     * Open a special style element, who's only purpose is to hold style
     * attributes. This method is called by an open method which does not
     * create an element which could hold the attributes.
     *
     * @param dom        place to add style element.
     * @param attributes source of styles to add to element.
     */
    protected void openStyleMarker(DOMOutputBuffer dom,
                                   StyleContainer attributes) {
        dom.openStyledElement(CSSConstants.STYLE_ELEMENT, attributes);
    }

    /**
     * Matching close special style element.
     *
     * @param dom
     */
    protected void closeStyleMarker(DOMOutputBuffer dom) {
        dom.closeElement(CSSConstants.STYLE_ELEMENT);
    }

    /**
     * Does this protocol support events
     * @return
     */
    public boolean supportsEvents() {
        return this.protocolConfiguration.supportsEvents();
    }

    /**
     * Output a caption for a form field.
     *
     * @param attributes    to use to determine how to output the field's
     *                      caption
     * @throws ProtocolException if there was a problem writing the caption.
     */
    protected void writeCaption(XFFormFieldAttributes attributes)
            throws ProtocolException {

        ContainerInstance captionContainerInstance =
                attributes.getCaptionContainerInstance();
        ContainerInstance entryContainerInstance =
                attributes.getEntryContainerInstance();
        String value = getPlainText(attributes.getCaption());

        // If the caption container instance is null, then output to the entry
        // container instance.
        if (captionContainerInstance == null) {
            captionContainerInstance = entryContainerInstance;
        }

        // can only continue if there is a caption and a pane to which to output.
        if (captionContainerInstance != null && value != null) {
            DOMOutputBuffer dom = getCurrentBuffer(captionContainerInstance);

            SpanAttributes spanAttributes = new SpanAttributes();
            spanAttributes.copy(attributes);
            // Ensure that we do not copy ID as duplicate IDs in the ouput
            // are invalid and will confused Javascript, etc.
            // TODO: later: copy id from label element if it is available.
            // Trivial way to do this of adding setCaptionId seems wrong. It
            // should be associated with the styles, etc in its own object, but
            // this refactoring looks a bit tricky and Emma is away so later...
            spanAttributes.setId(null);
            // if the caption styles are non null, then use these, otherwise
            // use the ones on the field attributes.
            if (attributes.getCaptionStyles() != null) {
                spanAttributes.setStyles(attributes.getCaptionStyles());
            }

            openSpan(dom, spanAttributes);
            dom.appendEncoded(value);
            closeSpan(dom, spanAttributes);
        }
    }

    /**
     * Get the maximum line length for the requesting device.
     * @return the maximum line length in characters for the requesting
     *         device.
     */
    protected int getMaximumLineLength() {
        String lineLength = context.getDevicePolicyValue(
                DevicePolicyConstants.MAXIMUM_LINE_CHARS);

        int result = 0;

        if ((lineLength != null) && (lineLength.length() > 0)) {
            result = Integer.parseInt(lineLength);
        }

        return result;
    }

    /**
     * Get the content of the STYLE element for the requesting device.
     * @return the content of the STYLE element for the requesting
     *         device.
     */
    protected String getXElementStyleContent() {
        return context.getDevicePolicyValue(
                DevicePolicyConstants.X_ELEMENT_STYLE_CONTENT);
    }

    /**
     * Get the content of the SCRIPT element for the requesting device.
     * @return the content of the SCRIPT element for the requesting
     *         device.
     */
    protected String getXElementScriptContent() {
        return context.getDevicePolicyValue(
                DevicePolicyConstants.X_ELEMENT_SCRIPT_CONTENT);
    }

    /**
     * Creates a script element with the given attributes.
     *
     * @param attributes the script attributes to add
     * @return the create element of null if the protocol doesn't support
     * script elements.
     */
    public Element createScriptElement(final ScriptAttributes attributes) {
        return null;
    }

    /**
     * Create the style sheet renderer that this protocol requires and
     * store it in {@link #styleSheetRenderer}.
     */
    protected void createStyleEmulationRenderer() {
        styleEmulationRenderer = new DefaultStyleEmulationRenderer(
                protocolConfiguration.getStyleEmulationPropertyRendererSelector());
    }

    /**
     * Create the style emulation transformer that should be used by this
     * protocol. It will be configured using the
     * {@link StyleEmulationElementConfiguration} specified in the protocol
     * configuration.
     *
     * @return StyleEmulationTransformer
     */
    private StyleEmulationTransformer createStyleEmulationTransformer() {
        final StyleEmulationElementConfiguration styleEmulationElements =
                protocolConfiguration.getStyleEmulationElementConfiguration();
        return new StyleEmulationTransformer(styleEmulationElements);
    }

    /**
     * Returns a set with locked elements, creating it if not
     * already created, unless the <code>create</code> flag is <code>false</code>.
     * 
     * @param create The create flag.
     * @return A set of locked elements.
     */
    private Set getLockedElements(boolean create) {
        if (lockedElements == null) {
            if (create) {
                lockedElements = new HashSet();
            }
        }

        return lockedElements;
    }

    /**
     * Marks specified element as locked.
     * <p>
     * A locked element will be not changed or removed
     * during DOM optimalization/transformation. It'll 
     * remain unchanged in the output page.
     * 
     * @param element An element to lock
     */
    public void setElementLocked(Element element) {
        // Retrieve a set with locked elements, creating it if nessecarry,
        // and add specified element to that set.
        getLockedElements(true).add(element);
    }

    /**
     * Returns <code>true</code>, if the element is locked.
     * 
     * @param element The element to check.
     */
    public boolean isElementLocked(Element element) {
        Set lockedElements = getLockedElements(false);

        return (lockedElements != null) && lockedElements.contains(element);
    }
    
    /**
     * Set or reset a marker on an element indicating, that the element
     * will be transformed to a text node. The content of the text node
     * will equal to the serialized form of the element XML content.
     * 
     * @param element The element to set/reset the marker on
     * @param marked The marker value
     */
    public void setTransformToTextMarker(Element element, boolean marked) {
        if (marked) {
            element.setAttribute(TRANSFORM_TO_TEXT_ATTRIBUTE_NAME, "");
        } else {
            element.removeAttribute(TRANSFORM_TO_TEXT_ATTRIBUTE_NAME);
        }
    }

    /**
     * Returns true, if the element is marked to be transformed to a text node.
     * 
     * @param element The element to check.
     * @return The marker.
     */
    public boolean getTransformToTextMarker(Element element) {
        return element.getAttributeValue(TRANSFORM_TO_TEXT_ATTRIBUTE_NAME) != null;
    }
    
    /**
     * Transform the document, so that all marked element nodes are transformed
     * to text nodes, where the content of the text node equals to the
     * serialized form of an element (the string representing the DOM).
     */
    private void transformMarkedElementsToText() {
        document = new ElementToTextTransformer().transform(this, document);        
    }
    
    /**
     * Returns the extractor context used.
     * 
     * @return the extractor context used.
     */
    public RuntimeExtractorContext getExtractorContext() {
        return extractorContext;
    }
}


/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 12-Dec-05	10791/1	ianw	VBM:2005071309 Ported forward meta changes

 12-Dec-05	10785/7	ianw	VBM:2005071309 Ported forward meta changes

 12-Dec-05	10785/5	ianw	VBM:2005071309 Ported forward meta changes

 12-Dec-05	10785/3	ianw	VBM:2005071309 Ported forward meta changes

 12-Dec-05	10785/1	ianw	VBM:2005071309 Ported forward meta changes

 07-Dec-05	10321/4	emma	VBM:2005103109 Supermerge required

 15-Nov-05	10321/1	emma	VBM:2005103109 Forward port: Styling not applied correctly to some xf selectors

 14-Nov-05	10300/1	emma	VBM:2005103109 Styling not applied correctly to some xf selectors

 06-Dec-05	10638/4	emma	VBM:2005120505 supermerged

 06-Dec-05	10638/1	emma	VBM:2005120505 Forward port: Generated XHTML was invalid - had no head tag but had head content

 06-Dec-05	10623/1	emma	VBM:2005120505 Generated XHTML was invalid: missing head tag but head content

 06-Dec-05	10641/1	geoff	VBM:2005113024 Pagination page rendering issues

 06-Dec-05	10621/1	geoff	VBM:2005113024 Pagination page rendering issues

 05-Dec-05	10527/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 29-Nov-05	10505/13	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (7)

 29-Nov-05	10347/7	pduffin	VBM:2005111405 Massive changes for performance

 29-Nov-05	10505/9	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (6)

 21-Nov-05	10347/1	pduffin	VBM:2005111405 Cleaned up PropertyValues to remove synthesised properties and moved specified into an extended interface

 29-Nov-05	10505/5	pduffin	VBM:2005111405 Committing transactions from MCS 3.5.0 (4)

 18-Nov-05	10370/1	geoff	VBM:2005111405 interim commit

 29-Nov-05	10480/1	pduffin	VBM:2005070711 Merged changes from main trunk

 29-Nov-05	10478/1	pduffin	VBM:2005070711 Fixed merge conflicts

 28-Nov-05	10443/2	ianw	VBM:2005111812 interim commit for IB

 25-Nov-05	10453/1	rgreenall	VBM:2005092107 Restrict the length of lines written by MCS for devices that have a maximum line limit.

 25-Nov-05	9708/6	rgreenall	VBM:2005092107 Restrict the length of lines written by MCS for devices that have a maximum line limit.

 03-Oct-05	9683/1	rgreenall	VBM:2005092107 Added buffering to LineLengthRestrictingWriter and simplified implementation.

 03-Oct-05	9681/1	rgreenall	VBM:2005092107 Added buffering to LineLengthRestrictingWriter and simplified implementation.

 07-Nov-05	10116/3	emma	VBM:2005103107 Supermerge

 07-Nov-05	10116/1	emma	VBM:2005103107 Fixes to correctly apply styles to various selectors

 31-Oct-05	10048/1	ibush	VBM:2005081219 Horizontal Rule Emulation

 24-Oct-05	9565/19	ibush	VBM:2005081219 Horizontal Rule Emulation

 29-Sep-05	9565/8	ibush	VBM:2005081219 Horizontal Rule Emulation

 22-Sep-05	9565/5	ibush	VBM:2005081219 HR Rule Emulation

 22-Sep-05	9565/2	ibush	VBM:2005081219 HR Rule Emulation

 29-Nov-05	10478/1	pduffin	VBM:2005070711 Fixed merge conflicts

 28-Nov-05	10467/1	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 28-Nov-05	10443/2	ianw	VBM:2005111812 interim commit for IB

 25-Nov-05	9708/6	rgreenall	VBM:2005092107 Restrict the length of lines written by MCS for devices that have a maximum line limit.

 03-Oct-05	9683/1	rgreenall	VBM:2005092107 Added buffering to LineLengthRestrictingWriter and simplified implementation.

 03-Oct-05	9681/1	rgreenall	VBM:2005092107 Added buffering to LineLengthRestrictingWriter and simplified implementation.

 07-Nov-05	10173/3	emma	VBM:2005103107 Supermerge required

 07-Nov-05	10173/1	emma	VBM:2005103107 Forward port: Fixes to correctly apply styles to various selectors

 07-Nov-05	10116/1	emma	VBM:2005103107 Fixes to correctly apply styles to various selectors

 24-Oct-05	9565/19	ibush	VBM:2005081219 Horizontal Rule Emulation

 29-Sep-05	9565/8	ibush	VBM:2005081219 Horizontal Rule Emulation

 22-Sep-05	9565/5	ibush	VBM:2005081219 HR Rule Emulation

 22-Sep-05	9565/2	ibush	VBM:2005081219 HR Rule Emulation

 12-Oct-05	9673/7	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 10-Oct-05	9673/4	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 11-Oct-05	9729/2	geoff	VBM:2005100507 Mariner Export fails with NPE

 10-Oct-05	9637/11	emma	VBM:2005092807 Adding tests for XForms emulation
 05-Oct-05	9440/2	schaloner	VBM:2005070711 Added marker pseudo-element support

 03-Oct-05	9673/2	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 02-Oct-05	9637/9	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 30-Sep-05	9637/7	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 30-Sep-05	9637/5	emma	VBM:2005092807 XForms in XDIME-CP (without tests)

 27-Sep-05	9487/3	pduffin	VBM:2005091203 Committing new CSS Parser

 22-Sep-05	9540/4	geoff	VBM:2005091906 Protocol Parameterisation: Basic Rendundant CSS Property Filtering

 21-Sep-05	9128/12	pabbott	VBM:2005071114 Review feedback for XHTML2 elements

 20-Sep-05	9128/8	pabbott	VBM:2005071114 Add XHTML 2 elements

 19-Sep-05	9472/8	ibush	VBM:2005090808 Add default styling for sub/sup elements

 14-Sep-05	9472/5	ibush	VBM:2005090808 Add default styling for sub/sup elements

 09-Sep-05	9472/1	ibush	VBM:2005090808 Add default styling for sub/sup elements

 15-Sep-05	9524/1	emma	VBM:2005091503 Added ContainerInstance to allow regions and panes to be treated in the same way

 12-Sep-05	9372/4	ianw	VBM:2005082221 Allow only one instance of MarinerPageContext for a page

 02-Sep-05	9408/3	pabbott	VBM:2005083007 Move over to using JiBX accessor

 01-Sep-05	9407/1	pduffin	VBM:2005083007 Changed MIB2_1 and Netfront3 configuration to remove device specific theme, and replaced it with a new initial value finder that is device aware

 02-Sep-05	9407/4	pduffin	VBM:2005083007 Committing resolved conflicts

 01-Sep-05	9407/1	pduffin	VBM:2005083007 Changed MIB2_1 and Netfront3 configuration to remove device specific theme, and replaced it with a new initial value finder that is device aware

 01-Sep-05	9375/4	geoff	VBM:2005082301 XDIMECP: clean up protocol creation

 30-Aug-05	9353/5	pduffin	VBM:2005081912 Removed style class from MCS Attributes

 22-Aug-05	9363/6	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9363/3	emma	VBM:2005080405 Remove the 'supports multi class' properties

 22-Aug-05	9298/10	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 22-Aug-05	9324/5	ianw	VBM:2005080202 Move validation for WapCSS into styling

 22-Aug-05	9184/1	pabbott	VBM:2005080508 Move CSS eumlator to post process step

 18-Aug-05	9007/2	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 04-Aug-05	9151/3	pduffin	VBM:2005080205 Added back in some code that should not have been removed

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 01-Aug-05	9110/4	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 22-Jul-05	9110/2	pduffin	VBM:2005072107 First stab at integrating new themes stuff together

 11-Jul-05	8862/2	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 23-Jun-05	8833/8	pduffin	VBM:2005042901 Addressing review comments

 23-Jun-05	8833/5	pduffin	VBM:2005042901 Addressing review comments

 23-Jun-05	8833/3	pduffin	VBM:2005042901 Addressing review comments

 21-Jun-05	8833/1	pduffin	VBM:2005042901 Merged changes from MCS 3.3.1, improved testability of the protocols

 17-Jun-05	8830/2	pduffin	VBM:2005042901 Merging from 3.2.3 - Added support for specifying symbols, punctuation marks, or numbers in a text format

 20-Jun-05	8483/3	emma	VBM:2005052410 Migrate styling to use the new styling support framework (still using CSSEmulator to style underneath)

 09-Jun-05	8665/5	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info, some tests commented out temporarily

 09-Jun-05	8665/2	emma	VBM:2005060204 Refactoring in order to annotate DOM with style info

 02-Jun-05	8005/4	pduffin	VBM:2005050404 Moved dom to its own subsystem

 05-May-05	8005/1	pduffin	VBM:2005050404 Separated DOM from within runtime into its own subsystem, move concrete DOM objects out of API, replaced with interfaces and factories, removed pooling

 24-May-05	8123/4	ianw	VBM:2005050906 Fix merge conflicts

 11-May-05	8123/1	ianw	VBM:2005050906 Refactored DeviceTheme to seperat out CSSEmulator

 18-May-05	8273/1	tom	VBM:2004091703 Added Stylistic Blink Support to iMode

 05-Apr-05	7459/4	tom	VBM:2005032101 Added SmartClientSkin protocol

 04-Apr-05	7459/1	tom	VBM:2005032101 Added SmartClientSkin protocol

 05-Apr-05	7513/1	geoff	VBM:2003100606 DOMOutputBuffer allows creation of text which renders incorrectly in WML

 11-Mar-05	7308/3	tom	VBM:2005030702 Added XHTMLSmartClient and support for image sequences

 17-Feb-05	6957/6	geoff	VBM:2005021103 R821: Branding using Projects: Prerequisites: use current project in PAPI phase

 17-Feb-05	6957/3	geoff	VBM:2005021103 R821: Branding using Projects: Prerequisites: use current project in PAPI phase

 11-Feb-05	6931/1	geoff	VBM:2005020901 R821: Branding using Projects: Prerequisites: Fix remaining manual id creation

 09-Feb-05	6914/1	geoff	VBM:2005020707 R821: Branding using Projects: Prerequisites: Fix schema and related expressions

 16-Feb-05	6129/14	matthew	VBM:2004102019 yet another supermerge

 16-Feb-05	6129/12	matthew	VBM:2004102019 yet another supermerge

 03-Feb-05	6129/10	matthew	VBM:2004102019 Add code for Shortcut Label renderin and remove the testcases for the old menu system

 28-Jan-05	6129/8	matthew	VBM:2004102019 supermerge required

 27-Jan-05	6129/6	matthew	VBM:2004102019 supermerge required

 23-Nov-04	6129/3	matthew	VBM:2004102019 Enable shortcut menu link rendering

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/6	doug	VBM:2004111702 Refactored Logging framework

 26-Nov-04	6298/2	geoff	VBM:2004112405 MCS NullPointerException in wml code path

 22-Nov-04	5733/1	geoff	VBM:2004093001 Support OMA WCSS subset of CSS2

 19-Nov-04	6253/1	claire	VBM:2004111704 mergevbm: Handle portal themes correctly and remove caching of themes and emulation in protocols

 19-Nov-04	6236/1	claire	VBM:2004111704 Handle portal themes correctly and remove caching of themes and emulation in protocols

 12-Nov-04	6135/2	byron	VBM:2004081726 Allow spatial format iterators within forms

 05-Nov-04	6112/5	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 05-Nov-04	6112/3	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 03-Nov-04	5871/3	byron	VBM:2004101319 Support style classes: Runtime XHTMLFull

 02-Nov-04	5882/2	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 20-Oct-04	5816/6	byron	VBM:2004101318 Support style classes: Runtime XHTMLBasic - merge issues

 20-Oct-04	5816/3	byron	VBM:2004101318 Support style classes: Runtime XHTMLBasic - rework

 19-Oct-04	5816/1	byron	VBM:2004101318 Support style classes: Runtime XHTMLBasic

 20-Oct-04	5879/1	claire	VBM:2004100503 mergevbm: Ensure portlet styles work on multiple inclusions

 20-Oct-04	5858/1	claire	VBM:2004100503 Ensure portlet styles work on multiple inclusions

 14-Oct-04	5808/1	byron	VBM:2004101317 Support style classes: Runtime DOMProtocol/DeviceLayoutRenderer

 12-Oct-04	5763/2	geoff	VBM:2004100105 MCS: internal-generation cache has issues with themes in a Portlet

 01-Oct-04	5635/2	geoff	VBM:2004092216 Port VDXML to MCS: update menu support

 20-Jul-04	4713/9	geoff	VBM:2004061004 Support iterated Regions (fix merge conflicts)

 02-Jul-04	4713/6	geoff	VBM:2004061004 Support iterated Regions (review comments)

 29-Jun-04	4713/3	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 12-Jul-04	4783/3	geoff	VBM:2004062302 Implementation of theme style options: WML Family

 01-Jul-04	4778/3	allan	VBM:2004062912 Use the Volantis.pageURLRewriter to rewrite page urls

 30-Jun-04	4781/1	adrianj	VBM:2002111405 VolantisProtocol.defaultMimeType() and DOMProtocol made abstract

 28-Jun-04	4733/2	allan	VBM:2004062105 Convert Volantis to use the new PageURLRewriter

 16-Jun-04	4704/4	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name. (use generic name for current format index)

 14-Jun-04	4704/2	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 08-Jun-04	4661/1	steve	VBM:2004060309 enable asset URL suffix attribute

 08-Jun-04	4643/2	steve	VBM:2004060309 enable asset URL suffix attribute

 01-Jun-04	4616/1	geoff	VBM:2004060103 Add proper support for protocol page debug outputter

 01-Jun-04	4614/1	geoff	VBM:2004060103 Add proper support for protocol page debug outputter

 14-May-04	4315/7	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 13-May-04	4315/5	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 13-May-04	4315/3	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 12-May-04	4315/1	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 12-May-04	4279/3	pduffin	VBM:2004051104 Major refactoring to simplify extending the infrastructure

 11-May-04	4217/1	geoff	VBM:2004042807 Enhance Menu Support: Renderers: Menu Markup

 07-May-04	4164/6	pduffin	VBM:2004050404 Refactored separator arbitrator and started integrating with rest of menus

 30-Apr-04	3910/2	byron	VBM:2004021117 Fixed merge conflicts

 29-Apr-04	4091/3	claire	VBM:2004042804 Enhanced menus: protocol integration and Openwave end to end

 06-May-04	4174/5	claire	VBM:2004050501 Enhanced Menus: (X)HTML menu renderer selectors and protocol integration

 06-May-04	4153/5	geoff	VBM:2004043005 Enhance Menu Support: Renderers: HTML Menu Item Renderers: refactoring

 30-Apr-04	3910/2	byron	VBM:2004021117 Fixed merge conflicts

 29-Apr-04	4091/3	claire	VBM:2004042804 Enhanced menus: protocol integration and Openwave end to end

 06-May-04	3272/2	philws	VBM:2004021117 Fix merge issues

 30-Apr-04	3910/2	byron	VBM:2004021117 Fixed merge conflicts

 29-Apr-04	4091/3	claire	VBM:2004042804 Enhanced menus: protocol integration and Openwave end to end

 05-May-04	4167/3	steve	VBM:2004042901 Fix up javadoc

 04-May-04	4167/1	steve	VBM:2004042901 Patched from Proteus

 04-May-04	4117/2	steve	VBM:2004042901 Style class rendering fix

 29-Apr-04	4098/1	mat	VBM:2004042809 Made pooling of objects in the DOMProtocol configurable

 23-Mar-04	3362/1	steve	VBM:2003082208 Move API doclet to Synergetics and myriads of javadoc fixes

 26-Feb-04	3233/1	geoff	VBM:2004021609 styleclasses incorrectly rendered when within a region

 24-Feb-04	3179/1	geoff	VBM:2004021609 styleclasses incorrectly rendered when within a region

 25-Feb-04	2974/4	steve	VBM:2004020608 supermerged

 17-Feb-04	2974/1	steve	VBM:2004020608 SGML Quote handling

 05-Feb-04	2794/1	steve	VBM:2004012613 HTML Quote handling

 19-Feb-04	2789/3	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 02-Oct-03	1469/1	geoff	VBM:2003091701 Emulate Openwave-Style menus for non Openwave WML 1.2 protocols

 23-Sep-03	1412/9	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links (rework to get link style from source fragment)

 17-Sep-03	1412/6	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links

 18-Sep-03	1394/7	doug	VBM:2003090902 Added support for Openwave numeric shortcut menus

 17-Sep-03	1394/4	doug	VBM:2003090902 added support for openwave numeric shortcut menus

 10-Sep-03	1386/1	philws	VBM:2003090801 (X)HTML support for native markup

 10-Sep-03	1379/2	philws	VBM:2003090801 (X)HTML support for native markup

 21-Aug-03	1052/7	allan	VBM:2003073101 Wrap each menu item within a menu if required

 17-Aug-03	1052/3	allan	VBM:2003073101 Support styles on menu and menuitems

 01-Jul-03	178/3	sumit	VBM:2003032713 Fixed NullPointerException and updated DTD with menuitemgroup entities

 30-Jun-03	605/1	geoff	VBM:2003060607 port from metis to mimas

 27-Jun-03	559/1	geoff	VBM:2003060607 make WML use protocol configuration again

 17-Jun-03	427/1	mat	VBM:2003061607 Add Menu Horizontal Separator theme

 12-Jun-03	381/1	mat	VBM:2003061101 Better debugging for WMLRoot

 05-Jun-03	285/49	mat	VBM:2003042911 Merged with MCS

 ===========================================================================
*/
