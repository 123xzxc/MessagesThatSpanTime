package xyz.xiaosung;

import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Message;
import xyz.xiaosung.util.ConfigLoader;

import java.text.SimpleDateFormat;
import java.util.*;

public final class MessagesThatSpanTime extends JavaPlugin {
    public static final MessagesThatSpanTime INSTANCE = new MessagesThatSpanTime();
    private static final ConfigLoader cl = ConfigLoader.INSTANCE;
    List<Object[]> messageList = new ArrayList<>();

    private MessagesThatSpanTime() {
        super(new JvmPluginDescriptionBuilder("xyz.xiaosung.MessagesThatSpanTime", "0.2.0")
                .name("MessagesThatSpanTime")
                .author("123xzxc")
                .build());
    }

    @Override
    public void onEnable() {
        getLogger().info("MessagesThatSpanTime 已加载! 版本号:0.2.0");
        GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, g -> {
            try{
                fs(g);
                hf(g);
            }catch (IllegalArgumentException e){
                getLogger().error(e.getMessage() + "配置文件出错，请查看配置文件!");
                g.getSubject().sendMessage("配置文件出错，请查看配置文件!");
            }
        });
        GlobalEventChannel.INSTANCE.subscribeAlways(FriendMessageEvent.class, f -> {
            //null
        });
    }
    private void fs(GroupMessageEvent g) {

        String groupMessageString = g.getMessage().contentToString();
        if (groupMessageString.length() > 3) {
            if (groupMessageString.startsWith("ts @")) {
                long senderId = g.getSender().getId();
                String groupName = g.getGroup().getName();
                At at = null;
                for (Message mess : g.getMessage()) {
                    if (mess instanceof At) {
                        at = (At) mess;
                        break;
                    }
                }
                if (at == null){
                    g.getSubject().sendMessage(cl.getcNoneAt());
                    return;
                }
                long atId = at.getTarget();
                if(atId == g.getBot().getId()){
                    g.getSubject().sendMessage(cl.getcIsBot());
                    return;
                }
                if(atId == senderId){
                    g.getSubject().sendMessage(cl.getcAtMy());
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
                    SimpleDateFormat sdf = new SimpleDateFormat(cl.getcDateFormat());
                    sdf.setTimeZone(TimeZone.getTimeZone(cl.getcTimeZone()));
                    Object[] msg = new Object[]{groupMessageString, atId, senderId, senderName, atName, sdf.format(date),groupName};
                    messageList.add(msg);
                    g.getSubject().sendMessage(cl.getcSMessage());
                }
            }
        }
    }
    private void hf(GroupMessageEvent g) {
        String msg;
        //long senderId;
        long atId;
        String senderName;
        boolean b = false;
        //String atName;
        String date;
        String strTmp = cl.getcFMessage();
        strTmp = strTmp.replaceAll("\\{senderName}",g.getSenderName());
        StringBuilder ms = new StringBuilder(strTmp + "\n");
        String groupName;
        if (messageList.size() > 0) {
            Iterator<Object[]> it = messageList.iterator();
            while (it.hasNext()) {
                Object[] message = it.next();
                atId = (long) message[1];
                if (atId == g.getSender().getId()) {
                    msg = ((String) message[0]);
                    //senderId = (long) message[2];
                    senderName = (String) message[3];
                    //atName = (String) message[4];
                    date = (String) message[5];
                    groupName = (String) message[6];
                    it.remove();
                    int i = String.valueOf(atId).length() +1;
                    String str = msg.substring(msg.indexOf("@")+i).trim();
                    String mg = cl.getcStr();
                    mg = mg.replaceAll("\\{groupName}", groupName);
                    mg = mg.replaceAll("\\{senderName}", senderName);
                    mg = mg.replaceAll("\\{str}", str);
                    mg = mg.replaceAll("\\{date}", date);
                    String m = mg;
                    m = m+"\n";
                    ms.append(m).append(cl.getcLine() + "\n");
                    b = true;
                }
            }
            if(b){
                g.getSubject().sendMessage(ms.toString());
            }
        }
    }
}