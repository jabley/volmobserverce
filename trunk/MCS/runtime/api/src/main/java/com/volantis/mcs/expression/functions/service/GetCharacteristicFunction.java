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
package com.volantis.mcs.expression.functions.service;


import com.volantis.mcs.expression.functions.DefaultValueProvider;
import com.volantis.mcs.expression.functions.diselect.SingleMandatoryArgumentExpressionFunction;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.service.CharacteristicNotAvailableException;
import com.volantis.mcs.service.ServiceDefinition;
import com.volantis.shared.metadata.value.ImmutableMetaDataValueVisitee;
import com.volantis.shared.metadata.value.ImmutableMetaDataValueVisitor;
import com.volantis.shared.metadata.value.MetaDataValue;
import com.volantis.shared.metadata.value.SimpleValue;
import com.volantis.shared.metadata.value.immutable.ImmutableBooleanValue;
import com.volantis.shared.metadata.value.immutable.ImmutableChoiceValue;
import com.volantis.shared.metadata.value.immutable.ImmutableListValue;
import com.volantis.shared.metadata.value.immutable.ImmutableNumberValue;
import com.volantis.shared.metadata.value.immutable.ImmutableQuantityValue;
import com.volantis.shared.metadata.value.immutable.ImmutableSetValue;
import com.volantis.shared.metadata.value.immutable.ImmutableStringValue;
import com.volantis.shared.metadata.value.immutable.ImmutableStructureValue;
import com.volantis.shared.metadata.value.immutable.ImmutableUnitValue;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.atomic.BooleanValue;
import com.volantis.xml.expression.atomic.StringValue;
import com.volantis.xml.expression.atomic.numeric.DoubleValue;
import com.volantis.xml.expression.sequence.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * XPath function that provides access to a the various characteristics of a
 * Service
 */
public class GetCharacteristicFunction
        extends SingleMandatoryArgumentExpressionFunction {

    /**
     * The logger used by this class.
     */
    private static final LogDispatcher logger =
            LocalizationFactory.createLogger(GetCharacteristicFunction.class);

    /**
     * The DefaultValueProvider for this class.
     */
    private static final DefaultValueProvider DEFAULT_VALUE_PROVIDER =
            new DefaultValueProvider();

    // javadoc inherited
    protected String getFunctionName() {
        return "getCharacteristic";
    }

    // javadoc inherited
    protected Value execute(ExpressionContext expressionContext, String name,
                            Value defaultValue) {

        Value result = defaultValue;
        ServiceDefinition service = getServiceDefinition(expressionContext);
        if (service != null) {
            try {
                MetaDataValue value = service.getCharacteristic(name);
                result = createValue(expressionContext.getFactory(), value);
            } catch (CharacteristicNotAvailableException e) {
                // log the error but we still return the defaultValue
                logger.warn("get-characteristics-fn-failed",
                            new String[] {getFunctionName(), name},
                            e);
            }
        }
        return result;
    }

    /**
     * Creates the appropriate {@link Value} for the given {@link SimpleValue}
     *
     * @param factory an {@link ExpressionFactory} instance
     * @param value the {@link SimpleValue}
     * @return a {@link Value} instance
     */
    private Value createValue(ExpressionFactory factory,
                              MetaDataValue value) {
        Value xpathValue = null;
        if (value != null) {
            MetaValueFactory metaValueFactory = new MetaValueFactory(factory);
            xpathValue = metaValueFactory.createValue(value);
        }
        return xpathValue; 
    }

    // javadoc inherited
    protected DefaultValueProvider getDefaultValueProvider() {
        return DEFAULT_VALUE_PROVIDER;
    }

    /**
     * Extracts the {@link ServiceDefinition} from the ExpressionContext;
     *
     * @param context the ExpressionContext
     * @return a ServiceDefinition instance
     */
    private ServiceDefinition getServiceDefinition(ExpressionContext context) {
        ServiceDefinition service = (ServiceDefinition) context.getProperty(
                ServiceDefinition.class);

        if (service == null) {
            logger.error("service-def-retrieval-failed");
        }
        return service;
    }

    /**
     * Factory that creates {@link Value} from a {@link MetaDataValue}
     */
    private class MetaValueFactory implements ImmutableMetaDataValueVisitor {

        /**
         * The value that will be created
         */
        private Value value;

        /**
         * ExpressionFactory used to create values
         */
        private ExpressionFactory expressionFactory;

        /**
         * This is used if the
         */
        private List sequenceItems;

        /**
         * Creates a new MetaValueFactory
         *
         * @param factory the ExpressionFactory
         */
        public MetaValueFactory(ExpressionFactory factory) {

            expressionFactory = factory;
        }

        /**
         * Creates a {@link Value} from the given {@link MetaDataValue}
         *
         * @param mdValue the MetaDataValue
         * @return a Value instance
         */
        public Value createValue(MetaDataValue mdValue) {
            sequenceItems = null;
            value = null;
            ((ImmutableMetaDataValueVisitee) mdValue).accept(this);
            return value;
        }

        // javadoc inhertied
        public void visit(ImmutableBooleanValue immutableBooleanValue) {
            BooleanValue boolValue = expressionFactory.createBooleanValue(
                    immutableBooleanValue.getValueAsBoolean().booleanValue());

            if (sequenceItems == null) {
                // not creating a sequence of items set the value to be
                // the bool
                value = boolValue;
            } else {
                sequenceItems.add(boolValue);
            }
        }

        // javadoc inhertied
        public void visit(ImmutableListValue immutableListValue) {
            if (sequenceItems != null) {
                // throw an exception as we should cannot create a Sequence
                // of sqequences.
                throw new IllegalStateException(
                        "Cannot create a Sequence of Sequences");
            }

            // convert to a java list so that we can convert to a sequence
            // of items
            List values = immutableListValue.getContentsAsList();
            // create the list that will store the items that will form the
            // sequence
            sequenceItems = new ArrayList(values.size());


            for (Iterator i = values.iterator(); i.hasNext();) {
                // visit the values in the ImmutableListValue to build up the
                // sequence
                ((ImmutableMetaDataValueVisitee) i.next()).accept(this);
            }

            // create a sequence of Items
            value = expressionFactory.createSequence(
                    (Item[]) sequenceItems.toArray(
                            new Item[sequenceItems.size()]));
        }

        // javadoc inhertied
        public void visit(ImmutableNumberValue immutableNumberValue) {
            DoubleValue doubleValue =
                    expressionFactory.createDoubleValue(
                        immutableNumberValue.getValueAsNumber().doubleValue());
            if (sequenceItems == null) {
                // not creating a sequence of items set the value to be
                // the double
                value = doubleValue;
            } else {
                // this double is an item in a sequence
                sequenceItems.add(doubleValue);
            }
        }

        // javadoc inhertied
        public void visit(ImmutableQuantityValue immutableQuantityValue) {
            throw new IllegalArgumentException(
                    "Quantity values are not supported in service " +
                    "characteristics");
        }

        // javadoc inhertied
        public void visit(ImmutableSetValue immutableSetValue) {
            if (sequenceItems != null) {
                // throw an exception as we should cannot create a Sequence
                // of sequences.
                throw new IllegalStateException(
                        "Cannot create a Sequence of Sequences");
            }
            Collection values = immutableSetValue.getContentsAsCollection();
            // create the list that will temporarily hold the items of the
            // sequence
            sequenceItems = new ArrayList(values.size());
            for (Iterator i = values.iterator(); i.hasNext();) {
                ((ImmutableMetaDataValueVisitee) i.next()).accept(this);
            }
            // create a sequence of Items
            value = expressionFactory.createSequence(
                    (Item[]) sequenceItems.toArray(
                            new Item[sequenceItems.size()]));
        }

        // javadoc inhertied
        public void visit(ImmutableStructureValue immutableStructureValue) {
            if (sequenceItems != null) {
                // throw an exception as we should create a Sequence
                // of sequences.
                throw new IllegalStateException(
                        "Cannot create a Sequence of Sequences");
            }
            Map values = immutableStructureValue.getFieldValuesAsMap();
            // create the list that will temporarily hold the items of the
            // sequence
            sequenceItems = new ArrayList(values.size());
            for (Iterator i = values.values().iterator(); i.hasNext();) {
                ((ImmutableMetaDataValueVisitee) i.next()).accept(this);
            }
            // create a sequence of Items
            value = expressionFactory.createSequence(
                    (Item[]) sequenceItems.toArray(
                            new Item[sequenceItems.size()]));
        }

        // javadoc inhertied
        public void visit(ImmutableStringValue immutableStringValue) {
            StringValue strValue = expressionFactory.createStringValue(
                    immutableStringValue.getAsString());
            if (sequenceItems == null) {
                // not creating a sequence of items set the value to be
                // the string
                value = strValue;
            } else {
                sequenceItems.add(strValue);
            }
        }

        // javadoc inhertied
        public void visit(ImmutableUnitValue immutableUnitValue) {
            throw new IllegalArgumentException(
                    "Unit values are not supported in service characteristics");

        }

        // javadoc inhertied
        public void visit(ImmutableChoiceValue immutableChoiceValue) {
            throw new IllegalArgumentException(
                    "Choice values are not supported in service characteristics");

        }
    }
}
