package com.mavenr.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author mavenr
 * @Classname ClassTypeEnum
 * @Description TODO
 * @Date 2022/8/19 11:48
 */
@AllArgsConstructor
@Getter
public enum ClassTypeEnum {
    ENTITY("Entity"),
    MAPPER("Mapper"),
    SERVICE("Service"),
    VO("Vo"),
    BO("Bo");

    private String classType;

}
