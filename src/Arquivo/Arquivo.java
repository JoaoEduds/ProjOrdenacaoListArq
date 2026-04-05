package Arquivo;
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
    private int leNumero(int pos)
    {
        Registro reg = new Registro();
        seekArq(pos);
        reg.leDoArq(arquivo);
        return reg.getNumero();
    }

    private void gravaNumero(int pos, int numero)
    {
        Registro reg = new Registro(numero);
        seekArq(pos);
        reg.gravaNoArq(arquivo);
    }

    private void trocaReg(int i, int j)
    {
        Registro rg1 = new Registro();
        Registro rg2 = new Registro();
        seekArq(i);
        rg1.leDoArq(arquivo);
        seekArq(j);
        rg2.leDoArq(arquivo);
        seekArq(i);
        rg2.gravaNoArq(arquivo);
        seekArq(j);
        rg1.gravaNoArq(arquivo);
        mov += 2;
    }

    private void insercaoBucket(java.util.ArrayList<Integer> lista)
    {
        int i, j, aux;
        boolean continua;
        for(i = 1; i < lista.size(); i++)
        {
            aux = lista.get(i);
            j = i - 1;
            continua = true;

            while(j >= 0 && continua)
            {
                comp++;
                if(lista.get(j) > aux)
                {
                    lista.set(j + 1, lista.get(j));
                    mov++;
                    j--;
                }
                else
                    continua = false;
            }
            lista.set(j + 1, aux);
            mov++;
        }
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
    public void Heap()
    {
        int pai, F1, F2, maiorF, TL;
        Registro rg1 = new Registro();
        Registro rg2 = new Registro();
        Registro rg3 = new Registro();
        TL = filesize();
        while(TL > 1)
        {
            for(pai = TL/2-1; pai >= 0; pai--)
            {
                F1 = 2*pai+1;
                F2 = F1+1;
                maiorF = F1;
                seekArq(F1);
                rg1.leDoArq(arquivo);
                if(F2 < TL)
                {
                    seekArq(F2);
                    rg2.leDoArq(arquivo);
                    comp++;
                    if(rg2.getNumero() > rg1.getNumero())
                        maiorF = F2;
                }
                seekArq(maiorF);
                rg1.leDoArq(arquivo);
                seekArq(pai);
                rg2.leDoArq(arquivo);
                comp++;
                if(rg1.getNumero() > rg2.getNumero())
                {
                    seekArq(maiorF);
                    rg3.leDoArq(arquivo);
                    seekArq(pai);
                    rg2.leDoArq(arquivo);
                    seekArq(maiorF);
                    rg2.gravaNoArq(arquivo);
                    seekArq(pai);
                    rg3.gravaNoArq(arquivo);
                    mov += 2;
                }
            }
            seekArq(0);
            rg1.leDoArq(arquivo);
            seekArq(TL-1);
            rg2.leDoArq(arquivo);
            seekArq(0);
            rg2.gravaNoArq(arquivo);
            seekArq(TL-1);
            rg1.gravaNoArq(arquivo);
            mov += 2;
            TL--;
        }
    }
    public void shellSort()
    {
        int dist = 1;
        int TL = filesize();
        int aux, pos;

        Registro regAtual = new Registro();
        Registro regDist = new Registro();

        // calcula distância inicial (Knuth)
        while (dist < TL)
            dist = 3 * dist + 1;

        dist = dist / 3;

        while (dist > 0)
        {
            for (int i = dist; i < TL; i++)
            {
                // pega valor atual
                seekArq(i);
                regAtual.leDoArq(arquivo);
                aux = regAtual.getNumero();

                pos = i - dist;

                if (pos >= 0)
                {
                    seekArq(pos);
                    regDist.leDoArq(arquivo);
                }

                while (pos >= 0 && regDist.getNumero() > aux)
                {
                    comp++;

                    // move elemento maior para frente
                    seekArq(pos + dist);
                    regDist.gravaNoArq(arquivo);
                    mov++;

                    pos = pos - dist;

                    if (pos >= 0)
                    {
                        seekArq(pos);
                        regDist.leDoArq(arquivo);
                    }
                }

                // insere o elemento na posição correta
                seekArq(pos + dist);
                regAtual.setNumero(aux);
                regAtual.gravaNoArq(arquivo);
                mov++;
            }

            dist = dist / 3;
        }
    }
    public void QuicksemPivo()
    {
        QuickSP(0, filesize() - 1);
    }

    public void QuickcomPivo()
    {
        QuickCP(0, filesize() - 1);
    }

    private void QuickSP(int inicio, int fim)
    {
        int i = inicio, j = fim;
        boolean flag = true;
        int numI, numJ;

        while(i < j)
        {
            numI = leNumero(i);
            numJ = leNumero(j);

            if(flag)
            {
                while(i < j && numI <= numJ)
                {
                    comp++;
                    i++;
                    if(i < j)
                        numI = leNumero(i);
                }
            }
            else
            {
                while(i < j && numI <= numJ)
                {
                    comp++;
                    j--;
                    if(i < j)
                        numJ = leNumero(j);
                }
            }

            if(i < j)
                trocaReg(i, j);

            flag = !flag;
        }

        if(inicio < i - 1)
            QuickSP(inicio, i - 1);
        if(j + 1 < fim)
            QuickSP(j + 1, fim);
    }

    private void QuickCP(int inicio, int fim)
    {
        int i = inicio, j = fim, pivo;
        int numI, numJ;

        pivo = leNumero((i + j) / 2);

        while(i <= j)
        {
            numI = leNumero(i);
            numJ = leNumero(j);

            while(i <= j && numI < pivo)
            {
                comp++;
                i++;
                if(i <= j)
                    numI = leNumero(i);
            }

            while(i <= j && pivo < numJ)
            {
                comp++;
                j--;
                if(i <= j)
                    numJ = leNumero(j);
            }

            if(i <= j)
            {
                trocaReg(i, j);
                i++;
                j--;
            }
        }

        if(inicio < j)
            QuickCP(inicio, j);
        if(i < fim)
            QuickCP(i, fim);
    }

    public void Merge_Sort()
    {
        int seq = 1;
        int TL = filesize();
        int meio = TL / 2;
        int[] l1 = new int[meio];
        int[] l2 = new int[TL - meio];

        while(seq < TL)
        {
            particao(l1, l2, TL);
            fusao(l1, l2, seq, TL);
            seq = seq * 2;
        }
    }

    private void particao(int[] l1, int[] l2, int TL)
    {
        int meio = TL / 2;
        int i;

        for(i = 0; i < meio; i++)
        {
            l1[i] = leNumero(i);
            mov++;
        }

        for(i = meio; i < TL; i++)
        {
            l2[i - meio] = leNumero(i);
            mov++;
        }
    }

    private void fusao(int[] l1, int[] l2, int seq, int TL)
    {
        int i, j, tam_seq, atual, p1, p2;

        i = 0;
        j = 0;
        tam_seq = seq;
        atual = 0;
        p1 = 0;
        p2 = 0;

        while(atual < TL)
        {
            while(i < seq && j < seq && p1 < l1.length && p2 < l2.length)
            {
                comp++;
                if(l1[p1] < l2[p2])
                {
                    gravaNumero(atual, l1[p1]);
                    p1++;
                    i++;
                }
                else
                {
                    gravaNumero(atual, l2[p2]);
                    p2++;
                    j++;
                }
                mov++;
                atual++;
            }

            while(i < seq && p1 < l1.length)
            {
                gravaNumero(atual, l1[p1]);
                p1++;
                i++;
                atual++;
                mov++;
            }

            while(j < seq && p2 < l2.length)
            {
                gravaNumero(atual, l2[p2]);
                p2++;
                j++;
                atual++;
                mov++;
            }

            seq = seq + tam_seq;
        }
    }

    private void fusao2(int ini1, int fim1, int ini2, int fim2, int aux[])
    {
        int i, j, k;
        int numI, numJ;

        i = ini1;
        j = ini2;
        k = 0;

        numI = leNumero(i);
        numJ = leNumero(j);

        while(i <= fim1 && j <= fim2)
        {
            comp++;
            if(numI <= numJ)
            {
                aux[k++] = numI;
                mov++;
                i++;
                if(i <= fim1)
                    numI = leNumero(i);
            }
            else
            {
                aux[k++] = numJ;
                mov++;
                j++;
                if(j <= fim2)
                    numJ = leNumero(j);
            }
        }

        while(i <= fim1)
        {
            aux[k++] = numI;
            mov++;
            i++;
            if(i <= fim1)
                numI = leNumero(i);
        }

        while(j <= fim2)
        {
            aux[k++] = numJ;
            mov++;
            j++;
            if(j <= fim2)
                numJ = leNumero(j);
        }

        for(i = 0; i < k; i++)
        {
            gravaNumero(ini1 + i, aux[i]);
            mov++;
        }
    }

    private void merge(int esq, int dir, int aux[])
    {
        if(esq < dir)
        {
            int meio = (esq + dir) / 2;
            merge(esq, meio, aux);
            merge(meio + 1, dir, aux);
            fusao2(esq, meio, meio + 1, dir, aux);
        }
    }

    public void Merge_Sort2()
    {
        int TL = filesize();
        int aux[] = new int[TL];
        merge(0, TL - 1, aux);
    }

    public void Counting_Sort()
    {
        int TL = filesize();
        int maior, i, num;
        int[] vetCont;
        int[] vetFim;

        maior = leNumero(0);
        for(i = 1; i < TL; i++)
        {
            num = leNumero(i);
            comp++;
            if(maior < num)
                maior = num;
        }

        vetCont = new int[maior + 1];
        vetFim = new int[TL];

        for(i = 0; i < TL; i++)
        {
            num = leNumero(i);
            vetCont[num]++;
        }

        for(i = 1; i < vetCont.length; i++)
            vetCont[i] += vetCont[i - 1];

        for(i = TL - 1; i >= 0; i--)
        {
            num = leNumero(i);
            vetFim[--vetCont[num]] = num;
            mov++;
        }

        for(i = 0; i < TL; i++)
        {
            gravaNumero(i, vetFim[i]);
            mov++;
        }
    }

    public void Bucket_Sort()
    {
        int TL = filesize();
        int buckets, maior, setor, pos, i, j, atual, num;
        java.util.ArrayList<Integer>[] vetL;

        buckets = (int)Math.sqrt(TL);
        vetL = new java.util.ArrayList[buckets];

        for(i = 0; i < buckets; i++)
            vetL[i] = new java.util.ArrayList<Integer>();

        maior = leNumero(0);
        for(i = 1; i < TL; i++)
        {
            num = leNumero(i);
            comp++;
            if(maior < num)
                maior = num;
        }

        setor = maior / buckets + 1;

        for(i = 0; i < TL; i++)
        {
            num = leNumero(i);
            pos = num / setor;
            vetL[pos].add(num);
            mov++;
        }

        atual = 0;
        for(i = 0; i < buckets; i++)
        {
            insercaoBucket(vetL[i]);
            for(j = 0; j < vetL[i].size(); j++)
            {
                gravaNumero(atual, vetL[i].get(j));
                atual++;
                mov++;
            }
        }
    }

    public void Radix_Sort()
    {
        int TL = filesize();
        int maior, i, x, num, dig, pos;
        java.util.ArrayList<Integer>[] buckets = new java.util.ArrayList[10];

        for(i = 0; i < 10; i++)
            buckets[i] = new java.util.ArrayList<Integer>();

        maior = leNumero(0);
        for(i = 1; i < TL; i++)
        {
            num = leNumero(i);
            comp++;
            if(maior < num)
                maior = num;
        }

        for(x = 1; maior / x > 0; x = x * 10)
        {
            for(i = 0; i < 10; i++)
                buckets[i].clear();

            for(i = 0; i < TL; i++)
            {
                num = leNumero(i);
                dig = (num / x) % 10;
                buckets[dig].add(num);
                mov++;
            }

            pos = 0;
            for(i = 0; i < 10; i++)
            {
                int j = 0;
                while(j < buckets[i].size())
                {
                    gravaNumero(pos, buckets[i].get(j));
                    pos++;
                    j++;
                    mov++;
                }
            }
        }
    }

    public void Comb_Sort()
    {
        int TL = filesize();
        int inter, i, j, numI, numJ;
        boolean f;

        inter = TL;
        f = true;

        while(inter > 1 || f)
        {
            inter = inter * 10 / 13;
            if(inter == 9 || inter == 10)
                inter = 11;
            if(inter < 1)
                inter = 1;

            f = false;
            i = 0;
            j = inter;

            while(j < TL)
            {
                numI = leNumero(i);
                numJ = leNumero(j);
                comp++;

                if(numI > numJ)
                {
                    trocaReg(i, j);
                    f = true;
                }

                i++;
                j++;
            }
        }
    }

    public void Gnome_Sort()
    {
        int TL = filesize();
        int gnomo, num1, num2;

        gnomo = 0;

        while(gnomo < TL - 1)
        {
            num1 = leNumero(gnomo);
            num2 = leNumero(gnomo + 1);
            comp++;

            if(num1 > num2)
            {
                trocaReg(gnomo, gnomo + 1);
                if(gnomo == 0)
                    gnomo = gnomo + 1;
                else
                    gnomo = gnomo - 1;
            }
            else
                gnomo = gnomo + 1;
        }
    }

    private void insertion_Sort(int esq, int dir)
    {
        int i, pos, aux;
        boolean continua;
        Registro reg1 = new Registro();
        Registro reg2 = new Registro();

        for(i = esq + 1; i <= dir; i++)
        {
            seekArq(i);
            reg1.leDoArq(arquivo);
            aux = reg1.getNumero();
            pos = i;
            continua = true;

            while(pos > esq && continua)
            {
                seekArq(pos - 1);
                reg2.leDoArq(arquivo);
                comp++;

                if(aux < reg2.getNumero())
                {
                    seekArq(pos);
                    reg2.gravaNoArq(arquivo);
                    mov++;
                    pos--;
                }
                else
                    continua = false;
            }

            gravaNumero(pos, aux);
            mov++;
        }
    }

    private void inverte(int i, int l)
    {
        while(i < l)
        {
            trocaReg(i, l);
            i++;
            l--;
        }
    }

    private int findRun(int inicio, int n)
    {
        int fim;
        boolean decrescente;
        int num1, num2;

        fim = inicio + 1;
        decrescente = false;

        if(fim < n)
        {
            num1 = leNumero(inicio);
            num2 = leNumero(fim);
            comp++;

            if(num2 < num1)
                decrescente = true;

            if(decrescente)
            {
                while(fim < n && leNumero(fim) < leNumero(fim - 1))
                {
                    comp++;
                    fim++;
                }
                inverte(inicio, fim - 1);
            }
            else
            {
                while(fim < n && leNumero(fim) >= leNumero(fim - 1))
                {
                    comp++;
                    fim++;
                }
            }
        }

        return fim;
    }

    public void Tim_Sort()
    {
        int TL = filesize();
        int minRun = 32;
        int[][] vet = new int[TL][2];
        int[] aux = new int[TL];
        int i, top;
        boolean f;

        i = 0;
        top = 0;

        while(i < TL)
        {
            int runEnd = findRun(i, TL);
            int runLen = runEnd - i;

            if(runLen < minRun)
            {
                int fim = Math.min(i + minRun, TL);
                insertion_Sort(i, fim - 1);
                runEnd = fim;
            }

            vet[top][0] = i;
            vet[top][1] = runEnd;
            top++;
            i = runEnd;

            f = true;
            while(top > 1 && f)
            {
                int l1 = vet[top - 2][0];
                int r1 = vet[top - 2][1];
                int l2 = vet[top - 1][0];
                int r2 = vet[top - 1][1];
                int len1 = r1 - l1;
                int len2 = r2 - l2;

                if(len1 <= len2)
                {
                    fusao2(l1, r1 - 1, l2, r2 - 1, aux);
                    top--;
                    vet[top - 1][0] = l1;
                    vet[top - 1][1] = r2;
                }
                else
                    f = false;
            }
        }

        while(top > 1)
        {
            int l1 = vet[top - 2][0];
            int r1 = vet[top - 2][1];
            int l2 = vet[top - 1][0];
            int r2 = vet[top - 1][1];

            fusao2(l1, r1 - 1, l2, r2 - 1, aux);
            top--;
            vet[top - 1][0] = l1;
            vet[top - 1][1] = r2;
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
    private static class ResultadoCaso
    {
        long compProg;
        long movProg;
        long tempoMs;
        String compEqua;
        String movEqua;

        ResultadoCaso(long compProg, long movProg, long tempoMs, String compEqua, String movEqua)
        {
            this.compProg = compProg;
            this.movProg = movProg;
            this.tempoMs = tempoMs;
            this.compEqua = compEqua;
            this.movEqua = movEqua;
        }
    }

    private void preencherComVetor(int[] vet)
    {
        truncate(0);
        for (int v : vet)
            inserirRegNoFinal(new Registro(v));
    }

    private static int[] vetorOrdenado(int n)
    {
        int[] vet = new int[n];
        for (int i = 0; i < n; i++)
            vet[i] = i + 1;
        return vet;
    }

    private static int[] vetorReverso(int n)
    {
        int[] vet = new int[n];
        for (int i = 0; i < n; i++)
            vet[i] = n - i;
        return vet;
    }

    private static int[] vetorRandomico(int n)
    {
        Random random = new Random(12345);
        int[] vet = new int[n];
        for (int i = 0; i < n; i++)
            vet[i] = random.nextInt(1024) + 1;
        return vet;
    }

    private static void executarMetodo(Arquivo arq, String metodo)
    {
        switch (metodo)
        {
            case "Inserção Direta": arq.insercaoDireta(); break;
            case "Inserção Binária": arq.insercaoBinaria(); break;
            case "Seleção": arq.selecaoDireta(); break;
            case "Bolha": arq.bolha(); break;
            case "Shake": arq.shake(); break;
            case "Shell": arq.shellSort(); break;
            case "Heap": arq.Heap(); break;
            case "Quick s/ pivô": arq.QuicksemPivo(); break;
            case "Quick c/ pivô": arq.QuickcomPivo(); break;
            case "Merge 1ª Implement": arq.Merge_Sort(); break;
            case "Merge 2ª Implement": arq.Merge_Sort2(); break;
            case "Counting": arq.Counting_Sort(); break;
            case "Bucket": arq.Bucket_Sort(); break;
            case "Radix": arq.Radix_Sort(); break;
            case "Comb": arq.Comb_Sort(); break;
            case "Gnome": arq.Gnome_Sort(); break;
            case "Tim": arq.Tim_Sort(); break;
            default: throw new IllegalArgumentException("Método inválido: " + metodo);
        }
    }

    private static String calcCompEqua(String metodo, String caso, int n)
    {
        // Preencha aqui só para os métodos que possuem equação.
        // Do Shell em diante, pela sua regra, retorna "-".
        switch (metodo)
        {
            case "Shell":
            case "Heap":
            case "Quick s/ pivô":
            case "Quick c/ pivô":
            case "Merge 1ª Implement":
            case "Merge 2ª Implement":
            case "Counting":
            case "Bucket":
            case "Radix":
            case "Comb":
            case "Gnome":
            case "Tim":
                return "-";
            default:
                return "-"; // substitua pela fórmula da sua disciplina
        }
    }

    private static String calcMovEqua(String metodo, String caso, int n)
    {
        // Preencha aqui só para os métodos que possuem equação.
        // Do Shell em diante, pela sua regra, retorna "-".
        switch (metodo)
        {
            case "Shell":
            case "Heap":
            case "Quick s/ pivô":
            case "Quick c/ pivô":
            case "Merge 1ª Implement":
            case "Merge 2ª Implement":
            case "Counting":
            case "Bucket":
            case "Radix":
            case "Comb":
            case "Gnome":
            case "Tim":
                return "-";
            default:
                return "-"; // substitua pela fórmula da sua disciplina
        }
    }

    private static ResultadoCaso medir(String metodo, int[] dados, String caso, String nomeArquivo)
    {
        Arquivo arq = new Arquivo(nomeArquivo);
        arq.preencherComVetor(dados);
        arq.initComp();
        arq.initMov();

        long ini = System.nanoTime();
        executarMetodo(arq, metodo);
        long fim = System.nanoTime();

        return new ResultadoCaso(
                arq.getComp(),
                arq.getMov(),
                (fim - ini) / 1_000_000,
                calcCompEqua(metodo, caso, dados.length),
                calcMovEqua(metodo, caso, dados.length)
        );
    }

    public static String gerarTabela()
    {
        int n = 1024;

        String[] metodos = {
                "Inserção Direta", "Inserção Binária", "Seleção", "Bolha", "Shake",
                "Shell", "Heap", "Quick s/ pivô", "Quick c/ pivô",
                "Merge 1ª Implement", "Merge 2ª Implement",
                "Counting", "Bucket", "Radix", "Comb", "Gnome", "Tim"
        };

        int[] ord = vetorOrdenado(n);
        int[] rev = vetorReverso(n);
        int[] rnd = vetorRandomico(n);

        StringBuilder sb = new StringBuilder();

        sb.append(String.format(
                "%-20s | %-10s %-10s %-10s %-10s %-8s | %-10s %-10s %-10s %-10s %-8s | %-10s %-10s %-10s %-10s %-8s%n",
                "Método",
                "CProg", "CEqua", "MProg", "MEqua", "Tempo",
                "CProg", "CEqua", "MProg", "MEqua", "Tempo",
                "CProg", "CEqua", "MProg", "MEqua", "Tempo"
        ));

        sb.append("-".repeat(180)).append("\n");

        for (String metodo : metodos)
        {
            ResultadoCaso r1 = medir(metodo, ord, "ordenado", "tmp_ord_" + metodo + ".dat");
            ResultadoCaso r2 = medir(metodo, rev, "reverso",  "tmp_rev_" + metodo + ".dat");
            ResultadoCaso r3 = medir(metodo, rnd, "randomico","tmp_rnd_" + metodo + ".dat");

            sb.append(String.format(
                    "%-20s | %-10d %-10s %-10d %-10s %-8d | %-10d %-10s %-10d %-10s %-8d | %-10d %-10s %-10d %-10s %-8d%n",
                    metodo,
                    r1.compProg, r1.compEqua, r1.movProg, r1.movEqua, r1.tempoMs,
                    r2.compProg, r2.compEqua, r2.movProg, r2.movEqua, r2.tempoMs,
                    r3.compProg, r3.compEqua, r3.movProg, r3.movEqua, r3.tempoMs
            ));
        }

        return sb.toString();
    }
}
