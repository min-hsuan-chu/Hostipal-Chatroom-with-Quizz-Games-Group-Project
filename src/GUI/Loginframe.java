package GUI;

import javax.swing.*;

import client.Client;
import client.ClientController;

import java.awt.*;
import java.awt.event.*;

/**
 * 
 * @version 2020-02-29
 * 
 */
public class Loginframe extends JPanel implements ActionListener{
	
	private JPanel contentPane;
	private Client client;
	private ClientController ctrl;
	
//	Container container = getContentPane();
	JPanel container = new JPanel();
	JLabel titleLabel = new JLabel("WELCOME TO HOSPITAL CHAT");
	JLabel usernameLabel = new JLabel("USERNAME");
    JLabel passwordLabel = new JLabel("PASSWORD");
    JTextField userTextField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JButton loginButton = new JButton("LOGIN");
    JButton forgotPassword = new JButton("Forgot password");
    JButton registerButton = new JButton("REGISTER");
    JCheckBox showPassword = new JCheckBox("Show Password");
    ImageIcon boy_gif = new ImageIcon("boy.jpg");
	JLabel first_label= new JLabel("Boy Gif",boy_gif, SwingConstants.RIGHT);
//	javax.swing.GroupLayout container = new javax.swing.GroupLayout(this);
	
	public Loginframe() {
	}
	
	  //constructor of LoginFrame() class
	  public Loginframe(JPanel contentPane, Client client) {
		  
		  //Calling setLayoutManger(), setLocationAndSize(), addComponentsToContainer() and addActionEvent() method inside the constructor.
		  this.contentPane = contentPane;
		  this.client = client;
		  this.ctrl = new ClientController(client);
		  
		  setLayoutManager();
          setLocationAndSize();
          addComponentsToContainer();
          addActionEvent();
	  }
	
	   public void setLayoutManager(){
		   
	     //Setting layout manager of Container to null
//		   javax.swing.GroupLayout container = new javax.swing.GroupLayout(this);
	     setLayout(new BorderLayout(10, 10));
	     setBounds(250,100,900,600);
	   }
	
	  public void setLocationAndSize(){
		  
	       //Setting location and Size of each components using setBounds() method.
		   titleLabel.setBounds(585, 80, 200, 50);
	       usernameLabel.setBounds(550,150,100,30);
	       passwordLabel.setBounds(550,220,100,30);
	       userTextField.setBounds(650,150,150,30);
	       passwordField.setBounds(650,220,150,30);
	       showPassword.setBounds(650,250,150,30);
	       loginButton.setBounds(590,330,200,30);
	       registerButton.setBounds(700,300,100,30);
	       forgotPassword.setBounds(550,300,150,30);
	       first_label.setBounds(0, -65, 550, 700);
	   }
	
	   public void addComponentsToContainer(){
		   
	      //Adding each components to the Container
		   container.add(titleLabel);
	       container.add(usernameLabel);
	       container.add(passwordLabel);
	       container.add(userTextField);
	       container.add(passwordField);
	       container.add(showPassword);
	       container.add(loginButton);
	       container.add(registerButton);
	       container.add(forgotPassword);
	       container.add(first_label);
	       
	       container.setLayout(null); //
	       container.setPreferredSize(new Dimension(900,600));
	       container.setBackground(Color.yellow);
	       add(container);
	       
	   }
	   
	   public void addActionEvent(){
		   
	      //adding Action listener to components
	       loginButton.addActionListener(this);
	       registerButton.addActionListener(this);
	       showPassword.addActionListener(this);
	   }
	   
	   
	   /**
		* ActionListener contains only one method actionPerformed(ActionEvent e),
		* so if we are implementing the ActionListener interface in any class, 
		* then we have to override	 its method actionPerformed(ActionEvent e)
		* into that class.
		*/
	   @Override
	    public void actionPerformed(ActionEvent e) {
		   
	        //Coding Part of LOGIN button
	        if (e.getSource() == loginButton) {
	            String userText;
	            char[] pwdText;
	            userText = userTextField.getText();
	            pwdText = passwordField.getPassword();
	            ctrl.login(userText, String.valueOf(pwdText));
	        }
	        
	        //Coding Part of REGISTER button, lead the user to account create page
	        if (e.getSource() == registerButton) {
	            CardLayout layout = (CardLayout) contentPane.getLayout();
	            layout.show(contentPane, "Register");
	        }
	        
	       //Coding Part of showPassword JCheckBox
	        if (e.getSource() == showPassword) {
	            if (showPassword.isSelected()) {
	                passwordField.setEchoChar((char) 0);
	            } else {
	                passwordField.setEchoChar('*');
	            }
	        }
	    }
	   
	   // Accept result from client controller and activate appropriate action
	   public void receiveLoginResult(boolean isSuccess, String errorMsg) {
		   if (isSuccess) {
			   CardLayout layout = (CardLayout) contentPane.getLayout();
			   layout.show(contentPane, "Chatlobby");
		   } else {
			   JOptionPane.showMessageDialog(this, errorMsg);
		   }
	   }

}
