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
package com.volantis.mcs.layouts;

import com.volantis.mcs.model.TestValidator;
import com.volantis.mcs.model.validation.DiagnosticLevel;
import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * A test case which tests the new validation added to the old layout model.
 * Note that the old layout model is scheduled to be rewritten RSN.
 * <p>
 * This must be an integration test in order to perform validation.
 */
public class LayoutValidationTestCase extends TestCaseAbstract {

    // todo: later: convert all the tests which create layouts manually to xml files.
    // todo: later: move all these tests into individual test case classes and remove this

    // Formats of the same type within a layout (excluding fragments) may not
    // have the same name.

    /**
     * Ensure that a layout may contain two formats of the same type
     * with the same name as long as they are split between the layout and
     * it's fragment(s).
     */
    public void testValidateMultiplePanesInLayoutAndFragmentWithSameNameValid()
            throws LayoutException {

        final CanvasLayout layout = new CanvasLayout();

        Grid grid = createGrid(layout, "grid", 1, 2);
        layout.setRootFormat(grid);

        Pane layoutPane = new Pane(layout);
        layoutPane.setName("pane-same-name");
        link(grid, 0, layoutPane);

        Format fragment = new Fragment(layout);
        fragment.setName("fragment");
        layout.addFormat(fragment);
        link(grid, 1, fragment);

        Pane fragmentPane = new Pane(layout);
        fragmentPane.setName("pane-same-name");
        link(fragment, 0, fragmentPane);

        TestValidator testValidator = new TestValidator();
        testValidator.validate(layout);
    }

    /**
     * Ensure that a Layout (without Fragments) may not contain two Panes with
     * the same name.
     */
    public void testValidateMultiplePanesInLayoutWithSameNameInvalid()
            throws LayoutException {

        final CanvasLayout layout = new CanvasLayout();

        Grid grid = createGrid(layout, "grid", 1, 2);
        layout.setRootFormat(grid);

        Pane pane0 = new Pane(layout);
        pane0.setName("pane");
        link(grid, 0, pane0);

        Pane pane1 = new Pane(layout);
        pane1.setName("pane");
        link(grid, 1, pane1);

        TestValidator testValidator = new TestValidator();
        testValidator.expectDiagnostic(DiagnosticLevel.ERROR,
                "/rootFormat/formats/0/name",
                "format-name-duplicate", new Object[]{"Pane", "pane"});
        testValidator.expectDiagnostic(DiagnosticLevel.ERROR,
                "/rootFormat/formats/1/name",
                "format-name-duplicate", new Object[]{"Pane", "pane"});
        testValidator.validate(layout);
    }

    // Formats of the same type within a fragments may not have the same name.

    /**
     * Ensure that a fragment may not contain two formats of the same type
     * with the same name.
     */
    public void testValidateMultiplePanesInFragmentWithSameNameInvalid()
            throws LayoutException {

        final CanvasLayout layout = new CanvasLayout();

        Format fragment = new Fragment(layout);
        fragment.setName("fragment");
        layout.addFormat(fragment);
        layout.setRootFormat(fragment);

        Grid grid = createGrid(layout, "grid", 1, 2);
        link(fragment, 0, grid);

        Pane pane0 = new Pane(layout);
        pane0.setName("pane");
        link(grid, 0, pane0);

        Pane pane1 = new DissectingPane(layout);
        pane1.setName("pane");
        link(grid, 1, pane1);

        TestValidator testValidator = new TestValidator();
        testValidator.expectDiagnostic(DiagnosticLevel.ERROR,
                "/rootFormat/formats/0/formats/0/name",
                "format-name-duplicate", new Object[]{"Pane", "pane"});
        testValidator.expectDiagnostic(DiagnosticLevel.ERROR,
                "/rootFormat/formats/0/formats/1/name",
                "format-name-duplicate", new Object[]{"Pane", "pane"});
        testValidator.validate(layout);

    }

    // A single Dissecting Pane may only appear within a Fragment

    /**
     * Ensure that a Fragment may contain a single dissecting pane.
     *
     * @throws LayoutException
     */
    public void testValidateDissectingPaneInFragmentValid()
            throws LayoutException {

        final CanvasLayout layout = new CanvasLayout();

        Fragment fragment = new Fragment(layout);
        fragment.setName("fragment");
        layout.setRootFormat(fragment);

        Grid grid = createGrid(layout, "grid", 1, 1);
        link(fragment, 0, grid);

        DissectingPane dissectingPane = new DissectingPane(layout);
        dissectingPane.setName("dissectingPane");
        link(grid, 0, dissectingPane);

        TestValidator testValidator = new TestValidator();
        testValidator.validate(layout);

    }

    /**
     * Ensure that a Dissecting pane contained by a non Fragment is invalid.
     *
     * @throws LayoutException
     */
    public void testValidateDissectingPaneInNonFragmentInvalid()
            throws LayoutException {

        final CanvasLayout layout = new CanvasLayout();


        DissectingPane dissectingPane = new DissectingPane(layout);
        dissectingPane.setName("dissectingPane");
        layout.setRootFormat(dissectingPane);

        TestValidator testValidator = new TestValidator();
        testValidator.expectDiagnostic(DiagnosticLevel.ERROR, "/rootFormat",
                "dissecting-pane-must-be-in-fragment",
                new String[] {"dissectingPane"});
        testValidator.validate(layout);

    }

    /**
     * Ensure that a Fragment may not contain multiple dissecting panes.
     *
     * @throws LayoutException
     */
    public void testValidateMutipleDissectingPanesInFragment()
            throws LayoutException {

        final CanvasLayout layout = new CanvasLayout();

        Fragment fragment = new Fragment(layout);
        fragment.setName("fragment");
        layout.setRootFormat(fragment);

        Grid grid = createGrid(layout, "grid", 1, 2);
        link(fragment, 0, grid);

        DissectingPane dissectingPane0 = new DissectingPane(layout);
        dissectingPane0.setName("dissectingPane0");
        link(grid, 0, dissectingPane0);

        DissectingPane dissectingPane1 = new DissectingPane(layout);
        dissectingPane1.setName("dissectingPane1");
        link(grid, 1, dissectingPane1);

        TestValidator testValidator = new TestValidator();
        testValidator.expectDiagnostic(DiagnosticLevel.ERROR,
                "/rootFormat/formats/0/formats/0",
                "dissecting-pane-duplicate", new Object[]{});
        testValidator.expectDiagnostic(DiagnosticLevel.ERROR,
                "/rootFormat/formats/0/formats/1",
                "dissecting-pane-duplicate", new Object[]{});
        testValidator.validate(layout);

    }

    // Form Fragments may only appear within Forms and may not appear within
    // spatial and temporal iterators

    /**
     * Ensure that a form fragmment contained by form is valid.
     *
     * @throws LayoutException
     */
    public void testValidateFormFragmentInForm() throws LayoutException {

        final CanvasLayout layout = new CanvasLayout();

        Form form = new Form(layout);
        form.setName("form");
        layout.setRootFormat(form);

        Format formFragment = new FormFragment(layout);
        formFragment.setName("formFragment");
        link(form, 0, formFragment);
        layout.addFormat(formFragment);

        TestValidator testValidator = new TestValidator();
        testValidator.validate(layout);

    }

    /**
     * Ensure that a form fragment contained by a non form is invalid.
     *
     * @throws LayoutException
     */
    public void testValidateFormFragmentInNonFormInvalid() throws LayoutException {

        final CanvasLayout layout = new CanvasLayout();

        Grid grid = createGrid(layout, "grid", 1, 1);
        layout.setRootFormat(grid);

        Format formFragment = new FormFragment(layout);
        formFragment.setName("formFragment");
        link(grid, 0, formFragment);
        layout.addFormat(formFragment);

        TestValidator testValidator = new TestValidator();
        testValidator.expectDiagnostic(DiagnosticLevel.ERROR,
                "/rootFormat/formats/0",
                "form-fragment-must-be-in-form", new Object[]{"formFragment"});
        testValidator.validate(layout);

    }

    /**
     * Ensure that a form fragment contained by a form and iterator is invalid.
     *
     * @throws LayoutException
     */
    public void testValidateFormFragmentInFormAndIteratorInvalid()
            throws LayoutException {

        final CanvasLayout layout = new CanvasLayout();

        Form form = new Form(layout);
        form.setName("form");
        layout.setRootFormat(form);

        SpatialFormatIterator spatial = createSpatial(layout, "spatial");
        link(form, 0, spatial);

        Format formFragment = new FormFragment(layout);
        formFragment.setName("formFragment");
        link(spatial, 0, formFragment);
        layout.addFormat(formFragment);

        TestValidator testValidator = new TestValidator();
        testValidator.expectDiagnostic(DiagnosticLevel.ERROR,
                "/rootFormat/formats/0/formats/0",
                "form-fragment-must-not-be-in-spatial",
                new Object[]{"formFragment"});
        testValidator.validate(layout);

    }

    // Forms may not appear within a Form

    /**
     * Ensure that a form contained by another form is invalid.
     *
     * @throws LayoutException
     */
    public void testValidateFormInFormInvalid() throws LayoutException {

        final CanvasLayout layout = new CanvasLayout();

        Form parentForm = new Form(layout);
        parentForm.setName("parentForm");
        layout.setRootFormat(parentForm);

        Format childForm = new Form(layout);
        childForm.setName("childForm");
        link(parentForm, 0, childForm);
        layout.addFormat(childForm);

        TestValidator testValidator = new TestValidator();
        testValidator.expectDiagnostic(DiagnosticLevel.ERROR,
                "/rootFormat/formats/0",
                "form-must-not-be-in-form",
                new Object[]{"childForm"});
        testValidator.validate(layout);

    }

    /**
     * Ensure that a form contained by a non form is valid.
     *
     * @throws LayoutException
     */
    public void testValidateFormInNonFormValid() throws LayoutException {

        final CanvasLayout layout = new CanvasLayout();

        Grid grid = createGrid(layout, "grid", 1, 1);
        layout.setRootFormat(grid);

        Format form = new Form(layout);
        form.setName("form");
        link(grid, 0, form);
        layout.addFormat(form);

        TestValidator testValidator = new TestValidator();
        testValidator.validate(layout);

    }

    // Forms may not appear within spatial and temporal format iterators.

    /**
     * Ensure that a form contained by a grid is valid.
     *
     * @throws LayoutException
     */
    public void testValidateFormInNonIteratorValid() throws LayoutException {

        final CanvasLayout layout = new CanvasLayout();

        Grid grid = createGrid(layout, "grid", 1, 1);
        layout.setRootFormat(grid);

        Format form = new Form(layout);
        form.setName("form");
        link(grid, 0, form);
        layout.addFormat(form);

        TestValidator testValidator = new TestValidator();
        testValidator.validate(layout);

    }

    /**
     * Ensure that a form contained by a spatial is invalid.
     *
     * @throws LayoutException
     */
    public void testValidateFormInInteratorInvalid() throws LayoutException {

        final CanvasLayout layout = new CanvasLayout();

        SpatialFormatIterator spatial = createSpatial(layout, "spatial");
        layout.setRootFormat(spatial);

        Format form = new Form(layout);
        form.setName("form");
        link(spatial, 0, form);
        layout.addFormat(form);

        TestValidator testValidator = new TestValidator();
        testValidator.expectDiagnostic(DiagnosticLevel.ERROR,
                "/rootFormat/formats/0", "form-must-not-be-in-spatial",
                new String[] {"form"});
        testValidator.validate(layout);

    }

    // Fragments may not appear within spatial and temporal format iterators

    /**
     * Ensure that a fragmment contained by a grid is valid.
     *
     * @throws LayoutException
     */
    public void testValidateFragmentInNonIteratorValid() throws LayoutException {

        final CanvasLayout layout = new CanvasLayout();

        Grid grid = createGrid(layout, "grid", 1, 1);
        layout.setRootFormat(grid);

        Format fragment = new Fragment(layout);
        fragment.setName("fragment");
        link(grid, 0, fragment);
        layout.addFormat(fragment);

        TestValidator testValidator = new TestValidator();
        testValidator.validate(layout);

    }

    /**
     * Ensure that a fragment contained by a spatial is invalid.
     *
     * @throws LayoutException
     */
    public void testValidateFragmentInIteratorInvalid() throws LayoutException {

        final CanvasLayout layout = new CanvasLayout();

        SpatialFormatIterator spatial = createSpatial(layout, "spatial");
        layout.setRootFormat(spatial);

        Format fragment = new Fragment(layout);
        fragment.setName("fragment");
        link(spatial, 0, fragment);
        layout.addFormat(fragment);

        TestValidator testValidator = new TestValidator();
        testValidator.expectDiagnostic(DiagnosticLevel.ERROR,
                "/rootFormat/formats/0",
                "fragment-must-not-be-in-spatial",
                new Object[]{"fragment"});
        testValidator.validate(layout);

    }

    // Replicas may only appear within a Fragment

    /**
     * Ensure that a Replica contained by a Fragment is valid.
     *
     * @throws LayoutException
     */
    public void testValidateReplicaInFragmentValid() throws LayoutException {

        final CanvasLayout layout = new CanvasLayout();

        Fragment fragment = new Fragment(layout);
        fragment.setName("fragment");
        layout.setRootFormat(fragment);

        Replica replica = createReplica(layout, "replica");
        link(fragment, 0, replica);

        TestValidator testValidator = new TestValidator();
        testValidator.validate(layout);

    }

    /**
     * Ensure that a Replica contained by non-Fragment(s) is invalid.
     *
     * @throws LayoutException
     */
    public void testValidateReplicaInNonFragmentInvalid() throws LayoutException {

        final CanvasLayout layout = new CanvasLayout();

        Grid grid = createGrid(layout, "grid", 1, 1);
        layout.setRootFormat(grid);

        Replica replica = createReplica(layout, "replica");
        link(grid, 0, replica);

        TestValidator testValidator = new TestValidator();
        testValidator.expectDiagnostic(DiagnosticLevel.ERROR,
                "/rootFormat/formats/0",
                "replica-must-be-in-fragment",
                new Object[]{"replica"});
        testValidator.validate(layout);

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

    private SpatialFormatIterator createSpatial(final CanvasLayout layout,
            final String name) {
        SpatialFormatIterator spatial = new SpatialFormatIterator(layout);
        spatial.setName(name);
        spatial.setRowsFlexibility("fixed");
        spatial.setRows("1");
        spatial.setColumnsFlexibility("fixed");
        spatial.setColumns(1);
        return spatial;
    }

    private Replica createReplica(final CanvasLayout layout, final String name) {
        Replica replica = new Replica(layout);
        replica.setName(name);
        replica.setReplicantType(FormatType.GRID);
        replica.setReplicant("blah");
        return replica;
    }

    /**
     * Link two Formats together as parent and child, establishing both forward
     * and back links.
     *
     * @param parent the parent Format.
     * @param childIndex the location of the child format within the parent.
     * @param child the child Format.
     * @throws LayoutException
     */
    private void link(Format parent, final int childIndex, Format child)
            throws LayoutException {

        child.setParent(parent);
        parent.setChildAt(child, childIndex);

    }

}
