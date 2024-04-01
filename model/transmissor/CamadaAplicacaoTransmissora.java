package model.transmissor;

public class CamadaAplicacaoTransmissora {
  //CamadaFisicaTransmissora camadaFisicaTransmissora = new CamadaFisicaTransmissora();
  // aqui vamos transformar a mensagem em array de inteiros.
  
  public void enviarDado(String mensagem, int tipoDeCodificacao) {
    char arrayCaracteres[] = mensagem.toCharArray();

    int quadro[] = new int [retornarTamanho(tipoDeCodificacao, arrayCaracteres.length)];
    int indiceQuadro = 0;
    
    for (int i = 0; i < arrayCaracteres.length; i++) {
      for (int j = 7; j >= 0; j--) { // For do tamanho dos Bits de um Caractere
        int bit = (arrayCaracteres[i] >> j) & 1; // desloca o bit do caractere direita do por i posicoes e aplica a mascara
        System.out.print(bit);
      }
      System.out.print(" ");
    }

    //camadaFisicaTransmissora.enviarDado(quadro);
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
  
}
