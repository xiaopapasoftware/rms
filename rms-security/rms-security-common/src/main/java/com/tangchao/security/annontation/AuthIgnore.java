package com.tangchao.security.annontation;

import java.lang.annotation.*;

/**
 * @author wangganggang
 * @date 2017年07月21日 下午1:51
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthIgnore {
}
