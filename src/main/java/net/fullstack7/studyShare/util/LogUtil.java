package net.fullstack7.studyShare.util;

import java.util.Arrays;
import java.util.List;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class LogUtil {
    public static void logLine() {
        log.info("--------------------------------");
    }

    public static void info(String message) {
        log.info("---------------- {} ----------------", message);
    }

    public static void info(String message, Object obj) {
        log.info("---------------- message : {} obj : {} ----------------", message, obj.toString());
    }

    public static void info(String message, Object... objs) {
        log.info("---------------- message : {} obj : {} ----------------", message, Arrays.toString(objs));
    }
    
    public static void info(Object obj) {
        log.info("---------------- obj : {} ----------------", obj.toString());
    }

    public static void info(Object... objs) {
        log.info("---------------- obj : {} ----------------", Arrays.toString(objs));
    }

    public static void info(String message, boolean bool) {
        log.info("---------------- message : {} bool : {} ----------------", message, bool);
    }

    public static void info(String message, int num) {
        log.info("---------------- message : {} num : {} ----------------", message, num);
    }

    public static void info(String message, long num) {
        log.info("---------------- message : {} num : {} ----------------", message, num);
    }

    public static void info(String message, List<Object> list) {
        log.info("---------------- message : {} list : {} ----------------", message, list.toString());
    }

    public static void error(String message, Throwable e) {
        log.error("---------------- ERROR: {} ----------------", message);
        log.error("Stack trace:", e);
    }
}
