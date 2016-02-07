package com.senither.library.message;

import org.bukkit.entity.Player;

public interface MessageContract
{

    public void send(Player player, MessageType type);

    public void send(Player player);

    public void sendTitle(Player player);

    public void sendTab(Player player);
}
