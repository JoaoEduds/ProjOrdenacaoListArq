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
                p = ini;
                for (pos = i;pos > 0; pos--){
                    p = p.getProx();
                }
                aux = p.getInfo();
                p2 = p;
                for (pos = dist;p2 != null && pos > 0; pos--){
                    p2 = p2.getAnt();
                }
                while(p != ini && p2.getInfo()>aux){
                    p.setInfo(p2.getInfo());
                    p = p2;
                    for (pos = dist;p2 != null && pos > 0; pos--){
                        p2 = p2.getAnt();
                    }
                }
                p.setInfo(aux);
            }
            dist = dist/3;
        }
    }
}
