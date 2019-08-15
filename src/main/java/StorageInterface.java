

import java.net.SocketTimeoutException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.UUID;

public interface StorageInterface extends Remote {
	
    public HashMap<UUID, CrawlResult> KeyValueStore(CrawlTask crawTask, String text
    		,String functionality) throws RemoteException;
	   public boolean prepare(int proposalId, UUID taskId, int action) 
			  throws RemoteException, SocketTimeoutException;
	  public boolean accept(int proposalId, UUID taskId, int action) 
			  throws RemoteException, SocketTimeoutException;
	   public CrawlResult commit(UUID taskId, CrawlResult crawlResult, int action) 
			   throws RemoteException, SocketTimeoutException;

}
