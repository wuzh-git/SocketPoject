package com.kedian.tcp.server;

import com.kedian.tcp.server.handle.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wuzh
 * @version V1.0
 * @Package com.kedian.tcp.server
 * @Description:
 * @date 2019/7/19
 */
public class TCPServer {

    private final int port;

    private ClientListener mListener;

    private List<ClientHandler> clientHandlerList = new ArrayList<>();

    public TCPServer(int port) {
        this.port = port;
    }

    public boolean start() {
        try {
            ClientListener listener = new ClientListener(port);
            mListener = listener;
            listener.start();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void stop() {
        if (mListener != null) {
            mListener.exit();
        }

        for (ClientHandler clientHandler : clientHandlerList) {
            clientHandler.exit();
        }
        clientHandlerList.clear();
    }

    public void broadcast(String str) {
        for (ClientHandler clientHandler : clientHandlerList) {
            clientHandler.send(str);
        }
    }

    private class ClientListener extends Thread {
        private ServerSocket server;
        private boolean done;

        public ClientListener(int port) throws IOException {
            server = new ServerSocket(port);
            System.out.println("服务器信息：" + server.getInetAddress() + " P:" + server.getLocalPort());
        }

        @Override
        public void run() {
            super.run();
            System.out.println("服务器准备就绪~");
            //等待客户端连接
            do {
                //得到客户端
                Socket client;
                try {
                    client = server.accept();
                } catch (IOException e) {
                    //接收失败则继续下一次
                    continue;
                }

                //客户端构建异步线程
                try {
                    ClientHandler clientHandler = new ClientHandler(client, handler -> clientHandlerList.remove(handler));
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("客户端连接异常：" + e.getMessage());
                }
            } while (!done);

            System.out.println("服务器已关闭！");
        }

        void exit() {
            done = true;
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
