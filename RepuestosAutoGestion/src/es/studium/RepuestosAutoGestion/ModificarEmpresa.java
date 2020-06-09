package es.studium.RepuestosAutoGestion;

import java.awt.Button;
import java.awt.Choice;
import java.awt.FlowLayout;
import java.awt.Frame;
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

public class ModificarEmpresa extends Frame implements WindowListener, ActionListener
{
	private static final long serialVersionUID = 1L;
	Label lblEmpresaBorrar = new Label("Empresa a borrar:");
	Choice choEmpresa = new Choice();
	Button btnAceptar = new Button("Aceptar");
	Button btnLimpiar = new Button("Limpiar");
	
	Frame modificarEmpresa;
	TextField txtIdEmpresa;
	TextField txtNombreEmpresa;
	TextField txtDireccionEmpresa;
	TextField txtTelefonoEmpresa;
	TextField txtEmailEmpresa;
	Button btnAceptarCambios;
	Button btnCancelarCambios;
	
	String[] cadena;
	BaseDatos bd = new BaseDatos();
	Connection conexion = null;
	String usuario;
	Utilidades ut = new Utilidades();
	
	public ModificarEmpresa(String usuario) 
	{
		this.usuario = usuario;
		setTitle("Modificar empresas");
		setLayout(new FlowLayout());
		
		// Rellenar el Choice
		choEmpresa.add("Seleccionar una empresa...");
		// Conectar BD
		conexion = bd.conectar();
		cadena = (bd.consultarEmpresasChoice(conexion)).split("#");
		for(int i = 0; i < cadena.length; i++)
		{
			choEmpresa.add(cadena[i]);
		}
		// Desconectar de la base
		bd.desconectar(conexion);
		
		add(choEmpresa);
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
		setSize(270,150);
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
			ut.registrarLog(usuario, "Sale de modiricar Empresa");
			setVisible(false);
		}		
		
		else if(modificarEmpresa.isActive())
		{	
			modificarEmpresa.setVisible(false);
			ut.registrarLog(usuario, "Sale de modiricar Empresa");
			setVisible(true);
		}
		else if(ut.d.isActive())
		{			
			ut.d.setVisible(false);
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
			choEmpresa.select(0);
		}
		else if(a.equals(btnAceptar)) 
		{
			if(choEmpresa.getSelectedIndex()!=0) 
			{				
				modificar();
				setVisible(false);
			}
			else 
			{
				ut.mostrarDialogo("Error", "Debe elegir una Empresa.");
			}
		}
		else if(a.equals(btnCancelarCambios)) 
		{
			modificarEmpresa.setVisible(false);
			setVisible(true);
		}
		else if(a.equals(btnAceptarCambios)) 
		{
			ut.dialogoSeguro("¿Está seguro de modificar Cliente?");			
		}
		else if(a.equals(ut.btnNo))
		{
			ut.s.setVisible(false);
		}
		else if(a.equals(ut.btnSi))
		{				
			//hacer los cambios
			int id = Integer.parseInt(txtIdEmpresa.getText());
			String nombre = txtNombreEmpresa.getText();
			String direccion = txtDireccionEmpresa.getText();
			int telefono = Integer.parseInt(txtTelefonoEmpresa.getText());
			String email = txtEmailEmpresa.getText();
			
			// Conectar BD
			conexion = bd.conectar();
			// Ejecutar el UPDATE
			String sentencia = "UPDATE empresas SET nombreEmpresa = '"+nombre+"', direccionEmpresa = '"+direccion+"', telefonoEmpresa = '"+telefono+"', emailEmpresa = '"+email+"' WHERE idEmpresa="+id;
			
			// Mostramos resultado
			if((bd.ejecutarSentencia(conexion, sentencia))==0) 
			{
				ut.registrarLog(usuario, sentencia);
				ut.mostrarDialogo("Modificar Empresa", "MODIFICACIÓN de empresa correcta");
			}
			else 
			{
				ut.mostrarDialogo("Error", "ERROR:al hacer el MODIFICACIÓN en Empresa");
			}			
			
			ut.s.setVisible(false);
			modificarEmpresa.setVisible(false);	
			setVisible(true);
			
			// Actualizar el Choice
			choEmpresa.removeAll();
			// Rellenar el Choice
			choEmpresa.add("Seleccionar una empresa...");
			// Conectar BD
			conexion = bd.conectar();
			cadena = (bd.consultarEmpresasChoice(conexion)).split("#");
			for(int i = 0; i < cadena.length; i++)
			{
				choEmpresa.add(cadena[i]);
			}
			// Desconectar de la base
			bd.desconectar(conexion);		
		}
		else 
		{
			ut.d.setVisible(false);
		}
	}
	
	public void mostrarDatos(Connection con, int idEmpresa, TextField id, TextField nombreEmpresa, TextField direccionEmpresa, TextField telefonoEmpresa, TextField emailEmpresa)
	{
		String sql = "SELECT * FROM empresas WHERE idEmpresa = "+idEmpresa;
		try 
		{
			id.setText(idEmpresa+"");
			// Creamos un STATEMENT para una consulta SQL INSERT.
			Statement sta = con.createStatement();
			ResultSet rs = sta.executeQuery(sql);
			while(rs.next())
			{
				String n = rs.getString("nombreEmpresa");
				nombreEmpresa.setText(n);
				String direccion = rs.getString("direccionEmpresa");
				direccionEmpresa.setText(direccion);
				String telefono = rs.getString("telefonoEmpresa");
				telefonoEmpresa.setText(telefono);
				String email = rs.getString("emailEmpresa");
				emailEmpresa.setText(email);
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
		modificarEmpresa = new Frame("Modificar Empresa");
		modificarEmpresa.setLayout(new FlowLayout());
		Label lblIdEmpresa = new Label("idEmpresa");
		Label lblNombreEmpresa = new Label("Nombre:   ");
		Label lblDireccionEmpresa = new Label("Dirección:");
		Label lblTelefonoEmpresa = new Label("Teléfono:");
		Label lblEmailEmpresa = new Label("Email:    ");
		
		txtIdEmpresa = new TextField(20);
		txtNombreEmpresa = new TextField(20);
		txtDireccionEmpresa = new TextField(20);
		txtTelefonoEmpresa = new TextField(20);
		txtEmailEmpresa = new TextField(20);
		
		btnAceptarCambios = new Button("Aceptar");
		btnCancelarCambios = new Button("Cancelar");
		
		modificarEmpresa.add(lblIdEmpresa);
		modificarEmpresa.add(txtIdEmpresa);
		txtIdEmpresa.setEnabled(false);
		modificarEmpresa.add(lblNombreEmpresa);
		modificarEmpresa.add(txtNombreEmpresa);
		modificarEmpresa.add(lblDireccionEmpresa);
		modificarEmpresa.add(txtDireccionEmpresa);
		modificarEmpresa.add(lblTelefonoEmpresa);
		modificarEmpresa.add(txtTelefonoEmpresa);
		modificarEmpresa.add(lblEmailEmpresa);
		modificarEmpresa.add(txtEmailEmpresa);
		
		modificarEmpresa.add(btnAceptarCambios);
		modificarEmpresa.add(btnCancelarCambios);
		
		modificarEmpresa.addWindowListener(this);
		btnAceptarCambios.addActionListener(this);
		btnCancelarCambios.addActionListener(this);
		
		// Sacar el id del elemento elegido
		int id = Integer.parseInt(choEmpresa.getSelectedItem().split("-")[0]);		
		// Pero relleno-->
		// Conectar BD
		conexion = bd.conectar();
		// Seleccionar los datos del elemento
		mostrarDatos(conexion, id, txtIdEmpresa, txtNombreEmpresa, txtDireccionEmpresa, txtTelefonoEmpresa, txtEmailEmpresa);
		// Desconectar de la base
		bd.desconectar(conexion);
		
		modificarEmpresa.setSize(280,350);
		modificarEmpresa.setResizable(false);
		modificarEmpresa.setLocationRelativeTo(null);
		modificarEmpresa.setVisible(true);
		
		return modificarEmpresa;
	}
}