/**
 * 
 */
package com.kana.demo.VMInfo;

import java.net.*;
import java.rmi.RemoteException;
import java.util.Enumeration;

import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;

/**
 * @author arke
 *
 */
public class Name {

	/**
	 * @param args
	 * @throws MalformedURLException 
	 * @throws RemoteException 
	 */
	public static void main(String[] args) throws RemoteException, MalformedURLException, UnknownHostException, SocketException {

        //API Initialization
	    ServiceInstance si = new ServiceInstance(new URL("https://10.77.28.2/sdk"), "root", "badjmac2", true);
	    Folder rootFolder = si.getRootFolder();
	    ManagedEntity[] mes = new InventoryNavigator(rootFolder).searchManagedEntities("VirtualMachine");
	    if(mes==null || mes.length ==0)
	    {
	      return;
	    }

        //Getting local IP Address
        String localIP = "";
        Enumeration e = NetworkInterface.getNetworkInterfaces();
        int ctr=0;
        while(e.hasMoreElements())
        {
            NetworkInterface n = (NetworkInterface) e.nextElement();
            Enumeration ee = n.getInetAddresses();
            while (ee.hasMoreElements() && ctr<3)
            {       ctr++;
                if(ctr==3)
                    break;
                InetAddress i = (InetAddress) ee.nextElement();
                if(ctr==2)
                    localIP=i.getHostAddress();
            }
        }

        //comparing localIP var to machines IP in ESXi
        boolean match = false;
        String VMNameOnESXi="";
        for (int i = 0; i < mes.length; i++) {
            try {
                VirtualMachine vm = (VirtualMachine) mes[i];
                String  ipVMinESXi=vm.getGuest().getIpAddress();
                if(ipVMinESXi!=null) {
                    if (ipVMinESXi.equals(localIP)) {
                        VMNameOnESXi=vm.getName();
                        match=true;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (match) {
            System.out.println(VMNameOnESXi);
        } else {
            System.out.println("This machine is not hosted on ESXi!");
        }

	    si.getServerConnection().logout();
	}

}
