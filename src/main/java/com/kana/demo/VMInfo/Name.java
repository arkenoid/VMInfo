/**
 * 
 */
package com.kana.demo.VMInfo;

import java.io.IOException;
import java.net.*;
import java.rmi.RemoteException;
import java.util.Enumeration;

import com.vmware.vim25.InvalidProperty;
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

    private static String localIP = "";
    static ManagedEntity[] mes;

    public static void main(String[] args) throws RemoteException, MalformedURLException, UnknownHostException, SocketException {

        GetPropertiesValues properties = new GetPropertiesValues();

        try {
            boolean success = properties.checkPropertiesFile();

            if (success) {

                String url = properties.getURLProperties();
                String userName = properties.getUserNameProperties();
                String password = properties.getPasswordProperties();

                System.out.println("check properties = " + url + ", " + userName + ", " + password);

                ServiceInstance si = new ServiceInstance(new URL(url), userName, password, true);

                initializationAPI(si);
                getLocalIPAddress();
                comparingIPAddress();
                logoutConnection(si);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void initializationAPI(ServiceInstance si) throws RemoteException {

        //API Initialization
        Folder rootFolder = si.getRootFolder();

        mes = new InventoryNavigator(rootFolder).searchManagedEntities("VirtualMachine");

        if(mes==null || mes.length ==0)
        {
            System.out.println("Inventory not found");
        }

    }


    public static void getLocalIPAddress() throws SocketException {

        //Getting local IP Address
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

    }


    public static void comparingIPAddress(){

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
            System.out.println("This machine is not hosted on ESXi or run on local!");
        }

	}

    public static void logoutConnection(ServiceInstance si){

        si.getServerConnection().logout();

    }

}
