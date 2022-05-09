package com.guavapay.model.type;

public enum RoleType {

    USER(1L),
    ADMIN(2L),
    EMPLOYEE(3L);

    private Long value;

    RoleType(Long value) {
        this.value = value;
    }

    public Long value() {
        return this.value;
    }
}
