

/**
 * (c) Volantis Systems Ltd 2008. 
 */

/* Base class for load widget */
Widget.Load = Widget.define(
{
  src: null,
  when: null,
  owner: null,

  initialize: function(options) {
    this.initializeLoad(options)
  },

  initializeLoad: function(options) {
    this.initializeWidget(null, options)

    this.addAction("execute")

    this.addProperty("src")

    this.addEvent("succeeded")
    this.addEvent("failed")
  },

  setOwner: function(owner) {
    this.owner = owner
  },

  getSrc: function() {
    return this.src
  },

  setSrc: function(src) {
    if (this.src != src) {
      this.src = src

      this.notifyObservers("srcChanged")

      this.notifyObservers("canExecuteChanged")
    }
  },

  getWhen: function() {
    return this.when
  },

  execute: function() {
    if (this.canExecute()) {
      this.doExecute()
    }
  },

  doExecute: function() {
    throw "Method doExecute() is not implemented. Overwrite it in subclass.";
  },

  canExecute: function() {
    return this.src != null
  }
})


Widget.BlockLoad = Class.define(Widget.Load, {

  initialize: function(options) {
    this.initializeLoad(options);
  },

  doExecute: function() {
    this.createAjaxRequest(this.src);
  },

  createAjaxRequest : function(requestURL){
    //Widget.log("Widget.Load - createAjaxRequest",requestURL);
        new Widget.AjaxRequest(
            requestURL,
            { method: 'get',
            parameters: '',
            onSuccess: this.processAJAXResponse.bind(this),
            onFailure: this.processAJAXFailure.bind(this) }
        );
  },

  processAJAXFailure: function(request){
    this.notifyObservers("failed");
  },

  processAJAXResponse: function(request){

    // Get response text out of the request.
    var responseText = request.responseText;

    // Insert response content, stripping unnessecary scripts.
    this.getResponseArea().innerHTML = responseText.stripScripts();

    // Evaluate the JavaScript from the response content.
    eval(responseText.extractScripts()[0]);

    this.owner.setContent(this.blockContent);

    this.notifyObservers("succeeded");
  },

 /*
   * Creates and returns new HTML element, which will act
   * as a placeholder for response content.
   */
  createResponseArea: function(responseAreaId) {
    var responseArea = document.createElement("div");
    responseArea.id = responseAreaId;
    var responseAreaElement = Element.extend(responseArea);
    responseAreaElement.setStyle({display: 'none'});
    responseAreaElement.setStyle({visibility: 'hidden'});
    document.body.appendChild(responseArea);
    return responseArea;
  },

  /*
   * Returns a HTML element, which acts as a placeholder
   * for response content.
   */
  getResponseArea: function() {
    var responseAreaId = this.owner.getElement().id + '_ra';
    var el = $(responseAreaId);
    if (el == null) {
      return this.createResponseArea(responseAreaId);
    } else {
      return el;
    }

  }

});
