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

class De2Server{
    public static void main(String[] args) {
        try {
            // Tao UDP Server
            InetAddress serverAdd = InetAddress.getByName("localhost");
            int portUDP = 1214;
            DatagramSocket udpServer = new DatagramSocket(portUDP, serverAdd);
            // Nhan goi tin chua noi dung file 
            byte[] b = new byte[60000];
            DatagramPacket filePack = new DatagramPacket(b, b.length);
            udpServer.receive(filePack);
            System.out.println("Da nhan duoc goi noi dung file");
            // Gui qua UDP Client 1 mat khau
            String password = "VofP";
            DatagramPacket passPack = new DatagramPacket(password.getBytes(), password.getBytes().length, filePack.getAddress(), filePack.getPort());
            udpServer.send(passPack);
            System.out.println("Da gui goi chua mat khau");
            // Tao TCP Server
            ServerSocket tcpServer = new ServerSocket(2001);
            Socket socket = tcpServer.accept();
            // Nhan tu TCP Client dia chi IP 
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            Scanner sc = new Scanner(is);
            PrintStream ps = new PrintStream(os);
            String ipTCPClient = sc.nextLine();
            System.out.println("Nhan duoc dia chi IP cua TCP Client : "+ipTCPClient);
            // Nhan chuoi dao nguoc mat khau tu TCP Client
            String rePass = "";
            for (int i = password.length()-1; i >= 0 ; i--) {
                rePass += password.charAt(i);
            }
            String passReverse = sc.nextLine();
            System.out.println("Nhan duoc mat khau dao nguoc : "+passReverse);
            // Chung thuc
            if(passReverse.equals(rePass)){
                ps.println("SUCCESSFUL");
            }else{
                ps.println("-ERR");
            }
        } catch (UnknownHostException hostEx) {
            System.out.println("Sai dia chi");
        } catch (SocketException socEx){
            System.out.println("Khong khoi tao duoc UDP Socket");
        } catch (IOException ioEx){
            System.out.println("Loi Server : "+ioEx);
        }
    }
}