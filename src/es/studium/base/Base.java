package es.studium.base;

import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Base extends Frame implements WindowListener, ActionListener {
  
  private static final long serialVersionUID = 1L;
  
  TextField idEmpleado = new TextField(25);
  TextField nombreEmpleado = new TextField(25);
  
  Button next = new Button("Próximo");
  Button previous = new Button("Anterior");
  Button primero = new Button("Primero");
  Button ultimo = new Button("Último");
  
  //Crear un objeto tipo Connection
  Connection con = null;
  
  //Data Source Name de la Base de Datos
  String driver = "com.mysql.jdbc.Driver";
  String url = "jdbc:mysql://localhost:3306/empresa?autoReconnect=true&useSSL=false";
  String login = "root";
  String password = "Studium2019;";
  
  //Creamos una consulta a la base de datos
  String sentencia = "SELECT * FROM empleados";
  
  //Objeto donde se guarda la información de la consulta a la base de datos
  ResultSet rs = null;
  
  //Crear un statement de SQL
  Statement stmt = null;

  public Base() { 
    setLayout(new FlowLayout());
    setSize(250, 160);
    setResizable(false);
    
    add(idEmpleado);
    add(nombreEmpleado);
    add(primero);
    add(previous);
    add(next);
    add(ultimo);
    
    next.addActionListener(this);
    previous.addActionListener(this);
    primero.addActionListener(this);
    ultimo.addActionListener(this);
    addWindowListener(this);
        
    // Cargar el Driver
    try {
      Class.forName(driver);
    } catch (ClassNotFoundException e) {
      System.out.println("Se ha producido un error al cargar el Driver");
    }
    // Establecer la conexión con la base de datos
    try {
      con = DriverManager.getConnection(url, login, password);
    } catch (SQLException e) {
      System.out.println("Se produjo un error al conectar a la Base de Datos");
    }
    // Realizar la consulta
    try {
      stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
      rs = stmt.executeQuery("SELECT * FROM empleados");
      rs.next();
    //Poner en los TextField los valores obtenidos del 1º
      idEmpleado.setText(Integer.toString(rs.getInt("idEmpleado")));
      nombreEmpleado.setText(rs.getString("nombreEmpleado"));
    } catch (SQLException e) {
      System.out.println("Error en la sentencia SQL");
    }
    setVisible(true);
  }//Fin del constructor

  public static void main(String[] args) {
    new Base();
  }

  public void windowActivated(WindowEvent windowEvent) {
  }

  public void windowClosed(WindowEvent windowEvent) {
  }

  public void windowClosing(WindowEvent windowEvent) {
    //cerrar los elementos de la base de datos
    try {
      rs.close();
      stmt.close();
      con.close();
    } catch (SQLException e) {
      System.out.println("error al cerrar " + e.toString());
    }
    System.exit(0);
  }

  public void windowDeactivated(WindowEvent windowEvent) {
  }

  public void windowDeiconified(WindowEvent windowEvent) {
  }

  public void windowIconified(WindowEvent windowEvent) {
  }

  public void windowOpened(WindowEvent windowEvent) {
  }

  public void actionPerformed(ActionEvent actionEvent) {
    // Hemos pulsado Próximo
    if (next.equals(actionEvent.getSource())) { 
      try { 
        //Si no hemos llegado al final
        if (rs.next()) {      
          //Poner en los TextField los valores obtenidos
          idEmpleado.setText(Integer.toString(rs.getInt("idEmpleado")));
          nombreEmpleado.setText(rs.getString("nombreEmpleado"));     
        } else {    
          //Mueve el cursos al registro anterior
          rs.previous();
          idEmpleado.setText(Integer.toString(rs.getInt("idEmpleado")));
          nombreEmpleado.setText(rs.getString("nombreEmpleado"));     
        }     
      } catch (SQLException e) {    
        System.out.println("Error en la sentencia SQL" + e.getMessage());     
      } 
    }
    
    // Hemos pulsado Previo
    if (previous.equals(actionEvent.getSource())) { 
      try {   
        //Si no hemos llegado al final
        if (rs.previous()) {      
          //Poner en los TextField los valores obtenidos
          idEmpleado.setText(Integer.toString(rs.getInt("idEmpleado")));
          nombreEmpleado.setText(rs.getString("nombreEmpleado"));       
        } else {    
          rs.next();
          idEmpleado.setText(Integer.toString(rs.getInt("idEmpleado")));
          nombreEmpleado.setText(rs.getString("nombreEmpleado"));     
        } 
      } catch (SQLException e) {    
        System.out.println("Error en la sentencia SQL" + e.getMessage());   
      }
    }
    
    if (primero.equals(actionEvent.getSource())) {
      try {
        rs.first();
        idEmpleado.setText(Integer.toString(rs.getInt("idEmpleado")));
        nombreEmpleado.setText(rs.getString("nombreEmpleado")); 
      } catch (SQLException e) {  
        System.out.println("Error en la sentencia SQL" + e.getMessage());   
      }
    }
    
    if (ultimo.equals(actionEvent.getSource())) {
      try {
        rs.last();
        idEmpleado.setText(Integer.toString(rs.getInt("idEmpleado")));
        nombreEmpleado.setText(rs.getString("nombreEmpleado")); 
      } catch (SQLException e) {
        System.out.println("Error en la sentencia SQL" + e.getMessage());
      }
    }
  }
}
