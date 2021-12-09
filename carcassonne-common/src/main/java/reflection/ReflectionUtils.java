package reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtils {
    public static Object getField(Object obj, String fieldName) throws Exception {
        Field field = findField(obj.getClass(), fieldName);
        field.setAccessible(true);
        return field.get(obj);
    }

    public static void setField(Object obj, String fieldName, Object value) throws Exception {
        Field field = findField(obj.getClass(), fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }

    public static void callMethod(Object obj, String methodName, Object... args) throws Exception {
        Class<?>[] argTypes = new Class<?>[args.length];

        for (int i = 0; i < args.length; i++) {
            argTypes[i] = args[i].getClass();
        }

        Method method = findMethod(obj.getClass(), methodName, argTypes);
        method.setAccessible(true);
        method.invoke(obj, args);
    }

    private static Method findMethod(Class<?> clazz, String methodName, Class<?>... argTypes) throws Exception {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(methodName) && matchTypes(method.getParameterTypes(), argTypes)) {
                return method;
            }
        }

        if (clazz.getSuperclass() != null) {
            return findMethod(clazz.getSuperclass(), methodName, argTypes);
        }

        throw new Exception("Method not found");
    }

    private static boolean matchTypes(Class<?>[] types, Class<?>[] argTypes) {
        if (types.length != argTypes.length) {
            return false;
        }

        for (int i = 0; i < types.length; i++) {
            if (!types[i].isAssignableFrom(argTypes[i])) {
                return false;
            }
        }

        return true;
    }

    private static Field findField(Class<?> clazz, String name) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getName().equals(name)) {
                return field;
            }
        }

        if (clazz.getSuperclass() != null) {
            return findField(clazz.getSuperclass(), name);
        }

        return null;
    }
}
