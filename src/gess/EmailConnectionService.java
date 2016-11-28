package gess;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

import exceptions.NoMatchException;
import javafx.application.Platform;

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
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				MainWindow.setHeaderInfo("Downloading...", GESSColor.DOWNLOADING_ORANGE);
				MainWindow.addToLog("Starting to read e-mails...");
			}
		});
		
		
		
		
		int numberOfEmailsFromLast = 37;
		try {
						
			Session session = Session.getInstance(props);
			MainWindow.addToLog("Getting Store...");
			Store store = session.getStore("imap");
			MainWindow.addToLog("Connecting...");
			store.connect(host, email, password);
			MainWindow.addToLog("Connected!");
			Folder inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_ONLY);					
			MainWindow.addToLog("Reading last " + numberOfEmailsFromLast + " e-mails...");
						
			emailList = new ArrayList<Message>();
			for (int i = inbox.getMessageCount() ; i >= inbox.getMessageCount()-numberOfEmailsFromLast ; i--){
				Message message = inbox.getMessage(i);
				MainWindow.addToLog(message.getSubject());				
				emailList.add(message);
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						float percent = (float)(emailList.size())/(float)(numberOfEmailsFromLast) * 100;
						DecimalFormat df = new DecimalFormat("#.00");	
						
						MainWindow.setHeaderInfo("Downloading... (" + df.format(percent) + "%) [" + emailList.size() + "/" + numberOfEmailsFromLast +"]", GESSColor.DOWNLOADING_ORANGE);
					}
				});
				
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
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				MainWindow.setHeaderInfo("Applying rules...", GESSColor.DOWNLOADING_ORANGE);
			}
		});
		
		int appliedRules = 0;
		for (Message message : emailList){
			appliedRules++;
			try {
				String body = message.getContent().toString();
				Entry entry = new Entry();
				for (Rule rule : rules){
					try {
						if (entry.entryCSV == "")
							entry.entryCSV += rule.apply(body);
						else
							if (rule.getRuleName() != "")
								entry.entryCSV += "," + rule.apply(body);
					} catch (NoMatchException e) {
						entry.entryCSV += "<empty>";
					}
				}
				entries.add(entry);
				
				float percent = (float)(appliedRules)/(float)(emailList.size()) * 100;
				Platform.runLater(new Runnable() {
					@Override
					public void run() {						
						DecimalFormat df = new DecimalFormat("#.00");	
						MainWindow.setHeaderInfo("Applying rules... (" + df.format(percent) + "%)", GESSColor.DOWNLOADING_ORANGE);
					}
				});
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (MessagingException e) {
				e.printStackTrace();
			}
			
			Platform.runLater(new Runnable() {
				@Override
				public void run() {											
					MainWindow.setHeaderInfo("Rules appplied. Ready to export to CSV.", GESSColor.SUCCESS_GREEN);
				}
			});
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
