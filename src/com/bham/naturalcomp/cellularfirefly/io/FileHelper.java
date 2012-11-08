package com.bham.naturalcomp.cellularfirefly.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileHelper {
	
	private static BufferedWriter mBuffer;
	
	public static boolean createFile(String fileName) {
		try {
			if(fileName == null) {
				return false;
			}
			File file = new File(fileName);
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			} else {
				// Else don't write in it.
				return false;
			}
 
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			mBuffer = new BufferedWriter(fw);	
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean writeNextLine(String line) {
		try {
			if(mBuffer != null && line != null) {
				mBuffer.write(line + "\n");
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}	
	}
	
	public static boolean closeCurrentFile() {
		try {
			if(mBuffer != null) {
				mBuffer.close();
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

}
