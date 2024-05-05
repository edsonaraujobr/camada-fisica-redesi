/* ***************************************************************
* Autor............: Edson Araujo de Souza Neto
* Matricula........: 202210169
* Inicio...........: 28/04/2024
* Ultima alteracao.: 05/05/2024
* Nome.............: CamadaEnlaceDadosTransmissora
* Funcao...........: Neste momento, apenas enquadra os bits de acordo com o tipo de enquadramento escolhido.
*************************************************************** */


package model.transmissor;

public class CamadaEnlaceDadosTransmissora {
  
  CamadaFisicaTransmissora camadaFisicaTransmissora;
  
  public void enviarDado(int quadro [], int tipoDeCodificacao, int tipoDeEnquadramento) {
    int quadroEnquadrado[] = enquadramento(quadro, tipoDeEnquadramento);

    System.out.println("\nNa camada Enlace de Dados Transmissora");
    for(int i = 0; i < quadroEnquadrado.length; i++) {
      imprimirBits(quadroEnquadrado[i]);
      System.out.println();
    }
    
    camadaFisicaTransmissora.enviarDado(quadroEnquadrado, tipoDeCodificacao, tipoDeEnquadramento);
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
    int quadroEnquadrado [] = {};
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
        quadroEnquadrado = violacaoCamadaFisica(quadro); // retorna o proprio quadro(implementado na Camada Fisica)
        break;
    }//fim do switch/case
    
    return quadroEnquadrado;
  }
  
  public int [] contagemDeCaracteres (int quadro []) {
    int novoQuadro [] = new int[quadro.length * 2]; // pior dos casos tenho o dobro 

    int posicaoNovoQuadro = 0;
    int displayMask = 1 << 31; 
    int passo = 8;
    int contadorCaractere = 1;
    int contadorBitsNull = 0;
    
    for (int y = 0; y < quadro.length; y++) {
      
      for (int i = 0; i < 32; i++) { 
        
        if ((quadro[y] & displayMask) != 0) { // eh 1
          novoQuadro[posicaoNovoQuadro] |= (1 << (31 - passo));    
        } else { // eh 0
          ++contadorBitsNull;
        }
        
        ++passo;
        
        if(((i+1) % 8 == 0) && contadorBitsNull != 8) { // cada 8 bits temos 1 caractere
          ++contadorCaractere;         
          contadorBitsNull = 0; // nao veio byte null
        } else if(((i+1) % 8 == 0) && contadorBitsNull == 8) { // 8 bits nulos (nao ha informacao)
          break;
        }
        
        if(passo == 32) { // temos três caracteres sem contar o de controle, logo vamos coloca-lo no novo quadro e ir pro proximo index
          for (int j = 7, pos = 0; j >= 0; j--, pos++) {
            int bit = (contadorCaractere >> j) & 1;

            if (((contadorCaractere >> j) & 1) == 1) {
              novoQuadro[posicaoNovoQuadro] |= (1 << (31 - pos));
            }
          }              
          ++posicaoNovoQuadro;
          contadorCaractere = 1;
          passo = 8;
        }
        
        quadro[y] <<= 1;
      }
      
      // insiro o contador caractere nos 8 primeiros bits.
    }
    if(contadorCaractere > 1) { // teve um caractere
      for (int j = 7, pos = 0; j >= 0; j--, pos++) {
       int bit = (contadorCaractere >> j) & 1;

       if (((contadorCaractere >> j) & 1) == 1) {
           novoQuadro[posicaoNovoQuadro] |= (1 << (31 - pos));
       }
      }             
    }

    return novoQuadro;
  
  }
  
  public int [] insercaoDeBytes (int quadro []) {
    int novoQuadro [] = new int[quadro.length * 4]; // pior dos casos 
    
    int displayMask = 1 << 31; 
    int contadorIndexQuadroNovo = 0;
    int contadorIndexQuadroAnterior = 0;
    int passo = 23;
    String resultadoBits = "";
    String start = "01010011";
    String end =  "01000101";
    String esc = "01111100";
    int contadorCaractere = 0;
    int index = 0;
    
    for(int y = 0; y < quadro.length*32; y++) {
      if ((quadro[contadorIndexQuadroAnterior] & displayMask) != 0) { // eh 1
        resultadoBits += "1";
      } else { // eh 0
        resultadoBits += "0";
      }
      
      quadro[contadorIndexQuadroAnterior] <<=1;

      
      if((y+1) % 8 == 0) { // li um byte
        if(resultadoBits.equals("00000000")) { // li um byte vazio
          break;
        }
        if((y+1) % 32 == 0) {
          ++contadorIndexQuadroAnterior;
        }
        switch(resultadoBits) {
          case "01010011": // "S"
          case "01000101": // "E"
          case "01111100": // "|"
            if(contadorCaractere > 0) { // ja tivemos um caractere lido
              
              for(int i = 31; i > 23; i--) { // coloco S nos oito primeiros bits
                if(start.charAt(index) == '1')
                  novoQuadro[contadorIndexQuadroNovo] |= (1 << i);  
                ++index;
              }
              index = 0;

              for(int i = 15; i > 7; i--) { // coloco E nos oito ultimos bits
                if(end.charAt(index) == '1')
                  novoQuadro[contadorIndexQuadroNovo] |= (1 << i);  
                ++index;
              }   
              index = 0;
              
              ++contadorIndexQuadroNovo; // vou para o proximo quadro
              // coloco start, escape, o caractere e end.
              
              for(int i = 31; i > 23; i--) { // coloco S nos oito primeiros bits
                if(start.charAt(index) == '1')
                  novoQuadro[contadorIndexQuadroNovo] |= (1 << i);   
                ++index;
              }
              index = 0;
              
              for(int i = 23; i > 15; i--) { // coloco o escape
                if(esc.charAt(index) == '1')
                  novoQuadro[contadorIndexQuadroNovo] |= (1 << i);
                ++index;
              }
              index = 0;
              
              for(int i = 15; i > 7; i--) { // coloco o caractere
                if(resultadoBits.charAt(index) == '1')
                  novoQuadro[contadorIndexQuadroNovo] |= (1 << i);
                ++index;
              }
              index = 0;
              
              for(int i = 7; i >= 0; i--) { // coloco E nos oito ultimos bits
                if(end.charAt(index) == '1')
                  novoQuadro[contadorIndexQuadroNovo] |= (1 << i); 
                index++;
              }       
              index = 0;
              
              ++contadorIndexQuadroNovo; // vou para o proximo
              contadorCaractere = 0;
              
            } else { // nenhum caractere lido
              // coloco start, escape, o caractere, end
              for(int i = 31; i > 23; i--) { // coloco S nos oito primeiros bits
                if(start.charAt(index) == '1')
                  novoQuadro[contadorIndexQuadroNovo] |= (1 << i);  
                ++index;
              }
              index = 0;
              
              for(int i = 23; i > 15; i--) { // coloco o escape
                if(esc.charAt(index) == '1')
                  novoQuadro[contadorIndexQuadroNovo] |= (1 << i);
                ++index;
              }
              index = 0;
              
              for(int i = 15; i > 7; i--) { // coloco o caractere
                if(resultadoBits.charAt(index) == '1')
                  novoQuadro[contadorIndexQuadroNovo] |= (1 << i);
                ++index;
              }
              index = 0;
              
              for(int i = 7; i >= 0; i--) { // coloco E nos oito ultimos bits
                if(end.charAt(index) == '1')
                  novoQuadro[contadorIndexQuadroNovo] |= (1 << i);  
                ++index;
              }         
              index = 0;
              
              // vou para o proximo quadro
              ++contadorIndexQuadroNovo;
            }
            break;
          default: // coloco os oito bits e vou para o proximo.
            for(int i = 0; i < 8; i++) { 
              if(resultadoBits.charAt(i) == '1')
                novoQuadro[contadorIndexQuadroNovo] |= (1 << passo);
              --passo;
            }
            ++contadorCaractere;
            break;
        }

        resultadoBits = "";
      }
      
      if(contadorCaractere == 2 ) {
        // li dois caracteres que nao precisam do escape
        for(int i = 31; i > 23; i--) { // coloco S
          if(start.charAt(index) == '1')
            novoQuadro[contadorIndexQuadroNovo] |= (1 << i);   
          ++index;
        }
        index = 0;
        
        for(int i = 7; i >= 0; i--) { // coloco E
          if(end.charAt(index) == '1')
            novoQuadro[contadorIndexQuadroNovo] |= (1 << i);  
          ++index;
        }
        index = 0;
        
        ++contadorIndexQuadroNovo; //vou para novo quadro     
        contadorCaractere = 0;
        passo = 23;
      }
 
    }
    // se apos o for, tiver um caractere sem estar enquadrado
    if(contadorCaractere > 0) {
        for(int i = 31; i > 23; i--) { // coloco S
          if(start.charAt(index) == '1')
            novoQuadro[contadorIndexQuadroNovo] |= (1 << i);   
          ++index;
        }
        index = 0;
        
        for(int i = 15; i > 7; i--) { // coloco E
          if(end.charAt(index) == '1')
            novoQuadro[contadorIndexQuadroNovo] |= (1 << i);  
          ++index;
        }      
    }
    return novoQuadro;
  }
  
  public int [] insercaoDeBits (int quadroRecebido []) {
    int quadroEnquadrado[] = new int [quadroRecebido.length * 2];
    
    int posicao = 31;
    int displayMask = 1 << 31; 
    String resultadoBits = "";
    int contadorIndexQuadroEnquadrado = 0;
    String saidaResultadoBits = "";
    int contadorBits = 0;
    String byteFlag = "01111110";
    int contadorCaractere = 0;
    
    for(int i = 0; i < 8; i++) {
      if(byteFlag.charAt(i) == '1') {
        quadroEnquadrado[contadorIndexQuadroEnquadrado] |= (1 << posicao); 
      }
      --posicao;
    }
    
    for(int i = 0; i < quadroRecebido.length; i++) {
      for(int y = 0; y < 32; y++) {
        if ((quadroRecebido[i] & displayMask) != 0) { // eh 1
          resultadoBits += "1";
        } else { // eh 0
          resultadoBits += "0";
        }
        quadroRecebido[i] <<= 1;
        
        if((y+1) % 8 == 0) {
          if(resultadoBits.equals("00000000")) {
            break;
          }
          ++contadorCaractere;
          int contadorBitUm = 0;
          for(int x = 0; x < 8; x++) { // for para verificar a sequencia de bits
            
            if(resultadoBits.charAt(x) == '1') {
              ++contadorBitUm;
              if(contadorBitUm == 6) {
                contadorBits += 2;
                saidaResultadoBits += "01";
              } else {
                ++contadorBits;
                saidaResultadoBits += "1";
              }
            } else {
              saidaResultadoBits += "0";
              contadorBitUm = 0;
            }
          } // fim for 8 bits
          
          for(int x = 0; x < saidaResultadoBits.length(); x++) { // for para inserir os bits
            if(saidaResultadoBits.charAt(x) == '1') {
              quadroEnquadrado[contadorIndexQuadroEnquadrado] |= (1 << posicao); 
            } 
            --posicao;
            if(posicao == -1) {
              posicao = 31;
              ++contadorIndexQuadroEnquadrado;
            }            
          }
          
          if(contadorCaractere == 2) {
            contadorBits = 0;
            contadorCaractere = 0;
            for(int z = 0; z < 8; z++) {
              if(byteFlag.charAt(z) == '1') {
                quadroEnquadrado[contadorIndexQuadroEnquadrado] |= (1 << posicao); 
              }
              --posicao;
              if(posicao == -1) {
                posicao = 31;
                ++contadorIndexQuadroEnquadrado;
              } 
            }
          }
          saidaResultadoBits = "";
          resultadoBits = "";
        } // fim % 8


      } // fim for 16 bits
 
    } // fim for quadro
    if(contadorCaractere > 0) {
      for(int i = 0; i < 8; i++) {
        if(byteFlag.charAt(i) == '1') {
          quadroEnquadrado[contadorIndexQuadroEnquadrado] |= (1 << posicao); 
        }
        --posicao;
      }
    }
    
    return quadroEnquadrado;   
  }
  
  public int [] violacaoCamadaFisica(int quadro []) {
    // implementado na camada Física
    return quadro;
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

