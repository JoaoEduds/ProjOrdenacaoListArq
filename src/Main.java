import Arquivo.Arquivo;
import Lista.List;

public class Main {
    public static void main(String[] args) {
        Arquivo arq = new Arquivo("teste.dat");
        arq.geraArquivoRandomico();
        arq.shake();
        arq.exibirArquivo();
        System.out.println(Arquivo.gerarTabela());

    }
}