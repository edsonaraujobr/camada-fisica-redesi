/* ***************************************************************
* Autor............: Edson Araujo de Souza Neto
* Matricula........: 202210169
* Inicio...........: 01/04/2024
* Ultima alteracao.: 07/04/2024
* Nome.............: AplicacaoTransmissora
* Funcao...........: Transfere o resultado da mensagem para a camada aplicacao transmissora.
*************************************************************** */


package model.transmissor;

public class AplicacaoTransmissora {
  CamadaAplicacaoTransmissora camadaAplicacaoTransmissora;

  public void enviarDado (String mensagem, int tipoDeCodificacao) {
    camadaAplicacaoTransmissora.enviarDado(mensagem, tipoDeCodificacao);
  }
  
  public void setCamadaAplicacaoTransmissora(CamadaAplicacaoTransmissora camadaAplicacaoTransmissora) {
    this.camadaAplicacaoTransmissora = camadaAplicacaoTransmissora;
  }
}
