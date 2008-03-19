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

package com.volantis.mcs.expression;

import com.volantis.mcs.context.DevicePolicyAccessor;
import com.volantis.mcs.context.EnvironmentContext;
import com.volantis.mcs.context.MarinerRequestContext;
import com.volantis.mcs.expression.functions.device.DeviceFunctions;
import com.volantis.mcs.expression.functions.device.ExpressionDevicePolicyValueAccessor;
import com.volantis.mcs.expression.functions.diselect.DISelectFunctions;
import com.volantis.mcs.expression.functions.layout.LayoutFunctions;
import com.volantis.mcs.expression.functions.policy.PolicyFunctions;
import com.volantis.mcs.expression.functions.request.RequestFunctions;
import com.volantis.mcs.expression.functions.service.ServiceFunctions;
import com.volantis.mcs.runtime.Volantis;
import com.volantis.mcs.runtime.pipeline.PipelineInitialization;
import com.volantis.shared.environment.EnvironmentFactory;
import com.volantis.shared.environment.EnvironmentInteraction;
import com.volantis.shared.environment.EnvironmentInteractionTracker;
import com.volantis.xml.expression.ExpressionContext;
import com.volantis.xml.expression.ExpressionFactory;
import com.volantis.xml.expression.Function;
import com.volantis.xml.namespace.ImmutableExpandedName;
import com.volantis.xml.namespace.NamespaceFactory;
import com.volantis.xml.namespace.NamespacePrefixTracker;
import com.volantis.xml.pipeline.sax.XMLPipelineFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Utility class with methods to setup an ExpressionContext and
 * ExpressionFunctions.
 */

public class ExpressionSupport {

    /**
     * Map of externally registered functions.
     */
    private static List externalFunctions = new ArrayList();

    /**
     * Map of externally registered NameSpaces.
     */
    private static List externalNameSpaces = new ArrayList();

    /**
     * Namespace Registrations
     */
    static {
        PipelineInitialization.defineNamespaceURIs();
    }

    /**
     * Function Registrations
     */

    /**
     * Creates an ExpressionContext for use in the JSP page. Registers any
     * functions and sets up any properties that are required by these
     * functions. Effectively allows the expressionContext variable to be
     * available for scripting
     * @param context MarinerRequestContext
     * @param volantisBean
     * @param devicePolicyValueAccessor

     */
    public static void createExpressionEnv(
            MarinerRequestContext context,
            Volantis volantisBean,
            EnvironmentContext environmentContext,
            final DevicePolicyAccessor devicePolicyValueAccessor) {

        PipelineInitialization pipelineInitialization =
                volantisBean.getPipelineInitialization();
        XMLPipelineFactory xmlFactory =
                pipelineInitialization.getPipelineFactory();

        ExpressionFactory factory =
                xmlFactory.getExpressionFactory();

        // obtain the default EnvironmentFactory
        EnvironmentFactory environmentFactory =
                EnvironmentFactory.getDefaultInstance();

        // factor an EnvironmentInteractionTracker
        EnvironmentInteractionTracker envTracker =
                environmentFactory.createInteractionTracker();
        
        // obtain the rootEnvironmentInteraction and push onto the tracker
        EnvironmentInteraction envInteraction = 
        		environmentContext.createRootEnvironmentInteraction();
        envTracker.pushEnvironmentInteraction(envInteraction);

        environmentContext.pushEnvironmentInteraction(envTracker);

        // See if an expression context already exists or not; an expression
        // context is shared throughout the lifetime of the request but is
        // lazily instantiated
        ExpressionContext expressionContext =
                MCSExpressionHelper.getExpressionContext(context);

        if (expressionContext == null) {
            // create the expression context, register the functions that are
            // needed and store this expression context away in the page
            // context
            expressionContext =
                    factory.createExpressionContext(
                            envTracker,
                            NamespaceFactory.getDefaultInstance().
                    createPrefixTracker());

            registerFunctions(expressionContext);

            // Store the ExpressionContext in the EnvironmentContext. This
            // allows the expression context to be shared across all
            // sub-requests of a request.

            // The expression context needs to set this property so that expressions
            // work when rendered at runtime.
            expressionContext.setProperty(MarinerRequestContext.class,
                                          context,
                                          false);

            // Allow XPath expressions access to the device
            registerDevicePolicyValueAccessor(expressionContext, devicePolicyValueAccessor);

            // There is always an environment context associated with a request in
            // a properly functioning system!
            // This method was created to aid code readability.
            environmentContext.setExpressionContext(expressionContext);
        }
    }

    /**
     * Creates a {@link DevicePolicyValueAccessor} and registers
     * it with the supplied {@link ExpressionContext}.
     *
     * @param expressionContext the context in which the created
     * {@link DevicePolicyValueAccessor} will be registered.
     *@param devicePolicyValueAccessor
     */
    public static void registerDevicePolicyValueAccessor(
            ExpressionContext expressionContext,
            final DevicePolicyAccessor devicePolicyValueAccessor) {

        ExpressionDevicePolicyValueAccessor accessor =
                new ExpressionDevicePolicyValueAccessor(
                        expressionContext,
                        devicePolicyValueAccessor);

        // Allow XPath expressions access to the device
        expressionContext.setProperty(
                DevicePolicyValueAccessor.class,
                accessor,
                false);
    }

    /**
     * Register any extension functions that are defined in this context and
     * ensure that their default namespace prefixes are also registered.
     *
     * <p>The namespaces required are defined in
     * {@link com.volantis.mcs.runtime.pipeline.PipelineInitialization}</p>
     *
     * @param context the expression context within which the functions should
     *                be made available
     */
    public static void registerFunctions(ExpressionContext context) {
        // Register the pre-defined namespace prefix mappings. Note that the
        // Namespace.literal() method is used to retrieve the MCS-specific
        // extended Namespace instances otherwise not accessible. These are
        // created in PipelineInitialization.
        final NamespacePrefixTracker namespacePrefixTracker =
            context.getNamespacePrefixTracker();

        // Add external NameSpaces
        Iterator externalNameSpaceIterator = externalNameSpaces.iterator();
        while ( externalNameSpaceIterator.hasNext() ) {
            ExternalNameSpacePrefixDefinition 
                externalNameSpacePrefixDefinition =
                    (ExternalNameSpacePrefixDefinition)
                        externalNameSpaceIterator.next();
            
            namespacePrefixTracker.
            startPrefixMapping(externalNameSpacePrefixDefinition.getPrefix(),
                                externalNameSpacePrefixDefinition.getUri());    
        }
        
        // register the various MCS functions
        RequestFunctions.FUNCTION_TABLE.registerFunctions(context);
        DeviceFunctions.FUNCTION_TABLE.registerFunctions(context);
        DISelectFunctions.FUNCTION_TABLE.registerFunctions(context);
        LayoutFunctions.FUNCTION_TABLE.registerFunctions(context);
        PolicyFunctions.FUNCTION_TABLE.registerFunctions(context);
        ServiceFunctions.FUNCTION_TABLE.registerFunctions(context);

        // Add external Functions
        Iterator externalFunctionIterator = externalFunctions.iterator();
        while ( externalFunctionIterator.hasNext() ) {
            ExternalFunctionDefinition externalFunctionDefinition =
                (ExternalFunctionDefinition)externalFunctionIterator.next();
            
            context.registerFunction(
                externalFunctionDefinition.getImmutableExpandedName(),
                externalFunctionDefinition.getFunction());    
        }
    }

    public static void registerExternalNameSpacePrefix(String prefix,
                                                        String uri) {
          externalNameSpaces.add(new ExternalNameSpacePrefixDefinition(prefix,
                                                                        uri));                                                      
    }
    
    /**
     * This method enables an external entity to register functions into the
     * ExpressionContext passed to RegisterFunctions.
     * @param immutableExpandedName The ImmutableExpandedName for the Function.
     * @param function The class implementing the function.
     */
    public static void registerExternalFunction(
        ImmutableExpandedName immutableExpandedName, Function function) {
            
            externalFunctions.add(
                new ExternalFunctionDefinition(immutableExpandedName, 
                                                function));       
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 05-Dec-05	10527/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 05-Dec-05	10512/3	pduffin	VBM:2005112927 Fixed markers, before, after, hr, using images in content

 01-Dec-05	10447/1	emma	VBM:2005112308 Many bug fixes: xforms, GUI and pane styling

 04-Nov-05	10147/1	schaloner	VBM:2005092818 Added a getDeviceName function

 03-Nov-05	10049/1	schaloner	VBM:2005092818 Ongoing device name support

 02-Dec-05	10542/1	emma	VBM:2005112308 Forward port: Many bug fixes: xforms, GUI and pane styling

 01-Dec-05	10447/1	emma	VBM:2005112308 Many bug fixes: xforms, GUI and pane styling

 03-Nov-05	10049/1	schaloner	VBM:2005092818 Ongoing device name support

 20-Sep-05	9557/1	rgreenall	VBM:2005091402 Extract the registration of the DevicePolicyValueAccessor into a new method so that it may be used from DSB

 14-Sep-05	9510/3	rgreenall	VBM:2005091402 registerDevicePolicyValueAccessor made public

 14-Sep-05	9510/1	rgreenall	VBM:2005091402 Extract the registration of the DevicePolicyValueAccessor into a new method so that it may be used from DSB.

 13-Sep-05	9415/7	emma	VBM:2005072710 Making unbranded policy name function a static final

 13-Sep-05	9415/5	emma	VBM:2005072710 Making unbranded policy name function a static final

 09-Sep-05	9415/3	emma	VBM:2005072710 Add mappings for DISelect Set XPath Functions

 25-Jul-05	9121/1	doug	VBM:2005072219 Refactored device policy xpath functions so that they can be used in homedeck

 15-Jul-05	9073/1	pduffin	VBM:2005071420 Created runtime device layout that only exposes the parts of DeviceLayout that are needed at runtime.

 07-Jul-05	8967/1	pduffin	VBM:2005070702 Refactored resolving of expressions into component identities

 11-Feb-05	6931/1	geoff	VBM:2005020901 R821: Branding using Projects: Prerequisites: Fix remaining manual id creation

 08-Dec-04	6416/4	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/2	ianw	VBM:2004120703 New Build

 07-Dec-04	5800/2	ianw	VBM:2004090605 New Build system

 01-Nov-04	4948/4	ianw	VBM:2004052804 Expression Support Performance Fix Pt 2

 23-Jul-04	4948/1	ianw	VBM:2004052804 Pt II of Expression Support performance enhancements

 28-May-04	4611/1	ianw	VBM:2004052804 Performance improvments Pt II for object reduction

 07-Oct-04	5239/1	pcameron	VBM:2004072012 Fixed layout:getPaneInstance with a parameter and nested contexts

 07-Oct-04	5237/4	pcameron	VBM:2004072012 Fixed layout:getPaneInstance with a parameter and nested contexts

 12-Aug-04	5181/1	allan	VBM:2004081106 Support branding post MCS 3.0.

 30-Jul-04	4713/5	geoff	VBM:2004061004 Support iterated Regions (supermerge again)

 21-Jun-04	4713/2	geoff	VBM:2004061004 Support iterated Regions (commit prototype for safety)

 10-Jun-04	4683/1	ianw	VBM:2004052407 Reduced object creation in MarinerExpressionSupport

 13-Feb-04	2966/1	ianw	VBM:2004011923 Added mcsi:policy function

 08-Jan-04	2461/1	steve	VBM:2003121701 Patch pane name changes from Proteus2

 07-Jan-04	2389/4	steve	VBM:2003121701 Enhanced pane referencing

 06-Jan-04	2389/2	steve	VBM:2003121701 Pre-test save

 02-Nov-03	1771/1	doug	VBM:2003103104 Allowed getPolicyValue function to handle composite policy values

 18-Aug-03	1137/1	steve	VBM:2003081410 Fix multiple parameters bug by calling getParametersFunction

 14-Aug-03	1096/1	adrian	VBM:2003070805 updated usages of XMLPipelineContext and PropertyContainer to match pipeline api changes

 11-Aug-03	1017/3	allan	VBM:2003070409 Add getParameters, getPolicyValue, refactor functions and unit tests

 11-Aug-03	1013/3	chrisw	VBM:2003080806 Refactored expressions to be jsp independent

 06-Aug-03	954/3	doug	VBM:2003080503 changed jsEnvironmentFactory variable to jspEnvironmentFactory

 06-Aug-03	954/1	doug	VBM:2003080503 Refactored Pipeline to use DynamicElementRules

 05-Aug-03	928/2	philws	VBM:2003071601 Provide getHeader() and getHeaders() expression functions

 01-Aug-03	880/2	doug	VBM:2003072804 Refactored XMLPipelineFactory to meet new Public API requirements

 31-Jul-03	828/2	philws	VBM:2003071802 Update MCS against new Volantis Expression API from the Pipeline depot

 30-Jun-03	552/1	philws	VBM:2003062507 Provide JSP and XML variants of the vt:usePipeline and vt:include markup

 27-Jun-03	503/3	sumit	VBM:2003061906 ServletRequestFunction fixed to take MarinerRequestContext as source of parameters

 ===========================================================================
*/
