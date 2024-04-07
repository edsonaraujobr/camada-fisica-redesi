/* ***************************************************************
* Autor............: Edson Araujo de Souza Neto
* Matricula........: 202210169
* Inicio...........: 01/04/2024
* Ultima alteracao.: 07/04/2024
* Nome.............: CamadaAplicacaoTransmissora
* Funcao...........: Transforma uma string em bits de acordo com a codificacao.
*************************************************************** */

package model.transmissor;

public class CamadaAplicacaoTransmissora {
  CamadaFisicaTransmissora camadaFisicaTransmissora;
  static boolean manchesterDiferencialPrimeiroBit = true; // para diferenciar o primeiro bit
  static char ultimoBitLido;
  
  public void enviarDado(String mensagem, int tipoDeCodificacao) {
    char arrayCaracteres[] = mensagem.toCharArray();
    
    int quadro[] = new int [retornarTamanho(tipoDeCodificacao, arrayCaracteres.length)];
    String stringDeBits = transformarCaracteresEmStringDeBits(arrayCaracteres, tipoDeCodificacao);
    
    System.out.println("A palavra é: " + mensagem);
    System.out.println("String de Bits antes: " + stringDeBits);
    System.out.println("Tamanho do Quadro: " + quadro.length);
    
    switch(tipoDeCodificacao) {
      case 0: // binario
        for(int i = 0; i < quadro.length; i++) {
          quadro[i] = inserirBitBinario(stringDeBits, quadro[i]);

          if(stringDeBits.length() > 32) { // remover da variavel String para ser lida na próxima iteração.
            stringDeBits = removerBitsLidos(stringDeBits, 32);

            System.out.println("String de Bits depois de remover bits: " + stringDeBits);
          }
        }        
        break;
      case 1: // manchester
        for(int i = 0; i < quadro.length; i++) {
          quadro[i] = inserirBitManchester(stringDeBits, quadro[i]);

          if(stringDeBits.length() > 32) { // remover da variavel String para ser lida na próxima iteração.
            stringDeBits = removerBitsLidos(stringDeBits, 32);

            System.out.println("String de Bits depois de remover bits: " + stringDeBits);
          }
        }           
        break;
      case 2: // manchester diferencial
        for(int i = 0; i < quadro.length; i++) {
          quadro[i] = inserirBitManchesterDiferencial(stringDeBits, quadro[i]);

          if(stringDeBits.length() > 32) { // remover da variavel String para ser lida na próxima iteração.
            stringDeBits = removerBitsLidos(stringDeBits, 32); // preciso do ultimo como informacao

            System.out.println("String de Bits depois de remover bits: " + stringDeBits);
          }
        }            
        break;
    } // fim switch

    
    System.out.println("Os bits de quadro são: ");
    
    for (int i = 0; i < quadro.length; i++) {
      System.out.println("quadro" + i);
      imprimirBits(quadro[i]);
      System.out.println();
    }


    camadaFisicaTransmissora.enviarDado(quadro, tipoDeCodificacao);
  } // fim metodo enviarDado
  
  public int inserirBitManchesterDiferencial(String stringDeBits, int inteiro) {
    if ( manchesterDiferencialPrimeiroBit) {
      char bitAnterior = stringDeBits.charAt(0);
      char bit = stringDeBits.charAt(1); 
      if (bitAnterior == '0' && bit == '1') {
        inteiro |= (1 << 30);         // segundo bit recebe 1    
      } else {
        inteiro |= (1 << 31);      // primeiro bit recebe 1
      }
      
      // vamos para o for desconsiderando os dois primeiros bits, por isso index = 2
      for (int index = 2; index < 32; index += 2) {
        if(index+1 < stringDeBits.length()) { // verificar se existe aquela posicao na string de Bits.
          bit = stringDeBits.charAt(index); 
          bitAnterior = stringDeBits.charAt(index - 1);

          if ((bitAnterior == '1' && bit == '0') || (bitAnterior == '0' && bit == '1')) { // teve transicao
            if (bitAnterior == '1' && bit == '0') { 
              inteiro |= (1 << (31 - index - 1));              
            } else if(bitAnterior == '0' && bit == '1') { 
               inteiro |= (1 << (31 - index ));              
            }
          } else { // nao teve transicao
            if(bitAnterior == '1' && bit == '1') {
              inteiro |= (1 << (31 - index));              
            } else if(bitAnterior == '0' && bit == '0'){
              inteiro |= (1 << (31 - index - 1));                
            }
          }

        }
      } // fim do for         
     manchesterDiferencialPrimeiroBit = false; // apenas uma vez, por isso virou falso
    } else { // nao eh a primeira iteracao
      for (int index = 0; index < 32; index += 2) {
        if(index+1 < stringDeBits.length()) { // verificar se existe aquela posicao na string de Bits.
          char bitAtual = stringDeBits.charAt(index); 
          char bitSucessor = stringDeBits.charAt(index + 1);
          char bitAnterior = ultimoBitLido;
          
          if ((bitAtual == '0' && bitAnterior == '1') || (bitAtual == '1' && bitAnterior == '0')) { // teve transicao
            if (bitAtual == '0' && bitAnterior == '1') { 
              inteiro |= (1 << (31 - index));     
              bitAnterior = '1';
            } else if( bitAtual == '1' && bitAnterior == '0') { 
              inteiro |= (1 << (31 - index - 1)); 
              bitAnterior = '0';               
            }
          } else { // nao teve transicao
            if( bitAtual == '1' && bitAnterior == '1' ) {
              inteiro |= (1 << (31 - index ));
              bitAnterior = '0';              
            } else if( bitAtual == '0' && bitAnterior == '0' ){
              inteiro |= (1 << (31 - index - 1)); 
              bitAnterior = '1';
            }
          } // fim else       

        } // fim if
      }  // fim for      
    } // fim else
    ultimoBitLido  = stringDeBits.charAt(stringDeBits.length() - 1);
    return inteiro;
  } // fim metodo inserirBitManchesterDiferencial
  
  public int inserirBitManchester(String stringDeBits, int inteiro) {
    for (int index = 0; index < 32; index += 2) {
      if(index < stringDeBits.length()) { // verificar se existe aquela posicao na string de Bits.
        char bit = stringDeBits.charAt(index); 
        char bit2 = stringDeBits.charAt(index + 1);

        if (bit == '1' && bit2 == '0' ) 
          inteiro |= (1 << (31 - index));
        else  // 01
          inteiro |= (1 << (31 - index-1));    
      }
    }   
    return inteiro;
  } // fim metodo inserirBitManchester
  
  public int inserirBitBinario(String stringDeBits, int inteiro){
    for (int index = 0; index < 32; index++) {
      if(index < stringDeBits.length()) { // verificar se existe aquela posicao na string de Bits.
        char bit = stringDeBits.charAt(index); 
        if (bit == '1' ) 
          inteiro |= (1 << (31 - index));
      }
    }
    return inteiro;
  }
  
  public String removerBitsLidos(String stringDeBits, int quantidade){ // remover a string de bits lidos
    stringDeBits = stringDeBits.substring(quantidade);
    return stringDeBits;
  }
  
  public String transformarCaracteresEmStringDeBits(char arrayCaracteres[], int tipoDeCodificacao) {
    String resultado = "";
    
    switch(tipoDeCodificacao) {
      case 0: // binario
        for (int i = 0; i < arrayCaracteres.length; i++) {
          for (int j = 7; j >= 0; j--) { // For do tamanho dos Bits de um Caractere
            int bit = (arrayCaracteres[i] >> j) & 1; // desloca o bit do caractere direita do por i posicoes e aplica a mascara
            resultado += bit;
          }
        }
        break;
      case 1: // manchester
        for (int i = 0; i < arrayCaracteres.length; i++) {
          for (int j = 7; j >= 0; j--) { // For do tamanho dos Bits de um Caractere
            int bit = (arrayCaracteres[i] >> j) & 1; // desloca o bit do caractere direita do por i posicoes e aplica a mascara
            if (bit == 0) 
              resultado += "01";
            else 
              resultado += "10";
          }
        }
        break;
      case 2: // manchester diferencial       
        for (int i = 0; i < arrayCaracteres.length; i++) {
          for (int j = 7; j >= 0; j--) { // For do tamanho dos Bits de um Caractere
            int bit = (arrayCaracteres[i] >> j) & 1; // desloca o bit do caractere direita do por i posicoes e aplica a mascara
            char ultimoBit = resultado.isEmpty() ? 'X' : resultado.charAt(resultado.length() - 1);
            
            if(ultimoBit == '1' || ultimoBit == 'X') {
              if(bit == 0) 
                resultado += "01";
              else  // bit eh 1
                resultado += "10";      
            } else { // o ultimo bit eh 0
              if(bit == 0) 
                resultado += "10"; // tem transicao
              else // bit eh 1
                resultado += "01"; // nao tem transicao    
            } // fim de verificacao ultimoBit
          } // fim segundo for
        } // fim primeiro for        
        break;
    } // fim switch
    return resultado;
  } // fim metodo transformarCaractereEmStringDeBits
  
  public int retornarTamanho(int tipoDeCodificacao, int tamanhoPalavra){
    switch(tipoDeCodificacao){
      case 0: // binária
        if(tamanhoPalavra % 4 == 0)  // vai retornar o numero exato que precisa ser criado em quadros.
          return (tamanhoPalavra / 4); 
        else 
          return ((tamanhoPalavra / 4)+1);
        
      default: // manchester ou manchester diferencial
        if (tamanhoPalavra % 2 == 0) 
          return tamanhoPalavra/2; // divide por dois pois Manchester e ManchesterDiferencial usa o dobro de espaco.
        else 
          return (tamanhoPalavra/2)+1;
    }
  } // fim metodoRetornarTamanho
  
  public static void imprimirBits(int numero){ 
    int displayMask = 1 << 31; 
    
    for (int i = 1; i <= 32 ; i++) { 
      System.out.print((numero & displayMask) == 0 ? "0" : "1");
      numero <<= 1; 
      
      if (i % 8 == 0) //exibe espaco a cada 8 bits
        System.out.print(" ");
    }
    System.out.println();
  } // fim metodo imprimirBits
  
  public void setCamadaFisicaTransmissora(CamadaFisicaTransmissora camadaFisicaTransmissora) {
    this.camadaFisicaTransmissora = camadaFisicaTransmissora;
  }
  
}
