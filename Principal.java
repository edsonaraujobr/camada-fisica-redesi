import java.net.URL;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Principal  extends Application {
  
  private static Scene scene;
  
  public static void main(String[] args) {
    launch(args);
  }
  
  @Override
  public void start(Stage primaryStage) throws Exception {
    primaryStage.setTitle("Camada Física");
    Parent root = FXMLLoader.load(getClass().getResource("/view/interface.fxml"));
    scene = new Scene(root);

    primaryStage.setResizable(false); // evitar Maximizar
    primaryStage.setScene(scene);
    primaryStage.setOnCloseRequest(t -> { // fechar o processo caso seja fechado a janela
      Platform.exit();
      System.exit(0);
    });
    primaryStage.show();

  } 

}
