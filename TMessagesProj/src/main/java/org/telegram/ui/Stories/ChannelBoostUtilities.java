package org.telegram.ui.Stories;

import android.text.TextUtils;

import org.telegram.bautrukevich.ChatObject;
import org.telegram.bautrukevich.MessagesController;
import org.telegram.tgnet.TLRPC;

public class ChannelBoostUtilities {
    public static String createLink(int currentAccount, long dialogId) {
        TLRPC.Chat chat = MessagesController.getInstance(currentAccount).getChat(-dialogId);
        String username = ChatObject.getPublicUsername(chat);
        if (!TextUtils.isEmpty(username)) {
            return "https://t.me/" + ChatObject.getPublicUsername(chat) + "?boost";
        } else {
            return "https://t.me/c/" + -dialogId + "?boost";
        }
    }
}
