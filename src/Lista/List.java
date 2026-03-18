package Lista;

public class List {
    private Node ini, fim;

    public List() {}

    public void inicialise(){
        ini = null;
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
    }

    public void exibi(){
        Node aux = ini;
        while (aux != null){
            System.out.println(aux.getInfo());
            aux = aux.getProx();
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

    public void
}
