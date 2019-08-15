import java.text.DateFormat;
import java.text.SimpleDateFormat;


public interface Parameters {

	/** The template for Date and timestamp **/
	DateFormat DateTemplate = new SimpleDateFormat("HH:mm:ss:SSS");
	
	/** The port numbers for all 5 servers */
	int PortofServer1 = 50001;
	int PortofServer2 = 50002;
	int PortofServer3 = 50003;
	int PortofServer4 = 50004;
	int PortofServer5 = 50005;

	/** The RMI bind Name for all 5 servers */
	String SERVER1 = "Server1";
	String SERVER2 = "Server2";
	String SERVER3 = "Server3";
	String SERVER4 = "Server4";
	String SERVER5 = "Server5";
	 
	/** Sum of servers in the system  **/
	int ServerTotal = 5;

}
