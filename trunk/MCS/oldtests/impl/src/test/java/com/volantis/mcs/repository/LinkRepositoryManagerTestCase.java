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

import com.volantis.mcs.components.LinkComponent;
import com.volantis.mcs.repository.xml.XMLRepository;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * This class unit tests the LinkRepositoryManager
 */
public class LinkRepositoryManagerTestCase
    extends RepositoryManagerTestAbstract {

    /**
       * Test that adding an LinkComponent adds the component along with a
       * Folder reference.
       */
    public void testAddLinkComponent() throws Exception {
        XMLRepositoryManager xmlRepositoryMgr = new XMLRepositoryManager();
        xmlRepositoryMgr.executeWith(new XMLRepositoryManager.RepositoryCommand() {
            public void execute(RepositoryConnection repositoryConnection,
                                XMLRepository repository,
                                String repositoryDir) throws Exception {
                LinkComponent lc = new LinkComponent("/testAddLinkComponent.mlnk");
                LinkRepositoryManager manager =
                        new LinkRepositoryManager(repository);
                manager.addLinkComponent(repositoryConnection, lc);
                containerExists(lc.getName(), false, repositoryConnection,
                        repository);
            }
        });
    }

    /**
     * Test that removing an LinkComponent removes the component along with
     * the Folder reference.
     */
    public void testRemoveLinkComponent() throws Exception {
        XMLRepositoryManager xmlRepositoryMgr = new XMLRepositoryManager();
        xmlRepositoryMgr.executeWith(new XMLRepositoryManager.RepositoryCommand() {
            public void execute(RepositoryConnection repositoryConnection,
                                XMLRepository repository,
                                String repositoryDir) throws Exception {
                LinkComponent lc = new LinkComponent("/testRemoveLinkComponent.mlnk");
                LinkRepositoryManager manager =
                        new LinkRepositoryManager(repositoryConnection);
                manager.addLinkComponent(repositoryConnection, lc);
                containerExists(lc.getName(), false, repositoryConnection,
                        repository);

                manager.removeLinkComponent(repositoryConnection, lc.getName());

                containerExists(lc.getName(), true, repositoryConnection,
                        repository);
            }
        });
    }

    /**
     * This method runs the entire test suite
     * for the com.volantis.mcs.repository.ImageRepositoryManager class.
     */
    public static Test suite() {
        return new TestSuite(LinkRepositoryManagerTestCase.class);
    }

    public static void main(String args[]) {
        junit.textui.TestRunner.run(suite());
    }

}

/*
 ===========================================================================
 Change History
 ===========================================================================
 $Log$

 11-Oct-05	9729/1	geoff	VBM:2005100507 Mariner Export fails with NPE

 21-Feb-05	6986/2	emma	VBM:2005021411 Changes merged from MCS3.3

 17-Feb-05	6974/3	emma	VBM:2005021411 Reworking tests to use testtools TempFileManager mechanism

 16-Feb-05	6974/1	emma	VBM:2005021411 Fixing tests to provide absolute repository directory

 08-Dec-04	6416/3	ianw	VBM:2004120703 New Build

 08-Dec-04	6416/1	ianw	VBM:2004120703 New Build

 15-Jan-04	2595/1	andy	VBM:2004011404 changed internal representation of policy names

 19-Nov-03	1964/2	mat	VBM:2003111104 Change add/remove methods to use the LinkRepositoryManager

 19-Nov-03	1955/1	mat	VBM:2003111104 Change add/remove methods to use the LinkRepositoryManager

 ===========================================================================
*/
