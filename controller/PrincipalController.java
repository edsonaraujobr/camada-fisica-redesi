/* ***************************************************************
* Autor............: Edson Araujo de Souza Neto
* Matricula........: 202210169
* Inicio...........: 01/04/2024
* Ultima alteracao.: 05/05/2024
* Nome.............: principalController
* Funcao...........: manipula a gui do usuario, instancia as camadas e executa as mesmas.
*************************************************************** */

package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import model.receptor.*;
import model.transmissor.*;
import model.MeioDeComunicacao;

public class PrincipalController implements Initializable {
  
  @FXML
  private ChoiceBox<String> boxTipoCodificacao;
  @FXML
  private ChoiceBox<String> boxTipoEnquadramento;

  @FXML
  private Label labelAviso;
  @FXML
  private Label labelAvisoEnquadramento;      
  
  @FXML
  private Button buttonEnviar;

  @FXML
  private ImageView imageDemarcacaoSinais;
  
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
  private ImageView imgManchesterAltoBaixo01;
  @FXML
  private ImageView imgManchesterAltoBaixo02;
  @FXML
  private ImageView imgManchesterAltoBaixo03;
  @FXML
  private ImageView imgManchesterAltoBaixo04;
  @FXML
  private ImageView imgManchesterAltoBaixo05;
  @FXML
  private ImageView imgManchesterAltoBaixo06;
  @FXML
  private ImageView imgManchesterAltoBaixo07;
  @FXML
  private ImageView imgManchesterAltoBaixo08;

  @FXML
  private ImageView imgManchesterBaixoAlto01;
  @FXML
  private ImageView imgManchesterBaixoAlto02;
  @FXML
  private ImageView imgManchesterBaixoAlto03;
  @FXML
  private ImageView imgManchesterBaixoAlto04;
  @FXML
  private ImageView imgManchesterBaixoAlto05;
  @FXML
  private ImageView imgManchesterBaixoAlto06;
  @FXML
  private ImageView imgManchesterBaixoAlto07;
  @FXML
  private ImageView imgManchesterBaixoAlto08;
  
  @FXML
  private TextArea textAreaReceptor;
  @FXML
  private TextArea textAreaTransmissor;
  
  private AplicacaoTransmissora aplicacaoTransmissora;
  private CamadaAplicacaoTransmissora camadaAplicacaoTransmissora;
  private CamadaEnlaceDadosTransmissora camadaEnlaceDadosTransmissora;
  private CamadaFisicaTransmissora camadaFisicaTransmissora;
  
  private MeioDeComunicacao meioDeComunicacao;
  
  private CamadaFisicaReceptora camadaFisicaReceptora;
  private CamadaAplicacaoReceptora camadaAplicacaoReceptora;
  private CamadaEnlaceDadosReceptora camadaEnlaceDadosReceptora;
  private AplicacaoReceptora aplicacaoReceptora;
  
  private int tipoDeCodificacao = -1;
  private int tipoDeEnquadramento = -1;
  
  private String mensagem = "";
  
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    boxTipoCodificacao.setItems(FXCollections.observableArrayList("Binaria","Manchester","Manchester Diferencial"));
    boxTipoEnquadramento.setItems(FXCollections.observableArrayList("Contagem de Caracteres","Inserção de bytes", "Inserção de bits", "Violação de codificação da camada física"));
    boxTipoCodificacao.getSelectionModel().selectFirst();
    boxTipoEnquadramento.getSelectionModel().selectFirst();
    
    buttonEnviar.setOnMouseEntered(event -> {
      buttonEnviar.setStyle("-fx-background-color: #16782e");
      buttonEnviar.setCursor(javafx.scene.Cursor.HAND);
    });

    buttonEnviar.setOnMouseExited(event -> {
      buttonEnviar.setStyle("-fx-background-color: #1cb241");
      buttonEnviar.setCursor(javafx.scene.Cursor.DEFAULT);
    });
    
    boxTipoCodificacao.setOnMouseEntered(event -> {
      boxTipoCodificacao.setCursor(javafx.scene.Cursor.HAND);
    });
    boxTipoCodificacao.setOnMouseExited(event -> {
      boxTipoCodificacao.setCursor(javafx.scene.Cursor.DEFAULT);
    });
    
    boxTipoEnquadramento.setOnMouseEntered(event -> {
      boxTipoEnquadramento.setCursor(javafx.scene.Cursor.HAND);
    });
    boxTipoEnquadramento.setOnMouseExited(event -> {
       boxTipoEnquadramento.setCursor(javafx.scene.Cursor.DEFAULT);
    });
   
    
    instanciarCamadas();
   
  } // fim metodoInitialize
  

  public void instanciarCamadas() {
    aplicacaoTransmissora = new AplicacaoTransmissora();
    camadaAplicacaoTransmissora = new CamadaAplicacaoTransmissora();
    aplicacaoTransmissora.setCamadaAplicacaoTransmissora(camadaAplicacaoTransmissora);
  
    camadaEnlaceDadosTransmissora = new CamadaEnlaceDadosTransmissora();
    camadaAplicacaoTransmissora.setCamadaEnlaceDadosTransmissora(camadaEnlaceDadosTransmissora);

    camadaFisicaTransmissora = new CamadaFisicaTransmissora();
    camadaEnlaceDadosTransmissora.setCamadaFisicaTransmissora(camadaFisicaTransmissora);
    
    camadaFisicaTransmissora.setPrincipalController(this); // setar o controle para esse proprio.
    meioDeComunicacao = new MeioDeComunicacao();
    camadaFisicaTransmissora.setMeioDeComunicacao(meioDeComunicacao);
    
    camadaFisicaReceptora = new CamadaFisicaReceptora();
    meioDeComunicacao.setCamadaFisicaReceptora(camadaFisicaReceptora);
    
    camadaEnlaceDadosReceptora = new CamadaEnlaceDadosReceptora();
    camadaFisicaReceptora.setCamadaEnlaceDadosReceptora(camadaEnlaceDadosReceptora);
    
    camadaAplicacaoReceptora = new CamadaAplicacaoReceptora();
    camadaEnlaceDadosReceptora.setCamadaAplicacaoReceptora(camadaAplicacaoReceptora);
    aplicacaoReceptora = new AplicacaoReceptora();
    
    camadaAplicacaoReceptora.setAplicacaoReceptora(aplicacaoReceptora);
    
    aplicacaoReceptora.setPrincipalController(this);
  } // fim metodo instaciarCamadas
  
  @FXML
  void handleButtonEnviar(ActionEvent event) {
    if(buttonEnviar.getText().equals("Enviar")) {
      if(boxTipoCodificacao.getValue() != null && boxTipoEnquadramento.getValue() != null  && !textAreaTransmissor.getText().equals("")) {
        if(boxTipoCodificacao.getValue().equals("Binaria") && boxTipoEnquadramento.getValue().equals("Violação de codificação da camada física")) {
          labelAvisoEnquadramento.setVisible(true);
          labelAviso.setVisible(false);
        } else {
          textAreaReceptor.setText("");
          labelAvisoEnquadramento.setVisible(false);
          labelAviso.setVisible(false);
          imageDemarcacaoSinais.setVisible(true);
          buttonEnviar.setText("Aguarde");
          buttonEnviar.setStyle("-fx-background-color:  #E1AF00"); // button fica amarelo
          textAreaTransmissor.setEditable(false); // nao permite editar
          boxTipoCodificacao.setDisable(true);
          boxTipoEnquadramento.setDisable(true);   
          
          aplicacaoTransmissora.enviarDado(textAreaTransmissor.getText(), tipoDeCodificacao, tipoDeEnquadramento);
       
        }

      } else { // nao digitou
        labelAvisoEnquadramento.setVisible(false);
        labelAviso.setVisible(true);
      }
    } 
  } // fim metodo handleButtonEnviar
  
  public static void limparTerminal() {
    // Verifica se o sistema operacional é Windows
    if (System.getProperty("os.name").contains("Windows")) {
      // Se for, utiliza o comando "cls"
      try {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else {
      // Se não for Windows, utiliza o comando "clear"
      System.out.print("\033[H\033[2J");
      System.out.flush();
    }
  }
  
  public void possibilitarReiniciar() {
    Platform.runLater(()-> {
      textAreaReceptor.setText(mensagem);
      limparTerminal();
      instanciarCamadas();
      buttonEnviar.setText("Enviar");
      textAreaTransmissor.setText("");
      imageDemarcacaoSinais.setVisible(false);
      buttonEnviar.setStyle("-fx-background-color:  #1cb241"); // button fica verde
      textAreaTransmissor.setEditable(true); // permite editar
      boxTipoCodificacao.setDisable(false);
      boxTipoEnquadramento.setDisable(false);
      
    });
  }
  
  
  @FXML
  public void handleTipoCodificacao(ActionEvent event ) {
    if(boxTipoCodificacao.getValue() != null) {
      switch(boxTipoCodificacao.getValue()) {
        case "Binaria":
          tipoDeCodificacao = 0;
          break;
        case "Manchester":
          tipoDeCodificacao = 1;
          break;
        case "Manchester Diferencial":
          tipoDeCodificacao = 2;
          break;
      }
    }
  }

  @FXML
  public void handleTipoEnquadramento(ActionEvent event ) {
    if(boxTipoEnquadramento.getValue() != null) {
      switch(boxTipoEnquadramento.getValue()) {
        case "Contagem de Caracteres":
          tipoDeEnquadramento = 0;
          break;
        case "Inserção de bytes":
          tipoDeEnquadramento = 1;
          break;
        case "Inserção de bits":
          tipoDeEnquadramento = 2;
          break;
        case "Violação de codificação da camada física":
          tipoDeEnquadramento = 3;
          break;
      }
    }
  }
  
  public void setMensagemReceptor(String mensagem){
    this.mensagem = mensagem;
  }
  
  public void exibirSinaisBinarios(int quadro[]){
    
    ImageView arraySinaisAltos[] = {imgSinal01Alto,imgSinal02Alto,imgSinal03Alto, imgSinal04Alto, imgSinal05Alto, imgSinal06Alto, imgSinal07Alto, imgSinal08Alto};
    ImageView arraySinaisBaixos[] = {imgSinal01Baixo,imgSinal02Baixo,imgSinal03Baixo, imgSinal04Baixo, imgSinal05Baixo, imgSinal06Baixo, imgSinal07Baixo, imgSinal08Baixo};
    
    List<Integer> listSinaisResultado = new ArrayList<>();
    int[] copiaQuadro = Arrays.copyOf(quadro, quadro.length);   
    
    new Thread(() -> {
      int contadorBitsNull = 0;
      boolean sairLoopExterno = false;
      
      for (int k = 0; k < copiaQuadro.length && !sairLoopExterno; k++) {
        int displayMask = 1 << 31; // 10000000 00000000 00000000 0000000
        
        for (int i = 1; i <= 32 ; i++) { // da direita para esquerda.
                    
          if(i%8 == 0 && contadorBitsNull == 7) {
            sairLoopExterno = true;
            break;
          } else if(i%8 == 0) {
            contadorBitsNull = 0;
          }

          if((copiaQuadro[k] & displayMask) == 0) {
            listSinaisResultado.add(0, 0);
            ++contadorBitsNull;
          } else {
            listSinaisResultado.add(0, 1);
          }
          copiaQuadro[k] <<= 1;    

          for (int value = 0; value <= listSinaisResultado.size() - 1; value++) {
            if(listSinaisResultado.get(value) == 0) {
              arraySinaisBaixos[value].setVisible(true);
              arraySinaisAltos[value].setVisible(false);
            } else {
              arraySinaisAltos[value].setVisible(true);
              arraySinaisBaixos[value].setVisible(false);
            }
          }
        
          try {
             Thread.sleep(100);
          } catch(InterruptedException e) {}
        
          if (listSinaisResultado.size() - 1 == 7) {
            listSinaisResultado.remove(listSinaisResultado.size()-1); // remover o ultimo elemento.
          }
        } // fim for dos 32 bits
      }// fim for de quadro

      for(int k = 0; k < 8; k++) {
        for (int i = arraySinaisAltos.length - 1; i > 0; i--) {
          arraySinaisAltos[i].setVisible(arraySinaisAltos[i - 1].isVisible());
          arraySinaisBaixos[i].setVisible(arraySinaisBaixos[i - 1].isVisible());
        }
        try {
          Thread.sleep(100);
        } catch(InterruptedException e) {}
        // Desaparecimento da primeira imagem
        arraySinaisAltos[k].setVisible(false);
        arraySinaisBaixos[k].setVisible(false);        
      }
      possibilitarReiniciar();
    }).start();

  } // fim do metodo exibirSinaisBinarios
  
  public void exibirSinaisManchester(int quadro[]) {
    ImageView arraySinaisAltos[]   = { imgManchesterAltoBaixo01, imgManchesterAltoBaixo02, imgManchesterAltoBaixo03, imgManchesterAltoBaixo04, imgManchesterAltoBaixo05, imgManchesterAltoBaixo06, imgManchesterAltoBaixo07, imgManchesterAltoBaixo08};
    ImageView arraySinaisBaixos[]   = { imgManchesterBaixoAlto01, imgManchesterBaixoAlto02, imgManchesterBaixoAlto03, imgManchesterBaixoAlto04, imgManchesterBaixoAlto05, imgManchesterBaixoAlto06, imgManchesterBaixoAlto07, imgManchesterBaixoAlto08};
  
    List<Integer> listSinaisResultado = new ArrayList<>();
    int[] copiaQuadro = Arrays.copyOf(quadro, quadro.length);  
  
    new Thread(() -> {
      for (int k = 0; k < copiaQuadro.length; k++) {
        int displayMask = 1 << 31; // 10000000 00000000 00000000 0000000
        
        for (int i = 1; i <= 16 ; i++) { // da direita para esquerda.
          if((copiaQuadro[k] & displayMask) == 0) {
            listSinaisResultado.add(0, 0);
          } else {
            listSinaisResultado.add(0, 1);
          }
          copiaQuadro[k] <<= 2;    
        
          for (int value = 0; value <= listSinaisResultado.size() - 1; value++) {
            if(listSinaisResultado.get(value) == 0) {
              arraySinaisBaixos[value].setVisible(true);
              arraySinaisAltos[value].setVisible(false);
            } else {
              arraySinaisAltos[value].setVisible(true);
              arraySinaisBaixos[value].setVisible(false);
            }
          }
        
          try {
            Thread.sleep(100);
          } catch(InterruptedException e) {}
        
          if (listSinaisResultado.size() - 1 == 7) {
            listSinaisResultado.remove(listSinaisResultado.size()-1); // remover o ultimo elemento.
          }
        } // fim for dos 32 bits
      }// fim for de quadro

      for(int k = 0; k < 8; k++) {
        for (int i = arraySinaisAltos.length - 1; i > 0; i--) {
          arraySinaisAltos[i].setVisible(arraySinaisAltos[i - 1].isVisible());
          arraySinaisBaixos[i].setVisible(arraySinaisBaixos[i - 1].isVisible());
        }
        try {
          Thread.sleep(100);
        } catch(InterruptedException e) {}
        // Desaparecimento da primeira imagem
        arraySinaisAltos[k].setVisible(false);
        arraySinaisBaixos[k].setVisible(false);        
      }
      possibilitarReiniciar();
    }).start();

  }
  
  public void exibirSinaisManchesterDiferencial(int quadro[]) {
    ImageView arraySinaisAltos[]   = { imgManchesterAltoBaixo01, imgManchesterAltoBaixo02, imgManchesterAltoBaixo03, imgManchesterAltoBaixo04, imgManchesterAltoBaixo05, imgManchesterAltoBaixo06, imgManchesterAltoBaixo07, imgManchesterAltoBaixo08};
    ImageView arraySinaisBaixos[]   = { imgManchesterBaixoAlto01, imgManchesterBaixoAlto02, imgManchesterBaixoAlto03, imgManchesterBaixoAlto04, imgManchesterBaixoAlto05, imgManchesterBaixoAlto06, imgManchesterBaixoAlto07, imgManchesterBaixoAlto08};
  
    List<Integer> listSinaisResultado = new ArrayList<>();
    int[] copiaQuadro = Arrays.copyOf(quadro, quadro.length);  
    
    new Thread(() -> {
      int ultimoBitLido = -1;
      int displayMask = 1 << 31; // 10000000 00000000 00000000 0000000

      for (int k = 0; k < copiaQuadro.length; k++) {

        
        if(k == 0) { // primeira iteracao
          if((copiaQuadro[0] & displayMask) == 0) {
            listSinaisResultado.add(0, 0); 
            ultimoBitLido = 1;
            arraySinaisBaixos[0].setVisible(true);
          } else {
            listSinaisResultado.add(0, 1);  
            ultimoBitLido = 0;
            arraySinaisBaixos[0].setVisible(true);
          }     
          
          try {
            Thread.sleep(100);
          } catch(InterruptedException e) {}
          copiaQuadro[0] <<= 2;  
          
          for (int i = 2; i < 32 ; i+=2) { 
            
            if((copiaQuadro[k] & displayMask) == 0) {
              if(ultimoBitLido == 1) { // houve transicao (10)
                listSinaisResultado.add(0,0);
                ultimoBitLido = 1;            
              } else { // nao houve transicao (00)         
                listSinaisResultado.add(0,0);  
                ultimoBitLido = 1;   
              }
            } else {
              if(ultimoBitLido == 1) { // nao houve transicao (11)
                listSinaisResultado.add(0,1);  
                ultimoBitLido = 0;
              } else { // houve transicao (01)
                ultimoBitLido = 0;
                listSinaisResultado.add(0,1);
              }
            }
            copiaQuadro[k] <<= 2;   
        

            for (int value = 0; value <= listSinaisResultado.size() - 1; value++) {
              if(listSinaisResultado.get(value) == 0) {
                arraySinaisBaixos[value].setVisible(true);
                arraySinaisAltos[value].setVisible(false);
              } else {
                arraySinaisAltos[value].setVisible(true);
                arraySinaisBaixos[value].setVisible(false);
              }
            }

            try {
              Thread.sleep(100);
            } catch(InterruptedException e) {}

            if (listSinaisResultado.size() - 1 == 7) {
              listSinaisResultado.remove(listSinaisResultado.size()-1); // remover o ultimo elemento.
            }
          } // fim for dos 32 bits          
          
        } else { // nao eh a primeira iteracao
          for (int i = 0; i < 32 ; i+=2) { 
            
            if((copiaQuadro[k] & displayMask) == 0) {
              if(ultimoBitLido == 1) { // houve transicao (10)
                listSinaisResultado.add(0,0);
                ultimoBitLido = 1;            
              } else { // nao houve transicao (00)         
                listSinaisResultado.add(0,0);  
                ultimoBitLido = 1;   
              }
            } else {
              if(ultimoBitLido == 1) { // nao houve transicao (11)
                listSinaisResultado.add(0,1);  
                ultimoBitLido = 0;
              } else { // houve transicao (01)
                ultimoBitLido = 0;
                listSinaisResultado.add(0,1);
              }
            }
            copiaQuadro[k] <<= 2;   
        

            for (int value = 0; value <= listSinaisResultado.size() - 1; value++) {
              if(listSinaisResultado.get(value) == 0) {
                arraySinaisBaixos[value].setVisible(true);
                arraySinaisAltos[value].setVisible(false);
              } else {
                arraySinaisAltos[value].setVisible(true);
                arraySinaisBaixos[value].setVisible(false);
              }
            }

            try {
              Thread.sleep(100);
            } catch(InterruptedException e) {}

            if (listSinaisResultado.size() - 1 == 7) {
              listSinaisResultado.remove(listSinaisResultado.size()-1); // remover o ultimo elemento.
            }
          } // fim for dos 32 bits              
        }
 
      }// fim for de quadro

      for(int k = 0; k < 8; k++) {
        for (int i = arraySinaisAltos.length - 1; i > 0; i--) {
          arraySinaisAltos[i].setVisible(arraySinaisAltos[i - 1].isVisible());
          arraySinaisBaixos[i].setVisible(arraySinaisBaixos[i - 1].isVisible());
        }
        try {
          Thread.sleep(100);
        } catch(InterruptedException e) {}
        // Desaparecimento da primeira imagem
        arraySinaisAltos[k].setVisible(false);
        arraySinaisBaixos[k].setVisible(false);        
      }
      possibilitarReiniciar();
    }).start();    
  }
  
}
