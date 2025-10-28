package com.terrideboer.bookbase.models.enums;

public enum ReservationStatus {
    PENDING,
    READY_FOR_PICKUP,
    COLLECTED,
    CANCELLED,
    EXPIRED

//    todo: als 7 dagen verstreken zijn, veranderen naar expired.
//    todo: endpoint om reservering te kunnen cancelen
}
