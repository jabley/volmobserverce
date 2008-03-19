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
 * $Header: /src/voyager/com/volantis/mcs/protocols/DeviceLayoutContext.java,v 1.20 2003/04/17 10:21:07 geoff Exp $
 * ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2001. 
 * ----------------------------------------------------------------------------
 * Change History:
 *
 * Date         Who             Description
 * ---------    --------------- -----------------------------------------------
 * 02-Nov-01    Paul            VBM:2001102403 - Created.
 * 19-Nov-01    Paul            VBM:2001110202 - Added the inclusionPath
 *                              property.
 * 19-Dec-01    Doug            VBM:2001121701 - Added a new Map property to
 *                              store away references to the FragmentInstance
 *                              objects that represent the fragments in the
 *                              current layout. Added a getFragmentMap method
 *                              to return the Map. Modified the
 *                              FormatContextVisitor.visit (Fragment, Object)
 *                              method to populate the map.
 * 21-Dec-01    Paul            VBM:2001121702 - Removed some unused code.
 * 25-Jan-02    Steve       VBM:2002011412 - Allow creation of replica
 *                              formats
 * 30-Jan-02    Mat             VBM:2002011410 - Added pre and postamble
 *                              buffers
 *                              to hold information to hold open and close
 *                              information for included pages.
 * 13-Feb-02    Paul            VBM:2002021203 - Fixed problem with the
 *                              comments on the set/getInclusionPath methods.
 * 14-Feb-02    Steve           VBM:2001101803 - Allows visiting of
 *                              FormFragments and maintains the current form
 *                              fragment.
 * 20-Feb-02    Steve           VBM:2002021404 - Added methods to retrieve a
 *                              named FormFragment from the papi so that
 *                              link text can be changed at run-time.
 * 28-Feb-02    Paul            VBM:2002022804 - Made the preamble and
 *                              postamble buffers general OutputBuffers and
 *                              added support for allocating and freeing them.
 * 08-Mar-02    Paul            VBM:2002030607 - Added ability to get arbitrary
 *                              output buffers associated with the device
 *                              layout.
 * 18-Mar-02    Ian             VBM:2002031203 Changed log4j Category from
 *                              class to string.
 * 22-Feb-02    Paul            VBM:2002021802 - Added a reference to the
 *                              including device layout context and a buffer
 *                              which contains the contents of the layout.
 * 19-Jun-02    Adrian          VBM:2002053104 - Added methods to get and set
 *                              short inclusion path.
 * 06-Aug-02    Paul            VBM:2002073008 - Added overlay and inRegion
 *                              attributes.
 * 02-Sep-02    Sumit           VBM:2002030703 New HashMap added to keep
 *                              track of PaneContexts
 * 28-Oct-02    Chris W         VBM:2002101801 - Added visit method for
 *                              SpatialFormatIterators and TemporalFormatIterators
 *                              to FormatContextVisitor inner class.
 * 01-Nov-02    Phil W-S        VBM:2002100906 - Adding in the layout device
 *                              theme management. Changes setDeviceLayout and
 *                              initialise. Adds initializeLayoutDeviceTheme,
 *                              getLayoutDeviceTheme and the layoutDeviceTheme
 *                              member.
 * 03-Nov-02    Sumit           VBM:2002111105 - Added a NDimensionalIndex
 *                              member and get/set for use during rendering.
 * 08-Nov-02    Phil W-S        VBM:2002102306 - Add in storage of the device
 *                              theme being used by the canvas associated
 *                              with the device layout. This is required to
 *                              ensure that the "device theme stack" is
 *                              available during protocol execution to allow
 *                              mariner element thematic optimization.
 * 16-Apr-03    Geoff           VBM:2003041603 - Add declaration for
 *                              FormatVisitorException where necessary, catch
 *                              FormatVisitorException and rethrow
 *                              RuntimeWrappingException in initialise().
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.layouts.ColumnIteratorPane;
import com.volantis.mcs.layouts.DissectingPane;
import com.volantis.mcs.layouts.Form;
import com.volantis.mcs.layouts.FormFragment;
import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.FormatNamespace;
import com.volantis.mcs.layouts.FormatScope;
import com.volantis.mcs.layouts.FormatVisitorAdapter;
import com.volantis.mcs.layouts.FormatVisitorException;
import com.volantis.mcs.layouts.Fragment;
import com.volantis.mcs.layouts.Grid;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.layouts.Region;
import com.volantis.mcs.layouts.Replica;
import com.volantis.mcs.layouts.RowIteratorPane;
import com.volantis.mcs.layouts.Segment;
import com.volantis.mcs.layouts.SegmentGrid;
import com.volantis.mcs.layouts.SpatialFormatIterator;
import com.volantis.mcs.layouts.TemporalFormatIterator;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.layouts.ColumnIteratorPaneInstance;
import com.volantis.mcs.protocols.layouts.DissectingPaneInstance;
import com.volantis.mcs.protocols.layouts.FormFragmentInstance;
import com.volantis.mcs.protocols.layouts.FormInstance;
import com.volantis.mcs.protocols.layouts.FormatInstance;
import com.volantis.mcs.protocols.layouts.FragmentInstance;
import com.volantis.mcs.protocols.layouts.GridInstance;
import com.volantis.mcs.protocols.layouts.PaneInstance;
import com.volantis.mcs.protocols.layouts.RegionInstance;
import com.volantis.mcs.protocols.layouts.ReplicaInstance;
import com.volantis.mcs.protocols.layouts.RowIteratorPaneInstance;
import com.volantis.mcs.protocols.layouts.SegmentGridInstance;
import com.volantis.mcs.protocols.layouts.SegmentInstance;
import com.volantis.mcs.protocols.layouts.SpatialFormatIteratorInstance;
import com.volantis.mcs.protocols.layouts.TemporalFormatIteratorInstance;
import com.volantis.mcs.protocols.renderer.layouts.SegmentLinkWriter;
import com.volantis.mcs.protocols.renderer.shared.layouts.DefaultSegmentLinkWriter;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayout;
import com.volantis.mcs.runtime.styling.CompiledStyleSheetCollection;
import com.volantis.shared.throwable.ExtendedRuntimeException;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.styling.values.ImmutablePropertyValues;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains all the state associated with a Layout in a particular
 * MarinerPageContext, including the device theme associated with the layout
 * and a layout device theme that is to be generated from the layout. Note
 * that this is a pooled object class so should generally not be instantiated
 * directly.
 *
 * @mock.generate
 */
public class DeviceLayoutContext {

    /**
     * Used for logging.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(DeviceLayoutContext.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
            LocalizationFactory.createExceptionLocalizer(DeviceLayoutContext.class);

    /**
     * A FormatVisitor implementation which traverses the entire format tree
     * and pre-creates format specific FormatInstance instances for those
     * format types which require this, and collects them into maps as
     * necessary for PAPI.
     */
    private final FormatInstanceCollector formatInstanceCollector
            = new FormatInstanceCollector(this);

    /**
     * A FormatVisitor implementation which is used to lazily create
     * individual format specific FormatInstance instances as required for
     * those format types which do not require collection.
     */
    private final FormatInstanceCreator formatInstanceCreator
            = new FormatInstanceCreator(this);

    /**
     * A map to hold all the fragmentInstances for the fragments in the current
     * layout
     */
    private final Map fragmentInstances;

    /**
     * A map to hold all the fragmentInstances for the form fragments in the
     * current layout
     */
    private final Map formFragmentInstances;

    /**
     * A map to hold all the paneInstances for the panes in the
     * current layout
     */
    private final Map paneInstances;


    /**
     * OutputBuffers to hold pre- and postamble content for the layout.
     * Used in portlets or inclusions to hold the open- and closeInclusion
     * information.
     */

    private OutputBuffer preambleBuffer;
    private OutputBuffer contentBuffer;
    private OutputBuffer postambleBuffer;

    private final OutputBufferMap buffers;

    /**
     * The current fragment.
     */
    private Fragment currentFragment;

    /**
     * The current form fragment.
     */
    private FormFragment currentFormFragment;

    /**
     * The inclusion path.
     */
    private String inclusionPath;

    /**
     * The short inclusion path.
     */
    private String shortInclusionPath;

    /**
     * The layout with which this object is associated.
     */
    private RuntimeDeviceLayout deviceLayout;

    /**
     * The compiled theme style sheets for the page. This may include
     * themes from various source, eg project, link element, style element,
     * canvas element, etc, depending on what version of XDIME the page uses.
     */
    private CompiledStyleSheetCollection themeStyleSheets;

    /**
     * The FormatInstance objects which are associated with the formats within
     * the current device layout in the current context.
     * <p>
     * These format instances are indexed first by their format instance
     * numbers within the layout, and then by their format iteration
     * N dimensional index within any format iteration.
     * <p>
     * Those formats which are not within a format iterator, or do not
     * understand format iteration, will always use the index
     * {@link NDimensionalIndex#ZERO_DIMENSIONS}.
     */
    private NDimensionalContainer[] formatInstances;

    /**
     * The NDimensionalIndex of the current format. This is usually
     * {@link NDimensionalIndex#ZERO_DIMENSIONS} unless the format is inside
     * a format iterator.
     */
    private NDimensionalIndex currentFormatIndex;

    /**
     * The page context with which this object is associated.
     */
    protected MarinerPageContext pageContext;

    /**
     * This object creates protocol specific output buffer objects.
     */
    private OutputBufferFactory outputBufferFactory;

    /**
     * The DeviceLayoutContext which includes this one.
     */
    private DeviceLayoutContext includingDeviceLayoutContext;

    /**
     * Flag which indicates whether this is in a region or not.
     */
    private boolean inRegion;

    /**
     * Flag which indicates whether this is an overlay.
     */
    private boolean overlay;

    /**
     * The object responsible for writing out segment links.
     */
    private SegmentLinkWriter segmentLinkWriter;
    
    private ImmutablePropertyValues inheritableStyleValues;

    /**
     * Create a new <code>DeviceLayoutContext</code>.
     */
    public DeviceLayoutContext() {
        fragmentInstances = new HashMap();
        formFragmentInstances = new HashMap();
        paneInstances = new HashMap();
        buffers = new OutputBufferMap();
    }

    /**
     * Set the page context.
     * @param pageContext The page context.
     */
    public void setMarinerPageContext(MarinerPageContext pageContext) {
        this.pageContext = pageContext;
    }

    /**
     * Set the value of the includingDeviceLayoutContext property.
     * @param context The new value of the
     * includingDeviceLayoutContext property.
     */
    public void setIncludingDeviceLayoutContext(DeviceLayoutContext context) {
        this.includingDeviceLayoutContext = context;
    }

    /**
     * Get the value of the includingDeviceLayoutContext property.
     * @return The value of the includingDeviceLayoutContext property.
     */
    public DeviceLayoutContext getIncludingDeviceLayoutContext() {
        return includingDeviceLayoutContext;
    }

    /**
     * Set the value of the in region property.
     * @param inRegion The new value of the in region property.
     */
    public void setInRegion(boolean inRegion) {
        this.inRegion = inRegion;
    }

    /**
     * Get the value of the in region property.
     * @return The value of the in region property.
     */
    public boolean isInRegion() {
        return inRegion;
    }

    /**
     * Set the value of the overlay property.
     * @param overlay The new value of the overlay property.
     */
    public void setOverlay(boolean overlay) {
        this.overlay = overlay;
    }

    /**
     * Get the value of the overlay property.
     * @return The value of the overlay property.
     */
    public boolean isOverlay() {
        return overlay;
    }

    /**
     * Get the preamble buffer
     * @param create True if the caller needs to make sure that the buffer has
     * been created and false otherwise.
     * @return The preamble buffer
     */
    public OutputBuffer getPreambleBuffer(boolean create) {
        if (preambleBuffer == null && create) {
            preambleBuffer = buffers.getBuffer("_preamble", create);
        }

        return preambleBuffer;
    }

    /**
     * Get the postamble buffer
     * @param create True if the caller needs to make sure that the buffer has
     * been created and false otherwise.
     * @return The postamble buffer
     */
    public OutputBuffer getPostambleBuffer(boolean create) {
        if (postambleBuffer == null && create) {
            postambleBuffer = buffers.getBuffer("_postamble", create);
        }

        return postambleBuffer;
    }

    /**
     * Get the contents buffer
     * @param create True if the caller needs to make sure that the buffer has
     * been created and false otherwise.
     * @return The preamble buffer
     */
    public OutputBuffer getContentBuffer(boolean create) {
        if (contentBuffer == null && create) {
            contentBuffer = buffers.getBuffer("_contents", create);
        }

        return contentBuffer;
    }

    /**
     * Get the buffer with the specified name.
     * @param name The name of the buffer to create.
     * @param create Controls whether the buffer should be created if it does
     * not exist already.
     */
    public OutputBuffer getOutputBuffer(String name, boolean create) {
        return buffers.getBuffer(name, create);
    }

    /**
     * Get the page context.
     * @return The page context.
     */
    public MarinerPageContext getMarinerPageContext() {
        return pageContext;
    }

    /**
     * Set the current fragment.
     * @param fragment The current fragment.
     */
    public void setCurrentFragment(Fragment fragment) {
        this.currentFragment = fragment;
    }

    /**
     * Get the current fragment.
     * @return The current fragment.
     */
    public Fragment getCurrentFragment() {
        return currentFragment;
    }

    /**
     * Set the current form fragment.
     * @param fragment The current fragment.
     */
    public void setCurrentFormFragment(FormFragment fragment) {
        this.currentFormFragment = fragment;
    }

    /**
     * Get the current form fragment.
     * @return The current fragment.
     */
    public FormFragment getCurrentFormFragment() {
        return currentFormFragment;
    }

    /**
     * Set the device layout.
     * @param deviceLayout The device layout.
     */
    public void setDeviceLayout(RuntimeDeviceLayout deviceLayout) {
        this.deviceLayout = deviceLayout;
    }

    /**
     * Get the device layout.
     * @return The device layout.
     */
    public RuntimeDeviceLayout getDeviceLayout() {
        return deviceLayout;
    }

    /**
     * Set the theme style sheets associated with the device layout.
     *
     * @param themeStyleSheets the theme style sheet.
     */
    public void setThemeStyleSheets(
            CompiledStyleSheetCollection themeStyleSheets) {
        this.themeStyleSheets = themeStyleSheets;
    }

    /**
     * Return the theme style sheets associated with the device layout.
     *
     * @return the theme style sheets.
     */
    public CompiledStyleSheetCollection getThemeStyleSheets() {
        return themeStyleSheets;
    }

    /**
     * Set the inclusion path.
     * @param inclusionPath The inclusion path.
     */
    public void setInclusionPath(String inclusionPath) {
        this.inclusionPath = inclusionPath;
    }

    /**
     * Set the short version of the inclusion path.
     * @param shortInclusionPath The inclusion path.
     */
    public void setShortInclusionPath(String shortInclusionPath) {
        this.shortInclusionPath = shortInclusionPath;
    }

    /**
     * Get the inclusion path.
     *
     * @return The inclusion path.
     */
    public String getInclusionPath() {
        return inclusionPath;
    }

    /**
     * Get the short version of the inclusion path.
     * @return The inclusion path.
     */
    public String getShortInclusionPath() {
        return shortInclusionPath;
    }

    /**
     * This is called after the device layout and the context have been set
     * and allows the class to do any initialisation which depends on those
     * values.
     */
    public void initialise() {

        if (logger.isDebugEnabled()) {
            logger.debug("Initialising resources associated with " + this);
        }

        // Get the protocol from the canvas.
        VolantisProtocol protocol = pageContext.getProtocol();

        // Create an array to hold the FormatInstance objects.
        final int formatCount = deviceLayout.getFormatCount();
        initialiseFormatInstanceContainer(formatCount);

        // Get the protocol specific output buffer factory object.
        outputBufferFactory = protocol.getOutputBufferFactory();

        // Recurse through the entire layout creating the required context objects.
        // This is only required for those formats which are accessible via PAPI.
        Format root = deviceLayout.getRootFormat();
        if (root != null) {
            try {
                root.visit(formatInstanceCollector, this);
            } catch (FormatVisitorException e) {
                // We don't expect any exceptions, but just in case...
                throw new ExtendedRuntimeException(
                        exceptionLocalizer.format("unexpected-exception"),
                        e);
            }
        }

        // Initialise the buffers object.
        buffers.setOutputBufferFactory(outputBufferFactory);

        // Initialise the current pane index.
        currentFormatIndex = NDimensionalIndex.ZERO_DIMENSIONS;

        // Initialise the segment link writer.
        segmentLinkWriter = new DefaultSegmentLinkWriter(pageContext);
    }

    /**
     * Helper method to initialise the n dimensional containers we use
     * to store format instances.
     *
     * @param formatCount the number of formats the layout contains.
     */
    void initialiseFormatInstanceContainer(final int formatCount) {
        formatInstances = new NDimensionalContainer[formatCount];
        for (int i = 0; i < formatCount; i++) {
            formatInstances[i] = new NDimensionalContainer();
        }
    }

    /**
     * Sets a format's associated format instance for a particular index.
     *
     * @param format The format whose instance is being set.
     * @param index the index of the format instance which is to be set.
     * @param formatInstance The FormatInstance to associate with the specified
     * format.
     */
    private void setFormatInstance(Format format, NDimensionalIndex index,
                                   FormatInstance formatInstance) {

        getFormatInstancesContainer(format).set(index, formatInstance);
    }

    /**
     * Get the specified format, by name and namespace.
     *
     * @param name      the name of the format to retrieve
     * @param namespace the namespace of format to retreive
     * @return the format.
     */
    public Format getFormat(String name, FormatNamespace namespace) {
        Format format = null;
        // Get the format scope.
        FormatScope formatScope = null;
        // Attempt to get it from the fragment first
        final Fragment currentFragment = getCurrentFragment();
        if (currentFragment != null) {
            formatScope = currentFragment.getFormatScope();
        }

        // If it doesn't match a fragment, then try in the layout.
        if (formatScope == null) {
            formatScope = getDeviceLayout().getFormatScope();
        }
        
        if (formatScope != null) {
            format = formatScope.retrieveFormat(name, namespace);
        }

        return format;
    }

    /**
     * Gets the format instance for a particular format and index.
     * <p>
     * This represents the format "instance" at this index. For example,
     * if the format is a pane and it is contained within a single spatial
     * iterator, then requesting such an object for the pane with an index of
     * [2] would return the third "instance" of the pane within the spatial
     * iterator.
     *
     * @param format The object whose FormatInstance is required.
     * @param index the index of the format instance which is required.
     * @return The FormatInstance which was allocated for the specified format.
     */
    public FormatInstance getFormatInstance(Format format,
                                            NDimensionalIndex index) {

        if (format == null) {
            throw new IllegalArgumentException("Format cannot be null");
        }

        final NDimensionalContainer formatContextContainer =
                getFormatInstancesContainer(format);
        FormatInstance formatInstance =
                (FormatInstance) formatContextContainer.get(index);
        if (formatInstance == null) {
            // Create the context using the format instance visitor.
            try {
                formatInstanceCreator.setIndex(index);
                format.visit(formatInstanceCreator, this);
            } catch (FormatVisitorException e) {
                // We don't expect any exceptions, but just in case...
                throw new ExtendedRuntimeException(
                        exceptionLocalizer.format("unexpected-exception"),
                        e);
            }
            formatInstance = (FormatInstance) formatContextContainer.get(index);
        }
        return formatInstance;
    }

    /**
     * Gets the container for format instances for a particular format within
     * this layout.
     *
     * @param format the format to return the container for.
     * @return the container for format instances.
     */
    public NDimensionalContainer getFormatInstancesContainer(Format format) {

        int instanceIndex = format.getInstance();

        return formatInstances[instanceIndex];
    }

    /**
     * Check whether the format is empty in the current instance.
     *
     * @param format The format to check.
     * @return True if the format is empty and false otherwise.
     */
    public boolean isFormatEmpty(Format format) {

        if (format == null) {
            return true;
        }

        FormatInstance instance = getFormatInstance(format, currentFormatIndex);

        return instance.isEmpty();
    }

    /**
     * Returns the current format index.
     * <p>
     * This method is used within the device layout renderer to control the
     * current format instance instance during format iteration.
     *
     * @return NDimensionalIndex
     */
    public NDimensionalIndex getCurrentFormatIndex() {

        return currentFormatIndex;
    }

    /**
     * Sets the current format index.
     * <p>
     * This method is used within the device layout renderer to control the
     * current format instance instance during format iteration.
     *
     * @param currentFormatIndex The index to set
     */
    public void setCurrentFormatIndex(NDimensionalIndex currentFormatIndex) {

        this.currentFormatIndex = currentFormatIndex;
    }

    /**
     * Gets the format instance at the current format index for this format.
     * <p>
     * This method is the primary interface to allow the protocols to find
     * the current format instance for a format during layout rendering. The
     * device layout renderer must ensure that this is set appropriately
     * before delegating to the protocols.
     *
     * @param format the format to get the context for.
     * @return the current format instance.
     */
    public FormatInstance getCurrentFormatInstance(Format format) {

        return getFormatInstance(format, currentFormatIndex);
    }

    /**
     * Get the map that contains the FragmentInstance objects that represent all
     * the fragments in the current layout.
     * @return a map
     */
    public Map getFragmentInstancesMap() {
        return fragmentInstances;
    }

    /**
     * Get the map that contains the FormFragmentInstance objects that represent all
     * the form fragments in the current layout.
     * @return a map
     */
    public Map getFormFragmentInstancesMap() {
        return formFragmentInstances;
    }

    /**
     * Get the map that contains the PaneInstance objects that represent all
     * the panes in the current layout.
     * @return a map
     */
    public Map getPaneInstancesMap() {
        return paneInstances;
    }

    /**
     * Allocate an output buffer.
     * @return The newly allocated output buffer.
     */
    public OutputBuffer allocateOutputBuffer() {
        return outputBufferFactory.createOutputBuffer();
    }

    /**
     * Get the segment link writer.
     *
     * @return The segment link writer.
     */
    public SegmentLinkWriter getSegmentLinkWriter() {
        return segmentLinkWriter;
    }

    public void setInheritableStyleValues(
            ImmutablePropertyValues inheritableStyleValues) {
        this.inheritableStyleValues = inheritableStyleValues;
    }

    public ImmutablePropertyValues getInheritableStyleValues() {
        return inheritableStyleValues;
    }

    /**
     * This visits a single node in the format tree, creating a format instance
     * for that format at the index supplied.
     */
    private final class FormatInstanceCreator
            extends FormatVisitorAdapter {

        /**
         * The context we are creating into.
         */
        private final DeviceLayoutContext context;

        /**
         * The index we are creating for.
         */
        private NDimensionalIndex index;

        /**
         * Initialise.
         *
         * @param context
         */
        public FormatInstanceCreator(DeviceLayoutContext context) {

            this.context = context;
        }

        /**
         * @see #index
         */
        public void setIndex(NDimensionalIndex index) {

            this.index = index;
        }

        /**
         * Initialise the format instance which has just been created.
         *
         * @param formatInstance the instance to be initialised.
         * @param format the related format.
         */
        private void initialiseFormatInstance(FormatInstance formatInstance,
                                              Format format) {

            // Create "back" link from format instance to layout context.
            formatInstance.setDeviceLayoutContext(context);
            // Create "back" link from format instance to related format.
            formatInstance.setFormat(format);
            // Initialise the context.
            formatInstance.initialise();
            // Store the format instance into the context at the correct index.
            context.setFormatInstance(format, index, formatInstance);
        }

        // Javadoc inherited from super class.
        public boolean visit(ColumnIteratorPane format, Object object) {

            FormatInstance formatInstance = new ColumnIteratorPaneInstance(index);
            initialiseFormatInstance(formatInstance, format);
            // Only visit the topmost format.
            return true;
        }

        // Javadoc inherited from super class.
        public boolean visit(DissectingPane format, Object object) {

            FormatInstance formatInstance = new DissectingPaneInstance(index);
            initialiseFormatInstance(formatInstance, format);

            // Initialise the context with appropriate values from it's format.
            DissectingPaneInstance dissectingInstance =
                    (DissectingPaneInstance) formatInstance;
            dissectingInstance.setLinkToText(
                    format.getNextShardLinkText());
            dissectingInstance.setLinkFromText(
                    format.getPreviousShardLinkText());

            // Only visit the topmost format.
            return true;
        }

        // Javadoc inherited from super class.
        public boolean visit(Form format, Object object)
                throws FormatVisitorException {

            initialiseFormatInstance(new FormInstance(index), format);
            // Only visit the topmost format.
            return true;
        }

        // Javadoc inherited from super class.
        public boolean visit(Fragment format, Object object)
                throws FormatVisitorException {

            FormatInstance formatInstance = new FragmentInstance(index);
            initialiseFormatInstance(formatInstance, format);
            // Only visit the topmost format.
            return true;
        }

        // Javadoc inherited from super class.
        public boolean visit(FormFragment format, Object object)
                throws FormatVisitorException {

            FormatInstance formatInstance = new FormFragmentInstance(index);
            initialiseFormatInstance(formatInstance, format);
            FormFragmentInstance formFragmentInstance = 
                                      (FormFragmentInstance) formatInstance;
            formFragmentInstance.setNextLinkText(format.getNextLinkText());
            formFragmentInstance.setPreviousLinkText(format.
                                                     getPreviousLinkText());
            
            // Only visit the topmost format.
            return true;
        }

        // Javadoc inherited from super class.
        public boolean visit(Grid format, Object object)
                throws FormatVisitorException {

            initialiseFormatInstance(new GridInstance(index), format);
            // Only visit the topmost format.
            return true;
        }

        // Javadoc inherited from super class.
        public boolean visit(Pane format, Object object) {

            initialiseFormatInstance(new PaneInstance(index), format);
            // Only visit the topmost format.
            return true;
        }

        // Javadoc inherited from super class.
        public boolean visit(Replica format, Object object)
                throws FormatVisitorException {

            initialiseFormatInstance(new ReplicaInstance(index), format);
            // Only visit the topmost format.
            return true;
        }

        // Javadoc inherited from super class.
        public boolean visit(Region format, Object object) {

            initialiseFormatInstance(new RegionInstance(index), format);
            // Only visit the topmost format.
            return true;
        }

        // Javadoc inherited from super class.
        public boolean visit(RowIteratorPane format, Object object) {

            initialiseFormatInstance(new RowIteratorPaneInstance(index), format);
            // Only visit the topmost format.
            return true;
        }

        // Javadoc inherited from super class.
        public boolean visit(Segment format, Object object) {

            initialiseFormatInstance(new SegmentInstance(index), format);
            // Only visit the topmost format.
            return true;
        }

        // Javadoc inherited from super class.
        public boolean visit(SegmentGrid format, Object object)
                throws FormatVisitorException {

            initialiseFormatInstance(new SegmentGridInstance(index), format);
            // Only visit the topmost format.
            return true;
        }

        // Javadoc inherited from super class.
        public boolean visit(SpatialFormatIterator spatial, Object object)
                throws FormatVisitorException {

            initialiseFormatInstance(new SpatialFormatIteratorInstance(
                    index), spatial);
            // Only visit the topmost format.
            return true;
        }

        // Javadoc inherited from super class.
        public boolean visit(TemporalFormatIterator temporal, Object object)
                throws FormatVisitorException {

            initialiseFormatInstance(new TemporalFormatIteratorInstance(
                    index), temporal);
            // Only visit the topmost format.
            return true;
        }

    }

    /**
     * This visits the format tree, pre-creating and collecting the zero
     * dimension format instances for those formats which are accessible
     * directly via PAPI - i.e Panes, Fragments and FormFragments (but not
     * Dissecting Panes).
     * <p>
     * These format instances are then used to create the PAPI Facades for
     * the formats before being given back to the user.
     * <p>
     * This is required because the all the PAPI accessible formats are
     * required to be available as soon as the canvas is created, rather than
     * as they are individually referenced in the XDIME/PAPI, as would be the
     * case otherwise.
     * <p>
     * NOTE: This is rather nasty, for the following reasons:
     * <ul>
     *   <li>The original idea of being able to access formats via PAPI is
     *      dubious.
     *   <li>The PAPI accessible Formats are given Facade classes in the PAPI
     *      package with inconsistent names (some of which duplicate the
     *      related format names).
     *   <li>Since the PAPI formats were added, we have added format iterators
     *      which confuses the issue. For example, it is impossible to provide
     *      access to all the format instances in an iterated format as we do
     *      not know how many there will be.
     *   <li>It relies on the fact that we can use the zero dimension format
     *      context instance to represent all the format instance instances.
     *      This only works for Panes because the context is not used and the
     *      values are stored directly into the pane (which is itself a bug),
     *      and for Fragments and FormFragments because these are currently
     *      not iterated. The real fix for this is to invent an object which
     *      stores the state which is common to all format instances.
     *   <li>Support for Dissecting Panes is implemented differently, and in
     *      a more standard fashion, for some reason(s) which are now lost in
     *      the mists of time. It relies on the normal FormatScopes for
     *      looking up the pane and pane context.
     * </ul>
     *
     * @see com.volantis.mcs.papi.PaneFormat
     * @see com.volantis.mcs.papi.Fragment
     * @see com.volantis.mcs.papi.FormFragment
     * @see com.volantis.mcs.papi.DissectingPane
     * @see com.volantis.mcs.context.MarinerRequestContext#getPaneFormat
     * @see com.volantis.mcs.context.MarinerRequestContext#getPaneFormats
     * @see com.volantis.mcs.context.MarinerRequestContext#getFragment
     * @see com.volantis.mcs.context.MarinerRequestContext#getFragments
     * @see com.volantis.mcs.context.MarinerRequestContext#getFormFragment
     * @see com.volantis.mcs.context.MarinerRequestContext#getDissectingPane
     */
    private final class FormatInstanceCollector
            extends FormatVisitorAdapter {

        /**
         * The context we are collecting into.
         */
        private final DeviceLayoutContext context;

        /**
         * Initialise.
         *
         * @param context
         */
        public FormatInstanceCollector(DeviceLayoutContext context) {
            this.context = context;
        }

        // Javadoc inherited from super class.
        public boolean visit(Fragment format, Object object)
                throws FormatVisitorException {

            FormatInstance formatInstance = getFormatInstance(format,
                    NDimensionalIndex.ZERO_DIMENSIONS);
            if (logger.isDebugEnabled()) {
                logger.debug("Collected fragment " + format + " : " +
                        formatInstance);
            }

            context.fragmentInstances.put(format.getName(), formatInstance);

            // Handle the fragment's children
            return visitFormatChildren(format, object);
        }

        // Javadoc inherited from super class.
        public boolean visit(FormFragment format, Object object)
                throws FormatVisitorException {

            FormatInstance formatInstance = getFormatInstance(format,
                    NDimensionalIndex.ZERO_DIMENSIONS);
            if (logger.isDebugEnabled()) {
                logger.debug("Collected form fragment " + format + " : " +
                        formatInstance);
            }

            context.formFragmentInstances.put(format.getName(), formatInstance);

            // Handle the form fragment's children
            return visitFormatChildren(format, object);
        }

        // Javadoc inherited from super class.
        public boolean visit(Pane format, Object object) {

            FormatInstance formatInstance = getFormatInstance(format,
                    NDimensionalIndex.ZERO_DIMENSIONS);
            if (logger.isDebugEnabled()) {
                logger.debug("Collected pane " + format + " : " +
                        formatInstance);
            }

            context.paneInstances.put(format.getName(), formatInstance);

            // Continue on visiting the tree (a pane has no children).
            return false;
        }
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 28-Nov-05	10467/1	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 25-Nov-05	10394/1	ibush	VBM:2005111812 interim commit for Willobs

 22-Aug-05	9298/4	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 18-Aug-05	9007/3	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 04-Aug-05	9151/1	pduffin	VBM:2005080205 Removing a lot of unnecessary styling code

 15-Jul-05	9073/1	pduffin	VBM:2005071420 Created runtime device layout that only exposes the parts of DeviceLayout that are needed at runtime.

 11-Jul-05	8862/3	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 01-Jul-05	8927/1	rgreenall	VBM:2005052611 Merge from 331: Fixed SpatialIteratorFormatInstance#isEmptyImpl

 29-Jun-05	8734/1	rgreenall	VBM:2005052611 Fixed SpatialIteratorFormatInstance#isEmptyImpl

 09-May-05	8132/1	philws	VBM:2005050510 Port format instance collection bug fix from 3.3

 09-May-05	8128/1	philws	VBM:2005050510 Ensure that format instances are collected for the entire layout format tree

 03-Mar-05	7277/1	philws	VBM:2005011906 Port pane styling fix from MCS 3.3

 03-Mar-05	7273/1	philws	VBM:2005011906 Ensure panes are thematically styled as per the requesting XDIME style class specifications

 10-Dec-04	6391/2	adrianj	VBM:2004120207 Refactoring DeviceLayoutRenderer

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/3	doug	VBM:2004111702 Refactored Logging framework

 29-Nov-04	6320/1	geoff	VBM:2004112604 xfaction with captionPane= throws a NullPointerException

 26-Nov-04	6298/1	geoff	VBM:2004112405 MCS NullPointerException in wml code path

 05-Nov-04	6112/4	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 05-Nov-04	6112/2	byron	VBM:2004062909 Rename FormatContext et al to something more appropriate given recent changes.

 02-Nov-04	5882/1	ianw	VBM:2004102008 Split Code generators and move NDimensionalIndex for new build

 30-Sep-04	5705/1	geoff	VBM:2004093002 IllegalArgumentException trying to use dissection (shard link text is null)

 29-Jun-04	4713/9	geoff	VBM:2004061004 Support iterated Regions (make format contexts per format instance)

 16-Jun-04	4704/4	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name. (use generic name for current format index)

 14-Jun-04	4704/2	geoff	VBM:2004061404 Rename FormatInstanceRefence to a sensible name.

 12-Feb-04	2789/1	tony	VBM:2004012601 Localised logging (and exceptions)

 05-Dec-03	2075/1	mat	VBM:2003120106 Rename Device and add a public Device Interface

 22-Oct-03	1623/1	steve	VBM:2003102006 Undo 2003090501

 22-Oct-03	1620/1	steve	VBM:2003102006 Backout changes for 2003090501

 22-Oct-03	1616/1	steve	VBM:2003102006 Undo 2003090501

 29-Sep-03	1473/1	steve	VBM:2003092403 Patch styleclass fixes from 2003090501

 ===========================================================================
*/
