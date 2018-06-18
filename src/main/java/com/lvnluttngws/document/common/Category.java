package com.lvnluttngws.document.common;

public enum Category {
    DS(1), HS(2), QT(3);

    private Integer value;

    private Category(Integer value) {
        this.value = value;
    }

    public Integer getInt() {
        return this.value;
    }
}
