/* ***************************************************************
* Autor............: Edson Araujo de Souza Neto
* Matricula........: 202210169
* Inicio...........: 01/04/2024
* Ultima alteracao.: 05/05/2024
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
        fluxoBrutoDeBits = camadaFisicaTransmissoraCodificacaoManchester (quadro, tipoDeEnquadramento);
        break;
        
      case 2:  //codificacao manchester diferencial
        fluxoBrutoDeBits = camadaFisicaTransmissoraCodificacaoManchesterDiferencial (quadro, tipoDeEnquadramento);
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
  
  public int[] camadaFisicaTransmissoraCodificacaoManchester (int quadroRecebido [], int tipoDeEnquadramento) {
    // transformar de manchester para binario
    int quadro [];
    if(tipoDeEnquadramento == 3) { // tivemos violacao da Camada Fisica
      quadro  = violacaoCamadaFisica(quadroRecebido);
      
    } else {
      quadro = quadroRecebido;
    }
    
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
  
   public int[] camadaFisicaTransmissoraCodificacaoManchesterDiferencial (int quadroRecebido [], int tipoDeEnquadramento) {
    // transformar de manchester diferencial para binario
    int quadro [];
    if(tipoDeEnquadramento == 3) { // tivemos violacao da Camada Fisica
      quadro  = violacaoCamadaFisica(quadroRecebido);
    } else {
      quadro = quadroRecebido;
    }    
    
    int novoQuadro[] = new int [quadro.length/2]; 
    int posicao = 0;
    int displayMask = 1 << 31; 
    boolean primeiroBit = true;
    int ultimoBitLido;
    String bitsLidos = "";
    boolean byteNulo = false;
    
    // primeiros dois bits são especiais
    if((quadro[0] & displayMask) != 0) { // os dois primeiros bits eh 10
      bitsLidos += "1";
      ultimoBitLido = 0;
    } else {  // os dois primeiros bits eh 01
      ultimoBitLido = 1;
      bitsLidos += "0";
    }
    quadro[0] <<=2;
    
    
    for(int y = 0; y < novoQuadro.length && !byteNulo; y++) {
      for (int x = y, passo = 0 ; x < y+2 && !byteNulo; x++) { // para cada quadro, preciso ler dois
        for(int z = ( primeiroBit ? 1 : 0 ); z < 16 && !byteNulo; z++) { 
          // em um quadro inteiro de manchester, tenho metade em binario
          if ((quadro[posicao] & displayMask) != 0 && ultimoBitLido == 1) { // o bit é 1 (nao tem transicao)
            bitsLidos += "1";
            ultimoBitLido = 0;
          } else if((quadro[posicao] & displayMask) == 0 && ultimoBitLido == 0) { // o bit é 1 (nao tem transicao)
            bitsLidos += "1";
            ultimoBitLido = 1;
          } else {
            bitsLidos += "0";
          }
          
          quadro[posicao] <<= 2;
          
          if((z+1) % 8 == 0) {
            if(bitsLidos.equals("00000000") || bitsLidos.equals("10000000")) {
              bitsLidos = "";
              byteNulo = true;
              break;
            } else {
              for(int i = 0; i < 8; i++) {
                if(bitsLidos.charAt(i) == '1') {
                  novoQuadro[y] |= (1 << (31 - passo));
                }
                ++passo;
              }
              bitsLidos = "";
            }
          }
        }
        primeiroBit = false;
        ++posicao;
      }    
    
    } // for novoQuadro
    
    return novoQuadro;
  }
   
   public int [] violacaoCamadaFisica(int quadroRecebido []) {
    int novoQuadro[] = new int [quadroRecebido.length / 2];
    int displayMask = 1 << 31; 
    String resultadoBits = "";
    int posicaoNovoQuadro = 0;
    int contador = 0;
    int contadorBits = 0;
    quadroRecebido[0] <<= 2;
    
    for(int i = 0; i < quadroRecebido.length; i++) { 

      for(int y = 0; y < 30; y++) {
        ++contadorBits;
        if((quadroRecebido[i] & displayMask) != 0) {
          resultadoBits += "1";
        } else {
          resultadoBits += "0";
        }
        quadroRecebido[i] <<= 1;

        if((contadorBits) % 8 == 0) {
          if(resultadoBits.equals("00000000")) {
            resultadoBits = "";
            break;
          } else {
            for(int pos = 0; pos < 8; pos++) {
              if(resultadoBits.charAt(pos)== '1') {
                novoQuadro[posicaoNovoQuadro] |= (1 << (31 - contador));
              }
              ++contador;
            }
            if(contador == 32) {
              quadroRecebido[i] <<= 2;
              ++posicaoNovoQuadro;
              contador = 0;
              contadorBits = 0;
            }
            resultadoBits = "";
          }
        } // if
      } // for
    } // for 
     
    return novoQuadro;
   }
}
