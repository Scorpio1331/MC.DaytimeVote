package scorpio1331.github.io.DaytimeVote;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ChangeConfigurationCommand implements ICommand
{
    private static class AcceptedArguments {
        public static final String Day = "Day";
        public static final String Night = "Night";
        public static final String All = "All";
        public static final String Query = "Query";
        public static final String PollingTicks = "DaytimePollingTicks";
        public static final List<String> AcceptableArguments = Arrays.asList(Day, Night, All, Query, PollingTicks);
    }

    private JavaPlugin plugin;

    @Override
    public String GetName() {
        return "ChangeDaytimeVoteConfig";
    }

    @Override
    public void SetPlugin(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Boolean ValidateCommand(CommandSender sender, Command cmd, String label, String[] args, JavaPlugin plugin) {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;

            Player target = plugin.getServer().getPlayer(player.getUniqueId());
            if (target == null || !target.isOnline()) {
                sender.sendMessage(player.getDisplayName() + " is not online!");
                return false;
            }

            if (!player.hasPermission(Permissions.CanChangeConfig.getPermissionName())) {
                sender.sendMessage("You do not have permission to use this command!");
                return false;
            }
        }
        if (args.length == 0)
        {
            sender.sendMessage(String.format("This command requires an argument of '%s'.", Utils.GetListOfAvailableCommandsText(AcceptedArguments.AcceptableArguments)));
            return false;
        }

        String commandArg = args[0];
        if (AcceptedArguments.AcceptableArguments.stream().noneMatch(commandArg::equalsIgnoreCase))
        {
            sender.sendMessage(String.format("This command only accepts an argument of '%s'.", Utils.GetListOfAvailableCommandsText(AcceptedArguments.AcceptableArguments)));
            return false;
        }

        if (!commandArg.equalsIgnoreCase(AcceptedArguments.Query) && !commandArg.equalsIgnoreCase(AcceptedArguments.PollingTicks)  && args.length == 1)
        {
            sender.sendMessage(String.format("The %s argument requires %d more parameters!", commandArg, commandArg.equalsIgnoreCase(AcceptedArguments.All) ? 3 : 2));
            return false;
        }
        else if (commandArg.equalsIgnoreCase(AcceptedArguments.Query) && args.length > 1)
        {
            sender.sendMessage(String.format("The %s argument does not take any parameters!", commandArg));
            return false;
        }
        else if (commandArg.equalsIgnoreCase(AcceptedArguments.All) && args.length <= 3)
        {
            sender.sendMessage(String.format("The %s argument requires %d more parameters!", commandArg, 4 - args.length));
            return false;
        }
        else if (commandArg.equalsIgnoreCase(AcceptedArguments.PollingTicks) && args.length < 2) {
            sender.sendMessage(String.format("The %s argument requires [Number of Ticks] parameter!", commandArg));
            return false;
        }

        if (!commandArg.equalsIgnoreCase(AcceptedArguments.Query)) {
            Integer requireCount = null;
            try {
                requireCount = Integer.parseInt(args[1]);
            } catch (NumberFormatException nfe) {
                sender.sendMessage(String.format("The %s argument %s parameter needs to be a number!", commandArg, commandArg.equalsIgnoreCase(AcceptedArguments.PollingTicks) ? "[Number of Ticks]" : "[Required Votes]"));
                return false;
            }
            if (requireCount < 1) {
                sender.sendMessage(String.format("The %s argument %s parameter cannot be below 1!", commandArg, commandArg.equalsIgnoreCase(AcceptedArguments.PollingTicks) ? "[Number of Ticks]" : "[Required Votes]"));
                return false;
            }
            if (!commandArg.equalsIgnoreCase(AcceptedArguments.PollingTicks)) {
                if (!args[2].equalsIgnoreCase("true") && !args[2].equalsIgnoreCase("false")) {
                    sender.sendMessage(String.format("The %s argument [Is Absolute] parameter needs to be 'True' or 'False'!", commandArg));
                    return false;
                }

                if (commandArg.equalsIgnoreCase(AcceptedArguments.All) && !args[3].equalsIgnoreCase("true") && !args[3].equalsIgnoreCase("false")) {
                    sender.sendMessage(String.format("The %s argument [Use All] parameter needs to be 'True' or 'False'!", commandArg));
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public Boolean PerformCommand(CommandSender sender, Command cmd, String label, String[] args, JavaPlugin plugin) {
        String commandArg = AcceptedArguments.AcceptableArguments.stream().filter(args[0]::equalsIgnoreCase).findFirst().get();

        switch (commandArg) {
            case AcceptedArguments.Query:
                PrintConfigSettings(plugin, sender);
                break;
            case AcceptedArguments.PollingTicks:
                int requireTicks = Integer.parseInt(args[1]);
                int currentTicks = plugin.getConfig().getInt(ConfigurationSettings.DaytimePollingTicks);

                if (currentTicks != requireTicks) {
                    sender.sendMessage("DayTimeVote: Changed Daytime Polling Tick schedule to: ");

                    plugin.getConfig().set(ConfigurationSettings.DaytimePollingTicks, requireTicks);
                    sender.sendMessage(String.format("    -> Daytime Polling Ticks: %d, from %d. Please use /reload to use the new Tick rate.", requireTicks, currentTicks));
                } else {
                    sender.sendMessage("DayTimeVote: Given value matches the current stored Daytime Polling Tick schedule.");
                }
                break;
            default:
                int requireCount = Integer.parseInt(args[1]);
                boolean isAbsolute = Boolean.parseBoolean(args[2]);
                switch (commandArg) {
                    case AcceptedArguments.Day: {
                        SetConfigSettings(plugin, sender, requireCount, isAbsolute, ConfigurationSettings.NumberOfPlayersRequiredForVote.Daytime,
                                ConfigurationSettings.NumberOfPlayersRequiredForVote.DaytimeValueIsAbsolute, "Daytime");
                        break;
                    }
                    case AcceptedArguments.Night: {
                        SetConfigSettings(plugin, sender, requireCount, isAbsolute, ConfigurationSettings.NumberOfPlayersRequiredForVote.Nighttime,
                                ConfigurationSettings.NumberOfPlayersRequiredForVote.NighttimeValueIsAbsolute, "Night-time");
                        break;
                    }
                    case AcceptedArguments.All: {
                        boolean useAll = Boolean.parseBoolean(args[3]);

                        SetAllConfigSettings(plugin, sender, requireCount, isAbsolute, useAll);
                        break;
                    }
                }
                break;
        }

        this.plugin.saveConfig();
        return true;
    }

    @Override
    public List<String> TabComplete(String[] args) {
        if (args.length == 1) {
            return AcceptedArguments.AcceptableArguments.stream().filter(a -> a.toLowerCase().startsWith(args[0].toLowerCase())).collect(Collectors.toList());
        }
        if (args.length >= 2) {
            String commandArg = args[0];

            if (commandArg.equalsIgnoreCase(AcceptedArguments.Query)) {
                return new ArrayList<>();
            }

            if (args.length == 2 && args[1].isEmpty())
            {
                if (commandArg.equalsIgnoreCase(AcceptedArguments.PollingTicks)) {
                    return Collections.singletonList("Number of Ticks");
                }

                return Collections.singletonList("Required Votes");
            }

            if (!commandArg.equalsIgnoreCase(AcceptedArguments.PollingTicks)) {
                if (args.length == 3 && args[2].isEmpty())
                {
                    return Arrays.asList("True", "False");
                }

                if (args.length == 4 && commandArg.equalsIgnoreCase(AcceptedArguments.All) && args[3].isEmpty())
                {
                    return Arrays.asList("True", "False");
                }
            }

        }
        return new ArrayList<>();
    }

    private void SetConfigSettings(JavaPlugin plugin, CommandSender sender, int requireCount, boolean isAbsolute, String requiredCountPath, String isAbsolutePath, String time) {
        int currentCount = plugin.getConfig().getInt(requiredCountPath);
        boolean currentIsAbsolute = plugin.getConfig().getBoolean(isAbsolutePath);

        if (currentCount != requireCount || currentIsAbsolute != isAbsolute) {
            sender.sendMessage(String.format("DayTimeVote: Changed %s config settings to: ", time));

            if (currentCount != requireCount) {
                plugin.getConfig().set(requiredCountPath, requireCount);
                sender.sendMessage(String.format("    -> %s required votes: %d, from %d", time, requireCount, currentCount));
            }
            if (currentIsAbsolute != isAbsolute) {
                plugin.getConfig().set(isAbsolutePath, isAbsolute);
                sender.sendMessage(String.format("    -> %s required votes is absolute: %b, from %b", time, isAbsolute, currentIsAbsolute));
            }
        } else {
            sender.sendMessage(String.format("DayTimeVote: Given values match the current stored %s values.", time));
        }
    }

    private void SetAllConfigSettings(JavaPlugin plugin, CommandSender sender, int requireCount, boolean isAbsolute, boolean useAll) {
        int currentCount = plugin.getConfig().getInt(ConfigurationSettings.NumberOfPlayersRequiredForVote.All);
        boolean currentIsAbsolute = plugin.getConfig().getBoolean(ConfigurationSettings.NumberOfPlayersRequiredForVote.AllValueIsAbsolute);
        boolean currentUseAll = plugin.getConfig().getBoolean(ConfigurationSettings.NumberOfPlayersRequiredForVote.UseAll);

        if (currentCount != requireCount || currentIsAbsolute != isAbsolute || useAll != currentUseAll) {
            sender.sendMessage("DayTimeVote: Changed All config settings to: ");

            if (currentCount != requireCount) {
                plugin.getConfig().set(ConfigurationSettings.NumberOfPlayersRequiredForVote.All, requireCount);
                sender.sendMessage(String.format("    -> All required votes: %d, from %d", requireCount, currentCount));
            }
            if (currentIsAbsolute != isAbsolute) {
                plugin.getConfig().set(ConfigurationSettings.NumberOfPlayersRequiredForVote.AllValueIsAbsolute, isAbsolute);
                sender.sendMessage(String.format("    -> All required votes is absolute: %b, from %b", isAbsolute, currentIsAbsolute));
            }
            if (useAll != currentUseAll) {
                plugin.getConfig().set(ConfigurationSettings.NumberOfPlayersRequiredForVote.UseAll, useAll);
                sender.sendMessage(String.format("    -> Use All values for Daytime & Night-time: %b, from %b", useAll, currentUseAll));
            }
        } else {
            sender.sendMessage("DayTimeVote: Given values match the current stored All values.");
        }
    }

    private void PrintConfigSettings(JavaPlugin plugin, CommandSender sender) {
        boolean usingAll = plugin.getConfig().getBoolean(ConfigurationSettings.NumberOfPlayersRequiredForVote.UseAll);
        boolean allIsAbsolute = plugin.getConfig().getBoolean(ConfigurationSettings.NumberOfPlayersRequiredForVote.AllValueIsAbsolute);
        int allValue = plugin.getConfig().getInt(ConfigurationSettings.NumberOfPlayersRequiredForVote.All);

        boolean dayIsAbsolute = plugin.getConfig().getBoolean(ConfigurationSettings.NumberOfPlayersRequiredForVote.DaytimeValueIsAbsolute);
        int dayValue = plugin.getConfig().getInt(ConfigurationSettings.NumberOfPlayersRequiredForVote.Daytime);
        boolean nightIsAbsolute = plugin.getConfig().getBoolean(ConfigurationSettings.NumberOfPlayersRequiredForVote.NighttimeValueIsAbsolute);
        int nightValue = plugin.getConfig().getInt(ConfigurationSettings.NumberOfPlayersRequiredForVote.Nighttime);

        sender.sendMessage("DayTimeVote: Configuration Settings:");
        sender.sendMessage(String.format("    -> Daytime required votes: %d", dayValue));
        sender.sendMessage(String.format("    -> Daytime required votes is absolute: %b", dayIsAbsolute));
        sender.sendMessage(String.format("    -> Night-time required votes: %d", nightValue));
        sender.sendMessage(String.format("    -> Night-time required votes is absolute: %b", nightIsAbsolute));
        sender.sendMessage(String.format("    -> All required votes: %d", allValue));
        sender.sendMessage(String.format("    -> All required votes is absolute: %b", allIsAbsolute));
        sender.sendMessage(String.format("    -> Using All value for Daytime & Night-time: %b", usingAll));
    }
}
