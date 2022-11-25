import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

class De2Server {
    public static void main(String[] args) {
        try {
            // UDP Socket tham gia dia chi 227.0.0.2
            DatagramSocket multiServer = new DatagramSocket();
            InetAddress group = InetAddress.getByName("227.0.0.2");
            InetAddress addressUDP = InetAddress.getByName("127.0.0.1");
            // Gui 1 khoa (key) vao group
            String key = "VanofPhuc";
            DatagramPacket keyPack = new DatagramPacket(key.getBytes(), key.getBytes().length, group, 12345);
            multiServer.send(keyPack);
            System.out.println("Da gui key cho Client");
            // Nhan goi ho ten tu UDP Client
            DatagramSocket udpServer = new DatagramSocket(1997, addressUDP);
            byte[] b1 = new byte[1000];
            DatagramPacket namePack = new DatagramPacket(b1, b1.length);
            udpServer.receive(namePack);
            System.out.println("Da nhan goi ho ten");
            String fullName = new String(namePack.getData(), 0, namePack.getLength());
            System.out.println("Nhan duoc ho va ten sv : " + fullName);
            // Gui cong TCP Server cho UDP Client
            String tcpPort = "2001";
            DatagramPacket portPack = new DatagramPacket(tcpPort.getBytes(), tcpPort.getBytes().length,
                    namePack.getAddress(), namePack.getPort());
            udpServer.send(portPack);
            // Tao TCP Server
            ServerSocket tcpServer = new ServerSocket(2001);
            Socket socket = tcpServer.accept();
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            Scanner sc = new Scanner(is);
            PrintStream ps = new PrintStream(os);
            // Nhan key tu TCP Client
            String keyFromTCPClient = sc.nextLine();
            System.out.println("Nhan duoc key tu TCP Client : " + keyFromTCPClient);
            // Kiem tra key
            if (keyFromTCPClient.equals(key)) {
                File file = new File("abc.txt");
                FileInputStream fis = new FileInputStream(file);
                DataInputStream dis = new DataInputStream(fis);
                int size = (int) file.length();
                String strSize = String.valueOf(size);
                ps.println(strSize);
                byte[] b2 = new byte[size];
                if (file.exists() && file.isFile()) {
                    dis.readFully(b2);
                    System.out.println("Da doc thanh cong file");
                    // Gui noi dung file cho Client
                    DataOutputStream dos = new DataOutputStream(os);
                    dos.write(b2);
                    System.out.println("Da gui file thanh cong");
                    dos.close();
                }
                fis.close();
                dis.close();
            } else {
                ps.println("0");
            }
        } catch (UnknownHostException hostEx) {
            System.out.println("Sai dia chi Server");
        } catch (SocketException socEx) {
            System.out.println("Khong khoi tao duoc UDP Socket");
        } catch (FileNotFoundException FileEx) {
            System.out.println("Khong tim thay file");
        } catch (IOException ioEx) {
            System.out.println("Loi Server : " + ioEx);
        }
    }
}