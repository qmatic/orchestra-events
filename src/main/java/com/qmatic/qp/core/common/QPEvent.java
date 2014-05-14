/*
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR 
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE 
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, 
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE 
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.qmatic.qp.core.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.qmatic.qp.api.event.Event;
import com.qmatic.qp.api.event.EventType;
import com.qmatic.qp.exception.QRuntimeException;

/**
 * 
 * QMATIC Platform implementation of a non persisted runtime {@link Event}.
 * 
 * The QPEvent should NOT be used between Devices, since it does not have a deviceId field.
 * 
 * Instead use {@link com.qmatic.qp.api.device.DeviceEvent} for events to Devices.
 * 
 * 
 * @author fregus
 */
public class QPEvent implements Event {

    private static final long serialVersionUID = 1L;

    protected String eventName;
    protected String eventTime;
    protected EventType eventType;
    protected Long unitId;
    protected Map<String, Serializable> parameters = new HashMap<String, Serializable>(5);

    /** Creates a hollow QPEvent */
    public QPEvent() {}

    /** Creates a QPEvent from the given Event **/
    public QPEvent(Event event) {
        this.eventName = event.getEventName();
        this.eventTime = event.getEventTime();
        this.eventType = event.getEventType();
        this.parameters = event.getParameters();
        this.unitId = event.getUnitId();
        if (event.getParameters() != null) {
            for (String key : event.getParameters().keySet()) {
                this.setParameter(key, event.getParameters().get(key));
            }
        }
    }

    /**
     * Constructor used to define Event templates
     * 
     * @param eventName
     * @param eventType
     */
    public QPEvent(String eventName, EventType eventType) {
        this.eventName = eventName;
        this.eventType = eventType;
        this.setEventType(eventType);
    }

    /**
     * Constructor with everything needed included.
     *
     * @param eventTime
     * @param eventType
     * @param unitId
     * @param parameters
     */
    public QPEvent(String eventTime, EventType eventType, Long unitId, Map<String, Serializable> parameters) {
        this.eventTime = eventTime;
        this.unitId = unitId;
        this.parameters = parameters;
        this.setEventType(eventType);
    }

    /**
     * Constructor with everything included. Normally the eventName isn't necessary
     * 
     * @param eventName
     * @param eventTime
     * @param eventType
     * @param unitId
     */
    public QPEvent(String eventName, EventType eventType, Long unitId, String eventTime) {
        this.eventName = eventName;
        this.eventTime = eventTime;
        this.unitId = unitId;
        this.setEventType(eventType);
    }

    @Override
    public EventType getEventType() {
        return this.eventType;
    }

    @Override
    public String getEventName() {
        return this.eventName;
    }

    @Override
    public String getEventTime() {
        return this.eventTime;
    }

    @Override
    public Long getUnitId() {
        return this.unitId;
    }

    @Override
    public Serializable getParameter(String parameterName) {
        return this.getParameters().get(parameterName);
    }

    @Override
    @JsonIgnore
    public Set<String> getParameterNames() {
        return this.getParameters().keySet();
    }

    public void setParameter(String parameterName, Serializable value) {
        this.getParameters().put(parameterName, value);
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public void setEventType(EventType eventType) {
        if (EventType.DEVICE.equals(eventType))
            throw new QRuntimeException("QPEvent cannot handle EventType.DEVICE. Use com.qmatic.qp.api.device.DeviceEvent instead");
        this.eventType = eventType;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public void setParameters(Map<String, Serializable> parameters) {
        this.parameters = parameters;
    }

    @Override
    public Map<String, Serializable> getParameters() {
        return this.parameters;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{ \"event\":\"");
        sb.append(this.eventName).append("\", \"type\":\"");
        sb.append(this.eventType != null ? this.eventType.name() : "null");
        sb.append("\", \"ts\":\"");
        sb.append(this.eventTime != null ? this.eventTime : "null");
        sb.append("\", \"uid\":\"");
        sb.append(this.unitId != null ? this.unitId : "null");
        sb.append("\" }");
        return sb.toString();
    }
}
