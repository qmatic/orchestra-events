package com.qmatic.qp.events.stat.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class EventId implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "id")
	@NotNull
	private Long id;
	
	@Column(name = "ts")
	@NotNull
	private Timestamp ts;
	
	@Column(name = "branch_id")
	@NotNull
	private Integer branchId;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public Timestamp getTs() {
		return ts;
	}

	public void setTs(Timestamp ts) {
		this.ts = ts;
	}
	
	public Integer getBranchId() {
		return branchId;
	}

	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}

	@Override
    public boolean equals(Object obj) {
		if(obj instanceof EventId) {
			EventId eventId = (EventId) obj;
			
			if(eventId.getId() != id)
				return false;
			
			if(eventId.getTs() != ts)
				return false;
			
			if(eventId.getBranchId() != branchId)
				return false;
			
			return true;
		}
		
		return false;
	}
	
	@Override
    public int hashCode() {
		return id.hashCode() + ts.hashCode() + branchId.hashCode();
	}
}
