/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.hospital;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Hospital extends JFrame implements ActionListener {

    Connection conn;
    JFrame frame1;
    JButton b1_clinic,b1_Year,b1_Month,b2_No_App_Patient,b3_UpdateAgeGroup,b4_Production,b5_Delete;
    Connection con;
    ResultSet rs;
    Statement stmt;
    static JTable table;

    String[] columnNamesClinic = {"AppointmentCount","clinicName"};
    String[] columnNamesYear = {"AppointmentCount","Year"};
    String[] columnNamesMonth = {"AppointmentCount","Month","Year"};
    String[] columnNamesPatientInfo={"PatientId","Patient FirstName","PatientLastName","City","State","AppointmentDate"};
    String[] columnNamesPatientInfoUpd={"PatientId","Patient FirstName","PatientLastName","Birthdate","Age","PatientAgeGroup"};
    String[] columnNamesProductionInfo={"ProcedureType","ClinicnName","ProviderName","Month","Year","Amount"};
   
       Hospital() {

        b1_clinic = new JButton("ClinicAppointmentCount");
        b1_clinic.setBounds(20, 60, 170, 30);
        b1_clinic.addActionListener(this);

        b1_Year = new JButton("YearlyAppointmentCount");
        b1_Year.setBounds(210, 60, 220, 30);
        b1_Year.addActionListener(this);
        
        b1_Month = new JButton("MonthlyAppointmentCount");
        b1_Month.setBounds(450, 60, 190, 30);
        b1_Month.addActionListener(this);
 
        
        b2_No_App_Patient=new JButton("No FututeAppointment Patient list");
        b2_No_App_Patient.setBounds(210, 120, 230, 30);
        b2_No_App_Patient.addActionListener(this);
        
        b3_UpdateAgeGroup=new JButton("Update Age and Age Group");
        b3_UpdateAgeGroup.setBounds(210, 180, 220, 30);
        b3_UpdateAgeGroup.addActionListener(this); 
        
        b4_Production=new JButton("Production Info");
        b4_Production.setBounds(210, 240, 220, 30);
        b4_Production.addActionListener(this); 
        
        b5_Delete=new JButton("Delete Appointment(Amount<50)");
        b5_Delete.setBounds(210, 300, 220, 30);
        b5_Delete.addActionListener(this); 
        
        setTitle("Hospital Management");
        setLayout(null);
        setVisible(true);
        setSize(500, 500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        add(b1_clinic);
        add(b1_Year);
        add(b1_Month);
        add(b2_No_App_Patient);
        add(b3_UpdateAgeGroup);
        add(b4_Production);
        add(b5_Delete);
              
        try {

            con = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databasename=Hospital; user=sa; password=surekha123");
            stmt =conn.createStatement();
            DatabaseMetaData dm = (DatabaseMetaData) conn.getMetaData();
                System.out.println("Driver name: " + dm.getDriverName());
                System.out.println("Driver version: " + dm.getDriverVersion());
         
            stmt.close();
            rs.close();

        } catch (SQLException e) {
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == b1_clinic) {
            showTAppointByClinic();
        }

        if (ae.getSource() == b1_Year) {
            showAppointByYear();
        }

        if (ae.getSource() == b1_Month) {
            showAppointByMonth();
        }

        if (ae.getSource() == b2_No_App_Patient) {
            showNo_App_Patient();
        }

        if (ae.getSource() == b3_UpdateAgeGroup) {
            updateAgeGroup();
        }

        if (ae.getSource() == b4_Production) {
            show_Production_Info();
        }

        if (ae.getSource() == b5_Delete) {
            cancelPatientAppointments();
        }
 

    }

    public void showTAppointByClinic() {

        frame1 = new JFrame("No Of Appointments By Clinic");
        frame1.setLayout(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel();

        model.setColumnIdentifiers(columnNamesClinic);

        table = new JTable();
        table.setModel(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFillsViewportHeight(true);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setHorizontalScrollBarPolicy(
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        scroll.setVerticalScrollBarPolicy(
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        int Count = 0;
        String clinicName = "";

        try {
           con = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databasename=Hospital; user=sa; password=surekha123");
           stmt = con.createStatement();
           String query ="  SELECT count(*) as Count ,clinicinfo.clinicname as clinicname FROM appointmentinfo  Inner Join clinicinfo On appointmentinfo.clinicid=clinicinfo.clinicid\n" +
            "  group by clinicinfo.clinicid ,clinicinfo.clinicname ";
            rs = stmt.executeQuery(query);

            int i = 0;

            while(rs.next()) {

                Count = rs.getInt("Count");
                clinicName = rs.getString("clinicname");
                model.addRow(new Object[]{Count,clinicName});
                i++;

            }

            if (i < 1) {

                JOptionPane.showMessageDialog(null, "No Record Found", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {

            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        }
        
        frame1.add(scroll);
        frame1.setVisible(true);
        frame1.setSize(400, 300);
    }

 public void showAppointByYear() {
        frame1 = new JFrame("No Of Appointments By Year");
        frame1.setLayout(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columnNamesYear);

        table = new JTable();
        table.setModel(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFillsViewportHeight(true);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setHorizontalScrollBarPolicy(
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        scroll.setVerticalScrollBarPolicy(
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        int Count = 0;
        int Year =0;

        try {
           con = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databasename=Hospital; user=sa; password=surekha123");
           stmt = con.createStatement();
           String query ="SELECT count(*) as AppointmentCount,DATEPART(yyyy, appdate) as Year FROM appointmentinfo\n" +
"  group by DATEPART(yyyy, appdate)";
            rs = stmt.executeQuery(query);

            int i = 0;

            while(rs.next()) {

                Count = rs.getInt("AppointmentCount");
                Year = rs.getInt("Year");
                model.addRow(new Object[]{Count,Year});
                i++;

            }

            if (i < 1) {

                JOptionPane.showMessageDialog(null, "No Record Found", "Error", JOptionPane.ERROR_MESSAGE);

            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        }

        frame1.add(scroll);
        frame1.setVisible(true);
        frame1.setSize(400, 300);

 }
  public void showAppointByMonth() {
      frame1 = new JFrame("No Of Appointments By Month");
      frame1.setLayout(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel();

        model.setColumnIdentifiers(columnNamesMonth);

        table = new JTable();
        table.setModel(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFillsViewportHeight(true);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setHorizontalScrollBarPolicy(
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        scroll.setVerticalScrollBarPolicy(
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        int Count = 0;
        int Month =0;
        int Year=0;
        

        try {
           con = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databasename=Hospital; user=sa; password=surekha123");
           stmt = con.createStatement();
           String query =" SELECT count(*) as AppointmentCount,DATEPART(MM, appdate) as Month, DATEPART(yyyy, appdate) as Year FROM appointmentinfo\n" +
"  group by DATEPART(MM, appdate),DATEPART(yyyy, appdate) Order by DATEPART(yyyy, appdate)";
            rs = stmt.executeQuery(query);

            int i = 0;
            while(rs.next()) {

                Count = rs.getInt("AppointmentCount");
                Month = rs.getInt("Month");
                Year = rs.getInt("Year");
                model.addRow(new Object[]{Count,Month,Year});
                i++;

            }
            if (i < 1) {

                JOptionPane.showMessageDialog(null, "No Record Found", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        }

        frame1.add(scroll);
        frame1.setVisible(true);
        frame1.setSize(400, 300);

 }
public void showNo_App_Patient() {
      frame1 = new JFrame("Patient List with No Future Appointment");
      frame1.setLayout(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columnNamesPatientInfo);

        table = new JTable();
        table.setModel(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFillsViewportHeight(true);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setHorizontalScrollBarPolicy(
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        scroll.setVerticalScrollBarPolicy(
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        int PatientId = 0;
        String FirstName ="";
        String LasttName ="";
        String City ="";
        String State ="";
        Date AppointmentDate;
        int Year=0;
        
        try {
           con = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databasename=Hospital; user=sa; password=surekha123");
           stmt = con.createStatement();
           String query =" select patientinfo.patId,patientinfo.firstname ,patientinfo.lastname,patientinfo.city,patientinfo.state,appointmentinfo.appdate from dbo.patientinfo Inner Join appointmentinfo\n" +
" On patientinfo.patId=appointmentinfo.patid\n" +
" where appointmentinfo.appdate< CURRENT_TIMESTAMP";
            rs = stmt.executeQuery(query);

            int i = 0;

            while(rs.next()) {

                PatientId = rs.getInt("patId");
                FirstName = rs.getString("firstname");
                LasttName = rs.getString("lastname");
                City = rs.getString("city");
                State=rs.getString("state");
                AppointmentDate=rs.getDate("appdate");
                model.addRow(new Object[]{PatientId,FirstName,LasttName,City,State,AppointmentDate});
                i++;

            }

            if (i < 1) {

                JOptionPane.showMessageDialog(null, "No Record Found", "Error", JOptionPane.ERROR_MESSAGE);

            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        }

        frame1.add(scroll);
        frame1.setVisible(true);
        frame1.setSize(400, 300);

 }
    public void updateAgeGroup() {

        frame1 = new JFrame("Patient Info After Update");
        frame1.setLayout(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columnNamesPatientInfoUpd);

        table = new JTable();
        table.setModel(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFillsViewportHeight(true);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setHorizontalScrollBarPolicy(
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        scroll.setVerticalScrollBarPolicy(
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        int PatientId = 0;
        String FirstName ="";
        String LasttName ="";
        int Age=0;
        String PatientAgeGroup ="";
        Date Birthdate;
        int Year=0;

        try {
           con = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databasename=Hospital; user=sa; password=surekha123");
           stmt = con.createStatement();
           
           String selectQuery =" select patId,lastname,firstname,birthdate,age,patientagegroup from patientinfo";
           String updateQuery="update patientinfo set age=DATEDIFF(day,birthdate,GETDATE())/365 , patientagegroup=case when DATEDIFF(day,birthdate,GETDATE())/365 <18 then 'Minor' when DATEDIFF(day,birthdate,GETDATE())/365 >18  then 'Adult' end ";
           
           int count = stmt.executeUpdate(updateQuery);
           System.out.println(count +"Records Updated");
           rs = stmt.executeQuery(selectQuery);
           int i = 0;

            while(rs.next()) {

                PatientId = rs.getInt("patId");
                FirstName = rs.getString("firstname");
                LasttName = rs.getString("lastname");
                Birthdate = rs.getDate("birthdate");
                Age=rs.getInt("age");
                PatientAgeGroup=rs.getString("patientagegroup");
                model.addRow(new Object[]{PatientId,FirstName,LasttName,Birthdate,Age,PatientAgeGroup});
                i++;
            }
            if (i < 1) {

                JOptionPane.showMessageDialog(null, "No Record Found", "Error", JOptionPane.ERROR_MESSAGE);

            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        }

        frame1.add(scroll);
        frame1.setVisible(true);
        frame1.setSize(400, 300);

 }
    public void show_Production_Info() {
        
      frame1 = new JFrame("Production Info");
      frame1.setLayout(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columnNamesProductionInfo);

        table = new JTable();
        table.setModel(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFillsViewportHeight(true);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setHorizontalScrollBarPolicy(
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        scroll.setVerticalScrollBarPolicy(
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        String procedureType ="";
        String clinicnName ="";
        String ProviderName ="";
        int month =0;
        int year=0;
        float amount=0.0f;

        try {
           con = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databasename=Hospital; user=sa; password=surekha123");
           stmt = con.createStatement();
           String query ="select proceduretype, clinicinfo.clinicname,doctorinfo.firstname as ProviderName,DATEPART(MM, appointmentinfo.appdate) as Month, DATEPART(yyyy, appointmentinfo.appdate) as Year,sum(appointmentinfo.amount) as Amount from transactioninfo inner join clinicinfo on transactioninfo.clinicid=clinicinfo.clinicid \n" +
"inner join doctorinfo on transactioninfo.prov=doctorinfo.IDNo\n" +
"inner join appointmentinfo on transactioninfo.prov=appointmentinfo.provider\n" +
"group by proceduretype,clinicinfo.clinicname,prov,doctorinfo.firstname,appointmentinfo.provider,DATEPART(MM, appointmentinfo.appdate) , DATEPART(yyyy, appointmentinfo.appdate)\n" +
"order by clinicname,prov,DATEPART(MM, appointmentinfo.appdate), DATEPART(yyyy, appointmentinfo.appdate)";
            rs = stmt.executeQuery(query);

            int i = 0;

            while(rs.next()) {

             
                procedureType = rs.getString("proceduretype");
                clinicnName = rs.getString("clinicname");
                ProviderName = rs.getString("ProviderName");
                month=rs.getInt("Month");
                year=rs.getInt("Year");
                amount=rs.getFloat("Amount");
                model.addRow(new Object[]{procedureType,clinicnName,ProviderName,month,year,amount});
                i++;

            }

            if (i < 1) {

                JOptionPane.showMessageDialog(null, "No Record Found", "Error", JOptionPane.ERROR_MESSAGE);

            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        }

         frame1.add(scroll);
        frame1.setVisible(true);
        frame1.setSize(400, 300);

 }
    
   public void cancelPatientAppointments() {
      
        try {
           con = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databasename=Hospital; user=sa; password=surekha123");
           stmt = con.createStatement();
           String query ="delete from dbo.appointmentinfo\n" +
"where amount<50 ";
            int count = stmt.executeUpdate(query);
            JOptionPane.showMessageDialog(null,null,count+ " Records Deleted",JOptionPane.ERROR_MESSAGE);

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        }
 }
    public static void main(String args[]) {

        new Hospital();

    }

}
