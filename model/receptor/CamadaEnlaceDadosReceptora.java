package model.receptor;

public class CamadaEnlaceDadosReceptora {
  
  CamadaAplicacaoReceptora camadaAplicacaoReceptora;

  public void receberDado(int quadro [], int tipoDeEnquadramento) {
    int quadroDesenquadrado [] = enquadramento(quadro, tipoDeEnquadramento);
    
    System.out.println("Na camada Enlace de Dados Receptora");
    for(int i = 0; i < quadroDesenquadrado.length; i++) {
      System.out.println("QuadroDesenquadrado: "+ i);
      imprimirBits(quadroDesenquadrado[i]);
      System.out.println();
    }
    camadaAplicacaoReceptora.receberDado(quadroDesenquadrado);
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
  
  public int [] enquadramento(int quadro [], int tipoDeEnquadramento) {
    int quadroDesenquadrado [] = {};
    switch (tipoDeEnquadramento) {
      case 0 : //contagem de caracteres
        quadroDesenquadrado = contagemDeCaracteres(quadro);
        break;
      case 1 : //insercao de bytes
        quadroDesenquadrado = insercaoDeBytes(quadro);
        break;
      case 2 : //insercao de bits
        quadroDesenquadrado = insercaoDeBits(quadro);
        break;
      case 3 : //violacao da camada fisica
        quadroDesenquadrado = violacaoCamadaFisica(quadro);
        break;
    }//fim do switch/case
    return quadroDesenquadrado;
  }
  
  public int [] contagemDeCaracteres (int quadro []) {
    int novoQuadro[] = new int [quadro.length / 2];

    int displayMask = 1 << 31; 
    int posicaoNovoQuadro = 0;
    int contadorBitsNull = 0;
    int posicao = 0;

    for(int y = 0; y < quadro.length; y++) {
      quadro[y] <<= 8;
      for (int i = 0; i < 24; i++ ) {
        
        if ((quadro[y] & displayMask) != 0) { // eh 1
          novoQuadro[posicaoNovoQuadro] |= (1 << (31 - posicao));    
        } else { // eh 0
          ++contadorBitsNull;
        }
        ++posicao;
        
        if(posicao == 32) {
          ++posicaoNovoQuadro;
          posicao = 0;
        }
        
        if((i+1) % 8 == 0 && contadorBitsNull == 8) {
          break;
        } else if((i+1) % 8 == 0) {
          contadorBitsNull = 0;
        }
       
        quadro[y] <<= 1;
      }
    }
    
    return novoQuadro;
  
  }
  
  public int [] insercaoDeBytes (int quadro []) {
    int novoQuadro[] = new int [quadro.length / 4];
    
    int displayMask = 1 << 31; 
    String resultadoBits = "";
    String start = "01010011";
    String end =  "01000101";
    String esc = "01111100";
    int contadorEscape = 0;
    int passo = 31;
    boolean byteEnd = false;
    int contadorNovoQuadro = 0;
    
    for(int y = 0; y < quadro.length; y++) {
      for(int i = 0; i < 32 && !byteEnd; i++) {
        
        if ((quadro[y] & displayMask) != 0) { // eh 1
        resultadoBits += "1";
        } else { // eh 0
          resultadoBits += "0";
        }
        quadro[y] <<=1;
        
        if((i+1) % 8 == 0) { // li um byte 
          System.out.println("Resultado dos bits: " + resultadoBits);
          if(resultadoBits.equals("00000000")) { // li um byte vazio
            resultadoBits = "";
            byteEnd = true;
            break; // nao posso sair do segundo for pois ha casos em que os oito ultimos bits sao nulos mas tenho mais bits em outras posicoes do quadro
          }
          switch(resultadoBits) {
            case "01111100": // "|"
              if(contadorEscape == 1) {
                // insiro os 8 bits
                for(int pos = 0; pos < 8; pos++) { 
                  if(resultadoBits.charAt(pos) == '1')
                    novoQuadro[contadorNovoQuadro] |= (1 << passo);
                  --passo;
                }                
                contadorEscape = 0;                
              } else {
                ++contadorEscape;
              }
              break;
            case "01010011": // "S"
              if(contadorEscape == 1) { // nao eh o byte de flag
                // insere os 8 bits
                for(int pos = 0; pos < 8; pos++) { 
                  if(resultadoBits.charAt(pos) == '1')
                    novoQuadro[contadorNovoQuadro] |= (1 << passo);
                  --passo;
                }                
                contadorEscape = 0;
              } 
              break;
            case "01000101": // "E"
              if(contadorEscape == 1) { // nao eh o byte de flag
                // insere os 8 bits
                for(int pos = 0; pos < 8; pos++) { 
                  if(resultadoBits.charAt(pos) == '1')
                    novoQuadro[contadorNovoQuadro] |= (1 << passo);
                  --passo;
                }                
                contadorEscape = 0;
              } else { // eh byte de flag
                // vou para proximo quadro
                byteEnd = true;
              }
              break;
            default:
              for(int pos = 0; pos < 8; pos++) { 
                if(resultadoBits.charAt(pos) == '1')
                  novoQuadro[contadorNovoQuadro] |= (1 << passo);
                System.out.println("Passo: " + passo);
                --passo;
              }    
              break;
          } // fim switch
          if(passo == -1) {
            passo = 31;
            ++contadorNovoQuadro;
          }
          resultadoBits = "";
        } // fim if li um byte

      } // fim for 32 bits
      byteEnd = false;
    } // fim for quadro
    
    return novoQuadro;  
  }
  
  public int [] insercaoDeBits (int quadro []) {
     int quadro2[] = {1,2};
    return quadro2;   
  }
  
  public int [] violacaoCamadaFisica(int quadro []) {
     int quadro2[] = {1,2};
    return quadro2;   
  }
  
  public void controleDeErro(int quadro []) {
    // sera implementado em outro momento
  }
  
  public void controleDeFluxo(int quadro []) {
    // sera implementado em outro momento
  }
  
  public void setCamadaAplicacaoReceptora(CamadaAplicacaoReceptora camadaAplicacaoReceptora) {
    this.camadaAplicacaoReceptora = camadaAplicacaoReceptora;
  }
}

