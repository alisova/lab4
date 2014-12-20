
public class Convert {

public Convert ()
{
	
	
}


	int pow ( int a , int b)
	{
		if ( b == 0 )
			return 1;
				if ( b == 1 )
					return a;
		
		int c = a;
			for( int i = 1 ; i < b ; i++ )
				a *= c;	
					return a;
	}
//FUNCTION CONVERT 4 BYTES TO INT	
	public int byteToInt (byte b1,byte b2,byte b3,byte b4 )
	{

			int len=0;
			for(int i=7;i>=0;i--) if	( ( b1  >> (7-i) & 1 )== 1)	len+=   pow ( 2 , 7-i ) ;
				for(int i=7;i>=0;i--) if	( ( b2 >> (7-i) & 1 )== 1)	len+=   pow ( 2 , 15-i ) ;
					for(int i=7;i>=0;i--) if	( ( b3 >> (7-i) & 1 )== 1)	len+=   pow ( 2 , 23-i ) ;
						for(int i=7;i>=0;i--) if	( ( b4 >> (7-i) & 1 )== 1)	len+=   pow ( 2 , 31-i ) ;
			return len;
	}
	
//FUNCTION CONVERT INT TO 4 BYTES MASSIVE	
	public  byte [] intToByte (int len )
	{
			byte b [ ] = new byte [ 4 ];
			
			b [ 0 ] = 0 ; b [ 1 ] = 0 ; b [ 2 ] = 0 ; b [ 3 ] = 0;
			
			int i = 0 , k = -1;
			
			while ( len > 0 ){

				if ( i % 8 == 0 ) k++;
					
				if ( len % 2 == 1 )
						b [ k ] += pow ( 2 ,  i-8*k );		
				
				len /= 2;	
				i++;
							}
		return b;
		
	}
}
