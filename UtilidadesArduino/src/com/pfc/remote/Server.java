package com.pfc.remote;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

public class Server {
	private static int PORT = 1099;
	private static Registry registry;
	private static String macOSXPort = "/dev/tty.usbserial-A9007UX1";
	private static String linuxPort = "/dev/ttyUSB0";
	private static String windowsPort = "COM7";

	public static void startRegistry() throws RemoteException {
		registry = java.rmi.registry.LocateRegistry.createRegistry(PORT);
	}

	public static void registerObject(String name, Remote remoteObj)
			throws RemoteException, AlreadyBoundException {
		registry.bind(name, remoteObj);
		System.out.println("Registered: " + name + " -> "
				+ remoteObj.getClass().getName() + "[" + remoteObj + "]");
	}

	public static void main(String[] args) throws Exception {

		switch (args.length) {
		case 0:
			// Parámetros por defecto.
			break;
		case 1:
			// Puerto servidor.
			PORT = Integer.valueOf(args[0]);
			break;
		case 4: 
			// Puerto servidor y puerto receptor.
			PORT = Integer.valueOf(args[0]);

			macOSXPort = args[1];
			linuxPort = args[2];
			windowsPort = args[3];
			
			break;
		default:
			System.out
					.println("Número de parámetros incorrecto. Ejemplo: Server.jar || Server.jar puertoServidor || Server.jar puertoServidor puertoReceptorMACOSX puertoReceptorLinux puertoReceptorWindows");
		}
		startRegistry();
		ControladorMando controlador = new ControladorMandoImpl();
		controlador.setPorts(macOSXPort, linuxPort, windowsPort);
		registerObject(ControladorMando.class.getSimpleName(), controlador);
		Thread.sleep(5 * 60 * 1000);
	}
}