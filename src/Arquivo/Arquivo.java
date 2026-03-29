package Arquivo;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

public class Arquivo
{
    private String nomearquivo;
    private RandomAccessFile arquivo;
    private int comp, mov;
    public Arquivo(String nomearquivo)
    {
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
    public void truncate(long pos)
    {
        try
        {
            arquivo.setLength(pos * Registro.length());
        } catch (IOException exc)
        { }
    }
    public boolean eof()
    {
        boolean retorno = false;
        try
        {
            if (arquivo.getFilePointer() == arquivo.length())
                retorno = true;
        } catch (IOException e)
        { }
        return retorno;
    }
    public void seekArq(int pos)
    {
        try
        {
            arquivo.seek(pos * Registro.length());
        } catch (IOException e)
        { }
    }
    public int filesize()
    {
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
    public void inserirRegNoFinal(Registro reg)
    {
        seekArq(filesize());
        reg.gravaNoArq(arquivo);
    }
    public void insercaoDireta()
    {
        int tl = filesize();
        int pos = 1, i = 1;
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
                comp++; // conta a comparação entre numeros
                if (reg1.getNumero() < reg2.getNumero())
                {
                    seekArq(pos);
                    reg2.gravaNoArq(arquivo);
                    mov++; // deslocamento
                    pos--;
                }
                else
                    continua = false;
            }
            seekArq(pos);
            reg1.gravaNoArq(arquivo);
            mov++; // gravação final do reg1
            i++;
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
        for (int i = 0; i < 1024; i++)
            inserirRegNoFinal(new Registro(random.nextInt(1024) + 1));
    }
}
