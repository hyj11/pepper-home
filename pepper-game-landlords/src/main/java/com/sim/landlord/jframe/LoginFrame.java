package com.sim.landlord.jframe;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @Author: Huang Yujiao
 * @Date: 2021/9/29 9:36
 * @Desc:
 */
public class LoginFrame {
    private JPanel panel1;
    private JTextField TextField;
    private JPasswordField PasswordField;
    private JButton loginButton;
    private JLabel nameLabel;
    private JLabel passwordlabel;

    public LoginFrame() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("LoginFrame");
        JPanel rootPane=new LoginFrame().panel1;
        frame.setContentPane(new LoginFrame().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(300, 300);
        frame.setLocationRelativeTo(rootPane);//居中
        frame.setVisible(true);
    }
}
