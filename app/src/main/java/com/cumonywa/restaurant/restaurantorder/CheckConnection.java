package com.cumonywa.restaurant.restaurantorder;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class CheckConnection {

    public Connection shareclass(Context context){
        SharedPreferences mPreference= PreferenceManager.getDefaultSharedPreferences(context);
        String ip,dbname,user,password;
        ip=mPreference.getString("urlkey","");
        dbname=mPreference.getString("dbkey","");
        user=mPreference.getString("userkey","");
        password=mPreference.getString("passwordKey","");
        Log.i("ip address",ip+dbname+user+password);
        Connection connection=connectionclass(ip,dbname,user,password);
        return connection;
    }

    public Connection connectionclass(String ip,String db,String user,String password)
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;
        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            ConnectionURL = "jdbc:jtds:sqlserver://" + ip +";databaseName="+ db+";user=" + user+ ";password=" + password + ";integratedSecurity=true";
            connection = DriverManager.getConnection(ConnectionURL);
        }
        catch (SQLException se)
        {
            Log.i("error here 1 $$$$$: ", se.getMessage());
        }
        catch (ClassNotFoundException e)
        {
            Log.i("error here 2 $$$$$ : ", e.getMessage());
        }
        catch (Exception e)
        {
            Log.i("error here 3 $$$$$ : ", e.getMessage());
        }
        return connection;
    }

}
