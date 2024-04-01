/*package model.transmissor;
import model.MeioDeComunicacao;

public class CamadaFisicaTransmissora {
  MeioDeComunicacao meioDeComunicacao = new MeioDeComunicacao();
  
  public void enviarDado(String quadro){
    int tipoDeCodificacao = 0;
    int fluxoBrutoDeBits[];
    int ex [] = {3,2};
    
    switch(tipoDeCodificacao) {
      case 0: //codificao binaria
        fluxoBrutoDeBits = camadaFisicaTransmissoraCodificacaoBinaria (ex);
        break;
        
      case 1: //codificacao manchester
        fluxoBrutoDeBits = camadaFisicaTransmissoraCodificacaoManchester (ex);
        break;
        
      case 2:  //codificacao manchester diferencial
        fluxoBrutoDeBits = camadaFisicaTransmissoraCodificacaoManchesterDiferencial (ex);
        break;
        
    }
    meioDeComunicacao.enviarDado(quadro);
  }
  
  public int[] camadaFisicaTransmissoraCodificacaoBinaria (int quadro []) {
    // implementar o algoritmo
    int ex [] = {3,2};
    return ex;
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
  
}*/
