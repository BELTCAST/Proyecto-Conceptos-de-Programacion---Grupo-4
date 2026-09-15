package com.proyecto.generador;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Clase encargada de generar los archivos planos pseudoaleatorios
 * que servirán como entrada para el sistema de procesamiento de ventas.
 * Corresponde a la Entrega 1 del proyecto del módulo.
 *
 * Grupo: G4 CFP-B03
 *
 * @Authors:
 * Nicolas Beltran Castellanos
 * Felipe Berruecos
 */
public class GenerateInfoFiles {

    private static final Random RANDOM = new Random();

    private static final String[] NOMBRES = {
            "Carlos", "Ana", "Andres", "Maria", "Luis",
            "Sofia", "Jorge", "Lucia", "David", "Valentina"
    };

    private static final String[] APELLIDOS = {
            "Perez", "Gomez", "Rodriguez", "Lopez", "Martinez",
            "Garcia", "Gonzalez", "Hernandez", "Pineda", "Diaz"
    };

    private static final String[] TIPOS_DOC = {
            "CC", "CE", "TI"
    };

    public static void main(String[] args) {

        try {

            System.out.println(
                    "Iniciando la generación de archivos de prueba..."
            );

            int cantidadVendedores = 3;
            int cantidadProductos = 10;
            int cantidadVentasPorVendedor = 4;

            // =========================================================
            // 1. Crear el archivo maestro de productos
            // =========================================================
            createProductsFile(cantidadProductos);

            // =========================================================
            // 2. Crear el archivo maestro con la información
            //    de los vendedores
            // =========================================================
            createSalesManInfoFile(cantidadVendedores);

            // =========================================================
            // 3. Leer los vendedores recién creados
            // =========================================================
            List<VendedorInfo> listaVendedores =
                    leerVendedoresDesdeArchivo("vendedores.txt");

            // =========================================================
            // 4. Crear un archivo de ventas para cada vendedor
            // =========================================================
            for (VendedorInfo v : listaVendedores) {

                createSalesMenFile(
                        cantidadVentasPorVendedor,
                        v.nombre,
                        v.numeroDoc
                );
            }

            System.out.println(
                    "¡Finalización exitosa! Todos los archivos de prueba "
                            + "se han generado correctamente."
            );

        } catch (IOException e) {

            System.err.println(
                    "Error: Ocurrió un error al generar los archivos planos."
            );

            e.printStackTrace();
        }
    }

    /**
     * Crea un archivo plano con información de ventas pseudoaleatorias
     * para un vendedor específico.
     *
     * @param randomSalesCount Cantidad de registros de venta a generar.
     * @param name Nombre del vendedor para identificar el archivo.
     * @param id Número de documento del vendedor.
     */
    public static void createSalesMenFile(
            int randomSalesCount,
            String name,
            long id) throws IOException {

        int totalProductos =
                obtenerCantidadProductos("productos.txt");

        if (totalProductos == 0) {
            totalProductos = 10;
        }

        String fileName =
                "vendedor_" + name + "_" + id + ".txt";

        File file = new File(fileName);

        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(file))) {

            // Primera línea: tipo de documento y número de documento
            writer.write("CC;" + id);
            writer.newLine();

            // Generar las ventas
            for (int i = 0; i < randomSalesCount; i++) {

                int idProducto =
                        RANDOM.nextInt(totalProductos) + 1;

                int cantidadVendida =
                        RANDOM.nextInt(5) + 1;

                writer.write(
                        idProducto + ";" + cantidadVendida + ";"
                );

                writer.newLine();
            }
        }

        System.out.println(
                "-> Creado archivo de ventas: " + fileName
        );
    }

    /**
     * Crea un archivo con información pseudoaleatoria
     * de productos disponibles.
     *
     * @param productsCount Cantidad de productos a generar.
     */
    public static void createProductsFile(
            int productsCount) throws IOException {

        String fileName = "productos.txt";
        File file = new File(fileName);

        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(file))) {

            for (int i = 1; i <= productsCount; i++) {

                String nombreProducto =
                        "Producto_" + i;

                double precioUnitario =
                        10000 + (RANDOM.nextDouble() * 90000);

                writer.write(
                        i + ";"
                                + nombreProducto + ";"
                                + String.format("%.2f", precioUnitario)
                );

                writer.newLine();
            }
        }

        System.out.println(
                "-> Creado archivo de productos: " + fileName
        );
    }

    /**
     * Crea un archivo con información pseudoaleatoria
     * de los vendedores.
     *
     * @param salesmanCount Cantidad de vendedores a generar.
     */
    public static void createSalesManInfoFile(
            int salesmanCount) throws IOException {

        String fileName = "vendedores.txt";
        File file = new File(fileName);

        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(file))) {

            for (int i = 0; i < salesmanCount; i++) {

                // Seleccionar tipo de documento aleatorio
                String tipoDoc =
                        TIPOS_DOC[
                                RANDOM.nextInt(TIPOS_DOC.length)
                                ];

                // Generar número de documento
                long numeroDoc =
                        100000000L
                                + RANDOM.nextInt(900000000);

                // Seleccionar nombre y apellido
                String nombre =
                        NOMBRES[
                                RANDOM.nextInt(NOMBRES.length)
                                ];

                String apellido =
                        APELLIDOS[
                                RANDOM.nextInt(APELLIDOS.length)
                                ];

                // Escribir información del vendedor
                writer.write(
                        tipoDoc + ";"
                                + numeroDoc + ";"
                                + nombre + ";"
                                + apellido
                );

                writer.newLine();
            }
        }

        System.out.println(
                "-> Creado archivo maestro de vendedores: "
                        + fileName
        );
    }

    // ================================================================
    // CLASE AUXILIAR
    // ================================================================

    /**
     * Clase que almacena la información de un vendedor.
     */
    private static class VendedorInfo {

        String tipoDoc;
        long numeroDoc;
        String nombre;
        String apellido;

        public VendedorInfo(
                String tipoDoc,
                long numeroDoc,
                String nombre,
                String apellido) {

            this.tipoDoc = tipoDoc;
            this.numeroDoc = numeroDoc;
            this.nombre = nombre;
            this.apellido = apellido;
        }
    }

    // ================================================================
    // MÉTODO PARA LEER VENDEDORES
    // ================================================================

    /**
     * Lee los vendedores almacenados en vendedores.txt.
     *
     * @param filePath Ruta del archivo.
     * @return Lista de objetos VendedorInfo.
     * @throws IOException Si ocurre un error al leer el archivo.
     */
    private static List<VendedorInfo> leerVendedoresDesdeArchivo(
            String filePath) throws IOException {

        // CORRECCIÓN IMPORTANTE:
        // Ahora la lista indica que contiene VendedorInfo
        List<VendedorInfo> lista = new ArrayList<>();

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(filePath))) {

            String linea;

            while ((linea = reader.readLine()) != null) {

                String[] partes = linea.split(";");

                // Verificar que la línea tenga los 4 datos
                if (partes.length >= 4) {

                    String tipoDoc = partes[0];

                    long numDoc =
                            Long.parseLong(partes[1]);

                    String nombre = partes[2];

                    String apellido = partes[3];

                    VendedorInfo vendedor =
                            new VendedorInfo(
                                    tipoDoc,
                                    numDoc,
                                    nombre,
                                    apellido
                            );

                    lista.add(vendedor);
                }
            }
        }

        return lista;
    }

    // ================================================================
    // MÉTODO PARA CONTAR PRODUCTOS
    // ================================================================

    /**
     * Obtiene la cantidad de productos existentes
     * en el archivo productos.txt.
     *
     * @param filePath Ruta del archivo.
     * @return Cantidad de productos.
     */
    private static int obtenerCantidadProductos(
            String filePath) {

        int count = 0;

        File file = new File(filePath);

        if (!file.exists()) {
            return 0;
        }

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(filePath))) {

            while (reader.readLine() != null) {
                count++;
            }

        } catch (IOException e) {

            return 0;
        }

        return count;
    }
}
