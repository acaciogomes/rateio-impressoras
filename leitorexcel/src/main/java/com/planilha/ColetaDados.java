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
        for (int i = 0; i < departamento.length; i++) {
            departamento[i] = new Departamento();
            if(i == departamento.length-1)
                departamento[i].setName("kensington");
        }

        try (FileInputStream file = new FileInputStream(new File(caminhoArquivo));
             Workbook workbook = new XSSFWorkbook(file)) {
             
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();

            int total = 0;
            int totalPb = 0;
            int totalCl = 0;
            int totalTermica = 0;
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
                        totalPb += contagem;
                    } 
                    else if (tipoCopia.equals("color") || tipoCopia.equals("colorido")) {
                        departamento[deptoAtual].addContagemCl(contagem);
                        totalCl += contagem;
                    }   
                    else if (tipoCopia.equals("termica")) {
                        departamento[deptoAtual].addContagemTermica(contagem);
                        totalTermica += contagem;
                    }
                }

                total += contagem;
            }
            
            System.out.println("========== RELAT0RIO DE IMPRESSÕES ==========");
            System.out.println("TOTAL GERAL DA PLANILHA: " + total + "\n");
            System.out.println("TOTAL P&B: " + totalPb + "\n");
            System.out.println("TOTAL COLORIDA: " + totalCl + "\n");
            System.out.println("TOTAL TERMICA: " + totalTermica + "\n");

            for(int i = 0;i<departamento.length-1;i++)
                System.out.println(departamento[i].toStringContagem());
            
            for(int i = 0; i < departamento.length - 1; i++){
                departamento[i].addFaturamentoPb(departamento[i].getContagemPb());
                departamento[i].addFaturamentoCl(departamento[i].getContagemCl());
                departamento[i].addFaturamentoTermica(departamento[i].getContagemTermica());
            }


            System.out.println("========== RATEIO DE CUSTO ==========");
            
            double totalCostPb = 0;
            double totalCostCl = 0;
            double totalCostTermica  = 0;

            for(int i = 0;i<departamento.length-1;i++){
                System.out.println(departamento[i].toStringFaturamento());
                totalCostPb += departamento[i].getFaturamentoPb();
                totalCostCl += departamento[i].getFaturamentoCl();
                totalCostTermica += departamento[i].getFaturamentoTermica();
            }
            
            double totalGeral = totalCostPb + totalCostCl + totalCostTermica;
            
            System.out.println("---------------------------------------------\n");
            System.out.printf("CUSTO TOTAL P&B: R$ %.2f\n", totalCostPb);
            System.out.printf("CUSTO TOTAL COLOR: R$ %.2f\n", totalCostCl);
            System.out.printf("CUSTO TOTAL TÉRMICA: R$ %.2f\n", totalCostTermica);
            System.out.printf("CUSTO GERAL TOTAL: R$ %.2f\n", totalGeral);
            

        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo Excel: " + e.getMessage());
        }

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