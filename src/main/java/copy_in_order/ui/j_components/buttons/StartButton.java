package copy_in_order.ui.j_components.buttons;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

import copy_in_order.ui.AppWindow;
import copy_in_order.ui.j_components.JFileTextField;
import copy_in_order.ui.listeners.FileCopyPropertyChangeListener;
import copy_in_order.ui.workers.FileCopyTask;

public class StartButton extends JButton {

	private static final long serialVersionUID = -6805354002802955071L;

	public StartButton(AppWindow appWindow, JProgressBar progressBar, JTextArea taskOutput, JButton stopButton) {
		super("Copy files");
		JFileTextField sourceFileTextField = appWindow.getSourceFileTextField();
		JFileTextField destinationFileTextField = appWindow.getDestinationFileTextField();
		Collection<Component> sensitiveComponents = Arrays.asList(
			this, sourceFileTextField, destinationFileTextField,
			appWindow.getSourceFileChooseButton(), appWindow.getDestinationFileChooseButton()
		);
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File sourceDirectory = sourceFileTextField.getSelectedFile();
				File destinationDirectory = destinationFileTextField.getSelectedFile();
				
				progressBar.setValue(0);
		    	taskOutput.setText("");
		    	for (Component component : sensitiveComponents) {
		    		component.setEnabled(false);
		    	}
		        
		        //Instances of javax.swing.SwingWorker are not reusuable, so
		        //we create new instances as needed.
		    	FileCopyTask task = new FileCopyTask(
		        	sourceDirectory, destinationDirectory, taskOutput, sensitiveComponents, stopButton
		        );
		        PropertyChangeListener propertyChangeListener = new FileCopyPropertyChangeListener(
	            	progressBar, taskOutput, task
	            );
		        task.addPropertyChangeListener(propertyChangeListener);
		        appWindow.setTask(task);
		        task.execute();
		        
		        // After a task starts, the user has the possibility of stopping it.
		        stopButton.setEnabled(true);
			}
		});
	}
}
