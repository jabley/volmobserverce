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
/** (c) Volantis Systems Ltd 2003.  */
package com.volantis.mcs.repository;

import com.volantis.mcs.accessors.PolicyBuilderAccessor;
import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.project.LocalPolicySource;
import com.volantis.mcs.project.InternalProject;
import com.volantis.mcs.repository.xml.XMLRepository;
import junit.framework.TestCase;

/**
 * Abstract class for RepositoryManager test cases.
 * 
 * @author mat
 *
 */
public abstract class RepositoryManagerTestAbstract extends TestCase {

    /**
         * Convenience method for testing that components exist or do not exist
         * as required.
         * @param name The name of the component container.
     * @param isNegativeTest If true the test will fail if the component does
     */
    public void containerExists(String name,
                                boolean isNegativeTest,
                                RepositoryConnection repositoryConnection,
                                XMLRepository repository) {
    
        InternalProject project = (InternalProject)
                repository.getDefaultProject();
        LocalPolicySource source = (LocalPolicySource)
                project.getPolicySource();

        PolicyBuilderAccessor accessor = source.getPolicyBuilderAccessor();

        try {
            PolicyBuilder policyBuilder = accessor.retrievePolicyBuilder(
                    repositoryConnection.getUnderLyingConnection(), 
                    repository.getDefaultProject(),
                    name);
    
            if(policyBuilder!=null && isNegativeTest) {
                fail("Component " + name + " exists when it should not.");
            } else if(!isNegativeTest) {
                assertEquals(name, policyBuilder.getName());
            }
        } catch(RepositoryException e) {
            assertTrue("Component " + name + " does not exist when " +
                       "it should", !isNegativeTest);
            if(!isNegativeTest) {
                e.printStackTrace();
            }
        }
    }
}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Oct-05	9729/1	geoff	VBM:2005100507 Mariner Export fails with NPE

 21-Feb-05	6986/2	emma	VBM:2005021411 Changes merged from MCS3.3

 18-Feb-05	6974/5	emma	VBM:2005021411 Making the device repository and xml policies locations relative to mcs-config.xml

 17-Feb-05	6974/3	emma	VBM:2005021411 Reworking tests to use testtools TempFileManager mechanism

 16-Feb-05	6974/1	emma	VBM:2005021411 Fixing tests to provide absolute repository directory

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 11-Jun-04	4678/1	geoff	VBM:2004061001 old gui cleanup: remove folder support code

 29-Jan-04	2749/2	geoff	VBM:2003121704 Import/Export: XML Accessors: Add support for the default xml project

 13-Jan-04	2573/4	andy	VBM:2003121907 renamed file variables to directory

 13-Jan-04	2573/1	andy	VBM:2003121907 removed remnants of single file support

 23-Dec-03	2252/3	andy	VBM:2003121703 change to default name for non existant repository in test suite

 23-Dec-03	2252/1	andy	VBM:2003121703 removed policy desriptor file, removed single-file support, flattened xml repository structure

 19-Nov-03	1955/1	mat	VBM:2003111104 Change add/remove methods to use the LinkRepositoryManager

 ===========================================================================
*/
