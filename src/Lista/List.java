package Lista;
public class List {
    private Node ini, fim;
    int TL;

    public List() {
        TL = 0;
    }
    public void inicialise(){
        ini = fim = null;
        TL = 0;
    }
    public void insert(int info){
        Node nodo = new Node(info);
        if(ini == null){
            ini = fim = nodo;
        }
        else{
            fim.setProx(nodo);
            nodo.setAnt(fim);
            fim = nodo;
        }
        TL++;
    }
    public void exibi(){
        Node aux = ini;
        while (aux != null){
            System.out.print(aux.getInfo());
            aux = aux.getProx();
            if(aux != null)
                System.out.print(" - ");
        }
    }
    private Node posiciona(int pos){
        Node aux = ini;
        for(int i=0; aux!=null && i<pos;i++ )
            aux = aux.getProx();
        return aux;
    }

    //Algoritimos de Ordenação
    public void insercao_Direta(){
        Node pi = ini.getProx(), pos;
        int aux;

        while(pi != null){
            aux = pi.getInfo();
            pos = pi;

            while (pos != ini && aux < pos.getAnt().getInfo()){
                pos.setInfo(pos.getAnt().getInfo());
                pos = pos.getAnt();
            }
            pos.setInfo(aux);
            pi = pi.getProx();
        }
    }

    private Node busca_Binaria(int chave, int tl){
        int ini = 0, fim = tl-1, meio = fim/2;
        while(ini < fim && chave != posiciona(meio).getInfo()){
            if(chave > posiciona(meio).getInfo())
                ini = meio + 1;
            else
                fim = meio - 1;
            meio = (ini+fim)/2;
        }
        if(chave > posiciona(meio).getInfo())
            return posiciona(meio).getProx();
        return posiciona(meio);
    }

    public void insercao_Binaria(){
        int aux;
        Node pnt, pos;

        pnt = ini;
        for(int i = 1; i<TL; i++){
            aux = pnt.getInfo();
            pos = busca_Binaria(aux, i);
            for(Node j=pnt; j!=pos; j=j.getAnt())
                j.setInfo(j.getAnt().getInfo());
            pos.setInfo(aux);
            pnt = pnt.getProx();
        }
    }

    public void selecao_Direta(){
        Node pmenor,i = ini;
        int menor;

        while(i.getProx() != null){
            menor = i.getInfo();
            pmenor = i;
            for(Node j = i.getProx(); j!=null; j=j.getProx()){
                if(j.getInfo()<menor){
                    menor = j.getInfo();
                    pmenor = j;
                }
            }
            pmenor.setInfo(i.getInfo());
            i.setInfo(menor);
            i = i.getProx();
        }
    }

    public void Bolha(){
        Node TL = fim;
        int aux;
        boolean flag = true;
        while(TL != ini && flag){
            flag = false;
            for (Node pos = ini; pos != TL; pos=pos.getProx()){
                if(pos.getInfo()>pos.getProx().getInfo()){
                    aux = pos.getInfo();
                    pos.setInfo(pos.getProx().getInfo());
                    pos.getProx().setInfo(aux);
                    flag = true;
                }
            }
            TL = TL.getAnt();
        }
    }

    public void Shake(){
        Node tlini = ini, tlfim = fim, pos;
        int aux;
        boolean flag = true;
        while(tlini != tlfim && flag){
            flag = false;
            for (pos = tlini; pos != tlfim; pos=pos.getProx()){
                if(pos.getInfo()>pos.getProx().getInfo()){
                    aux = pos.getInfo();
                    pos.setInfo(pos.getProx().getInfo());
                    pos.getProx().setInfo(aux);
                    flag = true;
                }
            }
            tlfim = tlfim.getAnt();
            if(flag){
                flag = false;
                for (pos = tlfim; pos != tlini; pos=pos.getAnt()){
                    aux = pos.getInfo();
                    pos.setInfo(pos.getAnt().getInfo());
                    pos.getAnt().setInfo(aux);
                }
                tlini = tlini.getProx();
            }
        }
    }

    public void Shell_Sort(){
        int dist=1,aux,pos;
        Node p, p2;

        while(dist < TL)
            dist = 3*dist+1;
        dist = dist/3;

        while(dist>0){
            for (int i = dist;i<TL;i++){
                p = posiciona(i);
                aux = p.getInfo();
                pos = i - dist;
                p2 = posiciona(pos);
                while(p != ini && p2.getInfo()>aux){
                    p.setInfo(p2.getInfo());
                    p = p2;
                    pos = pos - dist;
                    p2 = posiciona(pos);
                }
                p.setInfo(aux);
            }
            dist = dist/3;
        }
    }

    public void Heap_Sort(){
        int tl, pos, aux;
        Node f1, f2, pai, maiorF;

        for (tl = TL; tl>1;tl--){
            exibi();
            System.out.println();
            pos = tl/2-1;
            pai = posiciona(pos);
            while (pai != null){
                f1 = posiciona(pos*2+1);
                f2 = f1.getProx();
                maiorF = f1;
                Node comp = posiciona(tl);
                if(f2 != comp && f1.getInfo() < f2.getInfo())
                    maiorF = f2;
                if (maiorF.getInfo() > pai.getInfo()) {
                    aux = maiorF.getInfo();
                    maiorF.setInfo(pai.getInfo());
                    pai.setInfo(aux);
                }
                pai = pai.getAnt();
                pos--;
            }
            aux = ini.getInfo();
            pai = posiciona(tl-1);
            ini.setInfo(pai.getInfo());
            pai.setInfo(aux);
        }
    }

    public void QuicksemPivo(){
        QuickSP(0,TL-1);
    }

    public void QuickcomPivo(){
        QuickCP(0,TL-1);
    }

    private void QuickSP(int inicio, int fim){
        int i = inicio, j = fim, aux;
        boolean flag = true;
        Node pi = posiciona(i), pj = posiciona(j);

        while (i<j){
            if(flag)
                while (i<j && pi.getInfo() <= pj.getInfo()) {
                    i++;
                    pi = pi.getProx();
                }
            else
                while (i<j && pi.getInfo() <= pj.getInfo()) {
                    j--;
                    pj = pj.getAnt();
                }
            aux = pi.getInfo();
            pi.setInfo(pj.getInfo());
            pj.setInfo(aux);
            flag = !flag;
        }
        if (inicio < i-1)
            QuickSP(inicio, i-1);
        if (j+1 < fim)
            QuickSP(j+1,fim);
    }

    private void QuickCP(int inicio, int fim){
        int i = inicio, j = fim, aux, pivo;
        Node pi = posiciona(i), pj = posiciona(j);
        pivo = posiciona((i+j)/2).getInfo();

        while (i<=j){
            while (i<=j && pi.getInfo() < pivo) {
                i++;
                pi = pi.getProx();
            }
            while (i<=j && pivo < pj.getInfo()) {
                j--;
                pj = pj.getAnt();
            }
            if (i<=j){
                aux = pi.getInfo();
                pi.setInfo(pj.getInfo());
                pj.setInfo(aux);
                i++;
                j--;
                if (pi!=null)
                    pi = pi.getProx();
                if (pj!=null)
                    pj = pj.getAnt();
            }

        }
        if (inicio < j)
            QuickCP(inicio, j);
        if (i < fim)
            QuickCP(i,fim);
    }

    public void Counting_Sort(){
        int maior;
        Node aux = ini;
        maior = aux.getInfo();
        while (aux != null){
            if(maior < aux.getInfo())
                maior = aux.getInfo();
            aux = aux.getProx();
        }
        int[] vetCont = new int[++maior];
        int[] vetFim = new int[TL];

        aux = ini;
        while(aux != null){
            vetCont[aux.getInfo()]++;
            aux = aux.getProx();
        }
        for (int i = 1; i < maior; i++){
            vetCont[i] += vetCont[i-1];
        }
        aux = fim;
        while (aux.getAnt() != null){
            vetFim[--vetCont[aux.getInfo()]] = aux.getInfo();

            aux = aux.getAnt();
        }
        vetFim[--vetCont[aux.getInfo()]] = aux.getInfo();
        for (int i=0;i<TL;i++){
            aux.setInfo(vetFim[i]);
            aux = aux.getProx();
        }
    }

    public void Bucket_Sort(){

    }

}