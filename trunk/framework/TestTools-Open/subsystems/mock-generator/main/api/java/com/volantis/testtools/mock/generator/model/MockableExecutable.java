/* ----------------------------------------------------------------------------
 * (c) Volantis Systems Ltd 2007. All Rights Reserved.
 * ----------------------------------------------------------------------------
 */

package com.volantis.testtools.mock.generator.model;

import java.util.List;
import java.util.Set;

public class MockableExecutable {
    
    private final String name;
    protected List parameters;
    private Set throwables;

    public MockableExecutable(
            String name, Set throwables, List parameters) {

        this.name = name;
        this.parameters = parameters;

        if (throwables == null || throwables.size() == 0) {
            this.throwables = null;
        } else {
            this.throwables = throwables;
        }
    }

    public List getParameters() {
        return parameters;
    }

    public Set getThrowables() {
        return throwables;
    }

    public String getName() {
        return name;
    }
}
