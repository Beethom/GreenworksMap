package com.GREENWORKS.eco;
 
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.GREENWORKS.eco.constants.LoggerConstants;

import org.tinylog.Logger;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
 
@WebServlet("/editlocation")
public class EditLocation extends HttpServlet {
    
    public EditLocation()
    {
        super();
    }
 
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {

        // Get ID of location
        String locationID = request.getParameter("locationID");
        String deleteChoose = request.getParameter("deleteChoose");
        String editChoose = request.getParameter("editChoose");

        Logger.info("Edit location request recieved from: " + request.getRemoteAddr());
        // Statement to select all location data
		String sql = "SELECT * FROM locations WHERE id = '" + locationID + "' LIMIT 1";

        // If selected delete
        if(deleteChoose != "" && deleteChoose != null)
        {
            sql = "DELETE FROM locations WHERE id = '" + locationID + "'";
        }

        // Connect to MySQL
        MysqlConnect mysqlConnect = new MysqlConnect();

		try
		{
			// Try statement
			PreparedStatement statement = mysqlConnect.connect().prepareStatement(sql);

            // If selected edit
            if(editChoose != "" && editChoose != null)
            {
                // Loop through statement
                ResultSet rs = statement.executeQuery();
                Logger.info(LoggerConstants.QUERY_EXECUTED + sql);
                while(rs.next())
                {
                    // Send data back
                    request.setAttribute("id", rs.getString(1));
                    request.setAttribute("iconid", rs.getString(2));
                    request.setAttribute("address", rs.getString(3));
                    request.setAttribute("name", rs.getString(4));
                    request.setAttribute("coord", rs.getString(5));
                    request.setAttribute("dateStart", rs.getString(6));
                    request.setAttribute("dateEnd", rs.getString(7));
                    request.setAttribute("content", rs.getString(8));
                }
            }
            else
            {
                // Execute delete
			    statement.executeUpdate();
                Logger.info(LoggerConstants.QUERY_UPDATE + sql);
            }

            // Redirect user
            RequestDispatcher dispatcher = request.getRequestDispatcher("admin.jsp");
            dispatcher.forward(request, response);
		}
		catch (SQLException e)
		{
			// Error
            Logger.error(LoggerConstants.QUERY_FAILED + sql);
			e.printStackTrace();
		}
		finally
		{
			// Disconnect when done
			mysqlConnect.disconnect();
		}
    }
}