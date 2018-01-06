package com.github.break27.ClassUtil;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class JarClassLoader {
	
	public static Class<?> loadFromJar(File jarpath, String className) throws MalformedURLException, 
																			  IllegalAccessException, 
																			  IllegalArgumentException, 
																			  InvocationTargetException, 
																			  NoSuchMethodException, 
																			  SecurityException, 
																			  ClassNotFoundException {
		
		if(jarpath != null) {
			URL url= jarpath.toURI().toURL();
			URLClassLoader urlClassLoader= (URLClassLoader) ClassLoader.getSystemClassLoader(); 
			Method add = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class }); 
			add.setAccessible(true);  
			add.invoke(urlClassLoader, new Object[] {url });
			Class<?> c = Class.forName(className);
			
			return c;
		} else {
			throw new NullPointerException();
		}
	}
	
	private static void invokeMethodFromClass(File jarpath, String className, String methodName, boolean b, Object[] o) {
		try {
			Class<?> clazz = loadFromJar(jarpath, className);
			if(clazz != null) {
				Object obj = clazz.newInstance();
				Method method;
				if(b) {
					Class<?> c[];
					int len = o.length;
		            c = new Class[len];
		            for(int i = 0; i < len; i++){
		                c[i] = o[i].getClass();
		            }
		            method = clazz.getDeclaredMethod(methodName, c);
					method.invoke(obj, o);
				} else {
					method = clazz.getDeclaredMethod(methodName, null);
					method.invoke(obj, null);
				}
			}
		} catch(MalformedURLException
				| IllegalAccessException
				| IllegalArgumentException
				| InvocationTargetException
				| NoSuchMethodException
				| SecurityException
				| ClassNotFoundException 
				| InstantiationException
				e) {
			e.printStackTrace();
		}
	}
	
	public static void invokeMethod(File jarpath, String className, String methodName) {
		invokeMethodFromClass(jarpath, className, methodName, false, null);
	}
	
	public static void invokeMethodWithArgs(File jarpath, String className, String methodName, Object[] o) {
		invokeMethodFromClass(jarpath, className, methodName, true, o);
	}
}
