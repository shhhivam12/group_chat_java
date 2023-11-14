//client side of shivam's java group chat project
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class client {
    static Socket s;

    public static void main(String[] args) throws Exception {
        new loginframe();
    }

}

class loginframe {
    JFrame framelogin;
    JPanel loginpanel;
    JTextField portno, username, ipadd;
    JLabel keytext, nametext, iptext, Logintext, errormsg;
    JButton connect;
    int onlinemembers;

    loginframe() throws Exception {
        URL iconURL=getClass().getResource("icon.png");
        framelogin = new JFrame("Login");
        framelogin.setSize(400, 500);
        framelogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        framelogin.setLayout(null);
        framelogin.setBackground(Color.gray);
        framelogin.setResizable(false);
        framelogin.setIconImage(new ImageIcon(iconURL).getImage());
        

        JLabel icon = new JLabel(new ImageIcon("icon.png"));
        icon.setBounds(155, 10, 100, 100);
        errormsg = new JLabel();

        nametext = new JLabel("Enter your name :");
        keytext = new JLabel("Enter portnumber :");
        iptext = new JLabel("Server's ip key  :");
        Logintext = new JLabel("Login");

        nametext.setBounds(15, 100, 120, 15);
        iptext.setBounds(15, 135, 120, 15);
        keytext.setBounds(15, 170, 120, 15);
        Logintext.setBounds(110, 20, 100, 50);

        errormsg.setBounds(75, 200, 180, 15);
        errormsg.setForeground(Color.red);
        errormsg.setVisible(false);
        Logintext.setFont(new Font(null, 0, 30));

        username = new JTextField();
        portno = new JTextField();
        ipadd = new JTextField();

        username.setBounds(125, 100, 150, 20);
        ipadd.setBounds(125, 135, 150, 20);
        portno.setBounds(125, 170, 150, 20);

        connect = new JButton("Connect to Chat");
        connect.setBounds(65, 230, 150, 20);
        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (username.getText().equals("") || portno.getText().equals("") || ipadd.getText().equals("")) {
                    errormsg.setVisible(true);
                    errormsg.setText("*This field cannot be empty");
                } else {

                    try {
                        checkcredentials(username.getText(), Integer.parseInt(portno.getText()),
                                InetAddress.getByName(ipadd.getText()));
                    } catch (Exception excp) {
                    }
                }
            }
        });

        loginpanel = new JPanel();
        loginpanel.setBackground(Color.white);
        loginpanel.setBounds(45, 120, 300, 280);
        loginpanel.setLayout(null);
        loginpanel.add(username);
        loginpanel.add(portno);
        loginpanel.add(ipadd);
        loginpanel.add(iptext);
        loginpanel.add(nametext);
        loginpanel.add(keytext);
        loginpanel.add(connect);
        loginpanel.add(Logintext);
        loginpanel.add(errormsg);

        framelogin.setTitle("Group chat ");
        framelogin.add(loginpanel);
        framelogin.add(icon);
        framelogin.setLocationRelativeTo(null);
        framelogin.setVisible(true);

    }

    void checkcredentials(String username, int serverkey, InetAddress ip) {
        try {
            client.s = new Socket(ip, serverkey);
            if (client.s.isConnected()) {

                DataOutputStream name = new DataOutputStream(client.s.getOutputStream());
                name.writeUTF(username);

                DataInputStream online = new DataInputStream(client.s.getInputStream());
                onlinemembers = Integer.parseInt(online.readUTF());

                chatframe nowchat = new chatframe(username, onlinemembers);
                nowchat.start();
                framelogin.dispose();
            } else {
                errormsg.setVisible(true);
                errormsg.setText("Cannot connect to the server at the moment. Recheck details");
            }

        } catch (Exception e) {
            errormsg.setVisible(true);
            errormsg.setText(e.getMessage());
        }
    }
}

class chatframe extends Thread {
    static String username;
    JFrame framechat;
    JPanel topPanel;
    static JTextArea middlePanel;
    static JTextField bottomPanel;
    static JLabel onlinemember, typingstatus;
    static int totalonline;

    chatframe(String usnm, int onlinemembers) {

        totalonline = onlinemembers;
        onlinemember = new JLabel("Online - " + totalonline);
        username = usnm;

        topPanel = new JPanel(new BorderLayout(20, 20));

        middlePanel = new JTextArea();
        JScrollPane scrollmid = new JScrollPane(middlePanel);
        scrollmid.setAutoscrolls(true);
        middlePanel.setLineWrap(true);
        middlePanel.setEditable(false);
        middlePanel.setText(usnm + " is online\nType /disconnect to disconnect from the chat :)\n");
        

        bottomPanel = new JTextField();
        bottomPanel.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                try {
                    if (e.getKeyCode() == 10) {
                        DataOutputStream out = new DataOutputStream(client.s.getOutputStream());
                        if (chatframe.bottomPanel.getText().equals("")) {
                        } else if (chatframe.bottomPanel.getText().equals("/disconnect")) {
                            chatframe.middlePanel.setText("\nDisconnected");
                            out.close();
                            client.s.close();
                        } else {
                            chatframe.middlePanel.append("Me : " + chatframe.bottomPanel.getText() + "\n");
                            out.writeUTF(chatframe.bottomPanel.getText());

                        }
                        chatframe.bottomPanel.setText("");
                    }

                } catch (Exception exc) {
                }
            }
        });

        bottomPanel.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    sendtypingstatus();

                } catch (Exception e1) {
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                try {
                    sendstopstatus();
                } catch (Exception e2) {
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                try {
                    sendtypingstatus();
                } catch (Exception e3) {
                }
            }

            public void sendtypingstatus() throws Exception {
                DataOutputStream out = new DataOutputStream(client.s.getOutputStream());
                out.writeUTF("=====client is typing=====");
            }

            public void sendstopstatus() throws Exception {
                DataOutputStream out = new DataOutputStream(client.s.getOutputStream());
                out.writeUTF("=====client is stopped=====");
            }

        });

        typingstatus = new JLabel("");

        topPanel.add(new JLabel(new ImageIcon("icon.png")), BorderLayout.CENTER);
        topPanel.add(onlinemember, BorderLayout.EAST);
        topPanel.add(typingstatus, BorderLayout.SOUTH);

        URL iconURL=getClass().getResource("icon.png");
        framechat.setIconImage(new ImageIcon(iconURL).getImage());
        framechat = new JFrame("Group Chat : " + username);
        framechat.setSize(500, 400);
        framechat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        framechat.setLayout(new BorderLayout());
        framechat.setLocationRelativeTo(null);
        framechat.add(topPanel, BorderLayout.NORTH);
        framechat.add(scrollmid, BorderLayout.CENTER);
        framechat.add(bottomPanel, BorderLayout.SOUTH);
    }

    public void run() {
        framechat.setVisible(true);
        inputfromserver o1 = new inputfromserver();
        o1.start();
    }
}

class inputfromserver extends Thread {

    public void run() {
        try {
            InputStream input = client.s.getInputStream();
            DataInputStream ip = new DataInputStream(input);
            while (true) {
                String servermsg = ip.readUTF();

                if (servermsg.endsWith("joined the chat") && servermsg.contains("=====server=====")) {
                    chatframe.onlinemember.setText("Online - " + (++chatframe.totalonline));
                    servermsg = servermsg.replaceAll("=====server=====", "");
                }
                if (servermsg.endsWith("left the chat") && servermsg.contains("=====server=====")) {
                    chatframe.onlinemember.setText("Online - " + (--chatframe.totalonline));
                    servermsg = servermsg.replaceAll("=====server=====", "");
                }
                if (servermsg.endsWith("is typing") && servermsg.contains("=====server=====")) {
                    servermsg = servermsg.replaceAll("=====server=====", "");
                    chatframe.typingstatus.setText(servermsg);
                    // servermsg="null";
                    continue;
                }
                if (servermsg.contains("=====server=====")) {
                    chatframe.typingstatus.setText("");
                    continue;
                }

                chatframe.typingstatus.setText("");
                chatframe.middlePanel.append(servermsg + "\n");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
