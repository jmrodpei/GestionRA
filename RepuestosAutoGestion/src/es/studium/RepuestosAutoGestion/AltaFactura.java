package es.studium.RepuestosAutoGestion;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AltaFactura extends Frame implements WindowListener, ActionListener 
{
	private static final long serialVersionUID = 1L;
	Label lblEmpresa = new Label("Empresa");
	Label lblFecha = new Label("Fecha");
	Choice choEmpresas = new Choice();
	TextField txtFecha = new TextField(20);
	Button btnAceptar = new Button("Aceptar");
	Button btnLimpiar = new Button("Limpiar");
	BaseDatos bd = new BaseDatos();
	Date fecha;
	Connection conexion = null;
	String[] cadena;
	String usuario;
	Utilidades ut = new Utilidades();

	public AltaFactura(String usuario)
	{
		this.usuario = usuario;
		setTitle("Nueva Factura");
		setLayout(new GridLayout(3,2));
		add(lblEmpresa);
		// Rellenar el Choice
		choEmpresas.add("Seleccionar una empresa...");
		// Conectar BD
		conexion = bd.conectar();
		cadena = (bd.consultarEmpresasChoice(conexion)).split("#");
		for(int i = 0; i < cadena.length; i++)
		{
			choEmpresas.add(cadena[i]);
		}
		add(choEmpresas);
		add(lblFecha);
		// Añadir fecha de hoy
		fecha = new Date();
		txtFecha.setText(new SimpleDateFormat("dd/MM/yyyy").format(fecha));
		add(txtFecha);
		btnAceptar.addActionListener(this);
		btnLimpiar.addActionListener(this);
		add(btnAceptar);
		add(btnLimpiar);
		ut.d.addWindowListener(this);
		ut.btnSalir.addActionListener(this);
		addWindowListener(this);
		setSize(350,120);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
		// Desconectar
		bd.desconectar(conexion);
	}

	public void windowActivated(WindowEvent windowEvent){}
	public void windowClosed(WindowEvent windowEvent) {}
	public void windowClosing(WindowEvent windowEvent)
	{
		if(this.isActive())
		{
			ut.registrarLog(usuario, "Salir de Alta Factura");
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
	
	public void actionPerformed(ActionEvent arg0)
	{
		if(btnLimpiar.equals(arg0.getSource()))
		{
			choEmpresas.select(0);
			fecha = new Date();
			txtFecha.setText(new SimpleDateFormat("dd/MM/yyyy").format(fecha));
		}
		else if(btnAceptar.equals(arg0.getSource()))
		{
			if(choEmpresas.getSelectedIndex()!=0) 
			{
				// Conectar BD
				conexion = bd.conectar();
				// Hacer INSERT
				String[] idEmpresaFK = choEmpresas.getSelectedItem().split("-");
				String[] fechaAmericana = txtFecha.getText().split("/");
				String sentencia = "INSERT INTO facturas VALUES(null,'"+fechaAmericana[2]+"-"+fechaAmericana[1]+"-"+fechaAmericana[0]+"',0,0,"+idEmpresaFK[0]+")";
				ut.registrarLog(usuario, sentencia);
				// Feedback
				int idFacturaFK = bd.altaFactura(conexion, sentencia);
				if(idFacturaFK!=0)
				{
					choEmpresas.select(0);
					fecha = new Date();
					txtFecha.setText(new SimpleDateFormat("dd/MM/yyyy").format(fecha));
					// Redirección
					setVisible(false);
					new DetallesFactura(idFacturaFK, usuario);
				}
				else
				{
					// Mensaje de error
					ut.mostrarDialogo("Error", "ERROR:al hacer el Insert en Facturas");
				}
			}
			else 
			{
				ut.mostrarDialogo("Error", "Debe elegir una empresa.");
			}			
		}
		else 
		{
			ut.d.setVisible(false);
		}
	}
}
