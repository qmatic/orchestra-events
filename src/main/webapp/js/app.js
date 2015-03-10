$(function () {
	'use strict';
	
	var content = $('#content');
	var status = $('#status');
	var cometd = $.cometd;
	
  // websocket not supported
	cometd.unregisterTransport('websocket');
	
	var r = document.location.href.match(/^(.*)\//);
	console.log(r[1]);
	
  cometd.configure({
    url: r[1] + '/cometd',
    logLevel: 'warn'
  });
  
  cometd.handshake(function(reply) {
    if(reply.successful) {
			status.html($('<p>', {text: 'connected!'}));
			startSubscription();
    }
  });
  
	function startSubscription() {
		cometd.subscribe('/events/**', function(msg) {
			console.log('Received message: ', msg);
		
			try {
				var json = JSON.parse(msg.data);
			} catch(e) {
				console.log('This doesn\'t look like a valid JSON message: ', msg.data);
				return;
			}
		
			addMessage(json);
		});
	}
	
	function addMessage(message) {
		content.append('<pre class="pre-scrollable">' + JSON.stringify(message, null, '\t') + '</pre>');
	}
});