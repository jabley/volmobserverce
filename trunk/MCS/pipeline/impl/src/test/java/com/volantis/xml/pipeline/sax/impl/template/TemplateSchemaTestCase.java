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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.xml.pipeline.sax.impl.template;

import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.xml.pipeline.sax.impl.validation.State;
import com.volantis.xml.pipeline.sax.impl.validation.Event;

public class TemplateSchemaTestCase
        extends TestCaseAbstract {

    private void checkExpects(State start, Event event, State expected) {
        State actual = start.transition(event);
        assertEquals("Did not expect to move to state " + actual +
                " from " + start + " on event " + event,
                expected, actual);
    }

    /**
     * Ensure that the state behaves correctly.
     */
    public void testExpectApplyStart() {
        State state = TemplateSchema.EXPECT_APPLY_START;

        checkExpects(state, TemplateSchema.APPLY_START, TemplateSchema.EXPECT_BINDINGS_START);
        checkExpects(state, TemplateSchema.APPLY_END, null);
    }

    /**
     * Ensure that the state behaves correctly.
     */
    public void testExpectApplyEnd() {
        State state = TemplateSchema.EXPECT_APPLY_END;

        checkExpects(state, TemplateSchema.APPLY_END, State.DONE);
        checkExpects(state, TemplateSchema.APPLY_START, null);
    }

    /**
     * Ensure that the state behaves correctly.
     */
    public void testExpectBindingsStart() {
        State state = TemplateSchema.EXPECT_BINDINGS_START;

        checkExpects(state, TemplateSchema.BINDINGS_START,
                TemplateSchema.EXPECT_BINDING_START_OR_BINDINGS_END);
        checkExpects(state, TemplateSchema.APPLY_START, null);
    }

    /**
     * Ensure that the state behaves correctly.
     */
    public void testExpectBindingStartOrBindingsEnd() {
        State state = TemplateSchema.EXPECT_BINDING_START_OR_BINDINGS_END;

        checkExpects(state, TemplateSchema.BINDING_START,
                TemplateSchema.EXPECT_BINDING_VALUE_START);
        checkExpects(state, TemplateSchema.BINDINGS_END, TemplateSchema.EXPECT_DEFINITION_START);
        checkExpects(state, TemplateSchema.APPLY_START, null);
    }

    /**
     * Ensure that the state behaves correctly.
     */
    public void testExpectBindingValueStart() {
        State state = TemplateSchema.EXPECT_BINDING_VALUE_START;

        checkExpects(state, TemplateSchema.VALUE_START, TemplateSchema.EXPECT_BINDING_VALUE_END);
        checkExpects(state, TemplateSchema.APPLY_START, null);
    }

    /**
     * Ensure that the state behaves correctly.
     */
    public void testExpectBindingValueEnd() {
        State state = TemplateSchema.EXPECT_BINDING_VALUE_END;

        checkExpects(state, TemplateSchema.VALUE_END, TemplateSchema.EXPECT_BINDING_END);
        checkExpects(state, TemplateSchema.APPLY_START, null);
    }

    /**
     * Ensure that the state behaves correctly.
     */
    public void testExpectBindingEnd() {
        State state = TemplateSchema.EXPECT_BINDING_END;

        checkExpects(state, TemplateSchema.BINDING_END,
                TemplateSchema.EXPECT_BINDING_START_OR_BINDINGS_END);
        checkExpects(state, TemplateSchema.APPLY_END, null);
    }

    /**
     * Ensure that the state behaves correctly.
     */
    public void testExpectDefinitionStart() {
        State state = TemplateSchema.EXPECT_DEFINITION_START;

        checkExpects(state, TemplateSchema.DEFINITION_START,
                TemplateSchema.EXPECT_DECLARATIONS_OR_BODY_START);
        checkExpects(state, TemplateSchema.APPLY_START, null);
    }

    /**
     * Ensure that the state behaves correctly.
     */
    public void testExpectDeclarationsOrBodyStart() {
        State state = TemplateSchema.EXPECT_DECLARATIONS_OR_BODY_START;

        checkExpects(state, TemplateSchema.DECLARATIONS_START,
                TemplateSchema.EXPECT_PARAMETER_START_OR_DECLARATIONS_END);
        checkExpects(state, TemplateSchema.BODY_START, TemplateSchema.EXPECT_BODY_END);
        checkExpects(state, TemplateSchema.APPLY_START, null);
    }

    /**
     * Ensure that the state behaves correctly.
     */
    public void testExpectParameterStartOrDeclarationsEnd() {
        State state = TemplateSchema.EXPECT_PARAMETER_START_OR_DECLARATIONS_END;

        checkExpects(state, TemplateSchema.PARAMETER_START,
                TemplateSchema.EXPECT_PARAMETER_VALUE_START);
        checkExpects(state, TemplateSchema.DECLARATIONS_END, TemplateSchema.EXPECT_BODY_START);
        checkExpects(state, TemplateSchema.APPLY_START, null);
    }

    /**
     * Ensure that the state behaves correctly.
     */
    public void testExpectParameterValueStart() {
        State state = TemplateSchema.EXPECT_PARAMETER_VALUE_START;

        checkExpects(state, TemplateSchema.VALUE_START,
                TemplateSchema.EXPECT_PARAMETER_VALUE_END);
        checkExpects(state, TemplateSchema.APPLY_START, null);
    }

    /**
     * Ensure that the state behaves correctly.
     */
    public void testExpectParameterValueEnd() {
        State state = TemplateSchema.EXPECT_PARAMETER_VALUE_END;

        checkExpects(state, TemplateSchema.VALUE_END, TemplateSchema.EXPECT_PARAMETER_END);
        checkExpects(state, TemplateSchema.APPLY_END, null);
    }

    /**
     * Ensure that the state behaves correctly.
     */
    public void testExpectParameterEnd() {
        State state = TemplateSchema.EXPECT_PARAMETER_END;

        checkExpects(state, TemplateSchema.PARAMETER_END,
                TemplateSchema.EXPECT_PARAMETER_START_OR_DECLARATIONS_END);
        checkExpects(state, TemplateSchema.APPLY_END, null);
    }

    /**
     * Ensure that the state behaves correctly.
     */
    public void testExpectBodyStart() {
        State state = TemplateSchema.EXPECT_BODY_START;

        checkExpects(state, TemplateSchema.BODY_START, TemplateSchema.EXPECT_BODY_END);
        checkExpects(state, TemplateSchema.APPLY_END, null);
    }

    /**
     * Ensure that the state behaves correctly.
     */
    public void testExpectBodyEnd() {
        State state = TemplateSchema.EXPECT_BODY_END;

        checkExpects(state, TemplateSchema.BODY_END, TemplateSchema.EXPECT_DEFINITION_END);
        checkExpects(state, TemplateSchema.APPLY_END, null);
    }

    /**
     * Ensure that the state behaves correctly.
     */
    public void testExpectDefinitionEnd() {
        State state = TemplateSchema.EXPECT_DEFINITION_END;

        checkExpects(state, TemplateSchema.DEFINITION_END, TemplateSchema.EXPECT_APPLY_END);
        checkExpects(state, TemplateSchema.APPLY_END, null);
    }
}
