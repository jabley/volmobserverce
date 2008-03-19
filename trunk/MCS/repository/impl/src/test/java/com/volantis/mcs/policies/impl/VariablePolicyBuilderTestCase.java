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
 * (c) Volantis Systems Ltd 2005. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.policies.impl;

import com.volantis.mcs.layouts.CanvasLayout;
import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.Grid;
import com.volantis.mcs.layouts.LayoutException;
import com.volantis.mcs.layouts.Pane;
import com.volantis.mcs.model.ModelFactory;
import com.volantis.mcs.model.TestValidator;
import com.volantis.mcs.model.jibx.JiBXSourceLocation;
import com.volantis.mcs.model.path.StepMock;
import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.mcs.model.validation.Validatable;
import com.volantis.mcs.model.validation.ValidationContextMock;
import com.volantis.mcs.model.validation.I18NMessageMock;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.policies.VariablePolicyBuilder;
import com.volantis.mcs.policies.PolicyReferenceMock;
import com.volantis.mcs.policies.PolicyModel;
import com.volantis.mcs.policies.impl.variants.VariantBuilderImpl;
import com.volantis.mcs.policies.impl.variants.layout.LayoutContentBuilderImpl;
import com.volantis.mcs.policies.impl.variants.selection.DefaultSelectionImpl;
import com.volantis.mcs.policies.variants.VariantBuilder;
import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.synergetics.testtools.TestCaseAbstract;

public class VariablePolicyBuilderTestCase
        extends TestCaseAbstract {

    ModelFactory modelFactory = ModelFactory.getDefaultInstance();

    /**
     * Create a context object, which expects altCount calls to pushIndexStep()
     * and popStep().
     *
     * @param altCount The number of calls to push/pop methods.
     * @return a context mock object
     */
    private ValidationContextMock createContext(int altCount) {
        StepMock altStep = new StepMock("altStep", expectations);

        ValidationContextMock context =
            new ValidationContextMock("contextMock", expectations);

        context.expects.pushPropertyStep(PolicyModel.ALTERNATE_POLICIES)
            .returns(altStep).fixed(1);

        for (int i = 0; i < altCount; i++) {
            StepMock step = new StepMock("stepMock" + i, expectations);
            context.expects.pushIndexedStep(i).returns(step).fixed(1);
            context.expects.popStep(step).fixed(1);
        }

        context.expects.popStep(altStep).fixed(1);

        return context;
    }

    /**
     * Create a single alternate of addition to a policy builder.
     *
     * @param name of alternate
     * @param type of alternate
     * @return policy reference mock
     */
    private PolicyReferenceMock creaetAlternate(String name, PolicyType type) {

        PolicyReferenceMock alternative = new PolicyReferenceMock(
                "alternativeMock", expectations);
        alternative.expects.getName().returns(name).any();
        alternative.expects.getExpectedPolicyType().returns(type).any();

        return alternative;
    }

    /**
     * Test that the allowable variants type can be set to only valid values and
     * invalid values are detected.
     */
    public void testSetAllowableVariantType() {

        VariablePolicyBuilder policy = new VariablePolicyBuilderImpl();

        policy.setVariablePolicyType(PolicyType.AUDIO);
        policy.setVariablePolicyType(PolicyType.CHART);
        policy.setVariablePolicyType(PolicyType.IMAGE);
        policy.setVariablePolicyType(PolicyType.LAYOUT);
        policy.setVariablePolicyType(PolicyType.LINK);
        policy.setVariablePolicyType(PolicyType.RESOURCE);
        policy.setVariablePolicyType(PolicyType.SCRIPT);
        policy.setVariablePolicyType(PolicyType.TEXT);
        policy.setVariablePolicyType(PolicyType.THEME);
        policy.setVariablePolicyType(PolicyType.VIDEO);

        try {
            policy.setVariablePolicyType(null);
            fail("Did not detect null type");
        } catch (IllegalArgumentException expected) {
        }
    }

    /**
     * Test that valid alternates are validated correctly. No messages should
     * be added to the context object.
     */
    public void testValidationOfCorrectAlternate() {

        ValidationContextMock context = createContext(1);

        PolicyReferenceMock audioAlt =
            creaetAlternate("good-name", PolicyType.AUDIO);

        VariablePolicyBuilderImpl policy = new VariablePolicyBuilderImpl();
        policy.setVariablePolicyType(PolicyType.AUDIO);

        policy.addAlternatePolicy(audioAlt);
        policy.validateAlternates(context);
    }

    /**
     * Test that an alternate with an incorrect name will result in a message
     * being added to the context.
     */
    public void testValidatationOfIncorrectAlternateName() {

        ValidationContextMock context = createContext(1);

        PolicyReferenceMock audioAlt =
            creaetAlternate("", PolicyType.AUDIO);

        I18NMessageMock message = new I18NMessageMock(
                "messageMock", expectations);

        VariablePolicyBuilderImpl policy = new VariablePolicyBuilderImpl();
        policy.setVariablePolicyType(PolicyType.AUDIO);
        policy.addAlternatePolicy(audioAlt);

        // These expectations represent the reporting of an error
        context.expects.createMessage(PolicyMessages.ALTERNATE_NAME_ILLEGAL,
            "", PolicyType.AUDIO).returns(message);
        context.fuzzy.addDiagnostic(
            mockFactory.expectsInstanceOf(JiBXSourceLocation.class),
            DiagnosticLevel.ERROR, message);

        policy.validateAlternates(context);
    }


    /**
     * Test that an alternate with an invalid type will result in a message
     * being added to the context.
     */
    public void testValidatationOfIncorrectAlternateType() {

        ValidationContextMock context = createContext(1);

        PolicyReferenceMock imageAlt =
            creaetAlternate("good-name", PolicyType.IMAGE);

        I18NMessageMock message = new I18NMessageMock(
                "messageMock", expectations);

        VariablePolicyBuilderImpl policy = new VariablePolicyBuilderImpl();
        policy.setVariablePolicyType(PolicyType.AUDIO);
        policy.addAlternatePolicy(imageAlt);

        // These expectations represent the reporting of an error
        context.expects.createMessage(PolicyMessages.ALTERNATE_TYPE_ILLEGAL,
            PolicyType.IMAGE.toString(), "good-name").returns(message);
        context.fuzzy.addDiagnostic(
            mockFactory.expectsInstanceOf(JiBXSourceLocation.class),
            DiagnosticLevel.ERROR, message);

        policy.validateAlternates(context);
    }

    /**
     * Test that 2 alternates with the same type will result in an error.
     */
    public void testValidatationOfTwoAlternatesWithTheSameType() {

        ValidationContextMock context = createContext(2);

        PolicyReferenceMock imageAlt1 =
            creaetAlternate("good-name", PolicyType.AUDIO);
        PolicyReferenceMock imageAlt2 =
            creaetAlternate("another-good-name", PolicyType.AUDIO);

        I18NMessageMock message = new I18NMessageMock(
                "messageMock", expectations);

        VariablePolicyBuilderImpl policy = new VariablePolicyBuilderImpl();
        policy.setVariablePolicyType(PolicyType.AUDIO);
        policy.addAlternatePolicy(imageAlt1);
        policy.addAlternatePolicy(imageAlt2);

        // These expectations represent the reporting of an error
        context.expects.createMessage(PolicyMessages.ALTERNATE_MULTIPLE_TYPES,
            PolicyType.AUDIO).returns(message);
        context.fuzzy.addDiagnostic(
            mockFactory.expectsInstanceOf(JiBXSourceLocation.class),
            DiagnosticLevel.ERROR, message);

        policy.validateAlternates(context);
    }

    /**
     * Ensure that without pruning, variants and their diagnostics are
     * processed as normal.
     *
     * @throws LayoutException
     */
    public void testPruningOff() throws LayoutException {

        VariablePolicyBuilder policy = createDuplicatePaneNamesLayout();

        TestValidator validator = new TestValidator();
        validator.expectDiagnostic(DiagnosticLevel.ERROR,
                "/variants/0/content/layout/rootFormat/formats/0/name",
                "format-name-duplicate", new Object[]{"Pane", "pane"});
        validator.expectDiagnostic(DiagnosticLevel.ERROR,
                "/variants/0/content/layout/rootFormat/formats/1/name",
                "format-name-duplicate", new Object[]{"Pane", "pane"});
        validator.validate((Validatable) policy);
        assertEquals("", 1, policy.getVariantBuilders().size());

    }

    /**
     * Ensure that with pruning, any variants with errors are pruned, their
     * diagnostics are made non-fatal, and additional diagnostics are added to
     * indicate that the variant has been removed.
     *
     * @throws LayoutException
     */
    public void testPruningOn() throws LayoutException {

        VariablePolicyBuilder policy = createDuplicatePaneNamesLayout();

        TestValidator validator = new TestValidator(true);
        validator.expectDiagnostic(DiagnosticLevel.WARNING,
                "/variants/0/content/layout/rootFormat/formats/0/name",
                "format-name-duplicate", new Object[]{"Pane", "pane"});
        validator.expectDiagnostic(DiagnosticLevel.WARNING,
                "/variants/0/content/layout/rootFormat/formats/1/name",
                "format-name-duplicate", new Object[]{"Pane", "pane"});
        validator.expectDiagnostic(DiagnosticLevel.WARNING,
                "/variants/0",
                PolicyMessages.VARIANT_INVALID_IGNORED, null);
        validator.validate((Validatable) policy);
        assertEquals("", 0, policy.getVariantBuilders().size());

    }

    /**
     * Create a layout with a grid contain two panes with the same name.
     * <p>
     * The names conflict so there should be two diagnostics when validated
     * normally.
     *
     * @return the invalid layout
     * @throws LayoutException
     */
    private VariablePolicyBuilder createDuplicatePaneNamesLayout()
            throws LayoutException {

        // Create the layout with a two panes of the same name.
        CanvasLayout layout = new CanvasLayout();
        Grid grid = createGrid(layout, "grid", 1, 2);
        layout.setRootFormat(grid);

        Pane pane0 = new Pane(layout);
        pane0.setName("pane");
        link(grid, 0, pane0);

        Pane pane1 = new Pane(layout);
        pane1.setName("pane");
        link(grid, 1, pane1);

        // Create the content and add the layout to it.
        LayoutContentBuilderImpl content = new LayoutContentBuilderImpl();
        content.setLayout(layout);
        // Create the variant and add the content to it.
        final VariantBuilder variant =
                new VariantBuilderImpl(VariantType.LAYOUT);
        variant.setSelectionBuilder(new DefaultSelectionImpl());
        variant.setContentBuilder(content);

        //
        VariablePolicyBuilder policy = new VariablePolicyBuilderImpl();
        policy.setVariablePolicyType(PolicyType.LAYOUT);
        policy.addVariantBuilder(variant);
        return policy;
    }

    /**
     * Create a Grid Format for testing.
     *
     * @param layout the layout that the grid belongs to.
     * @param name the name of the grid.
     * @param rows the number of rows in the grid.
     * @param columns the number of columns in the grid.
     * @return the created grid.
     */
    private Grid createGrid(final CanvasLayout layout, final String name,
            final int rows, final int columns) {

        Grid grid = new Grid(layout);
        grid.setName(name);
        grid.setRows(rows);
        grid.setColumns(columns);
        grid.attributesHaveBeenSet();
        return grid;

    }

    /**
     * Link two Formats together as parent and child, establishing both forward
     * and back links.
     *
     * @param parent the parent Format.
     * @param childIndex the location of the child format within the parent.
     * @param child the child Format.
     * @throws com.volantis.mcs.layouts.LayoutException
     */
    private void link(Format parent, final int childIndex, Format child)
            throws LayoutException {

        child.setParent(parent);
        parent.setChildAt(child, childIndex);

    }

}
