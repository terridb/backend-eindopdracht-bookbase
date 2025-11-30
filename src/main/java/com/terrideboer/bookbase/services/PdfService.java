package com.terrideboer.bookbase.services;

import com.terrideboer.bookbase.dtos.reservations.ReservationDto;
import com.terrideboer.bookbase.exceptions.PdfGenerationException;
import com.terrideboer.bookbase.utils.DateUtils;
import org.openpdf.text.*;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;

@Service
public class PdfService {
    public byte[] generateReservationsPdf(List<ReservationDto> reservations) {
        try {
            Document document = new Document(PageSize.A4.rotate());
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            PdfWriter.getInstance(document, out);

            Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            fontTitle.setSize(18);

            HeaderFooter footer = new HeaderFooter(new Phrase("Page "), true);
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setBorderWidthBottom(0);
            document.setFooter(footer);

            document.open();

            Paragraph title = new Paragraph("Reservations to prepare on " + DateUtils.formatDate(LocalDate.now()), fontTitle);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.addCell("Reservation ID");
            table.addCell("Book title");
            table.addCell("Authors");
            table.addCell("Tracking nr.");
            table.addCell("Member");
            table.addCell("Reservation Date");

            for (ReservationDto reservation : reservations) {
                table.addCell(String.valueOf(reservation.id));
                table.addCell(reservation.bookCopy.book.title);
                table.addCell(reservation.bookCopy.book.authors
                        .stream()
                        .map(Author -> Author.displayName)
                        .reduce((a, b) -> a + ", " + b)
                        .orElse("-"));
                table.addCell(reservation.bookCopy.trackingNumber);
                table.addCell(reservation.user.firstName + " " + reservation.user.lastName);
                table.addCell(DateUtils.formatDate(reservation.reservationDate));
            }

            document.add(table);
            document.close();

            return out.toByteArray();
        } catch (Exception e) {
            throw new PdfGenerationException("Failed to generate PDF", e);
        }
    }
}
