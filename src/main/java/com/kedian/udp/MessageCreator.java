package com.kedian.udp;

/**
 * @author wuzh
 * @version V1.0
 * @Package com.kedian.udp
 * @Description: 返回报文
 * @date 2019/7/17
 */
public class MessageCreator {

    private static final String SN_HEADER = "收到暗号，我是（SN）：";
    private static final String PORT_HEADER = "这是暗号，请回电端口（port）：";

    public static String buildWithPort(int port) {
        return PORT_HEADER + port;
    }

    public static int parsePort(String data) {
        if (data.startsWith(PORT_HEADER)) {
            return Integer.parseInt(data.substring(PORT_HEADER.length()));
        }
        return -1;
    }

    public static String buildWithSn(String sn) {
        return SN_HEADER + sn;
    }

    public static String parseSn(String data) {
        if (data.startsWith(SN_HEADER)) {
            return data.substring(SN_HEADER.length());
        }
        return null;
    }
}
