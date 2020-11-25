package GUI;

import client.Client;
import client.ClientController;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Register extends JPanel implements ActionListener {
	
	private Client client;
	private JPanel contentPane;
	
    JPanel container = new JPanel();
    JLabel titleLabel = new JLabel("REGISTERATION");
    private JTextField usernameField = new JTextField();
    private JPasswordField passwordField = new JPasswordField();
    private JPasswordField checkpasswordField = new JPasswordField();
    private JLabel usernamelabel = new JLabel("Username");
    private JLabel passwordlabel = new JLabel("Password");
    private JLabel password2label = new JLabel("PasswordAgain");
    private JLabel usertypelabel = new JLabel("role");
    private JButton submitButton = new JButton("Submit");
    String[] listData = new String[]{"Doctor", "Patient"};
    final JComboBox<String> comboBox = new JComboBox<String>(listData);
    
//    private ClientController model;
    
    /**
     * Constructor 
     */
    public Register(JPanel contentPane, Client client) {
    	this.contentPane = contentPane;
    	this.client = client;
        container.setLayout(null);
        
        addComponentsToContainer();
        usernameField.setBounds(350, 150, 200, 30);
        passwordField.setBounds(350, 200, 200, 30);
        checkpasswordField.setBounds(350, 250, 200, 30);
        usernamelabel.setBounds(250, 150, 100, 30);
        passwordlabel.setBounds(250, 200, 100, 30);
        password2label.setBounds(250, 250, 100, 30);
        usertypelabel.setBounds(250, 320, 100, 30);
        titleLabel.setBounds(350, 100, 100, 30);
        comboBox.setBounds(350,320,100,30);
        submitButton.setBounds(300, 370, 200, 30);
        submitButton.addActionListener(this);
    }
    
    public void addComponentsToContainer(){
    	//Adding each components to the Container
        container.add(usernamelabel);
        container.add(password2label);
        container.add(passwordlabel);
        container.add(usertypelabel);
        container.add(passwordField);
        container.add(checkpasswordField);
        container.add(usernameField);
        container.add(comboBox);
        container.add(titleLabel);
        container.add(submitButton);
        
        container.setLayout(null); //
	    container.setPreferredSize(new Dimension(900,600));
	    container.setBackground(Color.yellow);
	    add(container);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	ClientController ctrl = new ClientController(client);
    	
    	//if the user click submit, store there info to database and direct them to login page
        if (e.getSource() == submitButton) {
        	String userText;
            char[] pwdText;
            char[] pwdText2;
            int role;
            userText = usernameField.getText();
            pwdText = passwordField.getPassword();
            pwdText2 = checkpasswordField.getPassword();
            role = comboBox.getSelectedIndex() + 1; // id of role stored in db starts from 1 instead of 0
            
            ctrl.accountRegister(userText, String.valueOf(pwdText), String.valueOf(pwdText2), role);
            
            
        }
    }

    // Accept result from client controller and activate appropriate action
    public void receiveRegisterResult(boolean isSuccess, String errorMsg) {
    	if (isSuccess) {
			   CardLayout layout = (CardLayout) contentPane.getLayout();
			   JOptionPane.showMessageDialog(this, "Account has been successfully created!");
			   layout.show(contentPane, "Login");
		   } else {
			   JOptionPane.showMessageDialog(this, errorMsg);
		   }
    }
    
}
