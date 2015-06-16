/*
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR 
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE 
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, 
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE 
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.qmatic.qp.events.services.webhook;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.qmatic.qp.core.common.QPEvent;
import com.qmatic.qp.events.services.EventService;

/**
 * Webhook service, posts JSON event data to registered endpoints.
 * 
 * @author gavsmi
 *
 */
@Service
@Qualifier("webhook")
public class WebhookService implements EventService {

	private static final Logger log = LoggerFactory.getLogger(WebhookService.class);
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private WebhookRegistry webhookRegistry;
	
	@Value("${webhook.enabled}")
	private boolean enabled = false;
	
	@Override
	public void publishMessage(QPEvent event) {
			
		// POST JSON to each registered endpoint
		for(final String endpoint : webhookRegistry.all()) {
			log.debug("POSTing event to {}", endpoint);
			
			try {
				CloseableHttpAsyncClient httpClient = HttpAsyncClients.createDefault();
				httpClient.start();
				
				HttpPost post = new HttpPost(endpoint);
				post.setHeader("Content-Type", "application/json");
				post.setEntity(new StringEntity(objectMapper.writeValueAsString(event), "UTF-8"));
				
				httpClient.execute(post, new FutureCallback<HttpResponse>() {
					
					@Override
					public void failed(Exception ex) {
						log.warn("Request for endpoint {} failed, removing endpoint", endpoint);
						webhookRegistry.remove(endpoint);
					}
					
					@Override
					public void completed(HttpResponse result) {
						log.debug("Request for endpoint {} completed", endpoint);
					}
					
					@Override
					public void cancelled() {
						log.debug("Request for endpoint {} cancelled", endpoint);
					}
				});
			} catch(Exception x) {
				log.error("Error handling event.", x);
			}
		}
	}
	
	@Override
	public boolean isEnabled() {
		return this.enabled;
	}
}
