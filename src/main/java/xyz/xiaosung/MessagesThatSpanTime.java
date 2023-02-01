package xyz.xiaosung;

import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;

import java.text.SimpleDateFormat;
import java.util.*;

public final class MessagesThatSpanTime extends JavaPlugin {
    public static final MessagesThatSpanTime INSTANCE = new MessagesThatSpanTime();
    List<Object[]> messageList = new ArrayList<>();

    private MessagesThatSpanTime() {
        super(new JvmPluginDescriptionBuilder("xyz.xiaosung.MessagesThatSpanTime", "0.1.0")
                .name("MessagesThatSpanTime")
                .author("123xzxc")
                .build());
    }

    @Override
    public void onEnable() {
        getLogger().info("Plugin loaded!");
        GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, g -> {
                fs(g);
                hf(g);
        });
        GlobalEventChannel.INSTANCE.subscribeAlways(FriendMessageEvent.class, f -> {
            //监听好友消息
            //null
        });
    }

    public void fs(GroupMessageEvent g) {

        Object groupMessage = g.getMessage();
        String groupMessageString = g.getMessage().contentToString();
        if (groupMessageString.length() > 3) {
            if (groupMessageString.substring(0, 4).equals("ts @")) {
                long senderId = g.getSender().getId();
                At at = null;
                for (Message mess : g.getMessage()) {
                    if (mess instanceof At) {
                        at = (At) mess;
                        break;
                    }
                }
                if (at == null){
                    g.getSubject().sendMessage("你没有提示任何人哦");
                    return;
                }
                long atId = at.getTarget();
                if(atId == g.getBot().getId()){
                    g.getSubject().sendMessage("不能提示bot哦");
                    return;
                }
                if(atId == senderId){
                    g.getSubject().sendMessage("不能提示自己哦");
                    return;
                }
                String atName = null;
                try{
                    atName = g.getGroup().get(atId).getNameCard();
                }catch(NullPointerException n){
                    atName = String.valueOf(atId);
                }finally {
                    String senderName = g.getSenderName();
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                    sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
                    Object msg[] = new Object[]{groupMessageString, atId, senderId, senderName, atName, sdf.format(date)};
                    messageList.add(msg);
                    g.getSubject().sendMessage("提示成功!");
                }
            }
        }
    }

    public void hf(GroupMessageEvent g) {
        String msg = null;
        long senderId = 0;
        long atId = 0;
        String senderName = null;
        Boolean b = false;
        String atName = null;
        String date = null;
        String ms = "嘿! "+g.getSenderName()+"\n";
        if (messageList.size() > 0) {
            Iterator<Object[]> it = messageList.iterator();
            while (it.hasNext()) {
                Object[] message = it.next();
                atId = (long) message[1];
                if (atId == g.getSender().getId()) {
                    msg = ((String) message[0]);
                    senderId = (long) message[2];
                    senderName = (String) message[3];
                    atName = (String) message[4];
                    date = (String) message[5];
                    it.remove();
                    int i = String.valueOf(atId).length() +1;
                    String str = msg.substring(msg.indexOf("@")+i).trim();
                    String m = senderName + " 在 "+date+" 提醒你:" + str + "\n";
                    ms = ms + m + "*-=-*-=-*-=-*-=-*-=-*";
                    b = true;
                }
            }
            if(b){
                g.getSubject().sendMessage(ms);
                ms = ms + "\n";
            }
        }
    }
}