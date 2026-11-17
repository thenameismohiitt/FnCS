package com.fulfilment.application.monolith.stores;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.TransactionPhase;
import jakarta.inject.Inject;

@ApplicationScoped
public class StoreChangedObserver {

    @Inject LegacyStoreManagerGateway legacyStoreManagerGateway;

    void onStoreChanged(@Observes(during = TransactionPhase.AFTER_SUCCESS) StoreChangedEvent event) {
        switch (event.type()) {
            case CREATED:
                legacyStoreManagerGateway.createStoreOnLegacySystem(event.store());
                break;
            case UPDATED:
                legacyStoreManagerGateway.updateStoreOnLegacySystem(event.store());
                break;
        }
    }
}