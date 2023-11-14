//EXTRA file for client interface designing

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.ScrollPane;
import java.awt.RenderingHints.Key;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataOutputStream;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout.Group;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class testclass {
    public static void main(String[] args) {
        String username;
    JFrame framechat;
    JPanel topPanel;
    JTextArea middlePanel;
    JTextField bottomPanel;

        // username=usnm;

        topPanel=new JPanel(new BorderLayout(30, 10));
        middlePanel=new JTextArea();
        bottomPanel=new JTextField();
        // bottomPanel.addKeyListener(new KeyListener() {

        //     @Override
        //     public void keyTyped(KeyEvent e) {
        //         // TODO Auto-generated method stub
        //         throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
        //     }

        //     @Override
        //     public void keyPressed(KeyEvent e) {
        //         // TODO Auto-generated method stub
        //         System.out.println(e.getKeyCode());
        //         throw new UnsupportedOperationException("Unimplemented method 'keyPressed'");
        //     }

        //     @Override
        //     public void keyReleased(KeyEvent e) {
        //         // TODO Auto-generated method stub
        //         throw new UnsupportedOperationException("Unimplemented method 'keyReleased'");
        //     }
            
        // });

        middlePanel.setLineWrap(true);
        middlePanel.setEnabled(false);
        middlePanel.setBackground(new Color(100, 100, 100));

        topPanel.add(new JLabel(new ImageIcon("icon.png")),BorderLayout.CENTER);
        topPanel.add(new JLabel("Welcome"),BorderLayout.SOUTH);
        topPanel.add(new JLabel("online 5"),BorderLayout.NORTH);
        topPanel.add(new JLabel("online 5",SwingConstants.CENTER),BorderLayout.EAST);
        // topPanel.add(new JLabel(new ImageIcon("image_2023-11-11_115106280.ico")));


        bottomPanel.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    if(e.equals())
                    System.out.println(e.getDocument());
                    sendtypingstatus();
                } catch (Exception e1) {}
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                try {
                    
                    System.out.println(e.getDocument());
                    sendtypingstatus();
                } catch (Exception e2) {}
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                try {
                    // sendtypingstatus();
                } catch (Exception e3) {}
            }
            
            public void sendtypingstatus() throws Exception{
                System.out.println("he is typing");
            }
            
        });

        


        framechat=new JFrame("Chat Area");
        framechat.setSize(500, 400);
        framechat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        framechat.setLayout(new BorderLayout());
        framechat.setBackground(Color.gray);
        // framechat.setResizable(false);
        framechat.setLocationRelativeTo(null);
        framechat.add(topPanel,BorderLayout.NORTH);
        // framechat.add(middlePanel,BorderLayout.CENTER);
        JScrollPane scrollmid=new JScrollPane(middlePanel);
        framechat.add(scrollmid,BorderLayout.CENTER);
        framechat.add(bottomPanel,BorderLayout.SOUTH);
        framechat.setVisible(true);
    }

}
