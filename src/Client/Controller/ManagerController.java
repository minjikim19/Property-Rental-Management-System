package Client.Controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import Client.Communication;
import Client.View.ChangeFeeView;
import Client.View.EditPropertyView;
import Client.View.ManagerMenuView;
import Client.View.ManagerPropertyView;
import Client.View.PropertyView;
import Client.View.ReportRequestView;
import Client.View.SummaryReportView;
import Client.View.UserListView;
import Functionality.Manager;
import Functionality.Property;
import Functionality.PropertyFee;
import Functionality.SummaryReport;
import Functionality.User;

public class ManagerController implements ActionListener
{
	private ManagerMenuView managerView;
	private Manager manager;
	private SummaryReportView summary;
	private ManagerPropertyView propView;
	private ReportRequestView reportReq;
	private UserListView userView;
	private ChangeFeeView changeFeeView;
	private EditPropertyView editView;
	private ArrayList<Property> allProperties = new ArrayList<Property> ();
	private String[][] data;
	private ArrayList<User> u = new ArrayList<User>();
	private String[][] userData;
	private SummaryReport report;
	private String[][] reportData;
	
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
		managerView.addCreateListener(this);
		managerView.addExitListener(this);
	}
	
	private void showFeeWindow ()
	{
		changeFeeView = new ChangeFeeView ();
		PropertyFee fee = new PropertyFee();
		changeFeeView.setFee(fee.getAmount());
		changeFeeView.setPeriod(fee.getPeriod());
		changeFeeView.setVisible(true);
		changeFeeView.addSubmitListener (this);
		
	}
//test
	private void changeFee ()
	{
		Communication c = Communication.getInstance();
		try {
			PropertyFee temp = new PropertyFee ();
			if (changeFeeView.getChangeFee() != null)
				temp.setAmount(changeFeeView.getChangeFee());
			if (Integer.valueOf((changeFeeView.getChangePeriod())) != null)
				temp.setPeriod(changeFeeView.getChangePeriod());
			c.sendString("update fee");
			c.sendPropertyFee(temp);
			changeFeeView.setVisible(false);			
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
			showFeeWindow();
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
		case "submitFee":
			this.changeFee();
			break;
		case "exit":
			System.exit(1);
		}		
	}
	
	private void getUserInfo ()
	{
		Communication c = Communication.getInstance();
		
		try {
			c.sendString("get users");
			u = c.getUsers();
			System.out.println(Integer.toString(u.get(0).getID()));
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		setUserData();
			
		userView = new UserListView(userData, u);
		userView.setManagerController(this);
		//userView.setDisplay(c.getUsers());
		userView.setVisible(true);	
	}
	
	private void generateReport() 
	{
		Communication c = Communication.getInstance();
		try {
			c.sendString("get report");
			String start = reportReq.getStartYear() + "-" + reportReq.getStartMonth() + "-" + reportReq.getStartDate();
			c.sendString(start);
			String end = reportReq.getEndYear() + "-" + reportReq.getEndMonth() + "-" + reportReq.getEndDate();
			c.sendString(end);
			report = c.getReport();
			setReportData();
			summary = new SummaryReportView(reportData);
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
		
		try {
			c.sendString ("get all properties");
			allProperties = c.getProperties();
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		setPropertyData();
		
		propView = new ManagerPropertyView(data, allProperties);
		propView.setManagerController (this);
		propView.setVisible(true);
	}
	
//	private void getReport() 
//	{
//		Communication c = Communication.getInstance();
//		try {
//			c.sendString("get report");
//			c.sendString(reportReq.getStartDate());
//			c.sendString (reportReq.getEndDate());
//			SummaryReport report = c.getReport();
//			summaryReport = report;
//			summary = new SummaryReportView();
//			summary.setNumActiveList(report.getTotalActive());
//			summary.setNumHouseList(report.getTotalListed());
//			summary.setNumHouseRent(report.getTotalRented());
//			summary.setVisible(true);
//			summary.addCloseListener(this);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
	
	private void openReportForm() {
		reportReq = new ReportRequestView();
		reportReq.addGenerateReportListener(this);
		reportReq.setVisible(true);
	}
	
	public void updateStatus (Property temp)
	{
		Communication c = Communication.getInstance();
        
		try {
			c.sendString("change state");
			c.sendString (temp.getListingState());
			c.sendString(Integer.toString(temp.getID()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setPropertyData() {
		data = new String[allProperties.size()][6];
		for(int i = 0; i < allProperties.size(); i++) {
			   for(int j = 0; j < 6; j++) {
				    if (j == 0) {
					   data[i][j] = Integer.toString(allProperties.get(i).getID());
	       			} else if (j == 1) {
	       			   data[i][j] = allProperties.get(i).getType();
	       		    } else if (j == 2) {
	       			   data[i][j] = Integer.toString(allProperties.get(i).getNumOfBedrooms());
	       		    } else if (j == 3) {
	       			   data[i][j] = Integer.toString(allProperties.get(i).getNumOfBathrooms());
	       		    } else if (j == 4) {
	       			   if (allProperties.get(i).isFurnished() == true) {
	       				   data[i][j] = "Yes";
	       			   } else if (allProperties.get(i).isFurnished() == false) {
	       				   data[i][j] = "No";
	       			   }
	       		    } else if (j == 5) {
	       			   data[i][j] = allProperties.get(i).getCityQuadrant();
	       		    }
	       	   }
	       }
	}
	
	public void setUserData() {
		userData = new String[u.size()][6];
		   for(int i = 0; i < u.size(); i++) {
			   for(int j = 0; j < 6; j++) {
				    if (j == 0) {
				       //userData[i][j] = "test";
				    	userData[i][j] = Integer.toString(u.get(i).getID());
				    } else if (j == 1) {
				    	userData[i][j] = u.get(i).getUserName();
	       			} else if (j == 2) {
	       				userData[i][j] = u.get(i).getFirstName();
	       		    } else if (j == 3) {
	       		    	userData[i][j] = u.get(i).getLastName();
	       		    } else if (j == 4) {
	       		    	userData[i][j] = u.get(i).getEmail();
	       		    } else if (j == 5) {
	       		    	userData[i][j] = u.get(i).getType();
	       		    }
	       	   }
	       }
	}
	
	public void setReportData() {
		reportData = new String[report.getHousesRented().size()][3];
		for(int i = 0; i < report.getHousesRented().size(); i++) {
			for(int j = 0; j < 3; j++) {
			    if (j == 0) {
				   reportData[i][j] = report.getHousesRented().get(i).getLandlordName();
       			} else if (j == 1) {
       			   reportData[i][j] = Integer.toString(report.getHousesRented().get(i).getID());
       		    } else if (j == 2) {
       			   reportData[i][j] = report.getHousesRented().get(i).getAddress();
       		    }
       	   }
	    }
	}
}
