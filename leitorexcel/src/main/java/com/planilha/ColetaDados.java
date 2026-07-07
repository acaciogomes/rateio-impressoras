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
        Scanner sc = new Scanner(System.in);
        String caminhoArquivo = "C:\\Users\\aigomes\\OneDrive - ACCO Brands Corporation\\Documents\\cod\\java\\Rateio\\202605.xlsx";
        Departamento[] departamento = new Departamento[12];
        Departamento total = new Departamento();
        total.setName("total");
        int franquiaPbCount = 90000;
        double franquiaPbCost = 6768.00;
        char opcao;

        for (int i = 0; i < departamento.length; i++) {
            departamento[i] = new Departamento();
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
                    } 
                    else if (tipoCopia.equals("color") || tipoCopia.equals("colorido")) {
                        departamento[deptoAtual].addContagemCl(contagem);
                    }   
                    else if (tipoCopia.equals("termica")) {
                        departamento[deptoAtual].addContagemTermica(contagem);
                    }
                }
            }

            calcularCustos(departamento, total);
            
            if(total.getContagemPb() < franquiaPbCount)
                calcularResidual(departamento, total, franquiaPbCost);
            
            do {
                limparTela();
                System.out.println("\n===================== MENU =======================");
                System.out.println("[1] Ver Contagem de Cópias por Departamento");
                System.out.println("[2] Ver Faturamento Detalhado por Departamento");
                System.out.println("[3] Ver Cálculo Residual por Departamento");
                System.out.println("[4] Ver Resumo Total a Pagar por Departamento");
                System.out.println("[5] Ver Total Geral da Empresa");
                System.out.println("[Q] Sair");
                System.out.println("====================================================");
                    System.out.print("Escolha uma opção: ");
    
                // Lê a primeira letra digitada e já converte para maiúsculo
                opcao = sc.next().charAt(0);
                sc.nextLine();

                switch (opcao) {
                case '1':
                    System.out.println("\n--- CONTAGEM DE CÓPIAS ---");
                    for (int i = 0; i < departamento.length; i++) {
                        if (departamento[i] != null) {
                            System.out.print(departamento[i].toStringContagem());
                        }
                    }
                break;
            
                case '2':
                    System.out.println("\n--- FATURAMENTO DETALHADO ---");
                    for (int i = 0; i < departamento.length; i++) {
                        if (departamento[i] != null) {
                            System.out.print(departamento[i].toStringFaturamento());
                        }
                    }
                    break;
            
                case '3':
                    System.out.println("\n--- RATEIO RESIDUAL ---");
                    for (int i = 0; i < departamento.length; i++) {
                        if (departamento[i] != null) {
                            System.out.print(departamento[i].toStringResidual());
                        }
                    }
                    break;
            
                case '4':
                    System.out.println("\n--- RESUMO A PAGAR ---");
                    for (int i = 0; i < departamento.length; i++) {
                    if (departamento[i] != null) {
                        System.out.print(departamento[i].toStringTotalPorDpto());
                        }
                    }
                    dividirGastosKensinTilisp(departamento);
                break;
            
                case '5':
                    System.out.println("\n--- TOTAL GERAL ---");
                    if (total != null) {
                    // Imprime o objeto 'total' usando o método específico dele
                        System.out.print(total.toStringFaturamentoTotal());
                    }
                    break;
            
                case 'q':
                case 'Q':
                    System.out.println("\nEncerrando o sistema... Até logo!");
                    break;
            
                default:
                    System.out.println("\nOpção inválida! Tente novamente.");
                    break;
                }

                if (opcao != 'Q' && opcao != 'q') {
                    System.out.println("\n[ Pressione ENTER para voltar ao menu ]");
                    sc.nextLine(); // Fica travado aqui esperando o usuário bater no Enter
                }

            } while (opcao != 'Q' && opcao != 'q');
            
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo Excel: " + e.getMessage());
        }

    }

    static void calcularCustos(Departamento[] departamento, Departamento total){
        for(int i = 0; i < departamento.length; i++){
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
         
        for(int i = 0;i<departamento.length;i++){
                //System.out.println(departamento[i].toStringFaturamento());
                departamento[i].setProporcaoResidual(totalCostPb);
                departamento[i].setValorResidual(franquiaCost - total.getFaturamentoPbTotal());
                departamento[i].setFaturamentoTotalComResidual();

                departamento[i].setFaturamentoTotalDpto();
            }
    }

    static void dividirGastosKensinTilisp(Departamento[] departamento){
        System.out.println("Divisao de custos entre Kensington e Tilisp");
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

    public static void limparTela() {
    try {
        if (System.getProperty("os.name").contains("Windows")) {
            // Se for Windows, manda um comando "cls"
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } else {
            // Se for Linux ou Mac, manda um comando "clear"
            new ProcessBuilder("clear").inheritIO().start().waitFor();
        }
    } catch (Exception e) {
        System.out.println("Erro ao limpar a tela.");
    }
}
}