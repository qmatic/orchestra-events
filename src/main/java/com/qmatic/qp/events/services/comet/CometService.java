/*
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR 
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE 
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, 
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE 
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.qmatic.qp.events.services.comet;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.codehaus.jackson.map.ObjectMapper;
import org.cometd.annotation.Service;
import org.cometd.annotation.Session;
import org.cometd.bayeux.MarkedReference;
import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.bayeux.server.ConfigurableServerChannel;
import org.cometd.bayeux.server.LocalSession;
import org.cometd.bayeux.server.ServerChannel;
import org.cometd.server.authorizer.GrantAuthorizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;

import com.qmatic.qp.core.common.QPEvent;
import com.qmatic.qp.events.services.EventService;

/**
 * The cometD service, publishes event messages on dynamic channels
 * Channel name is derived from the stat prefix '/events/' with event name and branch id (e.g. '/events/VIST_CREATE/1')
 * 
 * @author gavsmi
 *
 */
@Named
@Singleton
@Service
@Qualifier("comet")
public class CometService implements EventService {

	private static final Logger log = LoggerFactory.getLogger(CometService.class);
	
	private static final String CHANNEL_PREFIX = "/events/";
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	@Inject
	private BayeuxServer bayeuxServer;
	
	@Session
	private LocalSession session;
	
	@Override
	public void publishMessage(QPEvent event) {
		// create the dynamic channel id
		String channelId = CHANNEL_PREFIX + event.getEventName() + "/" + event.getParameter("branchId");
		
		try {
			// create the dynamic channel
			MarkedReference<ServerChannel> ref = this.bayeuxServer.createChannelIfAbsent(channelId, new ConfigurableServerChannel.Initializer() {

				@Override
				public void configureChannel(ConfigurableServerChannel channel) {
					channel.addAuthorizer(GrantAuthorizer.GRANT_SUBSCRIBE);
					// set the channel to non-persistent after publishing so the sweeper can clean up if
					// nobody is listening
					channel.setPersistent(false);
					channel.setLazy(false);
				}
				
			});
			
			ServerChannel channel = ref.getReference();
			
			if(channel != null) {	
				// publish to the dynamic channel
				channel.publish(this.session, objectMapper.writeValueAsString(event));
			} else {
				log.warn("Unable to create dynamic channel '{}'", channelId);
			}
		} catch(Exception x) {
			log.error("Error handling event.", x);
		}
	}
	
	@Override
	public boolean isEnabled() {
		return (boolean) this.bayeuxServer.getOption("enabled");
	}
}
