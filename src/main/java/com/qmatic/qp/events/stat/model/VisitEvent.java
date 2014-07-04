package com.qmatic.qp.events.stat.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "visit_events")
public class VisitEvent {

	@EmbeddedId
	private EventId id;
	
	@Column(name = "branch_name")
	@NotNull
	private String branchName;
	
	@Column(name = "service_id")
	private Integer serviceId;
	
	@Column(name = "service_name")
	private String serviceName;
	
	@Column(name = "queue_id")
	private Long queueId;
	
	@Column(name = "queue_name")
	private String queueName;
	
	@Column(name = "tx_target")
	private Integer transactionTarget;
	
	@Column(name = "wt_sla")
	private Integer waitingSla;
	
	@Column(name = "wt_time")
	private Integer waitingTime;
	
	@Column(name = "tx_time")
	private Integer transationTime;

	@Column(name = "event")
	@NotNull
	private String event;
	
	public EventId getId() {
		return id;
	}
	
	public void setId(EventId id) {
		this.id = id;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public Long getQueueId() {
		return queueId;
	}

	public void setQueueId(Long queueId) {
		this.queueId = queueId;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public Integer getTransactionTarget() {
		return transactionTarget;
	}

	public void setTransactionTarget(Integer transactionTarget) {
		this.transactionTarget = transactionTarget;
	}

	public Integer getWaitingSla() {
		return waitingSla;
	}

	public void setWaitingSla(Integer waitingSla) {
		this.waitingSla = waitingSla;
	}

	public Integer getWaitingTime() {
		return waitingTime;
	}

	public void setWaitingTime(Integer waitingTime) {
		this.waitingTime = waitingTime;
	}

	public Integer getTransationTime() {
		return transationTime;
	}

	public void setTransationTime(Integer transationTime) {
		this.transationTime = transationTime;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}
	
	@Override
	public String toString() {
		String json = "{";
		json += "\"id\": " + id.getId() + ",";
		json += "\"ts\": \"" + id.getTs().toString() + "\",";
		json += "\"branchId\": " + id.getBranchId() + ",";
		json += "\"branchName\": \"" + branchName + "\",";
		json += "\"serviceId\": " + serviceId + ",";
		json += "\"serviceName\": \"" + serviceName + "\",";
		json += "\"queueId\": " + queueId + ",";
		json += "\"queueName\": \"" + queueName + "\",";
		json += "\"transactionTarget\": " + transactionTarget + ",";
		json += "\"waitingSla\": " + waitingSla + ",";
		json += "\"waitingTime\": " + waitingTime + ",";
		json += "\"transactionTime\": " + transationTime + ",";
		json += "\"event\": \"" + event + "\"";
		json += "}";
		
		return json;
	}
	
}

