package com.pfc.conexion;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.pfc.datostrama.DatosTrama;
import com.pfc.remote.ControladorMando;

/**
 * 
 * @author Raul
 */
public class ConexionArduino {

	/**
	 * Dirección del host.
	 */
	private String host = "192.168.2.102";
	/**
	 * Puerto del host.
	 */
	private int puerto = 1099;
	/**
	 * Registro del que se obtiene la interfaz remota.
	 */
	private static Registry registry;
	/**
	 * Interfaz remota.
	 */
	private ControladorMando remoteApi;

	public ConexionArduino() {
	}

	public void conectaServidor() {
		AccessController.doPrivileged(new PrivilegedAction<Object>() {
			@Override
			public Object run() {
				try {
					registry = LocateRegistry.getRegistry(host, puerto);
					remoteApi = (ControladorMando) registry
							.lookup(ControladorMando.class.getSimpleName());
				} catch (final RemoteException e) {
					registry = null;
					remoteApi = null;
					Logger.getLogger(ConexionArduino.class.getName()).log(
							Level.WARNING,
							"Error al conectarse al servidor: {0}",
							e.getMessage());
				} catch (final NotBoundException e) {
					Logger.getLogger(ConexionArduino.class.getName()).log(
							Level.WARNING,
							"Error al conectarse al servidor: {0}",
							e.getMessage());
				}
				return null;
			}
		});

	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPuerto() {
		return puerto;
	}

	public void setPuerto(int puerto) {
		this.puerto = puerto;
	}

	public ControladorMando getRemoteApi() {
		return remoteApi;
	}

	public void setRemoteApi(ControladorMando remoteApi) {
		this.remoteApi = remoteApi;
	}

	public DatosTrama leeDatos() {
		DatosTrama datosTrama = null;
		if (remoteApi != null) {
			try {
				String trama = remoteApi.leeLinea();
				datosTrama = DatosTrama.getData(trama);

				// El Arduino proporciona datos cada 100 ms
				Thread.sleep(80);
			} catch (RemoteException e1) {
				Logger.getLogger(ConexionArduino.class.getName()).log(
						Level.WARNING, "Error al conectarse al servidor: {0}",
						e1.getMessage());
			} catch (NullPointerException e1) {
				Logger.getLogger(ConexionArduino.class.getName()).log(
						Level.WARNING, "NPE: {0}", e1.getMessage());
			} catch (InterruptedException e) {
				Logger.getLogger(ConexionArduino.class.getName()).log(
						Level.WARNING, "Error en Thread.sleep(): {0}",
						e.getMessage());
			}
		}
		return datosTrama;
	}

	public boolean isConnected() {
		return registry != null && remoteApi != null;
	}
}
