package model.transmissor;

public class CamadaAplicacaoTransmissora {
  CamadaFisicaTransmissora camadaFisicaTransmissora;
  
  public void enviarDado(String mensagem, int tipoDeCodificacao) {
    System.out.println("A palavra é: " + mensagem);
    
    char arrayCaracteres[] = mensagem.toCharArray();
    
    int quadro[] = new int [retornarTamanho(tipoDeCodificacao, arrayCaracteres.length)];
    String stringDeBits = transformarCaracteresEmStringDeBits(arrayCaracteres);
    
    System.out.println("String de Bits antes: " + stringDeBits);
    System.out.println("quadro length: " + quadro.length);
    
    for(int i = 0; i < quadro.length; i++) {
      quadro[i] = inserirBit(stringDeBits, quadro[i]);
      
      if(stringDeBits.length() > 32) { // remover da variavel String para ser lida na próxima iteração.
        stringDeBits = removerBitsLidos(stringDeBits);
        
        System.out.println("String de Bits depois de remover bits: " + stringDeBits);
      }
    }
    
    System.out.println("Os bits de quadro são: ");
    
    for (int i = 0; i < quadro.length; i++) {
      System.out.println("quadro" + i);
      imprimirBits(quadro[i]);
      System.out.println();
    }


    camadaFisicaTransmissora.enviarDado(quadro, tipoDeCodificacao);
  }
  
  public int inserirBit(String stringDeBits, int inteiro){
    for (int index = 0; index < 32; index++) {
      if(index < stringDeBits.length()) { // verificar se existe aquela posicao na string de Bits.
        char bit = stringDeBits.charAt(index); // verificar se existe.
        if (bit == '1' ) {
          inteiro |= (1 << (31 - index));
        }
      }
    }
    return inteiro;
  }
  
  public String removerBitsLidos(String stringDeBits){
    stringDeBits = stringDeBits.substring(32);
    // stringDeBits recebe ele proprio com exceção do primeiro caractere que neste caso é um bit que ja foi lido.
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
  }
  
  public int retornarTamanho(int tipoDeCodificacao, int tamanhoPalavra){
    switch(tipoDeCodificacao){
      case 0: // binária
        if(tamanhoPalavra % 4 == 0) { // vai retornar o numero exato que precisa ser criado em quadros.
          return (tamanhoPalavra / 4); 
        } else {
          return ((tamanhoPalavra / 4)+1);
        }
      default: // manchester ou manchester diferencial
        if (tamanhoPalavra % 2 == 0) {
          return tamanhoPalavra/2; // divide por dois pois Manchester e ManchesterDiferencial usa o dobro de espaço.
        } else {
          return (tamanhoPalavra/2)+1;
        }
    }
  }
  
  public static void imprimirBits(int numero){ // usei para depurar.
    
    int displayMask = 1 << 31; // 10000000 00000000 00000000 0000000
    
    for (int i = 1; i <= 32 ; i++) { 
      System.out.print((numero & displayMask) == 0 ? "0" : "1");
      numero <<= 1; // desloca o valor uma posição a esquerda é oq permite q seja verificado bit a bit
      
      if (i % 8 == 0) //exibe espaço a cada 8 bits
        System.out.print(" ");
    }
    System.out.println();
  }
  
  public void setCamadaFisicaTransmissora(CamadaFisicaTransmissora camadaFisicaTransmissora) {
    this.camadaFisicaTransmissora = camadaFisicaTransmissora;
  }
  
}
