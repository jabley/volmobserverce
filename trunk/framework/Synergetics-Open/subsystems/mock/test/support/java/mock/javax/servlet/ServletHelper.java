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


package mock.javax.servlet;

import com.volantis.testtools.mock.ExpectationBuilder;
import com.volantis.testtools.mock.expectations.UnorderedExpectations;

/**
 * An class that provides method to help with using mock objects within this
 * library.
 */
public class ServletHelper {


    /**
     * Setup the expectations for a Pseudo Servlet environment.
     * <p>This method should be called prior to testing a Servlet</p>
     *
     * @param expectations The {@link ExpectationBuilder} on which to set the
     * expectations
     * @param servletConfigMock The mock ServletConfig object.
     * @param servletContextMock  The mock ServletContext object.
     * @param servletClass The servlet to be tested.
     */
    public static void setExpectedResults(
            final ExpectationBuilder expectations,
            final ServletConfigMock servletConfigMock,
            final ServletContextMock servletContextMock,
            final Class servletClass) {


        if (expectations == null) {
            throw new IllegalArgumentException("expectations cannot be null");
        }

        if (servletConfigMock == null) {
            throw new IllegalArgumentException("servletConfigMock cannot" +
                    " be null");
        }

        if (servletContextMock == null) {
            throw new IllegalArgumentException("servletContextMock cannot" +
                    " be null");
        }

        if (servletClass == null) {
            throw new IllegalArgumentException("servletClass cannot be null");
        }

        final String className = servletClass.getName();

        expectations.add(new UnorderedExpectations() {

            public void add() {
                servletConfigMock
                        .expects
                        .getServletContext()
                        .returns(servletContextMock)
                        .any();

                servletConfigMock
                        .expects
                        .getServletName()
                        .returns(className)
                        .any();

                servletContextMock.expects.log(className + ": init").any();

            }


        });
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Jul-05	8616/3	ianw	VBM:2005060103 Fixed Javadoc as per review comments

 01-Jul-05	8616/1	ianw	VBM:2005060103 New page level CSS servlet

 ===========================================================================
*/
