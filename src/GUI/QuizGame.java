package GUI;

import javax.swing.*;

import client.Client;
import component.Question;

import java.awt.event.*;
import java.util.ArrayList;
import java.awt.*;
public class QuizGame extends JPanel implements ActionListener{
	
	private JPanel contentPane;
	private Client client;
	
    JPanel container = new JPanel();
    JRadioButton rb1,rb2,rb3,rb4;
    JButton b1,b2;
    JLabel lb1,lb2;
    ButtonGroup bg;
    String ques[]={"who developed java?","What's the capitol of the UK"};
    String op1[]={"Tim","Birmingham"};
    String op2[]={"steve","Glasgow"};
    String op3[]={"mark","London"};
    String op4[]={"james","Bath"};
    String ans[]={"james","London"};
    int cn;
    
    // TODO: When user enters this page, call controller method getRandomQuestions()
    
    
    
    
    
    QuizGame(JPanel contentPane, Client client){
    	this.contentPane = contentPane;
    	this.client = client;
    	container.setLayout(null);
        
//        fr.setLayout(null);
//        fr.setBounds(250,100,900,600);
//        fr.setResizable(true);//can change the frame size

//        Container c=fr.getContentPane();
//        c.setBackground(Color.yellow);
    	
    	container.setLayout(null); //
	    container.setPreferredSize(new Dimension(900,600));
	    container.setBackground(Color.yellow);
	    add(container);

        /**
         * set label
         */
        lb1=new JLabel(ques[0]);
        lb1.setBounds(50,50,400,40);
        container.add(lb1);
        lb1.setFont(new Font("chiller",Font.HANGING_BASELINE,30));

        /**
         * set the button of menu
         */
        rb1=new JRadioButton(op1[0]);
        rb1.setBounds(100,120,150,30);
        container.add(rb1);

        rb2=new JRadioButton(op2[0]);
        rb2.setBounds(350,120,150,30);
        container.add(rb2);

        rb3=new JRadioButton(op3[0]);
        rb3.setBounds(100,200,150,30);
        container.add(rb3);

        rb4=new JRadioButton(op4[0]);
        rb4.setBounds(350,200,150,30);
        container.add(rb4);

        bg =new ButtonGroup();
        bg.add(rb1);
        bg.add(rb2);
        bg.add(rb3);
        bg.add(rb4);
        rb1.addActionListener(this);
        rb2.addActionListener(this);
        rb3.addActionListener(this);
        rb4.addActionListener(this);


        b1=new JButton("Submit");
        b1.setBounds(100,400,100,30);
        container.add(b1);
        b2=new JButton("Next");
        b2.setBounds(250,400,100,30);
        container.add(b2);
        b1.addActionListener(this);
        b2.addActionListener(this);
        container.setVisible(true);
    }

    public void actionPerformed(ActionEvent e)
    {
    	// TODO: When user click on any of the answer buttons, 
    	//       local variable score + 1 if user clicked the correct answer, otherwise, score + 0
    	//       then move to the next question
    	//       When all questions are done, call client controller method sendGameResult()
    	
    	
    	
    	
        if(e.getSource()==b1)
        {
            String en="";
            if(rb1.isSelected())
                en=rb1.getText();
            if(rb2.isSelected())
                en=rb2.getText();
            if(rb3.isSelected())
                en=rb3.getText();
            if(rb4.isSelected())
                en=rb4.getText();
            if(en.equals(ans[cn]))
                JOptionPane.showMessageDialog(null,"Right Answer");
            else
                JOptionPane.showMessageDialog(null,"Wrong Answer");
        }
        if (e.getSource()==b2)
        {
            cn++;
            lb1.setText(ques[cn]);
            rb1.setText(op1[cn]);
            rb2.setText(op2[cn]);
            rb3.setText(op3[cn]);
            rb4.setText(op4[cn]);
        }
    }
    
    
    public void receiveRandomQuestions(ArrayList<Question> questionList) {
    	// TODO: Store the list of Question in local variables for displaying on the GUI
    	
    	
    	
    }
    
    public void receiveRecordGameResult(boolean isSuccess) {
    	// TODO: if isSuccess = true, move back to chatroom and show the score on the chatroom screen as a conversation
    	
    	
    	
    }
}