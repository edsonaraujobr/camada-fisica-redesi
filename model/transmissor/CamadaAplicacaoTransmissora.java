/* ***************************************************************
* Autor............: Edson Araujo de Souza Neto
* Matricula........: 202210169
* Inicio...........: 01/04/2024
* Ultima alteracao.: 07/04/2024
* Nome.............: CamadaAplicacaoTransmissora
* Funcao...........: Transforma uma string em bits.
*************************************************************** */

package model.transmissor;

public class CamadaAplicacaoTransmissora {
  CamadaEnlaceDadosTransmissora camadaEnlaceDadosTransmissora;
  
  public void enviarDado(String mensagem, int tipoDeCodificacao) {
    char arrayCaracteres[] = mensagem.toCharArray();
    
    int quadro[] = new int [retornarTamanho( arrayCaracteres.length)];
    String stringDeBits = transformarCaracteresEmStringDeBits(arrayCaracteres);
    
    System.out.println("String de bits antes: " + stringDeBits);
    
    for(int i = 0; i < quadro.length; i++) {
      System.out.println("Quadro: " + quadro[i]);
      quadro[i] = inserirBitBinario(stringDeBits, quadro[i]);

      if(stringDeBits.length() > 32) { // remover da variavel String para ser lida na próxima iteração.
        stringDeBits = removerBitsLidos(stringDeBits, 32);
        System.out.println("String de bits pos remover: " + stringDeBits);
      }
    }   
    
    System.out.println("Na camada Aplicação Transmissora");
    for(int i = 0; i < quadro.length; i++) {
      System.out.println("Quadro: "+ i);
      imprimirBits(quadro[i]);
      System.out.println();
    }

    camadaEnlaceDadosTransmissora.enviarDado(quadro, tipoDeCodificacao);
  } // fim metodo enviarDado
  
  public void imprimirBits(int quadro) {
    int displayMask = 1 << 31; // 10000000 00000000 00000000 0000000
    for (int i = 1; i <= 32 ; i++) { // da direita para esquerda.
      System.out.print((quadro & displayMask) == 0 ? "0" : "1");
      quadro <<= 1; // desloca o valor uma posição a esquerda é oq permite q seja verificado bit a bit
      
      if (i % 8 == 0) //exibe espaço a cada 8 bits
        System.out.print(" ");
    }    
  }
  
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
  
  public String transformarCaracteresEmStringDeBits(char arrayCaracteres[]) {
    String resultado = "";
    for (int i = 0; i < arrayCaracteres.length; i++) {
      for (int j = 7; j >= 0; j--) { // For do tamanho dos Bits de um Caractere
        int bit = (arrayCaracteres[i] >> j) & 1; // desloca o bit do caractere direita do por i posicoes e aplica a mascara
        resultado += bit;
      }
    }
    return resultado;
  } // fim metodo transformarCaractereEmStringDeBits
  
  public int retornarTamanho(int tamanhoPalavra){
    if(tamanhoPalavra % 4 == 0)  // vai retornar o numero exato que precisa ser criado em quadros.
      return (tamanhoPalavra / 4); 
    else 
      return ((tamanhoPalavra / 4)+1);
  } // fim metodoRetornarTamanho
  
  
  public void setCamadaEnlaceDadosTransmissora(CamadaEnlaceDadosTransmissora camadaEnlaceDadosTransmissora) {
    this.camadaEnlaceDadosTransmissora = camadaEnlaceDadosTransmissora;
  }
  
}
