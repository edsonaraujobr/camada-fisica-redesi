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
