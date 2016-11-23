import java.io.IOException;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailConnectionService {

	private String email;
	private String password;

	private Properties props;
	private String host;
	private Session session;

	public EmailConnectionService(String email, String password) {
		this.email = email;
		this.password = password;
		
		props = new Properties();
		props.setProperty("mail.imap.ssl.enable", "true");
		
		host = "imap.gmail.com";
		
		
	}

	public boolean Login() {
				
		session = Session.getInstance(props);
		try {
			Store store = session.getStore("pop3s");
			store.connect(host, email, password);
		} catch (NoSuchProviderException e) {
			return false;
		} catch (MessagingException e) {
			return false;
		}
		
		return true;
	}

	public void SendEmail() {
		/*
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(email));
			Address[] toUser = InternetAddress.parse(email);
			message.setSubject("Test Subject");
			message.setText("Email enviado!");
			message.setRecipients(RecipientType.TO, toUser);

			Transport.send(message);
			MainWindow.addToLog("GGWP");

		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}

	public void ReadEmails() {
		MainWindow.setHeaderInfo("Downloading...", GESSColor.DOWNLOADING_ORANGE);
		MainWindow.addToLog("Starting to read e-mails...");
		try {
						
			Session session = Session.getInstance(props);
			MainWindow.addToLog("Getting Store...");
			Store store = session.getStore("imap");
			MainWindow.addToLog("Connecting...");
			store.connect(host, email, password);
			MainWindow.addToLog("Connected!");
			Folder inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_ONLY);
			MainWindow.addToLog("Found " + inbox.getMessageCount() + " e-mails.");
			MainWindow.addToLog("Searching for messages...");
			Message msg = inbox.getMessage(inbox.getMessageCount());			
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	

}
