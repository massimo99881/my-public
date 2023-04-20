package it.unimi.isa.utils;

import it.unimi.isa.controller.ExecutorController;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.annotation.PostConstruct;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultCaret;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class ISAConsole
{
   public JFrame mainFrame;
   public JTextArea commandArea;
   JTextArea resultArea;
   public JTextArea commandArea2;
   JTextArea resultArea2;
   static JLabel notifyline1;
   
   @Autowired
   ExecutorController executor;
  
   @Autowired
   TaskExecutor taskExecutor;
   
   private static final Logger logger = Logger.getLogger(ISAConsole.class);
   private static final String initialMessage = "press \"help\" for command line guide";
   
   private ISAConsole(){
	   
   }
   
   @PostConstruct
   public void init()
   {
      mainFrame = new JFrame();
      mainFrame.setTitle("ISA Console");
      mainFrame.setLayout(new FlowLayout(FlowLayout.LEFT));
      mainFrame.setLocation(200, 100);
      mainFrame.setPreferredSize(new Dimension(730, 500));
      mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//      mainFrame.setResizable(false);

      commandArea = new JTextArea(executor.user+"> ");
      commandArea.setFont(new Font("Courier", Font.PLAIN, 12));
      commandArea.setBackground(Color.decode("#171717"));
      commandArea.setForeground(Color.decode("#00FF00"));
      commandArea.setCaretColor(Color.WHITE);
      commandArea.select(6, 6);
      commandArea.addKeyListener(new KeyMonitor());
      
      commandArea2 = new JTextArea(executor.user+"> ");
      commandArea2.setFont(new Font("Courier", Font.PLAIN, 12));
      commandArea2.setBackground(Color.decode("#171717"));
      commandArea2.setForeground(Color.decode("#00FF00"));
      commandArea2.setCaretColor(Color.WHITE);
      commandArea2.select(executor.user.length()+2, executor.user.length()+2);
      commandArea2.addKeyListener(new KeyMonitor());
      
      JScrollPane scrollPane1 = new JScrollPane ( commandArea );
      scrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//      scrollPane1.setPreferredSize(new Dimension(710, 410));
      scrollPane1.setAutoscrolls(true);
      scrollPane1.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
      DefaultCaret caret = (DefaultCaret)commandArea.getCaret();
      caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
      
      JScrollPane scrollPane2 = new JScrollPane ( commandArea2 );
      scrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//      scrollPane2.setPreferredSize(new Dimension(710, 410));
      scrollPane2.setAutoscrolls(true);
      scrollPane2.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
      DefaultCaret caret2 = (DefaultCaret)commandArea2.getCaret();
      caret2.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

      final JTabbedPane jtp = new JTabbedPane();
      jtp.addTab("Tab1", scrollPane1);
      jtp.addTab("Tab2", scrollPane2);
      jtp.addChangeListener(new ChangeListener() {
          public void stateChanged(ChangeEvent e) {
              executor.setPrompt(jtp.getSelectedIndex()+1);
        	  logger.info("changed tab to "+jtp.getSelectedIndex());
        	  
          }
      });
      
      //mainFrame.getContentPane().add(jtp);
      
      Container contentPane = mainFrame.getContentPane();
      contentPane.setLayout(new BorderLayout());
      contentPane.add(jtp, BorderLayout.CENTER);
      
      notifyline1 = new JLabel();
      notifyline1.setText(initialMessage);
      notifyline1.setLayout(new BorderLayout());
      contentPane.add(notifyline1,  BorderLayout.PAGE_END);
      
      JMenuBar menuBar = new JMenuBar();
      JMenu menu = new JMenu("Menu");
      menuBar.add(menu);
      JMenuItem item = new JMenuItem("Exit");
      item.addActionListener(new ActionListener(){
          @Override
          public void actionPerformed(ActionEvent e) {
              System.exit(0);
          }
      });
      menu.add(item);
      JMenu option = new JMenu("Option");
      menuBar.add(option);
      JMenuItem item1 = new JMenuItem("Layout1");
      item1.addActionListener(new ActionListener(){
          @Override
          public void actionPerformed(ActionEvent e) {
            commandArea.setBackground(Color.decode("#171717"));
            commandArea.setForeground(Color.decode("#00FF00"));
            commandArea2.setBackground(Color.decode("#171717"));
            commandArea2.setForeground(Color.decode("#00FF00"));
          }
      });
      option.add(item1);
      JMenuItem item2 = new JMenuItem("Layout2");
      item2.addActionListener(new ActionListener(){
          @Override
          public void actionPerformed(ActionEvent e) {
        	  commandArea.setBackground(Color.decode("#DBDBDB"));
              commandArea.setForeground(Color.decode("#0000CD"));
              commandArea2.setBackground(Color.decode("#DBDBDB"));
              commandArea2.setForeground(Color.decode("#0000CD"));
          }
      });
      option.add(item2);
      
      JMenu help = new JMenu("?");
      menuBar.add(help);
      JMenuItem item3 = new JMenuItem("Help");
      item3.addActionListener(new ActionListener(){
          @Override
          public void actionPerformed(ActionEvent e) {
        	 
        	  	setResultArea(executor.user+"> ",executor.getPrompt());
	            executor.setCommand("help");
	        	taskExecutor.execute(executor);
        	
          }
      });
      help.add(item3);
      
      mainFrame.setJMenuBar(menuBar);
      
      
      mainFrame.pack();
      mainFrame.setVisible(true);
//      for(int i=0;i<100;i++)
//    	  this.resultArea.append("\nInserisce del testo alla riga "+i);
   }

//   public static void main(String[] args)
//   {
//	   final ApplicationContext context = new ClassPathXmlApplicationContext(Constants.CONFIG_PATH);
//	   isaConsole = context.getBean(ISAConsole.class);
//	   
//   }
   
   
   
   public void setResultArea(String value,int current_prompt){
//	   this.resultArea.setText(this.resultArea.getText()+"\n"+value);
	   
	   if(current_prompt==1){
		   this.commandArea.append(/*"\n" +*/ value);
	   }		   
	   else if(current_prompt==2){
		   this.commandArea2.append(/*"\n" +*/ value);
	   }		   
   }
   
   public void clear(int current_prompt) {
	   
	   if(current_prompt==1){
		   this.commandArea.setCaretPosition(0);
		   this.commandArea.setText("");
	   }
	   else if(current_prompt==2){
		   this.commandArea2.setCaretPosition(0);
		   this.commandArea2.setText("");
	   }
	}
   
   class KeyMonitor extends KeyAdapter
   {
	   String commandRcvd;
	   String commandRcvd2;
      
      public void keyPressed(KeyEvent e)
      {
    	  
    	  int prompt = executor.getPrompt();
    	  if(prompt==1){
    		  if(e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
    	         {
    	            if(commandArea.getText().charAt(commandArea.getCaretPosition() - 2) == '>')
    	            {
    	               e.consume();
    	            }
    	         }
    	         if(e.getKeyCode() == KeyEvent.VK_ENTER)
    	         {
    	        	if(!Utils.isEmptyOrNull(executor.getCommand())){
    	        		logger.info("esecuzione del comando precedente ancora in corso su prompt1..");
    	        		return;
//    	        		int startPos = commandArea.getText().lastIndexOf("Command> ") + 9;
//    	                commandRcvd = commandArea.getText().substring(startPos);
    	        	}
    	        	 
    	            int startPos = commandArea.getText().lastIndexOf(executor.user+"> ") + executor.user.length()+2;
    	            commandRcvd = commandArea.getText().substring(startPos);
    	            //System.out.println("comando ricevuto: "+commandRcvd);
    	            logger.info("comando ricevuto su prompt1: "+commandRcvd);
    	            
    	            e.consume();
    	            executor.setPrompt(1);
    	            executor.setCommand(commandRcvd);
    	            taskExecutor.execute(executor);
    	            
    	         }
    	  }
    	  else if(prompt==2){
    		  if(e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
    	         {
    	            if(commandArea2.getText().charAt(commandArea2.getCaretPosition() - 2) == '>')
    	            {
    	               e.consume();
    	            }
    	         }
    	         if(e.getKeyCode() == KeyEvent.VK_ENTER)
    	         {
    	        	if(!Utils.isEmptyOrNull(executor.getCommand())){
    	        		logger.info("esecuzione del comando precedente ancora in corso su prompt2..");
    	        		return;
//    	        		int startPos = commandArea.getText().lastIndexOf("Command> ") + 9;
//    	                commandRcvd = commandArea.getText().substring(startPos);
    	        	}
    	        	 
    	            int startPos2 = commandArea2.getText().lastIndexOf(executor.user+"> ") + executor.user.length()+2;
    	            commandRcvd2 = commandArea2.getText().substring(startPos2);
    	            //System.out.println("comando ricevuto: "+commandRcvd);
    	            logger.info("comando ricevuto su prompt2: "+commandRcvd2);
    	            
    	            e.consume();
    	            executor.setPrompt(2);
    	            executor.setCommand(commandRcvd2);
    	            taskExecutor.execute(executor);
    	            
    	         }
    	  }
    	  
    	  
         
      }
   }
   
   public static void setNotifylineText(String value){
	   //se sto eseguendo il calcolo degli indipendenti da JUnit la var notifyline1 è null
	   if(notifyline1==null)
		   return;
	   String currentText = notifyline1.getText();
	   if(!Utils.isEmptyOrNull(currentText) || initialMessage.equalsIgnoreCase(currentText) || initialMessage.equalsIgnoreCase("complete")){
		   notifyline1.setText(value);
	   }
   }

}