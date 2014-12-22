import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;



import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;


public class Interface extends JFrame {

	/**
	 * 
	 */

	@SuppressWarnings("unused")
	private AES aes = null;
	private LSB lsb=null;
	BufferedInputStream in=null;
	private static final long serialVersionUID = 1L;
	JTextPane textPane = null;
	JScrollPane scroll = null;
	private byte Bcont[] = null , Bdata[] = null;//read data from files into them

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
		setSize(800,600);
		textPane = new JTextPane();
		scroll = new JScrollPane (textPane);
		
		JButton btnNewButton = new JButton("Start");
		
		JButton btnNewButton_1 = new JButton("Save as data");
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(scroll, GroupLayout.DEFAULT_SIZE, 764, Short.MAX_VALUE)
						.addComponent(btnNewButton)
						.addComponent(btnNewButton_1))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(scroll, GroupLayout.PREFERRED_SIZE, 193, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btnNewButton_1)
					.addPreferredGap(ComponentPlacement.RELATED, 283, Short.MAX_VALUE)
					.addComponent(btnNewButton)
					.addContainerGap())
		);
		getContentPane().setLayout(groupLayout);
		
		 // Processing button ". . ."
		btnNewButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				
				Thread t = new Thread(new Runnable(){
			        public 	void run() {
			            try {
				     int port = 6666;
				       try {

					       
				         @SuppressWarnings("resource")
						ServerSocket ss = new ServerSocket(port); 
				        textPane.setText("Waiting for a client..."+"\n");


				         Socket socket = ss.accept(); 
				         textPane.setText(textPane.getText()+"Got a client :) ... Finally, someone saw me through all the cover!"+"\n");

				
				         InputStream sin = socket.getInputStream();
				
				         DataInputStream dis = new DataInputStream(sin);		
			         
				         while(true) { 				        		   

				        	
				        	int len = dis.readInt();
				        	System.out.println(len);
				        	byte[] data = new byte[len];
				        	if (len > 0) 
				        		 dis.readFully(data);
				        		    			
				        	
					 		 lsb = new LSB();		                 
							 Bcont=lsb.dencryptByte(data);
							
							 aes = new AES();
						     Bdata = AES.decrypt(Bcont,"1234567812345678".getBytes());
						     
						     StringBuilder text= new StringBuilder();
						     for(int i=0;i<Bdata.length;i++)
								 text.append((char)Bdata[i]);
							 
						    
							 textPane.setText("\n"+textPane.getText()+"char: \n"+text);
							

			                 
			         

				             }
				         
				      } catch(Exception x) { x.printStackTrace(); }		
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
		
		
		 // Choose file to input source
		btnNewButton_1.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae){				
					JFileChooser fileopen = new JFileChooser();  
						int ret = fileopen.showDialog(null, "OPEN");
							if (ret == JFileChooser.APPROVE_OPTION) {
								File cphF = fileopen.getSelectedFile(); 
								try {
									BufferedOutputStream chiperStream = new BufferedOutputStream(new FileOutputStream(cphF));
								

									chiperStream.write(Bdata);				
								 	chiperStream.flush();
								 	chiperStream.close();
								} catch (FileNotFoundException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								//textField_2.setText(cphF.getAbsolutePath());	
 catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
				}}});
	}	

	
	
	
}
