package com.planilha;
import com.entitie.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.text.Normalizer;
import java.util.Scanner;

public class ColetaDados {

    public static void main(String[] args) {
        // Scanner sc = new Scanner(System.in);
        // String planilha = sc.nextLine();
        String caminhoArquivo = "C:\\Users\\aigomes\\OneDrive - ACCO Brands Corporation\\Documents\\cod\\java\\Rateio\\202605.xlsx";

        //Rateio rateio = new Rateio();
        
        Departamento[] departamento = new Departamento[13];
        Departamento total = new Departamento();
        total.setName("total");
        int franquiaPbCount = 90000;
        double franquiaPbCost = 6768.00;

        for (int i = 0; i < departamento.length; i++) {
            departamento[i] = new Departamento();
            if(i == departamento.length-1)
                departamento[i].setName("kensington");
        }

        try (FileInputStream file = new FileInputStream(new File(caminhoArquivo));
             Workbook workbook = new XSSFWorkbook(file)) {
             
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();


            System.out.println("Processando contagem por departamento...\n");    
            
            for(int i = 93; i <= 138; i++){
                Row row = sheet.getRow(i);
    
                if (row == null) continue; 

                Cell cellDpt = row.getCell(2);
                Cell cellType = row.getCell(9);
                Cell cellCount = row.getCell(12);

                String departamentoCru = formatter.formatCellValue(cellDpt);
                String tipoCopiaCru = formatter.formatCellValue(cellType);
                
                // Limpa acentos, espaços e joga tudo para minúsculo para facilitar a comparação
                String auxDepartamento = removerAcentos(departamentoCru).trim().toLowerCase();
                String tipoCopia = removerAcentos(tipoCopiaCru).trim().toLowerCase();

                int contagem = 0;
                if (cellCount != null) {
                    contagem = (int) cellCount.getNumericCellValue();
                }

                int deptoAtual = -1;

                switch (auxDepartamento) {
                    case "apoio":  deptoAtual = 0;  break;
                    case "cedis":  deptoAtual = 1;  break;
                    case "rh":     deptoAtual = 2;     break;
                    case "vendas": deptoAtual = 3; break;
                    case "pcpalm": deptoAtual = 4; break;
                    case "diradm": deptoAtual = 5; break;
                    case "financ": deptoAtual = 6; break;
                    case "infra":  deptoAtual = 7;  break;
                    case "market": deptoAtual = 8; break;
                    case "qualid": deptoAtual = 9; break;
                    case "unid1":  deptoAtual = 10;  break;
                    case "tilisp": deptoAtual = 11; break;
                }

                if (deptoAtual != -1 && departamento[deptoAtual] != null) {
                    departamento[deptoAtual].setName(auxDepartamento);
                    if (tipoCopia.equals("p&b") || tipoCopia.equals("pb")) {
                        departamento[deptoAtual].addContagemPb(contagem);
                        //total.addFaturamentoPb(contagem);
                    } 
                    else if (tipoCopia.equals("color") || tipoCopia.equals("colorido")) {
                        departamento[deptoAtual].addContagemCl(contagem);
                        //total.addFaturamentoCl(contagem);
                    }   
                    else if (tipoCopia.equals("termica")) {
                        departamento[deptoAtual].addContagemTermica(contagem);
                        //total.addFaturamentoTermica(contagem);
                    }
                }
            }

            calcularCustos(departamento, total);
            
            

            
            
            System.out.println(total.toStringContagem());
            //System.out.println(total.toStringFaturamento());
            
            System.out.printf("Color: %.2f\n",total.getFaturamentoClTotal());
            System.out.printf("PB: %.2f\n",total.getFaturamentoPbTotal());
            System.out.printf("Termica: %.2f\n",total.getFaturamentoTermicaTotal());
            System.out.printf("Total: %.2f\n",total.getFaturamentoTotal());
            System.out.println();

            if(total.getContagemPb() < franquiaPbCount)
                calcularResidual(departamento,total, franquiaPbCost);

            for(int i = 0;i<departamento.length-1;i++)
                 System.out.println(departamento[i].toStringContagem());
            
            for(int i = 0;i<departamento.length-1;i++){
                departamento[i].setFaturamentoTotalDpto();
                System.out.println(departamento[i].toStringFaturamento());
            }

            dividirGastosKensinTilisp(departamento);
            

        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo Excel: " + e.getMessage());
        }

    }

    static void calcularCustos(Departamento[] departamento, Departamento total){
        for(int i = 0; i < departamento.length - 1; i++){
                departamento[i].addFaturamentoCl(departamento[i].getContagemCl());
                departamento[i].addFaturamentoTermica(departamento[i].getContagemTermica()) ;
                departamento[i].addFaturamentoPb(departamento[i].getContagemPb());
                
                total.addContagemCl(departamento[i].getContagemCl());
                total.addContagemTermica(departamento[i].getContagemTermica());
                total.addContagemPb(departamento[i].getContagemPb());

                total.addFaturamentoPbTotal(departamento[i].getFaturamentoPb());
                total.addFaturamentoClTotal(departamento[i].getFaturamentoCl());
                total.addFaturamentoTermicaTotal(departamento[i].getFaturamentoTermica());
                total.setFaturamentoTotal();
        }
        

    }
    
    static void calcularResidual(Departamento[] departamento, Departamento total, double franquiaCost){
         double totalCostPb = total.getFaturamentoPbTotal() + total.getFaturamentoTermicaTotal();
         
        for(int i = 0;i<departamento.length-1;i++){
                //System.out.println(departamento[i].toStringFaturamento());
                departamento[i].setProporcaoResidual(totalCostPb);
                departamento[i].setValorResidual(franquiaCost - total.getFaturamentoPbTotal());
                departamento[i].setFaturamentoTotalComResidual();
                System.out.println(departamento[i].getName());
                System.out.printf("Proporcao residual: %.2f%%\n",departamento[i].getProporcaoResidual()*100);
                System.out.printf("Valor residual: R$%.2f\n",departamento[i].getValorResidual());
                System.out.printf("Total com residual: R$%.2f\n",departamento[i].getFaturamentoComResidual());
            }
    
    }

    static void dividirGastosKensinTilisp(Departamento[] departamento){
        System.out.printf("Kesington: %.2f\n", departamento[11].getFaturamentoTotalDpto()*0.7);
        System.out.printf("Tilisp: %.2f\n", departamento[11].getFaturamentoTotalDpto()*0.3);
    }



    // FUNÇÃO PARA REMOVER ACENTOS
    public static String removerAcentos(String texto) {
        if (texto == null) {
            return "";
        }
        String textoNormalizado = Normalizer.normalize(texto, Normalizer.Form.NFD);
        return textoNormalizado.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
    }
}