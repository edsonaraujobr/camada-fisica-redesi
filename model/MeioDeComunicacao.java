/* ***************************************************************
* Autor............: Edson Araujo de Souza Neto
* Matricula........: 202210169
* Inicio...........: 01/04/2024
* Ultima alteracao.: 07/04/2024
* Nome.............: MeioDeComunicacao
* Funcao...........: Move os bits de um inteiro para outro (do transmissor para o receptor).
*************************************************************** */

package model;

import model.receptor.CamadaFisicaReceptora;

public class MeioDeComunicacao {
  CamadaFisicaReceptora camadaFisicaReceptora;
  
  // mover do ponto A para o B.
  public void enviarDado( int fluxoBrutoDeBits[], int tipoDeCodificacao, int tipoDeEnquadramento) {
    int fluxoBrutoDeBitsPontoA[], fluxoBrutoDeBitsPontoB[];
    
    fluxoBrutoDeBitsPontoA = fluxoBrutoDeBits;
    fluxoBrutoDeBitsPontoB = new int [fluxoBrutoDeBits.length];
    
    for (int tamanho = 0; tamanho < fluxoBrutoDeBitsPontoA.length; tamanho++) {
      int displayMask = 1 << 31; // 10000000 00000000 00000000 0000000
      for (int i = 0; i <= 31 ; i++) { 
        if((fluxoBrutoDeBitsPontoA[tamanho] & displayMask) != 0) {
          fluxoBrutoDeBitsPontoB[tamanho] |= (1 << (31 - i));
        }
        fluxoBrutoDeBitsPontoA[tamanho] <<= 1;
      }
    }

    camadaFisicaReceptora.receberDado(fluxoBrutoDeBitsPontoB, tipoDeCodificacao, tipoDeEnquadramento);
  } // fim metodo enviar dado
  
  public void setCamadaFisicaReceptora(CamadaFisicaReceptora camadaFisicaReceptora) {
    this.camadaFisicaReceptora = camadaFisicaReceptora;
  }
}
