package test.unimi.isa.swing;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.border.Border;

public class ProgressSample {
	
	private JProgressBar progressBar2 = null;
	private JFrame f;
	private Border border;
	
  public static void main(String args[]) {
    JFrame f = new JFrame("JProgressBar Sample");
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Container content = f.getContentPane();
    JProgressBar progressBar = new JProgressBar();
    progressBar.setValue(25);
    progressBar.setStringPainted(true);
    Border border = BorderFactory.createTitledBorder("Reading...");
    progressBar.setBorder(border);
    content.add(progressBar, BorderLayout.NORTH);
    f.setSize(300, 100);
    f.setVisible(true);
  }
  
  public ProgressSample(){
	    f = new JFrame("Working");
	    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    f.setPreferredSize(new Dimension(200, 600));
	    Container content = f.getContentPane();
	    progressBar2 = new JProgressBar();
	    progressBar2.setValue(0);
	    progressBar2.setStringPainted(true);
	    border = BorderFactory.createTitledBorder("Reading...");
	    progressBar2.setBorder(border);
	    content.add(progressBar2, BorderLayout.NORTH);
	    f.setSize(300, 100);
	    f.setVisible(true);
  }
  
  public void closeProgress(){
	  f.dispose();
  }
	
	public void setValue(int value) {
		progressBar2.setValue(value);
	}
	
	public void setBorder(String value){
		border = BorderFactory.createTitledBorder(value);
		progressBar2.setBorder(border);
	}
	
	public void setTitle(String title){
		f.setTitle(title);
	}
}

