package Utils;

public class WaitNotifyManager{
	
	Object myMonitorObject = new Object();
	boolean wasSignalled = false;
	
	public void doWait()
	{
		synchronized(myMonitorObject)
		{
			while(!wasSignalled)
			{
				try
				{
					myMonitorObject.wait();
				} 
				catch(InterruptedException e)
				{
				}
			}
			//clear signal and continue running.
			wasSignalled = false;
		}
	}
	
	public void doNotify()
	{
		synchronized(myMonitorObject)
		{
			wasSignalled = true;
			myMonitorObject.notify();
	    }
	}
}
