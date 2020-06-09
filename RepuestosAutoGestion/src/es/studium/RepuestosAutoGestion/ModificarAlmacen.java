package es.studium.RepuestosAutoGestion;

import java.awt.Button;
import java.awt.Choice;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ModificarAlmacen extends Frame implements WindowListener, ActionListener
{
	private static final long serialVersionUID = 1L;
	Label lblAlmacenBorrar = new Label("Almacén a Modificar:");
	Choice choAlmacen = new Choice();
	Button btnAceptar = new Button("Aceptar");
	Button btnLimpiar = new Button("Limpiar");
	
	Frame modificarAlmacenes;
	TextField txtIdAlmacen;
	TextField txtProductoAlmacen;
	TextField txtPrecioProductoAlmacen;
	TextField txtPasilloAlmacen;
	TextField txtEstanteriaAlmacen;
	TextField txtBaldaAlmacen;
	Button btnAceptarCambios;
	Button btnCancelarCambios;
	
	String[] cadena;
	BaseDatos bd = new BaseDatos();
	Connection conexion = null;
	String usuario;
	Utilidades ut = new Utilidades();
	
	public ModificarAlmacen(String usuario) 
	{
		this.usuario = usuario;
		setTitle("Modificar almacenes");
		setLayout(new FlowLayout());
		add(lblAlmacenBorrar);
		// Rellenar el Choice
		choAlmacen.add("Seleccionar un almacén...");
		// Conectar BD
		conexion = bd.conectar();
		cadena = (bd.consultarAlmacenChoice(conexion)).split("#");
		for(int i = 0; i < cadena.length; i++)
		{
			choAlmacen.add(cadena[i]);
		}
		// Desconectar de la base
		bd.desconectar(conexion);	
		
		add(choAlmacen);
		add(btnAceptar);
		add(btnLimpiar);
		btnAceptar.addActionListener(this);
		btnLimpiar.addActionListener(this);
		ut.d.addWindowListener(this);
		ut.s.addWindowListener(this);
		ut.btnSalir.addActionListener(this);
		ut.btnNo.addActionListener(this);
		ut.btnSi.addActionListener(this);
		addWindowListener(this);
		setSize(520,150);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void windowActivated(WindowEvent windowEvent){}
	public void windowClosed(WindowEvent windowEvent) {}
	public void windowClosing(WindowEvent windowEvent)
	{
		if(this.isActive())
		{
			ut.registrarLog(usuario, "Sale de modificar almacén");
			setVisible(false);
		}		
		else if(ut.d.isActive())
		{			
			ut.d.setVisible(false);
		}
		else if(modificarAlmacenes.isActive())
		{	
			modificarAlmacenes.setVisible(false);
			ut.registrarLog(usuario, "Sale de modificar almacén");
			setVisible(true);
		}
		else 
		{
			ut.s.setVisible(false);
		}
	}
	public void windowDeactivated(WindowEvent windowEvent) {}
	public void windowDeiconified(WindowEvent windowEvent) {}
	public void windowIconified(WindowEvent windowEvent) {}
	public void windowOpened(WindowEvent windowEvent) {}
	
	public void actionPerformed(ActionEvent evento) 
	{
		Object a;
		a=evento.getSource();
		if(a.equals(btnLimpiar))
		{
			choAlmacen.select(0);
		}
		else if(a.equals(btnAceptar)) 
		{
			if(choAlmacen.getSelectedIndex()!=0) 
			{				
				modificar();
				setVisible(false);
			}
			else 
			{
				ut.mostrarDialogo("Error", "Debe elegir un almacén.");
			}
		}
		else if(a.equals(btnCancelarCambios)) 
		{
			modificarAlmacenes.setVisible(false);
			choAlmacen.select(0);
			setVisible(true);
		}
		else if(a.equals(btnAceptarCambios)) 
		{
			ut.dialogoSeguro("¿Está seguro de modificar almacén?");			
		}
		else if(a.equals(ut.btnNo))
		{
			ut.s.setVisible(false);
		}
		else if(a.equals(ut.btnSi)) 
		{
			//hacer los cambios
			int id = Integer.parseInt(txtIdAlmacen.getText());
			String nombre = txtProductoAlmacen.getText();
			Float precioVenta = Float.parseFloat(txtPrecioProductoAlmacen.getText());
			int pasillo = Integer.parseInt(txtPasilloAlmacen.getText());
			int estanteria = Integer.parseInt(txtEstanteriaAlmacen.getText());
			int balda = Integer.parseInt(txtBaldaAlmacen.getText());
			
			// Conectar BD
			conexion = bd.conectar();
			// Ejecutar el UPDATE
			String sentencia = "UPDATE almacenes SET productoAlmacen = '"+nombre+"', precioProductoAlmacen = '"+precioVenta+"', pasilloAlmacen = '"+pasillo+"', estanteriaAlmacen = '"+estanteria+"', baldaAlmacen = '"+balda+"' WHERE idAlmacen="+id;
			// Mostramos resultado
			if((bd.ejecutarSentencia(conexion, sentencia))==0) 
			{
				ut.registrarLog(usuario, sentencia);
				ut.mostrarDialogo("Modificar Almacen", "MODIFICACIÓN de almacén correcta");
			}
			else 
			{
				ut.mostrarDialogo("Error", "ERROR:al hacer el MODIFICACIÓN en almacén");
			}			
						
			ut.s.setVisible(false);
			modificarAlmacenes.setVisible(false);	
			setVisible(true);
			
			// Actualizar el Choice
			choAlmacen.removeAll();
			// Rellenar el Choice
			choAlmacen.add("Seleccionar un almacén...");
			// Conectar BD
			conexion = bd.conectar();
			cadena = (bd.consultarEmpresasChoice(conexion)).split("#");
			for(int i = 0; i < cadena.length; i++)
			{
				choAlmacen.add(cadena[i]);
			}
			// Desconectar de la base
			bd.desconectar(conexion);
		}
		else 
		{
			ut.d.setVisible(false);
		}
	}
	
	public void mostrarDatos(Connection con, int idAlmacen, TextField id, TextField nombre, TextField precioVenta, TextField pasillo, TextField estanteria, TextField balda)
	{
		String sql = "SELECT * FROM almacenes WHERE idAlmacen = "+idAlmacen;
		try 
		{
			id.setText(idAlmacen+"");
			// Creamos un STATEMENT para una consulta SQL INSERT.
			Statement sta = con.createStatement();
			ResultSet rs = sta.executeQuery(sql);
			while(rs.next())
			{
				String n = rs.getString("productoAlmacen");
				nombre.setText(n);
				String d = rs.getString("precioProductoAlmacen");
				precioVenta.setText(d);
				String pv = rs.getString("pasilloAlmacen");
				pasillo.setText(pv);
				String pc = rs.getString("estanteriaAlmacen");
				estanteria.setText(pc);
				String pb = rs.getString("baldaAlmacen");
				balda.setText(pb);
			}
			sta.close();
		} 
		catch (SQLException ex) 
		{
			System.out.println("ERROR:al hacer el SELECT");
			ex.printStackTrace();
		}
	}
	
	public Frame modificar() 
	{
		modificarAlmacenes = new Frame("Modificar Almacén");
		modificarAlmacenes.setLayout(new GridLayout(7,2));
		Label lblIdAlmacen = new Label("idAlmacen");
		Label lblProductoAlmacen = new Label("Nombre:");
		Label lblPrecioProductoAlmacen = new Label("Precio:");
		Label lblPasilloAlmacen = new Label("Pasillo:");
		Label lblEstanteriaAlmacen = new Label("Estantería:");
		Label lblBaldaAlmacen = new Label("Balda:");
		
		txtIdAlmacen = new TextField(20);
		txtProductoAlmacen = new TextField(20);
		txtPrecioProductoAlmacen = new TextField(20);
		txtPasilloAlmacen = new TextField(20);
		txtEstanteriaAlmacen = new TextField(20);
		txtBaldaAlmacen = new TextField(20);
		
		btnAceptarCambios = new Button("Aceptar");
		btnCancelarCambios = new Button("Cancelar");
		
		modificarAlmacenes.add(lblIdAlmacen);
		modificarAlmacenes.add(txtIdAlmacen);
		txtIdAlmacen.setEnabled(false);
		modificarAlmacenes.add(lblProductoAlmacen);
		modificarAlmacenes.add(txtProductoAlmacen);
		modificarAlmacenes.add(lblPrecioProductoAlmacen);
		modificarAlmacenes.add(txtPrecioProductoAlmacen);
		modificarAlmacenes.add(lblPasilloAlmacen);
		modificarAlmacenes.add(txtPasilloAlmacen);
		modificarAlmacenes.add(lblEstanteriaAlmacen);
		modificarAlmacenes.add(txtEstanteriaAlmacen);
		modificarAlmacenes.add(lblBaldaAlmacen);
		modificarAlmacenes.add(txtBaldaAlmacen);
		modificarAlmacenes.add(btnAceptarCambios);
		modificarAlmacenes.add(btnCancelarCambios);
		
		modificarAlmacenes.addWindowListener(this);
		btnAceptarCambios.addActionListener(this);
		btnCancelarCambios.addActionListener(this);
		
		// Sacar el id del elemento elegido
		int id = Integer.parseInt(choAlmacen.getSelectedItem().split("-")[0]);		
		// Pero relleno-->
		// Conectar BD
		conexion = bd.conectar();
		// Seleccionar los datos del elemento
		mostrarDatos(conexion, id, txtIdAlmacen, txtProductoAlmacen, txtPrecioProductoAlmacen, txtPasilloAlmacen, txtEstanteriaAlmacen, txtBaldaAlmacen);
		// Desconectar de la base
		bd.desconectar(conexion);
		
		modificarAlmacenes.setSize(350,200);
		modificarAlmacenes.setResizable(false);
		modificarAlmacenes.setLocationRelativeTo(null);
		modificarAlmacenes.setVisible(true);
		
		return modificarAlmacenes;
	}
}