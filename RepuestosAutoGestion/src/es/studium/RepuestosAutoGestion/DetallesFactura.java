package es.studium.RepuestosAutoGestion;

import java.awt.Button;
import java.awt.Choice;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;

public class DetallesFactura extends Frame implements WindowListener, ActionListener
{
	private static final long serialVersionUID = 1L;
	
	Panel panel1 = new Panel();
	Panel panel2 = new Panel();
	Panel panel3 = new Panel();
	Panel panel4 = new Panel();
	Panel panel5 = new Panel();
	Panel panel6 = new Panel();
	
	Label lblFactura = new Label("Factura Nº:");	
	
	Label lblArticulo = new Label("Artículo:");
	Choice choAlmacen = new Choice();
	Label lblCantidad = new Label("Cantidad:");
	TextField txtCantidad = new TextField(5);
	Button btnAgregar = new Button("Agregar");
	
	TextArea txaListado = new TextArea(10,50);
	
	Label lblSubtotal = new Label("Subtotal:");
	TextField txtSubtotal = new TextField(8);
	Label lblIva = new Label("Iva 21%:");
	TextField txtIva = new TextField(8);
	Label lblTotal = new Label("Total:");
	TextField txtTotal = new TextField(8);
	
	Button btnFinalizar = new Button("Finalizar");
	Button btnAnular = new Button("Anular");	
	
	int idFacturaFK;
	String[] cadena;
	BaseDatos bd = new BaseDatos();
	Connection conexion = null;
	String usuario;
	Utilidades ut = new Utilidades();
	Double subtotal = 0.0;
	Double iva = 0.0;
	Double total = 0.0;
	
	
	public DetallesFactura(int idFacturaFK, String usuario) 
	{
		this.idFacturaFK = idFacturaFK;
		this.usuario = usuario;
		setTitle("Detalles de Factura");
		setLayout(new FlowLayout());
		panel1.setLayout(new FlowLayout());
		lblFactura.setText("Factura Nº:"+idFacturaFK);
		panel1.add(lblFactura);
		add(panel1);
		panel2.setLayout(new FlowLayout());
		panel2.add(lblArticulo);
		// Rellenar el Choice
		choAlmacen.add("Seleccionar un servicio...");
		// Conectar BD
		conexion = bd.conectar();
		cadena = (bd.consultarAlmacenChoice(conexion)).split("#");
		for(int i = 0; i < cadena.length; i++)
		{
			choAlmacen.add(cadena[i]);
		}
		panel2.add(choAlmacen);
		// Desconectar
		bd.desconectar(conexion);
		add(panel2);
		panel3.setLayout(new FlowLayout());		
		panel3.add(lblCantidad);
		panel3.add(txtCantidad);		
		panel3.add(btnAgregar);
		add(panel3);
		panel4.setLayout(new FlowLayout());
		panel4.setSize(500, 300);
		panel4.add(txaListado);
		add(panel4);
		panel5.setLayout(new FlowLayout());
		panel5.add(lblSubtotal);
		panel5.add(txtSubtotal);
		panel5.add(lblIva);
		panel5.add(txtIva);
		panel5.add(lblTotal);
		panel5.add(txtTotal);
		add(panel5);
		panel6.setLayout(new FlowLayout());
		panel6.add(btnFinalizar);
		panel6.add(btnAnular);
		add(panel6);
		
		btnAgregar.addActionListener(this);
		btnFinalizar.addActionListener(this);
		btnAnular.addActionListener(this);
		ut.d.addWindowListener(this);
		ut.s.addWindowListener(this);
		ut.btnSalir.addActionListener(this);
		ut.btnNo.addActionListener(this);
		ut.btnSi.addActionListener(this);
		addWindowListener(this);
		setSize(580,420);
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
			if(txaListado.getText().equals("")) 
			{
				setVisible(false);
				// Conectar BD
				conexion = bd.conectar();
				String sentencia = "DELETE FROM facturas WHERE idFactura = "+idFacturaFK;
				ut.registrarLog(usuario, sentencia);
				if(bd.ejecutarSentencia(conexion, sentencia)==0)
				{
					ut.mostrarDialogo("Factura", "Factura anulada");
					
				}
				else
				{
					ut.mostrarDialogo("Error", "ERROR al anular Factura.");
				}
				bd.desconectar(conexion);
				new AltaFactura(usuario);
			}
			else
			{
				ut.dialogoSeguro("¿Está seguro de eliminar Factura?");
			}
			
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
		if(a.equals(btnAgregar)) 
		{			
			if(choAlmacen.getSelectedItem().equals("Seleccionar un producto...")||(txtCantidad.getText().equals("")))
			{
				ut.mostrarDialogo("Error", "Debe elegir un producto y cantidad.");
			}
			else 
			{
				// Coger el servicio seleccionado

				String[] seleccionado = choAlmacen.getSelectedItem().split("-");
				// seleccionado[0] = idAlmacenFK
				// seleccionado[1] = nombreProducto
				// seleccionado[2] = precioProductoAlmacen
				// Coger la cantidad escrita
				int cantidad = Integer.parseInt(txtCantidad.getText());
				// Insertamos en el TextArea
				txaListado.setText(txaListado.getText()+"\n"+seleccionado[1]+" "+seleccionado[2]+" "+cantidad+" "+cantidad*Double.parseDouble(seleccionado[2]));
				// Calculamos el Subtotal de linea (cantidad*precio)
				// Calcular Subtotal, Iva y Total
				subtotal = subtotal + cantidad*Double.parseDouble(seleccionado[2]);
				txtSubtotal.setText(subtotal+"");
				iva = subtotal * 0.21;
				txtIva.setText(iva+"");
				total = subtotal + iva;
				txtTotal.setText(total+"");
				String sentencia = "INSERT INTO poseen VALUES("+seleccionado[0]+","+idFacturaFK+","+cantidad+")";
				ut.registrarLog(usuario, sentencia);
				// Conectar BD
				conexion = bd.conectar();
				if(bd.ejecutarSentencia(conexion, sentencia)==0)
				{}
				else
				{
					// Error
					// Mostrar diálogo
					ut.mostrarDialogo("Error", "ERROR al insertar linea.");
				}
				bd.desconectar(conexion);
				// Resetear
				choAlmacen.select(0);
				txtCantidad.selectAll();
				txtCantidad.setText("");
			}
		}
		else if(a.equals(btnFinalizar)) 
		{
			// UPDATE de Subtotal y iva
			String sentencia = "UPDATE facturas SET totalFactura = "+total+" WHERE idFactura = "+idFacturaFK;
			ut.registrarLog(usuario, sentencia);
			// Conectar BD
			conexion = bd.conectar();
			if(bd.ejecutarSentencia(conexion, sentencia)==0)
			{
				ut.mostrarDialogo("Nueva Factura", "Factura realizada correctamente.");
			}
			else
			{
				ut.mostrarDialogo("Error", "ERROR al realizar Factura.");
			}
			// Desconectar de la base
			bd.desconectar(conexion);
			setVisible(false);
			new AltaFactura(usuario);
		}
		else if(a.equals(btnAnular)) 
		{
			ut.dialogoSeguro("¿Está seguro de eliminar Factura?");
		}
		else if(a.equals(ut.btnNo)) 
		{
			ut.s.setVisible(false);
		}
		else if(a.equals(ut.btnSi)) 
		{
			// Conectar BD
			conexion = bd.conectar();
			String sql = "DELETE FROM poseen WHERE idFacturaFK = "+idFacturaFK;
			ut.registrarLog(usuario, sql);
			if(bd.ejecutarSentencia(conexion, sql)==0)
			{}				
			else
			{
				ut.mostrarDialogo("Error", "ERROR al realizar Factura.");
			}			
			String sentencia = "DELETE FROM facturas WHERE idFactura = "+idFacturaFK;
			ut.registrarLog(usuario, sentencia);
			if(bd.ejecutarSentencia(conexion, sentencia)==0)
			{
				ut.mostrarDialogo("Factura", "Factura ANULADA");
			}
			else
			{
				ut.mostrarDialogo("Error", "ERROR al anular Factura.");
			}					
			// Desconectar de la base
			bd.desconectar(conexion);
			ut.s.setVisible(false);
			setVisible(false);			
			new AltaFactura(usuario);	
		}
		else 
		{
			ut.d.setVisible(false);
		}
	}	
}
