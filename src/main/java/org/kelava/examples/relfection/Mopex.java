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

	public static Field[] getInstanceVariables(Class<?> cls) {
		List<Field> accum = new ArrayList<>();
		while (cls != null) {
			accum.addAll(Arrays.stream(cls.getDeclaredFields())
					.filter(f -> !Modifier.isStatic(f.getModifiers())).collect(Collectors.toUnmodifiableList()));
			cls = cls.getSuperclass();
		}
		return accum.toArray(new Field[] {});
	}
}
