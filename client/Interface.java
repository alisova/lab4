import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JButton;


public class Interface extends JFrame {

	/**
	 * 
	 */
	 private LSB lsb=null;
	 private File fileContainer = null;
	 private File fileData = null;
	 // The input stream of bytes to be processed for encryption
	 private BufferedInputStream cont = null;
	 // The output stream of bytes to be procssed
	 private BufferedInputStream data = null; 
	 @SuppressWarnings("unused")
	private AES aes = new AES();
	 
	 
	 private byte Bcont[] = null , Bdata[] = null;//read data from files into them

	private static final long serialVersionUID = 1L;
	Socket socket = null;
	JTextField textField = null;
	JTextField textField_1 = null;
	JTextField textField_2 = null;
	JScrollPane scroll = null;
	JButton btnNewButton_1 = null;
	JTextPane textPane = null;
    int serverPort = 6666; 
    String address = "127.0.0.1"; 
                              

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Interface frame = new Interface();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Interface() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000,800);
		textPane = new JTextPane();
		textField_1 = new JTextField ();
		textField_2 = new JTextField ();
		scroll = new JScrollPane (textPane);
		
		JButton btnNewButton = new JButton("Connection");
		
		btnNewButton_1 = new JButton("Send data");
		
		JButton btnNewButton_2 = new JButton("Choose file container Image");
		
		JButton btnNewButton_3 = new JButton("Choose file for data inserting");
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(textField_1, GroupLayout.DEFAULT_SIZE, 529, Short.MAX_VALUE)
								.addComponent(textField_2))
							.addGap(18)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(btnNewButton_2, GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
								.addComponent(btnNewButton_3, GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
								.addComponent(btnNewButton_1, GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
								.addComponent(btnNewButton, GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)))
						.addComponent(scroll, GroupLayout.PREFERRED_SIZE, 944, GroupLayout.PREFERRED_SIZE))
					.addGap(26))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(scroll, GroupLayout.DEFAULT_SIZE, 513, Short.MAX_VALUE)
					.addGap(56)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnNewButton_2))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(textField_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnNewButton_3))
					.addGap(18)
					.addComponent(btnNewButton_1)
					.addGap(18)
					.addComponent(btnNewButton)
					.addGap(36))
		);
		getContentPane().setLayout(groupLayout);
		
		 // Processing button ". . ."
		btnNewButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				
				
	


				        try {
				            InetAddress ipAddress = InetAddress.getByName(address); 
				            textPane.setText("Any of you heard of a socket with IP address " + address + " and port " + serverPort + "?"+"\n");
				            socket = new Socket(ipAddress, serverPort); 
				            textPane.setText(textPane.getText()+"Yes! I just got hold of the program."+"\n");

				        } catch (Exception x) {
				            x.printStackTrace();
				        }
				    	

				        
				
				
				
				}});
		
		
		 // Processing button ". . ."
		btnNewButton_1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){				
				Thread t = new Thread(new Runnable(){
			        public 	void run() {
			            try {
				
		        try {
		           
		            OutputStream sout = socket.getOutputStream();

		     
		            DataOutputStream dos = new DataOutputStream(sout);


		            cont = new BufferedInputStream(new FileInputStream(fileContainer));
		            data = new BufferedInputStream(new FileInputStream(fileData));

		         
		            int len = cont.available();		 		
		 			Bcont = new byte [len];
		 			cont.read(Bcont,0,len);	
		 			cont.close();
		 			
		 			
		            len = data.available();		 		
		 			Bdata = new byte [len];
		 			data.read(Bdata,0,len);	
		 			data.close();
		 			
		 			
		 			aes = new AES();
			    	Bdata = AES.encrypt(Bdata,"1234567812345678".getBytes());
		 		
			    
					lsb = new LSB();
					Bcont=lsb.encryptByte(Bdata,Bcont);
		 			
				
		            dos.writeInt(Bcont.length);	
		         
		            dos.write(Bcont, 0, Bcont.length);

		        } catch (Exception x) {
		            x.printStackTrace();
		        }
 				Thread.sleep(10);
				
	    }
		catch (InterruptedException e) 
		{
			System.err.println ("Error create thread");
			System.exit(1);
		}
	}});
	t.start();
				
				}});
		
		
		 // Processing button "IMAGE"
		btnNewButton_2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){				
				JFileChooser fileopen = new JFileChooser();          
					int ret = fileopen.showDialog(null, "Open file");
						if (ret == JFileChooser.APPROVE_OPTION) {
							fileContainer = fileopen.getSelectedFile(); 
							textField_1.setText(fileContainer.getAbsolutePath());
			}}});
		 // Processing button "DATA"
		btnNewButton_3.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){				
				JFileChooser fileopen = new JFileChooser();          
					int ret = fileopen.showDialog(null, "Open file");
						if (ret == JFileChooser.APPROVE_OPTION) {
							fileData = fileopen.getSelectedFile(); 
							textField_2.setText(fileData.getAbsolutePath());
			}}});
		
		

	}
}
