package generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Clase encargada de generar los archivos planos pseudoaleatorios
 * que servirán como entrada para el sistema de procesamiento de ventas.
 * Corresponde a la Entrega 1 del proyecto del módulo.
 *
 * Grupo: G4 CFP-B03
 * @Estudiantes:
 * Nicolas beltran castellanos
 * Luis Felipe Berruecos
 */
public class GenerateInfoFiles {

    private static final Random RANDOM = new Random();

    // Listas de apoyo para generar datos coherentes
    private static final String[] NOMBRES = {"Carlos", "Ana", "Andres", "Maria", "Luis", "Sofia", "Jorge", "Lucia", "David", "Valentina"};
    private static final String[] APELLIDOS = {"Perez", "Gomez", "Rodriguez", "Lopez", "Martinez", "Garcia", "Gonzalez", "Hernandez", "Pineda", "Diaz"};
    private static final String[] TIPOS_DOC = {"CC", "CE", "TI"};

    public static void main(String[] args) {
        try {
            System.out.println("Iniciando la generación de archivos de prueba...");

            // Definir parámetros de prueba
            int cantidadVendedores = 5;
            int cantidadProductos = 10;
            int cantidadVentasPorVendedor = 4;

            // 1. Crear archivo de información de productos
            createProductsFile(cantidadProductos);

            // 2. Crear archivo de información de vendedores
            createSalesManInfoFile(cantidadVendedores);

            // 3. Crear archivos individuales de ventas por cada vendedor (simulación básica)
            // Nota: Para fines prácticos, generamos archivos con IDs de vendedores de ejemplo
            createSalesMenFile(cantidadVentasPorVendedor, "Carlos_Perez", 1012345678L);
            createSalesMenFile(cantidadVentasPorVendedor, "Ana_Gomez", 1023456789L);
            createSalesMenFile(cantidadVentasPorVendedor, "Andres_Rodriguez", 1034567890L);

            System.out.println("¡Finalización exitosa! Los archivos se han generado correctamente en la carpeta del proyecto.");

        } catch (IOException e) {
            System.err.println("Error: Ocurrió un error al generar los archivos plano.");
            e.printStackTrace();
        }
    }

    /**
     * Crea un archivo plano con información de ventas pseudoaleatorias para un vendedor específico.
     * Formato:
     * Tipo Documento Vendedor; Número Documento Vendedor
     * ID Producto1; Cantidad Producto1Vendido;
     *
     * @param randomSalesCount Cantidad de registros de venta a generar.
     * @param name Nombre del vendedor para identificar el archivo.
     * @param id Número de documento del vendedor.
     */
    public static void createSalesMenFile(int randomSalesCount, String name, long id) throws IOException {
        String fileName = "vendedor_" + name + "_" + id + ".txt";
        File file = new File(fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Línea de cabecera del vendedor dentro de su archivo
            String tipoDoc = TIPOS_DOC[RANDOM.nextInt(TIPOS_DOC.length)];
            writer.write(tipoDoc + ";" + id);
            writer.newLine();

            // Generar transacciones de ventas aleatorias
            for (int i = 0; i < randomSalesCount; i++) {
                int idProducto = RANDOM.nextInt(10) + 1; // IDs del 1 al 10
                int cantidadVendida = RANDOM.nextInt(5) + 1; // Cantidades entre 1 y 5

                writer.write(idProducto + ";" + cantidadVendida + ";");
                writer.newLine();
            }
        }
        System.out.println("-> Creado archivo de ventas: " + fileName);
    }

    /**
     * Crea un archivo con información pseudoaleatoria de productos disponibles.
     * Formato: ID Producto; Nombre Producto; Precio Por Unidad
     *
     * @param productsCount Cantidad de productos a generar.
     */
    public static void createProductsFile(int productsCount) throws IOException {
        String fileName = "productos.txt";
        File file = new File(fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (int i = 1; i <= productsCount; i++) {
                String nombreProducto = "Producto_" + i;
                double precioUnitario = 10000 + (RANDOM.nextDouble() * 90000); // Precio entre 10,000 y 100,000

                writer.write(i + ";" + nombreProducto + ";" + String.format("%.2f", precioUnitario));
                writer.newLine();
            }
        }
        System.out.println("-> Creado archivo de productos: " + fileName);
    }

    /**
     * Crea un archivo con información general de los vendedores.
     * Formato: Tipo Documento; Número Documento; Nombres Vendedor; Apellidos Vendedor
     *
     * @param salesmanCount Cantidad de vendedores a generar.
     */
    public static void createSalesManInfoFile(int salesmanCount) throws IOException {
        String fileName = "vendedores.txt";
        File file = new File(fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (int i = 0; i < salesmanCount; i++) {
                String tipoDoc = TIPOS_DOC[RANDOM.nextInt(TIPOS_DOC.length)];
                long numeroDoc = 100000000L + RANDOM.nextInt(900000000);
                String nombre = NOMBRES[RANDOM.nextInt(NOMBRES.length)];
                String apellido = APELLIDOS[RANDOM.nextInt(APELLIDOS.length)];

                writer.write(tipoDoc + ";" + numeroDoc + ";" + nombre + ";" + apellido);
                writer.newLine();
            }
        }
        System.out.println("-> Creado archivo maestro de vendedores: " + fileName);
    }
}