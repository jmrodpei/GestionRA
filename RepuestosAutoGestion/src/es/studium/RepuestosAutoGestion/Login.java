package es.studium.RepuestosAutoGestion;

import java.awt.Button;
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

public class Login extends Frame implements WindowListener, ActionListener
{

	private static final long serialVersionUID = 1L;
		
	Label lblUsuario = new Label("Usuario:");
	Label lblClave = new Label("Clave:   ");
	
	TextField txtUsuario = new TextField(20); 
	TextField txtClave = new TextField(20);
	
	Button btnAceptar = new Button("Aceptar");
	Button btnLimpiar = new Button("Limpiar");	
	
	BaseDatos bd = new BaseDatos();
	Connection conexion = null;
	Utilidades ut = new Utilidades();
	String usuario;
	
	
		
	
	public Login() 
	{
		setLayout(new FlowLayout());
		setSize(270, 150);
		setResizable(false);
		setLocationRelativeTo(null);
		
		add(lblUsuario);
		add(txtUsuario);
		add(lblClave);
		//Para que cuando escriba la clave sangan *
		txtClave.setEchoChar('*');
		add(txtClave);
		add(btnAceptar);
		add(btnLimpiar);
		
		txtUsuario.setText("Bebop");
		txtClave.setText("Studium2020;");
		ut.d.addWindowListener(this);
		addWindowListener(this);
		btnAceptar.addActionListener(this);
		btnLimpiar.addActionListener(this);
		ut.btnSalir.addActionListener(this);

		setVisible(true);
	}
	
	public static void main(String[] args)
	{
		new Login();		
	}
	
	public void windowActivated(WindowEvent windowEvent){}
	public void windowClosed(WindowEvent windowEvent) {}
	public void windowClosing(WindowEvent windowEvent)
	{
		if(this.isActive())
		{
			ut.registrarLog(txtUsuario.getText(), "Sale del programa");
			System.exit(0);
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
		if(evento.getSource().equals(btnLimpiar))
		{
			// Tareas del botón Limpiar
			txtUsuario.selectAll();
			txtUsuario.setText("");
			txtClave.selectAll();
			txtClave.setText("");
			txtUsuario.requestFocus();
		}

		else if(evento.getSource().equals(btnAceptar)) 
		{			
			// Tareas del botón Aceptar					
			String cadenaEncriptada = ut.getSHA256(txtClave.getText());
			String usuarioPermiso = txtUsuario.getText();
			String tipoUsuario = bd.tipoUsuario(txtUsuario.getText());
			
			String sentencia = "SELECT * FROM usuarios WHERE ";
			sentencia += "nombreUsuario = '"+usuarioPermiso+"'";
			sentencia += " AND claveUsuario = '"+cadenaEncriptada+"'";			
			
			// Conectar a la base de datos
			conexion = bd.conectar();
			
			if(conexion!=null) 
			{
				try
				{
					//Crear una sentencia
					Statement stmt = conexion.createStatement();
					//Crear un objeto ResultSet para guardar lo obtenido
					//y ejecutar la sentencia SQL
					ResultSet rs = stmt.executeQuery(sentencia);
					
					if(rs.next())
					{
												
						if(tipoUsuario.equals("0")) 
						{
							usuario = txtUsuario.getText();
							new MenuPrincipal(usuario);
							ut.registrarLog(txtUsuario.getText(), "Entra en el programa");							
							setVisible(false);
						}
						else 
						{
							usuario = txtUsuario.getText();
							new MenuPrincipal(usuario).sinPermisos();
							ut.registrarLog(txtUsuario.getText(), "Entra en el programa");							
							setVisible(false);
						}
						
					}
					else
					{
						ut.mostrarDialogo("Error", "Usuario y/o contraseña incorrectas");
					}
				}

				catch (SQLException sqle)
				{
					System.out.println("Error 2-"+sqle.getMessage());
				}
			}			
			// Desconectar BD
			bd.desconectar(conexion);			
		}
		else 
		{
			// Tareas de Volver
			ut.d.setVisible(false);
		}
	}
}