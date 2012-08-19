package com.pfc.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ControladorMando extends Remote {

	public String apagarAcelerometro() throws RemoteException;

	public String encenderAcelerometro() throws RemoteException;

	public String leeLinea() throws RemoteException;

	public void setPorts(String macOSX, String linux, String windows)
			throws RemoteException;
}
