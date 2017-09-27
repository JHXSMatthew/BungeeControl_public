package com.github.JHXSMatthew;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.huskehhh.mysql.mysql.MySQL;

public class MySQLManager {
	private Connection c =null;
	private MySQL my;
	SimpleDateFormat sdf;
	public MySQLManager(){
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    this.my = new MySQL("192.168.123.2", "3306", "lobby", "lobby", "NO_PUBLIC_INFO");
	}
	
	public void openConnection(){
	    try {
			c = my.openConnection();
		} catch (ClassNotFoundException e) {
			System.out.print("Connection error !");
			e.printStackTrace();
		} catch (SQLException e1) {
			System.out.print("Connection error !");
			e1.printStackTrace();
		}
	}
	
	public void CreateTblae() throws SQLException{
		Statement s = this.c.createStatement();
		s.executeUpdate("CREATE TABLE IF NOT EXISTS `banList` (`Name` varchar(32),`Date` date ,`Reason` varchar(32));");
		s.close();
	}
	
	public void clsoeConnection() throws SQLException{
		this.c.close();
	}
	
	
	
	public void closeDB() throws SQLException{
		this.my.closeConnection();
	}
	
	public int getStack(String name) throws ClassNotFoundException, SQLException {
		try {
			if (!this.my.checkConnection()) {
				this.c = this.my.openConnection();
			}
		} catch (Exception e) {

		}
		try (			Statement s = this.c.createStatement();
						 ResultSet result = s.executeQuery("SELECT Stack FROM `bannedPlayers` Where `Name`='" + name + "';");){
		if (result.next()) {
			int returnValue = result.getInt("Stack");
			result.getStatement().close();
			result.close();
			s.close();
			return returnValue;
		}
		}catch(Exception e){

		}
		return 0;
	}
	
	public void updateBannedPlayer(String name,String reason,int days , boolean isNew ,String tid,int stack){
		try {
			if (!this.my.checkConnection()) {
				this.c = this.my.openConnection();
			}
		}catch (Exception e){

		}
		try(
		Statement s = this.c.createStatement();) {
			Calendar c = Calendar.getInstance();
			c.add(java.util.Calendar.DATE, days);//name
			if (isNew) {
				s.executeUpdate("INSERT INTO `bannedPlayers` (`Time`,`Name`,`Date`,`Reason`,`Tid`,`Stack`)"
						+ " VALUES ('" + System.currentTimeMillis() + "','"
						+ name + "','"
						+ this.sdf.format(c.getTime()) + "','"
						+ reason + "','"
						+ tid + "','"
						+ stack
						+ "');");
			} else {
				s.executeUpdate("UPDATE `bannedPlayers` SET `Time`='" + System.currentTimeMillis()
						+ "',`Date`='" + this.sdf.format(c.getTime())
						+ "',`Reason`='" + reason
						+ "',`Tid`='" + tid
						+ "',`Stack`='" + stack
						+ "' Where `Name`='" + name + "';");
			}
		}catch (Exception e){

		}
	}
	
	public boolean isBanned(String name) {
		try {
			if(!this.my.checkConnection()){
                this.c = this.my.openConnection();
            }
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try(Statement s = this.c.createStatement();
		ResultSet result = s.executeQuery("SELECT Date FROM `bannedPlayers` Where `Name`='"+name+"';");) {
			if (result.next()) {
				Date returnValue = result.getDate("Date");
				result.getStatement().close();
				result.close();
				s.close();
				return !isDateBefore(returnValue);
			}
		}catch(Exception e){

		}
		return false;
	}
	
	public BannedInfoContainer getBannedLine(String name){
		try {
			if (!this.my.checkConnection()) {
				this.c = this.my.openConnection();
			}
		}catch(Exception e){

		}
		try(Statement s = this.c.createStatement();
		ResultSet result = s.executeQuery("SELECT Date,Reason,Tid,Stack FROM `bannedPlayers` Where `Name`='"+name+"';");) {
			if (result.next()) {
				BannedInfoContainer container = new BannedInfoContainer();
				container.date = result.getDate("Date").toString();
				container.reason = result.getString("Reason");
				container.tid = result.getString("Tid");
				container.stack = result.getInt("Stack");
				return container;
			}
		}catch(Exception e){

		}
		return null;
		
	}
	
	
	
	public ResultSet getUser(String name) throws ClassNotFoundException, SQLException{
		if(!this.my.checkConnection()){
			this.c = this.my.openConnection();
		}
		Statement s = this.c.createStatement();
		ResultSet result = s.executeQuery("SELECT * FROM `banList` Where `Name`='"+name+"';");
		return result;
	}
	
	
	public boolean isDateBefore(Date input){
		return input.before(new Date());
	}
	
	public void deletePlayer(String name) throws ClassNotFoundException, SQLException{
		if(!this.my.checkConnection()){
			this.c = this.my.openConnection();
		}
		Statement s = this.c.createStatement();
		s.executeUpdate("DELETE FROM `banList` Where `Name`='"+name+"';");
		s.close();
		s=null;
	}
	
	public void updatePlayer(String name,int days , String reason){

		try {
			if (!this.my.checkConnection()) {
				this.c = this.my.openConnection();
			}
		}catch(Exception e){

		}

		Calendar c = Calendar.getInstance();  
		c.add(java.util.Calendar.DATE, days);
		try(Statement s = this.c.createStatement();
			ResultSet result = getUser(name);) {

			if (result.next()) {
				//如果封号尚未结束,时间叠加
				Date date = result.getDate("Date");
				c.setTime(date);
				c.add(Calendar.DATE, days);
				s.executeUpdate("UPDATE `banList` SET `Date`='" + this.sdf.format(c.getTime()) + "',`Reason`='" + reason + "' Where `Name`='" + name + "';");
				date = null;
			} else {

				//如果封号时间结束 或者从未有过封号记录，则创建新封号纪录

				s.executeUpdate("INSERT INTO `banList` (`Name`,`Date`,`Reason`) VALUES ('" + name + "','" + this.sdf.format(c.getTime()) + "','" + reason + "');");
			}
		}catch(Exception e){

		}

	}


	
	
}
