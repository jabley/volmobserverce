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

package com.volantis.mcs.runtime.policies.expression;

import com.volantis.mcs.expression.PolicyExpression;
import com.volantis.mcs.project.Project;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.xml.expression.Expression;

public class RuntimePolicyReferenceExpression
        implements PolicyExpression {
    
    private final Project project;
    private final MarinerURL baseURL;
    private final String brandName;
    private final Expression expression;
    private final String asString;

    public RuntimePolicyReferenceExpression(
            Project project, MarinerURL baseURL, String brandName,
            Expression expression,
            String asString) {

        if (project == null) {
            throw new IllegalArgumentException("project cannot be null");
        }
        if (baseURL == null) {
            throw new IllegalArgumentException("baseURL cannot be null");
        }
        if (expression == null) {
            throw new IllegalArgumentException("expression cannot be null");
        }
        if (asString == null) {
            throw new IllegalArgumentException("asString cannot be null");
        }

        this.project = project;
        this.baseURL = baseURL;
        this.brandName = brandName;
        this.expression = expression;
        this.asString = asString;
    }

    public String getAsString() {
        return asString;
    }

    public Project getProject() {
        return project;
    }

    public MarinerURL getBaseURL() {
        return baseURL;
    }

    public String getBrandName() {
        return brandName;
    }

    public Expression getExpression() {
        return expression;
    }

    public boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }

    public int hashCode() {
        throw new UnsupportedOperationException();
    }
}
