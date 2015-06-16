/*
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR 
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE 
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, 
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE 
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.qmatic.qp.events.services.stat;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.qmatic.qp.api.event.PublicEvents;
import com.qmatic.qp.api.util.TimeUtil;
import com.qmatic.qp.core.common.QPEvent;
import com.qmatic.qp.events.jpa.ServicePointEventDao;
import com.qmatic.qp.events.jpa.VisitEventDao;
import com.qmatic.qp.events.jpa.model.EventId;
import com.qmatic.qp.events.jpa.model.ServicePointEvent;
import com.qmatic.qp.events.jpa.model.VisitEvent;
import com.qmatic.qp.events.services.EventService;

/**
 * StatService persists events to stat database.
 * 
 * @author gavsmi
 *
 */
@Service
@Qualifier("stat")
public class StatService implements EventService {

	private static final Logger log = LoggerFactory.getLogger(StatService.class);
	
	@Autowired
	private VisitEventDao visitEventDao;
	
	@Autowired
	private ServicePointEventDao servicePointEventDao;
	
	@Value("${stat.enabled}")
	private boolean enabled = false;
	
	@Override
	public void publishMessage(QPEvent event) {
		
		log.debug("Handling event {}", event.getEventName());
		
		ServicePointEvent spEvent = null;
		VisitEvent visitEvent = null;
		
		try {
			switch(PublicEvents.valueOf(event.getEventName())) {
			case VISIT_CREATE:
				visitEvent = new VisitEvent();
				visitEvent.setServiceId((Integer)event.getParameter("service")); //serviceOrigId
				visitEvent.setServiceName((String)event.getParameter("serviceIntName"));
				visitEvent.setQueueId((Long)event.getParameter("queueId"));
				visitEvent.setQueueName((String)event.getParameter("queueName"));
				visitEvent.setTransactionTarget((Integer)event.getParameter("serviceTargetTransTime"));
				visitEvent.setWaitingSla((Integer)event.getParameter("queueServiceLevel"));
				break;
				
			case VISIT_REMOVE:
				visitEvent = new VisitEvent();
				visitEvent.setServiceId((Integer)event.getParameter("service"));
				visitEvent.setQueueId((Long)event.getParameter("queueId"));
				visitEvent.setQueueName((String)event.getParameter("queueName"));
				visitEvent.setWaitingSla((Integer)event.getParameter("queueServiceLevel"));
				break;
				
			case VISIT_CALL:
				visitEvent = new VisitEvent();
				visitEvent.setServiceId((Integer)event.getParameter("service")); //serviceOrigId
				visitEvent.setQueueId((Long)event.getParameter("queueId"));
				visitEvent.setQueueName((String)event.getParameter("queueName"));
				visitEvent.setWaitingSla((Integer)event.getParameter("queueServiceLevel"));
				visitEvent.setWaitingTime((Integer)event.getParameter("waitingTime"));
				break;
				
			case VISIT_RECYCLE:
				visitEvent = new VisitEvent();
				visitEvent.setServiceId((Integer)event.getParameter("service"));
				visitEvent.setQueueId((Long)event.getParameter("queueId"));
				visitEvent.setQueueName((String)event.getParameter("queueName"));
				break;
				
			case VISIT_NOSHOW:
				visitEvent = new VisitEvent();
				visitEvent.setWaitingTime((Integer)event.getParameter("waitingTime"));
				break;
				
			case VISIT_TRANSFER_TO_QUEUE:
				visitEvent = new VisitEvent();
				visitEvent.setQueueId((Long)event.getParameter("queueId"));
				visitEvent.setWaitingSla((Integer)event.getParameter("queueServiceLevel"));
				break;
				
			case VISIT_TRANSFER_TO_SERVICE_POINT_POOL:
			case VISIT_TRANSFER_TO_USER_POOL:
				visitEvent = new VisitEvent();
				break;
				
			case VISIT_END:
				visitEvent = new VisitEvent();
				visitEvent.setWaitingTime((Integer)event.getParameter("waitingTime"));
				break;
			
				
			case SERVICE_POINT_OPEN:
			case USER_SERVICE_POINT_SESSION_START:
			case USER_SERVICE_POINT_WORK_PROFILE_SET:
				spEvent = new ServicePointEvent();
				spEvent.setServicePointName((String)event.getParameter("servicePointName"));
				spEvent.setWorkProfileId((Integer)event.getParameter("workProfile"));
				spEvent.setWorkProfileName((String)event.getParameter("workProfileName"));
				break;
				
			case USER_SERVICE_POINT_SESSION_END:
			case SERVICE_POINT_CLOSE:
				spEvent = new ServicePointEvent();
				break;
				
			default: 
				break;
			}
			
			if(visitEvent != null) {
				log.debug("Persisting event {}", event.getEventName());
				
				EventId eventId = new EventId();
				eventId.setId((Long)event.getParameter("visitId"));
				eventId.setTs(new Timestamp(TimeUtil.parseStringToDateTime(event.getEventTime()).getMillis()));
				eventId.setBranchId((Integer)event.getParameter("branchId"));
				visitEvent.setId(eventId);
				
				visitEvent.setEvent(event.getEventName());
				visitEvent.setBranchName((String)event.getParameter("branchName"));
				
				log.debug("Persisting event data {}", visitEvent.toString());
				
				visitEventDao.save(visitEvent);
			}
			
			if(spEvent != null) {
				log.debug("Persisting event {}", event.getEventName());
				
				EventId eventId = new EventId();
				eventId.setId(event.getUnitId());
				eventId.setTs(new Timestamp(TimeUtil.parseStringToDateTime(event.getEventTime()).getMillis()));
				eventId.setBranchId((Integer)event.getParameter("branchId"));
				spEvent.setId(eventId);
				
				spEvent.setEvent(event.getEventName());
				
				log.debug("Persisting event data {}", spEvent.toString());
				
				servicePointEventDao.save(spEvent);
			}
		} catch(Exception x) {
			log.error("Error handling event.", x);
		}
	}
	
	@Override
	public boolean isEnabled() {
		return this.enabled;
	}
}
