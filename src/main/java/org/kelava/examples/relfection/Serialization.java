package org.kelava.examples.relfection;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;

public class Serialization {

	public static Document serializeObject(Object source) throws IllegalArgumentException, IllegalAccessException {
		return serializeHelper(source, new Document(new Element("serialized")), new IdentityHashMap<>());
	}

	private static Element serializeVariable(Class<?> fieldType, Object child, Document target,
			Map<Object, String> table) throws IllegalArgumentException, IllegalAccessException {
		if (child == null) {
			return new Element("null");
		} else if (!fieldType.isPrimitive()) {
			Element reference = new Element("reference");
			if (table.containsKey(child)) {
				reference.setText(table.get(child).toString());
			} else {
				reference.setText(Integer.toString(table.size()));
				serializeHelper(child, target, table);
			}
			return reference;
		} else {
			Element value = new Element("value");
			value.setText(child.toString());
			return value;
		}
	}

	public static Document serializeHelper(Object source, Document target, Map<Object, String> table)
			throws IllegalArgumentException, IllegalAccessException {
		String id = Integer.toString(table.size());
		table.put(source, id);
		Class<?> sourceClass = source.getClass();
		Element oElt = new Element("object");
		oElt.setAttribute("class", sourceClass.getName());
		oElt.setAttribute("id", id);
		target.getRootElement().addContent(oElt);
		if (!sourceClass.isArray()) {
			Field[] fields = Mopex.getInstanceVariables(sourceClass);
			for (Field field : fields) {
				if (!Modifier.isPublic(field.getModifiers())) {
					field.setAccessible(true);
				}
				Element fElt = new Element("field");
				fElt.setAttribute("name", field.getName());
				fElt.setAttribute("declaringClass", field.getDeclaringClass().getName());

				Class<?> fieldType = field.getType();
				Object child = field.get(source);

				if (Modifier.isTransient(field.getModifiers())) {
					child = null;
				}
				fElt.addContent(serializeVariable(fieldType, child, target, table));
				oElt.addContent(fElt);
			}
		} else {
			Class<?> componentType = sourceClass.getComponentType();
			int length = Array.getLength(source);
			oElt.setAttribute("length", Integer.toString(length));
			for (int i = 0; i < length; i++) {
				oElt.addContent(serializeVariable(componentType, Array.get(source, i), target, table));
			}
		}
		return target;
	}
	
	public static Object deserializeObject(Document source) {
		List<Element> objList = source.getRootElement().getChildren();
		Map<String, Object> table = new HashMap<>();
		createInstances(table, objList);
		assignFieldValues(table, objList);
		return table.get("0");
	}
	
	private static void assignFieldValues(Map<String, Object> table, List<Element> objList) {
		// TODO Auto-generated method stub
		
	}

	private static void createInstances(Map<String, Object> table, List<Element> objList) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		for (Element element : objList) {
			Class<?> cls = Class.forName(element.getAttributeValue("class"));
			Object instance = null;
			if (!cls.isArray()) {
				Constructor<?> constructor = cls.getDeclaredConstructor();
				if (!Modifier.isPublic(constructor.getModifiers())) {
					constructor.setAccessible(true);
				}
				instance = constructor.newInstance();
			}
			else {
				instance = Array.newInstance(cls.getComponentType(), Integer.parseInt(element.getAttributeValue("length")));
			}
			table.put(element.getAttributeValue("id"), instance);
		}
	}

}
