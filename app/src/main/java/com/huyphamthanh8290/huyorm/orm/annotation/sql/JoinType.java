package com.huyphamthanh8290.huyorm.orm.annotation.sql;

public enum JoinType {
    LEFT {
        @Override
        public String toString() {
            return "left";
        }
    },
    INNER {
        @Override
        public String toString() {
            return "inner";
        }
    };
}