package model.transmissor;
import model.MeioDeComunicacao;
import controller.PrincipalController;

public class CamadaFisicaTransmissora {
  MeioDeComunicacao meioDeComunicacao;
  PrincipalController principalController;
  
  public void enviarDado(int quadro[], int tipoDeCodificacao){
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
    meioDeComunicacao.enviarDado(fluxoBrutoDeBits, tipoDeCodificacao);
  }
  
  public int[] camadaFisicaTransmissoraCodificacaoBinaria (int quadro []) {
    // implementar o algoritmo
    //principalController.exibirSinaisBinarios(quadro);
    return quadro; // neste caso ja esta na codificacao binaria
  }
  
  public int[] camadaFisicaTransmissoraCodificacaoManchester (int quadro []) {
    // implementar o algoritmo

    return quadro;
  }
  
   public int[] camadaFisicaTransmissoraCodificacaoManchesterDiferencial (int quadro []) {
    // implementar o algoritmo

    return quadro;
  }
   
   public void setPrincipalController(PrincipalController principalController){
     this.principalController = principalController;
   }
   
   public void setMeioDeComunicacao(MeioDeComunicacao meioDeComunicacao){
     this.meioDeComunicacao = meioDeComunicacao;
   }
  
}
