package ua.govnojon.jesusmode.util;
import org.bukkit.Bukkit;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Удобная рефлекция объекта.
 * При создании указывайте объект:
 *
 *   MyObject object = new MyObject(Object object);
 */
public class MyObject {

    private Object object;

    public static Object getFieldValue(Class clazz, Object object, String name) {
        try {
            Object get = null;
            Field field = clazz.getDeclaredField(name);
            if (!field.isAccessible()) {
                field.setAccessible(true);
                get = field.get(object);
                field.setAccessible(false);
            } else {
                get = field.get(object);
            }
            return get;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Получаем из готового объекта
     * @param object
     */
    public MyObject(Object object) {
        this.object = object;
    }

    /**
     * Создание нового объекта
     * @param clazz
     * @param objects (Можно указывать сразу MyObject, оно само достанет)
     */
    public MyObject(String clazz, Object... objects) {
        try {
            Class c = Class.forName(clazz);
            this.object = this.create(c,objects);
        } catch (ClassNotFoundException e) {
            throw new NullPointerException("Класс '" + clazz + "' не найден.");
        }
    }

    /**
     * Создание нового объекта
     * @param clazz
     * @param objects (Можно указывать сразу MyObject, оно само достанет)
     */
    public MyObject(Class clazz, Object... objects) {
        this.object = this.create(clazz,objects);
    }

    /**
     * Получай переменные.
     * Не нужно беспокоиться о приватности пеменной!
     * Использование:
     *
     *   object.getField("temp");
     *   где:
     *    * "temp" - название переменной
     *
     * @param name название переменной
     * @return MyObject этой переменной (ее можно получить через .getObject())
     */
    public MyObject getField(String name) {
        try {
            Field field = object.getClass().getDeclaredField(name);
            boolean isSetAccessible = false;
            if (!field.isAccessible()) {
                field.setAccessible(true);
                isSetAccessible = true;
            }
            Object get = field.get(this.object);
            if (isSetAccessible) {
                field.setAccessible(false);
            }
            return get == null ? null : new MyObject(get);
        } catch (Exception e) {
            Bukkit.getLogger().severe("Error in: " + this.getObject().getClass());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Установить значение переменной
     * @param name имя переменной
     * @param value значение
     */
    public void setField(String name, Object value) {
        try {
            Field field = this.object.getClass().getDeclaredField(name);
            boolean isSetAccessible = false;
            if (!field.isAccessible()) {
                field.setAccessible(true);
                isSetAccessible = true;
            }
            field.set(this.object, value instanceof MyObject ? ((MyObject)value).getObject() : value);
            if (isSetAccessible) {
                field.setAccessible(false);
            }
        } catch (Exception e) {
            System.out.println("Переменная '" + name + "' не найдена.");
            e.printStackTrace();
        }
    }

    /**
     * Вызывай методы.
     * Не нужно беспокоится о приватности метода!
     * Не нужно беспокоится о аргументах метода,
     * моя система сама все кастит и т.п.
     * Если этот метод функция, то вернет значение, иначе null.
     * Использование:
     *
     *   object.invokeMethod("exetute", true, Integer.valueOf(10));
     *   где:
     *    * "exetute" - название метода
     *    * true - (первый аргумент метода, boolean значение)
     *    * Integer.valueOf(10) - (второй аргумент метода) пример того, что неважно, что указывать (int или Integer)
     *
     * @param name название метода
     * @param args аргументы (Можно указывать сразу MyObject, оно само достанет)
     * @return если этот метод функция, то вернет что-то, иначе null
     */
    public MyObject invokeMethod(String name, Object... args) {
        try {

            Method method = null;
            if (args.length == 0) {
                method = this.object.getClass().getMethod(name);
            } else {
                this.fixArgs(args);
                for (Method m : object.getClass().getDeclaredMethods()) {
                    if (!m.getName().equals(name))
                        continue;
                    if (m.getParameterCount() != args.length)
                        continue;

                    for (int i = 0; i < m.getParameterCount(); i++) {
                        if (args[i] != null) {
                            if (!m.getParameterTypes()[i].isInstance(args[i])) {
                                continue;
                            }
                        }
                    }
                    method = m;
                    break;
                }
            }
            if (method == null) {
                throw new NullPointerException("Метод не найден.");
            }
            boolean isSetAccessible = false;
            if (!method.isAccessible()) {
                method.setAccessible(true);
                isSetAccessible = true;
            }
            Object returnObject = method.invoke(this.object, args);
            if (isSetAccessible) {
                method.setAccessible(false);
            }
            return returnObject == null ? null : new MyObject(returnObject);
        } catch (Exception e) {
            Bukkit.getLogger().severe("Error in: " + this.getObject().getClass());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Получить объект, с которым работаем
     * @return
     */
    public Object getObject() {
        return object;
    }

    /**
     * Создать новый объект
     * @param clazz класс
     * @param args аргументы
     * @return
     */
    private Object create(Class clazz, Object... args) {
        if (args.length == 0) {
            try {
                return clazz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            this.fixArgs(args);
            Constructor constructor = null;
            first: for (Constructor c : clazz.getDeclaredConstructors()) {
                if (c.getParameterCount() != args.length)
                    continue;

                for (int i = 0; i < args.length; i++) {
                    if (!c.getParameterTypes()[i].isInstance(args[i])) {
                        continue first;
                    }
                }
                constructor = c;
                break;
            }
            if (constructor == null) {
                System.out.println("Доступные конструкторы:");
                for (Constructor c : clazz.getDeclaredConstructors()) {
                    System.out.println(constructor.toString());
                }
                throw new NullPointerException("Конструктор не найден.");
            }
            try {
                return constructor.newInstance(args);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private void fixArgs(Object[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof MyObject) {
                args[i] = ((MyObject)args[i]).getObject();
            }
        }
    }
}