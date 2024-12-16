package com.dank1234.plugin.global.general.events;

import com.dank1234.utils.Consts;
import com.dank1234.utils.event.Event;
import com.dank1234.utils.event.Priority;
import com.dank1234.utils.event.RuneListener;
import com.dank1234.utils.event.events.UserChatEvent;

class ChatHandler : RuneListener() {
    @Event fun onChat(e: UserChatEvent) {
        if (e.user.groups.isEmpty()) {
            e.user.addGroup(Consts.MEMBER_GROUP);
        }
        e.format("&r" + e.user.groups.first().prefix + "&r " + e.user.username + " &8» &r" + e.message);
    }
}
