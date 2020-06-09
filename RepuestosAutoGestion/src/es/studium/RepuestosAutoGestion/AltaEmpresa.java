package es.studium.RepuestosAutoGestion;

import java.awt.Button;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;

public class AltaEmpresa extends Frame implements WindowListener, ActionListener
{

	private static final long serialVersionUID = 1L;
	
	Label lblNombreEmpresa = new Label("Nombre:   ");
	Label lblDireccionEmpresa = new Label("Dirección:   ");
	Label lblTelefonoEmpresa = new Label("Teléfono:");
	Label lblEmailEmpresa = new Label("Email:");
	
	
	TextField txtNombreEmpresa = new TextField(20);
	TextField txtDireccionEmpresa = new TextField(20);
	TextField txtTelefonoEmpresa = new TextField(20);
	TextField txtEmailEmpresa = new TextField(20);
	
	Button btnAceptar = new Button("Aceptar");	
	Button btnLimpiar = new Button("Limpiar");
	Button btnSalir = new Button("Salir");	
	
	BaseDatos bd = new BaseDatos();
	Connection conexion = null;
	String usuario;
	Utilidades ut = new Utilidades();
	
	
	
	public AltaEmpresa(String usuario) 
	{
		this.usuario = usuario;
		
		setTitle("ALTA de Empresa");
		setLayout(new GridLayout(6,2));		
		
		add(lblNombreEmpresa);
		add(txtNombreEmpresa);
		add(lblDireccionEmpresa);
		add(txtDireccionEmpresa);
		add(lblTelefonoEmpresa);
		add(txtTelefonoEmpresa);
		add(lblEmailEmpresa);
		add(txtEmailEmpresa);
		add(btnAceptar);		
		add(btnLimpiar);
		add(btnSalir);
		
		btnAceptar.addActionListener(this);		
		btnLimpiar.addActionListener(this);
		btnSalir.addActionListener(this);
		
		ut.d.addWindowListener(this);
		ut.btnSalir.addActionListener(this);
		addWindowListener(this);
		
		setSize(350,330);
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
			ut.registrarLog(usuario, "Salir de Alta Empresa");
			setVisible(false);
		}
		else
		{			
			ut.d.setVisible(false);
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
			txtNombreEmpresa.selectAll();
			txtNombreEmpresa.setText("");
			txtNombreEmpresa.requestFocus();
			txtDireccionEmpresa.selectAll();
			txtDireccionEmpresa.setText("");
			txtTelefonoEmpresa.selectAll();
			txtTelefonoEmpresa.setText("");
			txtEmailEmpresa.selectAll();
			txtEmailEmpresa.setText("");
			
		}
		else if(a.equals(btnAceptar)) 
		{
			// Conectar BD
			conexion = bd.conectar();
			// Hacer el INSERT
			String sentencia = "INSERT INTO empresas VALUES (null,'"+txtNombreEmpresa.getText()+"','"+txtDireccionEmpresa.getText()+"','"+txtTelefonoEmpresa.getText()+"','"+txtEmailEmpresa.getText()+"')";
			
			
			// Mostramos resultado
			if((bd.ejecutarSentencia(conexion, sentencia))==0) 
			{
				ut.registrarLog(usuario, sentencia);
				ut.mostrarDialogo("Alta Empresa", "ALTA de empresa correcta");
			}
			else 
			{
				ut.mostrarDialogo("Error", "ERROR:al hacer el Insert en Empresas");
			}
			// Desconectar de la base
			bd.desconectar(conexion);
			
			
			txtNombreEmpresa.selectAll();
			txtNombreEmpresa.setText("");
			txtNombreEmpresa.requestFocus();
			txtDireccionEmpresa.selectAll();
			txtDireccionEmpresa.setText("");
			txtTelefonoEmpresa.selectAll();
			txtTelefonoEmpresa.setText("");
			txtEmailEmpresa.selectAll();
			txtEmailEmpresa.setText("");
			
		}
		
		else if(a.equals(btnSalir)) 
		{
			ut.registrarLog(usuario, "Salir de Alta Empresa");
			setVisible(false);
		}
		else 
		{
			ut.d.setVisible(false);
		}
	}
}