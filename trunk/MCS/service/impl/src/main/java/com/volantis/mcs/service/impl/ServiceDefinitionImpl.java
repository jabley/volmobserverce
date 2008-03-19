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
package com.volantis.mcs.service.impl;

import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.service.CharacteristicNotAvailableException;
import com.volantis.mcs.service.ServiceDefinition;
import com.volantis.shared.metadata.value.ImmutableMetaDataValueVisitee;
import com.volantis.shared.metadata.value.ImmutableMetaDataValueVisitor;
import com.volantis.shared.metadata.value.SimpleValue;
import com.volantis.shared.metadata.value.immutable.ImmutableBooleanValue;
import com.volantis.shared.metadata.value.immutable.ImmutableChoiceValue;
import com.volantis.shared.metadata.value.immutable.ImmutableListValue;
import com.volantis.shared.metadata.value.immutable.ImmutableMetaDataValue;
import com.volantis.shared.metadata.value.immutable.ImmutableNumberValue;
import com.volantis.shared.metadata.value.immutable.ImmutableQuantityValue;
import com.volantis.shared.metadata.value.immutable.ImmutableSetValue;
import com.volantis.shared.metadata.value.immutable.ImmutableStringValue;
import com.volantis.shared.metadata.value.immutable.ImmutableStructureValue;
import com.volantis.shared.metadata.value.immutable.ImmutableUnitValue;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.log.LogDispatcher;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Implementation of the {@link ServiceDefinition} interface
 */
public class ServiceDefinitionImpl implements ServiceDefinition {

    /**
     * The logger used by this class.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(ServiceDefinitionImpl.class);

    /**
    * Used to retrieve localized exception messages.
    */
    private static final ExceptionLocalizer exceptionLocalizer =
              LocalizationFactory.createExceptionLocalizer(
                      ServiceDefinitionImpl.class);

    /**
     * Constant path to the default-xslt characteristic
     */
    private static final String DEFAULT_XSLT = "/proxy/default-xslt";

    /**
     * Constant path to the remote target characteristic
     */
    private static final String REMOTE_TARGET_PREFIX = "/proxy/target/root";

    /**
     * The ImmutableStructureValue that represents the characteristics
     * of the Service
     */
    private ImmutableStructureValue characteristics;

    /**
     * Name of the service
     */
    private String serviceName;


    /**
     * Constructs a new <code>ServiceDefinitionImpl</code> instance
     *
     * @param characteristics the {@link ImmutableStructureValue} that
     * represent this services characteristics
     */
    public ServiceDefinitionImpl(String serviceName,
                                 ImmutableStructureValue characteristics) {
        if (null == serviceName) {
            throw new IllegalArgumentException("serviceName cannot be null");
        }
        if (null == characteristics) {
            throw new IllegalArgumentException("characteristics cannot be null");
        }
        this.serviceName = serviceName;
        this.characteristics = characteristics;
    }

    // javadoc inherited
    public String getName() {
        return serviceName;
    }

    // javadoc inherited
    public ImmutableMetaDataValue getCharacteristic(String path)
            throws CharacteristicNotAvailableException {
        if (path == null || path.trim().length() == 0) {
            throw new CharacteristicNotAvailableException(
                        exceptionLocalizer.format("service-def-path-failure",
                                                  new String[] {path,
                                                                getName()}));
        }
        // let the CharacteristicAccessor do the work
        CharacteristicAccessor accessor =
                new CharacteristicAccessor(characteristics);
        return accessor.retrieveValue(path);
    }

    // javadoc inherited
    public String getTransform() {
        String transform = null;
        try {
            ImmutableMetaDataValue value
                    = getCharacteristic(DEFAULT_XSLT);
            transform = value.getAsString();
            // todo as optional we need to check for empty string and
            // make null. The PMSS server will be fixed to convert empty
            // strings to null to avoid client pain
            if (transform != null && transform.trim().length() == 0) {
                transform = null;
            }
        } catch (CharacteristicNotAvailableException e) {
            // we will log at debug as this is optional
            if (logger.isDebugEnabled()) {
                logger.debug("The characteristic  " + DEFAULT_XSLT +
                             " wash not set for service " + getName());
            }

        }
        return transform;
    }

    // javadoc inherited
    public String getRemoteTarget() {
        String remoteTarget = null;
        try {
            ImmutableMetaDataValue value =
                    getCharacteristic(REMOTE_TARGET_PREFIX);
            remoteTarget = value.getAsString();
        } catch (CharacteristicNotAvailableException e) {
            // this is not an optional characteristic. We shall log an error
            // and return null
            logger.error("service-def-path-failure",
                         new String[] {REMOTE_TARGET_PREFIX, getName()});
        }
        return remoteTarget;
    }

    /**
     * An {@link ImmutableMetaDataValueVisitor} implementation that is intended
     * to take a XPath like path (String) and a {@link ImmutableMetaDataValue}
     * structure and resolve them
     */
    private class CharacteristicAccessor
            implements ImmutableMetaDataValueVisitor {

        /**
         * Characteristics to be accessed
         */
        private ImmutableStructureValue characteristics;

        /**
         * The resolved value
         */
        private ImmutableMetaDataValue value;

        /**
         * Used to parse and process the xpath that is being resolved to a
         * ImmutableMetaDataValue
         */
        private XPathProcessor processor;

        /**
         * Path that is currently being resolved
         */
        private String xpath;

        /**
         * Construcs a new instance
         *
         * @param characteristics the characteristics to be accessed
         */
        public CharacteristicAccessor(ImmutableStructureValue characteristics) {
            if (null == characteristics) {
                throw new IllegalArgumentException(
                        "characteristics cannot be null");
            }
            this.characteristics = characteristics;
        }

        /**
         * Returns the {@link SimpleValue} that the givent XPath points to.
         * @return the value that the xpath points to.
         */
        public ImmutableMetaDataValue retrieveValue(String xpath)
                throws CharacteristicNotAvailableException {

            if (null == xpath || xpath.trim().length() == 0) {
                throw new IllegalArgumentException("xpath arg cannot be null or empty");
            }
            this.xpath = xpath;
            if (logger.isDebugEnabled()) {
                logger.debug("retrieving meta data value for path '" + xpath +
                             "' and service '" + serviceName + "'");
            }
            processor = new XPathProcessor(xpath);
            ImmutableMetaDataValueVisitee visitee =
                    (ImmutableMetaDataValueVisitee) characteristics;
            try {
                visitee.accept(this);
            } catch (RuntimeCharacteristicNotAvailableException e) {
                logger.error("service-def-path-failure",
                             new String[] {xpath, serviceName}, e);
                throw e.getCharacteristicNotAvailableException();
            }
            if (logger.isDebugEnabled()) {
                logger.debug("retrieved meta data value for path '" + xpath +
                             "' and service '" + serviceName + "' value is " +
                             value.getAsString());
            }
            return value;
        }

        // javadoc inherited
        public void visit(ImmutableBooleanValue immutableBooleanValue) {
            // we have navigated to a leaf. Check to see that all nodes
            // in the supplied path have been visited
            assertAllNodesVisisted();
            // ok we have
            value = immutableBooleanValue;
        }

        // javadoc inherited
        public void visit(ImmutableListValue immutableListValue) {
            if (isResolved()) {
                // we have resolved the path. This is the value that the
                // path pointed to
                value = immutableListValue;
            } else {
                // ok we have a list and the path has not been resovled.
                // First we need to check that the the next node is an index
                // (predicate) and not a key
                assertPredicateNext();
                // ok we have a predicate lets
                List list = immutableListValue.getContentsAsList();
                int index = processor.popIndex();

                // check that the index is valid
                assertIndexValid(list, index);

                // index is ok so we can retrive the value and visit that
                ImmutableMetaDataValueVisitee visitee =
                        (ImmutableMetaDataValueVisitee)
                            list.get(index);
                visitee.accept(this);
            }
        }

        // javadoc inherited
        public void visit(ImmutableNumberValue immutableNumberValue) {
            // we have navigated to a leaf. Check to see that all nodes
            // in the supplied path have been visited
            assertAllNodesVisisted();
            value = immutableNumberValue;
        }

        // javadoc inherited
        public void visit(ImmutableQuantityValue immutableQuantityValue) {
            // don't support quantities
            throw new IllegalStateException(
                    "Quantity Values are not supported in Service Defintions");
        }

        // javadoc inherited
        public void visit(ImmutableSetValue immutableSetValue) {
            if (isResolved()) {
                // we have resolved the path. This is the value that the
                // path pointed to
                value = immutableSetValue;
            } else {
                // we have reached a set. In order to navigate further we need
                // the next part of the path being resolved to be a predicate
                // e.g. [1]
                assertPredicateNext();
                // next item is an index.
                int index = processor.popIndex();
                Collection collection =
                        immutableSetValue.getContentsAsCollection();
                // before we retrieve the meta value from the set we need to
                // check the specified index is valid
                assertIndexValid(collection, index);
                // we can retrieve the meta value from the set and visit it
                Object[] items = collection.toArray();
                ImmutableMetaDataValueVisitee visitee =
                        (ImmutableMetaDataValueVisitee) items[index];
                visitee.accept(this);
            }
        }

        // javadoc inherited
        public void visit(ImmutableStructureValue immutableStructureValue) {
            if (isResolved()) {
                // we have resolved the path. This is the value that the
                // path pointed to
                value = immutableStructureValue;
            } else {
                Map fieldValues = immutableStructureValue.getFieldValuesAsMap();
                assertFieldNext();
                String field = processor.popPath();
                assertFieldAvaliable(fieldValues, field);
                ImmutableMetaDataValueVisitee visitee =
                        (ImmutableMetaDataValueVisitee) fieldValues.get(field);
                visitee.accept(this);
            }
        }

        // javadoc inherited
        public void visit(ImmutableStringValue immutableStringValue) {
            // we have navigated to a leaf. Check to see that all nodes
            // in the supplied path have been visited
            assertAllNodesVisisted();
            // we have made it to a leaf node
            value = immutableStringValue;
        }

        // javadoc inherited
        public void visit(ImmutableUnitValue immutableUnitValue) {
            throw new IllegalStateException(
                    "Unit Values are not supported in Service Defintions");
        }

        // javadoc inherited
        public void visit(ImmutableChoiceValue immutableChoiceValue) {
            throw new IllegalStateException(
                    "Choice Values are not supported in Service Defintions");
        }

        /**
         * This method should be invoked to assert that the path has been
         * resolved correctly. i.e the structure has been navigated
         */
        private void assertAllNodesVisisted() {
            if (!processor.isEmpty()) {
                CharacteristicNotAvailableException e =
                        new CharacteristicNotAvailableException(
                                exceptionLocalizer.format(
                                    "service-def-path-failure",
                                    new String[] {xpath, serviceName}));
                throw new RuntimeCharacteristicNotAvailableException(e);
            }
        }

        private void assertFieldAvaliable(Map fields, String field) {
            if (fields.get(field) == null) {
                CharacteristicNotAvailableException e =
                        new CharacteristicNotAvailableException(
                                exceptionLocalizer.format(
                                    "service-def-path-failure",
                                    new String[] {xpath, serviceName}));
                throw new RuntimeCharacteristicNotAvailableException(e);
            }
        }

        private void assertFieldNext() {
            if (!processor.isPath()) {
                // We need the next path node to be a predicate but it is
                // a key. Throw an excpetion
                CharacteristicNotAvailableException e =
                        new CharacteristicNotAvailableException(
                                exceptionLocalizer.format(
                                    "service-def-path-failure",
                                    new String[] {xpath, serviceName}));
                throw new RuntimeCharacteristicNotAvailableException(e);
            }
        }
        private void assertPredicateNext() {
            if (processor.isPath()) {
                // We need the next path node to be a predicate but it is
                // a key. Throw an excpetion
                CharacteristicNotAvailableException e =
                        new CharacteristicNotAvailableException(
                                exceptionLocalizer.format(
                                    "service-def-path-failure",
                                    new String[] {xpath, serviceName}));
                throw new RuntimeCharacteristicNotAvailableException(e);
            }
        };

        private void assertIndexValid(Collection collection, int index) {
            if (index < 0 || index >= collection.size()) {
                // index is out of bounds
                CharacteristicNotAvailableException e =
                        new CharacteristicNotAvailableException(
                                exceptionLocalizer.format(
                                    "service-def-path-failure",
                                    new String[] {xpath, serviceName}));
                throw new RuntimeCharacteristicNotAvailableException(e);
            }
        }
        /**
         * Returns true iff the path has been resolved to the
         * {@link com.volantis.shared.metadata.value.MetaDataValue}
         *
         * @return Returns true iff the path has been resolved
         */
        private boolean isResolved() {
            return processor.isEmpty();
        }
    }

    /**
     * Class to encapsulate and process the xpath that points to some
     * characteristic
     */
    private static class XPathProcessor {

        /**
         * Used to determine if a string has a predicate part
         */
        private static final String IS_PREDICATE_REGEXP = ".+\\[\\d+\\]";

        /**
         * used to separate the nodes in the path.
         */
        private Stack nodes;

        /**
         * Creates a new <code>XPathProcessor</code> node
         * @param xpath the xpath being processed
         */
        public XPathProcessor(String xpath) {
            nodes = new Stack();
            String xpathStr = xpath.trim();
            if (xpathStr.startsWith("/")) {
                xpathStr = xpathStr.substring(1);
            }
            String[] strNodes = xpathStr.split("/");
            for (int i = strNodes.length - 1; i >= 0; i--) {
                String node = strNodes[i];
                if (node.matches(IS_PREDICATE_REGEXP)) {
                    // could use a regexp to extract the name and index.
                    // For now we will just do some simple string manipulation
                    int length = node.length();
                    // we know the string ends with a ] char so lets find the
                    // the matching [
                    int pos = node.lastIndexOf("[");
                    String indexStr = node.substring(pos + 1, length - 1);
                    int index = Integer.parseInt(indexStr);
                    nodes.push(new Integer(index));
                    String name = node.substring(0, pos);
                    nodes.push(name);
                } else {
                    nodes.push(node);
                }
            }
        }

        /**
         * Returns the integer for predicate
         * @return the predicate int
         */
        public int popIndex() {
            return ((Integer) nodes.pop()).intValue();
        }

        /**
         * Returns the path part of the node
         * @return the path part of the node
         */
        public String popPath() {
            return (String) nodes.pop();
        }

        /**
         * Returns true iff the next part node to be processed is a path part
         * rather than a predicate
         * @return true iff the next part node to be processed is a path part
         * rather than a predicate
         */
        public boolean isPath() {
            Object o = nodes.peek();
            return (o instanceof String);
        }

        /**
         * Returns true if all nodes have been processed
         * @return true if all nodes have been processed
         */
        boolean isEmpty() {
            return nodes.empty();
        }
    }

    /**
     * RuntimeException to work around the fact that the
     * {@link ImmutableMetaDataValueVisitor} does not allow a checked excpetions
     * to be thrown. This exception is intended to be caught within the
     * ServiceDefintionImpl implementation iteslf.
     */
    private class RuntimeCharacteristicNotAvailableException
            extends RuntimeException {

        /**
         * Wrapped checked exception
         */
        private CharacteristicNotAvailableException checkedException;

        /**
         * Constructor
         * @param e the checked exception that is being wrapped
         */
        public RuntimeCharacteristicNotAvailableException(
                CharacteristicNotAvailableException e) {
            this.checkedException = e;
        }

        /**
         * Returns the checked CharacteristicNotAvailableException
         * @return
         */
        public CharacteristicNotAvailableException
                getCharacteristicNotAvailableException() {
            return checkedException;
        }
    }
}
