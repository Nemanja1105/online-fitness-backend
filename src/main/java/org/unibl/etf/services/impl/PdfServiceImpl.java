package org.unibl.etf.services.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.unibl.etf.exceptions.UnauthorizedException;
import org.unibl.etf.models.dto.JwtUserDTO;
import org.unibl.etf.models.dto.PdfDTO;
import org.unibl.etf.repositories.ActivityRepository;
import org.unibl.etf.repositories.BodyWeightRepository;
import org.unibl.etf.services.PdfService;


import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Transactional
public class PdfServiceImpl implements PdfService {

    private final ActivityRepository activityRepository;
    private final BodyWeightRepository bodyWeightRepository;

    public PdfServiceImpl(ActivityRepository activityRepository, BodyWeightRepository bodyWeightRepository) {
        this.activityRepository = activityRepository;
        this.bodyWeightRepository = bodyWeightRepository;
    }


    @Override
    public PdfDTO generatePdfForClient(Long clientId) {

        var activities = this.activityRepository.findAllByClientIdAndStatus(clientId, true);
        var bodyweights = this.bodyWeightRepository.findAllByClientIdOrderByCreatedAtAsc(clientId);
        try {
            Document document = new Document(PageSize.A4);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, byteArrayOutputStream);
            document.open();
            Paragraph titleParagraph = new Paragraph("Client activity report", FontFactory.getFont(FontFactory.TIMES_BOLD, 18, BaseColor.BLACK));
            titleParagraph.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(titleParagraph);

            Font font = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
            Font headerFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD, BaseColor.WHITE);
            document.add(new Paragraph("\n"));
            Paragraph activityPar = new Paragraph("Client activity:", FontFactory.getFont(FontFactory.TIMES_BOLD, 14, BaseColor.BLACK));
            document.add(activityPar);
            document.add(new Paragraph("\n"));
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            // Add header cells
            addHeaderCell(table, "ID", headerFont);
            addHeaderCell(table, "Name", headerFont);
            addHeaderCell(table, "Weight[kg]", headerFont);
            addHeaderCell(table, "Sets", headerFont);
            addHeaderCell(table, "Reps", headerFont);

            for (var el : activities) {
                addDataCell(table, el.getId().toString(), font);
                addDataCell(table, el.getName(), font);
                addDataCell(table, el.getWeight().toString(), font);
                addDataCell(table, el.getSets().toString(), font);
                addDataCell(table, el.getReps().toString(), font);
            }
            document.add(table);
            document.add(new Paragraph("\n"));

            Paragraph bodyweigthPar = new Paragraph("Client bodyweight history:", FontFactory.getFont(FontFactory.TIMES_BOLD, 14, BaseColor.BLACK));
            document.add(bodyweigthPar);
            document.add(new Paragraph("\n"));
            PdfPTable table1 = new PdfPTable(3);
            table.setWidthPercentage(100);
            // Add header cells
            addHeaderCell(table1, "ID", headerFont);
            addHeaderCell(table1, "Date of measure", headerFont);
            addHeaderCell(table1, "Weight[kg]", headerFont);

            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
            for (var el : bodyweights) {
                addDataCell(table1, el.getId().toString(), font);
                addDataCell(table1, formatter.format(el.getCreatedAt()), font);
                addDataCell(table1, el.getWeight().toString(), font);
            }
            document.add(table1);
            document.add(new Paragraph("\n"));
            var p=new Paragraph("On "+formatter.format(new Date()),FontFactory.getFont(FontFactory.TIMES_BOLD, 14, BaseColor.BLACK));
            p.setAlignment(Element.ALIGN_RIGHT);
            document.add(p);
            document.close();
            writer.flush();
            writer.close();
            return new PdfDTO("Activity_"+clientId, byteArrayOutputStream.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void addHeaderCell(PdfPTable table, String header, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(header, font));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setBackgroundColor(new BaseColor(30, 144, 255));
        cell.setPadding(8); // Dodajte ovde padding vrednost po potrebi
        table.addCell(cell);
    }

    private static void addDataCell(PdfPTable table, String data, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(data, font));
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setPadding(6);
        table.addCell(cell);
    }
}
