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
 * (c) Volantis Systems Ltd 2003. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.eclipse.ab.editors.dom;

import com.volantis.mcs.eclipse.common.odom.ChangeQualifier;
import com.volantis.mcs.eclipse.common.odom.ODOMAttribute;
import com.volantis.mcs.eclipse.common.odom.ODOMChangeListener;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;
import org.jdom.Attribute;
import org.jdom.DataConversionException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;

/**
 * The proxy Attribute that allows the setValue method to be used.
 */
public class ProxyAttribute extends ODOMAttribute {
    /**
     * Used for logging
     */
    private static final LogDispatcher logger = 
            LocalizationFactory.createLogger(ProxyAttribute.class);


    
    /**
     * The real (or "target") attribute for which the proxy attribute exists
     */
    protected final ODOMAttribute delegate;
    
    /**
     * The ProxyElement that owns this attribute
     */
    protected final ProxyElement element;

    /**
     * Constructor
     * @param delegate The attribute that this instance wraps
     * @param element The owning ProxyElement
     */
    public ProxyAttribute(ODOMAttribute delegate,
                          ProxyElement element) {
        // pass in null as the validator.
        super();
        this.delegate = delegate;
        this.element = element;
    }

    // javadoc inherited
    public Attribute detach() {
        return delegate.detach();
    }

    // javadoc inherited
    public int getAttributeType() {
        return delegate.getAttributeType();
    }

    // javadoc inherited
    public boolean getBooleanValue()
            throws DataConversionException {
        return delegate.getBooleanValue();
    }

    // javadoc inherited
    public Document getDocument() {
        return delegate.getDocument();
    }

    // javadoc inherited
    public double getDoubleValue()
            throws DataConversionException {
        return delegate.getDoubleValue();
    }

    // javadoc inherited
    public float getFloatValue()
            throws DataConversionException {
        return delegate.getFloatValue();
    }

    // javadoc inherited
    public int getIntValue()
            throws DataConversionException {
        return delegate.getIntValue();
    }

    // javadoc inherited
    public long getLongValue()
            throws DataConversionException {
        return delegate.getLongValue();
    }

    // javadoc inherited
    public String getName() {
        return delegate.getName();
    }

    // javadoc inherited
    public Namespace getNamespace() {
        return delegate.getNamespace();
    }

    // javadoc inherited
    public String getNamespacePrefix() {
        return delegate.getNamespacePrefix();
    }

    // javadoc inherited
    public String getNamespaceURI() {
        return delegate.getNamespaceURI();
    }

    // javadoc inherited
    public Element getParent() {
        return delegate.getParent();
    }

    // javadoc inherited
    public String getQualifiedName() {
        return delegate.getQualifiedName();
    }

    // javadoc inherited
    public String getValue() {
        return delegate.getValue();
    }

    /**
     * Ensure that this method is Unsupported.
     * @param type the attribute type.
     * @return n/a
     */
    public Attribute setAttributeType(int type) {
        throw new UnsupportedOperationException(
                "Cannot (re)set the type for attribute " + //$NON-NLS-1$
                delegate.getName());
    }

    /**
     * Ensure that this method is Unsupported.
     * @param name the attribute name.
     * @return n/a
     */
    public Attribute setName(String name) {
        throw new UnsupportedOperationException(
                "Cannot (re)set the name for attribute " + //$NON-NLS-1$
                delegate.getName());
    }

    /**
     * Ensure that this method is Unsupported.
     * @param namespace the attribute namespace..
     * @return n/a
     */
    public Attribute setNamespace(Namespace namespace) {
        throw new UnsupportedOperationException(
                "Cannot set the namespace for attribute " + //$NON-NLS-1$
                delegate.getName());
    }

    /**
     * Delegates to the owning element
     */
    public Attribute setValue(String value) {
        if(logger.isDebugEnabled()) {
            logger.debug("setValue: value=\"" + value + "\""); //$NON-NLS-1$ //$NON-NLS-2$
        }
        element.setAttribute(delegate.getName(), value);
        return delegate;
    }

    /**
     * Delegates to the owning element
     */
    public String toString() {
        return delegate.toString();
    }

    /**
     * Delegates to the owning element
     */
    public void addChangeListener(ODOMChangeListener listener) {
        delegate.addChangeListener(listener);
    }

    /**
     * Delegates to the owning element
     */
    public void addChangeListener(
            ODOMChangeListener listener,
            ChangeQualifier changeQualifier) {
        delegate.addChangeListener(listener, changeQualifier);
    }

    /**
     * Delegates to the owning element
     */
    public void removeChangeListener(ODOMChangeListener listener) {
        delegate.removeChangeListener(listener);
    }

    /**
     * Delegates to the owning element
     */
    public void removeChangeListener(
            ODOMChangeListener listener,
            ChangeQualifier changeQualifier) {
        delegate.removeChangeListener(listener, changeQualifier);
    }

    // JavaDoc inherited
    public void detachObservable() {
        delegate.detachObservable();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 29-Nov-04	6232/2	doug	VBM:2004111702 Refactored Logging framework

 09-Jul-04	4841/1	adrianj	VBM:2004010802 Removal of ODOMValidatable infrastructure

 18-May-04	4429/1	claire	VBM:2004051401 Remove listeners for detached ODOMObservable instances

 03-Feb-04	2820/1	doug	VBM:2004013002 Used the eclipse 'externalize strings wizard' to identify language specific resources

 15-Dec-03	2160/2	doug	VBM:2003120702 Modified ODOMObservables so that they cab validate themesevles.

 12-Dec-03	2123/1	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 10-Dec-03	1968/4	richardc	VBM:2003111502 Second semi-tested draft for code release

 03-Dec-03	1968/2	richardc	VBM:2003111502 Untested draft for code review

 28-Nov-03	2041/1	byron	VBM:2003111502 Provide sophisticated, hierarchical ODOM ProxyElement - testcases

 ===========================================================================
*/
