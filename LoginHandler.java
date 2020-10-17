package gui;
//login code sourced from https://www.onlinetutorialspoint.com/java/java-swing-login-example.html

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import dataBaseAccess.Login;

//CODE UNUSED, OUT OF TIME

//would have based my login form on this code, and authority levels using the database

//Updated Add credentials if Admin/Password Match -JH
public class LoginHandler extends JDialog implements ActionListener {

    static JPanel panel;
    JLabel user_label, password_label, message;
    JTextField userName_text;
    JPasswordField password_text;
    JButton submit, cancel;
    
    public LoginHandler() {
        
        // User Label
        user_label = new JLabel();
        user_label.setText("User Name :");
        userName_text = new JTextField();
        
        // Password

        password_label = new JLabel();
        password_label.setText("Password :");
        password_text = new JPasswordField();

        // Submit
        submit = new JButton("SUBMIT");

        panel = new JPanel(new GridLayout(3, 1));

        panel.add(user_label);
        panel.add(userName_text);
        panel.add(password_label);
        panel.add(password_text);

        message = new JLabel();
        panel.add(message);
        panel.add(submit);
        
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        // Adding the listeners to components..
        submit.addActionListener(this);
        add(panel, BorderLayout.CENTER);
        setTitle("Please Login Here !");
        setSize(300, 100);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String userName = userName_text.getText();
        String password = password_text.getText();
        if (userName.trim().equals("admin") && password.trim().equals("admin")) {
        	this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        	Login.updateCredentials(userName, password); // Updates Global Credential List
        } else {
            message.setText(" Invalid user.. ");
        }

    }
    
    public static JPanel getPanel()
    {
    	return panel;
    }

}