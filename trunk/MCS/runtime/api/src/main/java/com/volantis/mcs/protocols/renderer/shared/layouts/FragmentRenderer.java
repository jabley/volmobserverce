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
/* ---------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2004. 
 * ---------------------------------------------------------------------------
 */

package com.volantis.mcs.protocols.renderer.shared.layouts;

import com.volantis.mcs.context.MarinerPageContext;
import com.volantis.mcs.context.ResponseCachingDirectives;
import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.Fragment;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.layouts.FormatInstance;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.protocols.renderer.layouts.FormatRendererContext;
import com.volantis.mcs.protocols.renderer.layouts.FragmentLinkWriter;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.io.IOException;

/**
 * A format renderer that is used to render fragments.
 */
public class FragmentRenderer
        extends AbstractFormatRenderer {
    
    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(FragmentRenderer.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
                LocalizationFactory.createExceptionLocalizer(
                        FragmentRenderer.class);

    // javadoc inherited
    public void render(final FormatRendererContext context, final FormatInstance instance)
            throws RendererException {
        try {
            if (context.isFragmentationSupported()) {
                if (!instance.isEmpty()) {
                    final MarinerPageContext pageContext =
                        context.getDeviceLayoutContext().getMarinerPageContext();
                    final ResponseCachingDirectives cachingDirectives =
                        pageContext.getEnvironmentContext().getCachingDirectives();
                    // caching is not supported for fragmented pages
                    if (cachingDirectives != null) {
                        cachingDirectives.disable();
                    }

                    Fragment fragment = (Fragment)instance.getFormat();
                    Format child = fragment.getChildAt(0);

                    // If there is no child then there is nothing to write,
                    // not even a link.
                    if (child == null) {
                        return;
                    }

                    Fragment currentFragment =
                            context.getCurrentFragment();

                    // If the page is not being fragmented, then write out
                    // the children.
                    if (currentFragment == null) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Page is not being fragmented, " +
                                    "write everything");
                        }
                        FormatInstance childInstance =
                                context.getFormatInstance(
                                        child, instance.getIndex());
                        context.renderFormat(childInstance);
                    }
                    // If this fragment is the one which has been requested
                    // then write out the children.
                    else if (fragment == currentFragment) {

                        if (logger.isDebugEnabled()) {
                            logger.debug("Page is being fragmented, writing " +
                                    "fragment " + fragment.getName());
                        }
                        // Ask the children to write their output.
                        FormatInstance childInstance =
                                context.getFormatInstance(
                                        child, instance.getIndex());
                        context.renderFormat(childInstance);

                        // If this fragment is not the one which has been
                        // requested then write out just a link to this
                        // fragment.
                    } else {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Page is being fragmented, " +
                                    "creating link to fragment " +
                                    fragment.getName());
                        }
                        // Write out a fragment link, it should appear as an
                        // independent link as it is in the middle of the page
                        // somewhere rather than in the parent / peer link
                        // list.
                        final FragmentLinkWriter fragmentLinkWriter =
                                context.getFragmentLinkWriter();

                        fragmentLinkWriter.writeFragmentLink(
                                context, currentFragment, fragment,
                                false, false);
                    }
                }
            } else {
                throw new UnsupportedOperationException();
            }
        } catch (IOException e) {
            throw new RendererException(
                    exceptionLocalizer.format("renderer-error",
                            instance.getFormat()), e);
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8862/2	pduffin	VBM:2005062108 Refactored layout rendering to make it more testable.

 06-Jan-05	6391/7	adrianj	VBM:2004120207 Refactored DeviceLayoutRenderer into separate renderer classes

 06-Jan-05	6391/5	adrianj	VBM:2004120207 Refactored DeviceLayoutRenderer into separate renderer classes

 21-Dec-04	6402/1	philws	VBM:2004120208 Added aligning spatial format iterator renderer

 10-Dec-04	6391/1	adrianj	VBM:2004120207 Refactoring DeviceLayoutRenderer

 ===========================================================================
*/
