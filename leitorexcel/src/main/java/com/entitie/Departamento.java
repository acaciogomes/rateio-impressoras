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
    
    public void addContagemPb(int contagem){
        this.contagemPb += contagem;
    }

    public int getContagemCl(){
        return contagemCl;
    }
    
    public void addContagemCl(int contagem){
        this.contagemCl += contagem;
    }

    public int getContagemTermica(){
        return contagemTermica;
    }
    
    public void addContagemTermica(int contagem){
        this.contagemTermica += contagem;
    }

    public int getContagemTotal(){
        return contagemTermica;
    }

    public double getFaturamentoPb(){
        return faturamentoPb;
    }
    
    public void addFaturamentoPb(int contagem){
        this.faturamentoPb += contagem * 0.0752;
    }

    public double getFaturamentoCl(){
        return faturamentoCl;
    }
    
    public void addFaturamentoCl(int contagem){
        this.faturamentoCl += contagem * 0.627;
    }

    public double getFaturamentoTermica(){
        return faturamentoTermica;
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

    public double getProporcaoResidual(){
        return proporcaoResidual;
    }
    
    public void setProporcaoResidual(double valorTotalPb){
        if(faturamentoTermica > 0)
        this.proporcaoResidual = (faturamentoPb + faturamentoTermica)/valorTotalPb;
        else this.proporcaoResidual = faturamentoPb/valorTotalPb;
    }

    public double getValorResidual(){
        return valorResidual;
    }
    
    public void setValorResidual(double valorTotalPb){
        this.valorResidual = valorTotalPb * proporcaoResidual;
    }

    public double getTotalPbComResidual(){
        return totalPbComResidual;
    }

    public double getFaturamentoClTotal(){
        return faturamentoClTotal;
    }
    
    public void addFaturamentoClTotal(double faturamento){
        this.faturamentoClTotal += faturamento;
    }

    public double getFaturamentoPbTotal(){
        return faturamentoPbTotal;
    }
    
    public void addFaturamentoPbTotal(double faturamento){
       this.faturamentoPbTotal += faturamento;
    }

    public double getFaturamentoTermicaTotal(){
        return faturamentoTermicaTotal;
    }
    
    public void addFaturamentoTermicaTotal(double faturamento){
        this.faturamentoTermicaTotal += faturamento;
    }

    public double getFaturamentoTotal(){
        return faturamentoTotal;
    }
    
    public void setFaturamentoTotal(){
        this.faturamentoTotal = faturamentoClTotal + faturamentoPbTotal + faturamentoTermicaTotal;
    }

    public double getFaturamentoComResidual(){
        return faturamentoComResidual;
    }
    
    public void setFaturamentoTotalComResidual(){
        this.faturamentoComResidual = faturamentoPb + valorResidual;
    }

    public double getFaturamentoTotalDpto(){
        return faturamentoTotalDpto;
    }

    public void setFaturamentoTotalDpto(){
        if(valorResidual > 0)
        this.faturamentoTotalDpto = faturamentoCl + faturamentoComResidual + faturamentoTermica;
        else this.faturamentoTotalDpto = faturamentoCl + faturamentoPb + faturamentoTermica;
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
        } else formaFrase += "\nNada a pagar";
        
        return formaFrase + "\n";
    }
    
    public String toStringFaturamentoTotal(){
        String formaFrase = "Total: ";

        if (faturamentoTotal > 0.0) {
        if (faturamentoPbTotal > 0) formaFrase += String.format("\nP&B: R$ %.2f", faturamentoPbTotal);
        if (faturamentoClTotal > 0) formaFrase += String.format("\nColor: R$ %.2f", faturamentoClTotal);
        if (faturamentoTermicaTotal > 0) formaFrase += String.format("\nTermica: R$ %.2f", faturamentoTermicaTotal);
        formaFrase += String.format("\nTotal: R$ %.2f", faturamentoTotal);
        }else formaFrase += "\nNada a pagar";

        return formaFrase + "\n";
    }

    public String toStringResidual(){
        String nomeDepto = name != null ? name.toUpperCase() : "DESCONHECIDO";
        String formaFrase = nomeDepto + ": ";

        if (proporcaoResidual > 0.0) {
        formaFrase += "Proporcao Residual: " + String.format("%.2f%%", proporcaoResidual*100) + String.format("\nValor residual: R$ %.2f", valorResidual);
        } else formaFrase += "\nNada a pagar";
        
        return formaFrase + "\n";
    }

    public String toStringTotalPorDpto(){
        if(faturamentoTotalDpto>0){ 
            return name + ": " + String.format("R$ %.2f", faturamentoTotalDpto) + "\n";
        }
        else return name + ": Nada a pagar\n";
    }
        
}
