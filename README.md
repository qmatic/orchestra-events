#orchestra-events
##Description
A module for the [Qmatic](http://www.qmatic.com) Orchestra platform to publish events.

* Listens to the Orchestra public event JMS Topic  
* Supports client subscription via [CometD](http://cometd.org) channels  
* Supports client subscription via registering web hooks
* Supports sending stats to [StatHat](http://www.stathat.com) service (account required)
* Event payload data delivered as JSON

##Requirements
* Qmatic Orchestra 5.3 >

##Building
* Clone repository
* Run `gradlew clean build`
* Copy `build/libs/qp-events-x.x.x.war` to Orchestra `/custdeploy` folder

##Using
###CometD
Subscribe to the CometD channels via the URL `/qpevents/cometd`

Channel subscription is based on the syntax `/events/<event_name>/<branch_id>`  

Wildcards are supported in CometD for event subscriptions:

	/events/**  - subscribe to all events for all branches
	/events/*/1 - subscribe to all events for branch with id 1

###Web Hooks
Register a web hook endpoint via the URL `/qpevents/hooks/register?url=<your_endpoint>`

De-register a web hook endpoint via the URL `/qpevents/hooks/deregister?url=<your_endpoint>`

Event data will be sent via HTTP POST to the endpoint specified, example:

	POST /events HTTP/1.1
	Host: test.dev.qmatic.com
	Accept: application/json
	Content-Length: 163
	
	{"eventName": "USER_SESSION_START", "eventTime": "2014-05-27T09:46:10.757+0000", "eventType": "PUBLIC", "unitId": null, "parameters": {"user": null, "userId": 1}}

###StatHat
Send stats to [StatHat](http://www.stathat.com) SaaS stat tracking service.

Register with [StatHat](http://www.stathat.com) for an account.

Edit `events.properties` to enable StatHat support and specify our API key:

	 stathat.enabled = true
	 stahat.key = no.body@mycompany.com

See [StatHatService.java](src/main/java/com/qmatic/qp/events/stathat/StatHatService.java) to configure what gets sent to StatHat.

###Persistence
Optionally events can be persisted to the Orchestra stat database.

Edit `events.properties` to enable stat persistence

	stat.enabled = true

Creates two tables in stat DB to store event data, `visit_events` and `sp_events`

##Example
An example exists at the URL `/qpevents` that subscribes to all events via the CometD JavaScript client
and prints event JSON payloads to screen.

For event payload documentation see the [wiki](https://github.com/qmatic/orchestra-central-events-cometd/wiki/Events)