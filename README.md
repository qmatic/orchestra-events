#orchestra-central-events-cometd#
##Description##
A module for the [Qmatic](http://www.qmatic.com) Orchestra platform to publish events via [CometD](http://cometd.org)

Listens to the Orchestra public event JMS Topic  
Clients subscribe via CometD channels  
Event payload data delivered as JSON

##Requirements##
* Qmatic Orchestra 5.3 >

##Building##
* Clone repository
* Run `gradlew clean build`
* Copy `build/libs/qp-central-events-cometd-0.1.0.war` to Orchestra `/custdeploy` folder

##Using##
Subscribe to the CometD channels via the URL `/cometevents/cometd`

Channel subscription is based on the syntax `/events/<event_name>/<branch_id>`  

Event names:

* VISIT_REMOVE  
* VISIT_CREATE  
* VISIT_CALL  
* SERVICE_POINT_CLOSE  
* RESET  
* VISIT_CONFIRM  
* VISIT_END  
* VISIT_NOSHOW  
* USER_SESSION_START  
* USER_SESSION_END  
* VISIT_NEXT  
* USER_SERVICE_POINT_SESSION_START  
* USER_SERVICE_POINT_SESSION_END  
* SERVICE_POINT_OPEN  
* USER_SERVICE_POINT_WORK_PROFILE_SET  
* VISIT_TRANSFER_TO_QUEUE  
* VISIT_TRANSFER_TO_SERVICE_POINT_POOL  
* VISIT_TRANSFER_TO_USER_POOL  
* VISIT_RECYCLE  
* UNSUPPORTED  

Wildcards are supported in CometD for event subscriptions:

	/events/**  - subscribe to all events for all branches
	/events/*/1 - subscribe to all events for branch with id 1

##Example##
An example exists at the URL `/cometevents` that subscribes to all events via the CometD JavaScript client
and prints event JSON payloads to screen.