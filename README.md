#orchestra-events#
##Description##
A module for the [Qmatic](http://www.qmatic.com) Orchestra platform to publish events.

* Listens to the Orchestra public event JMS Topic  
* Supports client subscription via [CometD](http://cometd.org) channels  
* Event payload data delivered as JSON

##Requirements##
* Qmatic Orchestra 5.3 >

##Building##
* Clone repository
* Run `gradlew clean build`
* Copy `build/libs/qp-events-x.x.x.war` to Orchestra `/custdeploy` folder

##Using##
###CometD###
Subscribe to the CometD channels via the URL `/qpevents/cometd`

Channel subscription is based on the syntax `/events/<event_name>/<branch_id>`  

Wildcards are supported in CometD for event subscriptions:

	/events/**  - subscribe to all events for all branches
	/events/*/1 - subscribe to all events for branch with id 1

##Example##
An example exists at the URL `/qpevents` that subscribes to all events via the CometD JavaScript client
and prints event JSON payloads to screen.

For event payload documentation see the [wiki](https://github.com/qmatic/orchestra-central-events-cometd/wiki/Events)