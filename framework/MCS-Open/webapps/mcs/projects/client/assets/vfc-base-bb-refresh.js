
/**
 * (c) Volantis Systems Ltd 2008. 
 */
/**
 * (c) Volantis Systems Ltd 2008. 
 */

/* Base class for refresh widget */
Widget.Refresh = Widget.define(
{
  src: null,
  interval: null,

  // parent widget reference
  owner: null,

  initializeRefresh: function(options) {
    this.initializeWidget(null, options)

    this.addAction("execute")

    this.addProperty("src")
    this.addProperty("interval")
  },

  setOwner: function(owner) {
    this.owner = owner;
  },

  getInterval: function() {
    return this.interval
  },

  _setInterval: function(interval) {
    this.transformation = interval
    this.notifyObservers("intervalChanged")
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


Widget.BlockRefresh = Class.define(Widget.Refresh, Widget.Refreshable, {

  initialize: function(options) {
    this.initializeRefresh(options);
  },

  doExecute: function() {
    //do nothing
  },

  processAJAXResponse: function(request) {
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
  createResponseArea: function() {
    var responseArea = document.createElement("div");
    var responseAreaElement = Element.extend(responseArea);
    responseAreaElement.setStyle({display: 'none'});
    responseAreaElement.setStyle({visibility: 'hidden'});
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
  }

});

