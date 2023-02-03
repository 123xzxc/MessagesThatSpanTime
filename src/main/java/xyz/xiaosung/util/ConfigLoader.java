package xyz.xiaosung.util;

import xyz.xiaosung.MessagesThatSpanTime;

import java.io.*;
import java.nio.charset.StandardCharsets;


public class ConfigLoader{
    public static final ConfigLoader INSTANCE = new ConfigLoader();
    MessagesThatSpanTime mtst = MessagesThatSpanTime.INSTANCE;

    private String cNoneAt;
    private String cIsBot;
    private String cAtMy;
    private String cDateFormat;
    private String cTimeZone;
    private String cSMessage;
    private String cFMessage;
    private String cStr;
    private String cLine;

    ConfigManager cm;
    private boolean config(){
        String configPath = mtst.getConfigFolderPath().toString();
        String configFile = configPath + "/config.properties";
        try {
            cm = ConfigManager.getInstance(configFile);
        } catch (FileNotFoundException e) {
            File file = new File(configFile);
            try {
                if(file.createNewFile()){
                    mtst.getLogger().info("配置文件文件不存在，正在创建.");
                    FileWriter fw = new FileWriter(file.getAbsoluteFile());
                    BufferedWriter bw = new BufferedWriter(fw);
                    String str = "#==================================================\n" +
                            "#未提示任何人的提示\n" +
                            "noneAt=你没有提示任何人哦~\n" +
                            "#提示的目标为Bot\n" +
                            "isBot=你不能提示bot哦~\n" +
                            "#如果提示的目标为自己\n" +
                            "atMy=你不可以提示自己哦~\n" +
                            "#提示成功的消息\n" +
                            "SMessage=提示成功w\n" +
                            "#==================================================\n" +
                            "#日期格式 (yyyy:MM:dd HH:mm:ss)\n" +
                            "dateFormat=yyyy年MM月dd日 HH:mm:ss\n" +
                            "#时区 (Asia/Shanghai)\n" +
                            "timeZone=Asia/Shanghai\n" +
                            "#==================================================\n" +
                            "#\n" +
                            "#               接收提示的格式 \\n换行\n" +
                            "#关键字列表:groupName senderName date str\n" +
                            "#             群名      发件人   日期 内容\n" +
                            "#\n" +
                            "#前缀(同时接收多个提示只会显示一次)(只支持{SenderName})\n" +
                            "FMessage=嘿!{senderName}\\n\n" +
                            "#内容\n" +
                            "str=来自{groupName}的{senderName}在{date}提醒你{str}\\n\n" +
                            "#多个通知的分割线/通知结尾\n" +
                            "line=*-=-*-=-*-=-*-=-*-=-*\\n\n" +
                            "#==================================================";
                    bw.write(new String(str.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
                    bw.close();
                    mtst.getLogger().info("配置文件创建成功!");
                    cm = ConfigManager.getInstance(configFile);
                }
            } catch (IOException ex) {
                mtst.getLogger().error("错误，无法创建配置文件!");
                mtst.getLogger().error(ex.getMessage());
                return false;
            }
        } catch (UnsupportedEncodingException e) {
            mtst.getLogger().error(e.getMessage());
        }
        return true;
    }
    public ConfigLoader() {
        if(config()){
            cNoneAt = cm.getString("noneAt");
            cIsBot = cm.getString("isBot");
            cAtMy = cm.getString("atMy");
            cDateFormat = cm.getString("dateFormat");
            cTimeZone = cm.getString("timeZone");
            cSMessage = cm.getString("SMessage");
            cFMessage = cm.getString("FMessage");
            cLine = cm.getString("line");
            cStr = cm.getString("str");
        }
    }

    public String getcStr() {
        return cStr;
    }


    public String getcNoneAt() {
        return cNoneAt;
    }

    public String getcIsBot() {
        return cIsBot;
    }

    public String getcAtMy() {
        return cAtMy;
    }

    public String getcDateFormat() {
        return cDateFormat;
    }

    public String getcTimeZone() {
        return cTimeZone;
    }

    public String getcSMessage() {
        return cSMessage;
    }

    public String getcFMessage() {
        return cFMessage;
    }

    public String getcLine() {
        return cLine;
    }
}
