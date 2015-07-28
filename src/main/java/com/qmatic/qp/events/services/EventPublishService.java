/*
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR 
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE 
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, 
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE 
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.qmatic.qp.events.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.qmatic.qp.core.common.QPEvent;

/**
 * EventService, publishes event messages to enabled services.
 * 
 * @author gavsmi
 *
 */
@Service
public class EventPublishService {

	private static final Logger log = LoggerFactory.getLogger(EventPublishService.class);
			
	@Autowired
	@Qualifier("comet")
	private EventService cometService;
	
	@Autowired
	@Qualifier("webhook")
	private EventService webhookService;
	
	@Autowired
	@Qualifier("stathat")
	private EventService statHatService;
	
	@Autowired
	@Qualifier("stat")
	private EventService statService;
	
	@Autowired
	@Qualifier("ws")
	private EventService websocketService;
	
	@Async
	public void publishMessage(QPEvent event) throws Exception {
		// Publish message to each enabled service
		if(cometService.isEnabled()) {
			log.debug("Comet service enabled, publishing...");
			cometService.publishMessage(event);
		}
		
		if(webhookService.isEnabled()) {
			log.debug("Webhook service enabled, publishing...");
			webhookService.publishMessage(event);
		}
		
		if(statHatService.isEnabled()) {
			log.debug("StatHat service enabled, publishing...");
			statHatService.publishMessage(event);
		}
		
		if(statService.isEnabled()) {
			log.debug("Stat service enabled, publishing...");
			statService.publishMessage(event);
		}
		
		if(websocketService.isEnabled()) {
			log.debug("Websocket service enabled, publishing...");
			websocketService.publishMessage(event);
		}
	}
}
