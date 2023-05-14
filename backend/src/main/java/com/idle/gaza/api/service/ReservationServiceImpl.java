package com.idle.gaza.api.service;

import com.idle.gaza.api.request.ReservationCreatePostRequest;
import com.idle.gaza.api.response.ReservationResponse;
import com.idle.gaza.api.response.ReviewResponse;
import com.idle.gaza.db.entity.Guide;
import com.idle.gaza.db.entity.Reservation;
import com.idle.gaza.db.entity.Review;
import com.idle.gaza.db.entity.User;
import com.idle.gaza.db.repository.GuideRepository;
import com.idle.gaza.db.repository.ReservationRepository;
import com.idle.gaza.db.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 *  예약 관련 비즈니스 로직 처리를 위한 서비스 구현 정의.
 */
@Service("reservationService")
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    GuideRepository guideRepository;

    @Override
    public Reservation createReservation(ReservationCreatePostRequest reservationInfo) {
        Optional<User> userOptional = userRepository.findById(reservationInfo.getUserId());
        User user = null;
        if(userOptional.isPresent()){
            user = userOptional.get();
        }

        Optional<Guide> guide = guideRepository.findGuideByGuideId(reservationInfo.getGuideId());

        if(!guide.isPresent()) {
            return null;
        }

        Reservation reservation = Reservation.builder()
                .userId(user)
                .guideId(guide.get())
                .consultingDate(reservationInfo.getConsultingDate())
                .reservationDate(LocalDateTime.now())
                .travelStartDate(reservationInfo.getTravelStartDate())
                .travelEndDate(reservationInfo.getTravelEndDate())
                .numberOfPeople(reservationInfo.getNumberOfPeople())
                .withChildren(reservationInfo.getWithChildren())
                .withElderly(reservationInfo.getWithElderly())
                .withDisabled(reservationInfo.getWithDisabled())
                .note(reservationInfo.getNote())
                .stateCode("RE02")
                .build();

        return reservationRepository.save(reservation);
    }

    @Override
    public void cancelReservation(int reservationId) throws Exception {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() ->new NoSuchElementException("Reservation not found."));

        if(reservation.getStateCode() == "RE01"){
            throw new IllegalStateException("완료한 상담은 취소할 수 없습니다.");
        }

        reservationRepository.deleteById(reservationId);
    }

    @Override
    public List<ReservationResponse> getReservationListByGuide(String guideId) {
        List<Reservation> reservations = reservationRepository.findResevationsByGuide(guideId);
        List<ReservationResponse> reservationRes = new ArrayList<>(reservations.size());
        for(int i=0; i<reservations.size(); i++){
            Reservation reservation = reservations.get(i);
            ReservationResponse res = new ReservationResponse(
                    reservation.getGuideId().getGuideId(),
                    reservation.getUserId().getName(),
                    reservation.getGuideId().getPicture(),
                    reservation.getReservationId(),
                    reservation.getGuideId().getUser().getName(),
                    reservation.getGuideId().getUser().getId(),
                    reservation.getNumberOfPeople(),
                    reservation.getWithChildren(),
                    reservation.getWithElderly(),
                    reservation.getWithDisabled(),
                    reservation.getNote(),
                    reservation.getConsultingDate(),
                    reservation.getTravelStartDate(),
                    reservation.getTravelEndDate(),
                    reservation.getStateCode(),
                    reservation.getSessionId());
            reservationRes.add(res);
        }
        return reservationRes;
    }

    @Override
    public List<ReservationResponse> getReservationListByUser(String userId) {
        List<Reservation> reservations = reservationRepository.findResevationsByUser(userId);
        List<ReservationResponse> reservationRes = new ArrayList<>(reservations.size());
        for(int i=0; i<reservations.size(); i++){
            Reservation reservation = reservations.get(i);
            ReservationResponse res = new ReservationResponse(
                    reservation.getGuideId().getGuideId(),
                    reservation.getUserId().getName(),
                    reservation.getGuideId().getPicture(),
                    reservation.getReservationId(),
                    reservation.getGuideId().getUser().getName(),
                    reservation.getGuideId().getUser().getId(),
                    reservation.getNumberOfPeople(),
                    reservation.getWithChildren(),
                    reservation.getWithElderly(),
                    reservation.getWithDisabled(),
                    reservation.getNote(),
                    reservation.getConsultingDate(),
                    reservation.getTravelStartDate(),
                    reservation.getTravelEndDate(),
                    reservation.getStateCode(),
                    reservation.getSessionId());
            reservationRes.add(res);
        }
        return reservationRes;
    }

    @Override
    public List<Integer> getImpossibleTime(int guideId, Date selectedDate) {
        List<Timestamp> timestamps = reservationRepository.getImpossibleTime(guideId, selectedDate);
        List<Integer> times = new ArrayList<>(timestamps.size());
        for(int i=0; i<timestamps.size(); i++){
            Timestamp time = timestamps.get(i);
            Integer res = Integer.parseInt(time.toString().substring(11, 13));
            times.add(res);
        }
        return times;
    }

    @Override
    public void createConsulting(int reservationId, String sessionId) {
        Optional<Reservation> oReservation = reservationRepository.findById(reservationId);
        if(oReservation.isPresent()) {
            Reservation reservation = oReservation.get();
            reservation.setSessionId(sessionId);
            reservation.setStateCode("RE03");
            reservationRepository.save(reservation);
        }
    }

    @Override
    public void endConsulting(int reservationId) {
        Optional<Reservation> oReservation = reservationRepository.findById(reservationId);
        if(oReservation.isPresent()) {
            Reservation reservation = oReservation.get();
            reservation.setStateCode("RE01");
            reservationRepository.save(reservation);
        }
    }

    @Override
    public void changeReservationState(int reservationId, String status) {
        Optional<Reservation> oReservation = reservationRepository.findById(reservationId);
        if(oReservation.isPresent()) {
            Reservation reservation = oReservation.get();
            reservation.setStateCode(status);
            reservationRepository.save(reservation);
        }
    }

    @Override
    public String getConsulting(int reservationId) {
        String sessionId = null;
        Optional<Reservation> oReservation = reservationRepository.findById(reservationId);
        if(oReservation.isPresent()) {
            Reservation reservation = oReservation.get();
            sessionId = reservation.getSessionId();
        }
        return sessionId;
    }

}
