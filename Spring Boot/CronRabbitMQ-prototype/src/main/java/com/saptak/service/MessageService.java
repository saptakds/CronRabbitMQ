package com.saptak.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import org.springframework.stereotype.Service;

@Service
public class MessageService {

	public byte[] messageInByteArray(Object obj) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
		objectOutput.writeObject(obj);
		objectOutput.flush();
		objectOutput.close();

		byte[] byteMessage = byteArrayOutputStream.toByteArray();
		byteArrayOutputStream.close();

		return byteMessage;

	}
}