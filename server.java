//java group chat project(TCP/IP over lan or wireless) started on 9th nov first commit->14 nov  by-Shivam Mahendru

//Can add later-> user can talk to indiviual clients on choice(by calling a funtion which asks for permission and 
//initiates the chat), notification sound (add a function in message recived to user),server can remove clients or 
//broadcast a message ,record messages in database on user or server command

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class server{

    static ServerSocket ss;
    static Socket s[] = new Socket[5];    //Increase this and inputfrom client []s to increase member limit

    public static void main(String[] args) {
        new serverframe();
    } 
    }

class serverframe {

    JFrame mainframe;
    JPanel p1;
    static boolean showchatflag = false;
    static JTextArea p2;
    static JComboBox<String> removebox;
    static JLabel onlinelLabel, myip,memberlimitlabel;
    static int onlinemember;

    serverframe() {
        mainframe = new JFrame("Server : Group Chat");
        p1 = new JPanel(new GridLayout(3, 2, 5, 5));
        p2 = new JTextArea();
        

        onlinelLabel = new JLabel("Online :" + onlinemember);  //function 1
        removebox = new JComboBox<>();                                      //function 2
        removebox.setName("all members");
        JCheckBox showchat = new JCheckBox("Show chat");                //function 4
        memberlimitlabel=new JLabel("Maximum member limit- "+Integer.toString(server.s.length));       //function 3
        
        try {
            myip = new JLabel("<html>Share with members!<br/>IP Address- " + InetAddress.getLocalHost().getHostAddress()+"<br/>Port no.- 1000<html/>");       //function 5
        } catch (Exception e) {}

        showchat.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (showchat.isSelected()) {
                    showchatflag = true;
                    p2.setEnabled(true);
                } else {
                    showchatflag = false;
                    p2.setEnabled(false);
                }
            }

        });

        p1.add(onlinelLabel);
        p1.add(removebox);
        p1.add(showchat);
        p1.add(myip);
        p1.add(memberlimitlabel);

        p2.setEditable(false);
        p2.append("Chat started\n");

        JScrollPane scrollarea = new JScrollPane(p2);
        scrollarea.setAutoscrolls(true);
        p2.setEditable(false);


        URL iconURL=getClass().getResource("servericon.png");
        mainframe.setIconImage(new ImageIcon(iconURL).getImage());

        mainframe.setSize(400, 500);
        mainframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainframe.setLocationRelativeTo(null);
        mainframe.setLayout(new GridLayout(2, 1, 20, 20));
        mainframe.add(p1);
        mainframe.add(scrollarea);
        mainframe.setBackground(new Color(195, 195, 195));
        mainframe.setVisible(true);

        try {
            server.ss = new ServerSocket(1000);
            new acceptnewclient();
        } catch (Exception e) {
        }
        
    }
}

class inputfromclient extends Thread {
    int i;
    String clientmsg, clientname;

    inputfromclient(int x, String cname) {
        i = x;
        clientname = cname;                     
    }

    public void run() {
        try {
            InputStream input = server.s[i].getInputStream();
            DataInputStream ip = new DataInputStream(input);
            while (true) {
                clientmsg = ip.readUTF();
                if (clientmsg.equals("")) {
                    continue;
                }
                
                
                if(clientmsg.equals("=====client is typing====="))
                {outputtoclient(clientname, " =====server=====is typing", i);continue;}
                
                if(clientmsg.equals("=====client is stopped====="))
                {outputtoclient(clientname, " =====server=====", i);continue;}
                
                if (serverframe.showchatflag) {  //flag for check box(server function 4)
                    serverframe.p2.append(clientname + ">> " + clientmsg+"\n");
                }
                outputtoclient(clientname, clientmsg, i);
                
                if (clientmsg.equals("/disconnect")) {
                    server.s[i].close();
                    server.s[i]=null;
                    serverframe.onlinelLabel.setText("Online :" + --serverframe.onlinemember);
                    serverframe.removebox.removeItem(clientname);
                    outputtoclient(clientname, " =====server=====left the chat", i);
                    serverframe.p2.append(clientname+"  is offline\n");
                }
            }
        } catch (Exception e) {
            try {
                server.s[i].close();
                server.s[i]=null;
                serverframe.onlinelLabel.setText("Online :" + --serverframe.onlinemember); 
                serverframe.removebox.removeItem(clientname);
                outputtoclient(clientname, " =====server=====left the chat", i);
                serverframe.p2.append(clientname+"  is offline\n");
                
            } catch (Exception e1) {}
        }
    }
    
    static void outputtoclient(String ccname, String ccmsg, int currentclient) {
        try {
            for (Socket otherSocket : server.s) {
                if (otherSocket == server.s[currentclient] || otherSocket==null) {         
                    continue;
                }
                OutputStream output = otherSocket.getOutputStream();
                DataOutputStream out = new DataOutputStream(output);
                out.writeUTF(ccname + " : " + ccmsg);
            }
        } catch (Exception e) {e.printStackTrace();}
    }
}

class acceptnewclient {

    acceptnewclient() throws Exception {
        inputfromclient a[] = new inputfromclient[5];  //To increase members 
        for (int i = 0; i < server.s.length; i++) {
            server.s[i] = server.ss.accept();
            if (server.s[i] == null) {
                continue;
            }
            serverframe.onlinelLabel.setText("Online :" + ++serverframe.onlinemember);

            DataInputStream name = new DataInputStream(server.s[i].getInputStream());
            String cname = name.readUTF();

            DataOutputStream sendonlinedata=new DataOutputStream(server.s[i].getOutputStream());
            sendonlinedata.writeUTF(Integer.toString(serverframe.onlinemember));

            serverframe.p2.append("Connection established with " + cname+"\n");
            inputfromclient.outputtoclient(cname, " =====server=====joined the chat", i);
            serverframe.removebox.addItem(cname);
            a[i] = new inputfromclient(i, cname);
            a[i].start();

        }
    }
}
