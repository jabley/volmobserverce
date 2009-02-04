
/**
 * (c) Volantis Systems Ltd 2008. 
 */

/**
 * An Effect Block widget.
 */
Widget.Internal.EffectBlock = Widget.define(Widget.Appearable, Widget.Disappearable,
{
  appearable: null,
  disappearable: null,

  initialize: function(id, options) {
    this.initializeWidget(id, options);

    this.status = (this.getElement().getStyle("display") == "none") ? "hidden" : "shown";

    this.duringTransition = false;

    // This flag indicates, whether hide or show was requested by the user.
    // It does not represent the actual state of the widget.
    this.hidden = (this.status == "hidden");

    // Indicate, that the default style for the element is "block".
    // This is used by Element.show() method in "prototype.js" library
    // to change the display:none style to display:block.
    this.getElement().vfcDefaultStyle = 'block';
  },

  /**
   * Shows the content, if it's not already shown.
   */
  show: function() {
    this.setHidden(false);
  },

  /**
   * Hides the content, if not already hidden.
   */
  hide: function() {
    this.setHidden(true);
  },

  canShow: function() {
    return this.hidden;
  },

  canHide: function() {
    return !this.hidden;
  },

  getStatus: function() {
    return this.status;
  },

  _setStatus: function(status) {
    if (this.status != status) {
      this.status = status;

      // Notify observers of status change
      this.notifyObservers("statusChanged");

      // Send an event named as the status:
      // "hiding", "hidden", "showing" or "shown".
      this.notifyObservers(status);
    }
  },

  isHidden: function() {
    return this.hidden;
  },

  setHidden: function(hidden) {
    // Don't know why the timer helps here, but without it some effects just fails.
    // Maybe it's due to some asynchronous aspects of the effects.
    // TODO: This is a quick-fix. I believe that this bug needs to be fixed in effects code,
    // which invokes afterFinish notifications.
    setTimeout(this.doSetHidden.bind(this, hidden), 0);
  },

  doSetHidden: function(hidden) {
    if (this.hidden != hidden) {
      this.hidden = hidden;

      if (!this.duringTransition) {
        if (this.hidden) {
          this.startDisappear();
        } else {
          this.startAppear();
        }
      }

      // Notify observers.
      this.notifyObservers('canHideChanged');
      this.notifyObservers('canShowChanged');
      this.notifyObservers('isHiddenChanged');
    }
  },

  /**
   * Starts the disappear transition.
   */
  startDisappear: function() {
    //Widget.log("Widget.Block", "Starting disappear...")

    this._setStatus("hiding");

    // Update the transition flag.
    this.duringTransition = true;

    // Run disappear transition.
    Widget.TransitionFactory.createDisappearEffect(
      this.getElement(),
      this.getDisappearable(),
      { element : this.getElement(),
        afterFinish: this.afterDisappear.bind(this)});
  },

  /**
   * Invoked after the disappear transition finishes.
   */
  afterDisappear: function() {
    //Widget.log("Widget.Block", "Disappear finished...")


    // Update the transition flag.
    this.duringTransition = false;

    // Send the 'disappeared' notification.
    this._setStatus("hidden");

    this.checkForAppear();
  },

  checkForAppear: function() {
    if (!this.hidden) {
      this.startAppear();
    }
  },

  /**
   * Start the appear transition.
   */
  startAppear: function() {
    //Widget.log("Widget.Block", "Starting appear...")

    // Update the transition flag.
    this.duringTransition = true;

    this._setStatus("showing");

    // Run appear transition
    Widget.TransitionFactory.createAppearEffect(
      this.getElement(),
      this.getAppearable(),
      { element : this.getElement(),
        afterFinish: this.afterAppear.bind(this)}
    );
  },

  /**
   * Invoked after the appear transition finishes.
   */
  afterAppear: function() {
    //Widget.log("Widget.Block", "Appear finished...")

    // Update the transition flag.
    this.duringTransition = false;

    // Sent 'appeared' notification.
    this._setStatus("shown");

    // If in the meantime this widget was requested to be hidden
    // (hidden flag is true), start the disappear effect now.
      this.checkForDisappear();
  },

  checkForDisappear: function() {
    if (this.hidden) {
      this.startDisappear();
    }
  },

  /**
   * Returns true, if this block is currently during transition.
   */
  isDuringTransition: function() {
    return this.duringTransition;
  },

  getAppearable: function() {
    return (this.appearable === null) ? this : this.appearable;
  },

  getDisappearable: function() {
    return (this.disappearable === null) ? this : this.disappearable;
  }
});
