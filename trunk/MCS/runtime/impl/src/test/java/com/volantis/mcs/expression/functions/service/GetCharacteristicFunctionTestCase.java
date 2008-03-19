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

import com.volantis.mcs.expression.functions.AbstractFunctionTestAbstract;
import com.volantis.mcs.service.ServiceDefinition;
import com.volantis.mcs.service.ServiceDefinitionMock;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.Expression;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.sequence.Sequence;
import com.volantis.xml.expression.sequence.SimpleSequence;
import com.volantis.xml.expression.sequence.Item;
import com.volantis.xml.expression.atomic.StringValue;
import com.volantis.xml.expression.atomic.SimpleStringValue;
import com.volantis.xml.expression.atomic.BooleanValue;
import com.volantis.xml.expression.atomic.numeric.DoubleValue;
import com.volantis.xml.expression.functions.AbstractFunction;
import com.volantis.shared.metadata.value.MetaDataValueFactory;
import com.volantis.shared.metadata.value.NumberValue;
import com.volantis.shared.metadata.value.UnitValue;
import com.volantis.shared.metadata.value.immutable.ImmutableMetaDataValue;
import com.volantis.shared.metadata.value.mutable.MutableStringValue;
import com.volantis.shared.metadata.value.mutable.MutableBooleanValue;
import com.volantis.shared.metadata.value.mutable.MutableNumberValue;
import com.volantis.shared.metadata.value.mutable.MutableQuantityValue;
import com.volantis.shared.metadata.value.mutable.MutableListValue;
import com.volantis.shared.metadata.value.mutable.MutableSetValue;
import com.volantis.shared.metadata.value.mutable.MutableStructureValue;
import com.volantis.shared.metadata.MetaDataFactory;

import java.util.List;
import java.util.Set;
import java.util.Map;

/**
 *
 */
public class GetCharacteristicFunctionTestCase
        extends AbstractFunctionTestAbstract {

    static String FUNCTION_NAME = "getServiceCharacteristic";

    static String FUNCTION_QNAME = "service:getServiceCharacteristic";

      /**
     * Factory for creating meta data values
     */
    private static final MetaDataValueFactory META_DATA_VALUE_FACTORY =
            MetaDataFactory.getDefaultInstance().getValueFactory();

    private ServiceDefinitionMock serviceDefMock;

    // javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();
        serviceDefMock = new ServiceDefinitionMock("serviceDefMock",
                                                   expectations);

        // register the mock
        expressionContext.setProperty(ServiceDefinition.class,
                                      serviceDefMock,
                                      false);

    }

    /**
     * Test the getCharacteristic() function with a characteristic that has
     * a value
     * @throws Exception if an error occurs
     */
    public void testWhenCharacteristicIsNoAvailable() throws Exception {
        // add a policy/policy value pair to the request
        String charateristic = "null";
        serviceDefMock.expects.getCharacteristic(charateristic).returns(null);

        // invoke the function
        Expression expression = parser.parse(getFunctionQName() +
                                             "('" + charateristic + "')");

        // evalute the expression
        Value result = expression.evaluate(expressionContext);

        // Value returned should be the EmptySequence
        assertTrue("return value should be a Sequence instance",
                   result instanceof Sequence);

        // check the StringValues string
        assertEquals("Unexpected Empty Sequence",
                     Sequence.EMPTY,
                     result);

    }

     /**
     * Test the getCharacteristic() function with a characteristic that has
     * a value
     * @throws Exception if an error occurs
     */
    public void testWhenCharacteristicIsStr() throws Exception {
        // add a policy/policy value pair to the request
        String charateristic = "string";
        MutableStringValue value = META_DATA_VALUE_FACTORY.createStringValue();
        value.setValue("a_string");
        ImmutableMetaDataValue stringVal = (ImmutableMetaDataValue)
                                              value.createImmutable();

        serviceDefMock.expects.getCharacteristic(charateristic).returns(stringVal);

        // invoke the function
        Expression expression = parser.parse(getFunctionQName() +
                                             "('" + charateristic + "')");

        // evalute the expression
        Value result = expression.evaluate(expressionContext);

        // Value returned should be a StringValue
        assertTrue("return value should be a StringValue instance",
                   result instanceof StringValue);

        // check the StringValues string
        assertEquals("Unexpected StringValue value",
                     "a_string",
                     result.stringValue().asJavaString());
    }

     /**
     * Test the getCharacteristic() function with a characteristic that has
     * a value
     * @throws Exception if an error occurs
     */
    public void testWhenCharacteristicIsBool() throws Exception {
        // add a policy/policy value pair to the request
        String charateristic = "bool";
        MutableBooleanValue value = META_DATA_VALUE_FACTORY.createBooleanValue();
        value.setValue(Boolean.FALSE);
        ImmutableMetaDataValue boolVal = (ImmutableMetaDataValue)
                                              value.createImmutable();

        serviceDefMock.expects.getCharacteristic(charateristic).returns(boolVal);

        // invoke the function
        Expression expression = parser.parse(getFunctionQName() +
                                             "('" + charateristic + "')");

        // evalute the expression
        Value result = expression.evaluate(expressionContext);

        // Value returned should be a StringValue
        assertTrue("return value should be a BooleanValue instance",
                   result instanceof BooleanValue);

        // check the StringValues string
        assertEquals("Unexpected BooleanValue value",
                     false,
                     ((BooleanValue)result).asJavaBoolean());
    }

    /**
     * Test the getCharacteristic() function with a characteristic that has
     * a value
     * @throws Exception if an error occurs
     */
    public void testWhenCharacteristicIsNumber() throws Exception {
        // add a policy/policy value pair to the request
        String charateristic = "num";
        MutableNumberValue value = META_DATA_VALUE_FACTORY.createNumberValue();
        value.setValue(new Double(11));
        ImmutableMetaDataValue numVal = (ImmutableMetaDataValue)
                                              value.createImmutable();

        serviceDefMock.expects.getCharacteristic(charateristic).returns(numVal);

        // invoke the function
        Expression expression = parser.parse(getFunctionQName() +
                                             "('" + charateristic + "')");

        // evalute the expression
        Value result = expression.evaluate(expressionContext);

        // Value returned should be a StringValue
        assertTrue("return value should be a DoubleValue instance",
                   result instanceof DoubleValue);

        // check the StringValues string
        assertEquals("Unexpected DoubleValue value",
                     0d,
                     11d,
                     ((DoubleValue)result).asJavaDouble());
    }

    /**
     * Test the getCharacteristic() function with a characteristic that has
     * a value
     * @throws Exception if an error occurs
     */
    public void testWhenCharacteristicIsList() throws Exception {
        // add a policy/policy value pair to the request
        String charateristic = "list";
        MutableListValue value = META_DATA_VALUE_FACTORY.createListValue();
        List list = value.getContentsAsMutableList();
        MutableStringValue strVal = META_DATA_VALUE_FACTORY.createStringValue();
        strVal.setValue("str");
        list.add(strVal.createImmutable());
        ImmutableMetaDataValue listVal = (ImmutableMetaDataValue)
                                              value.createImmutable();

        serviceDefMock.expects.getCharacteristic(charateristic).returns(listVal);

        // invoke the function
        Expression expression = parser.parse(getFunctionQName() +
                                             "('" + charateristic + "')");

        // evalute the expression
        Value result = expression.evaluate(expressionContext);

        // Value returned should be a StringValue
        assertTrue("return value should be a DoubleValue instance",
                   result instanceof Sequence);

        Sequence seq = (Sequence) result;

        // check the sequence is of lenght 1
        assertEquals("Unexpected sequence length",
                     1,
                     seq.getLength());

        Item item = seq.getItem(1);

        // Value returned should be a StringValue
        assertTrue("Item in list should be a StringValue instance",
                  item instanceof StringValue);

        StringValue str = (StringValue)item;
        // check the StringValues string
        assertEquals("Unexpected StringValue value",
                     "str",
                     str.asJavaString());
    }

    /**
     * Test the getCharacteristic() function with a characteristic that has
     * a value
     * @throws Exception if an error occurs
     */
    public void testWhenCharacteristicIsSet() throws Exception {
        // add a policy/policy value pair to the request
        String charateristic = "set";
        MutableSetValue value = META_DATA_VALUE_FACTORY.createSetValue();
        Set set = value.getContentsAsMutableSet();
        MutableStringValue strVal = META_DATA_VALUE_FACTORY.createStringValue();
        strVal.setValue("str");
        set.add(strVal.createImmutable());
        ImmutableMetaDataValue setVal = (ImmutableMetaDataValue)
                                              value.createImmutable();

        serviceDefMock.expects.getCharacteristic(charateristic).returns(setVal);

        // invoke the function
        Expression expression = parser.parse(getFunctionQName() +
                                             "('" + charateristic + "')");

        // evalute the expression
        Value result = expression.evaluate(expressionContext);

        // Value returned should be a StringValue
        assertTrue("return value should be a DoubleValue instance",
                   result instanceof Sequence);

        Sequence seq = (Sequence) result;

        // check the sequence is of lenght 1
        assertEquals("Unexpected sequence length",
                     1,
                     seq.getLength());

        Item item = seq.getItem(1);

        // Value returned should be a StringValue
        assertTrue("Item in list should be a StringValue instance",
                  item instanceof StringValue);

        StringValue str = (StringValue)item;
        // check the StringValues string
        assertEquals("Unexpected StringValue value",
                     "str",
                     str.asJavaString());
    }

    /**
     * Test the getCharacteristic() function with a characteristic that has
     * a value
     * @throws Exception if an error occurs
     */
    public void testWhenCharacteristicIsStruct() throws Exception {
        // add a policy/policy value pair to the request
        String charateristic = "struct";
        MutableStructureValue value =
                META_DATA_VALUE_FACTORY.createStructureValue();
        Map map = value.getFieldValuesAsMutableMap();
        MutableStringValue strVal = META_DATA_VALUE_FACTORY.createStringValue();
        strVal.setValue("str");
        map.put("key", strVal.createImmutable());
        ImmutableMetaDataValue structVal = (ImmutableMetaDataValue)
                                                 value.createImmutable();

        serviceDefMock.expects.getCharacteristic(charateristic).returns(structVal);

        // invoke the function
        Expression expression = parser.parse(getFunctionQName() +
                                             "('" + charateristic + "')");

        // evalute the expression
        Value result = expression.evaluate(expressionContext);

        // Value returned should be a StringValue
        assertTrue("return value should be a DoubleValue instance",
                   result instanceof Sequence);

        Sequence seq = (Sequence) result;

        // check the sequence is of lenght 1
        assertEquals("Unexpected sequence length",
                     1,
                     seq.getLength());

        Item item = seq.getItem(1);

        // Value returned should be a StringValue
        assertTrue("Item in list should be a StringValue instance",
                  item instanceof StringValue);

        StringValue str = (StringValue)item;
        // check the StringValues string
        assertEquals("Unexpected StringValue value",
                     "str",
                     str.asJavaString());
    }

    /**
     * Test the getCharacteristic() function with a characteristic that has
     * a value
     * @throws Exception if an error occurs
     */
    public void testWhenCharacteristicIsQuantity() throws Exception {
        // add a policy/policy value pair to the request
        String charateristic = "quantity";
        MutableQuantityValue value =
                META_DATA_VALUE_FACTORY.createQuantityValue();

        ImmutableMetaDataValue quantityVal = (ImmutableMetaDataValue)
                                              value.createImmutable();

        serviceDefMock.expects.getCharacteristic(charateristic).returns(quantityVal);

        // invoke the function
        Expression expression = parser.parse(getFunctionQName() +
                                             "('" + charateristic + "')");

        try {
            // evalute the expression
            Value result = expression.evaluate(expressionContext);
            fail("Do not expect QuantintyValues to be supported");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }


    // javadoc inherited
    protected AbstractFunction createTestableFunction(
            ExpressionFactory factory) {
        return new GetCharacteristicFunction();
    }

    // javadoc inherited
    protected String getFunctionQName() {
        return FUNCTION_QNAME;
    }

    // javadoc inherited
    protected String getURI() {
        return SERVICE_URI;
    }

    // javadoc inherited
    protected String getFunctionName() {
        return FUNCTION_NAME;
    }

}
