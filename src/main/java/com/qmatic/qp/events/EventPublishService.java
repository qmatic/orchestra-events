/*
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR 
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE 
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, 
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE 
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.qmatic.qp.events;

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
	}
}
