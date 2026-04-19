package org.tcl.app.slot

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.tcl.app.AvailableSlot
import org.tcl.app.Booking
import org.tcl.app.CourtSlot
import org.tcl.app.booking.BookingRepository
import org.tcl.app.court.CourtRepository
import org.tcl.app.util.plusMinutes

class SlotService(
    private val bookingRepository: BookingRepository,
    private val courtRepository: CourtRepository
) {
    suspend fun getCourtSlots(courtId: Int, date: LocalDate): List<CourtSlot> {
        courtRepository.courtById(courtId)
            ?: throw IllegalArgumentException("Court $courtId not found")

        val bookings = bookingRepository.allBookingsForCourtAndDate(courtId, date)

        return ALL_START_TIMES.map { startTime ->
            val slotEndTime = startTime.plusMinutes(30)

            val taken = bookings.any {
                booking -> booking.overlapsWith(startTime, slotEndTime)
            }

            CourtSlot(
                startTime = startTime.toString(),
                taken = taken
            )
        }
    }

    suspend fun getAvailableSlots(date: LocalDate, duration: Int): List<AvailableSlot> {
        val courts = courtRepository.allCourts()
        val bookings = bookingRepository.allBookingsForDate(date)
        val available = mutableListOf<AvailableSlot>()

        for (slotStart in ALL_START_TIMES) {
            for (court in courts) {
                val requestedEnd = slotStart.plusMinutes(duration)

                if (requestedEnd > END_TIME) continue

                val hasConflict = bookings.any { booking ->
                    if (booking.courtId != court.id) return@any false
                    booking.overlapsWith(slotStart, requestedEnd)
                }

                if (!hasConflict) {
                    available.add(AvailableSlot(
                        startTime = slotStart.toString(),
                        court = court
                    ))
                }
            }
        }

        return available
    }
}

private const val START_HOUR = 8
private const val END_HOUR = 22

val END_TIME = LocalTime(END_HOUR, 0)

fun Booking.overlapsWith(timeSpanStart: LocalTime, timeSpanEnd: LocalTime): Boolean {
    val endTime = startTime.plusMinutes(duration)
    return timeSpanStart < endTime && timeSpanEnd > startTime
}

private val ALL_START_TIMES = (START_HOUR until END_HOUR).flatMap { hour ->
    listOf(LocalTime(hour, 0), LocalTime(hour, 30))
}