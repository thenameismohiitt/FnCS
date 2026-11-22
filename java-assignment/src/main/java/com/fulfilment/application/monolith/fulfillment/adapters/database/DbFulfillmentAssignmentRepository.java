package com.fulfilment.application.monolith.fulfillment.adapters.database;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
class DbFulfillmentAssignmentRepository implements PanacheRepository<DbFulfillmentAssignment> {}
