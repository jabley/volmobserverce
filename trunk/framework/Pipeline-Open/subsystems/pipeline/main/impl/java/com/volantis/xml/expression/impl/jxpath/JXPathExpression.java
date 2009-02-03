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
package com.volantis.xml.expression.impl.jxpath;

import com.volantis.shared.throwable.ExtendedRuntimeException;
import com.volantis.xml.expression.Expression;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionException;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.Value;
import com.volantis.xml.expression.ValueConversionException;
import com.volantis.xml.expression.atomic.BooleanValue;
import com.volantis.xml.expression.atomic.StringValue;
import com.volantis.xml.expression.atomic.numeric.DoubleValue;
import com.volantis.xml.expression.atomic.numeric.IntValue;
import com.volantis.xml.expression.sequence.Item;
import com.volantis.xml.expression.sequence.Sequence;
import our.apache.commons.jxpath.CompiledExpression;
import our.apache.commons.jxpath.JXPathContext;
import our.apache.commons.jxpath.JXPathException;
import our.apache.commons.jxpath.NodeSet;
import our.apache.commons.jxpath.Pointer;
import our.apache.commons.jxpath.ri.EvalContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter class that adapts a JXPath CompiledExpression to an instance of
 * the Expression class.
 */
public class JXPathExpression implements Expression {
    /**
     * The factory by which this expression was created.
     */
    protected ExpressionFactory factory;

    /**
     * The Underlying JXPath expression
     */
    private CompiledExpression compiledExpression;

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param factory            the factory by which this expression was
     *                           created
     * @param compiledExpression the underlying object that is being adapted
     */
    public JXPathExpression(ExpressionFactory factory,
                               CompiledExpression compiledExpression) {
        this.factory = factory;
        this.compiledExpression = compiledExpression;
    }

    // javadoc inherited
    public Value evaluate(ExpressionContext context)
            throws ExpressionException {
        // Need to cast the ExpressionContext to the type that we expect
        JXPathExpressionContext jxpExpressionContext =
                (JXPathExpressionContext)context;

        JXPathContext jxpContext = jxpExpressionContext.getJXPathContext();

        Object result = null;

        try {
            result = compiledExpression.getValue(jxpContext);
        } catch (JXPathException e) {
            throw new ExpressionException(e);
        } catch (ExtendedRuntimeException e) {
            // JXPath does not support the propogation of checked exceptions.
            // Therefore, we use our own ExtendedRuntimeException and propogate
            // that or its cause upwards.
            Throwable cause = e.getCause();

            if (cause instanceof ExpressionException) {
                throw (ExpressionException)cause;
            } else {
                throw e;
            }
        }

        // Ensure that the result of the evaluation is appropriate and
        // of a supported type
        return asValue(factory, result);
    }

    /**
     * Supporting method used to convert an object into a Value. Those types of
     * object that can be converted into a Value implementation instance are
     * converted by this method. If the type of the object is unsupported, an
     * exception will be thrown.
     *
     * @param factory the factory to use in creating any needed Value
     *                implementations
     * @param object  the object to be converted
     * @return the object as a Value
     * @throws ExpressionException if the object cannot be converted
     */
    public static Value asValue(ExpressionFactory factory, Object object)
            throws ExpressionException {
        Value result = null;

        if (object == null) {
            // a result of null indicates a non existent path result which
            // should be represented by an empty sequence.
            result = Sequence.EMPTY;
        } else if (object instanceof Value) {
            // This handles the no-op conversion. This covers both Values and
            // Sequences since the latter are just Values anyway
            result = (Value)object;
        } else if (object instanceof Item[]) {
            result = factory.createSequence((Item[])object);
        } else if (object instanceof String[]) {
            result = createSequence(factory, (String[])object);
        } else if (object instanceof Integer[]) {
            result = createSequence(factory, (Integer[])object);
        } else if (object instanceof int[]) {
            result = createSequence(factory, (int[])object);
        } else if (object instanceof Double[]) {
            result = createSequence(factory, (Double[])object);
        } else if (object instanceof double[]) {
            result = createSequence(factory, (double[])object);
        } else if (object instanceof Boolean[]) {
            result = createSequence(factory, (Boolean[])object);
        } else if (object instanceof boolean[]) {
            result = createSequence(factory, (boolean[])object);
        } else if (object instanceof NodeSet) {
            // NodeSets appear in parameters to functions when the function
            // is being called with one or more variables
            result = convertToValue(factory, (NodeSet)object);
        } else if (object instanceof Pointer) {
            result = asValue(factory, ((Pointer)object).getValue());
        } else if (object instanceof EvalContext) {
            result = asValue(factory,
                             ((EvalContext)object).getSingleNodePointer());
        } else {
            result = convertToValue(factory, object);
        }

        return result;
    }

    /**
     * Converts instances of {@link Value} to instances of corresponding Java
     * classes.
     * 
     * @param value value to be converted
     * @return converted value
     */
    public static Object asJavaValue(Object value) {
        if (value instanceof IntValue) {
            return new Integer(((IntValue)value).asJavaInt());
        } else if (value instanceof DoubleValue) {
            return new Double(((DoubleValue)value).asJavaDouble());
        } else if (value instanceof StringValue) {
            return ((StringValue)value).asJavaString();
        } else if (value instanceof BooleanValue) {
            return Boolean.valueOf(((BooleanValue)value).asJavaBoolean());
        } else {
            return value;
        }
    }

    /**
     * Creates a sequence from the given array using the given factory.
     *
     * @param factory the factory used to create the sequence and values
     * @param array   the array of java type values to be wrapped into a
     *                sequence
     * @return a sequence containing Value instances wrapping the java type
     *         values given
     */
    private static Sequence createSequence(ExpressionFactory factory,
                                           boolean[] array) {
        Item[] items = new Item[array.length];

        for (int i = 0;
             i < array.length;
             i++) {
            items[i] = factory.createBooleanValue(array[i]);
        }

        return factory.createSequence(items);
    }

    /**
     * Creates a sequence from the given array using the given factory.
     *
     * @param factory the factory used to create the sequence and values
     * @param array   the array of java type values to be wrapped into a
     *                sequence
     * @return a sequence containing Value instances wrapping the java type
     *         values given
     */
    private static Sequence createSequence(ExpressionFactory factory,
                                           Boolean[] array) {
        Item[] items = new Item[array.length];

        for (int i = 0;
             i < array.length;
             i++) {
            items[i] = factory.createBooleanValue(array[i].
                                                  booleanValue());
        }

        return factory.createSequence(items);
    }

    /**
     * Creates a sequence from the given array using the given factory.
     *
     * @param factory the factory used to create the sequence and values
     * @param array   the array of java type values to be wrapped into a
     *                sequence
     * @return a sequence containing Value instances wrapping the java type
     *         values given
     */
    private static Sequence createSequence(ExpressionFactory factory,
                                           double[] array) {
        Item[] items = new Item[array.length];

        for (int i = 0;
             i < array.length;
             i++) {
            items[i] = factory.createDoubleValue(array[i]);
        }

        return factory.createSequence(items);
    }

    /**
     * Creates a sequence from the given array using the given factory.
     *
     * @param factory the factory used to create the sequence and values
     * @param array   the array of java type values to be wrapped into a
     *                sequence
     * @return a sequence containing Value instances wrapping the java type
     *         values given
     */
    private static Sequence createSequence(ExpressionFactory factory,
                                           Double[] array) {
        Item[] items = new Item[array.length];

        for (int i = 0;
             i < array.length;
             i++) {
            items[i] = factory.createDoubleValue(array[i].doubleValue());
        }

        return factory.createSequence(items);
    }

    /**
     * Creates a sequence from the given array using the given factory.
     *
     * @param factory the factory used to create the sequence and values
     * @param array   the array of java type values to be wrapped into a
     *                sequence
     * @return a sequence containing Value instances wrapping the java type
     *         values given
     */
    private static Sequence createSequence(ExpressionFactory factory,
                                           int[] array) {
        Item[] items = new Item[array.length];

        for (int i = 0;
             i < array.length;
             i++) {
            items[i] = factory.createIntValue(array[i]);
        }

        return factory.createSequence(items);
    }

    /**
     * Creates a sequence from the given array using the given factory.
     *
     * @param factory the factory used to create the sequence and values
     * @param array   the array of java type values to be wrapped into a
     *                sequence
     * @return a sequence containing Value instances wrapping the java type
     *         values given
     */
    private static Sequence createSequence(ExpressionFactory factory,
                                           Integer[] array) {
        Item[] items = new Item[array.length];

        for (int i = 0;
             i < array.length;
             i++) {
            items[i] = factory.createIntValue(array[i].intValue());
        }

        return factory.createSequence(items);
    }

    /**
     * Creates a sequence from the given array using the given factory.
     *
     * @param factory the factory used to create the sequence and values
     * @param array   the array of java type values to be wrapped into a
     *                sequence
     * @return a sequence containing Value instances wrapping the java type
     *         values given
     */
    private static Sequence createSequence(ExpressionFactory factory,
                                           String[] array) {
        Item[] items = new Item[array.length];

        for (int i = 0;
             i < array.length;
             i++) {
            items[i] = factory.createStringValue(array[i]);
        }

        return factory.createSequence(items);
    }

    /**
     * The given node set is converted into a value, be it an atomic one or a
     * sequence of atomic ones.
     *
     * @param factory the factory to use in creating the Value wrapper(s)
     *                and/or sequence
     * @param nodeSet the node set to be converted
     * @return a value representing the node set value(s)
     * @throws ExpressionException if the node set contains an unrecognised or
     *                             unsupported type
     */
    private static Value convertToValue(ExpressionFactory factory,
                                        NodeSet nodeSet)
            throws ExpressionException {
        Value result = null;

        List values = nodeSet.getValues();

        // Provide some optimizations to avoid unnecessary garbage generation
        if (values.size() == 0) {
            // An empty set can be represented by an empty sequence
            result = Sequence.EMPTY;
        } else if (values.size() == 1) {
            // A set with one item can be represented by a single value
            result = asValue(factory, values.get(0));
        } else {
            // Convert the multi-value set into a sequence of atomic
            // items
            int size = values.size();
            int i;
            int j;
            ArrayList resultValues = new ArrayList(size);
            Sequence sequence;
            Item[] items;

            for (i = 0;
                 i < size;
                 i++) {
                // Each value could feasibly be a multi-value sequence.
                // Atomic values can always provide a sequence containing just
                // themselves. Thus, it is easiest to get the sequence each
                // time and process its values later
                sequence = asValue(factory, values.get(i)).getSequence();

                // Sequences are not allowed to contain nested sequences, so
                // this must be avoided by "flattening" the sequence of
                // sequences into a single sequence of atomic values. Thus the
                // sequence (atomic) items are individually added to the
                // current resultValues array for later addition into the
                // resulting sequence
                //
                // Sequence indices are in the range [1..n] and not the
                // normal java range of [0..(n - 1)]
                for (j = 1;
                     j <= sequence.getLength();
                     j++) {
                    resultValues.add(sequence.getItem(j));
                }
            }

            // Generate the item array needed to create a sequence from the
            // resultValues
            items = new Item[resultValues.size()];

            // Populate the required Item array. Even though the resultValues
            // array strictly contains Value instances, the previous loop
            // ensured that each element in the array is actually an atomic
            // value. Atomic values are actually all Items. Hence this cast
            // should never fail
            for (i = 0;
                 i < items.length;
                 i++) {
                items[i] = (Item)resultValues.get(i);
            }

            // Create the sequence from these items
            result = factory.createSequence(items);
        }

        return result;
    }

    /**
     * The given object is tested to see if it is a java type that can be
     * converted to or wrappered by a {@link Value} implementation. If so, a
     * wrapping {@link Value} specialization instance is returned, otherwise an
     * exception is thrown.
     *
     * @param factory the factory to use in creating the Value wrapper
     * @param object  the object to be converted/wrappered
     * @return a {@link Value} specialization instance wrappering the given
     *         object
     * @throws ExpressionException if the object is of an unrecognised or
     *                             unsupported java type
     */
    private static Value convertToValue(ExpressionFactory factory,
                                        Object object)
            throws ExpressionException {
        Value result = null;

        if (object instanceof Boolean) {
            result = factory.
                    createBooleanValue(((Boolean)object).booleanValue());
        } else if (object instanceof String) {
            result = factory.
                    createStringValue((String)object);
        } else if (object instanceof Integer) {
            result = factory.
                    createIntValue(((Integer)object).intValue());
        } else if (object instanceof Double) {
            result = factory.
                    createDoubleValue(((Double)object).doubleValue());
        } else {
            throw new ValueConversionException(
                    "Expression evaluates to an unrecognized type (" +
                    object.getClass().getName() + ")");
        }

        return result;
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 31-Aug-04	828/3	pcameron	VBM:2004072012 Fixed layout:getPaneInstance with a parameter and nested contexts

 16-Aug-04	828/1	pcameron	VBM:2004072012 Fixed layout:getPaneInstance with a parameter and nested contexts

 16-Aug-04	826/1	pcameron	VBM:2004072012 Fixed layout:getPaneInstance with a parameter and nested contexts

 08-Jul-04	773/1	adrianj	VBM:2003080416 Fixed propagation of ExpressionException during function evaluation

 27-Oct-03	433/1	doug	VBM:2003102002 Added several new comparison operators

 22-Aug-03	386/1	doug	VBM:2003080702 Fixed issue with expression predicates

 31-Jul-03	222/3	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 25-Jun-03	102/1	sumit	VBM:2003061906 request:getParameter XPath function support

 18-Jun-03	100/1	sumit	VBM:2003061602 Converted all references to org.apache to our.apache

 06-Jun-03	26/1	doug	VBM:2003051402 Expression Processing checkin

 ===========================================================================
*/
