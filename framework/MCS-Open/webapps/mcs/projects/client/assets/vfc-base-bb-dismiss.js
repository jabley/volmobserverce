/**
 * (c) Volantis Systems Ltd 2008. 
 */


/**
 * A dismiss widget.
 */
Widget.Dismiss = Widget.define({

  initialize: function(id, type, button, dismissable) {
    this.initializeWidget(id);
    this.type = type;
    this.button = button;
    this.action = dismissable.getAction("dismiss");

    this.observe(button, "pressed", "_buttonPressed");
    if (this.action != null) {
      this.observe(this.action, "isEnabledChanged", "_updateButtonWithAction")
      // Set button's initial state
      this._updateButtonWithAction()
    }
  },

  _buttonPressed: function() {
    if (this.action != null) {
      this.action.invoke(this.type);
    }
  },

  _updateButtonWithAction: function() {
    this.button.setEnabled(this.action.isEnabled());
  }
});