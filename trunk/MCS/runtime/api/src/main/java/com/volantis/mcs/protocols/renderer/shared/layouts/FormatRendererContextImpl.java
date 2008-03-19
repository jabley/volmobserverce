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

package com.volantis.mcs.protocols.renderer.shared.layouts;

import com.volantis.mcs.context.ApplicationContext;
import com.volantis.mcs.context.ContextInternals;
import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.context.OutputBufferStack;
import com.volantis.mcs.layouts.FormFragment;
import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.Fragment;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.protocols.DeviceLayoutContext;
import com.volantis.mcs.protocols.DeviceLayoutRenderer;
import com.volantis.mcs.protocols.NDimensionalContainer;
import com.volantis.mcs.protocols.layouts.FormatInstance;
import com.volantis.mcs.protocols.layouts.LayoutModule;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.protocols.renderer.layouts.FormatRenderer;
import com.volantis.mcs.protocols.renderer.layouts.FormatRendererContext;
import com.volantis.mcs.protocols.renderer.layouts.FormatRendererSelector;
import com.volantis.mcs.protocols.renderer.layouts.FragmentLinkWriter;
import com.volantis.mcs.protocols.renderer.layouts.SegmentLinkWriter;
import com.volantis.mcs.runtime.layouts.RuntimeDeviceLayout;
import com.volantis.mcs.runtime.layouts.styling.FormatStylingEngine;
import com.volantis.mcs.runtime.layouts.styling.FormatStylingEngineImpl;
import com.volantis.styling.StylingFactory;
import com.volantis.styling.values.ImmutablePropertyValues;
import com.volantis.styling.engine.StylingEngine;

/**
 * Implementation of {@link FormatRendererContext} that wraps a
 * {@link DeviceLayoutContext}.
 *
 * <p>This does not resolve the {@link MarinerPageContext} at construction
 * time because it is possible that the {@link MarinerPageContext} associated
 * with the {@link DeviceLayoutContext} will be changed. This occurs in nested
 * canvasses, i.e. portlets and inclusions. When they are created they are
 * associated with their own {@link MarinerPageContext} but when the layouts
 * are rendered they are associated with the {@link MarinerPageContext} of the
 * containing page. This causes a number of issues and will be resolved in
 * future.</p>
 */
public final class FormatRendererContextImpl
        implements FormatRendererContext {

    /**
     * The default format renderer selector to use.
     */
    private static final FormatRendererSelector DEFAULT_FORMAT_RENDERER_SELECTOR
            = new DefaultFormatRendererSelector();

    /**
     * The mariner page context.
     */
    private final MarinerPageContext pageContext;
    private final FragmentLinkWriter fragmentLinkWriter;
    private final FormatStylingEngine formatStylingEngine;
    private IteratedFormatInstanceCounterImpl instanceCounter;

    /**
     * The {@link FormatRendererSelector} that must be used to select the
     * appropriate {@link FormatRenderer} when rendering formats within this
     * context.
     */
    private final FormatRendererSelector formatRendererSelector =
            DEFAULT_FORMAT_RENDERER_SELECTOR;

    /**
     * Initialise.
     *
     * @param pageContext The {@link MarinerPageContext} that this wraps.
     * @param fragmentLinkWriter
     */
    public FormatRendererContextImpl(
            MarinerPageContext pageContext,
            FragmentLinkWriter fragmentLinkWriter,
            StylingFactory stylingFactory) {

        this.pageContext = pageContext;
        this.fragmentLinkWriter = fragmentLinkWriter;

        StylingEngine engine = stylingFactory.createStylingEngine();
        formatStylingEngine = new FormatStylingEngineImpl(engine);
    }

    /**
     * Initialise.
     *
     * @param pageContext The {@link MarinerPageContext} that this wraps.
     */
    public FormatRendererContextImpl(
            MarinerPageContext pageContext,
            StylingFactory stylingFactory) {
        this(pageContext, new DefaultFragmentLinkWriter(pageContext),
                stylingFactory);
    }

    // Javadoc inherited.
    public LayoutModule getLayoutModule() {
        return pageContext.getProtocol();
    }

    // Javadoc inherited.
    public OutputBufferStack getOutputBufferStack() {
        return pageContext.getOutputBufferStack();
    }

    // Javadoc inherited.
    public void pushDeviceLayoutContext(DeviceLayoutContext context) {
        ImmutablePropertyValues propertyValues =
                context.getInheritableStyleValues();
        FormatStylingEngine stylingEngine = getFormatStylingEngine();
        stylingEngine.pushPropertyValues(propertyValues);

        pageContext.pushDeviceLayoutContext(context);
    }

    // Javadoc inherited.
    public void popDeviceLayoutContext() {
        DeviceLayoutContext context = pageContext.popDeviceLayoutContext();

        ImmutablePropertyValues propertyValues =
                context.getInheritableStyleValues();
        FormatStylingEngine stylingEngine = getFormatStylingEngine();
        stylingEngine.popPropertyValues(propertyValues);
    }

    // Javadoc inherited.
    public DeviceLayoutContext getDeviceLayoutContext() {
        return pageContext.getDeviceLayoutContext();
    }

    // Javadoc inherited.
    public NDimensionalContainer getFormatInstancesContainer(Format format) {
        DeviceLayoutContext context = getDeviceLayoutContext();
        return context.getFormatInstancesContainer(format);
    }

    public void setCurrentFormatIndex(NDimensionalIndex index) {
        pageContext.getDeviceLayoutContext().setCurrentFormatIndex(index);
    }

    public FormatInstance getFormatInstance(Format format, NDimensionalIndex index) {
        return pageContext.getDeviceLayoutContext().getFormatInstance(format, index);
    }

    public String getInclusionPath() {
        return pageContext.getDeviceLayoutContext().getInclusionPath();
    }

    public boolean isFragmentationSupported() {
        MarinerRequestContext requestContext = pageContext.getRequestContext();
        ApplicationContext applicationContext =
                ContextInternals.getApplicationContext(requestContext);
        return applicationContext.isFragmentationSupported();
    }

    public FormFragment getCurrentFormFragment() {
        return pageContext.getDeviceLayoutContext().getCurrentFormFragment();
    }

    public Fragment getCurrentFragment() {
        return pageContext.getDeviceLayoutContext().getCurrentFragment();
    }

    public FragmentLinkWriter getFragmentLinkWriter() {
        return fragmentLinkWriter;
    }

    public void beginNestedLayout(DeviceLayoutContext context) {
//        MarinerPageContext pageContext = context.getMarinerPageContext();
//        context.setMarinerPageContext(pageContext);
    }

    public void endNestedLayout(DeviceLayoutContext context) {
    }

    public RuntimeDeviceLayout getDeviceLayout() {
        return pageContext.getDeviceLayoutContext().getDeviceLayout();
    }

    public SegmentLinkWriter getSegmentLinkWriter() {
        return pageContext.getDeviceLayoutContext().getSegmentLinkWriter();
    }

    public DeviceLayoutRenderer getDeviceLayoutRenderer() {
        return DeviceLayoutRenderer.getSingleton();
    }

    public IteratedFormatInstanceCounter getInstanceCounter() {
        if (instanceCounter == null) {
            instanceCounter = new IteratedFormatInstanceCounterImpl(this);
        }

        return instanceCounter;
    }

    public void renderFormat(FormatInstance instance)
            throws RendererException {

        Format format = instance.getFormat();
        FormatRenderer renderer =
                formatRendererSelector.selectFormatRenderer(format);
        renderer.render(this, instance);
    }

    public FormatStylingEngine getFormatStylingEngine() {
        return formatStylingEngine;
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 28-Nov-05	10467/1	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 28-Nov-05	10394/3	ibush	VBM:2005111812 Styling Fixes for Orange Test Page

 25-Nov-05	10394/1	ibush	VBM:2005111812 interim commit for Willobs

 15-Nov-05	10326/7	ianw	VBM:2005110425 Fixed up formating/comments

 15-Nov-05	10326/1	ianw	VBM:2005110425 Fixed stacking issue with device layouts in DeviceLayoutRenderer

 14-Nov-05	10278/1	ianw	VBM:2005110425 Fix the releasing of DeviceLayoutContext's

 15-Nov-05	10278/3	ianw	VBM:2005110425 Fixed up formating/comments

 14-Nov-05	10278/1	ianw	VBM:2005110425 Fix the releasing of DeviceLayoutContext's

 03-Oct-05	9673/1	pduffin	VBM:2005092906 Added support for targeting content at layout using styles

 29-Sep-05	9654/1	pduffin	VBM:2005092817 Added support for expressions and functions in styles

 12-Sep-05	9372/1	ianw	VBM:2005082221 Allow only one instance of MarinerPageContext for a page

 22-Aug-05	9298/2	geoff	VBM:2005080402 Style portlets and inclusions correctly.

 18-Aug-05	9007/3	pduffin	VBM:2005071209 Committing massive changes to the product to improve styling, specifically for layouts

 15-Jul-05	9073/1	pduffin	VBM:2005071420 Created runtime device layout that only exposes the parts of DeviceLayout that are needed at runtime.

 11-Jul-05	8862/1	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 ===========================================================================
*/
