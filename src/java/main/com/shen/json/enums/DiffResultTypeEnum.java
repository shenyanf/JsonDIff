package com.shen.json.enums;

import java.util.Arrays;
import java.util.List;

public enum DiffResultTypeEnum {
    ADDNEWKEY(100, "json1 add new key"), DELKEY(200, "json1 del key"), DIFFERENCE(300,
            "json1 not equals json2"), LENGTHNOTEQUAL(301, "length is not equal");
    private int value;
    private String desc;

    DiffResultTypeEnum(int value, String desc) {
        this.setValue(value);
        this.setDesc(desc);
    }

    public static DiffResultTypeEnum getDiffResultTypeEnumByValue(int value) {
        List<DiffResultTypeEnum> diffResultTypeEnums = Arrays.asList(DiffResultTypeEnum.values());
        DiffResultTypeEnum diffResultTypeEnum = null;

        for (DiffResultTypeEnum d : diffResultTypeEnums) {
            if (d.getValue() == value) {
                diffResultTypeEnum = d;
            }
        }
        return diffResultTypeEnum;
    }

    public static DiffResultTypeEnum getDiffResultTypeEnumByDesc(String desc) {
        List<DiffResultTypeEnum> diffResultTypeEnums = Arrays.asList(DiffResultTypeEnum.values());
        DiffResultTypeEnum diffResultTypeEnum = null;

        for (DiffResultTypeEnum d : diffResultTypeEnums) {
            if (d.getDesc().equals(desc)) {
                diffResultTypeEnum = d;
            }
        }
        return diffResultTypeEnum;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}