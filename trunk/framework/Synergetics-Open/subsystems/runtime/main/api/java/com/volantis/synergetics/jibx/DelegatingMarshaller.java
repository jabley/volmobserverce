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
 * (c) Copyright Volantis Systems Ltd. 2007. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.synergetics.jibx;

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

public abstract class DelegatingMarshaller
    implements IMarshaller, IUnmarshaller, IAliasable {

    /**
     * Used to localize the messages in exceptions
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
        LocalizationFactory.createExceptionLocalizer(DelegatingMarshaller.class);

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
    public DelegatingMarshaller() {
        this("not a uri", 0,"not my real name");
    }

    /**
     * Construct the marshaller with the specified uri, name and index
     *
     * @param uri the namepsace of the element this marshaller is bound to
     * @param index the index (don't know what this is for)
     * @param name the name of the element this marshaller is bound to
     */
    public DelegatingMarshaller(String uri, int index, String name) {
        this.uri = uri;
        this.name = name;
        this.index = index;
    }

    /**
     * Implement this method to return the unqualified name of the target
     * binding
     *
     * @return the unqualified name of the target binding
     */
    public abstract String getTargetBindingName();

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
     * Implement this method to return the Class of the target element.
     * @return the Class of the target element.
     */
    public abstract Class getTargetClass();

    /**
     * Return false. This can not be an extension
     *
     * @param i
     * @return
     */
    public boolean isExtension(int i) {
        return false;
    }

    // javadoc inherited
    public void marshal(Object o, IMarshallingContext iMarshallingContext)
        throws JiBXException {

        IBindingFactory factory = BindingDirectory.getFactory(
            getTargetBindingName(), getTargetClass());

        MarshallingContext ctx = (MarshallingContext) iMarshallingContext;

        MarshallingContext delegateContext =
            (MarshallingContext) factory.createMarshallingContext();
        try {
            delegateContext.setFromContext(ctx);
        } catch(java.io.IOException e) {
            throw new JiBXException("Could not set from another context", e);
        }

        delegateContext.setXmlWriter(ctx.getXmlWriter());
        int pos = findIndex(factory.getMappedClasses(), getTargetClass().getName());
        if (pos < 0) {
            throw new JiBXException(EXCEPTION_LOCALIZER.format(
                "marshaller-not-found", o.getClass().getName()));
        }

        IMarshaller marshaller =
            delegateContext.getMarshaller(pos, getTargetClass().getName());

        marshaller.marshal(o, delegateContext);
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
            if (table[i].equals(match)) {
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
     * @throws JiBXException
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
     * @throws JiBXException
     */
    public Object unmarshal(Object o,
                            IUnmarshallingContext iUnmarshallingContext)
        throws JiBXException {

        UnmarshallingContext ctx = (UnmarshallingContext) iUnmarshallingContext;

        IBindingFactory delegateFactory =
            BindingDirectory.getFactory(getTargetBindingName(), getTargetClass());

        UnmarshallingContext delegateContext =
            (UnmarshallingContext) delegateFactory.createUnmarshallingContext();
        delegateContext.setFromContext(ctx);
        // note that this is not checking that the namespace matches
        final int pos = findIndex(
            delegateFactory.getMappedClasses(), getTargetClass().getName());
        if (pos < 0) {
            throw new JiBXException(
                EXCEPTION_LOCALIZER.format("unmarshaller-not-found",
                    "{" + uri + "} " + getTargetClass().getName()));
        }
        IUnmarshaller unmarshaller =  delegateContext.getUnmarshaller(pos);
        return unmarshaller.unmarshal(o, delegateContext);
    }
}
