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
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 29-Oct-01    Paul            VBM:2001102901 - Created.
 * 02-Nov-01    Paul            VBM:2001102403 - Added support for regions,
 *                              this included changing the extra parameter from
 *                              MarinerPageContext to DeviceLayoutContext as it
 *                              is now possible for a single page to contain
 *                              multiple layouts which need rendering. The
 *                              DeviceLayoutContext class encapsulates all that
 *                              information.
 * 09-Nov-01    Allan           VBM:2001110905 - Some app servers append
 *                              a / to the base URL some don't.  Fix to
 *                              ensure that there is only one / between the
 *                              baseURL and the URL. Changed
 *                              createFragmentAnchor().
 * 19-Nov-01    Paul            VBM:2001110202 - Renamed createFragmentAnchor
 *                              to writeFragmentLink and added support for
 *                              storing the fragmentation state in the session.
 * 21-Nov-01    Payal           VBM:2001111902 - Modified method doComment(),
 *                              Added flag ,set to true makes comments work
 *                              with the debug config attribute "comments"
 *                              that enable or disable comments when specified
 *                              using doComment.
 * 22-Nov-01    Paul            VBM:2001110202 - Added support for dissecting
 *                              within inclusions. This involved passing enough
 *                              information through to the protocol for the
 *                              WMLContentTree to determine which inclusion
 *                              the dissecting content came from and also to
 *                              make sure that it did not need to find the
 *                              correct DissectingPane instance.
 * 22-Nov-01    Pether          VBM:2001112101 - In writeFragmentLink() added
 *                              support to retrive linkText and backLinkText
 *                              and check if it's plain text or text component.
 * 26-Nov-01    Doug            VBM:2001112004 - modified the method
 *                              writeFragmentLink to use PAPIURL's rather than
 *                              MutableURL's and to perform any URL rewriting
 *                              that might be required.
 * 26-Nov-01    Pether          VBM:2001112101 - In writeFragmentLink() in
 *                              order to maintain backwards compatability if
 *                              the back link is not set we use the normal
 *                              link text.
 * 27-Nov-01    Pether          VBM:2001112101 - Changed the way to handle
 *                              expressions in the linkText, are now using
 *                              the method handleMarinerExpression() in
 *                              MarinerPageContext.  This changes was made in
 *                              the writeFragmentLink() method.
 * 28-Nov-01    Paul            VBM:2001112202 - Updated to reflect changes
 *                              in papi classes and removed dependency of this
 *                              class on HttpServletRequest.
 * 29-Nov-01    Paul            VBM:2001112906 - Renamed the method
 *                              getMarinerRequestContext to getRequestContext
 *                              for consistency.
 * 19-Dec-01    Doug            VBM:2001121701 - modified the method
 *                              writeFragmentLink so that the text for the link
 *                              is retrived from a FragmentInstance object
 * 21-Dec-01    Paul            VBM:2001121702 - Use session URLRewriter
 *                              instead of calling request.encodeURL directly.
 * 02-Jan-02    Paul            VBM:2002010201 - Use log4j for logging.
 * 24-Jan-02    Steve	    	VBM:2002011412 - New visit() and render()
 *                              methods for replicas.
 * 29-Jan-02    Steve           VBM:2002011411 - Peer fragment links.
 * 30-Jan-02    Mat             VBM:2002011410 - Changed render() to write
 *                              the pre- and postamble buffers from the
 *                              DeviceLayoutContext around the layout.
 * 31-Jan-02    Paul            VBM:2001122105 - Updated to reflect changes
 *                              to protocols.
 * 12-Feb-02    Paul            VBM:2002021201 - Removed System.out.println
 *                              statement.
 * 13-Feb-02    Paul            VBM:2002021203 - Updated to reflect the fact
 *                              that the fragmentation state cache has moved
 *                              from session into PageGenerationCache. Also
 *                              fixed formatting on the peer fragments.
 * 14-Feb-02    Steve           VBM:2001101803 - Added suite of methods to
 *                              maintain the fragmentation state for forms.
 *                              uses the new PageGenerationCache to do so.
 * 15-Feb-02    Paul            VBM:2002021203 -  Replaced literal url
 *                              parameter names with constants from
 *                              URLConstants, also renamed server side include
 *                              methods to content component include.
 * 15-Feb-02    Allan           VBM:2002021303 - Modified all calls to
 *                              writePageComment() so that the overide param
 *                              is set to false.
 * 28-Feb-02    Paul            VBM:2002022804 - Made the protocols
 *                              responsible for writing the contents of the
 *                              different formats.
 * 01-Mar-02    Mat             VBM:2002021203 - In render(), changed call to
 *                              writeContentComponentInclude() to
 *                              writeWSDirective()
 * 08-Mar-02    Paul            VBM:2002030607 - Moved the writing of the
 *                              device layouts preamble and postamble buffer
 *                              into the protocol writeOpenLayout methods.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 22-Feb-02    Paul            VBM:2002021802 - Set a flag to indicate whether
 *                              dissection is needed and also redid the
 *                              rendering of regions to match the changes to
 *                              the region context.
 * 26-Mar-02    Allan           VBM:2002022007 - Removed all calls to
 *                              writePageComment() - too many methods to list.
 * 25-Apr-02    Paul            VBM:2002042202 - Removed a lot of unnecessary
 *                              writeFormatPre/Post methods.
 * 26-Apr-02    Paul            VBM:2002042205 - Initialise the device layout
 *                              context properly.
 * 03-May-02    Paul            VBM:2002042203 - Always called protocol when
 *                              a dissecting pane is found as the protocols
 *                              need to be able to decide when to dissect
 *                              based on the properties.
 * 06-Aug-02    Paul            VBM:2002073008 - Allow protocol to control
 *                              where inclusions are written.
 * 10-Sep-02    Steve           VBM:2002040809 - Set the pane styleclass in
 *                              row and column iterator panes from the
 *                              relevant pane context.
 * 23-Oct-02    Geoff           VBM:2002100901 - Added call to isEmpty to all
 *                              the visit methods and removed it from grid
 *                              render so that pages with layouts that don't
 *                              start with a grid still render properly when
 *                              no content is provided for those layouts.
 * 28-Oct-02    Chris W         VBM:2002101801 - Added a visit method for
 *                              spatial and temporal format iterators to the
 *                              FormatRenderer inner class. Added corresponding
 *                              render methods too.
 * 01-Nov-02    Ian             VBM:2002091806 - Updated Dissecting Panes and
 *                              Fragments to be application specific.
 * 07-Nov-02    Sumit           VBM:2002111105 - Implemented render methods
 *                              for Spatial and Temporal Format Iterators.
 *                              Modified the render methods for Row and Column
 *                              iterator panes to use the new NDimensional
 *                              container
 * 15-Nov-02    Sumit           VBM:2002102403 - Removed dimension from render
 *                              spatial and temporal and incFormatInstanceRef()
 * 18-Nov-02    Geoff           VBM:2002111504 - Avoid deprecated methods of
 *                              page context.
 * 20-Nov-02    Chris W         VBM:2002111105 - Reset NDimensionalIndex
 *                              at the start of each row when rendering a down
 *                              then across SpatialFormatIterator, only release
 *                              a SpatialFormatIterator context if it is not
 *                              contained in another SpatialFormatIterator and
 *                              chnaged calls to access static methods correctly.
 * 21-Nov-02    Chris W         VBM:2002111105 - Only release row/column iterator
 *                              context if they are not contained within a
 *                              SpatialFormatIterator.
 * 18-Nov-02    Geoff           VBM:2002111504 - Avoid deprecated methods of
 *                              page context.
 * 29-Nov-02    Geoff           VBM:2002112905 - Add workaround to render(form)
 *                              so that people writing content into child panes
 *                              of a form that has no matching XFForm in the
 *                              content doesn't throw an exception, and cleaned
 *                              up the usual suspects (javadocs, unused).
 * 06-Dec-02    Sumit           VBM:2002112806 - render(Grid) uses the current
 *                              FormatInstanceRef to get the requiredRows/Cols
 * 09-Dec-02    Allan           VBM:2002120615 - Replaced String version of
 *                              Format type with FormatType where possible.
 * 12-Dec-02    Sumit           VBM:2002112614 - render(spatial) sets the
 *                              format on the gridrow and gridcolumn attributes
 * 13-Jan-03    Chris W         VBM:2003011311 - render(spatial) rewritten to
 *                              implmenent variable or fixed no. of columns and
 *                              rows. render(temporal) now copes with variable
 *                              or fixed no. of cells too.
 * 28-Jan-03    Mat             VBM:2003012224 - When rendering a temporal
 *                              format iterator, prevent the index for the
 *                              timeValueArray wrapping at the end of the
 *                              array.
 * 28-Jan-03    Geoff           VBM:2003012802 - Fix render(form) so that it
 *                              detects form content even when we are using
 *                              WML fragmented forms.
 * 29-Jan-03    Chris W         VBM:2003012203 - Decision on whether or not a
 *                              cell is rendered in a spatial or temporal format
 *                              iterator moved up to PAPI elements, so
 *                              render(spatial) and render(temporal) amended
 *                              to improve performance.
 * 11-Feb-03    Ian             VBM:2003020607 - Ported MPS conditional check
 *                              on dissection from Metis.
 * 05-Mar-03    Sumit           VBM:2003022605 - Updated to render spatials
 *                              using new methods in VolantisProtocol.
 * 14-Mar-03    Doug            VBM:2003030409 - Modified
 *                              renderLayout(DeviceLayoutContext context) to
 *                              render parent/peer links in the order specified
 *                              in the fragments attributes. Modified
 *                              render(DissectingPane, DeviceLayoutContext) to
 *                              handle shard link ordering.
 * 20-Mar-03    sumit           VBM:2003031809 - Wrapped logger.debug
 *                              statements in if(logger.isDebugEnabled()) block
 * 21-Mar-03    Byron           VBM:2003031907 - Modified render to use set
 *                              Link/BackLink text on the attributes.
 * 31-Mar-03    Steve           VBM:2003031907 - Use the passed Context in the
 *                              dissecting pane renderer as the page context
 *                              returns the wrong one if you are included in
 *                              a region.
 * 16-Apr-03    Geoff           VBM:2003041603 - Add declaration for
 *                              FormatVisitorException where necessary, catch
 *                              ProtocolException and rethrow
 *                              FormatVisitorException in
 *                              writeDefaultSegmentLink and writeFragmentLink,
 *                              remove RendererException and replace with
 *                              FormatVisitorException.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.FormatType;
import com.volantis.mcs.layouts.Fragment;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.layouts.FormatInstance;
import com.volantis.mcs.protocols.layouts.LayoutAttributesFactory;
import com.volantis.mcs.protocols.layouts.LayoutAttributesFactoryImpl;
import com.volantis.mcs.protocols.layouts.LayoutModule;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.protocols.renderer.layouts.FormatRendererContext;
import com.volantis.mcs.protocols.renderer.layouts.FragmentLinkWriter;
import com.volantis.mcs.protocols.renderer.layouts.SegmentLinkWriter;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayout;
import com.volantis.mcs.runtime.layouts.styling.FormatStylingEngine;
import com.volantis.mcs.runtime.styling.CompiledStyleSheetCollection;
import com.volantis.styling.sheet.CompiledStyleSheet;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.IOException;

/**
 * This class renders a layout to the page.<p>
 *
 * Many of the methods were copied from Layout and the Format classes and so
 * have very similar names. For this reason separating comments have been
 * added to group the methods into sections relating to one particular Format
 * sub class. Please make sure that when adding new methods to this class that
 * you follow the same style as otherwise it will become very difficult to
 * maintain.
 */
public class DeviceLayoutRenderer {
    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(DeviceLayoutRenderer.class);

    /**
     * The reference to the single allowable instance of this class.
     */
    private static DeviceLayoutRenderer singleton;

    /**
     * Get the single allowable instance of this class.
     * @return The single allowable instance of this class.
     */
    public static DeviceLayoutRenderer getSingleton() {
        if (singleton == null) {
            singleton = new DeviceLayoutRenderer(
                    new LayoutAttributesFactoryImpl());
        }

        return singleton;
    }

    /**
     * The factory to use to create attributes classes.
     */
    private final LayoutAttributesFactory factory;

    /**
     * Initialise.
     *
     * @param factory The factory to use to construct any attributes classes
     * used internally.
     */
    public DeviceLayoutRenderer(LayoutAttributesFactory factory) {
        this.factory = factory;
    }

    /**
     * Render the layout.
     * @param context The layout to render.
     */
    public void renderLayout(DeviceLayoutContext context,
                             FormatRendererContext formatRendererContext)
            throws IOException, RendererException {

        NDimensionalIndex index = NDimensionalIndex.ZERO_DIMENSIONS;

        LayoutModule module = formatRendererContext.getLayoutModule();

        // Save the current DeviceLayoutContext to restore later.

        // Set the current device layout context.
        formatRendererContext.pushDeviceLayoutContext(context);

        RuntimeDeviceLayout deviceLayout = formatRendererContext.getDeviceLayout();

        LayoutAttributes attributes = factory.createLayoutAttributes();
        attributes.setDeviceLayoutContext(context);

        // Get the style sheet associated with the layout and push it into the
        // styling engine.
        RuntimeDeviceLayout runtimeDeviceLayout = context.getDeviceLayout();
        CompiledStyleSheet layoutStyleSheet =
                runtimeDeviceLayout.getCompiledStyleSheet();

        // Set up the styling engine with the applicable style sheets.
        FormatStylingEngine formatStylingEngine =
                formatRendererContext.getFormatStylingEngine();
        // First add the layout style sheet into the styling engine.
        formatStylingEngine.pushStyleSheet(layoutStyleSheet);
        // Then add the theme style sheet(s) into the styling engine.
        CompiledStyleSheetCollection themeStyleSheets =
                context.getThemeStyleSheets();
        if (themeStyleSheets != null) {
            themeStyleSheets.pushAll(formatStylingEngine);
        }
        // else, allow tests to run without doing the above. Dodgy.

        // Process a nested inclusion, this is done after the page context has been
        // updated in order to make sure that the nesting depth is correct.
        boolean inclusion = (context.getIncludingDeviceLayoutContext() != null);

        if (inclusion) {
            module.beginNestedInclusion();
        }

        module.writeOpenLayout(attributes);

        Fragment fragment = formatRendererContext.getCurrentFragment();
        if (fragment == null) {
            if (logger.isDebugEnabled()) {
                String name =
                        deviceLayout != null ? deviceLayout.getName():null;
                logger.debug("Writing out the Layout named "
                             + name + " to the page");

                // Write the format tree

            }
            Format root = deviceLayout.getRootFormat();
            if (root != null) {
                FormatInstance rootInstance =
                        formatRendererContext.getFormatInstance(
                                root, index);
                formatRendererContext.renderFormat(rootInstance);
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("Empty layout");
                }
            }

        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Writing out the fragment named "
                             + fragment.getName() + " to the page");
            }
            // Write out the fragment and the fragment link list which contains
            // links to the fragment's parent and peer fragments.
            if (!module.getSupportsFragmentLinkListTargetting()) {
                // Write out parent/peer fragment link list *after* the fragment.
                // This is necessary as the fragment link list is being appended to
                // the existing content and must therefore come after it.

                // First, write out the fragment itself
                FormatInstance fragmentInstance =
                        formatRendererContext.getFormatInstance(
                                fragment, index);
                formatRendererContext.renderFormat(fragmentInstance);

                // Then, write out the parent/peer fragment link list.
                writeFragmentLinkList(fragment, formatRendererContext);
            } else {
                // Write out parent/peer fragment link list *before* the fragment.
                // This is necessary as the fragment link list is going into it's
                // own pane which would be empty if we tried to render it before
                // creating them.

                // First, write out parent/peer fragment link list.
                writeFragmentLinkList(fragment, formatRendererContext);
                // Then, write out the fragment itself.
                FormatInstance fragmentInstance =
                        formatRendererContext.getFormatInstance(
                                fragment, index);

                formatRendererContext.renderFormat(fragmentInstance);
            }

        }

        // Write the link to the default segment if necessary.
        SegmentLinkWriter segmentLinkWriter =
                formatRendererContext.getSegmentLinkWriter();
        segmentLinkWriter.writeDefaultSegmentLink();

        module.writeCloseLayout(attributes);

        // Finish processing the nested inclusion.
        if (inclusion) {
            module.endNestedInclusion();
        }

        // Clean up the styling information we added to the styling engine.
        // This should be done in reverse order.
        // First we remove the theme style sheet(s).
        if (themeStyleSheets != null) {
            themeStyleSheets.popAll(formatStylingEngine);
        }
        // else, allow tests to run without the above. Dodgy.

        // Then we remove the layout style sheet.
        formatStylingEngine.popStyleSheet(layoutStyleSheet);

        // Restore the old DeviceLayoutContext.
        formatRendererContext.popDeviceLayoutContext();
    }

    /**
     * Write out the parent and peer links that form a fragment link list.
     *
     * @param fragment the fragment being rendering.
     * @param context
     * @throws IOException
     * @throws RendererException
     */
    private void writeFragmentLinkList(Fragment fragment,
                                       FormatRendererContext context)
            throws IOException, RendererException {

        Fragment enclosingFragment = fragment.getEnclosingFragment();
        if (logger.isDebugEnabled()) {
            logger.debug("Fragment is " + fragment);
            logger.debug("Enclosing fragment is " + enclosingFragment);
        }

        FragmentLinkWriter fragmentLinkWriter =
                context.getFragmentLinkWriter();

        if (enclosingFragment != null) {
            if (fragment.isParentLinkFirst()) {
                fragmentLinkWriter.writeFragmentLink(
                        context, fragment, enclosingFragment, true, true);
                writePeerFragmentLinks(context, fragment);
            } else {
                writePeerFragmentLinks(context, fragment);
                fragmentLinkWriter.writeFragmentLink(
                        context, fragment, enclosingFragment, true, true);
            }
        }
    }

    private void writePeerFragmentLinks(FormatRendererContext context,
                                          Fragment fragment)
            throws IOException, RendererException {

        // Only write the links if we have been asked to.
        if (!fragment.getPeerLinks()) {
            return;
        }

        FragmentLinkWriter fragmentLinkWriter =
                context.getFragmentLinkWriter();

        // Walk through the children of the parent (our peers)
        Format parent = fragment.getParent();
        if (parent != null) {
            int numChildren = parent.getNumChildren();
            if (logger.isDebugEnabled()) {
                logger.debug("Searching " + numChildren + " peers");
            }
            for (int child = 0; child < numChildren; child++) {
                // Check if the peer is a fragment
                Format peer = parent.getChildAt(child);
                if (logger.isDebugEnabled()) {
                    logger.debug("Found peer " + peer);
                }
                if ((peer != null)
                        && peer.getFormatType().equals(FormatType.FRAGMENT)) {
                    Fragment peerFragment = (Fragment) peer;
                    // Dont write to ourselves
                    if (!peerFragment.getName().equals(fragment.getName())) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Adding link to peer fragment"
                                         + peerFragment.getName());
                        }
                        fragmentLinkWriter.writeFragmentLink(
                                context, fragment, peerFragment, true, false);
                    }
                }
            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10527/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 05-Dec-05	10512/1	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 25-Nov-05	10394/1	ibush	VBM:2005111812 interim commit for Willobs

 15-Nov-05	10278/3	ianw	VBM:2005110425 Fixed up formating/comments

 14-Nov-05	10278/1	ianw	VBM:2005110425 Fix the releasing of DeviceLayoutContext's

 28-Nov-05	10467/1	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 25-Nov-05	10394/1	ibush	VBM:2005111812 interim commit for Willobs

 15-Nov-05	10326/7	ianw	VBM:2005110425 Fixed up formating/comments

 15-Nov-05	10326/1	ianw	VBM:2005110425 Fixed stacking issue with device layouts in DeviceLayoutRenderer

 14-Nov-05	10278/1	ianw	VBM:2005110425 Fix the releasing of DeviceLayoutContext's

 15-Nov-05	10278/3	ianw	VBM:2005110425 Fixed up formating/comments

 14-Nov-05	10278/1	ianw	VBM:2005110425 Fix the releasing of DeviceLayoutContext's

 22-Aug-05	9298/1	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 18-Aug-05	9007/1	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 15-Jul-05	9073/1	pduffin	VBM:2005071420 Created runtime device layout that only exposes the parts of DeviceLayout that are needed at runtime.

 11-Jul-05	8862/2	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 06-Jan-05	6391/7	adrianj	VBM:2004120207 Refactored DeviceLayoutRenderer into separate renderer classes

 06-Jan-05	6391/5	adrianj	VBM:2004120207 Refactored DeviceLayoutRenderer into separate renderer classes

 10-Dec-04	6391/3	adrianj	VBM:2004120207 Refactoring DeviceLayoutRenderer

 10-Dec-04	6391/1	adrianj	VBM:2004120207 Refactoring DeviceLayoutRenderer

 09-Dec-04	6417/1	philws	VBM:2004120703 Committing tidy up

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6232/7	doug	VBM:2004111702 refactored logging framework

 29-Nov-04	6332/1	doug	VBM:2004112913 Refactored logging framework

 29-Nov-04	6232/5	doug	VBM:2004111702 Refactored Logging framework

 05-Nov-04	6112/4	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 05-Nov-04	6112/2	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 01-Nov-04	6068/1	tom	VBM:2004101508 renamed VolantisAttribute to MCSAttributes and added style properties container

 19-Oct-04	5816/1	byron	VBM:2004101318 Support style classes: Runtime XHTMLBasic

 14-Oct-04	5808/1	byron	VBM:2004101317 Support style classes: Runtime DOMProtocol/DeviceLayoutRenderer

 23-Sep-04	5599/1	geoff	VBM:2004092214 Port VDXML to MCS: port existing protocol code

 02-Jul-04	4713/13	geoff	VBM:2004061004 Support iterated Regions (review comments)

 29-Jun-04	4713/10	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 21-Jun-04	4713/6	geoff	VBM:2004061004 Support iterated Regions (commit prototype for safety)

 28-Jun-04	4733/1	allan	VBM:2004062105 Convert Volantis to use the new PageURLRewriter

 16-Jun-04	4704/7	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name. (use generic name for current format index)

 14-Jun-04	4704/5	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name. (rework issues)

 14-Jun-04	4704/3	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 14-Jun-04	4698/1	geoff	VBM:2003061912 RegionContent should not store a MarinerPageContext

 13-May-04	4315/1	geoff	VBM:2004051204 Enhance Menu Support: WML Dissection: Integration

 23-Mar-04	3555/1	allan	VBM:2004032205 Patch performance fixes from MCS 3.0GA

 23-Mar-04	3512/3	allan	VBM:2004032205 MCS performance enhancements.

 22-Mar-04	3512/1	allan	VBM:2004032205 MCS performance enhancements.

 19-Feb-04	2789/4	tony	VBM:2004012601 refactored localised logging to synergetics

 12-Feb-04	2789/2	tony	VBM:2004012601 Localised logging (and exceptions)

 11-Feb-04	2761/1	mat	VBM:2004011910 Add Project repository

 17-Nov-03	1888/2	mat	VBM:2003110512 Only check value of rows for an empty iterator

 17-Nov-03	1891/3	mat	VBM:2003110512 Still write the FIR to the context, even if iterator was skipped

 14-Nov-03	1891/1	mat	VBM:2003110512 Prevent rendering of empty spatial iterators

 23-Sep-03	1412/5	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links (rework to get link style from source fragment)

 17-Sep-03	1412/3	geoff	VBM:2003091105 Modify WML Openwave protocols to render fragment links as numeric style links

 14-Aug-03	1083/1	geoff	VBM:2003081305 Port 2003060909 forward

 14-Aug-03	1063/1	geoff	VBM:2003081305 Port 2003060909 forward

 24-Jun-03	450/1	steve	VBM:2003060909 Style classes on Spatial Iterator panes

 01-Jul-03	677/1	doug	VBM:2003032706 Fixed problem if fragments

 30-Jun-03	631/1	sumit	VBM:2003032706 Fixed issue with default fragment missing on layouts preventing rendering

 19-Jun-03	407/2	steve	VBM:2002121215 Flow elements and PCData in regions

 17-Jun-03	427/1	mat	VBM:2003061607 Add Menu Horizontal Separator theme

 13-Jun-03	339/1	steve	VBM:2003051905 Horizontal Menu Separator Character

 ===========================================================================
*/
