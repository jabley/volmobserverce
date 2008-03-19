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

package com.volantis.xml.pipeline.sax.drivers.web;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.xml.pipeline.sax.XMLProcessingException;
import com.volantis.xml.pipeline.sax.operation.AbstractOperationProcess;
import org.xml.sax.SAXException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * The operation process for the parameter, header and cookie adapter
 * processes.
 */
public class HTTPMessageEntityOperationProcess
        extends AbstractOperationProcess {

    /**
     *  Volantis copyright mark.
     */
    private static String mark
            = "(c) Volantis Systems Ltd 2003. ";

    /**
     * Used for logging
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(HTTPMessageEntityOperationProcess.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer =
                LocalizationFactory.createExceptionLocalizer(
                            HTTPMessageEntityOperationProcess.class);

    /**
     * Store the characters found by the invocations to the characters(..)
     * method.
     */
    private StringBuffer chars;

    /**
     * Store the <code>DerivableHTTPMessageEntity</code> object.
     */
    private DerivableHTTPMessageEntity derivableHTTPMessageEntity;

    /**
     * Default constructor.
     */
    public HTTPMessageEntityOperationProcess() {
    }

    /**
     * Set the <code>DerivableHTTPMessageEntity</code> object.
     *
     * @param entity the <code>DerivableHTTPMessageEntity</code> object.
     */
    public void setDerivableHTTPMessageEntity(DerivableHTTPMessageEntity entity) {
        this.derivableHTTPMessageEntity = entity;
    }

    // Javadoc inherited
    public void characters(char ch[],
                           int start,
                           int length) throws SAXException {
        // Store the characters in a variable..
        if (chars == null) {
            chars = new StringBuffer(length);
        }
        chars.append(ch, start, length);
    }

    // Javadoc inherited
    public void endElement(String namespaceURI,
                           String localName,
                           String qName) throws SAXException {

        if ((localName != null) && (chars != null) &&
                (derivableHTTPMessageEntity != null)) {

            final Class[] parameterTypes = new Class[]{String.class};
            // The value is stripped because whitespace is not allowed in
            // attribute names in HTML 1.1. See
            // http://www.w3.org/Protocols/rfc2616/rfc2616-sec2.html#sec2.1
            final String value = chars.toString().trim();
            final Object[] arguments = new Object[]{value};
            final String methodName = "set" +
                    Character.toUpperCase(localName.charAt(0)) +
                    localName.substring(1);

            if (logger.isDebugEnabled()) {
                logger.debug("Contents used for: " + methodName +
                             ": value: '" + value + "'");
            }

            try {
                final Class cls = derivableHTTPMessageEntity.getClass();
                Method method = cls.getMethod(methodName, parameterTypes);
                method.invoke(derivableHTTPMessageEntity, arguments);

            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                logger.error("unexpected-no-such-method-exception", e);
                error(new XMLProcessingException(
                            exceptionLocalizer.format(
                                        "unexpected-no-such-method-exception"),
                            getPipelineContext().getCurrentLocator(),
                            e));
            } catch (SecurityException e) {
                e.printStackTrace();
                logger.error("unexpected-security-exception", e);
                error(new XMLProcessingException(
                            exceptionLocalizer.format(
                                        "unexpected-security-exception"),
                            getPipelineContext().getCurrentLocator(),
                            e));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                logger.error("unexpected-illegal-access-exception", e);
                error(new XMLProcessingException(
                            exceptionLocalizer.format(
                                        "unexpected-illegal-access-exception"),
                            getPipelineContext().getCurrentLocator(),
                            e));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                logger.error("unexpected-exception", e);
                error(new XMLProcessingException(
                            exceptionLocalizer.format("unexpected-excpetion"),
                            getPipelineContext().getCurrentLocator(),
                            e));
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                logger.error("unexpected-invocation-target-exception", e);
                error(new XMLProcessingException(
                            exceptionLocalizer.format(
                                        "unexpected-invocation-target-exception"),
                            getPipelineContext().getCurrentLocator(),
                            e));
            } finally {
                // Reset the chars array.
                chars = null;
            }
        } else {
            final String message = "localName, characters and message entity " +
                    "should not be null. Values are: localName='" + localName +
                    "', chars='" + chars + "', derivableMessageEntity='" +
                    derivableHTTPMessageEntity + "'";
            if (logger.isDebugEnabled()) {
                logger.debug(message);
            }

            error(new XMLProcessingException(
                    message, getPipelineContext().getCurrentLocator()));
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 29-Nov-04	6232/1	doug	VBM:2004111702 Refactored Logging framework

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 11-Aug-03	275/1	doug	VBM:2003073104 Provided default implementation of DynamicProcess interface

 31-Jul-03	238/8	byron	VBM:2003072309 Create the adapter process for parent task v4

 31-Jul-03	217/1	allan	VBM:2003071702 Add and use identities for HTTPMessageEntity objects.

 30-Jul-03	238/5	byron	VBM:2003072309 Create the adapter process for parent task - preliminary commit v3

 30-Jul-03	238/3	byron	VBM:2003072309 Create the adapter process for parent task - preliminary commit v2

 30-Jul-03	238/1	byron	VBM:2003072309 Create the adapter process for parent task - preliminary commit

 ===========================================================================
*/
