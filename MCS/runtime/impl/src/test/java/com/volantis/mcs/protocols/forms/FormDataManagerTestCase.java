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
 * (c) Volantis Systems Ltd 2006. 
 * ----------------------------------------------------------------------------
 */
package com.volantis.mcs.protocols.forms;

import com.volantis.synergetics.testtools.TestCaseAbstract;

/**
 * Verifies that the {@link FormDataManager} works as expected.
 */
public class FormDataManagerTestCase extends TestCaseAbstract {

    private FormDataManager manager;
    private FormDescriptor fd1;
    private FormDescriptor fd2;

    protected void setUp() throws Exception {
        super.setUp();
        manager = new FormDataManager();
        fd1 = new FormDescriptor();
        fd2 = new FormDescriptor();
    }

    /**
     * Verify that {@link FormDataManager#getSessionFormData} returns the
     * correct data when the form specifier is valid and refers to a form data
     * that has already been created with some form data.
     */
    public void testGetFormDataWhenFormDataExists() {
        // verify that the manager is managing data for any forms at the moment
        assertFalse(manager.getFormSpecifiers().hasNext());

        // Add some form descriptors to the cache (retrieving the specifier
        // will cause the FD to be added to the cache if not already present).
        String specifier1 = manager.getFormSpecifier(fd1);
        String specifier2 = manager.getFormSpecifier(fd2);

        SessionFormData formData1 = new SessionFormData(specifier1, fd1);

        manager.getForms().put(specifier1, formData1);

        assertEquals(formData1, manager.getSessionFormData(specifier1));
    }

    /**
     * Verify that {@link FormDataManager#getSessionFormData} returns a new,
     * empty data set when called with a valid form specifier for which no form
     * data has yet been stored.
     */
    public void testGetFormDataWhenFormDataDoesNotExist() {
        // verify that the manager is managing data for any forms at the moment
        assertFalse(manager.getFormSpecifiers().hasNext());

        // Add some form descriptors to the cache (retrieving the specifier
        // will cause the FD to be added to the cache if not already present).
        String specifier1 = manager.getFormSpecifier(fd1);
        String specifier2 = manager.getFormSpecifier(fd2);

        SessionFormData formData = manager.getSessionFormData(specifier1);
        assertNotNull(formData);
        assertEquals(specifier1, formData.getFormSpecifier());
        assertNull(formData.getFormFields());
    }

    /**
     * Verify that {@link FormDataManager#getSessionFormData} throws an
     * exception if it is called with a form specifier which does not map to an
     * index into the form descriptor cache.
     */
    public void testGetFormDataWhenSpecifierInvalid() {
        // verify that the manager is managing data for any forms at the moment
        assertFalse(manager.getFormSpecifiers().hasNext());

        // Add some form descriptors to the cache (retrieving the specifier
        // will cause the FD to be added to the cache if not already present).
        String specifier1 = manager.getFormSpecifier(fd1);
        String specifier2 = manager.getFormSpecifier(fd2);

        final String invalidSpecifier = "s11";
        assertNotEquals(specifier1, invalidSpecifier);
        assertNotEquals(specifier2, invalidSpecifier);

        try {
            manager.getSessionFormData(invalidSpecifier);
            fail("getSessionFormData was called with an invalid specifier" +
                    " and should have thrown an exception");
        } catch (MissingFormDataException e) {
            // do nothing, correct behaviour.
        }

        final String nonNumericSpecifier = "hello";
        assertNotEquals(specifier1, nonNumericSpecifier);
        assertNotEquals(specifier2, nonNumericSpecifier);

        try {
            manager.getSessionFormData(nonNumericSpecifier);
            fail("getSessionFormData was called with an invalid specifier" +
                    " and should have thrown an exception");
        } catch (MissingFormDataException e) {
            // do nothing, correct behaviour.
        }
    }
}
