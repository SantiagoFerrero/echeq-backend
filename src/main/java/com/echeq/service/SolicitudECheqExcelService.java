package com.echeq.service;

import com.echeq.dto.response.solicitudEcheq.SolicitudECheqResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class SolicitudECheqExcelService {

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public byte[] generar(
            List<SolicitudECheqResponse> solicitudes) {

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream output =
                     new ByteArrayOutputStream()) {

            Sheet sheet =
                    workbook.createSheet("Solicitudes eCheq");

            sheet.createFreezePane(0, 1);

            CellStyle encabezadoStyle =
                    workbook.createCellStyle();

            Font encabezadoFont =
                    workbook.createFont();

            encabezadoFont.setBold(true);
            encabezadoStyle.setFont(encabezadoFont);

            CellStyle montoStyle =
                    workbook.createCellStyle();

            montoStyle.setDataFormat(
                    workbook.createDataFormat()
                            .getFormat("#,##0.00")
            );

            String[] encabezados = {
                    "ID",
                    "Fecha",
                    "Cliente",
                    "Banco",
                    "CBU",
                    "Alias",
                    "Cuenta Corriente",
                    "Monto",
                    "Concepto",
                    "Estado"
            };

            Row filaEncabezado =
                    sheet.createRow(0);

            for (int i = 0; i < encabezados.length; i++) {
                Cell cell = filaEncabezado.createCell(i);
                cell.setCellValue(encabezados[i]);
                cell.setCellStyle(encabezadoStyle);
            }

            int filaNumero = 1;

            for (SolicitudECheqResponse solicitud : solicitudes) {

                Row fila =
                        sheet.createRow(filaNumero++);

                if (solicitud.getId() != null) {
                    fila.createCell(0)
                            .setCellValue(solicitud.getId());
                }

                if (solicitud.getFechaSolicitud() != null) {
                    fila.createCell(1).setCellValue(
                            solicitud.getFechaSolicitud()
                                    .format(FORMATO_FECHA)
                    );
                }

                escribirTexto(
                        fila, 2, solicitud.getUsuarioNombre()
                );

                escribirTexto(
                        fila, 3, solicitud.getBancoNombre()
                );

                escribirTexto(
                        fila, 4, solicitud.getCbu()
                );

                escribirTexto(
                        fila, 5, solicitud.getCuentaCorrienteAlias()
                );

                escribirTexto(
                        fila, 6, solicitud.getCuentaCorrienteNumero()
                );

                if (solicitud.getMonto() != null) {
                    Cell montoCell = fila.createCell(7);
                    montoCell.setCellValue(solicitud.getMonto());
                    montoCell.setCellStyle(montoStyle);
                }

                escribirTexto(
                        fila, 8, solicitud.getConcepto()
                );

                escribirTexto(
                        fila,
                        9,
                        solicitud.getEstado() != null
                                ? solicitud.getEstado().name()
                                : null
                );
            }

            configurarAnchos(sheet);

            workbook.write(output);

            return output.toByteArray();

        } catch (IOException ex) {
            throw new IllegalStateException(
                    "No se pudo generar el archivo Excel",
                    ex
            );
        }
    }

    private void escribirTexto(
            Row fila,
            int columna,
            String valor) {

        if (valor != null) {
            fila.createCell(columna)
                    .setCellValue(valor);
        }
    }

    private void configurarAnchos(Sheet sheet) {
        sheet.setColumnWidth(0, 10 * 256);
        sheet.setColumnWidth(1, 20 * 256);
        sheet.setColumnWidth(2, 30 * 256);
        sheet.setColumnWidth(3, 28 * 256);
        sheet.setColumnWidth(4, 26 * 256);
        sheet.setColumnWidth(5, 28 * 256);
        sheet.setColumnWidth(6, 24 * 256);
        sheet.setColumnWidth(7, 18 * 256);
        sheet.setColumnWidth(8, 45 * 256);
        sheet.setColumnWidth(9, 18 * 256);
    }
}
