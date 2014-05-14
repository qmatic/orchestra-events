$(function () {
	'use strict';
	
	var content = $('#content');
	var status = $('#status');
	var cometd = $.cometd;
	
	cometd.registerTransport('websocket', new org.cometd.WebSocketTransport());
	cometd.registerTransport('long-polling', new org.cometd.LongPollingTransport());
	cometd.registerTransport('callback-polling', new org.cometd.CallbackPollingTransport());
	
	var r = document.location.href.match(/^(.*)\//);
	console.log(r[1]);
	cometd.init(r[1] + '/cometd');
	
	cometd.addListener('/meta/handshake', function(handshake) {
		if (handshake.successful === true ) {
			cometd.batch(function () {
				status.html($('<p>', {text: 'connected!'}));
				startSubscription();
			});
		}
	});
	
	cometd.handshake();
	
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