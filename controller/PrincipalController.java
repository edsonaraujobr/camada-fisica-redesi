package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
import model.receptor.*;
import model.transmissor.*;
import model.MeioDeComunicacao;

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
  
  MeioDeComunicacao meioDeComunicacao;
  
  CamadaFisicaReceptora camadaFisicaReceptora;
  CamadaAplicacaoReceptora camadaAplicacaoReceptora;
  AplicacaoReceptora aplicacaoReceptora;
  
  int tipoDeCodificacao = -1;
  
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
    
    meioDeComunicacao = new MeioDeComunicacao();
    
    camadaFisicaTransmissora.setMeioDeComunicacao(meioDeComunicacao);
    
    camadaFisicaReceptora = new CamadaFisicaReceptora();
    
    meioDeComunicacao.setCamadaFisicaReceptora(camadaFisicaReceptora);
    
    camadaAplicacaoReceptora = new CamadaAplicacaoReceptora();
    
    camadaFisicaReceptora.setAplicacaoReceptora(camadaAplicacaoReceptora);
    
    aplicacaoReceptora = new AplicacaoReceptora();
    
    camadaAplicacaoReceptora.setAplicacaoReceptora(aplicacaoReceptora);
    
    aplicacaoReceptora.setPrincipalController(this);
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
  
  public void exibirMensagemReceptor(String mensagem){
    textAreaReceptor.setText(mensagem);
  }
  
  public void exibirSinaisBinarios(int quadro[]){
    int displayMask = 1 << 31;
    
    ImageView arraySinaisAltos[] = {imgSinal01Alto,imgSinal02Alto,imgSinal03Alto, imgSinal04Alto, imgSinal05Alto, imgSinal06Alto, imgSinal07Alto, imgSinal08Alto};
    ImageView arraySinaisBaixos[] = {imgSinal01Baixo,imgSinal02Baixo,imgSinal03Baixo, imgSinal04Baixo, imgSinal05Baixo, imgSinal06Baixo, imgSinal07Baixo, imgSinal08Baixo};

    List<Integer> listSinaisResultado = new ArrayList<>();
    
    new Thread(() -> {
      for(int j = 0; j < quadro.length; j++) {
        for (int i = 1; i <= 32; i++) {

        if((quadro[j] & displayMask) == 0) {
          listSinaisResultado.add(0, 0);
        } else {
          listSinaisResultado.add(0, 1);
        }
        
        for (int k = 0; k <= listSinaisResultado.size() - 1; k++) {
          if(listSinaisResultado.get(k) == 0) {
            arraySinaisBaixos[k].setVisible(true);
            arraySinaisAltos[k].setVisible(false);
          } else {
            arraySinaisAltos[k].setVisible(true);
            arraySinaisBaixos[k].setVisible(false);
          }
        }
        
        try {
          Thread.sleep(100);
        } catch(InterruptedException e) {}
        
        if (listSinaisResultado.size() - 1 == 7) {
          listSinaisResultado.remove(listSinaisResultado.size()-1); // remover o ultimo elemento.
        }

        quadro[j] <<= 1;
        } // fim for dos 32 bits
      }// fim for de quadro
      while(!listSinaisResultado.isEmpty()) {
        if(listSinaisResultado.get(listSinaisResultado.size() - 2) == 0) { // penultimo
          arraySinaisBaixos[7].setVisible(true);
          arraySinaisAltos[7].setVisible(false);
        } else {
          arraySinaisBaixos[7].setVisible(false);
          arraySinaisAltos[7].setVisible(true);
      
        }
        listSinaisResultado.remove(listSinaisResultado.size() - 1); // remove o ultimo   

      }


    }).start();

  } // fim do metodo exibirSinaisBinarios
  
}
