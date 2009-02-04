/**
 * (c) Volantis Systems Ltd 2008. 
 */

Widget.Internal.Select = Class.define(Widget.Internal.Control, {

  internalValue: [],
  initialize: function(id, options) {
    this.initializeControl(id, options);
  },

  getValueFromControl: function() {
    if(this.multiple == false) {
      return this.getElement().value
    } else {
      this.value = $A();
      var options = this.getElement().options;
      for(var i = 0; i < options.length; i++) {
        this.value[i] = options[i].selected;
      }
      return this.value;
    }
  },

  // for single select type - value is string,
  // for multiple type - value is array of selected values
  setValue: function(value) {
    if(this.multiple == false) {
      this.getElement().value = value;
    } else {
      var options = this.getElement().options;
      for(var i = 0; i < options.length; i++) {
        if(value.indexOf(options[i].value) != -1) {
          options[i].selected = true;
        } else {
          options[i].selected = false;
        }
      }
    }
    this.checkValueChanged();
  },

  getValue: function() {
    if(this.multiple == false) {
      return this.value;
    } else {
      var array = $A();
      var options = this.getElement().options;
      for(var i = 0; i < this.value.length; i++) {
        if(this.value[i]) {
          array.push(options[i].value);
        }
      }
      return array;
    }
  },

  checkValueChanged: function() {
    if(this.multiple == false) {
      if (this.value != this.getElement().value) {
        this.value = this.getElement().value;
        this.valueChanged();
      }
    } else {
      var options = this.getElement().options;
      var changed = false;
      for(var i = 0; i < options.length; i++) {
        if(options[i].selected != this.value[i]) {
          changed = true;
          this.value[i] = options[i].selected;
        }
      }
      if(changed) {
        this.valueChanged();
      }
    }
  }
});


/**
 * An Select widget
 */
Widget.Select = Class.define(Widget.Control,
{
  initialize: function(id, options) {
    if($(id).getAttribute('multiple') == 'multiple') {
      this.initializeControl(new Widget.Internal.Select(id, options), "list", options)
    } else {
      this.initializeControl(new Widget.Internal.Select(id, options), "string", options)
    }
  }
});
