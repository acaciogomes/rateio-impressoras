package com.planilha;

import com.entitie.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.Scanner;

public class Rateio {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        limparTela();
        int anoAtual = LocalDate.now().getYear();
        int mesSelecionado = selecionarMes(sc);

        String userHome = System.getProperty("user.home");
        
        String caminhoArquivo = String.format("%s\\OneDrive - ACCO Brands Corporation\\Documents\\cod\\java\\Rateio\\%d%02d.xlsx",userHome, anoAtual, mesSelecionado);
        
        Departamento[] departamento = new Departamento[12];
        Departamento total = new Departamento();
        total.setName("total");
        double franquiaPbCost = 6768.00;
        char opcao;

        for (int i = 0; i < departamento.length; i++) {
            departamento[i] = new Departamento();
        }

        try (FileInputStream file = new FileInputStream(new File(caminhoArquivo));
             Workbook workbook = new XSSFWorkbook(file)) {
             
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();

            System.out.println("\nProcessando contagem por departamento...\n");    
            
            for(int i = 93; i <= 138; i++){
                Row row = sheet.getRow(i);
                if (row == null) continue; 

                Cell cellDpt = row.getCell(2);
                Cell cellType = row.getCell(9);
                Cell cellCount = row.getCell(12);

                String departamentoCru = formatter.formatCellValue(cellDpt);
                String tipoCopiaCru = formatter.formatCellValue(cellType);
                
                String auxDepartamento = removerAcentos(departamentoCru).trim().toLowerCase();
                String tipoCopia = removerAcentos(tipoCopiaCru).trim().toLowerCase();

                int contagem = 0;
                if (cellCount != null) {
                    contagem = (int) cellCount.getNumericCellValue();
                }

                int deptoAtual = verificarDepartamento(auxDepartamento);

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
            calcularResidual(departamento, total, franquiaPbCost);
            
            do {
                limparTela();
                System.out.println("\n===================== MENU =======================");
                System.out.println("[1] Ver Contagem de Cópias por Departamento");
                System.out.println("[2] Ver Faturamento Detalhado por Departamento");
                System.out.println("[3] Ver Cálculo Residual por Departamento");
                System.out.println("[4] Ver Resumo Total a Pagar por Departamento");
                System.out.println("[5] Ver Total de consumo da Empresa");
                System.out.println("[Q] Sair");
                System.out.println("====================================================");
                System.out.print("Escolha uma opção: ");
    
                opcao = sc.next().charAt(0);
                sc.nextLine(); // Limpa o enter fantasma

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
                    sc.nextLine(); 
                }

            } while (opcao != 'Q' && opcao != 'q');
            
            prencherConsumoGeral(departamento, formatter, mesSelecionado, sc);

            sc.close();
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo Excel: " + e.getMessage());
        }
    }

    static int selecionarMes(Scanner sc) {
        int mes = -1;
        while (mes < 1 || mes > 12) {
            System.out.println("========== SELEÇÃO DE MÊS ==========");
            System.out.println("[1] Janeiro   [2] Fevereiro  [3] Março");
            System.out.println("[4] Abril     [5] Maio       [6] Junho");
            System.out.println("[7] Julho     [8] Agosto     [9] Setembro");
            System.out.println("[10] Outubro  [11] Novembro  [12] Dezembro");
            System.out.print("\nDigite o número do mês que deseja processar (1-12): ");

            if (sc.hasNextInt()) {
                mes = sc.nextInt();
                if (mes < 1 || mes > 12) {
                    System.out.println("\n[ERRO] Mês inválido! Digite um número de 1 a 12.\n");
                }
            } else {
                System.out.println("\n[ERRO] Entrada inválida! Digite apenas números.\n");
                sc.next();
            }
        }
        sc.nextLine();
        return mes;
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
        for(int i = 0; i < departamento.length; i++) {
            departamento[i].setFaturamentoTotalDpto();
        }
    }
    
    static void calcularResidual(Departamento[] departamento, Departamento total, double franquiaCost){
        double totalCostPb = total.getFaturamentoPbTotal() + total.getFaturamentoTermicaTotal();
         
        for(int i = 0; i < departamento.length; i++){
            departamento[i].setProporcaoResidual(totalCostPb);
            departamento[i].setValorResidual(franquiaCost - total.getFaturamentoPbTotal());
            departamento[i].setFaturamentoTotalComResidual();
            departamento[i].setFaturamentoTotalDpto();
        }
    }

    static void dividirGastosKensinTilisp(Departamento[] departamento){
        System.out.println("Divisao de custos entre Kensington e Tilisp");
        System.out.printf("Kensington: %.2f\n", departamento[11].getFaturamentoTotalDpto()*0.7);
        System.out.printf("Tilisp: %.2f\n", departamento[11].getFaturamentoTotalDpto()*0.3);
    }

    public static String removerAcentos(String texto) {
        if (texto == null) {
            return "";
        }
        String textoNormalizado = Normalizer.normalize(texto, Normalizer.Form.NFD);
        return textoNormalizado.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
    }

    static void prencherConsumoGeral(Departamento[] departamento, DataFormatter formatter, int mes, Scanner sc){

        String userHome = System.getProperty("user.home");

        String caminhoArquivo = userHome +  "\\OneDrive - ACCO Brands Corporation\\Documents\\cod\\java\\Rateio\\ConsumoGeralcopia.xlsx";
        
        try {
            Workbook workbook;
            try (FileInputStream fileIn = new FileInputStream(new File(caminhoArquivo))) {
                workbook = new XSSFWorkbook(fileIn);
            }

            Sheet sheet = workbook.getSheetAt(0);

            if (verificarSeMesJaPreenchido(sheet, mes, formatter)) {
                System.out.println("\n[ATENÇÃO] Já existem dados cadastrados para este mês na planilha Consumo Geral!");
                System.out.print("Deseja sobrescrever os dados existentes? (S para Sim / N para Não): ");
                
                char resposta = sc.next().toUpperCase().charAt(0);
                sc.nextLine();

                if (resposta != 'S') {
                    System.out.println("\n[CANCELADO] Os dados existentes na planilha geral foram mantidos intactos.");
                    workbook.close();
                    return;
                }
                System.out.println("\n[OK] Sobrescrevendo dados antigos...");
            }

            for(int i = 2; i <= 41; i += 2){
                Row linha = sheet.getRow(i);
                if (linha == null) continue;

                Cell cellDpt = linha.getCell(0);
                if (cellDpt == null) continue;

                String texto = formatter.formatCellValue(cellDpt).trim().toLowerCase();
                int numDpto = verificarDepartamento(texto);
                
                if (numDpto == -1) continue;

                if (texto.contains("p&b")) {
                    Cell celula = linha.getCell(mes);
                    if (celula == null) {
                        celula = linha.createCell(mes);   
                    }
                    celula.setCellValue(departamento[numDpto].getContagemPb());
                    
                    Row auxLine = sheet.getRow(i+1);
                    if (auxLine == null) auxLine = sheet.createRow(i+1);
                    
                    Cell auxCell = auxLine.getCell(mes);
                    if (auxCell == null) {
                        auxCell = auxLine.createCell(mes); // CORRIGIDO: auxLine em vez de linha
                    }
                    auxCell.setCellValue(departamento[numDpto].getFaturamentoPb());
                }
                
                if (texto.contains("color")) {
                    Cell celula = linha.getCell(mes);
                    if (celula == null) {
                        celula = linha.createCell(mes);   
                    }
                    celula.setCellValue(departamento[numDpto].getContagemCl());
                    
                    Row auxLine = sheet.getRow(i+1);
                    if (auxLine == null) auxLine = sheet.createRow(i+1);
                    
                    Cell auxCell = auxLine.getCell(mes);
                    if (auxCell == null) {
                        auxCell = auxLine.createCell(mes); 
                    }
                    auxCell.setCellValue(departamento[numDpto].getFaturamentoCl());
                }
            }
            
            // Grava os dados salvando por cima
            try (FileOutputStream fileOut = new FileOutputStream(caminhoArquivo)) {
                workbook.write(fileOut);
                System.out.println("\n[SUCESSO] Planilha Consumo Geral atualizada com sucesso!");
            }
            
            workbook.close(); 
            
        } catch (IOException e) {
            System.err.println("Erro ao manipular o arquivo Excel Geral: " + e.getMessage());
        }
    }

    static boolean verificarSeMesJaPreenchido(Sheet sheet, int mes, DataFormatter formatter) {
        for (int i = 2; i <= 41; i++) {
            Row linha = sheet.getRow(i);
            if (linha != null) {
                Cell celula = linha.getCell(mes);
                if (celula != null) {
                    String valor = formatter.formatCellValue(celula).trim();
                    // Se a célula tiver qualquer valor diferente de vazio ou zero, consideramos preenchida
                    if (!valor.isEmpty() && !valor.equals("0") && !valor.equals("0,00") && !valor.equals("-")) {
                        return true; // Encontrou dados!
                    }
                }
            }
        }
        return false; // Coluna está limpa/vazia
    }

    static int verificarDepartamento(String texto){
        if (texto.contains("apoio")) return 0;
        if (texto.contains("cedis")) return 1;
        if (texto.contains("rh")) return 2;
        if (texto.contains("vendas")) return 3;
        if (texto.contains("pcpalm")) return 4;
        if (texto.contains("diradm")) return 5;
        if (texto.contains("financ")) return 6;
        if (texto.contains("infra")) return 7;
        if (texto.contains("market")) return 8;
        if (texto.contains("qualid")) return 9;
        if (texto.contains("unid1")) return 10;
        if (texto.contains("tilisp")) return 11;
        return -1;
    }

    public static void limparTela() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            System.out.println("Erro ao limpar a tela.");
        }
    }
}