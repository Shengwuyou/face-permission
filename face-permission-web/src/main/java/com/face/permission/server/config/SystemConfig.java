package com.face.permission.server.config;

/**
 * @Description
 * @Author xuyizhong
 * @Date 2019-08-17 13:41
 */
public class SystemConfig {
    /**
     * 本地
     */
    public static String LOCAL = "local";
    /**
     * 开发
     */
    public static String DEV = "dev";
    /**
     * 测试
     */
    public static String TEST = "test";
    /**
     * 生成
     */
    public static String PRO = "pro";

    /**
     * 当前系统的环境
     */
    private static String deployMode= LOCAL;

    public static boolean isProMode(){
        if (PRO.equals(deployMode)){
            return true;
        }
        return false;
    }

    public static String getDeployMode() {
        return deployMode;
    }

    public static void setDeployMode(String deployMode) {
        SystemConfig.deployMode = deployMode;
    }

    public String getLOCAL() {
        return LOCAL;
    }

    public void setLOCAL(String LOCAL) {
        this.LOCAL = LOCAL;
    }

    public String getDEV() {
        return DEV;
    }

    public void setDEV(String DEV) {
        this.DEV = DEV;
    }

    public String getTEST() {
        return TEST;
    }

    public void setTEST(String TEST) {
        this.TEST = TEST;
    }

    public String getPRO() {
        return PRO;
    }

    public void setPRO(String PRO) {
        this.PRO = PRO;
    }
}
