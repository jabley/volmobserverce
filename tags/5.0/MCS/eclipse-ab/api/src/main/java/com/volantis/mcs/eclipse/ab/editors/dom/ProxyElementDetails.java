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

import org.jdom.Namespace;

import java.util.Iterator;
    


/**
 * Implementations of this interface are required to tell their encapsulating
 * {@link com.volantis.mcs.eclipse.ab.editors.dom.ProxyElement} what
 * the element's name should be and which attributes it should support.
 *
 * <p>The implementations may have to maintain internal state (the set of
 * proxied elements). Therefore instances of these implementations must
 * not be shared between different ProxyElement instances.</p>
 */
public interface ProxyElementDetails {

    /**
     * This class is a typesafe enumeration that enumerates the set of
     * reasons why a client of this interface would call setProxiedElements;
     * an instance of this class is passed to that method
     */
    final class ChangeReason {
        /**
         * Constructor is private to enforce instances to those declared
         * here as enumeration literals
         */
        private ChangeReason() {            
        }
    }
        
    /**
     * Indicates that (at least) one attribute value in (at least) one
     * target element has been changed (but that the set of attributes
     * and elements in the target have not changed)
     */
    ChangeReason ATTRIB_VALUES = new ChangeReason ();
        
    /**
     * Indicates that the set of proxied elements has changed because
     * (at least) one target element has had its name changed
     */
    ChangeReason ELEMENT_NAMES = new ChangeReason ();

    /**
     * Indicates that the text content of (at least) one target element has
     * been changed.
     */
    ChangeReason TEXT_VALUE = new ChangeReason();

    /**
     * Indicates that the set of proxied elements has changed because
     * (at least) one target element has been deleted or added to a
     * containing source element (NB this case can only occur when the
     * target elements are children of source elements)
     */
    ChangeReason ELEMENTS = new ChangeReason ();

    /**
     * Indicates that (at least) one attribute has been added and/or
     * removed from (at least) one target element (but the set of target
     * elements has not changed)
     */
    ChangeReason ATTRIBUTES = new ChangeReason ();

    /**
     * The set of elements that make up the "proxied elements" (i.e. the
     * "targets" in the terminology of ProxyElementDelegates, some or all
     * of which may possibly be null) are notified to the details object via
     * this method. The given iterator should be a freshly retrieved one.
     * 
     * <p>The reason parameter is to promote efficiency: an implementation
     * may decide that the reason a set of proxied elements has been updated
     * is one that it can simply ignore (or alternatively the reason may for
     * example optimise the processing necessary)</p>
     * 
     * <p>The return value is also to promote efficiency: It indicates to
     * the caller whether the call to this method has had an effect on at
     * least one of this interface's get/is method return values. So a return
     * of false guarantees to the caller that it need not re-call any of the
     * get/is methods in this interface.</p>
     *
     * <p>Implementations should not use the iterator's remove method.
     * On completion of this method, the iterator will be in an undefined
     * state.</p>
     * 
     * @param elements The (new) set of elements that are to be proxied
     * @param reason The reason for the change in the set of elements
     * @return true or false as described in the method javadocs
     */
    boolean setProxiedElements(Iterator elements, ChangeReason reason);

    /**
     * Returns the appropriate element name, which must be an XML-valid name
     * (and therefore, inter alia, non-null and non-empty).
     * 
     * <p>Note that although the return of this method may change
     * following a call to {@link #setProxiedElements} it must still be
     * supported before that method is called, and the abovementioned
     * restrictions on its return must apply in all cases.</p>
     * 
     * @return The proxy element name, constrained as described above
     */
    String getElementName();

    /**
     * Returns the appropriate element namespace, which may not be null: if
     * "no namespace" is to be returned, then the caller should return
     * Namespace.NO_NAMESPACE.
     * 
     * <p>Note that although the return of this method may change following
     * a call to {@link #setProxiedElements} it must still be supported
     * before that method is called.</p>
     * 
     * @return The proxy element namespace, constrained as described above
     */
    Namespace getElementNamespace();

    /**
     * Returns a copy of the appropriate set of attribute names, which may not
     * be null, but may be of zero length; if of non-zero length it must not
     * contain any null entries.
     * 
     * <p>Note that although the return of this method may change following
     * a call to {@link #setProxiedElements} it must still be supported
     * before that method is called.</p>
     * 
     * <p>Also note that a copy of the names is returned not only to
     * enforce data protection, but also as a convenience to clients who may
     * wish to preserve this data before the return of this method is
     * (potentially) changed by a call to {@link #setProxiedElements}.</p>
     * 
     * @return The appropriate (aka supported or aggregated) set of
     * attribute names, constrained as described above
     */
    String[] getAttributeNames();

    /**
     * Convenience method for determining whether the given attribute
     * name is currently within the array that would be returned by
     * a current call to getAttributeNames.
     *
     * @param name Attribute name for which it is to be determined
     * whether it is (currently) in the supported attributes list
     * @return True if and only if the attribute is supported, as described
     * above
     */
    boolean isAttributeName(String name);
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 06-Oct-05	9734/1	adrianj	VBM:2005100510 Allow text in ProxyElements

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 28-Jun-04	4764/1	matthew	VBM:2003121802 remove ProxyElementDetails.GENERAL

 17-Dec-03	2137/3	richardc	VBM:2003120402 Version for initial review (code and test harness)

 17-Dec-03	2137/1	richardc	VBM:2003120402 Version for initial review (code and test harness)

 16-Dec-03	2203/2	richardc	VBM:2003121102 Attribute-from-target logic test corrected

 12-Dec-03	2123/1	allan	VBM:2003102005 Candidate ImageComponentEditor - no validation.

 10-Dec-03	1968/16	richardc	VBM:2003111502 Second semi-tested draft for code release

 03-Dec-03	1968/5	richardc	VBM:2003111502 Untested draft for code review

 28-Nov-03	1968/3	richardc	VBM:2003111502 Code complete subject to javadoc, nulls check, exception details and review

 27-Nov-03	1968/1	richardc	VBM:2003111502 First draft for unit test

 ===========================================================================
*/
