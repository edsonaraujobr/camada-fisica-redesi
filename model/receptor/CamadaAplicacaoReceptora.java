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
  
  public void receberDado(int quadro[], int tipoDeCodificacao){
    String mensagem = "";

    for(int i = 0; i < quadro.length; i++) { 
      mensagem += transformarCodificacaoBinariaEmString(quadro[i]);
    }        

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
  
} // fim da classe
