/**
 * (c) Volantis Systems Ltd 2008. 
 */

Widget.Internal.Input = Class.define(Widget.Internal.Control, {
  initialize: function(id, options) {
    this.initializeControl(id, options);

    // Check every 0.1 seconds, if the value has changed.
    // Send 'valueChanged' callback, if it did.
    // TODO: Maybe there's another way to check for incremental value updates.
    // TODO: If this solution remains, don't spawn timer if there are no obervers
    // on partial value change. This requires to extends obervers architecture.
    this.lastPartialValue = this.getPartialValue();

    setInterval(this.checkPartialValueChanged.bind(this), 100);

  },

  getPartialValue: function() {
    return this.getElement().value;
  },

  partialValueChanged: function() {
    this.lastPartialValue = this.getPartialValue()

    this.notifyObservers('partialValueChanged')
  },

  checkPartialValueChanged: function() {
    if (this.getPartialValue() != this.lastPartialValue) {
      this.partialValueChanged()
    }
  }
});

/**
 * An Input widget
 */
Widget.Input = Class.define(Widget.Control,
{
  initialize: function(id, options) {
    this.initializeControl(new Widget.Internal.Input(id, options), "string", options);

    this.observe(this.internalControl, "partialValueChanged", "partialValueChanged");
    this.addProperty("partial-value")
  },

  getPartialValue: function() {
    return this.internalControl.getPartialValue()
  },

  partialValueChanged: function() {
    this.notifyObservers("partialValueChanged")
  }

});
