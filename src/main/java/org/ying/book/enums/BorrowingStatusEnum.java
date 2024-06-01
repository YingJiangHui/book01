package org.ying.book.enums;

public enum BorrowingStatusEnum {
    OVERDUE_RETURNED("OVERDUE_RETURNED"),
    RETURNED("RETURNED"),
    OVERDUE_NOT_RETURNED("OVERDUE_NOT_RETURNED"),
    NOT_RETURNED("NOT_RETURNED");

    private String status;

    BorrowingStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
