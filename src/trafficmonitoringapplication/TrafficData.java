/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trafficmonitoringapplication;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author Shane Plater
 */
public class TrafficData implements Serializable
{
    
    String Time;
    String LocationID;
    String NumberOfLanes;
    String TotalVehicleNum; 
    String AvVehicleNum;
    String AvVelocity; 

    public TrafficData()
    {
       Time = "";
       LocationID = "";
       NumberOfLanes = "";
       TotalVehicleNum = "";
       AvVehicleNum = "";
       AvVelocity = "";
               
    }      

    
    public TrafficData(ResultSet reader) throws SQLException
    {
       Time = reader.getString(2).substring(11, 19); // remove the date within the string to only return the time
       LocationID = reader.getString(3);
       NumberOfLanes = reader.getString(4);
       TotalVehicleNum = reader.getString(5);
       AvVehicleNum = reader.getString(6);
       AvVelocity = reader.getString(7);
    }  

    public ArrayList<TrafficData> GetAllData() throws SQLException
    {
        return Select("SELECT * FROM ITTownList");
    }
    public ArrayList Select(String query) throws SQLException
    {
        ArrayList<TrafficData> data = new ArrayList<>();
        java.io.File f = new java.io.File("1b_ITTownTrafficMonitoringPrototype.mdb");
        String fileLoc = f.getAbsolutePath(); // return database file path to have dynamic reference
        String conString = "jdbc:ucanaccess://" + fileLoc;
        Connection conn = DriverManager.getConnection(conString);
        Statement s = conn.createStatement();
        ResultSet rs = s.executeQuery(query);
         while (rs.next())
        {
           data.add(new TrafficData(rs));
        }
        
        return data;
    }
    public String getTime()
    {
        return Time;
    }

    public void setTime(String Time)
    {
        this.Time = Time;
    }

    public String getLocationID()
    {
        return LocationID;
    }

    public void setLocationID(String LocationID)
    {
        this.LocationID = LocationID;
    }

    public String getNumberOfLanes()
    {
        return NumberOfLanes;
    }

    public void setNumberOfLanes(String NumberOfLanes)
    {
        this.NumberOfLanes = NumberOfLanes;
    }

    public String getTotalVehicleNum()
    {
        return TotalVehicleNum;
    }

    public void setTotalVehicleNum(String TotalVehicleNum)
    {
        this.TotalVehicleNum = TotalVehicleNum;
    }

    public String getAvVehicleNum()
    {
        return AvVehicleNum;
    }

    public void setAvVehicleNum(String AvVehicleNum)
    {
        this.AvVehicleNum = AvVehicleNum;
    }

    public String getAvVelocity()
    {
        return AvVelocity;
    }

    public void setAvVelocity(String AvVelocity)
    {
        this.AvVelocity = AvVelocity;
    }
}
