package com.senither.library.message;

public class Message extends MessageManager
{

    public Message setType(MessageType type)
    {
        this.type = type;

        return this;
    }

    public MessageType getType()
    {
        return type;
    }

    public Message setHeader(String header)
    {
        this.header = header;

        return this;
    }

    public String getHeader()
    {
        return header;
    }

    public Message setFooter(String footer)
    {
        this.footer = footer;

        return this;
    }

    public String getFooter()
    {
        return footer;
    }

    public Message setFadeIn(int fadeIn)
    {
        this.fadeIn = fadeIn;

        return this;
    }

    public int getFadeIn()
    {
        return fadeIn;
    }

    public Message setFadeOut(int fadeOut)
    {
        this.fadeOut = fadeOut;

        return this;
    }

    public int getFadeOut()
    {
        return fadeOut;
    }

    public Message setStay(int stay)
    {
        this.stay = stay;

        return this;
    }

    public int getStay()
    {
        return stay;
    }
}
