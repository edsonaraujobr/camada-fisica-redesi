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
    int quadro2[] = {1,2};
    return quadro2;  
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

