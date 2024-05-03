/* ***************************************************************
* Autor............: Edson Araujo de Souza Neto
* Matricula........: 202210169
* Inicio...........: 01/04/2024
* Ultima alteracao.: 07/04/2024
* Nome.............: CamadaFisicaTransmissora
* Funcao...........: Move o fluxo de bits para o meio de comunicacao de acordo com a codificacao
*************************************************************** */

package model.transmissor;
import model.MeioDeComunicacao;
import controller.PrincipalController;

public class CamadaFisicaTransmissora {
  MeioDeComunicacao meioDeComunicacao;
  PrincipalController principalController;

  public void enviarDado(int quadro[], int tipoDeCodificacao, int tipoDeEnquadramento){
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
    
    System.out.println("Na camada Fisica Transmissora");
    for (int i = 0; i < fluxoBrutoDeBits.length; i++) {
      imprimirBits(fluxoBrutoDeBits[i]);
      System.out.println();
    }
    meioDeComunicacao.enviarDado(fluxoBrutoDeBits, tipoDeCodificacao, tipoDeEnquadramento);
  }
  
  public int[] camadaFisicaTransmissoraCodificacaoBinaria (int quadro []) {
    //principalController.exibirSinaisBinarios(quadro);
    return quadro; 
  }
  
  public int[] camadaFisicaTransmissoraCodificacaoManchester (int quadro []) {
    int novoQuadro[] = new int [quadro.length * 2]; // Usamos o dobro de espaco, pois em cada quadro nao cabe agora 4 caracteres e sim 2
    
    int displayMask = 1 << 31; // 10000000 00000000 00000000 0000000
    int posicao = 0;

    for(int y = 0; y < quadro.length; y++ ) { // leio cada quadro individualmente
      
      for (int x = y, passo = 0; x < y+2; x++) { // cada quadro preciso dividir em dois quadros
        for (int z = 0; z < 16; z++) {
          if ((quadro[y] & displayMask) == 0) 
            novoQuadro[posicao] |= (1 << (31 - passo-1)); 
          else  
            novoQuadro[posicao] |= (1 << (31 - passo));  
          
          passo += 2;
          quadro[y] <<= 1;
        }
        ++posicao;
      }
    }
    principalController.exibirSinaisManchester(novoQuadro);
    return novoQuadro;
  }
  
  public int[] camadaFisicaTransmissoraCodificacaoManchesterDiferencial (int quadro []) {
    int novoQuadro[] = new int [quadro.length * 2]; // Usamos o dobro de espaco, pois em cada quadro nao cabe agora 4 caracteres e sim 2
    
    int displayMask = 1 << 31; // 10000000 00000000 00000000 0000000
    int posicao = 0;
    int ultimoBitLido;
    boolean primeiroBit = true;
    // primeiros dois bits sao especiais (usa manchester)
    if ((quadro[0] & displayMask) == 0)  {
      novoQuadro[0] |= (1 << (30)); 
      System.out.println("O primeiro bit é 0");
      ultimoBitLido = 1;
    } else {
      novoQuadro[0] |= (1 << (31));
      System.out.println("O primeiro bit é 1");
      ultimoBitLido = 0;
    }
              
    quadro[0] <<= 1;
    
    for(int y = 0; y < quadro.length; y++ ) { // leio cada quadro individualmente
      
      for (int x = y, passo = (primeiroBit ?  2 : 0 ); x < y+2; x++) { // cada quadro preciso dividir em dois quadros
        for (int z = (primeiroBit ?  1 : 0 ) ; z < 16; z++) {
          
          if ((quadro[y] & displayMask) == 0) { // o bit atual sendo lido eh 0
            
            if(ultimoBitLido == 0) { // o bit anterior eh 0
              novoQuadro[posicao] |= (1 << (31 - passo)); // coloco 1 (tem transicao)
              ultimoBitLido = 0;
            } else { // o bit anterior eh 1
              novoQuadro[posicao] |= (1 << (31 - passo-1));// coloco 0 (tem transicao)
              ultimoBitLido = 1;
            }

          } else {  // o bit atual sendo lido eh 1
            if(ultimoBitLido == 0) { // o bit anterior eh 0
              novoQuadro[posicao] |= (1 << (31 - passo-1)); // coloco 0 (nao tem transicao)
              ultimoBitLido = 1;
            } else { // o bit anterior eh 1
              novoQuadro[posicao] |= (1 << (31 - passo));// coloco 1 (nao tem transicao)
              ultimoBitLido = 0;
            }
            
          }
          
          passo += 2;
          quadro[y] <<= 1;
        }
        primeiroBit = false;
        ++posicao;
      }
    }
    
    
    principalController.exibirSinaisManchester(novoQuadro);
    return novoQuadro;
  }
  
  
   public void imprimirBits(int quadro) {
    int displayMask = 1 << 31; // 10000000 00000000 00000000 0000000
    for (int i = 1; i <= 32 ; i++) { // da direita para esquerda.
      System.out.print((quadro & displayMask) == 0 ? "0" : "1");
      quadro <<= 1; // desloca o valor uma posição a esquerda é oq permite q seja verificado bit a bit
      
      if (i % 8 == 0) //exibe espaço a cada 8 bits
        System.out.print(" ");
    }    
  }
  

   
   public void setPrincipalController(PrincipalController principalController){
     this.principalController = principalController;
   }
   
   public void setMeioDeComunicacao(MeioDeComunicacao meioDeComunicacao){
     this.meioDeComunicacao = meioDeComunicacao;
   }

}
