package com.fulfilment.application.monolith.stores;

public class StoreChangedEvent {

    public enum Type {
        CREATED,
        UPDATED
    }

    private final Store store;
    private final Type type;

    public StoreChangedEvent(Store store, Type type) {
        this.store = store;
        this.type = type;
    }

    public Store store() {
        return store;
    }

    public Type type() {
        return type;
    }
}