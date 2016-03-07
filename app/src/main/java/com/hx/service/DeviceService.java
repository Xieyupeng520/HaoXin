package com.hx.service;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;


import android_serialport_api.SerialPort;

import com.hx.protocol.IComDevice;

public class DeviceService {
	
	private static IComDevice instance = null;
	
	
	private DeviceService() {  }

	public static IComDevice getDevice()  throws SecurityException, IOException, InvalidParameterException {
		if (instance == null) {
			/* Read serial port parameters */
			//SharedPreferences sp = getSharedPreferences("android_serialport_api.sample_preferences", MODE_PRIVATE);
			//String path = "/dev/ttyMT1";//sp.getString("DEVICE", "");ttyMT3
			String path = "/dev/ttyMT3";

			int baudrate = 115200;//Integer.decode(sp.getString("BAUDRATE", "-1"));

			/* Check parameters */
			if ( (path.length() == 0) || (baudrate == -1)) {
				throw new InvalidParameterException();
			}
			try {
			/* Open the serial port */
				instance = new SerialPort(new File(path), baudrate, 0);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return instance;
	}
}
