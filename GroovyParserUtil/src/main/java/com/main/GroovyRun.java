package com.main;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import groovy.lang.GroovyClassLoader;

public class GroovyRun {

	public static void main(String[] args) throws Exception {
		String filename = "src/main/resources/something.txt";
		String groovyFile = "src/main/resources/StandardParser.groovy";
		
		@SuppressWarnings("resource")
		GroovyClassLoader classLoader = new GroovyClassLoader();
		Class<?> groovyClass = classLoader.parseClass(new File(groovyFile));

		Object groovyObject = groovyClass.newInstance();
		Method groovyMethod = groovyClass.getMethod("reverseString", String.class);

		try {
			List<String> msgs = Files.readAllLines(Paths.get(filename));
			for (String msg : msgs) {
				String result = (String) groovyMethod.invoke(groovyObject, msg);
			}
		} catch (Exception e) {
			System.out.println(e);
		}

	}

}
