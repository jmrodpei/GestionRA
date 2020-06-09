package es.studium.RepuestosAutoGestion;

import java.awt.Button;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.TextField;
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

public class DetalleFactura extends Frame implements WindowListener, ActionListener
{
	private static final long serialVersionUID = 1L;
	int idFactura;
	Label lblFactura = new Label("Factura Nº");
	Label lblTotal = new Label("Total");
	TextField txtTotal = new TextField(8);
	TextArea txaListado = new TextArea(8,50);
	Button btnSalir = new Button("Salir");
	Button btnExportar = new Button("Exportar PDF");
	int idFacturaFK;
	BaseDatos bd = new BaseDatos();
	Connection conexion = null;
	String[] cadena;
	Double total = 0.0;
	String usuario;
	Utilidades ut = new Utilidades();
	
	public DetalleFactura(int idFactura, String usuario) 
	{
		this.idFactura = idFactura;
		this.usuario = usuario;
		setTitle("Detalle Factura");
		setLayout(new FlowLayout());
		lblFactura.setText("Factura Nº "+idFactura);
		add(lblFactura);
		// Conectar a la BD
		conexion = bd.conectar();
		// Sacar los datos de la factura concreta
		cadena = bd.consultarFactura(conexion, idFactura).split("-");
		// cadena[0] = fechaPedido
		// cadena[1] = nombreEmpresa
		// cadena[2] = subtotalpedido
		// cadena[3] = ivaPedido
		// cadena[4] = total
		
	
		total = Double.parseDouble(cadena[4]);		
		// Sacar los detalles de la factura concreta
		txaListado.setText("Fecha:"+cadena[0]+"\n"+"Empresa:"+cadena[1]+"\n"+bd.consultarDetallesFactura(conexion, idFactura));
		// SELECT * FROM lineasFactura, servicios
		// Los idServicioFK y las cantidades correspondientes
		// Por cada idServicioFK, tengo que sacar la descripcion y el precio
		add(txaListado);
		add(lblTotal);
		txtTotal.setText(total+"");
		txtTotal.setEnabled(false);
		add(txtTotal);
		btnSalir.addActionListener(this);
		btnExportar.addActionListener(this);
		add(btnSalir);
		add(btnExportar);
		addWindowListener(this);
		setSize(420,300);
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
		ut.registrarLog(usuario, "Salir de Consulta Detalle Factura");
		setVisible(false);
	}
	public void windowDeactivated(WindowEvent windowEvent) {}
	public void windowDeiconified(WindowEvent windowEvent) {}
	public void windowIconified(WindowEvent windowEvent) {}
	public void windowOpened(WindowEvent windowEvent) {}
	
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource().equals(btnSalir))
		{
			ut.registrarLog(usuario, "Salir de Consulta Detalle Factura");
			setVisible(false);
		}
		else
		{
			// Exportar a PDF
			// Se crea el documento 
			Document documento = new Document(PageSize.A4);
			try 
			{ 
				ut.registrarLog(usuario, "Generar PDF de Consulta Detalle Factura");
				// Se crea el OutputStream para el fichero donde queremos dejar el pdf. 
				FileOutputStream ficheroPdf3 = new FileOutputStream("Factura.pdf");
				PdfWriter.getInstance(documento, ficheroPdf3).setInitialLeading(22);
				// Se abre el documento. 
				documento.open();
				Paragraph titulo = new Paragraph("Factura Nº "+idFactura, 
						FontFactory.getFont("arial", // fuente 
								22, // tamaño 
								Font.ITALIC, // estilo 
								BaseColor.BLUE)); // color
				titulo.setAlignment(Element.ALIGN_CENTER);
				documento.add(titulo);
				
				Paragraph cliente = new Paragraph("Empresa: "+cadena[1], 
						FontFactory.getFont("arial", // fuente 
								22, // tamaño 
								Font.ITALIC, // estilo 
								BaseColor.BLUE)); // color
				titulo.setAlignment(Element.ALIGN_LEFT);
				documento.add(cliente);		
				
				
				Paragraph fecha = new Paragraph("FECHA: "+cadena[0], 
						FontFactory.getFont("arial", // fuente 
								22, // tamaño 
								Font.ITALIC, // estilo 
								BaseColor.BLUE)); // color
				titulo.setAlignment(Element.ALIGN_LEFT);
				documento.add(fecha);
				
				// Sacar los datos
				conexion = bd.conectar();
				String[] cadena = bd.consultarDetallesFactura(conexion, idFactura).split("\n");
				bd.desconectar(conexion);
				PdfPTable tabla = new PdfPTable(4); // Se indica el número de columnas
				tabla.setSpacingBefore(20); // Espaciado ANTES de la tabla
				tabla.setWidthPercentage(100F);//Ancho de la tabla en %
				tabla.setWidths(new int[]{40,150,40,40,});//Anchos de las columnas
				tabla.addCell("Cantidad");
				tabla.addCell("Nombre Producto");
				tabla.addCell("Precio");
				tabla.addCell("Subtotal");
							
				// En cada posición de cadena tenemos un registro completo
				// cadena[0] = "1-Fulanito-7.5-7.5"
				String[] subCadena;
				// En subCadena, separamos cada campo por -
				// subCadena[0] = 1
				// subCadena[1] = Fulanito
				// subCadena[2] = 7,5
				// subCadena[3] = 7,5
				for (int i = 0; i < cadena.length; i++) 
				{
					subCadena = cadena[i].split("-");					
					for(int j = 0; j < 4;j++)
					{
						tabla.addCell(subCadena[j]);
					}
				}
				documento.add(tabla); 
				
				PdfPTable tabla1 = new PdfPTable(6); // Se indica el número de columnas
				tabla1.setSpacingBefore(3); // Espaciado ANTES de la tabla
				tabla1.setWidthPercentage(100F);//Ancho de la tabla en %
				tabla1.setWidths(new int[]{40,40,40,40,40,40,});//Anchos de las columnas
				tabla1.addCell("TOTAL");
				tabla1.addCell(total+"€");
				documento.add(tabla1);
				documento.close(); 
				//Abrimos el archivo PDF recién creado 
				try 
				{
					File path3 = new File ("Factura.pdf"); 
					Desktop.getDesktop().open(path3); 
				}
				catch (IOException ex) 
				{
					System.out.println("Se ha producido un error al abrir el archivo PDF"); 
				}
			}
			catch ( Exception ea ) 
			{ 
				System.out.println("Se ha producido un error al generar el archivo PDF");  
			}		
		}
	}
}
