/*
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR 
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE 
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, 
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE 
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.qmatic.qp.events.webhooks;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.qmatic.qp.core.common.QPEvent;
import com.qmatic.qp.events.EventService;

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
	public void publishMessage(QPEvent event) throws Exception {
		
		// POST JSON to each registered endpoint
		for(String endpoint : webhookRegistry.all()) {
			log.debug("POSTing event to {}", endpoint);
			
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpPost post = new HttpPost(endpoint);
			
			post.setHeader("Content-Type", "application/json");
			post.setEntity(new StringEntity(objectMapper.writeValueAsString(event), "UTF-8"));
			
			HttpResponse response = httpClient.execute(post);
			log.debug("HTTP client response: {}", response.getStatusLine().getStatusCode());
		}
	}
	
	@Override
	public boolean isEnabled() {
		return this.enabled;
	}
}
