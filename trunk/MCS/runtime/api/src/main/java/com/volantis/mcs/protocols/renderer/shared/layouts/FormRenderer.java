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

import com.volantis.mcs.layouts.Form;
import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.FormAttributes;
import com.volantis.mcs.protocols.OutputBuffer;
import com.volantis.mcs.protocols.layouts.FormInstance;
import com.volantis.mcs.protocols.layouts.FormatInstance;
import com.volantis.mcs.protocols.layouts.LayoutAttributesFactory;
import com.volantis.mcs.protocols.layouts.LayoutModule;
import com.volantis.mcs.protocols.renderer.RendererException;
import com.volantis.mcs.protocols.renderer.layouts.FormatRendererContext;
import com.volantis.synergetics.localization.ExceptionLocalizer;

import java.io.IOException;

/**
 * A format renderer that is used to render forms.
 */
public class FormRenderer
        extends AbstractFormatRenderer {
    
    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
                LocalizationFactory.createExceptionLocalizer(
                        FormRenderer.class);

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
    public FormRenderer(LayoutAttributesFactory factory) {
        this.factory = factory;
    }

    // javadoc inherited
    public void render(final FormatRendererContext context, final FormatInstance instance)
            throws RendererException {
        try {
            if (!instance.isEmpty()) {
                Form form = (Form)instance.getFormat();

                Format child = form.getChildAt(0);

                // If there is no child then there is nothing to write.
                if (child == null) {
                    return;
                }

                LayoutModule module = context.getLayoutModule();
                FormInstance formInstance = (FormInstance) instance;

                FormatInstance childInstance =
                        context.getFormatInstance(
                                child, instance.getIndex());

                // If the page contained an XFForm element
                if (formInstance.getPreambleBuffer(false) != null ||
                        formInstance.getContentBuffer(false) != null ||
                        formInstance.getPostambleBuffer(false) != null) {
                    
                    // Write out the form and contained layouts
                    FormAttributes attributes = factory.createFormAttributes();
                    attributes.setForm(form);

                    module.writeOpenForm(attributes);

                    OutputBuffer buffer =
                            formInstance.getPreambleBuffer(false);
                    if (buffer != null) {
                        module.writeFormPreamble(buffer);
                    }

                    context.renderFormat(childInstance);

                    buffer = formInstance.getPostambleBuffer(false);
                    if (buffer != null) {
                        module.writeFormPostamble(buffer);
                    }

                    module.writeCloseForm(attributes);
                } else {
                    // Ignore the form, and just write the contained layouts
                    // NOTE: this is only here for backwards compatibility
                    // because otherwise if you add content to form's child
                    // panes from "outside" the form (and never created the
                    // form), it crashed.
                    // NOTE: this will probably be deprecated / removed in
                    // future since Paul believes that, in general, we ought
                    // to respect the hierarchy in the input markup, so adding
                    // content to a pane from "outside" its tree should be
                    // illegal.
                    context.renderFormat(childInstance);
                }
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

 06-Jan-05	6391/5	adrianj	VBM:2004120207 Refactored DeviceLayoutRenderer into separate renderer classes

 21-Dec-04	6402/1	philws	VBM:2004120208 Added aligning spatial format iterator renderer

 10-Dec-04	6391/1	adrianj	VBM:2004120207 Refactoring DeviceLayoutRenderer

 ===========================================================================
*/
