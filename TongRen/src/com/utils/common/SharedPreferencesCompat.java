package com.utils.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Reflection utils to call {@link SharedPreferences.Editor} apply() when
 * possible, falling back to commit when apply isn't available.
 */
public final class SharedPreferencesCompat {

    private final static Method mApplyMethod = findApplyMethod();

    /**
     * @return The apply() method from {@link SharedPreferences.Editor}.
     */
    private final static Method findApplyMethod() {
        try {
            final Class<Editor> class1 = SharedPreferences.Editor.class;
            return class1.getMethod("apply");
        } catch (final NoSuchMethodException ignored) {
        }
        return null;
    }

    /**
     * @param editor The {@link SharedPreferences.Editor} to use.
     */
    public static void apply(final SharedPreferences.Editor editor) {
        if (mApplyMethod != null) {
            try {
                mApplyMethod.invoke(editor);
                return;
            } catch (final InvocationTargetException ignored) {
            } catch (final IllegalAccessException ignored) {
            }
        }
        editor.commit();
    }
}
