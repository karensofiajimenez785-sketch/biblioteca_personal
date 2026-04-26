import java.sql.*;
import java.util.Scanner;

public class index {

    static Connection con;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws Exception {

        // Cargar driver y conectar a SQLite

        Class.forName("org.sqlite.JDBC");
        con = DriverManager.getConnection("jdbc:sqlite:Biblioteca_personal.db");
        System.out.println("✔ Conectado a SQLite");

        // Crear tablas

        con.createStatement().execute(
            "CREATE TABLE IF NOT EXISTS libros ("+
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "titulo  TEXT    NOT NULL,"+
            "autor   TEXT    NOT NULL,"+
            "genero  TEXT, anio    INTEGER,"+
            "estado  TEXT    DEFAULT 'Por leer',"+
            "nota    REAL    DEFAULT 0)"
        );


        int opcion;

        do { 
            System.out.println("--- MENU ELIGA UNA OPCION ---");
            System.out.println("1. Agregar libro");
            System.out.println("2. Actualizar estado");
            System.out.println("3. Buscar libro");
            System.out.println("4. Eliminar libro");

            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1 -> insertar();
                
                case 2 -> actualizarEstado();

                case 3-> buscarLibro();
                
                case 4-> eliminarLibro(opcion);
                
            }
            
        } while (opcion != 3);

        con.close();
    }

    // ──────────────────────────────────────────
    //  CREACIÓN DE TABLAS
    // ──────────────────────────────────────────
    static void insertar() throws Exception {

        System.out.print("Nombre: ");
        String nombre = sc.nextLine();

        System.out.print("Autor: ");
        String autor1 = sc.nextLine();

        System.out.print("Genero: ");
        String genero1 = sc.nextLine();

        System.out.print("Año: ");
        int anio1 = Integer.parseInt(sc.nextLine());

        System.out.print("Estado: ");
        String estado1 = sc.nextLine();

        System.out.print("Nota: ");
        double nota1 = Double.parseDouble(sc.nextLine());
        
        PreparedStatement ps = con.prepareStatement(
            "INSERT INTO libros (titulo, autor, genero, anio, estado, nota) VALUES (?,?,?,?,?,?)"
        );
        
        ps.setString(1, nombre);
        ps.setString(2, autor1);
        ps.setString(3, genero1);
        ps.setInt(4, anio1);
        ps.setString(5, estado1);
        ps.setDouble(6, nota1);
        ps.executeUpdate();

        try{
            ps.executeUpdate();
            System.out.println("Libro agregado: ");
        } catch (SQLException e) {
            System.err.println("Ya existe este libro: ");
        }
    }


    static void actualizarEstado() throws Exception {

        System.out.print("Actualizar estado del libro:");
        System.out.println();

        System.out.println("Nuevo estado: ");
        String estado = sc.nextLine();

        System.out.println("Nueva nota: ");
        double nota = sc.nextDouble();

        System.out.println("ID del libro: ");
        int id = sc.nextInt();

        sc.nextLine();

        PreparedStatement ps= con.prepareStatement("UPDATE libros SET estado = ?, nota = ? WHERE id = ?");

        ps.setString(1, estado);
            ps.setDouble(2, nota);
            ps.setInt(3, id);
            
            int filas = ps.executeUpdate();
        if (filas > 0) {
            System.out.println("Libro actualizado");
        } else {
            System.out.println("Libro no encontrado");
        }
    }

    static void buscarLibro() throws Exception {
        System.out.print("Buscar libro por ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        PreparedStatement ps = con.prepareStatement("SELECT * FROM libros WHERE id = ?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            System.out.println("ID: " + rs.getInt("id"));
            System.out.println("Título: " + rs.getString("titulo"));
            System.out.println("Autor: " + rs.getString("autor"));
            System.out.println("Género: " + rs.getString("genero"));
            System.out.println("Año: " + rs.getInt("anio"));
            System.out.println("Estado: " + rs.getString("estado"));
            System.out.println("Nota: " + rs.getDouble("nota"));
        } else {
            System.out.println("Libro no encontrado");
        }
    }


    static void eliminarLibro(int id) throws Exception {
        System.out.print("Eliminar libro con ID: ");
        System.out.println();
        int id1 = sc.nextInt();
        sc.nextLine();

            PreparedStatement ps = con.prepareStatement("DELETE FROM libros WHERE id = ?");

            ps.setInt(1, id1);
            ps.executeUpdate();
            

            int filas = ps.executeUpdate();

        if (filas > 0) {
            System.out.println("Eliminado");
        } else {
            System.out.println("No encontrado");
        }
    }

}