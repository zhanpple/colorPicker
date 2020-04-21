package com.zmp.widget.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * @author zmp
 */
public class ColorSPUtils {

        /**
         * 保存在手机里面的文件名
         */
        private static final String FILE_NAME = "color_sp";

        private static final String TAG = "ColorSPUtils";

        public static final String COLOR_KEY = "colors";

        private static SharedPreferences sp;

        public static void saveString(Context context, String key, String value) {
                if (null == context) {
                        return;
                }
                if (sp == null)
                        sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
                sp.edit().putString(key, value).apply();
        }

        public static String getString(Context context, String key, String defValue) {
                if (sp == null)
                        sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
                return sp.getString(key, defValue);
        }

        public static void saveLong(Context context, String key, long value) {
                if (null == context) {
                        return;
                }
                if (sp == null)
                        sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
                sp.edit().putLong(key, value).apply();
        }

        public static long getLong(Context context, String key, long defValue) {
                if (sp == null)
                        sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
                return sp.getLong(key, defValue);
        }

        public static void saveInt(Context context, String key, int value) {
                if (null == context) {
                        return;
                }
                if (sp == null)
                        sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
                sp.edit().putInt(key, value).apply();
        }

        public static int getInt(Context context, String key, int defValue) {
                if (sp == null)
                        sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
                return sp.getInt(key, defValue);
        }

        public static void saveBoolean(Context context, String key, boolean value) {
                if (sp == null)
                        sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
                sp.edit().putBoolean(key, value).apply();
        }

        public static boolean getBoolean(Context context, String key, boolean defValue) {
                if (sp == null)
                        sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
                return sp.getBoolean(key, defValue);
        }

        /**
         * 移除某个key值已经对应的值
         *
         * @param context context
         * @param key     key
         */
        public static void remove(Context context, String key) {
                if (sp == null)
                        sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
                sp.edit().remove(key).apply();
        }

        /**
         * 清除所有数据
         *
         * @param context context
         */
        public static void clear(Context context) {
                if (sp == null)
                        sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
                sp.edit().clear().apply();
        }

        /**
         * 查询某个key是否已经存在.
         *
         * @param context context
         * @param key     key
         * @return contains
         */
        public static boolean contains(Context context, String key) {
                if (sp == null)
                        sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
                return sp.contains(key);
        }

        /**
         * 返回所有的键值对
         *
         * @param context context
         * @return getAll
         */
        public static Map<String, ?> getAll(Context context) {
                if (sp == null)
                        sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
                return sp.getAll();
        }
}
