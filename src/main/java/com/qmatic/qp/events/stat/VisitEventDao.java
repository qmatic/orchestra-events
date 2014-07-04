package com.qmatic.qp.events.stat;

import org.springframework.data.repository.CrudRepository;

import com.qmatic.qp.events.stat.model.EventId;
import com.qmatic.qp.events.stat.model.VisitEvent;

public interface VisitEventDao extends CrudRepository<VisitEvent, EventId> {

}
