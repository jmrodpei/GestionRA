package es.studium.RepuestosAutoGestion;

import java.awt.Button;
import java.awt.Choice;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;

public class BajaAlmacen extends Frame implements WindowListener, ActionListener
{
	private static final long serialVersionUID = 1L;
	Label lblAlmacenBorrar = new Label("Almacén a borrar:");
	Choice choAlmacen = new Choice();
	Button btnAceptar = new Button("Aceptar");
	Button btnLimpiar = new Button("Limpiar");
	String[] cadena;
	BaseDatos bd = new BaseDatos();
	Connection conexion = null;
	String usuario;
	Utilidades ut = new Utilidades();
	
	
	public BajaAlmacen(String usuario) 
	{
		this.usuario = usuario;
		setTitle("BAJA de Almacén");
		setLayout(new FlowLayout());
		
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
		add(lblAlmacenBorrar);
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
			ut.registrarLog(usuario, "Salir de Baja Almacén");
			setVisible(false);
		}
		else if(ut.d.isActive())
		{			
			ut.d.setVisible(false);
		}
		else 
		{
			ut.s.setVisible(false);
			choAlmacen.select(0);
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
				ut.dialogoSeguro("¿Está seguro de eliminar Almacén?");
			}
			else 
			{
				ut.mostrarDialogo("Error", "Debe elegir un Almacén.");
			}
		}
		else if(a.equals(ut.btnNo))
		{
			ut.s.setVisible(false);
			choAlmacen.select(0);
		}
		else if(a.equals(ut.btnSi))
		{
			// Conectar BD
			conexion = bd.conectar();
			// Coger el elemento seleccionado
			String[] tabla = choAlmacen.getSelectedItem().split("-");
			// El idCliente que quiero borrar está en tabla[0]
			int idAlmacen = Integer.parseInt(tabla[0]);
			// Ejecutar DELETE
			String sentencia = "DELETE FROM almacenes WHERE idAlmacen = "+idAlmacen;
			
			// Mostramos resultado
			if((bd.ejecutarSentencia(conexion, sentencia))==0) 
			{
				ut.registrarLog(usuario, sentencia);
				ut.mostrarDialogo("Baja Almacén", "BAJA de Almacén correcta");
			}
			else 
			{
				ut.mostrarDialogo("Error", "Error: al hacer la BAJA en Almacén");
			}
			
			// Actualizar el Choice
			choAlmacen.removeAll();
			
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
			
			ut.s.setVisible(false);
		}
		else 
		{
			ut.d.setVisible(false);
		}
	}
}
