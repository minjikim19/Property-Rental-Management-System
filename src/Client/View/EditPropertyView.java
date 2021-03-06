package Client.View;

import javax.swing.*;
import Functionality.Property;

import java.awt.*;
import java.awt.event.ActionListener;

public class EditPropertyView extends JFrame{
	private static final long serialVersionUID = 1L;
	private String [] status = {"Active", "Rented", "Cancelled", "Suspended"}; 
	private JButton saveBtn = new JButton("Save");
	private JTextArea id = new JTextArea();
	private JComboBox comboBox = new JComboBox(status);
	private Property property;
	
	public EditPropertyView(Property p) {
		property = p;
		
		setSize(278, 222);
		getContentPane().setBackground(new Color(230, 230, 250));
		setTitle("Edit Property");
		getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Property ID");
		lblNewLabel.setBounds(15, 16, 94, 20);
		getContentPane().add(lblNewLabel);
		
		JLabel lblCurrentStatus = new JLabel("Status");
		lblCurrentStatus.setBounds(25, 57, 112, 20);
		getContentPane().add(lblCurrentStatus);
		
		comboBox.setBounds(124, 54, 117, 26);
		getContentPane().add(comboBox);
		
		saveBtn.setBounds(56, 108, 115, 29);
		getContentPane().add(saveBtn);
		
		id.setEnabled(false);
		id.setEditable(false);
		id.setBounds(124, 16, 117, 24);
		getContentPane().add(id);
	}
	
	public void addSaveListener(ActionListener al)  {
    	saveBtn.addActionListener(al);
    	saveBtn.setActionCommand("saveEditProperty");
	}
	
	public void setID() {
		id.setText(Integer.toString(property.getID()));
	}
	
	public void setStatus()
	{
		comboBox.setSelectedItem(property.getListingState());
	}
	
	public String getStatus() {
		return (String) comboBox.getSelectedItem();
	}
	
	public Property getSelectedProperty() {
		return property;
	}
}