package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import model.transmissor.AplicacaoTransmissora;

public class PrincipalController implements Initializable {
  @FXML
  private Label labelAviso;
      
  @FXML
  private Button buttonEnviar;

  @FXML
  private ImageView imageDemarcacaoSinais;
  @FXML
  private ImageView imagemSinalVertical01e02;
  @FXML
  private ImageView imagemSinalVertical02e03;
  @FXML
  private ImageView imagemSinalVertical03e04;
  @FXML
  private ImageView imagemSinalVertical04e05;
  @FXML
  private ImageView imagemSinalVertical05e06;
  @FXML
  private ImageView imagemSinalVertical06e07;
  @FXML
  private ImageView imagemSinalVertical07e08;
  
  @FXML
  private ImageView imgSinal01Alto;
  @FXML
  private ImageView imgSinal01Baixo;
  @FXML
  private ImageView imgSinal02Alto;
  @FXML
  private ImageView imgSinal02Baixo;
  @FXML
  private ImageView imgSinal03Alto;
  @FXML
  private ImageView imgSinal03Baixo;
  @FXML
  private ImageView imgSinal04Alto;
  @FXML
  private ImageView imgSinal04Baixo;
  @FXML
  private ImageView imgSinal05Alto;
  @FXML
  private ImageView imgSinal05Baixo;
  @FXML
  private ImageView imgSinal06Alto;
  @FXML
  private ImageView imgSinal06Baixo;
  @FXML
  private ImageView imgSinal07Alto;
  @FXML
  private ImageView imgSinal07Baixo;
  @FXML
  private ImageView imgSinal08Alto;
  @FXML
  private ImageView imgSinal08Baixo;

  @FXML
  private RadioButton radioButtonCodificacaoBinaria;
  @FXML
  private RadioButton radioButtonManchester;
  @FXML
  private RadioButton radioButtonManchesterDiferencial;
  
  @FXML
  private TextArea textAreaReceptor;
  @FXML
  private TextArea textAreaTransmissor;
  
  ToggleGroup grupoRadioTipoDeCodificacao;
  
  AplicacaoTransmissora aplicacaoTransmissora;
  
  int tipoDeCodificacao = -1;
  
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    grupoRadioTipoDeCodificacao = new ToggleGroup();
    radioButtonCodificacaoBinaria.setToggleGroup(grupoRadioTipoDeCodificacao);
    radioButtonManchester.setToggleGroup(grupoRadioTipoDeCodificacao);
    radioButtonManchesterDiferencial.setToggleGroup(grupoRadioTipoDeCodificacao);


  }
  
  @FXML
  void handleButtonEnviar(ActionEvent event) {
    if(buttonEnviar.getText().equals("Enviar")) {
      if(grupoRadioTipoDeCodificacao.getSelectedToggle() != null && !textAreaTransmissor.getText().equals("")) {
        labelAviso.setVisible(false);
        imageDemarcacaoSinais.setVisible(true);
        buttonEnviar.setText("Reiniciar");
        
        aplicacaoTransmissora = new AplicacaoTransmissora();
        aplicacaoTransmissora.enviarDado(textAreaTransmissor.getText(), tipoDeCodificacao);
      } else {
        labelAviso.setVisible(true);
      }
    } else { // reiniciar
      buttonEnviar.setText("Enviar");
      textAreaTransmissor.setText("");
      imageDemarcacaoSinais.setVisible(false);
      grupoRadioTipoDeCodificacao.selectToggle(null);
    }

  }
  
  @FXML
  void handleButtonCodificacaoBinaria(ActionEvent event) {
    tipoDeCodificacao = 0;
  }

  @FXML
  void handleButtonManchester(ActionEvent event) {
    tipoDeCodificacao = 1;
  }

  @FXML
  void handleButtonManchesterDiferencial(ActionEvent event) {
    tipoDeCodificacao = 2;
  }
  
}
