package org.ying.book.dto.systemSettings;

import lombok.Data;

@Data
public class SystemSettingDto {
    Integer MAX_BORROW_SIZE;
    //    违约上限次数
    Integer MAX_OVERDUE_TIMES;
    //    默认借阅天数
    Integer DEFAULT_BORROW_DAYS;
    //    最大借阅天数
    Integer MAX_BORROW_DAYS;
    //    验证码有效时间
    Integer CAPTCHA_EXPIRE_TIME;
    //    邀请码有效时间
    Integer INVITATION_EXPIRE_TIME;
    //    邀请注册地址
    String INVITATION_URL;
}
