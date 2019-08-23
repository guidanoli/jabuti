package gui.component;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Semaphore;
import java.util.function.Consumer;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import gui.error.LightError;

/**
 * 
 * @author guidanoli
 *
 */
public class Console extends JTextPane {
	
	private static final long serialVersionUID = 3665780772356185179L;
	private StreamHandler stdoutThread = new StreamHandler(MessageType.OUTPUT);
	private StreamHandler stderrThread = new StreamHandler(MessageType.ERROR);
	private Semaphore mutex = new Semaphore(1);
	private Deque<ProcessBuilder> processList = new ConcurrentLinkedDeque<ProcessBuilder>(); 
	private Consumer<Boolean> listener;
	private Process currentProcess;
	
	/* Public */
	
	/**
	 * Constructs a console.
	 * @param listener - alarms an observer whenever
	 * a process has terminated and if successful or not
	 */
	public Console(Consumer<Boolean> listener) {
		setBackground(Color.BLACK);
		setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
		this.listener = listener;
	}
	
	/**
	 * Adds a process builder to the queue and checks if
	 * it can be run.
	 * @param processBuilder - process builder to be added
	 */
	public void run(ProcessBuilder processBuilder) {
		processList.offerLast(processBuilder);
		checkNewProcess();
	}
	
	/**
	 * Checks if there is a process running on the console.
	 * @return
	 */
	public boolean isRunning() {
		return mutex.availablePermits() == 0;
	}
	
	public void clearScreen() {
		setText("");
	}
	
	/* Private */
	
	private enum MessageType {
		ERROR(Color.ORANGE),
		CRITICAL_ERROR(Color.RED),
		OUTPUT(Color.WHITE);
		
		private Color color;
		MessageType(Color c) {
			this.color = c;
		}
	}
	
	private class Message {
		private MessageType type;
		private String string;
		public Message(MessageType type, String message) {
			this.type = type;
			this.string = message;
		}
	}
			
	private class StreamHandler extends Thread {

		private InputStream is;
		private MessageType type;
		
		public StreamHandler(MessageType messageType) {
			this.type = messageType;
		}
		
		public void setStream(InputStream inputStream) {
			this.is = inputStream;
		}
		
		private Message newMessage(String message) {
			return new Message(type, message);
		}
		
		private Message newException(Exception e) {
			return new Message(MessageType.CRITICAL_ERROR, e.getMessage());
		}
		
		@Override
		public void run() {
			try { 
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				String line = null;
				while ((line = br.readLine()) != null) appendToPane(newMessage(line));
				if (type == MessageType.ERROR) listener.accept(true);
			} catch (IOException ioe) {
				appendToPane(newException(ioe));
				if (type == MessageType.ERROR) listener.accept(false);
			}
		}
 
	}
	
	private void checkNewProcess() {
		if( !processList.isEmpty() && !stderrThread.isAlive() && !stdoutThread.isAlive() ) {
			try {
				currentProcess = processList.pollFirst().start();
				stdoutThread = new StreamHandler(MessageType.OUTPUT);
				stderrThread = new StreamHandler(MessageType.ERROR);
				stdoutThread.setStream(currentProcess.getInputStream());
				stdoutThread.start();
				stderrThread.setStream(currentProcess.getErrorStream());
				stderrThread.start();
			} catch (IOException e) {
				e.printStackTrace();
				appendToPane(new Message(MessageType.CRITICAL_ERROR, "Could not initiate process"));
			}
		}
	}
	
	private void appendToPane(Message message)
    {
		try {
			mutex.acquire();
		} catch (InterruptedException e) {
			LightError.show(e);
			return;
		}
		Color c = message.type.color;
		String msg = message.string;
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = (AttributeSet) sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = (AttributeSet) sc.addAttribute((javax.swing.text.AttributeSet) aset, StyleConstants.FontFamily, "Lucida Console");
        aset = (AttributeSet) sc.addAttribute((javax.swing.text.AttributeSet) aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = getDocument().getLength();
        setCaretPosition(len);
        setCharacterAttributes((javax.swing.text.AttributeSet) aset, false);
        replaceSelection(msg+"\n");
        mutex.release();
    }

}
