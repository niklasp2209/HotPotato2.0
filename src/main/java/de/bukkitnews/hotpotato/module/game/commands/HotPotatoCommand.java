package de.bukkitnews.hotpotato.module.game.commands;

import de.bukkitnews.hotpotato.module.arena.model.Arena;
import de.bukkitnews.hotpotato.utils.MessageUtil;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HotPotatoCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        if(!(sender instanceof Player)){
            return true;
        }

        Player player = (Player) sender;

        //hotpotato setup create test
        if(args[0].equalsIgnoreCase("setup")){
            if(args.length == 3){
                if(args[1].equalsIgnoreCase("create")){
                    Arena arena = new Arena(args[2]);

                    if(arena.alreadyExists()){
                        player.sendMessage(MessageUtil.getMessage("setup_arena_exists"));
                        return true;
                    }

                    player.sendMessage(MessageUtil.getMessage("setup_arena_spawn"));
                }
            }
        }
        return false;
    }
}
