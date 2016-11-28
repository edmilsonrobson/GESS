package gess;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

import exceptions.NoMatchException;

public class EmailConnectionService {

	private String email;
	private String password;

	private Properties props;
	private String host;
	private Session session;
	
	private List<Message> emailList;
	private List<Rule> rules;
	private List<Entry> entries;
	
	public EmailConnectionService(String email, String password) {
		this.email = email;
		this.password = password;
		
		props = new Properties();
		props.setProperty("mail.imap.ssl.enable", "true");
		
		host = "imap.gmail.com";
		
		rules = new ArrayList<Rule>();
		entries = new ArrayList<Entry>();
		
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

	public void ReadEmails() throws MessagingException {
		MainWindow.setHeaderInfo("Downloading...", GESSColor.DOWNLOADING_ORANGE);
		MainWindow.addToLog("Starting to read e-mails...");
		
		int numberOfEmailsFromLast = 10;
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
			
			emailList = new ArrayList<Message>();
			for (int i = inbox.getMessageCount() ; i >= inbox.getMessageCount()-numberOfEmailsFromLast ; i--){
				Message message = inbox.getMessage(i);
				MainWindow.addToLog(message.getSubject());				
				emailList.add(message);
				MainWindow.setHeaderInfo("Downloading... [" + emailList.size() + "/" + numberOfEmailsFromLast +"]", GESSColor.DOWNLOADING_ORANGE);
			}
			MainWindow.addToLog("Finished downloading e-mails.");
			MainWindow.addToLog("Ready to apply rules.");
		} catch (MessagingException e) {
			MainWindow.addToLog("ERROR reading questions. Exception: " + e.getClass().getSimpleName());
			e.printStackTrace();
			
			throw new MessagingException();
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
	
	public void ReadAllEmails(){
		for (Message message : emailList){
			try {
				String body = message.getContent().toString();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void ApplyAllRules(){
		for (Message message : emailList){
			try {
				String body = message.getContent().toString();
				Entry entry = new Entry();
				for (Rule rule : rules){
					try {
						entry.entryCSV += rule.apply(body);
					} catch (NoMatchException e) {
						entry.entryCSV += "<empty>";
					}
				}
				entries.add(entry);
				MainWindow.addToLog("New rule applied!");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void AddRule(Rule rule){
		if (rules.contains(rule) == false){
			rules.add(rule);
		}
	}
	
	public void ExportCSV(){
		MainWindow.addToLog("Exporting " + entries.size() + " entries...");
		for (Entry entry : entries){
			MainWindow.addToLog(entry.entryCSV);
		}
		MainWindow.addToLog("Finished exporting.");
	}

	public void ResetRules() {
		rules = new ArrayList<Rule>();
	}
	
	

}