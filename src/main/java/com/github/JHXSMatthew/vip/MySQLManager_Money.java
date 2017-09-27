package com.github.JHXSMatthew.vip;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import com.huskehhh.mysql.mysql.MySQL;
import com.mcndsj.BC_RedisConnector.Utils.SQLUtils;
import com.mcndsj.JHXSMatthew.Shared.MoneyManager;

public class MySQLManager_Money {

	
	public MySQLManager_Money(){
	}

	
	
	public int getVipLevel(String name) throws ClassNotFoundException, SQLException{
		try(Connection c = MoneyManager.getInstance().getConnection();
		Statement s = c.createStatement();
		ResultSet result = s.executeQuery("SELECT level FROM `vipStats` Where `name`='"+name+"';")) {
			if (result.next()) {
				int i = result.getInt("level");;
				return i;
			}
		}catch(Exception e){

		}
		return -1;
	}
	
	
}
