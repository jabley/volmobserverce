/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2006. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.generator.model;

import com.thoughtworks.qdox.model.Type;
import com.volantis.testtools.mock.generator.CallUpdaterInfo;
import com.volantis.testtools.mock.generator.CodeUtils;

import java.util.List;
import java.util.Set;

public class MockableMethod
        extends MockableExecutable {

    private final String bindSignature;
    private final CallUpdaterInfo callUpdaterInfo;
    private final boolean unique;
    private String uniqueIdentifier;
    private final Type returnType;
    private final boolean declareIdentifier;
    private final String identifierReference;
    private final boolean addExpectsFuzzy;
    private final String fixedBody;
    private final String mockClass;

    public MockableMethod(
            String name, List parameters, String bindSignature, Set throwables,
            Type returns, CallUpdaterInfo callUpdaterInfo, boolean unique,
            String uniqueIdentifier, boolean declareIdentifier,
            String identifierReference,
            boolean addExpectsFuzzy, String fixedBody,
            String mockClass) {

        super(name, throwables, parameters);

        this.bindSignature = bindSignature;
        this.callUpdaterInfo = callUpdaterInfo;
        this.unique = unique;
        this.uniqueIdentifier = uniqueIdentifier;
        returnType = returns;
        this.declareIdentifier = declareIdentifier;
        this.identifierReference = identifierReference;
        this.addExpectsFuzzy = addExpectsFuzzy;
        this.fixedBody = fixedBody;
        this.mockClass = mockClass;
    }

    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public String getCallUpdaterCreationCode() {
        return callUpdaterInfo.getCreationCode();
    }

    public String getCallUpdaterQualifiedName() {
        return callUpdaterInfo.getQualifiedName();
    }

    public String getBindSignature() {
        return bindSignature;
    }

    public boolean isUnique() {
        return unique;
    }

    public Type getReturnType() {
        return returnType;
    }

    public boolean getDeclareIdentifier() {
        return declareIdentifier;
    }

    public String getIdentifierReference() {
        return identifierReference;
    }

    public boolean getAddExpectsFuzzy() {
        return addExpectsFuzzy;
    }

    public String getFixedBody() {
        return fixedBody;
    }

    public String getMockClass() {
        return mockClass;
    }

    public String unbox(Type type, String name) {
        return CodeUtils.unwrapValue(name,
            CodeUtils.fixInnerClassName(type.toString()));
    }
}
