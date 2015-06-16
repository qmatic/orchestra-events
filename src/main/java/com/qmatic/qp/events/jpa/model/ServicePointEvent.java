package com.qmatic.qp.events.jpa.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "sp_events")
public class ServicePointEvent {

	@EmbeddedId
	private EventId id;
	
	@Column(name = "branch_name")
	private String branchName;
	
	@Column(name = "work_profile_id")
	private Integer workProfileId;
	
	@Column(name = "work_profile_name")
	private String workProfileName;
	
	@Column(name = "service_point_name")
	private String servicePointName;

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

	public Integer getWorkProfileId() {
		return workProfileId;
	}

	public void setWorkProfileId(Integer workProfileId) {
		this.workProfileId = workProfileId;
	}

	public String getWorkProfileName() {
		return workProfileName;
	}

	public void setWorkProfileName(String workProfileName) {
		this.workProfileName = workProfileName;
	}

	public String getServicePointName() {
		return servicePointName;
	}

	public void setServicePointName(String servicePointName) {
		this.servicePointName = servicePointName;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}
}
