package org.ying.book.enums;

public enum BorrowingStatusEnum {
//    逾期已归还
    OVERDUE_RETURNED("OVERDUE_RETURNED"),
//    已归还
    RETURNED("RETURNED"),
//    逾期未归还
    OVERDUE_NOT_RETURNED("OVERDUE_NOT_RETURNED"),
//    未归还
    NOT_RETURNED("NOT_RETURNED");

    private String status;

    BorrowingStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
