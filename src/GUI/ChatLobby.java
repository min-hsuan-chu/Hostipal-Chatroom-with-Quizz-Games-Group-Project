package GUI; 
/*
 * chat lobby, to see who is online and which doctor is available
 */

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import client.Client;
import client.ClientController;
import component.User;

public class ChatLobby extends JPanel implements ActionListener, MouseListener {
	
	private JPanel contentPane;
	private Client client;
	private ClientController ctrl;
	
	static JPanel container = new JPanel();
	
    //Dr.
    JPanel WholePanelForDoctor, PanelForOnlinePeopleInDoctor, PutPatientButtonInDoc;
    JButton DrButtonInDoctor, PatientButtonInDoctor, jpdr1_jb3;
    JScrollPane ScrollPaneForOnline;
//    String ownerId;
    JLabel[] LabelForHowManyOnline;

    //patients
    JPanel WholePanelForPatient, PanelForOnlinePeopleInPatient, PutDrButtonInPatient;
    JButton DrButtonInPatient, PatientButtonInPatient, jpmsr_jb3;
    JScrollPane ScrollPaneForOnlinePatient;
    
    JPanel BarFunctionPanelForDoc = new JPanel();
    JPanel BarFunctionPanelForPatient = new JPanel();
    JPanel WholePanelForBar;
    JButton DrButtonInBar, PatientButtonInBar;
    JButton createGroupBtnForDoc; //create group chat button
    JButton createGroupBtnForPatient;
  	JLabel showwhoisonline; //show how many people online
  	
  	JList<User> jListOfDoc;
  	JList<User> jListOfPatient;
  	User[] docArray;
  	User[] patientArray;
  	
  	Set<User> selectedUserSet = new HashSet<User>();
    
  	
    //set a whole JPanel to CardLayout
    CardLayout cl;
    
    public JPanel BarFunction(JPanel BarFunctionPane, JButton btn) {
    	btn = new JButton("+ Chatroom +");
    	btn.setBounds(10, 540, 50, 50);
    	// If user click + Chatroom + button to create chatroom with selected users
    	btn.addActionListener(new ActionListener() {
    	    @Override
    	    public void actionPerformed(ActionEvent evt) {
    	    	selectedUserSet.add(client.getThisUser());
    	    	// Create chatroom request only send when there are more than 1 user in selected list
    	    	if (selectedUserSet.size() > 1) { 
    	    		ctrl.createChatroom(selectedUserSet);
    	    	} else {
    	    		JOptionPane.showMessageDialog(container, "Please select at least one person in the list you want to chat with.");
    	    	}
    	    }
    	});
      //show how many people online
      showwhoisonline = new JLabel("how many people are online");
      BarFunctionPane.setBackground(Color.yellow);
      BarFunctionPane.setPreferredSize(new Dimension(300, 50));
      BarFunctionPane.add(btn, "Left");
      BarFunctionPane.add(showwhoisonline, "Center");
      return BarFunctionPane;
    }
    
    /**
     * constructor
     */
    public ChatLobby(JPanel contentPane, Client client) {
    	
    	this.contentPane = contentPane;
    	this.client = client;
    	this.ctrl = new ClientController(client);
        
    	initCard1();
        initCard2();
        
        cl = new CardLayout();
        container.setLayout(cl);
        container.add(WholePanelForDoctor, "1");
        container.add(WholePanelForPatient, "2");
        container.setPreferredSize(new Dimension(900, 600));
        add(container);
        
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent evt) {
                ctrl.getOnlineUserList();
            }
        });
    }


    public void initCard1() {
        DrButtonInDoctor = new JButton("Dr");
        PatientButtonInDoctor = new JButton("Patients");
        DrButtonInDoctor.addActionListener(this);
        PatientButtonInDoctor.addActionListener(this);
//        jpdr1_jb3 = new JButton("BlackList");
//        jpdr1_jb3.addActionListener(this);
        
		//whole frame for doctor
        WholePanelForDoctor = new JPanel(new BorderLayout());
        
        //how many doctor
        WholePanelForDoctor.setBackground(Color.yellow);
        
        //put patient button in a new panel doctor 
        PutPatientButtonInDoc = new JPanel(new BorderLayout());
        //put two button in jp_hy3
        PutPatientButtonInDoc.add(PatientButtonInDoctor, "North");
        PutPatientButtonInDoc.add(BarFunction(BarFunctionPanelForDoc, createGroupBtnForDoc), "South");
//        PutPatientButtonInDoc.add(jpdr1_jb3);
        //jpdoctor3.add(jpdr1_jb3);
        
        // add scrollbar automatically 
//        ScrollPaneForOnline = new JScrollPane(PanelForOnlinePeopleInDoctor);
        
        WholePanelForDoctor.add(DrButtonInDoctor, "North");// doctor button
//        WholePanelForDoctor.add(ScrollPaneForOnline, "Center"); //who is online
        WholePanelForDoctor.add(PutPatientButtonInDoc, "South"); //patient button
//        WholePanelForDoctor.add(BarFunction(BarFunctionPanel), BorderLayout.PAGE_END);
//        PanelForOnlinePeopleInDoctor.setPreferredSize(new Dimension(870, 600));
    }
    
    public void initCard2() {
    	
    	DrButtonInPatient = new JButton("Dr");
    	DrButtonInPatient.addActionListener(this);
    	PatientButtonInPatient = new JButton("Patients");
        jpmsr_jb3 = new JButton("BlackList");
        jpmsr_jb3.addActionListener(this);
    	//whole frame for patient
    	WholePanelForPatient = new JPanel(new BorderLayout());
    	
        WholePanelForPatient.setBackground(Color.yellow);
        
      //put doctor button in a new panel patient        
        PutDrButtonInPatient = new JPanel(new GridLayout(2, 1));
        PutDrButtonInPatient.add(DrButtonInPatient);
        PutDrButtonInPatient.add(PatientButtonInPatient);

        //initialize jp_hy1
        WholePanelForPatient.add(PutDrButtonInPatient, "North");
//        WholePanelForPatient.add(ScrollPaneForOnlinePatient, "Center");
        WholePanelForPatient.add(BarFunction(BarFunctionPanelForPatient, createGroupBtnForPatient), "South");
        
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        //if click patients show initCard2()
        if (e.getSource() == PatientButtonInDoctor) {
            cl.show(container, "2");
        }
        //if click doctor show initCard1()
        else if (e.getSource() == DrButtonInPatient) {
            cl.show(container, "1");
        }
        
    }
    
    public void receiveOnlineUserList(Set<User> userSet) {
//    	if (ScrollPaneForOnline != null) WholePanelForDoctor.remove(ScrollPaneForOnline);
//    	if (ScrollPaneForOnlinePatient != null) WholePanelForPatient.remove(ScrollPaneForOnlinePatient);
    	
    	Set<User> docList = new HashSet<User>();
    	Set<User> patientList = new HashSet<User>();
    	for (User user : userSet) {
    		if (user.getRole() == 1) {
    			docList.add(user);
    		} else {
    			patientList.add(user);
    		}
    	}
    	docArray = docList.toArray(new User[docList.size()]);
    	patientArray = patientList.toArray(new User[patientList.size()]);
    	
    	jListOfDoc = new JList<User>(docArray);
    	jListOfDoc.setCellRenderer(new CheckListRenderer());
    	jListOfDoc.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    	jListOfDoc.addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent event) {
            JList list = (JList) event.getSource();
            
//            int index = list.locationToIndex(event.getPoint());// Get index of item
            int index = list.getSelectedIndex();
//                                                               // clicked
            User item = (User) list.getSelectedValue();
            User thisUser = client.getThisUser();
            if (item.getIsAvailable() && item.compareTo(thisUser) != 0) {
            	item.setIsSelected(!item.getIsSelected()); // Toggle selected state
	        	if (item.getIsSelected()) {
	        		selectedUserSet.add(item);
	        	} else {
	        		selectedUserSet.remove(item);
	        	}
            }
            list.repaint(list.getCellBounds(index, index));// Repaint cell
          }
        });
    	
//    	GridLayout GL = new GridLayout(15, 1, 4, 4);
//    	if (docArray.length > 15) {
//    		GL.setRows(docArray.length);
//    	}
//    	PanelForOnlinePeopleInDoctor = new JPanel(GL);
//        PanelForOnlinePeopleInDoctor.setBackground(Color.yellow);
//    	LabelForHowManyOnline = new JLabel[docArray.length];
//        int i = 0;
//    	for (User doc : docArray) {
//    		LabelForHowManyOnline[i] = new JLabel(doc.getUsername() + " - " + (doc.getIsAvailable() ? "(Available)" : "(In Chat)"), JLabel.LEFT);
//    		if (doc.getIsAvailable()) {
//    			LabelForHowManyOnline[i].setEnabled(true);
//    		} else {
//    			LabelForHowManyOnline[i].setEnabled(false);
//    		}
//    		LabelForHowManyOnline[i].addMouseListener(this);
//    		PanelForOnlinePeopleInDoctor.add(LabelForHowManyOnline[i]);
//    		i++;
//    	}
//    	PanelForOnlinePeopleInDoctor.setPreferredSize(new Dimension(870, 600));
    	jListOfDoc.setBackground(Color.yellow);
    	ScrollPaneForOnline = new JScrollPane(jListOfDoc);
    	WholePanelForDoctor.add(ScrollPaneForOnline, "Center"); //who is online
    	WholePanelForDoctor.validate();
    	WholePanelForDoctor.repaint();
    	
    	jListOfPatient = new JList<User>(patientArray);
    	jListOfPatient.setCellRenderer(new CheckListRenderer());
    	jListOfPatient.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    	jListOfPatient.addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent event) {
            JList list = (JList) event.getSource();
//            int index = list.locationToIndex(event.getPoint());// Get index of item
            int index = list.getSelectedIndex();
                                                               // clicked
            User item = (User) list.getSelectedValue();
            User thisUser = client.getThisUser();
            if (item.getIsAvailable() && item.compareTo(thisUser) != 0) {
	        	item.setIsSelected(!item.getIsSelected()); // Toggle selected state
	        	if (item.getIsSelected()) {
	        		selectedUserSet.add(item);
	        	} else {
	        		selectedUserSet.remove(item);
	        	}
            }
            list.repaint(list.getCellBounds(index, index));// Repaint cell
          }
        });
        
//    	GridLayout GLForP = new GridLayout(15, 1, 4, 4);
//    	if (patientArray.length > 15) {
//    		GLForP.setRows(patientArray.length);
//    	}
//        PanelForOnlinePeopleInPatient = new JPanel(GLForP);
//    	PanelForOnlinePeopleInPatient.setBackground(Color.yellow);
//        JLabel[] LabelForHowManyOnlineP = new JLabel[patientArray.length];
//        int j = 0;
//    	for (User patient : patientArray) {
//    		LabelForHowManyOnlineP[j] = new JLabel(patient.getUsername() + " - " + (patient.getIsAvailable() ? "(Available)" : "(In Chat)"), JLabel.LEFT);
//    		if (patient.getIsAvailable()) {
//    			LabelForHowManyOnlineP[j].setEnabled(true);
//    		} else {
//    			LabelForHowManyOnlineP[j].setEnabled(false);
//    		}
//    		LabelForHowManyOnlineP[j].addMouseListener(this);
//    		PanelForOnlinePeopleInPatient.add(LabelForHowManyOnlineP[j]);
//    		j++;
//    	}
//    	 PanelForOnlinePeopleInPatient.setPreferredSize(new Dimension(870, 600));
    	jListOfPatient.setBackground(Color.yellow);
    	ScrollPaneForOnlinePatient = new JScrollPane(jListOfPatient);
    	WholePanelForPatient.add(ScrollPaneForOnlinePatient, "Center");
    	WholePanelForPatient.validate();
    	WholePanelForPatient.repaint();
        
    }
    
    public void receiveCreateChatroomResult() {
    	JOptionPane.showMessageDialog(container, "You are now opening a chat.");
    	CardLayout layout = (CardLayout) contentPane.getLayout();
	    layout.show(contentPane, "Chatroom");
    }
    
    public void receiveUpdateAvailability(Set<User> userSet) {
    	
    	List<String> usernameList = new ArrayList<String>();
    	for (User u : userSet) {
    		usernameList.add(u.getUsername());
    	}
    	
    	DefaultListModel listModelForDoc = new DefaultListModel();
    	for (int i = 0; i < docArray.length; i++) {
    		if (usernameList.contains(docArray[i].getUsername())) {
    			docArray[i].setIsAvailable(false);
    		}
    		listModelForDoc.addElement((User)docArray[i]);
    	}
    	jListOfDoc.setModel(listModelForDoc);
    	
    	DefaultListModel listModelForPatient = new DefaultListModel();
    	for (int j = 0; j < patientArray.length; j++) {
    		if (usernameList.contains(patientArray[j].getUsername())) {
    			patientArray[j].setIsAvailable(false);
    		}
    		listModelForPatient.addElement((User)patientArray[j]);
    	}
    	jListOfPatient.setModel(listModelForPatient);
    	
    	
    }
    
    public void receiveUpdateOnlineUserList(User user) {
    	int roleId = user.getRole();
    	if (roleId == 1) {
    		DefaultListModel listModelForDoc = new DefaultListModel();
        	for (int x = 0; x < docArray.length; x++) {
        		listModelForDoc.addElement((User) docArray[x]);
        	}
        	listModelForDoc.addElement((User) user);
        	jListOfDoc.setModel(listModelForDoc);
    	} else {
    		DefaultListModel listModelForPatient = new DefaultListModel();
        	for (int j = 0; j < patientArray.length; j++) {
        		listModelForPatient.addElement((User)patientArray[j]);
        	}
        	listModelForPatient.addElement((User) user);
        	jListOfPatient.setModel(listModelForPatient);
    	}
    	
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        JLabel jl = (JLabel) e.getSource();
        jl.setForeground(Color.red);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        JLabel jl = (JLabel) e.getSource();
        jl.setForeground(Color.black);
    }

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	class CheckListRenderer extends JCheckBox implements ListCellRenderer {
	  public Component getListCellRendererComponent(JList list, Object value,
	      int index, boolean isSelected, boolean hasFocus) {
		  User thisUser = client.getThisUser();
		  String status = "";
	    if (((User) value).getIsAvailable() && ((User) value).compareTo(thisUser) != 0) {
	    	setSelected(((User) value).getIsSelected());
	    	setEnabled(list.isEnabled());
	    	status = "(Available)";
	    } else {
	    	setSelected(false);
	    	setEnabled(false);
    		if (((User) value).compareTo(thisUser) == 0) {
	    		status = "(You)";
	    	} else {
	    		status = "(In Chat)";
	    	}
	    }
	    setFont(list.getFont());
	    setBackground(list.getBackground());
	    setForeground(list.getForeground());
	    setText(((User) value).getUsername() + " - " + status);
	    return this;
	  }
	}
}