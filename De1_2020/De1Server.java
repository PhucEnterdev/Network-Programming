import java.io.DataOutputStream;
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
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

class De1Server{
    public static void main(String[] args) {
        try {
            // Khoi tao UDP Server
            int portUDP = 1214;
            InetAddress addUDPServer = InetAddress.getByName("127.0.0.1");
            DatagramSocket udpServer = new DatagramSocket(portUDP, addUDPServer);
            // Nhan goi tin tu UDP Client
            byte[] b = new byte[1000];
            DatagramPacket addressPack = new DatagramPacket(b, b.length);
            udpServer.receive(addressPack); 
            String addClient = new String(addressPack.getData(), 0, addressPack.getLength());
            System.out.println("Dia chi IP Client la : "+addClient);
            // Gui goi thu 1 den UDP Client, noi dung la cong TCP Server
            String strPortTCP = "2001";
            DatagramPacket portPack = new DatagramPacket(strPortTCP.getBytes(), strPortTCP.getBytes().length, addressPack.getAddress(), addressPack.getPort());
            udpServer.send(portPack);
            System.out.println("Da gui goi thu 1");
            // Gui goi thu 2 den UDP Client, noi dung goi la 1 mat khau
            String password = "12142001Phuccongtu";
            DatagramPacket passPack = new DatagramPacket(password.getBytes(), password.getBytes().length, addressPack.getAddress(), addressPack.getPort());
            udpServer.send(passPack);
            System.out.println("Da gui goi thu 2");
            // Tao TCP Server
            ServerSocket tcpServer = new ServerSocket(2001);
            Socket socket = tcpServer.accept();
            // Nhan chuoi co xuong hang tu TCP Client
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            Scanner sc = new Scanner(is);
            PrintStream ps = new PrintStream(os);
            String passwordFromTCPClient = sc.nextLine();
            System.out.println("Nhan duoc password tu TCP Client : "+passwordFromTCPClient);
            // Chung thuc
            if(!passwordFromTCPClient.equals(password)){
                ps.println("-ERR");
            }else{
                File file = new File("abc.txt");
                int size = (int)file.length();
                ps.println(String.valueOf(size));
                FileOutputStream fos = new FileOutputStream(file);
                byte[] b2 = new byte[size];

                if(file.exists() && file.isFile()){
                    fos.write(b2, 0, size);
                    System.out.println("Da doc thanh cong file");
                    DataOutputStream dos = new DataOutputStream(os);
                    dos.write(b2);
                    System.out.println("Da gui file thanh cong");
                    fos.close();
                    dos.close();
                }
            }
        } catch (UnknownHostException hostEx) {
            System.out.println("Sai dia chi Server");
        } catch (SocketException socEx){
            System.out.println("Khong khoi tao duoc UDP Socket");
        } catch (FileNotFoundException fileEx){
            System.out.println("Loi file");
        } catch (IOException ioEx){
            System.out.println("Loi Server : "+ioEx);
        }
    }
}