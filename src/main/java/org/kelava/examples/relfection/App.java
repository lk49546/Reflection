package org.kelava.examples.relfection;

import java.lang.reflect.Array;

import org.jdom2.Document;
import org.jdom2.output.XMLOutputter;

/**
 * Hello world!
 */

public class App {
	public static void main(String[] args) {
		Animal panda1 = new Animal("Tian Tian", "male", "Ailuropoda melanoleuca", 271);
		Animal panda2 = new Animal("Mei Xiang", "female", "Ailuropoda melanoleuca", 221);
		Zoo national = new Zoo("National Zoological Park", "Washington, D.C.");
		national.add(panda1);
		national.add(panda2);
		try {
			XMLOutputter out = new XMLOutputter();
			Document d = Serialization.serializeObject(national);
			out.output(d, System.out);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		Array.newInstance(String.class, new int[] {2, 3});
		Array.newInstance(String[].class, new int[] {2, 3});
	}
}
