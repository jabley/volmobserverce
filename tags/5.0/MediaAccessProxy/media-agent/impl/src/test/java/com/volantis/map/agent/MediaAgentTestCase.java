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
package com.volantis.map.agent;

import com.volantis.map.agent.impl.DefaultMediaAgent;
import com.volantis.map.common.encryption.Encrypter;
import com.volantis.map.common.param.Parameters;
import com.volantis.map.common.param.MissingParameterException;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import com.volantis.map.common.param.MissingParameterException;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Collections;

/**
 */
public class MediaAgentTestCase extends TestCaseAbstract {
    public void testSimpleAll() throws Exception {
        final MediaAgent agent = new DefaultMediaAgent(
            new ResourceDescriptorStoreStub(), "http://example.com/");
        final RequestStub request = new RequestStub();
        request.addInputParameter("external-id", "1234-5678-90");
        request.addOutputParameterName("outputParamName1");
        final ResponseCallbackStub responseCallback =
            new ResponseCallbackStub();
        agent.requestURL(request, responseCallback);

        Parameters outputParams = responseCallback.getParameters();
        assertFalse(outputParams.getParameterNames().hasNext());

        final Map expectedOutputParams = new HashMap();
        expectedOutputParams.put(
            "outputParamName1", "value-of-outputParamName1");
        expectedOutputParams.put(
            MediaAgent.OUTPUT_URL_PARAMETER_NAME,
            "http://example.com/" + Encrypter.defaultEncrypt("1234-5678-90"));
        expectedOutputParams.put(
            "an-extra-output-parameter", "value-of-the-extra-param");

        agent.waitForComplete();
        checkExpectedOutputParameters(responseCallback, expectedOutputParams);
    }

    public void testSimpleSingle() throws Exception {
        final MediaAgent agent = new DefaultMediaAgent(
            new ResourceDescriptorStoreStub(), "http://example.com/");
        final RequestStub request = new RequestStub();
        request.addInputParameter("external-id", "1234-5678-90");
        request.addOutputParameterName("outputParamName1");
        final ResponseCallbackStub responseCallback =
            new ResponseCallbackStub();
        final AgentRequestId requestId =
            agent.requestURL(request, responseCallback);

        Parameters outputParams = responseCallback.getParameters();
        assertFalse(outputParams.getParameterNames().hasNext());

        final Map expectedOutputParams = new HashMap();
        expectedOutputParams.put(
            "outputParamName1", "value-of-outputParamName1");
        expectedOutputParams.put(
            MediaAgent.OUTPUT_URL_PARAMETER_NAME,
            "http://example.com/" + Encrypter.defaultEncrypt("1234-5678-90"));
        expectedOutputParams.put(
            "an-extra-output-parameter", "value-of-the-extra-param");

        agent.waitForComplete(requestId);
        checkExpectedOutputParameters(responseCallback, expectedOutputParams);
    }

    public void testSimpleSet() throws Exception {
        final MediaAgent agent = new DefaultMediaAgent(
            new ResourceDescriptorStoreStub(), "http://example.com/");
        final RequestStub request = new RequestStub();
        request.addInputParameter("external-id", "1234-5678-90");
        request.addOutputParameterName("outputParamName1");
        final ResponseCallbackStub responseCallback =
            new ResponseCallbackStub();
        final AgentRequestId requestId =
            agent.requestURL(request, responseCallback);

        Parameters outputParams = responseCallback.getParameters();
        assertFalse(outputParams.getParameterNames().hasNext());

        final Map expectedOutputParams = new HashMap();
        expectedOutputParams.put(
            "outputParamName1", "value-of-outputParamName1");
        expectedOutputParams.put(
            MediaAgent.OUTPUT_URL_PARAMETER_NAME,
            "http://example.com/" + Encrypter.defaultEncrypt("1234-5678-90"));
        expectedOutputParams.put(
            "an-extra-output-parameter", "value-of-the-extra-param");

        agent.waitForComplete(Collections.singleton(requestId));
        checkExpectedOutputParameters(responseCallback, expectedOutputParams);
    }

    public void testMultipleCall() throws Exception {
        final MediaAgent agent = new DefaultMediaAgent(
            new ResourceDescriptorStoreStub(), "http://example.com/");
        final RequestStub request = new RequestStub();
        request.addInputParameter("external-id", "1234-5678-90");
        request.addOutputParameterName("outputParamName1");
        final ResponseCallbackStub responseCallback =
            new ResponseCallbackStub();
        final AgentRequestId requestId =
            agent.requestURL(request, responseCallback);

        Parameters outputParams = responseCallback.getParameters();
        assertFalse(outputParams.getParameterNames().hasNext());

        final Map expectedOutputParams = new HashMap();
        expectedOutputParams.put(
            "outputParamName1", "value-of-outputParamName1");
        expectedOutputParams.put(
            MediaAgent.OUTPUT_URL_PARAMETER_NAME,
            "http://example.com/" + Encrypter.defaultEncrypt("1234-5678-90"));
        expectedOutputParams.put(
            "an-extra-output-parameter", "value-of-the-extra-param");

        agent.waitForComplete(requestId);
        checkExpectedOutputParameters(responseCallback, expectedOutputParams);
        responseCallback.failOnExecute(true);
        agent.waitForComplete(requestId);
    }

    private void checkExpectedOutputParameters(
                final ResponseCallbackStub responseCallback,
                final Map expectedOutputParams)
            throws MissingParameterException {

        Parameters outputParams;
        outputParams = responseCallback.getParameters();
        for (Iterator iter = outputParams.getParameterNames(); iter.hasNext(); ) {
            final String name = (String) iter.next();
            final String value = outputParams.getParameterValue(name);
            final String expectedValue =
                (String) expectedOutputParams.remove(name);
            assertEquals(expectedValue, value);
        }
        assertTrue(expectedOutputParams.isEmpty());
    }
}
