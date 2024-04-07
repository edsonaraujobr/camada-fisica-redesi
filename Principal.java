/* ***************************************************************
* Autor............: Edson Araujo de Souza Neto
* Matricula........: 202210169
* Inicio...........: 01/04/2024
* Ultima alteracao.: 07/04/2024
* Nome.............: principal
* Funcao...........: executa a interface grafica
*************************************************************** */


import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import controller.PrincipalController;
  
public class Principal  extends Application {
  
  private static Scene scene;
  
  public static void main(String[] args) {
    launch(args);
  }
  
  @Override
  public void start(Stage primaryStage) throws Exception {
    primaryStage.setTitle("Trabalho de Redes I: Camada Física");
    Parent root = FXMLLoader.load(getClass().getResource("/view/interface.fxml"));
    scene = new Scene(root);
    Image icon = new Image("/img/icon.png");  // Carregando o ícone da janela
    primaryStage.getIcons().add(icon);
    
    primaryStage.setResizable(false); // evitar Maximizar
    primaryStage.setScene(scene);
    primaryStage.setOnCloseRequest(t -> { // fechar o processo caso seja fechado a janela
      Platform.exit();
      System.exit(0);
    });
    primaryStage.show();

  } 

}
