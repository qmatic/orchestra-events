/*
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR 
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE 
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, 
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE 
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.qmatic.qp.events.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.qmatic.qp.core.common.QPEvent;
import com.qmatic.qp.events.services.EventPublishService;

/**
 * Message driven POJO as a Spring component.
 * Receives JMS messages from the Orchestra JMS public Topic and calls the {@link EventPublishService} to publish them
 * 
 * @author gavsmi
 *
 */
@Component
public class EventListener {

	private static final Logger log = LoggerFactory.getLogger(EventListener.class);
	
	@Autowired
	private EventPublishService eventPublishService;
	
	@JmsListener(destination = "topic/qpPublicEventTopic")
	public void onMessage(QPEvent event) {
		log.debug("Event recieved on qpPublicEventTopic topic: {}", event.toString());
		
		try {
			eventPublishService.publishMessage(event);
		} catch(Exception x) {
			log.error("Error processing event on qpPublicEventTopic topic.", x);
		}
	}
	
}
