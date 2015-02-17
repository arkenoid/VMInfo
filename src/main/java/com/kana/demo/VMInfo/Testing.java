package com.kana.demo.VMInfo;
/**
 * Created by aditya on 12/02/15.
 */

import com.vmware.vim25.InvalidProperty;
import com.vmware.vim25.InvalidState;
import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.mo.VirtualMachine;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class Testing {

    public static void main (String argss[]) throws InvalidState, RuntimeFault, RemoteException{

        Esxi esxi = new Esxi();

        boolean cek = false;

        ArrayList<VirtualMachine> vms = new ArrayList<VirtualMachine>();
        try {

            cek = esxi.connect("10.77.28.2", "root", "badjmac2");
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            System.out.println("Error");

            e.printStackTrace();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println(cek);

        if (cek != false) {

            try {
                vms = esxi.getVirtualMachines();
            } catch (InvalidProperty e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (RuntimeFault e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            for (int i = 0; i < vms.size(); i++) {

                System.out.println("isi = " + vms.get(i).toString());

                ManagedVirtualMachine mn = null;
                try {
                    mn = new ManagedVirtualMachine(vms.get(i));
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                System.out.println("isi nama = " + mn.getName());
                System.out.println("isi nama = " + esxi.getIpAddress());
                System.out.println("===============================================================");
            }


        }


    }

}
