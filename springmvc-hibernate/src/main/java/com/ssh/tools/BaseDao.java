package com.ssh.tools;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 使用需要继承basedao  并且<T>要变成相应的实体Bean
 */

public  class BaseDao<T> {
	
	private Class<T> clz;
	
	
	{
		getClz();
	}
	
	public Class<T> getClz(){
		if(clz==null){
			clz = (Class<T>)(((ParameterizedType)(this.getClass().getGenericSuperclass())).getActualTypeArguments()[0]);
		}
		return clz;
	}
	
	/**
	 * 删方法
	 * @return
	 */
	public int delete(String id,String name) {
		 Connection con =null;
		 ResultSet rs = null;
		 PreparedStatement sta =null;
		 int row=0;
		try {
			String sql="delete from "+name+" where id in (";
			String[] ids=id.split(",");
			List<Object> list=new ArrayList<>();
			for(int i=0;i<ids.length;i++){
				sql+="?,";
				list.add(ids[i]);
			}
			sql=sql.substring(0,sql.length()-1)+")";
			con=JDBCUtil.getConnection();
			sta=con.prepareStatement(sql);
			 //参数的绑定
			 for(int i=0;i<list.size();i++){
			   sta.setObject(i+1, list.get(i));
			 }
			 row = sta.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			JDBCUtil.closeConnection(rs, sta, con);
		}
		 return row;
	}
	/**
	 * 修改通用方法
	 * @return
	 */
	public int update(T en,String name) {
		Connection con =null;
		ResultSet rs = null;
		PreparedStatement sta =null;
		T t=null;
		int b=0;
		try {
			List<Object> list=new ArrayList<>();
			String sql=" update "+name+" set ";
			Field[] ff=en.getClass().getDeclaredFields();
			for(Field i:ff){
				i.setAccessible(true);
				if(i.getName()!="id"){
					Field f = en.getClass().getDeclaredField(i.getName());
					f.setAccessible(true);
					sql+=i.getName()+"= ? ,";
					list.add(f.get(en));
				}
			}
			sql=sql.substring(0,sql.length()-1);
			sql+=" where id =? ";
			Field f = en.getClass().getDeclaredField("id");
			f.setAccessible(true);
			list.add(f.get(en));
			con=JDBCUtil.getConnection();
			sta=con.prepareStatement(sql);
			//参数绑定
			for(int i=0;i<list.size();i++){
				sta.setObject(i+1, list.get(i));
			}
			b=sta.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			JDBCUtil.closeConnection(rs, sta, con);
		}
		return b;
	}
	/**
	 * 保存对象
	 * @return
	 */
	public int save(T en,String name) {
		Connection con =null;
		ResultSet rs = null;
		PreparedStatement sta =null;
		T t=null;
		int b=0;
		try {
			List<Object> list=new ArrayList<>();
			String sql="insert into "+name+"(";
			String sql2=" values ( ";
			Field[] ff=en.getClass().getDeclaredFields();
			for(Field i:ff){
				i.setAccessible(true);
				if(i.getName()!="id"){
					Field f = en.getClass().getDeclaredField(i.getName());
					f.setAccessible(true);
					sql+=i.getName()+",";
					sql2+="? ,";
					list.add(f.get(en));
				}
			}
			sql=sql.substring(0,sql.length()-1);
			sql+=")";
			sql2=sql2.substring(0,sql2.length()-1);
			sql2+=")";
			con=JDBCUtil.getConnection();
			sta=con.prepareStatement(sql+sql2);
			//参数绑定
			for(int i=0;i<list.size();i++){
				sta.setObject(i+1, list.get(i));
			}
			b=sta.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			JDBCUtil.closeConnection(rs, sta, con);
		}
return b;
	}
	/**
	 * 查询结果返回单个实体类对象
	 * @param sql
	 * @return
	 */
	public T load(String sql,Object []o) {
		 Connection con =null;
		 ResultSet rs = null;
		 PreparedStatement sta =null;
		 T t=null;
		try {
			con=JDBCUtil.getConnection();
			 sta=con.prepareStatement(sql);
			 //参数绑定
			 for(int i=0;i<o.length;i++){
				   sta.setObject(i+1, o[i]);
				 }
			 rs=sta.executeQuery();
			 Field []field=clz.getDeclaredFields();
			
			 if(rs.next()){
				 t = clz.newInstance();
				 for(Field f:field){
					 f.setAccessible(true);
					 if(!(f.getName().endsWith("list"))){
						 try {
								String str= ""+f.getType();
								if(str.equals("int")){
									f.set(t, rs.getInt(f.getName()));
								}else if(str.equals("double")){
									f.set(t, rs.getDouble(f.getName()));
								}else if(str.equals("class java.sql.Date")){
									f.set(t, rs.getDate(f.getName()));
								}else if(str.equals("float")){
									f.set(t, rs.getFloat(f.getName()));
								}else if(str.equals("short")){
									f.set(t, rs.getShort(f.getName()));
								}else{//字符串类型
									f.set(t, rs.getObject(f.getName()));
								}
						} catch (Exception e) {
						}
					 }
				 }
			 }
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			JDBCUtil.closeConnection(rs, sta, con);
		}
		return t;
	}
/**
 * 查询所有返回一个集合
 */
	public List<T> list(String sql,Object []o) {
		 Connection con =null;
		 ResultSet rs = null;
		 PreparedStatement sta =null;
		List<T> list = new ArrayList<T>();
		 T t=null;
			try {
				con=JDBCUtil.getConnection();
				 sta=con.prepareStatement(sql);
				 //参数绑定	
				 for(int i=0;i<o.length;i++){
					   sta.setObject(i+1, o[i]);
					 }
				 rs=sta.executeQuery();
				 Field []field=clz.getDeclaredFields();
				 while (rs.next()) {
					 t = clz.newInstance();
					 for(Field f:field){
						 f.setAccessible(true);
						 if(!(f.getName().endsWith("list"))){
							 try {
									String str= ""+f.getType();

									if(str.equals("int")){
										f.set(t, rs.getInt(f.getName()));
									}else if(str.equals("double")){
										f.set(t, rs.getDouble(f.getName()));
									}else if(str.equals("class java.sql.Date")){
										f.set(t, rs.getDate(f.getName()));
									}else if(str.equals("float")){
										f.set(t, rs.getFloat(f.getName()));
									}else if(str.equals("short")){
										f.set(t, rs.getShort(f.getName()));
									}else{//字符串类型
										f.set(t, rs.getObject(f.getName()));
									}
									
									
									
									
							} catch (Exception e) {
							}
						 }
					 }
					 list.add(t);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				JDBCUtil.closeConnection(rs, sta, con);
			}
			return list;
	}
	
	
}
