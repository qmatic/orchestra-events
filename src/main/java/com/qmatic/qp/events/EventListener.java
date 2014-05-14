/*
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR 
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE 
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, 
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE 
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.qmatic.qp.events;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.qmatic.qp.core.common.QPEvent;
import com.qmatic.qp.events.comet.EventService;

/**
 * Message driven POJO as a Spring component.
 * Receives JMS messages from the Orchestra JMS public Topic and calls the {@link EventService} to publish them via cometD
 * 
 * @author gavsmi
 *
 */
@Component
public class EventListener implements MessageListener {

	private static final Logger log = LoggerFactory.getLogger(EventListener.class);
	
	@Autowired
	private EventService eventService;
	
	@Override
	public void onMessage(Message message) {
		if(message instanceof ObjectMessage) {
			try {
				ObjectMessage msg = (ObjectMessage) message;
				QPEvent event = (QPEvent) msg.getObject();
				
				log.debug("Message recieved on qpPublicEventTopic topic. event name: {}", new Object[]{event.getEventName()});
				
				eventService.publishMessage(event);
			} catch(Exception x) {
				log.error("Error processing event on qpPublicEventTopic topic.", x);
			}
		}
	}
	
}
