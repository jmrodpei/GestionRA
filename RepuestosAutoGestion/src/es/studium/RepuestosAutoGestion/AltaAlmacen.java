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

public class AltaAlmacen extends Frame implements WindowListener, ActionListener
{
	private static final long serialVersionUID = 1L;
	
	Label lblProductoAlmacen = new Label("Producto:");
	Label lblPrecioProductoAlmacen = new Label("Precio:");
	Label lblPasilloAlmacen = new Label("Pasillo:");
	Label lblEstanteriaAlmacen = new Label("Estantería:");
	Label lblBaldaAlmacen = new Label("Balda:");
	
	TextField txtProductoAlmacen = new TextField(20);
	TextField txtPrecioProductoAlmacen = new TextField(20);
	TextField txtPasilloAlmacen = new TextField(20);
	TextField txtEstanteriaAlmacen = new TextField(20);
	TextField txtBaldaAlmacen = new TextField(20);
	
	Button btnAceptar = new Button("Aceptar");	
	Button btnLimpiar = new Button("Limpiar");
	Button btnSalir = new Button("Salir");
	
	BaseDatos bd = new BaseDatos();
	Connection conexion = null;
	String usuario;
	Utilidades ut = new Utilidades();
	
	public AltaAlmacen(String usuario) 
	{
		this.usuario = usuario;
		setTitle("Alta de Almacén");
		setLayout(new GridLayout(7,2));		
		
		add(lblProductoAlmacen);
		add(txtProductoAlmacen);
		add(lblPrecioProductoAlmacen);
		add(txtPrecioProductoAlmacen);
		add(lblPasilloAlmacen);
		add(txtPasilloAlmacen);
		add(lblEstanteriaAlmacen);
		add(txtEstanteriaAlmacen);
		add(lblBaldaAlmacen);
		add(txtBaldaAlmacen);
		add(btnAceptar);		
		add(btnLimpiar);
		add(btnSalir);
		
		btnAceptar.addActionListener(this);
		btnSalir.addActionListener(this);
		btnLimpiar.addActionListener(this);
		ut.d.addWindowListener(this);
		ut.btnSalir.addActionListener(this);
		addWindowListener(this);
		
		setSize(350,250);
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
			ut.registrarLog(usuario, "Salir de Alta Almacén");
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
			txtProductoAlmacen.selectAll();
			txtProductoAlmacen.setText("");
			txtProductoAlmacen.requestFocus();
			txtPrecioProductoAlmacen.selectAll();
			txtPrecioProductoAlmacen.setText("");
			txtPasilloAlmacen.selectAll();
			txtPasilloAlmacen.setText("");
			txtEstanteriaAlmacen.selectAll();
			txtEstanteriaAlmacen.setText("");
			txtBaldaAlmacen.selectAll();
			txtBaldaAlmacen.setText("");		
		}
		else if(a.equals(btnAceptar)) 
		{
			// Conectar BD
			conexion = bd.conectar();
			// Hacer el INSERT
			String sentencia = "INSERT INTO almacenes VALUES (null,'"+txtProductoAlmacen.getText()+"','"+txtPrecioProductoAlmacen.getText()+"','"+txtPasilloAlmacen.getText()+"','"+txtEstanteriaAlmacen.getText()+"','"+txtBaldaAlmacen.getText()+"')";
			
			
			// Mostramos resultado
			if((bd.ejecutarSentencia(conexion, sentencia))==0) 
			{
				ut.registrarLog(usuario, sentencia);
				ut.mostrarDialogo("Alta Almacén", "ALTA de almacén correcta.");
			}
			else 
			{
				ut.mostrarDialogo("Error", "Error: Datos introducidos incorrectos.");
			}
			// Desconectar de la base
			bd.desconectar(conexion);
			
			
			txtProductoAlmacen.selectAll();
			txtProductoAlmacen.setText("");
			txtProductoAlmacen.requestFocus();
			txtPrecioProductoAlmacen.selectAll();
			txtPrecioProductoAlmacen.setText("");
			txtPasilloAlmacen.selectAll();
			txtPasilloAlmacen.setText("");
			txtEstanteriaAlmacen.selectAll();
			txtEstanteriaAlmacen.setText("");
			txtBaldaAlmacen.selectAll();
			txtBaldaAlmacen.setText("");		
			
		}
		
		else if(a.equals(btnSalir)) 
		{
			ut.registrarLog(usuario, "Salir de Alta Almacén");
			setVisible(false);
		}
		else 
		{
			ut.d.setVisible(false);
		}
	}
}