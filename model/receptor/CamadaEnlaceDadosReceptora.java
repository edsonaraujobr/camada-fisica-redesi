package model.receptor;

public class CamadaEnlaceDadosReceptora {
  
  CamadaAplicacaoReceptora camadaAplicacaoReceptora;

  public void receberDado(int quadro [], int tipoDeCodificacao) {
    /*enquadramento(quadro, tipoDeCodificacao);
    controleDeErro(quadro);
    controleDeFluxo(quadro);*/
    camadaAplicacaoReceptora.receberDado(quadro, tipoDeCodificacao);
  }
  
  /*public void enquadramento(int quadro [], int tipoDeEnquadramento) {
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
    
  }
  
  public void controleDeFluxo(int quadro []) {
    
  }*/
  
  public void setCamadaAplicacaoReceptora(CamadaAplicacaoReceptora camadaAplicacaoReceptora) {
    this.camadaAplicacaoReceptora = camadaAplicacaoReceptora;
  }
}

