package com.entitie;


public class Rateio{

    
    // private int contagemPb;
    // private int contagemCl;
    // private int contagemTermica;

    //     //System.out.printf("Total Pb: %.2f\nTotal Cl: %.2f\nTotal termica: %.2f\nTotal: %.2f\n", faturamentoPb(contagemPb), faturamentoCl(contagemCl), faturamentoTermica(contagemTermica), faturamentoTotal);

    // public Rateio(int contagemPb, int contagemCl, int contagemTermica){
    //     this.contagemPb = contagemPb;
    //     this.contagemCl = contagemCl;
    //     this.contagemTermica = contagemTermica;
    // }
    
    public double faturamentoPb(int contagem){
        double franquia = 6768.00;
        if(contagem > 90000)
            return franquia + (contagem - 90000) * 0.0752;
        else return franquia;
    }

    public double faturamentoCl(int contagem){
        return contagem * 0.627;
    }

    public double faturamentoTermica(int contagem){
        double franquia = 130.00;
        if (contagem > 30) {
            return franquia + (contagem-30) * 0.0752;
        }
        else return franquia;
    }

    public double faturamentoTotal(int contagemPb, int contagemCl, int contagemTermica){
        return faturamentoPb(contagemPb) + faturamentoCl(contagemCl) + faturamentoTermica(contagemTermica);
    }
}