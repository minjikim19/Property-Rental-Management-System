package Client.Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import Client.Communication;
import Client.View.ManagerMenuView;
import Client.View.ManagerPropertyView;
import Client.View.PropertyView;
import Client.View.ReportRequestView;
import Client.View.SummaryReportView;
import Client.View.UserListView;
import Functionality.Manager;
import Functionality.Property;
import Functionality.SummaryReport;

public class ManagerController implements ActionListener
{
	private ManagerMenuView managerView;
	private Manager manager;
	private SummaryReportView summary;
	private ManagerPropertyView propView;
	private ReportRequestView reportReq;
	private UserListView userView;
	
	public ManagerController (Manager m)
	{
		managerView = new ManagerMenuView();
		managerView.setVisible(true);
		manager = m;
		addListeners();
	}
	
	private void addListeners ()
	{
		managerView.addChangeFeeListener(this);
		managerView.addChangeStatusListener(this);
		managerView.addGetUserInfoListener(this);
	}
	
	private void changeFee ()
	{
		Communication c = Communication.getInstance();
		try {
			c.sendString("change fee");
			Double newFee = managerView.getNewFee(); //TODO
			c.sendString(newFee.toString());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		switch (e.getActionCommand())
		{
		case "change fee":
			changeFee();
			break;
		case "change status":
			this.changeStatus();			
			break;
		case "getInfo":
			getUserInfo();
			break;
		case "report":
			openReportForm();
			break;
		case "generateReport":
			generateReport();
			break;
		case "closeReport":
			summary.setVisible(false);
			break;
		}		
	}
	
	private void getUserInfo ()
	{
		userView = new UserListView ();
		Communication c = Communication.getInstance();
		try {
			c.sendString("get users");
			userView.setDisplay(c.getUsers());
			userView.setVisible(true);
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	private void generateReport() 
	{
		Communication c = Communication.getInstance();
		try {
			c.sendString("get report");
			c.sendString(reportReq.getStartDate());
			c.sendString (reportReq.getEndDate());
			SummaryReport report = c.getReport();
			summary = new SummaryReportView();
			summary.setNumActiveList(report.getTotalActive());
			summary.setNumHouseList(report.getTotalListed());
			summary.setNumHouseRent(report.getTotalRented());
			summary.setVisible(true);
			summary.addCloseListener(this);
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void changeStatus() 
	{
		Communication c = Communication.getInstance();
		ArrayList<Property> allProperties = new ArrayList<Property> ();
		try {
			c.sendString ("get all properties");
			allProperties = c.getProperties();
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		propView = new ManagerPropertyView();		
		propView.setDisplay(allProperties);
		propView.setManagerController (this);
	}
	
	private void getReport() 
	{
		Communication c = Communication.getInstance();
		try {
			c.sendString("get report");
			c.sendString(reportReq.getStartDate());
			c.sendString (reportReq.getEndDate());
			SummaryReport report = c.getReport();
			summary = new SummaryReportView();
			summary.setNumActiveList(report.getTotalActive());
			summary.setNumHouseList(report.getTotalListed());
			summary.setNumHouseRent(report.getTotalRented());
			summary.setVisible(true);
			summary.addCloseListener(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void openReportForm() {
		reportReq = new ReportRequestView();
		reportReq.addGenerateReportListener(this);
	}
	
	public void updateStatus (Property p)
	{
		Communication c = Communication.getInstance();
		try {
			c.sendString("change state");
			c.sendString (p.getListingState());
			c.sendString(Integer.toString(p.getID()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
