import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

class De2Client{
    public static void main(String[] args) {
        try {
            // 1 Nhap tu ban phim cong UDP Server
            Scanner kb = new Scanner(System.in);
            System.out.print("Nhap cong cua UDP Server : ");
            int portUDP = kb.nextInt();
            // 2 Nhap cong cua TCP Server
            System.out.print("Nhap vao cong cua TCP Server : ");
            int portTCP = kb.nextInt();
            System.out.print("Nhap vao dia chi Server : ");
            String serverAdd = kb.nextLine();
            // 3 Tao 1 UDP Client
            DatagramSocket udpClient = new DatagramSocket();
            // 4 Tao goi tin chua noi dung file data.tk trong thu muc lam bai
            // chieu dai goi la kich thuoc file
            File file = new File("data.tk");
            int size = (int)file.length();
            FileInputStream fis = new FileInputStream(file);
            byte[] b = new byte[size];
            if(file.exists() && file.isFile()){
                DataInputStream dis = new DataInputStream(fis);
                dis.readFully(b);
                System.out.println("Da doc file thanh cong");
                fis.close();
                dis.close();
            }else{
                System.out.println("File khong ton tai");
            }
            InetAddress iServerUDP = InetAddress.getByName(serverAdd);
            DatagramPacket filePack = new DatagramPacket(b, size, iServerUDP, portUDP);
            // 5 Gui den UDPServer o dia chi va cong nhap o tren
            udpClient.send(filePack);
            System.out.println("Da gui goi tin chua file");
            // 6 Nhan 1 goi tin tu UDP server chua 1 mat khau
            byte[] b1 = new byte[1000];
            DatagramPacket passPack = new DatagramPacket(b1, b1.length);
            udpClient.receive(passPack);
            String password = new String(passPack.getData(), 0, passPack.getLength());
            System.out.println("Da nhan duoc mat khau : "+password);
            // 7 Noi ket den TCP Server tai dia chi va cong nhap o tren
            Socket tcpClient = new Socket(serverAdd, portTCP);
            // 8 Gui qua TCP Server ip may cuc bo co xuong hang
            InputStream is = tcpClient.getInputStream();
            OutputStream os = tcpClient.getOutputStream();
            Scanner sc = new Scanner(is);
            PrintStream ps = new PrintStream(os);
            String ipLocal = InetAddress.getLocalHost().getHostAddress();
            System.out.println("Dia chi may cuc bo la : "+ipLocal);
            ps.println(ipLocal);
            System.out.println("Da gui dia chi may cuc bo");
            // 9 Gui qua TCP Server la chuoi dao nguoc mat khau vua nhan
            String passReverse = "";
            for (int i = password.length()-1; i >= 0 ; i--) {
                passReverse += password.charAt(i);
            }
            System.out.println("Mat khau bi dao nguoc la : "+passReverse);
            ps.println(passReverse);
            System.out.println("Da gui mat khau dao nguoc");
            // 10 Nhan 1 chuoi chung thuc
            String authentication = sc.nextLine();
            if(authentication.equals("-ERR")){
                System.out.println("Chung thuc that bai");
            }else{
                System.out.println("Chung thuc thanh cong");
            }
            // Dong cac socket
            tcpClient.close();
            udpClient.close();
        } catch (UnknownHostException hostEx) {
            System.out.println("Sai dia chi Server");
        } catch (SocketException socEx){
            System.out.println("Khong khoi tao duoc UDP Socket");
        } catch (FileNotFoundException fileEx){
            System.out.println("Loi file");
        } catch (IOException ioEx){
            System.out.println("Loi : "+ioEx);
        }
    }
}