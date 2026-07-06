package com.entitie;

public class Departamento {
    private String name;
    private int contagemPb;
    private int contagemCl;
    private int contagemTermica;
    private double faturamentoPb;
    private double faturamentoCl;
    private double faturamentoTermica;
    private double proporcaoResidual;
    private double valorResidual;
    private double totalPbComResidual;
    private double faturamentoClTotal;
    private double faturamentoPbTotal;
    private double faturamentoTermicaTotal;
    private double faturamentoTotal;
    private double faturamentoComResidual;
    private double faturamentoTotalDpto;

    public Departamento(){}

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
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

    public int getContagemTotal(){
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

    public double getProporcaoResidual(){
        return proporcaoResidual;
    }

    public double getValorResidual(){
        return valorResidual;
    }

    public double getTotalPbComResidual(){
        return totalPbComResidual;
    }

    public double getFaturamentoClTotal(){
        return faturamentoClTotal;
    }

    public double getFaturamentoPbTotal(){
        return faturamentoPbTotal;
    }

    public double getFaturamentoTermicaTotal(){
        return faturamentoTermicaTotal;
    }

    public double getFaturamentoTotal(){
        return faturamentoTotal;
    }

    public double getFaturamentoComResidual(){
        return faturamentoComResidual;
    }

    public double getFaturamentoTotalDpto(){
        return faturamentoTotalDpto;
    }

    public void setFaturamentoTotalDpto(){
        if(valorResidual > 0)
        this.faturamentoTotalDpto = faturamentoCl + faturamentoComResidual + faturamentoTermica;
        else this.faturamentoTotalDpto = faturamentoCl + faturamentoPb + faturamentoTermica;
    }

    public void setFaturamentoTotal(){
        this.faturamentoTotal = faturamentoClTotal + faturamentoPbTotal + faturamentoTermicaTotal;
    }


    public void setValorResidual(double valorTotalPb){
        this.valorResidual = valorTotalPb * proporcaoResidual;
    }

    public void setProporcaoResidual(double valorTotalPb){
        if(faturamentoTermica > 0)
        this.proporcaoResidual = (faturamentoPb + faturamentoTermica)/valorTotalPb;
        else this.proporcaoResidual = faturamentoPb/valorTotalPb;
    }
    
    public void setFaturamentoTotalComResidual(){
        this.faturamentoComResidual = faturamentoPb + valorResidual;
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

    public void addFaturamentoClTotal(double faturamento){
        this.faturamentoClTotal += faturamento;
    }

    public void addFaturamentoPbTotal(double faturamento){
       this.faturamentoPbTotal += faturamento;
    }

    public void addFaturamentoTermicaTotal(double faturamento){
        this.faturamentoTermicaTotal += faturamento;
    }

    public void addFaturamentoTermica(int contagem){
        if("rh".equals(name) || "total".equals(name)){
            double franquia = 130.00;
            if (contagem > 30) {
                this.faturamentoTermica += franquia + (contagem-30) * 0.0752;
            }
            else this.faturamentoTermica += franquia;
        } else this.faturamentoTermica += 0.0;
    }

    public String toStringContagem(){
        String nomeDepto = name != null ? name.toUpperCase() : "DESCONHECIDO";
        String formaFrase = nomeDepto + ": ";
        if (contagemCl+contagemPb+contagemTermica>0) {
            if(contagemPb>0) formaFrase += "\nP&B: " + contagemPb;
            if(contagemCl>0) formaFrase += "\nColor: " + contagemCl;
            if(contagemTermica>0) formaFrase += "\nTermica: " + contagemTermica;
        }
        else formaFrase += "\nSem cópias";
        return formaFrase + "\n";
    }

    public String toStringFaturamento(){
        String nomeDepto = name != null ? name.toUpperCase() : "DESCONHECIDO";
        String formaFrase = nomeDepto + ": ";

        if (faturamentoTotalDpto > 0.0) {
        if (faturamentoPb > 0) formaFrase += String.format("\nP&B: R$ %.2f", faturamentoPb);
        if (faturamentoCl > 0) formaFrase += String.format("\nColor: R$ %.2f", faturamentoCl);
        if (faturamentoTermica > 0) formaFrase += String.format("\nTermica: R$ %.2f", faturamentoTermica);
        formaFrase += String.format("\nTotal: R$ %.2f", faturamentoTotalDpto);
        }

        else {
            formaFrase += "\nNada a pagar";
        }
    
        return formaFrase + "\n";
    }
        
}
