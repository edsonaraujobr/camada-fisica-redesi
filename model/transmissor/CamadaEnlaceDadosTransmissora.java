package model.transmissor;

public class CamadaEnlaceDadosTransmissora {
  
  CamadaFisicaTransmissora camadaFisicaTransmissora;

  public void enviarDado(int quadro [], int tipoDeCodificacao, int tipoDeEnquadramento) {
    enquadramento(quadro, tipoDeCodificacao);
    camadaFisicaTransmissora.enviarDado(quadro, tipoDeCodificacao, tipoDeEnquadramento);
  }
  
  public void enquadramento(int quadro [], int tipoDeEnquadramento) {
    int quadroEnquadrado [];
    switch (tipoDeEnquadramento) {
      case 0 : //contagem de caracteres
        quadroEnquadrado = contagemDeCaracteres(quadro);
        break;
      case 1 : //insercao de bytes
        quadroEnquadrado = insercaoDeBytes(quadro);
        break;
      case 2 : //insercao de bits
        quadroEnquadrado = insercaoDeBits(quadro);
        break;
      case 3 : //violacao da camada fisica
        quadroEnquadrado = violacaoCamadaFisica(quadro);
        break;
    }//fim do switch/case
  }
  
  public int [] contagemDeCaracteres (int quadro []) {
  }
  
  public int [] insercaoDeBytes (int quadro []) {
  
  }
  
  public int [] insercaoDeBits (int quadro []) {
    
  }
  
  public int [] violacaoCamadaFisica(int quadro []) {
    
  }
  
  public void controleDeErro(int quadro []) {
    // nao sera implementado neste momento
  }
  
  public void controleDeFluxo(int quadro []) {
    // nao sera implementado neste momento
  }
  
  public void setCamadaFisicaTransmissora(CamadaFisicaTransmissora camadaFisicaTransmissora) {
    this.camadaFisicaTransmissora = camadaFisicaTransmissora;
  }
}

