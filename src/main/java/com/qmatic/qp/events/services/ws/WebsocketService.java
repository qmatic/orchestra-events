/*
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR 
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE 
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, 
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE 
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.qmatic.qp.events.services.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.qmatic.qp.core.common.QPEvent;
import com.qmatic.qp.events.services.EventService;

/**
 * WebsocketService - sends events on websocket channel via STOMP
 * 
 * @author gavsmi
 *
 */
@Service
@Qualifier("ws")
public class WebsocketService implements EventService {

	private static final Logger log = LoggerFactory.getLogger(WebsocketService.class);
	
	@Value("${websocket.enabled}")
	private boolean enabled = false;
	
	@Autowired
	private SimpMessagingTemplate template;
	
	@Override
	public void publishMessage(QPEvent event) {
		log.info("Sending event on websocket!");
		
		template.convertAndSend("/topic/event", event);
	}
	
	@Override
	public boolean isEnabled() {
		return this.enabled;
	}
}
