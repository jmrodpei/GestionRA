package es.studium.RepuestosAutoGestion;

import java.awt.Button;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utilidades extends Frame
{
	private static final long serialVersionUID = 1L;
	// Creamos la ventana de diálogo Información
	Dialog d = new Dialog(this, "", true);	
	Label lblDialogo = new Label();
	Button btnSalir = new Button("Salir");
	
	//Creamos la ventana de diálogo Seguro
	Dialog s = new Dialog(this, "¿Seguro?", true);	
	Label lblDialogoSeguro = new Label();
	Button btnSi = new Button("Si");
	Button btnNo = new Button("No");
	
	public void mostrarDialogo(String error, String mensajeLbl)
	{
		d.setLayout(new FlowLayout());
		d.setTitle(error);
		lblDialogo.setText(mensajeLbl);            
		d.add(lblDialogo);
		d.add(btnSalir);
		d.setSize(250,150);
		d.setLocationRelativeTo(null);
		d.setVisible(true);
	}
	
	public void dialogoSeguro(String mensajeLbl) 
	{
		s.setLayout(new FlowLayout());		
		lblDialogoSeguro.setText(mensajeLbl);            
		s.add(lblDialogoSeguro);
		s.add(btnSi);
		s.add(btnNo);
		s.setSize(250,150);
		s.setLocationRelativeTo(null);
		s.setVisible(true);
	}
	
	
	
	public String getSHA256(String data)
	{
		StringBuffer sb = new StringBuffer();
		try
		{
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(data.getBytes());
			byte byteData[] = md.digest();
			
			for (int i = 0; i < byteData.length; i++) 
			{
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}
		} 

		catch(Exception e)
		{
			e.printStackTrace();
		}

		return sb.toString();
	}
	
	public void registrarLog(String usuario, String mensaje)
	{
		//FileWriter también puede lanzar una excepción 
		try
		{
			// Destino de los datos
			FileWriter fw = new FileWriter("Login.log", true);
			// Buffer de escritura
			BufferedWriter bw = new BufferedWriter(fw);
			// Objeto para la escritura
			PrintWriter salida = new PrintWriter(bw);
			// Guardamos la fecha y hora actuales
			Date fechaHoraActual = new Date();
			// Formato deseado
			DateFormat fechaHoraFormato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			salida.print("["+fechaHoraFormato.format(fechaHoraActual)+"]");
			salida.print("["+usuario+"]");
			salida.println("["+mensaje+"]");
			// Cerrar el objeto salida, el objeto bw y el fw
			salida.close();
			bw.close();
			fw.close();
		}
		catch(IOException i)
		{
			System.out.println("Se produjo un error de Archivo");
		}
	}
}
