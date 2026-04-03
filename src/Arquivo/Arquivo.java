package Arquivo;

import Lista.Node;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

public class Arquivo
{
    private String nomearquivo;
    private RandomAccessFile arquivo;
    private int comp, mov;
    public Arquivo(String nomearquivo) {
        this.nomearquivo = nomearquivo;
        try
        {
            arquivo = new RandomAccessFile(nomearquivo, "rw");
        } catch (IOException e)
        { }
    }
    public void copiaArquivo(RandomAccessFile arquivoOrigem)
    {
        Registro reg = new Registro();
        int tl = 0;

        try
        {
            arquivoOrigem.seek(0);
            arquivo.setLength(0);
            tl = (int) (arquivoOrigem.length() / Registro.length());

            for (int i = 0; i < tl; i++)
            {
                reg.leDoArq(arquivoOrigem);
                reg.gravaNoArq(arquivo);
            }

            arquivo.seek(0);
            arquivoOrigem.seek(0);
        } catch (IOException e)
        { }
    }
    public RandomAccessFile getFile()
    {
        return arquivo;
    }
    public void truncate(long pos){
        try
        {
            arquivo.setLength(pos * Registro.length());
        } catch (IOException exc)
        { }
    }
    public boolean eof(){
        boolean retorno = false;
        try
        {
            if (arquivo.getFilePointer() == arquivo.length())
                retorno = true;
        } catch (IOException e)
        { }
        return retorno;
    }
    public void seekArq(int pos) {
        try
        {
            arquivo.seek(pos * Registro.length());
        } catch (IOException e)
        { }
    }
    public int filesize() {
        int retorno = 0;
        try
        {
            retorno = (int) (arquivo.length() / Registro.length());
        } catch (IOException e)
        { }
        return retorno;
    }
    public void initComp()
    {
        comp = 0;
    }
    public void initMov()
    {
        mov = 0;
    }
    public int getComp()
    {
        return comp;
    }
    public int getMov()
    {
        return mov;
    }
    public void inserirRegNoFinal(Registro reg) {
        seekArq(filesize());
        reg.gravaNoArq(arquivo);
    }
    public void exibirArquivo() {
        Registro reg = new Registro();

        seekArq(0);

        while (!eof())
        {
            reg.leDoArq(arquivo);
            System.out.println(reg.getNumero());
        }
    }

    public void insercaoDireta() {
        int tl = filesize();
        int pos, i = 1;
        boolean continua = true;
        Registro reg1 = new Registro();
        Registro reg2 = new Registro();
        while (i < tl)
        {
            seekArq(i);
            reg1.leDoArq(arquivo);
            pos = i;
            continua = true;
            while (pos > 0 && continua)
            {
                seekArq(pos - 1);
                reg2.leDoArq(arquivo);
                comp++;
                if (reg1.getNumero() < reg2.getNumero())
                {
                    seekArq(pos);
                    reg2.gravaNoArq(arquivo);
                    mov++;
                    pos--;
                }
                else
                    continua = false;
            }
            seekArq(pos);
            reg1.gravaNoArq(arquivo);
            mov++;
            i++;
        }
    }

    private int buscaBinaria(int chave, int TL){
        int inicio = 0, fim = TL-1, meio = fim/2;
        Registro regAux = new Registro();
        while (inicio <= fim) {
            seekArq(meio);
            regAux.leDoArq(arquivo);
            comp++;

            if (chave > regAux.getNumero())
                inicio = meio + 1;
            else
                fim = meio - 1;
            meio = (inicio + fim) / 2;
        }
        seekArq(meio);
        regAux.leDoArq(arquivo);
        comp++;
        if(chave > regAux.getNumero())
            return meio+1;
        return meio;
    }

    public void insercaoBinaria() {
        int tl = filesize();
        int i, pos;

        Registro reg1 = new Registro();
        Registro reg2 = new Registro();

        for (i = 1; i < tl; i++){
            seekArq(i);
            reg1.leDoArq(arquivo);

            pos = buscaBinaria(reg1.getNumero(), i);

            for (int j = i; j > pos; j--){
                seekArq(j - 1);
                reg2.leDoArq(arquivo);
                seekArq(j);
                reg2.gravaNoArq(arquivo);
                mov++;
            }
            seekArq(pos);
            reg1.gravaNoArq(arquivo);
            mov++;
        }
    }

    public void selecaoDireta() {
        int tl = filesize(), posMenor;

        Registro reg1 = new Registro();
        Registro reg2 = new Registro();
        Registro regMenor = new Registro();

        for (int i = 0; i < tl - 1; i++) {
            posMenor = i;

            seekArq(i);
            regMenor.leDoArq(arquivo);

            for (int j = i + 1; j < tl; j++) {
                seekArq(j);
                reg2.leDoArq(arquivo);

                comp++;

                if (reg2.getNumero() < regMenor.getNumero()) {
                    posMenor = j;
                    regMenor.setNumero(reg2.getNumero());
                }
            }

            if (posMenor != i){
                seekArq(i);
                reg1.leDoArq(arquivo);

                seekArq(posMenor);
                reg2.leDoArq(arquivo);

                seekArq(i);
                reg2.gravaNoArq(arquivo);

                seekArq(posMenor);
                reg1.gravaNoArq(arquivo);
                mov += 2;
            }
        }
    }

    public void bolha() {
        int tl = filesize();
        Registro reg1 = new Registro();
        Registro reg2 = new Registro();
        boolean troca = true;

        while (tl > 1 && troca)
        {
            troca = false;

            for (int i = 0; i < tl - 1; i++)
            {
                seekArq(i);
                reg1.leDoArq(arquivo);

                seekArq(i + 1);
                reg2.leDoArq(arquivo);

                comp++;

                if (reg1.getNumero() > reg2.getNumero())
                {
                    seekArq(i);
                    reg2.gravaNoArq(arquivo);

                    seekArq(i + 1);
                    reg1.gravaNoArq(arquivo);

                    mov += 2;

                    troca = true;
                }
            }
            tl--;
        }
    }

    public void shake()
    {
        int inicio = 0;
        int fim = filesize() - 1;
        Registro reg1 = new Registro();
        Registro reg2 = new Registro();
        boolean troca = true;

        while (inicio < fim && troca)
        {
            troca = false;
            // ida (esquerda → direita)
            for (int i = inicio; i < fim; i++)
            {
                seekArq(i);
                reg1.leDoArq(arquivo);

                seekArq(i + 1);
                reg2.leDoArq(arquivo);
                comp++;

                if (reg1.getNumero() > reg2.getNumero())
                {
                    seekArq(i);
                    reg2.gravaNoArq(arquivo);

                    seekArq(i + 1);
                    reg1.gravaNoArq(arquivo);
                    mov += 2;
                    troca = true;
                }
            }

            fim--;

            if (troca)
            {
                troca = false;

                for (int i = fim; i > inicio; i--)
                {
                    seekArq(i - 1);
                    reg1.leDoArq(arquivo);

                    seekArq(i);
                    reg2.leDoArq(arquivo);
                    comp++;

                    if (reg1.getNumero() > reg2.getNumero())
                    {
                        seekArq(i - 1);
                        reg2.gravaNoArq(arquivo);

                        seekArq(i);
                        reg1.gravaNoArq(arquivo);

                        mov += 2;

                        troca = true;
                    }
                }
                inicio++;
            }
        }
    }

    //demais metodos de ordenacao
    public void geraArquivoOrdenado()
    {
        truncate(0);
        for (int i = 0; i < 1024; i++)
            inserirRegNoFinal(new Registro(i + 1));
    }
    public void geraArquivoReverso()
    {
        truncate(0);
        for (int i = 1024; i > 0; i--)
            inserirRegNoFinal(new Registro(i));
    }
    public void geraArquivoRandomico()
    {
        Random random = new Random();
        truncate(0);
        for (int i = 0; i < 10/*1024*/; i++)
            inserirRegNoFinal(new Registro(random.nextInt(1024) + 1));
    }
}
