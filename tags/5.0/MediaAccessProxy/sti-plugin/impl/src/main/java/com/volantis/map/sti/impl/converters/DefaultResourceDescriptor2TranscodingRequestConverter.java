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

package com.volantis.map.sti.impl.converters;

import com.volantis.map.common.param.MissingParameterException;
import com.volantis.map.common.param.ParameterNames;

import com.volantis.map.operation.ObjectParameters;
import com.volantis.map.operation.ResourceDescriptor;
import com.volantis.map.sti.converters.ConverterException;
import com.volantis.map.sti.converters.ResourceDescriptor2TranscodingRequestConverter;
import com.volantis.map.sti.mime.MimeTypeRetriever;
import com.volantis.map.sti.mime.MimeTypeRetrieverException;
import com.volantis.map.sti.model.Audio;
import com.volantis.map.sti.model.Image;
import com.volantis.map.sti.model.Media;
import com.volantis.map.sti.model.Text;
import com.volantis.map.sti.model.TranscodingJob;
import com.volantis.map.sti.model.TranscodingJobSource;
import com.volantis.map.sti.model.TranscodingJobTarget;
import com.volantis.map.sti.model.TranscodingParams;
import com.volantis.map.sti.model.TranscodingRequest;
import com.volantis.map.sti.model.Transformation;
import com.volantis.map.sti.model.Transformations;
import com.volantis.map.sti.model.Video;
import com.volantis.map.sti.model.VideoVideoAudio;
import com.volantis.map.sti.model.VideoVideoVisual;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.synergetics.localization.LocalizationFactory;

/**
 * Converts ResourceDescriptor to TranscodingRequest.
 * <p>
 * Since ResourceDescriptor describes one resource to transcode, there'll be
 * exactly one transcoding job in the converted request.
 * <p>
 * This implementation is not thread-safe.
 */
public class DefaultResourceDescriptor2TranscodingRequestConverter implements
        ResourceDescriptor2TranscodingRequestConverter {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer EXCEPTION_LOCALIZER = LocalizationFactory
            .createExceptionLocalizer(DefaultResourceDescriptor2TranscodingRequestConverter.class);

    /**
     * Currently processed resourceDescriptor.
     */
    private ResourceDescriptor resourceDescriptor;

    /**
     * InputParameters of currently processed ResourceDescriptor.
     */
    private ObjectParameters inputParameters;

    /**
     * Retrieves MIME type of the resource to transcode.
     */
    private final MimeTypeRetriever mimeTypeRetriever;

    /**
     * The MIME type of the resource to transcode.
     */
    private String contentType;

    /**
     * The originator ID used for the request.
     */
    private String originatorID;

    /**
     * Initializes this builder.
     */
    public DefaultResourceDescriptor2TranscodingRequestConverter(
            MimeTypeRetriever mimeTypeRetriever,
            String originatorID) {
        this.mimeTypeRetriever = mimeTypeRetriever;
        this.originatorID = originatorID;
    }

    /**
     * Builds and returns an instance of TranscodingRequest from specified
     * ResourceDescriptor.
     * 
     * @param resourceDescriptor The resource descriptor to build for.
     * @return built transcoding request.
     */
    public TranscodingRequest convert(ResourceDescriptor resourceDescriptor)
            throws ConverterException {
        this.resourceDescriptor = resourceDescriptor;
        this.inputParameters = resourceDescriptor.getInputParameters();

        // First, retrieve MIME type of the resource to be transcoded.
        try {
            this.contentType = mimeTypeRetriever
                    .retrieveMIMEType(resourceDescriptor);
        } catch (MimeTypeRetrieverException e) {
            throw new ConverterException("mime-type-error", new Object() {
            }, null);
        }

        return getTranscodingRequest();
    }

    /**
     * Retrieving transcoding request object from http request.
     * 
     * @return transcoding request model object.
     */
    private TranscodingRequest getTranscodingRequest()
            throws ConverterException {
        // Create new instance of TranscodingRequest.
        TranscodingRequest req = new TranscodingRequest();

        // Fill originator and operation IDs.
        req.setOriginatorID(getOriginatorID());
        req.setOperationID(generateOperationID());

        // Since there's only one resource to be transcoded, transcoding
        // parameters may be specified globally.
        req.setTranscodingParams(getTranscodingParams());

        // Add single transcoding job.
        req.addTranscodingJob(getTranscodingJob());

        return req;
    }

    /**
     * Retrieving transcoding job model object from request. Prefix giving
     * parameter path where informations can be found.
     * 
     * @param prefix prefix of parameter where parameter can be found.
     * @return transcoding job model object.
     */
    private TranscodingJob getTranscodingJob() throws ConverterException {
        TranscodingJob job = new TranscodingJob();

        job.setJobID(getJobID());

        job.setSource(getTranscodingJobSource());

        job.setTarget(getTranscodingJobTarget());

        return job;
    }

    /**
     * Retrieving transcoding job source model object from request. Prefix
     * giving parameter path where informations can be found.
     * 
     * @param prefix prefix of parameter where parameter can be found.
     * @return transcoding job source model object.
     * 
     * @throws ConverterException in case conversion fails.
     */
    private TranscodingJobSource getTranscodingJobSource()
            throws ConverterException {
        TranscodingJobSource transcodingJobSource = new TranscodingJobSource();

        // Retrieve location of the resource to transcode.
        String location = getParameterValue(ParameterNames.SOURCE_URL);

        if (location == null) {
            throw new ConverterException("missing-source-url", new Object[] {},
                    null);
        }

        transcodingJobSource.setLocation(location);

        // Retrieve MIME type of the resource to transcode.
        transcodingJobSource.setContentType(contentType);

        return transcodingJobSource;
    }

    /**
     * Retrieving transcoding job target model object from request. Prefix
     * giving parameter path where informations can be found.
     * 
     * @param prefix prefix of parameter where parameter can be found.
     * @return transcoding job target model object.
     */
    private TranscodingJobTarget getTranscodingJobTarget() {
        TranscodingJobTarget target = new TranscodingJobTarget();

        // Nothing to set here. Since there's only one transcoding job,
        // all transcoding parameters are specified on global level.

        return target;
    }

    /**
     * Retrieving transcoding params model object from request. Prefix giving
     * parameter path where informations can be found.
     * 
     * @param prefix prefix of parameter where parameter can be found.
     * @return transcoding params model object.
     * @throws ConverterException
     */
    private TranscodingParams getTranscodingParams() throws ConverterException {
        TranscodingParams transcodingParameters = new TranscodingParams();

        if (contentType.startsWith("audio")) {
            transcodingParameters.setAudio(getAudio());

        } else if (contentType.startsWith("image")) {
            transcodingParameters.setImage(getImage());

        } else if (contentType.startsWith("video")) {
            transcodingParameters.setVideo(getVideo());

        } else if (contentType.startsWith("text")) {
            transcodingParameters.setText(getText());
        }

        return transcodingParameters;
    }

    /**
     * Retrieving Text model object from request. Prefix giving parameter path
     * where informations can be found.
     * 
     * @param prefix prefix of parameter where parameter can be found.
     * @return text model object.
     */
    private Text getText() throws ConverterException {
        Text text = new Text();

        setMediaParameters(text);

        // Nothing to do yet.

        return text;
    }

    /**
     * Retrieving Video model object from request. Prefix giving parameter path
     * where informations can be found.
     * 
     * @param prefix prefix of parameter where parameter can be found.
     * @return video model object.
     */
    private Video getVideo() throws ConverterException {
        Video video = new Video();

        setMediaParameters(video);

        String limitParameter = getParameterValue(ParameterNames.MAX_VIDEO_SIZE);

        if (limitParameter != null) {
            video.setSizeLimit(Long.parseLong(limitParameter));
        }

        video.setVideoAudio(getVideoAudio());
        video.setVideoVisual(getVideoVisual());

        return video;
    }

    /**
     * Retrieving video visual model object from request. Prefix giving
     * parameter path where informations can be found.
     * 
     * @param prefix prefix of parameter where parameter can be found.
     * @return video visual model object.
     */
    private VideoVideoVisual getVideoVisual() throws ConverterException {
        VideoVideoVisual videoVisual = new VideoVideoVisual();

        videoVisual
                .setCodec(getParameterValue(ParameterNames.VIDEO_VISUAL_CODEC));

        return videoVisual;
    }

    /**
     * Retrieving video audio model object from request. Prefix giving parameter
     * path where informations can be found.
     * 
     * @param prefix prefix of parameter where parameter can be found.
     * @return video audio model object.
     */
    private VideoVideoAudio getVideoAudio() throws ConverterException {
        VideoVideoAudio videoAudio = new VideoVideoAudio();

        videoAudio
                .setCodec(getParameterValue(ParameterNames.VIDEO_AUDIO_CODEC));

        return videoAudio;
    }

    /**
     * Retrieving image model object from request. Prefix giving parameter path
     * where informations can be found.
     * 
     * @param prefix prefix of parameter where parameter can be found.
     * @return image model object.
     * @throws ConverterException
     */
    private Image getImage() throws ConverterException {
        Image image = new Image();

        setMediaParameters(image);

        String limitParameter = getParameterValue(ParameterNames.MAX_IMAGE_SIZE);

        if (limitParameter != null) {
            image.setSizeLimit(Long.parseLong(limitParameter));
        }

        // Set image width
        image.setWidth(getIntParameterValue(ParameterNames.IMAGE_WIDTH, 0));

        // Set image transformations
        Transformations transformations = getTransformations();

        if (null != transformations) {
            image.setTransformations(transformations);
        }

        return image;
    }

    /**
     * Retrieving audio model object from request. Prefix giving parameter path
     * where informations can be found.
     * 
     * @param prefix prefix of parameter where parameter can be found.
     * @return audio model object.
     */
    private Audio getAudio() throws ConverterException {
        Audio audio = new Audio();

        // Set standard media parameters
        setMediaParameters(audio);

        // Set audio size.
        String limitParameter = getParameterValue(ParameterNames.MAX_AUDIO_SIZE);

        if (limitParameter != null) {
            audio.setSizeLimit(Long.parseLong(limitParameter));
        }

        // Set audio specific parameters.
        audio.setCodec(getParameterValue(ParameterNames.AUDIO_CODEC));

        return audio;
    }

    /**
     * Retrieving transformations model object from request. Prefix giving
     * parameter path where informations can be found.
     * 
     * @param prefix prefix of parameter where parameter can be found.
     * @return transformations model object.
     */
    private Transformations getTransformations() throws ConverterException {
        Transformations transformations = new Transformations();

        addCroppingTransformation(transformations);

        return transformations;
    }

    /**
     * Adds cropping transformation to the specified set of transformations, as
     * specified in resource descriptor parameters.
     * 
     * @param transformations transformations to add to
     */
    private void addCroppingTransformation(Transformations transformations) {
        // Add cropping transformationi if all four cropping borders are
        // specified...
        if (inputParameters.containsName(ParameterNames.RIGHT_X)
                && inputParameters.containsName(ParameterNames.LEFT_X)
                && inputParameters.containsName(ParameterNames.TOP_Y)
                && inputParameters.containsName(ParameterNames.BOTTOM_Y)) {

            // Rewrite them as-is, since no conversion is needed.
            Transformation transformation = new Transformation();

            transformation.setType("Cropping");

            transformation.addAttribute("top",
                    getParameterValue(ParameterNames.TOP_Y));

            transformation.addAttribute("bottom",
                    getParameterValue(ParameterNames.BOTTOM_Y));

            transformation.addAttribute("right",
                    getParameterValue(ParameterNames.RIGHT_X));

            transformation.addAttribute("left",
                    getParameterValue(ParameterNames.LEFT_X));

            transformations.addTransformation(transformation);
        }
    }

    /**
     * Generates possibly unique operation ID.
     * 
     * @return generated operation ID.
     */
    private String generateOperationID() {
        // Currently it returns a random value.
        return Double.toString(Math.random());
    }

    /**
     * Returns the originator ID.
     * 
     * @return the originator ID.
     */
    private String getOriginatorID() {
        return originatorID;
    }

    private String getJobID() {
        return "Job 1";
    }

    private void setMediaParameters(Media media) throws ConverterException {
        // Nothing to do here, since at this moment there are no
        // media-independent parameter names.
    }

    /**
     * Returns parameter value as int number. If no parameter value is
     * specified, default value is returned.
     * 
     * @param parameterName the parameter name
     * @param defaultValue the default value
     * @return the parameter value
     * @throws ConverterException In case parameter is not valid int number.
     */
    private int getIntParameterValue(String parameterName, int defaultValue)
            throws ConverterException {
        String parameterValue = getParameterValue(parameterName);

        if (parameterValue == null) {
            return defaultValue;
        } else {
            try {
                return Integer.parseInt(parameterValue);
            } catch (NumberFormatException e) {
                throw new ConverterException(e);
            }
        }
    }

    /**
     * Returns parameter value, or null if parameter does not exist.
     * 
     * @param name parameter name
     * @return parameter value
     */
    private String getParameterValue(String name) {
        String value = null;

        try {
            value = inputParameters.getParameterValue(name);
        } catch (MissingParameterException e) {
            // NOP
        }

        return value;

    }
}
