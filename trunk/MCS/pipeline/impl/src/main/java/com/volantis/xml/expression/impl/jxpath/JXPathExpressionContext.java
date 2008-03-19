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

import com.volantis.shared.environment.EnvironmentInteractionTracker;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.ExpressionScope;
import com.volantis.xml.expression.Function;
import com.volantis.xml.expression.impl.SimpleExpressionContext;
import com.volantis.xml.expression.functions.CurrentDateFunction;
import com.volantis.xml.expression.functions.CurrentDateTimeFunction;
import com.volantis.xml.expression.functions.CurrentTimeFunction;
import com.volantis.xml.expression.functions.DateFunction;
import com.volantis.xml.expression.functions.DateTimeFunction;
import com.volantis.xml.expression.functions.DurationFunction;
import com.volantis.xml.expression.functions.EmptyFunction;
import com.volantis.xml.expression.functions.ExceptFunction;
import com.volantis.xml.expression.functions.ExistsFunction;
import com.volantis.xml.expression.functions.IndexOfFunction;
import com.volantis.xml.expression.functions.IntersectFunction;
import com.volantis.xml.expression.functions.PositionFunction;
import com.volantis.xml.expression.functions.RoundFunction;
import com.volantis.xml.expression.functions.StringFunction;
import com.volantis.xml.expression.functions.SubsequenceFunction;
import com.volantis.xml.expression.functions.TimeFunction;
import com.volantis.xml.expression.functions.TokenizeFunction;
import com.volantis.xml.expression.functions.UnionFunction;
import com.volantis.xml.expression.impl.jxpath.functions.JXPathExpressionFunctionAdapter;
import com.volantis.xml.expression.impl.jxpath.functions.JXPathFunctions;
import com.volantis.xml.namespace.ImmutableExpandedName;
import com.volantis.xml.namespace.NamespacePrefixTracker;
import our.apache.commons.jxpath.JXPathContext;

/**
 * JXPath implementation of the ExpressionContext interface.
 * 
 * <p>The functions registered in context should use the standard XPath
 * function namespace but JxPath has no support for setting a default
 * function namespace so for now we pretend by having no namespace.</p>
 */
public class JXPathExpressionContext
        extends SimpleExpressionContext {

    /**
     * The namespace uri for XML Schema
     */
    private static final String XML_SCHEMA_NAMESPACE_URI =
            "http://www.w3.org/2001/XMLSchema";

    /**
     * The union standard XPath function instance.
     */
    private static final Function UNION_FUNCTION = new UnionFunction();

    /**
     * The union standard XPath function name.
     */
    private static final ImmutableExpandedName UNION_NAME =
            new ImmutableExpandedName("", "union");

    /**
     * The except standard XPath function instance.
     */
    private static final Function EXCEPT_FUNCTION = new ExceptFunction();

    /**
     * The except standard XPath function name.
     */
    private static final ImmutableExpandedName EXCEPT_NAME =
            new ImmutableExpandedName("", "except");

    /**
     * The intersect standard XPath function instance.
     */
    private static final Function INTERSECT_FUNCTION = new IntersectFunction();

    /**
     * The intersect standard XPath function name.
     */
    private static final ImmutableExpandedName INTERSECT_NAME =
            new ImmutableExpandedName("", "intersect");
    
    /**
     * The string standard XPath function instance.
     */
    private static final Function STRING_FUNCTION = new StringFunction();
    
    /**
     * The string standard XPath function name.
     */
    private static final ImmutableExpandedName STRING_NAME =
            new ImmutableExpandedName("", "string");
    
    /**
     * The current-dateTime standard XPath function instance.
     */
    private static final Function CURRENT_DATE_TIME_FUNCTION =
            new CurrentDateTimeFunction();
    
    /**
     * The current-dateTime standard XPath function name.
     */
    private static final ImmutableExpandedName CURRENT_DATE_TIME_NAME = 
            new ImmutableExpandedName("", "current-dateTime");

    /**
     * The current-date standard XPath function instance.
     */
    private static final Function CURRENT_DATE_FUNCTION =
            new CurrentDateFunction();
    
    /**
     * The current-date standard XPath function name.
     */
    private static final ImmutableExpandedName CURRENT_DATE_NAME = 
            new ImmutableExpandedName("", "current-date");

    /**
     * The current-time standard XPath function instance.
     */
    private static final Function CURRENT_TIME_FUNCTION =
            new CurrentTimeFunction();
    
    /**
     * The current-time standard XPath function name.
     */
    private static final ImmutableExpandedName CURRENT_TIME_NAME = 
            new ImmutableExpandedName("", "current-time");

    /**
     * The round standard XPath function instance.
     */
    private static final Function ROUND_FUNCTION = new RoundFunction();
    
    /**
     * The round standard XPath function name.
     */
    private static final ImmutableExpandedName ROUND_NAME =
            new ImmutableExpandedName("", "round");

    /**
     * The empty standard XPath function name.
     */
    private static final ImmutableExpandedName EMPTY_NAME =
            new ImmutableExpandedName("", "empty");
    
    /**
     * The empty standard XPath function instance.
     */
    private static final Function EMPTY_FUNCTION = new EmptyFunction();
    
    /**
     * The exists standard XPath function name.
     */
    private static final ImmutableExpandedName EXISTS_NAME =
            new ImmutableExpandedName("", "exists");
    
    /**
     * The exists standard XPath function instance.
     */
    private static final Function EXISTS_FUNCTION = new ExistsFunction();
    
    /**
     * The subsequence standard XPath function name.
     */
    private static final ImmutableExpandedName SUBSEQUENCE_NAME =
            new ImmutableExpandedName("", "subsequence");
    
    /**
     * The subsequence standard XPath function instance.
     */
    private static final Function SUBSEQUENCE_FUNCTION =
            new SubsequenceFunction();
    
    /**
     * The position standard XPath function name.
     */
    private static final ImmutableExpandedName POSITION_NAME =
            new ImmutableExpandedName("", "position");
    
    /**
     * The position standard XPath function instance.
     */
    private static final Function POSITION_FUNCTION =
            new PositionFunction();

    /**
     * The dateTime standard XPath function name.
     */
    private static final ImmutableExpandedName DATE_TIME_NAME =
            new ImmutableExpandedName(XML_SCHEMA_NAMESPACE_URI, "dateTime");
    
    /**
     * The dateTime standard XPath function instance.
     */
    private static final Function DATE_TIME_FUNCTION = new DateTimeFunction();

    /**
     * The date standard XPath function name.
     */
    private static final ImmutableExpandedName DATE_NAME =
            new ImmutableExpandedName(XML_SCHEMA_NAMESPACE_URI, "date");

    /**
     * The date standard XPath function instance.
     */
    private static final Function DATE_FUNCTION = new DateFunction();

    /**
     * The time standard XPath function name.
     */
    private static final ImmutableExpandedName TIME_NAME =
            new ImmutableExpandedName(XML_SCHEMA_NAMESPACE_URI, "time");
    
    /**
     * The time standard XPath function instance.
     */
    private static final Function TIME_FUNCTION = new TimeFunction();

    /**
     * The duration standard XPath function name.
     */
    private static final ImmutableExpandedName DURATION_NAME =
            new ImmutableExpandedName(XML_SCHEMA_NAMESPACE_URI, "duration");
    
    /**
     * The duration standard XPath function instance.
     */
    private static final Function DURATION_FUNCTION = new DurationFunction();
    
    /**
     * The index-of standard XPath function name.
     */
    private static final ImmutableExpandedName INDEXOF_NAME =
            new ImmutableExpandedName("", "index-of");
    
    /**
     * The index-of standard XPath function instance.
     */
    private static final Function INDEXOF_FUNCTION =
            new IndexOfFunction();
    
    /**
     * The tokenize standard XPath function name.
     */
    private static final ImmutableExpandedName TOKENIZE_NAME =
            new ImmutableExpandedName("", "tokenize");
    
    /**
     * The tokenize standard XPath function instance.
     */
    private static final Function TOKENIZE_FUNCTION =
            new TokenizeFunction();

    /**
     * Reference to the underlying JXPathContext instance that this
     * class wraps
     */
    private JXPathContext expressionContext;

    /**
     * Reference to the JXPathQualifiedVariables instance that
     * manages variable declarations.
     */
    private JXPathQualifiedVariables variables;

    /**
     * List of functions maintained by this context
     */
    private JXPathFunctions functions;

    /**
     * Initializes the new instance using the given parameters.
     *
     * @param factory                       the factory by which this context
     *                                      was created
     * @param environmentInteractionTracker the environment interaction tracker
     *                                      to be used and published by this
     *                                      context
     * @param namespacePrefixTracker        the namespace prefix tracker to be
     *                                      used and published by this context
     */
    public JXPathExpressionContext(
            ExpressionFactory factory,
            EnvironmentInteractionTracker environmentInteractionTracker,
            NamespacePrefixTracker namespacePrefixTracker) {
        super(factory, environmentInteractionTracker, namespacePrefixTracker);

        variables = new JXPathQualifiedVariables(factory,
                namespacePrefixTracker);
        functions = new JXPathFunctions(getNamespacePrefixTracker());

        expressionContext = JXPathContext.newContext(null);
        // switching on lenient mode results in null being returned for
        // non existent paths rather than an exception being thrown.
        expressionContext.setLenient(true);
        expressionContext.setVariables(variables);
        expressionContext.setFunctions(functions);

        // Register standard XPath functions.
        registerFunction(UNION_NAME, UNION_FUNCTION);
        registerFunction(EXCEPT_NAME, EXCEPT_FUNCTION);
        registerFunction(INTERSECT_NAME, INTERSECT_FUNCTION);
        registerFunction(STRING_NAME, STRING_FUNCTION);
        registerFunction(CURRENT_DATE_NAME, CURRENT_DATE_FUNCTION);
        registerFunction(CURRENT_TIME_NAME, CURRENT_TIME_FUNCTION);
        registerFunction(CURRENT_DATE_TIME_NAME, CURRENT_DATE_TIME_FUNCTION);
        registerFunction(ROUND_NAME, ROUND_FUNCTION);
        registerFunction(DATE_TIME_NAME, DATE_TIME_FUNCTION);
        registerFunction(DATE_NAME, DATE_FUNCTION);
        registerFunction(DURATION_NAME, DURATION_FUNCTION);
        registerFunction(EMPTY_NAME, EMPTY_FUNCTION);
        registerFunction(EXISTS_NAME, EXISTS_FUNCTION);
        registerFunction(POSITION_NAME, POSITION_FUNCTION);
        registerFunction(SUBSEQUENCE_NAME, SUBSEQUENCE_FUNCTION);
        registerFunction(TIME_NAME, TIME_FUNCTION);
        registerFunction(INDEXOF_NAME, INDEXOF_FUNCTION);
        registerFunction(TOKENIZE_NAME, TOKENIZE_FUNCTION);
    }

    // javadoc inherited
    public ExpressionScope getCurrentScope() {
        return variables.getCurrentScope();
    }

    // javadoc inherited
    public void pushStackFrame() {
        variables.pushStackFrame();
    }

    // javadoc inherited
    public void popStackFrame() {
        variables.popStackFrame();
    }

    // javadoc inherited
    public void pushBlockScope() {
        variables.pushBlockScope();
    }

    // javadoc inherited
    public void popBlockScope() {
        variables.popBlockScope();
    }

    /**
     * Return the underlying JXPathContext that this class wraps
     *
     * @return the JXPathContext instance.
     */
    public JXPathContext getJXPathContext() {
        return expressionContext;
    }

    // javadoc inherited
    public void registerFunction(
            ImmutableExpandedName functionName,
            Function function) {
        JXPathExpressionFunctionAdapter adapter =
                new JXPathExpressionFunctionAdapter(factory,
                        function,
                        this);
        functions.addFunction(functionName, adapter);
    }

    // javadoc inherited from RecoverableTransaction interface.
    public void startTransaction() {
        super.startTransaction();
        functions.startTransaction();
        variables.startTransaction();
    }

    // javadoc inherited from RecoverableTransaction interface.
    public void commitTransaction() {
        super.commitTransaction();
        functions.commitTransaction();
        variables.commitTransaction();
    }

    // javadoc inherited from RecoverableTransaction interface.
    public void rollbackTransaction() {
        super.rollbackTransaction();
        functions.rollbackTransaction();
        variables.rollbackTransaction();
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 20-Oct-04	5438/3	philws	VBM:2004082706 Reformat production pipeline code

 20-Oct-04	5438/1	philws	VBM:2004082706 Transfer Pipeline source into MCS

 22-Aug-03	386/1	doug	VBM:2003080702 Fixed issue with expression predicates

 10-Aug-03	264/1	adrian	VBM:2003072807 added error recovery support to pipeline context and associated object hierarchy

 31-Jul-03	222/7	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 31-Jul-03	222/5	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 31-Jul-03	222/3	philws	VBM:2003071802 New pipeline API and implementation of the equals and not equals expression feature

 27-Jun-03	102/7	sumit	VBM:2003061906 Defunct ServletRequestFunction

 25-Jun-03	102/5	sumit	VBM:2003061906 request:getParameter XPath function support

 18-Jun-03	100/1	sumit	VBM:2003061602 Converted all references to org.apache to our.apache

 06-Jun-03	26/1	doug	VBM:2003051402 Expression Processing checkin

 ===========================================================================
*/
