

/**
 * (c) Volantis Systems Ltd 2008. 
 */

Widget.BlockContent = Widget.define(Widget.Appearable, Widget.Disappearable,
{
  initialize: function(internalContent, options) {
    this.initializeWidget(null, options);

    this.internalContent = internalContent;

    var parentBlock = this.getOption("parentBlock");

    if (parentBlock !== undefined) {
      parentBlock.content = this;
      parentBlock.displayedContent = this;
      parentBlock.innerBlock.appearable = this;
      parentBlock.innerBlock.disappearable = this;
    }
  },

  getInternalContent: function() {
    return this.internalContent;
  }
});

Widget.Block = Widget.define(
{
  load: null,
  fetch: null,
  refresh: null,

  initialize: function(outerEffectBlock, innerEffectBlock, blockContainer, options) {
    this.initializeWidget(null, options);

    if(this.refresh !== null) {
      this.refresh.setOwner(this);
      this.refresh.startRefresh();
    }

    if(this.load !== null) {
      this.load.setOwner(this);
      if(this.load.getWhen() == 'onload') {
        this.load.execute();
      }
    }

    if(this.fetch !== null) {
      this.fetch.setOwner(this);
      if(this.fetch.getWhen() == 'onload') {
        this.fetch.execute();
      }
    }

    this.outerBlock = outerEffectBlock;
    this.innerBlock = innerEffectBlock;
    this.container = blockContainer;
    this.displayedContent = this.content;

    this.contentHidden = false;

    this.observe(this.outerBlock, "canShowChanged", "canShowChanged");
    this.observe(this.outerBlock, "canHideChanged", "canHideChanged");

    this.observe(this.outerBlock, "hiding", "hiding");
    this.observe(this.outerBlock, "hidden", "hidden");
    this.observe(this.outerBlock, "showing", "showing");
    this.observe(this.outerBlock, "shown", "shown");

    this.observe(this.outerBlock, "statusChanged", "statusChanged");

    this.observe(this.innerBlock, "hiding", "innerBlockHiding");
    this.observe(this.innerBlock, "hidden", "innerBlockHidden");
    this.observe(this.innerBlock, "showing", "innerBlockShowing");
    this.observe(this.innerBlock, "shown", "innerBlockShown");

    this.observe(this.innerBlock, "statusChanged", "contentStatusChanged");

    // Initialize initial content
    if (this.getOption("content") !== null) {
      this.setContent(this.getOption("content"));
    }

    // Define actions, properties and events
    // Part 1: Showing/hiding
    this.addAction("show");
    this.addAction("hide");
    this.addEvent("hiding");
    this.addEvent("hidden");
    this.addEvent("showing");
    this.addEvent("shown");
    this.addProperty("status");

    // Part 2: Content setting
    this.addAction("clear-content");
    this.addAction("show-content");
    this.addAction("hide-content");

    this.addProperty("content", {type: "widget"});
    this.addProperty("displayed-content", {type: "widget"});

    this.addEvent("content-hiding");
    this.addEvent("content-hidden");
    this.addEvent("content-showing");
    this.addEvent("content-shown");
    this.addProperty("content-status");

    this.addProperty("load", {type: "widget"});
    this.addProperty("fetch", {type: "widget"});
    this.addProperty("refresh", {type: "widget"});
  },

  getElement: function() {
    return this.outerBlock.getElement();
  },

  show: function() {
    this.outerBlock.show();
  },

  canShow: function() {
    return this.outerBlock.canShow();
  },

  canShowChanged: function() {
    this.notifyObservers("canShowChanged");
  },

  hide: function() {
    this.outerBlock.hide();
  },

  canHide: function() {
    return this.outerBlock.canHide();
  },

  canHideChanged: function() {
    this.notifyObservers("canHideChanged");
  },

  hiding: function() {
    this.notifyObservers("hiding");
  },

  hidden: function() {
    this.notifyObservers("hidden");
  },

  showing: function() {
    this.notifyObservers("showing");
  },

  shown: function() {
    this.notifyObservers("shown");
  },

  getStatus: function() {
    return this.outerBlock.getStatus();
  },

  statusChanged: function() {
    this.notifyObservers("statusChanged");
  },

  setContent: function(content) {
    if (this.content != content) {
      this.content = content;

      if (this.innerBlock.getStatus() == "shown") {
        // If the inner block is currently shown,
        // hide it right now.
            this.updateInnerBlock();

      } else if (this.innerBlock.getStatus() == "hidden") {
        // If the inner block is currently hidden,
        // and displayed content is not null,
        // show it right now.
        this.updateContainer();

        this.checkForInnerBlockShow();
      }

      this.notifyObservers("contentChanged");
    }
  },

  getContent: function() {
    return this.content;
  },

  clearContent: function() {
    this.setContent(null);
  },

  hideContent: function() {
    if (!this.contentHidden) {
      this.contentHidden = true;

      this.innerBlock.hide();

      this.canHideContentChanged();
      this.canShowContentChanged();
    }
  },

  canHideContent: function() {
    return !this.contentHidden;
  },

  canHideContentChanged: function() {
    this.notifyObservers("canHideContentChanged");
  },

  showContent: function() {
    if (this.contentHidden) {
      this.contentHidden = false;

      this.checkForInnerBlockShow();

      this.canHideContentChanged();
      this.canShowContentChanged();
    }
  },

  canShowContent: function() {
    return this.contentHidden;
  },

  canShowContentChanged: function() {
    this.notifyObservers("canShowContentChanged");
  },

  getDisplayedContent: function() {
    return this.displayedContent;
  },

  getContentStatus: function() {
    if (this.displayedContent === null) {
      return "none";
    } else {
      return this.innerBlock.getStatus();
    }
  },

  contentStatusChanged: function() {
    this.notifyObservers("contentStatusChanged");
  },

  updateDisplayedContent: function() {
    if (this.displayedContent !== this.content) {
      this.displayedContent = this.content;

      this.notifyObservers("displayedContentChanged");
      this.notifyObservers("contentStatusChanged");
      this.notifyObservers("canShowContentChanged");
      this.notifyObservers("canHideContentChanged");
    }
  },

  updateInnerBlock: function() {
    if (this.content !== this.displayedContent) {
      this.innerBlock.hide();
    }
  },

  innerBlockHiding: function() {
    if (this.displayedContent !== null) {
      this.notifyObservers("contentHiding");
      this.notifyObservers("contentStatusChanged");
    }
  },

  innerBlockHidden: function() {
    if (this.displayedContent !== null) {
      this.notifyObservers("contentHidden");
      this.notifyObservers("contentStatusChanged");
    }

    this.updateContainer();

    this.checkForInnerBlockShow();
  },

  innerBlockShowing: function() {
    if (this.displayedContent !== null) {
      this.notifyObservers("contentShowing");
      this.notifyObservers("contentStatusChanged");
    }
  },

  innerBlockShown: function() {
    if (this.displayedContent !== null) {
      this.notifyObservers("contentShown");
      this.notifyObservers("contentStatusChanged");
    }

    this.updateInnerBlock();
  },

  updateContainer: function() {
    if (this.content === null) {
      this.container.setContent(null);
    } else {
      this.container.setContent(this.content.getInternalContent());
    }

    this.updateDisplayedContent();
  },

  checkForInnerBlockShow: function() {
    if (!this.contentHidden && this.displayedContent !== null) {
      this.innerBlock.appearable = this.displayedContent;
      this.innerBlock.disappearable = this.displayedContent;

      this.innerBlock.show();
    }
  },

  getFetch: function() {
    return this.fetch;
  },

  getLoad: function() {
    return this.load;
  },

  getRefresh: function() {
    return this.refresh;
  }
});


