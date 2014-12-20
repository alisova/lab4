import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JProgressBar;


public class LSB {
 private File fileContainer = null;
 private File fileSource = null;
 // The input stream of bytes to be processed for encryption
 private BufferedInputStream in = null;

 // The output stream of bytes to be procssed
 private BufferedInputStream outR = null;
 
 // The output stream of bytes to be procssed
 private BufferedOutputStream outW = null; 
 
 private byte Bin[] = null , Bout[] = null;//read data from files into them

 private JProgressBar progressBar = new JProgressBar();
 
 private long  b = 0;//size of fileI and fileO
 
 private Convert con = new Convert();
 public LSB(){
	 
 }
 public LSB(File in, File out, JProgressBar jb)
 {
 fileSource = in;
 fileContainer  = out;
 progressBar = jb;
 }
 public LSB(File in, File out)
 {
 fileSource = in;
 fileContainer  = out;
 progressBar = new JProgressBar();
  }
 
 public void decrypt (){
		Thread t = new Thread(new Runnable(){
	        public 	void run() {
	            try {
			
	        		try
	                {
	                    in = new BufferedInputStream(new FileInputStream(fileSource));
	                }
	                catch (IOException fnf)
	                {
	                    System.err.println("Input container file not found ["+fileSource.getAbsolutePath()+"]");
	                    System.exit(1);
	                }
	
	                try
	                {
	                    outW = new BufferedOutputStream(new FileOutputStream(fileContainer));
	                }
	                catch (IOException fnf)
	                {
	                    System.err.println("Output source file not created ["+fileContainer.getAbsolutePath()+"]");
	                    System.exit(1);
	                }
	//READ DATA FROM CONTAINER FILE
	        		try 
	        		{
	        			int len = in.available();
	        			b=len;
	        			Bin = new byte [len];
	        			in.read(Bin,0,len);
	        			
	        		} 
	        		catch (IOException e1)
	        		{
	        			System.err.println("Error in file source Buffered read ( of encrypt)");
	        			System.exit(1);
	        		}
	      
	        		progressBar.setMaximum((int)b+(int)b/5);
	//LSB PULL ALGORITHM
	        		
			byte b1=Bin[(int)b-1];
				byte b2=Bin[(int)b-2];
					byte b3=Bin[(int)b-3];
						byte b4=Bin[(int)b-4];
			
			int len = con.byteToInt(b1,b2,b3,b4);
			System.out.println("len pull="+len);
		byte W = 0;
		
		Bout= new byte [len];
		
		for(int i=1000,j=0,m=0; i < 1000+len*8; i++ )
		{
			if	( ( Bin [i] >> 0 & 1 )== 1)
				W+=   con .pow ( 2 , 7 - j ) ;	
			j++;
			if(j==8)
			{
				Bout[m]=W;
					m++;
						W=0;
							j=0;
							progressBar.setValue(m);
			}
		}
	//CREATE SOURCE FILE
			try 
			{
				outW.write(Bout);
				progressBar.setValue((int)b+(int)b/5);
				System.err.println("DONE COMPLETE");
			 } 
			catch (IOException e) 
			{
				System.err.println("Error of write in container file");
				System.exit(1);
			}

	//CLOSE CONTAINER FILE	
			try 
			{
				in.close();
			} 
			catch (IOException e) 
			{
				System.err.println ("Error of close source file");
				System.exit(1);
			}
	//CLOSE SOURCE FILE
			try 
			{
				outW.close();
			} 
			catch (IOException e) 
			{
				System.err.println ("Error of close conteiner file");
				System.exit(1);
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

		}
 
 public void encrypt (){
		
	 Thread t = new Thread(new Runnable(){
	     public 	void run() {
	     try 
	     {
	    	 System.err.println ("PROGRESS . . .");
			
	 		try
	         {
	             in = new BufferedInputStream(new FileInputStream(fileSource));
	         }
	         catch (IOException fnf)
	         {
	             System.err.println("Input file not found ["+fileSource.getAbsolutePath()+"]");
	             System.exit(1);
	         }
	
	         try
	         {
	             outR = new BufferedInputStream(new FileInputStream(fileContainer));
	         }
	         catch (IOException fnf)
	         {
	             System.err.println("Output file not created ["+fileContainer.getAbsolutePath()+"]");
	             System.exit(1);
	         }
	 //READ DATA FROM SOURCE FILE
	 		try 
	 		{
	 			int len = in.available();
	 			b=len;
	 			Bin = new byte [len];
	 			in.read(Bin,0,len);
	 			
	 		} 
	 		catch (IOException e1)
	 		{
	 			System.err.println ("Error in file source Buffered read ( of encrypt)");
	 			System.exit(1);
	 		}
	 //READ DATA FROM CONTAINER FILE
	 		try 
	 		{
	 			int len = outR.available();
	 			Bout=new byte [len];
	 			outR.read(Bout,0,len);
	 		} 
	 		catch (IOException e1)
	 		{
	 			System.err.println ("Error in file container Buffered read ( of encrypt)");
	 			System.exit(1);						 
	 		}
	 		
	 		progressBar.setMaximum((int)b+(int)b/5);
	 //LSB INSERT BITS , FIRST 2540 bytes SKIP TO SAVE HEADER FILE
	 	for ( int i = 0, p=0 ; i < b ;  )
	 	{
	 		
	 		if(p>=1000)
	 		{	
	 			for(int l=0;l<8;l++){

	 				if( ( Bin[i] >> (7-l) & 1) == 1 
	 						&& ( Bout[p] >> 0 & 1) == 0 )
	 					Bout[p]++;	
	 				
	 				if( ( Bin[i] >> (7-l) & 1) == 0 
	 						&& ( Bout[p] >> 0 & 1) == 1 )
	 					Bout[p]--;
	 				
	 				
	 				
	 				p++;
	 								}
	 				i++;
	 		}
	 		else
	 			p++;
	 	}
	 //CLOSE SOURCE FILE	
	 	try 
	 	{
	 		in.close();
	 	} 
	 	catch (IOException e) 
	 	{
	 		System.err.println ("Error of close container file");
	 		System.exit(1);
	 	}
	 //CLOSE CONTAINER FILE
	 	try 
	 	{
	 		outR.close();
	 	} 
	 	catch (IOException e) 
	 	{
	 		System.err.println ("Error of close source file");
	 		System.exit(1);
	 	}
	 //OPEN CONTAINER FILE FOR REWRITE
	 	try
	     {
	         outW = new BufferedOutputStream(new FileOutputStream(fileContainer));
	     }
	     catch (IOException fnf)
	     {
	         System.err.println("Output file  write not found ["+fileContainer.getAbsolutePath()+"]");
	         System.exit(1);
	     }
	 //REWRITE CONTEINER FILE
	 	try 
	 	{
	 		outW.write(Bout);
	 		System.out.println("len push="+b);
	 		byte bw[]=con.intToByte((int)b);
	 			outW.write(bw[3]);
	 				outW.write(bw[2]);
	 					outW.write(bw[1]);
	 						outW.write(bw[0]);
	 		progressBar.setValue((int)b+(int)b/5);
	 		System.err.println("DONE COMPLETE");
	 	 } 
	 	catch (IOException e) 
	 	{
	 		System.err.println("Error of write in container file");
	 		System.exit(1);
	 	}
	 //CLOSE CONTEINER FILE 
	 	try 
	 	{
	 		outW.close();
	 	} 
	 	catch (IOException e)
	 	{
	 		System.err.println ("Error of close container file");
	 		System.exit(1);
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

	 	}
 
 
 
 public byte [] encryptByte (byte Bin[],byte Bout[]){
	 this.Bin = Bin;
	 this.Bout = Bout;
	 int len = Bin.length;
	 byte[] ret = new byte [Bout.length+4];
	 this.b=len;


	 		
	
	 //LSB INSERT BITS , FIRST 1000 bytes SKIP TO SAVE HEADER FILE
	 	for ( int i = 0, p=0 ; i < b ;  )
	 	{
	 		
	 		if(p>=1000)
	 		{	
	 			for(int l=0;l<8;l++){

	 				if( ( Bin[i] >> (7-l) & 1) == 1 
	 						&& ( Bout[p] >> 0 & 1) == 0 )
	 					Bout[p]++;	
	 				
	 				if( ( Bin[i] >> (7-l) & 1) == 0 
	 						&& ( Bout[p] >> 0 & 1) == 1 )
	 					Bout[p]--;
	 				
	 				
	 				
	 				p++;
	 								}
	 				i++;
	 		}
	 		else
	 			p++;
	 	}
	


	 	byte bw[]=con.intToByte((int)b); 	
	 
	 		int i=0;
	 			for(;i<Bout.length;i++)
	 				ret[i]=Bout[i];
	 		 			ret[i]=bw[3];
	 						ret[i+1]=bw[2];
	 							ret[i+2]=bw[1];
		 							ret[i+3]=bw[0];
		 						
		 	
	 			return ret;

		

	 	}
 
 public byte [] dencryptByte (byte Bin1[]){
	this.Bin = new byte [Bin1.length];
	 for(int i=0;i<Bin1.length;i++)
		 this.Bin[i]=Bin1[i];
		 
	 this.b=Bin.length;
	 

	 		
	
	    	//LSB PULL ALGORITHM
     		
				byte b1=Bin[(int)b-1];
					byte b2=Bin[(int)b-2];
						byte b3=Bin[(int)b-3];
							byte b4=Bin[(int)b-4];
				
				int len = con.byteToInt(b1,b2,b3,b4);
				
		
			byte W = 0;
			
			Bout= new byte [len];
			
			for(int i=1000,j=0,m=0; i < 1000+len*8; i++ )
			{
				if	( ( Bin [i] >> 0 & 1 )== 1)
					W+=   con .pow ( 2 , 7 - j ) ;	
				j++;
				if(j==8)
				{
					Bout[m]=W;
						m++;
							W=0;
								j=0;
							//	progressBar.setValue(m);
				}
			}
	

		 	

		return Bout;

	 	}
 
 
}
