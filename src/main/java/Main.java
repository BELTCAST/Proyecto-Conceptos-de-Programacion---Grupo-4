package org.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        try {
            Map productosMap = cargarProductos("productos.txt");
            Map vendedoresMap = cargarVendedores("vendedores.txt");

            procesarVentas(vendedoresMap, productosMap);

            generarReporteVendedores(vendedoresMap);
            generarReporteProductos(productosMap);

            System.out.println("¡Finalización exitosa de la generación de reportes!");
        } catch (Exception e) {
            System.err.println("Ocurrió un error durante la ejecución del programa: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static Map cargarProductos(String ruta) throws IOException {
        Map mapa = new HashMap<>();
        File archivo = new File(ruta);
        if (!archivo.exists()) return mapa;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length >= 3) {
                    String id = partes[0].trim();
                    String nombre = partes[1].trim();

                    // Reemplazamos la coma por punto para que Java lo pueda leer
                    String precioStr = partes[2].trim().replace(",", ".");
                    double precio = Double.parseDouble(precioStr);

                    mapa.put(id, new Producto(id, nombre, precio));
                }
            }
        }
        return mapa;
    }

    private static Map cargarVendedores(String ruta) throws IOException {
        Map mapa = new HashMap<>();
        File archivo = new File(ruta);
        if (!archivo.exists()) return mapa;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length >= 4) {
                    String tipoDoc = partes[0].trim();
                    String numDoc = partes[1].trim();
                    String nombres = partes[2].trim();
                    String apellidos = partes[3].trim();
                    String nombreCompleto = nombres + " " + apellidos;
                    mapa.put(numDoc, new Vendedor(tipoDoc, numDoc, nombreCompleto));
                }
            }
        }
        return mapa;
    }

    private static void procesarVentas(Map vendedoresMap, Map productosMap) {
        File carpeta = new File(".");
        File[] archivos = carpeta.listFiles((dir, name) -> name.endsWith(".txt") && !name.equals("productos.txt") && !name.equals("vendedores.txt"));

        if (archivos == null) return;

        for (File archivo : archivos) {
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String linea = br.readLine();
                if (linea == null) continue;

                String[] cabecera = linea.split(";");
                if (cabecera.length < 2) continue;
                String numDocVendedor = cabecera[1].trim();

                Vendedor vendedor = (Vendedor) vendedoresMap.get(numDocVendedor);

                while ((linea = br.readLine()) != null) {
                    String[] datosVenta = linea.split(";");
                    if (datosVenta.length >= 2) {
                        String idProd = datosVenta[0].trim();
                        int cantidad = Integer.parseInt(datosVenta[1].trim());

                        Producto prod = (Producto) productosMap.get(idProd);
                        if (prod != null) {
                            double subtotal = prod.precio * cantidad;
                            if (vendedor != null) {
                                vendedor.totalRecaudado += subtotal;
                            }
                            prod.cantidadVendida += cantidad;
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Error leyendo el archivo: " + archivo.getName());
            }
        }
    }

    private static void generarReporteVendedores(Map vendedoresMap) throws IOException {
        List lista = new ArrayList<>(vendedoresMap.values());
        lista.sort((v1, v2) -> Double.compare(((Vendedor) v2).totalRecaudado, ((Vendedor) v1).totalRecaudado));

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("reporte_vendedores.csv"))) {
            for (Object obj : lista) {
                Vendedor v = (Vendedor) obj;
                bw.write(v.nombreCompleto + ";" + v.totalRecaudado);
                bw.newLine();
            }
        }
    }

    private static void generarReporteProductos(Map productosMap) throws IOException {
        List lista = new ArrayList<>(productosMap.values());
        lista.sort((p1, p2) -> Integer.compare(((Producto) p2).cantidadVendida, ((Producto) p1).cantidadVendida));

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("reporte_productos.csv"))) {
            for (Object obj : lista) {
                Producto p = (Producto) obj;
                bw.write(p.nombre + ";" + p.precio);
                bw.newLine();
            }
        }
    }

    public static class Producto {
        public String id, nombre;
        public double precio;
        public int cantidadVendida = 0;

        public Producto(String id, String nombre, double precio) {
            this.id = id;
            this.nombre = nombre;
            this.precio = precio;
        }
    }

    public static class Vendedor {
        public String tipoDoc, numDoc, nombreCompleto;
        public double totalRecaudado = 0;

        public Vendedor(String tipoDoc, String numDoc, String nombreCompleto) {
            this.tipoDoc = tipoDoc;
            this.numDoc = numDoc;
            this.nombreCompleto = nombreCompleto;
        }
    }
}