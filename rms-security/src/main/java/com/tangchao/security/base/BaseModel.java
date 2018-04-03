package com.tangchao.security.base;

import lombok.Data;

import javax.persistence.Column;
import java.time.LocalTime;

/**
 * @author wangganggang
 * @date 2017年09月07日 下午8:41
 */
@Data
public class BaseModel {

    @Column(name = "del_flag")
    private boolean delFlag;

    @Column(name = "create_time")
    private LocalTime createTime;

    @Column(name = "create_user_id")
    private String createUserId;

    @Column(name = "update_time")
    private LocalTime updateTime;

    @Column(name = "update_user_id")
    private String updateUserId;

    @Column(name = "remark")
    private String remark;
}
