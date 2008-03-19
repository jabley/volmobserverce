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
 * (c) Volantis Systems Ltd 2007. 
 * ----------------------------------------------------------------------------
 */

package com.volantis.mcs.xdime.xhtml2;

import com.volantis.map.agent.MediaAgent;
import com.volantis.map.agent.MediaAgentMock;
import com.volantis.map.agent.Request;
import com.volantis.map.agent.RequestFactory;
import com.volantis.map.agent.RequestMock;
import com.volantis.map.agent.ResponseCallback;
import com.volantis.map.agent.impl.DefaultAgentParameters;
import com.volantis.mcs.devices.InternalDeviceMock;
import com.volantis.mcs.integration.PageURLType;
import com.volantis.mcs.policies.PolicyFactory;
import com.volantis.mcs.policies.variants.Variant;
import com.volantis.mcs.policies.variants.VariantBuilder;
import com.volantis.mcs.policies.variants.VariantType;
import com.volantis.mcs.policies.variants.audio.AudioEncoding;
import com.volantis.mcs.policies.variants.audio.AudioMetaDataBuilder;
import com.volantis.mcs.policies.variants.content.BaseLocation;
import com.volantis.mcs.policies.variants.content.URLContentBuilder;
import com.volantis.mcs.policies.variants.image.ImageEncoding;
import com.volantis.mcs.policies.variants.image.ImageMetaDataBuilder;
import com.volantis.mcs.policies.variants.text.TextEncoding;
import com.volantis.mcs.policies.variants.text.TextMetaDataBuilder;
import com.volantis.mcs.policies.variants.video.VideoEncoding;
import com.volantis.mcs.policies.variants.video.VideoMetaDataBuilder;
import com.volantis.mcs.protocols.assets.implementation.LiteralTextAssetReference;
import com.volantis.mcs.runtime.PluggableAssetTranscoderManager;
import com.volantis.mcs.runtime.policies.RuntimePolicyReferenceMock;
import com.volantis.mcs.runtime.policies.SelectedVariantMock;
import com.volantis.mcs.utilities.MarinerURL;
import com.volantis.testtools.mock.method.MethodAction;
import com.volantis.testtools.mock.method.MethodActionEvent;

import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.Map;

import org.xml.sax.SAXException;

/**
 * Integration testcase to test XDIME 2 object element with a HTML 4 protocol.
 *
 * The following types are tested: image, audio, video, flash and text
 *
 * Tests with 'implicit' in the method name are cases of no srctype attribute
 * specified. These tests assume mime-type heuristics to be used and provide
 * approproiate expectations
 *
 * Tests with 'explicit' in the method name are cases of mime-type specified in
 * srctype attribute of object element
 *
 * Tests with 'external' in the method name are cases of mcs-media-style: external
 * and they assume rendering of anchor
 *
 * Tests with 'transcodeOff' in the method name are cases of mcs-transcode parameter
 * of object element specifed with value false
 *
 * Tests with 'transcodeDefault' in the method name are cases of mcs-transcode parameter
 * of object element not specifed, so the default value of true is assumed.
 *
 */
public class HTMLV4ObjectElementTestCase extends HTMLV4TestCaseAbstract {

    private static final PolicyFactory POLICY_FACTORY = PolicyFactory
            .getDefaultInstance();

    
    // Javadoc inherited
    protected void setUp() throws Exception {
        super.setUp();

        addCanvasExpectations();
        addElementOutputStateExpectations();

        marinerPageContextMock
            .expects.getEnvironmentContext()
            .returns(environmentContextMock).any();
    }

    /**
     * Test that an object element with an implicit device dependent image type
     * and transcoding turned off generates a image element with the input URI.
     *
     * @throws Exception
     */
    public void testObjectImplicitDeviceDependentImageTranscodeOff()
            throws Exception {

        String imageSrc = "image.png";

        environmentContextMock
                .expects.getMimeType(imageSrc)
                .returns("image/png").any();

        addRewriteObjectURLExpectations(imageSrc);
        
        openDocument();
        startElement("p");
        startObject(imageSrc);
        writeTranscodeOff();
        writeMessage("body content");
        endObject();
        endElement("p");
        closeDocument();

        String expected = "<p>" + "<img alt=\"body content\" border=\"0\" "
                + "src=\"" + imageSrc + "\" />" + "</p>";
        assertBodyEqual(expected, getMarkup());
    }

    /**
     * Test that an object element with an explicit device dependent image type
     * and transcoding turned off generates a image element with the input URI.
     *
     * @throws Exception
     */
    public void testObjectExplicitDeviceDependentImageTranscodeOff()
            throws Exception {

        String imageSrc = "image";

        addRewriteObjectURLExpectations(imageSrc);

        openDocument();
        startElement("p");
        startObject(imageSrc, "image/png");
        writeTranscodeOff();
        writeMessage("body content");
        endObject();
        endElement("p");
        closeDocument();

        String expected = "<p>" + "<img alt=\"body content\" border=\"0\" "
                + "src=\"image\" />" + "</p>";
        assertBodyEqual(expected, getMarkup());
    }

    /**
     * Test that an object element with an implicit device dependent image type
     * and transcoding turned on (implicitly as this is the default) generates
     * an image element with a transcoded URI.
     *
     * @throws Exception
     */
    public void testObjectImplicitDeviceDependentImageTranscodeDefault()
            throws Exception {

        String imageSrc = "http://xxx/image.jpg";
        String transcodedImageSrc = "http://xxx/transcoded-image.jpg";

        marinerPageContextMock.expects.constructImageURL(imageSrc, null)
                .returns(imageSrc + "?v.width=999");
        // as the imagesrc is abolute this shuould just return the expected
        // transcodable output
        marinerPageContextMock.expects.getAbsoluteURL(
            new MarinerURL(imageSrc + "?v.width=999"), Boolean.TRUE).returns(
                new URL(transcodedImageSrc));
        addMediaAgentExpectations(transcodedImageSrc);

        // TODO: Use mocks, when RequestFactory becomes mocked.
        RequestFactory requestFactory = new RequestFactory() {
            public Request createRequestFromICSURI(URI uri) {
                return new RequestMock("request mock", expectations);
            }

            public Request createRequest(final String s, final URI uri, final Map map) {
                return null;
            }
        };


        marinerPageContextMock.expects.getMediaAgentRequestFactory()
            .returns(requestFactory).any();
        
        addRewriteImageURLExpectations(imageSrc + "?v.width=999");

        volantisMock.expects.getAssetTranscoder().returns(
                new PluggableAssetTranscoderManager(""));

        environmentContextMock
                .expects.getMimeType("http://xxx/image.jpg")
                .returns("image/jpeg").any();

        openDocument();
        startElement("p");
        startObject(imageSrc);
        writeMessage("body content");
        endObject();
        endElement("p");
        closeDocument();

        String expected = "<p>" + "<img alt=\"body content\" border=\"0\" "
                + "src=\"" + transcodedImageSrc + "\" />" + "</p>";
        assertBodyEqual(expected, getMarkup());
    }

    /**
     * Test that an object element with an implicit device dependent non image
     * type generates a span element containing the body content.
     *
     * @throws Exception
     */
    public void testObjectImplicitDeviceDependentOther() throws Exception {

        environmentContextMock
                .expects.getMimeType("note.txt")
                .returns("text/plain").any();

        openDocument();
        startElement("p");
        startObject("note.txt");
        writeMessage("body content");
        endObject();
        endElement("p");
        closeDocument();

        final String expected = "<p>" + "body content" + "</p>";

        assertBodyEqual(expected, getMarkup());
    }

    /**
     * Test that an object element with an explicit device dependent non image
     * type generates a span element containing the body content.
     *
     * @throws Exception
     */
    public void testObjectExplicitDeviceDependentOther() throws Exception {

        String src = "bogus.png";
        
        addRewriteObjectURLExpectations(src);

        openDocument();
        startElement("p");
        startObject(src, "text/xxx");
        writeMessage("body content");
        endObject();
        endElement("p");
        closeDocument();

        final String expected = "<p>" + "body content" + "</p>";

        assertBodyEqual(expected, getMarkup());
    }

    /**
     * Test that an object element with an implicit device independent image
     * type generates an image with the asset URI, width and height.
     *
     * @throws Exception
     */
    public void testObjectImplicitDeviceIndependentImage() throws Exception {

        String imageSrc = "image.mimg";

        addImageSrcExpectations(imageSrc);
        environmentContextMock
                .expects.getMimeType("image.mimg")
                .returns("application/x-mcs-image").any();
        
        addImageSrcExpectations(imageSrc);

        openDocument();
        startElement("p");
        startObject(imageSrc);
        writeMessage("body content");
        endObject();
        endElement("p");
        closeDocument();

        String expected = "<p>"
                + "<img alt=\"body content\" border=\"0\" height=\"200\" "
                + "src=\"http://host/foo.jpeg\" " + "width=\"100\" />"
                + "</p>";

        assertBodyEqual(expected, getMarkup());
    }

    /**
     * Test that an object element with an implicit device independent image
     * type and a caption generates a div containing the caption and an image
     * with the asset URI, width and height.
     *
     * @throws Exception
     */
    public void testObjectImplicitDeviceIndependentImageCaption()
            throws Exception {

        String imageSrc = "image.mimg";
        addImageSrcExpectations(imageSrc);
        environmentContextMock.expects.getMimeType("image.mimg").returns(
                "application/x-mcs-image").any();

        
        openDocument();
        startElement("p");
        startObject(imageSrc);
        startElement("caption");
        writeMessage("caption content");
        endElement("caption");
        writeMessage("body content");
        endObject();
        endElement("p");
        closeDocument();

        String expected = "<p>" + "<div>"
                + "<div class=\"a\">caption content</div>"
                + "<img alt=\"body content\" border=\"0\" height=\"200\" "
                + "src=\"http://host/foo.jpeg\" " + "width=\"100\" />"
                + "</div>" + "</p>";

        assertBodyEqual(expected, ".a{display:table-caption}", getMarkup());
    }

    /**
     * Test that an object element with an explicit device independent image
     * type generates an image with the asset URI, width and height.
     *
     * @throws Exception
     */
    public void testObjectExplicitDeviceIndependentImage() throws Exception {

        String imageSrc = "image.mimg";
        addImageSrcExpectations(imageSrc);

        openDocument();
        startElement("p");
        startObject(imageSrc, "application/x-mcs-image");
        writeMessage("body content");
        endObject();
        endElement("p");
        closeDocument();

        String expected = "<p>"
                + "<img alt=\"body content\" border=\"0\" height=\"200\" "
                + "src=\"http://host/foo.jpeg\" " + "width=\"100\" />"
                + "</p>";

        assertBodyEqual(expected, getMarkup());
    }

    /**
     * Test that an object element with an implicit device independent text type
     * generates a span containing the asset text.
     *
     * @throws Exception
     */
    public void testObjectImplicitDeviceIndependentText() throws Exception {

        String textSrc = "note.mtxt";
        addTextExpectations(textSrc);


        referenceResolverMock.expects
                .resolveUnquotedTextExpression("note.mtxt").returns(
                        new LiteralTextAssetReference("a note")).any();
        environmentContextMock
                .expects.getMimeType("note.mtxt")
                .returns("application/x-mcs-text").any();

        openDocument();
        startElement("p");
        startObject(textSrc);
        writeMessage("body content");
        endObject();
        endElement("p");
        closeDocument();

        String expected = "<p>" + "<span>" + "a note" + "</span>" + "</p>";

        assertBodyEqual(expected, getMarkup());
    }
 

    /**
     * Test that an object element with an IMPLICIT device dependent AUDIO type
     * and transcoding turned off generates an object element with the input URI.
     *
     * @throws Exception
     */
    public void testObjectImplicitDeviceDependentAudioTranscodeOff()
            throws Exception {

        String src = "audio.mp3";
        String audioSrcType = "audio/mp3";

        environmentContextMock
                .expects.getMimeType(src)
                .returns("audio/mp3").any();
        
        addRewriteObjectURLExpectations(src);

        openDocument();
        startElement("p");
        startObject(src);
        writeTranscodeOff();
        writeMessage("body content");
        endObject();
        endElement("p");
        closeDocument();

        String expected = "<p><object data=\"" + src + "\" type=\""+ audioSrcType +"\">" +
                "body content" +
                "</object></p>";
        assertBodyEqual(expected, getMarkup());
    }

    /**
     * Test that an object element with an EXPLICIT device dependent audio type
     * and transcoding turned off generates an object element with the input URI.
     *
     * @throws Exception
     */
    public void testObjectExplicitDeviceDependentAudioTranscodeOff()
            throws Exception {

        String src = "audio";
        String audioSrcType = "audio/mp3";

        addRewriteObjectURLExpectations(src);

        openDocument();
        startElement("p");
        startObject(src, "audio/mp3");
        writeTranscodeOff();
        writeMessage("body content");
        endObject();
        endElement("p");
        closeDocument();

        String expected = "<p><object data=\"" + src + "\" type=\""+ audioSrcType +"\">" +
                "body content" +
                "</object></p>";
        assertBodyEqual(expected, getMarkup());
    }

    /**
     * Test that an object element with an IMPLICIT device dependent AUDIO type
     * and transcoding turned on (implicitly as this is the default) generates
     * an object element with a transcoded URI.
     *
     * @throws Exception
     */
    public void testObjectImplicitDeviceDependentAudioTranscodeDefault()
            throws Exception {

        String src = "audio.mp3";
        String srcType = "audio/mp3";
        String transcoded = "http://fakehost:7070/project/transcoded-audio.mp3";        

        environmentContextMock
                .expects.getMimeType(src)
                .returns(srcType).any();

        // as the src is relative abolute this shuould just return the expected
        // absolute value
        marinerPageContextMock.expects.getAbsoluteURL(
            new MarinerURL(src), Boolean.TRUE).returns(new URL(transcoded));


        // TODO: Use mocks, when RequestFactory becomes mocked.
        RequestFactory requestFactory = new RequestFactory() {
            public Request createRequestFromICSURI(URI uri) {
                return null;
            }

            public Request createRequest(final String s, final URI uri, final Map map) {
                return new RequestMock("request mock", expectations);
            }
        };
        marinerPageContextMock.expects.getMediaAgentRequestFactory()
            .returns(requestFactory).any();

        addMediaAgentExpectations(transcoded);
        
        openDocument();
        startElement("p");
        startObject(src);
        writeMessage("body content");
        endObject();
        endElement("p");
        closeDocument();

        String expected = "<p><object data=\"" + transcoded + "\">" +
                "body content" +
                "</object></p>";
        assertBodyEqual(expected, getMarkup());
    }

    /**
     * Test that an object element with an IMPLICIT device dependent VIDEO type
     * and transcoding turned off generates an object element with the input URI.
     *
     * @throws Exception
     */
    public void testObjectImplicitDeviceDependentVideoTranscodeOff()
            throws Exception {

        String src = "video.mpeg";
        String expectedVideoSrcType = "video/mpeg";

        environmentContextMock
                .expects.getMimeType(src)
                .returns(expectedVideoSrcType).any();

        addRewriteObjectURLExpectations(src);

        openDocument();
        startElement("p");
        startObject(src);
        writeTranscodeOff();
        writeMessage("body content");
        endObject();
        endElement("p");
        closeDocument();

        String expected = "<p><object data=\"" + src + "\" type=\""+ expectedVideoSrcType +"\">" +
                "body content" +
                "</object></p>";
        assertBodyEqual(expected, getMarkup());
    }

    /**
     * Test that an object element with an EXPLICIT device dependent VIDEO type
     * and transcoding turned off generates an object element with the input URI.
     *
     * @throws Exception
     */
    public void testObjectExplicitDeviceDependenVideoTranscodeOff()
            throws Exception {

        String src = "video";
        String expectedVideoSrcType = "video/mpeg";

        addRewriteObjectURLExpectations(src);

        openDocument();
        startElement("p");
        startObject(src, "video/mpeg");
        writeTranscodeOff();
        writeMessage("body content");
        endObject();
        endElement("p");
        closeDocument();

        String expected = "<p><object data=\"" + src + "\" type=\""+ expectedVideoSrcType +"\">" +
                "body content" +
                "</object></p>";
        assertBodyEqual(expected, getMarkup());
    }

    /**
     * Test that an object element with an IMPLICIT device dependent VIDEO type
     * and transcoding turned on (implicitly as this is the default) generates
     * an object element with a transcoded URI.
     */
    public void testObjectImplicitDeviceDependentVideoTranscodeDefault()
            throws Exception {

        String src = "video.mpeg";
        String srcType = "video/mpeg";
        String transcoded = "http://fakehost:7777/projects/transcoded-video.mpeg";        

        environmentContextMock
                .expects.getMimeType(src)
                .returns(srcType).any();

        // as the src is relative abolute this shuould just return the expected
        // absolute value
        marinerPageContextMock.expects.getAbsoluteURL(
            new MarinerURL(src), Boolean.TRUE).returns(new URL(transcoded));

        // TODO: Use mocks, when RequestFactory becomes mocked.
        RequestFactory requestFactory = new RequestFactory() {
            public Request createRequestFromICSURI(URI uri) {
                return null;
            }

            public Request createRequest(final String s, final URI uri, final Map map) {
                return new RequestMock("request mock", expectations);
            }
        };
        marinerPageContextMock.expects.getMediaAgentRequestFactory()
            .returns(requestFactory).any();

        addMediaAgentExpectations(transcoded);
        
        openDocument();
        startElement("p");
        startObject(src);
        writeMessage("body content");
        endObject();
        endElement("p");
        closeDocument();

        String expected = "<p><object data=\"" + transcoded + "\">" +
                "body content" +
                "</object></p>";
        assertBodyEqual(expected, getMarkup());
    }

    /**
     * Test that an object element with an IMPLICIT device dependent FLASH type
     * and transcoding turned off generates an object element with the input URI.
     *
     * @throws Exception
     */
    public void testObjectImplicitDeviceDependentFlashTranscodeOff()
            throws Exception {

        String src = "flash.swf";
        String expectedSrcType = "application/x-shockwave-flash";

        environmentContextMock
                .expects.getMimeType(src)
                .returns("application/x-shockwave-flash").any();

        addRewriteObjectURLExpectations(src);

        openDocument();
        startElement("p");
        startObject(src);
        writeTranscodeOff();
        writeMessage("body content");
        endObject();
        endElement("p");
        closeDocument();

        String expected = "<p><object data=\"" + src + "\" type=\""+expectedSrcType+"\">" +
                "body content" +
                "</object></p>";
        assertBodyEqual(expected, getMarkup());
    }

    /**
     * Test that an object element with an EXPLICIT device dependent FLASH type
     * and transcoding turned off generates an object element with the input URI.
     *
     * @throws Exception
     */
    public void testObjectExplicitDeviceDependenFlashTranscodeOff()
            throws Exception {

        String src = "flash";
        String expectedSrcType = "application/x-shockwave-flash";

        addRewriteObjectURLExpectations(src);

        openDocument();
        startElement("p");
        startObject(src,expectedSrcType);
        writeTranscodeOff();
        writeMessage("body content");
        endObject();
        endElement("p");
        closeDocument();

        String expected = "<p><object data=\"" + src + "\" type=\""+expectedSrcType+"\">" +
                "body content" +
                "</object></p>";
        assertBodyEqual(expected, getMarkup());
    }

    /**
     * Test that an object element with an IMPLICIT device dependent FLASH type
     * and transcoding turned on (implicitly as this is the default) generates
     * an exception
     *
     * @throws Exception
     */
    public void testObjectImplicitDeviceDependentFlashTranscodeDefault()
            throws Exception {

        String src = "flash.swf";
        String expectedSrcType = "application/x-shockwave-flash";
        environmentContextMock
                .expects.getMimeType(src)
                .returns(expectedSrcType).any();

        addRewriteObjectURLExpectations(src);

        try {
            openDocument();
            startElement("p");
            startObject(src);
            writeMessage("body content");
            endObject();
            endElement("p");
            closeDocument();

            fail("Exception expected");

        } catch (Exception x) {
            // OK - exception expected
            // TODO: set the type caught above to the expected one instead of generic Exception
        }
    }

    /**
     * Test that an object element with an IMPLICIT device independent AUDIO type
     * generates object with device dependent URL
     * The case of src pointing to a device independent audio file, no srctype attribute.
     * Test expects that mime-type heuristics will be used and expected markup is object.
     *
     * @throws Exception
     */
    public void testImplicitDeviceIndependentAudio() throws Exception {

        String expectedAudioType = "audio/mp3";
        // The URL to device independent file
        String src = "audio.mauc";


        // The chosen URL to device dependent file
        String actual = "audio.mp3";


        environmentContextMock
                .expects.getMimeType(src)
                .returns("application/x-mcs-audio").any();
        addAudioExpectations(src,actual);

        openDocument();
        startElement("p");
        startObject(src);
        writeMessage("body content");
        endObject();
        endElement("p");
        closeDocument();

        String expected = "<p><object data=\"" + actual + "\" type=\""+expectedAudioType +"\">" +
                "body content" +
                "</object></p>";
        assertBodyEqual(expected, getMarkup());
    }

    /**
     * Test that an object element with an EXPLICIT device independent VIDEO type
     * generates object with device dependent URL
     * The case of src pointing to a device independent video file with no extension,
     * srctype attribute correctly set. Expected markup is object.
     *
     * @throws Exception
     */

    public void testExplicitDeviceIndependentVideo()  throws Exception {

        // The URL to device independent file
        String src = "video";

        String expectedVideoType = "video/mpeg";

        // The chosen URL to device dependent file
        String actual = "video.mpeg";

        addVideoExpectations(src,actual);
        
        environmentContextMock
                .expects.getMimeType("video")
                .returns("application/x-mcs-video").any();        

        openDocument();
        startElement("p");
        startObject(src, "application/x-mcs-video");
        writeMessage("body content");
        endObject();
        endElement("p");
        closeDocument();

        String expected = "<p><object data=\"" + actual +"\" type=\""+expectedVideoType +"\">" +
                "body content" +
                "</object></p>";
        assertBodyEqual(expected, getMarkup());
    }
    
    /**
     * The case of image with mcs-media-style set to "external" and transcode off.
     * Expected markup is anchor to the original image.
     *
     * @throws Exception
     */
    public void testExternallDeviceDependentImageTranscodeOff() throws Exception {

        String src = "image.png";
        String label = "link to image";

        marinerPageContextMock.expects.getBooleanDevicePolicyValue("aggregation")
                .returns(false).any();

        addRewriteObjectURLExpectations(src);

        openDocument();
        startElement("p");
        startObject(src, "image/png", "mcs-media-style: external");
        writeParam("mcs-external-label", label);
        writeTranscodeOff();
        writeMessage("body content");
        endObject();
        endElement("p");
        closeDocument();

        String expected = "<p><a href=\"" + src + "\">" +
                label + "</a></p>";
        assertBodyEqual(expected, getMarkup());
    }

    /**
     *  The case of audio with mcs-media-style set to "external" and transcoding.
     *  Expected markup is anchor to transcoded resource.
     *
     * @throws Exception
     */
    public void testExternalDeviceDependentAudioTranscodeDefault() throws Exception {

        String src = "audio.mp3";
        String label = "link to audio";
        String transcoded = "http://fakehost:7777/projects/transcoded-video.mpeg";

        marinerPageContextMock.expects.getBooleanDevicePolicyValue("aggregation")
                .returns(false).any();

        // as the src is relative abolute this shuould just return the expected
        // absolute value
        marinerPageContextMock.expects.getAbsoluteURL(
            new MarinerURL(src), Boolean.TRUE).returns(new URL(transcoded));
        // TODO: Use mocks, when RequestFactory becomes mocked.
        RequestFactory requestFactory = new RequestFactory() {
            public Request createRequestFromICSURI(URI uri) {
                return null;
            }

            public Request createRequest(final String s, final URI uri, final Map map) {
                return new RequestMock("request mock", expectations);
            }
        };
        marinerPageContextMock.expects.getMediaAgentRequestFactory()
            .returns(requestFactory).any();

        addMediaAgentExpectations(transcoded);
        
        openDocument();
        startElement("p");
        startObject(src, "audio/mp3", "mcs-media-style: external");
        writeParam("mcs-external-label", label);
        writeMessage("body content");
        endObject();
        endElement("p");
        closeDocument();

        String expected = "<p><a href=\"" + transcoded + "\">" +
                label + "</a></p>";
        assertBodyEqual(expected, getMarkup());
    }

    /**
     *  The case of video with mcs-media-style set to "external". Expected markup is anchor.
     *
     * @throws Exception
     */
    public void testExternalDeviceIndependentVideo() throws Exception {

        // The URL to device independent file
        String src = "video.mdyv";

        // External label
        String label = "link to video";

        // The chosen URL to device dependent file
        String actual = "video.mpeg";

        addVideoExpectations(src,actual);
        marinerPageContextMock.expects.getBooleanDevicePolicyValue("aggregation")
                .returns(false).any();

        environmentContextMock
                .expects.getMimeType("video.mdyv")
                .returns("application/x-mcs-video").any();

        openDocument();
        startElement("p");
        startObject(src, "application/x-mcs-video", "mcs-media-style: external");
        writeParam("mcs-external-label", label);
        writeMessage("body content");
        endObject();
        endElement("p");
        closeDocument();

        String expected = "<p><a href=\"" + actual + "\">" +
                label + "</a></p>";
        assertBodyEqual(expected, getMarkup());

    }

    /**
     *  The case of flash with mcs-media-style set to "external" and notranscoding.
     *  Expected markup is anchor to the original file.
     *
     * @throws Exception
     */
    public void testExternalFlashTranscodeOff() throws Exception {

        String src = "flash.swf";
        String label = "link to flash";

        marinerPageContextMock.expects.getBooleanDevicePolicyValue("aggregation")
                .returns(false).any();

        addRewriteObjectURLExpectations(src);

        openDocument();
        startElement("p");
        startObject(src, "application/x-shockwave-flash", "mcs-media-style: external");
        writeTranscodeOff();
        writeParam("mcs-external-label", label);
        writeMessage("body content");
        endObject();
        endElement("p");
        closeDocument();

        String expected = "<p><a href=\"" + src + "\">" +
                label + "</a></p>";
        assertBodyEqual(expected, getMarkup());
    }

    /**
     * Test that nested objects are processed
     *
     * @throws Exception
     */
    public void testNestedObjects() throws Exception {

        String firstSrc = "first.mpeg";
        String secondSrc = "second.mdyv";
        String thirdSrc = "third.png";

        String firstSrcType = "video/mpeg";
        String secondSrcType = "video/mpeg";

        addRewriteObjectURLExpectations(firstSrc);
        addRewriteObjectURLExpectations(secondSrc);
        addRewriteObjectURLExpectations(thirdSrc);

        addVideoExpectations(secondSrc,secondSrc);

        openDocument();
        startElement("p");
        startObject(firstSrc, "video/mpeg");
        writeTranscodeOff();
        startObject(secondSrc, "application/x-mcs-video");
        writeTranscodeOff();
        startObject(thirdSrc, "image/png");
        writeTranscodeOff();
        writeMessage("body content");
        endObject();
        endObject();
        endObject();
        endElement("p");
        closeDocument();

        String expected = "<p><object data=\"" + firstSrc + "\" type=\""+firstSrcType+"\">" +
                "<object data=\"" + secondSrc + "\" type=\""+firstSrcType+"\">" +
                "<img alt=\"body content\" border=\"0\" src=\"" + thirdSrc + "\" />" +
                "</object></object></p>";
        assertBodyEqual(expected, getMarkup());
    }

    /**
     * Nested content expected to be ignored
     *
     * @throws Exception
     */
    public void testNestedContentWhenAnchor() throws Exception {

        String src = "audio.mp3";
        String label = "link to audio";
        String transcoded = "http://fakehost:7777/projects/audio.mp3";

        marinerPageContextMock.expects.getBooleanDevicePolicyValue("aggregation")
                .returns(false).any();                      

        // as the src is relative abolute this shuould just return the expected
        // absolute value
        marinerPageContextMock.expects.getAbsoluteURL(
            new MarinerURL(src), Boolean.TRUE).returns(new URL(transcoded));

        // TODO: Use mocks, when RequestFactory becomes mocked.
        RequestFactory requestFactory = new RequestFactory() {
            public Request createRequestFromICSURI(URI uri) {
                return null;
            }

            public Request createRequest(final String s, final URI uri, final Map map) {
                return new RequestMock("request mock", expectations);
            }
        };
        marinerPageContextMock.expects.getMediaAgentRequestFactory()
            .returns(requestFactory).any();

        addMediaAgentExpectations(src);

        openDocument();
        startElement("p");
        startObject(src, "audio/mp3", "mcs-media-style: external");
        writeParam("mcs-external-label", label);
        writeMessage("body content");
        endObject();
        endElement("p");
        closeDocument();

        String expected = "<p><a href=\"" + src + "\">" +
                label + "</a></p>";
        assertBodyEqual(expected, getMarkup());
    }

    /**
     * Text from nested content expected to used as alt text
     *
     * @throws Exception
     */
    public void testNestedContentWhenImage() throws Exception {

        String src = "image.png";

        addRewriteObjectURLExpectations(src);

        openDocument();
        startElement("p");
        startObject(src, "image/png");
        writeTranscodeOff();
        startElement("p");
        writeMessage("body content");
        endElement("p");
        endObject();
        endElement("p");
        closeDocument();

        String expected = "<p>" + "<img alt=\"body content\" border=\"0\" "
                + "src=\"" + src + "\" />" + "</p>";
        assertBodyEqual(expected, getMarkup());

    }

    /**
     * Nested content expected to rendered inside object
     *
     * @throws Exception
     */
    public void testNestedContentWhenObject() throws Exception {

        String expectedSrcType = "video/mpeg";
        String src = "video.mpeg";

        addRewriteObjectURLExpectations(src);

        openDocument();
        startElement("p");
        startObject(src, expectedSrcType);
        writeTranscodeOff();
        writeMessage("body content");
        endObject();
        endElement("p");
        closeDocument();

        String expected = "<p><object data=\"" + src + "\" type=\""+expectedSrcType +"\">" +
                "body content</object></p>";
        assertBodyEqual(expected, getMarkup());
    }


    /**
     * Expectations for device independent text processing
     */
    private void addTextExpectations(String imageSrc) {

        VariantBuilder variantBuilder = POLICY_FACTORY
                .createVariantBuilder(VariantType.TEXT);

        variantBuilder.setSelectionBuilder(POLICY_FACTORY
                .createDefaultSelectionBuilder());

        TextMetaDataBuilder builder = POLICY_FACTORY.
                createTextMetaDataBuilder();
        builder.setTextEncoding(TextEncoding.PLAIN);
        

        URLContentBuilder urlContentBuilder = POLICY_FACTORY
                .createURLContentBuilder();
        urlContentBuilder.setURL("/text.txt");
        variantBuilder.setContentBuilder(urlContentBuilder);

        variantBuilder.setMetaDataBuilder(builder);

        Variant variant = variantBuilder.getVariant();

        final RuntimePolicyReferenceMock textReferenceMock = new RuntimePolicyReferenceMock(
                "textReferenceMock", expectations);

        referenceResolverMock.expects.resolveUnquotedPolicyExpression(imageSrc,
                null).returns(textReferenceMock).any();

        final SelectedVariantMock selectedVariantMock = new SelectedVariantMock(
                "selectedVariantMock", expectations);

        assetResolverMock.expects.selectBestVariant(textReferenceMock, null)
                .returns(selectedVariantMock).any();

        assetResolverMock.expects.retrieveVariantURLAsString(
                selectedVariantMock).returns("http://host/text.txt").any();

        assetResolverMock.expects.getBaseLocation(selectedVariantMock).returns(
                BaseLocation.CONTEXT).any();

        selectedVariantMock.expects.getVariant().returns(variant).any();
    }


    /**
     * Expectations for device independent image processing
     */
    private void addImageSrcExpectations(String imageSrc) {

        VariantBuilder variantBuilder = POLICY_FACTORY
                .createVariantBuilder(VariantType.IMAGE);

        variantBuilder.setSelectionBuilder(POLICY_FACTORY
                .createDefaultSelectionBuilder());

        ImageMetaDataBuilder builder = POLICY_FACTORY
                .createImageMetaDataBuilder();
        builder.setWidth(100);
        builder.setHeight(200);
        builder.setImageEncoding(ImageEncoding.JPEG);
        builder.setPixelDepth(24);

        URLContentBuilder urlContentBuilder = POLICY_FACTORY
                .createURLContentBuilder();
        urlContentBuilder.setURL("/foo.jpeg");
        variantBuilder.setContentBuilder(urlContentBuilder);

        variantBuilder.setMetaDataBuilder(builder);

        Variant variant = variantBuilder.getVariant();

        final RuntimePolicyReferenceMock imageReferenceMock = new RuntimePolicyReferenceMock(
                "imageReferenceMock", expectations);

        referenceResolverMock.expects.resolveUnquotedPolicyExpression(imageSrc,
                null).returns(imageReferenceMock).any();

        final SelectedVariantMock selectedVariantMock = new SelectedVariantMock(
                "selectedVariantMock", expectations);

        assetResolverMock.expects.selectBestVariant(imageReferenceMock, null)
                .returns(selectedVariantMock).any();

        assetResolverMock.expects.retrieveVariantURLAsString(
                selectedVariantMock).returns("http://host/foo.jpeg").any();

        addRewriteImageURLExpectations("http://host/foo.jpeg");

        assetResolverMock.expects.getBaseLocation(selectedVariantMock).returns(
                BaseLocation.CONTEXT).any();

        selectedVariantMock.expects.getVariant().returns(variant).any();
    }

    /**
     * Expectations for device independent image processing
     */
    private void addAudioExpectations(String audioSrc,String audioExpected) {

        VariantBuilder variantBuilder = POLICY_FACTORY
                .createVariantBuilder(VariantType.AUDIO);

        variantBuilder.setSelectionBuilder(POLICY_FACTORY
                .createDefaultSelectionBuilder());

        AudioMetaDataBuilder builder = POLICY_FACTORY
                .createAudioMetaDataBuilder();

        builder.setAudioEncoding(AudioEncoding.MP3);

        URLContentBuilder urlContentBuilder = POLICY_FACTORY
                .createURLContentBuilder();
        urlContentBuilder.setURL("/foo.mp3");
        variantBuilder.setContentBuilder(urlContentBuilder);

        variantBuilder.setMetaDataBuilder(builder);

        Variant variant = variantBuilder.getVariant();

        final RuntimePolicyReferenceMock audioReferenceMock = new RuntimePolicyReferenceMock(
                "audioReferenceMock", expectations);

        referenceResolverMock.expects.resolveUnquotedPolicyExpression(audioSrc,
                null).returns(audioReferenceMock).any();

        final SelectedVariantMock selectedVariantMock = new SelectedVariantMock(
                "selectedVariantMock", expectations);

        assetResolverMock.expects.selectBestVariant(audioReferenceMock, null)
                .returns(selectedVariantMock).any();

        assetResolverMock.expects.retrieveVariantURLAsString(
                selectedVariantMock).returns(audioExpected).any();

        assetResolverMock.expects.getBaseLocation(selectedVariantMock).returns(
                BaseLocation.CONTEXT).any();

        selectedVariantMock.expects.getVariant().returns(variant).any();
    }

    /**
     * Expectations for device independent image processing
     */
    private void addVideoExpectations(String videoSrc,String expectation) {

        VariantBuilder variantBuilder = POLICY_FACTORY
                .createVariantBuilder(VariantType.VIDEO);

        variantBuilder.setSelectionBuilder(POLICY_FACTORY
                .createDefaultSelectionBuilder());

        VideoMetaDataBuilder builder = POLICY_FACTORY
                .createVideoMetaDataBuilder();

        builder.setVideoEncoding(VideoEncoding.MPEG4);
        builder.setHeight(200);
        builder.setWidth(200);

        URLContentBuilder urlContentBuilder = POLICY_FACTORY
                .createURLContentBuilder();
        urlContentBuilder.setURL("/foo.mpeg");
        variantBuilder.setContentBuilder(urlContentBuilder);

        variantBuilder.setMetaDataBuilder(builder);

        Variant variant = variantBuilder.getVariant();

        final RuntimePolicyReferenceMock videoReferenceMock = new RuntimePolicyReferenceMock(
                "videoReferenceMock", expectations);

        referenceResolverMock.expects.resolveUnquotedPolicyExpression(videoSrc,
                null).returns(videoReferenceMock).any();

        final SelectedVariantMock selectedVariantMock = new SelectedVariantMock(
                "selectedVariantMock", expectations);

        assetResolverMock.expects.selectBestVariant(videoReferenceMock, null)
                .returns(selectedVariantMock).any();

        assetResolverMock.expects.retrieveVariantURLAsString(
                selectedVariantMock).returns(expectation).any();

        assetResolverMock.expects.getBaseLocation(selectedVariantMock).returns(
                BaseLocation.CONTEXT).any();

        selectedVariantMock.expects.getVariant().returns(variant).any();
    }


    private void addRewriteImageURLExpectations(String url) {
        assetResolverMock
                .expects.rewriteURLWithPageURLRewriter(url, PageURLType.IMAGE)
                .returns(url).any();
    }

    private void addRewriteObjectURLExpectations(String url) {
        assetResolverMock
                .expects.rewriteURLWithPageURLRewriter(url, PageURLType.OBJECT)
                .returns(url).any();
    }

    private void writeParam(String name, String value) throws SAXException {
        startElement("param",
                new String[] { "name", "value" },
                new String[] { name, value });
        endElement("param");
    }

    private void writeTranscodeOff() throws SAXException {
        writeParam("mcs-transcode", "false");
    }

    private void startObject() throws SAXException {
        startElement("object");
    }

    private void startObject(String src) throws SAXException {
        startElement("object",
                new String[] { "src"},
                new String[] {  src });                                     

    }

    private void startObject(String src, String srctype) throws SAXException {
        startElement("object",
                new String[] { "src", "srctype" },
                new String[] {  src, srctype });

    }

    private void startObject(String src, String srctype, String style) throws SAXException {
        startElement("object",
                new String[] { "src", "srctype", "style" },
                new String[] {  src, srctype, style });
    }

    private void endObject() throws SAXException {
        endElement("object");
    }
    
    private void addMediaAgentExpectations(String transcodedUrl) {
        MediaAgentMock mediaAgentMock = 
            new MediaAgentMock("media agent", expectations);
        
        final DefaultAgentParameters parameters = new DefaultAgentParameters();
        
        parameters.setParameterValue(MediaAgent.OUTPUT_URL_PARAMETER_NAME,
                transcodedUrl);
        
        InternalDeviceMock internalDeviceMock = 
            new InternalDeviceMock("internal device", expectations);
        
        internalDeviceMock.fuzzy
            .getPolicyValue(mockFactory.expectsAny()).returns(null).any();
        internalDeviceMock.fuzzy
            .getIntegerPolicyValue(mockFactory.expectsAny()).returns(0).any();
        internalDeviceMock.fuzzy
            .getIntegerPolicyValue(
                    mockFactory.expectsAny(),
                    mockFactory.expectsAny()).returns(0).any();
        internalDeviceMock.fuzzy
            .getCommaSeparatedPolicyValues(mockFactory.expectsAny())
            .returns(Collections.EMPTY_LIST).any();
        internalDeviceMock.expects.getPixelsX().returns(0).any();
        internalDeviceMock.expects.getPixelsY().returns(0).any();
        
        mediaAgentMock.fuzzy
            .requestURL(
                mockFactory.expectsInstanceOf(Request.class),
                mockFactory.expectsInstanceOf(ResponseCallback.class))
            .does(
                new MethodAction() {
                    public Object perform(MethodActionEvent event)
                            throws Throwable {
                        ResponseCallback callback = (ResponseCallback) event
                                .getArgument(ResponseCallback.class);

                        callback.execute(parameters);

                        return null;
                    }
                }).atLeast(1);
        
        mediaAgentMock.expects.waitForComplete().any();

        marinerPageContextMock.expects.getDevice()
            .returns(internalDeviceMock).any();
        
        marinerPageContextMock.expects.getMediaAgent(true)
            .returns(mediaAgentMock).any();

        marinerPageContextMock.expects.getMediaAgent(false)
            .returns(mediaAgentMock).any();
        
        addRewriteObjectURLExpectations(transcodedUrl);
    }
}
