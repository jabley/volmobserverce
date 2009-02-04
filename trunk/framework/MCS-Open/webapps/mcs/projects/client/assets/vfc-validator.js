/**
 * (c) Volantis Systems Ltd 2006.
 */

/*
 * Callback handler for use with validators.
 */
Widget.ValidatorCallbackHandler = Class.define(Widget.OptionsContainer,
{
    /*
     * Initialises callbacks.
     */
    initialize: function(options) {
        // Install options
        this.installOptions(options);

        // Create callback handlers
        this.successCallbackHandler = new Widget.CallbackHandler();
        this.failureCallbackHandler = new Widget.CallbackHandler();
        this.fallbackCallbackHandler = new Widget.CallbackHandler();

        // Add default callbacks, reading from
        // onSuccess, onFailure and onFallback options.
        if(this.getOption('onSuccess')) { this.successCallbackHandler.add(this.getOption('onSuccess')); }
        if(this.getOption('onFailure')) { this.failureCallbackHandler.add(this.getOption('onFailure')); }
        if(this.getOption('onFallback')) { this.fallbackCallbackHandler.add(this.getOption('onFallback')); }
    },

    /*
     * Adds callback invoked on validation success.
     */
    addOnSuccess: function(callback) {
        this.successCallbackHandler.add(callback);
    },

    /*
     * Adds callback invoked on validation failure.
     */
    addOnFailure: function(callback) {
        this.failureCallbackHandler.add(callback);
    },

    /*
     * Adds callback invoked on validation fallback.
     */
    addOnFallback: function(callback) {
        this.fallbackCallbackHandler.add(callback);
    },

    /*
     * Invokes success callbacks.
     */
    invokeOnSuccess: function() {
        this.successCallbackHandler.invoke.apply(this.successCallbackHandler, arguments);
    },

    /*
     * Invokes failure callbacks.
     */
    invokeOnFailure: function() {
        this.failureCallbackHandler.invoke.apply(this.failureCallbackHandler, arguments);
    },

    /*
     * Invokes fallback callbacks.
     */
    invokeOnFallback: function() {
        this.fallbackCallbackHandler.invoke.apply(this.fallbackCallbackHandler, arguments);
    }
});

/*
 * Mixin to be used for validator objects,
 * which contains callback handling.
 *
 * This mixin requires the property this.callbackHandler
 * to contain an instance of Widget.ValidatorCallbackHandler.
 */
Widget.ValidatorCallbackMixin = Class.define(
{
    /**
     * Adds callback invoked on validation success.
     *
     * @volantis-api-include-in PublicAPI
     */
    addSuccessCallback: function(callback) {
        this.callbackHandler.addOnSuccess(callback);
    },

    /**
     * Adds callback invoked on validation failure..
     *
     * @volantis-api-include-in PublicAPI
     */
    addFailureCallback: function(callback) {
        this.callbackHandler.addOnFailure(callback);
    },

    /**
     * Adds callback invoked on validation fallback.
     *
     * @volantis-api-include-in PublicAPI
     */
    addFallbackCallback: function(callback) {
        this.callbackHandler.addOnFallback(callback);
    }
});

/*
 * Validation visualizer provides ways to visualize validation results.
 *
 * Following visualization features are available, activated
 * on validation failure:
 *
 *  - change style of input field,
 *  - change style of label element,
 *  - display validation message in specified message area
 *  - show message in specified popup widget
 *  - display a message in standard browser's dialog
 *  - regain focus
 *  - revert to original state, if validation succeeds
 *
 *  Example
 * ---------
 *
 * Create an instance of validation visualizer.
 * In this example it's assumed, that all referred HTML
 * elements are already in place.
 *
 *   var visualizer = new Widget.ValidationVisualizer({
 *       inputId: 'first-name',
 *       labelId: 'first-name-label',
 *       messageAreaId: 'first-name-validation-message',
 *       invalidInputStyle: {backgroundColor: 'yellow'},
 *       invalidLabelStyle: {color: 'red'},
 *       popupId: 'first-name-popup'})
 *
 * Now, to visualize validation failure, invoke following method:
 *
 *   visualizer.validationFailed("Invalid first name")
 *
 * To visualize validation success, invoke following method:
 *
 *   visualizer.validationPassed()
 */
Widget.ValidationVisualizer = Class.define(Widget.Observer, Widget.OptionsContainer,
{
    /*
     * ID of the input element to visualise.
     */
    inputId: null,

    /*
     * ID of the label element to visualise.
     */
    labelId: null,

    /*
     * Id of the element containing area to display validation message.
     * If null, no message will be displayed.
     */
    messageAreaId: null,

    /*
     * Style applied to input field, when validation fails.
     * If null, no styles will be applied.
     */
    invalidInputStyle: null,

    /*
     * Style applied to label element, when validation fails.
     * If null, no styles will be applied.
     */
    invalidLabelStyle: null,

    /*
     * If true, input field will receive the focus back,
     * if validation fails.
     *
     * Note: Very annoying feature, because once the control
     * receives focus, it'll not let you move to another control
     * until you correct your input.
     *
     * Before using this feature, consider it twice.
     */
    autoFocus: false,

    /*
     * ID of the popup widget to be displayed when validation fails.
     * If null, popup will not ber displayed.
     */
    popupId: null,

    /*
     * Controls, if validator will display alerts
     * on validation failures.
     */
    displayAlerts: false,

    /*
     * Function invoked immediately after user dismissed
     * validation visualisation (popups, dialog boxes)
     */
    onDismiss: null,

    /*
     * Initialisation method.
     */
    initialize: function(options) {
        // Install all options
        this.installOptions(options);
    },

    /*
     * Visualizes validation failure.
     *
     * @param message The message to be displayed.
     */
    validationFailed: function(message) {

        this.setInvalidStyle();

        this.setMessage(message);

        this.showPopup(message);

        if (this.displayAlerts) {
            alert(message.unescapeHTML());
        }

        // If there's no popup widget to display,
        // visuzalisation is finished, since no user interaction
        // is needed anymore. If there is popup, visualization
        // is dismissed after user dismisses that popup.
        if (!this.getPopup()) {
            this.visualizationDismissed();
        }
    },

    /*
     * Visualizes validation success.
     */
    validationPassed: function() {

        this.setOriginalStyle();

        this.dismissPopup();

        this.setMessage();
    },

    /*
     * Returns input element to validate its value,
     * or null if it does not exist.
     */
    getInputElement: function() {
        if (this.inputId) {
            return $(this.inputId);
        }
    },

    /*
     * Returns the label element, of null if it does not exist.
     */
    getLabelElement: function() {
        if (this.labelId) {
            return $(this.labelId);
        }
    },

    /*
     * Returns the popup widget to display validation messages,
     * or null if it's not specified.
     */
    getPopup: function() {
        if (!this.popup) {
            this.popup = Widget.getInstance(this.popupId);

            if (this.popup) {
                this.observe(this.popup, "dismissed", "visualizationDismissed");
                //this.popup.addDismissCallback(this.visualizationDismissed.bind(this))
            }
        }

        return this.popup;
    },

    /*
     * Returns the message area to display validation messages,
     * or null if messages are not to be displayed there.
     */
    getMessageArea: function() {
        return $(this.messageAreaId);
    },

    /*
     * Sets style indicating validation failure.
     */
    setInvalidStyle: function() {
        // First, revert styles to originals.
        this.setOriginalStyle();

        // Replace styles for input element.
        var inputElement = this.getInputElement();

        if (inputElement) {
            if (this.invalidInputStyle) {
                inputElement.vfcReplaceStyle(this.invalidInputStyle);
            }
        }

        // Replace styles for label element (if there is one).
        var labelElement = this.getLabelElement();

        if (labelElement) {
            if (this.invalidLabelStyle) {
                labelElement.vfcReplaceStyle(this.invalidLabelStyle);
            }
        }
    },

    /*
     * Reverts all styles to originals.
     */
    setOriginalStyle: function() {
        // Revert to original styles on input element.
        var inputElement = this.getInputElement();

        if (inputElement) {
            inputElement.vfcRevertStyle();
        }

        // Revert to original styles on label element (if there is any).
        var labelElement = this.getLabelElement();

        if (labelElement) {
            labelElement.vfcRevertStyle();
        }
    },

    /*
     * Sets focus on input field.
     */
    setFocus: function() {
        var inputElement = this.getInputElement();

        if (inputElement) {
            inputElement.focus();
        }
    },

    /*
     * Shows popup with given message, if popup exists.
     *
     * @param message Popup content as HTML string.
     */
    showPopup: function(message) {
        popup = this.getPopup();

        if (popup) {
            popup.show();
        }
    },

    /*
     * Dismisses popup, if exists.
     */
    dismissPopup: function() {
        popup = this.getPopup();

        if (popup) {
            popup.hide();
        }
    },

    /*
     * If there exists area for messages,
     * it displays given message in that area.
     *
     * @param message Message to display as HTML string.
     */
    setMessage: function(message) {
        var messageArea = this.getMessageArea();

        if (messageArea) {
            messageArea.innerHTML = message ? message : "";
        }
    },

    /*
     * Invoked, when visualization is dismissed.
     */
    visualizationDismissed: function() {
        // Now, after all visualization has been dismissed,
        // it's time to set focus on input element.
        if (this.autoFocus) {
            this.setFocus();
        }

        // Invoke dismiss callback.
        if (this.onDismiss) {
            // Perform onDismiss callback using timers, to make sure
            // that the 'blur' event occured by invoking the setFocus()
            // method above will be handled before the callback.
            setTimeout(this.onDismiss, 0);
        }
    }
});

/*
 * Offline Validation Engine, responsible for validating a value
 * against specified input format.
 *
 *  Usage
 * -------
 *
 * Create an instance of the engine, providing an input format.
 * Perform validation invoking validate() method with a value
 * to validate.
 *
 *  Example
 * ---------
 *
 * Create an instance of validation engine:
 *
 *    var engine = new Widget.OfflineValidationEngine("##-###")
 *
 * Perform validation:
 *
 *   var result = engine.validate("12-432")
 */
Widget.OfflineValidationEngine = Class.define(Widget.OptionsContainer,
{
    /*
     * Input format
     */
    inputFormat: null,

    /*
     * Initializator/constructor.
     */
    initialize: function(inputFormat) {
        this.inputFormat = inputFormat;
    },

    /*
     * Validates value against input format, and returns
     * validation result.
     *
     * @param value The value to validate
     * @return validation result as boolean value
     */
    validate: function(value) {
        var result = true;

        if (this.inputFormat) {
            var matcher = new RegEx(this.inputFormat, value);

            result = matcher.match();
        }

        return result;
    },

    /*
     * Sets validation input format.
     */
    setInputFormat: function(inputFormat) {
        this.inputFormat = inputFormat;
    }
});

/*
 * Online Validation Engine, responsible for sending validation
 * requests and processing responses with validation results.
 *
 *  Usage
 * -------
 *
 * Create an instance of the engine, providing following data:
 *  - an URL of the validator
 *  - set of callbacks (because validation is asynchronous)
 *
 * Now, whenether you want to perform validation, invoke validate()
 * function with a mapping of field names and values. After validation
 * is performed, one of the specified callback will be called.
 *
 *  Communication protocol
 * ------------------------
 *
 * This engine will use specified URL to get validatiohn results.
 * Field values will be added to the URL in HTML query format.
 *
 * Ie. for URL 'http://www.validator.com/validate.html' and field
 * values firstName='John' and lastName='Smith', the validation URL
 * would be 'http://www.validator.com/validate.html?firstName=John&lastName=Smith'
 *
 * Response should be a HTML fragment, including element with validation
 * messages, and a script tag containing specific method call.
 *
 * Following example illustrates proper response content:
 *
 *   <div id='validation-message'>Validation failed</div>
 *   <div id='first-name-message'>Invalid first name</div>
 *   <div id='last-name-message'>Invalid last name</div>
 *
 *   script
 *       Widget.processOnlineValidationResponse(false, 'validation-message', {
 *           firstName: 'first-name-message',
 *           lastName: 'last-name-message'})
 *   script
 *
 * Note, that the IDs used in response should not clash with the IDs
 * already used on the page, from which the validation is invoked.
 *
 *  Example
 * ---------
 *
 * var onSuccess = function() {...}
 * var onFailure = function(reason, fields) {...}
 * var onFallback = function() {...}
 *
 * var engine = new Widget.OnlineValidationEngine({
 *     url: "http://www.my-page.com/validator.html",
 *     onSuccess: onSuccess,
 *     onFailure: onFailure,
 *     onFallback: onFallback})
 *
 * engine.validate({firstName: 'Bruce', lastName: 'Lee'})
 */
Widget.OnlineValidationEngine = Class.define(Widget.OptionsContainer, Widget.ValidatorCallbackMixin,
{
    /*
     * URL of the validator.
     */
    url: null,

    /*
     * Initializator/constructor.
     */
    initialize: function(options) {
        this.installOptions(options);

        this.callbackHandler = new Widget.ValidatorCallbackHandler(options);
    },

    /*
     * Invokes validation. When validation finished,
     * one of the registered callbacks will be called:
     *  - onSuccess - when validation passes
     *  - onFailure - when validation fails
     *  - onFallback - when validation could not be performed.
     *
     * @param fields The mapping between field names and values.
     */
    validate: function(fields) {
        if (!this.url) {
            // No URL, no validation.
            this.validationPassed();
        } else {
            // Create and spawn AJAX request.
            this.request = new Widget.AjaxRequest(this.url, {
                parameters: $H(fields).toQueryString(),
                method: "get",
                onSuccess: this.onAJAXRequestSuccess.bind(this),
                onFailure: this.onAJAXRequestFailure.bind(this),
                onException: this.onAJAXRequestFailure.bind(this)});
        }
    },

    /*
     * Internal AJAX callback, invoked on HTTP success.
     */
    onAJAXRequestSuccess: function(httpRequest) {
        this.response = httpRequest.responseText;

        // Insert response content, stripping unnessecary scripts.
        this.getResponseArea().innerHTML = this.response.stripScripts();

        // Evaluates scripts from the response content.
        setTimeout(this.evalResponseScripts.bind(this), 10);
    },

    /*
     * Internal AJAX callback, invoked on HTTP failure.
     */
    onAJAXRequestFailure: function() {
        this.validationFallback();
    },

    /*
     * Creates and returns new HTML element, which can act
     * as a placeholder for response content.
     */
    createResponseArea: function() {
        var responseArea = document.createElement("div");

        var responseAreaElement = Element.extend(responseArea);

        responseAreaElement.setStyle({display: 'none'});

        document.body.appendChild(responseArea);

        return responseAreaElement;
    },

    /*
     * Returns a HTML element, which acts as a placeholder
     * for response content.
     */
    getResponseArea: function() {
        if (!this.responseArea) {
            this.responseArea = this.createResponseArea();
        }

        return this.responseArea;
    },

    /*
     * Evaluates scripts enclosed in response content.
     */
    evalResponseScripts: function() {
        // Clear the flag, indicating that the response
        // has not been processed.
        this.responseProcessed = false;

        // Set the current validation engine to this one,
        // so that the script included in the response
        // will invoke processResponse() method on this
        // particular instance.
        Widget.currentOnlineValidationEngine = this;

        // Evaluate response scripts, which will invoke
        // processResponse() method.
        this.response.evalScripts();

        // Clear response area (because sometimes display:none
        // does not help).
        this.getResponseArea().innerHTML = "";

        // After response is processed, reset current
        // validator.
        Widget.currentOnlineValidationEngine = null;

        // Check if response has been processed.
        // If not, invoke the fallback.
        if (!this.responseProcessed) {
            this.validationFallback();
        }
    },

    /*
     * Processes validator response
     */
    processResponse: function(result, messageId, fieldMessageIds) {
        if (result) {
            this.validationPassed();

        } else {
            var message = messageId ? $(messageId).innerHTML : null;

            var fieldMessages = {};

            for (var id in fieldMessageIds) {
              if (!Prototype.hiddenAttr(id)) {
                fieldMessages[id] = fieldMessageIds[id] ? $(fieldMessageIds[id]).innerHTML : null;
              }
            }

            this.validationFailed(message, fieldMessages);
        }

        // Set the flag indicating, that response has been processed.
        this.responseProcessed = true;
    },

    /*
     * Function invoked, when validation fails.
     *
     * @param message The HTML content of the validation message
     * @param fieldMessages The mapping between fields, which failed
     *    validation, and field-specific failure messages (as HTMLs).
     */
    validationFailed: function(message, fieldMessages) {
        this.callbackHandler.invokeOnFailure(message, fieldMessages);
    },

    /*
     * Function invoked, when validation passes.
     */
    validationPassed: function() {
        this.callbackHandler.invokeOnSuccess();
    },

    /*
     * Function invoked, when validation could not be performed.
     */
    validationFallback: function() {
        this.callbackHandler.invokeOnFallback();
    },

    /*
     * Sets validation URL.
     */
    setURL: function(url) {
        this.url = url;
    }
});

/*
 * Function invoked within OnlineValidationEngine response content
 * to pass validation results.
 *
 * @param result The result of the validation (true of false)
 * @param messageId The ID of the element containing failure message
 * @param fields The mapping between fields and element IDs containing failure messages
 */
Widget.processOnlineValidationEngineResponse = function(result, messageId, fieldMessageIds) {
    var engine = Widget.currentOnlineValidationEngine;

    if (engine) {
        engine.processResponse(result, messageId, fieldMessageIds);
    }
};

/*
 * This is global flag enabling/disabling simple validation.
 * Used to prevent entering an infinite focus/blur event loop.
 */
Widget.isSimpleValidationDisabled = false;

/*
 * Simple validator provides offline and online validation
 * on single input field.
 */
Widget.SimpleValidator = Class.define(Widget.OptionsContainer, Widget.ValidatorCallbackMixin,
{
    /*
     * ID of the input element with value to validate.
     */
    inputId: null,

    /*
     * Input format for off-line validation.
     * If null, no off-line validation is performed.
     */
    inputFormat: null,

    /*
     * URL of the online validator.
     * If null, no on-line validation is performed.
     */
    url: null,

    /*
     * Id of the element containing empty validation message content.
     * If null, this.emptyMessage will be used as message content.
     */
    emptyMessageId: null,

    /*
     * Id of the element containing invalid validation message content.
     * If null, this.invalidMessage will be used as message content.
     */
    invalidMessageId: null,

    /*
     * Message (as HTML string) displayed when validation fails
     * due to empty value.
     */
    emptyMessage: "Input required",

    /*
     * Message (as HTML string) displayed when validation fails,
     * due to other than non-empty value.
     */
    invalidMessage: "Input invalid",

    /*
     * Initialisation method.
     */
    initialize: function(options) {
        // Install all options
        this.installOptions(options);

        // Create an instance of callback handler.
        this.callbackHandler = new Widget.ValidatorCallbackHandler(options);

        // Create an instance of off-line validation engine,
        // to validate input against specified format.
        this.offlineEngine = new Widget.OfflineValidationEngine(this.inputFormat);

        // Create an instance of on-line validation engine,
        // to perform an on-line validation.
        this.onlineEngine = new Widget.OnlineValidationEngine({
            url: this.url,
            onSuccess: this.onEngineSuccess.bind(this),
            onFailure: this.onEngineFailure.bind(this),
            onFallback: this.onEngineFallback.bind(this)});

        // Create an instance of validation visualizer,
        // to visualize validation results.
        this.visualizer = new Widget.ValidationVisualizer(options);

        // Register a callback on visualization dismiss, which
        // would re-enable simple validation, which was temporarily
        // disabled during UI handling.
        this.visualizer.onDismiss = this.onVisualizerDismiss.bind(this);
    },

    /**
     * Set onblur or mouseout event
     */
    setInputEvents: function() {
      this.element = $(this.inputId);
      // if empty event attribute rendered then set appropriate event function
      if(this.element.onblur) {
        if(Prototype.useMouseAsSelect()) {
          Event.observe(this.element,Widget.MOUSEOUT, function() { Widget.getInstance(this.inputId).validate() }.bind(this) );
        } else {
          Event.observe(this.element,Widget.BLUR, function() { Widget.getInstance(this.inputId).validate() }.bind(this) );
        }
      }
    },

    /**
     * Causes element to run validation on element.
     *
     * @volantis-api-include-in PublicAPI
     */
    validate: function() {
        // If simple validation is disabled, do nothing.
        if (Widget.isSimpleValidationDisabled) {
            return;
        }

        // First, perform offline validation.
        // If it fails, return immediately without performing
        // on-line validation.
        if (this.offlineEngine) {
            // Get input value to validate.
            var inputValue = this.getInputValue();

            offlineResult = this.offlineEngine.validate(inputValue);

            if (!offlineResult) {
                if (inputValue === "") {
                    this.onEngineFailure(this.getEmptyMessage());
                } else {
                    this.onEngineFailure(this.getInvalidMessage());
                }

                return;
            }
        }

        // If off-line validation passes, perform the
        // on-line validation.
        if (this.onlineEngine) {
            this.onlineEngine.validate();

        } else {
            // If there is no on-line validation engine,
            // assume validation success.
            this.onEngineSuccess();
        }
    },

    /*
     * Perform only offline validation, and returns its result.
     *
     * @returns The offline validation result.
     */
    validateOffline: function() {
        if (this.offlineEngine) {
            // Get input value to validate.
            var inputValue = this.getInputValue();

            result = this.offlineEngine.validate(inputValue);

            if (result) {
                this.onEngineSuccess();
            } else {
                if (inputValue === "") {
                    this.onEngineFailure(this.getEmptyMessage());
                } else {
                    this.onEngineFailure(this.getInvalidMessage());
                }
            }
        }

        return result;
    },

    /*
     * Function invoked on validation success.
     */
    validationPassed: function() {
        // Visualize validation success.
        this.visualizer.validationPassed();

        // Call registered callbacks.
        this.callbackHandler.invokeOnSuccess();
    },

    /*
     * Function invoked on validation failure.
     *
     * @param message The validation failure reason as HMTL.
     */
    validationFailed: function(message) {
        // Temporarily disable simple validation until user
        // dismisses all UI features (popups, focuses etc).
        // It'll be re-enabled by onVisualizerDismiss() callback
        // registered on the visualizer.
        Widget.isSimpleValidationDisabled = true;

        // Visualize validation failure
        this.visualizer.validationFailed(message);
    },

    /*
     * Function invoked on validation success.
     */
    onEngineSuccess: function() {
        // Visualize validation success.
        this.validationPassed();
    },

    /*
     * Function invoked on validation failure.
     *
     * @param message The validation failure reason as HMTL.
     * @param fieldMessages Mapping between failed fields and its messages.
     */
    onEngineFailure: function(message, fieldMessages) {
        // Visualize validation failure
        this.validationFailed(message);
    },

    /*
     * Function invoked if validation could not be performed.
     */
    onEngineFallback: function() {
        // Call registered callback.
        this.callbackHandler.invokeOnFallback();
    },

    /*
     * Action invoked after used dismissed visualization.
     * Re-enables simple validation, which was temporarily disabled
     * and invokes all callbacks.
     */
    onVisualizerDismiss: function() {
        // Re-enable simple validation.
        Widget.isSimpleValidationDisabled = false;

        // Call registered callback.
        this.callbackHandler.invokeOnFailure();
    },

    /*
     * Returns input element to validate.
     */
    getInputElement: function() {
        return $(this.inputId);
    },

    /*
     * Returns input value to validate.
     */
    getInputValue: function() {
        return this.getInputElement().value;
    },

    /*
     * Returns the element containing content of the
     * empty validation message, of null if it does not exist.
     */
    getEmptyMessageElement: function() {
        if (this.emptyMessageId) {
            return $(this.emptyMessageId);
        }
    },

    /*
     * Returns the element containing content of the
     * invalid validation message, of null if it does not exist.
     */
    getInvalidMessageElement: function() {
        if (this.invalidMessageId) {
            return $(this.invalidMessageId);
        }
    },

    /*
     * Returns the empty validation message.
     */
    getEmptyMessage: function() {
        var element = this.getEmptyMessageElement();

        if (element) {
            return element.innerHTML;
        } else {
            return this.emptyMessage;
        }
    },

    /*
     * Returns the invalid validation message.
     */
    getInvalidMessage: function() {
        var element = this.getInvalidMessageElement();

        if (element) {
            return element.innerHTML;
        } else {
            return this.invalidMessage;
        }
    },

    /*
     * Sets validation input format.
     */
    setInputFormat: function(inputFormat) {
        this.inputFormat = inputFormat;
        this.offlineEngine.setInputFormat(inputFormat);
    },

    /*
     * Sets validation URL.
     */
    setURL: function(url) {
        this.url = url;
        this.onlineEngine.setURL(url);
    }
});

/*
 * Function invoked within SimpleValidator response content
 * to pass validation results.
 *
 * @param result The result of the validation (true of false)
 * @param messageId The ID of the element with failure message
 */
Widget.processSimpleValidatorResponse = function(result, messageId) {
    // Because SimpleValidator is implemented using
    // OnlineMultipleValidationEngine, redirect response
    // to be processed by that engine.
    Widget.processOnlineValidationEngineResponse(result, messageId, {});
};

/*
 * Multiple validator widget provides on-line validation
 * for any number of fields.
 */
Widget.MultipleValidator = Class.define(Widget.OptionsContainer, Widget.ValidatorCallbackMixin,
{
    /*
     * Definitions of fields to validate.
     */
    fields: {},

    /*
     * URL of the validator.
     */
    url: null,

    /*
     * Area to display validation message.
     * If null, no message will be displayed.
     */
    messageArea: null,

    /*
     * Auto focus feature
     */
    autoFocus: false,

    /*
     * Popup widget used to display a message, if validation fails
     * If null, no popup will be displayed.
     */
    popup: null,

    /*
     * If true, if moves focus to the first failed field.
     */
    autoFocus: false,

    /*
     * Initialisation method.
     */
    initialize: function(options) {
        // Install all options.
        this.installOptions(options);

        // Create an instance of callback handler.
        this.callbackHandler = new Widget.ValidatorCallbackHandler(options);

        // Create an instance of validation engine.
        this.engine = new Widget.OnlineValidationEngine({
            url: this.url,
            onSuccess: this.onEngineSuccess.bind(this),
            onFailure: this.onEngineFailure.bind(this),
            onFallback: this.onEngineFallback.bind(this)});

        // Create global validation visualiser
        this.visualizer = new Widget.ValidationVisualizer(options);

        // Register dismiss callback, so that focus will be set just
        // after visualization is dismissed.
        this.visualizer.onDismiss = this.setFocus.bind(this);

        // For each field, create a SimpleValidator instance.
        this.visualizers = {};

        for (var id in this.fields) {
          if (!Prototype.hiddenAttr(id)) {
            this.visualizers[id] = new Widget.ValidationVisualizer(this.fields[id]);
          }
        }
    },


    /**
     * Performs validation.
     * If validation passes, it reverts all field styles to original state.
     * If validation fails, all styles for all fields are changed to invalid,
     * focus is set on first invalid field, and if popup widget is provided,
     * it's dispayed with the validation message.
     *
     * @volantis-api-include-in PublicAPI
     */
    validate: function() {
        this.prepareForValidation();
        this.validateSimpleThenMultiple();
    },

    /*
     * Validation function designed to be used within the wizard.
     * It does not invoke simple validators, since they'll be invoked
     * individually by the wizard.
     */
    validateForWizard: function() {
        this.prepareForValidation();

        this.validateMultiple();
    },

    /*
     * Action invoked to prepare for validation
     */
    prepareForValidation: function() {
        this.focusElement = null;

        // Clear visualization results
        for (var id in this.visualizers) {
          if (!Prototype.hiddenAttr(id)) {
            this.visualizers[id].validationPassed();
          }
        }

        this.visualizer.validationPassed();
    },

    /*
     * Returns validator, which would perform
     * simple validation.
     */
    getSimpleValidator: function() {
        // If it's not already created, create it now.
        if (!this.simpleValidator) {
            // Prepare instance of chained validator, which
            // would validate all simple validators embedded
            // within this multiple validator
            var simpleValidators = new Array();

            for (var id in this.fields) {
              // Get an instance of SimpleValidator - it has the same ID
              // as the inputId
              // TODO: This restriction should be relaxed in the future,
              // so that user should explicitely specify list of simple
              // validators to be invoked before multiple validation occurs.

              if (!Prototype.hiddenAttr(id)) {
                var validator = Widget.getInstance(this.fields[id].inputId);
                if (validator) {
                    simpleValidators.push(validator);
                }
              }
            }

            this.simpleValidator = new Widget.ChainedValidator(simpleValidators, {
                failFast: false,
                onSuccess: this.onSimpleSuccess.bind(this),
                onFailure: this.onSimpleFailure.bind(this),
                onFallback: this.onSimpleFallback.bind(this)});
        }
        
        return this.simpleValidator;
    },

    /*
     * Invokes simple validation, and
     * them multiple validation.
     */
    validateSimpleThenMultiple: function() {
        this.getSimpleValidator().validate();
    },

    /*
     * Invokes multiple validation only.
     */
    validateMultiple: function() {
        var fieldValues = this.getFieldValues();
        this.engine.validate(fieldValues);
    },

    /*
     * Invoked when simple validation passes.
     * If so, invoke multiple validation now.
     */
    onSimpleSuccess: function() {
        this.validateMultiple();
    },

    /*
     * Invoked when simple validation fails.
     * If so, do not invoke multiple validation and
     * report a failure immediately.
     */
    onSimpleFailure: function() {
        this.callbackHandler.invokeOnFailure();
    },

    /*
     * Invoked on simple validation fallback.
     * If so, do not invoke multiple validation and
     * report a fallback immediately.
     */
    onSimpleFallback: function() {
        this.callbackHandler.invokeOnFallback();
    },

    /*
     * Invoked, when multiple validation passes.
     */
    onEngineSuccess: function() {
        this.callbackHandler.invokeOnSuccess();
    },

    /*
     * Invoked, when multiple validation fails.
     */
    onEngineFailure: function(message, fields) {
        var firstFailedField = true;

        for (var id in this.fields) {
          if (!Prototype.hiddenAttr(id)) {
            if (fields[id] === undefined) {
                this.visualizers[id].validationPassed();

            } else {
                this.visualizers[id].validationFailed(fields[id]);

                if (firstFailedField && this.autoFocus) {
                    this.focusElement = this.getInputElement(id);

                    firstFailedField = false;
                }
            }
          }
        }

        this.visualizer.validationFailed(message);

        this.callbackHandler.invokeOnFailure();
    },

    /*
     * Invoked, on multiple validation fallback.
     */
    onEngineFallback: function() {
        this.callbackHandler.invokeOnFallback();
    },

    /*
     * Returns input element for given field ID.
     */
    getInputElement: function(id) {
        return $(this.fields[id].inputId);
    },

    /*
     * Reads and returns values of all fields.
     */
    getFieldValues: function() {
        var values = {};

        for (var id in this.fields) {
          if (!Prototype.hiddenAttr(id)) {
            values[id] = this.getInputElement(id).value;
          }
        }
        return values;
    },

    /*
     * Sets validation URL.
     */
    setURL: function(url) {
        this.url = url;
        this.engine.setURL(url);
    },

    /*
     * Sets focus on first field which failed during last validation.
     */
    setFocus: function() {
        if (this.focusElement) {
            this.focusElement.focus();
        }
    }
});

/*
 * Function invoked within MultipleValidator response content
 * to pass validation results.
 *
 * @param result The result of the validation (true of false)
 * @param messageId The ID of the element containing failure message
 * @param fields The mapping between fields and element IDs containing failure messages
 */
Widget.processMultipleValidatorResponse = function(result, message, fields) {
    // Because MultipleValidator is implemented using
    // OnlineMultipleValidatorEngine, redirect response
    // to be processed by that engine.
    Widget.processOnlineValidationEngineResponse(result, message, fields);
};

/*
 * Generic function invoked within SimpleValidator or MultipleValidator
 * response content to pass validation results.
 *
 * @param result The result of the validation (true of false)
 * @param messageId The ID of the element containing failure message
 * @param fields The mapping between fields and element IDs containing failure messages
 */
Widget.processValidatorResponse = function(result, message, fields) {
    // Because MultipleValidator is implemented using
    // OnlineMultipleValidatorEngine, redirect response
    // to be processed by that engine.
    Widget.processOnlineValidationEngineResponse(result, message, fields);
};

/*
 * Validator, which invokes chain of child validators.
 *
 * The result of this validator is a logical conjuction
 * of the child validators' results.
 *
 * Usage: To create a chained validator, pass an array of
 * child validators in the constructor:
 *
 *  chainedValidator = new Widget.ChainedValidator(new Array(validatorA, validatorB, validatorC, ...))
 */
Widget.ChainedValidator = Class.define(Widget.OptionsContainer, Widget.ValidatorCallbackMixin,
{
    /*
     * Array with chained validators
     */
    validators: null,

    /*
     * If true, validation will brake after first failure (or fallback).
     * Default value is false.
     */
    failFast: false,

    /*
     * Initialisation method.
     */
    initialize: function(validators, options) {
        this.installOptions(options);

        // Make sure that this.validators is not null.
        this.validators = validators ? validators : new Array();

        // Create callback handler
        this.callbackHandler = new Widget.ValidatorCallbackHandler(options);

        // Add callbacks on child validators.
        for(index = 0; index<this.validators.length; index++) {
            var validator = this.validators[index];

            validator.addSuccessCallback(this.onValidatorSuccess.bind(this));
            validator.addFailureCallback(this.onValidatorFailure.bind(this));
            validator.addFallbackCallback(this.onValidatorFallback.bind(this));
        }

        // This flag controls, whether we are currently during
        // validation.
        this.isDuringValidation = false;
    },

    /*
     * Performs validation, invoking appropriate callback
     * when validation result is available.
     */
    validate: function() {
        this.isDuringValidation = true;

        this.nextValidatorIndex = 0;

        this.currentResult = true;
        this.validateNextStep();
    },

    /*
     * Performs validation on the next validator in chain.
     */
    validateNextStep: function() {
        var finishValidation = false;

        // Check current validation result.
        // If it's different than 'success' and 'failFast' flag is set,
        // finish validation immediately.
        if (this.currentResult !== true) {
            if (this.failFast) {
                finishValidation = true;
            }
        }

        // Check, if there are more validation steps to do.
        // If no, finish validation.
        if (this.nextValidatorIndex >= this.validators.length) {
            finishValidation = true;
        }

        // Finish or continue validation.
        if (finishValidation) {
            this.finishValidation();
        } else {
            validator = this.validators[this.nextValidatorIndex];
            validator.validate();
        }
    },

    /*
     * Invoked, when validation of the current validator
     * in chain finished.
     */
    validateStepFinished: function() {
        this.nextValidatorIndex++;

        this.validateNextStep();
    },

    /*
     * Invoked, when validation of the current child
     * validator passed.
     */
    onValidatorSuccess: function() {
        if (this.isDuringValidation) {
            this.validateStepFinished();
        }
    },

    /*
     * Invoked, when validation of the current child
     * validator failed.
     */
    onValidatorFailure: function() {
        if (this.isDuringValidation) {
            if (this.currentResult === true) {
                this.currentResult = false;
            }

            this.validateStepFinished();
        }
    },

    /*
     * Invoked, on validation of the current child
     * validator fallback.
     */
    onValidatorFallback: function() {
        if (this.isDuringValidation) {
            if (this.currentResult === true) {
                this.currentResult = null;
            }

            this.validateStepFinished();
        }
    },

    /*
     * Invoked on validation finish.
     */
    finishValidation: function() {
        if (this.currentResult === true) {
            this.callbackHandler.invokeOnSuccess();

        } else if (this.currentResult === false) {
            this.callbackHandler.invokeOnFailure();
        } else {
            this.callbackHandler.invokeOnFallback();
        }

        this.isDuringValidation = false;
    }
});
