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

package com.volantis.shared.metadata.jibx;

import com.volantis.shared.metadata.InternalMetaDataFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.localization.LocalizationFactory;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IAliasable;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshaller;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshaller;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;
import org.jibx.runtime.impl.MarshallingContext;
import org.jibx.runtime.impl.UnmarshallingContext;

public class MetaDataWrapperMarshaller extends MetaDataDelegatingMarshaller {

    /**
     * Default constructor
     */
    public MetaDataWrapperMarshaller() {
        super();
    }

    /**
     * Construct the marshaller with the specified uri, name and index
     *
     * @param uri the namepsace of the element this marshaller is bound to
     * @param index the index (don't know what this is for)
     * @param name the name of the element this marshaller is bound to
     */
    public MetaDataWrapperMarshaller(String uri, int index, String name) {
        super(uri, index, name);
    }

    protected void preprocessMarshallingContext(final MarshallingContext ctx)
            throws JiBXException {
        ctx.startTagNamespaces(index, name, new int[]{index}, new String[]{""});
        ctx.closeStartContent();
    }

    protected void postprocessMarshallingContext(final MarshallingContext ctx)
            throws JiBXException {
        ctx.endTag(index, name);
    }

    protected void preprocessUnmarshallingContext(final UnmarshallingContext ctx)
            throws JiBXException {
        ctx.parsePastStartTag(uri, name);
    }

    protected void postprocessUnmarshallingContext(final UnmarshallingContext ctx)
            throws JiBXException {
        ctx.parsePastEndTag(uri, name);
    }

}
