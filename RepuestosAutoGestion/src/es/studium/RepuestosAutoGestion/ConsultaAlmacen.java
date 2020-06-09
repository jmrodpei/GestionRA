package es.studium.RepuestosAutoGestion;

import java.awt.Button;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class ConsultaAlmacen extends Frame implements WindowListener, ActionListener
{
	private static final long serialVersionUID = 1L;
	TextArea listado = new TextArea(13,65);
	Button btnSalir = new Button("Salir");
	Button exportarPdf = new Button("Exportar PDF");
	BaseDatos bd = new BaseDatos();
	Connection conexion = null;
	String usuario;
	Utilidades ut = new Utilidades();
	
	public ConsultaAlmacen(String usuario)
	{
		this.usuario = usuario;
		setTitle("Consulta Almacen");
		setLayout(new FlowLayout());
		// Conectar con la BD
		conexion = bd.conectar();
		listado.setText(bd.consultarAlmacenes(conexion));
		bd.desconectar(conexion);
		add(listado);		
		add(btnSalir);
		add(exportarPdf);
		btnSalir.addActionListener(this);
		exportarPdf.addActionListener(this);
		addWindowListener(this);
		setSize(500,300);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void windowActivated(WindowEvent windowEvent){}
	public void windowClosed(WindowEvent windowEvent) {}
	public void windowClosing(WindowEvent windowEvent)
	{
		ut.registrarLog(usuario, "Salir de Consulta Almacenes");
		setVisible(false);
	}
	public void windowDeactivated(WindowEvent windowEvent) {}
	public void windowDeiconified(WindowEvent windowEvent) {}
	public void windowIconified(WindowEvent windowEvent) {}
	public void windowOpened(WindowEvent windowEvent) {}
	
	public void actionPerformed(ActionEvent evento) 
	{
		Object a;
		a=evento.getSource();
		if(a.equals(btnSalir))
		{
			ut.registrarLog(usuario, "Salir de Consulta Almacenes");
			setVisible(false);
		}
		else
		{
			// Exportar a PDF
			// Se crea el documento 
			Document documento = new Document(PageSize.A4.rotate());
			try 
			{ 
				ut.registrarLog(usuario, "Generar PDF de consulta Almacenes");
				// Se crea el OutputStream para el fichero donde queremos dejar el pdf. 
				FileOutputStream ficheroPdf1 = new FileOutputStream("Almacenes.pdf");
				PdfWriter.getInstance(documento, ficheroPdf1).setInitialLeading(22);
				// Se abre el documento. 
				documento.open();
				Paragraph titulo = new Paragraph("Informe de Almacenes", 
						FontFactory.getFont("arial", // fuente 
								22, // tamaño 
								Font.ITALIC, // estilo 
								BaseColor.BLUE)); // color
				titulo.setAlignment(Element.ALIGN_CENTER);
				documento.add(titulo);
				// Sacar los datos
				conexion = bd.conectar();
				String[] cadena = bd.consultarAlmacenes(conexion).split("\n");
				bd.desconectar(conexion);
				PdfPTable tabla = new PdfPTable(6); // Se indica el número de columnas
				tabla.setSpacingBefore(20); // Espaciado ANTES de la tabla
				tabla.setWidthPercentage(100F);
				tabla.setWidths(new int[]{10,100,150,50,50,50,});
				tabla.addCell("#");
				tabla.addCell("Nombre Producto");
				tabla.addCell("Precio");
				tabla.addCell("Pasillo");
				tabla.addCell("Estantería");
				tabla.addCell("Balda");
				
				// En cada posición de cadena tenemos un registro completo
				// cadena[0] = "1-Fulanito-12/12/2020"
				String[] subCadena;
				// En subCadena, separamos cada campo por -
				// subCadena[0] = 1
				// subCadena[1] = Fulanito
				// subCadena[2] = 12/12/2020
				for (int i = 0; i < cadena.length; i++) 
				{
					subCadena = cadena[i].split("-");					
					for(int j = 0; j < 6;j++)
					{
						tabla.addCell(subCadena[j]);
					}
				}
				documento.add(tabla); 
				documento.close(); 
				//Abrimos el archivo PDF recién creado 
				try 
				{
					File path1 = new File ("Productos.pdf"); 
					Desktop.getDesktop().open(path1); 
				}
				catch (IOException ex) 
				{
					System.out.println("Se ha producido un error al abrir el archivo PDF"); 
				}
			}
			catch ( Exception e ) 
			{ 
				System.out.println("Se ha producido un error al generar el archivo PDF");  
			}
		}
	}
}