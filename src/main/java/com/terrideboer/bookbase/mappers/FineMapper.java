package com.terrideboer.bookbase.mappers;

import com.terrideboer.bookbase.dtos.fines.FineDto;
import com.terrideboer.bookbase.dtos.fines.FineInputDto;
import com.terrideboer.bookbase.dtos.fines.FineSummaryDto;
import com.terrideboer.bookbase.models.Fine;

public class FineMapper {

    public static Fine toEntity(FineInputDto fineInputDto, Fine fine) {
        if (fine == null) {
            fine = new Fine();
        }

        if (fineInputDto.fineAmount != null) {
            fine.setFineAmount(fineInputDto.fineAmount);
        }

        if (fineInputDto.paymentStatus != null) {
            fine.setPaymentStatus(fineInputDto.paymentStatus);
        }

        return fine;
    }

    public static FineDto toDto(Fine fine) {
        FineDto fineDto = new FineDto();

        fineDto.id = fine.getId();
        fineDto.fineAmount = fine.getFineAmount();
        fineDto.paymentDate = fine.getPaymentDate();
        fineDto.paymentStatus = fine.getPaymentStatus();

        if (fine.getLoan() != null) {
            fineDto.loanId = fine.getLoan().getId();
        }

        return fineDto;
    }

    public static FineSummaryDto toSummaryDto(Fine fine) {
        FineSummaryDto fineSummaryDto = new FineSummaryDto();

        fineSummaryDto.id = fine.getId();
        fineSummaryDto.fineAmount = fine.getFineAmount();
        fineSummaryDto.paymentStatus = fine.getPaymentStatus();

        return fineSummaryDto;
    }
}
