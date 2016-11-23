import java.util.List;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

public class EmailConnectionService {

	private String email;
	private String password;

	private Properties props;
	private String host;
	private Session session;
	
	private List<Message> emailList;

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
			MainWindow.addToLog("Reading last 30 e-mails...");
			for (int i = inbox.getMessageCount() ; i >= inbox.getMessageCount()-30 ; i--){
				Message message = inbox.getMessage(i);
				MainWindow.addToLog(message.getSubject());
			}
			MainWindow.addToLog("Finished reading e-mails.");
			MainWindow.addToLog("Ready to apply rules.");
		} catch (MessagingException e) {
			MainWindow.addToLog("ERROR reading questions. Exception: " + e.getClass().getSimpleName());
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
