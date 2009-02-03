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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.shared.metadata.jibx;

import com.volantis.shared.metadata.InternalMetaDataFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.localization.LocalizationFactory;

import org.jibx.runtime.IMarshaller;
import org.jibx.runtime.IUnmarshaller;
import org.jibx.runtime.IAliasable;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.JiBXException;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.impl.MarshallingContext;
import org.jibx.runtime.impl.UnmarshallingContext;

/**
 * Meta data delegating marshaller/unmarshaller.
 */
public class MetaDataDelegatingMarshaller
        implements IMarshaller, IUnmarshaller, IAliasable {

    private static final Class JIBX_FACTORY_CLASS =
        InternalMetaDataFactory.getInternalInstance().getJiBXFactoryClass();

    /**
     * Used to localize the messages in exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(
            MetaDataWrapperMarshaller.class);

    /**
     * Namespace uri
     */
    protected String uri;
    /**
     * random index
     */
    protected int index;
    /**
     * Name given to this "daily" instance.
     */
    protected String name;

    /**
     * Default constructor
     */
    public MetaDataDelegatingMarshaller() {
        this("not a uri", 0,"not my real name");
    }

    /**
     * Construct the marshaller with the specified uri, name and index
     *
     * @param uri the namepsace of the element this marshaller is bound to
     * @param index the index (don't know what this is for)
     * @param name the name of the element this marshaller is bound to
     */
    public MetaDataDelegatingMarshaller(
            final String uri, final int index, final String name) {
        this.uri = uri;
        this.name = name;
        this.index = index;
    }

    /**
     * Implement this method to return the namespace (uri) of the target
     * element
     *
     * @return the namespace (uri) of the target element
     */
    public String getTargetNamespace() {
        return this.uri;
    }

    /**
     * Return false. This cannot be an extension
     */
    public boolean isExtension(int i) {
        return false;
    }

    // javadoc inherited
    public void marshal(Object o, IMarshallingContext iMarshallingContext)
        throws JiBXException {

        IBindingFactory factory = BindingDirectory.getFactory(
            "metadata", JIBX_FACTORY_CLASS);

        MarshallingContext ctx = (MarshallingContext) iMarshallingContext;
        preprocessMarshallingContext(ctx);

        MarshallingContext delegateContext =
            (MarshallingContext) factory.createMarshallingContext();
        try {
            delegateContext.setFromContext(ctx);
        } catch(java.io.IOException e) {
            throw new JiBXException("Could not set from another context", e);
        }
        int pos = findIndex(factory.getMappedClasses(), o.getClass().getName());
        if (pos < 0) {
            throw new JiBXException(EXCEPTION_LOCALIZER.format(
                "marshaller-not-found", o.getClass().getName()));
        }

        IMarshaller marshaller =
            delegateContext.getMarshaller(pos, o.getClass().getName());

        marshaller.marshal(o, delegateContext);
        postprocessMarshallingContext(ctx);
    }

    protected void preprocessMarshallingContext(final MarshallingContext ctx)
            throws JiBXException {
        // do nothing
    }

    protected void postprocessMarshallingContext(final MarshallingContext ctx)
            throws JiBXException {
        // do nothing
    }

    /**
     * Find the index that contains the string that matches "match"
     *
     * @param table the String[] to search for the match
     * @param match the string to match against the String[]
     * @return the index of the specified class
     */
    private int findIndex(String[] table, String match) {
        int result = -1;
        boolean finished = false;
        for(int i=0; i<table.length && !finished; i++) {
            if (table[i] != null && table[i].equals(match)) {
                finished = true;
                result = i;
            }
        }
        return result;
    }

    /**
     * Return true if the element is named as expected
     *
     * @param iUnmarshallingContext
     * @return true if the element is named as expectede
     * @throws org.jibx.runtime.JiBXException
     */
    public boolean isPresent(IUnmarshallingContext iUnmarshallingContext)
        throws JiBXException {

        boolean result = false;
        UnmarshallingContext ctx =
            (UnmarshallingContext) iUnmarshallingContext;
        if (ctx.toStart().equals(name)) {
            result = true;
        }

        return result;
    }

    /**
     * Unmarshall objects in the delegated bindings.
     *
     * @param o
     * @param iUnmarshallingContext
     * @return
     * @throws org.jibx.runtime.JiBXException
     */
    public Object unmarshal(Object o,
                            IUnmarshallingContext iUnmarshallingContext)
        throws JiBXException {

        UnmarshallingContext ctx = (UnmarshallingContext) iUnmarshallingContext;
        preprocessUnmarshallingContext(ctx);
        final String nextElement = ctx.toStart();

        IBindingFactory delegateFactory =
            BindingDirectory.getFactory("metadata", JIBX_FACTORY_CLASS);

        UnmarshallingContext delegateContext =
            (UnmarshallingContext) delegateFactory.createUnmarshallingContext();
        delegateContext.setFromContext(ctx);
        // note that this is not checking that the namespace matches
        final int pos = findIndex(
            delegateFactory.getElementNames(),
            delegateFactory.getElementNamespaces(),
            nextElement, ctx.getElementNamespace());
        if (pos < 0) {
            throw new JiBXException(
                EXCEPTION_LOCALIZER.format("unmarshaller-not-found",
                    "{" + uri + "} " + JIBX_FACTORY_CLASS.getName()));
        }
        IUnmarshaller unmarshaller =  delegateContext.getUnmarshaller(pos);
        final Object value = unmarshaller.unmarshal(o, delegateContext);
        postprocessUnmarshallingContext(ctx);
        return value;
    }

    protected void preprocessUnmarshallingContext(final UnmarshallingContext ctx)
            throws JiBXException {
        // do nothing
    }

    protected void postprocessUnmarshallingContext(final UnmarshallingContext ctx)
            throws JiBXException {
        // do nothing
    }

    private int findIndex(final String[] table1, final String[] table2,
                          final String match1, final String match2) {
        int result = -1;
        boolean finished = false;
        for(int i=0; i<table1.length && !finished; i++) {
            if (table1[i] != null && table1[i].equals(match1) &&
                    table2[i] != null && table2[i].equals(match2)) {
                finished = true;
                result = i;
            }
        }
        return result;
    }
}
