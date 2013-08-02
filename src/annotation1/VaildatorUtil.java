package annotation1;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class VaildatorUtil {

    public static boolean validateBean(Object bean) {
        try {
            PropertyDescriptor[] descs = 
                Introspector.getBeanInfo(bean.getClass()).getPropertyDescriptors();
            for (PropertyDescriptor desc : descs) {
                if (desc.getReadMethod() != null) {
                    Method readMethod = desc.getReadMethod();
                    Object value = readMethod.invoke(bean, new Object[0]);
                    Class returnClass = readMethod.getReturnType();
                    Annotation[] annotations = readMethod.getAnnotations();
                    for (Annotation annotation : annotations) {
                        if (!validate(annotation, value, returnClass)) {
                            return false;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    
    private static boolean validate(Annotation annotation, Object value, Class valueClass) {
        if (value == null) {
            return true;
        }
        if (annotation.annotationType().equals(MaxLength.class)) {
            MaxLength maxLength = (MaxLength) annotation;
            int len = maxLength.value();
            String str = value.toString();
            if (str.length() > len){
                return false;
            } else {
                return true;
            }
        } else if (annotation.annotationType().equals(MinLength.class)) {
            MinLength minLength = (MinLength) annotation;
            int len = minLength.value();
            String str = value.toString();
            if (str.length() < len){
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
}
