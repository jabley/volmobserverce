/*
 * (c) Volantis Systems Ltd 2006.
 */

Widget.Wizard = Class.define(Widget.Widget,Widget.Appearable, Widget.Disappearable, {
  // store identifiers of wizard steps.
  stepsIdsArray : null,

initialize: function(id, options) {

  this.initializeWidget(id, options);

  this.id = id;
  this.biggestHeight  =0;
  this.formElement = null;
  this.stepArray = [];
  this.simpleValidatorIds = [];
  this.multipleValidatorIds = [];
  this.stepValidators = null;
  this.currentIndex = 0;
  this.isDisplayed = false;
  this.launcherId = null;
  this.popupInstance = null;
  this.wizardElement = null;
  this.clientWidth = window.document.body.clientWidth;
  this.clientHeight = window.document.body.clientHeight;
  this.cancelCallbackHandler = new Widget.CallbackHandler();

  // fields initialized, so we can register steps.
  var i;
  for(i = 0; i < this.stepsIdsArray.length; i++) {
     this.registerStep(i,this.stepsIdsArray[i]);
  }

  // add actions supported by this widget
  this.addAction('next');
  this.addAction('previous');
  this.addAction('cancel');
  this.addAction('complete');
  this.addAction('launch');

  this.addEvent('completed');

  this.registrationCompleted();
},

/**
  * Init style for Wizard.
  */
initStyles : function() {
  //vbm: 2006092629
  if(Prototype.operaMobile()) {
    var displayHeight;
    //set bigger height of wizard div to ensure content is hidden
    if (window.innerHeight > this.wizardElement.offsetHeight) {
      // wizard is smaller then client's pane scrolling can be disabled
      displayHeight = window.innerHeight;
      //  Element.setStyle(document.body, {'overflow':'hidden'});
    } else {
      // wizard is bigger then clients pane, scrolling must enabled
      displayHeight = this.wizardElement.offsetHeight;
      // Element.setStyle(document.body, {'overflow':'scroll'});
    }
    Element.setStyle(this.wizardElement, {
                                        'position': 'absolute',
                                        'height': displayHeight + 'px',
                                        'top': '0px',
                                        'left': '0px',
                                        'z-index': '2',
                                        'visibility': 'visible',
                                        'display': 'none'});
  } else {
    var displayWidth = document.documentElement.clientWidth || document.body.clientWidth ||  window.innerWidth;
    Element.setStyle(this.wizardElement, {'width': displayWidth + 'px',
                                        'height': '100%',
                                        'position': 'fixed',
                                        'z-index': '2',
                                        'top': '0px',
                                        'left': '0px',
                                        'visibility': 'visible',
                                        'overflow':'auto',
                                        'display': 'none'});
  }

  if (Prototype.msieBrowser()) {
    // '-1' is added because without it, horizontal and vertical scrollbars are displayed.
    this.wizardElement.style.width = document.documentElement.clientWidth - 1;
    this.wizardElement.style.height = document.documentElement.clientHeight - 1;
    this.wizardElement.style.position = 'absolute';
  }
},

/**
  * Hide Wizard.
  */
hide : function(){
  if(this.isDisplayed){
    this.isDisplayed = false;
    this.reset();
    Element.hide(this.wizardElement);
    Element.setStyle(document.body,{ 'overflow' : 'hidden'});
  }
},

/**
 * reset all values from form
 */
  reset : function(){
    this.formElement.reset();
  },

/**
 * Open poppup with message.
 */
cancel : function(){
  this.setButtonsEnabled(false);
  // here should be checking if popup should be displayed
  this.popupInstance.show();
},

/**
* Set all Wizard buttons enabled or dispabled
*/
setButtonsEnabled : function(display){
  // there is no buttons anymore it will be handled
  // by Widget.Button instead
  var inputs = $(this.id).getElementsByTagName('input');
  for (var i=0; i<inputs.length; i++){
    if(inputs[i].type == 'button' || inputs[i].type == 'submit'){
      inputs[i].disabled=!display;
    }
  }
},

/**
 * validate current step and progress to next if validation completed
 * sucessfully.
 */
next : function(){
  // Invoke the validation for current step.
  // Validation is asynchronous, so this method
  // will immediately return. The appropriate
  // callback will be called when validation
  // finishes, which will continue the wizard.
  this.validateStep(this.currentIndex);
},

/**
 * Go to previous step.
 */
previous : function(){
  this.displayStep(this.currentIndex-1);
},

/**
 * validate last step and do submit if all fields valid
 */
complete : function(){
  // Invoke the validation for current step.
  // Validation is asynchronous, so this method
  // will immediately return. The appropriate
  // callback will be called when validation
  // finishes, which will continue the wizard.
  this.validateStep(this.stepArray.length - 1);
},

/**
 * go direct to provided step
 *
 * @param stepIndex
 */
goToStep : function(stepIndex){
  if((stepIndex>=0) && (stepIndex<this.stepArray.length)){
    this.displayStep(stepIndex);
  }
},

/**
 * Simple validator registration - called for each field
 * with simple validator
 *
 * @param stepIndex
 * @param validatorId
 */
registerSimpleValidator : function(stepIndex, validatorId){
  if(!this.simpleValidatorIds[stepIndex]){
    this.simpleValidatorIds[stepIndex] = [];
  }
  this.simpleValidatorIds[stepIndex].push(validatorId);
},

/**
 * Multiple validator registration - called for each field
 * with multiple validator
 *
 * @param stepIndex
 * @param validatorId
 */
registerMultipleValidator : function(stepIndex, validatorId){
  if(this.multipleValidatorIds[stepIndex] === undefined){
    this.multipleValidatorIds[stepIndex] = new Array();
  }
  this.multipleValidatorIds[stepIndex].push(validatorId);
},

/**
 * register single step - called for each wizard step
 *
 * @param stepIndex
 * @param stepElement
 */
registerStep : function(stepIndex,stepElement){
  this.stepArray[stepIndex] = $(stepElement);
  Element.setStyle(this.stepArray[stepIndex],
    { 'display' : 'none', 'visibility': 'visible'});
},

/**
 * register Launcher element
 *
 * @param launchElement
 */
registerLauncher : function(launchElement){
  this.launcherId = launchElement;
},

/**
 * regitster Popup
 *
 * @param popupId
 */
registerPopup : function(popupId){
  this.popupInstance = Widget.getInstance(popupId);
  this.observe(this.popupInstance, "dismissed", "doCancel");
},

/**
 * Must be called when all fielst are registered, now wizard starts
 * initializations actions.
 */
registrationCompleted : function(){
  this.wizardElement = $(this.id);
  var formArray = $A(this.wizardElement.getElementsByTagName('form'));
  this.formElement = formArray[0];
  this.initStyles();
  if(this.launcherId) {
    this.addInputElement($(this.launcherId));
  }
  // wizard will not be visible at start
  // when there is no additional elements on page
  // wizard could be initialized only by javascript interface then
},

/*
 * Returns array with step validators
 */
getStepValidators: function() {
  if (this.stepValidators === null) {
    this.initializeStepValidators();
  }
  return this.stepValidators;
},

/*
 * Initializes step validators out from registered
 * simple and multiple validators.
 */
initializeStepValidators: function() {
  this.stepValidators = new Array();

  // For each wizard step, create instance of combined validator,
  // which will invoke all simple and then all multiple validators.
  for(var index = 0; index < this.stepArray.length; index++) {
    // Retrieve registered simple and multiple validators.
    var simpleValidatorIds = this.simpleValidatorIds[index];
    var multipleValidatorIds = this.multipleValidatorIds[index];
    var simpleValidator = null;
    var multipleValidator = null;

    // Build chained validator out of all registered simple validators.
    if (simpleValidatorIds) {
      var simpleValidators = [];

      // Convert array of validator IDs into array of validator instances.
      for (subIndex = 0; subIndex < simpleValidatorIds.length; subIndex++) {
        simpleValidators.push(Widget.getInstance(simpleValidatorIds[subIndex]));
      }

      // Create combined validator out of simple validators.
      simpleValidator = new Widget.ChainedValidator(
        simpleValidators, {failFast: false});
    }

    // Build chained validator out of all registered multiple validators.
    if (multipleValidatorIds) {
      var multipleValidators = [];

      // Convert array of validator IDs into array of validator instances.
      for (subIndex = 0; subIndex < multipleValidatorIds.length; subIndex++) {
        multipleValidators.push(Widget.getInstance(multipleValidatorIds[subIndex]));
      }

      // Create combined validator out of multiple validators.
      multipleValidator = new Widget.ChainedValidator(
        multipleValidators, {failFast: true});
    }

    // Build combined validator including simple and multiple validator.
    if (simpleValidator === null) {
      if (multipleValidator === null) {
        // Case 1: No simple and no multiple validator -> do nothing
        stepValidator = null;

      } else {
        // Case 2: Only multiple validator
        stepValidator = multipleValidator;
      }

    } else {
      if (multipleValidator === null) {
        // Case 3: Only simple validator
        stepValidator = simpleValidator;

      } else {
        // Case 4: Both simple & multiple validator -> combine them
        stepValidator = new Widget.ChainedValidator(
          new Array(simpleValidator, multipleValidator), {failFast: true});
      }
    }

    if (stepValidator !== null) {
      // Register callbacks
      stepValidator.addSuccessCallback(this.onValidatorSuccess.bind(this));
      stepValidator.addFailureCallback(this.onValidatorFailure.bind(this));
      stepValidator.addFallbackCallback(this.onValidatorFallback.bind(this));

      // Finally, register step validator.
      this.stepValidators[index] = stepValidator;
    }
  }
},

/**
 * check result from cancel dialog
 * If OK pressed hide wizard and clear all data
 *
 * @param message
 */
doCancel : function(message){
  if(message == "yes"){
    this.hide();
    this.cancelCallbackHandler.invoke();
    Element.setStyle(this.stepArray[this.currentIndex],
      { 'display' : 'none', 'visibility': 'visible'});
    Element.setStyle(document.body, {'overflow':'auto'});
  }else{
    this.setButtonsEnabled(true);
  }
},

/**
 * Adds callback invoked when the wizard is cancelled.
 *
 * @param callback The callback function
 *
 * @volantis-api-include-in PublicAPI
 */
addCancelCallback: function(callback) {
  this.cancelCallbackHandler.add(callback);
},

/**
 * set current index with specified value
 *
 * @param newIndex
 */
setCurrentIndex : function (newIndex){
  this.currentIndex = newIndex;
},


/**
 * show step specified by current index
 */
showCurrentStep : function() {
  this.currentStep = this.stepArray[this.currentIndex];
  var it = this;
  this.setButtonsEnabled(false);
  Widget.TransitionFactory.createAppearEffect(this.currentStep, this,{
      afterFinish: function(effect){
        it.setButtonsEnabled(true);
      }
  });
},

/**
 * hide step specified by current index
 */
hideCurrentStep : function() {
  this.currentStep = this.stepArray[this.currentIndex];
  this.setButtonsEnabled(false);
  var it = this;
  Widget.TransitionFactory.createDisappearEffect(this.currentStep, this, {
    afterFinish: function(effect){
      it.setButtonsEnabled(true);
    }
   });
},

/**
 * hide current step, display step specified by @stepIndex
 *  @param stepIndex - index of step to display
 */
displayStep : function(stepIndex) {
  this.currentStep = this.stepArray[this.currentIndex];
  this.nextStep = this.stepArray[stepIndex];
  this.setButtonsEnabled(false);
  var n = this.nextStep;
  var it = this;
  //first hide current step
  Widget.TransitionFactory.createDisappearEffect(this.currentStep, this, {
    //after effect is finished, show next step
    afterFinish: function(effect){
        Widget.TransitionFactory.createAppearEffect(n, it,{
        //after finish of both effects, enable buttons
          afterFinish: function(effect){
            it.setButtonsEnabled(true);
            it.prepareScrolling(n);
          }
        });
      }
    });
  this.setCurrentIndex(stepIndex);
},

/**
* vbm: 2006092629, if wizard steps height is grater then cleints view pane, scrolling must be enabled
* it causes that underlaying content may be visible but it is the best sollution for now
*/
prepareScrolling : function(step){
  if(Prototype.operaMobile()) {
    if (window.innerHeight > step.offsetHeight) {
      //wizard is smaller then client's pane - scrolling can be disabled
      Element.setStyle(document.body, {'overflow':'hidden'});
    } else {
      //wizard is bigger then clients pane, scrolling must be enabled
      Element.setStyle(document.body, {'overflow':'scroll'});
    }
  }
},


/**
  * Validate step with specified index.
  * When validation finishes, one of the following
  * callbacks will be invoked, according to the
  * validation result:
  *  - onValidatorSuccess()
  *  - onValidatorFailure()
  *  - onValidatorFallback()
  *
  * @param index of step to be validate
  */
validateStep : function(stepIndex){
  var stepValidators = this.getStepValidators();
  var stepValidator = stepValidators[stepIndex];

  if (!stepValidator) {
    // Case 1: No validator registered for current step,
    // so invoke the success callback immediately, as if there
    // was a validator.
    this.onValidatorSuccess();

  } else {
    // Case 2: There is a validator registered for current step,
    // so perform the validation. After validation is completed,
    // the callback method will be invoked, which will continue
    // the wizard.
    stepValidator.validate();
  }
},

/*
 * Invoked, when user pressed 'next', and validation
 * has already been performed.
 */
nextAfterValidation: function() {
  this.displayStep(this.currentIndex+1);
},

/*
 * Invoked, when user pressed 'complete', and validation
 * has already been performed.
 */
completeAfterValidation: function() {
  //  this.hideCurrentStep();
  this.currentStep = this.stepArray[this.currentIndex];
  this.setButtonsEnabled(false);
  var it = this;
  //submit form after effect is finished
  Widget.TransitionFactory.createDisappearEffect(this.currentStep, this, {
    afterFinish: function(effect){
      it.doSubmit();
    }
   });
},

/**
 * Invoke submit on form and notify observers that form is completed
 */
doSubmit : function(){
  this.formElement.submit();
  this.notifyObservers("completed");
},


/*
 * Invoked, when user pressed 'next' or 'complete',
 * and validation has already been performed.
 */
nextOrCompleteAfterValidation: function() {
  if (this.currentIndex != this.stepArray.length - 1) {
    // Case 1: Current step is not the final step,
    // so continue to the next step.
    this.nextAfterValidation();

  } else {
    // Case 2: Current step is final step,
    // so complete the wizard.
    this.completeAfterValidation();
  }
},

/*
 * Invoked when validation for current step passed.
 */
onValidatorSuccess: function() {
  // Continue to the next step or complete the wizard.
  this.nextOrCompleteAfterValidation();
},

/*
 * Invoked when validation for current step fails.
 */
onValidatorFailure: function() {
  // Do nothing there
},

/*
 * Invoked when validation for current step could not be performed.
 */
onValidatorFallback: function() {
  // Continue to the next step or complete the wizard.
  // TODO: The question, what to do if the validation could not
  // be performed, is still open. Should we continue to the
  // next step, or should we NOT? The reason that we chose to continue
  // to the next step, is because such a behaviour is similar to how the
  // fallback for wizard works => no validation is performed at all.
  this.nextOrCompleteAfterValidation();
},

/*
* Add small focusable element to div element el
*/
  addInputElement: function(el) {
    var imp = document.createElement("input");
    imp.type="button";
    imp.style.width='1px';
    imp.style.height='1px';
    imp.style.backgroundColor='transparent';
    imp.style.border='0px';
    el.appendChild(imp);
  },

  /**
  * Causes the wizard to be displayed. If the wizard is already displayed it has no effect.
  *
  * @volantis-api-include-in PublicAPI
  */
  launch : function(){
    if(!this.isDisplayed){
      this.isDisplayed = true;
      this.prepareScrolling(this.wizardElement);
      Element.show(this.wizardElement);
      this.setCurrentIndex(0);
      this.showCurrentStep();
    }
  },

  /**
  * For backward compatibility only.
  * Because launch is responsible for initiating widget
  */
  show : function(){
    this.launch();
  }
});
