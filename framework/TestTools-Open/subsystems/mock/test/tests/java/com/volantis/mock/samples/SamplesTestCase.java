/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2007. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mock.samples;

import com.volantis.testtools.mock.test.MockTestCaseAbstract;

public class SamplesTestCase
        extends MockTestCaseAbstract {

    public void testCreation() throws Exception {
        new CircleMock("circleMock", expectations);
        new ClassWithAnotherMethodMock("classWithAnotherMethodMock",
                expectations);
        new ClassWithCloneAndExceptionMock("classWithCloneAndExceptionMock",
                expectations);
        new ClassWithCloneNoExceptionMock("classWithCloneNoExceptionMock",
                expectations);
        new ClassWithCtorsMock("classWithCtorsMock", expectations);
        new ClassWithFinalObjectMethodsMock("classWithFinalObjectMethodsMock",
                expectations);
        new ClassWithMultipleInterfaces1Mock("classWithMultipleInterfacesMock1",
                expectations);
        new ClassWithoutMethodMock("classWithoutMethodMock", expectations);
        new Interface1Mock("interface1Mock", expectations);
        new Interface2Mock("interface2Mock", expectations);
        new Interface4Mock("interface4Mock", expectations);
        new InterfaceExtendingInterfaceWithMethodThrowsMock(
                "interfaceExtendingInterfaceWithMethodThrowsMock",
                expectations);
        new InterfaceFuzzyByExtensionMock("interfaceFuzzyByExtensionMock",
                expectations);
        new InterfaceNotFuzzyMock("interfaceNotFuzzyMock", expectations);
        new InterfaceWithArrayClashMock("interfaceWithArrayClashMock",
                expectations);
        new InterfaceWithCaseClashMock("interfaceWithCaseClashMock",
                expectations);
        new InterfaceWithClashingMethodsMock("interfaceWithClashingMethodsMock",
                expectations);
        new InterfaceWithCloneAndExceptionMock(
                "interfaceWithCloneAndExceptionMock", expectations);
        new InterfaceWithCloneNoExceptionMock(
                "interfaceWithCloneNoExceptionMock", expectations);
        new InterfaceWithEqualsMock("interfaceWithEqualsMock", expectations);
        new InterfaceWithHashCodeMock("interfaceWithHashCodeMock",
                expectations);
        new InterfaceWithMethodMock("interfaceWithMethodMock", expectations);
        new InterfaceWithMethodThrowsMock("interfaceWithMethodThrowsMock",
                expectations);
        new InterfaceWithMultipleInterfaces1Mock(
                "interfaceWithMultipleInterfaces1Mock", expectations);
        new InterfaceWithMultipleInterfaces2Mock(
                "interfaceWithMultipleInterfaces2Mock", expectations);
        new InterfaceWithToStringMock("interfaceWithToStringMock",
                expectations);
        new InterfaceWithWeirdNamesMock("interfaceWithWeirdNamesMock",
                expectations);
        new MarkerInterfaceMock("markerInterfaceMock", expectations);
        new ShapeMock("shapeMock", expectations);
        new SquareMock("squareMock", expectations);
    }


    /**
     * Ensure that multiple interfaces works correctly.
     */
    public void testMultipleInterfaces() throws Exception {

        final ClassWithMultipleInterfaces1Mock classMock =
                new ClassWithMultipleInterfaces1Mock("classMock", expectations);

        Interface1Mock.Expects mock1Expects = classMock.expects;
        Interface2Mock.Expects mock2Expects = classMock.expects;
        Interface3Mock.Expects mock3Expects = classMock.expects;
        InterfaceWithMultipleInterfaces1Mock.Expects multipleMockExpects =
                classMock.expects;

        assertSame(mock1Expects, mock2Expects);
        assertSame(mock3Expects, multipleMockExpects);
    }

    /**
     * Ensure that multiple interfaces works correctly.
     */
    public void testCloneMethod() {

        // =====================================================================
        //   Create Mocks
        // =====================================================================

        final InterfaceWithCloneNoExceptionMock interfaceCloneNoExceptionMock =
                new InterfaceWithCloneNoExceptionMock(
                        "interfaceCloneNoExceptionMock", expectations);

        final InterfaceWithCloneAndExceptionMock interfaceCloneWithExceptionMock =
                new InterfaceWithCloneAndExceptionMock(
                        "interfaceCloneWithExceptionMock", expectations);

        final ClassWithCloneNoExceptionMock classCloneNoExceptionMock =
                new ClassWithCloneNoExceptionMock(
                        "classCloneNoExceptionMock", expectations);

        final ClassWithCloneAndExceptionMock classCloneWithExceptionMock =
                new ClassWithCloneAndExceptionMock(
                        "classCloneWithExceptionMock", expectations);

        // =====================================================================
        //   Set Expectations
        // =====================================================================

        interfaceCloneNoExceptionMock.expects._clone().returns(this);
        interfaceCloneWithExceptionMock.expects._clone().returns(this);
        classCloneNoExceptionMock.expects._clone().returns(this);
        classCloneWithExceptionMock.expects._clone().returns(this);

        // =====================================================================
        //   Test Expectations
        // =====================================================================

        assertSame(this, interfaceCloneNoExceptionMock.clone());
        try {
            assertSame(this, interfaceCloneWithExceptionMock.clone());
        } catch (CloneNotSupportedException unexpected) {
            fail("Clone exception was unexpected");
        }

        assertSame(this, classCloneNoExceptionMock.clone());
        try {
            assertSame(this, classCloneWithExceptionMock.clone());
        } catch (CloneNotSupportedException unexpected) {
            fail("Clone exception was unexpected");
        }
    }
}
