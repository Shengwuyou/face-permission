package com.face.permission.common.constants.sys.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.Properties;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-08-01 10:35
 */
public enum LocalSystemOSEnum {
    MAC_OS(1, "Mac OS X", "UNKONW"),
    LINUX_OS(2, "Linux", "dmidecode -t system"),
    WINDOWS(3, "WINDOWS", "cmd /k systeminfo")
    ;


    private Integer code;

    private String name;

    private String sysSh;

    LocalSystemOSEnum(Integer code, String name, String sysSh) {
        this.code = code;
        this.name = name;
        this.sysSh = sysSh;
    }

    LocalSystemOSEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private static Integer getSystemOsCode(String name){
        if (StringUtils.isBlank(name)){
            return null;
        }
        for (LocalSystemOSEnum value : LocalSystemOSEnum.values()) {
            if (Objects.equals(name, value.getName())){
                return value.getCode();
            }
        }
        return null;
    }

    /**
     * 获取当前系统
     * @return
     */
    public static Integer getLocalSystemOsCode(){
        Properties properties = System.getProperties();
        String osName = properties.getProperty("os.name", LocalSystemOSEnum.LINUX_OS.getName());
        return LocalSystemOSEnum.getSystemOsCode(osName);
    }
}
