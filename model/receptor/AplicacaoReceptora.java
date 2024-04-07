/* ***************************************************************
* Autor............: Edson Araujo de Souza Neto
* Matricula........: 202210169
* Inicio...........: 01/04/2024
* Ultima alteracao.: 07/04/2024
* Nome.............: AplicacaoReceptora
* Funcao...........: Exibe o resultado da mensagem
*************************************************************** */

package model.receptor;
import controller.PrincipalController;

public class AplicacaoReceptora {
  PrincipalController principalController;
  
  public void receberDado(String mensagem) {
    System.out.println(mensagem);
    principalController.exibirMensagemReceptor(mensagem);
  }
  
  public void setPrincipalController(PrincipalController principalController){
    this.principalController = principalController;
  }
  
}
