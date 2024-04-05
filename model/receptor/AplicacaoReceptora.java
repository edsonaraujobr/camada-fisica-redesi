package model.receptor;
import controller.PrincipalController;

public class AplicacaoReceptora {
  PrincipalController principalController;
  
  public void receberDado(String mensagem) {
    System.out.println("Chegou na aplicacao receptora ");
    System.out.println(mensagem);
    principalController.exibirMensagemReceptor(mensagem);
  }
  
  public void setPrincipalController(PrincipalController principalController){
    this.principalController = principalController;
  }
  
}
