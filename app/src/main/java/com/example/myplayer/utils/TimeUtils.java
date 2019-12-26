package com.example.myplayer.utils;

/**
 * author:张智富  Email:464930073@qq.com
 * date:2019/12/11
 * TODO:时间转换工具类
 */
public class TimeUtils {

    /**
     * TODO:毫秒转H:M:S
     * @param ms
     * @return H:M:S
     */
    public String msToHMS(long ms){
        String HMStime;
        ms /= 1000;
        long hour = ms/3600;
        long mint = (ms%3600)/60;
        long sec = ms%60;
        HMStime = hour + ":" + mint + ":" + sec;
        return HMStime;
    }
    /**
     * TODO:毫秒转M:S
     * @param ms
     * @return M:S
     */
    public String msToMS(long ms){
        String MStime;
        ms /= 1000;
        long mint = ms/60;
        long sec = ms%60;
        MStime = mint + ":" + sec;
        return MStime;
    }

}
