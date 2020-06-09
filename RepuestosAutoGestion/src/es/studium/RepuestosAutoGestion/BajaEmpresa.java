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

public class BajaEmpresa extends Frame implements WindowListener, ActionListener
{
	private static final long serialVersionUID = 1L;
	Label lblEmpresaBorrar = new Label("Empresa a borrar:");
	Choice choEmpresa = new Choice();
	Button btnAceptar = new Button("Aceptar");
	Button btnLimpiar = new Button("Limpiar");
	String[] cadena;
	BaseDatos bd = new BaseDatos();
	Connection conexion = null;
	String usuario;
	Utilidades ut = new Utilidades();
	
	
	public BajaEmpresa(String usuario) 
	{
		this.usuario = usuario;
		setTitle("Baja de empresa");
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
		add(lblEmpresaBorrar);
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
		
		setSize(290,150);
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
			ut.registrarLog(usuario, "Salir de Baja Empresa");
			setVisible(false);
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
				ut.dialogoSeguro("¿Está seguro de eliminar Empresa?");
			}
			else 
			{
				ut.mostrarDialogo("Error", "Debe elegir una Empresa.");
			}
		}
		else if(a.equals(ut.btnNo))
		{
			ut.s.setVisible(false);
			choEmpresa.select(0);
		}
		else if(a.equals(ut.btnSi))
		{
			// Conectar BD
			conexion = bd.conectar();
			// Coger el elemento seleccionado
			String[] tabla = choEmpresa.getSelectedItem().split("-");
			// El idEmpresa que quiero borrar está en tabla[0]
			int idEmpresa = Integer.parseInt(tabla[0]);
			// Ejecutar DELETE
			String sentencia = "DELETE FROM empresas WHERE idEmpresa = "+ idEmpresa;
			ut.registrarLog(usuario, sentencia);
			// Mostramos resultado
			if((bd.ejecutarSentencia(conexion, sentencia))==0) 
			{
				ut.mostrarDialogo("Alta Empresa", "BAJA de empresa correcta");
			}
			else 
			{
				ut.mostrarDialogo("Error", "ERROR:al hacer el BAJA en Empresas");
			}
			
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
			
			ut.s.setVisible(false);
		}
		else 
		{
			ut.d.setVisible(false);
		}
	}
}