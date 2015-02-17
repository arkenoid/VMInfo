/**
 * 
 */
package com.kana.demo.VMInfo;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import com.vmware.vim25.VirtualMachineCapability;
import com.vmware.vim25.VirtualMachineConfigInfo;
import com.vmware.vim25.VirtualMachineSummary;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;
import com.vmware.vim25.GuestInfo;

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
	public static void main(String[] args) throws RemoteException, MalformedURLException {
		long start = System.currentTimeMillis();
	    ServiceInstance si = new ServiceInstance(new URL("https://10.77.28.2/sdk"), "root", "badjmac2", true);

	    long end = System.currentTimeMillis();
	    System.out.println("time taken:" + (end-start));
	    Folder rootFolder = si.getRootFolder();
	    String name = rootFolder.getName();
	    System.out.println("root:" + name);
	    ManagedEntity[] mes = new InventoryNavigator(rootFolder).searchManagedEntities("VirtualMachine");
	    if(mes==null || mes.length ==0)
	    {
	      return;
	    }
	    
	    VirtualMachine vm = (VirtualMachine) mes[10];


		VirtualMachineConfigInfo vminfo = vm.getConfig();
	    VirtualMachineCapability vmc = vm.getCapability();

		GuestInfo ss = vm.getGuest();

		vm.getResourcePool();
	    System.out.println("Hello " + vm.getName());

		System.out.println("Hello my IP Address " + vm.getGuest().getIpAddress());

		System.out.println("Hello locationID = " + vm.getConfig().getLocationId());


		System.out.println("GuestOS: " + vminfo.getGuestFullName());
	    System.out.println("Multiple snapshot supported: " + vmc.isMultipleSnapshotsSupported());
	    System.out.println("GuestOS: " + vminfo);

	    si.getServerConnection().logout();
	}

}
