package org.kelava.examples.relfection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Mopex {

	public static Method getSupportedMethod(Class<?> cls, String name, Class<?>[] paramType) {
		Objects.requireNonNull(cls, "Class must be non null");
		try {
			return cls.getDeclaredMethod(name, paramType);
		} catch (NoSuchMethodException e) {
			return getSupportedMethod(cls.getSuperclass(), name, paramType);
		}
	}

	public static Field[] getInstanceVariables(Object obj) {
		Class<?> cls = obj.getClass();
		List<Field> accum = new ArrayList<>();
		while (cls != null) {
			accum.addAll(Arrays.asList(cls.getDeclaredFields()).stream()
					.filter(f -> !Modifier.isStatic(f.getModifiers())).collect(Collectors.toUnmodifiableList()));
		}
		return accum.toArray(new Field[] {});
	}
}
