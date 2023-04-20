/* Marco Faustinelli, 2013 (http://www.faustinelli.net/, https://github.com/muzietto)
   Select Xor for jQuery - release 0.3
   Dual licensed under the GPL (http://dev.jquery.com/browser/trunk/jquery/GPL-LICENSE.txt) and 
   MIT (http://dev.jquery.com/browser/trunk/jquery/MIT-LICENSE.txt) licenses. 
   Please attribute the author if you use this plugin.
   Requires underscore.js */

(function($) {
    var SelectX, getters, isNotChained, plugin, log
    ;

    /* SelectX */
    SelectX = function() {
        this._defaults = {
            /* if true, ALL options disappear from the list after the first choice is made
		     and reappear when the selection widget is destroyed */
            selectOne: false,
            /* if true, selected options disappear from the list
		     and reappear when related selection widget is destroyed */
            selectOnce: false,
            /* jQuery selector of the container where the plugin
		     creates a widget for each selection made */
            selectionsContainer: null,
            /* flag for template engine usage - only jtemplates allowed so far */
            templateEngine: false,
            /* if true, each option receives a unique id (select id as prefix - if any) */
            optionIds: false,
            /* must be set to some sort of function(msg){...}}, callable with 'inst.options.log('abdce');
             * NB - if you use it you gotta put yourself in the code all log instructions you need */
            log: function(){
                return false;
            },
            /* flag to signal that an item has been chosen;
             * used during plugin redraw */
            flagChosenOption: 'chosenOption',
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
	   @param  target   (element) the control to affect - IT MUST BE AN HTML SELECT!!!
	   @param  options  (object) the custom options for this instance */
        _attachPlugin: function(target, options) {
            var inst,
            $target = $(target)
            ;
            if ($target.hasClass(this.markerClassName) /* || target is already a selectx */) {
                return;
            }
            inst = {
                options: $.extend({}, this._defaults), // this = SelectX object
                selectionsContainer: $([])
            };

            $target.addClass(this.markerClassName).
            data(this.pluginName, inst).
            bind('change.' + this.pluginName, function() { // MAIN BINDING FOR THE WHOLE PLUGIN!!!
                plugin._changeSelect($(this),inst.options); // attach main handler
            });
            this._optionPlugin(target, options);
            return this;
        },

        /* Retrieve or reconfigure the settings for a control.
	   @param  target   (element) the control to affect - IT MUST BE AN HTML SELECT!!!
	   @param  options  (object) the new options for this instance or
	                    (string) an individual property name
	   @param  value    (any) the individual property value (omit if options
	                    is an object or to retrieve the value of a setting)
	   @return  (any) if retrieving a value */
        _optionPlugin: function(target, options, value) {
            var $target = $(target),
            inst = $target.data(this.pluginName),
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
            /* end options preprocessing - optional exit here */
            if (typeof value !== 'undefined') {  // if we are just setting an option...
                return this;
            }
		
            /* otherwise, start build operations by this method */
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
                    if (optObj.text && optObj.value){ // minimal guard against bad optionObjects
                        var $option = $('<option/>',optObj);

                        // options decide whether to give an id to each option
                        if (inst.options.optionIds){
                            // e.g. "<selectid>.123"
                            $option.attr('id',$target.attr('id')+'.'+optObj.value);
                        }

                        // append given data to the option
                        if (optObj && optObj.data) {  // workaround to IE/underscore.js quirk
                            $option.data(that.pluginName+'-optiondata',optObj.data);
                        }
                        $target.append($option);

                        /* AAA!!! - optional re-initialisation of the option widget
                        * NB: at this step in the code instance options and defaults
                        * haven't yet been merged! */
                        if (optObj.data && optObj.data[options.flagChosenOption||plugin._defaults.flagChosenOption]){
                            plugin._changeSelect($target, options, $option);
                        }
                    }
                });
            }
            return this;
        },

        /* Remove the plugin functionality from a control.
		'this' is the SelectX object
		@param target is the select the plugin is attached to  */
        _destroyPlugin: function(target,KEEP_OPTIONS) {
            var $target = $(target),
            options
            ;
            if (!$target.hasClass(this.markerClassName)) {
                return false;
            }
            options = $(target).data('selectx').options;

            if (options.selectOne || options.selectOnce) {  // clear all the <option>
                this._resetPlugin($target);
            }
            // remove all stuff: selectionsContainer and whatever
            $target.next('.selectx-selections-container').remove()
            // clean up target (NB: chained call over more lines)
            $target.removeClass(this.markerClassName).  // plus any further class
            removeData(this.pluginName).
            unbind('.' + this.pluginName);  // all plugin event handlers
            if (!KEEP_OPTIONS){
                $('option',$target).remove();
            }
            return this;
        },
	
        /* Restore the visibility of all <option>'s.
		If !selectOnce, this is kinda pointless 
		- 'this' is the SelectX object
		- @param $target is the $(select) the plugin is attached to  */
        _resetPlugin: function($target) {
            var inst
            ;
            $target = $($target);
            if (!$target.hasClass(this.markerClassName)) {
                return;
            }
            inst = $target.data(this.pluginName);
		
            $('span',$target).each(function(index,span){
                var opt, dummySpan
                ;
                // cross-browser hack (cfr. http://ajax911.com/hide-options-selecbox-jquery/)
                opt = $('option',$(span));
                $(span).replaceWith(opt);
            });
            return this;
        },
	
        /* Manages all activities following a user choice on the $target select
        'this' is the SelectX object
        @param $target is the $(select) the plugin is attached to
        @param options is the whole set of instance options
        @param $optionToDress (optional) is sent during a reinitialisation of a selectx
        based on db data related to previous choices   */
        _changeSelect: function($target, options, $optionToDress) {
            var $optionsWidgetContainer, $optionsWidget,
            $chosenOption, $widgetDeleter,
            inst = $target.data(this.pluginName),
            widgetModelData, optionData,
            that = this
            ;

            $chosenOption = $optionToDress ? $optionToDress : $(':selected',$target);
            optionData = $chosenOption.data(that.pluginName+'-optiondata');

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
                    widgetModelData = inst.options.optionWidgetDataModel(optionData);
                }
                else {
                    /* otherwise it must be ready to provide data to the template */
                    widgetModelData = inst.options.optionWidgetDataModel;
                }

                // if desired, pass the option id to the widget (slightly modified!)
                if (inst.options.optionIds){
                    widgetModelData.id = $chosenOption.attr('id')+'_widget';
                }

                $optionsWidgetContainer.setTemplate(options.templateHtml)
                .processTemplate(widgetModelData);
                // only now I can instantiate $optionsWidget
                $optionsWidget = $('.'+this.optionWidgetClass,$optionsWidgetContainer);
                inst.selectionsContainer.append($optionsWidgetContainer.append($optionsWidget));
            }
		
            /* widget deleter - it receives a reference to the chosen option
		   in order to make it again visible once clicked */
            $widgetDeleter = $('.'+this.deleteWidgetClass,$optionsWidget);
            $widgetDeleter.data(this.pluginName+'-option',$chosenOption);  // attach option to $deleter
            $widgetDeleter.click(function(event){
                var $opt, dummySpan
                ;
                /* NB - 'this' = $widgetDeleter[0] !!!
                 * gotta use 'that' to refer to the plugin instance */
                if (options.selectOne) { // restore all <option>
                    that._resetPlugin($target);
                }
                else if (options.selectOnce) {
                    // option returns visible
                    //	$(this).data(that.pluginName+'-option')
                    //	.removeClass(that.selectedOptionClass);  //  - THIS WON'T WORK IN IE!!!

                    $opt = $(this).data(that.pluginName+'-option');
                    /*
                    // cross-browser hack (cfr. http://ajax911.com/hide-options-selecbox-jquery/)
                    if ($opt.parent('span').length > 0) {
                        dummySpan = $opt.parent('span');
                        dummySpan.replaceWith($opt);
                    }
                    */
                   $opt.removeAttr('disabled');
                }
                /* remove object locator; if more stuff needs removing,
                 * put instructions inside onSelectCallback */
                if (optionData && optionData.locator) {
                    delete optionData.locator;
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

            if (options.selectOne) {  // make invisible ALL options
                $('option',$target).wrap('<span style="display:none;">');
            }
            else if (options.selectOnce) {
                // find and mark selected option - it becomes invisible - THIS WON'T WORK IN IE!!!
                //$chosenOption.addClass(this.selectedOptionClass);
			
                // cross-browser hack (cfr. http://ajax911.com/hide-options-selecbox-jquery/)
                //$chosenOption.wrap('<span style="display:none;">');
                $chosenOption.attr('disabled','disabled');
            }
		
            // execute custom callback - this = $optionsWidget
            if (options.onSelectCallback) {
                options.onSelectCallback.apply($optionsWidget);
            }
            $target.val('');  // reset select to 'please choose item'
        },
	
        /* Returns an array with all the chosen option data.
         * If no option data used, returns []
        @param target is the select whose $() the plugin is attached to */
        _chosenDataPlugin: function(target) {
            var result = [], that = this
            ;
            // cycle through all the option widgets and collect the attached objects
            $('.'+this.optionWidgetClass,$(target).next('.'+this.selectionsClassName)).each(function(index,optWidget){
                if ($(optWidget).data(that.pluginName+'-optiondata')) {
                    result.push($(optWidget).data(that.pluginName+'-optiondata'));
                }
            });
            return result;
        },

        /* Returns an integer with the base2 bitmask
         * of positions of the chosen options.
         * AAA - initial null option (if present) is taken into account!!!
        @param $target is the $(select) the plugin is attached to */
        _bitmaskPlugin: function(target) {
            var result = 0
            ;
            $('option',$(target)).each(function(index,option){
                if ($(option).parent('span').length>0){
                    result = result + Math.pow(2, index);
                }
            })
            return result;
        },

        log : function(text){
            if (this.options.logger) {
                this.options.logger.log('SelectX: ' +text);
            }
        }

    });

    // The list of methods that return values and don't permit chaining
    getters = ['chosenData','bitmask'];

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
    };

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
