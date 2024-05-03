/* ***************************************************************
* Autor............: Edson Araujo de Souza Neto
* Matricula........: 202210169
* Inicio...........: 01/04/2024
* Ultima alteracao.: 07/04/2024
* Nome.............: CamadaFisicaReceptora
* Funcao...........: Recebe o fluxo de bits do meio de comunicacao de acordo com o tipo de codificacao
                     e move para camada aplicacao receptora
*************************************************************** */

package model.receptor;

public class CamadaFisicaReceptora {
  CamadaEnlaceDadosReceptora camadaEnlaceDadosReceptora;
  
  public void receberDado(int quadro [], int tipoDeCodificacao, int tipoDeEnquadramento){
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

    System.out.println("\nNa camada Fisica Receptora");
    for(int i = 0; i < fluxoBrutoDeBits.length; i++) {
      imprimirBits(fluxoBrutoDeBits[i]);
      System.out.println();
    }
    
    camadaEnlaceDadosReceptora.receberDado(fluxoBrutoDeBits, tipoDeEnquadramento);
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
  
  public void setCamadaEnlaceDadosReceptora(CamadaEnlaceDadosReceptora camadaEnlaceDadosReceptora){
    this.camadaEnlaceDadosReceptora = camadaEnlaceDadosReceptora;
  }
  
  public int[] camadaFisicaTransmissoraCodificacaoBinaria (int quadro []) {
    return quadro;
  }
  
  public int[] camadaFisicaTransmissoraCodificacaoManchester (int quadro []) {
    // transformar de manchester para binario
    
    System.out.println(quadro.length/2);
    int novoQuadro[] = new int [quadro.length/2]; 
    
    int displayMask = 1 << 31; // 10000000 00000000 00000000 0000000
    int posicao = 0;
    
    for(int y = 0; y < novoQuadro.length; y++) {
      
      for (int x = y, passo = 0; x < y+2; x++) { // para cada quadro, preciso ler dois
         for(int z = 0; z < 16; z++) { 
          // em um quadro inteiro de manchester, tenho metade em binario
          if ((quadro[posicao] & displayMask) != 0) 
            novoQuadro[y] |= (1 << (31 - passo));   

          ++passo;
          quadro[posicao] <<= 2;

        }    
        ++posicao;
      }
    }
    
    
    return novoQuadro;
  }
  
   public int[] camadaFisicaTransmissoraCodificacaoManchesterDiferencial (int quadro []) {
    // transformar de manchester para binario
     
    int novoQuadro[] = new int [quadro.length/2]; 
    int posicao = 0;
    int displayMask = 1 << 31; // 10000000 00000000 00000000 0000000
    boolean primeiroBit = true;
    int ultimoBitLido;
    
    // primeiros dois bits são especiais
    if((quadro[0] & displayMask) != 0) { // os dois primeiros bits eh 10
      novoQuadro[0] |= (1 << (31));  
      ultimoBitLido = 0;
    } else {  // os dois primeiros bits eh 01
      ultimoBitLido = 1;
    }
    quadro[0] <<=2;
    
    
    for(int y = 0; y < novoQuadro.length; y++) {
      for (int x = y, passo = ( primeiroBit ? 1 : 0 ) ; x < y+2; x++) { // para cada quadro, preciso ler dois
        for(int z = ( primeiroBit ? 1 : 0 ); z < 16; z++) { 
          // em um quadro inteiro de manchester, tenho metade em binario
          if ((quadro[posicao] & displayMask) != 0 && ultimoBitLido == 1) { // o bit é 1 (nao tem transicao)
            //System.out.println("Em quadro: " + y +" e no bit: " + z + "É igual a 1");
            novoQuadro[y] |= (1 << (31 - passo)); 
            ultimoBitLido = 0;
          } else if((quadro[posicao] & displayMask) == 0 && ultimoBitLido == 0) { // o bit é 1 (nao tem transicao)
            novoQuadro[y] |= (1 << (31 - passo));  
            ultimoBitLido = 1;
          }

          ++passo;
          quadro[posicao] <<= 2;

        }
        primeiroBit = false;
        ++posicao;
      }    
    
    }
    
    return novoQuadro;
  }
}
