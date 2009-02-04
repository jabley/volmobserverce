/**
 * (c) Volantis Systems Ltd 2008. 
 */

Widget.Internal.Control = Class.define(Widget.OptionsContainer, Widget.Observable,
{
  id: null,

  initializeControl: function(id, options) {
    this.installOptions(options);

    this.id = id;

    var element = this.getElement();

    this.value = this.getValueFromControl();

    Widget.addElementObserver(element, Widget.CHANGE, this.controlValueChanged.bindAsEventListener(this));
  },

  getValueFromControl: function() {
      return this.getElement().value
  },

  getValue: function() {
      return this.value;
  },

  setValue: function(string) {
    this.getElement().value = string;
    this.checkValueChanged();
  },

  checkValueChanged: function() {
    if (this.value != this.getElement().value) {
      this.value = this.getElement().value;
      this.valueChanged();
    }
  },

  valueChanged: function() {
    this.notifyObservers('valueChanged')
  },

  controlValueChanged: function() {
    this.checkValueChanged()
  },

  getElement: function() {
    return $(this.id)
    }
});


Widget.Control = Widget.define(
{
  property: null,

  initializeControl: function(internalControl, valueType, options) {
    this.initializeWidget(null, options)

    this.internalControl = internalControl

    // Those fields are set to true while changing property value
    // and/or activating, to prevent callback recursion.
    this.isUpdatingFromProperty = false

    if (this.property != null) {
      this.observe(this.property, "valueChanged", "propertyValueChanged")

      this.updateFromProperty()
    }

    this.observe(this.internalControl, "valueChanged", "internalControlValueChanged")
    this.addProperty("value", {type: valueType})
  },

  getValue: function() {
    return this.internalControl.getValue()
  },

  setValue: function(string) {
    this.internalControl.setValue(string)
  },

  internalControlValueChanged: function() {
    if (this.property != null && !this.isUpdatingFromProperty) {
      this.updateProperty()
    }

    this.valueChanged()
  },

  propertyValueChanged: function() {
    this.updateFromProperty()
  },

  updateFromProperty: function() {
    this.isUpdatingFromProperty = true

    this.setValue(this.property.getValue())

    this.isUpdatingFromProperty = false
  },

  updateProperty: function() {
    this.property.setValue(this.getValue())
  },

  valueChanged: function() {
    this.notifyObservers("valueChanged")
  }
});
