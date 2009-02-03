/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2007. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.mock.samples;

import com.volantis.testtools.mock.test.MockTestCaseAbstract;

public class SampleTestCase
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
}
