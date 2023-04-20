/* http://www.faustinelli.org/
   Select Xor for jQuery - release 0.1
   Dual licensed under the GPL (http://dev.jquery.com/browser/trunk/jquery/GPL-LICENSE.txt) and 
   MIT (http://dev.jquery.com/browser/trunk/jquery/MIT-LICENSE.txt) licenses. 
   Plugin framework derived from http://www.manning.com/wood/
   Please attribute the author if you use this plugin. */

(function($) { // Hide scope, no $ conflict
    var SelectX, getters, isNotChained, plugin
    ;

    /* SelectX */
    SelectX = function() {
        this._defaults = {
            /* if true, selected options disappear from the list
		     and reappear when related selection widget is destroyed */
            selectOnce: true,
            /* jQuery selector of the container where the plugin
		     creates a widget for each selection made */
            selectionsContainer: null,
            /* flag for template engine usage - only jtemplates allowed so far */
            templateEngine: false,
            /* sample dummy widget for displaying the selections made */
            templateHtml: String()
            + '<div class="selectx-option-widget">'
            + '	<input type="hidden" class="selectx-option-hidden-value" value="">'
            + '	<label class="selectx-option-value" style="width:200px;min-height:18px;display:inline-block;"></label>'
            + '	<button class="selectx-delete-widget" title="click to delete widget">delete</button>'
            + '</div>',
            /* data for template engine processing of the option widget;
			used only if templateEngine is true 
			may be a JSON or a JSON-returning function 
			properties of the returned object must match parameters in the template */
            optionWidgetDataModel: {
                value:'dummy label'
            },
            /* 	sample dummy data for the options
			used only if the plugin target has no options on its own 
			in that case, it is to be overridden by the user */
            optionsList: [
            {
                text:'1st option',
                value:1,
                data:null
            },

            {
                text:'2nd option',
                value:2,
                data:null
            },

            {
                text:'3rd option',
                value:3,
                data:null
            },
            ],
            /* first option for the select (does not have value) */
            nullOptionMessage: 'pick one',
            /* 	if overridden, it runs at the end of the _changeSelect event handler
			in the context of the $optionsWidget */
            onSelectCallback : null,
            /* 	if overridden, it runs at the end of the $widgetDeleter click handler
			in the context of the $optionsWidget */
            onUnselectCallback : null,
            /*  map with pre-set values for template parameters
		      must be overridden by the user, e.g. {max_qty:15}  */
            constrainedValues: null
        };
    }

    $.extend(SelectX.prototype, {
        /* Class name added to elements to indicate already configured with max length. */
        markerClassName: 'hasSelectX',
        /* Name of the data property for instance settings. */
        pluginName: 'selectx',
        /* marks the selections container */
        selectionsClassName: 'selectx-selections-container',
        /*marks the already selected options */
        selectedOptionClass: 'selectx-selected-option',
        /*marks each option widget built in the DOM by the plugin */
        optionWidgetClass: 'selectx-option-widget',
        /*marks the value label in each option widget */
        optionValueClass: 'selectx-option-value',
        /*marks the hidden input carrying the corresponding option value in each option widget */
        optionHiddenValueClass: 'selectx-option-hidden-value',
        /*marks the 'delete widget' item in each option widget */
        deleteWidgetClass: 'selectx-delete-widget',
        /*marks each option widget container built in the DOM by the plugin
		a option widget container contains a single option widget and it is
		necessary to give the templating engine an empty div to fill 
	*/
        optionWidgetContainerClass: 'selectx-option-widget-container',

        /* Override the default settings for all SelectXinstances in the current page.
	   @param  options  (object) the new settings to use as defaults
	   @return  (SelectX) this object */
        setDefaults: function(options) {
            $.extend(this._defaults, options || {});
            return this;
        },

        /* Attach the plugin to an existing select, adding all functionalities that do not depend on option values.
	   @param  $target   (element) the control to affect - IT MUST BE AN HTML SELECT!!!
	   @param  options  (object) the custom options for this instance */
        _attachPlugin: function($target, options) {
            var inst
            ;
            $target = $($target);  // better safe than sorry!
            if ($target.hasClass(this.markerClassName) /* || target is not a select */) {
                return;
            }
            inst = {
                options: $.extend({}, this._defaults), // this = SelectX object (am I right?!?!)
                selectionsContainer: $([])
            };

            //$("#tree li:parent").unbind("collapse.TreeEvent"); // just remove the collapse event
            //$("#tree li:parent").unbind(".TreeEvent"); // remove all events under the TreeEvent namespace
		
            $target.addClass(this.markerClassName).
            data(this.pluginName, inst).
            bind('change.' + this.pluginName, function() {
                plugin._changeSelect($(this),inst.options); // attach main handler
            });
            this._optionPlugin($target, options);
        },

        /* Retrieve or reconfigure the settings for a control.
	   @param  $target   (element) the control to affect - IT MUST BE AN HTML SELECT!!!
	   @param  options  (object) the new options for this instance or
	                    (string) an individual property name
	   @param  value    (any) the individual property value (omit if options
	                    is an object or to retrieve the value of a setting)
	   @return  (any) if retrieving a value */
        _optionPlugin: function($target, options, value) {
            var inst = $target.data(this.pluginName),
            that = this
            ;
		
            /* start options preprocessing */
            if (!options || (typeof options == 'string' && value == null)) { // Get option
                var name = options;
                options = (inst || {}).options;
                return (options && name ? options[name] : options);
            }

            if (!$target.hasClass(this.markerClassName)) {
                return;
            }
            options = options || {};
            if (typeof options == 'string') {
                var name = options;
                options = {};
                options[name] = value;
            }
            $.extend(inst.options, options); // user options are mixed with previous instance options
            /* end options preprocessing */
		
            /* start true operations of this method */
            // 1) construction selections container
            if ($.isFunction(inst.options.selectionsContainer)) {
                inst.selectionsContainer = inst.options.selectionsContainer.apply(target[0], []);
            }
            else if (inst.options.selectionsContainer) {
                inst.selectionsContainer = $(inst.options.selectionsContainer);
            }
            else {
                inst.selectionsContainer = $('<div/>').insertAfter($target);
            }
            inst.selectionsContainer.addClass(this.selectionsClassName);

            // 2) -optional- construction of select options
            if ($('option',$target).length===0){
                $target.append($('<option/>',{
                    text:inst.options.nullOptionMessage,
                    value:''
                }));
                _(inst.options.optionsList).each(function(optObj){
                    var $option = $('<option/>',optObj);
                    // append given data to the option
                    if (optObj && optObj.data) {  // workaround to IE/underscore.js quirk
                        $option.data(that.pluginName+'-optiondata',optObj.data);
                    }
                    $target.append($option);
                });
            }
        },

        /* STILL TO BE IMPLEMENTED -
	   Remove the plugin functionality from a control.
	   @param  target  (element) the control to affect */
        _destroyPlugin: function($target) {
            var inst
            ;
            $target = $($target);
            if (!$target.hasClass(this.markerClassName)) {
                return;
            }
            inst = $target.data(this.pluginName);
            // TODO - remove all stuff: selectionsContainer and whatever
		
            // TODO - clean up options (if not added by the plugin)

            // clean up target
            $target.removeClass(this.markerClassName).  // plus any further class
            removeData(this.pluginName).
            unbind('.' + this.pluginName);  // all plugin event handlers
        },
	
        /* Manages all activities following a user choice on the $target select
		'this' is the SelectX object
		@param $target is the select the plugin is attached to
		@param options is the whole set of instance options
	*/
        _changeSelect: function($target,options) {
            var $optionsWidgetContainer, $optionsWidget,
            $widgetDeleter,
            $chosenOption = $(':selected',$target),
            inst = $target.data(this.pluginName),
            widgetModelData,
            that = this
            ;
            // create options widget inside a brand new empty options widget container
            $optionsWidgetContainer = $('<div/>').addClass(this.optionWidgetContainerClass);
            if (!options.templateEngine){  // vanilla process - no templating engine
                $optionsWidget = $(options.templateHtml)
                inst.selectionsContainer.append($optionsWidgetContainer.append($optionsWidget));
                $('.'+this.optionValueClass,$optionsWidget).text($chosenOption.text());
                $('.'+this.optionHiddenValueClass,$optionsWidget).val($chosenOption.val());
            // TODO - insert here the coupling of data
            }
            else { // jtemplates stuff here - model comes from optionWidgetDataModel()
                if ($.isFunction(inst.options.optionWidgetDataModel)) {
                    /*	if it is a function, it is meant to process the data attached to the $chosenOption */
                    widgetModelData = inst.options.optionWidgetDataModel($chosenOption.data(that.pluginName+'-optiondata'));
                }
                else {
                    /* otherwise it must be ready to provide data to the template */
                    widgetModelData = inst.options.optionWidgetDataModel;
                }
                $optionsWidgetContainer.setTemplate(options.templateHtml)
                .processTemplate(widgetModelData);
                // only now I can instantiate $optionsWidget
                $optionsWidget = $('.'+this.optionWidgetClass,$optionsWidgetContainer);
                inst.selectionsContainer.append($optionsWidgetContainer.append($optionsWidget));
            }
		
            /* implement widget deleter - it receives a reference to the chosen option
		     to be able to make it visible once clicked */
            $widgetDeleter = $('.'+this.deleteWidgetClass,$optionsWidget);
            $widgetDeleter.data(this.pluginName+'-option',$chosenOption);  // attach option to $deleter
            $widgetDeleter.click(function(event){
                var opt, dummySpan
                ;
                /* NB - here 'this' = $widgetDeleter[0] !!!
                   gotta use 'that' to refer to the plugin instance */
                // -optional- implement selectOnce
                if (options.selectOnce) {

                    // cross-browser hack (cfr. http://ajax911.com/hide-options-selecbox-jquery/)
                    opt = $(this).data(that.pluginName+'-option');
                    if (opt.parent('span').length > 0) {
                        dummySpan = opt.parent('span');
                        dummySpan.replaceWith(opt);
                    }
                }
                // execute custom callback - this = $optionsWidget
                if (options.onUnselectCallback) {
                    options.onUnselectCallback.apply($optionsWidget);
                }
                $optionsWidgetContainer.remove();
            });
		
            // -optional- attach option data also to the optionwidget, if present
            if ($chosenOption.data(that.pluginName+'-optiondata')) {
                $optionsWidget.data(that.pluginName+'-optiondata',
                    $chosenOption.data(that.pluginName+'-optiondata'));
            }
		
            // -optional- implement selectOnce
            if (options.selectOnce) {
                // find and mark selected option - it becomes invisible - THIS WONT WORK IN IE!!!
                //$chosenOption.addClass(this.selectedOptionClass);
			
                // cross-browser hack (cfr. http://ajax911.com/hide-options-selecbox-jquery/)
                $chosenOption.wrap('<span>');
            }
		
            // execute custom callback - this = $optionsWidget
            if (options.onSelectCallback) {
                options.onSelectCallback.apply($optionsWidget);
            }
            $target.val('');  // reset select to 'please choose item'
        }
	
    });

    // The list of methods that return values and don't permit chaining
    getters = [''];

    /* Determine whether a method is a getter and doesn't permit chaining.
   @param  method     (string, optional) the method to run
   @param  otherArgs  ([], optional) any other arguments for the method
   @return  true if the method is a getter, false if not */
    isNotChained = function(method, otherArgs) {
        if (method == 'option' && (otherArgs.length == 0 ||
            (otherArgs.length == 1 && typeof otherArgs[0] == 'string'))) {
            return true;
        }
        return $.inArray(method, getters) > -1;
    }

    /* Attach the selectx functionality to a jQuery selection.
   @param  options  (object) the new settings to use for these instances (optional) or
                    (string) the method to run (optional)
   @return  (jQuery) for chaining further calls or
            (any) getter value */
    $.fn.selectx = function(options) {
        var otherArgs = Array.prototype.slice.call(arguments, 1);
        if (isNotChained(options, otherArgs)) {
            return plugin['_' + options + 'Plugin'].apply(plugin, [this[0]].concat(otherArgs));
        }
        return this.each(function() {
            if (typeof options == 'string') {
                if (!plugin['_' + options + 'Plugin']) {
                    throw 'Unknown method: ' + options;
                }
                plugin['_' + options + 'Plugin'].apply(plugin, [this].concat(otherArgs));
            }
            else {
                plugin._attachPlugin(this, options || {});
            }
        });
    };

    /* Initialise the max length functionality. */
    plugin = $.selectx = new SelectX(); // Singleton instance

})(jQuery);
