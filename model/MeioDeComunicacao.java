package model;

import model.receptor.CamadaFisicaReceptora;


public class MeioDeComunicacao {
  CamadaFisicaReceptora camadaFisicaReceptora;
  
  // mover do ponto A para o B.
  public void enviarDado( int fluxoBrutoDeBits[], int tipoDeCodificacao) {
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
    
    for (int i = 0; i < fluxoBrutoDeBitsPontoB.length; i++) {
      System.out.println("FluxoBrutoDeBits ponto B: " + i);
      imprimirResultado(fluxoBrutoDeBitsPontoB[i]);
      System.out.println();
    }
    
    camadaFisicaReceptora.receberDado(fluxoBrutoDeBitsPontoB, tipoDeCodificacao);
  } // fim metodo enviar dado
  
  public void imprimirResultado(int numero){
    int displayMask = 1 << 31; // 10000000 00000000 00000000 0000000
    
    for (int i = 1; i <= 32 ; i++) { 
      System.out.print((numero & displayMask) == 0 ? "0" : "1");
      numero <<= 1; // desloca o valor uma posição a esquerda é oq permite q seja verificado bit a bit
      
      if (i % 8 == 0) //exibe espaço a cada 8 bits
        System.out.print(" ");
    }
    System.out.println();
  }
  
  public void setCamadaFisicaReceptora(CamadaFisicaReceptora camadaFisicaReceptora) {
    this.camadaFisicaReceptora = camadaFisicaReceptora;
  }
}
