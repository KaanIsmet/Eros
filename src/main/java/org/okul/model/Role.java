package org.okul.model;

public enum Role {

    USER,
    ASSISTANT;

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }

}
