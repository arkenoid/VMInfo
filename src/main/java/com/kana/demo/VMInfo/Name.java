/**
 * 
 */
package com.kana.demo.VMInfo;

import java.io.*;
import java.net.*;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.concurrent.TimeoutException;

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

    public static void main(String[] args) {

        readInput();

    }

    public static void readInput(){

        String path = ".";

        String files;
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        String propFileName = null;

        for (int i = 0; i < listOfFiles.length; i++)
        {
            if (listOfFiles[i].isFile())
            {
                files = listOfFiles[i].getName();
                if (files.equals("config.properties"))
                {
                    propFileName = files;
                }
            }
        }


        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(propFileName));
        } catch (FileNotFoundException e) {
            System.out.println("File config.properties not found!");
            //e.printStackTrace();
        }

        String line;
        String[] data = new String[3];
        int i=0;

        try {
            while ((line = br.readLine()) != null) {
                // process the line.
                String[] words = line.split("=");
                data[i] = words[1].trim();
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        makeConnection(data[0], data[1], data[2]);
    }

    public static void makeConnection(String url,String userName,String password ){

        ServiceInstance si = null;
        try {
            si = new ServiceInstance(new URL(url), userName, password, true);
        } catch (RemoteException e) {
            System.out.println("Please check your config.properties");
            //e.printStackTrace();
        } catch (MalformedURLException e) {
            System.out.println("Please check your config.properties");
            //e.printStackTrace();
        }

        initializationAPI(si);

        getLocalIPAddress();
        comparingIPAddress();
        logoutConnection(si);
    }


    public static void initializationAPI(ServiceInstance si) {

        //API Initialization
        Folder rootFolder = si.getRootFolder();

        try {
            mes = new InventoryNavigator(rootFolder).searchManagedEntities("VirtualMachine");
        } catch (RemoteException e) {
            System.out.println("Please check your config.properties");

            //e.printStackTrace();
        }

        if(mes==null || mes.length ==0)
        {
            System.out.println("Inventory not found");
        }

    }


    public static void getLocalIPAddress() {

        //Getting local IP Address
        Enumeration e = null;
        try {
            e = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e1) {
            System.out.println("Please check your config.properties");

            //e1.printStackTrace();
        }
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
                String ipVMinESXi = vm.getGuest().getIpAddress();
                if (ipVMinESXi != null) {
                    if (ipVMinESXi.equals(localIP)) {
                        VMNameOnESXi = vm.getName();
                        match = true;
                    }
                }
            } catch (Exception te) {
                System.out.println("time out");
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
