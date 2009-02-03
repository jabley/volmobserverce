/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2007. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.generator.model;

import com.thoughtworks.qdox.model.JavaMethod;
import com.volantis.testtools.mock.generator.GenerationHelper;

import java.util.Set;

public class MockableConstructor
        extends MockableExecutable {

    public MockableConstructor(JavaMethod constructor, Set throwables) {
        super(constructor.getName(), throwables, GenerationHelper.getParameters(
                constructor));
    }
}
