/* ***************************************************************
* Autor............: Edson Araujo de Souza Neto
* Matricula........: 202210169
* Inicio...........: 01/04/2024
* Ultima alteracao.: 05/05/2024
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
        fluxoBrutoDeBits = camadaFisicaTransmissoraCodificacaoManchester (quadro, tipoDeEnquadramento);
        break;
        
      case 2:  //codificacao manchester diferencial
        fluxoBrutoDeBits = camadaFisicaTransmissoraCodificacaoManchesterDiferencial (quadro, tipoDeEnquadramento);
        break;
    }
    
    System.out.println("\nNa camada Fisica Transmissora");
    for (int i = 0; i < fluxoBrutoDeBits.length; i++) {
      imprimirBits(fluxoBrutoDeBits[i]);
      System.out.println();
    }
    meioDeComunicacao.enviarDado(fluxoBrutoDeBits, tipoDeCodificacao, tipoDeEnquadramento);
  }
  
  public int[] camadaFisicaTransmissoraCodificacaoBinaria (int quadro []) {
    principalController.exibirSinaisBinarios(quadro);
    // ja vem em binario
    return quadro; 
  }
  
  public int[] camadaFisicaTransmissoraCodificacaoManchester (int quadro [], int tipoDeEnquadramento) {
    int novoQuadro[] = new int [quadro.length * 2]; // Usamos o dobro de espaco, pois em cada quadro nao cabe agora 4 caracteres e sim 2
    
    int displayMask = 1 << 31; // 10000000 00000000 00000000 0000000
    int posicao = 0;
    String resultadoBits = "";
    
    for(int y = 0; y < quadro.length; y++ ) { // leio cada quadro individualmente
      for (int x = y, passo = 0; x < y+2; x++) { // cada quadro preciso dividir em dois quadros
        for (int z = 0; z < 16; z++) {
          
          if ((quadro[y] & displayMask) == 0) {
            resultadoBits += "0";
          } else  {
            resultadoBits += "1";
          }
          quadro[y] <<= 1;
          
          if((z+1)% 8 == 0) { // li um byte
            if(resultadoBits.equals("00000000")) {
              resultadoBits = "";
              break;
            } else {
              for(int i = 0; i < 8; i++) {
                if(resultadoBits.charAt(i) == '1') {
                  novoQuadro[posicao] |= (1 << (31 - passo));  
                } else {
                  novoQuadro[posicao] |= (1 << (31 - passo-1)); 
                }
              passo += 2;
              } // fim for
              resultadoBits = "";
            } // fim else
          } // fim if

        }
        ++posicao;
      }
    }
   
    
    if( tipoDeEnquadramento == 3) { // violacao Camada Fisica
      int quadroEnquadrado [] = violacaoCamadaFisica(novoQuadro);
      principalController.exibirSinaisManchester(quadroEnquadrado);
     
      return quadroEnquadrado;
    }
    else {
      principalController.exibirSinaisManchester(novoQuadro);
      return novoQuadro;     
    }

  }
  
  public int[] camadaFisicaTransmissoraCodificacaoManchesterDiferencial (int quadro [], int tipoDeEnquadramento) {
    int novoQuadro[] = new int [quadro.length * 2]; // Usamos o dobro de espaco, pois em cada quadro nao cabe agora 4 caracteres e sim 2
    
    int displayMask = 1 << 31; // 10000000 00000000 00000000 0000000
    int posicao = 0;
    int ultimoBitLido;
    boolean primeiroBit = true;
    String resultadoBits = "";
    boolean byteNulo = false;
    // primeiros dois bits sao especiais (usa manchester)
    if ((quadro[0] & displayMask) == 0)  {
      novoQuadro[0] |= (1 << (30)); 
      resultadoBits += "0";
      ultimoBitLido = 1;
    } else {
      novoQuadro[0] |= (1 << (31));
      resultadoBits += "1";
      ultimoBitLido = 0;
    }
              
    quadro[0] <<= 1;
    
    for(int y = 0; y < quadro.length && !byteNulo; y++ ) { // leio cada quadro individualmente
      
      for (int x = y, passo = (primeiroBit ?  2 : 0 ); x < y+2 && !byteNulo; x++) { // cada quadro preciso dividir em dois quadros
        for (int z = (primeiroBit ?  1 : 0 ) ; z < 16 && !byteNulo; z++) {
          
          if((quadro[y] & displayMask) == 0) {
            resultadoBits += "0";
          } else {
            resultadoBits += "1";
          }
          
          quadro[y] <<= 1;
          
          if( ( z + 1 )  % 8 == 0) {
            if(resultadoBits.equals("00000000")) {
              resultadoBits = "";
              byteNulo = true;
              break;
            } else {
              for(int i = (primeiroBit? 1 : 0); i < 8; i++) {
                if(resultadoBits.charAt(i) == '1') {
                  if(ultimoBitLido == 0) { // o bit anterior eh 0
                    novoQuadro[posicao] |= (1 << (31 - passo-1)); // coloco 0 (nao tem transicao)
                    ultimoBitLido = 1;
                  } else { // o bit anterior eh 1
                    novoQuadro[posicao] |= (1 << (31 - passo));// coloco 1 (nao tem transicao)
                    ultimoBitLido = 0;
                  }
                } else {
                  if(ultimoBitLido == 0) { // o bit anterior eh 0
                    novoQuadro[posicao] |= (1 << (31 - passo)); // coloco 1 (tem transicao)
                    ultimoBitLido = 0;
                  } else { // o bit anterior eh 1
                    novoQuadro[posicao] |= (1 << (31 - passo-1));// coloco 0 (tem transicao)
                    ultimoBitLido = 1;
                  }
                }
                passo += 2;
              } // fim for 8 bits
              primeiroBit = false;
              resultadoBits = "";
            } // fim else    
          } // fim if 

        } // fim for 16 bits
        ++posicao;

      } // fim segundo for
    } // fim for quadro
    
    if( tipoDeEnquadramento == 3) { // violacao Camada Fisica
      
      int quadroEnquadrado [] = violacaoCamadaFisica(novoQuadro);
      
      principalController.exibirSinaisManchester(quadroEnquadrado);
      return quadroEnquadrado;
    }
    else {
      principalController.exibirSinaisManchester(novoQuadro);
      return novoQuadro;     
    }
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
  
   public int [] violacaoCamadaFisica(int quadroManchester []) {
    int novoQuadro[] =  new int [quadroManchester.length * 2];
    
    int contador = 0;
    int displayMask = 1 << 31; 
    int posicaoNovoQuadro = 0;
    String resultadoBits = "";
    boolean byteNulo = false;
    boolean bitsAltoAlto = false; // bool de controle para saber quando por bits de flag
    
    novoQuadro[posicaoNovoQuadro] |= (1 << (31 - contador));
    ++contador;
    novoQuadro[posicaoNovoQuadro] |= (1 << (31 - contador));
    ++contador;
    
    for(int i = 0; i < quadroManchester.length; i++) {
      // coloco dois primeiros bits 1
      
      for(int y = 0; y < 32; y++) {
        if((quadroManchester[i] & displayMask) != 0) {
          resultadoBits += "1";
        } else {
          resultadoBits += "0";
        }
        
        quadroManchester[i] <<=1;
        
        if((y+1)%8 == 0) {
          if(resultadoBits.equals("00000000")) {
            resultadoBits = "";
            byteNulo = true;
            break;
          } else {
            bitsAltoAlto = false;
            for(int x = 0; x < 8; x++) {
              if(resultadoBits.charAt(x) ==  '1') {
                novoQuadro[posicaoNovoQuadro] |= (1 << (31 - contador));
              }
              ++contador;
              if(contador == 32) {
                ++posicaoNovoQuadro;
                contador = 0;
              }
            }
            resultadoBits = "";
          }
        }
      } // for 32 bits
      if(byteNulo) {
        break;
      } else {
        novoQuadro[posicaoNovoQuadro] |= (1 << (31 - contador));
        ++contador;
        novoQuadro[posicaoNovoQuadro] |= (1 << (31 - contador));
        ++contador;
        bitsAltoAlto = true;
      }

    }
    if(!bitsAltoAlto) { // coloco os ultimos dois bits alto alto
      novoQuadro[posicaoNovoQuadro] |= (1 << (31 - contador));
      ++contador;
      novoQuadro[posicaoNovoQuadro] |= (1 << (31 - contador));
      ++contador;
    }

    return novoQuadro;
   }
   
   public void setPrincipalController(PrincipalController principalController){
     this.principalController = principalController;
   }
   
   public void setMeioDeComunicacao(MeioDeComunicacao meioDeComunicacao){
     this.meioDeComunicacao = meioDeComunicacao;
   }

}
