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

package com.volantis.mcs.xml.schema.impl.validation;

import com.volantis.mcs.xml.schema.impl.compiler.CompiledSchemaInternalMock;
import com.volantis.mcs.xml.schema.model.Content;
import com.volantis.mcs.xml.schema.model.ElementType;
import com.volantis.mcs.xml.schema.validation.DocumentValidator;
import com.volantis.mcs.xml.schema.validation.ValidationException;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.testtools.mock.expectations.OrderedExpectations;

public class DocumentValidatorTestCase
        extends TestCaseAbstract {
    private CompiledSchemaInternalMock compiledSchemaMock;
    private ElementValidatorMock validatorMock;
    private ElementType elementType;


    protected void setUp() throws Exception {
        super.setUp();

        compiledSchemaMock = new CompiledSchemaInternalMock(
                "compiledSchemaMock", expectations);

        validatorMock = new ElementValidatorMock("validatorMock", expectations);

        validatorMock.expects.hasExcludes().returns(false).any();

        elementType = new ElementType("", "e", "e");

    }

    /**
     * Ensure that when multiple consecutive blocks of non whitespace
     * characters are validated that the element validator is only asked about
     * one.
     */
    public void testCoalescingNonWhitespaceCharacterData() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        compiledSchemaMock.expects.getElementValidator(elementType)
                .returns(validatorMock).any();

        expectations.add(new OrderedExpectations() {
            public void add() {
                validatorMock.expects.open(null);
                {
                    // The character data is required (not whitespace) and was
                    // consumed (returns true).
                    validatorMock.expects.content(Content.PCDATA, true)
                            .returns(true);
                }
                validatorMock.expects.close();
            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        DocumentValidator validator = new DocumentValidatorImpl(
                compiledSchemaMock);

        validator.open(elementType);
        assertTrue(validator.pcdata(false));
        assertTrue(validator.pcdata(false));
        assertTrue(validator.pcdata(false));
        validator.close(elementType);
    }

    /**
     * Ensure that when multiple consecutive blocks of consumed whitespace
     * characters are validated that the element validator is only asked about
     * one.
     */
    public void testCoalescingAllowedWhitespaceCharacterData() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        compiledSchemaMock.expects.getElementValidator(elementType)
                .returns(validatorMock).any();

        expectations.add(new OrderedExpectations() {
            public void add() {
                validatorMock.expects.open(null);
                {
                    // The character data is not required (whitespace) and was
                    // consumed (returns true).
                    validatorMock.expects.content(Content.PCDATA, false)
                            .returns(true);
                }
                validatorMock.expects.close();
            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        DocumentValidator validator = new DocumentValidatorImpl(
                compiledSchemaMock);

        validator.open(elementType);
        assertTrue(validator.pcdata(true));
        assertTrue(validator.pcdata(true));
        assertTrue(validator.pcdata(true));
        validator.close(elementType);
    }

    /**
     * Ensure that when multiple consecutive blocks of ignorable whitespace
     * characters are validated that the element validator is only asked about
     * one.
     */
    public void testCoalescingIgnorableWhitespaceCharacterData() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        compiledSchemaMock.expects.getElementValidator(elementType)
                .returns(validatorMock).any();

        expectations.add(new OrderedExpectations() {
            public void add() {
                validatorMock.expects.open(null);
                {
                    // The character data is not required (whitespace) and was
                    // not consumed (returns false).
                    validatorMock.expects.content(Content.PCDATA, false)
                            .returns(false);
                }
                validatorMock.expects.close();
            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        DocumentValidator validator = new DocumentValidatorImpl(
                compiledSchemaMock);

        validator.open(elementType);
        assertFalse(validator.pcdata(true));
        assertFalse(validator.pcdata(true));
        assertFalse(validator.pcdata(true));
        validator.close(elementType);
    }

    /**
     * Ensure that when consecutive blocks of first unconsumed whitespace
     * and then non whitespace characters are validated that the element
     * validator is asked about both. This is needed because the first block is
     * ignored because it is whitespace and character data is not supported but
     * the second block should not fail.
     */
    public void testCoalescingMixtureCharacterData() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        compiledSchemaMock.expects.getElementValidator(elementType)
                .returns(validatorMock).any();

        expectations.add(new OrderedExpectations() {
            public void add() {
                validatorMock.expects.open(null);
                {
                    // The character data is not required (whitespace) and was
                    // not consumed because it is not allowed (returns false).
                    validatorMock.expects.content(Content.PCDATA, false)
                            .returns(false);

                    // The character data is required (not whitespace) but as
                    // the previous whitespace was not consumed because
                    // character data is not allowed this should fail.
                    validatorMock.expects.content(Content.PCDATA, true)
                            .fails(new ValidationException(
                                    "PCDATA not allowed"));
                }
                validatorMock.expects.close();
            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        DocumentValidator validator = new DocumentValidatorImpl(
                compiledSchemaMock);

        validator.open(elementType);
        assertFalse(validator.pcdata(true));
        try {
            validator.pcdata(false);
        } catch (ValidationException e) {
            assertEquals("PCDATA not allowed", e.getMessage());
        }
        validator.close(elementType);
    }

    /**
     * Ensure that character data before a start tag and after a start tag
     * are not coalesced together.
     */
    public void testNoCoalescingAcrossStartElement() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        compiledSchemaMock.expects.getElementValidator(elementType)
                .returns(validatorMock).any();

        expectations.add(new OrderedExpectations() {
            public void add() {
                validatorMock.expects.open(null);
                {
                    // The character data is required (not whitespace) and
                    // was consumed (returns true).
                    validatorMock.expects.content(Content.PCDATA, true)
                            .returns(true);

                    validatorMock.expects.open(validatorMock);
                    {
                        // The character data is not required (whitespace) and
                        // was not consumed (returns false).
                        validatorMock.expects.content(Content.PCDATA, false)
                                .returns(false);
                    }
                    validatorMock.expects.close();

                }
                validatorMock.expects.close();
            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        DocumentValidator validator = new DocumentValidatorImpl(
                compiledSchemaMock);


        validator.open(elementType);
        {
            // Content is not whitespace so must be valid and will be consumed.
            assertTrue(validator.pcdata(false));

            validator.open(elementType);
            {
                // Content is whitespace and is not valid and so won't be
                // consumed.
                assertFalse(validator.pcdata(true));
            }
            validator.close(elementType);
        }
        validator.close(elementType);
    }

    /**
     * Ensure that character data before a end tag and after a end tag
     * are not coalesced together.
     */
    public void testNoCoalescingAcrossEndElement() {

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        compiledSchemaMock.expects.getElementValidator(elementType)
                .returns(validatorMock).any();

        expectations.add(new OrderedExpectations() {
            public void add() {
                validatorMock.expects.open(null);
                {
                    validatorMock.expects.open(validatorMock);
                    {
                        // The character data is required (not whitespace) and
                        // was consumed (returns true).
                        validatorMock.expects.content(Content.PCDATA, true)
                                .returns(true);
                    }
                    validatorMock.expects.close();

                    // The character data is not required (whitespace) and was
                    // not consumed (returns false).
                    validatorMock.expects.content(Content.PCDATA, false)
                            .returns(false);
                }
                validatorMock.expects.close();
            }
        });

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        DocumentValidator validator = new DocumentValidatorImpl(
                compiledSchemaMock);

        validator.open(elementType);
        {
            validator.open(elementType);
            {
                // Content is not whitespace so must be valid and will be
                // consumed.
                assertTrue(validator.pcdata(false));
            }
            validator.close(elementType);

            // Content is whitespace and is not valid and so won't be consumed.
            assertFalse(validator.pcdata(true));
        }
        validator.close(elementType);
    }
}
