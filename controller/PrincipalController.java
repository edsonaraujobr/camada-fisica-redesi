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
import model.transmissor.CamadaAplicacaoTransmissora;
import model.transmissor.CamadaFisicaTransmissora;

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
  
  CamadaAplicacaoTransmissora camadaAplicacaoTransmissora;
  
  CamadaFisicaTransmissora camadaFisicaTransmissora;
  
  int tipoDeCodificacao = -1;
  
   ImageView arrayBitsAltos[] = {imgSinal01Alto, imgSinal02Alto, imgSinal03Alto, imgSinal04Alto, imgSinal05Alto, imgSinal06Alto, imgSinal07Alto, imgSinal08Alto};
   ImageView arrayBitsBaixos[] = {imgSinal01Baixo, imgSinal02Baixo, imgSinal03Baixo, imgSinal04Baixo, imgSinal05Baixo, imgSinal06Baixo, imgSinal07Baixo, imgSinal08Baixo};
  
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    grupoRadioTipoDeCodificacao = new ToggleGroup();
    radioButtonCodificacaoBinaria.setToggleGroup(grupoRadioTipoDeCodificacao);
    radioButtonManchester.setToggleGroup(grupoRadioTipoDeCodificacao);
    radioButtonManchesterDiferencial.setToggleGroup(grupoRadioTipoDeCodificacao);

    instanciarCamadas();
   
  }
  
  public void instanciarCamadas() {
    aplicacaoTransmissora = new AplicacaoTransmissora();
    camadaAplicacaoTransmissora = new CamadaAplicacaoTransmissora();
    aplicacaoTransmissora.setCamadaAplicacaoTransmissora(camadaAplicacaoTransmissora);
    
    camadaFisicaTransmissora = new CamadaFisicaTransmissora();
    camadaAplicacaoTransmissora.setCamadaFisicaTransmissora(camadaFisicaTransmissora);

    camadaFisicaTransmissora.setPrincipalController(this); // setar o controle para esse proprio.
  }
  
  @FXML
  void handleButtonEnviar(ActionEvent event) {
    if(buttonEnviar.getText().equals("Enviar")) {
      if(grupoRadioTipoDeCodificacao.getSelectedToggle() != null && !textAreaTransmissor.getText().equals("")) {
        labelAviso.setVisible(false);
        imageDemarcacaoSinais.setVisible(true);
        buttonEnviar.setText("Reiniciar");
        
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
  
  public void exibirSinaisBinarios(int quadro[]){
    //transformarBitsEmString(quadro);
    //arrayBitsBaixos[0].setVisible(true);
    //imgSinal01Alto.setVisible(true);
    imgSinal01Alto.setVisible(true);
  }
  
  public void transformarBitsEmString(int quadro[]){
    int displayMask = 1 << 31;
    
    /*ImageView arrayResultado[] = new ImageView [8];
    int index = 1;*/
    
    for(int j = 0; j < quadro.length; j++) {
      for (int i = 1; i <= 32; i++) {
        
        if((quadro[j] & displayMask) == 0) {
          arrayBitsBaixos[0].setVisible(true);
          /*arrayResultado[index] = arrayBitsBaixos[0];
          index++;*/
          
        } else {
          
          arrayBitsAltos[0].setVisible(true);
        }

        quadro[j] <<= 1;
      }
    }

  }
  
}
