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
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

class De1Client{
    public static void main(String[] args) {
        try {
            // 1 Nhap tu ban phim 
            Scanner kb = new Scanner(System.in);
            System.out.print("Nhap dia chi Server : ");
            String serverAdd = kb.nextLine();
            System.out.print("Nhap cong UDP Server : ");
            int portUDPServer = kb.nextInt();
            // 2 Tao UDP Client
            DatagramSocket udpClient = new DatagramSocket();
            // 3 Gui goi UDP den UDP Server
            InetAddress addressLocal = udpClient.getLocalAddress();
            String strAddLocal = addressLocal.toString();
            System.out.println("Dia chi may cuc bo la : "+strAddLocal);
            InetAddress addressServer = InetAddress.getByName(serverAdd);
            DatagramPacket addLocalPack = new DatagramPacket(strAddLocal.getBytes(), strAddLocal.getBytes().length, addressServer, portUDPServer);
            udpClient.send(addLocalPack);
            // 4 Nhan goi tu UDPServer, noi dung la cong TCP Server
            byte[] b = new byte[1000];
            DatagramPacket portPack = new DatagramPacket(b, b.length);
            udpClient.receive(portPack);
            String strPortTCP = new String(portPack.getData(), 0, portPack.getLength());
            System.out.println("Cong cua TCP Server la : "+strPortTCP);
            int portTCP = Integer.parseInt(strPortTCP);
            //5 Nhan goi thu 2 tu UDPServer, noi dung la 1 mat khau
            byte[] b1 = new byte[1000];
            DatagramPacket passPack = new DatagramPacket(b1, b1.length);
            udpClient.receive(passPack);
            String password = new String(passPack.getData(), 0, passPack.getLength());
            System.out.println("Mat khau nhan duoc la : "+ password);
            // 6 Noi ket voi TCPServer
            Socket tcpClient = new Socket(serverAdd, portTCP);
            InputStream is = tcpClient.getInputStream();
            OutputStream os = tcpClient.getOutputStream();
            // 7 Gui qua TCP Server password vua nhan
            Scanner sc = new Scanner(is);
            PrintStream ps = new PrintStream(os);
            ps.println(password);
            // 8 Neu chung thuc that bai nhan chuoi co xuong hang
            String authentication = sc.nextLine();
            if(authentication.equals("-ERR")){
                System.out.println("Chung thuc that bai");
            }else{
                // 9 Chung thuc thanh cong
                int size = Integer.parseInt(authentication);
                System.out.println("Kich thuoc file ket qua la : "+size);
                // 10 Nhan tiep noi dung file
                File file = new File("ketqua.pdf");
                FileOutputStream fos = new FileOutputStream(file);
                byte[] b2 = new byte[size];
                DataInputStream dis = new DataInputStream(is);
                dis.readFully(b2);
                // ghi file 
                fos.write(b2, 0, size);
                System.out.println("Da ghi file thanh cong");
                fos.close();
                dis.close();
            }
            // Dong tat ca socket
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