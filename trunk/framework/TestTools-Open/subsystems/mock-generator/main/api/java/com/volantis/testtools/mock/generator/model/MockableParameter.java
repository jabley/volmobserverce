/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2007. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.generator.model;

import com.thoughtworks.qdox.model.JavaParameter;
import com.volantis.testtools.mock.generator.CodeUtils;

public class MockableParameter {

    private JavaParameter parameter;

    public MockableParameter(JavaParameter parameter) {
        this.parameter = parameter;
    }

    public String getName() {
        return parameter.getName();
    }

    public String getType() {
        return CodeUtils.fixInnerClassName(parameter.getType().toString());
    }

    public String getComponentName() {
        final String className =
            parameter.getType().getJavaClass().getFullyQualifiedName();
        return CodeUtils.fixInnerClassName(className);
    }

    public int getDimensions() {
        return parameter.getType().getDimensions();
    }

    public String box() {
        return CodeUtils.wrapValue(parameter.getName(),
                CodeUtils.fixInnerClassName(parameter.getType().toString()));
    }
}
