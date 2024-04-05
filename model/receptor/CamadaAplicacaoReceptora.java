package model.receptor;

// transforma bits em mensagem
public class CamadaAplicacaoReceptora {
  AplicacaoReceptora aplicacaoReceptora;
  
  public void receberDado(int quadro[], int tipoDeCodificacao){
    String mensagem = "";

    for(int i = 0; i < quadro.length; i++) { 
      // o ideal seria a cada 8 bits mostrar o caractere correspondente.
      mensagem += transformarBitsEmString(quadro[i]);
    }
    System.out.println("Passou pela camada AplicacaoReceptora, a mensagem é: " + mensagem);
    aplicacaoReceptora.receberDado(mensagem);
  }
  
  public void setAplicacaoReceptora( AplicacaoReceptora aplicacaoReceptora){
    this.aplicacaoReceptora = aplicacaoReceptora;
  }
  
  public static String transformarBitsEmString(int numero){ 
    
    int displayMask = 1 << 31; // 10000000 00000000 00000000 0000000
    String resultado = "";
    int byteAtual = 0; // a cada 8 bits temos um caractere
    for (int i = 0; i <= 31 ; i++) { 
        byteAtual = (byteAtual << 1) | ((numero & displayMask) == 0 ? 0 : 1);
        numero <<= 1; // desloca o valor uma posição a esquerda é oq permite q seja verificado bit a bit
        if((i + 1) % 8 == 0) {
            resultado += (char) byteAtual;
            byteAtual = 0;
        }
    }
    return resultado;
  }
}
