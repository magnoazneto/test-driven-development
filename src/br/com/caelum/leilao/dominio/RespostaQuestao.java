package br.com.caelum.leilao.dominio;

public class RespostaQuestao {

    private Integer nota;

    public RespostaQuestao(Integer nota) {
        if(nota == null){
            throw new IllegalArgumentException("Nota não pode ser nula");
        }
        if (nota < 0) {
            throw new IllegalArgumentException("A nota não pode ser menor que zero");
        }
        if (nota > 10) {
            throw new IllegalArgumentException("A nota não pode ser maior que10");
        }
        this.nota = nota;
    }

    public int getNota() {
        return nota;
    }
}
