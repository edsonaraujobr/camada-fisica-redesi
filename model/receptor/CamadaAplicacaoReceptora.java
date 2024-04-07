/* ***************************************************************
* Autor............: Edson Araujo de Souza Neto
* Matricula........: 202210169
* Inicio...........: 01/04/2024
* Ultima alteracao.: 07/04/2024
* Nome.............: CamadaAplicacaoReceptora
* Funcao...........: Transforma os bits em uma string de acordo com a codificacao.
*************************************************************** */

package model.receptor;

public class CamadaAplicacaoReceptora {
  AplicacaoReceptora aplicacaoReceptora;
  static boolean manchesterDiferencialPrimeiroBit = true; // para diferenciar o primeiro bit
  static int ultimoBitLido;
  
  public void receberDado(int quadro[], int tipoDeCodificacao){
    String mensagem = "";

    switch(tipoDeCodificacao) {
      case 0: // binario
        for(int i = 0; i < quadro.length; i++) { 
          mensagem += transformarCodificacaoBinariaEmString(quadro[i]);
        }        
        break;
      case 1: // manchester
        for(int i = 0; i < quadro.length; i++) { 
          mensagem += transformarManchesterEmString(quadro[i]);
        } 
        break;
      case 2: // manchester diferencial
        for(int i = 0; i < quadro.length; i++) { 
          mensagem += transformarManchesterDiferencialEmString(quadro[i]);
        }         
        break;
        
    }
    System.out.println("A mensagem é: " + mensagem);
    aplicacaoReceptora.receberDado(mensagem);
  }
  
  public void setAplicacaoReceptora( AplicacaoReceptora aplicacaoReceptora){
    this.aplicacaoReceptora = aplicacaoReceptora;
  }
  
  public static String transformarCodificacaoBinariaEmString(int numero) {
    String resultado = "";
    int displayMask = 1 << 31; // 10000000 00000000 00000000 0000000
    int byteAtual = 0; // a cada 8 bits temos um caractere

    for (int i = 0; i <= 31 ; i++) { 
      byteAtual = (byteAtual << 1) | ((numero & displayMask) == 0 ? 0 : 1);
      numero <<= 1; 
      if((i + 1) % 8 == 0) {
        resultado += (char) byteAtual;
        byteAtual = 0;
      }
    }       
    return resultado;
  } // fim metodo transformarCodificacaoBinariaEmString
  
  public static String transformarManchesterEmString(int numero) {
    String resultado = "";
    int displayMask = 1 << 31; // 10000000 00000000 00000000 0000000
    int byteAtual = 0; // a cada 8 bits temos um caractere  
    for (int i = 0; i < 16 ; i++) { // 16 porque analisamos de 2 em 2 bits
      byteAtual = (byteAtual << 1) | ((numero & displayMask) == 0 ? 0 : 1);
      numero <<= 2; 
      if((i+1) % 8 == 0) {
        resultado += (char) byteAtual;
        byteAtual = 0;
      }
    }          
    return resultado;
  } // fim metodo transformarManchesterEmString
  
  public static String transformarManchesterDiferencialEmString(int numero) {
    String saida = "";
    int displayMask = 1 << 31; // 10000000 00000000 00000000 0000000
    StringBuilder resultado = new StringBuilder();

    if(manchesterDiferencialPrimeiroBit) {
      // a primeira vez vez tenho que ler os dois primeiros e ler o restante de forma separada
      if((numero & displayMask) == 0) {
        resultado.append(0);
        System.out.println("O primeiro é 0");
        ultimoBitLido = 1;
      } else {
        resultado.append(1);
        System.out.println("O primeiro é 1");
        ultimoBitLido = 0;
      }
      numero <<= 2; 
      
      for(int i = 2; i < 32; i+=2)  { // em 32 bits temos 2 caracteres
    
        if( (numero & displayMask) == 0) {
          if(ultimoBitLido == 1) { // houve transicao (10)
            resultado.append(0); 
            System.out.println("Houve transição: (0) | posição: "+i);
            ultimoBitLido = 1;            
          } else { // nao houve transicao (00)
            System.out.println("Não Houve transição: (1) | posição: "+i);            
            resultado.append(1);  
            ultimoBitLido = 1;   
          }
        } else {
          if(ultimoBitLido == 1) { // nao houve transicao (11)
            resultado.append(1);   
            ultimoBitLido = 0;
            System.out.println("Não Houve transição: (1) | posição: "+i); 
          } else { // houve transicao (01)
            ultimoBitLido = 0;
            resultado.append(0);   
            System.out.println("Houve transição: (0) | posição: "+i); 
          }
        }
        
        if ((i + 2) % 16 == 0) {
          System.out.println("Bit: " + resultado.toString() );
          saida += converterStringDeBitsEmCaractere(resultado);
          System.out.println("Caractere: " + saida);
          resultado = new StringBuilder();
        }
        numero <<= 2;
      }
      manchesterDiferencialPrimeiroBit = false;
    } else {
      for(int i = 0; i < 32; i+=2)  { // em 32 bits temos 2 caracteres
    
        if( (numero & displayMask) == 0) {
          if(ultimoBitLido == 1) { // houve transicao (10)
            resultado.append(0); 
            System.out.println("Houve transição: (0) | posição: "+i);
            ultimoBitLido = 1;            
          } else { // nao houve transicao (00)
            System.out.println("Não Houve transição: (1) | posição: "+i);            
            resultado.append(1);  
            ultimoBitLido = 1;   
          }
        } else {
          if(ultimoBitLido == 1) { // nao houve transicao (11)
            resultado.append(1);   
            ultimoBitLido = 0;
            System.out.println("Não Houve transição: (1) | posição: "+i); 
          } else { // houve transicao (01)
            ultimoBitLido = 0;
            resultado.append(0);   
            System.out.println("Houve transição: (0) | posição: "+i); 
          }
        }
        
        if ((i + 2) % 16 == 0) {
          System.out.println("Bit: " + resultado.toString() );
          saida += converterStringDeBitsEmCaractere(resultado);
          System.out.println("Caractere: " + saida);
          resultado = new StringBuilder();
        }
        numero <<= 2;
      }
    } // fim else
    

    return saida;     
  } 
   // fim metodo transformarManchesterDiferencialEmString
  public static char converterStringDeBitsEmCaractere(StringBuilder resultado) {
    String resultadoString = resultado.toString();
    char caracter;

    // Obtém o substring de 8 bits correspondente a um caractere
    String sub = resultadoString.substring( 0,  8);
    // Converte o substring de 8 bits em um caractere usando Integer.parseInt()
    caracter = (char) Integer.parseInt(sub, 2);
    
    return caracter;
  }
} // fim da classe
