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

    //### Algoritimos de Ordenação ###
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

    public void insercao_Binaria(){
        int aux;
        Node pnt, pos;

        pnt = ini.getProx();
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

    public void Merge_Sort(){
        int seq = 1;

        while (seq<TL){
            List l1 = new List();
            List l2 = new List();

            particao(l1,l2);
            fusao(l1,l2,seq);

            seq = seq * 2;
        }
    }

    private void particao(List l1, List l2){
        int meio = TL/2;
        Node aux1=ini, aux2=posiciona(meio);
        for(int i = 0; i<meio; i++){
            l1.insert(aux1.getInfo());
            l2.insert(aux2.getInfo());  
            aux1 = aux1.getProx();
            aux2 = aux2.getProx();
        }
    }

    private void fusao(List l1, List l2, int seq){
        int i,j,tam_seq=seq;
        Node atual = ini, aux1, aux2;
        aux1 = l1.ini;
        aux2 = l2.ini;
        i = j = 0;
        while (atual != null){
            while (i<seq && j<seq && aux1!=null  && aux2!=null){
                if(aux1.getInfo() < aux2.getInfo()) {
                    atual.setInfo(aux1.getInfo());
                    aux1 = aux1.getProx();
                    i++;
                }
                else {
                    atual.setInfo(aux2.getInfo());
                    aux2 = aux2.getProx();
                    j++;
                }
                atual = atual.getProx();
            }
            while (i<seq && aux1!=null){
                atual.setInfo(aux1.getInfo());
                aux1 = aux1.getProx();
                i++;
                atual = atual.getProx();
            }
            while (j<seq && aux2!=null){
                atual.setInfo(aux2.getInfo());
                aux2 = aux2.getProx();
                j++;
                atual = atual.getProx();
            }
            seq = seq+tam_seq;
        }
    }

    private void fusao2(int ini1, int fim1, int ini2, int fim2, int aux[]){
        int i = ini1, j = ini2, k = 0;
        Node p1,p2;
        p1 = posiciona(i);
        p2 = posiciona(j);
        while (i<=fim1 && j<=fim2) {
            if (p1.getInfo() <= p2.getInfo()) {
                aux[k++] = p1.getInfo();
                p1 = p1.getProx();
                i++;
            } else {
                aux[k++] = p2.getInfo();
                p2 = p2.getProx();
                j++;
            }
        }
        while (i<=fim1) {
            aux[k++] = p1.getInfo();
            p1 = p1.getProx();
            i++;
        }
        while (j<=fim2) {
            aux[k++] = p2.getInfo();
            p2 = p2.getProx();
            j++;
        }
        p1 = posiciona(ini1);
        for (i=0; i<k; i++) {
            p1.setInfo(aux[i]);
            p1 = p1.getProx();
        }
    }

    private void merge(int esq, int dir, int aux[]){
        if(esq<dir){
            int meio = (esq + dir)/2;
            merge(esq,meio,aux);
            merge(meio+1,dir,aux);
            fusao2(esq,meio,meio+1,dir,aux);
        }
    }

    public void Merge_Sort2(){
        int aux[] = new int[TL];
        merge(0,TL-1,aux);
    }

    public void Counting_Sort(){
        int maior;
        Node aux = ini.getProx();
        maior = ini.getInfo();
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
        int buckets = (int)Math.sqrt(TL), maior, setor, pos;
        List[] vetL = new List[buckets];
        Node aux = ini.getProx(), atual;

        for(int i = 0; i < buckets; i++)
            vetL[i] = new List();

        maior = ini.getInfo();
        while (aux != null){
            if(maior < aux.getInfo())
                maior = aux.getInfo();
            aux = aux.getProx();
        }
        setor = maior/buckets+1;
        aux = ini;
        while (aux != null) {
            pos = aux.getInfo()/setor;
            vetL[pos].insert(aux.getInfo());
            aux = aux.getProx();
        }
        atual = ini;
        for(int i=0; i<buckets; i++) {
            vetL[i].insercao_Direta();
            aux = vetL[i].ini;
            while (aux!=null){
                atual.setInfo(aux.getInfo());
                atual = atual.getProx();
                aux = aux.getProx();
            }
        }
    }

    public void Radix_Sort(){
        int maior, temp;
        Node aux = ini.getProx();
        List[] buckets = new List[10];

        for (int i = 0; i < 10; i++) {
            buckets[i] = new List();
        }

        maior = ini.getInfo();
        while (aux != null){
            if(maior < aux.getInfo())
                maior = aux.getInfo();
            aux = aux.getProx();
        }

        for (int x=1; maior/x > 0; x = x * 10){
            aux = ini;
            while (aux != null){
                int dig = (aux.getInfo()/x)%10;
                buckets[dig].insert(aux.getInfo());
                aux = aux.getProx();

            }
            aux = ini;
            for (int i = 0; i < 10; i++) {
                while (buckets[i].ini != null) {
                    aux.setInfo(buckets[i].ini.getInfo());
                    aux = aux.getProx();
                    buckets[i].ini = buckets[i].ini.getProx();
                }
            }
        }
    }

    public void Comb_Sort(){
        int inter = TL, aux;
        boolean f = true;
        Node i, j;

        while (inter>1 || f){
            inter = inter*10/13;
            if (inter == 9 || inter == 10)
                inter = 11;
            if (inter < 1)
                inter = 1;
            f = false;
            i = ini;
            j = posiciona(inter);
            while (j != null){
                if (i.getInfo() > j.getInfo()){
                    aux = i.getInfo();
                    i.setInfo(j.getInfo());
                    j.setInfo(aux);
                    f = true;
                }
                i = i.getProx();
                j = j.getProx();
            }
        }
    }

    public void Gnome_Sort(){
        Node gnomo = ini;

        while (gnomo.getProx() != null){
            if(gnomo.getInfo() > gnomo.getProx().getInfo()){
                int aux = gnomo.getInfo();
                gnomo.setInfo(gnomo.getProx().getInfo());
                gnomo.getProx().setInfo(aux);
                if (gnomo == ini)
                    gnomo = gnomo.getProx();
                else
                    gnomo = gnomo.getAnt();
            }
            else
                gnomo = gnomo.getProx();
        }
    }

    private void insertion_Sort(int esq, int dir){
        Node pi = posiciona(esq).getProx(), pos, pesq = posiciona(esq), pdir = posiciona(dir);
        int aux;

        while(pi != pdir.getProx()){
            aux = pi.getInfo();
            pos = pi;

            while (pos != pesq && aux < pos.getAnt().getInfo()){
                pos.setInfo(pos.getAnt().getInfo());
                pos = pos.getAnt();
            }
            pos.setInfo(aux);
            pi = pi.getProx();
        }
    }

    private void inverte(int i, int l){
        Node pi = posiciona(i), pl = posiciona(l);
        while (i<l){
            int aux = pi.getInfo();
            pi.setInfo(pl.getInfo());
            pl.setInfo(aux);
            i++;
            l--;
        }
    }

    private int findRun(int inicio, int n){
        int fim = inicio+1;
        if (fim == n)
            return fim;
        Node pfim = posiciona(fim);
        if(pfim.getInfo() < posiciona(inicio).getInfo()) {
            while (fim < n && pfim != null && pfim.getInfo() < pfim.getAnt().getInfo()) {
                fim++;
                pfim = pfim.getProx();
            }
            inverte(inicio, fim-1);
        } else
            while (fim < n && pfim != null && pfim.getInfo() >= pfim.getAnt().getInfo()){
                fim++;
                pfim = pfim.getProx();
            }
        return fim;
    }

    public void Tim_Sort(){
        int minRun = 32;
        int[][] vet = new int[TL][2];
        int[] aux = new int[TL];

        int i = 0;
        int top = 0;
        while (i < TL) {
            int runEnd = findRun(i, TL);
            int runLen = runEnd - i;

            // Extend short runs to minRun using insertion sort
            if (runLen < minRun) {
                int fim = Math.min(i + minRun, TL);
                insertion_Sort(i, fim - 1);
                runEnd = fim;
            }
            vet[top][0] = i;
            vet[top++][1] = runEnd;
            i = runEnd;

            // Maintain merge balance
            boolean f = true;
            while (top > 1 && f) {
                int l1 = vet[top - 2][0];
                int r1 = vet[top - 2][1];
                int l2 = vet[top - 1][0];
                int r2 = vet[top - 1][1];

                int len1 = r1 - l1;
                int len2 = r2 - l2;

                if (len1 <= len2) {
                    fusao2(l1, r1 - 1, l2,r2 - 1, aux);
                    top--;

                    vet[top - 1][0] = l1;
                    vet[top - 1][1] = r2;
                }
                else
                    f = false;
            }
        }
        while (top > 1) {
            int l1 = vet[top - 2][0];
            int r1 = vet[top - 2][1];
            int l2 = vet[top - 1][0];
            int r2 = vet[top - 1][1];

            fusao2(l1, r1 - 1, l2,r2 - 1, aux);
            top--;
            vet[top - 1][0] = l1;
            vet[top - 1][1] = r2;
        }
    }
}