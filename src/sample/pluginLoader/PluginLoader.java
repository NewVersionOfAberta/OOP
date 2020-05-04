//package sample.pluginLoader;
//
//import plugin.IPlugin;
//
//import java.io.File;
//import java.io.FileFilter;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.net.URLClassLoader;
//
//public class PluginLoader {
//    private void loadPlugins() {
//        File pluginDir = new File("plugins");
//
//        File[] jars = pluginDir.listFiles(file -> file.isFile() && file.getName().endsWith(".jar"));
//        Class[] pluginClasses = new Class[jars.length];
//        for (int i = 0; i < jars.length; i++)
//        try {
//            URL jarURL = jars[i].toURI().toURL();
//            URLClassLoader classLoader = new URLClassLoader(new URL[]{jarURL});
//            pluginClasses[i] = classLoader.loadClass("com.test.HelloPlugin");
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        for (Class clazz : pluginClasses) {
//            try {
//                IPlugin instance = (IPlugin) clazz.newInstance();
//                byte[] s = instance.processText("Hello");
//                System.out.println(s);
//            } catch (InstantiationException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
//
