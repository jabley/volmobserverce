/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2006. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.generated;

import com.volantis.testtools.mock.ExpectationContainer;
import com.volantis.testtools.mock.MockFactory;
import com.volantis.testtools.mock.MockObject;
import com.volantis.testtools.mock.method.MethodCall;
import com.volantis.testtools.mock.test.MockTestHelper;

import java.lang.reflect.UndeclaredThrowableException;

public class GeneratedHelper {

    private static final MockFactory _mockFactory = MockFactory.getDefaultInstance();

    public static boolean equalsMock(MockObject mock, MockObjectConfiguration configuration, Object p0) {

        if (!configuration.equalsShouldCheckExpectations() ||
                MockTestHelper.isInsideFramework()) {
            return mock == p0;
        } else {

            final Object[] _arguments = new Object[]{p0};

            MethodCall methodCall = _mockFactory.createMethodCall(
                    mock, MockObjectBase.EQUALS_METHOD, _arguments);

            try {
                Object _result = doMethodCall(mock, methodCall);
                return ((Boolean) _result).booleanValue();
            } catch (Throwable _throwable) {
                // If result is a subclass of Error, RuntimeException then
                // rethrow it, if it is one of the exceptions that this method
                // declares then cast and rethrow it, otherwise rethrow it as
                // an undeclared throwable exception.
                if (_throwable instanceof RuntimeException) {
                    throw (RuntimeException) _throwable;
                } else if (_throwable instanceof Error) {
                    throw (Error) _throwable;
                } else {
                    throw new UndeclaredThrowableException(_throwable);
                }
            }
        }
    }

    public static int hashCodeMock(MockObject mock, MockObjectConfiguration configuration) {

        if (!configuration.hashCodeShouldCheckExpectations() ||
                MockTestHelper.isInsideFramework()) {
            return System.identityHashCode(mock);
        } else {


            MethodCall methodCall = _mockFactory.createMethodCall(
                    mock, MockObjectBase.HASH_CODE_METHOD, null);

            try {
                Object _result = doMethodCall(mock, methodCall);
                return ((Integer) _result).intValue();
            } catch (Throwable _throwable) {
                // If result is a subclass of Error, RuntimeException then
                // rethrow it, if it is one of the exceptions that this method
                // declares then cast and rethrow it, otherwise rethrow it as
                // an undeclared throwable exception.
                if (_throwable instanceof RuntimeException) {
                    throw (RuntimeException) _throwable;
                } else if (_throwable instanceof Error) {
                    throw (Error) _throwable;
                } else {
                    throw new UndeclaredThrowableException(_throwable);
                }
            }
        }
    }

    public static String toStringMock(MockObject mock, MockObjectConfiguration configuration) {

        if (!configuration.toStringShouldCheckExpectations() ||
                MockTestHelper.isInsideFramework()) {
            return mock._getIdentifier();
        } else {

            MethodCall methodCall = _mockFactory.createMethodCall(
                    mock, MockObjectBase.TO_STRING_METHOD, null);

            try {
                Object _result = doMethodCall(mock, methodCall);
                return (String) _result;
            } catch (Throwable _throwable) {
                // If result is a subclass of Error, RuntimeException then
                // rethrow it, if it is one of the exceptions that this method
                // declares then cast and rethrow it, otherwise rethrow it as
                // an undeclared throwable exception.
                if (_throwable instanceof RuntimeException) {
                    throw (RuntimeException) _throwable;
                } else if (_throwable instanceof Error) {
                    throw (Error) _throwable;
                } else {
                    throw new UndeclaredThrowableException(_throwable);
                }
            }
        }
    }

    private static Object doMethodCall(MockObject mock, MethodCall methodCall)
            throws Throwable {
        Object _result;
        ExpectationContainer _container = mock._getExpectationContainer();
        if (_container == null) {
            _result = MockTestHelper.getGlobalExpectationContainer()
                    .doMethodCall(methodCall);
        } else {
            _result = _container.doMethodCall(methodCall);
        }
        return _result;
    }
}
