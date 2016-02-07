package com.senither.library.message;

import com.senither.library.SenLibrary;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageManager implements MessageContract
{

    protected static SenLibrary library = null;

    protected String header = null;
    protected String footer = null;
    protected int fadeIn = 20;
    protected int stay = 20;
    protected int fadeOut = 20;

    protected MessageType type = MessageType.TITLE;

    public static void setLibrary(SenLibrary library)
    {
        MessageManager.library = library;
    }

    public static boolean hasLibrary()
    {
        return library != null;
    }

    @Override
    public void send(Player player)
    {
        switch (type) {
            case TAB:
                sendTab(player);
                break;

            case TITLE:
                sendTitle(player);
                break;

            default:
                sendTitle(player);
                break;
        }
    }

    @Override
    public void send(Player player, MessageType type)
    {
        this.type = type;

        this.send(player);
    }

    @Override
    public void sendTitle(Player player)
    {
        this.formatProperties(player);

        try {
            if (header.length() > 0) {
                Object enumTitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);

                Object chatTitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[]{
                    String.class
                }).invoke(null, new Object[]{"{\"text\":\"" + header + "\"}"});

                Constructor<?> titleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(new Class[]{
                    getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0],
                    getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE}
                );

                Object titlePacket = titleConstructor.newInstance(new Object[]{enumTitle, chatTitle, fadeIn, stay, fadeOut});

                sendPacket(player, titlePacket);
            }

            if (footer.length() > 0) {
                Object enumSubtitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null);

                Object chatSubtitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[]{
                    String.class
                }).invoke(null, new Object[]{"{\"text\":\"" + footer + "\"}"});

                Constructor<?> subtitleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(new Class[]{
                    getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0],
                    getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE}
                );

                Object subtitlePacket = subtitleConstructor.newInstance(new Object[]{
                    enumSubtitle, chatSubtitle, fadeIn, stay, fadeOut
                });

                sendPacket(player, subtitlePacket);
            }
        } catch (SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
        }
    }

    @Override
    public void sendTab(Player player)
    {
        this.formatProperties(player);

        try {
            Object tabHeader = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[]{
                String.class
            }).invoke(null, new Object[]{"{\"text\":\"" + header + "\"}"});

            Object tabFooter = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[]{
                String.class
            }).invoke(null, new Object[]{"{\"text\":\"" + footer + "\"}"});

            Constructor<?> titleConstructor = getNMSClass("PacketPlayOutPlayerListHeaderFooter").getConstructor(new Class[]{
                getNMSClass("IChatBaseComponent")}
            );

            Object packet = titleConstructor.newInstance(new Object[]{tabHeader});

            Field field = packet.getClass().getDeclaredField("b");
            field.setAccessible(true);
            field.set(packet, tabFooter);

            sendPacket(player, packet);
        } catch (SecurityException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException | NoSuchFieldException ex) {
        }
    }

    private void sendPacket(Player player, Object packet)
    {
        try {
            Object handle = player.getClass().getMethod("getHandle", new Class[0]).invoke(player, new Object[0]);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);

            playerConnection.getClass().getMethod("sendPacket", new Class[]{
                getNMSClass("Packet")}).invoke(playerConnection, new Object[]{packet}
            );
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException e) {
        }
    }

    private Class<?> getNMSClass(String name)
    {
        String version = org.bukkit.Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        } catch (ClassNotFoundException e) {
        }

        return null;
    }

    private void formatProperties(Player player)
    {
        header = colorize(header == null ? "" : header);
        footer = colorize(footer == null ? "" : footer);

        header = library.getPlaceholder().format(header, player);
        footer = library.getPlaceholder().format(footer, player);

        fadeOut = fadeOut < 1 ? 20 : fadeOut;
        fadeIn = fadeIn < 1 ? 20 : fadeIn;
        stay = stay < 1 ? 20 : stay;
    }

    /**
     * Colorize a message, using Bukkit/Spigots
     * standard and(&) symbol syntax.
     *
     * @param message The message the colorize.
     * @return String
     */
    private String colorize(String message)
    {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
