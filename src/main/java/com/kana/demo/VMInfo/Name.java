/**
 * 
 */
package com.kana.demo.VMInfo;

import java.net.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Enumeration;

import com.vmware.vim25.*;
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
//		long start = System.currentTimeMillis();
	    ServiceInstance si = new ServiceInstance(new URL("https://10.77.28.2/sdk"), "root", "badjmac2", true);

//	    long end = System.currentTimeMillis();
//	    System.out.println("time taken:" + (end-start));
	    Folder rootFolder = si.getRootFolder();
//	    String name = rootFolder.getName();
//	    System.out.println("root:" + name);
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
        System.out.println("Local IP = " + localIP);

        //comparing localIP var to machines IP in ESXi
//        ArrayList<VirtualMachine> vms = new ArrayList<VirtualMachine>();

//            vms = esxi.getVirtualMachines();
            for (int i = 0; i < mes.length; i++) {

                ManagedVirtualMachine mn = null;
                try {
                    VirtualMachine vm = (VirtualMachine) mes[i];
                    String  ipVMinESXi=vm.getGuest().getIpAddress();
                    if(ipVMinESXi!=null) {
                        if (ipVMinESXi.equals(localIP)) {
                            System.out.println(vm.getName());
                        } else {
                            System.out.println("This machine is not hosted on ESXi!");
                            break;
                        }
                    }
                } catch (Exception ex) {
                    // TODO Auto-generated catch block
                    ex.printStackTrace();
                }
            }

//        VirtualMachine vms = (VirtualMachine) mes[10];
//
//
//		VirtualMachineConfigInfo vminfo = vms.getConfig();
//	    VirtualMachineCapability vmc = vms.getCapability();
//
//		GuestInfo ss = vms.getGuest();
//
//		vms.getResourcePool();
//	    System.out.println("Hello " + vms.getName());
//
//		System.out.println("Hello my IP Address " + vms.getGuest().getIpAddress());
//
//		System.out.println("Hello locationID = " + vms.getConfig().getLocationId());
//
//		System.out.println("GuestOS: " + vminfo.getGuestFullName());
//	    System.out.println("Multiple snapshot supported: " + vmc.isMultipleSnapshotsSupported());
//	    System.out.println("GuestOS: " + vminfo);

	    si.getServerConnection().logout();
	}

}
