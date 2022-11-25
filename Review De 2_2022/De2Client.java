import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

class De2Client {
    public static void main(String[] args) {
        try {
            // 1 Nhap tu ban phim
            Scanner kb = new Scanner(System.in);
            System.out.print("Nhap ho ten cua sinh vien : ");
            String fullName = kb.nextLine();
            System.out.print("Nhap dia chi IP Server : ");
            String ipServer = kb.nextLine();
            System.out.print("Nhap cong UDP Server : ");
            int portUDP = kb.nextInt();
            // 2 Tao muticast va tham gia dia chi 227.0.0.2
            MulticastSocket mulClient = new MulticastSocket(12345);
            InetAddress addressUDP = InetAddress.getByName("227.0.0.2");
            mulClient.joinGroup(addressUDP);
            // 3 Nhan goi Multicast, noi dung la 1 key
            byte[] b = new byte[1000];
            DatagramPacket keyPack = new DatagramPacket(b, 1000);
            mulClient.receive(keyPack);
            String key = new String(keyPack.getData(), 0, keyPack.getLength());
            System.out.println("Da nhan duoc khoa : " + key);
            // 4 Tao 1 UDP Socket Client, gui den UDP Server ho ten vua nhap
            DatagramSocket udpClient = new DatagramSocket();
            byte[] b1 = fullName.getBytes();
            InetAddress addUDPSv = InetAddress.getByName("127.0.0.1");
            DatagramPacket namePack = new DatagramPacket(b1, b1.length, addUDPSv, portUDP);
            udpClient.send(namePack);
            System.out.println("Da gui goi ho ten cho Server");
            // 5 Nhan goi tin UDP tu UDP Server, noi dung la cong TCP
            byte[] b2 = new byte[1000];
            DatagramPacket portPack = new DatagramPacket(b2, 1000);
            udpClient.receive(portPack);
            String strPortTCP = new String(portPack.getData(), 0, portPack.getLength());
            int portTCP = Integer.parseInt(strPortTCP);
            System.out.println("Nhan duoc cong : " + strPortTCP);
            // 6 Tao TCP Client noi ket voi TCP Server dia chi nhap tu ban phim va cong vua
            // nhan
            Socket tcpClient = new Socket(ipServer, portTCP);
            // 7 Gui qua TCP Server key nhan duoc o tren.
            InputStream is = tcpClient.getInputStream();
            OutputStream os = tcpClient.getOutputStream();
            Scanner sc = new Scanner(is);
            PrintStream ps = new PrintStream(os);
            ps.println(key);
            // 8 Nhan tu TCp Server 1 chuoi co xuong hang la so byte
            String strByte = sc.nextLine();
            System.out.println("Nhan duoc so luong byte : " + strByte);
            int quantityByte = Integer.parseInt(strByte);
            // 9 Neu byte = 0 -> nhap sai
            if (quantityByte == 0) {
                System.out.println("Ban da gui sai key");
            } else {
                byte[] b3 = new byte[quantityByte];
                DataInputStream dis = new DataInputStream(is);
                dis.readFully(b3);
                File file = new File("Ketqua.docx");
                FileOutputStream fos = new FileOutputStream(file);
                fos.write((b3), 0, quantityByte);
                System.out.println("Da ghi file thanh cong");
                dis.close();
                fos.close();
            }
            // Dong tat ca socket
            mulClient.close();
            udpClient.close();
            tcpClient.close();
        } catch (UnknownHostException hostEx) {
            System.out.println("Sai dia chi Server");
        } catch (SocketException socEx) {
            System.out.println("Khong khoi tao duoc UDP Socket");
        } catch (FileNotFoundException fileEx) {
            System.out.println("Khong tim thay file");
        } catch (IOException ioEx) {
            System.out.println("Loi : " + ioEx);
        }
    }
}