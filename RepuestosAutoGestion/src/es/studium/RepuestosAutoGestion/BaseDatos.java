package es.studium.RepuestosAutoGestion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class BaseDatos 
{
	String driver = "com.mysql.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/repuestosautomovil?autoReconnect=true&useSSL=false";
	String login = "root";
	String password = "studium";
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;	
	
	Connection conexion = null;
	String usuario = " ";

	public Connection conectar()
	{
		try
		{
			//Cargar los controladores para el acceso a la BD
			Class.forName(driver);
			//Establecer la conexión con la BD Empresa
			connection = DriverManager.getConnection(url, login, password);
		}
		catch (ClassNotFoundException cnfe)
		{
			System.out.println("Error 1-"+cnfe.getMessage());
		}
		catch (SQLException sqle)
		{
			System.out.println("Error 2-"+sqle.getMessage());
		}
		return connection;
	}
	
	public String tipoUsuario(String texto) 
	{
		String tipo = null;
		String sql = "SELECT * FROM usuarios WHERE nombreUsuario = " +"'"+texto+"'";
		// Conectar a la base de datos
		conexion = conectar();		
		try 
		{			
			// Creamos un STATEMENT para una consulta SQL INSERT.
			Statement sta = conexion.createStatement();
			ResultSet rs = sta.executeQuery(sql);
			while(rs.next()) 
			{
				tipo = rs.getString("tipoUsuario");
			}			
		}
		catch (SQLException ex) 
		{
			System.out.println("ERROR:al hacer el SELECT");
			ex.printStackTrace();
		}
		return tipo;
	}
	
	public int ejecutarSentencia(Connection c, String sentencia)
	{
		int resultado = 1;
		try
		{
			//Crear una sentencia
			statement = c.createStatement();
			//Crear un objeto ResultSet para guardar lo obtenido
			//y ejecutar la sentencia SQL
			if((statement.executeUpdate(sentencia))==1)
			{
				resultado = 0;
			}
			else
			{
				resultado = 1;
			}
		}
		catch (SQLException sqle)
		{
			System.out.println("Error 2-"+sqle.getMessage());
		}
		return (resultado);
	}
	
	public String consultarEmpresasChoice(Connection c)
	{
		String resultado = "";
		
		try
		{
			String sentencia = "SELECT * FROM empresas";
			//Crear una sentencia
			statement = c.createStatement();
			//Crear un objeto ResultSet para guardar lo obtenido
			//y ejecutar la sentencia SQL
			rs = statement.executeQuery(sentencia);
			while (rs.next())
			{
				resultado = resultado + rs.getInt("idEmpresa") + "-" +
						rs.getString("nombreEmpresa") + "-" +
						", "+rs.getString("telefonoEmpresa")+"#";
			}
		}
		catch (SQLException sqle)
		{
			System.out.println("Error 2-"+sqle.getMessage());
		}
		return (resultado);
	}
	
	public String consultarEmpresas(Connection c)
	{
		String resultado = "";		
		try
		{
			String sentencia = "SELECT * FROM empresas";
			//Crear una sentencia
			statement = c.createStatement();
			//Crear un objeto ResultSet para guardar lo obtenido
			//y ejecutar la sentencia SQL
			rs = statement.executeQuery(sentencia);
			while (rs.next())
			{				
				resultado = resultado + rs.getInt("idEmpresa") + "-" +
						rs.getString("nombreEmpresa") +
						" - "+rs.getString("direccionEmpresa")+
						" - "+rs.getString("telefonoEmpresa")+
						" - "+rs.getString("emailEmpresa")+"\n";
			}
		}
		catch (SQLException sqle)
		{
			System.out.println("Error 2-"+sqle.getMessage());
		}
		return (resultado);
	}
	
	public String consultarAlmacenChoice(Connection c)
	{
		String resultado = "";		
		try
		{
			String sentencia = "SELECT * FROM almacenes";
			//Crear una sentencia
			statement = c.createStatement();
			//Crear un objeto ResultSet para guardar lo obtenido
			//y ejecutar la sentencia SQL
			rs = statement.executeQuery(sentencia);
			while (rs.next())
			{
				
				resultado = resultado + rs.getInt("idAlmacen") + "-" +
						rs.getString("productoAlmacen") +
						" - "+rs.getString("precioProductoAlmacen")+
						" - "+rs.getString("pasilloAlmacen")+
						" - "+rs.getString("estanteriaAlmacen")+
						" - "+rs.getString("baldaAlmacen")+"#";
			}
		}
		catch (SQLException sqle)
		{
			System.out.println("Error 2-"+sqle.getMessage());
		}
		return (resultado);
	}
	
	public String consultarAlmacenes(Connection c)
	{
		String resultado = "";		
		try
		{
			String sentencia = "SELECT * FROM almacenes";
			//Crear una sentencia
			statement = c.createStatement();
			//Crear un objeto ResultSet para guardar lo obtenido
			//y ejecutar la sentencia SQL
			rs = statement.executeQuery(sentencia);
			while (rs.next())
			{				
				resultado = resultado + rs.getInt("idAlmacen") + "-" +
						rs.getString("productoAlmacen") +
						" - "+rs.getString("precioProductoAlmacen")+
						" - "+rs.getString("pasilloAlmacen")+
						" - "+rs.getString("estanteriaAlmacen")+
						" - "+rs.getString("baldaAlmacen")+"\n";
			}
		}
		catch (SQLException sqle)
		{
			System.out.println("Error 2-"+sqle.getMessage());
		}
		return (resultado);
	}
	
	public int altaFactura(Connection c, String sentencia)
	{
		int resultado = 0; // INSERT incorrecto
		try
		{
			//Crear una sentencia
			statement = c.createStatement();
			//Crear un objeto ResultSet para guardar lo obtenido
			//y ejecutar la sentencia SQL
			if((statement.executeUpdate(sentencia))==1)
			{
				String sentenciaConsulta = "SELECT idFactura FROM facturas ORDER BY 1 DESC LIMIT 1";
				ResultSet rs = statement.executeQuery(sentenciaConsulta);
				if(rs.next())
				{
					resultado = rs.getInt("idFactura");
				}
			}
			else
			{
				resultado = 0;
			}
		}
		catch (SQLException sqle)
		{
			System.out.println("Error 2-"+sqle.getMessage());
		}
		return (resultado);
	}
	
	public String consultarFacturas(Connection c)
	{
		String resultado = "";
		String[] fechaEuropea;
		try
		{
			String sentencia = "SELECT idFactura, fechaFactura, nombreEmpresa FROM facturas, empresas WHERE idEmpresa = idEmpresaFK ORDER BY idFactura";
			//Crear una sentencia
			statement = c.createStatement();
			//Crear un objeto ResultSet para guardar lo obtenido
			//y ejecutar la sentencia SQL
			rs = statement.executeQuery(sentencia);
			while (rs.next())
			{
				fechaEuropea = (rs.getString("fechaFactura")).split("-");
				resultado = resultado + rs.getInt("idFactura") + "-"
						+ fechaEuropea[2]+"/"+fechaEuropea[1]+"/"+fechaEuropea[0]+ "-"+ rs.getString("nombreEmpresa")+"\n";
			}
		}
		catch (SQLException sqle)
		{
			System.out.println("Error 2-"+sqle.getMessage());
		}
		return (resultado);
	}
	
	public String consultarFacturasChoice(Connection c)
	{
		String resultado = "";
		String[] fechaEuropea;
		try
		{
			String sentencia = "SELECT idFactura, fechaFactura, nombreEmpresa FROM facturas, empresas WHERE idEmpresa = idEmpresaFK ORDER BY idFactura";
			//Crear una sentencia
			statement = c.createStatement();
			//Crear un objeto ResultSet para guardar lo obtenido
			//y ejecutar la sentencia SQL
			rs = statement.executeQuery(sentencia);
			while (rs.next())
			{
				fechaEuropea = (rs.getString("fechaFactura")).split("-");
				resultado = resultado + rs.getInt("idFactura") + "-"
						+ fechaEuropea[2]+"/"+fechaEuropea[1]+"/"+fechaEuropea[0]+ "-"+ rs.getString("nombreEmpresa")+"#";
			}
		}
		catch (SQLException sqle)
		{
			System.out.println("Error 2-"+sqle.getMessage());
		}
		return (resultado);
	}
	
	public String consultarFactura(Connection c, int idFactura)
	{
		String resultado = "";
		String[] fechaEuropea;
		try
		{
			String sentencia = "SELECT idFactura, fechaFactura, ivaFactura, totalFactura, totalFactura+ivaFactura AS total, nombreEmpresa FROM facturas, empresas WHERE idFactura = "+idFactura+" AND idEmpresa = idEmpresaFK ORDER BY idFactura";
			//Crear una sentencia
			statement = c.createStatement();
			//Crear un objeto ResultSet para guardar lo obtenido
			//y ejecutar la sentencia SQL
			rs = statement.executeQuery(sentencia);
			rs.next();
			fechaEuropea = (rs.getString("fechaFactura")).split("-");
			resultado = resultado + fechaEuropea[2] + "/" + fechaEuropea[1] + "/" + fechaEuropea[0] + "-"+ rs.getString("nombreEmpresa") + "-" + rs.getDouble("totalFactura")+ "-" + rs.getDouble("ivaFactura")+ "-" + rs.getDouble("total");
		}
		catch (SQLException sqle)
		{
			System.out.println("Error 2-"+sqle.getMessage());
		}
		return (resultado);
	}
	public String consultarDetallesFactura(Connection c, int idFactura)
	{
		String resultado = "";
		try
		{
			String sentencia = "SELECT cantidadPoseen, productoAlmacen, precioProductoAlmacen, precioProductoAlmacen*cantidadPoseen AS total FROM poseen, almacenes WHERE idFacturaFK = "+idFactura+" AND idAlmacenFK = idAlmacen";
			//Crear una sentencia
			statement = c.createStatement();
			//Crear un objeto ResultSet para guardar lo obtenido
			//y ejecutar la sentencia SQL
			rs = statement.executeQuery(sentencia);
			while(rs.next())
			{
				resultado = resultado + rs.getInt("cantidadPoseen") + "-" + rs.getString("productoAlmacen") + "-" + rs.getDouble("precioProductoAlmacen") + "-" + rs.getDouble("total")+"\n";
			}
		}
		catch (SQLException sqle)
		{
			System.out.println("Error 2-"+sqle.getMessage());
		}
		return (resultado);
	}
	
	public void desconectar(Connection con)
	{
		try
		{
			con.close();				
		}
		catch(Exception e) {}
	}
}
