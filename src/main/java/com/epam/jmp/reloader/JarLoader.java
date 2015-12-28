package com.epam.jmp.reloader;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarLoader {

    private static final Logger LOGGER = Logger.getLogger(JarLoader.class);

    private JarFile jarFile;
    private URLClassLoader urlClassLoader;

    public JarLoader(String path) throws IOException {
        jarFile = new JarFile(path);
        URL[] urls = {new URL("jar:file:" + path + "!/")};
        urlClassLoader = URLClassLoader.newInstance(urls);
    }

    public void loadClasses() throws ClassNotFoundException {
        Enumeration e = jarFile.entries();
        while (e.hasMoreElements()) {
            JarEntry je = (JarEntry) e.nextElement();
            if(je.isDirectory() || !je.getName().endsWith(".class")){
                continue;
            }
            String className = je.getName().substring(0, je.getName().length() - 6); // -6 to strip .class at the end
            className = className.replace('/', '.');
            urlClassLoader.loadClass(className);
            LOGGER.debug("Loaded class: " + className);
        }
    }
}
