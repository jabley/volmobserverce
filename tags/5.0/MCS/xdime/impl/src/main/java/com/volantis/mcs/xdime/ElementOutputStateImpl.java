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
package com.volantis.mcs.xdime;

import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MetaData;
import com.volantis.mcs.layouts.Fragment;
import com.volantis.mcs.layouts.LayoutException;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.protocols.DeviceLayoutContext;
import com.volantis.mcs.protocols.DeviceLayoutRegionContent;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.RegionContent;
import com.volantis.mcs.protocols.layouts.ContainerInstance;
import com.volantis.mcs.protocols.layouts.FragmentInstance;
import com.volantis.mcs.protocols.layouts.RegionInstance;
import com.volantis.mcs.runtime.layouts.LayoutContentActivator;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayout;
import com.volantis.mcs.xdime.xhtml2.meta.property.MetaPropertyHandlerFactory;

import java.util.Iterator;
import java.util.List;

/**
 * Implementation of the {@link ElementOutputState} interface.
 */
public class ElementOutputStateImpl implements ElementOutputState {

    /**
     * This state explicitly specifies the container in which any output markup
     * generated (for the element to which this state applies) should appear.
     * <p/>
     * If this container is null, the element should not generate any output
     * markup. If it is not null, then the container must be pushed/popped on
     * to the container instance stack in the page context when applying and
     * reverting this state.
     */
    private ContainerInstance containerInstance;

    /**
     * This describes the layout that was specified by the element to which
     * this state applies.
     * <p/>
     * If it is null, then the element did not specify a new layout. If it is
     * not null, it must be tracked by wrapping it in a {@link
     * DeviceLayoutContext} and adding it to the containing region instance
     * when reverting this state.
     */
    private DeviceLayoutContext layoutContext;

    /**
     * The region in which the element whose state is described by this class
     * appeared. This may be null if the element does not specify a new layout.
     */
    private RegionInstance containingRegionInstance;

    /**
     * Context in which this state is being used.
     */
    private XDIMEContextInternal context;

    /**
     * Determines whether or not the element to which this output state applies
     * should be processed, skipped or suppressed based solely on the specified
     * format information.
     */
    private FormattingResult formattingResult;

    /**
     * Indicates whether or not the element to which this output state applies
     * either is, or is contained by, an inactive group.
     */
    private boolean isInactiveGroup;

    /**
     * Unique identifier of the element that is currently being processed
     * (whose output state the built object will describe).
     */
    private String elementID;

    /**
     * Name of the container which was specified. May be null if the container
     * was either not explicitly specified, or was specified using a
     * {@link com.volantis.mcs.runtime.layouts.StyleFormatReference}.
     * <p/>
     * This is used if the container instance is null because it may refer to a
     * fragment instance. This cannot be checked up front because it must wait
     * until the pageContext has been updated with the specified
     * {@link DeviceLayoutContext}.
     */
    private String containerName;

    private boolean inSuppressedLayout;

    /**
     * Indicates if also the descentants of the current element should be suppressed.
     */
    private boolean isSuppressingDescendants;

    /**
     * Create an element state which does not target a particular container.
     *
     * @param context           in which this state is being used
     * @param formattingResult  indicates whether or not the element to which
     *                          this state applies should be processed, skipped
     *                          or suppressed
     * @param inactiveGroup     true if the element currently being processed
     *                          either is, or is contained by, an inactive
     *                          group, false otherwise
     * @param elementID         uniquely identifies (in the document) the
     *                          element for which an output state is being
 *                              constructed
     */
    public ElementOutputStateImpl(XDIMEContextInternal context,
                                  FormattingResult formattingResult,
                                  boolean inactiveGroup,
                                  String elementID) {
        this(null, null, null, formattingResult,
                inactiveGroup, elementID, context, null, false, false);
    }

    /**
     * @param containerInstance container in which any output markup generated
     *                          (for the element to which this state applies)
     *                          should appear
     * @param layoutContext     layout specified for the element to which this
     *                          state applies
     * @param containingRegion  region in which the element whose state is
     *                          described by this class appeared
     * @param formattingResult  indicates whether or not the element to which
     *                          this state applies should be processed, skipped
     *                          or suppressed
     * @param inactiveGroup     true if the element currently being processed
     *                          either is, or is contained by, an inactive
     *                          group, false otherwise
     * @param elementID         uniquely identifies (in the document) the
     *                          element for which an output state is being
     *                          constructed
     * @param context           in which this state is being used
     * @param containerName     name of the container that was specified (will
     *                          be null if the container was not specified by
     * @param isInSuppressedLayout
     */
    public ElementOutputStateImpl(ContainerInstance containerInstance,
                                  DeviceLayoutContext layoutContext,
                                  RegionInstance containingRegion,
                                  FormattingResult formattingResult,
                                  boolean inactiveGroup,
                                  String elementID,
                                  XDIMEContextInternal context,
                                  String containerName,
                                  boolean isInSuppressedLayout) {
        this(containerInstance, layoutContext, containingRegion, formattingResult,
                inactiveGroup, elementID, context, containerName, isInSuppressedLayout, false);
    }

    /**
     * @param containerInstance container in which any output markup generated
     *                          (for the element to which this state applies)
     *                          should appear
     * @param layoutContext     layout specified for the element to which this
     *                          state applies
     * @param containingRegion  region in which the element whose state is
     *                          described by this class appeared
     * @param formattingResult  indicates whether or not the element to which
     *                          this state applies should be processed, skipped
     *                          or suppressed
     * @param inactiveGroup     true if the element currently being processed
     *                          either is, or is contained by, an inactive
     *                          group, false otherwise
     * @param elementID         uniquely identifies (in the document) the
     *                          element for which an output state is being
     *                          constructed
     * @param context           in which this state is being used
     * @param containerName     name of the container that was specified (will
     *                          be null if the container was not specified by
     * @param isInSuppressedLayout
     * @param isSuppressingDescendants indicates if all of the elements descendants
     *                          should be suppressed as well
     */
    public ElementOutputStateImpl(ContainerInstance containerInstance,
                                  DeviceLayoutContext layoutContext,
                                  RegionInstance containingRegion,
                                  FormattingResult formattingResult,
                                  boolean inactiveGroup,
                                  String elementID,
                                  XDIMEContextInternal context,
                                  String containerName,
                                  boolean isInSuppressedLayout,
                                  boolean isSuppressingDescendants) {
        this.containerInstance = containerInstance;
        this.layoutContext = layoutContext;
        this.containingRegionInstance = containingRegion;
        this.formattingResult = formattingResult;
        isInactiveGroup = inactiveGroup;
        this.context = context;
        this.elementID = elementID;
        this.containerName = containerName;
        this.inSuppressedLayout = isInSuppressedLayout;
        this.isSuppressingDescendants = isSuppressingDescendants;
    }

    // Javadoc inherited.
    public boolean isSuppressing() {
        boolean isSuppressing = false;
        if (isInactiveGroup || formattingResult == FormattingResult.SKIP ||
                formattingResult == FormattingResult.SUPPRESS) {
            isSuppressing = true;
        }
        return isSuppressing;
    }

    // Javadoc inherited.
    public boolean isSuppressingDescendants() {
        return isSuppressingDescendants;
    }

    // Javadoc inherited.
    public boolean isInSuppressedLayout() {
        return inSuppressedLayout;
    }

    // Javadoc inherited.
    public void setIsInactiveGroup(boolean isInactiveGroup) {
        this.isInactiveGroup = isInactiveGroup;
    }

    // Javadoc inherited.
    public boolean isInactiveGroup() {
        return isInactiveGroup;
    }

    // Javadoc inherited.
    public FormattingResult apply() throws XDIMEException {

        MarinerPageContext pageContext =
                ContextInternals.getMarinerPageContext(
                        context.getInitialRequestContext());

        if (containerInstance != null) {
            pageContext.pushContainerInstance(containerInstance);
        }

        if (layoutContext != null) {
            pageContext.pushDeviceLayoutContext(layoutContext);

            // Make sure that the device layout context is updated with
            // the fragmentation state.
            try {
                pageContext.initialiseDeviceLayoutContext(
                        containingRegionInstance);
                pageContext.initialiseCurrentFragment();
            } catch (LayoutException e) {
                throw new XDIMEException(e);
            }
        }

        // If a container instance was explicitly specified using a
        // StyleString, but it didn't map to a valid container instance, then
        // check if it maps to a valid fragment instance.
        updateFragmentInstance(pageContext, containerName);

        return formattingResult;
    }

    // Javadoc inherited.
    public void revert() {

        MarinerPageContext pageContext =
                ContextInternals.getMarinerPageContext(
                        context.getInitialRequestContext());

        if (containerInstance != null) {
            // Pop the container.
            pageContext.popContainerInstance(containerInstance);
        }

        if (layoutContext != null) {

            // Pop the layout.
            pageContext.popDeviceLayoutContext();

            // If the layout being popped was included in a region then we must
            // add it to the enclosing region context (or it will be lost).
            RegionContent regionContent =
                    new DeviceLayoutRegionContent(layoutContext);
            containingRegionInstance.addRegionContent(regionContent);
        }
    }

    /**
     * Attempt to find the fragment instance which encloses the container with
     * the specified name, and if one exists, update its fragment links.
     *
     * @param pageContext   in which this fragment is being updated
     * @param containerName name of the container whose enclosing fragment to
     *                      update
     */
    private void updateFragmentInstance(MarinerPageContext pageContext,
                                        String containerName) {

        if (containerInstance == null && containerName != null &&
                elementID != null && pageContext.getCurrentFragment() != null) {

            final MetaData metaData = pageContext.getElementMetaData(elementID);

            // get the link meta data associated with this container
            OutputBuffer linkToLabel = (OutputBuffer)
                    metaData.getPropertyValue(
                            MetaPropertyHandlerFactory.FRAGMENT_LINK_LABEL);
            OutputBuffer linkFromLabel = (OutputBuffer)
                    metaData.getPropertyValue(
                            MetaPropertyHandlerFactory.ENCLOSING_FRAGMENT_LINK_LABEL);

            // If no labels have been specified using meta data then there is
            // nothing to update the fragment instance with and so
            if (linkToLabel != null || linkFromLabel != null) {
                final RuntimeDeviceLayout deviceLayout =
                        pageContext.getDeviceLayoutContext().getDeviceLayout();
                final List fragments =
                        deviceLayout.getEnclosingFragments(containerName);
                for (Iterator iter = fragments.iterator(); iter.hasNext();) {
                    final LayoutContentActivator.ContainerPosition position =
                            (LayoutContentActivator.ContainerPosition) iter.next();
                    final Fragment fragment = position.getFragment();

                    // get the associated fragment instance
                    final int dimensions = fragment.getDimensions();
                    final NDimensionalIndex index;
                    if (dimensions > 0) {
                        index = new NDimensionalIndex(new int[dimensions]);
                    } else {
                        index = NDimensionalIndex.ZERO_DIMENSIONS;
                    }
                    final FragmentInstance fragmentInstance =
                            (FragmentInstance)
                            pageContext.getFormatInstance(fragment, index);

                    // set the linkTo/linkFrom buffer
                    final int containerIndex = position.getIndex();
                    if (linkToLabel != null) {
                        fragmentInstance.setLinkToBuffer(linkToLabel,
                                containerIndex);
                    }
                    if (linkFromLabel != null) {
                        fragmentInstance.setLinkFromBuffer(linkFromLabel,
                                containerIndex);
                    }
                }
            }
        }
    }
}
