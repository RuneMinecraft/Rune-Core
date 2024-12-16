package com.dank1234.plugin.global.general.events;

import com.dank1234.utils.Consts;
import com.dank1234.utils.event.Event;
import com.dank1234.utils.event.Priority;
import com.dank1234.utils.event.RuneListener;
import com.dank1234.utils.event.events.UserChatEvent;

public class CustomChatHandler extends RuneListener {

    @Event(priority = Priority.NORMAL)
    public void onChat(UserChatEvent e) {
        if (e.getUser().getGroups().isEmpty()) {
            assert Consts.MEMBER_GROUP != null;
            e.getUser().addGroup(Consts.MEMBER_GROUP);
        }
        e.format("&r" + e.getUser().getGroups().getFirst().getPrefix() + "&r " + e.getUser().getUsername() + " &8» &r" + e.getMessage());
    }
}
