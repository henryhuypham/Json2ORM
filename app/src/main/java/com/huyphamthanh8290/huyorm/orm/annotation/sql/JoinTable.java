package com.huyphamthanh8290.huyorm.orm.annotation.sql;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface JoinTable {
    String name();
    String joinColumn();
    String localColumn();
    JoinType joinType();
}
