/*
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR 
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE 
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, 
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE 
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.qmatic.qp.events.services.webhook;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Simple singleton registry to keep a list of registered endpoints.
 * 
 * @author gavsmi
 *
 */
@Component
@Scope("singleton")
public class WebhookRegistry {

	private static final Logger log = LoggerFactory.getLogger(WebhookRegistry.class);
			
	private List<String> endpoints = new ArrayList<String>();
	
	public void add(String endpoint) {
		if(!endpoints.contains(endpoint)) {
			log.debug("Adding endpoint {}", endpoint);
			endpoints.add(endpoint);
		} else {
			log.debug("Received registration for already registered endpoint {}", endpoint);
		}
	}
	
	public void remove(String endpoint) {
		log.debug("Removing endpoint {}", endpoint);
		endpoints.remove(endpoint);
	}
	
	public List<String> all() {
		return endpoints;
	}
}
