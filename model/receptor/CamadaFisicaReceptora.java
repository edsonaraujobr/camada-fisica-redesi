package model.receptor;

public class CamadaFisicaReceptora {
  CamadaAplicacaoReceptora camadaAplicacaoReceptora;
  
  public void receberDado(int quadro [], int tipoDeCodificacao){
    int fluxoBrutoDeBits[] = {};
    
    switch(tipoDeCodificacao) {
      case 0: //codificao binaria
        fluxoBrutoDeBits = camadaFisicaTransmissoraCodificacaoBinaria (quadro);
        break;
        
      case 1: //codificacao manchester
        fluxoBrutoDeBits = camadaFisicaTransmissoraCodificacaoManchester (quadro);
        break;
        
      case 2:  //codificacao manchester diferencial
        fluxoBrutoDeBits = camadaFisicaTransmissoraCodificacaoManchesterDiferencial (quadro);
        break;
        
    }
    System.out.println("Passou pela camadaFisicaReceptora");
    camadaAplicacaoReceptora.receberDado(fluxoBrutoDeBits, tipoDeCodificacao);
  }
  
  public void setAplicacaoReceptora(CamadaAplicacaoReceptora camadaAplicacaoReceptora){
    this.camadaAplicacaoReceptora = camadaAplicacaoReceptora;
  }
  
  public int[] camadaFisicaTransmissoraCodificacaoBinaria (int quadro []) {

    return quadro;
  }
  
  public int[] camadaFisicaTransmissoraCodificacaoManchester (int quadro []) {
    // implementar o algoritmo
    int ex [] = {3,2};
    return ex;
  }
  
   public int[] camadaFisicaTransmissoraCodificacaoManchesterDiferencial (int quadro []) {
    // implementar o algoritmo
    int ex [] = {3,2};
    return ex;
  }
}
