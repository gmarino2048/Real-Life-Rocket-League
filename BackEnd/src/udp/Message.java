package udp;

import java.net.DatagramPacket;
import java.net.InetAddress;

public class Message implements Runnable {

	DatagramPacket received, send;
	InetAddress address;
	int port;
	String message;
	byte[] buffer;

	public Message(DatagramPacket pack) {
		this.received = pack;
		this.port = pack.getPort();
		this.address = pack.getAddress();
		this.buffer = new byte[256];
		this.received = new DatagramPacket(buffer, buffer.length, this.address, this.port);
		this.message = new String(this.received.getData(), 0, this.received.getLength());
	
	}
	
	

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
