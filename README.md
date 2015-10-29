#orchestra-events
##Description
A module for the [Qmatic](http://www.qmatic.com) Orchestra platform to publish events.

* Listens to the Orchestra public event JMS Topic  
* Supports client subscription via STOMP over websocket
* Supports client subscription via registering web hooks
* Event payload data delivered as JSON

##Requirements
* Qmatic Orchestra 6 >

##Building
* Clone repository
* Run `gradlew clean build`
* Copy `build/libs/qp-events-x.x.x.war` to Orchestra `/custdeploy` folder

##Using
###STOMP
See [Spring STOMP client documentation](http://docs.spring.io/spring/docs/current/spring-framework-reference/html/websocket.html#websocket-stomp-client)

For JavaScript client see the example application provided, change the URL for the SockJS client accordingly: 

    `var socket = new SockJS('http://my.orchestra.host:8080/qpevents/events');`

Message subscription is based on the syntax `/topic/events/<event_name>/<branch_id>`

###Web Hooks
Register a web hook endpoint via the URL `/qpevents/hooks/register?url=<your_endpoint>`

De-register a web hook endpoint via the URL `/qpevents/hooks/deregister?url=<your_endpoint>`

Event data will be sent via HTTP POST to the endpoint specified, example:

	POST /events HTTP/1.1
	Host: test.dev.qmatic.com
	Accept: application/json
	Content-Length: 163
	
	{"eventName": "USER_SESSION_START", "eventTime": "2014-05-27T09:46:10.757+0000", "eventType": "PUBLIC", "unitId": null, "parameters": {"user": null, "userId": 1}}

##Example
An example exists at the URL `/qpevents` that subscribes to all events via STOMP over websocket
and prints event JSON payloads to screen.

For event payload documentation see the [wiki](https://github.com/qmatic/orchestra-events/wiki/Events)