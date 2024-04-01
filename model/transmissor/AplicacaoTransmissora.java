package model.transmissor;

public class AplicacaoTransmissora {
  CamadaAplicacaoTransmissora camadaAplicacaoTransmissora = new CamadaAplicacaoTransmissora();

  public void enviarDado (String mensagem, int tipoDeCodificacao) {
    camadaAplicacaoTransmissora.enviarDado(mensagem, tipoDeCodificacao);
  }
  
}
