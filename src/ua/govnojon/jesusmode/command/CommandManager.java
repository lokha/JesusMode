package ua.govnojon.jesusmode.command;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import ua.govnojon.jesusmode.util.MyObject;

import java.util.ArrayList;
import java.util.Map;

public class CommandManager {
    public static final Map<String, Command> knownCommands = (Map<String, Command>)
            new MyObject(Bukkit.getServer())
                    .getField("commandMap")
                    .getField("knownCommands")
                    .getObject();

    private static ArrayList<Command> removed = new ArrayList<>();

    //Связка команд и их объектов
    private static ArrayList<Command> commands = new ArrayList<>();

    /**
     * Зарегистировать команду
     * @param command
     */
    public static void registerCommand(Command command) {
        knownCommands.remove(command.getName());
        knownCommands.put(command.getName(), command);
        for (String a : command.getAliases()) {
            knownCommands.remove(a);
            knownCommands.put(a, command);
        }
        commands.add(command);
    }


    /**
     * Получить все команды
     * @return команды
     */
    public static ArrayList<Command> getCommands() {
        return commands;
    }

    /**
     * Получить команду по названию
     * @param command название команды
     * @return команду
     */
    public static Command getCommand(String command) {
        return knownCommands.get(command);
    }


    /**
     * Удалить команду
     * @param command
     */
    public static void unregisterCommand(String command) {
        Command c = knownCommands.remove(command);
        if (c != null) {
            knownCommands.remove(c.getName());
            for (String a : c.getAliases())
                knownCommands.remove(a);
        }
        commands.remove(c);
    }
}
