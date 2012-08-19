package com.pfc.remote;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ControladorMandoImpl extends UnicastRemoteObject implements
		ControladorMando, SerialPortEventListener {

	/**
	 * Puerto serie.
	 */
	private SerialPort serialPort;

	/**
	 * Puertos que van a usarse.
	 */
	private static String port_names[] = { "/dev/tty.usbserial-A9007UX1", // Mac
																			// OS
																			// X
			"/dev/ttyUSB0", // Linux
			"COM7", // Windows
	};

	private static final long serialVersionUID = 1L;

	/**
	 * Búfer de entrada del puerto.
	 */
	private static InputStream input;
	/**
	 * Búfer de salida del puerto.
	 */
	private static OutputStream output;
	/**
	 * Milisegundos de bloque mientras se abre el puerto.
	 */
	private static final int TIME_OUT = 2000;
	/**
	 * Baudios por segundo del puerto.
	 */
	private static final int DATA_RATE = 9600;
	private BufferedReader br;

	/**
	 * Constructor de la clase.
	 * 
	 * @throws RemoteException
	 */
	protected ControladorMandoImpl() throws RemoteException {
		// open();
	}

	/**
	 * Abre el puerto.
	 */
	public void open() {

		CommPortIdentifier portId = null;
		Enumeration<?> portEnum = CommPortIdentifier.getPortIdentifiers();

		// Busca el puerto.
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum
					.nextElement();
			for (String portName : port_names) {
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
				}
			}
		}

		if (portId == null) {
			System.out
					.println("No se pudo encontrar el puerto COM. Reinicie el servidor indicando un puerto válido.");
			return;
		}

		try {

			// Abre el puerto.
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
					TIME_OUT);

			// Establece los parámetros del puerto.
			serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

			// Abre los búfers.
			input = serialPort.getInputStream();
			output = serialPort.getOutputStream();

			br = new BufferedReader(new InputStreamReader(input));

			// Añade escuchadores de eventos.
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);

		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}

	/**
	 * Cierra el puerto.
	 */
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	/**
	 * Maneja un evento en el puerto serie.
	 */
	public synchronized void serialEvent(SerialPortEvent oEvent) {

		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			// System.out.println(leeLinea());
		}
	}

	public String convertToString(InputStream is) {
		try {
			return new java.util.Scanner(is).useDelimiter(",").next();
		} catch (NoSuchElementException e) {
			return "";
		}
	}

	public String leeLinea() throws RemoteException {
		String inputString = null;

		String strLine; // Read File Line By Line
		try {
			while ((strLine = br.readLine()) != null) { // Print the content
				inputString = strLine; // on the console
				// Logger.getLogger(ControladorMando.class.getName()).log(
				// Level.INFO, "Lee línea " + inputString);
			}
			// Logger.getLogger(ControladorMando.class.getName()).log(Level.INFO,
			// "¡NO lee línea!" + strLine);
		} catch (IOException e) {
			/*
			 * Logger.getLogger(ControladorMando.class.getName()).log(
			 * Level.WARNING, e.getMessage());
			 */
			// Con readLine() no maneja bien el TimeOut
			// http://mailman.qbang.org/pipermail/rxtx/2008-February/9748098.html
			// http://mailman.qbang.org/pipermail/rxtx/2009-April/9537609.html
			// e.printStackTrace();
		}

		return inputString;
	}

	/**
	 * Habilita la señal de SLEEP del acelerómetro.
	 */
	public String apagarAcelerometro() throws RemoteException {
		try {
			output.write('S');
		} catch (IOException e) {
			System.err.println("Error al apagar el acelerómetro: "
					+ e.getMessage());
		}
		return "Apagado " + leeLinea();
	}

	/**
	 * Deshabilita la señal de SLEEP del acelerómetro.
	 */
	public String encenderAcelerometro() throws RemoteException {
		try {
			output.write('W');
		} catch (IOException e) {
			System.err.println("Error al encender el acelerómetro: "
					+ e.getMessage());
		}
		return "Encendido " + leeLinea();
	}

	public void setPorts(String macOSX, String linux, String windows)
			throws RemoteException {

		port_names[0] = macOSX;
		port_names[1] = linux;
		port_names[2] = windows;

		open();
	}
}
