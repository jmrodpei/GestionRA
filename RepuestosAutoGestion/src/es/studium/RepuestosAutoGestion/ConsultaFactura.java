package es.studium.RepuestosAutoGestion;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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


public class ConsultaFactura extends Frame implements WindowListener, ActionListener, ItemListener
{
	private static final long serialVersionUID = 1L;
	TextArea txaListado = new TextArea(13,40);
	Button exportarPdf = new Button("Exportar PDF");
	Choice choLista = new Choice();
	BaseDatos bd = new BaseDatos();
	Connection conexion = null;
	String[] cadena;
	String usuario;
	Utilidades ut = new Utilidades();
	
	public ConsultaFactura(String usuario)
	{
		this.usuario = usuario;
		setTitle("Consulta Facturas");
		setLayout(new FlowLayout());
		// Conectar con la BD
		conexion = bd.conectar();
		txaListado.setText(bd.consultarFacturas(conexion));
		add(txaListado);
		exportarPdf.addActionListener(this);
		// Rellenar
		choLista.add("Seleccionar una factura para editar...");
		cadena = (bd.consultarFacturasChoice(conexion)).split("#");
		for(int i = 0; i < cadena.length; i++)
		{
			choLista.add(cadena[i]);
		}
		choLista.addItemListener(this);
		add(choLista);
		bd.desconectar(conexion);
		add(exportarPdf);
		addWindowListener(this);
		setSize(400,300);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void windowActivated(WindowEvent windowEvent){}
	public void windowClosed(WindowEvent windowEvent) {}
	public void windowClosing(WindowEvent windowEvent)
	{
		ut.registrarLog(usuario, "Salir de Consulta Facturas");
		setVisible(false);
	}
	public void windowDeactivated(WindowEvent windowEvent) {}
	public void windowDeiconified(WindowEvent windowEvent) {}
	public void windowIconified(WindowEvent windowEvent) {}
	public void windowOpened(WindowEvent windowEvent) {}
	
	public void actionPerformed(ActionEvent evento) 
	{
		// Exportar a PDF
		// Se crea el documento 
		Document documento = new Document(PageSize.A4);
		try 
		{ 
			ut.registrarLog(usuario, "Generar PDF de Consulta Facturas");
			// Se crea el OutputStream para el fichero donde queremos dejar el pdf. 
			FileOutputStream ficheroPdf2 = new FileOutputStream("Facturas.pdf");
			PdfWriter.getInstance(documento, ficheroPdf2).setInitialLeading(22);
			// Se abre el documento. 
			documento.open();
			Paragraph titulo = new Paragraph("Informe de Facturas", 
					FontFactory.getFont("arial", // fuente 
							22, // tamaño 
							Font.ITALIC, // estilo 
							BaseColor.BLUE)); // color
			titulo.setAlignment(Element.ALIGN_CENTER);
			documento.add(titulo);
			// Sacar los datos
			conexion = bd.conectar();
			String[] cadena = bd.consultarFacturas(conexion).split("\n");
			bd.desconectar(conexion);
			PdfPTable tabla = new PdfPTable(3); // Se indica el número de columnas
			tabla.setSpacingBefore(20); // Espaciado ANTES de la tabla
			tabla.setWidthPercentage(100F);//Ancho de la tabla en %
			tabla.setWidths(new int[]{50,50,150,});//Anchos de las columnas
			tabla.addCell("Número Pedido");
			tabla.addCell("Fecha Pedido");
			tabla.addCell("Empresa");
						
			// En cada posición de cadena tenemos un registro completo
			// cadena[0] = "1-Fulanito-12/12/2020"
			String[] subCadena;
			// En subCadena, separamos cada campo por -
			// subCadena[0] = 1
			// subCadena[1] = 12/12/2020
			// subCadena[2] = Fulanito
			for (int i = 0; i < cadena.length; i++) 
			{
				subCadena = cadena[i].split("-");					
				for(int j = 0; j < 3;j++)
				{
					tabla.addCell(subCadena[j]);
				}
			}
			documento.add(tabla); 
			documento.close(); 
			//Abrimos el archivo PDF recién creado 
			try 
			{
				File path2 = new File ("Facturas.pdf"); 
				Desktop.getDesktop().open(path2); 
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
	
	public void itemStateChanged(ItemEvent arg0)
	{
		// Averiguar elemento seleccionado
		String[] seleccionado = choLista.getSelectedItem().split("-");
		// seleccionado[0] --> idFactura
		new DetalleFactura(Integer.parseInt(seleccionado[0]), usuario);
	}
}
