


/**
 * (c) Volantis Systems Ltd 2008. 
 */


/* Base class for fetch widget */
Widget.Fetch = Widget.define(
{
  src: null,
  when: null,
  transformation: null,
  //localization of installed MCS
  pageBase: null,
  // path to fetch service with dafault value
  service: "/services/fetch",
  transformCompile: null,
  transformCache: null,

  owner: null,

  initializeFetch: function(options) {
    this.initializeWidget(null, options)

    this.addAction("execute")

    this.addProperty("src")
    this.addProperty("transformation")

    this.addEvent("succeeded")
    this.addEvent("failed")
  },

  setOwner: function(owner) {
    this.owner = owner;
  },

  isTransformCompiled: function() {
    return this.transformCompile
  },

  isTransformCached: function() {
    return this.transformCache
  },

  getTransformation: function() {
    return this.transformation
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


Widget.BlockFetch = Class.define(Widget.Fetch, {

    //serviceLocation: "/services/fetch",

  initialize: function(options) {
    this.initializeFetch(options);

    this.load = new Widget.BlockLoad({when: this.when, src: this.prepareRequest()});

    this.observe(this.load, "succeeded", "loadSucceeded");
    this.observe(this.load, "failed", "loadFailed");
  },

  doExecute: function() {

    this.load.setOwner(this.owner);
    this.load.setSrc(this.prepareRequest());
    this.load.execute();
  },

  prepareRequest: function(){
    var urlRequest = this.pageBase + this.service + '?src=' + escape(this.src);
    if(this.transformation !== null) {
      urlRequest += '&trans=' + escape(this.transformation);
    }
    return urlRequest;
  },

  loadSucceeded: function() {
    this.notifyObservers("succeeded");
  },

  loadFailed: function() {
    this.notifyObservers("failed");
  }
});
