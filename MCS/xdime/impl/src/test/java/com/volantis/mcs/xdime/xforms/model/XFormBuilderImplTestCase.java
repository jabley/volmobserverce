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
package com.volantis.mcs.xdime.xforms.model;

import com.volantis.mcs.context.MarinerPageContextMock;
import com.volantis.mcs.protocols.DOMProtocolMock;
import com.volantis.mcs.protocols.DeviceLayoutContextMock;
import com.volantis.mcs.protocols.EventAttributes;
import com.volantis.mcs.protocols.ProtocolConfigurationMock;
import com.volantis.mcs.protocols.ProtocolSupportFactoryMock;
import com.volantis.mcs.protocols.XFFormAttributes;
import com.volantis.mcs.protocols.XFFormFieldAttributes;
import com.volantis.mcs.protocols.XFFormFieldAttributesMock;
import com.volantis.mcs.protocols.forms.EmulatedXFormDescriptor;
import com.volantis.mcs.protocols.forms.FieldDescriptor;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.properties.MCSBreakAfterKeywords;
import com.volantis.mcs.xdime.XDIMEAttribute;
import com.volantis.mcs.xdime.XDIMEAttributes;
import com.volantis.mcs.xdime.XDIMEAttributesImpl;
import com.volantis.mcs.xdime.XDIMEAttributesMock;
import com.volantis.mcs.xdime.XDIMEException;
import com.volantis.mcs.xdime.xforms.XFormElements;
import com.volantis.styling.StylesMock;
import com.volantis.styling.values.MutablePropertyValuesMock;
import com.volantis.synergetics.testtools.TestCaseAbstract;

import java.util.List;

/**
 * Verifies that {@link XFormBuilderImpl} behaves as expected.
 */
public class XFormBuilderImplTestCase extends TestCaseAbstract {

    private static final String MODEL_ID = "model1";
    private static final String MODEL_ID2 = "model2";
    private static final String DEFAULT_MODEL_ID = "__DEFAULT__";

    private static final String ITEM_NAME = "item1";
    private static final String ITEM_NAME2 = "item2";
    private static final String CONTENT = "Content: an initial value?";

    private static final String SUBMISSION_ID = "submission1";
    private static final String SUBMISSION_ID2 = "submission2";
    private static final String ACTION = "http://www.volantis.com";
    private static final String METHOD = "get";

    private static final String CONTROL_ID = "control1";
    private static final String CONTROL_ID2 = "control2";

    private static DOMProtocolMock protocol;
    private static ProtocolConfigurationMock protocolConfig;
    private static ProtocolSupportFactoryMock psf;
    private static EventAttributes events;
    private static EmulatedXFormDescriptor TEST_FD;
    private static EmulatedXFormDescriptor TEST_FD2;
    private static EmulatedXFormDescriptor TEST_FD_DEFAULT;

    XFormBuilderImpl builder;

    public void setUp() throws Exception {
        super.setUp();
        protocolConfig = new ProtocolConfigurationMock("protocolConfig",
                expectations);
        psf = new ProtocolSupportFactoryMock("psf", expectations);
        psf.expects.getDOMFactory().returns(null);
        protocol = new DOMProtocolMock("protocol", expectations, psf,
                protocolConfig);
        events = new EventAttributes();
        TEST_FD = new EmulatedXFormDescriptor();
        TEST_FD.setContainingFormName(MODEL_ID);
        TEST_FD2 = new EmulatedXFormDescriptor();
        TEST_FD2.setContainingFormName(MODEL_ID2);
        TEST_FD_DEFAULT = new EmulatedXFormDescriptor();
        TEST_FD_DEFAULT.setContainingFormName(DEFAULT_MODEL_ID);

        builder = new XFormBuilderImpl();
    }

    public void testAddItemWhenNoModelDefined() {
        // this should not happen, as we should be using a validating parser,
        // but just in case...
        try {
            builder.addItem(ITEM_NAME, CONTENT);
            fail("Should throw an exception if trying to add an item when no" +
                    "models have been defined");
        } catch (XDIMEException e) {
            // do nothing - correct behaviour.
        }
    }

    public void testAddItemWithNullName() throws XDIMEException {

        builder.addModel(MODEL_ID, TEST_FD);

        try {
            builder.addItem(null, CONTENT);
            fail("Should throw an exception if attempting to add an item " +
                    "with a null name");
        } catch (IllegalArgumentException e) {
            // do nothing - correct behaviour.
        }
    }

    /**
     * Verifies that it is possible to add an item with a non null name and
     * null content.
     *
     * @throws XDIMEException if there is a problem running the test
     */
    public void testAddItemWithNullContent() throws XDIMEException {

        builder.addModel(MODEL_ID, TEST_FD);
        builder.addItem(ITEM_NAME, null);
    }

    public void testAddItemWithValidNameAndContent() throws XDIMEException {

        builder.addModel(MODEL_ID, TEST_FD);
        builder.addItem(ITEM_NAME, CONTENT);
    }

    /**
     * Verifies that calling {@link XFormBuilderImpl#getItem} with a null name
     * will return a null item.
     *
     * @throws XDIMEException if there was a problem running the test
     */
    public void testGetItemWithNullName() throws XDIMEException {

        builder.addModel(MODEL_ID, TEST_FD);
        builder.getItem(null, MODEL_ID);
        builder.getItem(null, null);
    }

    /**
     * Verifies that if the model id refers to a valid model, the call to
     * getItem doesn't fail (it may return null if the item doesn't exist).
     */
    public void testGetItemWithNonNullNameAndModelID() throws XDIMEException {
        builder.addModel(MODEL_ID, TEST_FD);

        // verifies that even though the item doesn't exist, it doesn't fail
        // if the model id is valid.
        SIItem item = builder.getItem(ITEM_NAME, MODEL_ID);
        assertNull(item);

        builder.addItem(ITEM_NAME, CONTENT);
        item = builder.getItem(ITEM_NAME, MODEL_ID);
        assertNotNull(item);
    }

    /**
     * Verifies that if the model id refers to a valid model, and the item name
     * is not in that model, but does exist in another model, null is returned.
     */
    public void testGetItemWithNameOfItemThatExistsButNotInTheSpecifiedModel()
            throws XDIMEException {

        builder.addModel(DEFAULT_MODEL_ID, TEST_FD_DEFAULT);
        builder.addItem(ITEM_NAME, CONTENT);
        builder.addModel(MODEL_ID, TEST_FD);

        // verifies that even though an item with that name exists in another
        // model, it is NOT returned.
        SIItem item = builder.getItem(ITEM_NAME, MODEL_ID);
        assertNull(item);
    }

    /**
     * Verifies that if the model ID is null, but an item with the specified
     * name is found in the default model then this item is returned.
     */
    public void testGetItemWithNameOfItemInTheDefaultModelAndNullModelID()
            throws XDIMEException {

        // create a default model with the requested item and another model
        // with no items.
        builder.addModel(DEFAULT_MODEL_ID, TEST_FD_DEFAULT);
        builder.addItem(ITEM_NAME, CONTENT);
        builder.addModel(MODEL_ID, TEST_FD);

        SIItem item = builder.getItem(ITEM_NAME, null);
        assertNotNull(item);
        assertEquals(item, builder.getItem(ITEM_NAME, DEFAULT_MODEL_ID));
    }

    /**
     * Verifies that if the model ID is null, and the name is present but does
     * not refer to an item in any of the defined models, null is returned.
     */
    public void testGetItemWithNameOfItemThatDoesNotExistAndNullModelID()
            throws XDIMEException {
        builder.addModel(DEFAULT_MODEL_ID, TEST_FD_DEFAULT);
        builder.addItem("test", "test");
        builder.addModel(MODEL_ID, TEST_FD);
        builder.addItem("test2", "test2");

        SIItem item = builder.getItem(ITEM_NAME, null);
        assertNull(item);
    }

    /**
     * Verifies that if the model ID passed to getItem is null, but an item
     * with the specified name exists in another model, then that item is
     * returned.
     */
    public void testGetItemWithNameOfItemThatExistsAndNullModelID()
            throws XDIMEException {

        builder.addModel(MODEL_ID, TEST_FD);
        builder.addItem(ITEM_NAME, CONTENT);
        SIItem item = builder.getItem(ITEM_NAME, null);
        assertNotNull(item);
    }

    public void testAddModelWithNullID() {

    }

    /**
     * Verify that calling {@link XFormBuilder#addModel} causes a new model
     * to be created and returned. Also that this model is returned by
     * {@link XFormBuilderImpl#modelBeingBuilt}, and that
     * {@link XFormBuilder#numberOfModels()} is increased every time this is
     * called.
     */
    public void testAddModel() throws XDIMEException {

        // Verify that there are no models or controls when the builder is
        // first created.
        assertEquals(0, builder.numberOfModels());
        assertNull(builder.modelBeingBuilt);

        // Add the model
        XFormModel model = builder.addModel(MODEL_ID, TEST_FD);

        // Verify that the new model is not null..
        assertNotNull(model);
        // ..and is configured as specified
        assertEquals(MODEL_ID, model.getID());
        assertFalse(model.getItemIterator().hasNext());
        assertNull(model.getSubmission());

        // Verify the state of the builder
        assertEquals(1, builder.numberOfModels());
        assertEquals(model, builder.modelBeingBuilt);

        // Create a second model
        XFormModel modelTwo = builder.addModel(MODEL_ID2, TEST_FD2);

        // Verify that this second model is not null..
        assertNotNull(modelTwo);
        // ..or the same as the first model..
        assertNotEquals(model, modelTwo);
        // ..and is configured as specified.
        assertEquals(MODEL_ID2, modelTwo.getID());
        assertFalse(modelTwo.getItemIterator().hasNext());
        assertNull(modelTwo.getSubmission());

        // Verify the state of the builder
        assertEquals(2, builder.numberOfModels());
        assertEquals(modelTwo, builder.modelBeingBuilt);
    }

    /**
     * Verify that calling {@link XFormBuilder#addModel} causes a new model
     * to be created and returned, and that calling the same method with the
     * same parameters will cause an exception to be thrown.
     */
    public void testAddModelTwice() throws XDIMEException {
        // Verify that there are no models when the builder is first created.
        assertEquals(0, builder.numberOfModels());
        assertNull(builder.modelBeingBuilt);

        // Add the model
        XFormModel model = builder.addModel(MODEL_ID, TEST_FD);

        // Verify that the new model is not null..
        assertNotNull(model);
        // ..and is configured as specified
        assertEquals(MODEL_ID, model.getID());
        assertFalse(model.getItemIterator().hasNext());
        assertNull(model.getSubmission());

        // Verify the state of the builder
        assertEquals(1, builder.numberOfModels());
        assertEquals(model, builder.modelBeingBuilt);

        try {
            // Add the same control again
            model = builder.addModel(MODEL_ID, TEST_FD);
            fail("The XFormBuilder should not allow a model with the same " +
                    "ID to be added twice");
        } catch (XDIMEException e) {
            // do nothing, correct behaviour.
        }
    }

    /**
     * Verify that {@link XFormBuilder#getModel} returns the {@link XFormModel}
     * which was added with the specified id. May return null.
     */
    public void testGetModelWithID() throws XDIMEException {

        // Verify that there are no models when the builder is first created.
        assertEquals(0, builder.numberOfModels());
        assertNull(builder.modelBeingBuilt);
        assertNull(builder.getModel(MODEL_ID));

        // Add the model
        XFormModel model = builder.addModel(MODEL_ID, TEST_FD);

        // Verify that the new model is not null..
        assertNotNull(model);
        assertEquals(model, builder.getModel(MODEL_ID));

        // Create a second model
        XFormModel modelTwo = builder.addModel(MODEL_ID2, TEST_FD2);
        // Verify that this second model is not null..
        assertNotNull(modelTwo);
        // ..or the same as the first model..
        assertNotEquals(model, modelTwo);

        assertEquals(modelTwo, builder.getModel(MODEL_ID2));
    }

    /**
     * Verify that {@link XFormBuilder#getModel} returns the {@link XFormModel}
     * in which the specified item was defined. May return null.
     */
    public void testGetModelWithItem() throws XDIMEException {
        // Verify that there are no models when the builder is first created.
        assertEquals(0, builder.numberOfModels());
        assertNull(builder.modelBeingBuilt);
        assertNull(builder.getModel(MODEL_ID));

        // Add the model and item
        XFormModel model = builder.addModel(MODEL_ID, TEST_FD);
        builder.addItem(ITEM_NAME, CONTENT);

        // Verify that the new model is not null..
        assertNotNull(model);
        final SIItem item = model.getItem(ITEM_NAME);
        assertEquals(model, builder.getModel(item));

        // Create a second model
        XFormModel modelTwo = builder.addModel(MODEL_ID2, TEST_FD2);
        builder.addItem(ITEM_NAME2, CONTENT);
        // Verify that this second model is not null..
        assertNotNull(modelTwo);
        // ..or the same as the first model..
        assertNotEquals(model, modelTwo);

        final SIItem item2 = modelTwo.getItem(ITEM_NAME2);
        assertEquals(modelTwo, builder.getModel(item2));
    }

    /**
     * Verifies that {@link XFormBuilder#numberOfModels()} returns the number
     * of models that have been added to the builder.
     */
    public void testNumberOfModels() throws XDIMEException {
        XFormBuilder builder = new XFormBuilderImpl();

        // Verify that there are no models when the builder is first created.
        assertEquals(0, builder.numberOfModels());

        final String id = "testID";
        for (int i = 1; i <= 10; i++) {
            // Add a control
            builder.addModel(id + Integer.toString(i), TEST_FD);
            assertEquals(i, builder.numberOfModels());
        }
    }

    public void testAddSubmissionWhenNoModelDefined() {
        // this should not happen, as we should be using a validating parser,
        // but just in case...
        try {
            builder.addSubmission(SUBMISSION_ID, events, ACTION, METHOD);

            fail("Should throw an exception if trying to add a submission " +
                    "when no models have been defined");
        } catch (XDIMEException e) {
            // do nothing - correct behaviour.
        }
    }

    public void testAddSubmissionWhenIDIsNull() throws XDIMEException {
        builder.addModel(MODEL_ID, TEST_FD);

        try {
            builder.addSubmission(null, events, ACTION, METHOD);
            fail("Should throw an exception if trying to add a submission" +
                    "with a null ID");
        } catch (IllegalArgumentException e) {
            // do nothing - correct behaviour.
        }
    }

    public void testAddSecondSubmissionToOneModel() throws XDIMEException {

        builder.addModel(MODEL_ID, TEST_FD);
        builder.addSubmission(SUBMISSION_ID, events, ACTION, METHOD);

        try {
            builder.addSubmission(SUBMISSION_ID2, events, ACTION, METHOD);
            fail("Should throw an exception if we try to add more than one" +
                    " submission to a model");
        } catch (XDIMEException e) {
            // do nothing - correct behaviour.
        }
    }

    public void testAddMultipleSubmissionsToDifferentModels()
            throws XDIMEException {
        builder.addModel(MODEL_ID, TEST_FD);
        builder.addSubmission(SUBMISSION_ID, events, ACTION, METHOD);

        builder.addModel(MODEL_ID2, TEST_FD2);
        builder.addSubmission(SUBMISSION_ID, events, ACTION, METHOD);
    }

    public void testAddValidSubmission() throws XDIMEException {
        builder.addModel(MODEL_ID, TEST_FD);
        builder.addSubmission(SUBMISSION_ID, events, ACTION, METHOD);
    }

    /**
     * Verifies that calling {@link XFormBuilderImpl#getSubmission} with a null
     * submission causes an exception to be thrown regardless of the model ID.
     *
     * @throws XDIMEException if there was a problem running the test
     */
    public void testGetSubmissionWithNullID() throws XDIMEException {

        builder.addModel(MODEL_ID, TEST_FD);
        try {
            builder.getSubmission(null, MODEL_ID);
            fail("Should throw an exception if attempting to retrieve a " +
                    "submission by specifying a null name");

            builder.getSubmission(null, null);
            fail("Should throw an exception if attempting to retrieve a " +
                    "submission by specifying a null name and model id");
        } catch (XDIMEException e) {
            // do nothing - correct behaviour.
        }
    }

    /**
     * Verifies that if the model id and submissionID refer to a valid
     * submission in a valid model, it is returned.
     */
    public void testGetSubmissionWithNonNullIDAndModelID()
            throws XDIMEException {
        builder.addModel(MODEL_ID, TEST_FD);

        // verifies that if the submission doesn't exist, an exception is thrown
        XFSubmission submission = null;
        try {
            submission = builder.getSubmission(SUBMISSION_ID, MODEL_ID);
            fail("Should not reference a submission that doesn't exist");
        } catch (XDIMEException e) {
            // do nothing - correct behaviour
        }


        builder.addSubmission(SUBMISSION_ID, events, ACTION, METHOD);
        submission = builder.getSubmission(SUBMISSION_ID, MODEL_ID);
        assertNotNull(submission);
    }

    /**
     * Verifies that if the model id refers to a valid model, but the
     * submission id does not match the model's submission ID, then an
     * exception is thrown (even if a submission with that ID exists in
     * another model).
     */
    public void testGetSubmissionWithIDThatExistsButNotInTheSpecifiedModel()
            throws XDIMEException {

        builder.addModel(DEFAULT_MODEL_ID, TEST_FD_DEFAULT);
        builder.addSubmission(SUBMISSION_ID, events, ACTION, METHOD);
        builder.addModel(MODEL_ID, TEST_FD);

        // verifies that an exception is thrown because a submission with the
        // specified id cannot be found in the specified model (even though a
        // submission with that id exists in another model)
        try {
            // XFSubmission submission =
                    builder.getSubmission(SUBMISSION_ID, MODEL_ID);
            fail("Should throw an exception if the specified submission " +
                    "does not match that of the specified model");
        } catch (XDIMEException e) {
            // do nothing - correct behaviour.
        }
    }

    /**
     * Verifies that if the model ID is null, but a submission with the
     * specified id is found in the default model then this submission is
     * returned.
     */
    public void testGetSubmissionWithIDOfSubmissionInTheDefaultModelAndNullModelID()
            throws XDIMEException {

        // create a default model with the requested submission and another
        // model with no submission.
        builder.addModel(DEFAULT_MODEL_ID, TEST_FD_DEFAULT);
        builder.addSubmission(SUBMISSION_ID, events, ACTION, METHOD);
        builder.addModel(MODEL_ID, TEST_FD);

        XFSubmission submission = builder.getSubmission(SUBMISSION_ID, null);
        assertNotNull(submission);
        assertEquals(submission,
                builder.getSubmission(SUBMISSION_ID, DEFAULT_MODEL_ID));
    }

    /**
     * Verifies that if the model ID is null, and the name is present but does
     * not refer to a submission in any of the defined models, an exception is
     * thrown.
     */
    public void testGetSubmissionWithNameOfSubmissionThatDoesNotExistAndNullModelID()
            throws XDIMEException {
        builder.addModel(DEFAULT_MODEL_ID, TEST_FD_DEFAULT);
        builder.addSubmission(SUBMISSION_ID, events, ACTION, METHOD);
        builder.addModel(MODEL_ID, TEST_FD);
        builder.addSubmission(SUBMISSION_ID2, events, ACTION, METHOD);

        try {
            builder.getSubmission("notPresent", null);
            fail("Should throw an exception if attempting to retrieve a " +
                    "submission that does not exist");
        } catch (XDIMEException e) {
            // do nothing - correct behaviour.
        }
    }

    /**
     * Verifies that if the model ID passed to getSubmission is null, but a
     * submission with the specified name exists in another model, then that
     * submission is returned.
     */
    public void testGetSubmissionWithNameOfSubmissionThatExistsAndNullModelID()
            throws XDIMEException {

        builder.addModel(MODEL_ID, TEST_FD);
        builder.addSubmission(SUBMISSION_ID, events, ACTION, METHOD);
        XFSubmission submission = builder.getSubmission(SUBMISSION_ID, null);
        assertNotNull(submission);
    }

    public void testGenerateImplicitElementsWithNoUnreferencedItems()
            throws XDIMEException {
        // add a model
        builder.addModel(MODEL_ID, TEST_FD);
        //  add an item
        builder.addItem(ITEM_NAME, CONTENT);
        // reference it
        SIItem item = builder.getItem(ITEM_NAME, MODEL_ID);
        item.setIsReferenced();

        // verify that no implicit elements are generated
        builder.generateImplicitElements(protocol);
    }

    public void testGenerateImplicitElementsWithUnreferencedItems()
            throws XDIMEException {

        // set expectations
        protocol.fuzzy.doImplicitValue(mockFactory.expectsInstanceOf(
                XFFormFieldAttributes.class)).returns().fixed(2);

        // add a model
        builder.addModel(MODEL_ID, TEST_FD);
        //  add an item
        builder.addItem(ITEM_NAME, CONTENT);

        // add a model
        builder.addModel(MODEL_ID2, TEST_FD2);
        //  add some items
        builder.addItem(ITEM_NAME, CONTENT);
        builder.addItem(ITEM_NAME2, CONTENT);

        // reference one of them
        SIItem item = builder.getItem(ITEM_NAME, MODEL_ID2);
        item.setIsReferenced();

        // verify that the implicit elements are generated correctly
        builder.generateImplicitElements(protocol);
    }

    /**
     * Verify that it is possible to register a control with a null ID.
     */
    public void testRegisterControlWithNullID() throws XDIMEException {
        XDIMEAttributesMock xdimeAttributes = new XDIMEAttributesMock(
                "attributes", expectations);
        xdimeAttributes.expects.getValue("", XDIMEAttribute.ID.toString()).
                returns(null);
        xdimeAttributes.expects.getValue("", XDIMEAttribute.REF.toString()).
                returns(null);
        xdimeAttributes.expects.getValue("", XDIMEAttribute.MODEL.toString()).
                returns(MODEL_ID);
        builder.addModel(MODEL_ID, TEST_FD);
        XFormModel model = builder.registerControl(xdimeAttributes);
        assertEquals(MODEL_ID, model.getID());
    }

    /**
     * Verify that it an exception is thrown if attempting to register a
     * control against a model when nested in a group which references a
     * different model.
     */
    public void testRegisterControlInsideGroupWithDifferentModel()
            throws XDIMEException {
        try {
            XDIMEAttributesMock xdimeAttributes = new XDIMEAttributesMock(
                "xdimeAttributes", expectations);
            XDIMEAttributesMock groupAttributes = new XDIMEAttributesMock(
                "groupAttributes", expectations);
            MarinerPageContextMock pageContext = new MarinerPageContextMock(
                    "pageContext", expectations);
            StylesMock styles = new StylesMock("styles", expectations);
            MutablePropertyValuesMock values = new MutablePropertyValuesMock(
                    "values", expectations);
            DeviceLayoutContextMock dlc =
                    new DeviceLayoutContextMock("dlc", expectations);

            groupAttributes.expects.getValue("", XDIMEAttribute.REF.toString()).
                returns(null);
            groupAttributes.expects.getValue("", XDIMEAttribute.MODEL.toString()).
                returns(MODEL_ID2);

            xdimeAttributes.expects.getValue("", XDIMEAttribute.ID.toString()).
                returns(CONTROL_ID);
            xdimeAttributes.expects.getValue("", XDIMEAttribute.REF.toString()).
                returns(null);
            xdimeAttributes.expects.getValue("", XDIMEAttribute.MODEL.toString()).
                returns(MODEL_ID);

            pageContext.expects.getDeviceLayoutContext().returns(dlc);
            dlc.expects.getInclusionPath().returns(null);
            pageContext.expects.updateFormFragmentationState(MODEL_ID2).returns(null);
            styles.expects.getPropertyValues().returns(values);
            values.expects.getSpecifiedValue(StylePropertyDetails.MCS_BREAK_AFTER).
                    returns(MCSBreakAfterKeywords.ALWAYS);

            builder.addModel(MODEL_ID, TEST_FD);
            builder.addModel(MODEL_ID2, TEST_FD2);
            // in default model
            XFormModel groupModel = builder.registerGroup(groupAttributes);
            groupModel.pushGroup(styles, pageContext);
            builder.registerControl(xdimeAttributes);
            fail("Should throw an exception if attempting to register a " +
                    "control against a model while in a group referencing a " +
                    "different model");
        } catch (XDIMEException e) {
            // do nothing - correct behaviour
        }
    }

    /**
     * Verify that trying to re-register a control with the same id will cause
     * an exception to be thrown, regardless of whether they should appear in
     * different models.
     */
    public void testRegisterSameControlTwice() throws XDIMEException {

        XDIMEAttributesMock xdimeAttributes = new XDIMEAttributesMock(
                "attributes", expectations);
        xdimeAttributes.expects.getValue("", XDIMEAttribute.ID.toString()).
                returns(CONTROL_ID).fixed(3);
        xdimeAttributes.expects.getValue("", XDIMEAttribute.REF.toString()).
                returns(null);
        xdimeAttributes.expects.getValue("", XDIMEAttribute.MODEL.toString()).
                returns(MODEL_ID);

        builder.addModel(MODEL_ID, TEST_FD);
        builder.registerControl(xdimeAttributes);

        try {
            builder.registerControl(xdimeAttributes);
            fail("Should throw an exception if attempting to re-register a " +
                    "control with the same ID");
        } catch (XDIMEException e) {
            // do nothing - correct behaviour
        }

        builder.addModel(MODEL_ID2, TEST_FD2);

        try {
            builder.registerControl(xdimeAttributes);
            fail("Should throw an exception if attempting to re-register a " +
                    "control with the same ID");
        } catch (XDIMEException e) {
            // do nothing - correct behaviour
        }
    }

    public void testRegisterControlsInDifferentModels() throws XDIMEException {
        XFormModel model = builder.addModel(MODEL_ID, TEST_FD);
        XFormModel model2 = builder.addModel(MODEL_ID2, TEST_FD2);
        assertNull(builder.modelBeingWritten);

        XDIMEAttributes attributes =
                new XDIMEAttributesImpl(XFormElements.INPUT);
        attributes.setValue("", XDIMEAttribute.ID.toString(), CONTROL_ID);
        attributes.setValue("", XDIMEAttribute.REF.toString(), null);
        attributes.setValue("", XDIMEAttribute.MODEL.toString(), MODEL_ID);
        builder.registerControl(attributes);
        assertEquals(model, builder.modelBeingWritten);

        XDIMEAttributes attributes2 = new XDIMEAttributesImpl(
                XFormElements.INPUT);
        attributes2.setValue("", XDIMEAttribute.ID.toString(), CONTROL_ID2);
        attributes2.setValue("", XDIMEAttribute.REF.toString(), null);
        attributes2.setValue("", XDIMEAttribute.MODEL.toString(), MODEL_ID2);
        builder.registerControl(attributes2);
        assertEquals(model2, builder.modelBeingWritten);
    }

    public void testRegisterControlsInDifferentModelsWhenSecondModelIsInBadState()
            throws XDIMEException {
        XFormModel model = builder.addModel(MODEL_ID, TEST_FD);
        XFormModel model2 = builder.addModel(MODEL_ID2, TEST_FD2);
        assertNull(builder.modelBeingWritten);

        XDIMEAttributes attributes =
                new XDIMEAttributesImpl(XFormElements.INPUT);
        attributes.setValue("", XDIMEAttribute.ID.toString(), CONTROL_ID);
        attributes.setValue("", XDIMEAttribute.REF.toString(), null);
        attributes.setValue("", XDIMEAttribute.MODEL.toString(), MODEL_ID);
        builder.registerControl(attributes);
        assertEquals(model, builder.modelBeingWritten);

        XDIMEAttributes attributes2 =
                new XDIMEAttributesImpl(XFormElements.INPUT);
        attributes2.setValue("", XDIMEAttribute.ID.toString(), CONTROL_ID2);
        attributes2.setValue("", XDIMEAttribute.REF.toString(), null);
        attributes2.setValue("", XDIMEAttribute.MODEL.toString(), MODEL_ID2);
        builder.registerControl(attributes2);
        assertEquals(model2, builder.modelBeingWritten);

        try {
            builder.registerControl(attributes2);
            fail("Should not be able to register a control unless the model" +
                    "state is BEFORE");
        } catch (XDIMEException e) {
            // do nothing - correct behaviour
        }
    }

    public void testRegisterControlsInSameModel() throws XDIMEException {
        XFormModel model = builder.addModel(MODEL_ID, TEST_FD);
        builder.addModel(MODEL_ID2, TEST_FD2);
        assertNull(builder.modelBeingWritten);

        XDIMEAttributes attributes =
                new XDIMEAttributesImpl(XFormElements.INPUT);
        attributes.setValue("", XDIMEAttribute.ID.toString(), CONTROL_ID);
        attributes.setValue("", XDIMEAttribute.REF.toString(), null);
        attributes.setValue("", XDIMEAttribute.MODEL.toString(), MODEL_ID);
        builder.registerControl(attributes);
        assertEquals(model, builder.modelBeingWritten);

        XDIMEAttributes attributes2 =
                new XDIMEAttributesImpl(XFormElements.INPUT);
        attributes2.setValue("", XDIMEAttribute.ID.toString(), CONTROL_ID2);
        attributes2.setValue("", XDIMEAttribute.REF.toString(), null);
        attributes2.setValue("", XDIMEAttribute.MODEL.toString(), MODEL_ID);
        builder.registerControl(attributes2);
        assertEquals(model, builder.modelBeingWritten);
    }

    public void testUpdateControlWhenNoneHaveBeenRegistered() {
        // this shouldn't happen, but test just in case...
        FieldDescriptor fd = new FieldDescriptor();
        XFFormFieldAttributesMock attributes =
                new XFFormFieldAttributesMock("attributes", expectations);
        try {
            builder.updateControl(fd, attributes);
            fail("Should throw an exception if attempting to update a control" +
                    "when none have been registered");
        } catch (IllegalStateException e) {
            // do nothing - correct behaviour.
        }
    }

    public void testUpdateControl() throws XDIMEException {
        XFormModel model = builder.addModel(MODEL_ID, TEST_FD);
        XDIMEAttributesMock xdimeAttributes = new XDIMEAttributesMock(
                "attributes", expectations);
        xdimeAttributes.expects.getValue("", XDIMEAttribute.ID.toString()).
                returns(CONTROL_ID);
        xdimeAttributes.expects.getValue("", XDIMEAttribute.REF.toString()).
                returns(null);
        xdimeAttributes.expects.getValue("", XDIMEAttribute.MODEL.toString()).
                returns(MODEL_ID);
        builder.registerControl(xdimeAttributes);

        XFFormAttributes attributes = model.getXFFormAttributes();
        EmulatedXFormDescriptor fd = model.getXFormDescriptor();

        assertNotNull(attributes);
        assertEquals(MODEL_ID, attributes.getId());
        assertEquals(MODEL_ID, attributes.getName());
        assertTrue(attributes.getFields().isEmpty());
        assertNotNull(fd);
        assertEquals(MODEL_ID, fd.getContainingFormName());
        assertTrue(fd.getFields().isEmpty());

        FieldDescriptor fieldDescriptor = new FieldDescriptor();
        XFFormFieldAttributesMock fieldAttributes =
                new XFFormFieldAttributesMock("fieldAttributes", expectations);
        builder.updateControl(fieldDescriptor, fieldAttributes);

        final List attFields = attributes.getFields();
        assertFalse(attFields.isEmpty());
        assertEquals(1, attFields.size());
        assertEquals(fieldAttributes, attFields.get(0));

        final List fdFields = fd.getFields();
        assertFalse(fdFields.isEmpty());
        assertEquals(1, fdFields.size());
        assertTrue(fieldDescriptor == fdFields.get(0));
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 02-Dec-05	10542/1	emma	VBM:2005112308 Forward port: Many bug fixes: xforms, GUI and pane styling

 01-Dec-05	10447/1	emma	VBM:2005112308 Many bug fixes: xforms, GUI and pane styling

 10-Oct-05	9673/2	pduffin	VBM:2005092906 Improved validation and fixed layout formatting

 10-Oct-05	9637/1	emma	VBM:2005092807 Adding tests for XForms emulation

 ===========================================================================
*/
