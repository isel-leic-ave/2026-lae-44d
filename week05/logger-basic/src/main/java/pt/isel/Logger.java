package pt.isel;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Logger {

    public static void log(Appendable out, Object obj) throws Exception {
        Class<?> classJava = obj.getClass();
        out.append("Object of Type " + classJava.getSimpleName() + System.lineSeparator());
        Map<String, Object> map = new HashMap<>();
        for (Field field: classJava.getDeclaredFields()){
            field.setAccessible(true);
            map.putIfAbsent(field.getName(), field.get(obj));
        }
        for (Method mth : classJava.getDeclaredMethods()) {
            if (isGetter(mth)) {
                String propName = mth.getName().replaceFirst("^get", "");
                Object propValue = mth.invoke(obj);
                map.putIfAbsent(propName.substring(0, 1).toLowerCase() + propName.substring(1), propValue);
            }
        }
        for (String key : map.keySet()){
            out.append("  - " + key + ": " + map.get(key) + System.lineSeparator());
        }
    }

    static boolean isGetter(Method method){
        return (method.getName().startsWith("get") // starts with 'get'
                && method.getParameterCount() == 0 // There is no parameters
                && ! method.getReturnType().equals(void.class) // Returned type is not void
        );
    }
}

