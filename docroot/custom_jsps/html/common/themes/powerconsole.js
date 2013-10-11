(function(jQuery) {
    jQuery.fn.tilda = function(eval, options) {
        if (jQuery('body').data('tilda')) {
            return jQuery('body').data('tilda').terminal;
        }
        this.addClass('tilda');
        options = options || {};
        eval = eval || function(command, term) {
            term.echo("you don't set eval for tilda");
        };
        var settings = {
            prompt: 'pwc>',
            name: 'pwc',
            height: consoleHeight,
            enabled: false,
            greetings: welcomeMessage
        };
        if (options) {
            jQuery.extend(settings, options);
        }
        this.append('<div class="td"></div>');
        var self = this;
        self.terminal = this.find('.td').terminal(eval, settings);
        var focus = false;
        jQuery(document.documentElement).keypress(function(e) {
            if (e.which == 96) {
                self.slideToggle('fast');
                self.terminal.set_command('');
                self.terminal.focus(focus = !focus);
                self.terminal.attr({
                    scrollTop: self.terminal.attr("scrollHeight")
                });
            }
        });
        jQuery('body').data('tilda', this);
        this.hide();
        return self;
    };
})(jQuery);

jQuery('#tilda').tilda(function(command, term) {
	var id = 1;
    if (command == 'help') {
      term.echo("Power Console runs in these modes: server, client, db, jvm\nTo exit from a mode, type 'exit'");
    } else if (command == 'server'){
      term.push(function(command, term) {
    	  if(command == '') {
    		  return;
    	  }
    	  term.pause();
    	  Liferay.Service.powerconsole.PowerConsole.runCommand(
    			  {
    				  userId: userId,
    				  companyId: companyId,
    				  mode:'server',
    				  command:command
    			  }, 
    			  function(message) {
    				  var exception = message.exception;
    				  if (!exception) {
    					  term.resume();
    					  term.echo(message.returnValue);
    				  } else {
    					  term.resume();
    					  term.error(exception);
    				  }
    			  }
		  );
      }, {
    	greetings: "The Liferay server console",
        prompt: 'server>',
        name: 'server'
        });
    } else if (command == "client") {
      term.push(function(command, term) {
        var result = window.eval(command);
        if (result != undefined) {
          term.echo(String(result));
        }
      }, {
        name: 'client',
        prompt: 'client>'
        });
      } else if (command == 'db') {
        term.push(function(command, term) {
    	  if(command == '') {
    	  	return;
    	  }
          term.pause();
          Liferay.Service.powerconsole.PowerConsole.runCommand(
    			  {
    				  userId: userId,
    				  companyId: companyId,
    				  mode:'db',
    				  command:command
    			  }, 
    			  function(message) {
    				  var exception = message.exception;
    				  if (!exception) {
    					  term.resume();
    					  term.echo(message.returnValue);
    				  } else {
    					  term.resume();
    					  term.error(exception);
    				  }
    			  }
		  );
          }, {
            greetings: "The database console",
            name: 'db',
            prompt: "db>"});
      } else if (command == 'jvm') {
	        term.push(function(command, term) {
	        	  if(command == '') {
		    		  return;
		    	  }
		          term.pause();
		          Liferay.Service.powerconsole.PowerConsole.runCommand(
		    			  {
		    				  userId: userId,
		    				  companyId: companyId,
		    				  mode:'jvm',
		    				  command:command
		    			  }, 
		    			  function(message) {
		    				  var exception = message.exception;
		    				  if (!exception) {
		    					  term.resume();
		    					  term.echo(message.returnValue);
		    				  } else {
		    					  term.resume();
		    					  term.error(exception);
		    				  }
		    			  }
				  );
          }, {
            greetings: "The JVM console",
            name: 'jvm',
            prompt: "jvm>"});
	  } else {
        term.echo("Unknown command " + command);
      }
});