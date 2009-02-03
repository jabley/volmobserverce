package com.volantis.xml.pipeline.sax.drivers.googledocs;

import com.volantis.xml.pipeline.sax.dynamic.DynamicProcess;
import com.volantis.xml.pipeline.sax.XMLPipelineContext;
import com.volantis.xml.pipeline.sax.XMLPipelineException;
import com.volantis.xml.pipeline.sax.drivers.web.*;
import com.volantis.shared.net.http.HTTPMessageEntities;
import com.volantis.shared.net.http.SimpleHTTPMessageEntities;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.pipeline.localization.LocalizationFactory;
import org.xml.sax.SAXException;

import java.text.MessageFormat;
import java.io.InputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.net.HttpURLConnection;

/**
 * Helper class used by GDocs rules for Google Authentication 
 */
class Authenticator {

    /*
     * Specific google authentication token keys
     */
    private static final String AUTH_TOKEN_KEY = "Auth";
    private static final String ERROR_TOKEN_KEY = "Error";
    private static final String CAPTCHA_TOKEN_TOKEN_KEY = "CaptchaToken";
    private static final String CAPTCHA_URL_TOKEN_KEY = "CaptchaUrl";

    private static final List ALL_GOOGLE_TOKENS = Arrays.asList(new String[]{AUTH_TOKEN_KEY,
            ERROR_TOKEN_KEY,
            CAPTCHA_TOKEN_TOKEN_KEY,
            CAPTCHA_URL_TOKEN_KEY});

    /*
     * Specific google authentication error codes value(s). See http://code.google.com/apis/accounts/docs/AuthForInstalledApps.html
     */
    private static final String ERROR_CODE_BAD_AUTHENTICATION = "BadAuthentication";
    private static final String ERROR_CODE_NOT_VERIFIED = "NotVerified";
    private static final String ERROR_CODE_TERMS_NOT_AGREED = "TermsNotAgreed";
    private static final String ERROR_CODE_CAPTCHA_REQUIRED = "CaptchaRequired";
    private static final String ERROR_CODE_UNKNOWN = "Unknown";
    private static final String ERROR_CODE_ACCOUNT_DELETED = "AccountDeleted";
    private static final String ERROR_CODE_ACCOUNT_DISABLED = "AccountDisabled";
    private static final String ERROR_CODE_SERVICCE_DISABLED = "ServiceDisabled";
    private static final String ERROR_CODE_SERVICE_UNAVAILABLE = "ServiceUnavailable";

    /*
     * Identifier for the google authentication service
     */
    private static final String GOOGLE_AUTHENTICATION_URL =
            "https://www.google.com/accounts/ClientLogin?" +
                    "accountType=HOSTED_OR_GOOGLE&" +
                    "Email={0}&" +
                    "Passwd={1}&" +
                    "service=writely&" +
                    "source=Volantis-MCS";

    /**
     * URL which should be used to prefix captcha path
     */
    private static final String GOOGLE_CAPTCHA_PREFIX_URL = "http://www.google.com/accounts/";
    
    /**
     * Optional captcha parameters to above request URL
     */
    private static final String GOOGLE_AUTHENTICATION_URL_CAPTCHA_PARAMS = "&logintoken={0}&logincaptcha{1}";

    /*
     * Identifier for the google authorization header name
     */
    private static final String GOOGLE_AUTH_HEADER_KEY = "Authorization";

    /*
     * Identifier for the google authorization header value
     */
    private static final String GOOGLE_AUTH_HEADER_VALUE = "GoogleLogin auth={0}";

    /**
     * Keys and values inserted into Pipeline Error Infos
     */
    private static final String GDOCS_ERROR_CAPTCHA_TOKEN_KEY = "captcha-key";
    private static final String GDOCS_ERROR_CAPTCHA_URL_KEY = "captcha-url";
    private static final String GDOCS_ERROR_LOGIN_KEY = "login";
    private static final String GDOCS_ERROR_LOGIN_KEY_VALID_VALUE = "valid";
    private static final String GDOCS_ERROR_LOGIN_KEY_INVALID_VALUE = "invalid";

    /**
     * Used for logging.
     */
    private static final LogDispatcher LOGGER =
            LocalizationFactory.createLogger(Authenticator.class);

    private static final ExceptionLocalizer EXCEPTION_LOCALIZER =
            LocalizationFactory.createExceptionLocalizer(Authenticator.class);

    private final DynamicProcess dynamicProcess;
    private final XMLPipelineContext context;
    private final AuthData authData;
    private final String id;

    private int googleRequestCounter = 0;
    private static final int MAX_GOOGLE_REQUEST_NUMBER = 2;

    Authenticator(DynamicProcess dynamicProcess, AuthData authData, String id) {
        this.dynamicProcess = dynamicProcess;
        this.context = dynamicProcess.getPipelineContext();
        this.authData = authData;
        this.id = id;
    };

    /**
     * Tries to retrieve token from cache. If it is not possible, requests it from Google.
     * After successful retrieve, sets authentication token header on context. 
     *
     * Authenticator remembers its state. It can only request to Google once.
     *  
     * @throws SAXException
     */
    void authenticate() throws SAXException {
        //obtain authentication token if needed requesting it from Google
        obtainAuthenticationToken();
        //set Authentication header to be use on subsequent request(s)
        setAuthenticationTokenHeader();
    }

    private void obtainAuthenticationToken() throws SAXException {

        //if there was no data in cache
        if (!canRequestGoogle()) {
            XMLPipelineException error = new XMLPipelineException(EXCEPTION_LOCALIZER.format("gdocs-authentication-too-many-attempts", googleRequestCounter), null);
            error.initErrorInfo(id, null, null, null);
            dynamicProcess.fatalError(error);
        }
        
        if (authData.needsRequest()) {
            googleRequestCounter++;
            if  (LOGGER.isDebugEnabled()) {
                if (authData.getUserId() != null) {
                    LOGGER.debug("Trying to obtain authentication token for user "+authData.getUserId()+" from Google. Attempt: " + googleRequestCounter);
                }
            }
            try {
                HTTPClientPluggableHTTPManager httpClientManager = new HTTPClientPluggableHTTPManager();
                httpClientManager.sendRequest(createAuthenticationRequestDetails(authData), context);
            } catch (HTTPException e) {
                //TODO (AK): handle this
                throw new SAXException(e);
            }
        }
    }

    /**
     * Sets Google Authorization header.
     * If there was Google authorization header set already, it is cleared.
     * Header value is retrieved from cache or request
     *
     * It is assumed, that this method is called after setting authentication
     * token on contextAuthData
     */
    private void setAuthenticationTokenHeader() {
        HTTPMessageEntities entities = (HTTPMessageEntities)
                context.getProperty(WebRequestHeader.class);
        if (entities == null) {
            entities = new SimpleHTTPMessageEntities();
        }
        WebRequestHeader header = new WebRequestHeader();
        header.setName(GOOGLE_AUTH_HEADER_KEY);
        header.setValue(MessageFormat.format(GOOGLE_AUTH_HEADER_VALUE, authData.getAuthToken()));
        entities.remove(header.getIdentity());
        entities.add(header);

        context.setProperty(WebRequestHeader.class, entities, false);
    }

    /**
     * Factory method that creates a {@link com.volantis.xml.pipeline.sax.drivers.web.RequestDetails} instance
     * @return
     */
    private RequestDetails createAuthenticationRequestDetails(AuthData authData) {

        String googleAuthUrl = MessageFormat.format(GOOGLE_AUTHENTICATION_URL, authData.getUserId(), authData.getPassword());

        if (authData.isCaptchaSet()) {
            googleAuthUrl += MessageFormat.format(GOOGLE_AUTHENTICATION_URL_CAPTCHA_PARAMS, authData.getCaptchaToken(), authData.getCaptchaKey());
        }

        return new RequestDetails(
                googleAuthUrl,
                googleAuthUrl,
                HTTPRequestType.POST,
                null,
                null,
                false,
                HTTPVersion.HTTP_1_1,
                (HTTPRequestPreprocessor)
                context.getProperty(
                        HTTPRequestPreprocessor.class),
                (HTTPResponsePreprocessor)
                context.getProperty(
                        HTTPResponsePreprocessor.class),
                createHTTPResponseProcessor(authData)
        );
    }

    private HTTPResponseProcessor createHTTPResponseProcessor(AuthData authData) {
        return new HTTPResponseProcessor() {
            // javadoc inherited
            public void processHTTPResponse(String redirectURL,
                                            InputStream responseStream,
                                            int statusCode,
                                            String contentType,
                                            String contentEncoding)
                    throws HTTPException {
                try {
                    processResponse(redirectURL,
                                    responseStream,
                                    statusCode,
                                    contentType,
                                    contentEncoding);
                } catch (SAXException e) {
                    // If the pipeline is already handling this error,
                     if (context.inErrorRecoveryMode()) {
                         // Then we assume that there is a containing try operation that
                         // needs help to avoid the pipeline crashing, so we need to
                         // mask the exception. So just continue on and hope the
                         // pipeline error handling will save us. Note that we cannot
                         // re-report it via fatalError or the flow control manager
                         // objects.
                         if (LOGGER.isDebugEnabled()) {
                             LOGGER.debug("ListDocs rule  encountered XML parsing exception " +
                                     "while error recovery is in progress, ignoring", e);
                         }
                     } else {
                         // Else we assume that there is no containing try operation.
                         // In this case just let the exception propogate up and cause
                         // the pipeline to die.                         
                         if (LOGGER.isDebugEnabled()) {
                             LOGGER.debug("ListDocs rule encountered XML parsing exception " +
                                     "while no error recovery is in progress, rethrowing",
                                     e);
                         }
                         throw new HTTPException(e);
                     }

                } catch (IOException e) {
                    throw new HTTPException(e);
                }
            }
        };
    }

    /**
     * Process the response ensuring that it is conditioned as appropriate and
     * passed to an associated script if one exists.  The content may be stored
     * in the ignoredContent buffer of the response if {@link
     * com.volantis.xml.pipeline.sax.drivers.web.WebDriverConfiguration#setIgnoreContentEnabled} has been called with a
     * true parameter AND EITHER the ignoreContent flag is true OR the content
     * type is one that we have been asked to ignore.
     *
     * @param redirectURL
     *                   if a redirect was followed this parameter will
     *                   reference the URL that was followed. Will be null if a
     *                   redirect did not occur.
     * @param response   an InputStream that can be used to retrieve the actual
     *                   response body.
     * @param statusCode the status of the response.
     * @param contentType
     *                   the content type of the response.
     * @param contentEncoding
     *                   the content encoding of the response.
     */
    private void processResponse(String redirectURL,
                                   InputStream response,
                                   int statusCode,
                                   String contentType,
                                   String contentEncoding)
            throws IOException, SAXException {
        
        Map<String, String> extractedTokens = extractTokens(response);
        String messageKey = "gdocs-authentication-error";
        GDataException gerror;
        HashMap<String, String> errorInfos = new HashMap<String, String>(); 

        if (statusCode == HttpURLConnection.HTTP_OK) {
            // Everything is correct. Extract "Auth" token
            String authToken = extractedTokens.get(AUTH_TOKEN_KEY);
            if (authToken != null) {
                authData.setAuthToken(authToken);
                authData.storeInCache(context);
                return;
            } else {
                gerror = new UnknownError(EXCEPTION_LOCALIZER.format(messageKey, extractedTokens.get(ERROR_TOKEN_KEY)), id);
            }
        } else if (statusCode == HttpURLConnection.HTTP_FORBIDDEN) {

            if (ERROR_CODE_BAD_AUTHENTICATION.equals(extractedTokens.get(ERROR_TOKEN_KEY))) {
               errorInfos.put(GDOCS_ERROR_LOGIN_KEY, GDOCS_ERROR_LOGIN_KEY_INVALID_VALUE);
               gerror = new AuthenticationError(EXCEPTION_LOCALIZER.format(messageKey, ERROR_CODE_BAD_AUTHENTICATION), id, errorInfos);
            } else if (ERROR_CODE_CAPTCHA_REQUIRED.equals(extractedTokens.get(ERROR_TOKEN_KEY)) &&
                       extractedTokens.get(CAPTCHA_TOKEN_TOKEN_KEY) != null &&
                       extractedTokens.get(CAPTCHA_URL_TOKEN_KEY) != null) {                    
                errorInfos.put(GDOCS_ERROR_CAPTCHA_TOKEN_KEY, extractedTokens.get(CAPTCHA_TOKEN_TOKEN_KEY));
                // to make GDocs Connector more user-friendly, add the beginning to captcha_url
                errorInfos.put(GDOCS_ERROR_CAPTCHA_URL_KEY, GOOGLE_CAPTCHA_PREFIX_URL+extractedTokens.get(CAPTCHA_URL_TOKEN_KEY));
                gerror = new AuthenticationError(EXCEPTION_LOCALIZER.format("gdocs-authentication-captcha-required", ERROR_CODE_BAD_AUTHENTICATION), id, errorInfos);
            } else {
               gerror = new UnknownError(EXCEPTION_LOCALIZER.format(messageKey, extractedTokens.get(ERROR_TOKEN_KEY)), id);
            }
        } else {
            gerror = new UnknownError(EXCEPTION_LOCALIZER.format(messageKey), id);
        }

        // if there was no Auth token in HTTP_OK response
        // or response had different status code, throw Pipeline Process Error       
        dynamicProcess.fatalError(gerror);
    }

    /**
     * Extract all knownn tokens from input stream
     */
    private Map<String, String> extractTokens(InputStream response) throws IOException {
        String line;
        String tokenValue = null;
        String tokenName = null;
        HashMap<String, String> extractedTokens = new HashMap<String, String>();
        
        BufferedReader in = new BufferedReader(new InputStreamReader(response));
        int idx;

        while ((line = in.readLine()) != null && extractedTokens.size() < ALL_GOOGLE_TOKENS.size()) {
            if ((idx = line.indexOf('=')) != -1) {
                tokenName = line.substring(0,idx);
                if (ALL_GOOGLE_TOKENS.contains(tokenName)) {
                    tokenValue = line.substring(idx + 1);
                    extractedTokens.put(tokenName, tokenValue);
                }
            }            
        }

        return extractedTokens;
    }
    
    private boolean canRequestGoogle() {
        return googleRequestCounter < MAX_GOOGLE_REQUEST_NUMBER;
    }

}
