package com.entitie;

public class Departamento {
    private String name;
    private int contagemPb;
    private int contagemCl;
    private int contagemTermica;
    private double faturamentoPb;
    private double faturamentoCl;
    private double faturamentoTermica;

    public Departamento(){}

    public void setName(String name){
        this.name = name;
    }

    public int getContagemPb(){
        return contagemPb;
    }

    public int getContagemCl(){
        return contagemCl;
    }

    public int getContagemTermica(){
        return contagemTermica;
    }

    public double getFaturamentoPb(){
        return faturamentoPb;
    }

    public double getFaturamentoCl(){
        return faturamentoCl;
    }

    public double getFaturamentoTermica(){
        return faturamentoTermica;
    }

    public void addContagemPb(int contagem){
        this.contagemPb += contagem;
    }

    public void addContagemCl(int contagem){
        this.contagemCl += contagem;
    }

    public void addContagemTermica(int contagem){
        this.contagemTermica += contagem;
    }

    public void addFaturamentoPb(int contagem){
        this.faturamentoPb += contagem * 0.0752;
    }

    public void addFaturamentoCl(int contagem){
        this.faturamentoCl += contagem * 0.627;
    }

    public void addFaturamentoTermica(int contagem){
        if("rh".equals(name)){
            double franquia = 130.00;
            if (contagem > 30) {
                this.faturamentoTermica += franquia + (contagem-30) * 0.0752;
            }
            else this.faturamentoTermica += franquia;
        } else this.faturamentoTermica += 0.0;
    }

    public String toStringContagem(){
        String nomeDepto = name != null ? name.toUpperCase() : "DESCONHECIDO";
        String formaFrase = "Departamento: " + nomeDepto;
        if (contagemCl+contagemPb+contagemTermica>0) {
            if(contagemPb>0) formaFrase += "\nContagemPb: " + contagemPb;
            if(contagemCl>0) formaFrase += "\nContagemCl: " + contagemCl;
            if(contagemTermica>0) formaFrase += "\nContagemTermica: " + contagemTermica;
        }
        else formaFrase += "\nSem cópias";
        return formaFrase + "\n";
    }

    public String toStringFaturamento(){
        String nomeDepto = name != null ? name.toUpperCase() : "DESCONHECIDO";
        String formaFrase = "Departamento: " + nomeDepto;

        double total = faturamentoPb + faturamentoCl + faturamentoTermica;

        if (total > 0.0) {
        if (faturamentoPb > 0) formaFrase += String.format("\nP&B: R$ %.2f", faturamentoPb);
        
        // CORRIGIDO: Agora usa o valor Colorido certo
        if (faturamentoCl > 0) formaFrase += String.format("\nColor: R$ %.2f", faturamentoCl);
        
        if (faturamentoTermica > 0) formaFrase += String.format("\nTermica: R$ %.2f", faturamentoTermica);
        
        
        formaFrase += String.format("\nTotal: R$ %.2f", total);
        }

        else {
            formaFrase += "\nNada a pagar";
        }
    
        return formaFrase + "\n";
    }
        
}
