/*
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR 
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE 
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, 
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE 
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.qmatic.qp.events.stathat;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.qmatic.qp.api.event.PublicEvents;
import com.qmatic.qp.core.common.QPEvent;
import com.qmatic.qp.events.EventService;

@Service
@Qualifier("stathat")
public class StatHatService implements EventService {

	@Value("${stathat.enabled}")
	private boolean enabled = false;
	
	@Value("${stathat.key}")
	private String key = "";
	
	@Override
	public void publishMessage(QPEvent event) throws Exception {
		
		switch(PublicEvents.valueOf(event.getEventName())) {
		case SERVICE_POINT_OPEN:
			StatHat.ezPostCount(key, "counters_open", 1d);
			break;
		case SERVICE_POINT_CLOSE:
			StatHat.ezPostCount(key, "counters_closed", 1d);
			break;
		case USER_SERVICE_POINT_SESSION_START:
			StatHat.ezPostCount(key, "counter_session_start", 1d);
			break;
		case USER_SERVICE_POINT_SESSION_END:
			StatHat.ezPostCount(key, "counter_session_end", 1d);
			break;
		case USER_SESSION_START:
			StatHat.ezPostCount(key, "session_start", 1d);
			break;
		case USER_SESSION_END:
			StatHat.ezPostCount(key, "session_end", 1d);
			break;
		case VISIT_CREATE:
			StatHat.ezPostCount(key, "visit_create", 1d);
			break;
		case VISIT_REMOVE:
			StatHat.ezPostCount(key, "visit_remove", 1d);
			break;
		case VISIT_CALL:
			StatHat.ezPostCount(key, "visit_call", 1d);
			StatHat.ezPostValue(key, "waiting_time", (Double) event.getParameter("waitingTime"));
			break;
		case VISIT_END:
		case VISIT_NOSHOW:
			StatHat.ezPostCount(key, "visit_end", 1d);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

}
