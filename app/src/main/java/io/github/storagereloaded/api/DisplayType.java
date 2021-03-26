package io.github.storagereloaded.api;

public enum DisplayType {

    UNKNOWN(0), RANGE(1), EAN13(2);

    private int id;

    private DisplayType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
