
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImpalaJdbcUtil {
	private static String driverName = "com.cloudera.impala.jdbc41.Driver";
    private static String url = "jdbc:impala://127.0.0.1:21050/hadoop_db";
    private static String user = "root";
    private static String password = "";
 
    private static Connection conn = null;
    private static Statement stmt = null;
    private static ResultSet rs = null;
    static {
        try {
        	Class.forName(driverName);
        	conn = DriverManager.getConnection(url);
        	System.out.println("============"+conn);
			//stmt = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    // 加载驱动、创建连接
    public static void init() throws Exception {
        //Class.forName(driverName);
       // conn = DriverManager.getConnection(url, user, password);
        stmt = conn.createStatement();
    }
    //执行语句，返回成功或不成功
    public static boolean execute(String sql)throws Exception {
    	try {
    		init();
			return stmt.execute(sql);
		} catch (Exception e) {
			throw e;
		}finally {
			destory();
		}
    }
    //执行查询返回结果
    public static Map<String,Object> executeQuery(String sql) throws Exception {
    	try {
    		init();
    		Map<String,Object> reMap = new HashMap<String,Object>();
    		rs = stmt.executeQuery(sql);
    		ResultSetMetaData rsmd = rs.getMetaData();
    		int cls = 0;
    		if(rsmd != null) {
    			cls = rsmd.getColumnCount();
    		}
    		List<String> cnameList = new ArrayList<String>();
    		for(int i=0;i<cls;i++) {
    			String cname = rsmd.getColumnName(i+1);
    			cnameList.add(cname);
    		}
    		List<List<Object>> reList = new ArrayList<List<Object>>();
            while (rs.next()) {
            	//Map<String,Object> valMap = new HashMap<String, Object>();
            	List<Object> tlist = new ArrayList<Object>();
            	for(int i=0;i<cls;i++) {
            		Object obj = rs.getObject(i+1);
            		//valMap.put(cname, obj);
            		tlist.add(obj);
            	}
            	reList.add(tlist);
            }
            reMap.put("list",reList);
            reMap.put("cnameList", cnameList);
            return reMap;
		} catch (Exception e) {
			throw e;
		}finally {
			destory();
		}
    }
    
 
    // 释放资源
    public static void destory() throws Exception {
        if (rs != null) {
            rs.close();
        }
        if (stmt != null) {
            stmt.close();
        }
		/*
		 * if (conn != null) { //conn.close(); }
		 */
    }
}
