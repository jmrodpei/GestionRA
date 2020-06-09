package es.studium.RepuestosAutoGestion;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;


public class MenuPrincipal extends Frame implements WindowListener, ActionListener
{
	private static final long serialVersionUID = 1L;
	
	MenuBar barraMenu = new MenuBar();
	
	Menu mnuEmpresas = new Menu("Empresas");
	Menu mnuAlmacenes = new Menu("Almacenes");
	Menu mnuFacturas = new Menu("Facturas");
	Menu mnuAyuda = new Menu("Ayuda");
	
	MenuItem mniAltaEmpresa = new MenuItem("Alta");	
	MenuItem mniBajaEmpresa = new MenuItem("Baja");
	MenuItem mniModificaEmpresa = new MenuItem("Modificación");
	MenuItem mniConsultaEmpresa = new MenuItem("Consulta");
	
	MenuItem mniAltaAlmacenes = new MenuItem("Alta");
	MenuItem mniBajaAlmacenes = new MenuItem("Baja");
	MenuItem mniModificaAlmacenes = new MenuItem("Modificación");
	MenuItem mniConsultaAlmacenes = new MenuItem("Consulta");
	
	MenuItem mniAltaFactura = new MenuItem("Alta");
	MenuItem mniConsultaFactura = new MenuItem("Consulta");
	
	MenuItem mniAyuda = new MenuItem("Ayuda");
	
	String usuario;
	Utilidades ut = new Utilidades();
	BaseDatos bd = new BaseDatos();
	
	
	MenuPrincipal(String usuario)
	{
		this.usuario = usuario;
		
		setTitle("Repuestos Automóvil");
		setLayout(new FlowLayout());
		mnuEmpresas.add(mniAltaEmpresa);		
		mnuEmpresas.add(mniBajaEmpresa);
		mnuEmpresas.add(mniModificaEmpresa);
		mnuEmpresas.add(mniConsultaEmpresa);		
		
		mnuAlmacenes.add(mniAltaAlmacenes);
		mnuAlmacenes.add(mniBajaAlmacenes);
		mnuAlmacenes.add(mniModificaAlmacenes);
		mnuAlmacenes.add(mniConsultaAlmacenes);
		
		mnuFacturas.add(mniAltaFactura);
		mnuFacturas.add(mniConsultaFactura);
		
		mnuAyuda.add(mniAyuda);
								
		barraMenu.add(mnuEmpresas);
		barraMenu.add(mnuAlmacenes);
		barraMenu.add(mnuFacturas);
		barraMenu.add(mnuAyuda);
				
		addWindowListener(this);
		
		mniAltaEmpresa.addActionListener(this);
		mniBajaEmpresa.addActionListener(this);
		mniModificaEmpresa.addActionListener(this);
		mniConsultaEmpresa.addActionListener(this);
		
		mniAltaAlmacenes.addActionListener(this);
		mniBajaAlmacenes.addActionListener(this);
		mniModificaAlmacenes.addActionListener(this);
		mniConsultaAlmacenes.addActionListener(this);
		
		mniAltaFactura.addActionListener(this);		
		mniConsultaFactura.addActionListener(this);
		
		mniAyuda.addActionListener(this);
		
		setMenuBar(barraMenu);
		setSize(400,250);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void windowActivated(WindowEvent windowEvent){}
	public void windowClosed(WindowEvent windowEvent) {}
	public void windowClosing(WindowEvent windowEvent)
	{
		ut.registrarLog(usuario, "Cierra Menú Principal");
		setVisible(false);
		new Login();
		
	}
	public void windowDeactivated(WindowEvent windowEvent) {}
	public void windowDeiconified(WindowEvent windowEvent) {}
	public void windowIconified(WindowEvent windowEvent) {}
	public void windowOpened(WindowEvent windowEvent) {}
	
	public void actionPerformed(ActionEvent evento) 
	{
		Object a;
		a=evento.getSource();
		
		
		if(a.equals(mniAltaEmpresa))
		{
			ut.registrarLog(usuario, "Entra Alta Empresa");
			new AltaEmpresa(usuario);			
		}
		else if(a.equals(mniBajaEmpresa)) 
		{
			ut.registrarLog(usuario, "Entra Baja Empresa");
			new BajaEmpresa(usuario);
		}
		else if(a.equals(mniModificaEmpresa)) 
		{
			ut.registrarLog(usuario, "Entra Modificar Empresa");
			new ModificarEmpresa(usuario);
		}
		else if(a.equals(mniConsultaEmpresa)) 
		{
			ut.registrarLog(usuario, "Entra Consulta Empresa");
			new ConsultaEmpresa(usuario);
		}
		else if(a.equals(mniAltaAlmacenes))
		{
			ut.registrarLog(usuario, "Entra Alta Almacenes");
			new AltaAlmacen(usuario);
		}
		else if(a.equals(mniBajaAlmacenes))
		{
			ut.registrarLog(usuario, "Entra Baja Almacenes");
			new BajaAlmacen(usuario);
		}
		else if(a.equals(mniModificaAlmacenes))
		{
			ut.registrarLog(usuario, "Entra Modificar Almacenes");
			new ModificarAlmacen(usuario);
		}
		else if(a.equals(mniConsultaAlmacenes))
		{
			ut.registrarLog(usuario, "Entra Consulta Almacenes");
			new ConsultaAlmacen(usuario);
		}
		else if(a.equals(mniAltaFactura)) 
		{			
			ut.registrarLog(usuario, "Entra Alta Factura");
			new AltaFactura(usuario);
		}		
		else if(a.equals(mniConsultaFactura)) 
		{
			ut.registrarLog(usuario, "Entra Consulta Factura");
			new ConsultaFactura(usuario);
			
		}
		else 
		{
			ut.registrarLog(usuario, "Consultar Ayuda");
			try
			{
				Runtime.getRuntime().exec("hh.exe AyudaRA.chm");//aqui meto el archivo de ayuda
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public void sinPermisos() 
	{			
		mniBajaEmpresa.setEnabled(false);
		mniModificaEmpresa.setEnabled(false);
		mniConsultaEmpresa.setEnabled(false);
		mniBajaAlmacenes.setEnabled(false);
		mniModificaAlmacenes.setEnabled(false);
		mniConsultaAlmacenes.setEnabled(false);
		mniConsultaFactura.setEnabled(false);
	}
}