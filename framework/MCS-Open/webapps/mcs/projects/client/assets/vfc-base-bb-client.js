/**
 * (c) Volantis Systems Ltd 2008. 
 */

Widget.Internal.Client = Widget.define(
{
  url: null,
  parameters: null,
  json: false,

  initialize: function(options) {
    this.initializeWidget(null, options)
  },

  getURL: function() {
    return this.url
  },

  setURL: function(url) {
    Widget.log("Client", "Changing URL to: " + url)

    this.url = url

    this.notifyObservers("urlChanged")
    this.notifyObservers("canSendRequestChanged")
  },

  getParameters: function() {
    return this.parameters
  },

  setParameters: function(parameters) {
    Widget.log("Client", "Changing parameters to: " + parameters)

    this.parameters = parameters

    this.notifyObservers("parametersChanged")
  },

  expectsJSONResponse: function() {
    return this.json
  },

  setExpectsJSONResponse: function(expectsJSONResponse) {
    this.json = expectsJSONResponse
  },

  isBusy: function() {
    return this.pendingAJAXRequest != null
  },

  /**
   * Sends request using specified URL and parameters.
   * Before sending request, parameter values are appended to the query
   * component of the URL, using HTTP query syntax.
   *
   * This method returns immediately after request is sent.
   *
   * When response is received, a 'requestSucceeded' notification is sent,
   * with response content passed in the first argument.
   *
   * When request fails, a 'requestFailed' notification is sent,
   * with failure message string passed in the first argument,
   *
   * Together with 'requestSucceeded', one of two notifications is sent,
   * which may be used to handle user-errors in simplier way:
   *  - 'requestSucceededWithUserError'
   *  - 'requestSucceededWithoutUserError'
   *
   * Sending a request implicitely interrupts currently pending request.
   */
  sendRequest: function() {
    if (this.canSendRequest()) {
      this.interruptRequest()

      Widget.log("Client", "Sending AJAX request")

      this.setPendingAJAXRequest(
        new Ajax.Request(this.url, {
                  parameters: $H(this.parameters || {}).toQueryString(),
                  method: "get",
                  onSuccess: this.ajaxRequestSucceeded.bind(this),
                  onFailure: this.ajaxRequestFailed.bind(this),
                  onException: this.ajaxRequestThrownException.bind(this)}))
    }
  },

  canSendRequest: function() {
    return this.url != null
  },

  /**
   * Interrupts currently pending request, if any.
   */
  interruptRequest: function() {
    if (this.canInterruptRequest()) {
      Widget.log("Client", "Interrupting AJAX request")

      // Rather than interrupting AJAX request,
      // we just stop listening to its results.
      this.pendingAJAXRequest.options['onSuccess'] = null
      this.pendingAJAXRequest.options['onFailure'] = null
      this.pendingAJAXRequest.options['onException'] = null

      this.setPendingAJAXRequest(null)

      this.notifyObservers("requestInterrupted")
    }
  },

  canInterruptRequest: function() {
    return this.pendingAJAXRequest != null
  },

  /**
   * Invoked on AJAX request success.
   */
  ajaxRequestSucceeded: function(transport) {
    Widget.log("Client", "AJAX request succeeded")

    setTimeout(this.processAjaxRequestSuccess.bind(this, transport), 0)
  },

  processAjaxRequestSuccess: function(transport) {
    this.setPendingAJAXRequest(null)

    if (this.json) {
      // If the response is JSON string, simply evaluate it
      // and store on this.responseContent field.
      try {
        this.responseContent = eval(transport.responseText)

      } catch(e) {
        this.notifyObservers("requestFailed", "Error evaluating JSON. " + e)

        return
      }

    } else {
      // Otherwise, if response is a fragment of HTML containing
      // script parts, do the following:

      // Get response text out of the request.
      var html = transport.responseText

      // Insert response content into HTML, so that widgets included
      // within the scripts may access it by ID.
      this.getResponseArea().innerHTML = html.stripScripts()

      // Evaluate the JavaScript from the response content.
      // An instance of ServiceResponse object should be stored
      // on this.response field.
      this.responseContent = null

      try {
        var scripts = html.extractScripts()

        for (var i = 0; i < scripts.length; i++) {
          eval(scripts[i])
        }
      } catch(e) {
        this.notifyObservers("requestFailed", "Error evaluating response scripts. " + e)
        return
      }

      // After scripts are invoked, clear the response area,
      // since it's no more used.
      this.getResponseArea().innerHTML = ""
    }

    // Notify observers with success
    this.notifyObservers("requestSucceeded", this.responseContent)

    // Send alternative notification, with information whether
    // response contains Widget.Response.Error or other content.
    // This simplifies task of handling user-errors.
    if (this.responseContent != null && this.responseContent._widgetResponseErrorToken == true) {
      // Case 1: Response contains Widget.Response.Error.
      this.notifyObservers("requestSucceededWithError", this.responseContent)

    } else {
      // Case 2: Response does not contain Widget.Response.Error.
      this.notifyObservers("requestSucceededWithoutError", this.responseContent)
    }
  },

  /**
   * Invoked on AJAX request failure.
   */
  ajaxRequestFailed: function(transport) {
    Widget.log("Client", "AJAX request failed")

    setTimeout(this.processAjaxRequestFailure.bind(this, transport), 0)
  },

  processAjaxRequestFailure: function(transport) {
    this.setPendingAJAXRequest(null)

    this.notifyObservers("requestFailed", "AJAX Request failed. HTTP Status: " + transport.status)
  },

  /**
   * Invoked on AJAX request exception.
   */
  ajaxRequestThrownException: function(e) {
    Widget.log("Client", "AJAX request exception")

    setTimeout(this.processAjaxRequestException.bind(this, e), 0)
  },

  processAjaxRequestException: function(e) {
    this.setPendingAJAXRequest(null)

    this.notifyObservers("requestFailed", "AJAX Request thrown exception: " + e)
  },

  /*
   * Creates and returns new HTML element, which will act
   * as a placeholder for response content.
   */
  createResponseArea: function() {
    var responseArea = document.createElement("div")

    var responseAreaElement = Element.extend(responseArea)

    responseAreaElement.setStyle({display: 'none'})

    responseAreaElement.setStyle({visibility: 'hidden'})

    document.body.appendChild(responseArea)

    return responseAreaElement
  },

  /*
   * Returns a HTML element, which acts as a placeholder
   * for response content.
   */
  getResponseArea: function() {
    if (!this.responseArea) {
      this.responseArea = this.createResponseArea()
    }

    return this.responseArea
  },

  setPendingAJAXRequest: function(request) {
    this.pendingAJAXRequest = request

    this.notifyObservers("isBusyChanged")
    this.notifyObservers("canInterruptRequestChanged")
  }
})

/**
 * Response User Error.
 */
Widget.Response.Error = new Class.define(
{
  /**
   * Initializes this error with message.
   */
  initialize: function(message) {
    // Because there's no keyword similar to Java 'instanceof',
    // this special token is used to distinguish between
    // Error and other types of responses.
    this._widgetResponseErrorToken = true

    this.message = message
  },

  /**
   * Returns error message
   */
  getMessage: function() {
    return this.message
  }
})
